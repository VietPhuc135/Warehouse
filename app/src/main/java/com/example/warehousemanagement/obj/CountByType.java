package com.example.warehousemanagement.obj;

import androidx.annotation.Keep;

import com.github.mikephil.charting.data.Entry;

@Keep
public class CountByType {

    public CountByType( int soLuong,String type) {
        this.type = type;
        this.soLuong = soLuong;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(float soLuong) {
        this.soLuong = soLuong;
    }

    String type;
    float soLuong;
}
