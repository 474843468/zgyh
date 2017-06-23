package com.boc.bocsoft.mobile.bocmobile.buss.transfer.transferrecord.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnRemitReturnInfo.PsnRemitReturnInfoParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnRemitReturnInfo.PsnRemitReturnInfoResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryTransferRecord.PsnTransQueryTransferRecordParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryTransferRecord.PsnTransQueryTransferRecordResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryTransferRecordDetail.PsnTransQueryTransferRecordDetailParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryTransferRecordDetail.PsnTransQueryTransferRecordDetailResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryTransferRecordsNew.PsnTransQueryTransferRecordsNewParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryTransferRecordsNew.PsnTransQueryTransferRecordsNewResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transferrecord.model.RemitReturnInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transferrecord.model.TransferRecordDetailInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transferrecord.model.TransferRecordViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transferrecord.model.TransferRecordViewModelNew;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transferrecord.ui.TransferRecordContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * 转账记录BII通信逻辑处理
 * Created by wangf on 2016/6/8.
 */
public class TransferRecordPresenter implements TransferRecordContract.Presenter {

    private TransferRecordContract.View mTransferRecordView;
    private RxLifecycleManager mRxLifecycleManager;
    private RemitReturnInfoViewModel infoViewModel;

    /**
     * 会话
     */
    private String conversation;
    /**
     * 公共service
     */
    private GlobalService globalService;
    /**
     * 查询转账记录service
     */
    private TransferService transferService;


    public TransferRecordPresenter(TransferRecordContract.View transferRecordView) {
        mTransferRecordView = transferRecordView;
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        transferService = new TransferService();
    }

