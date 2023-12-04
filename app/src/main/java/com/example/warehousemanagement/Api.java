package com.example.warehousemanagement;

public class Api {
    public static String baseURL = "http://192.168.1.13:8080";
    public  static String header = DangNhap.account.getToken() ;
}
