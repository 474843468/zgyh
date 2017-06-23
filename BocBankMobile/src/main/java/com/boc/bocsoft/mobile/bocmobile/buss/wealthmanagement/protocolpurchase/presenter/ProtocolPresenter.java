package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadApplyAgreementResult.PsnXpadApplyAgreementResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadApplyAgreementResult.PsnXpadApplyAgreementResultParam;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAptitudeTreatyApplyCommit.PsnXpadAptitudeTreatyApplyCommitParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAptitudeTreatyApplyCommit.PsnXpadAptitudeTreatyApplyCommitResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAptitudeTreatyApplyVerify.PsnXpadAptitudeTreatyApplyVerifyParam;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAptitudeTreatyApplyVerify.PsnXpadAptitudeTreatyApplyVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAptitudeTreatyDetailQuery.PsnXpadAptitudeTreatyDetailQueryParam;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAptitudeTreatyDetailQuery.PsnXpadAptitudeTreatyDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductListQuery.PsnXpadProductListQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductListQuery.PsnXpadProductListQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryRiskMatch.PsnXpadQueryRiskMatchParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryRiskMatch.PsnXpadQueryRiskMatchResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadSignInit.PsnXpadSignInitParam;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadSignInit.PsnXpadSignInitResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadSignResult.PsnXpadSignResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadSignResult.PsnXpadSignResultParam;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.ProtocolModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.PsnXpadSignInitBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.PsnXpadSignResultBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui.ProtocolContact;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui.ProtocolPeriodContinueFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui.ProtocolSelectFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui.ProtocolSmartFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.ModelUtil;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 投资协议申请（presenter）
 * Created by wangtong on 2016/10/24.
 */
public class ProtocolPresenter extends RxPresenter implements ProtocolContact.Presenter {

    private RxLifecycleManager mRxLifecycleManager;
    private GlobalService globalService;// 公共Service
    private WealthManagementService wealthService;// 理财Service
    private ProtocolModel mViewModel;
    private BussFragment bussFragment;
    private static String conversationId;

    private ProtocolContact.ProtocolView protocolView;
    private ProtocolContact.ProtocolIntelligentView protocolIntelligentView;
    public ProtocolContact.ProtocolSmartConfirmView confirmView;
    public ProtocolContact.ProtocolFixPurchaseView protocolFixPurchaseView;
    public ProtocolContact.ProtocolFixConfirmView fixConfirmView;
    public ProtocolContact.ProtocolPeriodContinueView protocolPeriodContinueView;
    public ProtocolContact.ProtocolPeriodContinueConfirmView periodContinueConfirmView;

    public ProtocolPresenter() {
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        wealthService = new WealthManagementService();
    }

    public ProtocolPresenter(ProtocolContact.ProtocolView view) {
        this();
        protocolView = view;
        bussFragment = (BussFragment) view;
        mViewModel = bussFragment.findFragment(ProtocolSelectFragment.class).getViewModel();
    }

    public ProtocolPresenter(ProtocolContact.ProtocolIntelligentView view) {
        this();
        protocolIntelligentView = view;
        bussFragment = (BussFragment) view;
        mViewModel = bussFragment.findFragment(ProtocolSmartFragment.class).getViewModel();
    }

    public ProtocolPresenter(ProtocolContact.ProtocolSmartConfirmView confirmView) {
        this();
        this.confirmView = confirmView;
        bussFragment = (BussFragment) confirmView;
    }

    public ProtocolPresenter(ProtocolContact.ProtocolPeriodContinueView view) {
        this();
        protocolPeriodContinueView = view;
        bussFragment = (BussFragment) view;
        mViewModel = bussFragment.findFragment(ProtocolPeriodContinueFragment.class).getViewModel();
    }

    public ProtocolPresenter(ProtocolContact.ProtocolPeriodContinueConfirmView view) {
        this();
        periodContinueConfirmView = view;
        bussFragment = (BussFragment) view;
//        mViewModel = bussFragment.findFragment(ProtocolPeriodContinueFragment.class).getViewModel();
    }

