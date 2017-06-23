package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import java.util.List;

/**
 * Created by wangtong on 2016/9/13.
 */
public class PortfolioPurchaseContact {
    public interface MainView {
        //查询持有产品列表返回
        void psnXpadQueryGuarantyProductListReturned();
    }

    public interface BuyView {
        //查询客户风险等级与产品风险等级是否匹配返回
        void psnXpadQueryRiskMatchReturned();
    }

    public interface ConfirmView {
        //组合购买结果,返回列表用于结果页的猜你喜欢
        void psnXpadGuarantyBuyResultReturned(List<WealthListBean> wealthListBeen);
    }

    public interface Presenter extends BasePresenter {
        void setMainView(PortfolioPurchaseContact.MainView mainView);

        void setBuyView(PortfolioPurchaseContact.BuyView buyView);

        void setConfirmView(PortfolioPurchaseContact.ConfirmView confirmView);

        //查询持有产品列表
        void psnXpadQueryGuarantyProductList();

        //查询客户风险等级与产品风险等级是否匹配
        void psnXpadQueryRiskMatch();

        //组合购买
        void psnXpadGuarantyBuyResult();

    }
}
