package com.example.warehousemanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.obj.Account;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import okhttp3.*;

public class DangNhap extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    public static Account account;


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
                String email = "admin";
                String password = "sonha12";
//                        editTextPassword.getText().toString();

                if (editTextEmail.getText() == null && editTextPassword.getText() == null) {
                    View view = findViewById(R.id.password);
//
//                    Snackbar snackbar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT);
//                    View snackbarView = snackbar.getView();
//                    LayoutInflater inflater = LayoutInflater.from(snackbarView.getContext());
//                    View customSnackbarView = inflater.inflate(R.layout.custom_layout_snackbar, null);
//                    LinearLayout snackbarLayout = customSnackbarView.findViewById(R.id.snackbar_layout);
//                    ImageView snackbarIcon = customSnackbarView.findViewById(R.id.snackbar_icon);
//                    TextView snackbarText = customSnackbarView.findViewById(R.id.snackbar_text);
//
//// Tùy chỉnh giao diện và nội dung của Snackbar
////                    snackbarLayout.setBackgroundColor(getResources().getColor(R.color.custom_snackbar_background));
////                    snackbarIcon.setImageResource(R.drawable.custom_snackbar_icon);
//                    snackbarText.setText("Custom Snackbar");
//
//// Đặt tệp XML layout tùy chỉnh cho Snackbar
//                    Snackbar.SnackbarLayout snackbarLay = (Snackbar.SnackbarLayout) snackbar.getView();
//                    snackbarLay.addView(customSnackbarView, 0);
//                    snackbar.show();
                    System.out.println("khoong ddc null ");
                } else {
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
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonBody.toString());

        // Tạo yêu cầu POST
        Request request = new Request.Builder()
                .url("http://14.225.211.190:4001/api/auth/login")
                .post(requestBody)
//                .addHeader("Authorization", "Bearer" + "" )
                .build();

        // Tạo OkHttpClient
        OkHttpClient client = new OkHttpClient();

        // Thực hiện yêu cầu bất đồng bộ
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Xử lý khi gặp lỗi
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Xử lý khi nhận được phản hồi từ server
                String responseData = response.body().string();
                // lưu dữ liệu trả về từ api
                Gson gson = new Gson();
                account = gson.fromJson(responseData, Account.class);
                // Chuyển sang TrangChuActivity

                Intent intent = new Intent(DangNhap.this, TrangChu.class);
                startActivity(intent);
                System.out.println( responseData);


                // Tiếp tục xử lý dữ liệu phản hồi từ server
            }
        });
    }
}
