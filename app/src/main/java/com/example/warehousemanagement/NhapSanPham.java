package com.example.warehousemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class NhapSanPham extends AppCompatActivity {

    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);
//        btnAdd = findViewById(R.id.btnAdd);
//        btnAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(NhapSanPham.this, ThemSanPham.class);
//                startActivity(intent);
//            }
//        });
    }
}
