package com.boc.bocsoft.mobile.bocmobile.buss.transfer.phonetransferquery.ui;

import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileTransferQueryUnSubmitTrans.PsnMobileTransferQueryUnSubmitTransResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.phonetransferquery.model.PhoneTransferQueryViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by liuweidong on 2016/6/23.
 */
public class PhoneTransferQueryContract {

    public interface View extends BaseView<Presenter> {

        /**
         * 查询手机转账记录成功
         */
        void queryMobileTransferSuccess(PsnMobileTransferQueryUnSubmitTransResult psnMobileTransferQueryUnSubmitTransResult);

        /**
         * 查询手机转账记录失败
         */
        void queryMobileTransferFail(BiiResultErrorException biiResultErrorException);

    }

    public interface Presenter extends BasePresenter {
        /**
         * 查询手机转账记录
         */
        void queryMobileTransfer(PhoneTransferQueryViewModel phoneTransferQueryViewModel);
    }
}
