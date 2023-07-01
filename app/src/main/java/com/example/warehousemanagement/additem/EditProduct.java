package com.example.warehousemanagement.additem;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.DsSanPham;
import com.example.warehousemanagement.QLMarket;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.TrangChu;
import com.example.warehousemanagement.market.AddMarket;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditProduct extends AppCompatActivity {
    private EditText etName, etCode, etStock, etNote, etProducer, etStatus, etCategory;
    private Button btnSubmit;
    String header ;
    TextView etDate;
    JSONObject jsonObject = new JSONObject();
    String id  ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id");

        }
        setContentView(R.layout.activity_edititem);
        header = DangNhap.account.getToken();
        etName = findViewById(R.id.etName2);
        etCode = findViewById(R.id.etCode2);
        etDate = findViewById(R.id.etDate2);
        etStock = findViewById(R.id.etStock2);
        etNote = findViewById(R.id.etNote2);
        etProducer = findViewById(R.id.etProducer2);
        etStatus = findViewById(R.id.etStatus2);
        etCategory = findViewById(R.id.etCategory2);
        btnSubmit = findViewById(R.id.btnSubmit2);
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
                int stock = Integer.parseInt(etStock.getText().toString().trim());
                String note = etNote.getText().toString().trim();
                String producer = etProducer.getText().toString().trim();
                String status = etStatus.getText().toString().trim();
                String category = etCategory.getText().toString().trim();

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
    private class FetchProductDetails extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("text/plain");
            Request request = new Request.Builder()
                    .url("http://14.225.211.190:4001/api/product/"+ id)
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
                try {
                    // Populate the EditText fields with retrieved data
                    etName.setText(result.getString("name"));
                    etCode.setText(result.getString("code"));
                    etDate.setText(result.getString("date"));
                    etStock.setText(String.valueOf(result.getInt("stock")));
                    etNote.setText(result.getString("note"));
                    etProducer.setText(result.getString("producer"));
                    etStatus.setText(result.getString("status"));
                    etCategory.setText(result.getString("category"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
//                System.out.println(result.toString());
            }
        }
    }

    private class MyAsyncTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(mediaType, params[0]);
            Request request = new Request.Builder()
                    .url("http://14.225.211.190:4001/api/product/"+ id)
                    .addHeader("Authorization", "Bearer " +  header)
                    .method("PUT", requestBody)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                int i =  response.code();
                if (i == 201) {
                    System.out.println(jsonObject);
//                    Intent intent = new Intent(EditProduct.this, TrangChu.class);
//                    startActivity(intent);
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
                Intent intent = new Intent(EditProduct.this, DsSanPham.class);
                startActivity(intent);
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


