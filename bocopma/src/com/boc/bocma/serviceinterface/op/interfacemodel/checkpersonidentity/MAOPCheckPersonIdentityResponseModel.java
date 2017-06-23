package com.boc.bocma.serviceinterface.op.interfacemodel.checkpersonidentity;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

/**
 * @author
 * @version p601 增加 serviceResponse，responseCode，responseMsg by lgw 15.12.3<br>
 *          x64增加八个字段 by lgw 16.3.8
 */
public class MAOPCheckPersonIdentityResponseModel extends MAOPBaseResponseModel {

	private static final String UUID_CONST = "uuid";
	private static final String SERVICE_RESPONSE = "serviceResponse";
	private static final String RESPONSE_CODE = "responseCode";
	private static final String RESPONSE_MSG = "responseMsg";
	private static final String CARD2ND = "card2nd";
	private static final String CARD3RD = "card3rd";
	private static final String CARDOTH = "cardOth";
	private static final String OPENTYPEPRE = "openTypePre";
	private static final String REUUID1 = "reUuid1";
	private static final String REUUID2 = "reUuid2";
	private static final String PAGETYPE = "pageType";
	private static final String NAME = "name";
	private static final String PICID = "piCid";
	private static final String OPERTYPE = "operType";
	// private static final String PAGEFLAG = "pageFlag";
	// private static final String BOCFLAG = "bocFlag";

	public String uuid;
	public String serviceResponse;// jsonObject其中包含responseCode和responseMsg
	/**
	 * X(7) 0000000:交易成功 9999999:系统异常 1000001:必输项不能为空 1000002:数据校验失败
	 * 1000003:查询结果为空 当返回码为1000083时，需要调用重新发送接口。
	 */
	public String responseCode;

	/**
	 * X(100) 必输
	 */
	public String responseMsg;
	/**
	 * 二类账户账号 X(32)
	 */
	public String card2nd;
	/**
	 * 三类账户账号 X(32)
	 */
	public String card3rd;
	/**
	 * 绑定卡卡号 X(32)
	 */
	public String cardOth;
	/**
	 * 原业务类型 X(2)
	 */
	public String openTypePre;
	/**
	 * 待重发二（二、三同时）类账户开户申请uuid X(32)
	 */
	public String reUuid1;
	/**
	 * 待重发三类账户开户申请uuid X(32)
	 */
	public String reUuid2;
	/**
	 * 跳转页面 String(1) N 2身份证照片上传 3绑定卡信息
	 */
	public String pageType;
	/**
	 * 客户姓名 String(26) N 如果是中行客户需要给前端返回姓名
	 */
	public String name;
	/**
	 * 影像ID String(32) Y 分配的影像ID
	 */
	public String piCid;
	/**
	 * 影像操作类型 String(1) Y 影像操作类型 A新增 E修改
	 */
	public String operType;

	// /**
	// * 跳转标识 X(1) 0身份证照片上传 1绑定卡信息
	// */
	// private String pageFlag;
	// /**
	// * 中行客户标识 X(1) 0中行客户 1非中行客户
	// */
	// private String bocFlag;

	public MAOPCheckPersonIdentityResponseModel(JSONObject jsonResponse) {
		uuid = jsonResponse.optString(UUID_CONST);
		card2nd = jsonResponse.optString(CARD2ND);
		card3rd = jsonResponse.optString(CARD3RD);
		cardOth = jsonResponse.optString(CARDOTH);
		openTypePre = jsonResponse.optString(OPENTYPEPRE);
		reUuid1 = jsonResponse.optString(REUUID1);
		reUuid2 = jsonResponse.optString(REUUID2);
		pageType = jsonResponse.optString(PAGETYPE);
		name = jsonResponse.optString(NAME);
		piCid = jsonResponse.optString(PICID);
		operType = jsonResponse.optString(OPERTYPE);
		// pageFlag = jsonResponse.optString(PAGEFLAG);
		// bocFlag = jsonResponse.optString(BOCFLAG);
		serviceResponse = jsonResponse.optString(SERVICE_RESPONSE);
		JSONObject serviceResponseObject = jsonResponse
				.optJSONObject(SERVICE_RESPONSE);
		responseCode = serviceResponseObject.optString(RESPONSE_CODE);
		responseMsg = serviceResponseObject.optString(RESPONSE_MSG);
	}

	public static final Creator<MAOPCheckPersonIdentityResponseModel> CREATOR = new Creator<MAOPCheckPersonIdentityResponseModel>() {
		@Override
		public MAOPCheckPersonIdentityResponseModel createFromJson(
				JSONObject jsonResponse) throws JSONException {
			return new MAOPCheckPersonIdentityResponseModel(jsonResponse);
		}

	};
}
