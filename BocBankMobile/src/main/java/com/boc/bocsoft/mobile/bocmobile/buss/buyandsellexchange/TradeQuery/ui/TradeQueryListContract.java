package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.TradeQuery.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.TradeQuery.model.TradeQueryListModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 *
 * Created by wzn7074 on 2016/12/14.
 */
public class TradeQueryListContract {
    public interface View extends BaseView<Presenter>{

        /***
         * 成功回调：
         * 获取
         * @param result
         */
        void psnFessQueryHibsExchangeTransSuccess(TradeQueryListModel result);

        void psnFessQueryHibsExchangeTransFail(BiiResultErrorException biiResultErrorException);
    }


    public interface Presenter extends BasePresenter {
        void psnTradeQueryListQuery(TradeQueryListModel model );
    }
}

