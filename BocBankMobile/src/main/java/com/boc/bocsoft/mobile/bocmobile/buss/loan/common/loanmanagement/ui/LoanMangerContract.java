package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.ui;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepayAccountDetailQuery.PsnDrawingDetailResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.model.LoanAccountListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.model.LoanOverdueModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.model.LoanQuoteViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanQuoteViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * Created huixiaobo on 2016/9/4.
 */
public class LoanMangerContract {

    //贷款管理
    public interface LoanManageView extends BaseView<Presenter> {
        /**045查询签约中银E贷额度列表请求失败*/
        void eloanQuoteFail(BiiResultErrorException biiResultErrorException);
        /**
         * 045查询签约中银E贷额度列表请求成功
         * @param equoteResult
         */
        void eloanQuoteSuccess(List<EloanQuoteViewModel> equoteResult);

        /**052件查询贷款账户列表请求失败*/
        void queryLoanAccountFail(BiiResultErrorException biiResultErrorException);
        /**分条件查询贷款账户列表请求成功*/
        void queryLoanAccountSuccess(LoanAccountListModel eAccountResult);
        /**052已结清贷款失败方法*/
        void queryLoanSettledFail(BiiResultErrorException biiResultErrorException);
        /**052已结清贷款列表成功方法*/
        void queryLoanSettledSuccess(LoanAccountListModel eAccountResult);

        /**074查询逾期信息失败*/
        void querOverdueFail(BiiResultErrorException biiResultErrorException);
        /**074查询逾期信息成功*/
        void queryOverdueSuccess(LoanOverdueModel loanOverdue);

        /**025根据主账户查询对应的借记卡卡号失败*/
        void queryCardNumByAcctNumFail(BiiResultErrorException biiResultErrorException);
        /**025根据主账户查询对应的借记卡卡号成功*/
        void queryCardNumByAcctNumSuccess(String cardNum);
    }

    public interface AccountDetailQuaryView extends BaseView<Presenter> {
        /**007贷款账户详情查询失败*/
        void quaryAccountDetailFail(BiiResultErrorException biiResultErrorException);
        /**007贷款账户详情查询成功*/
        void quaryAccounDetailSuccess(PsnDrawingDetailResult eloanDrawDetailResult);
    }

    public interface Presenter extends BasePresenter {
        /**逾期信息*/
        void queryOverdue();

        /**查询签约中银E贷额度列表*/
        void queryEloanQuote();

        /**分条件查询贷款账户列表*/
        void queryLoanAccount(LoanAccountListModel loanRep);

        /**根据主账户查询对应的借记卡卡号*/
        void queryCardNumByAcctNum(String acctNum);
    }

    public interface AccountDetailQuaryPresenter extends BasePresenter {
        /**根据贷款账号查询账户详情*/
        void queryAccountDetail(String loanAccount);
    }
}
