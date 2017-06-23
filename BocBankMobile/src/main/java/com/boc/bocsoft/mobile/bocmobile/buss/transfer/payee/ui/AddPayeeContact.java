package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnEbpsRealTimePaymentSavePayeeViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnQueryActTypeByActNumViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransBocAddPayeeViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransIsSamePayeeViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransNationalAddPayeeViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contact:添加收款人
 * Created by zhx on 2016/7/22
 */
public class AddPayeeContact {
    public interface View extends BaseView<Presenter> {
        // 成功回调：判断是否存在相同收款人
        void psnTransIsSamePayeeSuccess(PsnTransIsSamePayeeViewModel viewModel);

        // 失败回调：判断是否存在相同收款人
        void psnTransIsSamePayeeFailed(BiiResultErrorException biiResultErrorException);

        // 成功回调：国内跨行汇款-新增收款人
        void psnTransNationalAddPayeeSuccess(PsnTransNationalAddPayeeViewModel viewModel);

        // 失败回调：国内跨行汇款-新增收款人
        void psnTransNationalAddPayeeFailed(BiiResultErrorException biiResultErrorException);

        // 成功回调：实时付款保存收款人
        void psnEbpsRealTimePaymentSavePayeeSuccess(PsnEbpsRealTimePaymentSavePayeeViewModel viewModel);

        // 失败回调：实时付款保存收款人
        void psnEbpsRealTimePaymentSavePayeeFailed(BiiResultErrorException biiResultErrorException);

        // 成功回调：中行内汇款-新增收款人
        void psnTransBocAddPayeeSuccess(PsnTransBocAddPayeeViewModel viewModel);

        // 失败回调：中行内汇款-新增收款人
        void psnTransBocAddPayeeFailed(BiiResultErrorException biiResultErrorException);

        // 成功回调：根据账号查询账户类型
        void psnQueryActTypeByActNumSuccess(PsnQueryActTypeByActNumViewModel viewModel);

        // 失败回调：根据账号查询账户类型
        void psnQueryActTypeByActNumFailed(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        // 判断是否存在相同收款人
        void psnTransIsSamePayee(PsnTransIsSamePayeeViewModel viewModel);

        // 国内跨行汇款：新增收款人
        void psnTransNationalAddPayee(PsnTransNationalAddPayeeViewModel viewModel);

        // 实时付款保存收款人
        void psnEbpsRealTimePaymentSavePayee(PsnEbpsRealTimePaymentSavePayeeViewModel viewModel);

        // 根据账号查询账户类型
        void psnQueryActTypeByActNum(PsnQueryActTypeByActNumViewModel viewModel);

        // 中行内汇款：新增收款人
        void psnTransBocAddPayee(PsnTransBocAddPayeeViewModel viewModel);
    }
}
