package com.example.warehousemanagement.storage;

import android.content.Context;
import android.content.Intent;
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
import com.example.warehousemanagement.additem.DsSanPham;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.obj.Storage;

import java.util.List;

public class ArrayStorage extends ArrayAdapter<Storage> {

    private Context context;
    private List<Storage> storageListList;
    String role;

    public ArrayStorage(Context context, List<Storage> itemList) {
        super(context, R.layout.activity_storage_manager, itemList);
        this.context = context;
        this.storageListList = itemList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.storage_item, parent, false);

        LinearLayout tapStorage = rowView.findViewById(R.id.tapStorage);
        TextView nameTextView = rowView.findViewById(R.id.nameTextViewSto);
        TextView codeTextView = rowView.findViewById(R.id.codeTextViewSto);
        ImageView SuabtnProduct = rowView.findViewById(R.id.SuabtnStorage);
        role = DangNhap.account.getUser().getRole();
        System.out.println("role"+ role);
        if (role.equals("saler")){
            SuabtnProduct.setVisibility(View.GONE);
        }
        Storage item = (Storage) getItem(position);
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
                                    Intent intent = new Intent(context, EditStorage.class);
                                    intent.putExtra("id", item.getId());
                                    context.startActivity(intent);
                                    // Pass the product ID to the EditProduct activity
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

        tapStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DsSanPham.class);
                intent.putExtra("id", item.getId());
                context.startActivity(intent);
            }
        });
        nameTextView.setText(item.getAddress());
        codeTextView.setText(item.getCode());

        return rowView;
    }
}

