package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGFilterDebitCardViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contact：双向宝-账户管理-保证金账户详情
 * Created by zhx on 2016/12/8
 */
public class ChooseLongshortAccountContact {
    public interface View extends BaseView<Presenter> {
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
         * 双向宝-账户管理-选择账户-过滤出符合条件的借记卡账户
         */
        void vFGFilterDebitCard(VFGFilterDebitCardViewModel viewModel);
    }
}
