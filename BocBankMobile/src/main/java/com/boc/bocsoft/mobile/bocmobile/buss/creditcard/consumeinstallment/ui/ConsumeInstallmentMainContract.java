package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.model.ConsumeTransModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.model.CrcdConsumeQueryModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Name: liukai
 * Time：2017/1/3 15:03.
 * Created by lk7066 on 2017/1/3.
 * It's used to
 */

public class ConsumeInstallmentMainContract{

    public interface ConsumeInstallmentMainView extends BaseView<ConsumeInstallmentMainPresenter> {

        void crcdDividedPayConsumeQrySuccess(ConsumeTransModel result);
        void crcdDividedPayConsumeQryFailed(BiiResultErrorException exception);

    }

    public interface ConsumeInstallmentMainPresenter extends BasePresenter {

        /**
         * 4.29 029办理账单分期输入PsnCrcdDividedPayBillSetInput
         *
         * @param
         */
        void crcdDividedPayConsumeQry(CrcdConsumeQueryModel queryModel);

    }




}
