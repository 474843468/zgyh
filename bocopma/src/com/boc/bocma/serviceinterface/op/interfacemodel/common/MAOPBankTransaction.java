package com.boc.bocma.serviceinterface.op.interfacemodel.common;

import android.text.TextUtils;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 存储交易信息的数据类
 */
public class MAOPBankTransaction extends MAOPBaseResponseModel {
	/**
	 * 当前查询账号
	 */
	private static final String ACCNO_KEY = "accno";
	/**
	 * 查询后余额
	 */
	private static final String SUBAMT_KEY = "subamt";
	/**
	 * 交易渠道
	 */
	private static final String CHANNEL_KEY = "channel";
	/**
	 * 网点名称
	 */
	private static final String MERCH_NAME_KEY = "merchname";

    /**
     * 交易日期
     * YYYYMMDD
     */
    private static final String TRAN_DATE_KEY = "trandate";
    /**
     * 交易时间
     * HH24:MI:SS
     */
    private static final String TRAN_TIME_KEY = "trantime";
    /**
     * 币种
     */
    private static final String CURRENCY_KEY = "currency";
    /**
     * 交易金额
     * 有符号位（+/-），无小数位；没有值时返回22位空格（来账为+，往账为-，冲正由网银前端作反向）
     */
    private static final String AMOUNT_KEY = "amt";
    /**
     * 钞汇
     * 借记卡时使用 
     * 1-钞 2-汇 空-不涉及钞或汇
     */
    private static final String BILL_FLAG_KEY = "billflag";
    /**
     * 对方开户名行号(还是对方开户行行号)
     */
    private static final String OPPS_OPEN_NUMBER_KEY = "oppsopeno";
    /**
     * 对方开户行
     */
    private static final String OPPS_OPEN_KEY = "oppsopen";
    /**
     * 对方账户名称
     */
    private static final String OPPS_OPEN_NAME_KEY = "oppsopename";
    /**
     * 对方账户账号
     */
    private static final String OPPS_OPEN_ACCOUNT_KEY = "oppsopenacc";
    /**
     * 业务摘要
     */
    private static final String REMARK_KEY = "remark";
    /**
     * 附言
     */
    private static final String POSTSCRIPT_KEY = "postscript";
    /**
     * 冲正标识 0-非冲正 1-冲正
     */
    private static final String REVERSE_FLAG_KEY = "reveflag";
    
    /**
     * 返回报文中的金额后三位分别为两个小数保留位和一个货币位
     */
    private static final long KEEP_DIGITS = 100;
    
    private double jsonString2Double(String amount) {
        if (TextUtils.isEmpty(amount)) {
            return 0f;
        } else {
            amount = amount.substring(0, amount.length()-1);
            if (TextUtils.isEmpty(amount)) {
                return 0f;
            } else {
                return Double.valueOf(amount)/KEEP_DIGITS;
            }
        }
    }
    
    private MAOPBankTransaction(JSONObject jsonResponse) {
    	accno = jsonResponse.optString(ACCNO_KEY);
        tranDate = jsonResponse.optString(TRAN_DATE_KEY);
        tranTime = jsonResponse.optString(TRAN_TIME_KEY);
        currency = jsonResponse.optString(CURRENCY_KEY);
        amount = jsonString2Double(jsonResponse.optString(AMOUNT_KEY));
        subamt = jsonString2Double(jsonResponse.optString(SUBAMT_KEY));
        billFlag = jsonResponse.optInt(BILL_FLAG_KEY);
        oppsOpenNumber = jsonResponse.optString(OPPS_OPEN_NUMBER_KEY);
        oppsOpen = jsonResponse.optString(OPPS_OPEN_KEY);
        oppsOpenName = jsonResponse.optString(OPPS_OPEN_NAME_KEY);
        oppsOpenAccount = jsonResponse.optString(OPPS_OPEN_ACCOUNT_KEY);
        remark = jsonResponse.optString(REMARK_KEY);
        postscript = jsonResponse.optString(POSTSCRIPT_KEY);
        reverseFlag = jsonResponse.optInt(REVERSE_FLAG_KEY);
        channel = jsonResponse.optString(CHANNEL_KEY);
        merchname = jsonResponse.optString(MERCH_NAME_KEY);
        
    }
    
    public static final Creator<MAOPBankTransaction> CREATOR = new Creator<MAOPBankTransaction>() {
        @Override
        public MAOPBankTransaction createFromJson(JSONObject jsonResponse) throws JSONException {
            return new MAOPBankTransaction(jsonResponse);
        }
        
    };
    
    /**
     * @return 交易日期
     */
    public String getTranDate() {
        return tranDate;
    }
    /**
     * @return 交易时间
     */
    public String getTranTime() {
        return tranTime;
    }
    /**
     * @return 币种
     */
    public String getCurrency() {
        return currency;
    }
    /**
     * @return 交易金额
     */
    public double getAmount() {
        return amount;
    }
    /**
     * @return 钞汇
     */
    public int getBillFlag() {
        return billFlag;
    }
    /**
     * @return 对方开户名行号
     */
    public String getOppsOpenNumber() {
        return oppsOpenNumber;
    }
    /**
     * @return 对方开户行
     */
    public String getOppsOpen() {
        return oppsOpen;
    }
    /**
     * @return 对方账户名称
     */
    public String getOppsOpenName() {
        return oppsOpenName;
    }
    /**
     * @return 对方账户账号
     */
    public String getOppsOpenAccount() {
        return oppsOpenAccount;
    }
    /**
     * @return 业务摘要
     */
    public String getRemark() {
        return remark;
    }
    /**
     * @return 附言
     */
    public String getPostscript() {
        return postscript;
    }
    /**
     * @return 冲正标识 0-非冲正 1-冲正
     */
    public int getReverseFlag() {
        return reverseFlag;
    }
    
    /**
     * @return 当前查询账号
     */
	public String getAccno() {
		return accno;
	}
	 /**
     * @return 交易后余额
     */
	public double getSubamt() {
		return subamt;
	}
	 /**
     * @return 交易渠道
     */
	public String getChannel() {
		return channel;
	}
	 /**
     * @return 网点名称
     */
	public String getMerchname() {
		return merchname;
	}

    private String tranDate;
    private String tranTime;
    private String currency;
    private double amount;
    private int billFlag = -1;
    private String oppsOpenNumber;
    private String oppsOpen;
    private String oppsOpenName;
    private String oppsOpenAccount;
    private String remark;
    private String postscript;
    private int reverseFlag = -1;
    private String accno;
    private double subamt;
    private String channel;
    private String merchname;

    
}
