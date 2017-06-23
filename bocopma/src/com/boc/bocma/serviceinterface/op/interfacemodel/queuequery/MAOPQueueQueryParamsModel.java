package com.boc.bocma.serviceinterface.op.interfacemodel.queuequery;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 预约取号-排队情况查询参数类
 */
public class MAOPQueueQueryParamsModel extends MAOPBaseParamsModel {
    private static final String INTERFACE_URL = "unlogin/qmas/numberQuery";
    
    /**
     * 网点机构号
     */
    private static final String BRANCH_NUMBER_KEY = "branch_no";

    private String branchNumber;
    
    /**
     * @param branchNumber 网点机构号
     */
    public void setBranchNumber(String branchNumber) {
        this.branchNumber = branchNumber;
    }

    @Override
    public String getUrl() {
        return OPURL.getFAABaseTransUrl() + INTERFACE_URL;
    }

    @Override
    public String getJsonBody() throws JSONException {
        JSONObject body = new JSONObject();
        body.put(BRANCH_NUMBER_KEY, branchNumber);
        return body.toString();
    }

}
