package com.example.warehousemanagement.additem;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.Api;
import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddProduct extends AppCompatActivity {
    private EditText etName, etCode, etStock, etNote, etProducer ;
    private Button btnSubmit;
    String header ;
    TextView etDate,etStatus,etCategory;
    JSONObject jsonObject = new JSONObject();
    private ArrayProduct adapter ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.Base_Theme_AppCompat_Dialog);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);
        String storId = DangNhap.account.getStorageId();
        header = DangNhap.account.getToken();
                etName = findViewById(R.id.etName);
                etCode = findViewById(R.id.etCode);
                etDate = findViewById(R.id.etDate);
                etStock = findViewById(R.id.etStock);
                etNote = findViewById(R.id.etNote);
                etProducer = findViewById(R.id.etProducer);
                etStatus = findViewById(R.id.etStatus);
                etCategory = findViewById(R.id.etCategory);
                btnSubmit = findViewById(R.id.btnSubmit);
                    etProducer.setVisibility(View.GONE);
        Spinner spinner = (Spinner) findViewById(R.id.spinnerStatus);
        List<String> items = new ArrayList<>();
        if (spinner != null) {
            items.add("Available");
            items.add("Pending");
            ArrayAdapter<String> adapterSta = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
            adapterSta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapterSta);

            // Thiết lập giá trị mặc định
            int defaultPosition = 0; // Vị trí mục mặc định (số thứ tự)
            spinner.setSelection(defaultPosition);
        } else {
            Log.e("Spinner Error", "Spinner not found or not initialized correctly");
        }

        Spinner spinner1 = (Spinner) findViewById(R.id.spinnerCategory);
        List<String> items1 = new ArrayList<>();
        if (spinner1 != null) {
            items1.add("CAKE");
            items1.add("CANDY");
            items1.add("MEAT");
            items1.add("MILK");
            items1.add("CANNED FOOD");
            ArrayAdapter<String> adapterCate = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items1);
            adapterCate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner1.setAdapter(adapterCate);

            // Thiết lập giá trị mặc định
            int defaultPosition = 0; // Vị trí mục mặc định (số thứ tự)
            spinner1.setSelection(defaultPosition);
        } else {
            Log.e("Spinner Error", "Spinner not found or not initialized correctly");
        }

        etDate.setOnClickListener(v -> showDatePickerDialog());
                btnSubmit.setOnClickListener(v -> {
                    // Lấy dữ liệu từ EditText
                    String name = etName.getText().toString().trim();
                    String code = etCode.getText().toString().trim();
                    int stock = Integer.parseInt(etStock.getText().toString().trim());
                    String note = etNote.getText().toString().trim();
//                        String producer = etProducer.getText().toString().trim();
                    String status = spinner.getSelectedItem().toString();
                    String category =spinner1.getSelectedItem().toString();

                    // Tạo JSON object từ dữ liệu

                    try {
                        jsonObject.put("name", name);
                        jsonObject.put("maSp", code);
                        jsonObject.put("storageId", storId);
                        jsonObject.put("soLuong", stock);
                        jsonObject.put("date", note);
                        jsonObject.put("category", category);
                        jsonObject.put("type",category);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new MyAsyncTask().execute(jsonObject.toString());
                });
            }
    private class MyAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(mediaType, params[0]);
            Request request = new Request.Builder()
                    .url(Api.baseURL + "/product/add")
                    .addHeader("Authorization", "Bearer " +  header)
                    .method("POST", requestBody)
                    .build();


            try {
                Response response = client.newCall(request).execute();
                   int i =  response.code();
                if (response.isSuccessful()) {
                    System.out.println(jsonObject);
                    return true;
                } else {
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
                System.out.println(jsonObject);
                AddProduct.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddProduct.this,"Thêm thành công", Toast.LENGTH_SHORT).show();
//                        adapter.notifyDataSetChanged();
                    }
                });

                finish();
            } else {
                AddProduct.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddProduct.this,"Thêm không thành công!!! ", Toast.LENGTH_SHORT).show();
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

}


