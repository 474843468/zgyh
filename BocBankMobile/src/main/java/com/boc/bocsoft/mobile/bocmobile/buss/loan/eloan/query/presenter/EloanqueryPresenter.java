package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCommonQueryOprLoginInfo.PsnQueryOprLoginInfoParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCommonQueryOprLoginInfo.PsnQueryOprLoginInfoResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnCommonQuerySystemDateTime.PsnQuerySystemDateTimeParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnCommonQuerySystemDateTime.PsnQuerySystemDateTimeResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANCycleLoanAccountListQuery.PsnCycleLoanAccountEQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANHistoryQuery.PsnEHistoryQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANHistoryQuery.PsnEHistoryQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANOverdueQuery.PsnEOverdueQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANOverdueQuery.PsnEOverdueQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANQuoteDetailQuery.PsnEQuoteDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANQuoteDetailQuery.PsnEQuoteDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANRemainQuery.PsnERemainQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANRemainQuery.PsnERemainQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELoanCredit.PsnLoanCreditParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELoanCredit.PsnLoanCreditResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepayAccountDetailQuery.PsnDrawingDetailParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepayAccountDetailQuery.PsnDrawingDetailResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANUseRecordsQuery.PsnLOANUseRecordsQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANUseRecordsQuery.PsnLOANUseRecordsQueryResultBean;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.utils.SpUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanAccountListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanCreditViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanDrawDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanDrawRecordModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanQuoteViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanRepaymentModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.UserCycleLoanModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui.EloanQueryContract;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;
import org.threeten.bp.LocalDateTime;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by huixiaobo on 2016/6/16.
 * 中银E贷数据请求处理类
 */
public class EloanqueryPresenter extends RxPresenter implements EloanQueryContract.Presenter {

    /**中银E贷查询service*/
    private PsnLoanService mPsnEloanQueryService;
    /**公用上送Id*/
    private GlobalService mGlobalService;
    /**公用的未结清ConversationId*/
    public  String mConversationId;
    /**公用的已结清ConversationId*/
    public String mSconversationId;
    boolean isLoan = false;

    private AccountService mAccountService;
    /**中银E贷激活页面*/
    private EloanQueryContract.View mEloanView;
    /**中银E贷用款页面*/
    private EloanQueryContract.DrawView mDrawView;
    /**中银E贷详情view*/
    private EloanQueryContract.RepayNumView mRepayNumView;
    /**中银E贷用款记录view*/
    private EloanQueryContract.DrawRecordView mdrawRecordView;
    /**开始日期提前三个月*/
    private static final int STATD_TIME = 3;

    /**
     * 中银E贷签约账号
     */
    public EloanqueryPresenter(EloanQueryContract.View eLoanView) {
        mEloanView = eLoanView;
        mEloanView.setPresenter(this);
        mPsnEloanQueryService = new PsnLoanService();
        mGlobalService = new GlobalService();
        mAccountService = new AccountService();
    }
    /**
     * 还款详情
     */
    public EloanqueryPresenter(EloanQueryContract.RepayNumView repayNumView) {
        mRepayNumView = repayNumView;
        mRepayNumView.setPresenter(this);
        mPsnEloanQueryService = new PsnLoanService();
        mGlobalService = new GlobalService();
    }
    /**
     * 用款页面记录列表
     */
    public EloanqueryPresenter(EloanQueryContract.DrawView drawView) {
        mDrawView = drawView;
        mDrawView.setPresenter(this);
        mGlobalService = new GlobalService();
        mPsnEloanQueryService = new PsnLoanService();
    }

    /**
     * 还款记录列表
     */
    public EloanqueryPresenter(EloanQueryContract.DrawRecordView drawRecordView) {
        mdrawRecordView = drawRecordView;
        mdrawRecordView.setPresenter(this);
        mGlobalService = new GlobalService();
        mPsnEloanQueryService = new PsnLoanService();
    }

