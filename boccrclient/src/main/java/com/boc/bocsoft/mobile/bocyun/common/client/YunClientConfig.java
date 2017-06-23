package com.boc.bocsoft.mobile.bocyun.common.client;


import com.boc.bocsoft.mobile.common.client.model.RequestParams;

/**
 * BIIClient的配置
 * Created by XieDu on 2016/2/23.
 */
public class YunClientConfig {
    public static String DATETIME_FORMAT = "yyyy/MM/dd HH:mm:ss";
    public static String DATETIME_FORMAT_1 = "yyyy/MM/dd HH:mm";
    public static String DATE_FORMAT_V2_1 = "yyyy/MM/dd";
    public static String DATE_FORMAT_V2_2 = "yyyy-MM-dd";
    public static String DATE_FORMAT_V2_3 = "MM月/yyyy";
    public static String DATE_FORMAT_V2_4 = "yyyy/MM";

    private static volatile YunConfigInterface config;



    public static String getUrl(){
        return config.getUrl();
    }

    /**
     * @return 通用参数，包括headers和urlParams
     */
    public static RequestParams getCommonParams(){
//        return config.getCommonParams();
        return null;
    }



    public static void setConfig(YunConfigInterface param){
        config = param;
    }
}
