package com.chinamworld.bocmbci.biz.safety.safetyhold;

import java.util.Map;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyBaseActivity;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 续保成功页面
 * @author fsm
 *
 */
public class SafetyInsuContSuccActivity extends SafetyBaseActivity {
	
	/** 保险公司名称， 产品名称， 投保人姓名， 币种， 保费金额, 付款账户*/
	private TextView safety_company, product_name, risk_bill_code, 
		safety_insurance_fee_amount, enddate, safety_email;
	
	/** 保险公司名称， 产品名称， 保单号，保费金额, 保单终止日期， 电子邮箱, 币种*/
	private String safetyCompany, productName, riskBillCode, 
		safetyInsuranceFeeAmount, endDate, safetyEmail, currency;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.safety_insu_cont_succ);
		setTitle(R.string.safety_hold_pro_detail_btn_continu);
		initParamsInfo();
		initViews();
		setViewInfos();
	}
	
	private void initViews(){
		setLeftTopGone();
		safety_company = (TextView)findViewById(R.id.safety_company);
		product_name = (TextView)findViewById(R.id.product_name);
		risk_bill_code = (TextView)findViewById(R.id.risk_bill_code);
		safety_insurance_fee_amount = (TextView)findViewById(R.id.safety_insurance_fee_amount);
		enddate = (TextView)findViewById(R.id.enddate);
		safety_email = (TextView)findViewById(R.id.safety_email);
		findViewById(R.id.next).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
			}
		});
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, safety_company);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, product_name);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, risk_bill_code);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, safety_insurance_fee_amount);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, safety_email);
	}
	
	private void initParamsInfo(){
		Map<String, Object> info = SafetyDataCenter.getInstance().getHoldProDetail();
		if(info != null){
			safetyCompany = (String)info.get(Safety.SAFETY_HOLD_INSU_NAME);
			safetyInsuranceFeeAmount = (String)info.get(Safety.SAFETY_HOLD_RISK_PREM);
			currency = (String)info.get(Safety.SAFETY_HOLD_CURRENCY);
		}
		Map<String, Object> insuranceContinueQuery = SafetyDataCenter.getInstance().getInsuranceContinueQuery();
		if(insuranceContinueQuery != null){
			productName = (String)info.get(Safety.SAFETY_HOLD_RISK_NAME);
		}
		Map<String, Object> insuranceNewSubmit = SafetyDataCenter.getInstance().getInsuranceNewSubmit();
		if(insuranceNewSubmit != null){
			riskBillCode = (String)insuranceNewSubmit.get(Safety.SAFETY_HOLD_POLICY_NO);
			safetyEmail = (String)insuranceNewSubmit.get(Safety.SAFETY_HOLD_APPLEMAIL);
			endDate = (String)insuranceNewSubmit.get(Safety.SAFETY_HOLD_POL_END_DATE);
		}
	}
	
	private void setViewInfos(){
		safety_company.setText(StringUtil.valueOf1(safetyCompany));
		product_name.setText(StringUtil.valueOf1(productName));
		risk_bill_code.setText(StringUtil.valueOf1(riskBillCode));
		safety_insurance_fee_amount.setText(StringUtil.parseStringCodePattern(currency, safetyInsuranceFeeAmount, 2));
		enddate.setText(StringUtil.valueOf1(endDate));
		safety_email.setText(StringUtil.valueOf1(safetyEmail));
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
