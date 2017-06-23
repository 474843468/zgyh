package com.boc.bocsoft.mobile.bocmobile.buss.system.main.eventbus;

import com.boc.bocsoft.mobile.bocmobile.base.eventbus.RxEventBus;

/**
 * 主页面的事件总线
 * Created by lxw on 2016/10/24 0024.
 */
public class MainBus extends RxEventBus {

    private static MainBus instance;

    public static MainBus getInstance() {
        if (instance == null)
            instance = new MainBus();
        return instance;
    }

    private MainBus() {
    }
}
