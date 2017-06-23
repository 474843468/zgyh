package com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.presenter;

import com.boc.bocsoft.mobile.bii.bus.activitymanagementpaltform.model.PsnQueryTransActivityStatus.PsnQueryTransActivityStatusParams;
import com.boc.bocsoft.mobile.bii.bus.activitymanagementpaltform.model.PsnQueryTransActivityStatus.PsnQueryTransActivityStatusResult;
import com.boc.bocsoft.mobile.bii.bus.activitymanagementpaltform.service.ActivityManagementPaltformService;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnSingleTransQueryTransferRecord.PsnSingleTransQueryTransferRecordParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnSingleTransQueryTransferRecord.PsnSingleTransQueryTransferRecordResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryTransferRecordDetail.PsnTransQueryTransferRecordDetailParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryTransferRecordDetail.PsnTransQueryTransferRecordDetailResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.ui.TransContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;

/**
 * Created by WYme on 2016/9/27.
 */
public class PsnTransResultPagePresenter extends RxPresenter implements TransContract.TransPresenterResultPage {

    /**
     * 转账汇款service
     */
    private TransferService transService;
    private TransContract.TransViewResultPage mTransResultView;//转账填写页面
    private ActivityManagementPaltformService activityManagementPaltformService; //活动管理平台

    public PsnTransResultPagePresenter(TransContract.TransViewResultPage resultPageView){
        mTransResultView=resultPageView;
        mTransResultView.setPresenter(this);
        transService=new TransferService();
        activityManagementPaltformService = new ActivityManagementPaltformService();
    }
//    @Override
//    public void transNationalRealtimeRecord(PsnSingleTransQueryTransferRecordParams params) {
//        transService.psnSingleTransQueryTransferRecord(params)
//                .compose(this.<PsnSingleTransQueryTransferRecordResult>bindToLifecycle())
//                .compose(SchedulersCompat.<PsnSingleTransQueryTransferRecordResult>applyIoSchedulers())
//                .subscribe(new BIIBaseSubscriber<PsnSingleTransQueryTransferRecordResult>() {
//                    @Override
//                    public void handleException(BiiResultErrorException biiResultErrorException) {
//                        mTransResultView.transNationalRealtimeRecordFailed(biiResultErrorException);
//                    }
//
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onNext(PsnSingleTransQueryTransferRecordResult recordResult) {
//                            mTransResultView.transNationalRealtimeRecordSuccess(recordResult);
//                    }
//                });
//    }

//    @Override
//    public void transNationalRecordDetail(PsnTransQueryTransferRecordDetailParams params) {
//        transService.psnTransQueryTransferRecordDetail(params)
//                .compose(this.<PsnTransQueryTransferRecordDetailResult>bindToLifecycle())
//                .compose(SchedulersCompat.<PsnTransQueryTransferRecordDetailResult>applyIoSchedulers())
//                .subscribe(new BIIBaseSubscriber<PsnTransQueryTransferRecordDetailResult>() {
//                    @Override
//                    public void handleException(BiiResultErrorException biiResultErrorException) {
//                        mTransResultView.transNationalRealtimeRecordFailed(biiResultErrorException);
//                    }
//
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onNext(PsnTransQueryTransferRecordDetailResult transResult) {
//                        mTransResultView.transNationalRecordDetailSuccess(transResult);
//                    }
//                });
//    }

    @Override
    public Observable<PsnTransQueryTransferRecordDetailResult> transNationalRecordDetail(PsnTransQueryTransferRecordDetailParams params) {
        return transService.psnTransQueryTransferRecordDetail(params);
    }

    /**
     * 判断是否满足抽奖资格，并返回票信息
     * @param transactionId 交易码
     */
    @Override
    public void queryTransActivityStatus(String transactionId) {
        PsnQueryTransActivityStatusParams psnQueryTransActivityStatusParams = new PsnQueryTransActivityStatusParams();
        psnQueryTransActivityStatusParams.setTransactionId(transactionId);
        activityManagementPaltformService.psnQueryTransActivityStatus(psnQueryTransActivityStatusParams)
                .compose(this.<PsnQueryTransActivityStatusResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnQueryTransActivityStatusResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnQueryTransActivityStatusResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransResultView.queryTransActivityStatusFailed();
                    }

                    //如果不想弹框报错，则不注释
//                    @Override
//                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
//
//                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnQueryTransActivityStatusResult psnQueryTransActivityStatusResult) {
                        mTransResultView.queryTransActivityStatusSuccess(psnQueryTransActivityStatusResult);
                    }
                });
    }


}
