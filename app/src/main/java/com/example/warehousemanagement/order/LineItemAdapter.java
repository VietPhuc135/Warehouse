package com.example.warehousemanagement.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.warehousemanagement.R;
import com.example.warehousemanagement.obj.LineItem;

import java.util.List;

public class LineItemAdapter extends ArrayAdapter<LineItem> {
    private Context context;
    private List<LineItem> lineItems;
    private AddOrder addOrder;


    public LineItemAdapter(Context context, List<LineItem> lineItems, AddOrder addOrder) {
        super(context, 0, lineItems);
        this.context = context;
        this.lineItems = lineItems;
        this.addOrder = addOrder;
    }


    @Override
    public View getView(int position, @Nullable View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.item_line_item, parent, false);
        }

        // Ánh xạ các thành phần trong item_line_item.xml
        TextView tvLineItemId = view.findViewById(R.id.tvLineItemId);
        TextView tvLineItemQuantity = view.findViewById(R.id.tvLineItemQuantity);
        Button btnAddLineItem = view.findViewById(R.id.addLineItem);
        btnAddLineItem.setVisibility(View.GONE);
        tvLineItemId.setFocusableInTouchMode(false);
        tvLineItemQuantity.setFocusableInTouchMode(false);

        LinearLayout itemLinebtn = view.findViewById(R.id.itemLinebtn);
        itemLinebtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addOrder.removeLineItem(position);
                    }
                }
        );

        // Lấy đối tượng LineItem tại vị trí hiện tại
        LineItem lineItem = lineItems.get(position);
        // Hiển thị thông tin LineItem trong TextViews
        tvLineItemId.setText(lineItem.getId());
        tvLineItemQuantity.setText(String.valueOf(lineItem.getQuanity()));

        return view;
    }
}

