package com.boc.bocsoft.mobile.bocmobile.buss.fund.redeem.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.fund.redeem.model.FundRedeemModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

public class FundRedeemContract {

    /**
     * 赎回信息填写页
     * */
    public interface FundRedeemView extends BaseView<BasePresenter> {

        //查询基金公司详情返回
        void queryFundCompanyDetail(FundRedeemModel fundCompanyInfoQueryModel);

        //查询可指定交易日期返回
        void queryFundCanDealDateQuery(FundRedeemModel fundCanDealDateQueryModel);

        //基金快速赎回额度查询返回
        void quickSellQuotaQuery(FundRedeemModel fundRedeemModel);
    }

    /**
     * 赎回确认页
     * */
    public interface FundRedeemConfirmView extends BaseView<BasePresenter> {

        //查询基金公司详情
        void queryFundCompanyDetail(FundRedeemModel fundRedeemModel);
        //基金赎回
        void fundSell(FundRedeemModel fundsellModel);

        //基金挂单赎回
        void fundNightSell(FundRedeemModel fundnightsellModel);

        //基金快速赎回
        void fundQuickSell(FundRedeemModel fundQuickSellModel);

    }

    /**
     * 赎回结果页
     * */
    public interface FundRedeemResultView extends BaseView<BasePresenter> {

    }

    public interface FundRedeemPresenter extends BasePresenter {

        //查询基金公司详情
        void queryFundCompanyDetail(FundRedeemModel fundRedeemModel);

        //查询可指定交易日期返回
        void queryFundCanDealDateQuery(FundRedeemModel fundCanDealDateQueryModel);

        //基金赎回
        void fundSell(FundRedeemModel fundsellModel);

        //基金挂单赎回
        void fundNightSell(FundRedeemModel fundnightsellModel);

        //基金快速赎回
        void fundQuickSell(FundRedeemModel fundQuickSellModel);

        //基金快速赎回额度查询
        void quickSellQuotaQuery(FundRedeemModel fundRedeemModel);

    }

}