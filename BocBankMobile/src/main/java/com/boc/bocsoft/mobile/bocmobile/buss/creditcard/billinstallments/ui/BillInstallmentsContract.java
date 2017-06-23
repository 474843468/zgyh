package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billinstallments.ui;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayBillSetConfirm.PsnCrcdDividedPayBillSetConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayBillSetResult.PsnCrcdDividedPayBillSetResultResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmContract;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billinstallments.model.BillInstallmentModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * 信用卡账单分期契约类
 * 作者：lq7090
 * 创建时间：2016/12/9.
 */


public class BillInstallmentsContract extends BaseConfirmContract {

    /**
     *消费分期页面，确认页面实现接口
     */

    public interface BaseView<Presenter> extends BaseConfirmContract.View<PsnCrcdDividedPayBillSetResultResult> {

        /**获取安全因子成功回调*/
        void securityFactorSuccess(PsnGetSecurityFactorResult psnGetSecurityFactorResult);

        /**获取随机数成功回调*/
        void randomSuccess(String random);

        /**
         * 账单分期预交易
         *

         */
        void crcdDividedPayBillSetConfirmSuccess(PsnCrcdDividedPayBillSetConfirmResult result , PsnGetSecurityFactorResult securityFactorResult );
        void crcdDividedPayBillSetConfirmFailed(BiiResultErrorException exception);

        /**
         * 账单分期提交交易
         *
         *     //关联账户转账
         void transLinkTransferSubmitSuccess(PsnTransLinkTransferSubmitResult result);
         void transLinkTransferSubmitFailed(BiiResultErrorException exception);
         */
        void crcdDividedPayBillSetResultSuccess(PsnCrcdDividedPayBillSetResultResult result );
        void crcdDividedPayBillSetResultFailed(BiiResultErrorException exception);



    }

    /**
     *消费分期Presenter实现此接口
     */
    public interface Presenter extends BasePresenter {

        /**获取安全因子*/
        void querySecurityFactor();
        /**
         * 消费分期预交易
         */
        void crcdDividedPayBillSetConfirm(BillInstallmentModel billInstallmentModel);

        /**获取随机数*/
        void queryRandom(String conversationId);

        /**
         * 消费分期提交交易
         */
        void crcdDividedPayBillSetResult(BillInstallmentModel billInstallmentModel);
        /**获取会话id/
         */
        String getConversationId();

    }

}