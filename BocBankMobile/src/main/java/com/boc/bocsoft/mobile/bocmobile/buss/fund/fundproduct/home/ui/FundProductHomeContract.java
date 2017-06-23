package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.ui;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnQueryInvtBindingInfo.PsnQueryInvtBindingInfoParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCompanyQueryOutlay.PsnFundCompanyQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryFundBalance.PsnFundQueryFundBalanceParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryFundBalance.PsnFundQueryFundBalanceResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryOutlay.PsnFundQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryOutlay.PsnFundQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnGetFundCompanyList.PsnGetFundCompanyListParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryFundDetail.PsnQueryFundDetailParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryFundDetail.PsnQueryFundDetailResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOcrmProductQuery.PsnOcrmProductQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOcrmProductQuery.PsnOcrmProductQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.model.FundCompanyListViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.recommend.ui.FundRecommendContract;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.querymultiplefund.WFSSQueryMultipleFundParams;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.querymultiplefund.WFSSQueryMultipleFundResult;

/**
 * 基金首页
 * Created by liuzc on 2016/12/01.
 */
public class FundProductHomeContract {

    public interface HomeView extends FundUserContract.View {
        //查询登录前Funds的基金产品列表成功
        void queryFundsProductListNLogSuccess(PsnFundQueryOutlayResult result);
        //查询登录前Funds的基金产品列表失败
        void queryFundsProductListNLogFail(BiiResultErrorException biiResultErrorException);

        //查询WFSS产品列表失败
        void queryWFSSProductListFail(BiiResultErrorException biiResultErrorException);
        //查询WFSS产品列表成功
        void queryWFSSProductListSuccess(WFSSQueryMultipleFundResult result);

        //查询基金持仓信息成功
        void queryFundBalanceSuccess(PsnFundQueryFundBalanceResult result);
        //查询基金持仓信息失败
        void queryFundBalanceFail(BiiResultErrorException biiResultErrorException);

        //登陆后查询基金公司成功
        void getFundCompanyListSuccess(FundCompanyListViewModel result);
        //登陆后查询基金公司失败
        void getFundCompanyListFail(BiiResultErrorException biiResultErrorException);

        //登录前查询基金公司成功
        void getFundCompanyListNLogSuccess(FundCompanyListViewModel result);
        //登录前查询基金公司失败
        void getFundCompanyListNLogFail(BiiResultErrorException biiResultErrorException);

        //查询基金行情信息成功
        void queryFundsProductListLogSuccess(PsnQueryFundDetailResult result);
        //查询基金行情信息失败
        void queryFundsProductListLogFail(BiiResultErrorException biiResultErrorException);

        //查询基金推荐成功
        void queryFundsRecommendSuccess(PsnOcrmProductQueryResult result);
        //查询基金推荐失败
        void queryFundsRecommendFail(BiiResultErrorException biiResultErrorException);

    }

    public interface Presenter extends FundRecommendContract.Presenter, FundUserContract.Presenter {
        /**
         * 登录前查询Funds的基金产品列表
         * @param params
         */
        void queryFundsProductListNLog(PsnFundQueryOutlayParams params);

        /**
         * 查询WFSS基金产品列表
         */
        void queryWFSSProductList(WFSSQueryMultipleFundParams params);

        /**
         * 查询基金持仓信息
         * @param params
         */
        void queryFundBalance(PsnFundQueryFundBalanceParams params);

        /**
         * 查询基金公司（登陆前）
         * @param params
         */
        void getFundCompanyListNLog(PsnFundCompanyQueryOutlayParams params);

        /**
         * 查询基金公司（登陆后）
         * @param params
         */
        void getFundCompanyList(PsnGetFundCompanyListParams params);

        /**
         * 从FUNDS系统查询基金信息（登陆后）
         * @param params
         */
        void queryFundsProductListLog(PsnQueryFundDetailParams params);
        /**
         * 查询基金推荐
         * @param params
         */
        void queryFundsRecommend(PsnOcrmProductQueryParams params);
    }
}