    /**
     * 4.74 074 智能协议详情查询
     */
    @Override
    public void queryTreatyDetail() {
        bussFragment.showLoadingDialog();
        PsnXpadAptitudeTreatyDetailQueryParam param = new PsnXpadAptitudeTreatyDetailQueryParam();
        param.setAccountId(mViewModel.getAccountList().get(0).getAccountId());
        param.setAgrCode(mViewModel.selectedProtocol.getAgrCode());// 产品协议编号
        wealthService.psnXpadAptitudeTreatyDetailQuery(param)
                .compose(mRxLifecycleManager.<PsnXpadAptitudeTreatyDetailQueryResult>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnXpadAptitudeTreatyDetailQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        bussFragment.closeProgressDialog();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnXpadAptitudeTreatyDetailQueryResult result) {
                        protocolView.queryTreatyDetailSuccess(BuildResponseResult.copyResultToViewModel(result));
                        bussFragment.closeProgressDialog();
                    }
                });
    }

    /**
     * 4.24 024查询客户风险等级与产品风险等级是否匹配
     */
    @Override
    public void queryRiskMatch(final ProtocolModel viewModel) {
        bussFragment.showLoadingDialog();
        PsnXpadQueryRiskMatchParams params = new PsnXpadQueryRiskMatchParams();
        params.setProductCode(viewModel.getProId());// 产品代码
        params.setAccountKey(viewModel.getAccountList().get(0).getAccountKey());
        wealthService.psnXpadQueryRiskMatch(params)
                .compose(mRxLifecycleManager.<PsnXpadQueryRiskMatchResult>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnXpadQueryRiskMatchResult>() {

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        bussFragment.closeProgressDialog();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnXpadQueryRiskMatchResult result) {
                        BuildResponseResult.copyResultToViewModel(result, mViewModel);
                        bussFragment.closeProgressDialog();
                        if (protocolIntelligentView != null) {
                            protocolIntelligentView.queryRiskMatchSuccess();
                        } else if (protocolFixPurchaseView != null) {
                            protocolFixPurchaseView.psnXpadQueryRiskMatchReturned();
                        } else if (protocolPeriodContinueView != null) {
                            protocolPeriodContinueView.psnXpadQueryRiskMatchReturned();
                        }
                    }
                });
    }

    /**
     * 4.24 024查询客户风险等级与产品风险等级是否匹配
     * 周期滚续专用
     *
     * @param serialCode
     */
    @Override
    public void queryRiskMatchByContinue(String serialCode, String accountKey) {
        bussFragment.showLoadingDialog();
        PsnXpadQueryRiskMatchParams params = new PsnXpadQueryRiskMatchParams();
        params.setSerialCode(serialCode);// 周期性产品系列编号
        params.setAccountKey(accountKey);
        wealthService.psnXpadQueryRiskMatch(params)
                .compose(mRxLifecycleManager.<PsnXpadQueryRiskMatchResult>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnXpadQueryRiskMatchResult>() {

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        bussFragment.closeProgressDialog();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnXpadQueryRiskMatchResult result) {
                        BuildResponseResult.copyResultToViewModel(result, mViewModel);
                        bussFragment.closeProgressDialog();
                        if (protocolPeriodContinueView != null) {
                            protocolPeriodContinueView.psnXpadQueryRiskMatchReturned();
                        }
                    }
                });
    }

    /**
     * 4.61 061 智能协议申请预交易
     */
    @Override
    public void confirmTreaty(final ProtocolModel viewModel) {
        bussFragment.showLoadingDialog();
        PSNCreatConversationParams conversationPreParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(conversationPreParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        PSNGetTokenIdParams params = new PSNGetTokenIdParams();
                        params.setConversationId(s);
                        conversationId = s;
                        return globalService.psnGetTokenId(params);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnXpadAptitudeTreatyApplyVerifyResult>>() {
                    @Override
                    public Observable<PsnXpadAptitudeTreatyApplyVerifyResult> call(String s) {
                        PsnXpadAptitudeTreatyApplyVerifyParam params = BuildRequestParams.buildPsnXpadAptitudeTreatyApplyVerifyParams(viewModel);
                        params.setConversationId(conversationId);
                        params.setToken(s);
                        return wealthService.psnXpadAptitudeTreatyApplyVerify(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnXpadAptitudeTreatyApplyVerifyResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        bussFragment.closeProgressDialog();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnXpadAptitudeTreatyApplyVerifyResult result) {
                        mViewModel.setConfirmBean(BuildResponseResult.copyResultToViewModel(result));
                        protocolIntelligentView.confirmTreatySuccess();
                        bussFragment.closeProgressDialog();
                    }
                });
    }

    /**
     * 4.62 062 智能协议申请提交交易
     */
    @Override
    public void resultTreaty(final ProtocolModel viewModel) {
        bussFragment.showLoadingDialog(false);
        PSNGetTokenIdParams params = new PSNGetTokenIdParams();
        params.setConversationId(conversationId);
        globalService.psnGetTokenId(params)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnXpadAptitudeTreatyApplyCommitResult>>() {
                    @Override
                    public Observable<PsnXpadAptitudeTreatyApplyCommitResult> call(String s) {
                        PsnXpadAptitudeTreatyApplyCommitParams params = new PsnXpadAptitudeTreatyApplyCommitParams();
                        params.setConversationId(conversationId);
                        params.setToken(s);
                        params.setProductCode(viewModel.getProId());// 产品代码
                        params.setAccountId(viewModel.getAccountList().get(0).getAccountId());// 账户ID
                        params.setProCur(viewModel.getCurCode());// 币种
                        params.setProductName(viewModel.getProdName());
                        return wealthService.psnXpadAptitudeTreatyApplyCommit(params);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnXpadAptitudeTreatyApplyCommitResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        bussFragment.closeProgressDialog();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnXpadAptitudeTreatyApplyCommitResult result) {
                        confirmView.resultTreatySuccess();
                    }
                });
    }

    /**
     * 4.28 028投资协议申请（定额定投与余额理财）
     */
    @Override
    public void resultApplyAgreement() {
        bussFragment.showLoadingDialog();
        PSNCreatConversationParams conversationPreParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(conversationPreParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        conversationId = s;
                        PSNGetTokenIdParams params = new PSNGetTokenIdParams();
                        params.setConversationId(conversationId);
                        return globalService.psnGetTokenId(params);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnXpadApplyAgreementResult>>() {
                    @Override
                    public Observable<PsnXpadApplyAgreementResult> call(String s) {
                        PsnXpadApplyAgreementResultParam params = new PsnXpadApplyAgreementResultParam();
                        params.setConversationId(conversationId);
                        params.setAccountId(mViewModel.getAccountList().get(0).getAccountId());
                        params.setCurCode(mViewModel.selectedProtocol.getProCur());
                        params.setInvestTime(mViewModel.fixInvestDate);
                        if (mViewModel.selectedProtocol.getAgrType().equals("4")) {
                            params.setInvestType("1");
                        } else {
                            params.setInvestType("0");
                        }
                        params.setMaxAmount(mViewModel.maxInvestAmount);
                        params.setMinAmount(mViewModel.accountLeastAmount);
                        params.setProductCode(mViewModel.selectedProtocol.getProid());
                        params.setProdName(mViewModel.selectedProtocol.getProNam());
                        params.setRedeemAmount(mViewModel.fixInvestAmount + "");
                        params.setRedeemAmount("100.00");
                        params.setTimeInvestRate(mViewModel.fixInvestOfen.charAt(0) + "");
                        params.setTimeInvestRateFlag(mViewModel.fixInvestOfen.charAt(1) + "");
                        params.setTimeInvestType(mViewModel.fixInvestType);
                        params.setToken(s);
                        params.setXpadCashRemit(mViewModel.cashRemit);
                        params.setTotalPeriod(mViewModel.totalPeriodNum + "");
                        return wealthService.psnXpadApplyAgreementResult(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnXpadApplyAgreementResult>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnXpadApplyAgreementResult result) {
                        bussFragment.closeProgressDialog();
                        fixConfirmView.psnXpadApplyAgreementResultReturned();
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        bussFragment.closeProgressDialog();
                        bussFragment.showErrorDialog(biiResultErrorException.getErrorMessage());
                    }
                });
    }

    @Override
    public void psnXpadAptitudeTreatyApplyVerify() {

    }

    /**
     * 4.10 010周期性产品续约协议签约/签约初始化PsnXpadSignInit
     */
    @Override
    public void psnXpadSignInit() {
        bussFragment.showLoadingDialog();
        PsnXpadSignInitParam param = new PsnXpadSignInitParam();
        param.setProductCode(mViewModel.selectedProtocol.getProid());
        param.setProductName(mViewModel.selectedProtocol.getProNam());
        param.setCurCode(mViewModel.selectedProtocol.getProCur());
        param.setRemainCycleCount(mViewModel.getRemainCycleCount());
        param.setAccountId(mViewModel.getAccountList().get(0).getAccountId());
        wealthService.psnXpadSignInit(param)
                .compose(mRxLifecycleManager.<PsnXpadSignInitResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnXpadSignInitResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadSignInitResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        bussFragment.closeProgressDialog();
                        bussFragment.showErrorDialog(biiResultErrorException.getErrorMessage());
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnXpadSignInitResult psnXpadSignInitResult) {
                        bussFragment.closeProgressDialog();
                        PsnXpadSignInitBean initBean = new PsnXpadSignInitBean();
                        protocolView.psnXpadSignInitSuccess(BeanConvertor.toBean(psnXpadSignInitResult, initBean));
                    }
                });
    }


    /**
     * 4.11 011周期性产品续约协议签约/签约结束PsnXpadSignResult
     *
     * @param viewModel
     */
    @Override
    public void psnXpadSignResult(final ProtocolModel viewModel) {
        bussFragment.showLoadingDialog();
        PSNCreatConversationParams conversationPreParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(conversationPreParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        PSNGetTokenIdParams params = new PSNGetTokenIdParams();
                        conversationId = s;
                        params.setConversationId(s);
                        return globalService.psnGetTokenId(params);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnXpadSignResult>>() {
                    @Override
                    public Observable<PsnXpadSignResult> call(String s) {
                        PsnXpadSignResultParam param = BuildRequestParams.buildPsnXpadSignResultParam(viewModel);
                        param.setToken(s);
                        param.setConversationId(conversationId);
                        return wealthService.psnXpadSignResult(param);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnXpadSignResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        bussFragment.closeProgressDialog();
                        bussFragment.showErrorDialog(biiResultErrorException.getErrorMessage());
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnXpadSignResult psnXpadSignResult) {
                        PsnXpadSignResultBean resultBean = new PsnXpadSignResultBean();
                        periodContinueConfirmView.psnXpadSignResultReturned(BeanConvertor.toBean(psnXpadSignResult, resultBean));
                    }
                });
    }


    @Override
    public void queryContinueProductList(final String prodCode, String accountId, String curCode) {
        PsnXpadProductListQueryParams params = ModelUtil.generateProductListQueryParams(accountId, curCode, conversationId);
        wealthService.psnXpadProductListQuery(params)
                .compose(mRxLifecycleManager.<PsnXpadProductListQueryResult>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnXpadProductListQueryResult>() {

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        bussFragment.closeProgressDialog();
                        periodContinueConfirmView.queryProductListSuccess(null);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnXpadProductListQueryResult result) {
                        bussFragment.closeProgressDialog();
                        if (periodContinueConfirmView != null) {
                            periodContinueConfirmView.queryProductListSuccess(BuildResponseResult.generateWealthListBeans(prodCode, result));
                        }
                        if (confirmView != null) {
                            confirmView.queryProductListSuccess(BuildResponseResult.generateWealthListBeans(prodCode, result));
                        }
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        //此处重写该方法，在错误处理时使底层不在弹窗
                    }
                });

    }

}
