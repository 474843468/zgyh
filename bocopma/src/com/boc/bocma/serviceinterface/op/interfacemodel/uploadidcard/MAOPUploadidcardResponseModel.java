package com.boc.bocma.serviceinterface.op.interfacemodel.uploadidcard;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

/**
 * 1.2.9 【SA9192】身份证图像上传
 * 
 * @author gwluo
 * 
 */
public class MAOPUploadidcardResponseModel extends MAOPBaseResponseModel {

	private final static String PICVERSION = "picVersion";
	/**
	 * 图像版本号 X（10） 正常返回本次上送的图像版本号，异常返回错误信息！
	 */
	public String picVersion;

	public MAOPUploadidcardResponseModel(JSONObject jsonResponse) {
		picVersion = jsonResponse.optString(PICVERSION);
	}

	public static final Creator<MAOPUploadidcardResponseModel> CREATOR = new Creator<MAOPUploadidcardResponseModel>() {
		@Override
		public MAOPUploadidcardResponseModel createFromJson(
				JSONObject jsonResponse) throws JSONException {
			return new MAOPUploadidcardResponseModel(jsonResponse);
		}

	};
}
