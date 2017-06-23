package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.ui;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayConsumeConfirm.PsnCrcdDividedPayConsumeConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayConsumeResult.PsnCrcdDividedPayConsumeResultResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmContract;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.model.ConsumeInstallmentModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * 作者：lq7090
 * 创建时间：2016/12/9.
 * 信用卡消费分期契约类
 */

public class ConsumeInstallmentContract extends BaseConfirmContract{

    /**
     *消费分期页面，确认页面实现接口
     */

    public interface BaseView<Presenter> extends BaseConfirmContract.View<PsnCrcdDividedPayConsumeResultResult>{

        /**获取安全因子成功回调*/
        void securityFactorSuccess(PsnGetSecurityFactorResult psnGetSecurityFactorResult);

        /**获取随机数成功回调*/
        void randomSuccess(String random);

        /**
         * 消费分期预交易
         *
         *     //关联账户转账
         void transLinkTransferSubmitSuccess(PsnTransLinkTransferSubmitResult result);
         void transLinkTransferSubmitFailed(BiiResultErrorException exception);
         */
        void crcdDividedPayConsumeConfirmSuccess(PsnCrcdDividedPayConsumeConfirmResult result ,PsnGetSecurityFactorResult securityFactorResult );
        void crcdDividedPayConsumeConfirmFailed(BiiResultErrorException exception);

        /**
         * 消费分期提交交易
         *
         *     //关联账户转账
         void transLinkTransferSubmitSuccess(PsnTransLinkTransferSubmitResult result);
         void transLinkTransferSubmitFailed(BiiResultErrorException exception);
         */
        void crcdDividedPayConsumeResultSuccess(PsnCrcdDividedPayConsumeResultResult result );
        void crcdDividedPayConsumeResultFailed(BiiResultErrorException exception);



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
        void crcdDividedPayConsumeConfirm(ConsumeInstallmentModel consumeInstallmentModel);

        /**获取随机数*/
        void queryRandom(String conversationId);

        /**
         * 消费分期提交交易
         */
        void crcdDividedPayConsumeResult(ConsumeInstallmentModel consumeInstallmentModel);
        /**获取会话id/
         */
        String getConversationId();

    }

}