package com.boc.bocsoft.mobile.bocmobile.base.widget.userwidget.echarsview;


import com.google.gson.Gson;

/**
 * Json格式化工具类
 * Created by yuht on 2016/10/18.
 */
public class GsonTools {

    /**
     * 将指定的对象序列化为json字符串
     */
    public static String toJson(Object Objcet){
        Gson gson = new Gson();
        return gson.toJson(Objcet);
    }

    /**
     * 将Json字符串转成指定的对象
     * @param json ：Ｊｓｏｎ字符串
     * @param type :指定对象的类型
     * @param <T> : 指定的对象
     * @return ：反序列化后的结构
     */
    public static <T> T fromJson(String json, Class<T> type){
        Gson gson = new Gson();
        return gson.fromJson(json,type);
    }
}
