package com.example.warehousemanagement.obj;

import java.io.Serializable;

public class Product  implements Serializable {
//
//    private String id ;
//        private String name;
//        private String code;
//        private String date;
//        private float stock;
//        private String note;
//        private String producer;
//        double quantity;
//        private String status;
//        String storageId;
//
//
//    public String getstorageId() {
//        return storageId;
//    }
//
//    public void setStorageIdObject(String storageIdObject) {
//        storageId = storageIdObject;
//    }
//
//    private String category;
//        private String image;
//
//
//        // Getter Methods
//        public String getId() {
//            return id;
//        }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//        public String getName() {
//            return name;
//        }
//
//    public double getQuanity() {
//        return quantity;
//    }
//
//    public void setQuanity(double quantity) {
//        this.quantity = quantity;
//    }
//
//    public String getCode() {
//            return code;
//        }
//
//        public String getDate() {
//            return date;
//        }
//
//        public float getStock() {
//            return stock;
//        }
//
//        public String getNote() {
//            return note;
//        }
//
//        public String getProducer() {
//            return producer;
//        }
//
//        public String getStatus() {
//            return status;
//        }
////
////        public StorageId getStorageId() {
////            return StorageIdObject;
////        }
//
//        public String getCategory() {
//            return category;
//        }
//
//        public String getImage() {
//            return image;
//        }
//
//        // Setter Methods
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public void setCode(String code) {
//            this.code = code;
//        }
//
//        public void setDate(String date) {
//            this.date = date;
//        }
//
//        public void setStock(float stock) {
//            this.stock = stock;
//        }
//
//        public void setNote(String note) {
//            this.note = note;
//        }
//
//        public void setProducer(String producer) {
//            this.producer = producer;
//        }
//
//        public void setStatus(String status) {
//            this.status = status;
//        }
//
////        public void setStorageId(StorageId storageIdObject) {
////            this.StorageIdObject = storageIdObject;
////        }
//
//        public void setCategory(String category) {
//            this.category = category;
//        }
//
//        public void setImage(String image) {
//            this.image = image;
//        }

    private String id;
    private String name;
    private String maSp;
    private float soLuong;
    private String date;
    private String category;
    private String storageId;


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
