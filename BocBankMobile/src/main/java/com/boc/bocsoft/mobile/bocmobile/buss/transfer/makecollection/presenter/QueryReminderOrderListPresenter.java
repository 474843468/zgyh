package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.presenter;

import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryReminderOrderList.PsnTransActQueryReminderOrderListParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryReminderOrderList.PsnTransActQueryReminderOrderListResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.QueryReminderOrderListViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui.QueryReminderOrderListContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter：催款指令查询
 * Created by zhx on 2016/7/5
 */
public class QueryReminderOrderListPresenter implements QueryReminderOrderListContract.Presenter {
    QueryReminderOrderListContract.View mView;
    private RxLifecycleManager mRxLifecycleManager;

    private TransferService transferService;

    public QueryReminderOrderListPresenter(QueryReminderOrderListContract.View view) {
        this.mView = view;
        mView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();

        transferService = new TransferService();
    }

    /**
     * 催款指令查询
     *
     * @param queryReminderOrderListViewModel
     */
    @Override
    public void queryReminderOrderList(QueryReminderOrderListViewModel queryReminderOrderListViewModel) {
        PsnTransActQueryReminderOrderListParams params = buildQueryReminderOrderListParams(queryReminderOrderListViewModel);
        transferService.psnTransActQueryReminderOrderList(params)
                .compose(mRxLifecycleManager.<PsnTransActQueryReminderOrderListResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnTransActQueryReminderOrderListResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransActQueryReminderOrderListResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.queryReminderOrderListFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnTransActQueryReminderOrderListResult onNext) {
                        mView.queryReminderOrderListSuccess(copyQueryReminderOrderList2UIModel(onNext));
                    }
                });
    }

    private QueryReminderOrderListViewModel copyQueryReminderOrderList2UIModel(PsnTransActQueryReminderOrderListResult onNext) {
        QueryReminderOrderListViewModel queryReminderOrderListViewModel = new QueryReminderOrderListViewModel();
        List<PsnTransActQueryReminderOrderListResult.ActiveReminderListBean>  resultList = onNext.getActiveReminderList();
        List<QueryReminderOrderListViewModel.ActiveReminderListBean> viewModelList = new ArrayList<QueryReminderOrderListViewModel.ActiveReminderListBean>();
        for(int i = 0; i < resultList.size(); i++) {
            QueryReminderOrderListViewModel.ActiveReminderListBean item = new QueryReminderOrderListViewModel.ActiveReminderListBean();
            PsnTransActQueryReminderOrderListResult.ActiveReminderListBean resultItem = resultList.get(i);

            item.setPayerName(resultItem.getPayerName());
            item.setCreateDate(resultItem.getCreateDate());
            item.setNotifyId(resultItem.getNotifyId());
            item.setRequestAmount(resultItem.getRequestAmount());
            item.setStatus(resultItem.getStatus());
            item.setTrfAmount(resultItem.getTrfAmount());

            viewModelList.add(item);
        }

        queryReminderOrderListViewModel.setRecordNum(onNext.getRecordNum());
        queryReminderOrderListViewModel.setActiveReminderList(viewModelList);
        return queryReminderOrderListViewModel;
    }

    /**
     * 封装请求参数：
     * 主动收款保存常用付款人
     *
     * @param queryReminderOrderListViewModel
     * @return PsnTransActQueryReminderOrderListParams
     */
    private PsnTransActQueryReminderOrderListParams buildQueryReminderOrderListParams(QueryReminderOrderListViewModel queryReminderOrderListViewModel) {
        PsnTransActQueryReminderOrderListParams params = new PsnTransActQueryReminderOrderListParams();
        params.setCurrentIndex(queryReminderOrderListViewModel.getCurrentIndex());
        params.setEndDate(queryReminderOrderListViewModel.getEndDate());
        params.setPageSize(queryReminderOrderListViewModel.getPageSize());
        params.setStartDate(queryReminderOrderListViewModel.getStartDate());
        return params;
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
