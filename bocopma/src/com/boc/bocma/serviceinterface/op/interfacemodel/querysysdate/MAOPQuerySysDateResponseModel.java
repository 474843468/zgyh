package com.boc.bocma.serviceinterface.op.interfacemodel.querysysdate;


import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

/**
 * 获取系统日期返回数据
 * @author lxw
 *
 */
public class MAOPQuerySysDateResponseModel extends MAOPBaseResponseModel {

	private static final String sysdate_const = "sysdate";
	private String sysdate;
	
	public String getSysdate() {
		return sysdate;
	}

	public void setSysdate(String sysdate) {
		this.sysdate = sysdate;
	}

	public MAOPQuerySysDateResponseModel(JSONObject jsonResponse) {
		sysdate = jsonResponse.optString(sysdate_const);
	}

	public static final Creator<MAOPQuerySysDateResponseModel> CREATOR = new Creator<MAOPQuerySysDateResponseModel>() {
		@Override
		public MAOPQuerySysDateResponseModel createFromJson(
				JSONObject jsonResponse) throws JSONException {
			return new MAOPQuerySysDateResponseModel(jsonResponse);
		}

	};
    

}
