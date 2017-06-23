package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.singleprepay.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model.PrepayAccountDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.singleprepay.model.SinglePrepaySubmitReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.singleprepay.model.SinglePrepaySubmitRes;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * 贷款管理-中银E贷-提前还款确认接口类
 * Created by liuzc on 2016/9/2.
 */
public class SinglePrepaySubmitConfirmContract {
    public interface View extends BaseView<Presenter> {
        //提前还款提交交易成功调用
        void singlePrepaySubmitSuccess(SinglePrepaySubmitRes result);
        //提前还款提交交易失败调用
        void singlePrepaySubmitFail(ErrorException e);
    }

    public interface Presenter extends BasePresenter {
        //提前还款提交交易
        void prepaySubmit(SinglePrepaySubmitReq params);
    }
}
