package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.presenter;

import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailListQuery.PsnVFGBailListQueryParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailListQuery.PsnVFGBailListQueryResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.service.LongShortForexService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.ui.SelectMarginAccountContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hty7062
 */

public class SelectMarginAccountPresenter extends RxPresenter implements SelectMarginAccountContract.Presenter {

    private SelectMarginAccountContract.View mSelectMarginAccountView;
    private LongShortForexService mLongShortForexService;

    public SelectMarginAccountPresenter(SelectMarginAccountContract.View view) {
        mSelectMarginAccountView = view;
        mLongShortForexService = new LongShortForexService();
    }

    @Override
    public void psnXpadBailListQuery(final VFGBailListQueryViewModel viewmodel) {
        ((BussFragment) mSelectMarginAccountView).showLoadingDialog("请稍候...");
        PsnVFGBailListQueryParams psnVFGBailListQueryParams = new PsnVFGBailListQueryParams();
        mLongShortForexService.psnVFGBailListQuery(psnVFGBailListQueryParams)
                .compose(SchedulersCompat.<List<PsnVFGBailListQueryResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnVFGBailListQueryResult>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        //                        ((BussFragment) mWealthHistoryListView).closeProgressDialog();

                        mSelectMarginAccountView.psnXpadBailListQueryFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(List<PsnVFGBailListQueryResult> psnVFGBailListQueryResult) {
                        ((BussFragment)mSelectMarginAccountView).closeProgressDialog();
                        BeanConvertor.toBean(psnVFGBailListQueryResult, viewmodel);
                        System.out.println("hjjhjj--->>>>" + psnVFGBailListQueryResult.size());
                        mSelectMarginAccountView.psnXpadBailListQuerySuccess(generateXpadBailListQueryViewModel(psnVFGBailListQueryResult,viewmodel));
                    }
                });
    }

    private VFGBailListQueryViewModel generateXpadBailListQueryViewModel(List<PsnVFGBailListQueryResult> results,VFGBailListQueryViewModel viewModel) {
        if (results != null) {
            List<VFGBailListQueryViewModel.BailItemEntity> viewList = new ArrayList<VFGBailListQueryViewModel.BailItemEntity>();
            for (PsnVFGBailListQueryResult queryresult : results) {
                VFGBailListQueryViewModel.BailItemEntity guarantyProductEntity = new VFGBailListQueryViewModel.BailItemEntity();
                BeanConvertor.toBean(queryresult, guarantyProductEntity);
                viewList.add(guarantyProductEntity);
            }
            viewModel.setEntityList(viewList);
        }
        return viewModel;
    }
}