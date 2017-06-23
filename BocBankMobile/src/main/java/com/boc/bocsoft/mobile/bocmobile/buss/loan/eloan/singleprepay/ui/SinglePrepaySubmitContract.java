package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.singleprepay.ui;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANRepayCount.PsnELOANRepayCountParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANRepayCount.PsnELOANRepayCountResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayerAcountCheck.PsnLOANPayerAcountCheckParams;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model.PrepayAccountDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.QueryAllChinaBankAccountRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.RepaymentAccountCheckRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.singleprepay.model.SinglePrepaySubmitReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.singleprepay.model.SinglePrepaySubmitRes;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * 贷款管理-中银E贷-提前还款接口类
 * Created by liuzc on 2016/9/2.
 */
public class SinglePrepaySubmitContract {
    public interface View extends BaseView<Presenter> {
        //获取会话ID，成功调用
        void obtainConversationSuccess(String conversationId);
        //获取会话ID，失败调用
        void obtainConversationFail(ErrorException e);

        //提前还款根据账户ID查询账户详情成功
        void prepayCheckAccountDetailSuccess(PrepayAccountDetailModel.AccountDetaiListBean result);
        //提前还款根据账户ID查询账户详情失败
        void prepayCheckAccountDetailFail(ErrorException e);
        //计算提前还款手续费成功
        void calcChargesSuccess(PsnELOANRepayCountResult result);
        //计算提前还款手续费失败
        void calcChargesFail(ErrorException e);
        // 获取中行所有帐户列表，成功调用
        void obtainAllChinaBankAccountSuccess(
                List<QueryAllChinaBankAccountRes> res);

        // 获取中行所有帐户列表，失败调用
        void obtainAllChinaBankAccountFail(ErrorException e);
        //还款账户检查，成功调用
        void doRepaymentAccountCheckSuccess(RepaymentAccountCheckRes res);
        //还款账户检查，失败调用
        void doRepaymentAccountCheckFail(ErrorException e);
    }

    public interface Presenter extends BasePresenter {
        //创建页面公共会话
        void creatConversation();
        /**
         * 查询中行所有帐户列表
         * @param mListDataAccountType 账户类型
         */
        void queryAllChinaBankAccount(List<String> mListDataAccountType);
        //提前还款根据账户ID查询账户详情
        void prepayCheckAccountDetail(String accountID);
        //计算提前还款手续费
        void calcCharges(PsnELOANRepayCountParams params);
        //还款账户检查
        void checkRepaymentAccount(PsnLOANPayerAcountCheckParams params);
    }
}
