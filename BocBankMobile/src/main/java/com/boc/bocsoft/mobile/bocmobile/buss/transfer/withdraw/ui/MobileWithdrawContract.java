package com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdraw.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdraw.model.MobileWithdrawViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * Created by liuweidong on 2016/7/12.
 */
public class MobileWithdrawContract {
    public interface BeforeView {
        void queryRandomFail(BiiResultErrorException biiResultErrorException);
        void queryRandomSuccess();

        /**
         * 汇款解付详细信息失败
         *
         * @param biiResultErrorException
         */
        void queryMobileWithdrawDetailsFail(BiiResultErrorException biiResultErrorException);

        /**
         * 汇款解付详细信息成功
         */
        void queryMobileWithdrawDetailsSuccess();
    }

    public interface ResultView {
        /**
         * 汇款解付失败
         *
         * @param biiResultErrorException
         */
        void queryMobileWithdrawFail(BiiResultErrorException biiResultErrorException);

        /**
         * 汇款解付成功
         */
        void queryMobileWithdrawSuccess();
    }

    public interface Presenter extends BasePresenter {
        void queryRandom();

        /**
         * 汇款解付详细信息
         *
         * @param viewModel
         */
        void queryMobileWithdrawDetails(MobileWithdrawViewModel viewModel);

        /**
         * 汇款解付
         */
        void queryMobileWithdraw(String withDrawPwd, String withDrawPwd_RC, String cfcaVersion);
    }
}
