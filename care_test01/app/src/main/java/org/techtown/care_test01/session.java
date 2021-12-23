package org.techtown.care_test01;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class session {
    static  String id;
    static  String request;
    static  int i;
    @SuppressLint("StaticFieldLeak")
    static Context mBase;
    static  String cl_socket;
    static  String now_window = "0";

    public static String getNow_window() {
        return now_window;
    }

    public static void setNow_window(String now_window) {
        session.now_window = now_window;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        session.id = id;
    }


    public static void setContext(Context context) {
        session.mBase = context;
    }


    public static String getCl_socket() {
        return cl_socket;
    }

    public static void setCl_socket(String cl_socket) {
        session.cl_socket = cl_socket;
    }

    public static  void setSharedPreferences(String key , String value){
        SharedPreferences sharedPreferences =getSharedPreferences(request,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public static SharedPreferences getSharedPreferences (String request , int i) {
      session.mBase=mBase;
      session.request =request;
      session.i = i ;

        return mBase.getSharedPreferences(request,i);
    };
















}
