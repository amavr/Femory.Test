package com.amavr.femory.utils;

import android.app.Application;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

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
}
