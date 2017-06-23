package com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedpurchase.presenter;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCompanyInfoQuery.PsnFundCompanyInfoQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCompanyInfoQuery.PsnFundCompanyInfoQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryFundBalance.PsnFundQueryFundBalanceParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryFundBalance.PsnFundQueryFundBalanceResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundStatusDdApplyQuery.PsnFundStatusDdApplyQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedpurchase.ui.InvestContract;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.ValidinvestModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundBalanceModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundCompanyModel;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huixiaobo on 2016/12/16.
 * 网络请求管理类
 */
public class InvestPresenter extends RxPresenter implements InvestContract.Presenter {
    /**定申请求view*/
    private InvestContract.InvestView mInvestView;
    /**网络请求服务类*/
    private FundService mFundService;

    public InvestPresenter(InvestContract.InvestView investView) {
        mInvestView = investView;
        mFundService = new FundService();
    }
    /**015接口请求*/
    @Override
    public void qureyfundBalance(final String fundcode) {
        PsnFundQueryFundBalanceParams params = new PsnFundQueryFundBalanceParams();
        params.setFundCode(fundcode);
        mFundService.psnFundQueryFundBalance(params)
                .compose(this.<PsnFundQueryFundBalanceResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFundQueryFundBalanceResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundQueryFundBalanceResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mInvestView.fundBalanceFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundQueryFundBalanceResult psnFundQueryFundBalanceResult) {
                        mInvestView.fundBalanceSuccess(builValidData(psnFundQueryFundBalanceResult));
                    }
                });
    }
    /**061接口请求*/
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
                        mInvestView.fundCompanyFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnFundCompanyInfoQueryResult psnFundCompanyInfoQueryResult) {
                        mInvestView.fundCompanySuccess(BeanConvertor.toBean(psnFundCompanyInfoQueryResult, new FundCompanyModel()));
                    }
                });

    }

    /**
     * 011接口有效申请数据转换
     * @param result
     * @return
     */
    private FundBalanceModel builValidData(PsnFundQueryFundBalanceResult result) {
        FundBalanceModel validModel = null;
        if (result != null && result.getFundBalance() != null && result.getFundBalance().size() > 0) {
            validModel = new FundBalanceModel();
            List<FundBalanceModel.FundBalanceBean> fundBeanList = new ArrayList<FundBalanceModel.FundBalanceBean>();
            for (PsnFundQueryFundBalanceResult.FundBalanceBean funstatus : result.getFundBalance()) {
                FundBalanceModel.FundBalanceBean fundBean = BeanConvertor.toBean(funstatus, new FundBalanceModel.FundBalanceBean());
                FundBalanceModel.FundBalanceBean.FundInfoBean  fundInfoBean = BeanConvertor.toBean(funstatus.getFundInfo(),
                        new FundBalanceModel.FundBalanceBean.FundInfoBean());
                fundBean.setFundInfo(fundInfoBean);
                fundBeanList.add(fundBean);
            }
            validModel.setFundBalance(fundBeanList);
        }
        return validModel;
    }
}
