package com.boc.bocsoft.mobile.bocmobile.buss.common.eventbus;

import com.boc.bocsoft.mobile.bocmobile.base.eventbus.RxEventBus;

/**
 *
 * 事件总线
 * Created by lxw on 2016/10/31 0031.
 */
public class BocEventBus extends RxEventBus{

    private static BocEventBus instance;

    public static BocEventBus getInstance() {
        if (instance == null)
            instance = new BocEventBus();
        return instance;
    }

    private BocEventBus() {
    }
}
