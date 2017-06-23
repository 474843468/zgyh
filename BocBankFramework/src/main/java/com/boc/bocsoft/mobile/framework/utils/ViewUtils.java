package com.boc.bocsoft.mobile.framework.utils;

import android.os.Looper;
import android.view.View;
import com.jakewharton.rxbinding.view.RxView;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 作者：XieDu
 * 创建时间：2016/11/14 22:48
 * 描述：
 */
public class ViewUtils {

    /**
     * 设置双击事件
     *
     * @param view 被双击的view
     * @param action 双击事件
     */
    public static void doubleClick(final View view, final Action1<View> action) {
        Observable<Void> observable = RxView.clicks(view).share();
        observable.buffer(observable.debounce(200, TimeUnit.MILLISECONDS))
                  .filter(new Func1<List<Void>, Boolean>() {
                      @Override
                      public Boolean call(List<Void> voids) {
                          return voids != null && voids.size() >= 2;
                      }
                  })
                  .map(new Func1<List<Void>, View>() {
                      @Override
                      public View call(List<Void> voids) {
                          return view;
                      }
                  })
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(action);
    }

    public static void invalidate(View view){
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            view.invalidate();
        } else {
            view.postInvalidate();
        }
    }
}
