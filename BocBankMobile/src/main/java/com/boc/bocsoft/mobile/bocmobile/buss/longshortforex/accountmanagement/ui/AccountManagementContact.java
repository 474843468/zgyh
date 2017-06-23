package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailAccountInfoListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGFilterDebitCardViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGGetBindAccountViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contact：双向宝-账户管理
 * Created by zhx on 2016/11/22
 */
public class AccountManagementContact {
    public interface View extends BaseView<Presenter> {
        /**
         * 成功回调：
         * 双向宝-账户管理-查询保证金账户列表
         */
        void vFGBailListQuerySuccess(VFGBailListQueryViewModel vfgBailListQueryViewModel);

        /**
         * 失败回调：
         * 双向宝-账户管理-查询保证金账户列表
         */
        void vFGBailListQueryFail(BiiResultErrorException biiResultErrorException);

        /**
         * 成功回调：
         * 双向宝-账户管理-双向宝交易账户
         */
        void vFGGetBindAccountSuccess(VFGGetBindAccountViewModel vfgGetBindAccountViewModel);

        /**
         * 失败回调：
         * 双向宝-账户管理-双向宝交易账户
         */
        void vFGGetBindAccountFail(BiiResultErrorException biiResultErrorException);

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

        /**
         * 成功回调：
         * 双向宝-账户管理-选择账户-过滤出符合条件的借记卡账户
         */
        void vFGFilterDebitCardSuccess(VFGFilterDebitCardViewModel viewModel);

        /**
         * 失败回调：
         * 双向宝-账户管理-选择账户-过滤出符合条件的借记卡账户
         */
        void vFGFilterDebitCardFail(BiiResultErrorException biiResultErrorException);

    }

    public interface Presenter extends BasePresenter {
        /**
         * 双向宝-账户管理-查询保证金账户列表
         */
        void vFGBailListQuery(VFGBailListQueryViewModel vfgBailListQueryViewModel);

        /**
         * 双向宝-账户管理-双向宝交易账户
         */
        void vFGGetBindAccount(VFGGetBindAccountViewModel vfgGetBindAccountViewModel);

        /**
         * 双向宝-账户管理-双向宝保证金账户基本信息多笔查询
         */
        void vFGBailAccountInfoListQuery(VFGBailAccountInfoListQueryViewModel viewModel);
        /**
         * 双向宝-账户管理-选择账户-过滤出符合条件的借记卡账户
         */
        void vFGFilterDebitCard(VFGFilterDebitCardViewModel viewModel);
    }
}
