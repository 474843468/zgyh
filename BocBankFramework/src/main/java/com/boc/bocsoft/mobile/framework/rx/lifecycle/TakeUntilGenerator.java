package com.boc.bocsoft.mobile.framework.rx.lifecycle;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by XieDu on 2016/5/23.
 */
public class TakeUntilGenerator {
    static <T> Observable<T> takeUntilEvent(final Observable<T> lifecycle, final T event) {
        return lifecycle.takeFirst(new Func1<T, Boolean>() {
            @Override
            public Boolean call(T lifecycleEvent) {
                return lifecycleEvent.equals(event);
            }
        });
    }

}
