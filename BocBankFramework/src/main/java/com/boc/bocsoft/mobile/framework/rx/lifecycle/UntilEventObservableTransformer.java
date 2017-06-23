package com.boc.bocsoft.mobile.framework.rx.lifecycle;

import rx.Observable;

/**
 * Created by XieDu on 2016/5/23.
 */
public class UntilEventObservableTransformer<T, R> implements Observable.Transformer<T, T> {

    final Observable<R> lifecycle;
    final R event;

    public UntilEventObservableTransformer(Observable<R> lifecycle, R event) {
        this.lifecycle = lifecycle;
        this.event = event;
    }

    @Override
    public Observable<T> call(Observable<T> source) {
        return source.takeUntil(TakeUntilGenerator.takeUntilEvent(lifecycle, event));
    }
}
