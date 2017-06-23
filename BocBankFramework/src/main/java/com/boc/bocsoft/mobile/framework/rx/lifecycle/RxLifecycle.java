package com.boc.bocsoft.mobile.framework.rx.lifecycle;

import rx.Observable;

import static com.boc.bocsoft.mobile.common.utils.PublicUtils.checkNotNull;

/**
 * Created by XieDu on 2016/5/21.
 */
public class RxLifecycle {

    public enum Event {
        START,
        DESTROY,
        BGTASK_DESTROY//后台任务destroy
    }

    public static <T, R> Observable.Transformer<T, T> bindUntilEvent(final Observable<R> lifecycle,
                                                                     final R event) {
        checkNotNull(lifecycle, "lifecycle == null");
        checkNotNull(event, "event == null");
        return new UntilEventObservableTransformer<T, R>(lifecycle, event);
    }
}
