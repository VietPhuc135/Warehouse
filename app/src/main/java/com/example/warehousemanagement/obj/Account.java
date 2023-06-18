package com.example.warehousemanagement.obj;

public class Account {
        User user;
        private String token;


        // Getter Methods

        public User getUser() {
            return user;
        }

        public String getToken() {
            return token;
        }

        // Setter Methods

        public void setUser(User user) {
            this.user = user;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

