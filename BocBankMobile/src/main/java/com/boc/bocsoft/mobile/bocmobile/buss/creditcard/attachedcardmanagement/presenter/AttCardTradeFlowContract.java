package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.presenter;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery.PsnCrcdCurrencyQueryResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAppertainAndMess.PsnCrcdQueryAppertainAndMessResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.model.AttCardSetUpModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.model.AttCardTradeFlowModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Name: liukai
 * Time：2016/12/8 21:23.
 * Created by lk7066 on 2016/12/8.
 * It's used to
 */

public class AttCardTradeFlowContract {

    public interface AttCardTradeFlowView extends BaseView<AttCardTradeFlowPresenter>{

        /*短信查询和流量查询回调*/
        void appertainAndMessSuccess(PsnCrcdQueryAppertainAndMessResult psnCrcdQueryAppertainAndMessResult, int flag);
        void appertainAndMessFailed(BiiResultErrorException exception, int flag);

        /*短信查询和流量查询回调*/
        void appertainAndMessSecondSuccess(PsnCrcdQueryAppertainAndMessResult psnCrcdQueryAppertainAndMessResult, int flag);
        void appertainAndMessSecondFailed(BiiResultErrorException exception, int flag);

        /*查询安全因子回调*/
        void querySecurityFactorSuccess(SecurityFactorModel securityFactorModel);
        void querySecurityFactorFailed(BiiResultErrorException exception);

        /*交易流量确认回调*/
        void setAppertainTranConfirmSuccess(VerifyBean verifyBean);
        void setAppertainTranConfirmFailed(BiiResultErrorException exception);

        /* 信用卡币种查询回调 */
        void crcdCurrencyQuerySuccess(PsnCrcdCurrencyQueryResult mCurrencyQuery);
        void crcdCurrencyQueryFailed(BiiResultErrorException exception);

    }

    public interface AttCardTradeFlowPresenter extends BasePresenter{

        /*短信查询和流量查询*/
        void queryAppertainAndMess(AttCardSetUpModel attCardSetUpModel, String currency, int mCurrencyStatus);

        /*短信查询和流量查询*/
        /*如果是多币种需要调用同一支接口两次*/
        void queryAppertainAndMessSecond(AttCardSetUpModel attCardSetUpModel, String currency, int mCurrencyStatus);

        /*查询安全因子*/
        void querySecurityFactor();

        /*交易流量确认*/
        void setAppertainTranConfirm(AttCardTradeFlowModel attCardModel);

        /*主卡币种查询*/
        void queryCrcdCurrency(String params);

    }

}
