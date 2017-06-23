package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransQueryExternalBankInfoViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contact:选择开户行
 * Created by zhx on 2016/7/22
 */
public class ChooseOpenBankContact {
    public interface View extends BaseView<Presenter> {
        // 成功：查询开户行
        void psnTransQueryExternalBankInfoSuccess(PsnTransQueryExternalBankInfoViewModel viewModel);

        // 失败：查询开户行
        void psnTransQueryExternalBankInfoFailed(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        // 查询开户行
        void psnTransQueryExternalBankInfo(PsnTransQueryExternalBankInfoViewModel viewModel);
    }
}
