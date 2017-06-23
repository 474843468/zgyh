package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.TaAccountRegisterModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by lyf7084 on 2016/12/30.
 */
public class TaAccountRegisterConfirmContract {

    public interface View extends BaseView<Presenter> {

        void registerFundTaAccountSuccess();

        void registerFundTaAccountFail(BiiResultErrorException biiResultErrorExceptio);
    }

    public interface Presenter extends BasePresenter {

        /**
         * 请求 010 Ta登记TA账户
         */
        void registerFundTaAccount(TaAccountRegisterModel model);
    }

}