package com.boc.bocsoft.mobile.bii.common.response;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bii.common.model.BIIResponse;
import com.boc.bocsoft.mobile.common.client.exception.HttpException;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by XieDu on 2016/3/9.
 */
public class BIIBeanParser<R> implements Func1<BIIResponse<R>, Observable<R>> {
    //    private Class<R> clazz;
    //    private Gson gson;
    //
    //    public BIIBeanParser(Class<R> clazz, Gson gson) {
    //        this.clazz = clazz;
    //        this.gson = gson;
    //    }

    @Override
    public Observable<R> call(BIIResponse<R> biiResponse) {
        if (null == biiResponse) {
            HttpException exception = new HttpException("通信错误");
            exception.setType(HttpException.ExceptionType.BII_NULL);
            return Observable.error(exception);
        }
        if (biiResponse.get_isException_()) {//如果返回信息中异常为true
            //TODO 异常
            BiiResultErrorException executeException = new BiiResultErrorException(biiResponse);

            return Observable.error(executeException);
        } else {
            //            String resultString = biiResponse.getResult()==null?"":biiResponse.getResult().toString();
            //            LoggerUtils.Info(resultString);
            //
            //            R result = gson.fromJson(resultString, clazz);

            return Observable.just(biiResponse.getResult());
        }
    }
}
