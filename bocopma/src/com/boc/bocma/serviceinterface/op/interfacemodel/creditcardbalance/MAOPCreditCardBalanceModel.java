package com.boc.bocma.serviceinterface.op.interfacemodel.creditcardbalance;

import android.text.TextUtils;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 信用卡余额查询结果类
 */
public class MAOPCreditCardBalanceModel extends MAOPBaseResponseModel {
    /**
     * 余额
     */
    private static final String BALANCE_KEY = "balance";
    /**
     * 消费积分
     */
    private static final String XFJF_KEY = "xfjf";
    /**
     * 整体限额
     */
    private static final String ZTXE_KEY = "ztxe";
    /**
     * 整体可用额度
     */
    private static final String ZTKYXE_KEY = "ztkyxe";
    /**
     * 取现限额
     */
    private static final String QXXE_KEY = "qxxe";
    /**
     * 可用取现额度
     */
    private static final String KYQXED_KEY = "kyqxed";
    /**
     * 分期额度
     */
    private static final String FQED_KEY = "fqed";
    /**
     * 分期可用额
     */
    private static final String FQKYE_KEY = "fqkye";
    
    
    private String balance;
    private String xfjf;
    private String ztxe;
    private String ztkyxe;
    private String qxxe;
    private String kyqxed;
    private String fqed;
    private String fqkye;
    
    public MAOPCreditCardBalanceModel(JSONObject jsonResponse) {
        balance = String.valueOf(jsonString2Double(jsonResponse.optString(BALANCE_KEY)));
        xfjf = jsonResponse.optString(XFJF_KEY);
        ztxe = jsonResponse.optString(ZTXE_KEY);
        ztkyxe = jsonResponse.optString(ZTKYXE_KEY);
        qxxe = jsonResponse.optString(QXXE_KEY);
        kyqxed = jsonResponse.optString(KYQXED_KEY);
        fqed = jsonResponse.optString(FQED_KEY);
        fqkye = jsonResponse.optString(FQKYE_KEY);
    }
    public MAOPCreditCardBalanceModel(){
    	
    }
    
    /**
     * 借记卡余额返回报文中的金额后两位为小数
     */
    private static final long KEEP_DIGITS = 100;
    
    private double jsonString2Double(String amount) {
        if (TextUtils.isEmpty(amount)) {
            return 0f;
        } else {
        	if(amount.contains("-")){
        		amount = amount.substring(amount.indexOf("-"), amount.length());
        	}
        	if(amount.contains("+")){
        		amount = amount.substring(amount.indexOf("-")+1, amount.length());
        	}
            return Double.valueOf(amount)/KEEP_DIGITS;
        }
    }
    
    public static final Creator<MAOPCreditCardBalanceModel> CREATOR = new Creator<MAOPCreditCardBalanceModel>() {
        @Override
        public MAOPCreditCardBalanceModel createFromJson(JSONObject jsonResponse) throws JSONException {
            return new MAOPCreditCardBalanceModel(jsonResponse);
        }
        
    };
    
    /**
     * 余额
     */
    public String getBalance() {
        return balance;
    }
    /**
     * 消费积分
     */
    public String getXfjf() {
        return xfjf;
    }
    /**
     * 整体限额
     */
    public String getZtxe() {
        return ztxe;
    }
    /**
     * 整体可用额度
     */
    public String getZtkyxe() {
        return ztkyxe;
    }
    /**
     * 取现限额
     */
    public String getQxxe() {
        return qxxe;
    }
    /**
     * 可用取现额度
     */
    public String getKyqxed() {
        return kyqxed;
    }
    /**
     * 分期额度
     */
    public String getFqed() {
        return fqed;
    }
    /**
     * 分期可用额
     */
    public String getFqkye() {
        return fqkye;
    }

}
