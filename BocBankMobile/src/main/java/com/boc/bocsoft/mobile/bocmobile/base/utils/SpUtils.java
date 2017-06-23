package com.boc.bocsoft.mobile.bocmobile.base.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by huixiaobo on 2016/7/22.
 * 本地存储
 */
public class SpUtils {
    private final static int VERSION = 1;//工具版本号

    public static final String SP_NAME ="com.boc.bocsoft.mobile.bocmobile";
    public static SharedPreferences sp;

    private static void initDefaultSp(Context context){
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, context.MODE_PRIVATE);
        }
    }

    public static boolean remove(Context context, String key){
        initDefaultSp(context);
       return sp.edit().remove(key).commit();
    }
    public static String saveString(Context context, String key, String value) {
        initDefaultSp(context);
        sp.edit().putString(key, value).commit();
        return key;
    }

    public static String getSpString(Context context, String key, String defValue) {
        initDefaultSp(context);
        return sp.getString(key, defValue);
    }

    public static String saveBoolean(Context context, String key, boolean value) {
        initDefaultSp(context);
        sp.edit().putBoolean(key, value).commit();
        return key;
    }

    public static boolean getSpBoolean(Context context, String key, boolean defValue) {
        initDefaultSp(context);
        return sp.getBoolean(key, defValue);
    }


    public static void saveSpFloat(Context context,String key,float value){
        initDefaultSp(context);
        sp.edit().putFloat(key,value).commit();
    }

    public static int getSpInt(Context context, String key, int defaultValue){
        initDefaultSp(context);
        return sp.getInt(key,defaultValue);
    }

    public static void saveSpInt(Context context,String key,int value){
        initDefaultSp(context);
        sp.edit().putInt(key,value).commit();
    }

    public static float getSpFloat(Context context, String key, float defaultValue){
        initDefaultSp(context);
        return sp.getFloat(key,defaultValue);
    }



    // -----------  联龙 sp ---
    private static final String LN_LOCAL_NAME = "local_shared_file";

    /**
     * 存储  - 适配联龙旧的数据 （新存储不要调用该方法）硬件绑定信息
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static String saveLNString(Context context, String key, String value) {
        SharedPreferences   sp = context.getSharedPreferences(LN_LOCAL_NAME, context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
        return key;
    }

    /**
     * 获取 - 联龙旧的数据  （新存储不要调用该方法）硬件绑定信息
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static String getLNSpString(Context context, String key, String defValue) {
        SharedPreferences   sp = context.getSharedPreferences(LN_LOCAL_NAME, context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    //存储登录手机号位置，和老版本联龙保持一致
    public  static final String CONFIG_FILE = "com.android.chinamworld.bocmbci.guide.config";
    /**
     * 存储  - 适配联龙旧的数据 （新存储不要调用该方法）登录手机号
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static String saveLNPhoneString(Context context, String key, String value) {
        SharedPreferences   sp = context.getSharedPreferences(CONFIG_FILE, context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
        return key;
    }

    /**
     * 获取 - 联龙旧的数据  （新存储不要调用该方法）登录手机号
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static String getLNhoneSpString(Context context, String key, String defValue) {
        SharedPreferences   sp = context.getSharedPreferences(CONFIG_FILE, context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static void removeLNhoneSpString(Context context, String key){
        SharedPreferences   sp = context.getSharedPreferences(CONFIG_FILE, context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }

  /**
   * sp存储key定义
   */
  public static class SPKeys{

      /**
       * 投资广告条高度 int
       */
      public static String KEY_INVEST_BANNER_HEIGHT = "inh";

    /**
     * 登录名 string
     */
    public static String KEY_LOGINNAME = "loginName";
      /**
       * 登录证件类型 string
       */
      public static String KEY_LOGINIDENTITYTYPE = "identityType";
      /**
       * 登录证件号码 string
       */
      public static String KEY_LOGINIDENTITYNUMBER = "identityNumber";

    /**
     * 资产页面 睁眼闭眼标示
     */
    public static String KEY_INVEST_SECRET = "inverst_secret";

      public static String KEY_LIFE_HASCHOOSE_LOCATION = "life_choose";
    }
}
