package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.changeaccount.ui;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANCycleLoanAccountListQuery.PsnCycleLoanAccountEQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANCycleLoanAccountListQuery.PsnCycleLoanAccountEQueryResult;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.changeaccount.model.ChangeAccountVerifyRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.changeaccount.model.ErrorException;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * 贷款管理-循环非循环-变更还款账户接口类
 * Created by liuzc on 2016/8/25.
 */
public class ChangeAccountContract {

    public interface ChangeAccountView extends BaseView<Presenter> {

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

        //变更循环非循环还款账户预交易，成功调用
        void changeAccountOEVerifySuccess(ChangeAccountVerifyRes res);
        //变更循环非循环还款账户预交易，失败调用
        void changeAccountOEVerifyFail(ErrorException e);

        //变更循环非循环还款账户提交交易，成功调用
        void changeAccountOESubmitSuccess();
        //变更循环非循环还款账户提交交易，失败调用
        void changeAccountOESubmitFail(ErrorException e);

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

        //变更循环非循环还款账户预交易
        void changeAccountOEVerify();

        //变更循环非循环还款账户提交交易
        void changeAccountOESubmit();
        //根据账户ID查询账户详情
        void checkAccountDetail(String accountID);
    }
}
