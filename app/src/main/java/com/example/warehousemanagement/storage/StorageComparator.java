package com.example.warehousemanagement.storage;

import com.example.warehousemanagement.obj.Product;
import com.example.warehousemanagement.obj.Storage;

import java.util.Comparator;

public class StorageComparator implements Comparator<Storage> {
    @Override
    public int compare(Storage storage1, Storage storage2) {
        return storage1.getName().compareTo(storage2.getName());
    }
}
