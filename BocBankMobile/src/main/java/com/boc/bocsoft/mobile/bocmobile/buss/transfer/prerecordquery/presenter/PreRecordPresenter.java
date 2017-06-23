package com.boc.bocsoft.mobile.bocmobile.buss.transfer.prerecordquery.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPreRecordDelete.PsnTransPreRecordDeleteParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPreRecordDelete.PsnTransPreRecordDeleteResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPreRecordDetailQuery.PsnTransPreRecordDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPreRecordDetailQuery.PsnTransPreRecordDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPreRecordQuery.PsnTransPreRecordQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPreRecordQuery.PsnTransPreRecordQueryResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.prerecordquery.model.PreRecordDetailInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.prerecordquery.model.PreRecordViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.prerecordquery.ui.PreRecordContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 预约管理BII通信逻辑处理
 * Created by wangf on 2016/7/22.
 */
public class PreRecordPresenter implements PreRecordContract.Presenter {
    private PreRecordContract.View mPreRecordView;
    private PreRecordContract.DetailInfoView mPreRecordInfoView;
    private RxLifecycleManager mRxLifecycleManager;

    /**
     * 公共service
     */
    private GlobalService globalService;
    /**
     * 转账汇款的service
     */
    private TransferService transferService;

    // 预交易删除的会话ID
    private String deleteRecordConversationID = "";


    public PreRecordPresenter(PreRecordContract.View view) {
        mPreRecordView = view;
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        transferService = new TransferService();
    }

    public PreRecordPresenter(PreRecordContract.DetailInfoView infoView) {
        mPreRecordInfoView = infoView;
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        transferService = new TransferService();
    }


