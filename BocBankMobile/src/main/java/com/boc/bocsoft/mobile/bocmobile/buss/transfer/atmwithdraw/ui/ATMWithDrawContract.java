package com.boc.bocsoft.mobile.bocmobile.buss.transfer.atmwithdraw.ui;

import android.content.Context;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.model.SecurityViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.atmwithdraw.model.ATMWithDrawDetailsViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.atmwithdraw.model.ATMWithDrawQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.atmwithdraw.model.ATMWithDrawViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * ATM无卡取款
 * <p/>
 * Created by liuweidong on 2016/6/25.
 */
public class ATMWithDrawContract {

    public interface View {
        /**
         * 查询ATM无卡取款记录成功
         */
        void queryATMWithDrawSuccess(ATMWithDrawQueryViewModel atmWithDrawQueryViewModel);

        /**
         * 查询ATM无卡取款记录失败
         */
        void queryATMWithDrawFail(BiiResultErrorException biiResultErrorException);

        /**
         * 查询ATM无卡取款记录详情成功
         */
        void queryATMWithDrawDetailsSuccess(ATMWithDrawDetailsViewModel atmWithDrawDetailsViewModel);

        /**
         * 查询ATM无卡取款记录详情失败
         */
        void queryATMWithDrawDetailsFail(BiiResultErrorException biiResultErrorException);
    }

    public interface CancelView {
        /**
         * ATM无卡取款撤销成功
         */
        void cancelATMWithDrawSuccess();

        /**
         * ATM无卡取款撤销失败
         */
        void cancelATMWithDrawFail(BiiResultErrorException biiResultErrorException);
    }

    public interface BeforeView {
        /**
         * 查询账户详情成功
         *
         * @param viewModel
         */
        void queryAccountDetailsSuccess(ATMWithDrawViewModel viewModel);

        /**
         * 查询账户详情失败
         */
        void queryAccountDetailsFail();

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

        /**
         * ATM无卡取款预交易失败
         *
         * @param biiResultErrorException
         */
        void atmWithDrawConfirmFail(BiiResultErrorException biiResultErrorException);

        /**
         * ATM无卡取款预交易成功
         */
        void atmWithDrawConfirmSuccess();

        void psnTransQuotaqueryFail();
        void psnTransQuotaquerySuccess(String quotaAmount);
    }

    public interface ResultView {
        /**
         * 查询账户详情成功
         *
         * @param viewModel
         */
        void queryAccountDetailsSuccess(ATMWithDrawViewModel viewModel);

        /**
         * 查询账户详情失败
         */
        void queryAccountDetailsFail();

        /**
         * ATM无卡取款预交易失败
         *
         * @param biiResultErrorException
         */
        void atmWithDrawConfirmFail(BiiResultErrorException biiResultErrorException);

        /**
         * ATM无卡取款预交易成功
         */
        void atmWithDrawConfirmSuccess();

        /**
         * ATM无卡取款提交交易失败
         *
         * @param biiResultErrorException
         */
        void atmWithDrawResultFail(BiiResultErrorException biiResultErrorException);

        /**
         * ATM无卡取款提交交易成功
         */
        void atmWithDrawResultSuccess();
    }

    public interface Presenter extends BasePresenter {
        /**
         * 查询ATM无卡取款记录
         */
        void queryATMWithDraw(ATMWithDrawQueryViewModel atmWithDrawQueryViewModel);

        /**
         * 查询ATM无卡取款记录详情
         */
        void queryATMWithDrawDetails(ATMWithDrawDetailsViewModel atmWithDrawDetailsViewModel);

        /**
         * 撤销ATM无卡取款
         */
        void cancelATMWithDraw(ATMWithDrawDetailsViewModel atmWithDrawDetailsViewModel);

        /**
         * 查询账户详情
         *
         * @param accountID
         */
        void queryAccountDetails(String accountID);

        /**
         * 查询安全因子
         */
        void querySecurityFactor();

        /**
         * ATM无卡取款预交易
         */
        void atmWithDrawConfirm(String accountID, String combinID, String money);

        /**
         * ATM无卡取款提交交易
         */
        void atmWithDrawResult(String accountID, String[] randomNums, String[] encryptPasswords,
                               int curCombinID, Context context, String devicePrint);

        void psnTransQuotaquery();
    }
}
