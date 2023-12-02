package com.example.warehousemanagement.order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.Api;
import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.additem.AddProduct;
import com.example.warehousemanagement.additem.ArrayProduct;
import com.example.warehousemanagement.additem.DsSanPham;
import com.example.warehousemanagement.obj.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProductPickPage extends AppCompatActivity {
    String header;
    private ListView listView;
    private ProductPickAdapter adapter;
    private List<Product> itemList;
    ImageView imgAddProduct,imaArrange;
    String id;
    String role;
    String storageId;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        imgAddProduct = findViewById(R.id.imgAddProduct);
        imaArrange = findViewById(R.id.imgArrageProduct);
        listView = findViewById(R.id.lvProduct);
        TextView idTitle = findViewById(R.id.idTitle);
        LinearLayout searchBarviewpd = findViewById(R.id.searchBarviewpd);

        Log.d("goitao","1");
      searchBarviewpd.setVisibility(View.GONE);
        idTitle.setText("Danh sách sản phẩm");
        header = DangNhap.account.getToken();
        adapter = new ProductPickAdapter(this, itemList);
        new MyAsyncTask().execute();
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        new MyAsyncTask().execute();
//
//    }


    @Override
    public void onBackPressed() {
       super.onBackPressed();
        Log.d("dat1","2");
        finish();
    }

    private class MyAsyncTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("text/plain");
            Request request = new Request.Builder()
                    .url(Api.baseURL + "/product/getlist")
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Khởi tạo và thiết lập Adapter
//                                if (id != null){
//                                    adapter = new ProductPickAdapter(ProductPickPage.this, itemList);
//                                    adapter.setOnProductClickListener(new ProductPickAdapter.OnProductClickListener() {
//                                        @Override
//                                        public void onProductClick(Product product) {
//                                            // Tạo Intent để chuyển dữ liệu về
//                                            Intent resultIntent = new Intent();
//                                            resultIntent.putExtra("pickedProductId", product);
//
//                                            // Đặt kết quả là RESULT_OK và gửi Intent về cho Activity gọi PickProduct
//                                            setResult(RESULT_OK, resultIntent);
//                                            finish();
//                                        }
//                                    });
//                                    listView.setAdapter(adapter);
//                                }
//                                else{
                                    adapter = new ProductPickAdapter(ProductPickPage.this, itemList);
                                    adapter.setOnProductClickListener(new ProductPickAdapter.OnProductClickListener() {
                                        @Override
                                        public void onProductClick(Product product) {
                                            Intent resultIntent = new Intent();
                                            resultIntent.putExtra("pickedProductId", product);
                                            setResult(RESULT_OK, resultIntent);
                                            finish();
                                            }
                                    });
                                    listView.setAdapter(adapter);
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
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                System.out.println("ok ");

            } else {
                System.out.println("lỗi ");
            }
        }
    }

}