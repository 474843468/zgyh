package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.ui;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGQueryMaxAmountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.model.XpadVFGGetBindAccountViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

public class MarginManagementContract {

    public interface View extends BaseView<Presenter> {

        /**
         * 005 双向宝交易账户查询成功返回
         * @param xpadVFGGetBindAccountViewModel
         */
        void psnVFGGetBindAccountSuccess(XpadVFGGetBindAccountViewModel xpadVFGGetBindAccountViewModel);

        /**
         * 005 双向宝交易账户查询失败
         * @param billResultErrorException
         */
        void psnVFGGetBindAccountFail(BiiResultErrorException billResultErrorException);

        /**
         * 账户余额查询成功返回
         * @param psnAccountQueryAccountDetailResult
         */
        void psnAccountQueryAccountSuccess(PsnAccountQueryAccountDetailResult psnAccountQueryAccountDetailResult);

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

        /**
         * 保证金钞余额成功
         */
        void psnVFGQueryMaxCashAmountSuccess(VFGQueryMaxAmountViewModel viewModel);

        /**
         * 保证金钞余额失败
         */
        void psnVFGQueryMaxCashAmountFail(BiiResultErrorException biiResultErrorException);
        /**
         * 保证金汇余额成功
         */
        void psnVFGQueryMaxSpotAmountSuccess(VFGQueryMaxAmountViewModel viewModel);

        /**
         * 保证金汇余额失败
         */
        void psnVFGQueryMaxSpotAmountFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {

        /**
         * 交易账户
         * @param xpadVFGGetBindAccountViewModel
         */
        void psnVFGGetBindAccount(XpadVFGGetBindAccountViewModel xpadVFGGetBindAccountViewModel);

        /**
         * 保证金账户列表
         */
        void psnXpadBailListQuery(VFGBailListQueryViewModel model);
        /**
         * 资金账户余额
         */
        void psnAccountQueryAccountDetail(String accountId);

        /**
         * 保证金余额查询
         */
        void psnVFGQueryMaxAmount(VFGQueryMaxAmountViewModel model);
    }

}