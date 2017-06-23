package com.boc.bocsoft.mobile.mbcg;

import com.boc.bocsoft.mobile.common.client.exception.HttpException;
import rx.Observable;
import rx.functions.Func1;

public class MBCGBeanParser<R> implements Func1<MBCGResponse<R>, Observable<R>> {

  @Override public Observable<R> call(MBCGResponse<R> response) {
    if (null == response) {
      HttpException exception = new HttpException("通信错误");
      exception.setType(HttpException.ExceptionType.OTHER);

      return Observable.error(exception);
    } else if (response.isException()) {
      return Observable.error(new Exception(response.getType()));
    } else {
      return Observable.just(response.getResult());
    }
  }
}
