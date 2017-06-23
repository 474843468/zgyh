package com.chinamworld.bocmbci.net.model;

/**
 * 通信返回数据接口
 * Created by yuht on 2016/10/20.
 */
public interface IHttpErrorCallBack {

    /**
     * 通信失败回调
     * @param exceptionMessage
     * @return 返回true,表示已处理，不需要公共机制继续处理
     */
    boolean onError(String exceptionMessage, Object extendParams);

}
