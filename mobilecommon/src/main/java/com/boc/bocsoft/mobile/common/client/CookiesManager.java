package com.boc.bocsoft.mobile.common.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * 管理Cookie
 * Created by XieDu on 2016/2/26.
 */
public class CookiesManager implements CookieJar {
    private final CookieStore cookieStore = new CookieStore();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && !cookies.isEmpty()) {
            for (Cookie cookie : cookies) cookieStore.add(url, cookie);
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return cookieStore.get(url);
    }

    public List<Cookie> loadForRequest(String urlString) {
        List<Cookie> cookies = null;
        try {
            URL url = new URL(urlString);
            HttpUrl httpUrl = HttpUrl.get(url);
            cookies = loadForRequest(httpUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return cookies;
    }

    /**
     * 保存cookies
     * @param cookiesMap
     */
    public void saveCookies(HashMap<String, HashMap<String, String>> cookiesMap){
        for (Map.Entry<String, HashMap<String, String>> cookieInfo : cookiesMap.entrySet()) {

            //HttpUrl url =HttpUrl.parse("http://" + cookieInfo.getKey() + "/BII/_bfwajax.do");
            HttpUrl url =HttpUrl.parse(cookieInfo.getKey());
            for (Map.Entry<String, String> keyValueMap:cookieInfo.getValue().entrySet()) {
                Cookie cookie = Cookie.parse(url, keyValueMap.getValue());
                cookieStore.add(url, cookie);
            }
        }
    }

    /**
     * 清楚Cookies
     */
    public void clearCookies(){
        cookieStore.removeAll();
    }

    /**
     * 返回cookieStore
     * @return
     */
    public  CookieStore getCookies(){
        return cookieStore;
    }
}
