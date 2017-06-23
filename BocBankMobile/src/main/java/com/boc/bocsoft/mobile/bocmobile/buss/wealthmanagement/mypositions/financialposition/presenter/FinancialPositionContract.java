package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.presenter;

import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProgressQuery.PsnXpadProgressQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProgressQueryOutlay.PsnXpadProgressQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQuantityDetail.PsnXpadQuantityDetailParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadReferProfitDetailQuery.PsnXpadReferProfitDetailQueryParams;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.PsnXpadExpectYieldQuery.PsnXpadExpectYieldQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.PsnXpadExpectYieldQueryOutlay.PsnXpadExpectYieldQueryOutlayResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.PsnXpadProgressQueryOutlay.PsnXpadProgressQueryOutlayResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.PsnXpadQuantityDetail.PsnXpadQuantityDetailResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnXpadSetBonusMode.psnXpadSetBonusModeResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnXpadreferprofitdetailquery.PsnXpadReferProfitDetailQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadaccountquery.PsnXpadAccountQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadproductbalancequery.PsnXpadProductBalanceQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadprogressquery.PsnXpadProgressQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadreferprofitquery.PsnXpadReferProfitQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadproductdetailquery.PsnXpadProductDetailQueryResModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * @author yx
 * @description 中银理财-我的持仓-Contract
 * @date 2016-9-16 13:52:32
 */
public class FinancialPositionContract {
    /**
     * @description 持仓列表界面 -请求后台-接口调用
     */
    public interface FinancialpositionPresenter extends BasePresenter {
        /**
         * @description 创建页面公共会话
         */
        void getPSNCreatConversation();

        /**
         * I42-4.36 036查询客户持仓信息PsnXpadProductBalanceQuery
         */
        void getPsnXpadProductBalanceQuery();

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
         * I42-4.37 037查询客户理财账户信息 PsnXpadAccountQuery
         *
         * @param xpadAccountSatus 账户状态	String	O	0：停用 1：可用 不输代表查询全部
         * @param queryType        查询类型	String	必输	0：查询所有已登记的理财账户  1、查询所有已登记并且关联到网银的理财账户
         */
        void getPsnXpadAccountQuery(String xpadAccountSatus, String queryType);
    }

    /**
     * @description 持仓列表页面 -接口请求响应-回调
     */
    public interface FinancialPositionView extends BaseView<BasePresenter> {

        //   =================== I42-4.36 036查询客户持仓信息PsnXpadProductBalanceQuery ========
        //        ======================获取会话ID=======================

        /**
         * @description 获取会话ID，成功调用
         */
        void obtainConversationSuccess(String conversationId);

        /**
         * @description 获取会话ID，失败调用
         */
        void obtainConversationFail();

        /**
         * I42-4.36 036查询客户持仓信息PsnXpadProductBalanceQuery，成功调用
         */
        void obtainPsnXpadProductBalanceSuccess(List<PsnXpadProductBalanceQueryResModel> mViewModel);

        /**
         * I42-4.36 036查询客户持仓信息PsnXpadProductBalanceQuery，失败调用
         */
        void obtainPsnXpadProductBalanceFail();
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
        //============I42-4.37 037查询客户理财账户信息 PsnXpadAccountQuery===========

        /**
         * I42-4.37 037查询客户理财账户信息 PsnXpadAccountQuery 成功调用
         *
         * @param mPsnXpadAccountQueryResModel
         */
        void obtainPsnXpadAccountQuerySuccess(PsnXpadAccountQueryResModel mPsnXpadAccountQueryResModel);

        /**
         * I42-4.37 037查询客户理财账户信息 PsnXpadAccountQuery 失败调用
         */
        void obtainPsnXpadAccountQueryFail();

    }
//    ========================收益累进===================================

    /**
     * 持仓详情页面(收益累进) - 请求后台-接口调用
     * 收益详情查询 - 请求后台-接口调用
     */
    public interface FinancialTypeEarnBuildDetailPresenter extends BasePresenter {
        /**
         * 4.59 059 参考收益详情查询 PsnXpadReferProfitDetailQuery
         */
        void getPsnXpadReferProfitDetailQuery(PsnXpadReferProfitDetailQueryParams params);

