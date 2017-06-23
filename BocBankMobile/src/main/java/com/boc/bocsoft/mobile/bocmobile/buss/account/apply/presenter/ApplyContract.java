package com.boc.bocsoft.mobile.bocmobile.buss.account.apply.presenter;


import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.apply.model.ApplyAccountModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;


/**
 * 申请定期/活期账户接口类
 * Created by liuyang on 2016/6/12.
 */
public class ApplyContract {

    public interface QueryAccountView extends BaseView<BasePresenter> {
        /**
         * 查询个人客户国籍信息成功
         * @param countryCode
         */
        void queryCountryCodeSuccess(String countryCode);
    }

    public interface ApplyAccountView extends BaseView<BasePresenter> {
        /**
         * 申请定期活期账户交易成功
         * @param ApplyAccountModel
         */
        void depositeResultSuccess(ApplyAccountModel ApplyAccountModel);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 查询个人客户国籍信息
         * @param accountId
         */
        void queryCountryCode(String accountId);
        /**
         * 申请定期活期账户预交易
         * @param applyAccountModel
         * @param currentFactorId
         */
        void psnApplyTermDepositeConfirm(ApplyAccountModel applyAccountModel, String currentFactorId);
        /**
         * 申请定期活期账户交易
         * @param applyAccountModel
         * @param deviceInfoModel
         * @param factorId
         * @param randomNums
         * @param encryptPasswords
         */
        void psnApplyTermDepositeResult(ApplyAccountModel applyAccountModel, DeviceInfoModel deviceInfoModel, String factorId, String[] randomNums, String[] encryptPasswords) ;
    }
}
