package com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedredeem.presenter;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCompanyInfoQuery.PsnFundCompanyInfoQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCompanyInfoQuery.PsnFundCompanyInfoQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryFundBalance.PsnFundQueryFundBalanceParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryFundBalance.PsnFundQueryFundBalanceResult;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedredeem.ui.RedeemContract;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundBalanceModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundCompanyModel;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

/**
 * Created by huixiaobo on 2016/12/16.
 * 网络请求管理类
 */
public class RedeemPresenter extends RxPresenter implements RedeemContract.Presenter {

    private RedeemContract.RedeemView mRedeemView;
    /**网络请求服务类*/
    private FundService mFundService;

    public RedeemPresenter(RedeemContract.RedeemView redeemView) {
        mRedeemView = redeemView;
        mFundService = new FundService();
    }
    @Override
    public void qureyfundBalance(String fundcode) {
        PsnFundQueryFundBalanceParams params = new PsnFundQueryFundBalanceParams();
       // params.setFundCode(fundcode);
        mFundService.psnFundQueryFundBalance(params)
                .compose(this.<PsnFundQueryFundBalanceResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFundQueryFundBalanceResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundQueryFundBalanceResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mRedeemView.fundBalanceFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundQueryFundBalanceResult psnFundQueryFundBalanceResult) {
                        mRedeemView.fundBalanceSuccess(BeanConvertor.toBean(psnFundQueryFundBalanceResult, new FundBalanceModel()));
                    }
                });
    }

    @Override
    public void queryFundCompany(String fundcode) {
        PsnFundCompanyInfoQueryParams params = new PsnFundCompanyInfoQueryParams();
        params.setFundCode(fundcode);
        mFundService.psnFundCompanyInfoQuery(params)
                .compose(this.<PsnFundCompanyInfoQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFundCompanyInfoQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundCompanyInfoQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mRedeemView.fundCompanyFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnFundCompanyInfoQueryResult psnFundCompanyInfoQueryResult) {
                        mRedeemView.fundCompanySuccess(BeanConvertor.toBean(psnFundCompanyInfoQueryResult, new FundCompanyModel()));
                    }
                });
    }
}
