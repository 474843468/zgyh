
package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.ui.repayoverdue;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.model.RepayOverdueViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.model.RepayRemainViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by liuzc on 2016/8/12.
 */
public class RepayOverdueContract {

    public interface View extends BaseView<Presenter> {

        /**
         * 查询逾期还款记录列表成功回调
         */
        void queryRepayOverdueListSuccess(RepayOverdueViewModel repayOverdueViewModel);

        /**
         * 查询逾期还款记录列表列表失败回调
         */
        void queryRepayOverdueListFail(BiiResultErrorException biiResultErrorException);
        /**
         * 非success、fail的其他情况的处理
         */
        void queryOtherReturn();
    }


    public interface Presenter extends BasePresenter {
        /**
         * 查询逾期还款记录
         */
        void queryRepayOverdueList(RepayOverdueViewModel repayOverdueViewModel);

    }

}
