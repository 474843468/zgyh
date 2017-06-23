package com.boc.bocsoft.mobile.bocmobile.buss.transfer.mobileremit.ui;

import android.content.Context;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.model.SecurityViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.mobileremit.model.AgentViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.mobileremit.model.MobileRemitViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by liuweidong on 2016/7/12.
 */
public class MobileRemitContract {

    public interface View {
        /**
         * 查询账户详情成功
         *
         * @param viewModel
         */
        void queryAccountDetailsSuccess(MobileRemitViewModel viewModel);

        /**
         * 查询账户详情失败
         *
         * @param biiResultErrorException
         */
        void queryAccountDetailsFail(BiiResultErrorException biiResultErrorException);

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
         * 汇往取款人预交易失败
         *
         * @param biiResultErrorException
         */
        void mobileRemitConfirmFail(BiiResultErrorException biiResultErrorException);

        /**
         * 汇往取款人预交易成功
         */
        void mobileRemitConfirmSuccess();

        void psnTransQuotaquerySuccess(String quotaAmount);
        void psnTransQuotaqueryFail();
    }

    public interface AgentView extends BaseView<Presenter> {
        /**
         * 查询代理点失败
         *
         * @param biiResultErrorException
         */
        void queryAgentFail(BiiResultErrorException biiResultErrorException);

        /**
         * 查询代理点成功
         */
        void queryAgentSuccess(AgentViewModel agentViewModel);
    }

    public interface ResultView extends BaseView<Presenter> {
        /**
         * 汇往取款人预交易失败
         *
         * @param biiResultErrorException
         */
        void mobileRemitConfirmFail(BiiResultErrorException biiResultErrorException);

        /**
         * 汇往取款人预交易成功
         */
        void mobileRemitConfirmSuccess();

        /**
         * 汇往取款人提交交易失败
         *
         * @param biiResultErrorException
         */
        void mobileRemitResultFail(BiiResultErrorException biiResultErrorException);

        /**
         * 汇往取款人提交交易成功
         */
        void mobileRemitResultSuccess();
    }

    public interface Presenter extends BasePresenter {
        /**
         * 查询账户详情
         *
         * @param accountID
         */
        void queryAccountDetails(String accountID);

        /**
         * 代理点查询
         *
         * @param prvIbkNum
         */
        void queryAgent(String prvIbkNum, String currentIndex, String pageSize);

        /**
         * 查询安全因子
         */
        void querySecurityFactor();

        /**
         * 汇往取款人预交易
         */
        void mobileRemitConfirm(String accountID, String combinID);

        /**
         * 汇往取款人提交交易
         */
        void mobileRemitResult(String accountID, String[] randomNums,
                               String[] encryptPasswords, int curCombinID, Context context);

        void psnTransQuotaquery();
    }
}
