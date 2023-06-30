package com.example.warehousemanagement.additem;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.warehousemanagement.R;
import com.example.warehousemanagement.obj.Product;
import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Picasso;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class ArrayProduct extends ArrayAdapter<Product> {

    private Context context;
    private List<Product> itemList;

    public ArrayProduct(Context context, List<Product> itemList) {
        super(context, R.layout.activity_view_product, itemList);
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


        Product item = itemList.get(position);


        SuabtnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(context, v);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_product, popupMenu.getMenu());
                    popupMenu.show();

                    // Xử lý các sự kiện khi người dùng chọn một item trong menu
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem items) {
                            switch (items.getItemId()) {
                                case R.id.detail_product:
                                    Intent intent = new Intent(context, EditProduct.class);
                                    intent.putExtra("id", item.getId());
                                    context.startActivity(intent);// Pass the product ID to the EditProduct activity
                                    return true;
                                case R.id.repair_product:
                                    // Xử lý khi người dùng chọn Edit
                                    Toast.makeText(context, "Đang hoàn thiện", Toast.LENGTH_SHORT).show();
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
        nameTextView.setText(item.getName());
        codeTextView.setText(item.getCode());
        stockTextView.setText("Stock: " + String.valueOf(item.getStock()));

        return rowView;
    }
}

