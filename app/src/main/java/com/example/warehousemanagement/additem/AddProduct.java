package com.example.warehousemanagement.additem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.TrangChu;

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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);

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
            items1.add("Bánh");
            items1.add("Kẹo");
            items1.add("Thịt");
            items1.add("Sữa");
            items1.add("Đồ đóng hộp");
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
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Lấy dữ liệu từ EditText
                        String name = etName.getText().toString().trim();
                        String code = etCode.getText().toString().trim();
                        int stock = Integer.parseInt(etStock.getText().toString().trim());
                        String note = etNote.getText().toString().trim();
                        String producer = etProducer.getText().toString().trim();
                        String status = spinner.getSelectedItem().toString();
                        String category =spinner1.getSelectedItem().toString();

                        // Tạo JSON object từ dữ liệu

                        try {
                            jsonObject.put("name", name);
                            jsonObject.put("code", code);
                            jsonObject.put("stock", stock);
                            jsonObject.put("note", note);
                            jsonObject.put("producer", producer);
                            jsonObject.put("status", status);
                            jsonObject.put("category", category);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new MyAsyncTask().execute(jsonObject.toString());
                    }});
            }
    private class MyAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(mediaType, params[0]);
            Request request = new Request.Builder()
                    .url("http://14.225.211.190:4001/api/product")
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
                finish();
            } else {
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


