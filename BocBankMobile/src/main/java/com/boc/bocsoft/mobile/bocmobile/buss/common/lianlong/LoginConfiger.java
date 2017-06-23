package com.boc.bocsoft.mobile.bocmobile.buss.common.lianlong;

import android.app.Activity;
import android.content.Intent;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BaseMobileActivity;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BroadcastConst;
import com.boc.bocsoft.mobile.bocmobile.buss.common.LoginCallback;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;

/**
 * 登录
 * Created by lxw on 2016/8/11 0011.
 */
public class LoginConfiger {


    /**
     * 联龙功能模块需要登录时，调用该方法
     * @param activity
     * @param callback
     */
    public void login(Activity activity, LoginCallback callback){
        ApplicationContext.getInstance().logout();
        ModuleActivityDispatcher.startToLogin(activity, callback);

    }

    /**
     * 联龙功能模块需要登录时，调用该方法
     * @param activity
     * @param callback
     */
    public void loginForResult(Activity activity, LoginCallback callback, int code){
        ApplicationContext.getInstance().logout();
        ModuleActivityDispatcher.startToLoginForResult(activity, callback, code);

    }


    /**
     * 会话超时
     * @param activity
     */
    public void sessinoTimout(Activity activity){
        ApplicationContext.getInstance().logout();
        Intent intent =
                new Intent(BroadcastConst.INTENT_ACTION_SERVICE_TIMEOUT);
        intent.putExtra(BaseMobileActivity.INTENT_MESSAGE, "会话已超时，请重新登录。");
        activity.sendBroadcast(intent);
    }

    /**
     * 判断当前app是否登录
     * @return
     */
    public boolean isLogin(){
        return ApplicationContext.getInstance().getUser().isLogin();
    }

}
