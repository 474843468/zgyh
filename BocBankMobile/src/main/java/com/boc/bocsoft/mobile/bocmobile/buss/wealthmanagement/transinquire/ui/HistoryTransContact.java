package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadHisTradStatusViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadRecentAccountQueryViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contact：中银理财-交易查询-历史交易页面
 * Created by zhx on 2016/9/8
 */
public class HistoryTransContact {
    public interface View extends BaseView<Presenter> {
        /**
         * 成功回调：
         * 中银理财-交易查询-历史交易（历史常规交易状况查询）
         */
        void psnXpadHisTradStatusSuccess(XpadHisTradStatusViewModel xpadHisTradStatusViewModel);

        /**
         * 失败回调：
         * 中银理财-交易查询-历史交易（历史常规交易状况查询）
         */
        void psnXpadHisTradStatusFail(BiiResultErrorException biiResultErrorException);

        /**
         * 成功回调：
         * 查询客户最近操作的理财账户
         */
        void psnXpadRecentAccountQuerySuccess(XpadRecentAccountQueryViewModel xpadRecentAccountQueryViewModel);

        /**
         * 失败回调：
         * 查询客户最近操作的理财账户
         */
        void psnXpadRecentAccountQueryFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 中银理财-交易查询-历史交易（历史常规交易状况查询）
         */
        void psnXpadHisTradStatus(XpadHisTradStatusViewModel xpadHisTradStatusViewModel);

        /**
         * 查询客户最近操作的理财账户（TODO　待移动到其他位置）
         */
        void psnXpadRecentAccountQuery(XpadRecentAccountQueryViewModel xpadRecentAccountQueryViewModel);
    }
}
