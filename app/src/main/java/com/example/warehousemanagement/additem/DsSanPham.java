package com.example.warehousemanagement.additem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.widget.PopupMenu;

import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.obj.Product;
import com.example.warehousemanagement.order.AddOrder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class DsSanPham extends AppCompatActivity {
    String header;
    private ListView listView;
    private ArrayProduct adapter;
    //    private ArrayAdapter<Product> adapter;
    private List<Product> itemList;
    ImageView imgAddProduct;
    Context context;
        String id;
        String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        imgAddProduct = findViewById(R.id.imgAddProduct);
        listView = findViewById(R.id.lvProduct);
        header = DangNhap.account.getToken();
        role = DangNhap.account.getUser().getRole();
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id");

        }

        adapter = new ArrayProduct(this, itemList);

        if (role.equals("saler")){
            imgAddProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DsSanPham.this, AddOrder.class);
                    startActivity(intent);
                }
            });
        }else{
            imgAddProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DsSanPham.this, AddProduct.class);
                    startActivity(intent);
                }
            });
        }

        listView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(context, v);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_product, popupMenu.getMenu());
                    popupMenu.show();

                    // Xử lý các sự kiện khi người dùng chọn một item trong menu
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.detail_product:
                                    // Xử lý khi người dùng chọn Delete
                                    return true;

                                case R.id.delete_product:
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
                    .url("http://14.225.211.190:4001/api/product/query")
                    .method("POST", body)
                    .addHeader("Authorization", "Bearer " + header)
                    .build();
            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Product>>() {
                        }.getType();
                        itemList = gson.fromJson(responseBody, listType);
                        System.out.println("Đây la itemlist " + responseBody);

                        List<Product> filteredList = new ArrayList<>();
                        if (id != null){
                            for (Product product : itemList) {
                                String idSto = product.getstorageId();
                                if (product.getstorageId().equals(id)) {
                                    filteredList.add(product);
                                }
                            }
                        }
                        System.out.println("Đây la itemlisst " + responseBody);
                        // Cập nhật giao diện trong luồng UI


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Khởi tạo và thiết lập Adapter
                                if (id != null){
                                    adapter = new ArrayProduct(DsSanPham.this, filteredList);
                                    listView.setAdapter(adapter);
                                }
                              else{
                                    adapter = new ArrayProduct(DsSanPham.this, itemList);
                                    listView.setAdapter(adapter);
                                }
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

}