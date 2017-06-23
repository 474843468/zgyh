package com.boc.bocsoft.mobile.framework.rx.lifecycle;

import rx.Observable;
import rx.Observable.Transformer;

/**
 * Created by XieDu on 2016/5/23.
 */
public interface LifecycleProvider {

    Observable<RxLifecycle.Event> lifecycle();

    <T> Transformer<T, T> bindUntilEvent(RxLifecycle.Event event);

    <T> Transformer<T, T> bindToLifecycle();
}
