package com.example.warehousemanagement.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warehousemanagement.R;
import com.example.warehousemanagement.additem.ProductAdapter;
import com.example.warehousemanagement.obj.Product;
import com.example.warehousemanagement.obj.User;

import java.util.List;

public class NguoiDungAdapter extends RecyclerView.Adapter<NguoiDungAdapter.ViewHolder>{
    private Context context;
    private List<User> userList;

    public NguoiDungAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public NguoiDungAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_user_manager, parent, false);
        return new NguoiDungAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NguoiDungAdapter.ViewHolder holder, int position) {
        User user = userList.get(position);

        // Set data to views
        holder.title.setText(user.getName());

        //Picasso.get().load(product.getImage());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.imgListUser);
            title = itemView.findViewById(R.id.txtNameUser);
        }
    }
}
