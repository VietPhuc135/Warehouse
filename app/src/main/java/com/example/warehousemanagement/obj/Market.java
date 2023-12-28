package com.example.warehousemanagement.obj;

import androidx.annotation.Keep;

@Keep
public class Market {
    String name ;
    String address;
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String code) {
        this.name = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
