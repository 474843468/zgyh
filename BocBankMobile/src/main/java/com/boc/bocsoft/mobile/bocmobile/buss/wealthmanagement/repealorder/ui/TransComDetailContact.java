package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.model.XpadQueryGuarantyProductViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.model.XpadRemoveGuarantyProductViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contact：中银理财-组合购买详情
 * Created by zhx on 2016/9/20
 */
public class TransComDetailContact {
    public interface View extends BaseView<Presenter> {
        /**
         * 成功回调：
         * 查询客户理财账户信息
         */
        void psnXpadQueryGuarantyProductResultSuccess(XpadQueryGuarantyProductViewModel viewModel);

        /**
         * 失败回调：
         * 查询客户理财账户信息
         */
        void psnXpadQueryGuarantyProductResultFail(BiiResultErrorException biiResultErrorException);

        /**
         * 成功回调：
         * 组合购买解除
         */
        void psnXpadRemoveGuarantyProductResultSuccess(XpadRemoveGuarantyProductViewModel viewModel);

        /**
         * 失败回调：
         * 组合购买解除
         */
        void psnXpadRemoveGuarantyProductResultFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 中银理财-组合购买详情
         */
        void psnXpadQueryGuarantyProductResult(XpadQueryGuarantyProductViewModel viewModel);

        /**
         * 中银理财-组合购买解除
         */
        void psnXpadRemoveGuarantyProductResult(XpadRemoveGuarantyProductViewModel viewModel);
    }

}
