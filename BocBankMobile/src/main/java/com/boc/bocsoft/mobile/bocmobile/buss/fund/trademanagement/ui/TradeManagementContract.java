package com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.model.PsnFundQueryHistoryDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.model.PsnFundQueryTransOntranModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.presenter.TradeManagementPresenter;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * Created by wy7105 on 2016/12/1.
 * 在途交易contract
 */
public class TradeManagementContract {

    public interface View {
        /**
         * 成功回调：
         * 在途交易查询
         */
        void psnFundQueryTransOntranSuccess(PsnFundQueryTransOntranModel viewModel);

        /**
         * 失败回调：
         * 在途交易查询
         */
        void psnFundQueryTransOntranFail(BiiResultErrorException biiResultErrorException);

        /**
         * 成功回调：
         * 历史交易查询
         */
        void psnFundQueryHistoryDetailSuccess(PsnFundQueryHistoryDetailModel viewModel);

        /**
         * 失败回调：
         * 历史交易查询
         */
        void psnFundQueryHistoryDetailFail(BiiResultErrorException biiResultErrorException);


        void setPresenter(TradeManagementPresenter tradeManagementPresenter);
    }

    public interface Presenter extends BasePresenter {

        void psnFundQueryTransOntran(final PsnFundQueryTransOntranModel model);

        void psnFundQueryHistoryDetail(final PsnFundQueryHistoryDetailModel model);

    }

}