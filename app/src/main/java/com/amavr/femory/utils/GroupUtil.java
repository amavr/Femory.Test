package com.amavr.femory.utils;

import android.app.Application;
import android.util.Log;

import com.amavr.femory.models.GroupInfo;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class GroupUtil {

    private static final String TAG = "XDBG.GU";

    private static String loadTexts(Application app, String assetName){
        try {
            InputStream is = app.getAssets().open(assetName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        }
        catch(IOException ex){
            Log.e("EPRST", ex.getMessage());
            return null;
        }
    }

    public static List<GroupInfo> loadGroups(Application app, String assetName){
        String json = loadTexts(app, assetName);
        Gson gson = new Gson();
        DataInfo di = gson.fromJson(json, DataInfo.class);
        return di.groups;
    }

    public class DataInfo{
        public List<GroupInfo> groups;
    }
}
