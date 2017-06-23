package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAccountListAndDetailQuery.PsnLOANAccountListAndDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAccountListAndDetailQuery.PsnLOANAccountListAndDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.ui.FacilityUseRecordQryContact;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by XieDu on 2016/7/12.
 */
public class FacilityUseRecordPresenter extends RxPresenter
        implements FacilityUseRecordQryContact.Presenter {

    private FacilityUseRecordQryContact.View mFacilityDetailView;
    private PsnLoanService mPsnLoanService;

    /**
     * 会话ID
     */
    private String conversationID = null;
    /**
     * 公共service
     */
    private GlobalService globalService;

    public FacilityUseRecordPresenter(FacilityUseRecordQryContact.View view) {
        mFacilityDetailView = view;
        mFacilityDetailView.setPresenter(this);
        mPsnLoanService = new PsnLoanService();
        globalService = new GlobalService();
    }

    @Override
    public void getUseFacilityRecord(PsnLOANAccountListAndDetailQueryParams params) {
//        PsnLOANAccountListAndDetailQueryResult viewModel = new PsnLOANAccountListAndDetailQueryResult();
//        List<PsnLOANAccountListAndDetailQueryResult.ListBean> list = new LinkedList<>();
//        for(int i = 0; i < 4; i ++){
//            PsnLOANAccountListAndDetailQueryResult.ListBean curBean = new PsnLOANAccountListAndDetailQueryResult.ListBean();
//
//            curBean.setCurrencyCode("001");
//            curBean.setAccountNumber("446862278649");
//            curBean.setLoanToDate("2017/10/02");
//            curBean.setLoanType("1046");
//            curBean.setInterestType("B");
//            curBean.setLoanAmount(new BigDecimal(2000));
//            curBean.setLoanPeriod(12);
//            curBean.setLoanPeriodUnit("M");
//            curBean.setNoclosedInterest("0.1");
//            curBean.setOverdueIssue(3);
//            curBean.setPayedIssueSum(0);
//            curBean.setRemainCapital(new BigDecimal(200));
//            curBean.setRemainIssue(3);
//            curBean.setThisIssueRepayAmount(new BigDecimal(55.11));
//            curBean.setThisIssueRepayDate("2016/03/09");
//            curBean.setThisIssueRepayInterest(new BigDecimal(17.99));
//            curBean.setPayAccountNumber("2343213");
//            curBean.setLoanRate(6.4);
//            curBean.setLoanDate("2015/10/02");
//            curBean.setOverdueIssueSum("2");
//
//            list.add(curBean);
//        }
//        viewModel.setAccountList(list);
//
//        viewModel.setRecordNumber(20);
//        viewModel.setMoreFlag("1");
//        mFacilityDetailView.onLoadSuccess(viewModel);

        if(conversationID == null){
            queryWithoutConversation(params);
        }
        else{
            queryWithConversation(params);
        }
    }

    private void queryWithConversation(final PsnLOANAccountListAndDetailQueryParams params){
        params.setConversationId(conversationID);
        mPsnLoanService.psnLOANAccountListAndDetailQuery(params)
            .compose(
                    this.<PsnLOANAccountListAndDetailQueryResult>bindToLifecycle())
            .compose(
                    SchedulersCompat.<PsnLOANAccountListAndDetailQueryResult>applyIoSchedulers())
            .subscribe(
                new BIIBaseSubscriber<PsnLOANAccountListAndDetailQueryResult>() {
                    @Override
                    public void handleException(
                            BiiResultErrorException biiResultErrorException) {
                        mFacilityDetailView.onLoadFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(
                            PsnLOANAccountListAndDetailQueryResult psnLOANAccountListAndDetailQueryResults) {
                        mFacilityDetailView.onLoadSuccess(psnLOANAccountListAndDetailQueryResults);
                    }
                });
    }

    private void queryWithoutConversation(final PsnLOANAccountListAndDetailQueryParams params){
        globalService.psnCreatConversation(new PSNCreatConversationParams())
            .compose(this.<String>bindToLifecycle())
            .flatMap(new Func1<String, Observable<PsnLOANAccountListAndDetailQueryResult>>() {
                @Override
                public Observable<PsnLOANAccountListAndDetailQueryResult> call(
                        String conversationId) {
                    conversationID = conversationId;
                    params.setConversationId(conversationId);
                    return mPsnLoanService.psnLOANAccountListAndDetailQuery(params);
                }
            })
            .compose(
                    SchedulersCompat.<PsnLOANAccountListAndDetailQueryResult>applyIoSchedulers())
            .subscribe(
                new BIIBaseSubscriber<PsnLOANAccountListAndDetailQueryResult>() {
                    @Override
                    public void handleException(
                            BiiResultErrorException biiResultErrorException) {
                        mFacilityDetailView.onLoadFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(
                            PsnLOANAccountListAndDetailQueryResult psnLOANAccountListAndDetailQueryResults) {
                        mFacilityDetailView.onLoadSuccess(psnLOANAccountListAndDetailQueryResults);
                    }
                });
    }
}
