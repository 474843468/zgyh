package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.balanceenquiry.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGGetBindAccount.PsnVFGGetBindAccountParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGGetBindAccount.PsnVFGGetBindAccountResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.service.LongShortForexService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.balanceenquiry.model.BalanceEnquiryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.balanceenquiry.model.XpadPsnVFGGetBindAccount;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.balanceenquiry.ui.BalanceEnquiryContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.LinkedList;
import java.util.List;

/**
 * *双向宝-余额查询  通信逻辑处理
 * Created by wjk7118 on 2016/11/29.
 */

public class BalanceEnquiryPresenter extends RxPresenter implements BalanceEnquiryContract.Presenter {

    private BalanceEnquiryContract.View mBalanceEnquiryView;
    private AccountService mAccountService;
    private LongShortForexService mLongShortForexService;

    public BalanceEnquiryPresenter(BalanceEnquiryContract.View view) {
        this.mBalanceEnquiryView = view;
        mBalanceEnquiryView.setPresenter(this);
        mAccountService = new AccountService();
        mLongShortForexService = new LongShortForexService();
    }

    //交易账户查询
    @Override
    public void queryPsnVFGGetBindAccount(final XpadPsnVFGGetBindAccount viewmodel) {
        PsnVFGGetBindAccountParams psnVFGGetBindAccountParams = new PsnVFGGetBindAccountParams();

        mLongShortForexService.psnVFGGetBindAccount(psnVFGGetBindAccountParams)
                .compose(SchedulersCompat.<PsnVFGGetBindAccountResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnVFGGetBindAccountResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mBalanceEnquiryView.queryPsnVFGGetBindAccountFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnVFGGetBindAccountResult psnVFGGetBindAccountResult) {
                        mBalanceEnquiryView.queryPsnVFGGetBindAccountSuccess(psnVFGGetBindAccountResult);
                    }
                });
    }

    public void queryBalanceEnquiryList(final BalanceEnquiryModel model) {
        PsnAccountQueryAccountDetailParams psnAccountQueryAccountDetailParams =
                buildParamsFromModel(model);
        mAccountService.psnAccountQueryAccountDetail(psnAccountQueryAccountDetailParams)
                .compose(this.<PsnAccountQueryAccountDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnAccountQueryAccountDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnAccountQueryAccountDetailResult>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnAccountQueryAccountDetailResult result) {
                        BalanceEnquiryModel viewModel = buildViewModelFromResult(result);
                        mBalanceEnquiryView.queryBalanceEnquiryListSuccess(viewModel);
                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        mBalanceEnquiryView.queryBalanceEnquiryListFail(e);

                    }
                });
    }

    private PsnAccountQueryAccountDetailParams buildParamsFromModel(BalanceEnquiryModel balanceEnquiryModel) {
        PsnAccountQueryAccountDetailParams result = new PsnAccountQueryAccountDetailParams();
        result.setAccountId(balanceEnquiryModel.getAccountId());
        return result;
    }

    private BalanceEnquiryModel buildViewModelFromResult(PsnAccountQueryAccountDetailResult psnAccountQueryAccountDetailResult) {
        BalanceEnquiryModel result = new BalanceEnquiryModel();
        List<BalanceEnquiryModel.AccountDetaiListBean> viewList = new LinkedList<>();
        List<PsnAccountQueryAccountDetailResult.AccountDetaiListBean> returnList =
                psnAccountQueryAccountDetailResult.getAccountDetaiList();
        if (returnList != null) {
            for (int i = 0; i < returnList.size(); i++) {
                BalanceEnquiryModel.AccountDetaiListBean viewBean = new BalanceEnquiryModel.AccountDetaiListBean();
                PsnAccountQueryAccountDetailResult.AccountDetaiListBean returnBean = returnList.get(i);
                viewBean.setCurrencyCode(returnBean.getCurrencyCode());
                viewBean.setBookBalance(returnBean.getBookBalance());
                viewBean.setAvailableBalance(returnBean.getAvailableBalance());
                viewBean.setCashRemit(returnBean.getCashRemit());

                viewList.add(viewBean);
            }
        }
        result.setAccountDetaiList(viewList);
        return result;
    }
}