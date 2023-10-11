package com.example.warehousemanagement.obj;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class LineItem  implements Serializable {
    String id;
    double soLuong;

    public String getId() {
        return id;
    }

    public LineItem(String id,
                    double soLuong) {
        this.id = id;
        this.soLuong = soLuong;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getsoLuong() {
        return soLuong;
    }

    public void setsoLuong(double soLuong) {
        this.soLuong = soLuong;
    }
}
