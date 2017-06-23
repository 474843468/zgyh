package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundposition.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundposition.model.FundPositionDetailModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by taoyongzhen on 2016/12/8.
 */

public class FundPositionDetailContract {

    public interface FundBonusDetailView extends BaseView {

        /**
         * 修改分红方式成功
         *
         * @param model
         */

        void alterFundBonusSuccess(FundPositionDetailModel model);

        /**
         * 修改分红方式失败
         *
         * @param biiResultErrorException
         */
        void alterFundNightBonusFail(BiiResultErrorException biiResultErrorException);

        /**
         * 修改分红方式（挂单）成功
         *
         * @param model
         */

        void alterFundNightBonusSuccess(FundPositionDetailModel model);

        /**
         * 修改分红方式（挂单）失败
         *
         * @param biiResultErrorException
         */
        void alterFundBonusFail(BiiResultErrorException biiResultErrorException);


    }

    public interface Presenter extends BasePresenter {

        /**
         * 修改分红方式
         *
         * @param params
         */
        void alterFundBonus(FundPositionDetailModel params);

        /**
         * 修改分红方式（挂单）
         *
         * @param params
         */
        void alterFundNightBonus(FundPositionDetailModel params);
    }
}
