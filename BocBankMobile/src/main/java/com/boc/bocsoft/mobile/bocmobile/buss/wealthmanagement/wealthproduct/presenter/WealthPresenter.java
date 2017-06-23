package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.presenter;

import com.boc.bocsoft.mobile.bii.bus.asset.model.PsnAssetBalanceQuery.PsnAssetBalanceQueryParams;
import com.boc.bocsoft.mobile.bii.bus.asset.model.PsnAssetBalanceQuery.PsnAssetBalanceQueryResult;
import com.boc.bocsoft.mobile.bii.bus.asset.service.AssetService;
import com.boc.bocsoft.mobile.bii.bus.financing.model.PsnXadProductQueryOutlay.PsnXadProductQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.financing.model.PsnXadProductQueryOutlay.PsnXadProductQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.financing.model.PsnXpadProductDetailQueryOutlay.PsnXpadProductDetailQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.financing.model.PsnXpadProductDetailQueryOutlay.PsnXpadProductDetailQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.financing.service.FinancingService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNBmpsCreatConversation.PSNBmpsCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvestmentManageIsOpen.PsnInvestmentManageIsOpenParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvtEvaluationInit.PsnInvtEvaluationInitParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvtEvaluationInit.PsnInvtEvaluationInitResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAptitudeTreatyDetailQuery.PsnXpadAptitudeTreatyDetailQueryParam;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAptitudeTreatyDetailQuery.PsnXpadAptitudeTreatyDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadNetHistoryQuery.PsnXpadNetHistoryQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadNetHistoryQuery.PsnXpadNetHistoryQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadNetHistoryQueryOutlay.PsnXpadNetHistoryQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadNetHistoryQueryOutlay.PsnXpadNetHistoryQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductDetailQuery.PsnXpadProductDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductDetailQuery.PsnXpadProductDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductInvestTreatyQuery.PsnXpadProductInvestTreatyQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductInvestTreatyQuery.PsnXpadProductInvestTreatyQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductListQuery.PsnXpadProductListQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductListQuery.PsnXpadProductListQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductQueryAndBuyInit.PsnXpadProductQueryAndBuyInitParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductQueryAndBuyInit.PsnXpadProductQueryAndBuyInitResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProfitCount.PsnXpadProfitCountParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProfitCount.PsnXpadProfitCountResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadRecentAccountUpdate.PsnXpadRecentAccountUpdateParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadRecentAccountUpdate.PsnXpadRecentAccountUpdateResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadSignInit.PsnXpadSignInitParam;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadSignInit.PsnXpadSignInitResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ErrorDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.openwealthmanager.ui.OpenStatusI;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.PsnXpadSignInitBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.presenter.BuildResponseResult;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.WealthConst;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.DetailsRequestBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthDetailsBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthProfitBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthContract;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 中银理财网络请求（首页与详情）
 * Created by liuweidong on 2016/9/9.
 */
public class WealthPresenter implements WealthContract.Presenter {
    private RxLifecycleManager mRxLifecycleManager;
    private GlobalService globalService;// 公共Service
    private AssetService assetService;// 资产
    private FinancingService financingService;
    private WealthManagementService wealthService;// 理财Service
    private WealthContract.HomeView homeView;
    private WealthContract.DetailsView mDetailsView;// 明细
    private WealthContract.ProfitView mProfitView;// 试算
    private BussFragment bussFragment;
    private boolean isDetailsView = false;

    public WealthPresenter() {
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        wealthService = new WealthManagementService();
        financingService = new FinancingService();
    }

    public WealthPresenter(WealthContract.HomeView homeView, boolean isDetailsView) {// 理财列表页
        this();
        assetService = new AssetService();// 资产市值接口
        this.homeView = homeView;
        this.bussFragment = (BussFragment) homeView;
        this.isDetailsView = isDetailsView;
    }

    public WealthPresenter(WealthContract.DetailsView detailsView, boolean isDetailsView) {// 理财明细页
        this();
        this.mDetailsView = detailsView;
        this.bussFragment = (BussFragment) detailsView;
        this.isDetailsView = isDetailsView;
    }

