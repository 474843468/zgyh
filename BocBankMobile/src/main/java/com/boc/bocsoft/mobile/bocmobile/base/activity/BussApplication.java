package com.boc.bocsoft.mobile.bocmobile.base.activity;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * 通用application类
 * Created by lxw on 2016/7/22 0022.
 */
public abstract class BussApplication extends Application {
    public abstract ModuleFactory getModuleFactory();

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
