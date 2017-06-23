
package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.ui.repayremain;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.model.RepayRemainViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by liuzc on 2016/8/12.
 */
public class RepayRemainContract {

    public interface View extends BaseView<Presenter> {

        /**
         * 查询剩余还款记录列表成功回调
         */
        void queryRepayRemainListSuccess(RepayRemainViewModel repayRemainViewModel);

        /**
         * 查询剩余还款记录列表列表失败回调
         */
        void queryRepayRemainListFail(BiiResultErrorException biiResultErrorException);
        
        /**
         * 非success、fail的其他情况的处理
         */
        void queryOtherReturn();
    }


    public interface Presenter extends BasePresenter {
        /**
         * 查询剩余还款记录
         */
        void queryRepayRemainList(RepayRemainViewModel repayRemainViewModel);

    }

}
