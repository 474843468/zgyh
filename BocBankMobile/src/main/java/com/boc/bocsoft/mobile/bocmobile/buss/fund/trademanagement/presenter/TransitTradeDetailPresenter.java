package com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.presenter;

import com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.ui.TransitTradeDetailContract;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

/**
 * Created by wy7105 on 2016/12/1.
 * 在途交易记录详情presenter
 */
public class TransitTradeDetailPresenter extends RxPresenter implements TransitTradeDetailContract.Presenter {

    private TransitTradeDetailContract.View mTransitTradeDetailView;

    public TransitTradeDetailPresenter(TransitTradeDetailContract.View view) {
        mTransitTradeDetailView = view;
    }

}