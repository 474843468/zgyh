package com.boc.bocma.serviceinterface.op.interfacemodel.findversion;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

/**
 * 版本检查的结果类
 */
public class MAOPFindVersionModel extends MAOPBaseResponseModel{
	private static final String ERR_CODE_KEY = "errcode";
	private static final String ERR_MSG_KEY = "errmsg";
	private static final String CLIENT_KEY = "clientkey";
	private static final String VERSION_KEY = "version";
	private static final String APP_URL_KEY = "appurl";
	private static final String APP_VERSION_KEY = "appversion";
	private static final String NEED_UPDATE_KEY = "need_update";
	private static final String NEW_FUNCTION_KEY = "new_function";

	public int getErrcode() {
		return errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public int getClientKey() {
		return clientKey;
	}

	public String getVersion() {
		return version;
	}
	
	public String getAppUrl() {
		return appUrl;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public int getNeedUpdate() {
		return needUpdate;
	}

	public String getNewFunction() {
		return newFunction;
	}

	/**
	 * 错误编码类型 
	 */
	 public static final class ErrorType{
		//0-成功
		public static final int SUCCESS = 0;
		//1-连接数据库失败
		public static final int SQL_CONTENT_FALSE = 1;
		//2-系统中查不到这个版本的应用
		public static final int NOT_FIND_APP = 2;
		//3-参数错误
		public static final int PARAMS_FALSE = 3;
	 }

	 /**
	  * 是否需要强制更新类型 
	  */
	 public static final class NeedUpdateType{
		 //1-需要强制更新
		 public static final int NEED = 1;
		 //0-不需要强制更新
		 public static final int NOTNEED = 0;
	 }

	 private int errcode;
	 private String errmsg;
	 private int clientKey;
	 private String version;
	 private String appUrl;
	 private String appVersion;
	 private int needUpdate;
	 private String newFunction;

	 public MAOPFindVersionModel(JSONObject jsonResponse){
		 errcode = jsonResponse.optInt(ERR_CODE_KEY);
		 errmsg = jsonResponse.optString(ERR_MSG_KEY);
		 clientKey = jsonResponse.optInt(CLIENT_KEY);
		 version = jsonResponse.optString(VERSION_KEY);
		 appUrl = jsonResponse.optString(APP_URL_KEY);
		 //调整url查询参数，自动下载APK
		 appUrl = appUrl.replaceFirst("act=appdownload", "act=download");
		 appVersion = jsonResponse.optString(APP_VERSION_KEY);
		 needUpdate = jsonResponse.optInt(NEED_UPDATE_KEY);
		 newFunction = jsonResponse.optString(NEW_FUNCTION_KEY);
	 }

	 public static final Creator<MAOPFindVersionModel> CREATOR = new Creator<MAOPFindVersionModel>() {
		 @Override
		 public MAOPFindVersionModel createFromJson(JSONObject jsonResponse) throws JSONException {
			 return new MAOPFindVersionModel(jsonResponse);
		 }
	 };
}
