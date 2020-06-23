package com.amavr.tools;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.amavr.femory.models.GroupInfo;
import com.amavr.femory.models.ItemInfo;
import com.amavr.femory.utils.GroupUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XMem {

    final static String TAG = "XDBG.mem";
    final static String PREF_FILE = "app";

    private static XMem instance = null;

    private SharedPreferences prefs = null;
    private Application app;
    private HashMap<String, Object> dic;
    private List<GroupInfo> groups;
    private int groupIndex = 0;

    public static XMem create(Application A){
        if (instance == null) {
            instance = new XMem();
            instance.app = A;
            instance.prefs = A.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
            instance.dic = new HashMap<String, Object>();
            instance.loadGroups();
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

    private void loadGroups(){
        try {
            groups = GroupUtil.loadGroups(app, "data.json");
            Log.d(TAG, String.format("groups: %s", groups.size()));
        }
        catch(Exception ex){
            Log.e(TAG, ex.getMessage());
        }
    }

    public List<String> getGroupsNames(){
        List<String> names = new ArrayList<String>();
        for(GroupInfo g : groups) names.add(g.getName());
        return names;
    }

    public List<GroupInfo> getGroups(){
        return groups;
    }

    public void setGroup(int index){
        this.groupIndex = index;
    }

    public List<ItemInfo> getItems(){
        List<ItemInfo> items = new ArrayList<ItemInfo>();
        items.addAll(groups.get(groupIndex).getItems());
        return items;
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
