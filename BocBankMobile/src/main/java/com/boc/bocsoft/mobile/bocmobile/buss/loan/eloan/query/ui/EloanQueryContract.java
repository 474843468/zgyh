package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui;



import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCommonQueryOprLoginInfo.PsnQueryOprLoginInfoResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANCycleLoanAccountListQuery.PsnCycleLoanAccountEQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANHistoryQuery.PsnEHistoryQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANOverdueQuery.PsnEOverdueQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANQuoteDetailQuery.PsnEQuoteDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANRemainQuery.PsnERemainQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELoanCredit.PsnLoanCreditResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanAccountListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanCreditViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanDrawDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanDrawRecordModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanOverdueModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanQuoteViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanRepaymentModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.UserCycleLoanModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * Created by huixiaobo on 2016/6/16.
 * 中银E贷接口回调类
 */
public class EloanQueryContract {

    /**中银E贷状态判断view*/
    public interface View extends BaseView<Presenter> {

        /**查询个人循环贷款请求失败 052接口*/
        void eCycleLoanFail(BiiResultErrorException biiResultErrorException);
        /**查询个人循环贷款请求成功*/
        void eCycleLoanSuccess(EloanAccountListModel eAccountResult);

        /**查询授信额度请求失败*/
        void eCreditFail(BiiResultErrorException biiResultErrorException);
        /**查询授信额度请求成功*/
        void eCreditSuccess(EloanCreditViewModel ecreditResult);

    }

    public interface DrawView extends BaseView<Presenter> {
        /**查询已结清记录列表失败*/
        void eLoanSettleFail(BiiResultErrorException biiResultErrorException);
        /**查询已结清记录列表成功*/
        void eLoanSettleSuccess(EloanAccountListModel eAccountResul);

        /**查询未还款记录列表*/
        void eLoanRepayFail(BiiResultErrorException biiResultErrorException);
        /**查询未还款记录列表*/
        void eLoanRpaySusccess(EloanAccountListModel eAccountResult);
    }

    /**
     * 分条件查询用款记录
     *
     */
    public interface DrawRecordView extends BaseView<Presenter> {
        /**分条件查询贷款账户列表请求失败*/
        void eRepaymentFail(BiiResultErrorException biiResultErrorException);
        /**分条件查询贷款账户列表请求成功*/
        void eRepaymentSuccess(EloanAccountListModel eAccountResult);
    }

    /**007中银E贷详情view*/
    public interface RepayNumView extends BaseView<Presenter> {
        /**007用款详情查询失败*/
        void eDrawingDetailFail(BiiResultErrorException biiResultErrorException);
        /**007用款详情查询成功*/
        void eDrawingDetailSuccess(EloanDrawDetailModel eloanDrawDetailResult);

        /**003查询逾期信息失败*/
        void eOverdueQueryFail(BiiResultErrorException biiResultErrorException);
        /**003查询逾期信息成功*/
        void eOverdueQuerySuccess(EloanOverdueModel overdueModel);

        /**018用款查询失败*/
        void eDrawRecordFail(BiiResultErrorException biiResultErrorException);
        /**018用款查询成功*/
        void eDrawRecordSuccess(List<EloanDrawRecordModel> drawRecord);
    }

    public interface Presenter extends BasePresenter {

        /**查询个人循环贷款请求*/
        void queryCycleLoan();
        /**查询授信额度*/
        void queryCredit();
        /**分条件查询贷款账户列表*/
        void queryLoanAccount(EloanAccountListModel alp);
        /**查询逾期信息*/
        void queryOverdue();
        /**用款详情查询*/
        void queryDrawingDetail(String loanActNum);
        /**贷款用途查询*/
        void queryDraw(String actNum, String date);

    }

}
