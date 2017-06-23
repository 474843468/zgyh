package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplancalc.presenter;



import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanRepaymentPlan.PsnLoanRepaymentPlanParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanRepaymentPlan.PsnLoanRepaymentPlanResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplancalc.model.RepayPlanCalcReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplancalc.model.RepayPlanCalcRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplancalc.ui.RepayPlanCalcContract;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * 还款计划试算--BII通信逻辑处理
 * Created by liuzc on 2016/8/30.
 */
public class RepayPlanCalcPresenter extends RxPresenter implements RepayPlanCalcContract.Presenter {

    private RepayPlanCalcContract.View mContractView;
    private PsnLoanService mLoanService;

    /**
     * 查询转账记录service
     */

    public RepayPlanCalcPresenter(RepayPlanCalcContract.View view){
        mContractView = view;
        mContractView.setPresenter(this);

        mLoanService = new PsnLoanService();
    }


    @Override
    public void queryRepayPlanCalc(RepayPlanCalcReq loanRepaymentPlanReq) {
//        RepayPlanCalcRes result = new RepayPlanCalcRes();
//        List<PsnLoanRepaymentPlanResult.ListBean> resList = new LinkedList<>();
//        for(int i = 0; i < 5; i ++){
//            PsnLoanRepaymentPlanResult.ListBean bean = new PsnLoanRepaymentPlanResult.ListBean();
//            bean.setRepayDate("2016/10/" + (i + 1));
//            bean.setRemainAmount(new BigDecimal(200));
//            bean.setRemainCapital(new BigDecimal(160));
//            bean.setRemainInterest(new BigDecimal(40));
//            resList.add(bean);
//        }
//        result.setList(resList);
//        mContractView.queryRepayPlanCalcSuccess(result);

        PsnLoanRepaymentPlanParams psnLoanRepaymentPlanParams
                = buildRepaymentPlan(loanRepaymentPlanReq);
        mLoanService.psnLoanRepaymentPlan(psnLoanRepaymentPlanParams)
                .compose(this.<PsnLoanRepaymentPlanResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnLoanRepaymentPlanResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnLoanRepaymentPlanResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnLoanRepaymentPlanResult result) {
                        RepayPlanCalcRes res = new RepayPlanCalcRes();
                        res.setList(result.getList());
                        res.setTotalAmount(result.getTotalAmount());
                        res.setTotalCapital(result.getTotalCapital());
                        res.setTotalInterest(result.getTotalInterest());
                        //获取还款计划试算成功调用
                        mContractView.queryRepayPlanCalcSuccess(res);

                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        //获取还款计划试算失败调用
                        mContractView.queryRepayPlanCalcFail(e);

                    }
                });
    }

    private PsnLoanRepaymentPlanParams buildRepaymentPlan(RepayPlanCalcReq loanRepaymentPlanReq){
        PsnLoanRepaymentPlanParams result = new PsnLoanRepaymentPlanParams();
        result.setProductBigType(loanRepaymentPlanReq.getActType());
        result.setAmount(loanRepaymentPlanReq.getAmount());
        result.setProductCatType(loanRepaymentPlanReq.getCat());
        result.setIssueRepayDate(loanRepaymentPlanReq.getIssueRepayDate());
        result.setLoanPeriod(loanRepaymentPlanReq.getLoanPeriod());
        result.setLoanRate(loanRepaymentPlanReq.getLoanRate());
        result.setLoanRepayAccount(loanRepaymentPlanReq.getLoanRepayAccount());
        result.setRepayFlag(loanRepaymentPlanReq.getPayType());
        result.setNextRepayDate(loanRepaymentPlanReq.getNextRepayDate());
        return result;
    }
}
