package com.example.appdiemdanh.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.appdiemdanh.data.model.User;

public class Utils {
//    public static final String BASE_URL = "http://192.168.2.8/qlhs/";
//    public static final String BASE_URL = "http://192.168.1.5/qlhs/";
    public static final String BASE_URL = "http://doan2023.000webhostapp.com/doan2023/";

    public static User user_current = new User();

    public static void saveUser(Context context, int iduser) {
        //tao doi tuong sharepreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        //goi phuong thuc editor de chinh sua
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //them du lieu
        editor.putInt("iduser", iduser);
        editor.apply();
    }

    public static int getUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("iduser", -1);
    }

}
