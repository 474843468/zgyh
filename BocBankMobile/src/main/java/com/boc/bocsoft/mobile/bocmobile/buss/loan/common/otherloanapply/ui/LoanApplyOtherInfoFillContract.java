package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.OnLineLoanFieldBean;

public class LoanApplyOtherInfoFillContract {
    public interface View {
        void onLoadOnLineLoanFieldSuccess(OnLineLoanFieldBean bean);
    }

    public interface Presenter {
        /**
         * 查询贷款产品申请栏位
         */
        void getOnLineLoanFieldQry(String productCode);

    }
}