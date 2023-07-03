package com.example.warehousemanagement.obj;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class LineItem  implements Serializable {
    String id;
    String quanity;

    public String getId() {
        return id;
    }

    public LineItem(String id,
                    String quanity) {
        this.id = id;
        this.quanity = quanity;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuanity() {
        return quanity;
    }

    public void setQuanity(String quanity) {
        this.quanity = quanity;
    }
}