    /**
     * 预约管理列表查询
     *
     * @param preRecordViewModel
     */
    @Override
    public void queryPreRecordList(PreRecordViewModel preRecordViewModel) {
        PsnTransPreRecordQueryParams params = buildPreRecordParams(preRecordViewModel);
        transferService.psnTransPreRecordQuery(params)
                .compose(mRxLifecycleManager.<PsnTransPreRecordQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnTransPreRecordQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransPreRecordQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mPreRecordView.queryPreRecordListFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnTransPreRecordQueryResult recordQueryResult) {
                        mPreRecordView.queryPreRecordListSuccess(copyPreRecord2UIModel(recordQueryResult));
                    }
                });

    }

    /**
     * 预约管理详情查询
     *
     * @param dateType      日期查询类型 0：按执行日期查 1：按预约日期查询
     * @param transactionId 网银交易序号
     * @param batSeq        转账批次号
     */
    @Override
    public void queryPreRecordInfo(String dateType, String transactionId, String batSeq) {
        PsnTransPreRecordDetailQueryParams params = buildPreRecordInfoParams(dateType, transactionId, batSeq);
        transferService.psnTransPreRecordDetailQuery(params)
                .compose(mRxLifecycleManager.<PsnTransPreRecordDetailQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnTransPreRecordDetailQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransPreRecordDetailQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mPreRecordView.queryPreRecordInfoFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnTransPreRecordDetailQueryResult detailQueryResult) {
                        mPreRecordView.queryPreRecordInfoSuccess(copyPreRecordInfo2UIModel(detailQueryResult));
                    }
                });
    }

    /**
     * 预约交易删除
     *
     * @param batSeq        转账批次号
     * @param dateType      日期查询类型
     * @param transactionId 网银交易序号
     */
    @Override
    public void loadDeletePreRecord(final String batSeq, final String dateType, final String transactionId) {
        //获取会话
        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversationId) {
                        deleteRecordConversationID = conversationId;
                        // 查询TokenID
                        PSNGetTokenIdParams getTokenIdParams = new PSNGetTokenIdParams();
                        getTokenIdParams.setConversationId(conversationId);
                        return globalService.psnGetTokenId(getTokenIdParams);
                    }
                })
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .map(new Func1<String, PsnTransPreRecordDeleteParams>() {
                    @Override
                    public PsnTransPreRecordDeleteParams call(String token) {
                        PsnTransPreRecordDeleteParams psnTransPreRecordDeleteParams = new PsnTransPreRecordDeleteParams();
                        psnTransPreRecordDeleteParams.setConversationId(deleteRecordConversationID);
                        psnTransPreRecordDeleteParams.setToken(token);
                        psnTransPreRecordDeleteParams.setBatSeq(batSeq);
                        psnTransPreRecordDeleteParams.setDateType(dateType);
                        psnTransPreRecordDeleteParams.setTransactionId(transactionId);
                        return psnTransPreRecordDeleteParams;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<PsnTransPreRecordDeleteParams, Observable<PsnTransPreRecordDeleteResult>>() {
                    @Override
                    public Observable<PsnTransPreRecordDeleteResult> call(PsnTransPreRecordDeleteParams preRecordDeleteParams) {
                        // 撤销交易
                        return transferService.psnTransPreRecordDelete(preRecordDeleteParams);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnTransPreRecordDeleteResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mPreRecordInfoView.loadDeletePreRecordFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnTransPreRecordDeleteResult deleteResult) {
                        mPreRecordInfoView.loadDeletePreRecordSuccess();
                    }
                });
    }


    /**
     * 封装 预约管理 - 列表查询  请求参数
     *
     * @param preRecordViewModel
     * @return
     */
    private PsnTransPreRecordQueryParams buildPreRecordParams(PreRecordViewModel preRecordViewModel) {
        PsnTransPreRecordQueryParams recordQueryParams = new PsnTransPreRecordQueryParams();
        recordQueryParams.setDateType(preRecordViewModel.getDateType());
        recordQueryParams.setStartDate(preRecordViewModel.getStartDate());
        recordQueryParams.setEndDate(preRecordViewModel.getEndDate());
        recordQueryParams.setCurrentIndex(preRecordViewModel.getCurrentIndex() + "");
        recordQueryParams.setPageSize(preRecordViewModel.getPageSize() + "");
        recordQueryParams.set_refresh(preRecordViewModel.get_refresh());
        return recordQueryParams;
    }

    /**
     * 封装 预约管理 - 详情查询  请求参数
     *
     * @param dateType      日期查询类型 0：按执行日期查 1：按预约日期查询
     * @param transactionId 网银交易序号
     * @param batSeq        转账批次号
     * @return
     */
    private PsnTransPreRecordDetailQueryParams buildPreRecordInfoParams(String dateType, String transactionId, String batSeq) {
        PsnTransPreRecordDetailQueryParams detailQueryParams = new PsnTransPreRecordDetailQueryParams();
        detailQueryParams.setDateType(dateType);
        detailQueryParams.setTransactionId(transactionId);
        detailQueryParams.setBatSeq(batSeq);
        return detailQueryParams;
    }


    /**
     * 转换 预约管理 列表查询 数据到UI层model
     */
    private PreRecordViewModel copyPreRecord2UIModel(PsnTransPreRecordQueryResult recordQueryResult) {
        PreRecordViewModel preRecordViewModel = new PreRecordViewModel();
        preRecordViewModel.setRecordCount(recordQueryResult.getRecordCount());

        List<PreRecordViewModel.ListBean> viewListBeanList = new ArrayList<PreRecordViewModel.ListBean>();
        for (int i = 0; i < recordQueryResult.getList().size(); i++) {
            PreRecordViewModel.ListBean listBean = new PreRecordViewModel.ListBean();
            PsnTransPreRecordQueryResult.ListBean item = recordQueryResult.getList().get(i);
            listBean.setAmount(item.getAmount());
            listBean.setBatSeq(item.getBatSeq());
            listBean.setChannel(item.getChannel());
            listBean.setCurrency(item.getCurrency());
            listBean.setFirstSubmitDate(item.getFirstSubmitDate());
            listBean.setPayeeAccountName(item.getPayeeAccountName());
            listBean.setPayeeAccountNumber(item.getPayeeAccountNumber());
            listBean.setPayerAccountNumber(item.getPayerAccountNumber());
            listBean.setPaymentDate(item.getPaymentDate());
            listBean.setPeriodicalSeq(item.getPeriodicalSeq());
            listBean.setStatus(item.getStatus());
            listBean.setTransMode(item.getTransMode());
            listBean.setTransactionId(item.getTransactionId());

            viewListBeanList.add(listBean);
        }
        preRecordViewModel.setList(viewListBeanList);
        return preRecordViewModel;
    }


    /**
     * 转换 预约管理 详情查询 数据到UI层model
     */
    private PreRecordDetailInfoViewModel copyPreRecordInfo2UIModel(PsnTransPreRecordDetailQueryResult detailQueryResult) {
        PreRecordDetailInfoViewModel infoViewModel = new PreRecordDetailInfoViewModel();
        infoViewModel.setAllAmount(detailQueryResult.getAllAmount());
        infoViewModel.setAmount(detailQueryResult.getAmount());
        infoViewModel.setBatSeq(detailQueryResult.getBatSeq());
        infoViewModel.setChannel(detailQueryResult.getChannel());
        infoViewModel.setCurrency(detailQueryResult.getCurrency());
        infoViewModel.setExecuteAmount(detailQueryResult.getExecuteAmount());
        infoViewModel.setFirstSubmitDate(detailQueryResult.getFirstSubmitDate());
        infoViewModel.setFurInfo(detailQueryResult.getFurInfo());
        infoViewModel.setNotExecuteAmount(detailQueryResult.getNotExecuteAmount());
        infoViewModel.setPayeeAccountName(detailQueryResult.getPayeeAccountName());
        infoViewModel.setPayeeAccountNumber(detailQueryResult.getPayeeAccountNumber());
        infoViewModel.setPayeeBankName(detailQueryResult.getPayeeBankName());
        infoViewModel.setPayeeIbknum(detailQueryResult.getPayeeIbknum());
        infoViewModel.setPayerAccountNumber(detailQueryResult.getPayerAccountNumber());
        infoViewModel.setPayerIbknum(detailQueryResult.getPayerIbknum());
        infoViewModel.setPaymentDate(detailQueryResult.getPaymentDate());
        infoViewModel.setPeriodicalSeq(detailQueryResult.getPeriodicalSeq());
        infoViewModel.setStatus(detailQueryResult.getStatus());
        infoViewModel.setTransMode(detailQueryResult.getTransMode());
        infoViewModel.setTransactionId(detailQueryResult.getTransactionId());
        infoViewModel.setTransferType(detailQueryResult.getTransferType());

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
