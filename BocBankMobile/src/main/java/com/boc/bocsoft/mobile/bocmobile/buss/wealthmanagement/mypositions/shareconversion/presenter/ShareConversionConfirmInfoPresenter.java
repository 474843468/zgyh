package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductListQuery.PsnXpadProductListQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductListQuery.PsnXpadProductListQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadRecentAccountUpdate.PsnXpadRecentAccountUpdateResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadShareTransitionCommit.PsnXpadShareTransitionCommitParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadShareTransitionCommit.PsnXpadShareTransitionCommitResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadaccountquery.PsnXpadAccountQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.util.RedeemCodeModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.util.ShareConversionModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.model.PortfolioPurchaseModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.ModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.WealthConst;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zn on 2016/9/18.
 *
 * @description 中银理财-份额转换-确认信息界面-Presenter
 */
public class ShareConversionConfirmInfoPresenter extends RxPresenter
        implements ShareConversionContract.ShareConversionConfirmInfoParenter {


    /**
     * 中银理财-份额转换-申请界面 接口处理回调方法
     */
    private ShareConversionContract.ShareConversionConfirmInfoView mShareConversionConfirmInfoView;
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

    public ShareConversionConfirmInfoPresenter(ShareConversionContract.ShareConversionConfirmInfoView mShareConversionConfirmInfoView) {
        globalService = new GlobalService();
        mWealthManagementService = new WealthManagementService();
        this.mShareConversionConfirmInfoView = mShareConversionConfirmInfoView;
    }

    /**
     * 初始化
     */
    private void init() {

    }

    /**
     * token          防重机制，通过PSNGetTokenId接口获取
     * accountKey     帐号缓存标识
     * conversationId 会话id
     */
    @Override
    public void getPsnXpadShareTransitionCommit(
            final String mConversationId,
            final String accountKey) {
        this.mConversationId = mConversationId;
        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
        psnGetTokenIdParams.setConversationId(mConversationId);
        // 查询TokenID
        globalService.psnGetTokenId(psnGetTokenIdParams)
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnXpadShareTransitionCommitResult>>() {
                    @Override
                    public Observable<PsnXpadShareTransitionCommitResult> call(String tokenID) {
                        PsnXpadShareTransitionCommitParams mParams = new PsnXpadShareTransitionCommitParams();
                        mParams.setConversationId(mConversationId);
                        mParams.setToken(tokenID);
                        mParams.setAccountKey(accountKey);
                        return mWealthManagementService.PsnXpadShareTransitionCommit(mParams);
                    }
                }).compose(SchedulersCompat.<PsnXpadShareTransitionCommitResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadShareTransitionCommitResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mShareConversionConfirmInfoView.obtainPsnXpadShareTransitionCommitFail();
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnXpadShareTransitionCommitResult doPaymentResult) {
                        mShareConversionConfirmInfoView.obtainPsnXpadShareTransitionCommitResModel(
                                ShareConversionModelUtil.transverterPsnXpadShareTransitionCommitViewModel(doPaymentResult));
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
//                        return mWealthManagementService.psnXpadProductListQuery(params);
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
                        mShareConversionConfirmInfoView.obtainQueryProductListFail();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(List<WealthListBean> wealthListBeen) {
                        mShareConversionConfirmInfoView.obtainQueryProductListSuccess(wealthListBeen);
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
