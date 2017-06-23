package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.batchprepay.ui.submit;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANBatchRepaySubmit.PsnELOANBatchRepaySubmitParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANBatchRepaySubmit.PsnELOANBatchRepaySubmitResultBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * 贷款管理-中银E贷-提前还款确认接口类
 * Created by liuzc on 2016/9/7.
 */
public class BatchPrepaySubmitConfirmContract {
    public interface View extends BaseView<Presenter> {
        //提前还款提交交易成功调用
        void batchPrepaySubmitSuccess(List<PsnELOANBatchRepaySubmitResultBean> result);
        //提前还款提交交易失败调用
        void batchPrepaySubmitFail(ErrorException e);
    }

    public interface Presenter extends BasePresenter {
        //提前还款提交交易
        void batchPrepaySubmit(PsnELOANBatchRepaySubmitParams params);
    }
}
