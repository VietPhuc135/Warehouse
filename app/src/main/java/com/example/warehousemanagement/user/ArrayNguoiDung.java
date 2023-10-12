package com.example.warehousemanagement.user;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.warehousemanagement.R;
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
        LinearLayout userTap = rowView.findViewById(R.id.userTap);

        User userItem = itemList.get(position);
        userTap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditNguoiDung.class);
                intent.putExtra("id", userItem.getId());
                context.startActivity(intent);
//
//                PopupMenu popupMenu = new PopupMenu(context, v);
//                popupMenu.getMenuInflater().inflate(R.menu.menu_user, popupMenu.getMenu());
//                popupMenu.show();
//
//                // Xử lý các sự kiện khi người dùng chọn một item trong menu
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.detail_user:
//                                // Xử lý khi người dùng chọn Delete
//
//                                return true;
//
//                            case R.id.delete_user:
//                                // Xử lý khi người dùng chọn Edit
//                                return true;
//                            default:
//                                return false;
//                        }
//                    }
//                });
            }
        });
        User item = itemList.get(position);

        //Picasso.get().load(item.getImage()).into(imageView);
        nameTextView.setText(item.getName());
        infoTextView.setText("Email: " + item.getEmail());

        return rowView;
    }
}
