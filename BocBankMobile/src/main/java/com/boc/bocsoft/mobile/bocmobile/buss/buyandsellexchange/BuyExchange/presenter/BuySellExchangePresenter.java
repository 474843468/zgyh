package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.presenter;

import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessGetUpperLimitOfForeignCurr.PsnFessGetUpperLimitOfForeignCurrParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessGetUpperLimitOfForeignCurr.PsnFessGetUpperLimitOfForeignCurrResult;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryAccount.PsnFessQueryAccountParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryAccount.PsnFessQueryAccountResult;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryAccountBalance.PsnFessQueryAccountBalanceResult;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryExchangeRate.PsnFessQueryExchangeRateParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryExchangeRate.PsnFessQueryExchangeRateResult;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryExchangeRebateRate.PsnFessQueryExchangeRebateRateParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryExchangeRebateRate.PsnFessQueryExchangeRebateRateResult;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessSignConfirmation4Focus.PsnFessSignConfirmation4FocusParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessSignConfirmation4Focus.PsnFessSignConfirmation4FocusResult;
import com.boc.bocsoft.mobile.bii.bus.fess.service.FessService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.model.BuyExchangeModel;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.common.BuySellBaseExchangeFragment;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 购汇presenter
 * Created by gwluo on 2016/11/29.
 */

public class BuySellExchangePresenter extends RxPresenter implements BuyExchangeContract.Presenter {
    protected GlobalService globalService;
    protected FessService fessService;
    protected BuyExchangeContract.BaseTransView mView;
    protected BuyExchangeModel model;

    public BuySellExchangePresenter(BuyExchangeContract.BaseTransView view) {
        fessService = new FessService();
        globalService = new GlobalService();
        mView = view;
        model = ((BuySellBaseExchangeFragment) view).getModel();
    }

