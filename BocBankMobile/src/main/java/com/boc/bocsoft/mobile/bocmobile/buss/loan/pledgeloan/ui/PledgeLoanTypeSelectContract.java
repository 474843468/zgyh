package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit.PledgeAvaAndPersonalTimeAccount;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.finance.ProductsData;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import java.util.List;

public class PledgeLoanTypeSelectContract {

    public interface View {

        void onLoadDepositDataSuccess(
                List<PledgeAvaAndPersonalTimeAccount> pledgeAvaAndPersonalTimeAccountList);

        void onLoadDepositDataFailed();

        void onLoadProductDataSuccess(ProductsData pledgeProductBeanList);

        void onLoadProductDataFailed();
    }

    public interface Presenter extends BasePresenter {
        void loadDepositData();
        void loadProductData();
    }
}