package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.ui;

import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.model.InstallmentRecordModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.model.SubmitModel;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by yangle on 2016/11/24.
 */
public interface PayAdvanceContract {

    interface View extends BaseView<Presenter> {

        void showLoading();

        void closeLoading();

        void payAdvanceConfirmSuccess(VerifyBean verifyBean, String random);

        void payAdvanceResultSuccess();

    }

    interface Presenter {
        // 提前结清入账 预交易
        void psnCrcdDividedPayAdvanceConfirm(InstallmentRecordModel recordModel);
        // 提前结清入账结果 提交交易
        void psnCrcdDividedPayAdvanceResult(InstallmentRecordModel recordModel, SubmitModel submitModel);
    }

}
