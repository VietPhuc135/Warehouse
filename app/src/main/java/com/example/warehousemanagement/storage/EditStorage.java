package com.example.warehousemanagement.storage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.additem.DsSanPham;
import com.example.warehousemanagement.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditStorage extends AppCompatActivity {
    private EditText etName, etCode;
    private Button btnSubmit;
    String header ;
    JSONObject jsonObject = new JSONObject();
    String id  ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id");

        }
        setContentView(R.layout.activity_editstorage);
        header = DangNhap.account.getToken();
        etName = findViewById(R.id.etAddressStorage2);
        etCode = findViewById(R.id.etCodeStorage2);
        btnSubmit = findViewById(R.id.btnSubmit2);

        new FetchProductDetails().execute(id);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ EditText
                String name = etName.getText().toString().trim();
                String code = etCode.getText().toString().trim();

                // Tạo JSON object từ dữ liệu

                try {
                    jsonObject.put("name", name);
                    jsonObject.put("code", code);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new MyAsyncTask().execute(jsonObject.toString());
            }});

    }
    private class FetchProductDetails extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("text/plain");
            Request request = new Request.Builder()
                    .url("http://14.225.211.190:4001/api/storage/"+ id)
                    .addHeader("Authorization", "Bearer " +  header)
                    .method("GET",null)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    return new JSONObject(responseData);
                } else {
                    // Handle error case
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if (result != null) {
                try {
                    // Populate the EditText fields with retrieved data
                    etName.setText(result.getString("name"));
                    etCode.setText(result.getString("code"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
//                System.out.println(result.toString());
            }
        }
    }

    private class MyAsyncTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(mediaType, params[0]);
            Request request = new Request.Builder()
                    .url("http://14.225.211.190:4001/api/storage/"+ id)
                    .addHeader("Authorization", "Bearer " +  header)
                    .method("PUT", requestBody)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                int i =  response.code();
                if (i == 201) {
                    System.out.println(jsonObject);
//                    Intent intent = new Intent(EditProduct.this, TrangChu.class);
//                    startActivity(intent);
                } else {
                    System.out.println("lỗi " + response.toString());
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
                Intent intent = new Intent(EditStorage.this, DsSanPham.class);
                startActivity(intent);
            } else {
                System.out.println("lỗi ");
            }
        }
    }


}