        /**
         * PsnXpadReferProfitQuery  收益汇总查询
         *
         * @param accountKey  账号缓存标识(必选)
         * @param productCode 产品代码(必选)
         * @param kind        产品性质(必选)
         * @param charCode    钞汇标识(必输01：钞;02：汇;00：人民币)
         * @param tranSeq     份额流水号(业绩基准产品必输（PsnXpadProductBalanceQuery返回项standPro不为0的产品），取自PsnXpadQuantityDetail接口返回项tranSeq)
         */
        void getPsnXpadReferProfitQuery(String accountKey, String productCode, String kind, String charCode, String tranSeq);

        /**
         * 4.14 014修改分红方式交易 PsnXpadSetBonusMode
         *
         * @param mConversationId
         * @param mCurrentBonusMode
         * @param banlanceDeta
         */
        void getPsnXpadSetBonusMode(String mConversationId, String mCurrentBonusMode, PsnXpadProductBalanceQueryResModel banlanceDeta);
    }

    /**
     * 持仓详情页面(收益累进) - 请求响应 - 回调
     * 收益详情查询 - 请求响应 - 回调
     */
    public interface FinancialTypeEarnBuildDetailView extends BaseView<BasePresenter> {
        /**
         * 4.59 059 参考收益详情查询 PsnXpadReferProfitDetailQuery
         * 成功回调
         */
        void obtainPsnXpadReferProfitDetailQuerySuccess(PsnXpadReferProfitDetailQueryResModel resModel);

        /**
         * 4.59 059 参考收益详情查询 PsnXpadReferProfitDetailQuery
         * 失败回调
         */
        void obtainPsnXpadReferProfitDetailQueryFault();

        /**
         * 收益汇总查询  PsnXpadReferProfitQuery,成功调用
         *
         * @param mViewModel
         */
        void obtainPsnXpadReferProfitQuerySuccess(PsnXpadReferProfitQueryResModel mViewModel);

        /**
         * 收益汇总查询  PsnXpadReferProfitQuery,失败调用
         */
        void obtainPsnXpadReferProfitQueryFail(BiiResultErrorException biiResultErrorException);

        /**
         * 4.14 014修改分红方式交易 PsnXpadSetBonusMode,成功调用
         *
         * @param mViewModel
         */
        void obtainPsnXpadSetBonusModeSuccess(psnXpadSetBonusModeResModel mViewModel);

        /**
         * 4.14 014修改分红方式交易 PsnXpadSetBonusMode,失败调用
         */
        void obtainPsnXpadSetBonusModeFail(BiiResultErrorException biiResultErrorException);
    }
//===========================日积月累==================================

    /**
     * 持仓详情页面(日积月累) - 请求响应 - 回调
     */
    public interface FinancialTypeFixedTermDetailPresenter extends BasePresenter {
        /**
         * 4.59 059 参考收益详情查询 PsnXpadReferProfitDetailQuery
         */
        void getPsnXpadReferProfitDetailQuery(PsnXpadReferProfitDetailQueryParams params);

        /**
         * PsnXpadReferProfitQuery  收益汇总查询
         *
         * @param accountKey  账号缓存标识(必选)
         * @param productCode 产品代码(必选)
         * @param kind        产品性质(必选)
         * @param charCode    钞汇标识(必输01：钞;02：汇;00：人民币)
         * @param tranSeq     份额流水号(业绩基准产品必输（PsnXpadProductBalanceQuery返回项standPro不为0的产品），
         *                    取自PsnXpadQuantityDetail接口返回项tranSeq)
         */
        void getPsnXpadReferProfitQuery(String accountKey, String productCode, String kind, String charCode, String tranSeq);


        /**
         * 4.14 014修改分红方式交易 PsnXpadSetBonusMode
         *
         * @param mConversationId
         * @param mCurrentBonusMode
         * @param banlanceDeta
         */
        void getPsnXpadSetBonusMode(String mConversationId, String mCurrentBonusMode, PsnXpadProductBalanceQueryResModel banlanceDeta);
    }

