package com.example.warehousemanagement.order;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.additem.AddProduct;
import com.example.warehousemanagement.additem.ArrayProduct;
import com.example.warehousemanagement.obj.Order;
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

public class OrderList extends AppCompatActivity {
    String header;
    private ListView listView;
    private ArrayOrder adapter;
    private List<Order> orderList;
    ImageView imgAddProduct;
    Context context;
    TextView idTitle;
    EditText search;
    String storageID,idMarket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        imgAddProduct = findViewById(R.id.imgAddProduct);
        listView = findViewById(R.id.lvProduct);
        idTitle = findViewById(R.id.idTitle);
        search = findViewById(R.id.searchProduct);
        search.setHint("Search order");
        header = DangNhap.account.getToken();
        idMarket = DangNhap.account.getUser().getMarketId() != null ? DangNhap.account.getUser().getMarketId() : " ";
        idTitle.setText("Order List");
         ImageView imgArrageProduct = findViewById(R.id.imgArrageProduct);
        imgArrageProduct.setVisibility(View.GONE);
        Intent intent = getIntent();
        if (intent != null) {
            storageID = intent.getStringExtra("id");
        }

        imgAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderList.this, AddOrder.class);
                intent.putExtra("id", storageID);
                startActivity(intent);
            }
        });
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
            RequestBody body = RequestBody.create(mediaType,
                    "{\r\n    \"filter\":{\r\n        \"marketId\":{\r\n            \"eq\":" + idMarket+"\r\n        }\r\n    }\r\n}");
            Request request = new Request.Builder()
                    .url("http://14.225.211.190:4001/api/order/query")
                    .method("POST", body)
                    .addHeader("Authorization", "Bearer " + header)
                    .build();
            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Order>>() {
                        }.getType();

                        orderList = gson.fromJson(responseBody, listType);
                        System.out.println("Đây la orrderlisst " + responseBody);
                        // Cập nhật giao diện trong luồng UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Khởi tạo và thiết lập Adapter
                                adapter = new ArrayOrder(OrderList.this, orderList);
                                listView.setAdapter(adapter);
                           }
                       }); }                }
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
    public void loadOrderList() {
        new MyAsyncTask().execute();
        adapter.notifyDataSetChanged();
    }
}