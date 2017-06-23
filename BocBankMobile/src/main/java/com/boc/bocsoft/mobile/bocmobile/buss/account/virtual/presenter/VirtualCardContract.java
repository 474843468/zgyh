package com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.presenter;

import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.model.VirtualBillModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.model.VirtualBillTransModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.model.VirtualCardModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * @author wangyang
 *         16/7/19 20:39
 */
public interface VirtualCardContract {

    interface VirCardView extends BaseView<BasePresenter> {
        /**
         * 查询虚拟卡列表回调
         * @param models
         */
        void virtualCardListQuery(List<VirtualCardModel> models);
    }
    interface VirCardApplyView extends BaseView<BasePresenter>{
        /**
         * 申请虚拟卡初始化回调
         * @param model
         */
        void initApplyVirtual(VirtualCardModel model);
    }
    interface VirBillView extends BaseView<BasePresenter>{
        /**
         * 未出账单合计
         * @param models
         */
        void queryUnsettledbillSum(List<VirtualBillModel> models);
        /**
         * 查询已出账单
         * @param models
         */
        void querySettledbill(List<List<VirtualBillModel>> models);
    }
    interface VirCardApplyTransactionView extends BaseView<BasePresenter>{
        /**
         *   申请虚拟卡交易回调
         */
        void psnCrcdVirtualCardApplySubmit(VirtualCardModel model);
    }

    interface VirCardUpdateTransactionView extends BaseView<BasePresenter>{
        /**
         *   修改交易限额
         * @param model
         */
        void psnCrcdVirtualCardFunctionSetSubmit(VirtualCardModel model);
    }
    interface VirCardCancelView extends BaseView<BasePresenter>{
        /**
         * 注销用户
         * @param isCancel
         */
        void psnCrcdVirtualCardCancel(boolean isCancel);
    }
    interface VirCardUnsettledBillView extends BaseView<BasePresenter>{
        /**
         * 查询未出账单
         */
        void queryUnsettledBill(List<VirtualBillTransModel> transModels,int recordNumber);
    }

    interface Presenter extends BasePresenter{
        /**
         * 根据账户查询虚拟卡列表
         * @param accountBean
         */
        void psnVirtualVircardListQuery(AccountBean accountBean);
        /**
         * 申请虚拟银行卡初始化
         * @param creditAccountBean
         */
        void psnCrcdVirtualCardApplyInit(AccountBean creditAccountBean);
        /**
         * 虚拟卡发送短信
         * @param model
         */
        void psnCrcdVirtualCardSendMessage(VirtualCardModel model);
        /**
         * 虚拟银行卡未出账单合计查询
         * @param model
         */
        void psnCrcdVirtualCardUnsettledbillSum(VirtualCardModel model);
        /**
         * 虚拟银行卡未出账单查询
         * @param model
         * @param currentPage
         */
        void psnCrcdVirtualCardUnsettledbillQuery(VirtualCardModel model, int currentPage);
        /**
         * 虚拟银行卡已出账单
         * @param model
         * @param isHadUnsettled
         */
        void psnCrcdVirtualCardSettledbillQuery(VirtualCardModel model,boolean isHadUnsettled);
        /**
         *  注销虚拟银行卡
         * @param model
         */
        void psnCrcdVirtualCardCancel(VirtualCardModel model);
    }

    interface TransactionPresenter extends BasePresenter{
        /**
         * 申请虚拟卡预交易
         * @param model
         * @param factorId
         */
        void psnCrcdVirtualCardApplyConfirm(VirtualCardModel model,String factorId);
        /**
         * 申请虚拟卡交易
         * @param model
         * @param deviceInfoModel
         * @param factorId
         * @param randomNums
         * @param encryptPasswords
         */
        void psnCrcdVirtualCardApplySubmit(VirtualCardModel model, DeviceInfoModel deviceInfoModel, String factorId, String[] randomNums, String[] encryptPasswords);
        /**
         * 修改交易限额预交易
         * @param model
         * @param factorId
         */
        void psnCrcdVirtualCardFunctionSetConfirm(VirtualCardModel model,String factorId);
        /**
         * 修改交易限额交易
         * @param model
         * @param deviceInfoModel
         * @param factorId
         * @param randomNums
         * @param encryptPasswords
         */
        void psnCrcdVirtualCardFunctionSetSubmit(VirtualCardModel model, DeviceInfoModel deviceInfoModel, String factorId, String[] randomNums, String[] encryptPasswords);
    }
}
