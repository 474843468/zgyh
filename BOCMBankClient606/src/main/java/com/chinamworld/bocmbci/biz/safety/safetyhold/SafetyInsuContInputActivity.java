package com.chinamworld.bocmbci.biz.safety.safetyhold;

import java.util.ArrayList;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyBaseActivity;
import com.chinamworld.bocmbci.biz.safety.SafetyConstant;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.SafetyUtils;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PublicTools;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 续保填写信息页面
 * @author fsm
 *
 */
public class SafetyInsuContInputActivity extends SafetyBaseActivity {
	
	/** 保险公司名称， 产品名称， 投保人姓名， 保单号， 币种， 新保单起期, 续保份数*/
	private TextView safety_company, product_name, safety_applicant_name, 
		risk_bill_code, currency, safety_new_insurance_start_date, safety_continue_deal;
	/**  电子邮箱*/
	private EditText safety_email;
	/** 付款账户*/
	private Spinner acc_payout;
	
	/** 保险公司名称， 产品名称， 投保人姓名， 保单号， 币种， 电子邮箱, 付款账户， 被保人姓名， 被保人证件号码
	 * 产品名称, 保费, 保单终止日期, 付款方式*/
	private String safetyCompany, productName, safetyApplicantName, riskBillCode, 
		currencyStr,safetyEmail, accPayout, benName, benIdNo,
		riskName, riskPrem, polEndDate, payMethod;
	/**  续保份数（默认1份）*/
	private int safetyContinueDeal = 1;
	//保险公司代码， 中间业务归属， 账户Id， 电子邮箱
	private String insuId, busiBelong, accId, applEmail;
	
	private Button next;
	
