package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.presenter;

import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui.SelectFundCoContract;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

public class SelectFundCoPresenter extends RxPresenter implements SelectFundCoContract.Presenter {

    private SelectFundCoContract.View mSelectFundCoView;

    public SelectFundCoPresenter(SelectFundCoContract.View view) {
        mSelectFundCoView = view;
    }


}