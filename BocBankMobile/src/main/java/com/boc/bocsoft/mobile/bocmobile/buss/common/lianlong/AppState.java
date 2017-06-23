package com.boc.bocsoft.mobile.bocmobile.buss.common.lianlong;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;

/**
 * App状态
 * Created by lxw on 2016/8/18 0018.
 */
public enum AppState {
    instance; // 单例对象

    /**
     * 是否登录
     * @return
     */
    public boolean isLogin(){
        return ApplicationContext.getInstance().getUser().isLogin();
    }
}
