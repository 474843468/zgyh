package com.chinamworld.bocmbci.net.model;

/**
 * OkHttp通信的错误码处理接口
 * Created by yuht on 2016/11/1.
 */
public interface IOkHttpErrorCode {

    /**
     * 通信数据异常时，处理错误码
     * @param responseData ： 返回数据
     * @param extendParams  ： 扩展参数，一般为能够标识接口的数据
     * @return  true:已处理。不在继续向下执行。false：不处理错误码，继续执行
     */
    boolean handlerErrorCode(BaseResponseData responseData, Object extendParams);
}
