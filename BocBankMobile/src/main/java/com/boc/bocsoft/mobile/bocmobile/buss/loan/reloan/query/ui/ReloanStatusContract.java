
package com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.ui;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepayAccountDetailQuery.PsnDrawingDetailResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanMinAmountQuery.PsnLOANCycleLoanMinAmountQueryParams;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanDrawDetailModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by liuzc on 2016/9/28.
 */
public class ReloanStatusContract {

    public interface View extends BaseView<Presenter> {
        /**007用款详情查询失败*/
        void eDrawingDetailFail(BiiResultErrorException biiResultErrorException);
        /**007用款详情查询成功*/
        void eDrawingDetailSuccess(PsnDrawingDetailResult eloanDrawDetailResult);
        //询个人循环贷款最低放款金额成功
        void queryLoanCycleMinAmountSuccess(String reuslt);
        //询个人循环贷款最低放款金额失败
        void queryLoanCycleMinAmountFail(ErrorException e);
    }


    public interface Presenter extends BasePresenter {
        /**用款详情查询*/
        void queryDrawingDetail(String loanActNum);

        /**查看用款最小放款金额*/
        void queryLoanCycleMinAmount(PsnLOANCycleLoanMinAmountQueryParams params);
    }

}
