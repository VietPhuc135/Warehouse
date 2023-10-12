package com.example.warehousemanagement;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.warehousemanagement.additem.DsSanPham;
import com.example.warehousemanagement.databinding.ActivityMapStorageBinding;
import com.example.warehousemanagement.obj.Storage;
import com.example.warehousemanagement.storage.ArrayStorage;
import com.example.warehousemanagement.storage.QLStorage;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private ArrayList<Storage> getItem;
    private List<Storage> itemList = new ArrayList<>();
    private GoogleMap mMap;
    private ActivityMapStorageBinding binding;
    String header, item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapStorageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();

        if (intent.hasExtra("item") ) {
            item = intent.getStringExtra("item");
        }

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Storage>>() {
        }.getType();

        itemList = gson.fromJson(item, listType);
        System.out.println(  "itemmmmmmmmmm \n"+itemList);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Intent intent = new Intent(MapsActivity.this, DsSanPham.class);
        intent.putExtra("id", marker.getId());
        startActivity(intent);
        return true;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng myLoca = new LatLng(20.9808, 105.7936);
            // Sử dụng latitude và longitude
            mMap.addMarker(new MarkerOptions().position(myLoca).title("Vị trí của bạn"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLoca));
        if (!itemList.isEmpty()){
            for (Storage item : itemList) {
                double latitude = Double.parseDouble(item.getLatitude());
                double longitude = Double.parseDouble(item.getLongtitude());
                LatLng location = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(location).title(item.getName()));
            }
        }
    }
}