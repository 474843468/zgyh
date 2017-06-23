
package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.ui.repayhistory;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.model.RepayHistoryViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by liuzc on 2016/8/11.
 */
public class RepayHistoryContract {

    public interface View extends BaseView<Presenter> {

        /**
         * 查询历史还款记录列表成功回调
         */
        void queryRepayHistoryListSuccess(RepayHistoryViewModel repayHistoryViewModel);

        /**
         * 查询历史还款记录列表列表失败回调
         */
        void queryRepayHistoryListFail(BiiResultErrorException biiResultErrorException);
        /**
         * 非success、fail的其他情况的处理
         */
        void queryOtherReturn();

    }


    public interface Presenter extends BasePresenter {
        /**
         * 查询历史还款记录
         */
        void queryRepayHistoryList(RepayHistoryViewModel repayHistoryViewModel);

    }

}
