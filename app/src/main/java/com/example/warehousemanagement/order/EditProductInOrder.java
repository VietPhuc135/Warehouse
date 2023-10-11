package com.example.warehousemanagement.order;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.obj.Product;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EditProductInOrder extends AppCompatActivity {
    private EditText etName, etCode, etStock, etNote, etProducer, etStatus, etCategory,etDate;
    private Button btnSubmit;
    JSONObject jsonObject = new JSONObject();
    String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_edititem);
        etName = findViewById(R.id.etName2);
        etCode = findViewById(R.id.etCode2);

        etStock = findViewById(R.id.etStock2);
        etNote = findViewById(R.id.etNote2);
        etProducer = findViewById(R.id.etProducer2);
        etStatus = findViewById(R.id.etStatus2);
        etCategory = findViewById(R.id.etCategory2);
        btnSubmit = findViewById(R.id.btnSubmit2);
        etDate =  findViewById(R.id.etDate2);
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        if (intent != null) {
          Product lineItems = (Product) intent.getSerializableExtra("item");
            setValuesFromLineItems(lineItems);
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy thông tin sản phẩm từ các EditText
                String name = etName.getText().toString();
                String code = etCode.getText().toString();
                String date = etDate.getText().toString();
                float stock = Float.parseFloat(etStock.getText().toString());
                String note = etNote.getText().toString();
                String producer = etProducer.getText().toString();
                String status = etStatus.getText().toString();
                String category = etCategory.getText().toString();

                // Tạo đối tượng Product mới với thông tin đã chỉnh sửa
                Product updatedProduct = new Product();
                updatedProduct.setId(id);
                updatedProduct.setName(name);
//                updatedProduct.setCode(code);
//                updatedProduct.setDate(date);
//                updatedProduct.setStock(stock);
//                updatedProduct.setNote(note);
//                updatedProduct.setProducer(producer);
//                updatedProduct.setStatus(status);
                updatedProduct.setCategory(category);

                // Gửi thông tin sản phẩm đã chỉnh sửa trở lại DsSanPhamOrder
                Intent intent = new Intent(EditProductInOrder.this, DsSanPhamOrder.class);
                intent.putExtra("updatedProduct", updatedProduct);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


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

    private void setValuesFromLineItems(Product product) {
        if (product != null ) {
            String dateTimeString = product.getDate();
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date date = null;
            try {
                date = inputFormat.parse(dateTimeString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Format lại chỉ có ngày
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = outputFormat.format(date);

            // Lấy phần tử đầu tiên từ danh sách lineItems (có thể điều chỉnh tùy vào logic của bạn)
            etName.setText(product.getName() != null ? product.getName() : "");
//            etCode.setText(product.getCode() != null ? product.getCode() : "");
//            etDate.setText(product.getDate() != null ? formattedDate : "");
//            etStock.setText(product.getStock() != 0 ? String.valueOf(product.getStock()) : "");
//            etNote.setText(product.getNote() != null ? product.getNote() : "");
//            etProducer.setText(product.getProducer() != null ? product.getProducer() : "");
//            etStatus.setText(product.getStatus() != null ? product.getStatus() : "");
            etCategory.setText(product.getCategory() != null ? product.getCategory() : "");
        }
    }


}
