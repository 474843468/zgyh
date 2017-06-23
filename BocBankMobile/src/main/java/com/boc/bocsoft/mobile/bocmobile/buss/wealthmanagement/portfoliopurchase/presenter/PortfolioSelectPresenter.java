package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductDetailQuery.PsnXpadProductDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductDetailQuery.PsnXpadProductDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.model.PortfolioPurchaseModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.ui.PortfolioSelectContract;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthDetailsBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.presenter.WealthResponseResult;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;
import rx.functions.Func1;

/**
 * 作者：XieDu
 * 创建时间：2016/11/11 16:18
 * 描述：
 */
public class PortfolioSelectPresenter extends RxPresenter
        implements PortfolioSelectContract.Presenter {

    private PortfolioSelectContract.View mView;
    /**
     * 中银理财-service
     */
    private WealthManagementService mWealthManagementService;
    /**
     * 公共service
     */
    private GlobalService mGlobalService;

    public PortfolioSelectPresenter(PortfolioSelectContract.View view) {
        mView = view;
        mWealthManagementService = new WealthManagementService();
        mGlobalService = new GlobalService();
    }

    @Override
    public void queryProductDetail(PortfolioPurchaseModel model) {
        PsnXpadProductDetailQueryParams params = new PsnXpadProductDetailQueryParams();
        params.setProductCode("AMRJYL01");
        params.setProductKind("0");
        mWealthManagementService.psnXpadProductDetailQuery(params)
                                .map(new Func1<PsnXpadProductDetailQueryResult, WealthDetailsBean>() {
                                    @Override
                                    public WealthDetailsBean call(
                                            PsnXpadProductDetailQueryResult psnXpadProductDetailQueryResult) {
                                        return WealthResponseResult.copyResultToViewModel(
                                                psnXpadProductDetailQueryResult);
                                    }
                                })
                                .compose(this.<WealthDetailsBean>bindToLifecycle())
                                .compose(SchedulersCompat.<WealthDetailsBean>applyIoSchedulers())
                                .subscribe(new BIIBaseSubscriber<WealthDetailsBean>() {
                                    @Override
                                    public void handleException(
                                            BiiResultErrorException biiResultErrorException) {

                                    }

                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onNext(WealthDetailsBean wealthDetailsBean) {
                                        mView.onQueryProductDetailSuccess(wealthDetailsBean);
                                    }
                                });
    }
}
