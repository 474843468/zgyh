package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadApplyAgreementResult.PsnXpadApplyAgreementResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadApplyAgreementResult.PsnXpadApplyAgreementResultParam;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductListQuery.PsnXpadProductListQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryRiskMatch.PsnXpadQueryRiskMatchParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.XpadApplyAgreementResultViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui.ProtocolBalanceInvestConfirmContact;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.ModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.model.PurchaseModel;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.functions.Func1;

/**
 * 余额理财投资确认
 * Created by zhx on 2016/11/10
 */
public class ProtocolBalanceInvestConfirmPresenter extends RxPresenter implements ProtocolBalanceInvestConfirmContact.Presenter {

    private ProtocolBalanceInvestConfirmContact.View mView;
    private RxLifecycleManager mRxLifecycleManager;

    private GlobalService globalService;
    private WealthManagementService wealthManagementService;

    private String conversationId;

    public ProtocolBalanceInvestConfirmPresenter(ProtocolBalanceInvestConfirmContact.View view) {
        this.mView = view;
        mView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();

        globalService = new GlobalService();
        wealthManagementService = new WealthManagementService();
    }

    @Override
    public void applyAgreementResult(final XpadApplyAgreementResultViewModel viewModel) {
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversation) {
                        ProtocolBalanceInvestConfirmPresenter.this.conversationId = conversation;

                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        psnGetTokenIdParams.setConversationId(conversation);
                        return globalService.psnGetTokenId(psnGetTokenIdParams);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnXpadApplyAgreementResult>>() {
                    @Override
                    public Observable<PsnXpadApplyAgreementResult> call(String token) {
                        PsnXpadApplyAgreementResultParam params = new PsnXpadApplyAgreementResultParam();

                        BeanConvertor.toBean(viewModel, params);

                        params.setToken(token);
                        params.setConversationId(ProtocolBalanceInvestConfirmPresenter.this.conversationId);
                        return wealthManagementService.psnXpadApplyAgreementResult(params);
                    }
                })
                .compose(SchedulersCompat.<PsnXpadApplyAgreementResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadApplyAgreementResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.applyAgreementResultFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnXpadApplyAgreementResult psnTransManagePayeeModifyMobileResult) {
                        mView.applyAgreementResultSuccess(generateViewModel(viewModel, psnTransManagePayeeModifyMobileResult));
                    }
                });
    }

    @Override
    public void queryProductList(final String productCode, final String productRisk, final PurchaseModel purchaseModel) {
        wealthManagementService.psnXpadProductListQuery(ModelUtil.generateProductListQueryParams(purchaseModel.getPayAccountId(),purchaseModel.getCurCode(), conversationId))
                .compose(mRxLifecycleManager.<PsnXpadProductListQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnXpadProductListQueryResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnXpadProductListQueryResult>() {
                    @Override
                    public void onNext(PsnXpadProductListQueryResult result) {
                        mView.queryProductListSuccess(ModelUtil.generateWealthListBeans(productCode, productRisk, result));
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }

                    @Override
                    public void handleException(BiiResultErrorException error) {
                        mView.queryProductListFailed(null);
                    }
                });
    }

    // 构建请求参数
    private PsnXpadQueryRiskMatchParams buildApplyAgreementResultParams(XpadApplyAgreementResultViewModel viewModel) {
        PsnXpadQueryRiskMatchParams psnXpadQueryRiskMatchParams = new PsnXpadQueryRiskMatchParams();
        BeanConvertor.toBean(viewModel, psnXpadQueryRiskMatchParams);
        return psnXpadQueryRiskMatchParams;
    }

    // 生成viewModel
    private XpadApplyAgreementResultViewModel generateViewModel(XpadApplyAgreementResultViewModel viewModel, PsnXpadApplyAgreementResult result) {
        BeanConvertor.toBean(result, viewModel);
        return viewModel;
    }
}