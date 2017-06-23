package com.chinamworld.bocmbci.biz.setting.setacct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.setting.SettingBaseActivity;
import com.chinamworld.bocmbci.biz.setting.SettingUtils;
import com.chinamworld.bocmbci.biz.setting.adapter.SettingAccountAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 设置默认账户
 * @author panwe
 *
 */
public class SettingAccountActivity extends SettingBaseActivity{
	
	private View mMainView;
	private Spinner mSpinner;
	private ListView mListView;
	private SettingAccountAdapter mAdapter;
	private int selectposition = -1;
	private String loginMobile;
	private List<Map<String, Object>> acctList;
	private boolean first = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addViews();
		initViews();
		getCustInfo();
		requestAllMobileNum();
	}
	
	@SuppressWarnings("unchecked")
	private void getCustInfo(){
		Map<String, Object> logInfo = (Map<String, Object>) BaseDroidApp
				.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA);
		loginMobile = (String)logInfo.get(Comm.LOGINNAME);
	}
	
	private void addViews(){
		mMainView = View.inflate(this, R.layout.setting_account, null);
		setTitle(R.string.setting_title);
		right.setVisibility(View.GONE);
		tabcontent.addView(mMainView);
	}
	
	private void initViews(){
		mListView = (ListView) mMainView.findViewById(R.id.cardlist);
		mSpinner = (Spinner)mMainView.findViewById(R.id.mobile);
		mSpinner.setOnItemSelectedListener(itemSelectClick);
		mAdapter = new SettingAccountAdapter(this, acctList);
		mListView.setOnItemClickListener(itemOnClick);
		mListView.setAdapter(mAdapter);
	}
	
	private void setListViewDefualtSelecte(String defualtAcct){
		if (StringUtil.isNull(defualtAcct)) {
			selectposition = -1;
			mAdapter.setSelectedPosition(-1); return;
		}
		for (int i = 0; i < acctList.size(); i++) {
			if (defualtAcct.equals((acctList).get(i).get(Comm.ACCOUNTNUMBER))) {
				selectposition = i;
				mAdapter.setSelectedPosition(i);
				return;
			}
		}
	}
	
	private void setSpinnerSelect(List<String> list){
		for (int i = 0; i < list.size(); i++) {
			if (loginMobile.equals((list).get(i))) {
				mSpinner.setSelection(i);
				return;
			}
		}
	}
	
	private List<String> initMobileList(List<String> list){
		boolean hasLoginMobile = false;
		if (!StringUtil.isNullOrEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				if (loginMobile.equals((list).get(i))) {
					hasLoginMobile = true;
				}
			}
		}
		if (hasLoginMobile) {
			return list;
		}
		ArrayList<String> newList = new ArrayList<String>();
		newList.add(loginMobile);
		newList.addAll(list);
		return newList;
	}
	
	private OnItemSelectedListener itemSelectClick = new OnItemSelectedListener(){
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			if (first) {
				first = false; return;
			}
			BaseHttpEngine.showProgressDialog();
			requestDefaultAccount(mSpinner.getSelectedItem().toString());
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	};
	
	/** 列表点击事件 **/
	private OnItemClickListener itemOnClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (selectposition != position) {
				selectposition = position;
				mAdapter.setSelectedPosition(selectposition);
			}
		}
	};
	
	/** 数据返回异常 **/
	private OnClickListener errorClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissErrorDialog();
			finish();
		}
	};
	
	/**
	 * 确认操作
	 * @param v
	 */
	public void confirmOnclick(View v){
		if (submitCheck()) {
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
		}
	}
	
	/** 提交校验  */
	private boolean submitCheck(){
		if (selectposition == -1) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.setting_noacct_error)); return false;
		}
		return true;
	}
	
	/** 请求所有手机号  */
	private void requestAllMobileNum(){
		BaseHttpEngine.showProgressDialogCanGoBack();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.METHOD_QUERYMOBILE);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "allMobileNumCallBack");
	}

	@SuppressWarnings("unchecked")
	public void allMobileNumCallBack(Object resultObj) {
		List<String> mList = initMobileList((List<String>) this.getHttpTools().getResponseResult(resultObj));
		SettingUtils.initSpinnerView(this, mSpinner, mList);
		if (mList != null && mList.size() == 1) {
			mSpinner.setClickable(false);
		}
		setSpinnerSelect(mList);
		requestAllAccount();
	}
	
	/**
	 * 请求默认账户
	 * @param mobile
	 */
	private void requestDefaultAccount(String mobile){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.METHOD_QUERYDEFAULTACCT);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Setting.MOBILE, mobile);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "defaultAccountCallBack");
		
	}
	
	@SuppressWarnings("unchecked")
	public void defaultAccountCallBack(Object resultObj) {
		String defualtAcct = null;
		Map<String, Object> mMap = (Map<String, Object>)this.getHttpTools().getResponseResult(resultObj);
		if (!StringUtil.isNullOrEmpty(mMap) && mMap.get(Setting.STATUS).equals("01")) {
			defualtAcct = (String) mMap.get(Comm.ACCOUNTNUMBER);
		}
		BaseHttpEngine.dissMissProgressDialog();
		setListViewDefualtSelecte(defualtAcct);
	}
	
	/** 请求所有账户  */
	public void requestAllAccount() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.QRY_ALL_BANK_ACCOUNT);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Comm.ACCOUNT_TYPE, SafetyDataCenter.accountTypeList);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "allAccountCallBack");
	}

	@SuppressWarnings("unchecked")
	public void allAccountCallBack(Object resultObj) {
		acctList = (List<Map<String, Object>>) this.getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(acctList)) {
			BaseDroidApp.getInstanse().showMessageDialog(getString(R.string.setting_comm_error), errorClick);return;
		}
		mAdapter.setData(acctList);
		requestDefaultAccount(loginMobile);
	}
	
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		pSNGetTokenId();
	}
	
	/** 请求token */
	public void pSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "aquirePSNGetTokenId");
	}
	
	public void aquirePSNGetTokenId(Object resultObj) {
		String tokenId = (String) this.getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		requestDefualSetSubmit(tokenId);
	}
	
	/**
	 * 请求设置默认账户
	 * @param token
	 */
	private void requestDefualSetSubmit(String token){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.METHOD_DEFAULTSETSUBMIT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Setting.TOKEN, token);
		params.put(Setting.MOBILE, mSpinner.getSelectedItem().toString().trim());
		params.put(Comm.ACCOUNT_ID, acctList.get(selectposition).get(Comm.ACCOUNT_ID));
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "setSubmitCallBack");
	}
	
	public void setSubmitCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Intent mIntent = new Intent(this, SettingAccountResultActivity.class);
		mIntent.putExtra(Setting.MOBILE, mSpinner.getSelectedItem().toString());
		mIntent.putExtra(Comm.ACCOUNTNUMBER, (String)acctList.get(selectposition).get(Comm.ACCOUNTNUMBER));
		startActivity(mIntent);
	}
}
