package com.boc.bocsoft.mobile.cr.common.client;


import com.boc.bocsoft.mobile.common.client.model.RequestParams;

/**
 * CRClient的配置
 */
public class CRClientConfig {

    private static volatile CRConfigInterface config;

    public static String getBiiUrl(){
        return config.getUrl();
    }

    /**
     * @return 通用参数，包括headers和urlParams
     */
    public static RequestParams getCommonParams(){
//        return config.getCommonParams();
        return null;
    }



    public static void setConfig(CRConfigInterface param){
        config = param;
    }
}
