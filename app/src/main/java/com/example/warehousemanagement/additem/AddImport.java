package com.example.warehousemanagement.additem;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.TrangChu;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AddImport extends AppCompatActivity {
    private EditText etName, etCode, etDate, etStock, etNote, etProducer, etStatus, etCategory;
    private Button btnSubmit;
    String header = DangNhap.account.getToken();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);

                etName = findViewById(R.id.etName);
                etCode = findViewById(R.id.etCode);
                etDate = findViewById(R.id.etDate);
                etStock = findViewById(R.id.etStock);
                etNote = findViewById(R.id.etNote);
                etProducer = findViewById(R.id.etProducer);
                etStatus = findViewById(R.id.etStatus);
                etCategory = findViewById(R.id.etCategory);
                btnSubmit = findViewById(R.id.btnSubmit);

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Lấy dữ liệu từ EditText
                        String name = etName.getText().toString().trim();
                        String code = etCode.getText().toString().trim();
                        int date = Integer.parseInt(etDate.getText().toString().trim());
                        int stock = Integer.parseInt(etStock.getText().toString().trim());
                        String note = etNote.getText().toString().trim();
                        String producer = etProducer.getText().toString().trim();
                        String status = etStatus.getText().toString().trim();
                        String category = etCategory.getText().toString().trim();

                        // Tạo JSON object từ dữ liệu
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("name", name);
                            jsonObject.put("code", code);
                            jsonObject.put("date", date);
                            jsonObject.put("stock", stock);
                            jsonObject.put("note", note);
                            jsonObject.put("producer", producer);
                            jsonObject.put("status", status);
                            jsonObject.put("category", category);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // Gửi yêu cầu HTTP POST
                        OkHttpClient client = new OkHttpClient();
                        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                        RequestBody requestBody = RequestBody.create(mediaType, jsonObject.toString());
                        Request request = new Request.Builder()
                                .url("http://14.225.211.190:4001/api/product")
                                .addHeader("Authorization", "Bearer" +  header)
                                .post(requestBody)
                                .build();

                        try {
                            Response response = client.newCall(request).execute();
                            // Xử lý phản hồi từ máy chủ (response)
                            if (response.isSuccessful()) {
                                System.out.println(jsonObject);
                                Intent intent = new Intent(AddImport.this, TrangChu.class);
                                startActivity(intent);
                            } else {
                                System.out.println("lỗi ");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }


