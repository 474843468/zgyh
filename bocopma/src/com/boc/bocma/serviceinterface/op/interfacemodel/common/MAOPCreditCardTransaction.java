package com.boc.bocma.serviceinterface.op.interfacemodel.common;

import android.text.TextUtils;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

import org.json.JSONException;
import org.json.JSONObject;

public class MAOPCreditCardTransaction extends MAOPBaseResponseModel {
    private static final String LAST_FOUR_KEY = "lastfour";
    private static final String TRAN_DATE_KEY = "trandate";
    private static final String ACCOUNT_DATE_KEY = "accdate";
    private static final String TRAN_CURRENCY_KEY = "trancur";
    private static final String ACCOUNT_CURRENCY_KEY = "acccur";
    private static final String TRAN_AMOUNT_KEY = "tranamt";
    private static final String ACCOUNT_AMOUNT_KEY = "accamt";
    private static final String CREDIT_DEBIT_KEY = "credebt";
    private static final String TRAN_CODE_KEY = "trancode";
    private static final String TRAN_DESCRIPTION_KEY = "trandes";
    private static final String RECV_BRANCH_KEY = "recvbarnch";
    private static final String ISSUB_BRANCH_KEY = "issubranch";
    
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
    
    public MAOPCreditCardTransaction(JSONObject jsonResponse) {
        lastFour = jsonResponse.optString(LAST_FOUR_KEY);
        tranDate = jsonResponse.optString(TRAN_DATE_KEY);
        accDate = jsonResponse.optString(ACCOUNT_DATE_KEY);
        tranCurrency = jsonResponse.optString(TRAN_CURRENCY_KEY);
        accCurrency = jsonResponse.optString(ACCOUNT_CURRENCY_KEY);
        tranAmount = jsonString2Double(jsonResponse.optString(TRAN_AMOUNT_KEY));
        accAmount = jsonString2Double(jsonResponse.optString(ACCOUNT_AMOUNT_KEY));
        credebt = jsonResponse.optString(CREDIT_DEBIT_KEY);
        tranCode = jsonResponse.optString(TRAN_CODE_KEY);
        tranDescription = jsonResponse.optString(TRAN_DESCRIPTION_KEY);
        recvBarnch = jsonResponse.optString(RECV_BRANCH_KEY);
        isSuBranch = jsonResponse.optString(ISSUB_BRANCH_KEY);
    }
    
    public static final Creator<MAOPCreditCardTransaction> CREATOR = new Creator<MAOPCreditCardTransaction>() {
        @Override
        public MAOPCreditCardTransaction createFromJson(JSONObject jsonResponse) throws JSONException {
            return new MAOPCreditCardTransaction(jsonResponse);
        }
        
    };
    /**
     * @return 卡号后4位
     */
    public String getLastFour() {
        return lastFour;
    }
    /**
     * @return 交易日期 YYYY-MM-DD
     */
    public String getTranDate() {
        return tranDate;
    }
    /**
     * @return 记帐日期 YYYY-MM-DD
     */
    public String getAccDate() {
        return accDate;
    }
    /**
     * @return 交易币种
     */
    public String getTranCurrency() {
        return tranCurrency;
    }
    /**
     * @return 记帐币种
     */
    public String getAccCurrency() {
        return accCurrency;
    }
    /**
     * @return 交易金额
     */
    public double getTranAmount() {
        return tranAmount;
    }
    /**
     * @return 记帐金额
     */
    public double getAccAmount() {
        return accAmount;
    }
    /**
     * @return 借方、贷方
     *  CRED表示贷方(例如，存款)
     *  DEBT表示借方（例如，消费）
     */
    public String getCredebt() {
        return credebt;
    }
    /**
     * @return 交易代码
     */
    public String getTranCode() {
        return tranCode;
    }
    /**
     * @return 交易描述,交易摘要
     */
    public String getTranDescription() {
        return tranDescription;
    }
    /**
     * @return 收单分行和网点编号
     */
    public String getRecvBarnch() {
        return recvBarnch;
    }
    /**
     * @return 发卡分行和网点编号
     */
    public String getIsSuBranch() {
        return isSuBranch;
    }

    /**
     * 卡号后4位
     */
    private String lastFour;
    /**
     * 交易日期 YYYY-MM-DD
     */
    private String tranDate;
    /**
     * 记帐日期 YYYY-MM-DD
     */
    private String accDate;
    /**
     * 交易币种
     */
    private String tranCurrency;
    /**
     * 记帐币种
     */
    private String accCurrency;
    /**
     * 交易金额
     */
    private double tranAmount;
    /**
     * 记帐金额
     */
    private double accAmount;
    /**
     * 借方、贷方
     *  CRED表示贷方(例如，存款)
     *  DEBT表示借方（例如，消费）
     */
    private String credebt;
    /**
     * 交易代码
     */
    private String tranCode;
    /**
     * 交易描述,交易摘要
     */
    private String tranDescription;
    /**
     * 收单分行和网点编号
     */
    private String recvBarnch;
    /**
     * 发卡分行和网点编号
     */
    private String isSuBranch;

}
