package com.boc.bocma.serviceinterface.op.interfacemodel.notbillquery;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 未出账单查询参数类
 */
public class MAOPNotBillQueryParamsModel extends MAOPBaseParamsModel {
    private static final String INTERFACE_URL = "notbillquery";
    
    private static final String USER_ID_KEY = "userid";
    private static final String LIMIT_AMT_KEY = "limitamt";
    private static final String CURRENCY_KEY = "currency";
    private static final String START_NUMBER_KEY = "stnum";
    private static final String SELECT_NUMBER_KEY = "selnum";
    
    @Override
    public String getUrl() {
        return OPURL.getFAABaseTransUrl() + INTERFACE_URL;
    }

    @Override
    public String getJsonBody() throws JSONException {
        JSONObject body = new JSONObject();
        body.putOpt(USER_ID_KEY, userId);
        body.putOpt(LIMIT_AMT_KEY, lmtamt);
        body.putOpt(CURRENCY_KEY, currency);
        body.putOpt(LIMIT_AMT_KEY, lmtamt);
        body.putOpt(START_NUMBER_KEY, startNumber);
        body.putOpt(SELECT_NUMBER_KEY, selectNumber);
        return body.toString();
    }
    
    /**
     * @param userId 用户ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @param lmtamt 唯一标识
     */
    public void setLmtamt(String lmtamt) {
        this.lmtamt = lmtamt;
    }

    /**
     * @param currency 币种
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * @param startNumber 开始条数（查询交易内容的起始位置）
     */
    public void setStartNumber(String startNumber) {
        this.startNumber = startNumber;
    }

    /**
     * @param selectNumber 选择条数（本次查询交易需要返回的结果数量）
     */
    public void setSelectNumber(String selectNumber) {
        this.selectNumber = selectNumber;
    }

    private String userId;
    private String lmtamt;
    private String currency;
    private String startNumber;
    private String selectNumber;

}
