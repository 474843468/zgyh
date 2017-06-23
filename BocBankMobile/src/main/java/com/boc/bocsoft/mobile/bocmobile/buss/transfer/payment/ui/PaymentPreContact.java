package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.model.PaymentPreModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by wangtong on 2016/6/30.
 */
public class PaymentPreContact {

    public interface View extends BaseView<Presenter> {

        /**
         * 付款人详情数据模型
         */
        public PaymentPreModel getModel();

        /**
         * 付款预交易结果
         */
        public void psnTransActPaymentVerifyReturned();

        /**
         * 安全因子返回
         */
        public void securityFactorReturned();

        /**
         * 主动收款付款提交返回
         */
        public void psnTransActPaymentSubmitReturned();

        /**
        * 账户余额查询返回
        */
        void psnAccountQueryAccountDetailReturned();

        /** * 查询信用卡账户详情成功*/
        void queryCreditAccountDetailReturned();

    }

    public interface Presenter extends BasePresenter {
        /**
         * 付款预交易
         */
        public void psnTransActPaymentVerify();

        /**
         * 获取安全因子
         */
        public void psnGetSecurityFactor();

        /**
         * 主动收款付款提交
         */
        public void psnTransActPaymentSubmit();

        /**
         * 查询账户余额
         */
        void psnAccountQueryAccountDetail();

        /***
         * 查询信用卡账户详情
         */
        void queryCreditAccountDetail(String accountId);
    }
}
