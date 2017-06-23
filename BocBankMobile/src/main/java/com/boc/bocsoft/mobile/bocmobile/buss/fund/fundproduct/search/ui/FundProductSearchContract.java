package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.search.ui;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryOutlay.PsnFundQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryFundDetail.PsnQueryFundDetailParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.searchallpro.SearchAllProParams;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.searchallpro.SearchAllProResult;
import com.boc.bocsoft.mobile.cr.bus.product.model.CRgetProductList.CRgetProductListResult;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * 基金产品搜索
 * Created by liuzc on 2016/12/31.
 */

public class FundProductSearchContract {
    public interface View extends BaseView<Presenter> {
        /**
         * 产品搜索成功
         *
         * @param resultList
         */
        void searchProductSucc(SearchAllProResult resultList);

        /**
         * 产品搜索失败
         */
        void searchProductFail(BiiResultErrorException biiResultErrorException);

        /**
         * 热门产品搜索
         */
        void getProductListSucc(CRgetProductListResult result);

        /**
         * 热门产品搜索
         */
        void getProductListFail(BiiResultErrorException biiResultErrorException);

    }

    public interface Presenter extends BasePresenter {
        void cRgetProductList();

        void searchAllPro(SearchAllProParams params, boolean isLoginAndBinding);
    }
}
