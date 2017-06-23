package com.boc.bocsoft.mobile.framework.ui;

import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycle;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import rx.Observable;

/**
 * Created by xdy4486 on 2016/6/24.
 */
public class RxPresenter implements BasePresenter {
    private RxLifecycleManager mRxLifecycleManager = new RxLifecycleManager();

    protected <T> Observable.Transformer<T, T> bindToLifecycle() {
        return mRxLifecycleManager.bindToLifecycle();
    }

    protected <T> Observable.Transformer<T, T> bindUntilEvent(RxLifecycle.Event event) {
        return mRxLifecycleManager.bindUntilEvent(event);
    }

    @Override
    public void subscribe() {
        mRxLifecycleManager.onStart();
    }

    @Override
    public void unsubscribe() {
        mRxLifecycleManager.onDestroy();
    }

    public void destroyEvent(RxLifecycle.Event event) {
        mRxLifecycleManager.onDestroyEvent(event);
    }

    public void destroyBgTask() {
        destroyEvent(RxLifecycle.Event.BGTASK_DESTROY);
    }
}
