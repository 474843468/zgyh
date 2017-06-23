package com.boc.bocma.serviceinterface.op.interfacemodel.querykeywords;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 查询关键字参数类
 */
public class MAOPQueryKeywordsParamsModel extends MAOPBaseParamsModel {

    private static final String INTERFACE_URL = "faa/keywords";
    
    private static final String START_ID_KEY = "startid";
    private static final String END_ID_KEY = "endid";
    
    /**
     * 每次可查询最大条数
     */
    public static final long MAX_LENGTH = 500;

    @Override
    public String getUrl() {
        return OPURL.getFAABaseTransUrl() + INTERFACE_URL;
    }

    @Override
    public String getJsonBody() throws JSONException {
        JSONObject body = new JSONObject();
        body.put(START_ID_KEY, startId);
        body.put(END_ID_KEY, endId);
        return body.toString();
    }

    /**
     * 开始条数，从1开始
     * @param startId
     */
    public void setStartId(long startId) {
        this.startId = startId;
        endId = startId + MAX_LENGTH;
    }

    private long startId;

    private long endId;
}
