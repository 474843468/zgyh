package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.ui;

import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.model.CreditContractRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.model.DistrictRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.model.LoanContractRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.model.PreRegisterVerifyRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;
import java.util.List;

/**
 * 贷款管理-中银E贷-贷款申请接口类
 * Created by xintong on 2016/6/13.
 */
public class ApplyContract {

    //申请页面
    public interface ApplyView extends BaseView<Presenter>{

        //获取会话ID，成功调用
        void obtainConversationSuccess(String conversationId);
        //获取会话ID，失败调用
        void obtainConversationFail(ErrorException e);

        //安全因子，成功调用
        void obtainSecurityFactorSuccess(SecurityFactorModel result);
        //安全因子，失败调用
        void obtainSecurityFactorFail(ErrorException e);

        //获取征信授权协议成功调用
        void obtainCreditContractSuccess(CreditContractRes result);
        //获取征信授权协议失败调用
        void obtainCreditContractFail(ErrorException e);

        //获取贷款合同成功调用
        void obtainLoanContractSuccess(LoanContractRes result);
        //获取贷款合同失败调用
        void obtainLoanContractFail(ErrorException e);

        //额度签约申请预交易成功调用
        void preLoanRegisterVerifySuccess(PreRegisterVerifyRes result);
        //额度签约申请预交易失败调用
        void preLoanRegisterVerifyFail(ErrorException e);

    }

    //申请确认页面
    public interface ApplyConfirmView extends BaseView<Presenter>{

        //额度签约申请预交易成功调用
        void preLoanRegisterVerifySuccess(PreRegisterVerifyRes result);
        //额度签约申请预交易失败调用
        void preLoanRegisterVerifyFail(ErrorException e);

        //获取随机数成功
        void obtainRandomSuccess(String random);
        //获取随机数失败
        void obtainRandomFail(ErrorException e);

        //额度签约申请提交交易成功调用
        void loanRegisterSumbitSuccess();
        //额度签约申请提交交易失败调用
        void loanRegisterSumbitFail(ErrorException e);
    }

    //地区选择页面
    public interface DistrictSelectView extends BaseView<Presenter>{
        //获取地区列表成功调用
        void obtainDistrictSuccess(List<DistrictRes> result);
        //获取地区列表失败调用
        void obtainDistrictFail(ErrorException e);
    }

    public interface Presenter extends BasePresenter {

        //创建页面公共会话
        void creatConversation();

        void getSecurityFactor();

        //获取随机数
        void getRandom();

        //查询地区
        void queryDistrict(String zoneCode);


        //查询征信授权协议
        void queryCreditContract();

        //查询贷款合同
        void queryLoanContract();

        //额度签约申请预交易
        void preLoanRegisterVerify();

        //额度签约申请提交交易
        void loanRegisterSumbit();

    }


}
