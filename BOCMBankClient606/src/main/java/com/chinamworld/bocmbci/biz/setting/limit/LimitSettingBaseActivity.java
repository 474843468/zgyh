package com.chinamworld.bocmbci.biz.setting.limit;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;

import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.biz.setting.SettingBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.GetPhoneInfo;
import com.chinamworld.bocmbci.http.HttpManager;

public class LimitSettingBaseActivity extends SettingBaseActivity {
	private static final String TAG = "SettingBaseActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		GetPhoneInfo.initActFirst(this);
		super.onCreate(savedInstanceState);

	}

	/**
	 * 查询限额设定信息
	 */
	protected void queryLimit() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.SET_QUERYLIMIT);
		biiRequestBody.setConversationId((String)BaseDroidApp
				.getInstanse()
				.getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(null);
		HttpManager
				.requestBii(biiRequestBody, this, "queryLimitCallback");
	}
	/**
	 *  查询限额设定 回调处理
	 *@Author xyl 
	 *@param resultObj 限额设定
	 */
	public void queryLimitCallback(Object resultObj) {
	}

	/**
	 * 交易限额设定
	 *@Author xyl 
	 *@param limit   服务ID_限额,例如   PB021_500.00|PB022_500.00
	 *@param otp     动态口令码
	 *@param tokenId 防重机制
	 */
//	protected void editLimit() {
////		BiiRequestBody biiRequestBody = new BiiRequestBody();
////		biiRequestBody.setMethod(Setting.SET_EDITLIMIT);
////		biiRequestBody.setConversationId((String)BaseDroidApp
////				.getInstanse()
////				.getBizDataMap()
////				.get(ConstantGloble.CONVERSATION_ID));
////		Map<String,Object> map = new HashMap<String, Object>();
////		map.put(Setting.SET_EDITLIMIT_PSNLIMIT, limit);
////		map.put(Setting.SET_EDITLIMIT_Otp, otp);
////		map.put(Setting.TOKEN, tokenId);
////		//防欺诈信息
////		GetPhoneInfo.addPhoneInfo(map);
////		biiRequestBody.setParams(map);
////		HttpManager
////				.requestBii(biiRequestBody, this, "editLimitCallback");
//	}
//	/**
//	 * 交易限额设定 回调处理
//	 *@Author xyl 
//	 *@param resultObj 返回的结果
//	 */
//	public void editLimitCallback(Object resultObj) {
//	}
	/**
	 * 交易限额设定预交易
	 *@Author xyl 
	 *@param limit
	 *@param combindId
	 */
	protected void editLimitConfirm(String limit,String combindId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.SET_EDITLIMITCONFIRM);
		
		biiRequestBody.setConversationId(settingControl.editLimitConversationId);
		Map<String,String> map = new HashMap<String, String>();
		map.put(Setting.SET_EDITLIMITCONFIRM_PSNLIMIT, limit);
		map.put(Setting.SET_EDITLIMITCONFIRM_CONBINID, combindId);
		biiRequestBody.setParams(map);
		HttpManager
				.requestBii(biiRequestBody, this, "editLimitConfirmCallback");
	}
	/**
	 * 交易限额舍得你该与交易返回结果
	 *@Author xyl 
	 *@param resultObj 返回结果
	 */
	public void editLimitConfirmCallback(Object resultObj) {
	}
	
}
