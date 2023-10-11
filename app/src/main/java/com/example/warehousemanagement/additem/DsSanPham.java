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
    ImageView imgAddProduct,imaArrange;
    Context context;
        String id;
        String role,dsSanPham;
        String storageId;
    RequestBody body,body1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        imgAddProduct = findViewById(R.id.imgAddProduct);
        imaArrange = findViewById(R.id.imgArrageProduct);
        listView = findViewById(R.id.lvProduct);
        header = DangNhap.account.getToken();
        role = DangNhap.account.getRole();
        storageId = DangNhap.account.getStorageId();
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id");
        }
        adapter = new ArrayProduct(this, itemList);
        if (role.equals("STOCKER")){
            storageId = intent.getStringExtra("idSto");
        }
        if (role.equals("SALER")){
            imgAddProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DsSanPham.this, AddOrder.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            });
            imaArrange.setVisibility(View.GONE);
        }else{
            imgAddProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DsSanPham.this, AddProduct.class);
                    startActivity(intent);
                }
            });
        }

        new MyAsyncTask().execute();
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
//            String requestBody = "{\n" +
//                    "    \"filter\":{\n" +
//                    "        \"storageId\":{\n" +
//                    "            \"eq\":" + storageId + "\n" +
//                    "        }\n" +
//                    "    }\n" +
//                    "}";
//            body = RequestBody.create(mediaType,requestBody);
//            String requestBody1 = "{\n" +
//                    "    \"filter\":{\n" +
//                    "        \"marketId\":{\n" +
//                    "            \"eq\": " + id + "\n" +
//                    "        }\n" +
//                    "    }\n" +
//                    "}";
//            body1 = RequestBody.create(mediaType,requestBody1);
//            if(role.equals("STOCKER"))
//            {
//                int stoID = Integer.parseInt(storageId);
////                String requestBody = "{\n" +
////                        "    \"filter\":{\n" +
////                        "        \"storageId\":{\n" +
////                        "            \"eq\": " + stoID + "\n" +
////                        "        }\n" +
////                        "    }\n" +
////                        "}";
//                body = RequestBody.create(mediaType,requestBody);
//                         System.out.println( "stocker" + body);
//            }
//            else
//                if (role.equals("SALER")){
//                if (id != null){
////                    String requestBody = "{\n" +
////                            "    \"filter\":{\n" +
////                            "        \"marketId\":{\n" +
////                            "            \"eq\": " + id + "\n" +
////                            "        }\n" +
////                            "    }\n" +
////                            "}";
//                    body = RequestBody.create(mediaType,requestBody);
//                    System.out.println( "stocker" + body);     System.out.println("else" + body);
//                }
//
//            }
//                else {
//                    body = RequestBody.create(mediaType," ");
//                }
             Request request = new Request.Builder()
                    .url("http://192.168.1.81:8080/product/getlist")
                    .method("GET",null)
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

//                        List<Product> filteredList = new ArrayList<>();
//                        if (id != null){
//                            for (Product product : itemList) {
//                                String idSto = product.getStorageId();
//                                if (product.getStorageId().equals(id)) {
//                                    filteredList.add(product);
//                                }
//                            }
//                        }
                        // Cập nhật giao diện trong luồng UI


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Khởi tạo và thiết lập Adapter
                                if (id != null){
                                    adapter = new ArrayProduct(DsSanPham.this, itemList);
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