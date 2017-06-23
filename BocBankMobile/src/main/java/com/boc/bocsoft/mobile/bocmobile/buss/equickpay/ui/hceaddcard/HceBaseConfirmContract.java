package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hceaddcard;

import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.HceTransactionViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.SubmitModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * Created by yangle on 2016/12/17.
 * 描述:
 */
public interface HceBaseConfirmContract {

    interface View<R> {

        HceTransactionViewModel getViewModel();

        void showLoading();

        void closeLoading();

        void onGetSecurityFactorSuccess(SecurityFactorModel securityFactorModel);

        void onVerifySuccess(VerifyBean verifyBean, String random);

        void onSubmitSuccess(R submitResult);
    }

    interface Present extends BasePresenter {

        void getSecurityFactor();

        void verify();

        void submit(SubmitModel submitModel);
    }

}
