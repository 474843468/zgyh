package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.presenter;

import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadproductbalancequery.PsnXpadProductBalanceQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadholdproductandredeem.PsnXpadHoldProductAndRedeemResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadholdproductredeemverify.PsnXpadHoldProductRedeemVerifyResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadproductdetailquery.PsnXpadProductDetailQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.model.PortfolioPurchaseModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * @author yx
 * @description 中银理财-我的持仓-赎回-Contract
 * @date 2016-9-7 13:52:32
 */
public class RedeemContract {
    /**
     * @author yx
     * @description 申请界面 接口请求响应 回调
     * @date 2016-9-7 16:33:58
     */
    public interface RedeemView extends BaseView<BasePresenter> {
        //    ====================产品详情查询============

        /**
         * I42-4.40 040产品详情查询PsnXpadProductDetailQuery，成功调用
         *
         * @param mPsnXpadProductDetailQueryResModel 响应model
         */
        void obtainPsnXpadProductDetailQuerySuccess(PsnXpadProductDetailQueryResModel mPsnXpadProductDetailQueryResModel);

        /**
         * @description I42-4.40 040产品详情查询PsnXpadProductDetailQuery，失败调用
         */
        void obtainPsnXpadProductDetailQueryFail();

        //        ======================获取会话ID=======================

        /**
         * @description 获取会话ID，成功调用
         */
        void obtainConversationSuccess(String conversationId);

        /**
         * @description 获取会话ID，失败调用
         */
        void obtainConversationFail();
        //        =====================I42-4.33 033持有产品赎回预交易 PsnXpadHoldProductRedeemVerify=======================

        /**
         * 持有产品赎回预交易，成功调用
         *
         * @param mPsnXpadHoldProductRedeemVerifyResModel 持有产品赎回预交易model
         */
        void obtainPsnXpadHoldProductRedeemVerifySuccess(PsnXpadHoldProductRedeemVerifyResModel mPsnXpadHoldProductRedeemVerifyResModel);

        /**
         * @description 持有产品赎回预交易，失败调用
         */
        void obtainPsnXpadHoldProductRedeemVerifyFail();

    }

    /**
     * @description 确认信息界面 接口请求响应 回调
     */
    public interface RedeemConfirmInfoView extends BaseView<BasePresenter> {

        //I42-4.13 013持有产品赎回 PsnXpadHoldProductAndRedeem 成功调用
        void obtainPsnXpadHoldProductAndRedeemSuccess(PsnXpadHoldProductAndRedeemResModel model);

        //I42-4.13 013持有产品赎回 PsnXpadHoldProductAndRedeem，失败调用
        void obtainPsnXpadHoldProductAndRedeemFail();
        //查询产品列表成功，用于结果页的猜你喜欢
        void obtainQueryProductListSuccess(List<WealthListBean> wealthListBeen);
        //查询产品列表失败，用于结果页的猜你喜欢
        void obtainQueryProductListFail();
    }

    /**
     * @description 申请界面 -请求后台-接口调用
     */
    public interface RedeemPresenter extends BasePresenter {
        /**
         * I42-4.40 040产品详情查询PsnXpadProductDetailQuery
         *
         * @param productCode  产品代码
         * @param ibknum{省行联行号 String	O
         *                     返回项需展示(剩余额度、工作时间、挂单时间)，此项必输
         *                     根据PsnXpadAccountQuery接口的返回项进行上送}
         * @param productKind  产品性质
         */
        void getPsnXpadProductDetailQuery(String productCode, String ibknum, String productKind);

        /**
         * @description 创建页面公共会话
         */
        void getPSNCreatConversation();
        /**
         * I42-4.33 033持有产品赎回预交易 PsnXpadHoldProductRedeemVerify
         * @param mConversationId 会话ID
         * @param mPsnXpadProductBalanceQueryResModel 36接口
         * @param mDetailQueryResModel I42-4.40 040 产品详情查询
         * @param isAllRedeemQuantity   是否全部赎回
         * @param mSharesRedemption 赎回份额
         * @param isRedeemDate
         * @param mRedeemDate
         * @param mTranSeq 交易流水号-业绩基准使用
         */
        void getPsnXpadHoldProductRedeemVerify(String mConversationId,PsnXpadProductBalanceQueryResModel mPsnXpadProductBalanceQueryResModel, PsnXpadProductDetailQueryResModel mDetailQueryResModel,
                                               boolean isAllRedeemQuantity, String mSharesRedemption, boolean isRedeemDate, String mRedeemDate,String mTranSeq);
    }

    /**
     * @description 确认信息界面 -请求后台-接口调用
     */
    public interface RedeemConfirmInfoPresenter extends BasePresenter {
        /**
         * I42-4.40 040产品详情查询PsnXpadProductDetailQuery
         *
         * @param token          防重标识	token
         * @param dealCode       指令交易后台交易ID			{指令交易上送字段不可为空}
         * @param conversationId 会话id
         */
        void getPsnXpadHoldProductAndRedeem(String token, String dealCode, String conversationId);

        /**
         *  请求猜你喜欢的数据
         * @param model
         */
        void getQueryProductList(PortfolioPurchaseModel model);
    }
}
