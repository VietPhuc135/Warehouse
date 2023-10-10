package com.example.warehousemanagement.storage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddStorage extends AppCompatActivity {
    EditText code, address , latlng , longlng;

    String header, marketId;
    JSONObject jsonObject;

    Button btnSubmitStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addstorage);
        code = findViewById(R.id.etCodeStorage);
        address = findViewById(R.id.etAddressStorage);
        latlng = findViewById(R.id.etLatitudeStorage);
        longlng = findViewById(R.id.etLongStorage);

        header = DangNhap.account.getToken();
            marketId = DangNhap.account.getMarketId();
        btnSubmitStorage = findViewById(R.id.btnSubmitStorage);


        btnSubmitStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ EditText
                String addresstxt = address.getText().toString().trim();
                String codetxt = code.getText().toString().trim();
                String lattext = latlng.getText().toString().trim();
                String longtext = longlng.getText().toString().trim();


                // Tạo JSON object từ dữ liệu
                jsonObject = new JSONObject();
                try {
                    jsonObject.put("name", codetxt);
                    jsonObject.put("address", addresstxt);
                    jsonObject.put("latitude", lattext);
                    jsonObject.put("longtitude", longtext);
                    jsonObject.put("marketId", marketId);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Gửi yêu cầu HTTP POST
                new MyAsyncTask().execute(jsonObject.toString());
            }
        });
    }

    private class MyAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(mediaType, params[0]);
            Request request = new Request.Builder()
                    .url("http://192.168.1.5:8080/storage/add")
                    .addHeader("Authorization", "Bearer " + header)
                    .post(requestBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                System.out.println(jsonObject);
                Intent intent = new Intent(AddStorage.this, QLStorage.class);
                startActivity(intent);
            } else {
                System.out.println("lỗi ");
            }
        }
    }
}
