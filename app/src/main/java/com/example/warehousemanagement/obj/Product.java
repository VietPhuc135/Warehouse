package com.example.warehousemanagement.obj;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class Product  implements Serializable{

    private String id;
    private String name;
    private String maSp;
    private float soLuong;
    private String date;
    private String category;
    private String storageId;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

// Getter Methods

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMaSp() {
        return maSp;
    }

    public float getSoLuong() {
        return soLuong;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public String getStorageId() {
        return storageId;
    }

    // Setter Methods

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaSp(String maSp) {
        this.maSp = maSp;
    }

    public void setSoLuong(float soLuong) {
        this.soLuong = soLuong;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }
    
}
