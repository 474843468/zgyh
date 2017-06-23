package com.boc.bocma.serviceinterface.op.interfacemodel.bindcardwithoutcheck;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

import org.apache.http.client.methods.HttpPut;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 新增用户卡资料（无需验证）
 *
 */
public class MAOPBindCardWithoutCheckParamsModel extends MAOPBaseParamsModel {
    private static final String INTERFACE_URL = "adduserinfo";
    
    private static final String USER_ID_KEY = "userid";
    private static final String ACCOUNT_NUMBER_KEY = "accno";
    private static final String ACCOUNT_TYPE_KEY = "acctyp";
    private static final String ALIAS_KEY = "alias";
    private static final String TM_TYPE_KEY = "trntyp";
    private static final String LIMIT_AMOUNT_KEY = "lmtamt";
    private static final String ETOKEN_VALUE_KEY = "etkval";
    
    @Override
    public String getUrl() {
        return OPURL.getFAABaseTransUrl() + INTERFACE_URL;
    }

    @Override
    public String getMethod() {
        return HttpPut.METHOD_NAME;
    }
    
    @Override
    public String getJsonBody() throws JSONException {
        JSONObject body = new JSONObject();
        body.put(USER_ID_KEY, userId);
        body.put(ACCOUNT_NUMBER_KEY, accountNumber);
        body.put(ACCOUNT_TYPE_KEY, accountType);
        body.put(ALIAS_KEY, alias);
        body.put(TM_TYPE_KEY, trnType);
        body.put(LIMIT_AMOUNT_KEY, limitAmount);
        body.put(ETOKEN_VALUE_KEY, eTokenValue);
        return body.toString();
    }
    
    private String userId;
    /**
     * @param userId 用户ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @param accountNumber 帐号
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * @param accountType 帐号类型 01-借记卡 02-IC卡
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    /**
     * @param alias 别名
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * @param trnType 卡用途分类 01-查询 02-支付
     */
    public void setTrnType(String trnType) {
        this.trnType = trnType;
    }

    /**
     * @param lmtamt 单笔限额
     */
    public void setLimitAmount(String limitAmount) {
        this.limitAmount = limitAmount;
    }

    /**
     * @param etokenValue eToken值
     */
    public void setEtokenValue(String eTokenValue) {
        this.eTokenValue = eTokenValue;
    }

    private String accountNumber;
    private String accountType;
    private String alias;
    private String trnType;
    private String limitAmount;
    private String eTokenValue;

}
