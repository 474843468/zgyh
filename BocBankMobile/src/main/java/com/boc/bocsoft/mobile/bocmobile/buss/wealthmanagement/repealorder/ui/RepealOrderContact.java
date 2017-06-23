package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadAccountQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadRecentAccountQueryViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contact：中银理财-撤单页面
 * Created by zhx on 2016/9/26
 */
public class RepealOrderContact {
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

        /**
         * 成功回调：
         * 查询客户最近操作的理财账户
         */
        void psnXpadRecentAccountQuerySuccess(XpadRecentAccountQueryViewModel xpadRecentAccountQueryViewModel);

        /**
         * 失败回调：
         * 查询客户最近操作的理财账户
         */
        void psnXpadRecentAccountQueryFail(BiiResultErrorException biiResultErrorException);


    }

    public interface Presenter extends BasePresenter {
        /**
         * 查询客户理财账户信息
         */

        void psnXpadAccountQuery(XpadAccountQueryViewModel xpadAccountQueryViewModel);
        /**
         * 查询客户最近操作的理财账户
         */
        void psnXpadRecentAccountQuery(XpadRecentAccountQueryViewModel xpadRecentAccountQueryViewModel);
    }
}
