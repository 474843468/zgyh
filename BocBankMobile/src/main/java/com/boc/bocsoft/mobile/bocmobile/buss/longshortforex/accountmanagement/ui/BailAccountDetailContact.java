package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailAccountInfoListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailDetailQueryViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contact：双向宝-账户管理-保证金账户详情
 * Created by zhx on 2016/11/23
 */
public class BailAccountDetailContact {
    public interface View extends BaseView<Presenter> {
        /**
         * 成功回调：
         * 双向宝-账户管理-保证金账户详情-保证金账户详情
         */
        void vFGBailDetailQuerySuccess(VFGBailDetailQueryViewModel vfgBailDetailQueryViewModel);

        /**
         * 失败回调：
         * 双向宝-账户管理-保证金账户详情-保证金账户详情
         */
        void vFGBailDetailQueryFail(BiiResultErrorException biiResultErrorException);

        /**
         * 成功回调：
         * 双向宝-账户管理-双向宝保证金账户基本信息多笔查询
         */
        void vFGBailAccountInfoListQuerySuccess(VFGBailAccountInfoListQueryViewModel viewModel);

        /**
         * 失败回调：
         * 双向宝-账户管理-双向宝保证金账户基本信息多笔查询
         */
        void vFGBailAccountInfoListQueryFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 双向宝-账户管理-保证金账户详情-保证金账户详情
         */
        void vFGBailDetailQuery(VFGBailDetailQueryViewModel vfgBailDetailQueryViewModel);

        /**
         * 双向宝-账户管理-双向宝保证金账户基本信息多笔查询
         */
        void vFGBailAccountInfoListQuery(VFGBailAccountInfoListQueryViewModel viewModel);
    }
}
