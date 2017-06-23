package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.openwealthmanager.ui.OpenStatusI;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.ProtocolIntelligentDetailsBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.ProtocolModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.PsnXpadSignInitBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.DetailsRequestBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthDetailsBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthHistoryBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthProfitBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

import java.math.BigDecimal;

/**
 * 中银理财首页
 * Created by liuweidong on 2016/9/18.
 */
public class WealthContract {

    public interface HomeView {
        /**
         * 市值（登录后）
         *
         * @param result
         */
        void queryAssetBalanceSuccess(String result);

        /**
         * 是否开通投资理财服务失败（登录后）
         */
        void isOpenInvestmentManageFail();

        /**
         * 是否开通投资理财服务成功（登录后）
         *
         * @param result
         */
        void isOpenInvestmentManageSuccess(boolean result);

        /**
         * 查询客户理财账户信息失败（登录后）
         */
        void queryFinanceAccountInfoFail();

        /**
         * 查询客户理财账户信息成功（登录后）
         */
        void queryFinanceAccountInfoSuccess();

        void queryRiskEvaluationFail(BiiResultErrorException biiResultErrorException);

        /**
         * 风险评估查询成功（登录后）
         */
        void queryRiskEvaluationSuccess(String custExist, String isRisk, String evalExpired);

        /**
         * 查询购买初始化成功（登录后）
         */
        void queryBuyInitSuccess();

        /**
         * 查询产品列表失败（登录前）
         */
        void queryProductListNFail();

        /**
         * 查询产品列表成功（登录前）
         */
        void queryProductListNSuccess(WealthViewModel viewModel);

        /**
         * 查询产品列表失败（登录后）
         */
        void queryProductListYFail();

        /**
         * 查询产品列表成功（登录后）
         */
        void queryProductListYSuccess(WealthViewModel viewModel);

        /**
         * 查询产品详情失败（登录前）
         */
        void queryProductDetailNFail();

        /**
         * 查询产品详情成功（登录前）
         */
        void queryProductDetailNSuccess(WealthDetailsBean detailsBean);

        /**
         * 查询产品详情失败（登录后）
         */
        void queryProductDetailYFail();

        /**
         * 查询产品详情成功（登录后）
         */
        void queryProductDetailYSuccess(WealthDetailsBean detailsBean);
    }

    public interface DetailsView {
        /**
         * 是否开通投资理财服务失败（登录后）
         */
        void isOpenInvestmentManageFail();

        /**
         * 是否开通投资理财服务成功（登录后）
         *
         * @param result
         */
        void isOpenInvestmentManageSuccess(boolean result);
        void queryNetHistoryFailY();

        void queryNetHistorySuccessY(WealthHistoryBean wealthHistoryBean);

        void queryNetHistoryFailN();

        void queryNetHistorySuccessN(WealthHistoryBean wealthHistoryBean);

        /**
         * 查询产品详情失败（登录前）
         */
        void queryProductDetailNFail();

        /**
         * 查询产品详情成功（登录前）
         */
        void queryProductDetailNSuccess(WealthDetailsBean detailsBean);

        /**
         * 查询产品详情失败
         */
        void queryProductDetailYFail();

        /**
         * 查询产品详情成功
         */
        void queryProductDetailYSuccess(WealthDetailsBean detailsBean);

        /**
         * 查询产品投资协议成功
         */
        void queryInvestTreatySuccess(ProtocolModel viewModel);

        /**
         * 周期性产品续约协议签约/签约初始化查询成功
         */
        void psnXpadSignInitSuccess(PsnXpadSignInitBean initBean);

        void queryTreatyDetailSuccess(ProtocolIntelligentDetailsBean details);
    }

    public interface ProfitView {
        void profitCalcFail();

        void profitCalcSuccess(BigDecimal expprofit, String procur);
    }

    public interface Presenter extends BasePresenter {

        void queryOpenStatus(OpenStatusI openStatusI);

        /**
         * 客户资产负债信息查询
         */
        void queryAssetBalance();

        /**
         * 是否开通投资理财服务
         */
        void isOpenInvestmentManage();

        /**
         * 查询客户理财账户信息
         */
        void queryFinanceAccountInfo();

        /**
         * 风险评估查询
         */
        void queryRiskEvaluation();

        /**
         * 查询购买初始化
         */
        void queryBuyInit();

        /**
         * 查询产品列表（登录前）
         */
        void queryProductListN(int index, String prodCode, boolean isSearch);

        /**
         * 查询产品详情（登录前）
         */
        void queryProductDetailN(DetailsRequestBean requestBean);

        /**
         * 更新客户最近操作的理财账号
         */
        void updateRecentAccount();

        /**
         * 产品查询与购买（登录后）
         */
        void queryProductListY(int index, String prodCode, boolean isSearch);

        /**
         * 查询产品详情（登录后）
         */
        void queryProductDetailY(DetailsRequestBean requestBean);

        /**
         * 历史净值查询
         */
        void queryNetHistoryY(String prodCode, String date);

        void queryNetHistoryN(String prodCode, String date);

        /**
         * 收益试算
         */
        void profitCalc(WealthProfitBean profitBean);

        /**
         * 产品投资协议查询
         */
        void queryInvestTreaty(String accountID, String prodCode);

        /**
         * 4.10 010周期性产品续约协议签约/签约初始化
         */
        void psnXpadSignInit(WealthDetailsBean detailsBean, DetailsRequestBean requestBean);

        void queryTreatyDetail(String accountId, String agrCode);
    }
}
