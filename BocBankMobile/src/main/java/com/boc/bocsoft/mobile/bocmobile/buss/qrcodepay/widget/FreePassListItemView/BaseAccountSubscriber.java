package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.widget.FreePassListItemView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.common.client.exception.HttpException;

/**
 * @author wangf
 *         16/6/28 19:43
 *         Subscriber
 */
public abstract class BaseAccountSubscriber<T> extends BIIBaseSubscriber<T> {

    @Override
    public void handleException(BiiResultErrorException error) {

    }

    /**
     * 生成Result类型异常信息,调用onError方法
     * @param message
     */
    public void onResultError(String message) {
        BiiResultErrorException biiResultErrorException = new BiiResultErrorException(message);
        biiResultErrorException.setType(HttpException.ExceptionType.RESULT);
        biiResultErrorException.setErrorMessage(message);
        super.onError(biiResultErrorException);
    }

    /**
     * 生成NetWork类型异常信息,调用onError方法
     * @param message
     */
    public void onNetWorkError(String message) {
        BiiResultErrorException biiResultErrorException = new BiiResultErrorException(message);
        biiResultErrorException.setType(HttpException.ExceptionType.NETWORK);
        biiResultErrorException.setErrorMessage(message);
        super.onError(biiResultErrorException);
    }

    /**
     * 生成Other类型异常信息,调用onError方法
     * @param message
     */
    public void onOtherError(String message) {
        BiiResultErrorException biiResultErrorException = new BiiResultErrorException(message);
        biiResultErrorException.setType(HttpException.ExceptionType.OTHER);
        biiResultErrorException.setErrorMessage(message);
        super.onError(biiResultErrorException);
    }

    @Override
    public void onCompleted() {}

    @Override
    public void onNext(T t) {
    }
}
