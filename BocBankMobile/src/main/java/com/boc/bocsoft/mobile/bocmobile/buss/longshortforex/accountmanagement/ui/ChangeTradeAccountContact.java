package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGFilterDebitCardViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGGetBindAccountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGSetTradeAccountViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contact：双向宝-账户管理-变更账户（从详情页进入）
 * Created by zhx on 2016/12/20
 */
public class ChangeTradeAccountContact {
    public interface View extends BaseView<Presenter> {
        /**
         * 临时-不一定
         * 成功回调：
         * 双向宝-账户管理-首次/重新设定双向宝账户
         */
        void vFGSetTradeAccountSuccess(VFGSetTradeAccountViewModel vfgSetTradeAccountViewModel);

        /**
         * 临时-不一定
         * 失败回调：
         * 双向宝-账户管理-首次/重新设定双向宝账户
         */
        void vFGSetTradeAccountFail(BiiResultErrorException biiResultErrorException);

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
         * 临时-不一定
         * 双向宝-账户管理-首次/重新设定双向宝账户
         */
        void vFGSetTradeAccount(VFGSetTradeAccountViewModel vfgSetTradeAccountViewModel);

        /**
         * 双向宝-账户管理-查询保证金账户列表
         */
        void vFGBailListQuery(VFGBailListQueryViewModel vfgBailListQueryViewModel);

        /**
         * 双向宝-账户管理-双向宝交易账户
         */
        void vFGGetBindAccount(VFGGetBindAccountViewModel vfgGetBindAccountViewModel);

        /**
         * 双向宝-账户管理-选择账户-过滤出符合条件的借记卡账户
         */
        void vFGFilterDebitCard(VFGFilterDebitCardViewModel viewModel);
    }
}
