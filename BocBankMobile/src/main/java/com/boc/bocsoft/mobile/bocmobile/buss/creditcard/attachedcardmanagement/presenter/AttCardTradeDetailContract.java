package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.presenter;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainTranQuery.PsnCrcdAppertainTranQueryResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery.PsnCrcdCurrencyQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.model.AttCardTradeDetailModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Name: liukai
 * Time：2016/12/10 13:22.
 * Created by lk7066 on 2016/12/10.
 * It's used to
 */

public class AttCardTradeDetailContract {

    public interface AttCardTradeDetailView extends BaseView<AttCardTradeDetailPresenter>{

        /*交易明细查询回调*/
        void appertainTranDetailSuccess(PsnCrcdAppertainTranQueryResult psnCrcdAppertainTranQueryResult);
        void appertainTranDetailFailed(BiiResultErrorException exception);

        /* 信用卡币种查询回调 */
        void crcdCurrencyQuerySuccess(PsnCrcdCurrencyQueryResult mCurrencyQuery);
        void crcdCurrencyQueryFailed(BiiResultErrorException exception);

    }

    public interface AttCardTradeDetailPresenter extends BasePresenter{

        /*交易明细查询（首次查询）*/
        void queryAppertainFirstTranDetail(AttCardTradeDetailModel attCardDetailModel);

        /*交易明细查询（上拉加载）*/
        void queryAppertainLoadTranDetail(AttCardTradeDetailModel attCardDetailModel);

        /*主卡币种查询*/
        void queryCrcdCurrency(String params);

    }

}
