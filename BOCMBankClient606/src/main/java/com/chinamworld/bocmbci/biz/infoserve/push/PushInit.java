package com.chinamworld.bocmbci.biz.infoserve.push;

import android.content.Context;

import com.chinamworld.bocmbci.biz.push.PushSetting;
import com.igexin.sdk.PushManager;

/**
 * Created by Administrator on 2016/9/8.
 */
public class PushInit {
    public static void initPushManager(Context context) {
        PushManager.getInstance().initialize(context);
        changePushManagerStatus(context);
    }

    public static void changePushManagerStatus(Context context) {
        //
        if (PushSetting.getPushState()){
            PushManager.getInstance().turnOnPush(context);
        }else {
            PushManager.getInstance().turnOffPush(context);
        }
    }
}
