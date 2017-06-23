package com.boc.bocsoft.mobile.bocmobile.base.widget.crop.enjoycrop.core.debug;

import android.util.Log;

/**
 * Created by Zhouztashin on 2016/4/22.
 */
public class L {
    private final static String TAG = "EnjoyCrop";
    private final  static boolean LOG_ENABLE = false;
    public static void error(String message){
        if(LOG_ENABLE){
            Log.e(TAG,message+"");
        }
    }
}
