package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.presenter;

import android.util.Log;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANQuoteDetailQuery.PsnEQuoteDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANQuoteDetailQuery.PsnEQuoteDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANOverdueStatusQuery.PsnLOANOverdueStatusQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANOverdueStatusQuery.PsnLOANOverdueStatusQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnQueryCardNumByAcctNum.PsnQueryCardNumByAcctNumParams;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.model.LoanAccountListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.model.LoanOverdueModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.ui.LoanMangerContract;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanQuoteViewModel;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by huixiaobo on 2016/9/4.
 *
 */
public class LoanMangerPresenter extends RxPresenter implements LoanMangerContract.Presenter {

    /**公用上送Id*/
    private GlobalService mGlobalService;
    /**未结清conversationId*/
    private String mConversationId;
    /**已结清conversationId*/
    private String mSconversationId;
    /**贷款查询service*/
    private PsnLoanService mPsnEloanQueryService;
    /**贷款列表*/
    private LoanMangerContract.LoanManageView mLoanView;


    public LoanMangerPresenter(LoanMangerContract.LoanManageView loanView) {
        mLoanView = loanView;
        mLoanView.setPresenter(this);
        mGlobalService = new GlobalService();
        mPsnEloanQueryService = new PsnLoanService();
    }

