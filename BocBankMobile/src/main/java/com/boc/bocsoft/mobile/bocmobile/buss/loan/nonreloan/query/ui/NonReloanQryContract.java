
package com.boc.bocsoft.mobile.bocmobile.buss.loan.nonreloan.query.ui;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepayAccountDetailQuery.PsnDrawingDetailResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by liuzc on 2016/9/28.
 */
public class NonReloanQryContract {

    public interface View extends BaseView<Presenter> {
        /**007用款详情查询失败*/
        void eDrawingDetailFail(BiiResultErrorException biiResultErrorException);
        /**007用款详情查询成功*/
        void eDrawingDetailSuccess(PsnDrawingDetailResult eloanDrawDetailResult);
    }


    public interface Presenter extends BasePresenter {
        /**用款详情查询*/
        void queryDrawingDetail(String loanActNum);

    }

}
