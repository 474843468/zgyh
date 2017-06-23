package com.boc.bocsoft.mobile.bocmobile.buss.account.limit.presenter;

import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.model.LimitModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.model.QuotaModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * @author wangyang
 *         2016/10/11 20:50
 *         交易限额修改
 */
public interface LimitContract {

    interface LimitView extends BaseView<BasePresenter> {
        /**
         * 开通服务查询
         * @param limitModel
         */
        void queryLimit(LimitModel limitModel);
    }

    interface QuotaView extends BaseView<BasePresenter> {
        /**
         * 限额服务查询
         * @param quotaModel
         */
        void queryAllQuota(QuotaModel quotaModel);
    }

    interface QuotaTransactionView extends BaseView<BasePresenter>{
        /**
         * 设置交易限额回调
         * @param isSuccess
         */
        void setQuota(boolean isSuccess);
    }

    interface LimitCloseView extends BaseView<BasePresenter>{
        /**
         * 关闭服务交易
         * @param limitModel
         */
        void closeService(LimitModel limitModel);
    }

    interface LimitUpdateView extends BaseView<BasePresenter>{
        /**
         * 修改限额交易
         * @param limitModel
         */
        void updateLimit(LimitModel limitModel);
    }

    interface LimitOpenView extends BaseView<BasePresenter>{
        /**
         * 开通限额交易
         * @param limitModel
         */
        void openLimit(LimitModel limitModel);
    }

    interface LimitPresenter extends BasePresenter {
        /**
         * 开通服务查询
         * @param accountBean
         * @param serviceType
         */
        void queryLimit(AccountBean accountBean, String serviceType);
        /**
         * 查询所有交易限额
         * @param accountBean
         */
        void queryAllQuota(QuotaModel accountBean);
    }

    interface LimitTransactionPresenter extends BasePresenter{
        /**
         * 设置交易限额
         * @param quotaModel
         * @param combinId
         */
        void setQuotaPre(QuotaModel quotaModel,String combinId);
        /**
         * 设置交易限额交易
         * @param quotaModel
         * @param deviceInfoModel
         * @param factorId
         * @param randomNums
         * @param encryptPasswords
         */
        void setQuota(QuotaModel quotaModel, DeviceInfoModel deviceInfoModel, String factorId, String[] randomNums, String[] encryptPasswords);
        /**
         * 关闭服务预交易
         * @param limitModel
         *
         */
        void closeServicePre(LimitModel limitModel);
        /**
         * 关闭服务交易
         * @param limitModel
         *
         */
        void closeService(LimitModel limitModel);
        /**
         * 交易限额修改预交易
         * @param limitModel
         * @param factorId
         */
        void updateLimitPre(LimitModel limitModel, String factorId);
        /**
         * 交易限额修改
         * @param limitModel
         * @param deviceInfoModel
         * @param factorId
         * @param randomNums
         * @param encryptPasswords
         */
        void updateLimit(LimitModel limitModel, DeviceInfoModel deviceInfoModel, String factorId, String[] randomNums, String[] encryptPasswords);
        /**
         * 开通限额服务预交易
         * @param limitModel
         * @param factorId
         */
        void openLimitPre(LimitModel limitModel, String factorId);
        /**
         * 开通限额交易
         * @param limitModel
         * @param deviceInfoModel
         * @param factorId
         * @param randomNums
         * @param encryptPasswords
         */
        void openLimit(LimitModel limitModel, DeviceInfoModel deviceInfoModel, String factorId, String[] randomNums, String[] encryptPasswords);

    }
}
