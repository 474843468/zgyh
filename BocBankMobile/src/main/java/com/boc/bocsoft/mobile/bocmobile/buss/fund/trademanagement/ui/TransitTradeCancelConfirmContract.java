package com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.model.PsnFundAppointCancelModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.model.PsnFundConsignAbortModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.presenter.TransitTradeCancelConfirmPresenter;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * Created by wy7105 on 2016/12/1.
 * 交易记录-撤单确认contract
 */
public class TransitTradeCancelConfirmContract {

    public interface View {

        /**
         * 成功回调：
         * 一般撤单
         */
        void psnFundConsignAbortSuccess(PsnFundConsignAbortModel viewModel);

        /**
         * 失败回调：
         * 一般撤单
         */
        void psnFundConsignAbortFail(BiiResultErrorException biiResultErrorException);

        /**
         * 成功回调：
         * 指定日期撤单
         */
        void psnFundAppointCancelSuccess(PsnFundAppointCancelModel viewModel);

        /**
         * 失败回调：
         * 指定日期撤单
         */
        void psnFundAppointCancelFail(BiiResultErrorException biiResultErrorException);


        void setPresenter(TransitTradeCancelConfirmPresenter transitTradeCancelConfirmPresenter);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 一般交易撤单失败
         */
        void psnFundConsignAbort(PsnFundConsignAbortModel model);

        /**
         * 指定日期交易撤单失败
         */
        void psnFundAppointCancel(PsnFundAppointCancelModel model);
    }

}