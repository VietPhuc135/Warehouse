package com.example.warehousemanagement.other;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Keep;
import androidx.appcompat.app.AppCompatActivity;
import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.storage.QLStorage;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.TrangChu;

@Keep
public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_TIMEOUT = 2000; // Thời gian hiển thị SplashScreen (2 giây)
    private  String role = DangNhap.account.getRole();
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
//                if(role != null ){
                    if (role.equals("STOCKER")) {
                        Intent intent = new Intent(SplashScreen.this, StockerPage.class);
                        startActivity(intent);
                    } else if (role.equals("SALER")) {
                        Intent intent = new Intent(SplashScreen.this, QLStorage.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SplashScreen.this, TrangChu.class);
                        startActivity(intent);
                    }
//
//                }
//                else {
//                    Intent intent = new Intent(SplashScreen.this, DangNhap.class);
//                    startActivity(intent);
//                }
                finish();           // Đóng SplashScreenActivity để không quay lại sau khi chuyển màn hình
            }
        }, SPLASH_TIMEOUT);
    }
}
