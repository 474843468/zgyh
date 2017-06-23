package com.boc.bocsoft.mobile.bocmobile.base.widget.countdowntime;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.progressbar.CircleProgressBar;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 作者：XieDu
 * 创建时间：2016/9/26 17:38
 * 描述：倒计时组件，用于倒计时中定时查询接口，直到倒计时结束
 */
public class CountDownPollingView<T> extends LinearLayout {

    protected TextView tvTips;
    protected CircleProgressBar viewProgress;
    private View rootView;
    private int totalTime;//倒计时总时间
    private int period;//轮询间隔
    private String mCountDownString;//倒计时显示
    private CountDownPollingTaskListener<T> mCountDownPollingTaskListener;

    public CountDownPollingView(Context context) {
        this(context, null);
    }

    public CountDownPollingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownPollingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        rootView = inflate(getContext(), R.layout.boc_view_countdowntime, this);
        tvTips = (TextView) rootView.findViewById(R.id.tv_tips);
        viewProgress = (CircleProgressBar) rootView.findViewById(R.id.view_progress);
    }

    public void startCountDownTime(final Func1<Integer, Observable<T>> task) {
        startCountDownTime(task, new Func1<T, Boolean>() {
            @Override
            public Boolean call(T t) {
                return true;
            }
        });
    }

    public void startCountDownTime(final Func1<Integer, Observable<T>> task,
            Func1<T, Boolean> firstFilter) {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                  .take(totalTime + 1)
                  .compose(SchedulersCompat.<Long>applyIoSchedulers())
                  .doOnCompleted(new Action0() {
                      @Override
                      public void call() {
                          //System.out.println("倒计时结束");
                          if (mCountDownPollingTaskListener != null) {
                              mCountDownPollingTaskListener.onTaskFailed();
                          }
                      }
                  })
                  .doOnNext(new Action1<Long>() {
                      @Override
                      public void call(Long aLong) {
                          mCountDownString = String.valueOf(totalTime - aLong);
                          viewProgress.setText(mCountDownString);
                          viewProgress.setProgress(aLong.intValue());
                      }
                  })
                  .observeOn(Schedulers.io())
                  .concatMap(new Func1<Long, Observable<T>>() {
                      @Override
                      public Observable<T> call(Long aLong) {
                          //aLong是秒数，每隔period秒一次请求
                          if (aLong % period == 0) {

                              int count = aLong.intValue() / period;
                              return Observable.just(count)
                                               .flatMap(task)
                                               .onErrorResumeNext(
                                                       new Func1<Throwable, Observable<? extends T>>() {
                                                           @Override
                                                           public Observable<? extends T> call(
                                                                   Throwable throwable) {
                                                               return Observable.empty();
                                                           }
                                                       });
                          }
                          return Observable.empty();
                      }
                  })
                  .first(firstFilter)//只需要第一个返回的数据,如果倒计时结束还没有数据返回，那么会抛出NoSuchElementException
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Subscriber<T>() {
                      @Override
                      public void onCompleted() {

                      }

                      @Override
                      public void onError(Throwable throwable) {
                          //由于接口的错误在此前的onErrorResumeNext里已经拦截，此处错误只有一个那么会抛出NoSuchElementException，暂无数据返回，视为交易处理中
                      }

                      @Override
                      public void onNext(T t) {
                          if (mCountDownPollingTaskListener != null) {
                              mCountDownPollingTaskListener.onTaskSuccess(t);
                          }
                      }
                  });
    }

    public void setCountDownTimeListener(
            CountDownPollingTaskListener<T> countDownPollingTaskListener) {
        mCountDownPollingTaskListener = countDownPollingTaskListener;
    }

    public CountDownPollingView<T> setTotalTime(int totalTime) {
        this.totalTime = totalTime;
        viewProgress.setMax(totalTime);
        return this;
    }

    public CountDownPollingView<T> setPeriod(int period) {
        this.period = period;
        return this;
    }

    public CountDownPollingView<T> setTips(String tips) {
        tvTips.setText(tips);
        return this;
    }

    public interface CountDownPollingTaskListener<T> {

        void onTaskFailed();

        void onTaskSuccess(T t);
    }
}
