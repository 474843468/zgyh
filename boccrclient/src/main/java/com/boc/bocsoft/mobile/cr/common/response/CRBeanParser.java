package com.boc.bocsoft.mobile.cr.common.response;

import com.boc.bocsoft.mobile.cr.common.CRException.CRResultErrorException;
import com.boc.bocsoft.mobile.cr.common.model.CRResponse;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by XieDu on 2016/3/9.
 */
public class CRBeanParser<R> implements Func1<CRResponse<R>, Observable<R>> {

  @Override public Observable<R> call(CRResponse<R> biiResponse) {
    if (null == biiResponse) {
      return Observable.just(null);
    }
    if (biiResponse.get_isException_()) {//如果返回信息中异常为true
      CRResultErrorException executeException = new CRResultErrorException(biiResponse);
      return Observable.error(executeException);
    } else {
      return Observable.just(biiResponse.getResult());
    }
  }
}
