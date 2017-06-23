package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.unsettledbills.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.unsettledbills.model.CrcdBillQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.unsettledbills.model.CrcdUnsettledBillDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.unsettledbills.model.CrcdUnsettledBillsModel;

import java.util.List;

/**
 * Created by liuweidong on 2016/12/14.
 */

public class CrcdBillNContract {

    public interface RecordView  {

        /**
         *  查询信用卡未出账单
         */
        void crcdQueryFutureBill(CrcdUnsettledBillDetailModel crcdBillQueryModel);


        /**
         *  查询信用卡未出账单合计
         */
        void crcdQueryFutureBillTotalIncome(List<CrcdUnsettledBillsModel> crcdUnsettledBillsModels);

    }

    public interface UnrecordView{
        /**
         *  查询信用卡未入账 代授权交易
         */
        void crcdQueryUnauthorizedTrans(CrcdUnsettledBillDetailModel crcdBillQueryModel);


        /**
         *  查询信用卡未入账 代授权交易
         */
        void crcdQueryUnauthorizedTransToatal(List<CrcdUnsettledBillsModel> crcdUnsettledBillsModels);
    }

    public interface Presenter  {

        /**
        *  查询信用卡未出账单
        */
        void crcdQueryFutureBill(CrcdBillQueryViewModel crcdBillQueryModel);


        /**
        *  查询信用卡未出账单合计
        */
        void crcdQueryFutureBillTotalIncome(CrcdBillQueryViewModel crcdBillQueryModel);


        /**
        *  查询信用卡未入账 代授权交易
        */
        void crcdQueryUnauthorizedTrans(CrcdBillQueryViewModel crcdBillQueryModel);


        /**
        *  查询信用卡未入账 代授权交易
        */
        void crcdQueryUnauthorizedTransToatal(CrcdBillQueryViewModel crcdBillQueryModel);

    }
}
