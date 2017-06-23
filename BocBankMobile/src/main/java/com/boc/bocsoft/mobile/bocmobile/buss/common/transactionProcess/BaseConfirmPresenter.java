package com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess;

import android.support.annotation.NonNull;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetRandom.PSNGetRandomParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.BaseFillInfoBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;
import rx.Observable;
import rx.functions.Func1;

public abstract class BaseConfirmPresenter<T extends BaseFillInfoBean,D> extends RxPresenter
        implements BaseConfirmContract.Presenter<T> {

    protected BaseConfirmContract.View<D> mView;

    protected GlobalService mGlobalService;
    protected PsnLoanService mLoanService;
    protected String mConversationId;
    protected String mRandom;
    protected VerifyBean mVerifyBean;

    public BaseConfirmPresenter(BaseConfirmContract.View<D> view) {
        mView = view;
        mGlobalService = new GlobalService();
        mLoanService = new PsnLoanService();
    }

    @NonNull
    protected abstract Observable<VerifyBean> getVerifyBean(T fillInfo);

    @Override
    public void verify(boolean securityTypeChanged, T fillInfo) {
        mConversationId = fillInfo.getConversationId();
        ((securityTypeChanged || mVerifyBean == null) ? getVerifyBean(fillInfo)
                : Observable.just(mVerifyBean)).compose(this.<VerifyBean>bindToLifecycle())
                                               .flatMap(
                                                       new Func1<VerifyBean, Observable<String>>() {
                                                           @Override
                                                           public Observable<String> call(
                                                                   VerifyBean verifyBean) {
                                                               mVerifyBean = verifyBean;
                                                               PSNGetRandomParams randomParams =
                                                                       new PSNGetRandomParams();
                                                               randomParams.setConversationId(
                                                                       mConversationId);
                                                               return mGlobalService.psnGetRandom(
                                                                       randomParams);
                                                           }
                                                       })
                                               .flatMap(new Func1<String, Observable<String>>() {
                                                   @Override
                                                   public Observable<String> call(String random) {
                                                       mRandom = random;
                                                       PSNGetTokenIdParams tokenIdParams =
                                                               new PSNGetTokenIdParams();
                                                       tokenIdParams.setConversationId(
                                                               mConversationId);
                                                       return mGlobalService.psnGetTokenId(
                                                               tokenIdParams);
                                                   }
                                               })
                                               .compose(
                                                       SchedulersCompat.<String>applyIoSchedulers())
                                               .subscribe(new BIIBaseSubscriber<String>() {
                                                   @Override
                                                   public void handleException(
                                                           BiiResultErrorException biiResultErrorException) {

                                                   }

                                                   @Override
                                                   public void onCompleted() {
                                                   }

                                                   @Override
                                                   public void onNext(String tokenId) {
                                                       mView.onVerifySuccess(mVerifyBean, mRandom,
                                                               tokenId);
                                                   }
                                               });
    }
}