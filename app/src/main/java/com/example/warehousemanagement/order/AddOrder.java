package com.example.warehousemanagement.order;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.Api;
import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.obj.LineItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddOrder extends AppCompatActivity {
    private EditText etName, etStock, etNote, etProducer, etStatus, etCategory,tvMarketIdE;
    private Button btnSubmit;
    String header, ownerIdacc, storageId, marketId;
    TextView etDate;
    private LineItemForm lineItemForm;
    JSONObject jsonObject = new JSONObject();
    JSONObject orderJson = new JSONObject();
    private List<LineItem> lineItems;
    private LineItemAdapter adapter;
    private ListView listViewLineItems;
    private static final int REQUEST_CODE_LINE_ITEM_FORM = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderadd);
        header = DangNhap.account.getToken();
        ownerIdacc = DangNhap.account.getId();
        Intent intent = getIntent();
        if (intent != null) {
            storageId = intent.getStringExtra("id");
        }
        marketId = DangNhap.account.getMarketId();
//        etName = findViewById(R.id.etOwnerIde);
      LinearLayout etCode = findViewById(R.id.tvLineItems);
        etStock = findViewById(R.id.tvMessageE);
        etNote = findViewById(R.id.tvStorageIdE);
        etStatus = findViewById(R.id.tvFlagMessageE);
        btnSubmit = findViewById(R.id.btnSubmitOrder);
        tvMarketIdE = findViewById(R.id.tvMarketIdE);
        listViewLineItems =findViewById(R.id.listViewLineItems);
        EditText etLineItemse = findViewById(R.id.etLineItemse);

        tvMarketIdE.setVisibility(View.GONE);
      etNote.setVisibility(View.GONE);
            etNote.setText(storageId);
        tvMarketIdE.setText(marketId);
        lineItems = new ArrayList<>();
        adapter = new LineItemAdapter(this, lineItems,this);
        listViewLineItems.setAdapter(adapter);

        listViewLineItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle the long click event here
                removeLineItem(position);
                return true;
            }
        });

        // Xử lý sự kiện khi nhấn nút "Add Line Item"
        etCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLineItemForm();
            }
        });
        etLineItemse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLineItemForm();
            }
        });


        // Xử lý sự kiện khi nhấn nút "Submit"
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitOrder();
            }
        });
    }
    public void removeLineItem(int position) {
        lineItems.remove(position);
        adapter.notifyDataSetChanged();
    }


    private void showLineItemForm() {
        Intent intent = new Intent(AddOrder.this, LineItemForm.class);
        startActivityForResult(intent, REQUEST_CODE_LINE_ITEM_FORM);
    }

    // Phương thức để gửi đơn hàng lên API
    private void submitOrder() {
        // Lấy thông tin từ các trường nhập liệu
        String ownerId = ownerIdacc;

        // Kiểm tra dữ liệu đã nhập
        if (ownerId.isEmpty() || lineItems.isEmpty()) {
            Toast.makeText(this, "Hãy nhập đủ", Toast.LENGTH_SHORT).show();
            return;
        }
        // Tạo JSON object để lưu thông tin đơn hàng
        try {
//            orderJson.put("ownerId", ownerId);
            JSONArray lineItemsJson = new JSONArray();
            for (LineItem lineItem : lineItems) {
                JSONObject lineItemJson = new JSONObject();
                lineItemJson.put("id", lineItem.getId());
                lineItemJson.put("soLuong", lineItem.getsoLuong());
                lineItemsJson.put(lineItemJson);
            }
            orderJson.put("lineItems", lineItemsJson);
            orderJson.put("option", "default");
            orderJson.put("message", JSONObject.NULL);
            orderJson.put("storageId", storageId);
            orderJson.put("marketId", marketId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new MyAsyncTask().execute(orderJson.toString());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LINE_ITEM_FORM && resultCode == RESULT_OK && data != null) {
            // Retrieve the LineItem object from the result
            LineItem lineItem = (LineItem) data.getSerializableExtra("lineItem");

            // Add the LineItem to the lineItems list
            lineItems.add(lineItem);
            adapter.notifyDataSetChanged();
        }
    }


    private class MyAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(mediaType, params[0]);
            Request request = new Request.Builder()
                    .url(Api.baseURL + "/order/add")
                    .addHeader("Authorization", "Bearer " + header)
                    .method("POST", requestBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    System.out.println(params[0]);
                    AddOrder.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddOrder.this,"Thêm thành công", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return true;
                } else {
                    AddOrder.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddOrder.this," Quá số lượng", Toast.LENGTH_SHORT).show();
                        }
                    });
                    System.out.println(params[0]);
                    System.out.println("lỗi " + response.toString());
                    return false ;
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
                finish();
            } else {
                System.out.println("lỗi ");
                finish();

            }
        }
    }
}


