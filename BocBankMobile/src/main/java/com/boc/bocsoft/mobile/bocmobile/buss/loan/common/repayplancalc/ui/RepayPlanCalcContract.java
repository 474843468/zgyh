
package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplancalc.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.model.RepayRemainViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplancalc.model.RepayPlanCalcReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplancalc.model.RepayPlanCalcRes;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**还款计划试算
 * Created by liuzc on 2016/8/20.
 */
public class RepayPlanCalcContract {

    public interface View extends BaseView<Presenter> {

        /**
         * 查询成功回调
         */
        void queryRepayPlanCalcSuccess(RepayPlanCalcRes result);

        /**
         * 查询失败回调
         */
        void queryRepayPlanCalcFail(BiiResultErrorException biiResultErrorException);
    }


    public interface Presenter extends BasePresenter {
        /**
         * 还款计划试算 查询
         */
        void queryRepayPlanCalc(RepayPlanCalcReq reqModel);

    }

}
