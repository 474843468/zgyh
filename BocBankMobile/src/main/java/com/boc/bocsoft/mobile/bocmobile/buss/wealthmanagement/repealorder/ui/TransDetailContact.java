package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.model.XpadDelegateCancelViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contact：中银理财-委托交易详情
 * Created by zhx on 2016/9/22
 */
public class TransDetailContact {
    public interface View extends BaseView<Presenter> {
        /**
         * 成功回调：
         * 常规委托交易撤单
         */
        void psnXpadDelegateCancelSuccess(XpadDelegateCancelViewModel viewModel);

        /**
         * 失败回调：
         * 常规委托交易撤单
         */
        void psnXpadDelegateCancelFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 常规委托交易撤单
         */
        void psnXpadDelegateCancel(XpadDelegateCancelViewModel viewModel);
    }
}
