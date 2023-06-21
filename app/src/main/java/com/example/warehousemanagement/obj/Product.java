package com.example.warehousemanagement.obj;

public class Product {

        private String name;
        private String code;
        private String date;
        private float stock;
        private String note;
        private String producer;
        private String status;
//        StorageId StorageIdObject;
        private String category;
        private String image;


        // Getter Methods

        public String getName() {
            return name;
        }

        public String getCode() {
            return code;
        }

        public String getDate() {
            return date;
        }

        public float getStock() {
            return stock;
        }

        public String getNote() {
            return note;
        }

        public String getProducer() {
            return producer;
        }

        public String getStatus() {
            return status;
        }

//        public StorageId getStorageId() {
//            return StorageIdObject;
//        }

        public String getCategory() {
            return category;
        }

        public String getImage() {
            return image;
        }

        // Setter Methods

        public void setName(String name) {
            this.name = name;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setStock(float stock) {
            this.stock = stock;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public void setProducer(String producer) {
            this.producer = producer;
        }

        public void setStatus(String status) {
            this.status = status;
        }

//        public void setStorageId(StorageId storageIdObject) {
//            this.StorageIdObject = storageIdObject;
//        }

        public void setCategory(String category) {
            this.category = category;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }