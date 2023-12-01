package com.example.warehousemanagement;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.additem.DsSanPham;
import com.example.warehousemanagement.order.OrderList;
import com.example.warehousemanagement.profile.ProfilePage;
import com.example.warehousemanagement.user.QLNguoiDung;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TrangChu extends AppCompatActivity {

    Button btnDN, btnDangki;
    ImageView imgImport, imgExport, imgProduct, imgUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        imgImport = findViewById(R.id.imgImport);

        imgProduct = findViewById(R.id.imgProduct);
        imgUser = findViewById(R.id.imgUser);

        imgImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrangChu.this, OrderList.class);
                startActivity(intent);
            }
        });


        imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(TrangChu.this, DsSanPham.class);
                startActivity(intent2);
            }
        });

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(TrangChu.this, QLNguoiDung.class);
                startActivity(intent3);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home_apps);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_apps:
                        return true;
                    case R.id.list_apps:
                        startActivity(new Intent(getApplicationContext(),

                                QLStore.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.info_app:
                        startActivity(new Intent(getApplicationContext(),
                                ProfilePage.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }
}