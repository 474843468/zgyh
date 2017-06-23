package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billinstallments.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayBillSetConfirm.PsnCrcdDividedPayBillSetConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayBillSetResult.PsnCrcdDividedPayBillSetResultParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayBillSetResult.PsnCrcdDividedPayBillSetResultResult;
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
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billinstallments.model.BillInstallmentModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billinstallments.ui.BillInstallmentsContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.functions.Func1;

/**
 * 信用卡账单分期Presenter
 * 作者：lq7090
 * 创建时间：2016/12/9.
 */

public class BillInstallmentsPresenter extends BaseConfirmPresenter<BillInstallmentModel, PsnCrcdDividedPayBillSetResultResult> implements BillInstallmentsContract.Presenter {

    private BaseConfirmContract.View<PsnCrcdDividedPayBillSetResultResult> mBillinstallmentsView;
    private BillInstallmentsContract.BaseView mBillinstallmentsView1;


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

    public BillInstallmentsPresenter(BaseConfirmContract.View<PsnCrcdDividedPayBillSetResultResult> view) {
        super(view);
        mBillinstallmentsView = view;
        globalService = new GlobalService();
        rxLifecycleManager = new RxLifecycleManager();
        crcdService = new CrcdService();

    }

    @Override
    public void querySecurityFactor() {

        if (mBillinstallmentsView instanceof BillInstallmentsContract.BaseView)
            mBillinstallmentsView1 = (BillInstallmentsContract.BaseView) mBillinstallmentsView;


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
                        Log.i("linq", "-----------handleExceptionWealth-获取安全因子------------");
                    }

                    @Override
                    public void onCompleted() {
                        Log.i("linq", "-----------onCompleted-获取安全因子------------");
                    }

