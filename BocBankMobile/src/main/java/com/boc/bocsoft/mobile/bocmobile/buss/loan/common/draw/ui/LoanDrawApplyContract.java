package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.draw.ui;


import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanMinAmountQuery.PsnLOANCycleLoanMinAmountQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayeeAcountCheck.PsnLOANPayeeAcountCheckParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayeeAcountCheck.PsnLOANPayeeAcountCheckResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayerAcountCheck.PsnLOANPayerAcountCheckParams;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.draw.model.LoanDrawApplyVerifyRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.QueryAllChinaBankAccountRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.RepaymentAccountCheckRes;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * 贷款管理-循环类贷款-提款接口类
 * Created by liuzc on 2016/8/24.
 */
public class LoanDrawApplyContract {

    public interface DrawView extends BaseView<Presenter> {

        //获取会话ID，成功调用
        void obtainConversationSuccess(String conversationId);
        //获取会话ID，失败调用
        void obtainConversationFail(ErrorException e);

        //安全因子，成功调用
        void obtainSecurityFactorSuccess(SecurityFactorModel result);
        //安全因子，失败调用
        void obtainSecurityFactorFail(ErrorException e);

        //用款预交易成功调用
        void drawApplyVerifySuccess(LoanDrawApplyVerifyRes result);
        //用款预交易失败调用
        void drawApplyVerifyFail(ErrorException e);

        //获取中行所有帐户列表，成功调用
        void obtainAllChinaBankAccountSuccess(List<QueryAllChinaBankAccountRes> res);
        //获取中行所有帐户列表，失败调用
        void obtainAllChinaBankAccountFail(ErrorException e);

        //询个人循环贷款最低放款金额成功
        void queryLoanCycleMinAmountSuccess(String reuslt);
        //询个人循环贷款最低放款金额失败
        void queryLoanCycleMinAmountFail(ErrorException e);

        //收款账户检查，成功调用
        void doPayeeAccountCheckSuccess(PsnLOANPayeeAcountCheckResult res);
        //收款账户检查，失败调用
        void doPayeeAccountCheckFail(ErrorException e);
    }

    public interface DrawConfirmView extends BaseView<Presenter> {

        //用款预交易成功调用
        void drawApplyVerifySuccess(LoanDrawApplyVerifyRes result);
        //用款预交易失败调用
        void drawApplyVerifyFail(ErrorException e);

        //获取随机数成功
        void obtainRandomSuccess(String random);
        //获取随机数失败
        void obtainRandomFail(ErrorException e);

        //用款提交交易成功调用
        void drawApplySubmitSuccess();
        //用款提交交易失败调用
        void drawApplySubmitFail(ErrorException e);

        //根据账户ID查询账户详情成功
        void checkAccountDetailSuccess(PsnAccountQueryAccountDetailResult result);
        //根据账户ID查询账户详情失败
        void checkAccountDetailFail(ErrorException e);
    }

    public interface Presenter extends BasePresenter {

        //创建页面公共会话
        void creatConversation();

        //获取安全因子
        void getSecurityFactor();

        //获取随机数
        void getRandom();

        //用款预交易
        void drawApplyVerify();

        //用款提交交易
        void drawApplySubmit();

        //查询中行所有帐户列表
        void queryAllChinaBankAccount();
        //根据账户ID查询账户详情
        void checkAccountDetail(String accountID);

        void queryLoanCycleMinAmount(PsnLOANCycleLoanMinAmountQueryParams params);
        //账户检查
        void checkPayeeAccount(PsnLOANPayeeAcountCheckParams params);

    }

}
