package com.example.warehousemanagement.order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.additem.EditProduct;
import com.example.warehousemanagement.obj.Product;

import java.util.List;

public class ProductPickAdapter extends ArrayAdapter<Product> {

    private Context context;
    private List<Product> itemList;
    String role;
    private OnProductClickListener onProductClickListener;

    public void setOnProductClickListener(OnProductClickListener listener) {
        this.onProductClickListener = listener;
    }

    public ProductPickAdapter(Context context, List<Product> itemList) {
        super(context, R.layout.activity_view_product, itemList);
        this.context = context;
        this.itemList = itemList;
    }

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.product_item, parent, false);

        ImageView imageView = rowView.findViewById(R.id.imageView);
        TextView nameTextView = rowView.findViewById(R.id.nameTextView);
        TextView titlenameTextView = rowView.findViewById(R.id.titlenameTextView);
        TextView titlestockTextView = rowView.findViewById(R.id.titlestockTextView);

        titlestockTextView.setText("");
        titlenameTextView.setText("Tên SP: ");
        TextView codeTextView = rowView.findViewById(R.id.codeTextView);
        TextView stockTextView = rowView.findViewById(R.id.stockTextView);
        ImageView SuabtnProduct = rowView.findViewById(R.id.SuabtnProduct);

        LinearLayout btnArrayProduct = rowView.findViewById(R.id.btnProductEach);
        Product item = itemList.get(position);


        role = DangNhap.account.getRole();
        System.out.println("role" + role);
        if (role.equals("SALER")) {
            SuabtnProduct.setVisibility(View.GONE);
        }
        btnArrayProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProductClickListener.onProductClick(itemList.get(position));

            }
        });
//        rowView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (onProductClickListener != null) {
//                }
//            }
//        });

        nameTextView.setText(item.getName());
        codeTextView.setText("Loại: " + item.getMaSp()/*+ "\t\tKho: " + item.getStorageId()*/);
        stockTextView.setText("Id: " + item.getId() + "\tStock: " + String.valueOf(item.getSoLuong()));

        return rowView;
    }
}
