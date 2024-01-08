package com.example.warehousemanagement.storage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.warehousemanagement.R;

public class StaticProduct extends AppCompatActivity {

    Button btnDsHetHan, btnDsSapHetHan, btnAllSanPham;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_product);

        btnAllSanPham = findViewById(R.id.btnAllSanPham);
        btnDsHetHan = findViewById(R.id.btnDsHetHan);
        btnDsSapHetHan = findViewById(R.id.btnDsSapHetHan);

        btnDsSapHetHan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StaticProduct.this, ListProduct.class);
                startActivity(intent);
            }
        });

        btnDsHetHan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(StaticProduct.this, ListProduct.class);
                startActivity(intent1);
            }
        });

        btnAllSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(StaticProduct.this, ListProduct.class);
                startActivity(intent2);
            }
        });
    }
}