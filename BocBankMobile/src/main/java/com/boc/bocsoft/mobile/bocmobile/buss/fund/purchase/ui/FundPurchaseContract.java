package com.boc.bocsoft.mobile.bocmobile.buss.fund.purchase.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.fund.purchase.model.FundbuyModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

public class FundPurchaseContract {

    /**
     * 购买信息填写页
     * */
    public interface FundPurchaseView extends BaseView<BasePresenter>{

        //查询协议详情

        //查询资金账户详情
        void queryAccountDetail(FundbuyModel accountDetailQueryModel);

        //查询可指定交易日期返回
        void queryFundCanDealDateQuery(FundbuyModel fundCanDealDateQueryModel);

        //查询基金公司详情返回
        void queryFundCompanyDetail(FundbuyModel fundCompanyInfoQueryModel);
    }

    /**
     * 购买确认页
     * */
    public interface FundPurchaseConfirmView extends  BaseView<BasePresenter>{

        /*//风险等级判定
        void  queryFundEvaluation(FundbuyModel fundbuyModel);*/
        //基金买入返回
        void fundBuySubmit(FundbuyModel fundbuyModel);

        //基金挂单买入返回
        void fundNightBuySubmit(FundbuyModel fundNightBuyModel);
    }

    /**
     * 购买结果页
     * */
    public interface FundPurchaseResultView extends BaseView<BasePresenter>{

    }

    /**
     * 电子合同签约页
     * */
    public interface  SignContractView extends  BaseView<BasePresenter>{
     void signContractSubmit(FundbuyModel signContractModel);
    }

    public interface FundPurchasePresenter extends BasePresenter {

        //查询协议详情

        //查询资金账户详情
        void queryAccountDetail(FundbuyModel accountDetailQueryModel);

        //查询可指定交易日期
        void queryFundCanDealDateQuery(FundbuyModel fundCanDealDateQueryModel);

        //查询基金公司详情
        void queryFundCompanyDetail(FundbuyModel fundCompanyInfoQueryModel);

        //基金买入
        void fundBuySubmit(FundbuyModel fundbuyModel);

        //基金挂单买入
        void fundNightBuySubmit(FundbuyModel fundNightBuyModel);

        //签署电子合同
        void signContractSubmit(FundbuyModel signContractModel);
    }

}