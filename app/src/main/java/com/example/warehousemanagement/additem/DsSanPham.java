package com.example.warehousemanagement.additem;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.Api;
import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.obj.CountByType;
import com.example.warehousemanagement.obj.Product;
import com.example.warehousemanagement.order.AddOrder;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DsSanPham extends AppCompatActivity {
    String header;
    private ListView listView;
    private ArrayProduct adapter;
    private List<Product> itemList;
    private List<Product> filteredData;
    ImageView imgAddProduct,imaArrange;
    AutoCompleteTextView edtSearchProduct;
    Context context;
        String id;
        String role;
    String storageId;
    Spinner sortSpinner;
    PieChart piechart;
    String type = "MEAT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        piechart = findViewById(R.id.pieChart);
        imgAddProduct = findViewById(R.id.imgAddProduct);
        imaArrange = findViewById(R.id.imgArrageProduct);
        edtSearchProduct = findViewById(R.id.searchProduct);
        listView = findViewById(R.id.lvProduct);
        header = DangNhap.account.getToken();
        role = DangNhap.account.getRole();
        storageId = DangNhap.account.getStorageId();
        sortSpinner = findViewById(R.id.sortSpinner);

//        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
//                this, R.array.sort_options, android.R.layout.simple_spinner_item);
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        sortSpinner.setAdapter(spinnerAdapter);

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

            ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                    this, R.array.sort_options, android.R.layout.simple_spinner_item);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sortSpinner.setAdapter(spinnerAdapter);

            sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // Xử lý sự kiện sắp xếp danh sách khi chọn mục trong Spinner
                    String selectedItem = parentView.getItemAtPosition(position).toString();
                    if (selectedItem.equals(getString(R.string.sort_by_Cake))) {
                        type = "CAKE";

                    } else if (selectedItem.equals(getString(R.string.sort_by_Candy))) {
                        type = "CANDY";
                    }
                    if (selectedItem.equals(getString(R.string.sort_by_MEAT))) {
                        type = "MEAT";
                    } else if (selectedItem.equals(getString(R.string.sort_by_MILK))) {
                        type = "MILK";
                    }
                    else if (selectedItem.equals(getString(R.string.sort_by_CannedFood))) {
                        type = "CANNED FOOD";
                    }
                    onRestart();

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // Không có hành động cụ thể khi không chọn mục nào
                }
            });
        }

        /*edtSearchProduct.addTextChangedListener(new TextWatcher() {
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
//<<<<<<< Updated upstream
        });*/

//=======
//        });
        new LoadDataPieTask(piechart).execute();
//>>>>>>> Stashed changes
        new MyAsyncTask().execute();

        // Thiết lập adapter cho AutoCompleteTextView
        ArrayAdapter<Product> autoCompleteAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, itemList);
        edtSearchProduct.setAdapter(autoCompleteAdapter);

        // Xử lý sự kiện chọn mục trong AutoCompleteTextView
        edtSearchProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Xử lý sự kiện khi chọn một mục từ danh sách gợi ý
                Product selectedProduct = (Product) parent.getItemAtPosition(position);
                // Hiển thị thông tin sản phẩm hoặc thực hiện hành động mong muốn
                Toast.makeText(DsSanPham.this, selectedProduct.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện thay đổi văn bản trong AutoCompleteTextView để lọc danh sách
        edtSearchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Không cần xử lý trước sự thay đổi văn bản
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Lọc danh sách sản phẩm theo văn bản nhập vào
                adapter.getFilter().filter(charSequence.toString());
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Không cần xử lý sau sự thay đổi văn bản
            }
        });


    }

    /*private void filterData(String query) {
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
    }*/

    private void toggleSpinnerVisibility() {
        // Toggle visibility of the Spinner
        int visibility = sortSpinner.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
        sortSpinner.setVisibility(visibility);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        new MyAsyncTask().execute();
        new LoadDataPieTask(piechart).execute();
    }

    public static class LoadDataPieTask extends AsyncTask<Void, Void, List<CountByType>> {
        @SuppressLint("StaticFieldLeak")
        private final PieChart pieChart;

        public LoadDataPieTask(PieChart pieChart) {
            this.pieChart = pieChart;
        }

        @Override
        protected List<CountByType> doInBackground(Void... voids) {
            try {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("text/plain");
                RequestBody body = RequestBody.create(mediaType, "");
                Request request = new Request.Builder()
                        .url(Api.baseURL + "/product/list-count-by-type")
                        .method("GET", null)
                        .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdG9ja2VyIiwiaWF0IjoxNzA0NjE2Njg5LCJleHAiOjE3MDUxNDIyODl9.EF6KKkuVeGVkkZUYeeWH4Mzm2Cp83Mi0Qs9HtueefZk")
                        .build();
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<CountByType>>() {}.getType();
                    return gson.fromJson(responseBody, listType);
                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<CountByType> pieaDataList) {
            if (pieaDataList != null) {
                List<PieEntry> entries = new ArrayList<>();
                for (CountByType product : pieaDataList) {
                    entries.add(new PieEntry(product.getSoLuong(), product.getType()));
                }

                PieDataSet dataSet = new PieDataSet(entries, "Sản phẩm");
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                PieData pieData = new PieData(dataSet);
                pieChart.setData(pieData);
                pieChart.invalidate();
            } else {
                // Xử lý khi có lỗi
            }
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class MyAsyncTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("text/plain");

            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("type",type)
                    .build();
            Request request = new Request.Builder()
                    .url(Api.baseURL + "/product/getlist")
                    .method("POST", body)
                    .addHeader("Authorization", "Bearer "+header)
                    .build();
            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Product>>() {
                        }.getType();
                        itemList = gson.fromJson(responseBody, listType);
                        System.out.println("Đây la itemlist " + responseBody);

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


            } else {
                System.out.println("lỗi ");
            }
        }
    }

}