package com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.model.QrcodeConfirmModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by wangtong on 2016/7/28.
 */
public class QrcodeConfirmContract {
    public interface View extends BaseView<Presenter> {
        /**
         * 付款人详情数据模型
         */
        QrcodeConfirmModel getModel();

        /**
         * 账户余额查询返回
         */
        void psnAccountQueryAccountDetailReturned();

        /**
         * 中行内转账预交易返回
         */
        void psnTransBocTransferVerifyReturned();


        /**
         * 中行内转账提交返回
         */
        void psnTransBocTransferSubmitReturned();

        /** * 查询信用卡账户详情成功*/
        void queryCreditAccountDetailReturned();

    }

    public interface Presenter extends BasePresenter {

        /**
         * 中行内转账预交易
         */
        void psnTransBocTransferVerify();


        /**
         * 中行内转账提交
         */
        void psnTransBocTransferSubmit();

        /**
         * 查询账户余额
         */
        void psnAccountQueryAccountDetail();

        /*** 查询信用卡账户详情*/
        void queryCreditAccountDetail(String accountId);
    }
}
