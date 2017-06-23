package com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdrawalquery.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileWithdrawalDetailsQuery.PsnMobileWithdrawalDetailsQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileWithdrawalQuery.PsnMobileWithdrawalQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileWithdrawalQuery.PsnMobileWithdrawalQueryResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdrawalquery.model.WithdrawalQueryDetailInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdrawalquery.model.WithdrawalQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdrawalquery.ui.WithdrawalQueryContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * 取款查询BII通信逻辑处理
 * Created by wangf on 2016/6/8.
 */
public class WithdrawalQueryPresenter implements WithdrawalQueryContract.Presenter {

    private WithdrawalQueryContract.View mWithdrawalQueryView;
    private RxLifecycleManager mRxLifecycleManager;

    /**
     * 会话
     */
    private String conversation;
    /**
     * 公共service
     */
    private GlobalService globalService;
    /**
     * 取款查询service
     */
    private TransferService transferService;


    public WithdrawalQueryPresenter(WithdrawalQueryContract.View view) {
        mWithdrawalQueryView = view;
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        transferService = new TransferService();
    }

    /**
     * 取款查询列表
     */
    @Override
    public void queryWithdrawalQueryList(final WithdrawalQueryViewModel withdrawalQueryViewModel) {
        //查询之前请求会话
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnMobileWithdrawalQueryResult>>() {
                    @Override
                    public Observable<PsnMobileWithdrawalQueryResult> call(String conversation) {
                        PsnMobileWithdrawalQueryParams detailParams = buildWithdrawalQueryParams(withdrawalQueryViewModel);
                        detailParams.setConversationId(conversation);
                        return transferService.psnMobileWithdrawalQuery(detailParams);
                    }
                })
                .compose(SchedulersCompat.<PsnMobileWithdrawalQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnMobileWithdrawalQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mWithdrawalQueryView.queryWithdrawalQueryListFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnMobileWithdrawalQueryResult psnMobileWithdrawalQueryResult) {
                        mWithdrawalQueryView.queryWithdrawalQueryListSuccess(copyWithdrawalQuery2UIModel(psnMobileWithdrawalQueryResult));
                    }
                });
    }

    /**
     * 取款查询 -- 详情查询
     *
     * @param detailInfoViewModel
     */
    @Override
    public void queryWithdrawalDetailInfo(WithdrawalQueryDetailInfoViewModel detailInfoViewModel) {
        PsnMobileWithdrawalDetailsQueryParams params = buildWithdrawalDetailsQueryParams(detailInfoViewModel);

        transferService.psnMobileWithdrawalDetailsQuery(params)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<String>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mWithdrawalQueryView.queryWithdrawalDetailInfoFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(String quertResult) {
                        mWithdrawalQueryView.queryWithdrawalDetailInfoSuccess(quertResult);
//                        mWithdrawalQueryView.queryWithdrawalDetailInfoSuccess(copyWithdrawalQueryDetailInfo2UIModel(result));
                    }
                });
    }


    /**
     * 封装取款查询请求参数
     *
     * @param withdrawalQueryViewModel
     * @return
     */
    private PsnMobileWithdrawalQueryParams buildWithdrawalQueryParams(WithdrawalQueryViewModel withdrawalQueryViewModel) {
        PsnMobileWithdrawalQueryParams withdrawalQueryParams = new PsnMobileWithdrawalQueryParams();
        withdrawalQueryParams.setStartDate(withdrawalQueryViewModel.getStartDate());
        withdrawalQueryParams.setEndDate(withdrawalQueryViewModel.getEndDate());
        withdrawalQueryParams.setPageSize(withdrawalQueryViewModel.getPageSize());
        withdrawalQueryParams.setCurrentIndex(withdrawalQueryViewModel.getCurrentIndex());
        return withdrawalQueryParams;
    }

    /**
     * 封装取款查询 详情 请求参数
     *
     * @param viewModel
     * @return
     */
    private PsnMobileWithdrawalDetailsQueryParams buildWithdrawalDetailsQueryParams(WithdrawalQueryDetailInfoViewModel viewModel) {
        PsnMobileWithdrawalDetailsQueryParams queryParams = new PsnMobileWithdrawalDetailsQueryParams();
        queryParams.setTransactionId(viewModel.getTransactionId());//网银交易序号
        queryParams.setCurrencyCode(viewModel.getCurrencyCode());//交易币种
        queryParams.setPayeeMobile(viewModel.getPayeeMobile());//收款人手机号
        queryParams.setPayeeName(viewModel.getPayeeName());//收款人姓名
        queryParams.setReceiptAmount(viewModel.getReceiptAmount());//交易金额

        return queryParams;
    }


    /**
     * 转换取款查询数据到UI层model
     */
    private WithdrawalQueryViewModel copyWithdrawalQuery2UIModel(PsnMobileWithdrawalQueryResult psnMobileWithdrawalQueryResult) {
        WithdrawalQueryViewModel withdrawalQueryViewModel = new WithdrawalQueryViewModel();
        withdrawalQueryViewModel.setRecordNumber(psnMobileWithdrawalQueryResult.getRecordNumber());
        withdrawalQueryViewModel.setNickName(psnMobileWithdrawalQueryResult.getNickName());
        withdrawalQueryViewModel.setAgentAcctNumber(psnMobileWithdrawalQueryResult.getAgentAcctNumber());

        List<WithdrawalQueryViewModel.ListBean> viewListBeanList = new ArrayList<WithdrawalQueryViewModel.ListBean>();
        for (int i = 0; i < psnMobileWithdrawalQueryResult.getList().size(); i++) {
            WithdrawalQueryViewModel.ListBean listBean = new WithdrawalQueryViewModel.ListBean();
            PsnMobileWithdrawalQueryResult.ListBean item = psnMobileWithdrawalQueryResult.getList().get(i);
            listBean.setAgentName(item.getAgentName());
            listBean.setAgentNum(item.getAgentNum());
            listBean.setCashRemit(item.getCashRemit());
            listBean.setChannel(item.getChannel());
            listBean.setCurrencyCode(item.getCurrencyCode());
            listBean.setPayeeMobile(item.getPayeeMobile());
            listBean.setPayeeName(item.getPayeeName());
            listBean.setRemitAmount(item.getRemitAmount());
            listBean.setRemitNo(item.getRemitNo());
            listBean.setRemitStatus(item.getRemitStatus());
            listBean.setTranDate(item.getTranDate());
            listBean.setTransactionId(item.getTransactionId());
            listBean.setRemark(item.getRemark());
            listBean.setBranchId(item.getBranchId());
            listBean.setFromActNumber(item.getFromActNumber());

            viewListBeanList.add(listBean);
        }
        withdrawalQueryViewModel.setList(viewListBeanList);
        return withdrawalQueryViewModel;
    }


