package com.boc.bocsoft.mobile.bocmobile.buss.transfer.remitquery.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileCancelTrans.PsnMobileCancelTransParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileCancelTrans.PsnMobileCancelTransResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileRemitDetailsQuery.PsnMobileRemitDetailsQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileRemitDetailsQuery.PsnMobileRemitDetailsQueryResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileRemitQuery.PsnMobileRemitQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileRemitQuery.PsnMobileRemitQueryResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileResetSendSms.PsnMobileResetSendSmsParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileResetSendSms.PsnMobileResetSendSmsResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.remitquery.model.RemitQueryDetailInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.remitquery.model.RemitQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.remitquery.model.ResetSendSmsViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.remitquery.ui.RemitQueryContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 汇出查询BII通信逻辑处理
 * Created by wangf on 2016/6/8.
 */
public class RemitQueryPresenter implements RemitQueryContract.Presenter {

    private RemitQueryContract.View mRemitQueryView;
    private RemitQueryContract.DetailInfoView mDetailInfoView;
    private RxLifecycleManager mRxLifecycleManager;


    /**
     * 公共service
     */
    private GlobalService globalService;
    /**
     * 转账汇款的service
     */
    private TransferService transferService;

    // 撤销交易的会话ID
    private String cancelTransConversationID = "";


    public RemitQueryPresenter(RemitQueryContract.View view) {
        mRemitQueryView = view;
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        transferService = new TransferService();
    }

    public RemitQueryPresenter(RemitQueryContract.DetailInfoView infoView) {
        mDetailInfoView = infoView;
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        transferService = new TransferService();
    }

