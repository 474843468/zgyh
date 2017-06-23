package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.presenter;

import android.support.annotation.NonNull;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayConsumeConfirm.PsnCrcdDividedPayConsumeConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayConsumeResult.PsnCrcdDividedPayConsumeResultParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayConsumeResult.PsnCrcdDividedPayConsumeResultResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.service.CrcdService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetRandom.PSNGetRandomParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.BaseSubmitBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmContract;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.model.ConsumeInstallmentModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.ui.ConsumeInstallmentContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.functions.Func1;

/**
 * 消费分期Presenter linq7090
 */
public class ConsumeInstallmentPresenter extends BaseConfirmPresenter<ConsumeInstallmentModel, PsnCrcdDividedPayConsumeResultResult> implements ConsumeInstallmentContract.Presenter {

    private BaseConfirmContract.View<PsnCrcdDividedPayConsumeResultResult> mConsumeInstallmentView;
    private ConsumeInstallmentContract.BaseView mConsumeInstallmentView1;
    /**
     * 会话
     */
    private String conversationId;

    private final String serviceId = "PB057C1";//信用卡消费分期，账单分期服务码

    /**
     * 公用service
     */
    private GlobalService globalService;
    /**
     * 信用卡service
     */
    private CrcdService crcdService;

    private RxLifecycleManager rxLifecycleManager;
    private PsnGetSecurityFactorResult securityFactorResult;

    public ConsumeInstallmentPresenter(BaseConfirmContract.View<PsnCrcdDividedPayConsumeResultResult> view) {
        super(view);
        mConsumeInstallmentView =  view;
        globalService = new GlobalService();
        rxLifecycleManager = new RxLifecycleManager();
        crcdService = new CrcdService();
    }


