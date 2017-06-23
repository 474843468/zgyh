package com.boc.bocma.serviceinterface.op.interfacemodel.yetbillquery;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

import org.json.JSONException;
import org.json.JSONObject;

public class MAOPYetBillQueryParamsModel extends MAOPBaseParamsModel {
    private static final String INTERFACE_URL = "yetbillquery";
    
    private static final String USER_ID_KEY = "userid";
    private static final String LIMIT_AMT_KEY = "limitamt";
    private static final String CURRENCY_KEY = "currency";
    private static final String ACC_DATE_KEY = "accdate";
    private static final String START_NUMBER_KEY = "stnum";
    private static final String SELECT_NUMBER_KEY = "selnum";
    
    @Override
    public String getUrl() {
        return OPURL.getFAABaseTransUrl() + INTERFACE_URL;
    }

    @Override
    public String getJsonBody() throws JSONException {
        JSONObject body = new JSONObject();
        body.put(USER_ID_KEY, userId);
        body.put(LIMIT_AMT_KEY, limitamt);
        body.putOpt(CURRENCY_KEY, currency);
        body.put(ACC_DATE_KEY, accDate);
        body.put(SELECT_NUMBER_KEY, selectNumber);
        body.put(START_NUMBER_KEY, startNumber);
        return body.toString();
    }

    /**
     * @param userId 用户ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @param limitamt 唯一标识
     */
    public void setLimitamt(String limitamt) {
        this.limitamt = limitamt;
    }

    /**
     * @param currency 币种
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * @param accDate 账单日期 YYYYMM
     */
    public void setAccDate(String accDate) {
        this.accDate = accDate;
    }

    /**
     * @param startNumber 开始条数 查询交易内容的起始位置
     */
    public void setStartNumber(String startNumber) {
        this.startNumber = startNumber;
    }

    /**
     * @param selectNumber 选择条数 本次查询交易需要返回的结果数量
     */
    public void setSelectNumber(String selectNumber) {
        this.selectNumber = selectNumber;
    }

    private String userId;
    private String limitamt;
    private String currency;
    private String accDate;
    private String startNumber;
    private String selectNumber;
}
