package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.presenter;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bii.bus.equickpay.service.EquickpayService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetRandom.PSNGetRandomParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.HceTransactionViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.SubmitModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hceaddcard.HceBaseConfirmContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by yangle on 2016/12/17.
 * 描述:Hce确认界面presenter抽取(激活，修改限额设置，解挂)
 * serviceId,预交易接口,提交交易接口不同子类要实现
 */
public abstract class HceBaseConfirmPresenter<R> extends RxPresenter implements HceBaseConfirmContract.Present {

    private final HceBaseConfirmContract.View mView;
    protected final GlobalService mGlobalService;
    protected final EquickpayService mEquickpayService;
    private VerifyBean  mVerifyBean;

    public HceBaseConfirmPresenter(HceBaseConfirmContract.View view) {
        mView = view;
        mGlobalService = new GlobalService();
        mEquickpayService = new EquickpayService();
    }

    @Override
    public void getSecurityFactor() {
        mView.showLoading();
                mGlobalService.psnGetSecurityFactor(generateFactorParams(mView.getViewModel().getConversationId(),getServiceId()))
                .map(new Func1<PsnGetSecurityFactorResult, SecurityFactorModel>() {
                    @Override
                    public SecurityFactorModel call(PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
                        return new SecurityFactorModel(psnGetSecurityFactorResult);
                    }
                })
                .compose(SchedulersCompat.<SecurityFactorModel>applyIoSchedulers())
                .compose(this.<SecurityFactorModel>bindToLifecycle())
                .subscribe(new BIIBaseSubscriber<SecurityFactorModel>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        LogUtil.e("=====getSecurityFactor=======handleException============="+biiResultErrorException.getErrorMessage());
                        mView.closeLoading();
                    }

                    @Override
                    public void onCompleted() {
                        mView.closeLoading();
                    }

                    @Override
                    public void onNext(SecurityFactorModel securityFactorModel) {
                        LogUtil.e("=====getSecurityFactor=======onNext=============");
                        mView.onGetSecurityFactorSuccess(securityFactorModel);
                    }
                });
    }
    //激活，修改限额设置，解挂对就的id不同
    protected abstract String getServiceId();

    private PsnGetSecurityFactorParams generateFactorParams(String conversationId,String serviceId) {
        PsnGetSecurityFactorParams factorParams = new PsnGetSecurityFactorParams();
        factorParams.setConversationId(conversationId);
        factorParams.setServiceId(serviceId);
        return factorParams;
    }

    @Override
    public void verify() {
        mView.showLoading();
        // TODO: 2016/12/17 分不同操作 如激活，修改限额设置，解挂
        verifyRequest(mView.getViewModel())
                .flatMap(new Func1<VerifyBean, Observable<String>>() {
                    @Override
                    public Observable<String> call(VerifyBean verifyBean) {
                        mVerifyBean = verifyBean;
                        return mGlobalService.psnGetRandom(generateRandomParams(mView.getViewModel().getConversationId()));
                    }
                })
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .compose(this.<String>bindToLifecycle())
                .subscribe(new BIIBaseSubscriber<String>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        LogUtil.e("=====verify=======handleException============="+biiResultErrorException.getErrorMessage());
                        mView.closeLoading();
                    }

                    @Override
                    public void onCompleted() {
                        mView.closeLoading();
                    }

                    @Override
                    public void onNext(String random) {
                        LogUtil.e("=====verify=======onNext=========random===="+random);
                        mView.onVerifySuccess(mVerifyBean, random);
                    }
                });

    }

    private PSNGetRandomParams generateRandomParams(String conversationId) {
        PSNGetRandomParams params = new PSNGetRandomParams();
        params.setConversationId(conversationId);
        return params;
    }

    @Override
    public void submit(final SubmitModel submitModel) {
        mView.showLoading();
        mGlobalService.psnGetTokenId(generateTokenParams(mView.getViewModel().getConversationId()))
                .flatMap(new Func1<String, Observable<R>>() {
                    @Override
                    public Observable<R> call(String token) {
                        submitModel.setToken(token);
                        // 分不同操作 调不同接口 如激活，修改限额设置，解挂
                        return submitRequest(mView.getViewModel(),submitModel);
                    }
                })
                .compose(SchedulersCompat.<R>applyIoSchedulers())
                .compose(this.<R>bindToLifecycle())
                .subscribe(new BIIBaseSubscriber<R>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        LogUtil.e("=====submit=======handleException============="+biiResultErrorException.getErrorMessage());
                        mView.closeLoading();
                    }

                    @Override
                    public void onCompleted() {
                        mView.closeLoading();
                    }

                    @Override
                    public void onNext(R psnSubmitResult) {
                        LogUtil.e("=====submit=======onNext=============");
                        mView.onSubmitSuccess(psnSubmitResult);
                    }
                });
    }

    protected abstract Observable<VerifyBean> verifyRequest(HceTransactionViewModel viewModel);

    protected abstract Observable<R> submitRequest(HceTransactionViewModel viewModel, SubmitModel submitModel);

    private PSNGetTokenIdParams generateTokenParams(String conversationId) {
        PSNGetTokenIdParams tokenIdParams = new PSNGetTokenIdParams();
        tokenIdParams.setConversationId(conversationId);
        return tokenIdParams;
    }

}
