package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.ui;

import com.boc.bocsoft.mobile.bocmobile.base.model.SecurityViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.model.InstallmentRecordModel;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by yangle on 2016/11/24.
 */
public interface InstallmentDetailContract {

    interface View extends BaseView<Presenter> {

        InstallmentRecordModel getViewModel();

        void showLoading();

        void closeLoading();

        void getSecurityFactorSuccess(SecurityViewModel securityModel);
    }

    interface Presenter {

        void getSecurityFactor();
    }
}
