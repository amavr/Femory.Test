package com.amavr.tools;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

public class XMem {

    final static String TAG = "XDBG.mem";
    final static String PREF_FILE = "app";

    private static XMem instance = null;

    private SharedPreferences prefs = null;
    private Application app;
    private HashMap<String, Object> dic;

    public static XMem create(Application A){
        if (instance == null) {
            instance = new XMem();
            instance.app = A;
            instance.prefs = A.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
            instance.dic = new HashMap<String, Object>();
            Log.d(TAG, "XMem created");
        }
        return instance;
    }

    public static XMem getInstance() {
        return instance;
    }

    public Object getTag(String key){
        return dic.get(key);
    }

    public void setTag(String key, Object value){
        dic.put(key, value);
    }

    public Object getGoogleClient(){
        return getTag("google-client");
    }

    public void setGoogleClient(Object value){
        setTag("google-client", value);
    }

//    public Object getGoogleAccount(){
//        return getTag("google-account");
//    }
//
//    public void setGoogleAccount(Object value){
//        setTag("google-account", value);
//    }

    public Object getFirebaseUser(){
        return getTag("fb-user");
    }

    public void setFirebaseUser(Object value){
        setTag("fb-user", value);
    }

    public String getAccessToken(){
        return getString("access_token");
    }

    public void setAccessToken(String token){
        setString("access_token", token);
    }

    public String getRefreshToken(){
        return getString("refresh_token");
    }

    public void setRefreshToken(String token){
        setString("refresh_token", token);
    }

    private String getString(String key){
        return prefs.contains(key) ? prefs.getString(key, "") : "";
    }

    private void setString(String key, String val){
        prefs.edit().putString(key, val).commit();
    }

}
