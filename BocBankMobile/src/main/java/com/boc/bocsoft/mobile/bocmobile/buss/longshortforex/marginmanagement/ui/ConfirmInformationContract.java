package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.model.XpadVFGBailTransferViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

public class ConfirmInformationContract {

    public interface View {
        /**
         * 查询成功返回
         * @param xpadVFGBailTransferViewModel
         */
        void psnVFGBailTransferSuccess(XpadVFGBailTransferViewModel xpadVFGBailTransferViewModel);

        /**
         * 查询失败
         * @param billResultErrorException
         */
        void psnVFGBailTransferFail(BiiResultErrorException billResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        void psnVFGBailTransfer(XpadVFGBailTransferViewModel xpadVFGBailTransferViewModel);
    }

}