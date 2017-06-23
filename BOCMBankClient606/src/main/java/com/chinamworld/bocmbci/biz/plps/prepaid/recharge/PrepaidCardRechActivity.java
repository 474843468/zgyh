package com.chinamworld.bocmbci.biz.plps.prepaid.recharge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.PlpsUtils;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrepaidCardRechActivity extends PlpsBaseActivity implements OnItemSelectedListener{

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** 预付卡类型下拉框 */
	private Spinner spPripaidType;
	/** 预付卡号 */
	private EditText etPripaidCarNum;
	/** 再次输入预付卡号 */
	private EditText etPripaidCarNumAgain;
	/** 姓名 */
	private EditText etName;
	/** 选择账户下拉框 */
	private Spinner spAccount;
	/** 充值金额 */
	private EditText etTransValue;
	/** 加密类型-动态口令 */
	private String otp;
	/** 加密类型-短信 */
	private String smc;
	/**银行账户*/
	private List<Map<String, Object>> cardList = new ArrayList<Map<String,Object>>();
	/**过滤信用卡*/
	private List<Map<String, Object>> nocardList = new ArrayList<Map<String,Object>>();
	//选择银行账户是否重新初始化
//	private Boolean isRefresh = true;
	//预付卡类型
	String PrepaidCardTypeName = null;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.plps_prepaid_card_replenishment);
		setTitle("预付卡充值");
		initView();
	}

	/** 初始化视图 */
	private void initView() {
		spPripaidType = (Spinner) findViewById(R.id.sp_pripaidType);
		etPripaidCarNum = (EditText) findViewById(R.id.et_pripaidCarNum);
		etPripaidCarNumAgain = (EditText) findViewById(R.id.et_pripaidCarNumAgain);
		etName = (EditText) findViewById(R.id.et_Name);
		spAccount = (Spinner) findViewById(R.id.sp_account);
		etTransValue = (EditText) findViewById(R.id.et_transValue);

		EditTextUtils.setLengthMatcher(this, etName, 30);

//		List<Map<String, Object>> cardType = PlpsDataCenter.getInstance().getResultCardType();
		PlpsUtils.initSpinnerView(this, spPripaidType, PlpsDataCenter.plpsPrepaidCardType);
		spPripaidType.setOnItemSelectedListener(this);
		cardList = PlpsDataCenter.getInstance().getAcctList();
		for(int i=0; i<cardList.size(); i++){
			if(!cardList.get(i).get(Comm.ACCOUNT_TYPE).equals("103")&&!cardList.get(i).get(Comm.ACCOUNT_TYPE).equals("104")){
				nocardList.add(cardList.get(i));
			}
		}
		PlpsUtils.initSpinnerView(this, spAccount, PlpsUtils.initSpinnerCardData(cardList, Comm.ACCOUNTNUMBER,Comm.NICKNAME, Plps.SP_DEFUALTTXT));
		setSpinnerBackground(false,spAccount);
//		spAccount.setOnItemSelectedListener(this);

		findViewById(R.id.btn_reset).setOnClickListener(this);
		findViewById(R.id.btn_Query).setOnClickListener(this);

		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) findViewById(R.id.tv_keyForPripaidType));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) findViewById(R.id.tv_keyForPripaidCarNum));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) findViewById(R.id.tv_keyForPripaidCarNumAgain));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) findViewById(R.id.tv_keyForAccount));
	}

	/**
	 * 设置Spinner背景
	 * @param isdefault
	 * @param v
	 */
	private void setSpinnerBackground(boolean isdefault,View... v){
		if(v.length > 0){
			for(View sp : v){
				if(sp != null){
					if (isdefault){
						sp.setClickable(true);
						sp.setBackgroundResource(R.drawable.bg_spinner);
					}else{
						sp.setClickable(false);
						sp.setBackgroundResource(R.drawable.bg_spinner_default);
					}
				}
			}
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> paramAdapterView, View paramView,
							   int paramInt, long paramLong) {
		if(StringUtil.isNullOrEmpty(cardList)){
			BaseDroidApp.getInstanse().showMessageDialog("无可用银行账户", new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					BaseDroidApp.getInstanse().dismissMessageDialog();
				}
			});
		}

		if(paramAdapterView == spPripaidType){
			if(spPripaidType.getSelectedItemPosition() > 0){
				setSpinnerBackground(true,spAccount);
			}else{
				setSpinnerBackground(false,spAccount);
			}
			if(spPripaidType.getSelectedItemPosition() >=0){
				//卡类型
				PrepaidCardTypeName = (String)PlpsDataCenter.plpsPrepaidCardType.get(spPripaidType.getSelectedItemPosition());
				if(PrepaidCardTypeName.equals("中铁银通") ||spPripaidType.getSelectedItemPosition() == 0){
//					if (isRefresh) {
					PlpsUtils.initSpinnerView(this, spAccount, PlpsUtils.initSpinnerCardData(cardList, Comm.ACCOUNTNUMBER,Comm.NICKNAME, Plps.SP_DEFUALTTXT));
//					}else {
//						isRefresh = true;
//					}
				}else {
					PlpsUtils.initSpinnerView(this, spAccount, PlpsUtils.initSpinnerCardData(nocardList, Comm.ACCOUNTNUMBER,Comm.NICKNAME, Plps.SP_DEFUALTTXT));
				}
			}
		}
		if(paramAdapterView == spAccount){
			if(spAccount.getSelectedItemPosition() > 0){
				//账户类型
				String accountType = null;
				if(spPripaidType.getSelectedItemPosition() == 0||PrepaidCardTypeName.equals("中铁银通")){
					accountType = (String)PlpsDataCenter.getInstance().getAcctList().get(spAccount.getSelectedItemPosition() - 1).get(Comm.ACCOUNT_TYPE);
				}else {
					accountType = (String)nocardList.get(spAccount.getSelectedItemPosition()-1).get(Comm.ACCOUNT_TYPE);
				}

				/*if(accountType.equals("103")||accountType.equals("104")){
//					List<Map<String, Object>> cardType = PlpsDataCenter.getInstance().getResultCardType();
					int m =0;
					List<String> prepardTypeList = (List<String>) PlpsDataCenter.plpsPrepaidCardType;
					for(int i=0; i<prepardTypeList.size(); i++){
						if(prepardTypeList.get(i).equals("中铁银通")){
							m = i;
							break;
						}
					}*/
//					if(spPripaidType.getSelectedItemPosition() == 0){
//						isRefresh = false;
//					}
//					spPripaidType.setSelection(m);
//				}
			}
		}
	}
	/** 重置 */
	private void reset() {
		etTransValue.setFocusable(false);
		etPripaidCarNum.setFocusable(true);
		etPripaidCarNum.setFocusableInTouchMode(true);
		spPripaidType.setSelection(0);
		etPripaidCarNum.setText("");
		etPripaidCarNumAgain.setText("");
		etName.setText("");
		spAccount.setSelection(0);
		etTransValue.setText("");
		etTransValue.setFocusable(true);
		etTransValue.setFocusableInTouchMode(true);
	}

	/** 解析加密类型数据  */
