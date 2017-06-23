package com.chinamworld.bocmbci.biz.safety.safetyhold;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.adapter.ChooseCardAdapter;
import com.chinamworld.bocmbci.biz.safety.safetyproduct.lifelnsurance.LifeInsuranceBaseActivity;
import com.chinamworld.bocmbci.biz.safety.safetyproduct.lifelnsurance.LifeInsurancePayLoseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 寿险退保/满期给付确认页
 * 
 * @author Zhi
 */
public class LifeInsuCancleOrFullActivity extends LifeInsuranceBaseActivity {

	public static String TAG = "LifeInsuCancleOrFullActivity";
	/** 退款账户下拉框 */
	private Spinner spAccount;
	/** 确认按钮 */
	private Button btnConfirm;
	/** 持有保单详情数据 */
	private Map<String, Object> detailMap;
	/** 卡列表 */
	private List<Map<String, Object>> listAccount;
	/** 错误标识位 0-退保试算报错 1-退保提交报错 */
	private int errorFlag = 0;
	/** 选择的卡下标 */
	private int selectIndexOfAcc = 0;
	/** 如果试算接口报错，错误信息 */
	private String errorInfo;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainView = addView(R.layout.safety_hold_life_cancleorfull);
		setTitle(R.string.safety_lifeInsurance_hold_cancleFinish);
		findView();
		viewSet();
	};

	@Override
	protected void findView() {
		spAccount = (Spinner) mMainView.findViewById(R.id.sp_payacct);
		btnConfirm = (Button) mMainView.findViewById(R.id.btn_confirm);
	}

	@Override
	protected void viewSet() {
		detailMap = SafetyDataCenter.getInstance().getHoldProDetail();
		((TextView) mMainView.findViewById(R.id.tv_name)).setText((String) detailMap.get(Safety.APPL_NAME));
		((TextView) mMainView.findViewById(R.id.tv_gender)).setText(detailMap.get(Safety.APPL_SEX).equals("1") ? "男" : "女");
		((TextView) mMainView.findViewById(R.id.tv_idType)).setText(SafetyDataCenter.credType.get(detailMap.get(Safety.SAFETY_HOLD_APPLIDTYPE)));
		((TextView) mMainView.findViewById(R.id.tv_idNum)).setText((String) detailMap.get(Safety.APPL_IDNO));
		((TextView) mMainView.findViewById(R.id.tv_trandate)).setText((String) detailMap.get(Safety.SAFETY_HOLD_TRANS_DATE));
		((TextView) mMainView.findViewById(R.id.tv_effdate)).setText((String) detailMap.get(Safety.SAFETY_HOLD_POL_EFF_DATE));
		((TextView) mMainView.findViewById(R.id.tv_enddate)).setText((String) detailMap.get(Safety.SAFETY_HOLD_POL_END_DATE));
		((TextView) mMainView.findViewById(R.id.tv_company)).setText((String) detailMap.get(Safety.SAFETY_HOLD_INSU_NAME));
		((TextView) mMainView.findViewById(R.id.tv_riskName)).setText((String) detailMap.get(Safety.RISKNAME));
		((TextView) mMainView.findViewById(R.id.tv_policyNo)).setText((String) detailMap.get(Safety.SAFETY_HOLD_POLICY_NO));
		mMainView.findViewById(R.id.tv_tip).setTag("noPopup");
		optionalShow((String) detailMap.get(Safety.SAFETY_HOLD_RISK_UNIT), R.id.ll_copies, R.id.tv_copies);
		PopupWindowUtils.getInstance().setOnShowAllTextListenerByViewGroup(this, (LinearLayout) mMainView.findViewById(R.id.ll_content));
		listAccount = SafetyDataCenter.getInstance().getAcctList();
		ChooseCardAdapter mAdapter = new ChooseCardAdapter(this, listAccount);
		spAccount.setAdapter(mAdapter);
		spAccount.setOnItemSelectedListener(selectAccListener);
		btnConfirm.setOnClickListener(confirmListener);
	}
	
	@Override
	public boolean doHttpErrorHandler(String method, BiiError biiError) {
		this.errorInfo = biiError.getMessage();
		showHttpErrorDialog(biiError.getCode(), biiError.getMessage());
		return true;
	}
	
	private void cancleOfFull() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Safety.TRANDATE, detailMap.get(Safety.SAFETY_HOLD_TRANS_DATE));
		params.put(Safety.SAFETY_HOLD_TRANS_ACCNO, detailMap.get(Safety.SAFETY_HOLD_TRANS_ACCNO));
		params.put(Safety.TOACCOUNTID, listAccount.get(selectIndexOfAcc).get(Acc.ACC_ACCOUNTID_RES));
		params.put(Safety.PAYEEACTNO, listAccount.get(selectIndexOfAcc).get(Acc.ACC_ACCOUNTNUMBER_RES));
		getHttpTools().requestHttp(Safety.PSNLIFEINSURANCECANCLEORFULLPAYCOUNT, "requestPsnLifeInsuranceCancleOrFullPayCountCallBack", params, true);
		getHttpTools().registAllErrorCode(Safety.PSNLIFEINSURANCECANCLEORFULLPAYCOUNT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		setResult(RESULT_OK);
		finish();
	}

	/** 选择收款账户监听 */
	private OnItemSelectedListener selectAccListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			((TextView) mMainView.findViewById(R.id.tv_backPrem)).setText("-");
			if (position == 0 && SafetyDataCenter.getInstance().getAcctList().size() != 1) {
				return;
			}
			errorInfo = "";
			selectIndexOfAcc = position;
			BaseHttpEngine.showProgressDialog();
			Map<String, Object> accountMap = listAccount.get(position);
			if ("101".equals(accountMap.get(Acc.ACC_ACCOUNTTYPE))) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put(Comm.ACCOUNT_ID, accountMap.get(Comm.ACCOUNT_ID));
				httpTools.requestHttp(Safety.METHOD_PSNACCOUNTQUERYACCOUNTDETAIL, "requestPsnAccountQueryAccountDetailCallBack", params, false);
			} else {
				cancleOfFull();
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};

	/** 确认监听 */
	private OnClickListener confirmListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!StringUtil.isNull(errorInfo)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(errorInfo);
				return;
			}
			if (listAccount.size() != 1 && spAccount.getSelectedItemPosition() == 0) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("请选择收款账户");
				return;
			}

			BaseHttpEngine.showProgressDialog();
			requestPSNGetTokenId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		}
	};

	/** 卡详情回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnAccountQueryAccountDetailCallBack(Object resultObj) {
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		List<Map<String, Object>> accountDetailList = (List<Map<String, Object>>) (resultMap.get(ConstantGloble.ACC_DETAILIST));
		int i = 0;
		for (; i < accountDetailList.size(); i++) {
			Map<String, Object> map = accountDetailList.get(i);
			if (map.get(Safety.CURRENCYCODE).equals("001")) {
				break;
			}
		}

		if (i >= accountDetailList.size()) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog("您当前选择的账户只支持外币，请选择其他人民币账户作为收款账户。");
			errorInfo = "您当前选择的账户只支持外币，请选择其他人民币账户作为收款账户。";
			return;
		}
		cancleOfFull();
	}

	/** 寿险退保/满期给付试算回调 */
	public void requestPsnLifeInsuranceCancleOrFullPayCountCallBack(Object resultObj) {
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		if ("IBAS.T1029".equals(resultMap.get(Safety.RETURNCODE))) {
			// 后台报错
			errorFlag = 0;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(Safety.TRANDATE, resultMap.get(Safety.TRSDATE));
			params.put(Safety.TRANSEQ, resultMap.get(Safety.TRANSEQ));
			getHttpTools().requestHttp(Safety.PSNINSURANCEERRORINFOQUERY, "requestPsnInsuranceErrorInfoQueryCallBack", params);
		} else {
			((TextView) mMainView.findViewById(R.id.tv_backPrem)).setText(StringUtil.parseStringPattern((String) resultMap.get(Safety.BACK_PREM), 2));
			BaseHttpEngine.dissMissProgressDialog();
		}
	}

	/** 保险公司失败返回信息查询回调 */
	public void requestPsnInsuranceErrorInfoQueryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		String loseInfo = (String) resultMap.get(Safety.ERRORMSG);
		if (errorFlag == 0) {
			errorInfo = loseInfo;
			BaseDroidApp.getInstanse().showInfoMessageDialog(loseInfo);
			return;
		}
		Intent intent = new Intent(this, LifeInsurancePayLoseActivity.class);
		intent.putExtra(Safety.ERRORMSG, loseInfo);
		intent.putExtra("jumpFlag", TAG);
		startActivityForResult(intent, 1);
	}

	/** 请求token回调 */
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Safety.TRANDATE, detailMap.get(Safety.SAFETY_HOLD_TRANS_DATE));
		params.put(Safety.SAFETY_HOLD_TRANS_ACCNO, detailMap.get(Safety.SAFETY_HOLD_TRANS_ACCNO));
		params.put(Safety.TOACCOUNTID, listAccount.get(spAccount.getSelectedItemPosition()).get(Acc.ACC_ACCOUNTID_RES));
		params.put(Safety.PAYEEACTNO, listAccount.get(spAccount.getSelectedItemPosition()).get(Acc.ACC_ACCOUNTNUMBER_RES));
		params.put(Safety.RISKNAME, detailMap.get(Safety.RISKNAME));
		params.put(Safety.SAFETY_HOLD_INSU_NAME, detailMap.get(Safety.SAFETY_HOLD_INSU_NAME));
		params.put(Safety.SAFETY_HOLD_POLICY_NO, detailMap.get(Safety.SAFETY_HOLD_POLICY_NO));
		params.put(ConstantGloble.PUBLIC_TOKEN, BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID));
		getHttpTools().requestHttp(Safety.PSNLIFEINSURANCECANCLEORFULLPAY, "requestPsnLifeInsuranceCancleOrFullPayCallBack", params, true);
	}

	/** 退保/满期给付回调 */
	public void requestPsnLifeInsuranceCancleOrFullPayCallBack(Object resultObj) {
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		if ("IBAS.T1029".equals(resultMap.get(Safety.RETURNCODE))) {
			// 后台报错
			errorFlag = 1;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(Safety.TRANDATE, resultMap.get(Safety.TRSDATE));
			params.put(Safety.TRANSEQ, resultMap.get(Safety.TRANSEQ));
			getHttpTools().requestHttp(Safety.PSNINSURANCEERRORINFOQUERY, "requestPsnInsuranceErrorInfoQueryCallBack", params);
		} else {
			BaseHttpEngine.dissMissProgressDialog();
			startActivityForResult(new Intent(this, LifeInsuCancleOrFullSuccessActivity.class).putExtra("backPrem", ((TextView) mMainView.findViewById(R.id.tv_backPrem)).getText().toString()), 0);
		}
	}
}
