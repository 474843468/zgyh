package com.chinamworld.bocmbci.biz.setting.accmanager;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;

import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.biz.setting.SettingBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.log.LogGloble;

/**
 * 账户管家的基类
 * 
 * @author xyl
 * 
 */
public class AccountManagerBaseActivity extends SettingBaseActivity {
	private static final String TAG = "AccountManagerBaseActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * 账户管家查看所有账号
	 * 
	 * @Author xyl
	 */
	protected void queryAccounts() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.SET_QUERYACCOUNTS);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "queryAccountsCallback");
		LogGloble.d(TAG, "requestMethod ==" +Setting.SET_QUERYACCOUNTS);
	}

	/**
	 * 
	 * @Author xyl
	 * @param resultObj
	 */
	public void queryAccountsCallback(Object resultObj) {
		LogGloble.d(TAG, "back ==" +Setting.SET_QUERYACCOUNTS);
	}


	/**
	 * 修改账户别名
	 * 
	 * @Author xyl
	 * @param accountId
	 *            账号表示
	 * @param accountName
	 *            账号名
	 * @param accType
	 *            账户类型
	 * @param accountNickName
	 *            账户别名
	 * @param token
	 *            防重机制
	 */
	protected void editAccAlias(String accountId, String accountName,
			String accType, String accountNickName, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.SET_EDITACCALIAS);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Setting.SET_EDITACCALIAS_ACCOUNTID, accountId);
		map.put(Setting.SET_EDITACCALIAS_ACCOUNTNAME, accountName);
		map.put(Setting.SET_EDITACCALIAS_ACCOUNTNICKNAME, accountNickName);
		map.put(Setting.TOKEN, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "eidtAccAliasCallback");
		LogGloble.d(TAG, "request ==" +Setting.SET_EDITACCALIAS);
	}

	/**
	 * 修改账户别名结果处理
	 * 
	 * @Author xyl
	 * @param resultObj
	 *            结果
	 */
	public void eidtAccAliasCallback(Object resultObj) {
		LogGloble.d(TAG, "back ==" +Setting.SET_EDITACCALIAS);
	}

	/**
	 * 查询默认账户
	 * @param mobileNum
	 */
	public void queryDefaultAcc(String mobileNum) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.SET_QUERYDEFAULTACCOUNT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Setting.SET_QUERYDEFAULTACCOUNT_MOBILENUM, mobileNum);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "queryDefaultAccCallback");
	}

	/**
	 * 查询默认账户  回调
	 * @param resultObj
	 */
	public void queryDefaultAccCallback(Object resultObj) {
	}
	
	/**
	 * 设定默认账户  
	 * @param mobileNum
	 * @param accountId
	 */
	public void setDefaultAcc(String mobileNum,String accountId,String tokenId)  {
		BiiRequestBody biiRequestBody = new BiiRequestBody(); 
		biiRequestBody.setMethod(Setting.SET_SETDEFAULTACCOUNT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Setting.SET_QUERYDEFAULTACCOUNT_MOBILENUM, mobileNum);
		map.put(Setting.SET_QUERYDEFAULTACCOUNT_ACCOUTID, accountId);
		map.put(Setting.TOKEN, tokenId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "setDefaultAccCallback");
	}
	/**
	 * 设定默认账户   回调
	 * @param resultObj
	 */
	public void setDefaultAccCallback(Object resultObj) {
	}
}