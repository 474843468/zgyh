package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.presenter;

import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.model.AccountInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.model.CrcdAccountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.model.CrcdBillInfoBean;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.model.RepaymentInfoBean;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.model.TransCommissionChargeBean;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.math.BigDecimal;
import java.util.List;

/**
 * 作者：xwg on 16/11/22 13:54
 */
public class RepaymentContract {

    public interface RepaymentMainView extends BaseView<BasePresenter> {

        /**
         *  查询实时账单，获取应还账单种类
         */
        void queryCrcdRTBillSuccess(List<CrcdBillInfoBean> beanList);

        /**
         *  查询还款方式
         */
        void queryCrcdPayway(String localPayway,String foreignPayway);

        /**
         *  全球交易人民币记账功能查询
         *  如果非单外币信用卡，且开通了2个账户的信用卡
         *  只有双币信用卡才能调用此接口进行查询
         *  如果该信用卡开通该功能，只能进行人民币账单还款，不能进行外币账单还款；
         */
         void crcdChargeOnRMBAccountQuery(boolean openFlag,String displayNum) ;

    }
    public interface RepaymentView extends BaseView<BasePresenter> {

        /**
         *  获取普通卡实时账单
         */
        void queryNormalAccountDetails(List<AccountInfoViewModel> viewModelList);
        /**
         *  获取信用卡实时账单
         */
        void queryCrcdAccountDetails(List<CrcdAccountViewModel> crcdAccountViewModels);

        /**
         * 还款信息校验
         * @param crcdInvalidDate  卡片失效日期
         * @param crcdOverdueFlag   卡片是否过期 “1”-过期，“0”-未过期
         * @param crcdState 卡片状态 “”-正常，“ACTP”待激活
         */
        void checkPaymentInfoSucc(String crcdInvalidDate,String crcdOverdueFlag,String crcdState, RepaymentInfoBean repaymentInfoBean);
        /**
        *  费用试算结果
        */
        void transferCommissionChargeResult(TransCommissionChargeBean transCommissionChargeBean);
    }


    public interface ForeignRepaymentView extends  BaseView<BasePresenter>{
        /**
         *  获取普通卡实时账单
         */
        void queryNormalAccountDetails(List<AccountInfoViewModel> viewModelList);
        /**
         *  获取信用卡实时账单
         */
        void queryCrcdAccountDetails(List<CrcdAccountViewModel> crcdAccountViewModels);

        /**
         * 还款信息校验
         * @param crcdInvalidDate  卡片失效日期
         * @param crcdOverdueFlag   卡片是否过期 “1”-过期，“0”-未过期
         * @param crcdState 卡片状态 “”-正常，“ACTP”待激活
         */
        void checkPaymentInfoSucc(String crcdInvalidDate,String crcdOverdueFlag,String crcdState, RepaymentInfoBean repaymentInfoBean);
        /**
         *  费用试算结果
         */
        void transferCommissionChargeResult(TransCommissionChargeBean transCommissionChargeBean);
        /**
         *  购汇还款信用卡外币账户列表查询 回调
         *  判断当前信用卡是否支持使用人民币购汇还款
         */
        void crcdForeignPayQuery(List<String>  accountNoList);

        /**
         *  购汇还款信息回调
         */
        void crcdQueryForeignPayOffResult(BigDecimal payedAmt,BigDecimal exchangeRate);



    }

    public interface RepaymentConfirmView extends BaseView<BasePresenter>{
        /**
        *  关联信用卡还款确认
        */
        void getTransPayoffResult(RepaymentInfoBean repaymentInfoBean);

        /**
         *  查询实时账单，获取应还账单种类
         */
        void queryCrcdRTBillSuccess(List<CrcdBillInfoBean> beanList);
    }


    public interface Presenter extends BasePresenter {

        /**
         *  查询实时账单
         */
        void queryCrcdRTBill(String accounId);
        /**
        *   查询信用卡卡片信息详情
        */
        void queryCrcdAccountDetail(AccountBean accountBean);

        /**
        *   查询普通借记卡卡片信息详情
        */
        void queryNormalAccountDetail(AccountBean accountBean);

        /**
         *  查询还款方式
         */
        void queryCrcdPayway(String accounId);

        /**
         *  行内转账费用试算
         */
        void getBocTransCommCharge(RepaymentInfoBean repaymentInfoBean);

        /**
         *  信用卡还款信息校验
         */
        void checkPaymentInfo(RepaymentInfoBean repaymentInfoBean);

        /**
        *  关联信用卡还款确认
        */
        void transferPayoffResult(RepaymentInfoBean repaymentInfoBean);

        /**
        *  购汇还款信用卡外币账户列表查询
         *  @param accountNumber 信用卡卡号
        */
        void crcdForeignPayQuery(String  accountNumber);


        /**
        *   信用卡查询购汇还款信息
        */
        void crcdQueryForeignPayOff(String accountId,String currType);

        /**
        *   全球交易人民币记账功能查询
        */
        void crcdChargeOnRMBAccountQuery(String accountId);

        /**
        *  购汇还款 费用试算
        */
        void crcdForeignPayoffFee(RepaymentInfoBean repaymentInfoBean);

        /**
         * 信用卡购汇还款提交
        */
        void crcdForeignPayOff(RepaymentInfoBean repaymentInfoBean);


    }
}
