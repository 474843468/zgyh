package com.chinamworld.bocmbci.net.model;

/**
 * 通信返回数据结构基类
 * Created by yuht on 2016/10/19.
 */
public abstract class BaseResponseData {
    /** 是否是异常通信 */
    public abstract boolean isError();
    /** 获得异常信息 */
    public abstract String getErrorMessage();
    /** 获得异常错误码 */
    public abstract String getErrorCode();

}
