package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.FundRegCompanyModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

public class TaAccountRegisterContract {

    public interface View {
        void queryFundCoListSuccess(FundRegCompanyModel result);

        void queryFundCoListFail(BiiResultErrorException biiResultErrorException);

    }

    public interface Presenter extends BasePresenter {
        /**
         * TA账户列表查询请求
         */
        void queryFundCoList();    // 上送参数为空（view-Model封装的params）
    }

}