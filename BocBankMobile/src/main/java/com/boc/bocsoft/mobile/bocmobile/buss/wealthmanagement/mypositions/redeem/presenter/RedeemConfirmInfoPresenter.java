package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdResponse;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.login.model.PSNGetTokenIdLoginPre.PSNGetTokenIdLoginPreResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHoldProductAndRedeem.PsnXpadHoldProductAndRedeemParms;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHoldProductAndRedeem.PsnXpadHoldProductAndRedeemResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductListQuery.PsnXpadProductListQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductListQuery.PsnXpadProductListQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadRecentAccountUpdate.PsnXpadRecentAccountUpdateResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadaccountquery.PsnXpadAccountQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.util.RedeemCodeModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.model.PortfolioPurchaseModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.ModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.model.PurchaseModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.WealthConst;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;


/**
 * @author yx
 * @description 中银理财-我的持仓-赎回-确认信息界面-Presenter
 * @date 2016-9-16 09:35:56
 */
public class RedeemConfirmInfoPresenter extends RxPresenter implements RedeemContract.RedeemConfirmInfoPresenter {

    /**
     * 中银理财-我的持仓-赎回-申请界面 接口处理回调方法
     */
    private RedeemContract.RedeemConfirmInfoView mRedeemConfirmInfoView;
    /**
     * 中银理财-service
     */
    private WealthManagementService mWealthManagementService;
    /**
     * 公共service
     */
    private GlobalService globalService;
    /**
     * 会话id
     */
    private String mConversationId;

    /**
     * 构造方法 做初始化
     *
     * @param mRedeemConfirmInfoView
     */
    public RedeemConfirmInfoPresenter(RedeemContract.RedeemConfirmInfoView mRedeemConfirmInfoView) {
        this.mRedeemConfirmInfoView = mRedeemConfirmInfoView;
        globalService = new GlobalService();
        mWealthManagementService = new WealthManagementService();
    }

    /**
     * 初始化
     */
    private void init() {

    }

    /**
     * I42-4.13 013持有产品赎回 PsnXpadHoldProductAndRedeem
     *
     * @param token          防重标识	token
     * @param dealCode       指令交易后台交易ID			{指令交易上送字段不可为空}
     * @param conversationId 会话id
     */
    @Override
    public void getPsnXpadHoldProductAndRedeem(final String token, final String dealCode, final String conversationId) {
        this.mConversationId = conversationId;
        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
        psnGetTokenIdParams.setConversationId(conversationId);
        // 查询TokenID
        globalService.psnGetTokenId(psnGetTokenIdParams)
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnXpadHoldProductAndRedeemResult>>() {
                    @Override
                    public Observable<PsnXpadHoldProductAndRedeemResult> call(String tokenID) {
                        PsnXpadHoldProductAndRedeemParms mParams = new PsnXpadHoldProductAndRedeemParms();
                        mParams.setConversationId(conversationId);
                        mParams.setToken(tokenID);
                        mParams.setDealCode(dealCode + "");
                        return mWealthManagementService.psnXpadHoldProductAndRedeem(mParams);
                    }
                }).compose(SchedulersCompat.<PsnXpadHoldProductAndRedeemResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadHoldProductAndRedeemResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mRedeemConfirmInfoView.obtainPsnXpadHoldProductAndRedeemFail();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnXpadHoldProductAndRedeemResult doPaymentResult) {
                        mRedeemConfirmInfoView.obtainPsnXpadHoldProductAndRedeemSuccess(RedeemCodeModelUtil.transverterPsnXpadHoldProductAndRedeemResModel(doPaymentResult));
                    }
                });
    }

    @Override
    public void getQueryProductList(final PortfolioPurchaseModel mPortfolioPurchaseModel) {
        Observable.just(mConversationId)
                .flatMap(new Func1<String, Observable<PsnXpadProductListQueryResult>>() {
                    @Override
                    public Observable<PsnXpadProductListQueryResult> call(String conversationId) {
//                        PsnXpadProductListQueryParams params =
//                                new PsnXpadProductListQueryParams();
//                        params.setConversationId(conversationId);
//                        params.setProductRiskType("0");
//                        params.setProductKind(mPortfolioPurchaseModel.getProductKind());
//                        params.setProductCurCode(mPortfolioPurchaseModel.getCurCode());
//                        params.setPageSize(String.valueOf(ApplicationConst.WEALTH_PAGE_SIZE));
//                        params.setIsLockPeriod(WealthConst.YES_1);// 是否支持业绩基准产品查询（支持）
//                        params.setAccountId(mPortfolioPurchaseModel.getAccountId());// 账户ID
//                        params.setXpadStatus("1");
//                        params.setIssueType("0");// 产品类型
//                        params.setProdTimeLimit("0");// 产品期限（月）
//                        params.setDayTerm("0");// 产品期限（天）
//                        params.setProRisk("0");// 风险等级
//                        params.setSortFlag("0");// 排序方式
//                        params.setSortType("0");// 排序条件
//                        params.setCurrentIndex("0");
//                        params.set_refresh("true");
                        return mWealthManagementService.psnXpadProductListQuery(ModelUtil.generateProductListQueryParams(mPortfolioPurchaseModel.getAccountId(),mPortfolioPurchaseModel.getCurCode(),conversationId));
                    }
                })
                .map(new Func1<PsnXpadProductListQueryResult, List<WealthListBean>>() {
                    @Override
                    public List<WealthListBean> call(
                            PsnXpadProductListQueryResult psnXpadProductListQueryResult) {
                        return ModelUtil.generateWealthListBeans(mPortfolioPurchaseModel.getProdCode(), mPortfolioPurchaseModel.getProductRisk(), psnXpadProductListQueryResult);
                    }
                })
                .compose(this.<List<WealthListBean>>bindToLifecycle())
                .compose(SchedulersCompat.<List<WealthListBean>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<WealthListBean>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mRedeemConfirmInfoView.obtainQueryProductListFail();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(List<WealthListBean> wealthListBeen) {
                        mRedeemConfirmInfoView.obtainQueryProductListSuccess(wealthListBeen);
                    }
                });
    }

    /**
     * 更新最近操作账户状态-交易记录查询使用
     *
     * @param xpadAccountEntity
     * @date 2016-12-05 13:42:35
     */

    public void updateRecentAccount(final PsnXpadAccountQueryResModel.XPadAccountEntity xpadAccountEntity) {
        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
        psnGetTokenIdParams.setConversationId(mConversationId);
        // 查询TokenID
        globalService.psnGetTokenId(psnGetTokenIdParams)
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnXpadRecentAccountUpdateResult>>() {
                    @Override
                    public Observable<PsnXpadRecentAccountUpdateResult> call(String tokenID) {

                        return mWealthManagementService.psnXpadRecentAccountUpdate(RedeemCodeModelUtil.generateUpdateRecentAccountParams(mConversationId, tokenID, xpadAccountEntity));
                    }
                }).compose(SchedulersCompat.<PsnXpadRecentAccountUpdateResult>applyIoSchedulers());
    }

}