    /**
     * 持仓详情页面(日积月累) - 请求响应 - 回调
     */
    public interface FinancialTypeFixedTermDetailView extends BaseView<BasePresenter> {
        /**
         * 4.59 059 参考收益详情查询 PsnXpadReferProfitDetailQuery
         * 成功回调
         */
        void obtainPsnXpadReferProfitDetailQuerySuccess(PsnXpadReferProfitDetailQueryResModel resModel);

        /**
         * 4.59 059 参考收益详情查询 PsnXpadReferProfitDetailQuery
         * 失败回调
         */
        void obtainPsnXpadReferProfitDetailQueryFault();

        /**
         * 收益汇总查询  PsnXpadReferProfitQuery,成功调用
         *
         * @param mViewModel
         */
        void obtainPsnXpadReferProfitQuerySuccess(PsnXpadReferProfitQueryResModel mViewModel);

        /**
         * 收益汇总查询  PsnXpadReferProfitQuery,失败调用
         */
        void obtainPsnXpadReferProfitQueryFail(BiiResultErrorException biiResultErrorException);

        /**
         * 4.14 014修改分红方式交易 PsnXpadSetBonusMode,成功调用
         *
         * @param mViewModel
         */
        void obtainPsnXpadSetBonusModeSuccess(psnXpadSetBonusModeResModel mViewModel);

        /**
         * 4.14 014修改分红方式交易 PsnXpadSetBonusMode,失败调用
         */
        void obtainPsnXpadSetBonusModeFail(BiiResultErrorException biiResultErrorException);
    }

//================================持仓详情（净值型）==========================

    /**
     * @author zn
     * @description 持仓详情（净值型）-请求后台-接口调用
     */
    public interface FinancialTypeNetValuePresenter extends BasePresenter {
//        /**
//         * 4.59 059 参考收益详情查询 PsnXpadReferProfitDetailQuery
//         */
//        void getPsnXpadReferProfitDetailQuery(PsnXpadReferProfitDetailQueryParams params);

        /**
         * PsnXpadReferProfitQuery  收益汇总查询
         *
         * @param accountKey  账号缓存标识(必选)
         * @param productCode 产品代码(必选)
         * @param kind        产品性质(必选)
         * @param charCode    钞汇标识(必输01：钞;02：汇;00：人民币)
         * @param tranSeq     份额流水号(业绩基准产品必输（PsnXpadProductBalanceQuery返回项standPro不为0的产品），
         *                    取自PsnXpadQuantityDetail接口返回项tranSeq)
         */
        void getPsnXpadReferProfitQuery(String accountKey, String productCode, String kind, String charCode, String tranSeq);


        /**
         * 4.14 014修改分红方式交易 PsnXpadSetBonusMode
         *
         * @param mConversationId
         * @param mCurrentBonusMode
         * @param banlanceDeta
         */
        void getPsnXpadSetBonusMode(String mConversationId, String mCurrentBonusMode, PsnXpadProductBalanceQueryResModel banlanceDeta);

    }

    /**
     * @description 持仓详情（净值型） -接口请求响应-回调
     */
    public interface FinancialTypeNetValueView extends BaseView<BasePresenter> {
//        /**
//         * 4.59 059 参考收益详情查询 PsnXpadReferProfitDetailQuery
//         * 成功回调
//         */
//        void obtainPsnXpadReferProfitDetailQuerySuccess(PsnXpadReferProfitDetailQueryResModel resModel);
//
//        /**
//         * 4.59 059 参考收益详情查询 PsnXpadReferProfitDetailQuery
//         * 失败回调
//         */
//        void obtainPsnXpadReferProfitDetailQueryFault();

        /**
         * 收益汇总查询  PsnXpadReferProfitQuery,成功调用
         *
         * @param mViewModel
         */
        void obtainPsnXpadReferProfitQuerySuccess(PsnXpadReferProfitQueryResModel mViewModel);

        /**
         * 收益汇总查询  PsnXpadReferProfitQuery,失败调用
         */
        void obtainPsnXpadReferProfitQueryFail(BiiResultErrorException biiResultErrorException);

        /**
         * 4.14 014修改分红方式交易 PsnXpadSetBonusMode,成功调用
         *
         * @param mViewModel
         */
        void obtainPsnXpadSetBonusModeSuccess(psnXpadSetBonusModeResModel mViewModel);

