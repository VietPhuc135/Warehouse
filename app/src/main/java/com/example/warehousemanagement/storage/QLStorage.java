package com.example.warehousemanagement.storage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.Api;
import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.MapsActivity;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.obj.Storage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QLStorage extends AppCompatActivity {
    private List<Storage> itemList;
    private List<Storage> mapList = new ArrayList<>();
    private ListView storageList;
//    private ArrayAdapter<String> adapter;
    private ArrayList<String> storageNames;
    private ArrayStorage adapter;
    String role;
    TextView title ;
    String header, responseC ;
    MapsActivity map ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_manager);
        header = DangNhap.account.getToken();
        storageList = findViewById(R.id.lvStorage);
        role = DangNhap.account.getRole();
        storageNames = new ArrayList<>();
        adapter = new ArrayStorage(this, itemList);
        ImageView imgArrageStorage = findViewById(R.id.imgAddProduct);
        imgArrageStorage.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(QLStorage.this, AddStorage.class);
                   startActivity(intent);
               }
           }
        );

//<<<<<<< HEAD
        ImageView imgAdd = findViewById(R.id.imgAddProduct);
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QLStorage.this, MapsActivity.class);
                intent.putExtra("item",responseC);
                startActivity(intent);
//=======
//        ImageView imgMap = findViewById(R.id.imgArrageMap);
//        imgMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent1 = new Intent(QLStorage.this, MapsActivity.class);
//                startActivity(intent1);
//>>>>>>> main
            }
        });

        if (role.equals("SALER")){
            title = findViewById(R.id.titleStorage);
            title.setText("Kho ");
        }

        registerForContextMenu(storageList);

        new MyAsyncTask().execute();
    }
    private class MyAsyncTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("text/plain");
//            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url(Api.baseURL + "/storage/getList")
                    .method("GET",null)
                    .addHeader("Authorization", "Bearer " + header)
                    .build();

            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Storage>>() {
                        }.getType();

                        itemList = gson.fromJson(responseBody, listType);
                        responseC = responseBody;

                        System.out.println("Đây la itemlisst " + responseBody);
                        // Cập nhật giao diện trong luồng UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Khởi tạo và thiết lập Adapter
                                adapter = new ArrayStorage(QLStorage.this, itemList);
                                storageList.setAdapter(adapter);
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
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_storage, menu);
    }

    // Xử lý sự kiện khi mục của menu context được chọn
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Lấy thông tin về phần tử trong ListView được chọn
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;

        switch (item.getItemId()) {
            case R.id.detail_storage:
                Intent intent1 = new Intent(QLStorage.this, EditStorage.class);
                startActivity(intent1);
                return true;
            case R.id.delete_storage:
                // Xóa phần tử được chọn khỏi ListView
                adapter.remove(adapter.getItem(position));
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}