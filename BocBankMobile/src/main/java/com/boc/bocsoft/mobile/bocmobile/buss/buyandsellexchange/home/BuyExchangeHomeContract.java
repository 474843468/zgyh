package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.home;

import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryQuotePrice.PsnFessQueryQuotePriceResult;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnGetExchangeOutlay.PsnGetExchangeOutlayResult;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.model.BuyAndSellExcHomeModel;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.home.model.PriceModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * Created by gwluo on 2016/11/30.
 */

public class BuyExchangeHomeContract {
    public interface View extends BaseView<Presenter> {
        /**
         * 牌价
         */
        void getDataSucc(List<PriceModel> list);

        void getDataFail();

    }

    public interface Presenter extends BasePresenter {
        /**
         * 获取购汇数据，账户、余额、结售汇额度
         */
//        void getBuyExchangeData();
    }
}