	/** 账户列表信息*/
	private List<Map<String, Object>> accList;
	/** 账号列表*/
	private List<String> accNumList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.safety_insu_cont_input);
		setTitle(getString(R.string.safety_hold_pro_detail_btn_continu));
		initParamsInfo();
		initViews();
		setViewInfos();
	}
	
	private void initViews(){
		safety_company = (TextView)findViewById(R.id.safety_company);
		product_name = (TextView)findViewById(R.id.product_name);
		safety_applicant_name = (TextView)findViewById(R.id.safety_applicant_name);
		risk_bill_code = (TextView)findViewById(R.id.risk_bill_code);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, risk_bill_code);
		currency = (TextView)findViewById(R.id.currency);
		safety_new_insurance_start_date = (TextView)findViewById(R.id.safety_new_insurance_start_date);
		safety_continue_deal = (TextView)findViewById(R.id.safety_continue_deal);
		safety_email = (EditText)findViewById(R.id.safety_email);
		acc_payout = (Spinner)findViewById(R.id.acc_payout);
		acc_payout.setOnItemSelectedListener(itemSelectedListener);
		next = (Button)findViewById(R.id.next);
		next.setOnClickListener(nextClick);
		safety_new_insurance_start_date.setOnClickListener(SafetyChooseDateClick);
		SafetyUtils.initSpinnerView(this, acc_payout, SafetyUtils.parseStringListToForSixFor(accNumList));
		EditTextUtils.setLengthMatcher(this, safety_email, 30);
	}
	
	private void initParamsInfo(){
		Map<String, Object> info = SafetyDataCenter.getInstance().getHoldProDetail();
		if(info != null){
			insuId = (String)info.get(Safety.SAFETY_HOLD_INSU_ID);
			safetyCompany = (String)info.get(Safety.SAFETY_HOLD_INSU_NAME);
			productName = (String)info.get(Safety.SAFETY_HOLD_RISK_NAME);
			safetyApplicantName = (String)info.get(Safety.SAFETY_HOLD_APPNAME);  
			riskBillCode = (String)info.get(Safety.SAFETY_HOLD_POLICY_NO);  
			currencyStr = (String)info.get(Safety.SAFETY_HOLD_CURRENCY);  
			benName = (String)info.get(Safety.SAFETY_HOLD_BENNAME);  
			benIdNo = (String)info.get(Safety.SAFETY_HOLD_BENIDNO);  
			riskName = (String)info.get(Safety.SAFETY_HOLD_RISK_NAME);  
			riskPrem = (String)info.get(Safety.SAFETY_HOLD_RISK_PREM);  
			polEndDate = (String)info.get(Safety.SAFETY_HOLD_POL_END_DATE);  
			applEmail = (String)info.get(Safety.APPL_EMAIL);  
		}
		accList = SafetyDataCenter.getInstance().getAcctList();
		dateTime = (String)BaseDroidApp.getInstanse().getBizDataMap().get(Safety.DATE_TIME);
		accNumList = PublicTools.getSpinnerData(accList, Acc.ACC_ACCOUNTNUMBER_RES);
	}
	
	private void setViewInfos(){
		safety_company.setText(StringUtil.valueOf1(safetyCompany));
		product_name.setText(StringUtil.valueOf1(productName));
		safety_applicant_name.setText(StringUtil.valueOf1(safetyApplicantName));
		risk_bill_code.setText(StringUtil.valueOf1(riskBillCode));
		currency.setText(StringUtil.valueOf1(LocalData.Currency.get(currencyStr)));
		safety_email.setText(applEmail);
		//续保时的起期：过了终保日期，是当前日期加1，没过终保日期，是终保日期加1
		if(QueryDateUtils.compareDate(dateTime, polEndDate)){
			safety_new_insurance_start_date.setText(QueryDateUtils.getOneDayLater(polEndDate));
		}else{
			safety_new_insurance_start_date.setText(QueryDateUtils.getOneDayLater(dateTime));
		}
	}
	
	
	private boolean validation(){
		safetyEmail = safety_email.getText().toString();
		RegexpBean regEmail = new RegexpBean(getString(R.string.safety_email_nocolon), safetyEmail,
				SafetyConstant.EMAIL);
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		lists.add(regEmail);
		if (!RegexpUtils.regexpDate(lists)) {// 校验不通过
			return false;
		}
		return true;
	}
	
	OnClickListener nextClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (!checkTime(polEndDate, safety_new_insurance_start_date.getText().toString())) {
				return;
			}
			if(validation()){
				//校验难过，进行续保查询交易  新单投保预交易
				requestCommConversationId();
			}
		}
	};
	
	private boolean checkTime(String startDate,String endDate){
		String oneDayLater = QueryDateUtils.getOneDayLater(startDate);
		String sysoneDayLater = QueryDateUtils.getOneDayLater(dateTime);
		if (!QueryDateUtils.compareDate(oneDayLater, endDate)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("新保单起期必须大于原保单终止日期");
			return false;
		}
		if (!QueryDateUtils.compareDate(sysoneDayLater, endDate)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("新保单起期应晚于系统日期");
			return false;
		}
		if (!QueryDateUtils.compareDateNextYear(oneDayLater, endDate)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请在保单终止日期前三个月以及后一年内进行续保");
			return false;
		}
		return true;
	}
	
	OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			accPayout = accNumList.get(arg2);
			Map<String, Object> map = accList.get(arg2);
			accId = (String)(map.get(Acc.ACC_ACCOUNTID_RES));
			payMethod = SafetyDataCenter.payMethod.get(map.get(Acc.ACC_ACCOUNTTYPE_REQ));
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
		
	};
	
	/**
	 * @Title: requestPsnInsuranceContinueQuery续保查询交易
	 * @param  
	 * @return void
	 */
	public void requestPsnInsuranceContinueQuery() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setMethod(Safety.SafetyInsuranceContinueQuery);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Safety.SAFETY_HOLD_INSU_ID, insuId);
		paramsmap.put(Safety.SAFETY_HOLD_POLICY_NO, riskBillCode);
		paramsmap.put(Safety.BUSI_BELONG, "0");
		paramsmap.put(Safety.SAFETY_HOLD_RISK_UNIT, safetyContinueDeal);
		paramsmap.put(Safety.SAFETY_HOLD_POL_EFF_DATE, safety_new_insurance_start_date.getText().toString());
		paramsmap.put(Safety.ACC_ID, accId);
		paramsmap.put(Safety.SAFETY_HOLD_APPLEMAIL, safetyEmail);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnInsuranceContinueQueryCallBack");
	}
	
	/**
	 * @Title: requestPsnInsuranceContinueQueryCallBack
	 * @Description: 续保查询交易回调
	 * @param @param resultObj 
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnInsuranceContinueQueryCallBack(Object resultObj) {
		Map<String, Object> map = (Map<String, Object>)this.getHttpTools().getResponseResult(resultObj);
		SafetyDataCenter.getInstance().setInsuranceContinueQuery(map);
		requestGetSecurityFactor(Safety.ServiceId);
	}
	
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPsnInsuranceContinueQuery();
	}
	
	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseDroidApp.getInstanse().showSeurityChooseDialog(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						requestPsnInsuranceNewVerify();
					}
				});
	}
	
	/**
	 * @Title: 新单投保预交易
	 * @param  
	 * @return void
	 */
	public void requestPsnInsuranceNewVerify() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID).toString());
		biiRequestBody.setMethod(Safety.SafetyInsuranceNewVerify);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Safety.SAFETY_HOLD_INSU_NAME, safetyCompany);
		paramsmap.put(Safety.SAFETY_HOLD_CURRENCY, currencyStr);
		paramsmap.put(Safety.SAFETY_HOLD_BENNAME, benName);
		paramsmap.put(Safety.SAFETY_HOLD_BENIDNO, benIdNo);
		paramsmap.put(Safety.SAFETY_HOLD_RISK_NAME, riskName);
		paramsmap.put(Safety.RISKPAEM, riskPrem);
		paramsmap.put(Acc.RELEVANCEACCPRE_ACC_COMBINID_REQ, BaseDroidApp.getInstanse().getSecurityChoosed());//安全因子
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnInsuranceNewVerifyCallBack");
	}
	
	/**
	 * @Title: 新单投保预交易回调
	 * @param @param resultObj 
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnInsuranceNewVerifyCallBack(Object resultObj) {
		Map<String, Object> map = (Map<String, Object>)this.getHttpTools().getResponseResult(resultObj);
		map.put(Acc.ACC_ACCOUNTNUMBER_RES, accPayout);
		map.put(Acc.ACC_ACCOUNTID_RES, accId);
		map.put(Safety.PAY_METHOD, payMethod);
		SafetyDataCenter.getInstance().setInsuranceNewVerify(map);
		//请求随机数
		requestForRandomNumber();
	}
	
	/**
	 * 请求密码控件随机数
	 */
	public void requestForRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this,
				"queryRandomNumberCallBack");
	}

	/**
	 * 请求密码控件随机数 回调
	 * 
	 * @param resultObj
	 */
	public void queryRandomNumberCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String randomNumber = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(randomNumber)) {
			BiiHttpEngine.dissMissProgressDialog();
			return;
		}
		SafetyDataCenter.getInstance().setRandomNumer(randomNumber);
		BiiHttpEngine.dissMissProgressDialog();
		startActivityForResult(new Intent(this, SafetyInsuContConfirmActivity.class), 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;
		default:
			break;
		}
	}

}
