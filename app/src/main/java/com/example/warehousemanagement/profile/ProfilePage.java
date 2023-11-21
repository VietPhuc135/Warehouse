package com.example.warehousemanagement.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.MainActivity;
import com.example.warehousemanagement.QLStore;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.TrangChu;
import com.example.warehousemanagement.user.SaveLogin;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfilePage extends AppCompatActivity {
    TextView idname, idemail, idphone, idAddress;
    String name,role;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);
        TextView idrole = findViewById(R.id.role);
        idname = (TextView) findViewById(R.id.IdName);
        idemail = (TextView) findViewById(R.id.idEmail);
        idphone = (TextView) findViewById(R.id.idPhone);
        idAddress = (TextView) findViewById(R.id.idAddress);
        Button btnDangXuat = findViewById(R.id.btnDangXuat);
        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfilePage.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                SaveLogin saveLogin = new SaveLogin(getApplicationContext());
                saveLogin.clearSession();
            }
        });
        name = DangNhap.account.getName();
        role = DangNhap.account.getRole();
        System.out.println(name);
        idrole.setText(role);
        idname.setText(name);
        idemail.setText(DangNhap.account.getEmail());
        idphone.setText("123 678 999");
        idAddress.setText("Ha Noi");
        //idAddress.setText(DangNhap.account.getUser().getAddress());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (!role.equals("ADMIN")){
            bottomNavigationView.setVisibility(View.GONE);
        }
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