    public WealthPresenter(WealthContract.ProfitView profitView) {
        this();
        this.mProfitView = profitView;
    }

    /**
     * 查询理财的开通状态（all）
     */
    @Override
    public void queryOpenStatus(final OpenStatusI openStatusI) {
        final boolean[] mOpenStatus = WealthProductFragment.getInstance().isOpenWealth();// 开通理财服务状态
        PsnInvestmentManageIsOpenParams params = new PsnInvestmentManageIsOpenParams();
        wealthService.psnInvestmentManageIsOpen(params)
                .compose(mRxLifecycleManager.<Boolean>bindToLifecycle())
                .flatMap(new Func1<Boolean, Observable<PsnInvtEvaluationInitResult>>() {
                    @Override
                    public Observable<PsnInvtEvaluationInitResult> call(Boolean aBoolean) {
                        if (aBoolean) {
                            mOpenStatus[0] = true;
                        } else {
                            mOpenStatus[0] = false;
                        }
                        PsnInvtEvaluationInitParams params = new PsnInvtEvaluationInitParams();
                        return wealthService.psnInvtEvaluationInit(params);
                    }
                })
                .flatMap(new Func1<PsnInvtEvaluationInitResult, Observable<PsnXpadAccountQueryResult>>() {
                    @Override
                    public Observable<PsnXpadAccountQueryResult> call(PsnInvtEvaluationInitResult result) {
                        /*风险测评*/
                        if ("0".equals(result.getCustExist())) {// 存在且总协议有效
                            if ("true".equals(result.getEvaluatedBefore()) && WealthConst.NO_1.equals(result.getEvalExpired())) {
                                mOpenStatus[1] = true;
                            } else {
                                mOpenStatus[1] = false;
                                if ("false".equals(result.getEvaluatedBefore())) {
                                    WealthProductFragment.getInstance().setRiskStatus(0);
                                }
                                if (WealthConst.YES_0.equals(result.getEvalExpired())) {
                                    WealthProductFragment.getInstance().setRiskStatus(1);
                                }
                            }
                        } else {
                            mOpenStatus[1] = false;
                        }
                        PsnXpadAccountQueryParams params = new PsnXpadAccountQueryParams();
                        params.setQueryType("1");
                        params.setXpadAccountSatus(WealthConst.YES_1);
                        return wealthService.psnXpadAccountQuery(params);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnXpadAccountQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        WealthProductFragment.getInstance().setRequestStatus(false);// 设置接口调用失败

                        ErrorDialog errorDialog = new ErrorDialog(ActivityManager.getAppManager().currentActivity());
                        errorDialog.setBtnText("确认");
                        errorDialog.setErrorData(biiResultErrorException.getErrorMessage());
                        errorDialog.show();
                        if (openStatusI != null) {
                            openStatusI.openFail(errorDialog);
                        }
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {
                        WealthProductFragment.getInstance().setRequestStatus(true);// 接口调用成功
                        WealthProductFragment.getInstance().setOpenWealth(mOpenStatus);
                        if (openStatusI != null) {
                            openStatusI.openSuccess();
                        }
                    }

                    @Override
                    public void onNext(PsnXpadAccountQueryResult accountResult) {
                        /*理财账户*/
                        WealthResponseResult.copyResponseAccount(accountResult);
                        if (accountResult.getList().size() > 0) {
                            mOpenStatus[2] = true;
                        } else {
                            mOpenStatus[2] = false;
                        }
                    }
                });
    }

    /**
     * 查询客户资产负债信息（登录后）
     */
    @Override
    public void queryAssetBalance() {
        PsnAssetBalanceQueryParams params = new PsnAssetBalanceQueryParams();
        assetService.psnAssetBalanceQuery(params)
                .compose(mRxLifecycleManager.<PsnAssetBalanceQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnAssetBalanceQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnAssetBalanceQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnAssetBalanceQueryResult result) {
                        homeView.queryAssetBalanceSuccess(result.getXpadAmt());
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        // 错误底层不弹窗
                    }
                });
    }

    /**
     * 是否开通投资理财服务（登录后）
     */
    @Override
    public void isOpenInvestmentManage() {
        PsnInvestmentManageIsOpenParams params = new PsnInvestmentManageIsOpenParams();
        wealthService.psnInvestmentManageIsOpen(params)
                .compose(mRxLifecycleManager.<Boolean>bindToLifecycle())
                .compose(SchedulersCompat.<Boolean>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<Boolean>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        if (isDetailsView) {
                            mDetailsView.isOpenInvestmentManageFail();
                        } else {
                            homeView.isOpenInvestmentManageFail();
                        }
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(Boolean result) {
                        if (isDetailsView) {
                            mDetailsView.isOpenInvestmentManageSuccess(result);
                        } else {
                            homeView.isOpenInvestmentManageSuccess(result);
                        }
                    }
                });
    }

    /**
     * 查询客户理财账户信息（登录后）
     */
    @Override
    public void queryFinanceAccountInfo() {
        PsnXpadAccountQueryParams params = new PsnXpadAccountQueryParams();
        params.setQueryType("1");// 0：查询所有已登记的理财账户 1、查询所有已登记并且关联到网银的理财账户
        params.setXpadAccountSatus(WealthConst.YES_1);// 可用
        wealthService.psnXpadAccountQuery(params)
                .compose(mRxLifecycleManager.<PsnXpadAccountQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnXpadAccountQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadAccountQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        if (homeView != null)
                            homeView.queryFinanceAccountInfoFail();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnXpadAccountQueryResult result) {
                        WealthResponseResult.copyResponseAccount(result);// 保存结果
                        if (homeView != null)
                            homeView.queryFinanceAccountInfoSuccess();
                    }
                });
    }

    /**
     * 4.20 020 风险评估查询（登录后）
     */
    @Override
    public void queryRiskEvaluation() {
        PsnInvtEvaluationInitParams params = new PsnInvtEvaluationInitParams();
        wealthService.psnInvtEvaluationInit(params)
                .compose(mRxLifecycleManager.<PsnInvtEvaluationInitResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnInvtEvaluationInitResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnInvtEvaluationInitResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        homeView.queryRiskEvaluationFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnInvtEvaluationInitResult result) {
                        homeView.queryRiskEvaluationSuccess(result.getCustExist(), result.getEvaluatedBefore(), result.getEvalExpired());
                    }
                });
    }

    /**
     * 更新客户最近操作的理财账号
     */
    @Override
    public void updateRecentAccount() {
        PsnXpadRecentAccountUpdateParams params = new PsnXpadRecentAccountUpdateParams();
        wealthService.psnXpadRecentAccountUpdate(params)
                .compose(mRxLifecycleManager.<PsnXpadRecentAccountUpdateResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnXpadRecentAccountUpdateResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadRecentAccountUpdateResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnXpadRecentAccountUpdateResult psnXpadRecentAccountUpdateResult) {

                    }
                });
    }

    /**
     * 4.4 004产品查询与购买初始化(产品种类查询)PsnXpadProductQueryAndBuyInit（登录后）
     */
    @Override
    public void queryBuyInit() {
        PsnXpadProductQueryAndBuyInitParams params = new PsnXpadProductQueryAndBuyInitParams();
        wealthService.psnXpadProductQueryAndBuyInit(params)
                .compose(mRxLifecycleManager.<List<PsnXpadProductQueryAndBuyInitResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnXpadProductQueryAndBuyInitResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnXpadProductQueryAndBuyInitResult>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(List<PsnXpadProductQueryAndBuyInitResult> resultList) {
                        homeView.queryBuyInitSuccess();
                    }
                });
    }

    /**
     * 4.51 051 PsnXadProductQueryOutlay 查询全国发行的理财产品列表（登录前）
     */
    @Override
    public void queryProductListN(final int index, final String prodCode, final boolean isSearch) {
        PSNBmpsCreatConversationParams params = new PSNBmpsCreatConversationParams();
        globalService.psnBmpsCreatConversation(params)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnXadProductQueryOutlayResult>>() {
                    @Override
                    public Observable<PsnXadProductQueryOutlayResult> call(String s) {
                        PsnXadProductQueryOutlayParams params = WealthRequestParams.buildPsnXadProductQueryOutlayParams(index, prodCode, isSearch);
                        params.setConversationId(s);
                        return financingService.psnXadProductQueryOutlay(params);
                    }
                })
                .compose(SchedulersCompat.<PsnXadProductQueryOutlayResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXadProductQueryOutlayResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        homeView.queryProductListNFail();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnXadProductQueryOutlayResult result) {
                        homeView.queryProductListNSuccess(WealthResponseResult.copyResultToViewModel(result));
                    }
                });
    }

    /**
     * 4.52 052 PsnXpadProductDetailQueryOutlay登录前查询理财产品详情（登录前）
     */
    @Override
    public void queryProductDetailN(DetailsRequestBean requestBean) {
        PsnXpadProductDetailQueryOutlayParams params = WealthRequestParams.buildPsnXpadProductDetailQueryOutlayParams(requestBean);
        financingService.psnXpadProductDetailQueryOutlay(params)
                .compose(mRxLifecycleManager.<PsnXpadProductDetailQueryOutlayResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnXpadProductDetailQueryOutlayResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadProductDetailQueryOutlayResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        if (isDetailsView) {
                            mDetailsView.queryProductDetailNFail();
                        } else {
                            homeView.queryProductDetailNFail();
                        }
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnXpadProductDetailQueryOutlayResult result) {
                        if (isDetailsView) {
                            mDetailsView.queryProductDetailNSuccess(WealthResponseResult.copyResultToViewModel(result));
                        } else {
                            homeView.queryProductDetailNSuccess(WealthResponseResult.copyResultToViewModel(result));
                        }
                    }
                });
    }

    /**
     * 4.41 041产品查询与购买PsnXpadProductListQuery（登录后）
     */
    @Override
    public void queryProductListY(final int index, final String prodCode, final boolean isSearch) {
        PSNCreatConversationParams params = new PSNCreatConversationParams();
        globalService.psnCreatConversation(params)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnXpadProductListQueryResult>>() {
                    @Override
                    public Observable<PsnXpadProductListQueryResult> call(String s) {
                        PsnXpadProductListQueryParams params = WealthRequestParams.buildPsnXpadProductListQueryParams(index, prodCode, isSearch);
                        params.setConversationId(s);
                        return wealthService.psnXpadProductListQuery(params);
                    }
                })
                .compose(SchedulersCompat.<PsnXpadProductListQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadProductListQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        homeView.queryProductListYFail();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnXpadProductListQueryResult result) {
                        homeView.queryProductListYSuccess(WealthResponseResult.copyResultToViewModel(result));
                    }
                });
    }

    /**
     * 4.40 040产品详情查询PsnXpadProductDetailQuery（登录后）
     */
    @Override
    public void queryProductDetailY(DetailsRequestBean requestBean) {
        PsnXpadProductDetailQueryParams params = WealthRequestParams.buildPsnXpadProductDetailQueryParams(requestBean);
        wealthService.psnXpadProductDetailQuery(params)
                .compose(mRxLifecycleManager.<PsnXpadProductDetailQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnXpadProductDetailQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadProductDetailQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        if (isDetailsView) {
                            mDetailsView.queryProductDetailYFail();
                        } else {
                            homeView.queryProductDetailYFail();
                        }
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnXpadProductDetailQueryResult result) {
                        if (isDetailsView) {
                            mDetailsView.queryProductDetailYSuccess(WealthResponseResult.copyResultToViewModel(result));
                        } else {
                            homeView.queryProductDetailYSuccess(WealthResponseResult.copyResultToViewModel(result));
                        }
                    }
                });
    }

    /**
     * 历史净值查询（登录后）
     */
    @Override
    public void queryNetHistoryY(String prodCode, String date) {
        PsnXpadNetHistoryQueryParams params = new PsnXpadNetHistoryQueryParams();
        params.setProductCode(prodCode);
        params.setPeriod(date);
        wealthService.psnXpadNetHistoryQuery(params)
                .compose(mRxLifecycleManager.<PsnXpadNetHistoryQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnXpadNetHistoryQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadNetHistoryQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mDetailsView.queryNetHistoryFailY();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnXpadNetHistoryQueryResult result) {
                        mDetailsView.queryNetHistorySuccessY(WealthResponseResult.copyResultToViewModel(result));
                    }
                });
    }

    /**
     * 历史净值查询（登录前）
     */
    @Override
    public void queryNetHistoryN(String prodCode, String date) {
        PsnXpadNetHistoryQueryOutlayParams params = new PsnXpadNetHistoryQueryOutlayParams();
        params.setProductCode(prodCode);
        params.setPeriod(date);
        wealthService.psnXpadNetHistoryQueryOutlay(params)
                .compose(mRxLifecycleManager.<PsnXpadNetHistoryQueryOutlayResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnXpadNetHistoryQueryOutlayResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadNetHistoryQueryOutlayResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mDetailsView.queryNetHistoryFailN();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnXpadNetHistoryQueryOutlayResult result) {
                        mDetailsView.queryNetHistorySuccessN(WealthResponseResult.copyResultToViewModel(result));
                    }
                });
    }

    /**
     * 收益试算
     */
    @Override
    public void profitCalc(WealthProfitBean profitBean) {
        PsnXpadProfitCountParams params = WealthRequestParams.buildPsnXpadProfitCountParams(profitBean);
        wealthService.psnXpadProfitCount(params)
                .compose(mRxLifecycleManager.<PsnXpadProfitCountResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnXpadProfitCountResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadProfitCountResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mProfitView.profitCalcFail();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnXpadProfitCountResult result) {
                        mProfitView.profitCalcSuccess(result.getExpprofit(), result.getProcur());
                    }
                });
    }

    /**
     * 产品投资协议查询
     */
    @Override
    public void queryInvestTreaty(final String accountID, final String prodCode) {
        bussFragment.showLoadingDialog();
        PSNCreatConversationParams conversationPreParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(conversationPreParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnXpadProductInvestTreatyQueryResult>>() {
                    @Override
                    public Observable<PsnXpadProductInvestTreatyQueryResult> call(String s) {
                        PsnXpadProductInvestTreatyQueryParams params = WealthRequestParams.buildProductInvestQueryParams(accountID, prodCode);
                        params.setConversationId(s);
                        return wealthService.psnXpadProductInvestTreatyQuery(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnXpadProductInvestTreatyQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException result) {
                        bussFragment.closeProgressDialog();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnXpadProductInvestTreatyQueryResult result) {
                        bussFragment.closeProgressDialog();
                        mDetailsView.queryInvestTreatySuccess(WealthResponseResult.copyResultToViewModel(result));
                    }
                });
    }

    /**
     * 4.74 074 智能协议详情查询
     */
    @Override
    public void queryTreatyDetail(String accountId, String agrCode) {
        bussFragment.showLoadingDialog();
        PsnXpadAptitudeTreatyDetailQueryParam param = new PsnXpadAptitudeTreatyDetailQueryParam();
        param.setAccountId(accountId);
        param.setAgrCode(agrCode);// 产品协议编号
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
                        mDetailsView.queryTreatyDetailSuccess(BuildResponseResult.copyResultToViewModel(result));
                        bussFragment.closeProgressDialog();
                    }
                });
    }

    /**
     * 4.10 010周期性产品续约协议签约/签约初始化
     */
    @Override
    public void psnXpadSignInit(WealthDetailsBean detailsBean, DetailsRequestBean requestBean) {
        bussFragment.showLoadingDialog();
        PsnXpadSignInitParam param = new PsnXpadSignInitParam();
        param.setProductCode(detailsBean.getProdCode());
        param.setProductName(detailsBean.getProdName());
        param.setCurCode(detailsBean.getCurCode());
        param.setRemainCycleCount(detailsBean.getRemainCycleCount());
        param.setAccountId(requestBean.getAccountBean().getAccountId());
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
                        mDetailsView.psnXpadSignInitSuccess(BeanConvertor.toBean(psnXpadSignInitResult, initBean));
                    }
                });

    }

    @Override
    public void subscribe() {
        mRxLifecycleManager.onStart();
    }

    @Override
    public void unsubscribe() {
        mRxLifecycleManager.onDestroy();
    }
}
