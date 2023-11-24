package com.example.warehousemanagement.additem;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.Api;
import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.obj.Product;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditProduct extends AppCompatActivity {
    private EditText etName, etCode, etStock, etNote, etProducer, etStatus, etCategory,etDate;
    private Button btnSubmit;
    String header ;

    JSONObject jsonObject = new JSONObject();
    String id  ;
    Product item ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
//            id = intent.getStringExtra("id");
            item = (Product) getIntent().getSerializableExtra("id");
            id = item.getId();
        }
        setContentView(R.layout.activity_edititem);
        header = DangNhap.account.getToken();
        String storId = DangNhap.account.getStorageId();
        etName = findViewById(R.id.etName2);
        etCode = findViewById(R.id.etCode2);
        etDate = findViewById(R.id.etDate2);
        etStock = findViewById(R.id.etStock2);
        etNote = findViewById(R.id.etNote2);

        etProducer = findViewById(R.id.etProducer2);
        etProducer.setVisibility(View.GONE);

        etStatus = findViewById(R.id.etStatus2);
        etStatus.setVisibility(View.GONE);

        etCategory = findViewById(R.id.etCategory2);
        btnSubmit = findViewById(R.id.btnSubmit2);
        Button btnDelete2 = findViewById(R.id.btnDelete2);
        btnDelete2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DeleteProduct(id);
                    }
                }
        );
        Spinner spinner1 = (Spinner) findViewById(R.id.spinnerCategory);
        List<String> items1 = new ArrayList<>();
        if (spinner1 != null) {
            items1.add("Cake");
            items1.add("Candy");
            items1.add("Meat");
            items1.add("Milk");
            items1.add("Canned Food");
            ArrayAdapter<String> adapterCate = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items1);
            adapterCate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner1.setAdapter(adapterCate);

            // Thiết lập giá trị mặc định
            int defaultPosition = 0; // Vị trí mục mặc định (số thứ tự)
            spinner1.setSelection(defaultPosition);
        } else {
            Log.e("Spinner Error", "Spinner not found or not initialized correctly");
        }
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        new FetchProductDetails().execute(id);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ EditText
                String name = etName.getText().toString().trim();
                String code = etCode.getText().toString().trim();
                float stock = Float.parseFloat(etStock.getText().toString().trim());
                String note = etNote.getText().toString().trim();
                String producer = etProducer.getText().toString().trim();
                String status = etStatus.getText().toString().trim();
                String category = etCategory.getText().toString().trim();
                    String date = etDate.getText().toString().trim();
                // Tạo JSON object từ dữ liệu

                try {
                    jsonObject.put("id",id);
                    jsonObject.put("name", name);
                    jsonObject.put("maSp", code);
                    jsonObject.put("soLuong", stock);
                    jsonObject.put("date", date);
                    jsonObject.put("category", category);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new MyAsyncTask().execute(jsonObject.toString());
            }});

    }
    private void DeleteProduct(String orderId) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(Api.baseURL + "/product/delete/" + orderId  )
                .delete()
                .addHeader("Authorization", "Bearer " + header)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Gọi lại MyAsyncTask để tải lại danh sách đơn hàng
                    EditProduct.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(EditProduct.this,"Xóa thành công", Toast.LENGTH_SHORT).show();
                        }
                    });
                    finish();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                EditProduct.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(EditProduct.this,"Đã có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                    }
                });
                finish();
                e.printStackTrace();
            }
        });
    }
    private class MyAsyncTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(mediaType, params[0]);
            Request request = new Request.Builder()
                    .url(Api.baseURL + "/product/update")
                    .addHeader("Authorization", "Bearer " +  header)
                    .put(requestBody)
                    .build();

            System.out.println("body" +  params[0]);
            try {
                Response response = client.newCall(request).execute();
                int i =  response.code();
                if (response.isSuccessful()) {
                    System.out.println(jsonObject);
                    return  true ;
                } else {
                    EditProduct.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(EditProduct.this,"Đã có lỗi"+ response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    System.out.println("lỗi " + response.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                System.out.println("Thành công");
                EditProduct.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(EditProduct.this,"Sửa thành công", Toast.LENGTH_SHORT).show();
                    }
                });
                finish();
            } else {
                EditProduct.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(EditProduct.this,"Đã có lxooi ", Toast.LENGTH_SHORT).show();
                    }
                });
                System.out.println("lỗi ");
            }
        }
    }
    private void showDatePickerDialog() {
        // Lấy ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Lưu giá trị ngày đã chọn vào TextView
                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                etDate.setText(selectedDate);

                // Gửi giá trị ngày đã chọn lên API
                try {
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = format.parse(selectedDate);
                    long timestamp = date.getTime();
                    jsonObject.put("date", timestamp);
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
        }, year, month, day);

        // Hiển thị DatePickerDialog
        datePickerDialog.show();
    }
    private class FetchProductDetails extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("text/plain");
            Request request = new Request.Builder()
                    .url(Api.baseURL + "/product/"+ id)
                    .addHeader("Authorization", "Bearer " +  header)
                    .method("GET",null)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    return new JSONObject(responseData);
                } else {
                    // Handle error case
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if (result != null) {
                // Populate the EditText fields with retrieved data
                etName.setText(item.getName());
                etCode.setText(item.getMaSp());
                etDate.setText(item.getDate());
                etStock.setText(String.valueOf(item.getSoLuong()));
//                    etNote.setText();
//                    etStatus.setText();
                etCategory.setText(item.getCategory());
//                    etName.setText(result.getString("name"));
//                    etCode.setText(result.getString("maSp"));
//                    etDate.setText(result.getString("date"));
//                    etStock.setText(String.valueOf(result.getInt("soLuong")));
                    etNote.setText(item.getStorageId());
//                    etStatus.setText(result.getString("status"));
//                    etCategory.setText(result.getString("category"));
            } else {
//                System.out.println(result.toString());
            }
        }
    }

}


