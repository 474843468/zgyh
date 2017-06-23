package com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.presenter;

import com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.ui.HistoryTradeDetailContract;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

/**
 * Created by wy7105 on 2016/11/28.
 * 历史交易记录详情presenter
 */
public class HistoryTradeDetailPresenter extends RxPresenter implements HistoryTradeDetailContract.Presenter {

    private HistoryTradeDetailContract.View mHistoryTradeDetailView;

    public HistoryTradeDetailPresenter(HistoryTradeDetailContract.View view) {
        mHistoryTradeDetailView = view;
    }

}