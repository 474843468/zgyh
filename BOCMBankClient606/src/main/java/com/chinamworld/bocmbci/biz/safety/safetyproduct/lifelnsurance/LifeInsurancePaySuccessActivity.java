package com.chinamworld.bocmbci.biz.safety.safetyproduct.lifelnsurance;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyConstant;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.safetyproduct.SafetyProductListActivity;

/**
 * 投保成功
 * 
 * @author Zhi
 */
public class LifeInsurancePaySuccessActivity extends LifeInsuranceBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainView = addView(R.layout.safety_life_product_pay_success);
		setTitle(getString(R.string.safety_msgfill_title));
		mLeftButton.setVisibility(View.GONE);
		findView();
		viewSet();
	}
	
	@Override
	protected void findView() {
		
	}

	@Override
	protected void viewSet() {
		Map<String, Object> submitResult = SafetyDataCenter.getInstance().getMapLifeInsurancePaySubmit();
		Map<String, Object> userInput = SafetyDataCenter.getInstance().getMapUserInput();
		Map<String, Object> userInputTemp = SafetyDataCenter.getInstance().getMapCarSafetyUserInput();
//		setTVText(R.id.tv_paymentDate, SafetyDataCenter.getInstance().getMapLifeInsuranceCalculation().get(Safety.SAFETY_HOLD_TRANS_ACCNO));
		setTVText(R.id.tv_effdate, submitResult.get(Safety.POLICYSTARTDATE));
//		setTVText(R.id.tv_enddate, submitResult.get(Safety.POLICYENDDATE));
		setTVText(R.id.tv_company, userInput.get(Safety.INSURANCE_COMANY));
		setTVText(R.id.tv_prodName, userInput.get(Safety.RISKNAME));
//		setTVText(R.id.tv_code, submitResult.get(Safety.SAFETY_HOLD_POLICY_NO));
//		optionalShow((String) userInput.get(Safety.SAFETY_HOLD_RISK_UNIT), R.id.ll_copies, R.id.tv_copies);
//		setTVText(R.id.tv_prem, SafetyDataCenter.getInstance().getMapLifeInsuranceCalculation().get(Safety.AMOUNT));
//		setTVText(R.id.tv_accountNumber, userInput.get(Safety.ACCOUNTNO));
//		setTVText(R.id.tv_email, userInput.get(Safety.APPL_EMAIL));
//		setTVText(R.id.tv_custType, SafetyDataCenter.getInstance().getMapInsuranceRiskEvaluationQuery().get(Safety.CUSTTYPE));
//		setTVText(R.id.tv_policyHandFlag, userInput.get(Safety.POLICYHANDFLAG).equals("1") ? "电子保单" : "邮寄保单");
		
		String tip = "*感谢您购买我行代理" + userInput.get(Safety.INSURANCE_COMANY) + "的保险产品，您的保单将由保险公司按您指定的方式发送电子保单至您的电子邮箱或邮寄纸质保单至您指定的地址，如您有任何疑问，请致电保险公司客户服务电话" + userInputTemp.get(Safety.TELEPHONE) + "或登录保险公司官方网站" + userInputTemp.get(Safety.URL) + "查询。";
		((TextView) mMainView.findViewById(R.id.tv_tip)).setText(tip);
		mMainView.findViewById(R.id.btnFinish).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(LifeInsurancePaySuccessActivity.this, SafetyProductListActivity.class));
				SafetyDataCenter.getInstance().clearAllData();
				setResult(SafetyConstant.QUIT_RESULT_CODE);
				finish();
			}
		});
	}

	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			startActivity(new Intent(LifeInsurancePaySuccessActivity.this, SafetyProductListActivity.class));
			SafetyDataCenter.getInstance().clearAllData();
			setResult(SafetyConstant.QUIT_RESULT_CODE);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	};
}
