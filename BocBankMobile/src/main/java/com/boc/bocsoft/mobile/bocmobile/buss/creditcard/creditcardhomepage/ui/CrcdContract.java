package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.model.CrcdModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.model.CrcdSettingsInfoBean;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

import java.math.BigDecimal;

/**
 * 信用卡首页与详情
 * Created by liuweidong on 2016/11/22.
 */
public class CrcdContract {

    public interface HomeView {
        void queryCrcdGeneralInfoSuccess();

        void queryCrcdPointFail();

        void queryCrcdPointSuccess();
        void queryCrcdBillsExistFail();
        void queryCrcdBillsExistSuccess();
        void queryBillInputSuccess(BigDecimal upLimit, BigDecimal lowLimit);
    }

    public interface MenuView {
        /**
         * 信用卡设置类信息查询 成功
         */
        void querySettingsInfoSuccess(CrcdSettingsInfoBean settingsInfoBean);

        /**
         * 3D安全认证信息查询 成功
         */
        void query3DCertifInfoSuccess(String openFlag);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 4.93 093 信用卡综合信息查询PsnCrcdQueryGeneralInfo
         */
        void queryCrcdGeneralInfo(CrcdModel item);

        void queryCrcdBillsExist(CrcdModel item);

        void queryCrcdPoint(CrcdModel item);

        /**
         * 4.1 001还款方式查询 PsnCrcdQueryCrcdPaymentWay
         */
        void queryCrcdPaymentWay(String accountID);

        /**
         * 4.67 067全球交易人民币记账功能查询 PsnCrcdChargeOnRMBAccountQuery
         */
        void queryChargeOnRMBAccount(String accountID);

        /**
         * 4.95 095信用卡设置类信息查询 PsnCrcdQuerySettingsInfo
         */
        void querySettingsInfo(String accountID);

        /**
         * 4.106 106 3D安全认证信息查询PsnCrcd3DQueryCertifInfo
         */
        void query3DCertifInfo(String accountID);

        /**
         * 4.29 029办理账单分期输入PsnCrcdDividedPayBillSetInput
         *
         * @param accountID
         */
        void queryBillInput(String accountID);
    }
}
