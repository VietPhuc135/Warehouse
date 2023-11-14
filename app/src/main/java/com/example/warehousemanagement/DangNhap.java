package com.example.warehousemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.obj.Account;
import com.example.warehousemanagement.other.SplashScreen;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DangNhap extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    public static Account account;
    boolean isResponseReceived = false;


    public static Account getAccount() {
        return account;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ánh xạ các EditText
        editTextEmail = findViewById(R.id.emailSignIn);
        editTextPassword = findViewById(R.id.password);

        buttonLogin = findViewById(R.id.Login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy giá trị email và password từ EditText
                String email =
//                        "admin";
//                       "stocker";
//                "duong";
                editTextEmail.getText().toString();
                //String email1 = "admin";
                String password =
//                        "123";
//                        "sonha1";
                        editTextPassword.getText().toString();
                //String password1 = "123";
                if (email.equals("") && password.equals("")) {

                    System.out.println("khong duoc null ");
                    Toast.makeText(getApplicationContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                if (email.equals("")){
                    Toast.makeText(getApplicationContext(), "Vui lòng điền email", Toast.LENGTH_SHORT).show();
                }
                if (password.equals("")){
                    Toast.makeText(getApplicationContext(), "Vui lòng điền password", Toast.LENGTH_SHORT).show();
//                if (email1.equals("admin") && password1.equals("123")){
//                    Intent intent = new Intent(DangNhap.this, SplashScreen.class);
//                    startActivity(intent);
//                }
                }else{
                    loginAPI(email, password);
                }
                // Gọi phương thức gửi yêu cầu POST API
            }
        });
    }

    private void loginAPI(String email, String password) {
        // Tạo JSON body
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", email);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Tạo request body
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                jsonBody.toString());

        // Tạo yêu cầu POST
        Request request = new Request.Builder()
                .url(Api.baseURL + "/api/v1/auth/authenticate")
                .post(requestBody)
                .build();

        // Tạo OkHttpClient
        OkHttpClient client = new OkHttpClient();

        // Thực hiện yêu cầu bất đồng bộ
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Xử lý khi gặp lỗi
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Xử lý khi nhận được phản hồi từ server
                String responseData = response.body().string();
                // lưu dữ liệu trả về từ api
                System.out.println( "responseData" + responseData + "responseData");
                if(responseData.trim().isEmpty()){
                    System.out.println("ok");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DangNhap.this, "Thông tin đăng nhập sai!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else {
                    System.out.println(responseData);
                    Gson gson = new Gson();
                    account = gson.fromJson(responseData, Account.class);
                    System.out.println( DangNhap.account.getToken());
                    Intent intent = new Intent(DangNhap.this, SplashScreen.class);
                    startActivity(intent);
                }
            }
        });
    }
}
