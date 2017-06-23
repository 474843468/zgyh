package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundposition.ui;


import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundposition.model.FundFloatingProfileLossModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundposition.model.FundPositionModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by taoyongzhen on 2016/11/29.
 */

public class FundPositionContract {

    public interface FundPositionView extends BaseView<Presenter> {

        //查询基金持仓信息成功
        void queryFundBalanceSuccess(FundPositionModel result);

        //查询基金持仓信息失败
        void queryFundBalanceFail(BiiResultErrorException biiResultErrorException);

        /**
         * 浮动盈亏失败
         * @param exception
         */
        void queryFloatingProfitLossFail(BiiResultErrorException exception);

        /**
         * 浮动盈亏成功
         * @param model
         */
        void queryFloatingProfitLossSuccess(FundFloatingProfileLossModel model);
    }


    public interface Presenter extends BasePresenter {
        /**
         * 查询基金持仓信息
         *
         * @param params
         */
        void QueryFundBalance(FundPositionModel params);
        /**
         *浮动盈亏
         */
        void queryFloatingProfitLoss(FundFloatingProfileLossModel model);
    }
}
