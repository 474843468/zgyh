package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransManagePayeeDeletePayeeViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransManagePayeeModifyAliasViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransManagePayeeModifyMobileViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransManagePayeeQueryPayeeListViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransPayeeListqueryForDimViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contact:收款人详情
 * Created by zhx on 2016/7/26
 */
public class PayeeDetailContact {
    public interface View extends BaseView<Presenter> {
        // 成功：修改收款人手机号
        void psnTransManagePayeeModifyMobileSuccess(PsnTransManagePayeeModifyMobileViewModel viewModel);

        // 失败：修改收款人手机号
        void psnTransManagePayeeModifyMobileFailed(BiiResultErrorException biiResultErrorException);

        // 成功：修改收款人别名
        void psnTransManagePayeeModifyAliasSuccess(PsnTransManagePayeeModifyAliasViewModel viewModel);

        // 失败：修改收款人别名
        void psnTransManagePayeeModifyAliasFailed(BiiResultErrorException biiResultErrorException);

        // 成功：删除收款人
        void psnTransManagePayeeDeletePayeeSuccess(PsnTransManagePayeeDeletePayeeViewModel viewModel);

        // 失败：删除收款人
        void psnTransManagePayeeDeletePayeeFailed(BiiResultErrorException biiResultErrorException);

        // 查询收款人列表
        void psnTransManagePayeeQueryPayeeListSuccess(PsnTransManagePayeeQueryPayeeListViewModel viewModel);

        // 查询收款人列表
        void psnTransManagePayeeQueryPayeeListFailed(BiiResultErrorException biiResultErrorException);

        // 查询收款人列表（支持模糊查询、分页）
        void psnTransPayeeListqueryForDimSuccess(PsnTransPayeeListqueryForDimViewModel viewModel);

        // 查询收款人列表（支持模糊查询、分页）
        void psnTransPayeeListqueryForDimFailed(BiiResultErrorException biiResultErrorException);
    }


    public interface Presenter extends BasePresenter {
        // 修改收款人手机号
        void psnTransManagePayeeModifyMobile(PsnTransManagePayeeModifyMobileViewModel viewModel);

        // 修改收款人别名
        void psnTransManagePayeeModifyAlias(PsnTransManagePayeeModifyAliasViewModel viewModel);

        // 删除收款人
        void psnTransManagePayeeDeletePayee(PsnTransManagePayeeDeletePayeeViewModel viewModel);

        // 查询收款人列表
        void psnTransManagePayeeQueryPayeeList(PsnTransManagePayeeQueryPayeeListViewModel viewModel);

        // 查询收款人列表（支持模糊查询、分页）
        void psnTransPayeeListqueryForDim(PsnTransPayeeListqueryForDimViewModel viewModel);
    }
}