        /**
         * 4.14 014修改分红方式交易 PsnXpadSetBonusMode,失败调用
         */
        void obtainPsnXpadSetBonusModeFail(BiiResultErrorException biiResultErrorException);
    }
//===========================业绩基准================================

    /**
     * 持仓详情--业绩基准  -请求后台-接口调用
     *
     * @author zn
     */
    public interface FinancialTypeOutStandPresenter extends BasePresenter {
        /**
         * 份额明细查询  PsnXpadQuantityDetail
         */
        void getPsnXpadQuantityDetail(PsnXpadQuantityDetailParams params);

//        /**
//         * 4.59 059 参考收益详情查询 PsnXpadReferProfitDetailQuery
//         */
//        void getPsnXpadReferProfitDetailQuery(PsnXpadReferProfitDetailQueryParams params);

        /**
         * PsnXpadReferProfitQuery  收益汇总查询
         *
         * @param accountKey  账号缓存标识(必选)
         * @param productCode 产品代码(必选)
         * @param kind        产品性质(必选)
         * @param charCode    钞汇标识(必输01：钞;02：汇;00：人民币)
         * @param tranSeq     份额流水号(业绩基准产品必输（PsnXpadProductBalanceQuery返回项standPro不为0的产品），
         *                    取自PsnXpadQuantityDetail接口返回项tranSeq)
         */
        void getPsnXpadReferProfitQuery(String accountKey, String productCode, String kind, String charCode, String tranSeq);


        /**
         * 4.14 014修改分红方式交易 PsnXpadSetBonusMode
         *
         * @param mConversationId   会话id
         * @param mCurrentBonusMode 分红方式
         * @param banlanceDeta      接口请求参数，都来自36接口
         */
        void getPsnXpadSetBonusMode(String mConversationId, String mCurrentBonusMode, PsnXpadProductBalanceQueryResModel banlanceDeta);
    }

    /**
     * @description 持仓详情--业绩基准 -接口请求响应-回调
     */
    public interface FinancialTypeOutStandView extends BaseView<BasePresenter> {
        /**
         * 4.68 068份额明细查询  PsnXpadQuantityDetail
         * 成功回调
         */
        void obtainPsnXpadQuantityDetailSuccess(PsnXpadQuantityDetailResModel resModel);

        /**
         * 4.68 068份额明细查询   PsnXpadQuantityDetail
         * 失败回调
         */
        void obtainPsnXpadQuantityDetailFail();

//        /**
//         * 4.59 059 参考收益详情查询 PsnXpadReferProfitDetailQuery
//         * 成功回调
//         */
//        void obtainPsnXpadReferProfitDetailQuerySuccess(PsnXpadReferProfitDetailQueryResModel resModel);
//
//        /**
//         * 4.59 059 参考收益详情查询 PsnXpadReferProfitDetailQuery
//         * 失败回调
//         */
//        void obtainPsnXpadReferProfitDetailQueryFault();

        /**
         * 收益汇总查询  PsnXpadReferProfitQuery,成功调用
         *
         * @param mViewModel
         */
        void obtainPsnXpadReferProfitQuerySuccess(PsnXpadReferProfitQueryResModel mViewModel);

        /**
         * 收益汇总查询  PsnXpadReferProfitQuery,失败调用
         */
        void obtainPsnXpadReferProfitQueryFail(BiiResultErrorException biiResultErrorException);

        /**
         * 4.14 014修改分红方式交易 PsnXpadSetBonusMode,成功调用
         *
         * @param mViewModel
         */
        void obtainPsnXpadSetBonusModeSuccess(psnXpadSetBonusModeResModel mViewModel);

        /**
         * 4.14 014修改分红方式交易 PsnXpadSetBonusMode,失败调用
         */
        void obtainPsnXpadSetBonusModeFail(BiiResultErrorException biiResultErrorException);
    }

    /**
     * 业绩基准 图表展示详情页  -请求后台-接口调用
     */
    public interface FinancialTypeOutStandQueryPresenter extends BasePresenter {
//        /**
//         * 4.59 059 参考收益详情查询 PsnXpadReferProfitDetailQuery
//         */
//        void getPsnXpadReferProfitDetailQuery(PsnXpadReferProfitDetailQueryParams params);

    }

