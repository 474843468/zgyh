package com.boc.bocsoft.mobile.bocmobile.base.widget.PullToRefreshSwipeMenuListView;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gengjunying on 2016/12/12.
 */
public class RefreshTime {
    final static String PRE_NAME = "refresh_time";
    final static String SET_FRESHTIME = "set_refresh_time";

    public static String getRefreshTime(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PRE_NAME, Context.MODE_APPEND);
        return preferences.getString(SET_FRESHTIME, "");
    }

    public static void setRefreshTime(Context context, String newPasswd) {
        SharedPreferences preferences = context.getSharedPreferences(PRE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SET_FRESHTIME, newPasswd);
        editor.commit();
    }
}
