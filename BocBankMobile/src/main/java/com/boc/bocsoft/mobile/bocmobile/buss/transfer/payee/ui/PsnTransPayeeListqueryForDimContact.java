package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransPayeeListqueryForDimViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contact:查询收款人列表（支持模糊查询、分页）
 * Created by zhx on 2016/7/20
 */
public class PsnTransPayeeListqueryForDimContact {
    public interface View extends BaseView<Presenter> {

        // 查询收款人列表（支持模糊查询、分页）
        void psnTransPayeeListqueryForDimSuccess(PsnTransPayeeListqueryForDimViewModel viewModel);

        // 查询收款人列表（支持模糊查询、分页）
        void psnTransPayeeListqueryForDimFailed(BiiResultErrorException biiResultErrorException);

    }

    public interface Presenter extends BasePresenter {
        // 查询收款人列表（支持模糊查询、分页）
        void psnTransPayeeListqueryForDim(PsnTransPayeeListqueryForDimViewModel viewModel);
    }
}