    /**
     * @description 业绩基准 图表展示详情页 -接口请求响应-回调
     */
    public interface FinancialTypeOutStandQueryView extends BaseView<BasePresenter> {
//        /**
//         * 4.59 059 参考收益详情查询 PsnXpadReferProfitDetailQuery
//         * 成功回调
//         */
//        void obtainPsnXpadReferProfitDetailQuerySuccess(PsnXpadReferProfitDetailQueryResModel resModel);
//
//        /**
//         * 4.59 059 参考收益详情查询 PsnXpadReferProfitDetailQuery
//         * 失败回调
//         */
//        void obtainPsnXpadReferProfitDetailQueryFault();
    }

    /**
     * 累进产品收益率产品
     */
    public interface FinancialTypeProgressQueryPresenter extends BasePresenter {
        /**
         * @description 创建页面公共会话
         */
        void getPSNCreatConversation();
        /**
         * 4.34 034累进产品收益率查询PsnXpadProgressQuery
         */
        void getPsnXpadProgressQuery(PsnXpadProgressQueryParams params);

        /**
         * 4.53 053 PsnXpadProgressQueryOutlay 登录前累进产品收益率查询
         */
        void getPsnXpadProgressQueryOutlay(PsnXpadProgressQueryOutlayParams params);

    }

    public interface FinancialTypeProgressQueryView extends BaseView<BasePresenter> {
        /**
         * @description 获取会话ID，成功调用
         */
        void obtainConversationSuccess(String conversationId);

        /**
         * @description 获取会话ID，失败调用
         */
        void obtainConversationFail();
        /**
         * 4.34 034累进产品收益率查询PsnXpadProgressQuery
         * 成功回调
         */
        void obtainPsnXpadProgressQuerySuccess(PsnXpadProgressQueryResModel resModel);

        /**
         * 4.34 034累进产品收益率查询PsnXpadProgressQuery
         * 失败回调
         */
        void obtainPsnXpadProgressQueryFault();

        /**
         * 4.53 053 PsnXpadProgressQueryOutlay 登录前累进产品收益率查询
         * 成功回调
         */
        void obtainPsnXpadProgressQueryOutlaySuccess(PsnXpadProgressQueryOutlayResModel resModel);

        /**
         * 4.53 053 PsnXpadProgressQueryOutlay 登录前累进产品收益率查询
         * 失败回调
         */
        void obtainPsnXpadProgressQueryOutlayFault();

    }


    /**
     * 业绩基准 预期年华收益率  请求
     * 4.72 072业绩基准产品预计年收益率查询 PsnXpadExpectYieldQuery
     */
    public interface FinancialPsnXpadExpectYieldQueryPresenter extends BasePresenter {
        /**
         * 4.72 072业绩基准产品预计年收益率查询 PsnXpadExpectYieldQuery  登陆后
         * @param productCode
         * @param queryDate
         */
        void getPsnXpadExpectYieldQuery(String productCode, String queryDate);

        /**
         *
         * @param productCode
         * @param queryDate
         */
        void getPsnXpadExpectYieldQueryOutlay(String productCode, String queryDate);
    }

    /**
     * 业绩基准 预期年华收益率   响应
     * 4.72 072业绩基准产品预计年收益率查询 PsnXpadExpectYieldQuery
     */
    public interface FinancialPsnXpadExpectYieldQueryView extends BaseView<BasePresenter> {
        /**
         * 4.72 072业绩基准产品预计年收益率查询 PsnXpadExpectYieldQuery  登陆后
         * @param resModel
         */
        void obtainPsnXpadExpectYieldQuerySuccess(PsnXpadExpectYieldQueryResModel resModel);

        void obtainPsnXpadExpectYieldQueryFault();

        /**
         * 4.73 073登录前业绩基准产品预计年收益率查询 PsnXpadExpectYieldQueryOutlay  登陆前
         * @param resModel
         */
        void obtainPsnXpadExpectYieldQueryOutlaySuccess(PsnXpadExpectYieldQueryOutlayResModel resModel);

        void obtainPsnXpadExpectYieldQueryOutlayFault();
    }

}
