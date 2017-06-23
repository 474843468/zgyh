package com.chinamworld.bocmbci.net.model;

/**
 * OkHttp通信基类
 * Created by Administrator on 2016/10/18.
 */
public abstract class BaseOkHttpModel {
    /** 获得通信URL */
    public abstract String getUrl();
    /** 设置扩展参数。此参数用于回调时回传。可用于区分接口 */
    public abstract Object getExtendParam();
    /**将对象转换为Json数据*/
    public abstract String toJson();
}
