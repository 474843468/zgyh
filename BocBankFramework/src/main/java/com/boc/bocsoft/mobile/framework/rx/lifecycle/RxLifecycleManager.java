package com.boc.bocsoft.mobile.framework.rx.lifecycle;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * 使用说明：待绑定生命周期管理的类，需要mRxLifecycleManager=new RxLifecycleManager();
 * 然后对于其中待绑定的任务，需要observable.compose(mRxLifecycleManager.bindToLifecycle());
 * 最后在需要生命周期结束注销任务的地方，mRxLifecycleManager.onDestroy();
 * <p>
 * Created by XieDu on 2016/5/23.
 */
public class RxLifecycleManager implements LifecycleProvider {
    private final BehaviorSubject<RxLifecycle.Event> lifecycleSubject = BehaviorSubject.create();

    public void onStart() {
        lifecycleSubject.onNext(RxLifecycle.Event.START);
    }

    public void onDestroy() {
        lifecycleSubject.onNext(RxLifecycle.Event.DESTROY);
    }

    public void onDestroyEvent(RxLifecycle.Event event) {
        lifecycleSubject.onNext(event);
    }

    @Override
    public Observable<RxLifecycle.Event> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Override
    public <T> Observable.Transformer<T, T> bindUntilEvent(RxLifecycle.Event event) {
        return RxLifecycle.bindUntilEvent(lifecycle(), event);
    }

    /**
     * 调用RxLifecycleManager的onDestroy方法时事件会被取消订阅
     *
     * @param <T> compose该方法的类的泛型类型
     * @return Transformer
     */
    @Override
    public <T> Observable.Transformer<T, T> bindToLifecycle() {
        return bindUntilEvent(RxLifecycle.Event.DESTROY);
    }
}