    /**
     *074逾期信息查询
     */
    @Override
    public void queryOverdue() {
        PsnLOANOverdueStatusQueryParams params = new PsnLOANOverdueStatusQueryParams();
        mPsnEloanQueryService.psnLOANOverdueStatusQuery(params)
                .compose(this.<PsnLOANOverdueStatusQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnLOANOverdueStatusQueryResult>applyComputationSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnLOANOverdueStatusQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mLoanView.querOverdueFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnLOANOverdueStatusQueryResult psnLOANOverdueStatusQueryResult) {
                        LoanOverdueModel loanOverdue = new LoanOverdueModel();
                        loanOverdue.setOverdueStatus(psnLOANOverdueStatusQueryResult.getOverdueStatus());
                        //逾期信息查询
                        mLoanView.queryOverdueSuccess(loanOverdue);
                    }
                });
    }

    /**
     * 045查询签约中银E贷额度列表接口
     */
    @Override
    public void queryEloanQuote() {
        PsnEQuoteDetailQueryParams psnEQuoteDetailQueryParams = new PsnEQuoteDetailQueryParams();
        psnEQuoteDetailQueryParams.setQuoteNo("");
        psnEQuoteDetailQueryParams.setQuoteFlag("F");
        psnEQuoteDetailQueryParams.setOption("02");
        mPsnEloanQueryService.psnEQuoteDetailQueryList(psnEQuoteDetailQueryParams)
                .compose(this.<List<PsnEQuoteDetailQueryResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnEQuoteDetailQueryResult>>applyComputationSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnEQuoteDetailQueryResult>>() {

                    // 重写网络请求失败弹框处理
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mLoanView.eloanQuoteFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(List<PsnEQuoteDetailQueryResult> psnEQuoteDetailQueryResults) {
                        List<EloanQuoteViewModel> eloanQuoteViewModel = eQuoteDetailResult2Model(psnEQuoteDetailQueryResults);
                        Log.d("23432543", eloanQuoteViewModel.toString());
                        mLoanView.eloanQuoteSuccess(eloanQuoteViewModel);
                    }
                });
    }

    @Override
    public void queryLoanAccount(LoanAccountListModel  lep) {
        if (mConversationId != null && "N".equals(lep.geteFlag())
                && !lep.isRefresh()) {
            queryLoanAccountconversation(lep);
        } else if (mSconversationId != null && "Y".equals(lep.geteFlag())
                && !lep.isRefresh()){
            queryLoanAccountconversation(lep);
        } else {
            queryWloanAccountconversation(lep);
        }
    }


    /**
     * 052贷款列表
     *
     */
    private void queryLoanAccountconversation(final LoanAccountListModel  lep) {
        PsnLOANListEQueryParams psnLOANListEQueryParams = builELoanAccountParams(lep);
        if ("Y".equals(lep.geteFlag())) {
            psnLOANListEQueryParams.setConversationId(lep.getSconversationId());
        } else if ("N".equals(lep.geteFlag())){
            psnLOANListEQueryParams.setConversationId(lep.getConversationId());
        }
       //psnLOANListEQueryParams.setConversationId(mConversationId);
        mPsnEloanQueryService.psnLOANListEQueryList(psnLOANListEQueryParams)
                .compose(this.<PsnLOANListEQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnLOANListEQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnLOANListEQueryResult>() {
                    //重写请求失败弹窗处理
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        if ("Y".equals(lep.geteFlag())) {
                            mLoanView.queryLoanSettledFail(biiResultErrorException);
                        } else if ("N".equals(lep.geteFlag())) {
                            mLoanView.queryLoanAccountFail(biiResultErrorException);
                        }
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnLOANListEQueryResult psnLOANListEQueryResult) {
                        LoanAccountListModel eloanAccountListModel = eLoanListResult2Model(psnLOANListEQueryResult, lep.geteFlag());
                        if ("Y".equals(lep.geteFlag())) {
                            mLoanView.queryLoanSettledSuccess(eloanAccountListModel);
                        } else if ("N".equals(lep.geteFlag())) {
                            mLoanView.queryLoanAccountSuccess(eloanAccountListModel);
                        }
                    }
                });
    }

    /**
     * 052接口分条件查询列表
     */
    private void queryWloanAccountconversation(final LoanAccountListModel  lep) {
        mGlobalService.psnCreatConversation(new PSNCreatConversationParams())
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnLOANListEQueryResult>>() {
                    @Override
                    public Observable<PsnLOANListEQueryResult> call(
                            String conversationId) {
                        if ("Y".equals(lep.geteFlag())) {
                           mSconversationId = conversationId;
                        } else if ("N".equals(lep.geteFlag())) {
                            mConversationId = conversationId;
                        }
                        PsnLOANListEQueryParams psnLOANListEQueryParams = builELoanAccountParams(lep);
                        psnLOANListEQueryParams.setConversationId(conversationId);
                        return mPsnEloanQueryService.psnLOANListEQueryList(psnLOANListEQueryParams);
                    }
                })
                .compose(this.<PsnLOANListEQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnLOANListEQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnLOANListEQueryResult>() {
                    //重写请求失败弹窗处理
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        if (mLoanView != null) {
                            if ("Y".equals(lep.geteFlag())) {
                                mLoanView.queryLoanSettledFail(biiResultErrorException);
                            } else if ("N".equals(lep.geteFlag())) {
                                mLoanView.queryLoanAccountFail(biiResultErrorException);
                            }
                        }
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnLOANListEQueryResult psnLOANListEQueryResult) {
                        LoanAccountListModel eloanAccountListModel = eLoanListResult2Model(psnLOANListEQueryResult,lep.geteFlag());
                        if (mLoanView != null ) {
                            if ("Y".equals(lep.geteFlag())) {
                                mLoanView.queryLoanSettledSuccess(eloanAccountListModel);
                            } else if ("N".equals(lep.geteFlag())) {
                                mLoanView.queryLoanAccountSuccess(eloanAccountListModel);
                            }
                        }
                    }
                });
    }

    /**
     * 025接口账户转换卡号
     */
    @Override
    public void queryCardNumByAcctNum(String acctNum) {
        PsnQueryCardNumByAcctNumParams acctNumParams = new PsnQueryCardNumByAcctNumParams();
        acctNumParams.setAcctNum(acctNum);
        mPsnEloanQueryService.psnQueryCardNumByAcctNumQuery(acctNumParams)
                .compose(this.<String>bindToLifecycle())
                .compose(SchedulersCompat.<String>applyComputationSchedulers())
                .subscribe(new BIIBaseSubscriber<String>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mLoanView.queryCardNumByAcctNumFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(String cardNum) {
                        mLoanView.queryCardNumByAcctNumSuccess(cardNum);
                    }
                });
    }


    /**
     * 查询签约中银e贷额度信息转换UiModle
     * @param result 返回值
     */
    private List<EloanQuoteViewModel> eQuoteDetailResult2Model(List<PsnEQuoteDetailQueryResult> result){
        List<EloanQuoteViewModel> eloanQuotelist = new ArrayList<EloanQuoteViewModel>();
        for (PsnEQuoteDetailQueryResult quoteData : result) {
            Log.e("添加数据", quoteData.toString());
            EloanQuoteViewModel eloanQuoteBean = BeanConvertor.toBean(quoteData, new EloanQuoteViewModel());
            eloanQuotelist.add(eloanQuoteBean);
        }
        return eloanQuotelist;
    }

    /**
     *分条件查询额度表信息转换UiModle
     * @param result 返回返回值
     */
    private LoanAccountListModel eLoanListResult2Model(PsnLOANListEQueryResult result, String quoteType) {
        LoanAccountListModel eloanAccountListModel = new LoanAccountListModel();
        eloanAccountListModel.setOverState(result.getOverState());
        eloanAccountListModel.setEndDate(result.getEndDate());
        eloanAccountListModel.setLoanList(result.getLoanList());
        eloanAccountListModel.setRecordNumber(result.getRecordNumber());
        eloanAccountListModel.setMoreFlag(result.getMoreFlag());
        if ("Y".equals(quoteType)) {
            eloanAccountListModel.setSconversationId(mSconversationId);
        } else if ("N".equals(quoteType)) {
            eloanAccountListModel.setConversationId(mConversationId);
        }
        return eloanAccountListModel;
    }

    /**
     * 分条件查询额度列表上传参数
     * @param lep 上传参数对象
     */
    private PsnLOANListEQueryParams builELoanAccountParams(LoanAccountListModel lep){
        PsnLOANListEQueryParams params = new PsnLOANListEQueryParams();
        if (lep != null) {
            params.seteLoanState(lep.geteLoanState());
            params.seteFlag(lep.geteFlag());
            params.setCurrentIndex(lep.getCurrentIndex());
            params.set_refresh(lep.is_refresh());
            params.setPageSize(lep.getPageSize());
        }
        return params;
    }

}