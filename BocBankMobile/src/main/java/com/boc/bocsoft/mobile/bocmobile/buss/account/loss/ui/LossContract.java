package com.boc.bocsoft.mobile.bocmobile.buss.account.loss.ui;

import android.content.Context;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.SecurityViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.loss.model.LossViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * Created by liuweidong on 2016/6/12.
 */
public class LossContract {

    public interface LossBeforeView {
        /**
         * 查询信用卡挂失费用失败
         *
         * @param biiResultErrorException
         */
        void queryCreditLossFeeFail(BiiResultErrorException biiResultErrorException);

        /**
         * 查询信用卡挂失费用成功
         */
        void queryCreditLossFeeSuccess();

        /**
         * 查询信用卡补卡地址失败
         *
         * @param biiResultErrorException
         */
        void queryCreditLossAddressFail(BiiResultErrorException biiResultErrorException);

        /**
         * 查询信用卡补卡地址成功
         */
        void queryCreditLossAddressSuccess();

        /**
         * 查询安全因子失败
         *
         * @param biiResultErrorException
         */
        void querySecurityFactorFail(BiiResultErrorException biiResultErrorException);

        /**
         * 查询安全因子成功
         *
         * @param securityViewModel
         */
        void querySecurityFactorSuccess(SecurityViewModel securityViewModel);
    }

    public interface LossView {
        /**
         * 借记卡挂失预交易失败
         *
         * @param biiResultErrorException
         */
        void debitCardLossConfirmFail(BiiResultErrorException biiResultErrorException);

        /**
         * 借记卡挂失预交易成功
         */
        void debitCardLossConfirmSuccess();

        /**
         * 借记卡挂失失败
         *
         * @param biiResultErrorException
         */
        void debitCardLossResultFail(BiiResultErrorException biiResultErrorException);

        /**
         * 借记卡挂失成功
         */
        void debitCardLossResultSuccess(LossViewModel lossViewModel);

        /**
         * 活期一本通挂失预交易失败
         *
         * @param biiResultErrorException
         */
        void accountLossConfirmFail(BiiResultErrorException biiResultErrorException);

        /**
         * 活期一本通挂失预交易成功
         */
        void accountLossConfirmSuccess();

        /**
         * 活期一本通挂失失败
         *
         * @param biiResultErrorException
         */
        void accountLossResultFail(BiiResultErrorException biiResultErrorException);

        /**
         * 活期一本通挂失成功
         */
        void accountLossResultSuccess();

        /**
         * 信用卡挂失预交易失败
         *
         * @param biiResultErrorException
         */
        void creditCardLossConfirmFail(BiiResultErrorException biiResultErrorException);

        /**
         * 信用卡挂失预交易成功
         */
        void creditCardLossConfirmSuccess();

        /**
         * 信用卡挂失失败
         *
         * @param biiResultErrorException
         */
        void creditCardLossResultFail(BiiResultErrorException biiResultErrorException);

        /**
         * 信用卡挂失成功
         */
        void creditCardLossResultSuccess();
    }

    public interface Presenter extends BasePresenter {
        /**
         * 查询信用卡挂失费用
         *
         * @param accountID
         */
        void queryCreditLossFee(int accountID);

        /**
         * 查询信用卡补卡地址
         *
         * @param accountID
         */
        void queryCreditLossAddress(int accountID);

        /**
         * 查询安全因子
         *
         * @param accountType
         */
        void querySecurityFactor(String accountType);

        /**
         * 借记卡挂失预交易
         *
         * @param accountNum
         * @param lossViewModel
         * @param combinID
         */
        void debitCardLossConfirm(String accountNum, LossViewModel lossViewModel, String combinID);

        /**
         * 借记卡挂失提交
         *
         * @param accountBean
         * @param encryptPasswords
         * @param curCombinID
         * @param context
         */
        void debitCardLossResult(AccountBean accountBean, String[] randomNums, String[] encryptPasswords,
                                 int curCombinID, Context context);

        /**
         * 活期一本通挂失预交易
         *
         * @param accountNum
         * @param combinID
         */
        void accountLossConfirm(String accountNum, String combinID);

        /**
         * 活期一本通挂失提交
         *
         * @param accountBean
         * @param encryptPasswords
         * @param curCombinID
         * @param context
         */
        void accountLossResult(AccountBean accountBean, String[] randomNums, String[] encryptPasswords,
                               int curCombinID, Context context);

        /**
         * 信用卡挂失预交易
         *
         * @param accountID
         * @param combinID
         */
        void creditCardLossConfirm(Integer accountID, String combinID);

        /**
         * 信用卡挂失提交
         *
         * @param accountID
         * @param encryptPasswords
         * @param curCombinID
         * @param context
         */
        void creditCardLossResult(String accountID, String[] randomNums, String[] encryptPasswords,
                                  int curCombinID, Context context);

        /**
         * 信用卡加强认证
         */
        void creditCardResultReinforce();
    }
}
