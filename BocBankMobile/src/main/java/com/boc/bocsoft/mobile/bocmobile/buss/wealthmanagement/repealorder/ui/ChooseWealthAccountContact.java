package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadAccountQueryViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contact：中银理财-选择理财帐号页面
 * Created by zhx on 2016/9/20
 */
public class ChooseWealthAccountContact {
    public interface View extends BaseView<Presenter> {
        /**
         * 成功回调：
         * 查询客户理财账户信息
         */
        void psnXpadAccountQuerySuccess(XpadAccountQueryViewModel xpadAccountQueryViewModel);

        /**
         * 失败回调：
         * 查询客户理财账户信息
         */
        void psnXpadAccountQueryFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 查询客户理财账户信息
         */

        void psnXpadAccountQuery(XpadAccountQueryViewModel xpadAccountQueryViewModel);
    }
}
