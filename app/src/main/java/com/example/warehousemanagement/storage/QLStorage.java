package com.example.warehousemanagement.storage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.warehousemanagement.DangNhap;
import com.example.warehousemanagement.R;
import com.example.warehousemanagement.user.AddNguoiDung;
import com.example.warehousemanagement.user.EditNguoiDung;
import com.example.warehousemanagement.user.QLNguoiDung;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class QLStorage extends AppCompatActivity {

    private ListView storageList;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> storageNames;
    String header ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_manager);
        header = DangNhap.account.getToken();
        storageList = findViewById(R.id.lvStorage);
        storageNames = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.activity_list_market, storageNames);
        storageList.setAdapter(adapter);
        ImageView imgArrageStorage = findViewById(R.id.imgArrageStorage);
        imgArrageStorage.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(QLStorage.this, AddStorage.class);
                   startActivity(intent);
               }
           }
        );
        // Gọi phương thức để thực hiện yêu cầu HTTP và hiển thị danh sách
        fetchStorageList();

        // Đăng ký menu context cho ListView, khi người dùng nhấn giữ trên một phần tử trong ListView
        registerForContextMenu(storageList);
    }

    private void fetchStorageList() {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("http://14.225.211.190:4001/api/storage/query")
                .method("POST", body)
                .addHeader("Authorization", "Bearer " + header)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int statusCode = response.code();
                if (statusCode == 201) {
                    String jsonData = response.body().string();
                    System.out.println("thành công");
                    Log.d("Response", jsonData);
                    System.out.println(jsonData);
                    try {
                        JSONArray jsonArray = new JSONArray(jsonData);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            //String storageName = jsonObject.optString("address");
                            String address = jsonObject.getString("address");
                            String code = jsonObject.getString("code");
                            String storageInfo = "Address: " + address + "\nCode: " + code;
                            storageNames.add(storageInfo);

                        }

                        // Cập nhật giao diện trong luồng UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });

                    } catch (JSONException e) {
                        System.out.println("khong thanh cong");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("khong thanh cong");
                e.printStackTrace();
            }
        });
    }

    // Tạo menu context cho mỗi phần tử trong ListView
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_storage, menu);
    }

    // Xử lý sự kiện khi mục của menu context được chọn
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Lấy thông tin về phần tử trong ListView được chọn
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;

        switch (item.getItemId()) {
            case R.id.add_storage:
                Intent intent = new Intent(QLStorage.this, AddStorage.class);
                startActivity(intent);
                return true;
            case R.id.edit_storage:
                Intent intent1 = new Intent(QLStorage.this, EditStorage.class);
                startActivity(intent1);
                return true;
            case R.id.delete_storage:
                // Xóa phần tử được chọn khỏi ListView
                adapter.remove(adapter.getItem(position));
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}