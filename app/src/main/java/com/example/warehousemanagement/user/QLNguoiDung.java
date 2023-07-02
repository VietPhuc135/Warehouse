package com.example.warehousemanagement.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.additem.AddProduct;
import com.example.warehousemanagement.additem.ArrayProduct;
import com.example.warehousemanagement.additem.DsSanPham;
import com.example.warehousemanagement.additem.ProductAdapter;
import com.example.warehousemanagement.obj.Product;
import com.example.warehousemanagement.obj.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class QLNguoiDung extends AppCompatActivity {

//    private ListView userList;
//    private ArrayAdapter<String> adapter;
//    private ArrayList<String> userNames;
//    String header ;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user_manager);
//
//        userList = findViewById(R.id.lvUser);
//        userNames = new ArrayList<>();
//        adapter = new ArrayAdapter<>(this, R.layout.activity_list_user, userNames);
//        userList.setAdapter(adapter);
//        header = DangNhap.account.getToken();
//        ImageView imgArrageUser = findViewById(R.id.imgArrageUser);
//        imgArrageUser.setOnClickListener(new View.OnClickListener() {
//               @Override
//               public void onClick(View v) {
//                   Intent intent = new Intent(QLNguoiDung.this, AddNguoiDung.class);
//                   startActivity(intent);
//               }
//           }
//        );
//        // Gọi phương thức để thực hiện yêu cầu HTTP và hiển thị danh sách
//        fetchMarketList();
//
//        // Đăng ký menu context cho ListView, khi người dùng nhấn giữ trên một phần tử trong ListView
//        registerForContextMenu(userList);
//    }
//
//    private void fetchMarketList() {
//        OkHttpClient client = new OkHttpClient();
//        MediaType mediaType = MediaType.parse("text/plain");
//        RequestBody body = RequestBody.create(mediaType, "");
//        Request request = new Request.Builder()
//                .url("http://14.225.211.190:4001/api/user/query")
//                .method("POST", body)
//                .addHeader("Authorization", "Bearer " + header)
//                .build();
//
//        client.newCall(request).enqueue(new okhttp3.Callback() {
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                int statusCode = response.code();
//                if (statusCode == 201) {
//                    String jsonData = response.body().string();
//                    System.out.println("thành công");
//                    Log.d("Response", jsonData);
//                    System.out.println(jsonData);
//                    try {
//                        JSONArray jsonArray = new JSONArray(jsonData);
//
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            String address = jsonObject.getString("address");
//                            String name = jsonObject.getString("name");
//                            String userInfo = "Name: " + name + "\nAddress: " + address;
//                            userNames.add(userInfo);
//
//                        }
//
//                        // Cập nhật giao diện trong luồng UI
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                adapter.notifyDataSetChanged();
//                            }
//                        });
//
//                    } catch (JSONException e) {
//                        System.out.println("khong thanh cong");
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//                System.out.println("khong thanh cong");
//                e.printStackTrace();
//            }
//        });
//    }
//
//    // Tạo menu context cho mỗi phần tử trong ListView
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_user, menu);
//    }
//
//    // Xử lý sự kiện khi mục của menu context được chọn
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        // Lấy thông tin về phần tử trong ListView được chọn
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        int position = info.position;
//
//        switch (item.getItemId()) {
//            case R.id.add_user:
//                Intent intent = new Intent(QLNguoiDung.this, AddNguoiDung.class);
//                startActivity(intent);
//                return true;
//            case R.id.edit_user:
//                Intent intent1 = new Intent(QLNguoiDung.this, EditNguoiDung.class);
//                startActivity(intent1);
//                return true;
//            case R.id.delete_user:
//                // Xóa phần tử được chọn khỏi ListView
//                adapter.remove(adapter.getItem(position));
//                //nguoiDungAdapter.notifyItemRemoved((int) nguoiDungAdapter.getItemId(position));
//                adapter.notifyDataSetChanged();
//                return true;
//            default:
//                return super.onContextItemSelected(item);
//        }
//    }
    String header;
    private ListView listView;
    private ArrayNguoiDung adapter;
    //    private ArrayAdapter<Product> adapter;
    private List<User> itemList;
    ImageView imgAddUser;
    Context context;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manager);
        imgAddUser = findViewById(R.id.imgAddUser);
        listView = findViewById(R.id.lvUser);
        header = DangNhap.account.getToken();
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id");

        }
        adapter = new ArrayNguoiDung(this, itemList);
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
                            case R.id.edit_user:
                                // Xử lý khi người dùng chọn Edit
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
    private class MyAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("http://14.225.211.190:4001/api/user/query")
                    .method("POST", body)
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

//                        List<User> filteredList = new ArrayList<>();
//                        if (id != null){
//                            for (User user : itemList) {
//                                String idSto = user.getId();
//                                if (user.getId().equals(id)) {
//                                    filteredList.add(user);
//                                }
//                            }
//                        }
//                        System.out.println("Đây la itemlist " + responseBody);
                        // Cập nhật giao diện trong luồng UI

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Khởi tạo và thiết lập Adapter
                                adapter = new ArrayNguoiDung(QLNguoiDung.this, itemList);
                                listView.setAdapter(adapter);
//                                if (id != null){
//                                    adapter = new ArrayNguoiDung(QLNguoiDung.this, filteredList);
//                                    listView.setAdapter(adapter);
//                                }
//                                else{
//                                    adapter = new ArrayNguoiDung(QLNguoiDung.this, itemList);
//                                    listView.setAdapter(adapter);
//                                }
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
//                System.out.println(jsonObject);
//                Intent intent = new Intent(AddStorage.this, QLStorage.class);
//                startActivity(intent);
            } else {
                System.out.println("lỗi ");
            }
        }
    }

    // Tạo menu context cho mỗi phần tử trong ListView
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_user, menu);
//    }
//
//    // Xử lý sự kiện khi mục của menu context được chọn
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        // Lấy thông tin về phần tử trong ListView được chọn
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        int position = info.position;
//        //User item1 = itemList.get(position);
//
//        switch (item.getItemId()) {
//            case R.id.detail_user:
//                Intent intent = new Intent(QLNguoiDung.this, EditNguoiDung.class);
////                intent.putExtra("id", item1.getId());
////                context.startActivity(intent);// Pass the product ID to the EditProduct activity
//                startActivity(intent);
//                return true;
//            case R.id.edit_user:
////                Intent intent1 = new Intent(QLNguoiDung.this, EditNguoiDung.class);
////                startActivity(intent1);
//                return true;
//            case R.id.delete_user:
//                // Xóa phần tử được chọn khỏi ListView
//                adapter.remove(adapter.getItem(position));
//                //nguoiDungAdapter.notifyItemRemoved((int) nguoiDungAdapter.getItemId(position));
//                adapter.notifyDataSetChanged();
//                return true;
//            default:
//                return super.onContextItemSelected(item);
//        }
//    }
}