package com.boc.bocsoft.mobile.common.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * 用于存储cookie
 * Created by XieDu on 2016/3/4.
 */
public class CookieStore {

    private Map<String, ConcurrentHashMap<String, Cookie>> cookies = null;

    public CookieStore() {
        cookies = new HashMap<String, ConcurrentHashMap<String, Cookie>>();
    }

    protected String getCookieToken(Cookie cookie) {
        return cookie.path()+cookie.name();
    }

    public void add(HttpUrl url, Cookie cookie) {
        String name = getCookieToken(cookie);
        if (!cookies.containsKey(url.host())) {
            cookies.put(url.host(), new ConcurrentHashMap<String, Cookie>());
        }
        cookies.get(url.host()).put(name, cookie);
    }

    public List<Cookie> get(HttpUrl url) {
        ArrayList<Cookie> cookieList = new ArrayList<Cookie>();
        if (cookies.containsKey(url.host()))
            cookieList.addAll(cookies.get(url.host()).values());
        return cookieList;
    }

    public void removeAll() {
        cookies.clear();
    }


    public boolean remove(HttpUrl url, Cookie cookie) {
        String name = getCookieToken(cookie);
        if (cookies.containsKey(url.host()) && cookies.get(url.host()).containsKey(name)) {
            cookies.get(url.host()).remove(name);
            return true;
        } else {
            return false;
        }
    }

    public List<Cookie> getCookies() {
        ArrayList<Cookie> cookieList = new ArrayList<Cookie>();
        for (String key : cookies.keySet())
            cookieList.addAll(cookies.get(key).values());
        return cookieList;
    }

}
