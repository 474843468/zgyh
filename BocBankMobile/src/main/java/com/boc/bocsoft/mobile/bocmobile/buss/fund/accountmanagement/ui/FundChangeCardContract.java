package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.ChangeCardReqModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * 基金-账户管理-基金交易账户-变更资金帐户页面
 * Created by lyf7084 on 2016/12/08.
 */

public class FundChangeCardContract {

    public interface View {
        void queryChangeCardSuccess();

        void queryChangeCardFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {

        /**
         * 请求049接口 变更资金帐户
         */
        void queryChangeCard(ChangeCardReqModel request);
    }
}
