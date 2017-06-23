package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnDirTransQueryPayeeListViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransManagePayeeQueryPayeeListViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contact:查询收款人列表
 * Created by zhx on 2016/7/20
 */
public class PsnTransManagePayeeQueryPayeeListContact {
    public interface View extends BaseView<Presenter> {

        // 查询收款人列表
        void psnTransManagePayeeQueryPayeeListSuccess(PsnTransManagePayeeQueryPayeeListViewModel viewModel);

        // 查询收款人列表
        void psnTransManagePayeeQueryPayeeListFailed(BiiResultErrorException biiResultErrorException);

        // 查询定向收款人列表
        void psnDirTransQueryPayeeListSuccess(PsnDirTransQueryPayeeListViewModel viewModel);

        // 查询定向收款人列表
        void psnDirTransQueryPayeeListFailed(BiiResultErrorException biiResultErrorException);


    }

    public interface Presenter extends BasePresenter {
        // 查询收款人列表
        void psnTransManagePayeeQueryPayeeList(PsnTransManagePayeeQueryPayeeListViewModel viewModel);

        // 查询定向收款人列表
        void psnDirTransQueryPayeeList(PsnDirTransQueryPayeeListViewModel viewModel);
    }
}
