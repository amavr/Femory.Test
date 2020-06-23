package com.amavr.tools;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class File {

    public static String LoadAllText(Context context, String assetFileName){
        try {
            InputStream is = context.getAssets().open(assetFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        }
        catch(IOException ex){
            Log.e("XDBG", ex.getMessage());
            return null;
        }
    }
}
