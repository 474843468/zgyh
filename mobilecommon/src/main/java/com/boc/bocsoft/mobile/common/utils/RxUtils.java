package com.boc.bocsoft.mobile.common.utils;

import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by XieDu on 2016/4/6.
 */
public class RxUtils {

    /**
     * 对列表中各元素在指定线程池里执行并发请求,如果有请求失败的情况，忽略它，不影响其他元素的请求。
     *
     * @param func1 要执行的请求
     * @param <T> 列表元素类型
     * @param <R> 要返回的Observable中的类型
     * @return Observable
     */
    public static <T, R> Observable.Transformer<List<T>, R> concurrentListRequestTransformer(
            final Func1<T, Observable<R>> func1, final Scheduler scheduler) {
        return new Observable.Transformer<List<T>, R>() {
            @Override
            public Observable<R> call(Observable<List<T>> listObservable) {
                return listObservable.concatMap(new Func1<List<T>, Observable<R>>() {
                    @Override
                    public Observable<R> call(List<T> list) {
                        final CopyOnWriteArrayList<T> tempList = new CopyOnWriteArrayList<T>(list);
                        return Observable.from(tempList).concatMap(new Func1<T, Observable<R>>() {
                            @Override
                            public Observable<R> call(final T t) {
                                return Observable.just(t).doOnNext(new Action1<T>() {
                                    @Override
                                    public void call(T t) {
                                        tempList.remove(t);
                                    }
                                }).subscribeOn(scheduler).flatMap(func1);
                            }
                        }).retry();
                    }
                });
            }
        };
    }

    /**
     * 对列表中各元素在io线程池里执行并发请求,如果有请求失败的情况，忽略它，不影响其他元素的请求。
     */
    public static <T, R> Observable.Transformer<List<T>, R> concurrentInIOListRequestTransformer(
            final Func1<T, Observable<R>> func1) {
        return concurrentListRequestTransformer(func1, Schedulers.io());
    }

    /**
     * 根据文件名得到文件的mimetype
     *
     * @param fileName 文件名
     * @return mimetype
     */
    public static String guessMimeType(String fileName) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(fileName);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
}
