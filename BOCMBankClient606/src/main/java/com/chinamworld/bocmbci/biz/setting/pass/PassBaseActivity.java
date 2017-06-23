package com.chinamworld.bocmbci.biz.setting.pass;

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
import com.chinamworld.bocmbci.utils.SipBoxUtils;

public class PassBaseActivity extends SettingBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	/**
	 * 判断客户是否使用安全工具
	 */
	protected void sVRPasswordChoose() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.SET_PWD_ACC_TYPE);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this,
				"sVRPasswordChooseCallBack");
	}
	
	public void sVRPasswordChooseCallBack(Object resultObj) {
	}

	/**
	 * 登录密码修改确认
	 * 
	 * @Author xyl
	 * @param oldPass
	 *            旧密码
	 * @param newPass1
	 *            新密码
	 * @param newPass2
	 *            新密码确认
	 * @param combinId
	 *            安全因子组合
	 */
	protected void settingLoginPassConfirm(String oldPass, String oldPass_Rc,
			String newPass1, String newPass_RC, String newPass2,
			String newPass2_RC, String combinId,String converSationId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.SET_PWDCOFIRM);
		biiRequestBody.setConversationId(converSationId);
		Map<String, Object> map = new HashMap<String, Object>();
		// 修改密码确认
		map.put(Setting.SET_PWDCOFIRM_OLDPASS, oldPass);
		map.put(Setting.SET_PWDCOFIRM_OLDPASS_RC, oldPass_Rc);
		map.put(Setting.SET_PWDCOFIRM_NEWPASS, newPass1);
		map.put(Setting.SET_PWDCOFIRM_NEWPASS_RC, newPass_RC);
		map.put(Setting.SET_PWDCOFIRM_NEWPASS2, newPass2);
		map.put(Setting.SET_PWDCOFIRM_NEWPASS2_RC, newPass2_RC);
		map.put(Setting.SET_PWDCOFIRM_COMBINID, combinId);
		// 防欺诈信息
		GetPhoneInfo.addPhoneInfo(map);
		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"settingLoginPassConfirmCallBack");
	}
	/**
	 * 查询版用户 修改密码 
	 * @param oldPass
	 * @param oldPass_Rc
	 * @param newPass1
	 * @param newPass_RC
	 * @param newPass2
	 * @param newPass2_RC
	 * @param converSationId
	 */
	protected void settingLoginPassQueryResult(String oldPass, String oldPass_Rc,
			String newPass1, String newPass_RC, String newPass2,
			String newPass2_RC,String converSationId,String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.SET_PWD_QUERY_RESET);
		biiRequestBody.setConversationId(converSationId);
		Map<String, Object> map = new HashMap<String, Object>();
		// 修改密码确认
		map.put(Setting.SET_PWDCOFIRM_OLDPASS, oldPass);
		map.put(Setting.SET_PWDCOFIRM_OLDPASS_RC, oldPass_Rc);
		map.put(Setting.SET_PWDCOFIRM_NEWPASS, newPass1);
		map.put(Setting.SET_PWDCOFIRM_NEWPASS_RC, newPass_RC);
		map.put(Setting.SET_PWDCOFIRM_NEWPASS2, newPass2);
		map.put(Setting.SET_PWDCOFIRM_NEWPASS2_RC, newPass2_RC);
		map.put(Setting.TOKEN, tokenId);
		// 防欺诈信息
		GetPhoneInfo.addPhoneInfo(map);
		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"settingLoginPassQueryResultCallBack");
	}
	public void settingLoginPassQueryResultCallBack(Object resultObj) {
	}

	/**
	 * 登录密码确认 修改 结果处理
	 * 
	 * @Author xyl
	 * @param resultObj
	 *            返回的结果
	 */
	public void settingLoginPassConfirmCallBack(Object resultObj) {
	}

	/**
	 * 登录密码提交修改
	 * 
	 * @Author xyl
	 * @param otp
	 *            动态口令
	 * @param smc
	 *            手机验证码
	 * @param token
	 *            防重机制
	 */
	protected void settingLoginPass(String otp, String smc, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.SET_PWD_RESULT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Setting.SET_PWD_RESULT_Otp, otp);
		map.put(Setting.SET_PWD_RESULT_SMC, smc);
		map.put(Setting.TOKEN, token);
		SipBoxUtils.setSipBoxParams(map);
		// 防欺诈信息
		GetPhoneInfo.addPhoneInfo(map);
		biiRequestBody.setParams(map);
		HttpManager
				.requestBii(biiRequestBody, this, "settingLoginPassCallBack");
	}

	/**
	 * 登录密码提交修改 返回结果处理
	 * 
	 * @Author xyl
	 * @param resultObj
	 *            返回结果
	 */
	public void settingLoginPassCallBack(Object resultObj) {
	}
}
