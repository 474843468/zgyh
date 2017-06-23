package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.automaticrepayment.presenter;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery.PsnCrcdCurrencyQueryResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdPaymentWaySetup.PsnCrcdPaymentWaySetupResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryCrcdPaymentWay.PsnCrcdQueryCrcdPaymentWayResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.automaticrepayment.model.AutoCrcdPayModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Name: liukai
 * Time：2016/11/25 15:57.
 * Created by lk7066 on 2016/11/25.
 * It's used to
 */

public class AutoCrcdPaymentContract {

    public interface AutoCrcdPaymentView extends BaseView<AutoCrcdPaymentPresenter> {

        /* 还款方式回调 */
        void crcdPaymentWaySuccess(PsnCrcdQueryCrcdPaymentWayResult mPaymentWayResult);
        void crcdPaymentWayFailed(BiiResultErrorException exception);

        /* 信用卡币种查询回调 */
        void crcdCurrencyQuerySuccess(PsnCrcdCurrencyQueryResult mCurrencyQuery);
        void crcdCurrencyQueryFailed(BiiResultErrorException exception);

        /* 信用卡还款方式设定回调 */
        void setCrcdPaymentWaySuccess(PsnCrcdPaymentWaySetupResult mPaymentWaySetResult);
        void setCrcdPaymentWayFailed(BiiResultErrorException exception);

    }

    public interface AutoCrcdPaymentPresenter extends BasePresenter {

        /* 还款方式查询 */
        void queryCrcdPaymentWay(String mParams);//入口参数为accountId，需要修改

        /* 信用卡币种查询 */
        void queryCrcdCurrency(String currencyQueryParams);

        /* 信用卡还款方式设定 */
        void setCrcdPaymentWay(AutoCrcdPayModel autoCrcdPayModel);

    }

}