//    /**
//     * /**
//     * 转换取款查询详情数据到UI层model
//     */
//    private WithdrawalQueryDetailInfoViewModel copyWithdrawalQueryDetailInfo2UIModel(String queryResult) {
//        WithdrawalQueryDetailInfoViewModel infoViewModel = new WithdrawalQueryDetailInfoViewModel();
//        infoViewModel.setResult(queryResult);
////        infoViewModel.setReceiptAmount(queryResult.getRemitAmount());//取款金额
////        infoViewModel.setReceiptAmount(queryResult.getRemitAmount());//代理点收款账户
////        infoViewModel.setRemitNo(queryResult.getRemitNo());//汇款编号
////        infoViewModel.setPayeeName(queryResult.getPayeeName());//收款人姓名
////        infoViewModel.setPayeeMobile(queryResult.getPayeeMobile());//收款人手机号
////        infoViewModel.setCurrencyCode(queryResult.getCurrencyCode());//取款币种
////        infoViewModel.setReceiptDate(queryResult.getReceiptDate());//取款日期
////        infoViewModel.setRemitStatus(queryResult.getRemitStatus());//取款状态
////        infoViewModel.setAgentName(queryResult.getAgentName());//代理点名称
////        infoViewModel.setAgentNum(queryResult.getAgentNum());//代理点编号
////        infoViewModel.setChannel(queryResult.getChannel());//交易渠道
//
//        return infoViewModel;
//    }


    @Override
    public void subscribe() {
        mRxLifecycleManager.onStart();
    }

    @Override
    public void unsubscribe() {
        mRxLifecycleManager.onDestroy();
    }


}
