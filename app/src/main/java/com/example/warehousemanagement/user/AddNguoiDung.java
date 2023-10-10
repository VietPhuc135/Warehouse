package com.example.warehousemanagement.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.TrangChu;
import com.example.warehousemanagement.additem.AddProduct;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddNguoiDung extends AppCompatActivity {
    private EditText username, password, email, name, role, address, age, marketId, storageId;
    private Button btnSubmitUser;
    String header;
    JSONObject jsonObject = new JSONObject();
    Spinner spinner;
    List<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adduser);
        header = DangNhap.account.getToken();

        username = findViewById(R.id.etUsername);
        password = findViewById(R.id.etPassword);
        email = findViewById(R.id.etEmail);
        name = findViewById(R.id.etName);
        role = findViewById(R.id.etRole);
        address = findViewById(R.id.etAddress);
        age = findViewById(R.id.etAge);
        marketId = findViewById(R.id.etMarketID);
        storageId = findViewById(R.id.etStorageId);
        btnSubmitUser = findViewById(R.id.btnSubmitUser);
        spinner = (Spinner) findViewById(R.id.spinnerRole);
        items = new ArrayList<>();
        if (spinner != null) {
            items.add("admin");
            items.add("SALER");
            items.add("STOCKER");
            ArrayAdapter<String> adapterSta = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
            adapterSta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapterSta);
        }
        btnSubmitUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ EditText
                String usernametxt = username.getText().toString().trim();
                String passwordtxt = password.getText().toString().trim();
                String emailtxt = email.getText().toString().trim();
                String nametxt = name.getText().toString().trim();
                String roletxt = spinner.getSelectedItem().toString();
                String addresstxt = address.getText().toString().trim();
                int agetxt = Integer.parseInt(age.getText().toString().trim());
                String marketIdtxt = marketId.getText().toString().trim();
                String storageIdtxt = storageId.getText().toString().trim();

                // Tạo JSON object từ dữ liệu
//                jsonObject = new JSONObject();
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
                    .url("http://14.225.211.190:4001/api/user")
                    .addHeader("Authorization", "Bearer " + header)
                    .method("POST", requestBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();

//                if (response.isSuccessful()) {
//                    return true;
//                }
                int i =  response.code();
                if (i == 201) {
                    System.out.println(jsonObject);
                    Intent intent = new Intent(AddNguoiDung.this, TrangChu.class);
                    startActivity(intent);
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
                Intent intent = new Intent(AddNguoiDung.this, QLNguoiDung.class);
                startActivity(intent);
            } else {
                System.out.println("lỗi ");
            }
        }
    }
}
