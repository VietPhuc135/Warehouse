package com.example.warehousemanagement.other;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.additem.DsSanPham;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.TrangChu;
import com.example.warehousemanagement.order.OrderList;
import com.example.warehousemanagement.profile.ProfilePage;
import com.example.warehousemanagement.storage.StaticProduct;

public class StockerPage extends  AppCompatActivity{
    Button btnDN, btnDangki;
    ImageView imgImport, imgProFile, imgProduct, imgUser;
    String idStor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_stocker);
            idStor = DangNhap.account.getStorageId() != null ? DangNhap.account.getStorageId()   : " ";
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
                Intent intent2 = new Intent(StockerPage.this, StaticProduct.class);
                intent2.putExtra("idSto",idStor);
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

    }

}
