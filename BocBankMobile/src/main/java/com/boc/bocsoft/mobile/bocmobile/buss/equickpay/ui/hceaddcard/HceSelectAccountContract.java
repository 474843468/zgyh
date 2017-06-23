package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hceaddcard;

import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.CardBrandModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * Created by yangle on 2016/12/15.
 * 描述:
 */
public interface HceSelectAccountContract {

    interface View {

        void showLoading();

        void closeLoading();

        void showErrorDialog(String msg);

        void onQueryCardBrandSuccess(AccountBean accountBean, CardBrandModel cardBrand);

    }

    interface Presenter extends BasePresenter{

        void queryAccountDetail(AccountBean bean);

        void queryCardBrand(AccountBean bean);
    }

}
