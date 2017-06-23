package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.model.PortfolioPurchaseModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthDetailsBean;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * 作者：XieDu
 * 创建时间：2016/11/11 16:20
 * 描述：
 */
public class PortfolioSelectContract {
    public interface View {
        void onQueryProductDetailSuccess(WealthDetailsBean wealthDetailsBean);
    }

    public interface Presenter extends BasePresenter {
        void queryProductDetail(PortfolioPurchaseModel model);
    }
}
