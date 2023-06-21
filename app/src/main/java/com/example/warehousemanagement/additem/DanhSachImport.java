package com.example.warehousemanagement.additem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.R; import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warehousemanagement.obj.Product;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class DanhSachImport extends AppCompatActivity {

        private RecyclerView recyclerView;
        private ProductAdapter productAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_view_product);

            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Sample JSON Array (replace with your API response)
            String jsonArray = "[{\"id\":\"3\",\"createdAt\":\"2023-06-18T16:12:32.917Z\",\"updatedAt\":\"2023-06-19T05:02:15.000Z\",\"deletedAt\":null,\"name\":\"Product 1\",\"code\":\"P_01\",\"date\":\"2023-06-20T23:12:26.000Z\",\"stock\":103,\"note\":null,\"producer\":null,\"status\":\"available\",\"storageId\":\"1\",\"category\":\"12\",\"image\":\"http://res.cloudinary.com/di1zqwo8y/image/upload/v1687150934/squg9wnbamwr2s3mf5g8.jpg\"},{\"id\":\"4\",\"createdAt\":\"2023-06-19T01:52:38.800Z\",\"updatedAt\":\"2023-06-20T13:19:48.475Z\",\"deletedAt\":null,\"name\":null,\"code\":\"PRO-3\",\"date\":\"2023-06-30T00:00:00.000Z\",\"stock\":10,\"note\":null,\"producer\":null,\"status\":\"available\",\"storageId\":\"1\",\"category\":null,\"image\":\"http://res.cloudinary.com/di1zqwo8y/image/upload/v1687149939/mq0i9dkh3mjzkgsvdejl.jpg\"},{\"id\":\"5\",\"createdAt\":\"2023-06-19T01:53:26.905Z\",\"updatedAt\":\"2023-06-20T13:19:47.897Z\",\"deletedAt\":null,\"name\":null,\"code\":\"PRO-4\",\"date\":\"2023-06-30T00:00:00.000Z\",\"stock\":10,\"note\":null,\"producer\":null,\"status\":\"available\",\"storageId\":\"1\",\"category\":null,\"image\":\"http://res.cloudinary.com/di1zqwo8y/image/upload/v1687149939/mq0i9dkh3mjzkgsvdejl.jpg\"}]";

            // Convert JSON Array to List of Product objects
            Gson gson = new Gson();
            List<Product> productList = Arrays.asList(gson.fromJson(jsonArray, Product[].class));

            // Set up the adapter
            productAdapter = new ProductAdapter(this, productList);
            recyclerView.setAdapter(productAdapter);

        }
    }
