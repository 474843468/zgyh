package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadAccountQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadHisTradStatusViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadQueryGuarantyProductListViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadRecentAccountQueryViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;
/**
 * Fragment：中银理财-历史交易-列表页面
 * Created by zc on 2016/9/14
 */
public class WealthHistoryListContract {

    public interface View extends BaseView<Presenter> {
        /**
         * 成功回调：
         * 历史常规交易状况查询
         */
        void psnXpadHisTradStatusSuccess(XpadHisTradStatusViewModel viewModel);

        /**
         * 失败回调：
         * 历史常规交易状况查询
         */
        void psnXpadHisTradStatusFail(BiiResultErrorException biiResultErrorException);

        /**
         * 成功回调：
         * 历史组合交易状况查询
         */
        void psnXpadHisComTradStatusSuccess(XpadQueryGuarantyProductListViewModel viewModel);

        /**
         * 失败回调：
         * 历史组合交易状况查询
         */
        void psnXpadHisComTradStatusFail(BiiResultErrorException biiResultErrorException);

        /**
         * 成功回调：
         * 查询客户理财账户信息
         */
        void psnXpadAccountQuerySuccess(XpadAccountQueryViewModel xpadAccountQueryViewModel);

        /**
         * 失败回调：
         * 查询客户理财账户信息
         */
        void psnXpadAccountQueryFail(BiiResultErrorException biiResultErrorException);

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
         * 历史常规交易状况查询
         */
        void psnXpadHisTradStatus(XpadHisTradStatusViewModel viewModel);

        /**
         * 历史组合交易状况查询
         */
        void psnXpadHisComTradStatus(XpadQueryGuarantyProductListViewModel viewModel);

        /**
         * 查询客户理财账户信息
         */
        void psnXpadAccountQuery(XpadAccountQueryViewModel xpadAccountQueryViewModel);

        /**
         * 查询客户最近操作的理财账户
         */
        void psnXpadRecentAccountQuery(XpadRecentAccountQueryViewModel xpadRecentAccountQueryViewModel);

    }

}
