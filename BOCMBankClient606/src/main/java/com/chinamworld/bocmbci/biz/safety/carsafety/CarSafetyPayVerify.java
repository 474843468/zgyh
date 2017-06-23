package com.chinamworld.bocmbci.biz.safety.carsafety;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Inves;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyBaseActivity;
import com.chinamworld.bocmbci.biz.safety.SafetyConstant;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.SafetyUtils;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PublicTools;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 车险投保缴费预交易界面
 * 
 * @author Zhi
 */
public class CarSafetyPayVerify extends SafetyBaseActivity {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** 主显示视图 */
	private View mMainView;
	/** 账户选择下标 */
	private int accSelectPosition;
	/** 加密类型-动态口令 */
	private String otp;
	/** 加密类型-短信 */
	private String smc;
	/** 当前选择的账户余额 */
	private double accMoney = 0;
	/** 账户类型 0-借记卡账户 1-信用卡账户 */
	private String accType;
	/** 账户类型 */
	private String accountType;
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.safety_msgfill_title);
		mMainView = LayoutInflater.from(this).inflate(R.layout.safety_carsafety_pay_verify, null);
		initView();
		findViewById(R.id.sliding_body).setPadding(0, 0, 0, 0);
		addView(mMainView);
	}
	
	/** 初始化控件元素 */
	private void initView() {
		setStep3();
		((TextView) mMainView.findViewById(R.id.tv_safetyType)).setText("车险");
		((TextView) mMainView.findViewById(R.id.tv_company)).setText("中银保险有限公司");
		((TextView) mMainView.findViewById(R.id.tv_applicant_name)).setText((String) SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Inves.CUSTOMERNAME));
		((TextView) mMainView.findViewById(R.id.tv_bizhong)).setText("人民币");
		((TextView) mMainView.findViewById(R.id.tv_amount)).setText(StringUtil.parseStringPattern((String) SafetyDataCenter.getInstance().getMapAutoInsuranceCalculation().get(Safety.TOTALREALPREMIUM), 2));

		mMainView.findViewById(R.id.btnNext).setOnClickListener(clickListener);
		
		List<String> mList = PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getAcctList(), Comm.ACCOUNTNUMBER);
		if (mList.size() != 1) {
			mMainView.findViewById(R.id.sp_payacct).setVisibility(View.VISIBLE);
			mMainView.findViewById(R.id.tv_payacct).setVisibility(View.GONE);
			SafetyUtils.initSpinnerView(this, (Spinner) mMainView.findViewById(R.id.sp_payacct), mList);
			if (SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.CONTINUEFLAG).equals("true") 
					&& mList.contains(SafetyDataCenter.getInstance().getHoldProDetail().get(Safety.ACCINFO))) {
				// 如果是续保且账户列表里有之前的付款账户
				((Spinner) mMainView.findViewById(R.id.sp_payacct)).setSelection(mList.indexOf(SafetyDataCenter.getInstance().getHoldProDetail().get(Safety.ACCINFO)));
			}
			((Spinner) mMainView.findViewById(R.id.sp_payacct)).setOnItemSelectedListener(selectListener);
		} else {
			mMainView.findViewById(R.id.sp_payacct).setVisibility(View.GONE);
			mMainView.findViewById(R.id.tv_payacct).setVisibility(View.VISIBLE);
			((TextView) mMainView.findViewById(R.id.tv_payacct)).setText(StringUtil.getForSixForString(mList.get(0)));
			accSelectPosition = 0;
			accDetail(0);
		}
	}
	
	private void setStep3() {
		mMainView.findViewById(R.id.layout_step1).setBackgroundResource(R.drawable.safety_step3);
		mMainView.findViewById(R.id.layout_step2).setBackgroundResource(R.drawable.safety_step2);
		mMainView.findViewById(R.id.layout_step3).setBackgroundResource(R.drawable.safety_step5);
		((TextView) mMainView.findViewById(R.id.index1)).setTextColor(getResources().getColor(R.color.gray));
		((TextView) mMainView.findViewById(R.id.index2)).setTextColor(getResources().getColor(R.color.gray));
		((TextView) mMainView.findViewById(R.id.index3)).setTextColor(getResources().getColor(R.color.red));
		((TextView) mMainView.findViewById(R.id.text1)).setTextColor(getResources().getColor(R.color.gray));
		((TextView) mMainView.findViewById(R.id.text2)).setTextColor(getResources().getColor(R.color.gray));
		((TextView) mMainView.findViewById(R.id.text3)).setTextColor(getResources().getColor(R.color.red));
	}

	/** 保存用户输入数据 */
	private void saveUserInput() {
		Map<String, Object> userInput = SafetyDataCenter.getInstance().getMapCarSafetyUserInput();
		userInput.put(Comm.Otp, otp);
		userInput.put(Comm.Smc, smc);
		userInput.put("accSelectPosition", accSelectPosition);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 4) {
			setResult(4);
			finish();
		}
	}
	
	private void accDetail(int position) {
		BaseHttpEngine.showProgressDialog();
		Map<String, Object> accMap = SafetyDataCenter.getInstance().getAcctList().get(position);
		accountType = (String) accMap.get(Comm.ACCOUNT_TYPE);
		if (accountType.equals("108") || accountType.equals("109")) {
			// 这两个账户不请求详情，因为不需要显示可用余额
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Comm.ACCOUNT_ID, accMap.get(Comm.ACCOUNT_ID));
		if (accMap.get(Comm.ACCOUNT_TYPE).equals("101")
				|| accMap.get(Comm.ACCOUNT_TYPE).equals("119")
				|| accMap.get(Comm.ACCOUNT_TYPE).equals("188")) {
			accType = "0";
			httpTools.requestHttp(Safety.METHOD_PSNACCOUNTQUERYACCOUNTDETAIL, "requestPsnAccountQueryAccountDetailCallBack", params, false);
		} else {
			accType = "1";
			params.put(Safety.SAFETY_HOLD_CURRENCY, "001");
			httpTools.requestHttp(Crcd.CRCD_ACCOUNTDETAIL_API, "requestPsnCrcdQueryAccountDetailCallBack", params, false);
		}
	}
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------控件事件-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	OnItemSelectedListener selectListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			accSelectPosition = arg2;
			accountType = (String) SafetyDataCenter.getInstance().getAcctList().get(arg2).get(Comm.ACCOUNT_TYPE);
			// 这两个账户不请求详情，因为不需要显示可用余额
			accType = "1";
			if (accountType.equals("108") || accountType.equals("109")) {
				mMainView.findViewById(R.id.ll_accMoney).setVisibility(View.GONE);
				return;
			}
			accDetail(arg2);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
	};
	
	/** 点击下一步按钮 */
	OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (accType.equals("0")) {
				// 借记卡账户余额不足以支付保费时提示语
				if (accMoney < 0) {
					BaseDroidApp.getInstanse().showInfoMessageDialog("您当前选择的账户只有外币，请选择其他足额人民币账户作为付款账户。");
					return;
				}
				double amount = Double.parseDouble((String) SafetyDataCenter.getInstance().getMapAutoInsuranceCalculation().get(Safety.TOTALREALPREMIUM));
				if (accMoney < amount) {
					BaseDroidApp.getInstanse().showInfoMessageDialog("您当前选择的账户余额少于应缴保费，请选择其他足额账户作为付款账户。");
					return;
				}
			} else {
				// 信用卡可用额度不足以支付保费时提示语
				if (!accountType.equals("108") && !accountType.equals("109")) {
					double amount = Double.parseDouble((String) SafetyDataCenter.getInstance().getMapAutoInsuranceCalculation().get(Safety.TOTALREALPREMIUM));
					if (accMoney < amount) {
						BaseDroidApp.getInstanse().showInfoMessageDialog("您当前选择的信用卡可用额度少于应缴保费，请选择其他足额账户作为付款账户。");
						return;
					}
				}
			}
			BaseHttpEngine.showProgressDialog();
			requestGetSecurityFactor(Safety.ServiceId);
		}
	};
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO ---------------------------------------网络请求与回调方法--------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 请求借记卡账户详情返回 */
	@SuppressWarnings("unchecked")
	public void requestPsnAccountQueryAccountDetailCallBack(Object resultObj) {
		mMainView.findViewById(R.id.ll_accMoney).setVisibility(View.VISIBLE);
		((TextView) mMainView.findViewById(R.id.tv_accMoney_key)).setText(getResources().getString(R.string.isForex_query_acc_accMoney));
		Map<String, Object> resultMap = this.getHttpTools().getResponseResult(resultObj);
		List<Map<String, Object>> accountDetailList = (List<Map<String, Object>>) (resultMap.get(ConstantGloble.ACC_DETAILIST));
		int i = 0;
		for (i = 0; i < accountDetailList.size(); i++) {
			Map<String, Object> map = accountDetailList.get(i);
			if (map.get(Safety.CURRENCYCODE).equals("001")) {
				String accMoneyStr = (String) map.get(Safety.AVAILABLEBALANCE);
				if (StringUtil.isNull(accMoneyStr)) {
					BaseHttpEngine.dissMissProgressDialog();
					return;
				}
				accMoney = Double.parseDouble(accMoneyStr);
				((TextView) mMainView.findViewById(R.id.tv_accMoney)).setText(StringUtil.parseStringPattern(accMoneyStr, 2));
				break;
			}
		}
		BaseHttpEngine.dissMissProgressDialog();
		if (i >= accountDetailList.size()) {
			((TextView) mMainView.findViewById(R.id.tv_accMoney)).setText("-");
			accMoney = -1;
			BaseDroidApp.getInstanse().showInfoMessageDialog("您当前选择的账户只有外币，请选择其他足额人民币账户作为付款账户。");
			return;
		}
		// 把请求指定accountId回来的账户详情保存到用户输入信息
		SafetyDataCenter.getInstance().getMapCarSafetyUserInput().put("accDetail", resultMap);
	}
	
	/** 请求信用卡账户详情返回 */
	@SuppressWarnings("unchecked")
	public void requestPsnCrcdQueryAccountDetailCallBack(Object resultObj) {
		mMainView.findViewById(R.id.ll_accMoney).setVisibility(View.VISIBLE);
		((TextView) mMainView.findViewById(R.id.tv_accMoney_key)).setText(getResources().getString(R.string.loan_query_availableQuota));
		Map<String, Object> resultMap = (Map<String, Object>) this.getHttpTools().getResponseResult(resultObj);
		List<Map<String, Object>> crcdAccountDetailList = (List<Map<String, Object>>) (resultMap.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST));
		int i = 0;
		for (i = 0; i < crcdAccountDetailList.size(); i++) {
			Map<String, Object> map = crcdAccountDetailList.get(i);
			if (map.get(Safety.SAFETY_HOLD_CURRENCY).equals("001")) {
				String accMoneyStr = (String) map.get(Crcd.CRCD_TOTALBALANCE);
				accMoney = Double.parseDouble(accMoneyStr);
				((TextView) mMainView.findViewById(R.id.tv_accMoney)).setText(StringUtil.parseStringPattern(accMoneyStr, 2));
				break;
			}
		}
		BaseHttpEngine.dissMissProgressDialog();
	}
	
	/** 安全因子返回结果 */
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseDroidApp.getInstanse().showSeurityChooseDialog(
			new OnClickListener() {
				Map<String, Object> userInput = SafetyDataCenter.getInstance().getMapCarSafetyUserInput();
				@Override
				public void onClick(View v) {
					Map<String, Object> params = new HashMap<String, Object>();
					Map<String, Object> map = SafetyDataCenter.getInstance().getMapAutoInsuranceCreatPolicy();
					params.put(Safety.TRANDATE, map.get(Safety.TRANDATE));
					params.put(Safety.JQXTRACENO, map.get(Safety.JQXTRACENO));
					params.put(Safety.BIZTRACENO, map.get(Safety.BIZTRACENO));
					params.put(Safety.TRADEAMOUNT, map.get(Safety.AMOUNT));
					params.put(Comm.ACCOUNT_ID, SafetyDataCenter.getInstance().getAcctList().get(accSelectPosition).get(Comm.ACCOUNT_ID));
					params.put(Safety.COMBINID, BaseDroidApp.getInstanse().getSecurityChoosed());
					// 币种默认上送001-人民币元
					params.put(Safety.CURRENCYCODE, "001");
					params.put(Safety.APPNTNAME, SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Inves.CUSTOMERNAME));
					params.put(Safety.INSUREDNAME, SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.BEN_NAME));
					params.put(Safety.INSUREDIDENTIFYNO, userInput.get(Safety.BEN_IDNUM));
					params.put(Safety.INSUCOMID, "2001");
					params.put(Safety.INSUCOMNAME, "中银保险有限公司");
					params.put(Safety.TOTALFEERATE, map.get(Safety.TOTALFEERATE));
					params.put(Safety.TOTALFEE, map.get(Safety.TOTALFEE));
					httpTools.requestHttp(Safety.METHOD_PSNAUTOINSURANCEPAYVERIFY, "requestPsnAutoInsurancePayVerifyCallBack", params, true);
				}
			});
	}
	/** 车险投保缴费预交易回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnAutoInsurancePayVerifyCallBack(Object resultObj) {
		Map<String, Object> result = (Map<String, Object>) this.getHttpTools().getResponseResult(resultObj);
		
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();return;
		}
		SafetyDataCenter.getInstance().setMapAutoInsurancePayVerify(result);
		requestForRandomNumber();
	}
	
	/** 请求随机数  */
	private void requestForRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this,"queryRandomNumberCallBack");
	}
	
	public void queryRandomNumberCallBack(Object resultObj) {
		String randomNumber = (String) this.getHttpTools().getResponseResult(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNull(randomNumber)) return;
		saveUserInput();
		SafetyDataCenter.getInstance().getMapCarSafetyUserInput()
		.put(SafetyConstant.RANDOMNUMBER, randomNumber);
		startActivityForResult((new Intent(this, CarSafetyPaySubmit.class)), 4);
	}
}
