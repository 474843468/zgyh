package com.boc.bocsoft.mobile.bocmobile.buss.login.ui;

import com.boc.bocsoft.mobile.bocmobile.base.activity.BussActivity;

/**
 * Created by feib on 16/7/13.
 * 登录activity
 */
public class LoginBaseActivity extends BussActivity{

    @Override
    public void initView() {
        start(new LoginMainFragment());
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        //页面销毁 回调设置为null
        LoginContext.instance.setCallback(null);
    }
}
