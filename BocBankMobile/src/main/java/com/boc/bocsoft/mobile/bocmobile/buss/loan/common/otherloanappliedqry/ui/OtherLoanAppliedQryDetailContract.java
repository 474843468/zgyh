
package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanappliedqry.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanappliedqry.model.OtherLoanAppliedQryDetailViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by liuzc on 2016/8/16.
 */
public class OtherLoanAppliedQryDetailContract {

    public interface View extends BaseView<Presenter> {

        /**
         * 查询其他类型贷款的申请进度详情成功回调
         */
        void queryOtherLoanOnlineDetailSuccess(OtherLoanAppliedQryDetailViewModel repayRemainViewModel);

        /**
         * 查询其他类型贷款的申请进度详情失败回调
         */
        void queryOtherLoanOnlineDetailFail(BiiResultErrorException biiResultErrorException);
    }


    public interface Presenter extends BasePresenter {
        /**
         * 查询其他类型贷款的申请进度
         */
        void queryOtherLoanOnlineDetail(OtherLoanAppliedQryDetailViewModel viewModel);

    }

}
