package com.example.warehousemanagement.other;

public class ParaItem {
    String Id ;
    String Name ;

    public ParaItem(String name, String Id) {
        this.Name = name;
        this.Id = Id;
    }

    public ParaItem() {

    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
