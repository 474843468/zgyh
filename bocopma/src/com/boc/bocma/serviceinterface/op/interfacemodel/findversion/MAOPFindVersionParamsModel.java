package com.boc.bocma.serviceinterface.op.interfacemodel.findversion;


import java.util.LinkedHashMap;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

/**
 * 版本检查的参数类
 */
public class MAOPFindVersionParamsModel extends MAOPBaseParamsModel {
	private static final String INTERFACE_URL = "findVersion";
    private static final String CHANNEL_KEY = "channel";
    private static final String CHANNEL_VALUE = "android";
    
	@Override
	public String getUrl() {
		return OPURL.getFAABaseTransUrl() + INTERFACE_URL;
	}

	@Override
	public String getJsonBody() throws JSONException {
	    JSONObject body = new JSONObject();
        body.put(CHANNEL_KEY, CHANNEL_VALUE);
		return body.toString();
	}
	
	@Override
	public String getMethod() {
        return HttpGet.METHOD_NAME;
    }
	
	@Override
	public LinkedHashMap<String, String> getHead() {
		LinkedHashMap<String , String > findversion = new LinkedHashMap<String, String>();
		findversion.put(CHANNEL_KEY, CHANNEL_VALUE);
        return findversion;
    }
	
}
