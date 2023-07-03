package com.example.warehousemanagement.order;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.obj.Order;
import com.example.warehousemanagement.obj.Product;
import com.example.warehousemanagement.other.MyAsyncTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ArrayOrder extends ArrayAdapter<Order> {

    private Context context;
    private List<Order> itemList;
    String header;

    public ArrayOrder(Context context,List<Order> itemList) {
        super(context, R.layout.activity_view_product ,itemList);
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.product_item, parent, false);

        ImageView imageView = rowView.findViewById(R.id.imageView);
        TextView nameTextView = rowView.findViewById(R.id.nameTextView);
        TextView codeTextView = rowView.findViewById(R.id.codeTextView);
        TextView stockTextView = rowView.findViewById(R.id.stockTextView);
        ImageView SuabtnProduct = rowView.findViewById(R.id.SuabtnProduct);

        LinearLayout btnProductEach = rowView.findViewById(R.id.btnProductEach);
        Order item = itemList.get(position);
        String id = item.getId();
        String role = DangNhap.account.getUser().getRole();
        String status = item.getStatus();
        codeTextView.setVisibility(View.GONE);
        ArrayList<Product>  lineItems = item.getLineItems();
        btnProductEach.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, DsSanPhamOrder.class);
                        intent.putExtra("lineItems",lineItems);
                        intent.putExtra("id",id);
                        context.startActivity(intent);// Pass the product ID to the EditProduct activity
                    }
                }
        );

        SuabtnProduct.setVisibility(View.GONE);

        if(status.equals("accepted")){
            imageView.setImageResource(R.drawable.accepted_icon);
        }else{
            if(status.equals("pending")){
                imageView.setImageResource(R.drawable.pending_icon);
                imageView.setOnClickListener(new View.OnClickListener() {
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
                                        acceptOrder(id,role);
                                        return true;
                                    case R.id.cancel_order:
                                        cancelOrder(id);
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });
                    }
                });
            }else{
                if(status.equals("success")){
                    imageView.setImageResource(R.drawable.success_icon);
                }
                else {
                    imageView.setImageResource(R.drawable.cancel);
                }
            }

        }

        nameTextView.setText(item.getOwnerId());
        stockTextView.setText(item.getStatus());

        return rowView;
    }
    private void acceptOrder(String orderId,String role) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("text/plain");

        if (role.equals("stocker")){
            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"status\": \"accepted\"\r\n}");
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
                        new OrderList().loadOrderList();
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
                        new OrderList().loadOrderList();
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
                .url("http://14.225.211.190:4001/api/order/" + orderId + "/cancel")
                .method("PUT", body)
                .addHeader("Authorization", "Bearer " + header)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Gọi lại MyAsyncTask để tải lại danh sách đơn hàng
                    new OrderList().loadOrderList();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }
}

