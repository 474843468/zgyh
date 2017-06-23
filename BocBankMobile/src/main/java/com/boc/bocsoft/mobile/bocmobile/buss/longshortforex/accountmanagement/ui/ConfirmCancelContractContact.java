package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailTransferViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGCancelContractViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGQueryMaxAmountViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contact：双向宝-账户管理-保证金账户详情
 * Created by zhx on 2016/11/23
 */
public class ConfirmCancelContractContact {
    public interface View extends BaseView<Presenter> {
        /**
         * 成功回调：
         * 双向宝-账户管理-解约确认页面-双向宝解约
         */
        void vFGCancelContractSuccess(VFGCancelContractViewModel vfgCancelContractViewModel);

        /**
         * 失败回调：
         * 双向宝-账户管理-解约确认页面-双向宝解约
         */
        void vFGCancelContractFail(BiiResultErrorException biiResultErrorException);

        /**
         * 成功回调：
         * 双向宝-账户管理-解约确认页面-保证金账户可转出最大金额查询
         */
        void vFGQueryMaxAmountSuccess(VFGQueryMaxAmountViewModel vfgQueryMaxAmountViewModel);

        /**
         * 失败回调：
         * 双向宝-账户管理-解约确认页面-保证金账户可转出最大金额查询
         */
        void vFGQueryMaxAmountFail(BiiResultErrorException biiResultErrorException);

        /**
         * 成功回调：
         * 双向宝-账户管理-解约确认页面-保证金存入/转出
         */
        void vFGBailTransferSuccess(VFGBailTransferViewModel vfgBailTransferViewModel);

        /**
         * 失败回调：
         * 双向宝-账户管理-解约确认页面-保证金存入/转出
         */
        void vFGBailTransferFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 双向宝-账户管理-解约确认页面-双向宝解约
         */
        void vFGCancelContract(VFGCancelContractViewModel vfgCancelContractViewModel);

        /**
         * 双向宝-账户管理-解约确认页面-保证金账户可转出最大金额查询
         */
        void vFGQueryMaxAmount(VFGQueryMaxAmountViewModel vfgQueryMaxAmountViewModel);

        /**
         * 双向宝-账户管理-解约确认页面-保证金存入/转出
         */
        void vFGBailTransfer(VFGBailTransferViewModel vfgBailTransferViewModel);
    }
}
