package com.example.warehousemanagement.other;

import android.os.AsyncTask;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import androidx.annotation.NonNull;

import com.example.warehousemanagement.Api;
import com.example.warehousemanagement.additem.ArrayProduct;
import com.example.warehousemanagement.additem.DsSanPham;
import com.example.warehousemanagement.obj.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyAsyncTask extends AsyncTask<String, Void, Boolean> {
    @Override
    protected Boolean doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("text/plain");
        Request request = new Request.Builder()
                .url(Api.baseURL + "/product/getlist")
                .method("GET",null)
                .addHeader("Authorization", "Bearer " )
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Product>>() {
                    }.getType();
                    List<Product> itemList;
                    itemList = gson.fromJson(responseBody, listType);
                    System.out.println("Đây la itemlist " + responseBody);
                    // Chuyển đổi danh sách từ Product sang ParaItem
                    List<ParaItem> paraItemList = convertProductListToParaItemList(itemList);

                    // In danh sách mới có ParaItem
                    for (ParaItem item : paraItemList) {
                        System.out.println("Id: " + item.getId() + ", Name: " + item.getName());
                    }

                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("fail");
                e.printStackTrace();
            }
        });
        return false;
    }

    private List<ParaItem> convertProductListToParaItemList(List<Product> productList) {
        List<ParaItem> paraItemList = new ArrayList<>();

        for (Product product : productList) {
            ParaItem paraItem = new ParaItem();
            paraItem.setId(product.getId());
            paraItem.setName(product.getName());
            paraItemList.add(paraItem);
        }

        return paraItemList;
    }
    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
//                System.out.println(jsonObject);
//                Intent intent = new Intent(AddStorage.this, QLStorage.class);
//                startActivity(intent);
        } else {
            System.out.println("lỗi ");
        }
    }

}

