package com.boc.bocsoft.mobile.bii.common.client;


import com.boc.bocsoft.mobile.common.client.model.RequestParams;

/**
 * BIIClient的配置
 * Created by XieDu on 2016/2/23.
 */
public class BIIClientConfig {
//volatile是一个类型修饰符（type specifier）。它是被设计用来修饰被不同线程访问和修改的变量。和c语言的const一样
    private static volatile BiiConfigInterface config;//

    public static String getBiiUrl(){
        return config.getBiiUrl();
    }

    public static String getBPMSUrl(){
        return config.getBMPSUrl();
    }

    public static String getVaryficationCodeUrl(){
        return config.getVaryficationCodeUrl();
    }

    /**
     * @return 通用参数，包括headers和urlParams
     */
    public static RequestParams getCommonParams(){
//        return config.getCommonParams();
        return null;
    }



    public static void setConfig(BiiConfigInterface param){
        config = param;
    }
}
