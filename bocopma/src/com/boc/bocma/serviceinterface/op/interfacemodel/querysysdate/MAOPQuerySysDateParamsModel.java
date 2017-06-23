package com.boc.bocma.serviceinterface.op.interfacemodel.querysysdate;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

/**
 * 获取系统日期请求参数
 * @author lxw
 *
 */
public class MAOPQuerySysDateParamsModel extends MAOPBaseParamsModel {
	
	private static final String INTERFACE_URL = "querysysdate";

	@Override
	public String getUrl() {
		return OPURL.getFAABaseTransUrl() + INTERFACE_URL;
	}

	@Override
	public String getJsonBody() throws JSONException {
		 JSONObject body = new JSONObject();

	        return body.toString();
	}

}
