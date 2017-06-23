package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnQueryInvtBindingInfo.PsnQueryInvtBindingInfoParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnQueryInvtBindingInfo.PsnQueryInvtBindingInfoResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundRiskEvaluationQuery.PsnFundRiskEvaluationQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundRiskEvaluationQuery.PsnFundRiskEvaluationQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvestmentManageIsOpen.PsnInvestmentManageIsOpenParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.model.InvstBindingInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.ui.FundUserContract;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.riskassessment.model.PsnFundRiskEvaluationQueryModel;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

/**
 * 基金账户相关接口的presenter
 * Created by liuzc on 2016/12/27.
 */
public class FundUserPresenter extends RxPresenter implements FundUserContract.Presenter{
    private FundUserContract.View mContractView;
    private AccountService accountService;// 账户service
    private WealthManagementService wealthService;// 理财Service
    private FundService fundService;// 基金Service

    public FundUserPresenter(FundUserContract.View contractView){
        mContractView = contractView;
        mContractView.setPresenter(this);

        wealthService = new WealthManagementService();
        accountService = new AccountService();
        fundService = new FundService();
    }

    //判断是否开通投资服务
    @Override
    public void queryInvestmentManageIsOpen(PsnInvestmentManageIsOpenParams params) {
        wealthService.psnInvestmentManageIsOpen(params)
            .compose(this.<Boolean>bindToLifecycle())
            .compose(SchedulersCompat.<Boolean>applyIoSchedulers())
            .subscribe(new BIIBaseSubscriber<Boolean>() {
                @Override
                public void handleException(BiiResultErrorException biiResultErrorException) {
                    mContractView.queryInvestmentManageIsOpenFail(biiResultErrorException);
                }

                @Override
                public void commonHandleException(BiiResultErrorException biiResultErrorException) {

                }

                @Override
                public void onCompleted() {

                }

                @Override
                public void onNext(Boolean result) {
                    mContractView.queryInvestmentManageIsOpenSuccess(result);
                }
            });
    }

    //查询投资交易账号绑定信息
    @Override
    public void queryInvtBindingInfo(PsnQueryInvtBindingInfoParams params) {
        accountService.psnQueryInvtBindingInfo(params)
            .compose(this.<PsnQueryInvtBindingInfoResult>bindToLifecycle())
            .compose(SchedulersCompat.<PsnQueryInvtBindingInfoResult>applyIoSchedulers())
            .subscribe(new BIIBaseSubscriber<PsnQueryInvtBindingInfoResult>() {
                @Override
                public void handleException(BiiResultErrorException biiResultErrorException) {
                    mContractView.queryInvtBindingInfoFail(biiResultErrorException);
                }

                @Override
                public void onCompleted() {

                }

                @Override
                public void onNext(PsnQueryInvtBindingInfoResult result) {
                    InvstBindingInfoViewModel viewResult = BeanConvertor.toBean(result, new InvstBindingInfoViewModel());
                    mContractView.queryInvtBindingInfoSuccess(viewResult);
                }
            });
    }

    //查询风险评估等级
    @Override
    public void queryFundRiskEvaluation(PsnFundRiskEvaluationQueryParams params) {
        fundService.psnFundRiskEvaluationQuery(params)
            .compose(SchedulersCompat.<PsnFundRiskEvaluationQueryResult>applyIoSchedulers())
            .subscribe(new BIIBaseSubscriber<PsnFundRiskEvaluationQueryResult>() {
                @Override
                public void handleException(BiiResultErrorException biiResultErrorException) {
                    mContractView.queryFundRiskEvaluationFail(biiResultErrorException);
                }

                @Override
                public void onCompleted() {

                }

                @Override
                public void onNext(PsnFundRiskEvaluationQueryResult psnFundDetailQueryOutlayResult) {
                    mContractView.queryFundRiskEvaluationSuccess(psnFundDetailQueryOutlayResult);
                }
            });
    }
}
