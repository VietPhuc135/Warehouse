package com.example.warehousemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.warehousemanagement.market.AddMarket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class QLStorage extends AppCompatActivity {

    private ListView storageList;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> storageNames;
    String header = DangNhap.account.getToken();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_manager);

        storageList = findViewById(R.id.lvStorage);
        storageNames = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.activity_list_market, storageNames);
        storageList.setAdapter(adapter);
        ImageView imgArrageStorage = findViewById(R.id.imgArrageStorage);
        imgArrageStorage.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(QLStorage.this, AddStorage.class);
                   startActivity(intent);
               }
           }
        );
        // Gọi phương thức để thực hiện yêu cầu HTTP và hiển thị danh sách
        fetchStorageList();
    }

    private void fetchStorageList() {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("http://14.225.211.190:4001/api/storage/query")
                .method("POST", body)
                .addHeader("Authorization", "Bearer " + header)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int statusCode = response.code();
                if (statusCode == 201) {
                    String jsonData = response.body().string();
                    System.out.println("thành công");
                    Log.d("Response", jsonData);
                    System.out.println(jsonData);
                    try {
                        JSONArray jsonArray = new JSONArray(jsonData);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            //String storageName = jsonObject.optString("address");
                            String address = jsonObject.getString("address");
                            String code = jsonObject.getString("code");
                            String storageInfo = "Address: " + address + "\nCode: " + code;
                            storageNames.add(storageInfo);

                        }

                        // Cập nhật giao diện trong luồng UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });

                    } catch (JSONException e) {
                        System.out.println("khong thanh cong");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("khong thanh cong");
                e.printStackTrace();
            }
        });
    }
}