                    @Override
                    public void onNext(PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
                        Log.i("linq", "-----------onNext-获取安全因子------------");
                        mBillinstallmentsView1.securityFactorSuccess(psnGetSecurityFactorResult);
                    }
                });


    }

    @Override
    public void crcdDividedPayBillSetConfirm(final BillInstallmentModel billInstallmentModel) {

        if (mBillinstallmentsView instanceof BillInstallmentsContract.BaseView)
            mBillinstallmentsView1 = (BillInstallmentsContract.BaseView) mBillinstallmentsView;

        //查询之前请求会话
        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(rxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnGetSecurityFactorResult>>() {
                    @Override
                    public Observable<PsnGetSecurityFactorResult> call(String conId) {
                        PsnGetSecurityFactorParams factorParams = new PsnGetSecurityFactorParams();
                        conversationId = conId;
                        factorParams.setConversationId(conId);
                        factorParams.setServiceId(serviceId);
                        return globalService.psnGetSecurityFactor(factorParams);
                    }
                })
                .flatMap(new Func1<PsnGetSecurityFactorResult, Observable<PsnCrcdDividedPayBillSetConfirmResult>>() {
                    @Override
                    public Observable<PsnCrcdDividedPayBillSetConfirmResult> call(PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
//                        0:虚拟 4:USBKey证书 8:动态口令令牌 32:短信认证码 40:动态口令令牌+短信认证码 96:短信认证码+硬件绑定
                        securityFactorResult = psnGetSecurityFactorResult;
                        CombinListBean combinListBean = psnGetSecurityFactorResult.get_defaultCombin();
                        if (billInstallmentModel.get_combinId() == null) {
                            if (null != combinListBean) {
                                billInstallmentModel.set_combinId(combinListBean.getId());
                            } else {
                                combinListBean = psnGetSecurityFactorResult.get_combinList().get(0);
                                billInstallmentModel.set_combinId(combinListBean.getId());
                            }
                        }


                        billInstallmentModel.setConversationId(conversationId);
                        return crcdService.psnCrcdDividedPayBillSetConfirm(billInstallmentModel.getConfirmParams());
                    }
                })
                .compose(SchedulersCompat.<PsnCrcdDividedPayBillSetConfirmResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdDividedPayBillSetConfirmResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mBillinstallmentsView1.crcdDividedPayBillSetConfirmFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnCrcdDividedPayBillSetConfirmResult result) {
                        mBillinstallmentsView1.crcdDividedPayBillSetConfirmSuccess(result, securityFactorResult);
                    }
                });

    }

    @Override
    public void queryRandom(final String conversationId) {
        if (mBillinstallmentsView instanceof BillInstallmentsContract.BaseView)
            mBillinstallmentsView1 = (BillInstallmentsContract.BaseView) mBillinstallmentsView;
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
                        mBillinstallmentsView1.randomSuccess(random);
                    }
                });

    }

    @Override
    public void crcdDividedPayBillSetResult(final BillInstallmentModel billInstallmentModel) {
        if (mBillinstallmentsView instanceof BillInstallmentsContract.BaseView)
            mBillinstallmentsView1 = (BillInstallmentsContract.BaseView) mBillinstallmentsView;
        PSNGetTokenIdParams mTokenIdParams = new PSNGetTokenIdParams();
        mTokenIdParams.setConversationId(billInstallmentModel.getConversationId());
        globalService.psnGetTokenId(mTokenIdParams)
                .compose(rxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnCrcdDividedPayBillSetResultResult>>() {
                    @Override
                    public Observable<PsnCrcdDividedPayBillSetResultResult> call(String tokenId) {
                        billInstallmentModel.setToken(tokenId);
                        return crcdService.psnCrcdDividedPayBillSetResult(billInstallmentModel.getResultParams());

                    }
                })
                .compose(SchedulersCompat.<PsnCrcdDividedPayBillSetResultResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdDividedPayBillSetResultResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        Log.i("linq7090", "————————————————————————————————————————提交消费分期失败");
                        mBillinstallmentsView1.crcdDividedPayBillSetResultFailed(biiResultErrorException);
                        rxLifecycleManager.onDestroy();
                    }

                    @Override
                    public void onCompleted() {
                        rxLifecycleManager.onDestroy();
                    }

                    @Override
                    public void onNext(PsnCrcdDividedPayBillSetResultResult result) {
                        Log.i("linq7090", "————————————————————————————————————————提交消费分期成功");
                        mBillinstallmentsView1.crcdDividedPayBillSetResultSuccess(result);
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


    /**
     * 用于实现BaseConFirm
     *
     * @param billInstallmentModel
     * @return
     */


    @NonNull
    @Override
    protected Observable<VerifyBean> getVerifyBean(final BillInstallmentModel billInstallmentModel) {

        return crcdService.psnCrcdDividedPayBillSetConfirm(billInstallmentModel.getConfirmParams())
                .map(new Func1<PsnCrcdDividedPayBillSetConfirmResult, VerifyBean>() {
                    @Override
                    public VerifyBean call(PsnCrcdDividedPayBillSetConfirmResult psnCrcdDividedPayBillSetConfirmResult) {

                        VerifyBean bean = BeanConvertor.toBean(psnCrcdDividedPayBillSetConfirmResult.getSecurityMap(),
                                new VerifyBean());
                        return bean;
                    }
                });

    }

    @Override
    public void submit(final BillInstallmentModel billInstallmentModel, final BaseSubmitBean submitBean) {
        PSNGetTokenIdParams mTokenIdParams = new PSNGetTokenIdParams();
        mTokenIdParams.setConversationId(billInstallmentModel.getConversationId());
        globalService.psnGetTokenId(mTokenIdParams)
                .compose(rxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnCrcdDividedPayBillSetResultResult>>() {
                    @Override
                    public Observable<PsnCrcdDividedPayBillSetResultResult> call(String tokenId) {
                        billInstallmentModel.setToken(tokenId);
                        PsnCrcdDividedPayBillSetResultParams params = new PsnCrcdDividedPayBillSetResultParams();
                        params = BeanConvertor.toBean(submitBean, params);
                        params = BeanConvertor.toBean(billInstallmentModel, params);
                        return crcdService.psnCrcdDividedPayBillSetResult(params);
                    }
                })
                .compose(SchedulersCompat.<PsnCrcdDividedPayBillSetResultResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdDividedPayBillSetResultResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {
                        rxLifecycleManager.onDestroy();
                    }

                    @Override
                    public void onNext(PsnCrcdDividedPayBillSetResultResult result) {
                        mView.onSubmitSuccess(result);
                    }
                });
    }
}