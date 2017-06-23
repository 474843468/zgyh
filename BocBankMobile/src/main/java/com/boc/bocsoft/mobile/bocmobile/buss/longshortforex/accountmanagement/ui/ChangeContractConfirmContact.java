package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGChangeContractViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGSetTradeAccountViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contact：双向宝-账户管理-变更账户确认
 * Created by zhx on 2016/12/14
 */
public class ChangeContractConfirmContact {
    public interface View extends BaseView<Presenter> {
        /**
         * 成功回调：
         * 双向宝-账户管理-变更账户确认
         */
        void vFGChangeContractSuccess(VFGChangeContractViewModel vfgChangeContractViewModel);

        /**
         * 失败回调：
         * 双向宝-账户管理-变更账户确认
         */
        void vFGChangeContractFail(BiiResultErrorException biiResultErrorException);

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
    }

    public interface Presenter extends BasePresenter {
        /**
         * 双向宝-账户管理-变更账户确认
         */
        void vFGChangeContract(VFGChangeContractViewModel vfgChangeContractViewModel);

        /**
         * 临时-不一定
         * 双向宝-账户管理-首次/重新设定双向宝账户
         */
        void vFGSetTradeAccount(VFGSetTradeAccountViewModel vfgSetTradeAccountViewModel);
    }
}
