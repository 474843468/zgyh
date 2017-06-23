package com.boc.bocsoft.mobile.bocmobile.base.cordova;

import android.app.Activity;
import java.util.concurrent.ExecutorService;
import org.apache.cordova.CordovaInterfaceImpl;
import org.apache.cordova.CordovaPlugin;

/**
 * 作者：XieDu
 * 创建时间：2016/12/7 10:48
 * 描述：
 */
public class BaseCordovaInterfaceImpl extends CordovaInterfaceImpl {
    public BaseCordovaInterfaceImpl(Activity activity) {
        super(activity);
    }

    public BaseCordovaInterfaceImpl(Activity activity, ExecutorService threadPool) {
        super(activity, threadPool);
    }

    public CordovaPlugin getActivityResultCallback(){
        return activityResultCallback;
    }
}
