package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHoldProductRedeemVerify.PsnXpadHoldProductRedeemVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHoldProductRedeemVerify.PsnXpadHoldProductRedeemVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadproductbalancequery.PsnXpadProductBalanceQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadproductdetailquery.PsnXpadProductDetailQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.util.RedeemCodeModelUtil;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.functions.Func1;


/**
 * @author yx
 * @description 中银理财-我的持仓-赎回-Presenter
 * @date 2016-9-7 13:52:32
 */
public class RedeemPresenter extends RxPresenter implements RedeemContract.RedeemPresenter {

    /**
     * 中银理财-我的持仓-赎回-申请界面 接口处理回调方法
     */
    private RedeemContract.RedeemView mRedeemView;

    /**
     * 中银理财-service
     */
    private WealthManagementService mWealthManagementService;
    /**
     * 公共service
     */
    private GlobalService globalService;

    /**
     * 构造方法 做初始化
     *
     * @param mRelationView
     */
    public RedeemPresenter(RedeemContract.RedeemView mRelationView) {
        this.mRedeemView = mRelationView;
        globalService = new GlobalService();
        mWealthManagementService = new WealthManagementService();
    }

    /**
     * 初始化
     */
    private void init() {

    }

    /**
     * I42-4.40 040产品详情查询 PsnXpadProductDetailQuery
     *
     * @param productCode  产品代码
     * @param ibknum{省行联行号 String	O
     *                     返回项需展示(剩余额度、工作时间、挂单时间)，此项必输
     *                     根据PsnXpadAccountQuery接口的返回项进行上送}
     * @param productKind  产品性质
     */
    @Override
    public void getPsnXpadProductDetailQuery(String productCode, String ibknum, String productKind) {

    }

    /**
     * 获取会话
     */
    @Override
    public void getPSNCreatConversation() {
        PSNCreatConversationParams mParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(mParams)
                .compose(this.<String>bindToLifecycle())
                .compose(SchedulersCompat.<String>applyComputationSchedulers())
                .subscribe(new BIIBaseSubscriber<String>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(String s) {
                        mRedeemView.obtainConversationSuccess(s);
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mRedeemView.obtainConversationFail();
                    }
                });
    }

    /**
     * I42-4.33 033持有产品赎回预交易 PsnXpadHoldProductRedeemVerify
     *
     * @param mConversationId 会话id
     * @param mBalanceQueryResModel 客户持仓信息
     * @param mDetailQueryResModel 产品详情
     * @param isPartRedeemQuantity  是否允许部分赎回
     * @param mSharesRedemption
     * @param isRedeemDate
     * @param mRedeemDate
     * @param mTranSeq 业绩基准-上送交易流水号
     */
    @Override
    public void getPsnXpadHoldProductRedeemVerify(final String mConversationId, final PsnXpadProductBalanceQueryResModel mBalanceQueryResModel, final PsnXpadProductDetailQueryResModel mDetailQueryResModel,
                                                  final boolean isPartRedeemQuantity, final String mSharesRedemption, final boolean isRedeemDate, final String mRedeemDate,final  String mTranSeq) {
        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
        psnGetTokenIdParams.setConversationId(mConversationId);
        // 查询TokenID
        globalService.psnGetTokenId(psnGetTokenIdParams)
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnXpadHoldProductRedeemVerifyResult>>() {
                    @Override
                    public Observable<PsnXpadHoldProductRedeemVerifyResult> call(String tokenID) {
                        PsnXpadHoldProductRedeemVerifyParams mParams = RedeemCodeModelUtil.buildPsnXpadHoldProductRedeemVerifyBiiParams(mBalanceQueryResModel,mDetailQueryResModel, isPartRedeemQuantity, mSharesRedemption, isRedeemDate, mRedeemDate,mTranSeq);
                        mParams.setConversationId(mConversationId);
                        mParams.setToken(tokenID);
                        mParams.setAccountKey(mBalanceQueryResModel.getBancAccountKey());
                        return mWealthManagementService.psnXpadHoldProductRedeemVerify(mParams);
                    }
                }).compose(SchedulersCompat.<PsnXpadHoldProductRedeemVerifyResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadHoldProductRedeemVerifyResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mRedeemView.obtainPsnXpadHoldProductRedeemVerifyFail();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnXpadHoldProductRedeemVerifyResult doPaymentResult) {
                        mRedeemView.obtainPsnXpadHoldProductRedeemVerifySuccess(RedeemCodeModelUtil.transverterPsnXpadHoldProductRedeemVerifyViewModel(doPaymentResult));
                    }
                });
    }
}