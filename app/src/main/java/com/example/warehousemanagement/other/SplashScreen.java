package com.example.warehousemanagement.other;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.DsSanPham;
import com.example.warehousemanagement.QLStorage;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.TrangChu;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_TIMEOUT = 2000; // Thời gian hiển thị SplashScreen (2 giây)
    private  String role = DangNhap.account.getUser().getRole();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashs);

        // Tạo animation cho hình ảnh vòng tròn xoay
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        ImageView imageView = findViewById(R.id.logo);
        imageView.startAnimation(rotation);

        // Sử dụng Handler để chuyển sang màn hình tiếp theo sau khi hiển thị SplashScreen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (role.equals("stocker")) {
                    Intent intent = new Intent(SplashScreen.this, QLStorage.class);
                    startActivity(intent);
                } else if (role.equals("saler")) {
                    Intent intent = new Intent(SplashScreen.this, DsSanPham.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashScreen.this, TrangChu.class);
                    startActivity(intent);
                }

                finish(); // Đóng SplashScreenActivity để không quay lại sau khi chuyển màn hình
            }
        }, SPLASH_TIMEOUT);
    }
}
