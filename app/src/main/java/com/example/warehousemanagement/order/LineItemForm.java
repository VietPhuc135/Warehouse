package com.example.warehousemanagement.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.obj.LineItem;
import com.example.warehousemanagement.obj.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
// LineItemFormActivity.java

public class LineItemForm extends AppCompatActivity {
    private EditText etLineItemId;
    private EditText etLineItemQuantity;
    private Button btnAddLineItem;
    String selectedLineItemId, header;
    List<Product> lineItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_line_item);
        header = DangNhap.account.getToken();
        etLineItemId = findViewById(R.id.tvLineItemId);
        etLineItemQuantity = findViewById(R.id.tvLineItemQuantity);
        btnAddLineItem = findViewById(R.id.addLineItem);

        btnAddLineItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLineItem();
            }
        });
//        String jsonData = "[\n" +
//                "    {\n" +
//                "        \"id\": \"1\",\n" +
//                "        \"name\": \"Bánh bông lan trứng muỗi OLODO6\",\n" +
//                "        \"code\": \"PPAP_01\",\n" +
//                "        \"date\": \"2024-01-01T00:00:00.000Z\",\n" +
//                "        \"stock\": 50,\n" +
//                "        \"note\": \"null\",\n" +
//                "        \"producer\": \"Công ty cổ phẩn OLODO\",\n" +
//                "        \"status\": \"available\",\n" +
//                "        \"storageId\": \"1\",\n" +
//                "        \"category\": \"Bánh\",\n" +
//                "        \"image\": null\n" +
//                "    },\n" +
//                "    {\n" +
//                "        \"id\": \"2\",\n" +
//                "        \"name\": \"Bánh Pate OLODO123\",\n" +
//                "        \"code\": \"PPAP_02\",\n" +
//                "        \"date\": \"2024-01-01T00:00:00.000Z\",\n" +
//                "        \"stock\": 30,\n" +
//                "        \"note\": \"null\",\n" +
//                "        \"producer\": \"Công ty cổ phẩn OLODO\",\n" +
//                "        \"status\": \"available\",\n" +
//                "        \"storageId\": \"1\",\n" +
//                "        \"category\": \"Bánh\",\n" +
//                "        \"image\": null\n" +
//                "    },\n" +
//                "    {\n" +
//                "        \"id\": \"3\",\n" +
//                "        \"name\": \"Bánh tuy OLODO\",\n" +
//                "        \"code\": \"PPAP_03\",\n" +
//                "        \"date\": \"2024-01-01T00:00:00.000Z\",\n" +
//                "        \"stock\": 100,\n" +
//                "        \"note\": null,\n" +
//                "        \"producer\": \"Công ty cổ phẩn OLODO\",\n" +
//                "        \"status\": \"pending\",\n" +
//                "        \"storageId\": \"1\",\n" +
//                "        \"category\": \"Bánh\",\n" +
//                "        \"image\": null\n" +
//                "    }\n" +
//                "]";
//        OkHttpClient client = new OkHttpClient();
//        MediaType mediaType = MediaType.parse("text/plain");
//        RequestBody body = RequestBody.create(mediaType, "");
//        Request request = new Request.Builder()
//                .url("http://14.225.211.190:4001/api/product/query")
//                .method("POST", body)
//                .addHeader("Authorization", "Bearer " + header)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
////                    String jsonData = response.body().string();
//
//                    lineItems = parseJsonData(jsonData);
//                }
//            }
//        });

//        // Create an ArrayList of name values for the Spinner
//        List<String> lineItemNames = new ArrayList<>();
//        for (Product lineItem : lineItems) {
//            lineItemNames.add(lineItem.getName());
//        }
//
//        // Get a reference to the spinner
//        Spinner spinnerLineItem = findViewById(R.id.spinnerItemLinner);
//
//        // Create an ArrayAdapter for the Spinner using the lineItemNames ArrayList
//        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lineItemNames);
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // Set the ArrayAdapter on the Spinner
//        spinnerLineItem.setAdapter(spinnerAdapter);
//
//        // Set a selection listener for the Spinner
//        spinnerLineItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedLineItemId = lineItems.get(position).getId();
//                // Use the selectedLineItemId for further processing or sending to the API
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // Handle the case when nothing is selected
//            }
//        });
//    }
    }
    private void addLineItem() {
        String lineItemId = etLineItemId.getText().toString().trim();
        String lineItemQuantity = etLineItemQuantity.getText().toString().trim();
        Double Quantity = Double.parseDouble(lineItemQuantity) ;
        // Create a new LineItem object
        LineItem lineItem = new LineItem(lineItemId, Quantity);

        // Pass the LineItem object back to the AddOrder activity
        Intent intent = new Intent();
        intent.putExtra("lineItem", lineItem);
        setResult(RESULT_OK, intent);
        finish();
    }

    private List<Product> parseJsonData(String jsonData) {
        List<Product> lineItems = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String id = jsonObject.getString("id");
                // Parse other fields from the JSON object


                // Set other fields of the LineItem object
//
//                lineItems.add(lineItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return lineItems;
    }


}
