package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.TradeQuery.ui;

import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryForErrormesg.PsnFessQueryForErrormesgResult;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryHibsExchangeTransDetail.PsnFessQueryHibsExchangeTransDetailResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.TradeQuery.model.TradeQueryTransDetailModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

;

/**
 *
 * Created by wzn7074 on 2016/12/1.
 */

public interface TradeQueryDetailContract {

    interface View extends BaseView<Presenter> {

        void showLoading();

        void closeLoading();

        void psnFessQueryHibsExchangeTransDetailFail(BiiResultErrorException biiResultErrorException);

        void psnFessQueryHibsExchangeTransDetailSuccess(PsnFessQueryHibsExchangeTransDetailResult result);

        void psnFessQueryForErrormesgFail(BiiResultErrorException biiResultErrorException);

        void psnFessQueryForErrormesgSuccess(PsnFessQueryForErrormesgResult result);
    }

    interface Presenter extends BasePresenter {

        /**
         加载交易详情数据
         */
        void queryTransDetail(TradeQueryTransDetailModel model);

        void queryTranRetMesg(TradeQueryTransDetailModel model);
    }
}

