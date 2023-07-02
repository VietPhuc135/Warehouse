package com.example.warehousemanagement.order;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.additem.AddProduct;
import com.example.warehousemanagement.additem.ArrayProduct;
import com.example.warehousemanagement.obj.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DsSanPhamOrder extends AppCompatActivity {
    String header;
    private ListView listView;
    private ArrayProductInOrder adapter;
    private List<Product> itemList = new ArrayList<>(); // Khởi tạo danh sách sản phẩm

    ImageView imgAddProduct;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        imgAddProduct = findViewById(R.id.imgAddProduct);
        listView = findViewById(R.id.lvProduct);
        header = DangNhap.account.getToken();
        Intent intent = getIntent();


        adapter = new ArrayProductInOrder(this, itemList); // Khởi tạo adapter và gán danh sách sản phẩm
        listView.setAdapter(adapter); // Gán adapter cho listView

        ArrayList<Product> lineItems = (ArrayList<Product>) intent.getSerializableExtra("lineItems");
        updateProductList(lineItems); // Cập nhật danh sách sản phẩm từ Intent
        System.out.println("lineItems" + lineItems);

    }

    public void updateProductList(ArrayList<Product> products) {
        itemList.clear(); // Xóa danh sách sản phẩm hiện tại
        itemList.addAll(products); // Thêm các sản phẩm mới vào danh sách
        adapter.notifyDataSetChanged(); // Thông báo cho adapter cập nhật hiển thị
    }
}
