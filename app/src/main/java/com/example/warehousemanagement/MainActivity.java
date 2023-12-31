package com.example.warehousemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.warehousemanagement.obj.Account;
import com.example.warehousemanagement.other.SplashScreen;
import com.example.warehousemanagement.user.SaveLogin;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    Button btnDN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnDN = (Button) findViewById(R.id.LoginButtonMain);
        btnDN.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Chuyển sang TrangChuActivity
                        SaveLogin login = new SaveLogin(getApplicationContext());
                        String info = null;
                        try {
                            info = login.getUserInfo();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (info != null) {
                            Gson gson = new Gson();
                            DangNhap.account = gson.fromJson(info, Account.class);
                            Intent intent = new Intent(MainActivity.this, SplashScreen.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(MainActivity.this, DangNhap.class);
                            startActivity(intent);
                        }
                        finish();
                    }
                }
        );
    }
}