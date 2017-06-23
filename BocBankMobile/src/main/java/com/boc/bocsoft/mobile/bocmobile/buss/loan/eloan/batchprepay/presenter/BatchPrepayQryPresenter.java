package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.batchprepay.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.batchprepay.model.BatchPrepayQryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.batchprepay.ui.query.BatchPrepayQryContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by liuzc on 2016/9/7.
 */
public class BatchPrepayQryPresenter extends RxPresenter implements BatchPrepayQryContract.Presenter {

    private BatchPrepayQryContract.View mContractView;
    private PsnLoanService mLoanService;

    /**
     * 会话ID
     */
    private String conversationID = null;
    /**
     * 公共service
     */
    private GlobalService globalService;
    /**
     * 查询转账记录service
     */

    public BatchPrepayQryPresenter(BatchPrepayQryContract.View view){
        mContractView = view;
        mContractView.setPresenter(this);
        globalService = new GlobalService();
        mLoanService = new PsnLoanService();
    }

    public void setConversationID(String value){
        conversationID = value;
    }

    @Override
    public void queryBatchPrepayList(final BatchPrepayQryModel params) {
//        BatchPrepayQryModel model = new BatchPrepayQryModel();
//        model.setRecordNumber(20);
//        model.setMoreFlag("1");
//        List<PsnLOANListEQueryResult.PsnLOANListEQueryBean> list =
//                new LinkedList<PsnLOANListEQueryResult.PsnLOANListEQueryBean>();
//
//        PsnLOANListEQueryResult qryResult = new PsnLOANListEQueryResult();
//        for(int i = 0 ; i < 5; i ++){
//            PsnLOANListEQueryResult.PsnLOANListEQueryBean bean = qryResult.new PsnLOANListEQueryBean();
//            bean.setLoanCycleLifeTerm("12");
//            bean.setCurrencyCode("CNY");
//            bean.setLoanCycleMatDate("2016/10/10");
//            bean.setRemainCapital("3000");
//            bean.setThisIssueRepayInterest("200");
//            bean.setLoanDate("2016/10/11");
//            if(i != 1){
//                bean.setOverDueStat("00");
//            }
//            else{
//                bean.setOverDueStat("01");
//            }
//
//            bean.setTransFlag("advance");
//            list.add(bean);
//        }
//        model.setLoanList(list);
//        mContractView.queryBatchPrepayListSuccess(model);


        if(conversationID == null){
            queryWithoutConversation(params);
        }
        else{
            queryWithConversation(params);
        }
    }

    //创建subscriber
    private BIIBaseSubscriber<PsnLOANListEQueryResult> genSubscriber(){
        return new BIIBaseSubscriber<PsnLOANListEQueryResult>() {
            // 重写网络请求失败弹框处理
            @Override
            public void commonHandleException(BiiResultErrorException biiResultErrorException) {
            }

            @Override
            public void handleException(BiiResultErrorException biiResultErrorException) {
                mContractView.queryBatchPrepayListFail(biiResultErrorException);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(PsnLOANListEQueryResult psnLOANListEQueryResult) {
                BatchPrepayQryModel model = eLoanListResult2Model(psnLOANListEQueryResult);
                mContractView.queryBatchPrepayListSuccess(model);
            }
        };
    }

    /**
     * 带有会话ID的请求，不用重新请求会话ID
     * @param params
     */
    private void queryWithConversation(final BatchPrepayQryModel params){
        PsnLOANListEQueryParams psnLOANListEQueryParams = builELoanAccountParams(params);
        psnLOANListEQueryParams.setConversationId(conversationID);
        psnLOANListEQueryParams.set_refresh("false");

        mLoanService.psnLOANListEQueryList(psnLOANListEQueryParams)
                .compose(this.<PsnLOANListEQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnLOANListEQueryResult>applyIoSchedulers())
                .subscribe(genSubscriber());
    }

    /**
     * 先请求会话ID，再开始查询数据
     * @param params
     */
    private void queryWithoutConversation(final BatchPrepayQryModel params){
        globalService.psnCreatConversation(new PSNCreatConversationParams())
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnLOANListEQueryResult>>() {
                    @Override
                    public Observable<PsnLOANListEQueryResult> call(
                            String conversationId) {
                        conversationID = conversationId;
                        PsnLOANListEQueryParams psnLOANListEQueryParams = builELoanAccountParams(params);
                        psnLOANListEQueryParams.setConversationId(conversationId);
                        return mLoanService.psnLOANListEQueryList(psnLOANListEQueryParams);
                    }
                })
                .compose(SchedulersCompat.<PsnLOANListEQueryResult>applyIoSchedulers())
                .subscribe(genSubscriber());
    }

    /**
     *分条件查询额度表信息转换UiModle
     * @param result 返回返回值
     */
    private BatchPrepayQryModel eLoanListResult2Model(PsnLOANListEQueryResult result ) {
        BatchPrepayQryModel eBatchPrepayQryModel = new BatchPrepayQryModel();
        eBatchPrepayQryModel.setOverState(result.getOverState());
        eBatchPrepayQryModel.setMoreFlag(result.getMoreFlag());
        eBatchPrepayQryModel.setEndDate(result.getEndDate());
        eBatchPrepayQryModel.setLoanList(result.getLoanList());
        return eBatchPrepayQryModel;
    }

    /**
     * 分条件查询额度列表上传参数
     * @param lep 上传参数对象
     */
    private PsnLOANListEQueryParams builELoanAccountParams(BatchPrepayQryModel lep){
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
