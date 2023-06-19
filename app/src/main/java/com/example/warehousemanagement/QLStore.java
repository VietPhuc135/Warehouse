package com.example.warehousemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.warehousemanagement.profile.ProfilePage;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class QLStore extends AppCompatActivity {

    ImageView imgStorage, imgMarket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_management);

        imgMarket = findViewById(R.id.imgMarket);
        imgStorage = findViewById(R.id.imgStorage);

        imgMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QLStore.this, QLMarket.class);
                startActivity(intent);
            }
        });

        imgStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(QLStore.this, QLStorage.class);
                startActivity(intent1);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.list_apps);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.list_apps:
                        return true;
                    case R.id.home_apps:
                        startActivity(new Intent(getApplicationContext(),
                                TrangChu.class));
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