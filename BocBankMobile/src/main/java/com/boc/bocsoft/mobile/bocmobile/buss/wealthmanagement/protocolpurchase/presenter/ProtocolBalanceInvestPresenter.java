package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryRiskMatch.PsnXpadQueryRiskMatchParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryRiskMatch.PsnXpadQueryRiskMatchResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.XpadQueryRiskMatchViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui.ProtocolBalanceInvestContact;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

/**
 * 余额理财投资
 * Created by zhx on 2016/11/10
 */
public class ProtocolBalanceInvestPresenter extends RxPresenter implements ProtocolBalanceInvestContact.Presenter {
    private ProtocolBalanceInvestContact.View mView;
    private RxLifecycleManager mRxLifecycleManager;

    private GlobalService globalService;
    private WealthManagementService wealthManagementService;

    public ProtocolBalanceInvestPresenter(ProtocolBalanceInvestContact.View view) {
        this.mView = view;
        mView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();

        globalService = new GlobalService();
        wealthManagementService = new WealthManagementService();
    }

    @Override
    public void queryRiskMatch(final XpadQueryRiskMatchViewModel viewModel) {
        ((BussFragment) mView).showLoadingDialog("请稍候...");
        PsnXpadQueryRiskMatchParams psnXpadQueryRiskMatchParams = buildQueryRiskMatchParams(viewModel);

        wealthManagementService.psnXpadQueryRiskMatch(psnXpadQueryRiskMatchParams)
                .compose(SchedulersCompat.<PsnXpadQueryRiskMatchResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadQueryRiskMatchResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) mView).closeProgressDialog();
                        mView.queryRiskMatchFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnXpadQueryRiskMatchResult psnXpadQueryRiskMatchResult) {
                        ((BussFragment) mView).closeProgressDialog();

                        mView.queryRiskMatchSuccess(generateViewModel(viewModel, psnXpadQueryRiskMatchResult));
                    }
                });
    }

    // 构建请求参数
    private PsnXpadQueryRiskMatchParams buildQueryRiskMatchParams(XpadQueryRiskMatchViewModel viewModel) {
        PsnXpadQueryRiskMatchParams psnXpadQueryRiskMatchParams = new PsnXpadQueryRiskMatchParams();
        BeanConvertor.toBean(viewModel, psnXpadQueryRiskMatchParams);
        return psnXpadQueryRiskMatchParams;
    }

    // 生成viewModel
    private XpadQueryRiskMatchViewModel generateViewModel(XpadQueryRiskMatchViewModel viewModel, PsnXpadQueryRiskMatchResult result) {
        BeanConvertor.toBean(result, viewModel);
        return viewModel;
    }
}