package com.example.warehousemanagement.order;

import android.content.Context;
import android.content.Intent;
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

import com.example.warehousemanagement.DsSanPham;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.additem.EditProduct;
import com.example.warehousemanagement.obj.Order;
import com.example.warehousemanagement.obj.Product;

import java.util.ArrayList;
import java.util.List;

public class ArrayOrder extends ArrayAdapter<Order> {

    private Context context;
    private List<Order> itemList;

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
        ArrayList<Product>  lineItems = item.getLineItems();
        btnProductEach.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, DsSanPhamOrder.class);
                        intent.putExtra("lineItems",lineItems);
                        context.startActivity(intent);// Pass the product ID to the EditProduct activity
                    }
                }
        );
        SuabtnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(context, v);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_order, popupMenu.getMenu());
                    popupMenu.show();


                    // Xử lý các sự kiện khi người dùng chọn một item trong menu
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem items) {
                            switch (items.getItemId()) {
                                case R.id.detail_product:
                                    Intent intent = new Intent(context, DsSanPhamOrder.class);
                                    intent.putExtra("lineItems", lineItems);
                                    context.startActivity(intent);// Pass the product ID to the EditProduct activity
                                    return true;

                                case R.id.delete_product:
                                    // Xử lý khi người dùng chọn Edit
                                    Toast.makeText(context, "Đang hoàn thiện", Toast.LENGTH_SHORT).show();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });

                popupMenu.show();
            }
        });
        //Picasso.get().load(item.getImage()).into(imageView);
        nameTextView.setText(item.getOwnerId());
        stockTextView.setText(item.getStatus());

        return rowView;
    }
}

