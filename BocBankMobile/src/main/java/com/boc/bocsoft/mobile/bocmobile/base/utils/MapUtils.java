package com.boc.bocsoft.mobile.bocmobile.base.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dding on 16/8/30.
 */
public class MapUtils {


    public static Map<String, Object> clzzField2Map(Object obj){
        HashMap<String,Object> maps = new HashMap<>();

        if(obj == null){
            return maps;
        }
        Class<?> aClass = obj.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        if(declaredFields == null || declaredFields.length == 0){
            return maps;
        }

        for(Field field:declaredFields){
            field.setAccessible(true);
            String name = field.getName();
            try {
                Object o = field.get(obj);
                maps.put(name,  o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return maps;
    }


}


