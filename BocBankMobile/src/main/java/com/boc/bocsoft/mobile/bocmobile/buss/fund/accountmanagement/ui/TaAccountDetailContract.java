package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.TaAccountCancelReqModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.TaAccountCancelResModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * 基金-账户管理-TA账户详情页
 * Created by lyf7084 on 2016/12/14.
 */

public class TaAccountDetailContract {

    public interface View extends BaseView<Presenter> {

        void onTaAccountCancelSuccess(TaAccountCancelResModel result);

        void onTaAccountCancelFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {

        /**
         * 请求 045Ta账户取消关联/销户接口
         */
        void taAccountCancel(TaAccountCancelReqModel params);
    }

}