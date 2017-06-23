package com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui;

import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryResult;
import com.boc.bocsoft.mobile.bocmobile.base.model.GoldBean;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.searchallpro.SearchAllProParams;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.searchallpro.SearchAllProResult;
import com.boc.bocsoft.mobile.cr.bus.product.model.CRgetProductList.CRgetProductListResult;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * 产品搜索
 * Created by gwluo on 2016/11/5.
 */

public class ProductSearchContract {
    public interface View extends BaseView<ProductSearchContract.Presenter> {
        /**
         * 产品搜索成功
         *
         * @param resultList
         */
        void searchProductSucc(SearchAllProResult resultList);

        /**
         * 产品搜索失败
         */
        void searchProductFail();

        /**
         * 热门产品搜索
         */
        void getProductListSucc(CRgetProductListResult result);

        /**
         * 热门产品搜索
         */
        void getProductListFail();

        /**
         * 客户理财账户信息
         */
        void queryFinanceAccountInfoFail();

        /**
         * 客户理财账户信息
         */
        void queryFinanceAccountInfoSuccess(PsnXpadAccountQueryResult result);

    }

    public interface Presenter extends BasePresenter {
        void searchAllPro(SearchAllProParams params);

        void cRgetProductList();

        void queryFinanceAccountInfo();
    }
}