//	@SuppressWarnings("unchecked")
//	private void getCFCtype(Map<String, Object> resultData) {
//		List<Map<String, Object>> sipList = (List<Map<String, Object>>)resultData.get(Safety.FACTOR_LIST);
//		for (int i = 0; i < sipList.size(); i++) {
//			Map<String, Object> map = (Map<String, Object>) sipList.get(i).get(Safety.SIP_FIELD);
//			String sipType = (String) map.get(Safety.SIP_NAME);
//			if (sipType.equals(Comm.Otp)) {
//				otp = sipType;
//			} else if (sipType.equals(Comm.Smc)) {
//				smc = sipType;
//			}
//		}
//		if (StringUtil.isNull(otp) && StringUtil.isNull(smc)) {
//			BaseHttpEngine.dissMissProgressDialog(); return;
//		}
//		requestHttp(Login.COMM_RANDOM_NUMBER_API, "requestPSNGetRandomCallBack", null, true);
//	}

	/** 校验预付卡号 */
	private boolean submitRegexp(boolean required) {
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		// 预付卡号
		if (onlyRegular(required, etPripaidCarNum.getText().toString().trim())) {
			RegexpBean buyerEmail = new RegexpBean(Plps.PREPARDCARDNUMBER, etPripaidCarNum.getText().toString().trim(), "plpscardCheck");
			lists.add(buyerEmail);
		}

//		else {
//			if (onlyRegular(required, etPripaidCarNumAgain.getText().toString().trim())) {
//				RegexpBean buyerEmailAgain = new RegexpBean(Plps.PREPARDCARDNUMBER,
//						etPripaidCarNumAgain.getText().toString().trim(),
//						"plpscardCheck");
//				lists.add(buyerEmailAgain);
//			}
//		}
		if (RegexpUtils.regexpDate(lists)) {
			// 再输入预付卡号
			if(StringUtil.isNullOrEmpty(etPripaidCarNumAgain.getText().toString().trim())){
				BaseDroidApp.getInstanse().showMessageDialog("请再次输入预付卡号", new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						BaseDroidApp.getInstanse().dismissMessageDialog();
					}
				});
				return false;
			}
			if (!etPripaidCarNum.getText().toString().trim().equals(etPripaidCarNumAgain.getText().toString().trim())) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("两次输入的预付卡号不同");
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	/** 校验姓名 */
	private boolean submitRegexp_name(boolean required) {
		if (StringUtil.isNull(etName.getText().toString().trim())) {
			return true;
		}
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		// 姓名
		if (onlyRegular(required, etName.getText().toString().trim())) {
			RegexpBean buyerEmail = new RegexpBean(Plps.NAME_REGEXPTEXT, etName.getText().toString().trim(), "prepaidname");
			lists.add(buyerEmail);
		}

		if (!RegexpUtils.regexpDate(lists)) {
			return false;
		}
		return true;
	}

	/** 校验充值金额 */
	private boolean submitRegexp_transValue(boolean required) {
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		if(StringUtil.isNullOrEmpty(etTransValue.getText().toString().trim())){
			BaseDroidApp.getInstanse().showMessageDialog("请输入充值金额", new OnClickListener() {

				@Override
				public void onClick(View paramView) {
					// TODO Auto-generated method stub
					BaseDroidApp.getInstanse().dismissMessageDialog();
				}
			});
			return false;
		}else {
			// 充值金额
			if (onlyRegular(required, etTransValue.getText().toString().trim())) {
				RegexpBean buyerEmail = new RegexpBean(Plps.TRANSVALUE, etTransValue.getText().toString().trim(), "plpsAmounts");
				lists.add(buyerEmail);
			}
		}
		if (!RegexpUtils.regexpDate(lists)) {
			return false;
		}
		return true;
	}

	/** 只作正则校验  */
	private boolean onlyRegular(Boolean required, String content){
		if ((!required && !StringUtil.isNull(content)) || required) {
			return true;
		}
		return false;
	}

	/** 查询校验流程 */
	private boolean regexp() {
		if (spPripaidType.getSelectedItemPosition() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请选择预付卡类型");
			return false;
		}

		if (!submitRegexp(true)) {
			return false;
		}

		if (!submitRegexp_name(true)) {
			return false;
		}

		if (spAccount.getSelectedItemPosition() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请选择银行账户");
			return false;
		}

		if (!submitRegexp_transValue(true)) {
			return false;
		}

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 4) {
			LogGloble.i("PrepaidCardRechActivity", "finish");
			reset();
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------控件事件-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.btn_reset:
				reset();
				break;

			case R.id.btn_Query:
				if (!regexp()) {
					return;
				}
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
				break;
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO ---------------------------------------网络请求与回调方法--------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestGetSecurityFactor(Plps.PREPAIDCARDRECHSERVICEID);
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BaseDroidApp.getInstanse().showSeurityChooseDialog(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						BaseHttpEngine.showProgressDialog();
						Map<String, Object> params = new HashMap<String, Object>();
						params.put(Comm.ACCOUNT_ID, PlpsDataCenter.getInstance().getAcctList().get(spAccount.getSelectedItemPosition() - 1).get(Comm.ACCOUNT_ID));
						params.put(Plps.MERCHNO, PlpsDataCenter.plpsPrepaidCardNo.get(spPripaidType.getSelectedItemPosition() -1));
						params.put(Plps.PREPARDQUERYNUMBER, etPripaidCarNum.getText().toString());
						if (!StringUtil.isNull(etName.getText().toString().trim())) {
							params.put(Plps.NAME, etName.getText().toString().trim());
						}
						params.put(Plps.AMOUNT, StringUtil.parseStringPattern(etTransValue.getText().toString().trim(), 2));
						params.put(Plps.PREPARDQUERYCURRENCY, "001");
						params.put(Plps.COMBINID, BaseDroidApp.getInstanse().getSecurityChoosed());
						requestHttp(Plps.PSNPREPAIDCARDREPLENISHMENTPRE, "requestPsnPrepaidCardReplenishmentPreCallBack", params, true);
					}
				});
	}

	/** 请求预付卡充值预交易回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnPrepaidCardReplenishmentPreCallBack(Object resultObj) {
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultMap)) {
			BaseHttpEngine.dissMissProgressDialog();return;
		}
		PlpsDataCenter.getInstance().setPrepaidResultMap(resultMap);
//		getCFCtype(resultMap);
		requestHttp(Login.COMM_RANDOM_NUMBER_API, "requestPSNGetRandomCallBack", null, true);
	}

	/** 请求随机数回调 */
	public void requestPSNGetRandomCallBack(Object resultObj) {
		String randomNumber =  HttpTools.getResponseResult(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNull(randomNumber)) return;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Plps.MERCHNAME, PlpsDataCenter.plpsPrepaidCardType.get(spPripaidType.getSelectedItemPosition()));
		map.put(Plps.MERCHNO, PlpsDataCenter.plpsPrepaidCardNo.get(spPripaidType.getSelectedItemPosition()-1));
		map.put(Plps.PREPARDQUERYNUMBER, etPripaidCarNum.getText().toString());
		map.put(Plps.NAME, etName.getText().toString());
		map.put(Comm.ACCOUNTNUMBER, PlpsDataCenter.getInstance().getAcctList().get(spAccount.getSelectedItemPosition() - 1).get(Comm.ACCOUNTNUMBER));
		map.put(Comm.ACCOUNT_ID, PlpsDataCenter.getInstance().getAcctList().get(spAccount.getSelectedItemPosition() - 1).get(Comm.ACCOUNT_ID));
		map.put(Comm.NICKNAME, PlpsDataCenter.getInstance().getAcctList().get(spAccount.getSelectedItemPosition() - 1).get(Comm.NICKNAME));
		map.put(Plps.AMOUNT, StringUtil.parseStringPattern(etTransValue.getText().toString().trim(), 2));
		map.put(Plps.RANDOMNUMBER, randomNumber);
//		map.put(Comm.Otp, otp);
//		map.put(Comm.Smc, smc);
		PlpsDataCenter.getInstance().setMapPrepaidCardRechPre(map);
		Intent intent = new Intent(this, PrepaidCardRechSubmitActivity.class);
		startActivityForResult(intent, 4);
	}
	@Override
	public void onNothingSelected(AdapterView<?> paramAdapterView) {
		// TODO Auto-generated method stub

	}
}