package com.example.warehousemanagement.user;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.warehousemanagement.R;
import com.example.warehousemanagement.additem.EditProduct;
import com.example.warehousemanagement.obj.Product;
import com.example.warehousemanagement.obj.User;

import java.util.List;

public class ArrayNguoiDung extends ArrayAdapter<User> {
    private Context context;
    private List<User> itemList;

    public ArrayNguoiDung(Context context, List<User> itemList) {
        super(context, R.layout.activity_user_manager, itemList);
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.activity_list_user, parent, false);

        ImageView imageView = rowView.findViewById(R.id.imgListUser);
        TextView nameTextView = rowView.findViewById(R.id.txtNameUser);
        TextView infoTextView = rowView.findViewById(R.id.txtInfoUser);

        User item = itemList.get(position);

        //Picasso.get().load(item.getImage()).into(imageView);
        nameTextView.setText(item.getName());
        infoTextView.setText("Address: " + item.getAddress() + " Age: " + item.getAge());

        return rowView;
    }
}