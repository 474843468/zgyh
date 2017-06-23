package com.boc.bocma.serviceinterface.op.interfacemodel.queryopeningbank;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

public class MAOPQueryOpeningBankParamsModel extends MAOPBaseParamsModel {
	
	private static final String INTERFACE_URL = "queryopeningbank";
	
	private final static String othCardBankName_const = "othCardBankName";
	private final static String othCardTopCnaps_const = "othCardTopCnaps";
	private final static String pageSize_const = "pageSize";
	private final static String pageNum_const = "pageNum";


	
	public String getOthCardBankName() {
		return othCardBankName;
	}

	public void setOthCardBankName(String othCardBankName) {
		this.othCardBankName = othCardBankName;
	}

	public String getOthCardTopCnaps() {
		return othCardTopCnaps;
	}

	public void setOthCardTopCnaps(String othCardTopCnaps) {
		this.othCardTopCnaps = othCardTopCnaps;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	private String othCardBankName;
	private String othCardTopCnaps;
	private String pageSize;
	private String pageNum;

	@Override
	public String getUrl() {
		return OPURL.getFAABaseTransUrl() + INTERFACE_URL;
	}

	@Override
	public String getJsonBody() throws JSONException {
		 JSONObject body = new JSONObject();
	        body.put(othCardBankName_const, othCardBankName);
	        body.put(othCardTopCnaps_const, othCardTopCnaps);
	        body.put(pageSize_const, pageSize);
	        body.put(pageNum_const, pageNum);
	        return body.toString();
	}

}
