package com.example.warehousemanagement.order;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanagement.Api;
import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.obj.Product;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DsSanPhamOrder extends AppCompatActivity {
    String header;
    private ListView listView;
    private ArrayProductInOrder adapter;
    private List<Product> itemList = new ArrayList<>(); // Khởi tạo danh sách sản phẩm
    String role;
    ImageView imgAddProduct;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        imgAddProduct = findViewById(R.id.imgAddProduct);
        ImageView imgAccepOrder = findViewById(R.id.imgAccepOrder);
        listView = findViewById(R.id.lvProduct);
        EditText searchProduct = findViewById(R.id.searchProduct);
        searchProduct.setVisibility(View.GONE);
        TextView idTitle = findViewById(R.id.idTitle);
        idTitle.setText("Sản phẩm trong Order");
        header = DangNhap.account.getToken();
        role = DangNhap.account.getRole();
        Intent intent = getIntent();
        String idorder = intent.getStringExtra("id");
        String status = intent.getStringExtra("status");

        if (status.equals("WAITING")){
            imgAccepOrder.setImageResource(R.drawable.pending_icon);
            imgAccepOrder.setVisibility(View.VISIBLE);
            imgAccepOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(context, v);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_choose_order, popupMenu.getMenu());

                    popupMenu.show();

                    // Xử lý các sự kiện khi người dùng chọn một item trong menu
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem items) {

                            switch (items.getItemId()) {
                                case R.id.accepted_order:
                                    try {
                                        acceptOrder(idorder, role);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    return true;

                                case R.id.cancel_order:
//                                    if (role.equals("STOCKER")) {
//                                        Toast.makeText(context,"Bạn là stocker khoogn thể hủy",Toast.LENGTH_SHORT).show();
//                                    }
//                                    else{
                                        cancelOrder(idorder);
//                                    }
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                }
            });
        }

        if (role.equals("SALER")){
            imgAddProduct.setVisibility(View.GONE);
        }
        imgAddProduct.setImageResource(R.drawable.cancel);

        imgAddProduct.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick (View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có muốn xóa order không?");

        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("text/plain");
                RequestBody body = RequestBody.create(mediaType, "");
                Request request = new Request.Builder()
                        .url(Api.baseURL + "/order/getlist" + idorder)
                        .delete()
                        .addHeader("Authorization", "Bearer " + header)
                        .build();

                client.newCall(request).enqueue(new okhttp3.Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            // Gọi lại MyAsyncTask để tải lại danh sách đơn hàng
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "Đã xóa order", Toast.LENGTH_SHORT).show();
                                }
                            });
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Không thành công", Toast.LENGTH_SHORT).show();
                            }
                        });
                        e.printStackTrace();
                    }
                });
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        //dialog.show();
    }

    });
    adapter =new

    ArrayProductInOrder(this,itemList); // Khởi tạo adapter và gán danh sách sản phẩm
        listView.setAdapter(adapter); // Gán adapter cho listView

    ArrayList<Product> lineItems = (ArrayList<Product>) intent.getSerializableExtra("lineItems");

    updateProductList(lineItems); // Cập nhật danh sách sản phẩm từ Intent
        System.out.println("lineItems"+lineItems);

}
    private void acceptOrder(String orderId,String role) throws JSONException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("text/plain");

        if (role.equals("STOCKER")){
            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"status\": \"accepted\"\r\n}");
            Request request = new Request.Builder()
                    .url(Api.baseURL + "/order/status/accepted/"+orderId)
                    .get()
                    .addHeader("Authorization", "Bearer "+ header)
                    .build();
            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        DsSanPhamOrder.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(DsSanPhamOrder.this,"Đã chấp nhận", Toast.LENGTH_SHORT).show();
                            }
                        });
                        finish();
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    DsSanPhamOrder.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DsSanPhamOrder.this,"Lỗi không thể chấp nhận", Toast.LENGTH_SHORT).show();
                        }
                    });
                    finish();e.printStackTrace();
                }
            });
        }
        else
        {
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("http://14.225.211.190:4001/api/order/" + orderId + "/accept")
                    .put(body)
                    .addHeader("Authorization", "Bearer " + header)
                    .build();
            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        // Gọi lại MyAsyncTask để tải lại danh sách đơn hàng
                       finish();
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
            });
        }



    }
    private void succesOrder(String orderId,String role) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("text/plain");

        if (role.equals("STOCKER")){
            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"status\": \"in_transit\"\r\n}");
            Request request = new Request.Builder()
                    .url("http://14.225.211.190:4001/api/order/" + orderId + "/status")
                    .put(body)
                    .addHeader("Authorization", "Bearer " + header)
                    .build();
            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        // Gọi lại MyAsyncTask để tải lại danh sách đơn hàng

                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
            });
        }
        else
        {
            RequestBody body = RequestBody.create(mediaType,"{\r\n    \"status\": \"success\"\r\n}");
            Request request = new Request.Builder()
                    .url("http://14.225.211.190:4001/api/order/" + orderId + "/accept")
                    .put(body)
                    .addHeader("Authorization", "Bearer " + header)
                    .build();
            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        // Gọi lại MyAsyncTask để tải lại danh sách đơn hàng
                        finish();
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
            });
        }



    }

    private void cancelOrder(String orderId) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(Api.baseURL + "/order/status/cancel/" + orderId )
                .method("GET", null)
                .addHeader("Authorization", "Bearer " + header)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Gọi lại MyAsyncTask để tải lại danh sách đơn hàng

                    finish();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void updateProductList(ArrayList<Product> products) {
        itemList.clear(); // Xóa danh sách sản phẩm hiện tại
        itemList.addAll(products); // Thêm các sản phẩm mới vào danh sách
        adapter.notifyDataSetChanged(); // Thông báo cho adapter cập nhật hiển thị
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                Product updatedProduct = (Product) data.getSerializableExtra("updatedProduct");
                // Thực hiện cập nhật sản phẩm đã chỉnh sửa trong danh sách
                updateProductInList(updatedProduct);
            }
        }
    }

    private void updateProductInList(Product updatedProduct) {
        // Tìm sản phẩm trong danh sách itemList có cùng id với sản phẩm đã chỉnh sửa
        for (int i = 0; i < itemList.size(); i++) {
            Product product = itemList.get(i);
            if (product.getId().equals(updatedProduct.getId())) {
                // Cập nhật thông tin sản phẩm
//                product.setName(updatedProduct.getName());
//                product.setCode(updatedProduct.getCode());
//                product.setDate(updatedProduct.getDate());
//                product.setStock(updatedProduct.getStock());
//                product.setNote(updatedProduct.getNote());
//                product.setProducer(updatedProduct.getProducer());
//                product.setStatus(updatedProduct.getStatus());
//                product.setCategory(updatedProduct.getCategory());
                break;
            }
        }
        adapter.notifyDataSetChanged(); // Thông báo cập nhật hiển thị
    }

}
