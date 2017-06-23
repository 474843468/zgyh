package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.deposit;

import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * 作者：XieDu
 * 创建时间：2016/9/8 19:24
 * 描述：
 */
public class PledgeReceiptAdapterTest {

    @Test
    public void testGetCdPeriodString() throws Exception {
        final int totalTime = 20;
        final RxLifecycleManager rxLifecycleManager = new RxLifecycleManager();
        Observable.interval(0, 1, TimeUnit.SECONDS)
                  .take(totalTime + 1)
                  .doOnCompleted(new Action0() {
                      @Override
                      public void call() {
                          System.out.println("倒计时结束");
                          rxLifecycleManager.onDestroy();
                      }
                  })
                  .flatMap(new Func1<Long, Observable<String>>() {
                      @Override
                      public Observable<String> call(Long aLong) {
                          final long countdown = totalTime - aLong;
                          System.out.println("倒计时:" + countdown + "s");
                          //aLong是秒数，5秒一次请求，最多四次请求
                          if (aLong % 5 == 0) {
                              int count = aLong.intValue() / 5 + 1;
                              return qryInPresenter(count).compose(
                                      rxLifecycleManager.<String>bindToLifecycle())
                                                          .onErrorResumeNext(new Func1<Throwable, Observable<? extends String>>() {
                                                              @Override
                                                              public Observable<? extends String> call(
                                                                      Throwable throwable) {
                                                                  return Observable.empty();
                                                              }
                                                          });
                          }
                          return Observable.empty();
                      }
                  })
                  .first(new Func1<String, Boolean>() {
                      @Override
                      public Boolean call(String s) {
                          return false;
                      }
                  })
                  .subscribe(new Subscriber<String>() {
                      @Override
                      public void onCompleted() {
                          System.out.println("onCompleted");
                          rxLifecycleManager.onDestroy();
                      }

                      @Override
                      public void onError(Throwable throwable) {
                          System.out.println("onError:" + throwable);
                      }

                      @Override
                      public void onNext(String string) {
                          System.out.println("onNext:" + string);
                      }
                  });
        Thread.sleep(30000);
    }

    Observable<String> qryInPresenter(final int number) {
        if (number == 1) {
            return Observable.error(new Exception("通信错误"));
        }
        if (number == 2) {
            return Observable.timer(8, TimeUnit.SECONDS).map(new Func1<Long, String>() {
                @Override
                public String call(Long aLong) {
                    return String.format("第%1$s次查询接口", number);
                }
            });
        }
        if (number < 4) {
            return Observable.timer(1, TimeUnit.SECONDS).map(new Func1<Long, String>() {
                @Override
                public String call(Long aLong) {
                    return String.format("第%1$s次查询接口", number);
                }
            });
        }
        return Observable.timer(1, TimeUnit.SECONDS).map(new Func1<Long, String>() {
            @Override
            public String call(Long aLong) {
                return String.format("第%1$s次查询接口", number) + "，这是最后一次";
            }
        });
    }
}