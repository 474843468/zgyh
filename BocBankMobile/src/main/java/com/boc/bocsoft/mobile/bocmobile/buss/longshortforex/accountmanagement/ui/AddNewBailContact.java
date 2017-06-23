package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailProductQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGGetBindAccountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGSignPreViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contact：双向宝-账户管理-新增保证金账户
 * Created by zhx on 2016/12/8
 */
public class AddNewBailContact {
    public interface View extends BaseView<Presenter> {
        /**
         * 成功回调：
         * 双向宝-账户管理-新增保证金账户-查询可签约保证金产品
         */
        void vFGBailProductQuerySuccess(VFGBailProductQueryViewModel viewModel);

        /**
         * 失败回调：
         * 双向宝-账户管理-新增保证金账户-查询可签约保证金产品
         */
        void vFGBailProductQueryFail(BiiResultErrorException biiResultErrorException);

        /**
         * 成功回调：
         * 双向宝-账户管理-新增保证金账户-双向宝签约预交易
         */
        void vFGSignPreSuccess(VerifyBean verifyBean);

        /**
         * 失败回调：
         * 双向宝-账户管理-新增保证金账户-双向宝签约预交易
         */
        void vFGSignPreFail(BiiResultErrorException biiResultErrorException);

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
         * 双向宝-账户管理-查询保证金账户列表
         */
        void vFGBailListQuerySuccess(VFGBailListQueryViewModel vfgBailListQueryViewModel);

        /**
         * 失败回调：
         * 双向宝-账户管理-查询保证金账户列表
         */
        void vFGBailListQueryFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 双向宝-账户管理-新增保证金账户-查询可签约保证金产品
         */
        void vFGBailProductQuery(VFGBailProductQueryViewModel viewModel);

        /**
         * 双向宝-账户管理-新增保证金账户-双向宝签约预交易
         */
        void vFGSignPre(VFGSignPreViewModel viewModel);

        /**
         * 双向宝-账户管理-双向宝交易账户
         */
        void vFGGetBindAccount(VFGGetBindAccountViewModel vfgGetBindAccountViewModel);

        /**
         * 双向宝-账户管理-查询保证金账户列表
         */
        void vFGBailListQuery(VFGBailListQueryViewModel vfgBailListQueryViewModel);
    }
}
