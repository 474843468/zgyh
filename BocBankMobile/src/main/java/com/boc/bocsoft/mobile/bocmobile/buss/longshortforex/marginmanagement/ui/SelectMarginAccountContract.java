package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailListQueryViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

public class SelectMarginAccountContract {

    public interface View {
        /**
         * 成功回调：
         * 保证金账户列表
         */
        void psnXpadBailListQuerySuccess(VFGBailListQueryViewModel viewModel);

        /**
         * 失败回调：
         * 保证金账户列表
         */
        void psnXpadBailListQueryFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 保证金账户列表
         */
        void psnXpadBailListQuery(VFGBailListQueryViewModel model);
    }

}