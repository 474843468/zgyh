package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.presenter;

import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryPayerList.PsnTransActQueryPayerListParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryPayerList.PsnTransActQueryPayerListResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.QueryPayerListViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui.QueryPayerListContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter:查询付款人列表
 * Created by zhx on 2016/6/30
 */
public class QueryPayerListPresenter implements QueryPayerListContract.Presenter {
    private QueryPayerListContract.View mView;
    private RxLifecycleManager mRxLifecycleManager;

    private TransferService transferService;

    public QueryPayerListPresenter(QueryPayerListContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();

        transferService = new TransferService();
    }

    /**
     * 查询付款人列表
     *
     * @param queryPayerListViewModel
     */
    @Override
    public void queryPayerList(final QueryPayerListViewModel queryPayerListViewModel) {
        ((BussFragment) mView).showLoadingDialog("请稍候...");

        PsnTransActQueryPayerListParams params = new PsnTransActQueryPayerListParams();
        transferService.psnTransActQueryPayerList(params)
                .compose(mRxLifecycleManager.<List<PsnTransActQueryPayerListResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnTransActQueryPayerListResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnTransActQueryPayerListResult>>() {

                    @Override
                    public void onCompleted() {
                        ((BussFragment) mView).closeProgressDialog();
                    }

                    @Override
                    public void onNext(List<PsnTransActQueryPayerListResult> onNext) {
                        mView.queryPayerListSuccess(copyQueryPayerList2UIModel(onNext, queryPayerListViewModel));
                        ((BussFragment) mView).closeProgressDialog();
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.queryPayerListFail(biiResultErrorException);
                        ((BussFragment) mView).closeProgressDialog();
                    }
                });
    }

    /**
     * 查询付款人列表
     * 转换到UI层model
     */
    private QueryPayerListViewModel copyQueryPayerList2UIModel(List<PsnTransActQueryPayerListResult> onNext, QueryPayerListViewModel queryPayerListViewModel) {
        if (onNext == null) {
            return queryPayerListViewModel;
        }

        ArrayList<QueryPayerListViewModel.ResultBean> viewResultBeanList = new ArrayList<QueryPayerListViewModel.ResultBean>();

        for (int i = 0; i < onNext.size(); i++) {
            QueryPayerListViewModel.ResultBean item = new QueryPayerListViewModel.ResultBean();
            PsnTransActQueryPayerListResult resultItem = onNext.get(i);

            item.setIdentifyType(resultItem.getIdentifyType());
            item.setPayerCustomerId(resultItem.getPayerCustomerId());
            item.setPayerId(resultItem.getPayerId());
            item.setPayerMobile(resultItem.getPayerMobile());
            item.setPayerName(resultItem.getPayerName());

            viewResultBeanList.add(item);
        }

        queryPayerListViewModel.setResult(viewResultBeanList);
        return queryPayerListViewModel;
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