    /**
     * 查询 转账记录 列表
     */
    @Override
    public void queryTransferRecordList(final TransferRecordViewModel transferRecordViewModel) {
        //查询之前请求会话
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnTransQueryTransferRecordResult>>() {
                    @Override
                    public Observable<PsnTransQueryTransferRecordResult> call(String conversation) {
                        PsnTransQueryTransferRecordParams detailParams = buildTransferRecordParams(transferRecordViewModel);
                        detailParams.setConversationId(conversation);
                        return transferService.psnTransQueryTransferRecord(detailParams);
                    }
                })
                .compose(SchedulersCompat.<PsnTransQueryTransferRecordResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransQueryTransferRecordResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransferRecordView.queryTransferRecordListFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnTransQueryTransferRecordResult transQueryTransferRecordResult) {
                        mTransferRecordView.queryTransferRecordListSuccess(copyTransferRecord2UIModel(transQueryTransferRecordResult));
                    }
                });
    }

    /**
     * 查询 转账记录 列表 - 新接口
     */
    @Override
    public void queryTransferRecordListNew(TransferRecordViewModelNew transferRecordViewModel) {
        PsnTransQueryTransferRecordsNewParams params = buildTransferRecordParamsNew(transferRecordViewModel);

        transferService.psnTransQueryTransferRecordsNew(params)
                .compose(mRxLifecycleManager.<PsnTransQueryTransferRecordsNewResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnTransQueryTransferRecordsNewResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransQueryTransferRecordsNewResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransferRecordView.queryTransferRecordListFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnTransQueryTransferRecordsNewResult transferRecordDetailResult) {
                        mTransferRecordView.queryTransferRecordListSuccessNew(copyTransferRecord2UIModelNew(transferRecordDetailResult));
                    }
                });
    }

    /**
     * 查询 转账记录 详细信息
     */
    @Override
    public void queryTransferRecordDetailInfo(String transactionId) {
        PsnTransQueryTransferRecordDetailParams params = buildTransferRecordDetailInfoParams(transactionId);

        transferService.psnTransQueryTransferRecordDetail(params)
                .compose(mRxLifecycleManager.<PsnTransQueryTransferRecordDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnTransQueryTransferRecordDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransQueryTransferRecordDetailResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransferRecordView.queryTransferRecordDetailInfoFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnTransQueryTransferRecordDetailResult transferRecordDetailResult) {
                        mTransferRecordView.queryTransferRecordDetailInfoSuccess(copyTransferRecordDetailInfo2UIModel(transferRecordDetailResult));
                    }
                });
    }


    /**
     * 查询 退汇交易 信息
     * 此处还需查询 转账记录 详细信息
     *
     * @param transactionId
     */
    @Override
    public void queryDetailAndRemitReturnInfo(final String transactionId) {
        infoViewModel = new RemitReturnInfoViewModel();

        PsnTransQueryTransferRecordDetailParams params = buildTransferRecordDetailInfoParams(transactionId);
        transferService.psnTransQueryTransferRecordDetail(params)
                .compose(mRxLifecycleManager.<PsnTransQueryTransferRecordDetailResult>bindToLifecycle())
                .flatMap(new Func1<PsnTransQueryTransferRecordDetailResult, Observable<PsnRemitReturnInfoResult>>() {
                    @Override
                    public Observable<PsnRemitReturnInfoResult> call(PsnTransQueryTransferRecordDetailResult detailResult) {

                        infoViewModel.setDetailInfoViewModel(copyTransferRecordDetailInfo2UIModel(detailResult));
                        PsnRemitReturnInfoParams returnInfoParams = buildRemitReturnInfoParams(transactionId);
                        return transferService.psnRemitReturnInfo(returnInfoParams);
                    }
                })
                .compose(SchedulersCompat.<PsnRemitReturnInfoResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnRemitReturnInfoResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransferRecordView.queryRemitReturnInfoFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnRemitReturnInfoResult psnRemitReturnInfoResult) {
                        copyRemitReturnInfo2UIModel(psnRemitReturnInfoResult);
                        mTransferRecordView.queryRemitReturnInfoSuccess(infoViewModel);
//                        mTransferRecordView.queryRemitReturnInfoSuccess(copyRemitReturnInfo2UIModel(psnRemitReturnInfoResult));
                    }
                });
    }



    /**
     * 封装 转账记录 请求参数
     *
     * @param transferRecordViewModel
     * @return
     */
    private PsnTransQueryTransferRecordParams buildTransferRecordParams(TransferRecordViewModel transferRecordViewModel) {
        PsnTransQueryTransferRecordParams recordParams = new PsnTransQueryTransferRecordParams();
        recordParams.setTransType(transferRecordViewModel.getTransType());
        recordParams.setStartDate(transferRecordViewModel.getStartDate());
        recordParams.setEndDate(transferRecordViewModel.getEndDate());
        recordParams.setPageSize(transferRecordViewModel.getPageSize());
        recordParams.setCurrentIndex(transferRecordViewModel.getCurrentIndex());
        return recordParams;
    }

    /**
     * 封装 转账记录 请求参数 - 新接口
     *
     * @param transferRecordViewModel
     * @return
     */
    private PsnTransQueryTransferRecordsNewParams buildTransferRecordParamsNew(TransferRecordViewModelNew transferRecordViewModel) {
        PsnTransQueryTransferRecordsNewParams recordParams = new PsnTransQueryTransferRecordsNewParams();
        recordParams.setTransType(transferRecordViewModel.getTransType());
        recordParams.setStartDate(transferRecordViewModel.getStartDate());
        recordParams.setEndDate(transferRecordViewModel.getEndDate());
        recordParams.setStartAmount(transferRecordViewModel.getStartAmount());
        recordParams.setEndAmount(transferRecordViewModel.getEndAmount());
        recordParams.setPayeeAccountName(transferRecordViewModel.getPayeeAccountName());
        recordParams.setStatus(transferRecordViewModel.getStatus());
        recordParams.setPageSize(transferRecordViewModel.getPageSize());
        recordParams.setCurrentIndex(transferRecordViewModel.getCurrentIndex());
        return recordParams;
    }

    /**
     * 封装 转账详情 请求参数
     *
     * @param transactionId
     * @return
     */
    private PsnTransQueryTransferRecordDetailParams buildTransferRecordDetailInfoParams(String transactionId) {
        PsnTransQueryTransferRecordDetailParams recordParams = new PsnTransQueryTransferRecordDetailParams();
        recordParams.setTransId(transactionId);
        return recordParams;
    }

    /**
     * 封装 退汇信息 请求参数
     *
     * @param transactionId
     * @return
     */
    private PsnRemitReturnInfoParams buildRemitReturnInfoParams(String transactionId) {
        PsnRemitReturnInfoParams remitReturnInfoParams = new PsnRemitReturnInfoParams();
        remitReturnInfoParams.setTransId(transactionId);
        return remitReturnInfoParams;
    }


    /**
     * 转换 转账记录 数据到UI层model
     */
    private TransferRecordViewModel copyTransferRecord2UIModel(PsnTransQueryTransferRecordResult transQueryTransferRecordResult) {
        TransferRecordViewModel transferRecordViewModel = new TransferRecordViewModel();
        transferRecordViewModel.setRecordnumber(transQueryTransferRecordResult.getRecordnumber());

        List<TransferRecordViewModel.ListBean> viewListBeanList = new ArrayList<TransferRecordViewModel.ListBean>();
        for (int i = 0; i < transQueryTransferRecordResult.getList().size(); i++) {
            TransferRecordViewModel.ListBean listBean = new TransferRecordViewModel.ListBean();
            PsnTransQueryTransferRecordResult.ListBean item = transQueryTransferRecordResult.getList().get(i);

            listBean.setPayeeAccountType(item.getPayeeAccountType());
            listBean.setPayerAccountType(item.getPayerAccountType());
            listBean.setTransMode(item.getTransMode());
            listBean.setCashRemit(item.getCashRemit());
            listBean.setTransferType(item.getTransferType());
            listBean.setPayeeAccountNumber(item.getPayeeAccountNumber());
            listBean.setPayerAccountNumber(item.getPayerAccountNumber());
            listBean.setPayeeCountry(item.getPayeeCountry());
            listBean.setFeeCur(item.getFeeCur());
            listBean.setAmount(item.getAmount());
            listBean.setChannel(item.getChannel());
            listBean.setCommissionCharge(item.getCommissionCharge());
            listBean.setFurInfo(item.getFurInfo());
            listBean.setPayeeBankName(item.getPayeeBankName());
            listBean.setPayeeIbk(item.getPayeeIbk());
            listBean.setPayerIbknum(item.getPayerIbknum());
            listBean.setPostage(item.getPostage());
            listBean.setReturnCode(item.getReturnCode());
            listBean.setTransactionId(item.getTransactionId());
            listBean.setPayeeAccountName(item.getPayeeAccountName());
            listBean.setPayerAccountName(item.getPayerAccountName());
            listBean.setBatSeq(item.getBatSeq());
            listBean.setStatus(item.getStatus());
            listBean.setPaymentDate(item.getPaymentDate());
            listBean.setFirstSubmitDate(item.getFirstSubmitDate());
            listBean.setFeeCur2(item.getFeeCur2());
            listBean.setReexchangeStatus(item.getReexchangeStatus());
            listBean.setCashRemitExchange(item.getCashRemitExchange());

            viewListBeanList.add(listBean);
        }

        transferRecordViewModel.setList(viewListBeanList);

        return transferRecordViewModel;
    }

    /**
     * 转换 转账记录 数据到UI层model - 新接口
     */
    private TransferRecordViewModelNew copyTransferRecord2UIModelNew(PsnTransQueryTransferRecordsNewResult transQueryTransferRecordResult) {
        TransferRecordViewModelNew transferRecordViewModel = new TransferRecordViewModelNew();
        transferRecordViewModel.setRecordnumber(transQueryTransferRecordResult.getRecordnumber());

        List<TransferRecordViewModelNew.ListBean> viewListBeanList = new ArrayList<TransferRecordViewModelNew.ListBean>();
        for (int i = 0; i < transQueryTransferRecordResult.getList().size(); i++) {
            TransferRecordViewModelNew.ListBean listBean = new TransferRecordViewModelNew.ListBean();
            PsnTransQueryTransferRecordsNewResult.ListBean item = transQueryTransferRecordResult.getList().get(i);

            listBean.setBatSeq(item.getBatSeq());
            listBean.setStatus(item.getStatus());
            listBean.setPaymentDate(item.getPaymentDate());
            listBean.setPayerAccountNumber(item.getPayerAccountNumber());
            listBean.setPayeeAccountName(item.getPayeeAccountName());
            listBean.setPayeeAccountNumber(item.getPayeeAccountNumber());
            listBean.setFeeCur(item.getFeeCur());
            listBean.setAmount(item.getAmount());
            listBean.setChannel(item.getChannel());
            listBean.setTransactionId(item.getTransactionId());
            listBean.setTransferType(item.getTransferType());
            listBean.setReexchangeStatus(item.getReexchangeStatus());

            viewListBeanList.add(listBean);
        }

        transferRecordViewModel.setList(viewListBeanList);

        return transferRecordViewModel;
    }

    /**
     * 转换 转账详情 数据到UI层model
     */
    private TransferRecordDetailInfoViewModel copyTransferRecordDetailInfo2UIModel(PsnTransQueryTransferRecordDetailResult transferRecordDetailResult) {
        TransferRecordDetailInfoViewModel infoViewModel = new TransferRecordDetailInfoViewModel();
        infoViewModel.setChannel(transferRecordDetailResult.getChannel());
        infoViewModel.setStatus(transferRecordDetailResult.getStatus());
        infoViewModel.setTransactionId(transferRecordDetailResult.getTransactionId());
        infoViewModel.setPayerAccountNumber(transferRecordDetailResult.getPayerAccountNumber());
        infoViewModel.setAmount(transferRecordDetailResult.getAmount());
        infoViewModel.setPaymentDate(transferRecordDetailResult.getPaymentDate());
        infoViewModel.setFirstSubmitDate(transferRecordDetailResult.getFirstSubmitDate());
        infoViewModel.setReturnCode(transferRecordDetailResult.getReturnCode());
        infoViewModel.setPayeeAccountName(transferRecordDetailResult.getPayeeAccountName());
        infoViewModel.setPayeeAccountNumber(transferRecordDetailResult.getPayeeAccountNumber());
        infoViewModel.setPayerAccountName(transferRecordDetailResult.getPayerAccountName());
        infoViewModel.setBatSeq(transferRecordDetailResult.getBatSeq());
        infoViewModel.setCommissionCharge(transferRecordDetailResult.getCommissionCharge());
        infoViewModel.setFurInfo(transferRecordDetailResult.getFurInfo());
        infoViewModel.setPayeeBankName(transferRecordDetailResult.getPayeeBankName());
        infoViewModel.setPayeeIbk(transferRecordDetailResult.getPayeeIbk());
        infoViewModel.setPostage(transferRecordDetailResult.getPostage());
        infoViewModel.setPayerIbknum(transferRecordDetailResult.getPayerIbknum());
        infoViewModel.setPayeeAccountType(transferRecordDetailResult.getPayeeAccountType());
        infoViewModel.setPayerAccountType(transferRecordDetailResult.getPayerAccountType());
        infoViewModel.setCashRemit(transferRecordDetailResult.getCashRemit());
        infoViewModel.setTransferType(transferRecordDetailResult.getTransferType());
        infoViewModel.setTransMode(transferRecordDetailResult.getTransMode());
        infoViewModel.setPayeeCountry(transferRecordDetailResult.getPayeeCountry());
        infoViewModel.setFeeCur(transferRecordDetailResult.getFeeCur());
        infoViewModel.setFeeCur2(transferRecordDetailResult.getFeeCur2());
        infoViewModel.setCashRemitExchange(transferRecordDetailResult.getCashRemitExchange());
        infoViewModel.setReexchangeStatus(transferRecordDetailResult.getReexchangeStatus());

        return infoViewModel;
    }

    /**
     * 转换 退汇信息 数据到UI层model
     */
    private void copyRemitReturnInfo2UIModel(PsnRemitReturnInfoResult psnRemitReturnInfoResult) {
//        RemitReturnInfoViewModel infoViewModel = new RemitReturnInfoViewModel();
        infoViewModel.setPayerActno(psnRemitReturnInfoResult.getPayerActno());
        infoViewModel.setPayeeActno(psnRemitReturnInfoResult.getPayeeActno());
        infoViewModel.setReexchangeAmount(psnRemitReturnInfoResult.getReexchangeAmount());
        infoViewModel.setReexchangeInfo(psnRemitReturnInfoResult.getReexchangeInfo());
        infoViewModel.setRemittanceInfo(psnRemitReturnInfoResult.getRemittanceInfo());
        infoViewModel.setReexchangeDate(psnRemitReturnInfoResult.getReexchangeDate());
        infoViewModel.setReexchangeStatus(psnRemitReturnInfoResult.getReexchangeStatus());

//        return infoViewModel;
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
