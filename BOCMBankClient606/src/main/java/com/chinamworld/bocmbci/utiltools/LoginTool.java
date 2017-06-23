package com.chinamworld.bocmbci.utiltools;

import android.app.Activity;

import com.boc.bocsoft.mobile.bocmobile.buss.common.LoginCallback;
import com.boc.bocsoft.mobile.bocmobile.buss.common.lianlong.LoginConfiger;
import com.chinamworld.bocmbci.abstracttools.AbstractLoginTool;
import com.chinamworld.bocmbci.base.application.CommonApplication;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.llbt.utils.TimerRefreshTools;

/**
 * Created by Administrator on 2016/9/21.
 */
public class LoginTool extends AbstractLoginTool {
    @Override
    public void Login(final Activity activity, final LoginTask.LoginCallback loginCallback) {
//        new LoginConfiger().login(activity,new LoginCallback(){
//
//            @Override
//            public void success() {
//                if(loginCallback!=null){
//                    loginCallback.loginStatua(true);
//                }
//            }
//        });
//        CommonApplication.getInstance().clientLogOut();
        new LoginConfiger().loginForResult(activity,new LoginCallback(){

            @Override
            public void success() {
                if(loginCallback!=null){
//                    CommonApplication.getInstance().setCurrentAct(activity);
                    new TimerRefreshTools(20, new TimerRefreshTools.ITimerRefreshListener() {
                        @Override
                        public void onRefresh() {
                            loginCallback.loginStatua(true);
                        }
                    }).startTimer();

                }
            }
        },ConstantGloble.ACTIVITY_REQUEST_LOGIN_CODE);
    }

    @Override
    public void Login(final Activity activity, final LoginTask.LoginCallback loginCallback, int code) {
        new LoginConfiger().loginForResult(activity,new LoginCallback(){

            @Override
            public void success() {
                if(loginCallback!=null){
//                    CommonApplication.getInstance().setCurrentAct(activity);
//                    loginCallback.loginStatua(true);
                    new TimerRefreshTools(20, new TimerRefreshTools.ITimerRefreshListener() {
                        @Override
                        public void onRefresh() {
                            loginCallback.loginStatua(true);
                        }
                    }).startTimer();

                }
            }
        },code);
    }

    @Override
    public boolean IsLogin() {
        return new LoginConfiger().isLogin();
    }
}
