package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.ui;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQueryAllChinaBankAccount.PsnCommonQueryAllChinaBankAccountResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeLoanERepayAccountVerify.PsnLOANChangeLoanERepayAccountVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayeeAcountCheck.PsnLOANPayeeAcountCheckResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayerAcountCheck.PsnLOANPayerAcountCheckResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.model.AccountListItemViewModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model.PrepayAccountDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ChangeAccountVerifyRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.CollectionAccountCheckRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.QueryAllChinaBankAccountRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.RepaymentAccountCheckRes;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * 贷款管理-中银E贷-变更还款账户接口类
 * Created by xintong on 2016/6/23.
 */
public class ChangeAccountContract {

    public interface changeAccountView extends BaseView<Presenter> {

        //获取会话ID，成功调用
        void obtainConversationSuccess(String conversationId);
        //获取会话ID，失败调用
        void obtainConversationFail(ErrorException e);

        //安全因子，成功调用
        void obtainSecurityFactorSuccess(SecurityFactorModel result);
        //安全因子，失败调用
        void obtainSecurityFactorFail(ErrorException e);

        //获取随机数成功
        void obtainRandomSuccess(String random);
        //获取随机数失败
        void obtainRandomFail(ErrorException e);

        //变更中E贷还款账户预交易，成功调用
        void changeAccountVerifySuccess(ChangeAccountVerifyRes res);
        //变更中E贷还款账户预交易，失败调用
        void changeAccountVerifyFail(ErrorException e);

        //变更中E贷还款账户提交交易，成功调用
        void changeAccountSubmitSuccess();
        //变更中E贷还款账户提交交易，失败调用
        void changeAccountSubmitFail(ErrorException e);

//        //变更循环非循环还款账户预交易，成功调用
//        void changeAccountOEVerifySuccess(ChangeAccountVerifyRes res);
//        //变更循环非循环还款账户预交易，失败调用
//        void changeAccountOEVerifyFail(ErrorException e);

//        //变更循环非循环还款账户提交交易，成功调用
//        void changeAccountOESubmitSuccess();
//        //变更循环非循环还款账户提交交易，失败调用
//        void changeAccountOESubmitFail(ErrorException e);

    }

    public interface AccountListView extends BaseView<Presenter> {

//        void onLoadAmountError(int position, String message);
//
//        void onLoadAmountSuccess(int position,
//                                 List<AccountListItemViewModel.CardAmountViewModel> list);

        //还款账户检查，成功调用
        void doRepaymentAccountCheckSuccess(RepaymentAccountCheckRes res);
        //还款账户检查，失败调用
        void doRepaymentAccountCheckFail(ErrorException e);

        //收款账户检查，成功调用
        void doCollectionAccountCheckSuccess(CollectionAccountCheckRes res);
        //收款账户检查，失败调用
        void doCollectionAccountCheckFail(ErrorException e);

        //获取中行所有帐户列表，成功调用
        void obtainAllChinaBankAccountSuccess(List<QueryAllChinaBankAccountRes> res);
        //获取中行所有帐户列表，失败调用
        void obtainAllChinaBankAccountFail(ErrorException e);

        //提前还款根据账户ID查询账户详情成功
        void prepayCheckAccountDetailSuccess(PrepayAccountDetailModel.AccountDetaiListBean result);
        //提前还款根据账户ID查询账户详情失败
        void prepayCheckAccountDetailFail(ErrorException e);


    }



    public interface Presenter extends BasePresenter {

        //创建页面公共会话
        void creatConversation();

//        void requestAmount(int item, AccountListItemViewModel position);

        //获取安全因子
        void getSecurityFactor();

        //获取随机数
        void getRandom();

        //还款账户检查
        void checkRepaymentAccount();

        //收款账户检查
        void checkCollectionAccount();

        //查询中行所有帐户列表
        void queryAllChinaBankAccount();

        //变更中E贷还款账户预交易
        void changeAccountVerify();

        //变变更中E贷还款账户提交交易
        void changeAccountSubmit();

        //提前还款根据账户ID查询账户详情
        void prepayCheckAccountDetail(String accountID);

//        //变更循环非循环还款账户预交易
//        void changeAccountOEVerify();

//        //变更循环非循环还款账户提交交易
//        void changeAccountOESubmit();

    }
}
