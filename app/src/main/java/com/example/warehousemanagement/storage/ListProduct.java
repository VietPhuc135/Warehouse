package com.example.warehousemanagement.storage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.warehousemanagement.Api;
import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.additem.AddProduct;
import com.example.warehousemanagement.additem.ArrayProduct;
import com.example.warehousemanagement.additem.DsSanPham;
import com.example.warehousemanagement.obj.CountByType;
import com.example.warehousemanagement.obj.Product;
import com.example.warehousemanagement.order.AddOrder;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ListProduct extends AppCompatActivity {
    String header;
    private ListView listView;
    private ArrayProduct adapter;
    private List<Product> itemList;
    ImageView imgAddProduct,imaArrange;
    AutoCompleteTextView edtSearchProduct;
    String id;
    String role;
    String storageId;
    Spinner sortSpinner;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        imgAddProduct = findViewById(R.id.imgAddProduct);
        imaArrange = findViewById(R.id.imgArrageProduct);
        edtSearchProduct = findViewById(R.id.searchProduct);
        listView = findViewById(R.id.lvProduct);
        header = DangNhap.account.getToken();
        role = DangNhap.account.getRole();
        storageId = DangNhap.account.getStorageId();
        sortSpinner = findViewById(R.id.sortSpinner);
        TextView idTitle = findViewById(R.id.idTitle);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.sort_options, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(spinnerAdapter);
        sortSpinner.setVisibility(View.GONE);


        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id");
        }

        if(id.equals("1") ){
            idTitle.setText("Danh sách sản phẩm hết hạn");
            idTitle.setTextSize(27);
        }
        else if (id.equals("2")){
            idTitle.setText("Danh sách sản phẩm sắp đến hạn");
            idTitle.setTextSize(26);
        }

        adapter = new ArrayProduct(this, itemList);
        if (role.equals("STOCKER")){
            storageId = intent.getStringExtra("idSto");
        }
        if (role.equals("SALER")){
            imgAddProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ListProduct.this, AddOrder.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            });
            imaArrange.setVisibility(View.GONE);
        }else{
            imgAddProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ListProduct.this, AddProduct.class);
                    startActivity(intent);
                }
            });

            imaArrange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Toggle visibility of the Spinner
//                    toggleSpinnerVisibility();
                }
            });
        }
        if(id.equals("1") ){
            new MyAsyncTask().execute();
        }
        else if (id.equals("2")){
            new MyAsyncTask2().execute();

        }

        // Thiết lập adapter cho AutoCompleteTextView
        ArrayAdapter<Product> autoCompleteAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, itemList);
        edtSearchProduct.setAdapter(autoCompleteAdapter);

        // Xử lý sự kiện chọn mục trong AutoCompleteTextView
        edtSearchProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Xử lý sự kiện khi chọn một mục từ danh sách gợi ý
                Product selectedProduct = (Product) parent.getItemAtPosition(position);
                // Hiển thị thông tin sản phẩm hoặc thực hiện hành động mong muốn
                Toast.makeText(ListProduct.this, selectedProduct.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện thay đổi văn bản trong AutoCompleteTextView để lọc danh sách
        edtSearchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Không cần xử lý trước sự thay đổi văn bản
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Lọc danh sách sản phẩm theo văn bản nhập vào
                adapter.getFilter().filter(charSequence.toString());
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Không cần xử lý sau sự thay đổi văn bản
            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(id.equals("1") ){
            new MyAsyncTask().execute();
        }
        else if (id.equals("2")){
            new MyAsyncTask2().execute();

        }
    }

    @SuppressLint("StaticFieldLeak")
    private class MyAsyncTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("text/plain");
            Request request = new Request.Builder()
                    .url(Api.baseURL + "/product/expiration")
                    .method("GET", null)
                    .addHeader("Authorization", "Bearer "+header)
                    .build();
            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
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
                                adapter = new ArrayProduct(ListProduct.this, itemList);
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
    private class MyAsyncTask2 extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("text/plain");
            Request request = new Request.Builder()
                    .url(Api.baseURL + "/product/still-expired/90")
                    .method("GET", null)
                    .addHeader("Authorization", "Bearer "+header)
                    .build();
            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
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
                                adapter = new ArrayProduct(ListProduct.this, itemList);
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