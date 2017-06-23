package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGSignPreViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGSignSubmitViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contact：双向宝-账户管理-新增保证金账户确认
 * Created by zhx on 2016/12/12
 */
public class AddNewBailConfirmContact {
    public interface View extends BaseView<Presenter> {
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
         * 双向宝-账户管理-新增保证金账户-双向宝签约预交易
         */
        void vFGSignSubmitSuccess(VFGSignSubmitViewModel viewModel);

        /**
         * 失败回调：
         * 双向宝-账户管理-新增保证金账户-双向宝签约预交易
         */
        void vFGSignSubmitFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 双向宝-账户管理-新增保证金账户-双向宝签约预交易
         */
        void vFGSignPre(VFGSignPreViewModel viewModel);

        /**
         * 双向宝-账户管理-新增保证金账户-双向宝签约预交易
         */
        void vFGSignSubmit(VFGSignSubmitViewModel viewModel);
    }
}
