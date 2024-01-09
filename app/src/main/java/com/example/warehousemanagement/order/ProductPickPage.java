package com.example.warehousemanagement.order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.Api;
import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.additem.AddProduct;
import com.example.warehousemanagement.additem.ArrayProduct;
import com.example.warehousemanagement.additem.DsSanPham;
import com.example.warehousemanagement.obj.Product;
import com.github.mikephil.charting.charts.PieChart;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
    Spinner sortSpinner;
    String type = "MEAT";


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
        sortSpinner = findViewById(R.id.sortSpinner);

        Log.d("goitao","1");

      searchBarviewpd.setVisibility(View.GONE);
        idTitle.setText("Danh sách sản phẩm");
        header = DangNhap.account.getToken();
        adapter = new ProductPickAdapter(this, itemList);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.sort_options, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(spinnerAdapter);
        sortSpinner.setVisibility(View.VISIBLE);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Xử lý sự kiện sắp xếp danh sách khi chọn mục trong Spinner
                String selectedItem = parentView.getItemAtPosition(position).toString();
                if (selectedItem.equals(getString(R.string.sort_by_Cake))) {
                    type = "CAKE";

                } else if (selectedItem.equals(getString(R.string.sort_by_Candy))) {
                    type = "CANDY";
                }
                if (selectedItem.equals(getString(R.string.sort_by_MEAT))) {
                    type = "MEAT";
                } else if (selectedItem.equals(getString(R.string.sort_by_MILK))) {
                    type = "MILK";
                }
                else if (selectedItem.equals(getString(R.string.sort_by_CannedFood))) {
                    type = "CANNED FOOD";
                }
                restart();
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.BLACK);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Không có hành động cụ thể khi không chọn mục nào
            }
        });
    }

    void restart(){
        new MyAsyncTask().execute();

    }


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
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("type",type)
                    .build();
            Request request = new Request.Builder()
                    .url(Api.baseURL + "/product/getlist")
                    .method("POST", body)
                    .addHeader("Authorization", "Bearer "+header)
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