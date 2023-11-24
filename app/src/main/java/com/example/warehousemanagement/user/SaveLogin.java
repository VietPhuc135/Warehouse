package com.example.warehousemanagement.user;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveLogin {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USER_INFO = "userInfo";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SaveLogin(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserInfo(String userInfoJson) {
        editor.putString(KEY_USER_INFO, userInfoJson);
        editor.apply();
    }

    public String getUserInfo() {

        return sharedPreferences.getString(KEY_USER_INFO, null);
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }

}
