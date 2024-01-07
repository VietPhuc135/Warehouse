package com.example.warehousemanagement.user;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.Api;
import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.obj.Storage;
import com.example.warehousemanagement.obj.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class QLNguoiDung extends AppCompatActivity {

    String header;
    private ListView listView;
    private ArrayNguoiDung adapter;
    //    private ArrayAdapter<Product> adapter;
    private List<User> itemList;
    ImageView imgAddUser, imgArrUser;
    Spinner sortSpinner;
    Context context;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manager);
        imgAddUser = findViewById(R.id.imgAddUser);
        imgArrUser = findViewById(R.id.imgArrageUser);
        listView = findViewById(R.id.lvUser);
        sortSpinner = findViewById(R.id.sortSpinner2);
        header = DangNhap.account.getToken();
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id");

        }
        adapter = new ArrayNguoiDung(this, itemList);

        imgArrUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle visibility of the Spinner
                toggleSpinnerVisibility();
            }
        });

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.sort_options2, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(spinnerAdapter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Xử lý sự kiện sắp xếp danh sách khi chọn mục trong Spinner
                String selectedItem = parentView.getItemAtPosition(position).toString();
                if (selectedItem.equals(getString(R.string.sort_by_name))) {
                    // Sắp xếp theo tên sản phẩm
                    Collections.sort(itemList, new Comparator<User>() {
                        @Override
                        public int compare(User p1, User p2) {
                            return p1.getName().compareToIgnoreCase(p2.getName());
                        }
                    });
                } else if (selectedItem.equals(getString(R.string.sort_by_email))) {
                    // Sắp xếp theo số lượng sản phẩm
                    Collections.sort(itemList, new Comparator<User>() {
                        @Override
                        public int compare(User p1, User p2) {
                            // Đổi sang kiểu số và so sánh
                            return p1.getEmail().compareToIgnoreCase(p2.getEmail());
                        }
                    });
                }
                // Cập nhật lại ListView sau khi sắp xếp
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Không có hành động cụ thể khi không chọn mục nào
            }
        });

        imgAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QLNguoiDung.this, AddNguoiDung.class);
                startActivity(intent);
            }
        });
        // Đăng ký menu context cho ListView, khi người dùng nhấn giữ trên một phần tử trong ListView
        registerForContextMenu(listView);

        listView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_user, popupMenu.getMenu());
                popupMenu.show();

                // Xử lý các sự kiện khi người dùng chọn một item trong menu
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.detail_user:
                                // Xử lý khi người dùng chọn Delete
                                Intent intent = new Intent(QLNguoiDung.this, EditNguoiDung.class);
                                startActivity(intent);
                                return true;

                            case R.id.delete_user:
                                // Xử lý khi người dùng chọn Edit
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                return true;
            }
        }
        );
        new MyAsyncTask().execute();
    }

    private void toggleSpinnerVisibility() {
        // Toggle visibility of the Spinner
        int visibility = sortSpinner.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
        sortSpinner.setVisibility(visibility);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        new MyAsyncTask().execute();
    }

    private class MyAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url(Api.baseURL + "/user/getList")
                    .method("GET", null)
                    .addHeader("Authorization", "Bearer " + header)
                    .build();
            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<User>>() {
                        }.getType();

                        itemList = gson.fromJson(responseBody, listType);
                        System.out.println("Đây la itemlist " + responseBody);
                        // Cập nhật giao diện trong luồng UI


                        // Cập nhật giao diện trong luồng UI

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Khởi tạo và thiết lập Adapter
                                adapter = new ArrayNguoiDung(QLNguoiDung.this, itemList);
                                listView.setAdapter(adapter);
                            }
                        });

                    }

                }

                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("fail");

                    e.printStackTrace();
                }
            });
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
            } else {
                System.out.println("lỗi ");
            }
        }
    }
}