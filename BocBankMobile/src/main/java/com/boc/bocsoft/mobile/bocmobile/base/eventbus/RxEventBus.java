package com.boc.bocsoft.mobile.bocmobile.base.eventbus;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * RX java事件总线
 * Created by lxw on 2016/10/24 0024.
 */
public class RxEventBus {

    private final Subject<Object, Object> bus = new SerializedSubject<>(PublishSubject.create());

    public void post(Object o) {
        bus.onNext(o);
    }

    public Observable<Object> getBusObservable() {
        return bus;
    }

}
