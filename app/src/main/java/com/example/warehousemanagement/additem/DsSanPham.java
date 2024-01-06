package com.example.warehousemanagement.additem;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.Api;
import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.obj.Product;
import com.example.warehousemanagement.order.AddOrder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DsSanPham extends AppCompatActivity {
    String header;
    private ListView listView;
    private ArrayProduct adapter;
    //    private ArrayAdapter<Product> adapter;
    private List<Product> itemList;
    private List<Product> filteredData;
    ImageView imgAddProduct,imaArrange;
    AutoCompleteTextView edtSearchProduct;
    Context context;
        String id;
        String role,dsSanPham;
        String storageId;
    RequestBody body,body1;
    Spinner sortSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        imgAddProduct = findViewById(R.id.imgAddProduct);
        imaArrange = findViewById(R.id.imgArrageProduct);
        edtSearchProduct = findViewById(R.id.searchProduct);
        listView = findViewById(R.id.lvProduct);
        header = DangNhap.account.getToken();
        role = DangNhap.account.getRole();
        storageId = DangNhap.account.getStorageId();
        sortSpinner = findViewById(R.id.sortSpinner);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.sort_options, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(spinnerAdapter);

        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id");
        }
        adapter = new ArrayProduct(this, itemList);
        if (role.equals("STOCKER")){
            storageId = intent.getStringExtra("idSto");
        }
        if (role.equals("SALER")){
            imgAddProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DsSanPham.this, AddOrder.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            });
            imaArrange.setVisibility(View.GONE);
        }else{
            imgAddProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DsSanPham.this, AddProduct.class);
                    startActivity(intent);
                }
            });

            imaArrange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Toggle visibility of the Spinner
                    toggleSpinnerVisibility();
                }
            });

            // Set up item selected listener for Spinner
            /*sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    // Handle sorting logic here based on the selected option
                    String selectedOption = adapterView.getItemAtPosition(position).toString();
                    sortPhoneNames(selectedOption);
                    // Refresh the ListView accordingly
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    // Do nothing
                }
            });*/

            sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // Xử lý sự kiện sắp xếp danh sách khi chọn mục trong Spinner
                    String selectedItem = parentView.getItemAtPosition(position).toString();
                    if (selectedItem.equals(getString(R.string.sort_by_name))) {
                        // Sắp xếp theo tên sản phẩm
                        Collections.sort(itemList, new Comparator<Product>() {
                            @Override
                            public int compare(Product p1, Product p2) {
                                return p1.getName().compareToIgnoreCase(p2.getName());
                            }
                        });
                    } else if (selectedItem.equals(getString(R.string.sort_by_type))) {
                        // Sắp xếp theo loại sản phẩm
                        Collections.sort(itemList, new Comparator<Product>() {
                            @Override
                            public int compare(Product p1, Product p2) {
                                return p1.getCategory().compareToIgnoreCase(p2.getCategory());
                            }
                        });
                    }
                    // Cập nhật lại ListView sau khi sắp xếp
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // Không có hành động cụ thể khi không chọn mục nào
                }
            });
        }

        edtSearchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Filter the data based on the entered text
                filterData(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Do nothing
            }
        });

        new MyAsyncTask().execute();
    }

    private void filterData(String query) {
        if (filteredData == null) {
            // Nếu chưa khởi tạo, hãy tạo một ArrayList mới
            filteredData = new ArrayList<>();
        } else {
            // Nếu đã khởi tạo, tiến hành làm sạch danh sách
            filteredData.clear();

            // If the query is empty, show all original data
            if (query.isEmpty()) {
                filteredData.addAll(itemList);
            } else {
                // Filter data based on the query
                for (Product item : itemList) {
                    if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                        filteredData.add(item);
                    }
                }
            }
        }

        // Notify the adapter that the data has changed
        adapter.notifyDataSetChanged();
    }

    private void toggleSpinnerVisibility() {
        // Toggle visibility of the Spinner
        int visibility = sortSpinner.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
        sortSpinner.setVisibility(visibility);
    }

    private void sortPhoneNames(String selectedOption) {
        // Handle sorting logic based on the selected option
        if ("Sort by Category".equals(selectedOption)) {
            // Implement sorting by date logic
            // For example, sort alphabetically for simplicity in this example
            Collections.sort(itemList);
        } else if ("Sort by Name".equals(selectedOption)) {
            // Implement sorting by name logic
            // For example, reverse the order for simplicity in this example
            ProductComparator productComparator = new ProductComparator();
            Collections.sort(itemList, productComparator);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        new MyAsyncTask().execute();

    }

    private class MyAsyncTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("text/plain");
            Request request = new Request.Builder()
                    .url(Api.baseURL + "/product/getlist")
                    .method("GET",null)
                    .addHeader("Authorization", "Bearer " + header)
                    .build();
            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Product>>() {
                        }.getType();
                        itemList = gson.fromJson(responseBody, listType);
                        System.out.println("Đây la itemlist " + responseBody);

//                        List<Product> filteredList = new ArrayList<>();
//                        if (id != null){
//                            for (Product product : itemList) {
//                                String idSto = product.getStorageId();
//                                if (product.getStorageId().equals(id)) {
//                                    filteredList.add(product);
//                                }
//                            }
//                        }
                        // Cập nhật giao diện trong luồng UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Khởi tạo và thiết lập Adapter
                                adapter = new ArrayProduct(DsSanPham.this, itemList);
                                listView.setAdapter(adapter);
                            }
                        });
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

}