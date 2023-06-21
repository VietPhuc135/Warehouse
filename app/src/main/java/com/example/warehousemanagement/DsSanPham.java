package com.example.warehousemanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.additem.AddProduct;
import com.example.warehousemanagement.additem.ArrayProduct;
import com.example.warehousemanagement.obj.Product;
import com.example.warehousemanagement.other.SplashScreen;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DsSanPham extends AppCompatActivity {
    String header;
    private ListView listView;
    private ArrayProduct adapter;
    //    private ArrayAdapter<Product> adapter;
    private List<Product> itemList;
    ImageView imgAddProduct;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        imgAddProduct = findViewById(R.id.imgAddProduct);
        listView = findViewById(R.id.lvProduct);
        header = DangNhap.account.getToken();
//        adapter = new ArrayAdapter<>(this,R.layout.product_item,itemList);
        adapter = new ArrayProduct(this, itemList);
        imgAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DsSanPham.this, AddProduct.class);
                startActivity(intent);
            }
        });
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
                                case R.id.repair_product:
                                    // Xử lý khi người dùng chọn Edit
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
                    System.out.println("Đây la itemlisst " + responseBody);
                    // Cập nhật giao diện trong luồng UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Khởi tạo và thiết lập Adapter
                            adapter = new ArrayProduct(DsSanPham.this, itemList);
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
    }
}