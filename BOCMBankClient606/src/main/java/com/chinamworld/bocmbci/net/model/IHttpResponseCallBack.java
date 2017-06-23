package com.chinamworld.bocmbci.net.model;

/**
 * 通信返回数据接口
 * Created by Administrator on 2016/10/19.
 */
public interface IHttpResponseCallBack {
    public abstract boolean responseCallBack(String response, Object extendParams);
}
