package com.example.warehousemanagement.market;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.QLMarket;
import com.example.warehousemanagement.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddMarket extends AppCompatActivity {
    EditText code, address;

    String header;
    JSONObject jsonObject;
    Button btnSubmitMarket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmarket);
        code = findViewById(R.id.etCodeMarket);
        address = findViewById(R.id.etCodeMarket);

        header = DangNhap.account.getToken();

        btnSubmitMarket = findViewById(R.id.btnSubmitMarket);


        btnSubmitMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ EditText
                String addresstxt = address.getText().toString().trim();
                String codetxt = code.getText().toString().trim();


                // Tạo JSON object từ dữ liệu
                jsonObject = new JSONObject();
                try {
                    jsonObject.put("code", codetxt);
                    jsonObject.put("address", addresstxt);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
                    .url("http://14.225.211.190:4001/api/market")
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
                Intent intent = new Intent(AddMarket.this, QLMarket.class);
                startActivity(intent);
            } else {
                System.out.println("lỗi ");
            }
        }
    }
}
