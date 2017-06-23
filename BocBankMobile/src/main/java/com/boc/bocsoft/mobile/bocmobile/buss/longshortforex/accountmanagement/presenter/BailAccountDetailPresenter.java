package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailAccountInfoListQuery.PsnVFGBailAccountInfoListQueryParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailAccountInfoListQuery.PsnVFGBailAccountInfoListQueryResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailDetailQuery.PsnVFGBailDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailDetailQuery.PsnVFGBailDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.service.LongShortForexService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailAccountInfoListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailDetailQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui.BailAccountDetailContact;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter：双向宝-账户管理-保证金账户详情
 * Created by zhx on 2016/11/23
 */
public class BailAccountDetailPresenter extends RxPresenter implements BailAccountDetailContact.Presenter {
    private BailAccountDetailContact.View mView;
    private RxLifecycleManager mRxLifecycleManager;

    private GlobalService globalService;
    private LongShortForexService longShortForexService;

    public BailAccountDetailPresenter(BailAccountDetailContact.View view) {
        this.mView = view;
        mView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();

        globalService = new GlobalService();
        longShortForexService = new LongShortForexService();
    }

    @Override
    public void vFGBailDetailQuery(final VFGBailDetailQueryViewModel viewModel) {
        PsnVFGBailDetailQueryParams params = new PsnVFGBailDetailQueryParams();
        params.setSettleCurrency(viewModel.getSettleCurrency());
        params.setAccountNumber(viewModel.getAccountNumber());
        longShortForexService.psnVFGBailDetailQuery(params)
                .compose(SchedulersCompat.<PsnVFGBailDetailQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnVFGBailDetailQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.vFGBailDetailQueryFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnVFGBailDetailQueryResult result) {
                        BeanConvertor.toBean(result, viewModel);
                        mView.vFGBailDetailQuerySuccess(viewModel);
                    }
                });
    }

    // 双向宝保证金账户基本信息多笔查询(I50 028)
    @Override
    public void vFGBailAccountInfoListQuery(final VFGBailAccountInfoListQueryViewModel viewModel) {
        PsnVFGBailAccountInfoListQueryParams params = new PsnVFGBailAccountInfoListQueryParams();

        longShortForexService.psnVFGBailAccountInfoListQuery(params)
                .compose(SchedulersCompat.<PsnVFGBailAccountInfoListQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnVFGBailAccountInfoListQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.vFGBailAccountInfoListQueryFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnVFGBailAccountInfoListQueryResult psnVFGGetBindAccountResult) {
                        mView.vFGBailAccountInfoListQuerySuccess(buildVfgBailAccountInfoListQueryViewModel(psnVFGGetBindAccountResult, viewModel));
                    }
                });
    }

    // 构建ViewModel(双向宝保证金账户基本信息多笔查询)
    private VFGBailAccountInfoListQueryViewModel buildVfgBailAccountInfoListQueryViewModel(PsnVFGBailAccountInfoListQueryResult result, VFGBailAccountInfoListQueryViewModel viewModel) {
        List<VFGBailAccountInfoListQueryViewModel.VFGBailAccountInfo> list = new ArrayList<VFGBailAccountInfoListQueryViewModel.VFGBailAccountInfo>();

        for (PsnVFGBailAccountInfoListQueryResult.VFGBailAccountInfo accountInfo : result.getList()) {
            VFGBailAccountInfoListQueryViewModel.VFGBailAccountInfo vfgBailAccountInfo = new VFGBailAccountInfoListQueryViewModel.VFGBailAccountInfo();
            BeanConvertor.toBean(accountInfo, vfgBailAccountInfo);

            // 对结算货币赋值
            VFGBailAccountInfoListQueryViewModel.VFGBailAccountInfo.SettleCurrencyEntity currencyEntity = new VFGBailAccountInfoListQueryViewModel.VFGBailAccountInfo.SettleCurrencyEntity();
            BeanConvertor.toBean(accountInfo.getSettleCurrency(), currencyEntity);

            vfgBailAccountInfo.setSettleCurrency2(currencyEntity);

            list.add(vfgBailAccountInfo);
        }

        viewModel.setList(list);

        return viewModel;
    }
}
