package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billinstallments.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.math.BigDecimal;

/**
 * Name: liukai
 * Time：2017/1/3 16:45.
 * Created by lk7066 on 2017/1/3.
 * It's used to
 */

public class BillInstallmentMainContract {

    public interface BillInstallmentMainView extends BaseView<BillInstallmentMainPresenter> {

        void queryBillInputSuccess(BigDecimal upLimit, BigDecimal lowLimit);
        void queryBillInputFailed(BiiResultErrorException e);

    }

    public interface BillInstallmentMainPresenter extends BasePresenter {

        /**
         * 4.29 029办理账单分期输入PsnCrcdDividedPayBillSetInput
         *
         * @param accountID
         */
        void queryBillInput(String accountID);

    }

}
