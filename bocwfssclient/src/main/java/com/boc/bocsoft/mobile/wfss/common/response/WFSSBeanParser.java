package com.boc.bocsoft.mobile.wfss.common.response;


import com.boc.bocsoft.mobile.wfss.common.exception.WFSSResultErrorException;
import com.boc.bocsoft.mobile.wfss.common.model.WFSSResponse;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by XieDu on 2016/3/9.
 */
public class WFSSBeanParser<R> implements Func1<WFSSResponse<R>, Observable<R>> {

  @Override public Observable<R> call(WFSSResponse<R> biiResponse) {
    if (null == biiResponse) {
      return Observable.just(null);
    }
    if (!"00".equals(biiResponse.getHead().getStat())) {//如果返回信息中异常为true
      WFSSResultErrorException executeException = new WFSSResultErrorException(biiResponse);
      executeException.setErrorCode(biiResponse.getHead().getStat());
      executeException.setErrorMessage(biiResponse.getHead().getResult());
      return Observable.error(executeException);
    } else {
      return Observable.just(biiResponse.getBody());
    }
  }
}