    /**
     * 获取安全因子
     */
    @Override
    public void querySecurityFactor() {
        if(mConsumeInstallmentView instanceof ConsumeInstallmentContract.BaseView)
            mConsumeInstallmentView1 = (ConsumeInstallmentContract.BaseView)mConsumeInstallmentView;

        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(rxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnGetSecurityFactorResult>>() {
                    @Override
                    public Observable<PsnGetSecurityFactorResult> call(String conversationRes) {
                        conversationId = conversationRes;
                        PsnGetSecurityFactorParams mSecurityFactorParams = new PsnGetSecurityFactorParams();
                        mSecurityFactorParams.setConversationId(conversationId);
                        //信用卡消费分期服务码
                        mSecurityFactorParams.setServiceId(serviceId);
                        return globalService.psnGetSecurityFactor(mSecurityFactorParams);
                    }
                })
                .compose(SchedulersCompat.<PsnGetSecurityFactorResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnGetSecurityFactorResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnGetSecurityFactorResult psnGetSecurityFactorResult) {

                        mConsumeInstallmentView1.securityFactorSuccess(psnGetSecurityFactorResult);
                    }
                });
    }

    /**
     * 消费分期预交易
     */
    @Override
    public void crcdDividedPayConsumeConfirm(final ConsumeInstallmentModel consumeInstallmentModel) {
        if(mConsumeInstallmentView instanceof ConsumeInstallmentContract.BaseView)
            mConsumeInstallmentView1 = (ConsumeInstallmentContract.BaseView)mConsumeInstallmentView;

        //查询之前请求会话
        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(rxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnGetSecurityFactorResult>>() {
                    @Override
                    public Observable<PsnGetSecurityFactorResult> call(String conId) {
                        PsnGetSecurityFactorParams factorParams=new PsnGetSecurityFactorParams();
                        conversationId=conId;
                        factorParams.setConversationId(conId);
                        factorParams.setServiceId(serviceId);
                        return globalService.psnGetSecurityFactor(factorParams);
                    }
                })
                .flatMap(new Func1<PsnGetSecurityFactorResult, Observable<PsnCrcdDividedPayConsumeConfirmResult>>() {
                    @Override
                    public Observable<PsnCrcdDividedPayConsumeConfirmResult> call(PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
//                        0:虚拟 4:USBKey证书 8:动态口令令牌 32:短信认证码 40:动态口令令牌+短信认证码 96:短信认证码+硬件绑定
                        securityFactorResult=psnGetSecurityFactorResult;
                        CombinListBean combinListBean=psnGetSecurityFactorResult.get_defaultCombin();
                        if(consumeInstallmentModel.get_combinId()==null)
                        {
                            if (null!=combinListBean){
                                consumeInstallmentModel.set_combinId(combinListBean.getId());
                            }else{
                                combinListBean=psnGetSecurityFactorResult.get_combinList().get(0);
                                consumeInstallmentModel.set_combinId(combinListBean.getId());
                            }
                        }
                        consumeInstallmentModel.setConversationId(conversationId);
                        return crcdService.psnCrcdDividedPayConsumeConfirm(consumeInstallmentModel.getConfirmParams());
                    }
                })
                .compose(SchedulersCompat.<PsnCrcdDividedPayConsumeConfirmResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdDividedPayConsumeConfirmResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mConsumeInstallmentView1.crcdDividedPayConsumeConfirmFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnCrcdDividedPayConsumeConfirmResult result) {
                        mConsumeInstallmentView1.crcdDividedPayConsumeConfirmSuccess(result,securityFactorResult);
                    }
                });

    }

    /**
     * 请求随机数
     */
    @Override
    public void queryRandom(final String conversationId) {
        if(mConsumeInstallmentView instanceof ConsumeInstallmentContract.BaseView)
            mConsumeInstallmentView1 = (ConsumeInstallmentContract.BaseView)mConsumeInstallmentView;
        PSNGetRandomParams psnGetRandomParams = new PSNGetRandomParams();
        psnGetRandomParams.setConversationId(conversationId);
        globalService.psnGetRandom(psnGetRandomParams)
                .compose(rxLifecycleManager.<String>bindToLifecycle())
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<String>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onNext(String random) {
                        SecurityVerity.getInstance().setConversationId(conversationId);
                        mConsumeInstallmentView1.randomSuccess(random);
                    }
                });

    }

    /**
     * 消费分期提交交易
     */
    @Override
    public void crcdDividedPayConsumeResult(final ConsumeInstallmentModel consumeInstallmentModel) {
        if(mConsumeInstallmentView instanceof ConsumeInstallmentContract.BaseView)
            mConsumeInstallmentView1 = (ConsumeInstallmentContract.BaseView)mConsumeInstallmentView;
        PSNGetTokenIdParams mTokenIdParams = new PSNGetTokenIdParams();
        mTokenIdParams.setConversationId(consumeInstallmentModel.getConversationId());
        globalService.psnGetTokenId(mTokenIdParams)
                .compose(rxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnCrcdDividedPayConsumeResultResult>>() {
                    @Override
                    public Observable<PsnCrcdDividedPayConsumeResultResult> call(String tokenId) {
                        consumeInstallmentModel.setToken(tokenId);
                        return crcdService.psnCrcdDividedPayConsumeResult(consumeInstallmentModel.getResultParams());

                    }
                })
                .compose(SchedulersCompat.<PsnCrcdDividedPayConsumeResultResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdDividedPayConsumeResultResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mConsumeInstallmentView1.crcdDividedPayConsumeResultFailed(biiResultErrorException);
                        rxLifecycleManager.onDestroy();
                    }

                    @Override
                    public void onCompleted() {
                        rxLifecycleManager.onDestroy();
                    }
                    @Override
                    public void onNext(PsnCrcdDividedPayConsumeResultResult result) {
                        mConsumeInstallmentView1.crcdDividedPayConsumeResultSuccess(result);
                    }
                });


    }
    /**
     * 获取会话id
     */
    @Override
    public String getConversationId() {
        return conversationId;
    }

    @NonNull
    @Override
    protected Observable<VerifyBean> getVerifyBean(ConsumeInstallmentModel fillInfo) {
        return crcdService.psnCrcdDividedPayConsumeConfirm(fillInfo.getConfirmParams())
                .map(new Func1<PsnCrcdDividedPayConsumeConfirmResult, VerifyBean>() {
                    @Override
                    public VerifyBean call(PsnCrcdDividedPayConsumeConfirmResult psnCrcdDividedPayConsumeConfirmResult) {

                        VerifyBean bean = BeanConvertor.toBean(psnCrcdDividedPayConsumeConfirmResult.getSecurityMap(),
                                new VerifyBean());
                        return bean;
                    }
                });
    }

    @Override
    public void submit(final ConsumeInstallmentModel fillInfo, final BaseSubmitBean submitBean) {
        PSNGetTokenIdParams mTokenIdParams = new PSNGetTokenIdParams();
        mTokenIdParams.setConversationId(fillInfo.getConversationId());
        globalService.psnGetTokenId(mTokenIdParams)
                .compose(rxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnCrcdDividedPayConsumeResultResult>>() {
                    @Override
                    public Observable<PsnCrcdDividedPayConsumeResultResult> call(String tokenId) {

                        PsnCrcdDividedPayConsumeResultParams params = new PsnCrcdDividedPayConsumeResultParams();
                        fillInfo.setToken(tokenId);
                        params = BeanConvertor.toBean(fillInfo,params);
                        params = BeanConvertor.toBean(submitBean,params);

                        return crcdService.psnCrcdDividedPayConsumeResult(params);

                    }
                })
                .compose(SchedulersCompat.<PsnCrcdDividedPayConsumeResultResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdDividedPayConsumeResultResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {
                        rxLifecycleManager.onDestroy();
                    }
                    @Override
                    public void onNext(PsnCrcdDividedPayConsumeResultResult result) {
                        mView.onSubmitSuccess(result);
                    }
                });

    }
}