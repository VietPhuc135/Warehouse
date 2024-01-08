package com.example.warehousemanagement.storage;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.warehousemanagement.Api;
import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.additem.DsSanPham;
import com.example.warehousemanagement.obj.CountByType;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StaticProduct extends AppCompatActivity {

    Button btnDsHetHan, btnDsSapHetHan, btnAllSanPham;
    PieChart piechart ;
    String idStor,soluongHang;
    TextView txtSoLuongStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_product);
        piechart = findViewById(R.id.pieChart);
        btnAllSanPham = findViewById(R.id.btnAllSanPham);
        btnDsHetHan = findViewById(R.id.btnDsHetHan);
        txtSoLuongStorage = findViewById(R.id.txtSoLuongStorage);
        btnDsSapHetHan = findViewById(R.id.btnDsSapHetHan);

        idStor = DangNhap.account.getStorageId() != null ? DangNhap.account.getStorageId()   : " ";


        btnDsSapHetHan.setOnClickListener(view -> {
            Intent intent = new Intent(StaticProduct.this, ListProduct.class);
            startActivity(intent);
        });

        btnDsHetHan.setOnClickListener(view -> {
            Intent intent1 = new Intent(StaticProduct.this, ListProduct.class);
            startActivity(intent1);
        });

        btnAllSanPham.setOnClickListener(view -> {
            Intent intent2 = new Intent(StaticProduct.this, DsSanPham.class);
            intent2.putExtra("idSto",idStor);
            startActivity(intent2);
        });
        new LoadDataPieTask(piechart).execute();
        new LoadSoLuongHang().execute();

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        new LoadDataPieTask(piechart).execute();
        new LoadSoLuongHang().execute();
    }

    public class LoadSoLuongHang extends  AsyncTask<Void, Void , String>{
        @Override
        protected String doInBackground(Void... voids) {
            try {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("text/plain");
                RequestBody body = RequestBody.create(mediaType, "");
                Request request = new Request.Builder()
                        .url(Api.baseURL + "/product/inventory-quantity")
                        .method("GET", null)
                        .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdG9ja2VyIiwiaWF0IjoxNzA0NjE2Njg5LCJleHAiOjE3MDUxNDIyODl9.EF6KKkuVeGVkkZUYeeWH4Mzm2Cp83Mi0Qs9HtueefZk")
                        .build();
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
//                    Gson gson = new Gson();
//                    Type listType = new TypeToken<List<CountByType>>() {}.getType();
                     soluongHang = responseBody;
                     System.out.println("Só lượng hàng "+responseBody);
                    return soluongHang;
                } else {
                    return "__";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "__";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                txtSoLuongStorage.setText(s);
            } else {
                txtSoLuongStorage.setText("--");

            }
        }
    }
    public static class LoadDataPieTask extends AsyncTask<Void, Void, List<CountByType>> {
        @SuppressLint("StaticFieldLeak")
        private final PieChart pieChart;

        public LoadDataPieTask(PieChart pieChart) {
            this.pieChart = pieChart;
        }

        @Override
        protected List<CountByType> doInBackground(Void... voids) {
            try {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("text/plain");
                RequestBody body = RequestBody.create(mediaType, "");
                Request request = new Request.Builder()
                        .url(Api.baseURL + "/product/list-count-by-type")
                        .method("GET", null)
                        .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdG9ja2VyIiwiaWF0IjoxNzA0NjE2Njg5LCJleHAiOjE3MDUxNDIyODl9.EF6KKkuVeGVkkZUYeeWH4Mzm2Cp83Mi0Qs9HtueefZk")
                        .build();
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<CountByType>>() {}.getType();
                    return gson.fromJson(responseBody, listType);
                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<CountByType> pieaDataList) {
            if (pieaDataList != null) {
                List<PieEntry> entries = new ArrayList<>();
                for (CountByType product : pieaDataList) {
                    entries.add(new PieEntry(product.getSoLuong(), product.getType()));
                }

                PieDataSet dataSet = new PieDataSet(entries, "Sản phẩm");
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                PieData pieData = new PieData(dataSet);
                pieChart.setData(pieData);
                pieChart.invalidate();
            } else {
                // Xử lý khi có lỗi
            }
        }
    }

}