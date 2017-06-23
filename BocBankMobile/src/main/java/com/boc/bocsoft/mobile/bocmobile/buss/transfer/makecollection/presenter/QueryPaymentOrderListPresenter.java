package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.presenter;

import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryPaymentOrderList.PsnTransActQueryPaymentOrderListParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryPaymentOrderList.PsnTransActQueryPaymentOrderListResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.QueryPaymentOrderListViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui.QueryPaymentOrderListContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter:付款指令查询
 * Created by zhx on 2016/7/6
 */
public class QueryPaymentOrderListPresenter implements QueryPaymentOrderListContract.Presenter {
    QueryPaymentOrderListContract.View mView;
    private RxLifecycleManager mRxLifecycleManager;

    private TransferService transferService;

    public QueryPaymentOrderListPresenter(QueryPaymentOrderListContract.View view) {
        this.mView = view;
        mView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();

        transferService = new TransferService();
    }

    @Override
    public void queryPaymentOrderList(QueryPaymentOrderListViewModel queryPaymentOrderListViewModel) {
        PsnTransActQueryPaymentOrderListParams params = buildQueryPaymentOrderListParams(queryPaymentOrderListViewModel);
        transferService.psnTransActQueryPaymentOrderList(params)
                .compose(mRxLifecycleManager.<PsnTransActQueryPaymentOrderListResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnTransActQueryPaymentOrderListResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransActQueryPaymentOrderListResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.queryPaymentOrderListFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnTransActQueryPaymentOrderListResult psnTransActSavePayerResult) {
                        mView.queryPaymentOrderListSuccess(copyResult2UIModel(psnTransActSavePayerResult));
                    }
                });
    }

    /**
     * 封装请求参数：
     * 付款指令查询
     *
     * @param queryPaymentOrderListViewModel
     * @return PsnTransActQueryReminderOrderListParams
     */
    private PsnTransActQueryPaymentOrderListParams buildQueryPaymentOrderListParams(QueryPaymentOrderListViewModel queryPaymentOrderListViewModel) {
        PsnTransActQueryPaymentOrderListParams psnTransActQueryPaymentOrderListParams = new PsnTransActQueryPaymentOrderListParams();
        psnTransActQueryPaymentOrderListParams.setCurrentIndex(queryPaymentOrderListViewModel.getCurrentIndex());
        psnTransActQueryPaymentOrderListParams.setStartDate(queryPaymentOrderListViewModel.getStartDate());
        psnTransActQueryPaymentOrderListParams.setEndDate(queryPaymentOrderListViewModel.getEndDate());
        psnTransActQueryPaymentOrderListParams.setPageSize(queryPaymentOrderListViewModel.getPageSize());
        return psnTransActQueryPaymentOrderListParams;
    }

    private QueryPaymentOrderListViewModel copyResult2UIModel(PsnTransActQueryPaymentOrderListResult result) {
        QueryPaymentOrderListViewModel queryPaymentOrderListViewModel = new QueryPaymentOrderListViewModel();
        List<PsnTransActQueryPaymentOrderListResult.ActiveReminderListBean> resultList = result.getActiveReminderList();
        List<QueryPaymentOrderListViewModel.ActiveReminderListBean> viewModelList = new ArrayList<QueryPaymentOrderListViewModel.ActiveReminderListBean>();

        for (int i = 0; i < resultList.size(); i++) {
            QueryPaymentOrderListViewModel.ActiveReminderListBean item = new QueryPaymentOrderListViewModel.ActiveReminderListBean();
            PsnTransActQueryPaymentOrderListResult.ActiveReminderListBean resultItem = resultList.get(i);

            item.setNotifyId(resultItem.getNotifyId());
            item.setStatus(resultItem.getStatus());
            item.setCreateDate(resultItem.getCreateDate());
            item.setPayeeName(resultItem.getPayeeName());
            item.setTrfAmount(resultItem.getTrfAmount());
            item.setRequestAmount(resultItem.getRequestAmount());

            viewModelList.add(item);
        }

        queryPaymentOrderListViewModel.setRecordNum(result.getRecordNum());
        queryPaymentOrderListViewModel.setActiveReminderList(viewModelList);
        return queryPaymentOrderListViewModel;
    }

    @Override
    public void subscribe() {
        //TODO onResume时需要做的工作
    }

    @Override
    public void unsubscribe() {
        //TODO 防止外界已经销毁，而后台线程的任务还在执行
        mRxLifecycleManager.onDestroy();
    }
}
