package com.boc.bocsoft.mobile.bocyun.common.response;

import com.boc.bocsoft.mobile.bocyun.common.YunException.YunResultErrorException;
import com.boc.bocsoft.mobile.bocyun.common.model.YunResponse;
import com.boc.bocsoft.mobile.common.client.exception.HttpException;
import rx.Observable;
import rx.functions.Func1;

public class YunBeanParser<R> implements Func1<YunResponse<R>, Observable<R>> {

  @Override public Observable<R> call(YunResponse<R> response) {
    if (null == response) {
      HttpException exception = new HttpException("通信错误");
      exception.setType(HttpException.ExceptionType.OTHER);

      return Observable.error(exception);
    } else if (response.isException()) {

      YunResultErrorException e = new YunResultErrorException(response);

      return Observable.error(e);
    } else {

      return Observable.just(response.getResult());

    }
  }
}
