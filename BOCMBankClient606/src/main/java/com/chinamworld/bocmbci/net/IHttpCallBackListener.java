package com.chinamworld.bocmbci.net;

import android.content.Context;

/**
 * Created by Administrator on 2016/10/17.
 */
public interface IHttpCallBackListener {

    /**
     * 通信成功回调
     * @param response
     * @return 返回true,表示已处理，不需要公共机制继续处理
     */
    boolean onResponse(Context context, String response, Object extendParams);
    /**
     * 通信失败回调
     * @param exceptionMessage
     * @return 返回true,表示已处理，不需要公共机制继续处理
     */
    boolean onError(Context context, String exceptionMessage, Object extendParams);
}
