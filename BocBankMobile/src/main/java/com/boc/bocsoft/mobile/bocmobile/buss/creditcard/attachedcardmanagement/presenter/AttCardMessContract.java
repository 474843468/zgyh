package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.presenter;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainMessSetResult.PsnCrcdAppertainMessSetResultResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery.PsnCrcdCurrencyQueryResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAppertainAndMess.PsnCrcdQueryAppertainAndMessResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.model.AttCardMessModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.model.AttCardSetUpModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Name: liukai
 * Time：2016/12/8 21:20.
 * Created by lk7066 on 2016/12/8.
 * It's used to
 */

public class AttCardMessContract {

    public interface AttCardMessView extends BaseView<AttCardMessPresenter>{

        /*短信查询和流量查询回调*/
        void appertainAndMessSuccess(PsnCrcdQueryAppertainAndMessResult psnCrcdQueryAppertainAndMessResult);
        void appertainAndMessFailed(BiiResultErrorException exception);

        /*交易短信设置回调*/
        void setAppertainMessResultSuccess(PsnCrcdAppertainMessSetResultResult psnCrcdAppertainMessSetResultResult);
        void setAppertainMessResultFailed(BiiResultErrorException exception);

        /* 信用卡币种查询回调 */
        void crcdCurrencyQuerySuccess(PsnCrcdCurrencyQueryResult mCurrencyQuery);
        void crcdCurrencyQueryFailed(BiiResultErrorException exception);

    }

    public interface AttCardMessPresenter extends BasePresenter{

        /*短信查询和流量查询*/
        void queryAppertainAndMess(AttCardSetUpModel attCardSetUpModel);

        /*交易短信设置*/
        void setAppertainMessResult(AttCardMessModel attCardModel);

        /*主卡币种查询*/
        void queryCrcdCurrency(String params);

    }

}
