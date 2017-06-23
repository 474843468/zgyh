package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.havebills.ui;

import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.havebills.model.CrcdBillQueryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.havebills.model.CrcdBilledDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.havebills.model.CrcdBilledModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.havebills.model.CrcdSetingsInfoModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by liuweidong on 2016/12/14.
 */

public class CrcdBillYContract {

    public interface CrcdBillQueryView extends BaseView<BasePresenter> {
        void queryCrcdBillIsExistSuccess(String billExsitFlag);

        void crcdQueryBilledTrans(CrcdBilledModel billedModle);

        void crcdQueryBilledTransDetail(CrcdBilledDetailModel billedDetailModel);

        void crcdDividedPayBillSetInput(BigDecimal upInstmtAmount, BigDecimal lowInstmtAmount);

        /**
         * 信用卡设置类信息查询
         */
        void querySettingsInfo(CrcdSetingsInfoModel crcdSetingsInfoModel);

        void reSetEmailPagerCheck();

        void reSetSmsPagerCheck();


    }

    public interface CrcdBillHistoryView {
        /**
         * 查询历史账单成功
         */
        void queryCrcdHistoryBillSuccess();
    }

    public interface Presenter extends BasePresenter {
        /**
         * 信用卡当月是否已出账单
         */
        void queryCrcdBillIsExist(String accountId);

        /**
         * 查询信用卡已出账单
         */
        void crcdQueryBilledTrans(CrcdBillQueryModel queryModle);

        /**
         * 查询信用卡已出账单交易明细
         */
        void crcdQueryBilledTransDetail(CrcdBillQueryModel queryModle);

        /**
         * 办理账单分期输入
         */
        void crcdDividedPayBillSetInput(AccountBean accountBean);

        /**
         * 查询历史账单
         */
        void queryCrcdHistoryBill(List<String> dates, String accountID);

        /**
        * 信用卡设置类信息查询
        */
        void querySettingsInfo(String accountId);

        /**
        *  发送电子邮件对账单
         *   billDate填写规则: YYYY/MM
        */
        void reSetEmailPagerCheck(String accountId,String billDate,String billAddress);
        /**
        *  发送手机邮件对账单
         *  billDate填写规则: YYYY/MM
        */
        void reSetSmsPagerCheck(String accountId,String billDate,String billAddress);

    }

}
