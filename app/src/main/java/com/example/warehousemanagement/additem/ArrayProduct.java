package com.example.warehousemanagement.additem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
        TextView stockTextView = rowView.findViewById(R.id.stockTextView);

        Product item = itemList.get(position);

        //Picasso.get().load(item.getImage()).into(imageView);
        nameTextView.setText(item.getName());
        stockTextView.setText("Stock: " + String.valueOf(item.getStock()));

        return rowView;
    }
}

