package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.presenter.finance;

import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeParameterQry.PsnLoanXpadPledgeParameterQryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeParameterQry.PsnLoanXpadPledgeParameterQryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.finance.FinancePledgeParamsData;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.finance.PledgeProductBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.finance.PledgeLoanFinanceProductSelectContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;
import rx.functions.Func1;

public class PledgeLoanFinanceProductSelectPresenter extends RxPresenter
        implements PledgeLoanFinanceProductSelectContract.Presenter {

    private PledgeLoanFinanceProductSelectContract.View mPledgeLoanFinanceProductSelectView;
    protected PsnLoanService mLoanService;
    protected GlobalService mGlobalService;
    protected String mConversationId;

    public PledgeLoanFinanceProductSelectPresenter(
            PledgeLoanFinanceProductSelectContract.View view) {
        mPledgeLoanFinanceProductSelectView = view;
        mLoanService = new PsnLoanService();
        mGlobalService = new GlobalService();
    }

    @Override
    public void setConversationId(String conversationId) {
        mConversationId = conversationId;
    }

    @Override
    public void qryFinancePledgeParameter(final PledgeProductBean pledgeProductBean) {
        PsnLoanXpadPledgeParameterQryParams params = new PsnLoanXpadPledgeParameterQryParams();
        params.setConversationId(mConversationId);
        params.setBancAccount(pledgeProductBean.getBancAccount());
        params.setFincCode(pledgeProductBean.getProdCode());
        params.setPledgeRate(pledgeProductBean.getPledgeRate());
        params.setProdEnd(pledgeProductBean.getProdEnd());
        mLoanService.psnLoanXpadPledgeParameterQry(params)
                    .map(new Func1<PsnLoanXpadPledgeParameterQryResult, FinancePledgeParamsData>() {
                        @Override
                        public FinancePledgeParamsData call(
                                PsnLoanXpadPledgeParameterQryResult psnLoanXpadPledgeParameterQryResult) {
                            return BeanConvertor.toBean(psnLoanXpadPledgeParameterQryResult,
                                    new FinancePledgeParamsData());
                        }
                    })
                    .compose(this.<FinancePledgeParamsData>bindToLifecycle())
                    .compose(SchedulersCompat.<FinancePledgeParamsData>applyIoSchedulers())
                    .subscribe(new BIIBaseSubscriber<FinancePledgeParamsData>() {
                        @Override
                        public void handleException(
                                BiiResultErrorException biiResultErrorException) {

                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(FinancePledgeParamsData financePledgeParamsData) {
                            financePledgeParamsData.setConversationId(mConversationId);
                            mPledgeLoanFinanceProductSelectView.onQryFinancePledgeParamsDataSuccess(
                                    financePledgeParamsData);
                        }
                    });
    }
}