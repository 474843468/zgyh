package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.presenter;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.CombinBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.SecurityViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.ui.InstallmentDetailContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by yangle on 2016/11/24.
 */
public class InstallmentDetailPresenter extends RxPresenter implements InstallmentDetailContract.Presenter {

    private static final String TAG = "InstallmentDetailPresenter";

    public static final String SERVICE_ID_CRCD_PAY_ADVANCE = "PB057C1";
    private final InstallmentDetailContract.View mView;
    private final GlobalService mGlobalService;

    public InstallmentDetailPresenter(InstallmentDetailContract.View view) {
        this.mView = view;
        mGlobalService = new GlobalService();
    }

    @Override
    public void getSecurityFactor() {
        mView.showLoading();
        mGlobalService.psnCreatConversation(new PSNCreatConversationParams())
                .flatMap(new Func1<String, Observable<PsnGetSecurityFactorResult>>() {
                    @Override
                    public Observable<PsnGetSecurityFactorResult> call(String conversationId) {
                        mView.getViewModel().setConversationId(conversationId);
                        return mGlobalService.psnGetSecurityFactor(createSecurityFactorParams(conversationId));
                    }
                })
                .map(new Func1<PsnGetSecurityFactorResult, SecurityViewModel>() {
                    @Override
                    public SecurityViewModel call(PsnGetSecurityFactorResult result) {
                        return toSecurityViewModel(result);
                    }
                })
                .compose(this.<SecurityViewModel>bindToLifecycle())
                .compose(SchedulersCompat.<SecurityViewModel>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<SecurityViewModel>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.closeLoading();
                    }

                    @Override
                    public void onCompleted() {
                        mView.closeLoading();
                    }

                    @Override
                    public void onNext(SecurityViewModel securityFactorModel) {
                        mView.getSecurityFactorSuccess(securityFactorModel);
                    }
                });
    }

    private PsnGetSecurityFactorParams createSecurityFactorParams(String conversationId) {
        PsnGetSecurityFactorParams params = new PsnGetSecurityFactorParams();
        params.setConversationId(conversationId);
        params.setServiceId(SERVICE_ID_CRCD_PAY_ADVANCE);
        return params;
    }

    private SecurityViewModel toSecurityViewModel(PsnGetSecurityFactorResult result) {
        SecurityViewModel viewModel = new SecurityViewModel();
        if (result.get_defaultCombin() != null) {
            viewModel.set_defaultCombin(BeanConvertor.toBean(result.get_defaultCombin(), new CombinBean()));// 设置默认安全因子
        }
        List<CombinBean> list = new ArrayList<>();
        for (CombinListBean item : result.get_combinList()) {
            list.add(BeanConvertor.toBean(item, new CombinBean()));
        }
        viewModel.set_combinList(list);
        return viewModel;
    }
}