    /**
     * 汇出查询列表
     */
    @Override
    public void queryRemitQueryList(final RemitQueryViewModel remitQueryViewModel) {
        //查询之前请求会话
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnMobileRemitQueryResult>>() {
                    @Override
                    public Observable<PsnMobileRemitQueryResult> call(String conversation) {
                        PsnMobileRemitQueryParams detailParams = buildRemitQueryParams(remitQueryViewModel);
                        detailParams.setConversationId(conversation);
                        return transferService.psnMobileRemitQuery(detailParams);
                    }
                })
                .compose(SchedulersCompat.<PsnMobileRemitQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnMobileRemitQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mRemitQueryView.queryRemitQueryListFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnMobileRemitQueryResult remitQueryResult) {
                        mRemitQueryView.queryRemitQueryListSuccess(copyRemitQuery2UIModel(remitQueryResult));
                    }
                });
    }

    /**
     * 汇出详情查询
     *
     * @param infoViewModel
     */
    @Override
    public void queryRemitDetailInfo(RemitQueryDetailInfoViewModel infoViewModel) {
        PsnMobileRemitDetailsQueryParams queryParams = buildRemitDetailInfoQueryParams(infoViewModel);

        transferService.psnMobileRemitDetailsQuery(queryParams)
                .compose(mRxLifecycleManager.<PsnMobileRemitDetailsQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnMobileRemitDetailsQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnMobileRemitDetailsQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mRemitQueryView.queryRemitDetailInfoFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnMobileRemitDetailsQueryResult psnMobileRemitDetailsQueryResult) {
                        mRemitQueryView.queryRemitDetailInfoSuccess(copyRemitDetailInfoQuery2UIModel(psnMobileRemitDetailsQueryResult));
                    }
                });


    }

    /**
     * 撤销交易
     *
     * @param remitNo
     */
    @Override
    public void loadCancelTrans(final String remitNo) {
        //获取会话
        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversationId) {
                        cancelTransConversationID = conversationId;
                        // 查询TokenID
                        PSNGetTokenIdParams getTokenIdParams = new PSNGetTokenIdParams();
                        getTokenIdParams.setConversationId(conversationId);
                        return globalService.psnGetTokenId(getTokenIdParams);
                    }
                })
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .map(new Func1<String, PsnMobileCancelTransParams>() {
                    @Override
                    public PsnMobileCancelTransParams call(String s) {
                        PsnMobileCancelTransParams psnMobileCancelTransParams = new PsnMobileCancelTransParams();
                        psnMobileCancelTransParams.setConversationId(cancelTransConversationID);
                        psnMobileCancelTransParams.setToken(s);
                        psnMobileCancelTransParams.setRemitNo(remitNo);
                        return psnMobileCancelTransParams;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<PsnMobileCancelTransParams, Observable<PsnMobileCancelTransResult>>() {
                    @Override
                    public Observable<PsnMobileCancelTransResult> call(PsnMobileCancelTransParams psnMobileCancelTransParams) {
                        // 撤销交易
                        return transferService.psnMobileCancelTrans(psnMobileCancelTransParams);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnMobileCancelTransResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mDetailInfoView.loadCancelTransFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnMobileCancelTransResult mobileCancelTransResult) {
                        mDetailInfoView.loadCancelTransSuccess();
                    }
                });
    }

    /**
     * 重新发送短信
     *
     * @param resetSendSmsViewModel
     */
    @Override
    public void loadResetSendSms(ResetSendSmsViewModel resetSendSmsViewModel) {
        PsnMobileResetSendSmsParams sendSmsParams = buildResetSendSmsParams(resetSendSmsViewModel);

        transferService.psnMobileResetSendSms(sendSmsParams)
                .compose(mRxLifecycleManager.<PsnMobileResetSendSmsResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnMobileResetSendSmsResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnMobileResetSendSmsResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mDetailInfoView.loadResetSendSmsFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnMobileResetSendSmsResult resetSendSmsResult) {
                        mDetailInfoView.loadResetSendSmsSuccess();
                    }
                });
    }

    /**
     * 封装 汇出查询 请求参数
     *
     * @param remitQueryViewModel
     * @return
     */
    private PsnMobileRemitQueryParams buildRemitQueryParams(RemitQueryViewModel remitQueryViewModel) {
        PsnMobileRemitQueryParams remitQueryParams = new PsnMobileRemitQueryParams();
        remitQueryParams.setAccountId(remitQueryViewModel.getAccountId());
        remitQueryParams.setStartDate(remitQueryViewModel.getStartDate());
        remitQueryParams.setEndDate(remitQueryViewModel.getEndDate());
        remitQueryParams.setPageSize(remitQueryViewModel.getPageSize());
        remitQueryParams.setCurrentIndex(remitQueryViewModel.getCurrentIndex());
        return remitQueryParams;
    }

    /**
     * 封装 汇出查询 详情 请求参数
     *
     * @param infoViewModel
     * @return
     */
    private PsnMobileRemitDetailsQueryParams buildRemitDetailInfoQueryParams(RemitQueryDetailInfoViewModel infoViewModel) {
        PsnMobileRemitDetailsQueryParams detailsQueryParams = new PsnMobileRemitDetailsQueryParams();
        detailsQueryParams.setTransactionId(infoViewModel.getTransactionId());
        return detailsQueryParams;
    }

    /**
     * 封装 重新发送短信 请求参数
     *
     * @param resetSendSmsViewModel
     * @return
     */
    private PsnMobileResetSendSmsParams buildResetSendSmsParams(ResetSendSmsViewModel resetSendSmsViewModel) {
        PsnMobileResetSendSmsParams resetSendSmsParams = new PsnMobileResetSendSmsParams();
        resetSendSmsParams.setFromName(resetSendSmsViewModel.getFromName());
        resetSendSmsParams.setRemitStatus(resetSendSmsViewModel.getRemitStatus());
        resetSendSmsParams.setRemitNo(resetSendSmsViewModel.getRemitNo());
        resetSendSmsParams.setRemitCurrencyCode(resetSendSmsViewModel.getRemitCurrencyCode());
        resetSendSmsParams.setPayeeMobile(resetSendSmsViewModel.getPayeeMobile());
        resetSendSmsParams.setRemitAmount(resetSendSmsViewModel.getRemitAmount());
        return resetSendSmsParams;
    }


    /**
     * 转换汇出查询数据到UI层model
     */
    private RemitQueryViewModel copyRemitQuery2UIModel(PsnMobileRemitQueryResult remitQueryResult) {
        RemitQueryViewModel remitQueryViewModel = new RemitQueryViewModel();
        remitQueryViewModel.setFromAcctNumber(remitQueryResult.getFromAcctNumber());
        remitQueryViewModel.setNickName(remitQueryResult.getNickName());
        remitQueryViewModel.setRecordNumber(remitQueryResult.getRecordNumber());

        List<RemitQueryViewModel.ListBean> viewListBeanList = new ArrayList<RemitQueryViewModel.ListBean>();
        for (int i = 0; i < remitQueryResult.getList().size(); i++) {
            RemitQueryViewModel.ListBean listBean = new RemitQueryViewModel.ListBean();
            PsnMobileRemitQueryResult.ListBean item = remitQueryResult.getList().get(i);
            listBean.setTransactionId(item.getTransactionId());
            listBean.setPayeeName(item.getPayeeName());
            listBean.setPayeeMobile(item.getPayeeMobile());
            listBean.setTranDate(item.getTranDate());
            listBean.setCurrencyCode(item.getCurrencyCode());
            listBean.setCashRemit(item.getCashRemit());
            listBean.setRemitAmount(item.getRemitAmount());
            listBean.setRemitNo(item.getRemitNo());
            listBean.setRemitStatus(item.getRemitStatus());
            listBean.setAgentName(item.getAgentName());
            listBean.setAgentNum(item.getAgentNum());
            listBean.setBranchId(item.getBranchId());
            listBean.setFromActNumber(item.getFromActNumber());
            listBean.setRemark(item.getRemark());
            listBean.setChannel(item.getChannel());

            viewListBeanList.add(listBean);
        }
        remitQueryViewModel.setList(viewListBeanList);
        return remitQueryViewModel;
    }

    /**
     * 转换汇出查询 详情 数据到UI层model
     */
    private RemitQueryDetailInfoViewModel copyRemitDetailInfoQuery2UIModel(PsnMobileRemitDetailsQueryResult queryResult) {
        RemitQueryDetailInfoViewModel infoViewModel = new RemitQueryDetailInfoViewModel();

        infoViewModel.setRemitNo(queryResult.getRemitNo());
        infoViewModel.setCardNo(queryResult.getCardNo());
        infoViewModel.setFromName(queryResult.getFromName());
        infoViewModel.setRemitAmount(queryResult.getRemitAmount());
        infoViewModel.setCurrencyCode(queryResult.getCurrencyCode());
        infoViewModel.setCashRemit(queryResult.getCashRemit());
        infoViewModel.setPayeeName(queryResult.getPayeeName());
        infoViewModel.setPayeeMobile(queryResult.getPayeeMobile());
        infoViewModel.setTranDate(queryResult.getTranDate());
        infoViewModel.setRemark(queryResult.getRemark());
        infoViewModel.setRemitStatus(queryResult.getRemitStatus());
        infoViewModel.setReceiptDate(queryResult.getReceiptDate());
        infoViewModel.setAgentName(queryResult.getAgentName());
        infoViewModel.setAgentNum(queryResult.getAgentNum());
        infoViewModel.setDueDate(queryResult.getDueDate());

        return infoViewModel;
    }


    @Override
    public void subscribe() {
        mRxLifecycleManager.onStart();
    }

    @Override
    public void unsubscribe() {
        mRxLifecycleManager.onDestroy();
    }


}
