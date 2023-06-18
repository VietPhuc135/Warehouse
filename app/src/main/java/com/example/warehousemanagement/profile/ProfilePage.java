package com.example.warehousemanagement.profile;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.obj.Account;
import com.example.warehousemanagement.obj.User;

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
    }
}