    public void getAccountList() {
        showLoadingDialog();
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams).
                compose(this.<String>bindToLifecycle()).
                subscribeOn(Schedulers.io()).
                flatMap(new Func1<String, Observable<PsnFessQueryAccountResult>>() {
                    @Override
                    public Observable<PsnFessQueryAccountResult> call(String s) {
                        PsnFessQueryAccountParams params = new PsnFessQueryAccountParams();
                        params.setConversationId(s);
                        model.setConversationId(s);
                        return fessService.psnFessQueryAccount(params);
                    }
                }).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new BIIBaseSubscriber<PsnFessQueryAccountResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        closeLoadingDialog();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFessQueryAccountResult result) {
                        List<AccountBean> copyList = model.getAccList();
                        copyList.clear();
                        for (PsnFessQueryAccountResult.Account account : result.getList()) {
                            AccountBean copyAcc = new AccountBean();
                            copyAcc.setAccountId(account.getAccountId());
                            copyAcc.setAccountIbkNum(account.getAccountIbkNum());
                            copyAcc.setAccountName(account.getAccountName());
                            String accNum = account.getAccountNumber();
                            int start = accNum.indexOf("*");
                            int end = accNum.lastIndexOf("*");
                            if (start == -1) {
                                copyAcc.setAccountNumber(account.getAccountNumber());
                            } else {
                                accNum = accNum.substring(0, start) + "******" + accNum.substring(end + 1, accNum.length());
                                copyAcc.setAccountNumber(accNum);
                            }
                            copyAcc.setAccountName(account.getAccountName());
                            copyAcc.setAccountType(account.getAccountType());
                            copyAcc.setNickName(account.getNickName());
                            copyList.add(copyAcc);
                        }
                        model.setIdentityType(result.getIdentityType());
                        mView.onAccListSucc();
                    }
                });
    }

    /**
     * 金额上限
     */
    public void getPsnFessGetUpperLimitOfForeignCurr(PsnFessGetUpperLimitOfForeignCurrParams params) {
        showLoadingDialog();
        fessService.psnFessGetUpperLimitOfForeignCurr(params)
                .compose(this.<PsnFessGetUpperLimitOfForeignCurrResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFessGetUpperLimitOfForeignCurrResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFessGetUpperLimitOfForeignCurrResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        closeLoadingDialog();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFessGetUpperLimitOfForeignCurrResult result) {
                        closeLoadingDialog();
                        model.setAvailableBalanceCUR(result.getAvailableBalanceCUR());
                        model.setAnnRmeAmtCUR(result.getAnnRmeAmtCUR());
                        mView.caculateMaxAmount();
                    }
                });
    }

    /**
     * 4.14 014PsnFessSignConfirmation4Focus重点关注对象确认书签署
     */
    public void getPsnFessSignConfirmation4FocusParams(final String fessFlag) {
        showLoadingDialog();
        PSNGetTokenIdParams params = new PSNGetTokenIdParams();
        params.setConversationId(model.getConversationId());
        globalService.psnGetTokenId(params)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnFessSignConfirmation4FocusResult>>() {
                    @Override
                    public Observable<PsnFessSignConfirmation4FocusResult> call(String s) {
                        PsnFessSignConfirmation4FocusParams params = new PsnFessSignConfirmation4FocusParams();
                        params.setAccountId(model.getAccountId());
                        params.setConversationId(model.getConversationId());
                        params.setToken(s);
                        return fessService.psnFessSignConfirmation4FocusResult(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnFessSignConfirmation4FocusResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFessSignConfirmation4FocusResult result) {
                        PsnFessGetUpperLimitOfForeignCurrParams params = new PsnFessGetUpperLimitOfForeignCurrParams();
                        params.setFessFlag(fessFlag);
                        params.setAnnRmeAmtUSD(model.getAnnRmeAmtUSD());
                        params.setAvailableBalanceRMB(model.getAvailableBalance());
                        params.setCashRemit(model.getCashRemit());
                        params.setCurrencyCode(model.getCurrency());
                        preSubmit(false, fessFlag);
                    }
                });
    }

    /**
     * 获取牌价
     */
    public void getPsnFessQueryExchangeRate(PsnFessQueryExchangeRateParams params) {
        showLoadingDialog();
        fessService.psnFessQueryExchangeRate(params)
                .compose(this.<PsnFessQueryExchangeRateResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFessQueryExchangeRateResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFessQueryExchangeRateResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFessQueryExchangeRateResult result) {
                        mView.onExchangeRateSucc(result.getReferenceRate().toPlainString());
                    }
                });

    }

    /**
     * 提交前调用的接口
     */
    public void preSubmit(boolean isShowDialog, String fessFlag) {
        if (isShowDialog) {
            showLoadingDialog();
        }
        PsnFessQueryExchangeRebateRateParams params = new PsnFessQueryExchangeRebateRateParams();
        params.setCashRemit(model.getCashRemit());
        params.setFessFlag(fessFlag);
        params.setAccountId(model.getAccountId());
        params.setAmount(model.getTransAmount());
        params.setCurrency(model.getCurrency());
        fessService.psnFessQueryExchangeRebateRate(params)
                .compose(SchedulersCompat.<PsnFessQueryExchangeRebateRateResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFessQueryExchangeRebateRateResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        closeLoadingDialog();
                        //TODO 接口不通，先传空
                        model.setExchangeRate("");
                        model.setReferenceRate("");
                        mView.onPreSubmit();
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        //TODO 接口出错不报错

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFessQueryExchangeRebateRateResult result) {
                        closeLoadingDialog();
                        model.setExchangeRate(result.getExchangeRate());
                        model.setReferenceRate(result.getReferenceRate());
                        mView.onPreSubmit();
                    }
                });
    }

    protected void showLoadingDialog() {
        ((BussFragment) mView).showLoadingDialog();
    }

    protected void closeLoadingDialog() {
        ((BussFragment) mView).closeProgressDialog();
    }

    /**
     * 筛选出人民币
     *
     * @param list
     * @return
     */
    public PsnFessQueryAccountBalanceResult electRMB(List<PsnFessQueryAccountBalanceResult> list) {
        for (PsnFessQueryAccountBalanceResult item : list) {

            if (item.getCurrency().equals("001")) {
                return item;
            }
        }
        return new PsnFessQueryAccountBalanceResult();
    }
}
