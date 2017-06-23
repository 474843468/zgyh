package com.chinamworld.bocmbci.biz.safety.safetyhold;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.SafetyUtils;
import com.chinamworld.bocmbci.biz.safety.safetyproduct.lifelnsurance.LifeInsuranceBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 寿险持有保单详情页
 * 
 * @author Zhi
 */
public class LifeInsuDetailActivity extends LifeInsuranceBaseActivity {

	/** 详情数据 */
	private Map<String, Object> detailMap;
	/** 按钮布局 */
	private LinearLayout btnLayout;
	/** 当日契撤按钮 */
	private Button btnCurrentCancle;
	/** 退保/满期给付按钮 */
	private Button btnCancleOrFull;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainView = addView(R.layout.safety_hold_life_detail);
		setTitle(getString(R.string.safety_hold_pro_detail_title));
		findView();
		viewSet();
	}

	@Override
	protected void findView() {
		btnLayout = (LinearLayout) mMainView.findViewById(R.id.btn_layout);
		btnCurrentCancle = (Button) mMainView.findViewById(R.id.btn_currentCancle);
		btnCancleOrFull = (Button) mMainView.findViewById(R.id.btn_cancleOrFull);
	}

	@Override
	protected void viewSet() {
		detailMap = SafetyDataCenter.getInstance().getHoldProDetail();
		((TextView) mMainView.findViewById(R.id.safety_hold_pro_code)).setText((String) detailMap.get(Safety.INSUCODE));
		((TextView) mMainView.findViewById(R.id.safety_hold_pro_name)).setText((String) detailMap.get(Safety.RISKNAME));
		((TextView) mMainView.findViewById(R.id.safety_hold_pro_type)).setText(SafetyDataCenter.insuranceType.get(detailMap.get(Safety.RISKTYPE)));
		((TextView) mMainView.findViewById(R.id.safety_hold_risk_company)).setText((String) detailMap.get(Safety.SAFETY_HOLD_INSU_NAME));
		((TextView) mMainView.findViewById(R.id.safety_hold_risk_bill_code)).setText((String) detailMap.get(Safety.SAFETY_HOLD_POLICY_NO));
		((TextView) mMainView.findViewById(R.id.safety_hold_buy_channel)).setText(SafetyDataCenter.channelFlag.get(detailMap.get(Safety.SAFETY_HOLD_CHANNEL)));
		((TextView) mMainView.findViewById(R.id.safety_hold_risk_bill_effdate)).setText((String) detailMap.get(Safety.SAFETY_HOLD_POL_EFF_DATE));
		((TextView) mMainView.findViewById(R.id.safety_hold_risk_bill_enddate)).setText((String) detailMap.get(Safety.SAFETY_HOLD_POL_END_DATE));
		((TextView) mMainView.findViewById(R.id.safety_hold_bizhong)).setText(LocalData.Currency.get(detailMap.get(Acc.FINANCEICDETAIL_CURRENCY_RES)));
		// 投保人信息
		((TextView) mMainView.findViewById(R.id.safety_hold_pro_detail_applicant_name)).setText((String) detailMap.get(Safety.APPL_NAME));
		((TextView) mMainView.findViewById(R.id.safety_hold_pro_detail_applicant_gender)).setText("1".equals(detailMap.get(Safety.APPLSEX)) ? "男" : "女");
		((TextView) mMainView.findViewById(R.id.safety_hold_pro_detail_applicant_birth_date)).setText((String) detailMap.get(Safety.SAFETY_HOLD_APPLBIRTH));
		// 设置投保人被保人国籍
		List<Map<String, Object>> listCountry = SafetyUtils.getCountry(this);
		String applCtryNo = (String) detailMap.get(Safety.SAFETY_HOLD_APPLCTRYNO);
		String benCtryNo = (String) detailMap.get(Safety.SAFETY_HOLD_BENCTRYNO);
		int num = 0;
		for (int i = 0; i < listCountry.size(); i++) {
			Map<String, Object> map = listCountry.get(i);
			if (applCtryNo.equals(map.get(Safety.CODE))) {
				((TextView) mMainView.findViewById(R.id.safety_hold_pro_detail_applicant_national)).setText((String) map.get(Safety.NAME));
				num++;
			}
			if (benCtryNo.equals(map.get(Safety.CODE))) {
				((TextView) mMainView.findViewById(R.id.safety_hold_pro_detail_applicant_by_national)).setText((String) map.get(Safety.NAME));
				num++;
			}
			if (num >= 2) {
				break;
			}
		}
		((TextView) mMainView.findViewById(R.id.safety_hold_pro_detail_applicant_cred_type)).setText(SafetyDataCenter.credType.get(detailMap.get(Safety.SAFETY_HOLD_APPLIDTYPE)));
		((TextView) mMainView.findViewById(R.id.safety_hold_pro_detail_applicant_cred_num)).setText((String) detailMap.get(Safety.SAFETY_HOLD_APPLIDNO));
		((TextView) mMainView.findViewById(R.id.safety_hold_pro_detail_applicant_email)).setText(StringUtil.valueOf1((String) detailMap.get(Safety.SAFETY_HOLD_APPLEMAIL)));
		// 被保人信息
		((TextView) mMainView.findViewById(R.id.safety_hold_pro_detail_applicant_by_name)).setText((String) detailMap.get(Safety.SAFETY_HOLD_BENNAME));
		((TextView) mMainView.findViewById(R.id.safety_hold_pro_detail_applicant_by_gender)).setText(detailMap.get(Safety.SAFETY_HOLD_BENSEX).equals("1") ? "男" : "女");
		((TextView) mMainView.findViewById(R.id.safety_hold_pro_detail_applicant_by_birth_date)).setText((String) detailMap.get(Safety.SAFETY_HOLD_BENBIRTH));
		((TextView) mMainView.findViewById(R.id.safety_hold_pro_detail_applicant_by_cred_type)).setText(SafetyDataCenter.credType.get(detailMap.get(Safety.SAFETY_HOLD_BENIDTYPE)));
		((TextView) mMainView.findViewById(R.id.safety_hold_pro_detail_applicant_by_cred_num)).setText((String) detailMap.get(Safety.SAFETY_HOLD_BENIDNO));
		((TextView) mMainView.findViewById(R.id.safety_hold_pro_detail_applicant_by_email)).setText(StringUtil.valueOf1((String) detailMap.get(Safety.SAFETY_HOLD_BENEMAIL)));
		// 产品信息
		((TextView) mMainView.findViewById(R.id.tv_payYearType)).setText((String) detailMap.get(Safety.PAYYEARNAME));
		((TextView) mMainView.findViewById(R.id.tv_payYear)).setText((String) detailMap.get(Safety.PAYYEAR));
		((TextView) mMainView.findViewById(R.id.tv_insuYearType)).setText((String) detailMap.get(Safety.INSUYEARNAME));
		((TextView) mMainView.findViewById(R.id.tv_insuYear)).setText((String) detailMap.get(Safety.INSUYEAR));
		optionalShow((String) detailMap.get(Safety.SAFETY_HOLD_RISK_UNIT), R.id.ll_copies, R.id.tv_copies);
		((TextView) mMainView.findViewById(R.id.tv_riskPrem)).setText(StringUtil.parseStringPattern((String) detailMap.get(Safety.SAFETY_HOLD_RISK_PREM), 2));
		((TextView) mMainView.findViewById(R.id.tv_coverage)).setText(StringUtil.parseStringPattern((String) detailMap.get(Safety.SAFETY_HOLD_RISK_AMT), 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListenerByViewGroup(this, (LinearLayout) mMainView.findViewById(R.id.ll_content));
		setBtnLayout();
	}

	/** 设置按钮区域 */
	private void setBtnLayout() {
		String cancelFlag = (String) detailMap.get(Safety.SAFETY_HOLD_CANCEL_FLAG);
		String returnFlag = (String) detailMap.get(Safety.SAFETY_HOLD_RETURN_FLAG);
		if (cancelFlag.equals("0") && returnFlag.equals("0")) {
			btnLayout.setVisibility(View.GONE);
			return;
		}

		btnLayout.setVisibility(View.VISIBLE);
		if (cancelFlag.equals("1")) {
			btnCurrentCancle.setVisibility(View.VISIBLE);
			btnCurrentCancle.setOnClickListener(currentCancleListener);
		}
		if (returnFlag.equals("2")) {
			btnCancleOrFull.setVisibility(View.VISIBLE);
			btnCancleOrFull.setOnClickListener(cancleOrFullListener);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			setResult(RESULT_OK);
			finish();
		}
	}

	/** 当日契撤事件 */
	private OnClickListener currentCancleListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().showErrorDialog(getResources().getString(R.string.safety_lifeInsurance_hold_currentCancle_hint), R.string.cancle, R.string.confirm, new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch ((Integer) v.getTag()) {
					case CustomDialog.TAG_CANCLE:
						BaseDroidApp.getInstanse().dismissMessageDialog();
						break;
					case CustomDialog.TAG_SURE:
						BaseHttpEngine.showProgressDialog();
						requestPSNGetTokenId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
						break;
					}
				}
			});
		}
	};

	/** 退保/满期给付事件 */
	private OnClickListener cancleOrFullListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseHttpEngine.showProgressDialog();
			requestBankAcctList(SafetyDataCenter.lifeAccountTypeList);
		}
	};

	/** 请求token回调 */
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Safety.TRANDATE, detailMap.get(Safety.SAFETY_HOLD_TRANS_DATE));
		params.put(Safety.SAFETY_HOLD_TRANS_ACCNO, detailMap.get(Safety.SAFETY_HOLD_TRANS_ACCNO));
		params.put(Safety.SAFETY_HOLD_INSU_NAME, detailMap.get(Safety.SAFETY_HOLD_INSU_NAME));
		params.put(Safety.SAFETY_HOLD_RISK_NAME, detailMap.get(Safety.SAFETY_HOLD_RISK_NAME));
		params.put(Safety.SAFETY_HOLD_POLICY_NO, detailMap.get(Safety.SAFETY_HOLD_POLICY_NO));
		params.put(Safety.SAFETY_HOLD_RISK_PREM, detailMap.get(Safety.SAFETY_HOLD_RISK_PREM));
		params.put(ConstantGloble.PUBLIC_TOKEN, BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID));
		getHttpTools().requestHttp(Safety.PSNLIFEINSURANCECURRENTCANCLE, "requestPsnLifeInsuranceCurrentCancleCallBack", params, true);
	}

	/** 当日契撤回调 */
	public void requestPsnLifeInsuranceCurrentCancleCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BaseDroidApp.getInstanse().showMessageDialog(getString(R.string.safety_cancel_succss1), new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				BaseDroidApp.getInstanse().dismissMessageDialog();
				setResult(RESULT_OK);
				finish();
			}
		});
	}

	/** 银行卡列表返回  */
	@Override
	public void bankAccListCallBack(Object resultObj) {
		super.bankAccListCallBack(resultObj);
		if (SafetyDataCenter.getInstance().getAcctList().size() != 1) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Acc.ACC_ACCOUNTTYPE, "请选择");
			SafetyDataCenter.getInstance().getAcctList().add(0, map);
		}

		BaseHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent(this, LifeInsuCancleOrFullActivity.class);
		startActivityForResult(intent, 0);
	}
}
