package com.boc.bocma.serviceinterface.op.interfacemodel.remoteopenacconlinecheckold;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

/**
 * 1.2.16 【SA9206】远程开户单笔联网核查接口SV2007
 * 
 * @author gwluo
 * 
 */
public class MAORemoteOpenAccOnLineCheckOldResponseModel extends
		MAOPBaseResponseModel {
	private final String CHECKRESULT = "checkResult";
	private final String ISSUEOFFICE = "issueOffice";
	private final String NAME = "name";
	private final String ID = "id";
	private final String MSGID = "msgId";
	private final String PICID = "piCid";
	private final String PICVER = "picVer";
	/**
	 * 核对结果 String X(2) 必填(见附录2)
	 */
	public String checkResult;
	/**
	 * 签发机关 String X(50) 后补空格
	 */
	public String issueOffice;
	/**
	 * 姓名 String X(30) 后补空格
	 */
	public String name;
	/**
	 * 身份证号码 String X(18) 后补空格
	 */
	public String id;
	/**
	 * 联网核查报文标识号 String X (20) 后补空格
	 */
	public String msgId;
	/**
	 * 影像ID String X(32) 选填 后补空格。如果为空说明客户没有保存过影像，需要新增。
	 */
	public String piCid;
	/**
	 * 影像版本 String X(32) 选填 后补空格
	 */
	public String picVer;

	public MAORemoteOpenAccOnLineCheckOldResponseModel(JSONObject jsonResponse) {
		checkResult = jsonResponse.optString(CHECKRESULT);
		issueOffice = jsonResponse.optString(ISSUEOFFICE);
		name = jsonResponse.optString(NAME);
		id = jsonResponse.optString(ID);
		msgId = jsonResponse.optString(MSGID);
		piCid = jsonResponse.optString(PICID);
		picVer = jsonResponse.optString(PICVER);
	}

	public static final Creator<MAORemoteOpenAccOnLineCheckOldResponseModel> CREATOR = new Creator<MAORemoteOpenAccOnLineCheckOldResponseModel>() {
		@Override
		public MAORemoteOpenAccOnLineCheckOldResponseModel createFromJson(
				JSONObject jsonResponse) throws JSONException {
			return new MAORemoteOpenAccOnLineCheckOldResponseModel(jsonResponse);
		}

	};
}
