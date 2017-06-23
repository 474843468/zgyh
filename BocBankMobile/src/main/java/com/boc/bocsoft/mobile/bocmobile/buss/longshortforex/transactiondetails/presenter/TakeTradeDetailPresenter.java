package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.presenter;

import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.ui.TakeTradeDetailContract;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

/**
 * 双向宝交易查询--斩仓交易查询详情界面
 * Created by zc on 2016/11/17
 */
public class TakeTradeDetailPresenter extends RxPresenter implements TakeTradeDetailContract.Presenter {

    private TakeTradeDetailContract.View mTakeTradeDetailView;

    public TakeTradeDetailPresenter(TakeTradeDetailContract.View view) {
        mTakeTradeDetailView = view;
    }

}
