package com.example.warehousemanagement.other;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.DsSanPham;
import com.example.warehousemanagement.QLStore;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.TrangChu;
import com.example.warehousemanagement.order.OrderList;
import com.example.warehousemanagement.profile.ProfilePage;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StockerPage extends  AppCompatActivity{
    Button btnDN, btnDangki;
    ImageView imgImport, imgProFile, imgProduct, imgUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_stocker);

        imgProduct = findViewById(R.id.imgProduct);
        imgUser = findViewById(R.id.imgUser);
        imgProFile = findViewById(R.id.imgProFile);
        imgProFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StockerPage.this,
                        ProfilePage.class));
            }
        });
        imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(StockerPage.this, DsSanPham.class);
                startActivity(intent2);
            }
        });

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StockerPage.this, OrderList.class);
                startActivity(intent);
            }
        });

//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setSelectedItemId(R.id.home_apps);
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.home_apps:
//
//                        return true;
////                    case R.id.list_apps:
////                        startActivity(new Intent(StockerPage.this,
////                                QLStore.class));
////                        overridePendingTransition(0, 0);
////                        return true;
//                    case R.id.info_app:
//                        startActivity(new Intent(StockerPage.this,
//                                ProfilePage.class));
//                        overridePendingTransition(0, 0);
//                        return true;
//                }
//                return false;
//            }
//        });
    }

}
