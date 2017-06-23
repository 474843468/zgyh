package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.SellExchange.presenter;

import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryAccount.PsnFessQueryAccountParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryAccount.PsnFessQueryAccountResult;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryAccountBalance.PsnFessQueryAccountBalanceParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryAccountBalance.PsnFessQueryAccountBalanceResult;
import com.boc.bocsoft.mobile.bii.bus.fess.service.FessService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.model.BuyExchangeModel;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.presenter.BuyExchangeContract;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.presenter.BuySellExchangePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.ui.BuyExchangeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.SellExchange.ui.CurrencyAccFragment;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 结汇币种页面
 * Created by gwluo on 2016/12/21.
 */

public class SellExchangeCurrencyPresenter extends RxPresenter implements BuyExchangeContract.Presenter {
    protected BuyExchangeContract.CurrencyAccView mView;
    protected FessService fessService;
    protected BuyExchangeModel model;
    protected GlobalService globalService;

    public SellExchangeCurrencyPresenter(BuyExchangeContract.CurrencyAccView view) {
        fessService = new FessService();
        globalService = new GlobalService();
        model = view.getModel();
        mView = view;
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
     * 获取余额
     *
     * @param accId
     * @param isSingleInterface 是否单个接口
     */
    public void getPsnFessQueryAccountBalance(final String accId, final boolean isSingleInterface) {
        showLoadingDialog();
        PsnFessQueryAccountBalanceParams params = new PsnFessQueryAccountBalanceParams();
        params.setAccountId(accId);
        fessService.psnFessQueryAccountBalance(params)
                .compose(this.<List<PsnFessQueryAccountBalanceResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnFessQueryAccountBalanceResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnFessQueryAccountBalanceResult>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        onAccBalanceResult(null, accId, isSingleInterface);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(List<PsnFessQueryAccountBalanceResult> result) {
                        onAccBalanceResult(result, accId, isSingleInterface);
                    }
                });
    }

    /**
     * 处理余额请求
     *
     * @param result 失败传 null
     * @param accId
     */
    private synchronized void onAccBalanceResult(List<PsnFessQueryAccountBalanceResult> result, String accId, boolean isSingle) {
        LinkedHashMap<String, List<PsnFessQueryAccountBalanceResult>> balanceMap = model.getBalanceMap();
        removeRMBBalanceZero(result);
        if (result != null && result.size() > 0) {
            balanceMap.put(accId, result);
        }
        mView.updateAccBalance(isSingle);
    }

    /**
     * 删除人民币币种和余额为0币种
     *
     * @param result
     */
    private void removeRMBBalanceZero(List<PsnFessQueryAccountBalanceResult> result) {
        if (result == null) return;
        List<PsnFessQueryAccountBalanceResult> rmbBalanceZero = new ArrayList<>();//余额为0和人民币币种集合
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getCurrency().equals(ApplicationConst.CURRENCY_CNY) || new BigDecimal(MoneyUtils.trimAmountZero(result.get(i).getAvailableBalance())).compareTo(new BigDecimal("0")) == 0) {
                rmbBalanceZero.add(result.get(i));
            }
        }
        //删除余额为0币种
        for (PsnFessQueryAccountBalanceResult item : rmbBalanceZero) {
            result.remove(item);
        }
    }

    protected void showLoadingDialog() {
        ((BussFragment) mView).showLoadingDialog();
    }

    protected void closeLoadingDialog() {
        ((BussFragment) mView).closeProgressDialog();
    }
}
