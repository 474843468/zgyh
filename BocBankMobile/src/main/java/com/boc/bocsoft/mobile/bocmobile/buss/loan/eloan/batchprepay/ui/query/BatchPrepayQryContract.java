
package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.batchprepay.ui.query;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.model.LoanAccountListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.model.RepayRemainViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.batchprepay.model.BatchPrepayQryModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by liuzc on 2016/9/6.
 */
public class BatchPrepayQryContract {

    public interface View extends BaseView<Presenter> {

        /**
         * 查询成功回调
         */
        void queryBatchPrepayListSuccess(BatchPrepayQryModel model);

        /**
         * 查询失败回调
         */
        void queryBatchPrepayListFail(BiiResultErrorException biiResultErrorException);

    }


    public interface Presenter extends BasePresenter {
        /**
         * 查询可提前还款记录
         */
        void queryBatchPrepayList(BatchPrepayQryModel params);

    }

}
