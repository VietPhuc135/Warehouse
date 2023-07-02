package com.example.warehousemanagement.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.additem.DsSanPham;
import com.example.warehousemanagement.additem.EditProduct;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditNguoiDung extends AppCompatActivity {
    private EditText username, password, email, name, role, address, age, marketId, storageId;
    private Button btnSubmitUser;
    String header ;
    JSONObject jsonObject = new JSONObject();
    String id  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edituser);
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id");

        }
        setContentView(R.layout.activity_edititem);
        header = DangNhap.account.getToken();
        username = findViewById(R.id.etUsername1);
        password = findViewById(R.id.etPassword1);
        email = findViewById(R.id.etEmail1);
        name = findViewById(R.id.etName1);
        role = findViewById(R.id.etRole1);
        address = findViewById(R.id.etAddress1);
        age = findViewById(R.id.etAge1);
        marketId = findViewById(R.id.etMarketID1);
        storageId = findViewById(R.id.etStorageId1);
        btnSubmitUser = findViewById(R.id.btnSubmitUser);

        new EditNguoiDung.FetchUserDetails().execute(id);

        btnSubmitUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ EditText
                String usernametxt = username.getText().toString().trim();
                String passwordtxt = password.getText().toString().trim();
                String emailtxt = email.getText().toString().trim();
                String nametxt = name.getText().toString().trim();
                String roletxt = role.getText().toString().trim();
                String addresstxt = address.getText().toString().trim();
                int agetxt = Integer.parseInt(age.getText().toString().trim());
                String marketIdtxt = marketId.getText().toString().trim();
                String storageIdtxt = storageId.getText().toString().trim();

                // Tạo JSON object từ dữ liệu

                try {
                    jsonObject.put("username", usernametxt);
                    jsonObject.put("password", passwordtxt);
                    jsonObject.put("email", emailtxt);
                    jsonObject.put("name", nametxt);
                    jsonObject.put("role", roletxt);
                    jsonObject.put("address", addresstxt);
                    jsonObject.put("age", agetxt);
                    jsonObject.put("marketId", marketIdtxt);
                    jsonObject.put("storageId", storageIdtxt);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new MyAsyncTask().execute(jsonObject.toString());
            }});
    }

    private class FetchUserDetails extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("text/plain");
            Request request = new Request.Builder()
                    .url("http://14.225.211.190:4001/api/user/"+ id)
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
                    username.setText(result.getString("username"));
                    password.setText(result.getString("password"));
                    email.setText(result.getString("email"));
                    name.setText(String.valueOf(result.getInt("name")));
                    role.setText(result.getString("role"));
                    address.setText(result.getString("adđress"));
                    age.setText(result.getString("age"));
                    marketId.setText(result.getString("marketId"));
                    storageId.setText(result.getString("storageId"));
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
                    .url("http://14.225.211.190:4001/api/user/"+ id)
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
                Intent intent = new Intent(EditNguoiDung.this, QLNguoiDung.class);
                startActivity(intent);
            } else {
                System.out.println("lỗi ");
            }
        }
    }
}