    /**
     *052查询个人循环贷款接口
     */
    @Override
    public void queryCycleLoan() {
        mGlobalService.psnCreatConversation(new PSNCreatConversationParams())
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnLOANListEQueryResult>>() {
                    @Override
                    public Observable<PsnLOANListEQueryResult> call(
                            String conversationId) {
                        PsnLOANListEQueryParams psnLOANListEQueryParams = builCycleLoanParams("10", "N");
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
                        mEloanView.eCycleLoanFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnLOANListEQueryResult psnLOANListEQueryResult) {
                        EloanAccountListModel eloanAccountListModel = eLoanListResult2Model
                                (psnLOANListEQueryResult, null);
                        mEloanView.eCycleLoanSuccess(eloanAccountListModel);

                    }

                });

    }

    /**
     *046查询授信额度接口
     */
    @Override
    public void queryCredit() {
        PsnLoanCreditParams psnLoanCreditParams = new PsnLoanCreditParams();
        psnLoanCreditParams.setLoanPrdNo("OC-LOAN");
        mPsnEloanQueryService.psnLoanCredit(psnLoanCreditParams)
                .compose(this.<PsnLoanCreditResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnLoanCreditResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnLoanCreditResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mEloanView.eCreditFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnLoanCreditResult creditResult) {
                        mEloanView.eCreditSuccess(eCreditResult2Model(creditResult));
                    }
                });

    }

    /**
     * 052分条件查询贷款账户列表接口
     */
    @Override
    public void queryLoanAccount(EloanAccountListModel alp) {
        if (mDrawView != null ) {
            queryLoanAccountWconversation(alp);
        } else {
            queryLoanAccountConversation(alp);
        }
      }

    @Override
    public void queryOverdue() {

    }


    /**
     * 052分条件查询贷款账户列表接口
     */
    private void queryLoanAccountConversation(final EloanAccountListModel alp) {
        PsnLOANListEQueryParams psnLOANListEQueryParams = builELoanAccountParams(alp);
        if ("Y".equals(alp.geteFlag())) {
            psnLOANListEQueryParams.setConversationId(alp.getSconversationId());
        } else if ("N".equals(alp.geteFlag())) {
            psnLOANListEQueryParams.setConversationId(alp.getConversationId());
        }
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
                        if (mDrawView != null) {
                            if ("Y".equals(alp.geteFlag())) {
                                mDrawView.eLoanSettleFail(biiResultErrorException);
                            } else if ("N".equals(alp.geteFlag())) {
                                mDrawView.eLoanRepayFail(biiResultErrorException);
                            }
                        } else {
                            mdrawRecordView.eRepaymentFail(biiResultErrorException);
                        }
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnLOANListEQueryResult psnLOANListEQueryResult) {
                        EloanAccountListModel eloanAccountListModel = eLoanListResult2Model(psnLOANListEQueryResult, alp.geteFlag());
                        if (mDrawView != null) {
                            if ("Y".equals(alp.geteFlag())) {
                                mDrawView.eLoanSettleSuccess(eloanAccountListModel);
                            } else if ("N".equals(alp.geteFlag())) {
                                mDrawView.eLoanRpaySusccess(eloanAccountListModel);
                            }
                        } else {
                            mdrawRecordView.eRepaymentSuccess(eloanAccountListModel);
                        }
                    }
                });

    }

    /**
     * 052分条件查询贷款账户列表接口
     */
    private void queryLoanAccountWconversation(final EloanAccountListModel alp) {

        mGlobalService.psnCreatConversation(new PSNCreatConversationParams())
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnLOANListEQueryResult>>() {
                    @Override
                    public Observable<PsnLOANListEQueryResult> call(
                            String conversationId) {
//                        mConversationId = conversationId;
//                        if (!isLoan) {
//                            mSconversationId = conversationId;
//                        } else  {
//                            mConversationId = conversationId;
//                        }
                        if ("Y".equals(alp.geteFlag())) {
                            mSconversationId = conversationId;
                        } else if ("N".equals(alp.geteFlag())) {
                            mConversationId = conversationId;
                        }
                        PsnLOANListEQueryParams psnLOANListEQueryParams = builELoanAccountParams(alp);
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
                        if (mDrawView != null) {
                            if ("Y".equals(alp.geteFlag())) {
                                mDrawView.eLoanSettleFail(biiResultErrorException);
                            } else if ("N".equals(alp.geteFlag())) {
                                mDrawView.eLoanRepayFail(biiResultErrorException);
                            }
                        } else {
                            mdrawRecordView.eRepaymentFail(biiResultErrorException);
                        }
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnLOANListEQueryResult psnLOANListEQueryResult) {
                        EloanAccountListModel eloanAccountListModel = eLoanListResult2Model(psnLOANListEQueryResult, alp.geteFlag());
                        if (mDrawView != null) {
                            if ("Y".equals(alp.geteFlag())) {
                                mDrawView.eLoanSettleSuccess(eloanAccountListModel);
                            } else if ("N".equals(alp.geteFlag())) {
                                mDrawView.eLoanRpaySusccess(eloanAccountListModel);
                            }
                        } else {
                            mdrawRecordView.eRepaymentSuccess(eloanAccountListModel);
                        }

                    }

                });
    }


    /**
     *007用款详情查询接口
     *@param loanActNum 贷款账户
     */
    @Override
    public void queryDrawingDetail(String loanActNum) {
        PsnDrawingDetailParams psnDrawingDetailParams = builElaonDrawingParams(loanActNum);
        mPsnEloanQueryService.psnDrawingDetail(psnDrawingDetailParams)
                .compose(this.<PsnDrawingDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnDrawingDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnDrawingDetailResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mRepayNumView.eDrawingDetailFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnDrawingDetailResult psnDrawingDetailResult) {
                        mRepayNumView.eDrawingDetailSuccess(detailDetailResult2Model(psnDrawingDetailResult));
                    }
                });
    }


    //TODO 需要测试T1环境下的 新返回字段 交易渠道、贷款用途
    /**
     * 018贷款用途详情查询
     * @param actNum 贷款账号
     */
    @Override
    public void queryDraw(String actNum, String date) {
        PsnLOANUseRecordsQueryParams params = builDrawRecordParams(actNum, date);
        mPsnEloanQueryService.psnLOANUseRecordsQueryList(params)
                .compose(this.<List<PsnLOANUseRecordsQueryResultBean>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnLOANUseRecordsQueryResultBean>>applyComputationSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnLOANUseRecordsQueryResultBean>>() {
                    //TODO 先屏蔽弹窗错误提示
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mRepayNumView.eDrawRecordFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(List<PsnLOANUseRecordsQueryResultBean> psnLOANUseRecordsQueryResults) {
                        List<EloanDrawRecordModel> drawRecoredRes = drawRecoredRes(psnLOANUseRecordsQueryResults);
                        mRepayNumView.eDrawRecordSuccess(drawRecoredRes);
                    }
                });
    }

    /**
     * 分条件查询额度列表上传参数
     * @param alp E贷标识
     */
    private PsnLOANListEQueryParams builELoanAccountParams(EloanAccountListModel alp){
        PsnLOANListEQueryParams psnLOANListEQueryParams = new PsnLOANListEQueryParams();
        if (alp != null) {
            psnLOANListEQueryParams.seteLoanState(alp.geteLoanState());
            psnLOANListEQueryParams.seteFlag(alp.geteFlag());
            psnLOANListEQueryParams.setPageSize(alp.getPageSize());
            psnLOANListEQueryParams.setCurrentIndex(alp.getCurrentIndex());
            psnLOANListEQueryParams.set_refresh(alp.get_refresh());
        }
        return psnLOANListEQueryParams;
    }

    /**
     * 查询循环贷款上传参数
     * @param eLoanState E贷标识
     */
    private PsnLOANListEQueryParams builCycleLoanParams(String eLoanState, String eFlag){
        PsnLOANListEQueryParams psnLOANListEQueryParams = new PsnLOANListEQueryParams();
        if (!TextUtils.isEmpty(eLoanState) && !TextUtils.isEmpty(eFlag)) {
            psnLOANListEQueryParams.seteLoanState(eLoanState);
            psnLOANListEQueryParams.seteFlag(eFlag);
            psnLOANListEQueryParams.setPageSize(50);
            psnLOANListEQueryParams.setCurrentIndex(0);
            psnLOANListEQueryParams.set_refresh("true");
        }
        return psnLOANListEQueryParams;
    }

    /**
     * 用款详情查询上送参数校验
     * @param loanActNum 贷款账户
     */
    private PsnDrawingDetailParams builElaonDrawingParams(String loanActNum) {
        PsnDrawingDetailParams psnDrawingDetailParams = new PsnDrawingDetailParams();
        if (!TextUtils.isEmpty(loanActNum)) {
            psnDrawingDetailParams.setLoanAccount(loanActNum);
        }
        return psnDrawingDetailParams;
    }

    /**
     * 贷款详情用途
     */
    private PsnLOANUseRecordsQueryParams builDrawRecordParams(String actNum, String date) {
        PsnLOANUseRecordsQueryParams params = new PsnLOANUseRecordsQueryParams();
        if (!TextUtils.isEmpty(actNum)) {
            //获取当前时间
           // LocalDateTime date =  ApplicationContext.getInstance().getCurrentSystemDate();
            //格式化当前时间
           // String time = date.minusMonths(STATD_TIME).format(DateFormatters.dateFormatter1);
            params.setStartDate(date);
            params.setEndDate(date);
            params.setLoanActNum(actNum);
            params.setPageSize("10");
            params.setCurrentIndex("1");
        }
        return  params;
    }

    /**
     * 查询签约中银e贷额度信息转换UiModle
     * @param result 返回值
     */
    private List<EloanQuoteViewModel> eQuoteDetailResult2Model(List<PsnEQuoteDetailQueryResult> result){
        List<EloanQuoteViewModel> eloanQuotelist = new ArrayList<EloanQuoteViewModel>();
        for(int i = 0; i< result.size(); i++) {
        	EloanQuoteViewModel eloanQuoteBean = new EloanQuoteViewModel();
            eloanQuoteBean.setQuoteNo(result.get(i).getQuoteNo());
            eloanQuoteBean.setQuoteState(result.get(i).getQuoteState());
            eloanQuoteBean.setQuoteType(result.get(i).getQuoteType());
            eloanQuoteBean.setRate(result.get(i).getRate());
            eloanQuoteBean.setLoanToDate(result.get(i).getLoanToDate());
            eloanQuoteBean.setLoanType(result.get(i).getLoanType());
            eloanQuoteBean.setLoanBanlance(result.get(i).getLoanBanlance());
            eloanQuoteBean.setAvailableAvl(result.get(i).getAvailableAvl());
            eloanQuoteBean.setCurrency(result.get(i).getCurrency());
            eloanQuoteBean.setNextRepayDate(result.get(i).getNextRepayDate());
            eloanQuoteBean.setIssueRepayDate(result.get(i).getIssueRepayDate());
            eloanQuoteBean.setRegisterStates(result.get(i).getRegisterStates());
            eloanQuoteBean.setRepayAcct(result.get(i).getRepayAcct());
            eloanQuoteBean.setUseAvl(result.get(i).getUseAvl());
            eloanQuotelist.add(eloanQuoteBean);
        }
        return eloanQuotelist;
    }


    /**
     *个人循环贷款信息转换UiModle
     *@param result 返回值
     */
    private List<UserCycleLoanModel> userCycleLoanResult2Model(List<PsnCycleLoanAccountEQueryResult> result) {
        List<UserCycleLoanModel> userloanList = new ArrayList<UserCycleLoanModel>();
        
        for (int i = 0; i< result.size(); i++) {
        	UserCycleLoanModel  userCycleLoanModel = new UserCycleLoanModel();
            userCycleLoanModel.setAccountNumber(result.get(i).getAccountNumber());
            userCycleLoanModel.setLoanAccountStats(result.get(i).getLoanAccountStats());
            userCycleLoanModel.setLoanCycleBalance(result.get(i).getLoanCycleBalance());
            userloanList.add(userCycleLoanModel);
        }
        return userloanList;
    }

    /**
     * 预授信额度信息转换UiModle
     * @param result 返回值
     */
    private EloanCreditViewModel eCreditResult2Model(PsnLoanCreditResult result) {
        EloanCreditViewModel eloanCreditViewModel = new EloanCreditViewModel();
        eloanCreditViewModel.setQuoteState(result.getQuoteState());
        eloanCreditViewModel.setQuote(result.getQuote());
        eloanCreditViewModel.setQuoteNo(result.getQuoteNo());
        return eloanCreditViewModel;
    }

    /**
     *分条件查询额度表信息转换UiModle
     * @param result 返回返回值
     */
    private EloanAccountListModel eLoanListResult2Model(PsnLOANListEQueryResult result, String type) {
        EloanAccountListModel eloanAccountListModel = new EloanAccountListModel();
        eloanAccountListModel.setOverState(result.getOverState());
        eloanAccountListModel.setEndDate(result.getEndDate());
        eloanAccountListModel.setRecordNumber(result.getRecordNumber());
        eloanAccountListModel.setMoreFlag(result.getMoreFlag());
        eloanAccountListModel.setLoanList(result.getLoanList());
        if ("Y".equals(type)) {
            eloanAccountListModel.setSconversationId(mSconversationId);
        } else if ("N".equals(type)) {
            eloanAccountListModel.setConversationId(mConversationId);
        }
        return eloanAccountListModel;
    }

    /**
     * 用款详情查询信息转换UiModle
     *
     */
    private EloanDrawDetailModel detailDetailResult2Model (PsnDrawingDetailResult result) {
        EloanDrawDetailModel eloanDrawDetailModel = new EloanDrawDetailModel();
        eloanDrawDetailModel.setCurrency(result.getCurrency());
        eloanDrawDetailModel.setLoanType(result.getLoanType());
        eloanDrawDetailModel.setLoanToDate(result.getLoanToDate());
        eloanDrawDetailModel.setRemainCapital(result.getRemainCapital());
        eloanDrawDetailModel.setLoanAccount(result.getLoanAccount());
        eloanDrawDetailModel.setLoanPeriod(result.getLoanPeriod());
        eloanDrawDetailModel.setLoanRate(result.getLoanRate());
        eloanDrawDetailModel.setInterestType(result.getInterestType());
        eloanDrawDetailModel.setLoanDate(result.getLoanDate());
        eloanDrawDetailModel.setPayAccountNumber(result.getPayAccountNumber());
        eloanDrawDetailModel.setLoanAmount(result.getLoanAmount());
        eloanDrawDetailModel.setThisIssueRepayInterest(result.getThisIssueRepayInterest());
        eloanDrawDetailModel.setThisIssueRepayDate(result.getThisIssueRepayDate());
        eloanDrawDetailModel.setThisIssueRepayAmount(result.getThisIssueRepayAmount());
        eloanDrawDetailModel.setLoanRepayPeriod(result.getLoanRepayPeriod());
        eloanDrawDetailModel.setOverdueIssue(result.getOverdueIssue());
        eloanDrawDetailModel.setOverdueIssueSum(result.getOverdueIssueSum());
        eloanDrawDetailModel.setPayedIssueSum(result.getPayedIssueSum());
        eloanDrawDetailModel.setRemainIssue(result.getRemainIssue());
        return eloanDrawDetailModel;
    }

    /**
     * 贷款用途详情
     */
    private List<EloanDrawRecordModel> drawRecoredRes(List<PsnLOANUseRecordsQueryResultBean> psnQueryResults) {
        List<EloanDrawRecordModel> drawRecord = new ArrayList<EloanDrawRecordModel>();
        if (psnQueryResults != null && psnQueryResults.size() > 0) {
            for (PsnLOANUseRecordsQueryResultBean drawRed : psnQueryResults) {
                EloanDrawRecordModel recordModel = new EloanDrawRecordModel();
                recordModel.setChannel(drawRed.getChannel());
                recordModel.setMerchant(drawRed.getMerchant());
                drawRecord.add(recordModel);
            }
        }
        return drawRecord;
    }


}
