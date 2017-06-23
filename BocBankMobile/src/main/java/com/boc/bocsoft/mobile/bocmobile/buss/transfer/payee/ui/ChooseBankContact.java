package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnOtherBankQueryForTransToAccountViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contact:查询银行列表
 * Created by zhx on 2016/7/20
 */
public class ChooseBankContact {
    public interface View extends BaseView<Presenter> {
        // 成功：查询银行列表
        // TODO 此处有问题，应使用ViewModel，而不是bii层的东西
        void psnOtherBankQueryForTransToAccountSuccess(PsnOtherBankQueryForTransToAccountViewModel psnOtherBankQueryForTransToAccountViewModel, String type);

        // 失败：查询银行列表
        void psnOtherBankQueryForTransToAccountFailed(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        // 查询银行列表
        void psnOtherBankQueryForTransToAccount(PsnOtherBankQueryForTransToAccountViewModel viewModel);
    }
}
