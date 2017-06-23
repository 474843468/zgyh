package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.ui;


import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model.PrepayAccountDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.CollectionAccountCheckRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.QueryAllChinaBankAccountRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.model.LOANCycleLoanEApplyVerifyRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.model.LoanRepaymentPlanRes;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * 贷款管理-中银E贷-提款接口类
 * Created by xintong on 2016/6/16.
 */
public class DrawContract {

    /**
     * 用款界面
     */
    public interface DrawView extends BaseView<Presenter> {

        //获取会话ID，成功调用
        void obtainConversationSuccess(String conversationId);

        //获取会话ID，失败调用
        void obtainConversationFail(ErrorException e);

        //安全因子，成功调用
        void obtainSecurityFactorSuccess(SecurityFactorModel result);

        //安全因子，失败调用
        void obtainSecurityFactorFail(ErrorException e);

        //获取还款计划试算成功调用
        void obtainLoanRepaymentPlanSuccess(LoanRepaymentPlanRes result);

        //获取还款计划试算失败调用
        void obtainLoanRepaymentPlanFail(ErrorException e);

        //中银E贷用款预交易成功调用
        void drawApplyVerifySuccess(LOANCycleLoanEApplyVerifyRes result);

        //中银E贷用款预交易失败调用
        void drawApplyVerifyFail(ErrorException e);

        //获取中行所有帐户列表，成功调用
        void obtainAllChinaBankAccountSuccess(List<QueryAllChinaBankAccountRes> res);

        //获取中行所有帐户列表，失败调用
        void obtainAllChinaBankAccountFail(ErrorException e);
        //2016年10月10日 17:08:17  闫勋add
        //收款账户检查，成功调用
        void doCollectionAccountCheckSuccess(CollectionAccountCheckRes res);
        //收款账户检查，失败调用
        void doCollectionAccountCheckFail(ErrorException e);

    }

    public interface DrawConfirmView extends BaseView<Presenter> {

        //中银E贷用款预交易成功调用
        void drawApplyVerifySuccess(LOANCycleLoanEApplyVerifyRes result);

        //中银E贷用款预交易失败调用
        void drawApplyVerifyFail(ErrorException e);

        //获取随机数成功
        void obtainRandomSuccess(String random);

        //获取随机数失败
        void obtainRandomFail(ErrorException e);

        //中银E贷用款提交交易成功调用
        void drawApplySubmitSuccess();

        //中银E贷用款提交交易失败调用
        void drawApplySubmitFail(ErrorException e);
    }

    /**
     * 结果界面-接口回调方法
     *
     * @author yx
     * @date 2016年10月10日 14:40:28
     */
    public interface DrawOperatingResultsView extends BaseView<Presenter> {
        // 根据账户ID查询账户详情成功
        void prepayCheckAccountDetailSuccess(
                PrepayAccountDetailModel.AccountDetaiListBean result);

        //根据账户ID查询账户详情失败
        void prepayCheckAccountDetailFail(ErrorException e);
    }

    public interface Presenter extends BasePresenter {

        //创建页面公共会话
        void creatConversation();

        //获取安全因子
        void getSecurityFactor();

        //获取随机数
        void getRandom();

        //试算还款计划
        void queryLoanRepaymentPlan();

        //中银E贷用款预交易
        void drawApplyVerify();

        //中银E贷用款提交交易
        void drawApplySubmit();

        //查询中行所有帐户列表
        void queryAllChinaBankAccount();

        // 根据账户ID查询账户详情
        void prepayCheckAccountDetail(String accountID);

        //收款账户检查
        void checkCollectionAccount(QueryAllChinaBankAccountRes queryAllChinaBankAccountRes);

    }

}
