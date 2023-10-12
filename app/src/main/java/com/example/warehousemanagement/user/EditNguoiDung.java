package com.example.warehousemanagement.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.Api;
import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditNguoiDung extends AppCompatActivity {
    private EditText username, password, email, name, role, phone, marketId, storageId;
    private Button btnSubmitUser;
    String header ;
    JSONObject jsonObject = new JSONObject();
    String id  ;
    List<String> items;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edituser);
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id");

        }
        header = DangNhap.account.getToken();
        username = findViewById(R.id.etUsername1);
        password = findViewById(R.id.etPassword1);
        email = findViewById(R.id.etEmail1);
        name = findViewById(R.id.etName1);
        role = findViewById(R.id.etRole1);
        phone = findViewById(R.id.etAddress1);
        //age = findViewById(R.id.etAge1);
        marketId = findViewById(R.id.etMarketID1);
        storageId = findViewById(R.id.etStorageId1);
        btnSubmitUser = findViewById(R.id.btnSubmitUser1);

        Button btnDeleteUser1 = findViewById(R.id.btnDeleteUser1);
        btnDeleteUser1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DeleteUser(id);
                    }
                }
        );
         spinner = (Spinner) findViewById(R.id.spinnerRole1);
        items = new ArrayList<>();
        if (spinner != null) {
            items.add("ADMIN");
            items.add("SALER");
            items.add("STOCKER");
            ArrayAdapter<String> adapterSta = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
            adapterSta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapterSta);

            // Thiết lập giá trị mặc định
//            int defaultPosition = 0; // Vị trí mục mặc định (số thứ tự)
//            spinner.setSelection(defaultPosition);
        } else {
            Log.e("Spinner Error", "Spinner not found or not initialized correctly");
        }

        new EditNguoiDung.FetchUserDetails().execute(id);

        btnSubmitUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ EditText
                String usernametxt = username.getText().toString().trim();
                String passwordtxt = password.getText().toString().trim();
                String emailtxt = email.getText().toString().trim();
                String nametxt = name.getText().toString().trim();
                String roletxt = spinner.getSelectedItem().toString();
                String phonetxt = phone.getText().toString().trim();
                //int agetxt = Integer.parseInt(age.getText().toString().trim());
                String marketIdtxt = marketId.getText().toString().trim();
                String storageIdtxt = storageId.getText().toString().trim();

                // Tạo JSON object từ dữ liệu

                try {
                    jsonObject.put("username", usernametxt);
                    jsonObject.put("password", passwordtxt);
                    jsonObject.put("email", emailtxt);
                    jsonObject.put("name", nametxt);
                    jsonObject.put("role", roletxt);
                    jsonObject.put("phone", phonetxt);
                    //jsonObject.put("age", agetxt);
                    jsonObject.put("marketId", marketIdtxt);
                    jsonObject.put("storageId", storageIdtxt);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new MyAsyncTask().execute(jsonObject.toString());
            }});
    }
    private void DeleteUser(String orderId) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(Api.baseURL +  "/user/delete/" + orderId  )
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

    private class FetchUserDetails extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("text/plain");
            Request request = new Request.Builder()
                    .url(Api.baseURL + "/user/getList"+ id)
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
                    String apiStatus = result.getString("role"); // Giả sử giá trị trả về từ API là "Food"

// Tìm vị trí của giá trị từ API trong danh sách
                    int position = items.indexOf(apiStatus);
                    if (position != -1) {
                        spinner.setSelection(position);
                    } else {

                    }
                    // Populate the EditText fields with retrieved data
                    username.setText(result.getString("username"));
                    password.setText(result.getString("password"));
                    email.setText(result.getString("email"));
                    name.setText(String.valueOf(result.getInt("name")));
                    role.setText(result.getString("role"));
                    phone.setText(result.getString("adđress"));
                    //age.setText(result.getString("age"));
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
                    .url(Api.baseURL + "/user/update"+ id)
                    .addHeader("Authorization", "Bearer " +  header)
                    .method("PUT", requestBody)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                int i =  response.code();
                if (response.isSuccessful()) {
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
