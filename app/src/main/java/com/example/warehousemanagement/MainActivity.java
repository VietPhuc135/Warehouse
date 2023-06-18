package com.example.warehousemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
                        Intent intent = new Intent(MainActivity.this, DangNhap.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }
}