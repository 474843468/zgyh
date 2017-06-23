
package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanappliedqry.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanappliedqry.model.OtherLoanAppliedQryListViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by liuzc on 2016/8/15.
 */
public class OtherLoanAppliedQryListContract {

    public interface View extends BaseView<Presenter> {

        /**
         * 查询其他类型贷款的申请进度成功回调
         */
        void queryOtherLoanOnlineListSuccess(OtherLoanAppliedQryListViewModel repayRemainViewModel);

        /**
         * 查询其他类型贷款的申请进度失败回调
         */
        void queryOtherLoanOnlineListFail(BiiResultErrorException biiResultErrorException);
    }


    public interface Presenter extends BasePresenter {
        /**
         * 查询其他类型贷款的申请进度
         */
        void queryOtherLoanOnlineList(OtherLoanAppliedQryListViewModel repayRemainViewModel);

    }

}
