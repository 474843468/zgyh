package com.boc.bocma.serviceinterface.op.interfacemodel.queryprovinceandcity;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

/**
 * 查询所属地
 * 
 * @author lxw
 * @version p601添加pageFlag字段输入，用来区分个人信息页面和归属地查询页面，此条件是为了判断查询城市列表。by lgw at
 *          15.12.3
 */
public class MAOPQueryProvinceAndCityParamsModel extends MAOPBaseParamsModel {

	private static final String INTERFACE_URL = "query_province_and_city";

	private final static String distType_const = "distType";
	private final static String distCode_const = "distCode";
	private final static String pageFlag_const = "pageFlag";

	private String distType;
	private String distCode;
	/**
	 * p601 新增 页面标识 String(1) 0-个人信息页面； 1-归属地查询页面。 Y
	 */
	private String pageFlag;

	public String getPageFlag() {
		return pageFlag;
	}

	public void setPageFlag(String pageFlag) {
		this.pageFlag = pageFlag;
	}

	public String getDistType() {
		return distType;
	}

	public void setDistType(String distType) {
		this.distType = distType;
	}

	public String getDistCode() {
		return distCode;
	}

	public void setDistCode(String distCode) {
		this.distCode = distCode;
	}

	@Override
	public String getUrl() {
		return OPURL.getFAABaseTransUrl() + INTERFACE_URL;
	}

	@Override
	public String getJsonBody() throws JSONException {
		JSONObject body = new JSONObject();
		body.put(distType_const, distType);
		body.put(distCode_const, distCode);
		body.put(pageFlag_const, pageFlag);
		return body.toString();
	}

}
