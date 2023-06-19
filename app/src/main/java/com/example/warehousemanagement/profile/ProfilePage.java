package com.example.warehousemanagement.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.QLStore;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.TrangChu;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfilePage extends AppCompatActivity {
    TextView idname,idemail,idphone,idAddress;
    String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);
        idname = (TextView) findViewById(R.id.IdName);
        idemail = (TextView) findViewById(R.id.idEmail);
        idphone = (TextView) findViewById(R.id.idPhone);
        idAddress = (TextView) findViewById(R.id.idAddress);

        name = DangNhap.account.getUser().getName();
        System.out.println(name);
        idname.setText(name);
        idemail.setText(DangNhap.account.getUser().getEmail());
        idphone.setText("123 678 999");
        idAddress.setText(DangNhap.account.getUser().getAddress());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.info_app);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.info_app:
                        return true;
                    case R.id.list_apps:
                        startActivity(new Intent(getApplicationContext(),
                                QLStore.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home_apps:
                        startActivity(new Intent(getApplicationContext(),
                                TrangChu.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }
}
