package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.openwealthmanager.presenter;

import com.boc.bocsoft.mobile.bii.bus.financing.service.FinancingService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvestmentManageIsOpen.PsnInvestmentManageIsOpenParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvtEvaluationInit.PsnInvtEvaluationInitParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvtEvaluationInit.PsnInvtEvaluationInitResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ErrorDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.openwealthmanager.ui.InvestTreatyContract;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.WealthConst;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.presenter.WealthResponseResult;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.openwealthmanager.ui.OpenStatusI;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 开通投资理财接口（开通投资理财服务、风险评估、登记账户）
 * Created by liuweidong on 2016/12/4.
 */

public class InvestTreatyPresenter extends RxPresenter implements InvestTreatyContract.Presenter {
    private FinancingService financingService;
    private WealthManagementService wealthService;// 理财Service

    private static boolean[] mRequestStatus = new boolean[3];// 判断理财开通状态接口调用状态
    private static boolean[] mOpenStatus = new boolean[3];// 理财开通状态（0开通理财服务1风险测评2登记账户）

    public InvestTreatyPresenter(){
        wealthService = new WealthManagementService();
        financingService = new FinancingService();
    }

    /**
     * 查询理财的开通状态（all）
     * 方法拆分
     */
    @Override
    public void queryOpenStatus(final OpenStatusI openStatusI, final boolean[] needs) {
        final boolean[] mOpenStatus = WealthProductFragment.getInstance().isOpenWealth();// 开通理财服务状态
        PsnInvestmentManageIsOpenParams params = new PsnInvestmentManageIsOpenParams();
        wealthService.psnInvestmentManageIsOpen(params)
                .compose(this.<Boolean>bindToLifecycle())
                .flatMap(new Func1<Boolean, Observable<PsnInvtEvaluationInitResult>>() {
                    @Override
                    public Observable<PsnInvtEvaluationInitResult> call(Boolean aBoolean) {
                        if (aBoolean) {
                            mOpenStatus[0] = true;
                        } else {
                            mOpenStatus[0] = false;
                        }
                        PsnInvtEvaluationInitParams params = new PsnInvtEvaluationInitParams();
                        if (needs[1]) {
                            return wealthService.psnInvtEvaluationInit(params);
                        } else {
                            mRequestStatus[1] = false;
                            return Observable.just(null);
                        }
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mRequestStatus[1] = false;
                    }
                })
                .flatMap(new Func1<PsnInvtEvaluationInitResult, Observable<PsnXpadAccountQueryResult>>() {
                    @Override
                    public Observable<PsnXpadAccountQueryResult> call(PsnInvtEvaluationInitResult result) {
                        if (needs[1]) {
                        /*风险测评*/
                            if ("true".equals(result.getEvaluatedBefore()) && WealthConst.NO_1.equals(result.getEvalExpired())) {
                                mOpenStatus[1] = true;
                            } else {
                                mOpenStatus[1] = false;
                            }
                        }
                        if (needs[2]) {
                            PsnXpadAccountQueryParams params = new PsnXpadAccountQueryParams();
                            params.setQueryType("1");
                            params.setXpadAccountSatus(WealthConst.YES_1);
                            return wealthService.psnXpadAccountQuery(params);
                        } else {
                            mRequestStatus[2] = false;
                            return Observable.just(null);
                        }
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
                        if (needs[2]) {
                        /*理财账户*/
                            WealthResponseResult.copyResponseAccount(accountResult);
                            if (accountResult.getList().size() > 0) {
                                mOpenStatus[2] = true;
                            } else {
                                mOpenStatus[2] = false;
                            }
                        }
                    }
                });
    }

}
