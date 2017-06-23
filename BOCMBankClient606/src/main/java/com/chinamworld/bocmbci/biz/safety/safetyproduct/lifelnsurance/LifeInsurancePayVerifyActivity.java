package com.chinamworld.bocmbci.biz.safety.safetyproduct.lifelnsurance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyConstant;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.adapter.ChooseCardAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 预交易页面
 * 
 * @author Zhi
 */
public class LifeInsurancePayVerifyActivity extends LifeInsuranceBaseActivity {

	/** 当前选择的账户余额 */
	private double accMoney = 0;
	/** 当前选择的账户余额字符串 */
	private String accMoneyStr;
	/** 用户选择的卡下标 */
	private int selectPosition = 0;
	/** 账户下拉框 */
	private Spinner spAccount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainView = addView(R.layout.safety_life_product_pay_verify);
		setTitle(R.string.safety_msgfill_title);
		findView();
		viewSet();
	}

	@Override
	protected void findView() {
		spAccount = (Spinner) mMainView.findViewById(R.id.sp_payacct);
	}

	@Override
	protected void viewSet() {
		Map<String, Object> userInput = SafetyDataCenter.getInstance().getMapUserInput();
		((TextView) mMainView.findViewById(R.id.tv_company)).setText((String) userInput.get(Safety.INSURANCE_COMANY));
		((TextView) mMainView.findViewById(R.id.tv_subCompany)).setText((String) userInput.get(Safety.SUBINSUNAME));
		((TextView) mMainView.findViewById(R.id.tv_prodName)).setText((String) userInput.get(Safety.RISKNAME));
		((TextView) mMainView.findViewById(R.id.tv_applicant_name)).setText((String) userInput.get(Safety.APPL_NAME));
		((TextView) mMainView.findViewById(R.id.tv_bizhong)).setText("人民币元");
		((TextView) mMainView.findViewById(R.id.tv_amount)).setText(StringUtil.parseStringPattern((String) SafetyDataCenter.getInstance().getMapLifeInsuranceCalculation().get(Safety.AMOUNT), 2));
		if (userInput.get(Safety.TRADEWAY).equals("01")) {
			((TextView) mMainView.findViewById(R.id.tv_keyForAmount)).setText(getResources().getString(R.string.safety_lifeInsurance_confirm_zong));
		} else {
			((TextView) mMainView.findViewById(R.id.tv_keyForAmount)).setText(getResources().getString(R.string.safety_lifeInsurance_confirm_shouqi));
		}

		mMainView.findViewById(R.id.btnNext).setOnClickListener(clickListener);
		
		List<Map<String, Object>> accList = SafetyDataCenter.getInstance().getAcctList();
		ChooseCardAdapter mAdapter = new ChooseCardAdapter(this, accList);
		spAccount.setAdapter(mAdapter);
		spAccount.setOnItemSelectedListener(selectListener);
		PopupWindowUtils.getInstance().setOnShowAllTextListenerByViewGroup(this, (LinearLayout) mMainView.findViewById(R.id.ll_content));
	}

	/** 查询卡详情 */
	private void accDetail(int position) {
		BaseHttpEngine.showProgressDialog();
		Map<String, Object> accountMap = SafetyDataCenter.getInstance().getAcctList().get(position);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Comm.ACCOUNT_ID, accountMap.get(Comm.ACCOUNT_ID));
		httpTools.requestHttp(Safety.METHOD_PSNACCOUNTQUERYACCOUNTDETAIL, "requestPsnAccountQueryAccountDetailCallBack", params, false);
	}

	/** 下一步 */
	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if ((spAccount.getSelectedItemPosition() == 0) && (SafetyDataCenter.getInstance().getAcctList().size() != 1)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("请选择付款账户");
				return;
			}
			if (accMoney < 0) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("您当前选择的账户只有外币，请选择其他足额人民币账户作为付款账户");
				return;
			}
			double amount = Double.parseDouble((String) SafetyDataCenter.getInstance().getMapLifeInsuranceCalculation().get(Safety.AMOUNT));
			if (accMoney < amount) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("您当前选择的账户余额少于应缴保费，请选择其他足额账户作为付款账户");
				return;
			}
			BaseHttpEngine.showProgressDialog();
			requestGetSecurityFactor(Safety.ServiceId);
		}
	};

	/** 选择账户事件 */
	private OnItemSelectedListener selectListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			((TextView) mMainView.findViewById(R.id.tv_accMoney)).setText("-");
			accMoney = -1;
			if (position == 0 && SafetyDataCenter.getInstance().getAcctList().size() != 1) {
				return;
			}
			selectPosition = position;
			accDetail(position);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};

	/** 卡详情回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnAccountQueryAccountDetailCallBack(Object resultObj) {
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
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
				this.accMoneyStr = accMoneyStr;
				((TextView) mMainView.findViewById(R.id.tv_accMoney)).setText(StringUtil.parseStringPattern(accMoneyStr, 2));
				break;
			}
		}
		BaseHttpEngine.dissMissProgressDialog();
		if (i >= accountDetailList.size()) {
			((TextView) mMainView.findViewById(R.id.tv_accMoney)).setText("-");
			accMoney = -1;
			return;
		}
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BaseHttpEngine.showProgressDialog();
				Map<String, Object> calculationMap = SafetyDataCenter.getInstance().getMapLifeInsuranceCalculation();
				Map<String, Object> userInput = SafetyDataCenter.getInstance().getMapUserInput();
				Map<String, Object> params = new HashMap<String, Object>();
				params.put(Safety.SAFETY_HOLD_TRANS_ACCNO, calculationMap.get(Safety.SAFETY_HOLD_TRANS_ACCNO));
				params.put(Safety.INSURANCE_ID, userInput.get(Safety.INSURANCE_ID));
				params.put(Safety.INSURANCE_COMANY, userInput.get(Safety.INSURANCE_COMANY));
				params.put(Safety.RISKCODE, userInput.get(Safety.RISKCODE));
				params.put(Safety.RISKNAME, userInput.get(Safety.RISKNAME));
				
				params.put(Acc.DETAIL_ACC_ACCOUNTID_REQ, SafetyDataCenter.getInstance().getAcctList().get(selectPosition).get(Acc.DETAIL_ACC_ACCOUNTID_REQ));
				params.put(Safety.ACCOUNTNO, SafetyDataCenter.getInstance().getAcctList().get(selectPosition).get(Acc.RELEVANCEACCRES_ACCOUNTNUMBER_REQ));
				// 投保提交和成功页面要用
				userInput.put(Acc.DETAIL_ACC_ACCOUNTID_REQ, params.get(Acc.DETAIL_ACC_ACCOUNTID_REQ));
				userInput.put(Safety.ACCOUNTNO, params.get(Safety.ACCOUNTNO));
				
				params.put(Acc.FINANCEICDETAIL_CURRENCY_RES, SafetyConstant.CURRENCY);
				params.put(Safety.SAFETY_HOLD_RISK_PREM, SafetyDataCenter.getInstance().getMapLifeInsuranceCalculation().get(Safety.AMOUNT));
				params.put(Safety.INSUREDNAME, userInput.get(Safety.BEN_NAME));
				params.put(Safety.INSUREDIDENTIFYNO, userInput.get(Safety.BEN_IDNUM));
				params.put(Safety.COMBINID, BaseDroidApp.getInstanse().getSecurityChoosed());
				getHttpTools().requestHttp(Safety.PSNLIFEINSURANCEPAYVERIFY, "requestPsnLifeInsurancePayVerifyCallBack", params, true);
			}
		});
	}

	/** 预交易返回 */
	public void requestPsnLifeInsurancePayVerifyCallBack(Object resultObj) {
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		SafetyDataCenter.getInstance().setMapLifeInsurancePayVerify(resultMap);
		
		getHttpTools().requestHttp(Login.COMM_RANDOM_NUMBER_API, "requestPSNGetRandomCallBack", null, true);
	}
	
	/** 请求随机数回调 */
	public void requestPSNGetRandomCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		String randomNum = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNull(randomNum)) {
			return;
		}
		Intent intent = new Intent(this, LifeInsurancePaySubmitActivity.class);
		intent.putExtra(SafetyConstant.RANDOMNUMBER, randomNum);
		intent.putExtra(Safety.AMOUNT, accMoneyStr);
		startActivityForResult(intent, 4444);
	}
}
