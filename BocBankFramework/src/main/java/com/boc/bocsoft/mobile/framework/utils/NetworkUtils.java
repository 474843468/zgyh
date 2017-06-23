package com.boc.bocsoft.mobile.framework.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import com.boc.bocsoft.mobile.bii.common.client.BIIClient;
import com.boc.bocsoft.mobile.common.client.CookieStore;
import java.lang.reflect.Field;
import okhttp3.Cookie;

/**
 * 网络工具类
 * Created by lxw on 2016/10/30 0030.
 */
public class NetworkUtils {

    /**
     * 检查当前是否有网络
     */
    public static boolean haveNetworkConnection(Context context) {

        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();

        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
                if (ni.isConnected()) {
                    haveConnectedWifi = true;
                }
            }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
                if (ni.isConnected()) {
                    haveConnectedMobile = true;
                }
            }
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static void setWebViewCookie(WebView webView, String cookieUrl) {
        CookieManager cookieManager;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        } else {
            CookieSyncManager.createInstance(webView.getContext());
            cookieManager = CookieManager.getInstance();
        }
        cookieManager.setAcceptCookie(true);
        CookieStore cookieStore = BIIClient.instance.getCookies();
        for (Cookie cookie : cookieStore.getCookies()) {
            if (cookieUrl.contains(cookie.domain())) {
                String cookieString = cookie.name() + "=" + cookie.value() + ";";
                cookieManager.setCookie(cookieUrl, cookieString);
            }
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().sync();
        } else {
            cookieManager.flush();
        }
    }

    public static String getFormBodyString(Object object) {
        String result = "";
        if (object == null) {
            return result;
        }
        StringBuilder stringBuilder = new StringBuilder();
        Field[] fieldList = object.getClass().getDeclaredFields();
        for (Field field : fieldList) {
            try {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object fieldValue = field.get(object);
                if (fieldValue != null) {
                    stringBuilder.append("&")
                                 .append(fieldName)
                                 .append("=")
                                 .append(fieldValue.toString());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (stringBuilder.length() != 0) {
            result = stringBuilder.substring(1);
        }
        return result;
    }
}
