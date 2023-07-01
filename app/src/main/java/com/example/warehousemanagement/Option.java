package com.example.warehousemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.warehousemanagement.additem.DsSanPham;
import com.example.warehousemanagement.storage.QLStorage;

public class Option extends AppCompatActivity {

    ImageView imgSaler, imgStocker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        imgSaler = findViewById(R.id.imgSaler);
        imgStocker = findViewById(R.id.imgStocker);

        imgSaler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Option.this, QLStorage.class);
                startActivity(intent);
            }
        });

        imgStocker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Option.this, DsSanPham.class);
                startActivity(intent1);
            }
        });
    }
}