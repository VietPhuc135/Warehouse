package com.example.warehousemanagement.obj;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class LineItem  implements Serializable {
    String id;
    double quanity;

    public String getId() {
        return id;
    }

    public LineItem(String id,
                    double quanity) {
        this.id = id;
        this.quanity = quanity;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getQuanity() {
        return quanity;
    }

    public void setQuanity(double quanity) {
        this.quanity = quanity;
    }
}
