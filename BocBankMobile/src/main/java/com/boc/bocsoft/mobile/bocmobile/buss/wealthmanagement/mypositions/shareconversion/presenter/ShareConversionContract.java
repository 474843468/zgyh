package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.presenter;

import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.PsnXpadQuantityDetail.PsnXpadQuantityDetailResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadproductdetailquery.PsnXpadProductDetailQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.model.PsnXpadQueryRiskMatch.PsnXpadQueryRiskMatchResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.model.PsnXpadShareTransitionCommit.PsnXpadShareTransitionCommitResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.model.PsnXpadShareTransitionVerify.PsnXpadShareTransitionVerifyResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.model.PortfolioPurchaseModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * Created by zn
 *
 * @description 中银理财---份额转换---Contract
 * @date 2016/9/12
 */
public class ShareConversionContract {

    /**
     * @author zn
     * @description 申请界面 接口请求响应 回调
     * @date 2016/9/12
     */
    public interface ShareConversionView extends BaseView<BasePresenter> {
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

        //=======================查询客户风险等级与产品风险等级是否匹配查询============

        /**
         * 查询客户风险等级与产品风险等级是否匹配   PsnXpadQueryRiskMatch  成功调用
         */
        void obtainPsnXpadQueryRiskMatchSuccess(PsnXpadQueryRiskMatchResModel PsnXpadQueryRiskMatchResModel);

        /**
         * 查询客户风险等级与产品风险等级是否匹配   PsnXpadQueryRiskMatch  失败调用
         */
        void obtainPsnXpadQueryRiskMatchFail();

        //====================获取协议书，说明书内容===================
//        /**
//         * 中国银行理财产品总协议书  成功调用
//         */
//        void obtainProductAgreementSuccess(CreditContractRes result);
//        /**
//         * 中国银行理财产品总协议书  失败调用
//         */
//        void obtainProductAgreementFail(ErrorException e);
//
//        /**
//         * 产品说明书   成功调用
//         */
//        void obtainProductDirectionSuccess(LoanContractRes result);
//        /**
//         * 产品说明书   失败调用
//         */
//        void obtainProductDirectionFail(ErrorException e);

        //====================份额转换 预交易===================

        /**
         * 份额转换 预交易 PsnXpadShareTransitionVerifyResModel 成功调用
         */
        void obtainPsnXpadShareTransitionVerifySuccess(PsnXpadShareTransitionVerifyResModel PsnXpadShareTransitionVerifyResModel);

        /**
         * 份额转换 预交易 PsnXpadShareTransitionVerifyResModel 失败调用
         */
        void obtainPsnXpadShareTransitionVerifyFail();


    }


    /**
     * @description 确认信息界面 接口请求响应 回调
     */
    public interface ShareConversionConfirmInfoView extends BaseView<BasePresenter> {
        /**
         * 4.71 071 份额转换  确认提交  PsnXpadShareTransitionCommit  成功调用
         */
        void obtainPsnXpadShareTransitionCommitResModel(PsnXpadShareTransitionCommitResModel PsnXpadShareTransitionCommitResModel);

        /**
         * 4.71 071 份额转换  确认提交  PsnXpadShareTransitionCommit  失败调用
         */
        void obtainPsnXpadShareTransitionCommitFail();

        //查询产品列表成功，用于结果页的猜你喜欢
        void obtainQueryProductListSuccess(List<WealthListBean> wealthListBeen);

        //查询产品列表失败，用于结果页的猜你喜欢
        void obtainQueryProductListFail();


    }


    /**
     * @description 申请界面 -请求后台-接口调用
     */
    public interface ShareConversionPresenter extends BasePresenter {
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
         * 查询客户风险等级与产品风险等级是否匹配  PsnXpadQueryRiskMatch
         *
         * @param serialCode  周期性产品系列编号
         * @param productCode 产品代码
         * @param digitalCode 产品数字代码
         * @param accountKey  账号缓存标识
         */
        void getPsnXpadQueryRiskMatch(String serialCode, String productCode, String digitalCode, String accountKey);

//        /**
//         * 中国银行理财产品总协议书
//         */
//        void getBOCProductAgreementContract(String conversationId,CreditContractReq req);
//
//        /**
//         *产品说明书
//         */
//        void getProductDirectionContract(String conversationId,LoanContractReq req);

        /**
         * 4.70 070份额转换预交易  PsnXpadShareTransitionVerify
         * <p/>
         * accountKey     帐号缓存标识
         * proId          产品代码
         * tranUnit       转换份额
         * token          防重标识
         * charCode       钞汇类型
         * serialNo       持仓流水号
         * conversationId 会话id
         */
        void getPsnXpadShareTransitionVerify(String mConversationId,
                                             PsnXpadQuantityDetailResModel.ListEntity mListInfo,
                                             String mShareMoney,
                                             String productCode);
    }

    public interface ShareConversionConfirmInfoParenter extends BasePresenter {

        /**
         * 4.71 071 份额转换  确认提交  PsnXpadShareTransitionCommit
         * <p>
         * token          防重机制，通过PSNGetTokenId接口获取
         * accountKey     帐号缓存标识
         * conversationId 会话id
         */
        void getPsnXpadShareTransitionCommit(String mConversationId, String accountKey);

        /**
         * 请求猜你喜欢的数据
         *
         * @param model
         */
        void getQueryProductList(PortfolioPurchaseModel model);
    }

}
