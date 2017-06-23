package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.ui;

import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadTransInfoDetailQuery.PsnXpadTransInfoDetailQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.TransInfoDetailViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * * Fragment：中银理财-历史交易-交易详情
 * Created by zc on 2016/9/12
 *
 */
public class FinanceHistoryDetailsContract {

    public interface View {
        /**
         * 成功回调：
         * 历史常规交易详情
         */
        void psnPsnXpadTransInfoDetailQuerySuccess(PsnXpadTransInfoDetailQueryResult psnXpadTransInfoDetailQueryResult);

        /**
         * 失败回调：
         * 历史常规交易状况查询
         */
        void psnPsnXpadTransInfoDetailQueryFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 历史常规交易状况查询
         */
        void psnPsnXpadTransInfoDetailQuery(TransInfoDetailViewModel viewModel);

    }

}
