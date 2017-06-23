package com.boc.bocma.serviceinterface.op.interfacemodel.yetbillquery;

import android.text.TextUtils;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.common.MAOPCreditCardTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MAOPYetBillQueryModel extends MAOPBaseResponseModel {
    private static final String START_DATE_KEY = "stdate";
    private static final String END_DATE_KEY = "endate";
    private static final String ACC_DATE_KEY = "accdate";
    private static final String CURRENCY_KEY = "currency";
    private static final String LIMIT_HIGHTEST_KEY = "limithightest";
    private static final String LIMIT_USABLE_KEY = "limitusable";
    private static final String LAST_BALANCE_KEY = "lastbalance";
    private static final String EXPEND_TOTAL_KEY = "expendtotal";
    private static final String DEPOSIT_TOTAL_KEY = "deposittotal";
    private static final String THIS_BALANCE_KEY = "thisbalance";
    private static final String REPAY_SMALLEST_KEY = "repaysmallest";
    private static final String REPAY_DATE_KEY = "repaydate";
    private static final String COIN_FLAG_KEY = "coinflag";
    private static final String RECORD_COUNT_KEY = "record_count";
    
    private static final String SAP_LIST_KEY = "saplist";
    
    /**
     * 返回报文中的金额后两位为小数位
     */
    private static final long KEEP_DIGITS = 100;
    
    private double jsonString2Double(String amount) {
        if (TextUtils.isEmpty(amount)) {
            return 0f;
        } else {
            return Double.valueOf(amount)/KEEP_DIGITS;
        }
    }
    
    /**
     * @return 开始日期
     */
    public String getStartDate() {
        return startDate;
    }
    /**
     * @return 结束日期
     */
    public String getEndDate() {
        return endDate;
    }
    /**
     * @return 账单日期
     */
    public String getAccDate() {
        return accDate;
    }
    /**
     * @return 币种
     */
    public String getCurrency() {
        return currency;
    }
    /**
     * @return 信用限额
     */
    public double getLimitHightest() {
        return limitHightest;
    }
    /**
     * @return 可用额度
     */
    public double getLimitUsable() {
        return limitUsable;
    }
    /**
     * 
     * @return 上期账面余额 有正负号，负数表示贷记余额；正号表示有借记余额
     */
    public double getLastBalance() {
        return lastBalance;
    }
    /**
     * @return 存入总计
     */
    public double getExpendTotal() {
        return expendTotal;
    }
    /**
     * @return 支出总计
     */
    public double getDepositTotal() {
        return depositTotal;
    }
    /**
     * @return 本期账面余额 有正负号，负数表示贷记余额；正号表示有借记余额
     */
    public double getThisBalance() {
        return thisBalance;
    }
    /**
     * @return 最小还款额
     */
    public double getRepaySmallest() {
        return repaySmallest;
    }
    /**
     * @return 到期还款日
     */
    public String getRepayDate() {
        return repayDate;
    }
    /**
     * @return 是否还有后续数据包
     * "1"：还有后续数据 "0"：无后续数据
     */
    public int getCoinFlag() {
        return coinFlag;
    }
    /**
     * @return 笔数
     */
    public int getRecordCount() {
        return recordCount;
    }
    
    /**
     * @return 账单的交易列表
     */

    public List<MAOPCreditCardTransaction> getTransList() {
        return transList;
    }
    
    public static final Creator<MAOPYetBillQueryModel> CREATOR = new Creator<MAOPYetBillQueryModel>() {
        @Override
        public MAOPYetBillQueryModel createFromJson(JSONObject jsonResponse) throws JSONException {
            return new MAOPYetBillQueryModel(jsonResponse);
        }
        
    };
    
    public MAOPYetBillQueryModel(JSONObject jsonResponse) throws JSONException {
        startDate = jsonResponse.optString(START_DATE_KEY);
        endDate = jsonResponse.optString(END_DATE_KEY);
        accDate = jsonResponse.optString(ACC_DATE_KEY);
        currency = jsonResponse.optString(CURRENCY_KEY);
        limitHightest = jsonString2Double(jsonResponse.optString(LIMIT_HIGHTEST_KEY));
        limitUsable = jsonString2Double(jsonResponse.optString(LIMIT_USABLE_KEY));
        lastBalance = jsonString2Double(jsonResponse.optString(LAST_BALANCE_KEY));
        expendTotal = jsonString2Double(jsonResponse.optString(EXPEND_TOTAL_KEY));
        depositTotal = jsonString2Double(jsonResponse.optString(DEPOSIT_TOTAL_KEY));
        thisBalance = jsonString2Double(jsonResponse.optString(THIS_BALANCE_KEY));
        repaySmallest = jsonString2Double(jsonResponse.optString(REPAY_SMALLEST_KEY));
        repayDate = jsonResponse.optString(REPAY_DATE_KEY);
        coinFlag = jsonResponse.optInt(COIN_FLAG_KEY);
        recordCount = jsonResponse.optInt(RECORD_COUNT_KEY);
        JSONArray trans = jsonResponse.optJSONArray(SAP_LIST_KEY);
        for (int i = 0; trans !=null && i < trans.length(); i++) {
            JSONObject tran = trans.getJSONObject(i);
            MAOPCreditCardTransaction model = MAOPCreditCardTransaction.CREATOR.createFromJson(tran);
            transList.add(model);
        }
    }
    
    public MAOPYetBillQueryModel() {
    }

    private String startDate;
    private String endDate;
    private String accDate;
    private String currency;
    private double limitHightest;
    private double limitUsable;
    private double lastBalance;
    private double expendTotal;
    private double depositTotal;
    /**
     * 有正负号，负数表示贷记余额；正号表示有借记余额。
     */
    private double thisBalance;
    private double repaySmallest;
    private String repayDate;
    /**
     * "1"：还有后续数据 "0"：无后续数据
     */
    private int coinFlag;
    private int recordCount;
    private List<MAOPCreditCardTransaction> transList = new ArrayList<MAOPCreditCardTransaction>();
}
