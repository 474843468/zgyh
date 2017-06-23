package com.boc.bocsoft.mobile.bocmobile.base.activity;

import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycle;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

/**
 * 作者：XieDu
 * 创建时间：2016/11/3 19:46
 * 描述：
 */
public abstract class RxBussFragment<P extends RxPresenter> extends MvpBussFragment<P> {

    public void destroyEvent(RxLifecycle.Event event) {
        if (getPresenter() != null) {
            getPresenter().destroyEvent(event);
        }
    }

    public void destroyBgTask() {
        if (getPresenter() != null) {
            getPresenter().destroyBgTask();
        }
    }

    @Override
    public void onDestroy() {
        destroyBgTask();//super里会把普通任务取消，这里取消一下后台任务就行。
        super.onDestroy();
    }
}
