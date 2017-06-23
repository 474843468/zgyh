package com.boc.bocsoft.mobile.bocmobile.buss.transfer.phone.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.transfer.phone.model.PhoneConfirmModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by wangtong on 2016/7/27.
 */
public class PhoneConfirmContact {
    public interface View extends BaseView<Presenter> {
        /**
         * 付款人详情数据模型
         */
        public PhoneConfirmModel getModel();

        /**
         * 手机号转账预交易结果
         */
        public void psnMobileTransferPreReturned();


        /**
         * 手机号转账提交交易返回
         */
        public void psnMobileTransferSubmitReturned();

        /**
         * 手续费试算返回
         */
        public void psnTransGetBocTransferCommissionChargeReturned();

        /**
         * 查询账户余额返回
         */
        public void psnAccountQueryAccountDetailReturned();

        /** * 查询信用卡账户详情成功*/
        void queryCreditAccountDetailReturned();

    }

    public interface Presenter extends BasePresenter {
        /**
         * 手机号转账预交易
         */
        public void psnMobileTransferPre();

        /**
         * 手机号转账提交交易
         */
        public void psnMobileTransferSubmit();

        /**
         * 手续费试算
         */
        public void psnTransGetBocTransferCommissionCharge();

        /**
         * 查询账户余额
         */
        public void psnAccountQueryAccountDetail();

        /***
         * 查询信用卡账户详情
         */
        void queryCreditAccountDetail(String accountId);
    }
}
