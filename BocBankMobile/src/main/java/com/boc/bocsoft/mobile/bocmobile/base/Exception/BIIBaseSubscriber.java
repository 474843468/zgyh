package com.boc.bocsoft.mobile.bocmobile.base.Exception;

import android.content.Intent;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BaseMobileActivity;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BroadcastConst;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.common.client.exception.HttpException;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;

import rx.Subscriber;

/**
 * 错误通用处理
 * Created by xdy on 2016/4/26.
 */
public abstract class BIIBaseSubscriber<T> extends Subscriber<T> {

    public final static String LOG_TAG = BIIBaseSubscriber.class.getSimpleName();
    BiiResultErrorException biiResultErrorException;
    

    @Override
    public void onError(Throwable e) {

        /**
         * 拿到当前弹出错误框的activity
         */
        BaseMobileActivity curActivity =
                (BaseMobileActivity) ActivityManager.getAppManager().currentActivity();
        /**
         * 输出错误日志
         */
        LogUtils.e(e.getMessage());
        StackTraceElement[] elements = e.getStackTrace();
        for (StackTraceElement element : elements) {
            LogUtils.e(element.toString());
        }
        if (e instanceof BiiResultErrorException) {
            LogUtils.i(LOG_TAG, "发生网络错误...");
            biiResultErrorException = (BiiResultErrorException) e;
            if (biiResultErrorException.getType().equals(HttpException.ExceptionType.RESULT)) {
                if (!StringUtils.isEmptyOrNull(biiResultErrorException.getErrorCode())) {
                    if (BiiResultErrorException.ERROR_CODE_ROLE_INVALID.equals(
                            biiResultErrorException.getErrorCode())
                            || BiiResultErrorException.ERROR_CODE_SESSION_INVALID.equals(
                            biiResultErrorException.getErrorCode())
                            || BiiResultErrorException.ERROR_CODE_SESSION_TIMEOUT.equals(
                            biiResultErrorException.getErrorCode())) {
                        // TODO 中银E贷超时处理  未完成
                        LogUtils.i(LOG_TAG, "BII会话已超时...");
                        doOnTimeOut();
                        if (curActivity != null) {
                            Intent intent =
                                    new Intent(BroadcastConst.INTENT_ACTION_SERVICE_TIMEOUT);
                            intent.putExtra(BaseMobileActivity.INTENT_MESSAGE, e.getMessage());
                            curActivity.sendBroadcast(intent);
                            return;
                        }
                    }
                    //else if (BiiResultErrorException.ERROR_CODE_PRODUCTEXCEPTION.equals(biiResultErrorException.getErrorCode())){
                    //    doOnException(biiResultErrorException);
                    //    return;
                    //}
                }
                //else {
                //    doOnException(biiResultErrorException);
                //}
            }
        } else if (e instanceof HttpException) {
            // TODO: 2016/4/27  网络连接错误
            biiResultErrorException = new BiiResultErrorException((HttpException) e);
            biiResultErrorException.setErrorMessage(
                    biiResultErrorException.getType() == HttpException.ExceptionType.BII_NULL
                            ? "通信错误" : "网络异常，请检查您的网络状态");
        } else {
            // TODO: 2016/4/27 其他类型的错误
            biiResultErrorException = new BiiResultErrorException(e.getMessage());
            biiResultErrorException.setType(HttpException.ExceptionType.OTHER);
            biiResultErrorException.setErrorMessage(e.getMessage());
        }
        doOnException(biiResultErrorException);
    }

    private void doOnException(BiiResultErrorException biiResultErrorException) {
        commonHandleException(biiResultErrorException);
        handleException(biiResultErrorException);
    }

    public void doOnTimeOut(){

    }

    /**
     * 通用错误处理
     */
    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
        //TODO 弹对话框显示错误信息
        BaseMobileActivity curActivity =
                (BaseMobileActivity) ActivityManager.getAppManager().currentActivity();
        curActivity.showErrorDialog(biiResultErrorException.getErrorMessage());
    }

    public abstract void handleException(BiiResultErrorException biiResultErrorException);
}

