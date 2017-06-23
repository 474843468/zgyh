package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.presenter;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayAdvanceConfirm.PsnCrcdDividedPayAdvanceConfirmParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayAdvanceConfirm.PsnCrcdDividedPayAdvanceConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayAdvanceResult.PsnCrcdDividedPayAdvanceResultParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayAdvanceResult.PsnCrcdDividedPayAdvanceResultResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.service.CrcdService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetRandom.PSNGetRandomParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.model.InstallmentRecordModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.model.SubmitModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.ui.PayAdvanceContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by yangle on 2016/11/24.
 */
public class PayAdvancePresenter extends RxPresenter implements PayAdvanceContract.Presenter {

    private static final String TAG = "PayAdvancePresenter";
    private final PayAdvanceContract.View mView;
    private final GlobalService mGlobalService;
    private final CrcdService mCrcdService;
    private String mRandom;

    public PayAdvancePresenter(PayAdvanceContract.View view ) {
        this.mView = view;
        this.mGlobalService = new GlobalService();
        this.mCrcdService = new CrcdService();
    }

    @Override
    public void psnCrcdDividedPayAdvanceConfirm(final InstallmentRecordModel recordModel) {
        mView.showLoading();
        mGlobalService.psnGetRandom(generateGetRandomParams(recordModel.getConversationId()))
                .flatMap(new Func1<String, Observable<PsnCrcdDividedPayAdvanceConfirmResult>>() {
                    @Override
                    public Observable<PsnCrcdDividedPayAdvanceConfirmResult> call(String random) {
                        mRandom = random;
                        return mCrcdService.psnCrcdDividedPayAdvanceConfirm(generateVerifyParams(recordModel));
                    }
                })
                .map(new Func1<PsnCrcdDividedPayAdvanceConfirmResult, VerifyBean>() {
                    @Override
                    public VerifyBean call(PsnCrcdDividedPayAdvanceConfirmResult result) {
                        //return VerifyViewModel.newInstanceFromResult(result);
                        return BeanConvertor.toBean(result, new VerifyBean());
                    }
                })
                .compose(SchedulersCompat.<VerifyBean>applyIoSchedulers())
                .compose(this.<VerifyBean>bindToLifecycle())
                .subscribe(new BIIBaseSubscriber<VerifyBean>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.closeLoading();
                    }

                    @Override
                    public void onCompleted() {
                        mView.closeLoading();
                    }

                    @Override
                    public void onNext(VerifyBean verifyBean) {
                        mView.payAdvanceConfirmSuccess(verifyBean, mRandom);
                    }
                });
    }

    @Override
    public void psnCrcdDividedPayAdvanceResult(final InstallmentRecordModel recordModel, final SubmitModel submitModel) {
        mView.showLoading();
        mGlobalService.psnGetTokenId(generateGetTokenParams(recordModel.getConversationId()))
                .flatMap(new Func1<String, Observable<PsnCrcdDividedPayAdvanceResultResult>>() {
                    @Override
                    public Observable<PsnCrcdDividedPayAdvanceResultResult> call(String token) {
                        submitModel.setToken(token);
                        return mCrcdService.psnCrcdDividedPayAdvanceResult(generateSubmitParams(recordModel, submitModel));
                    }
                })
                .compose(this.<PsnCrcdDividedPayAdvanceResultResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdDividedPayAdvanceResultResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdDividedPayAdvanceResultResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.closeLoading();
                    }

                    @Override
                    public void onCompleted() {
                        mView.closeLoading();
                    }

                    @Override
                    public void onNext(PsnCrcdDividedPayAdvanceResultResult psnCrcdDividedPayAdvanceResultResult) {
                        mView.payAdvanceResultSuccess();
                    }
                });

    }

    // 生成预交易请求params
    private PsnCrcdDividedPayAdvanceConfirmParams generateVerifyParams(InstallmentRecordModel recordModel) {
        PsnCrcdDividedPayAdvanceConfirmParams verifyParams = new PsnCrcdDividedPayAdvanceConfirmParams();
       return BeanConvertor.toBean(recordModel, verifyParams);

    }

    // 生成提交交易请求params
    private PsnCrcdDividedPayAdvanceResultParams generateSubmitParams(InstallmentRecordModel recordModel, SubmitModel submitModel) {
        PsnCrcdDividedPayAdvanceResultParams submitParams = new PsnCrcdDividedPayAdvanceResultParams();
        BeanConvertor.toBean(submitModel, submitParams);
        BeanConvertor.toBean(recordModel, submitParams);
        return submitParams;
    }

    private PSNGetTokenIdParams generateGetTokenParams(String conversationId) {
        PSNGetTokenIdParams params = new PSNGetTokenIdParams();
        params.setConversationId(conversationId);
        return params;
    }

    private PSNGetRandomParams generateGetRandomParams(String conversationId) {
        PSNGetRandomParams params = new PSNGetRandomParams();
        params.setConversationId(conversationId);
        return params;
    }
}
