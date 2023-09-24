package com.example.warehousemanagement.storage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.additem.DsSanPham;
import com.example.warehousemanagement.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditStorage extends AppCompatActivity {
    private EditText etName, etCode;
    private Button btnSubmit,btnDeleteStorage2;
    String header ;
    String role;
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
        role = DangNhap.account.getUser().getRole();
        etName = findViewById(R.id.etAddressStorage2);
        etCode = findViewById(R.id.etCodeStorage2);
        btnSubmit = findViewById(R.id.btnSubmitStorage2);
        btnDeleteStorage2 = findViewById(R.id.btnDeleteStorage2);

        new FetchProductDetails().execute(id);
        btnDeleteStorage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteStorage(id);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (role.equals("saler") ){
                    Toast.makeText(getBaseContext(), "Bạn là saler ! Bạn không có quyền sửa", Toast.LENGTH_SHORT).show();
                    return ;
                }
                // Lấy dữ liệu từ EditText
              else{
                  String name = etName.getText().toString().trim();
                    String code = etCode.getText().toString().trim();

                    // Tạo JSON object từ dữ liệu

                    try {
                        jsonObject.put("code", code);
                        jsonObject.put("address", name);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new MyAsyncTask().execute(jsonObject.toString());
                }

            }});

    }
    private void DeleteStorage(String orderId) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("http://14.225.211.190:4001/api/storage/" + orderId  )
                .delete()
                .addHeader("Authorization", "Bearer " + header)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Gọi lại MyAsyncTask để tải lại danh sách đơn hàng
                    finish();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
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
                    etCode.setText(result.getString("code"));
                    etName.setText(result.getString("address"));

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
                    .put(requestBody)
                    .build();

                System.out.println(params[0]);
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    System.out.println(jsonObject);
                    Intent intent = new Intent(EditStorage.this, QLStorage.class);
                    startActivity(intent);
                    finish();
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


