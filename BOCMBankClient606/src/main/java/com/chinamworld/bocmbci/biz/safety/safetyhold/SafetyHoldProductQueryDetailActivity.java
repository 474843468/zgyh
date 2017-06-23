package com.chinamworld.bocmbci.biz.safety.safetyhold;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.SBRemit;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyBaseActivity;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.SafetyUtils;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 保险  持有保单查询详情
 * 
 * @author fsm
 * 
 */
public class SafetyHoldProductQueryDetailActivity extends SafetyBaseActivity {
	
	/** 产品代码，产品名称， 产品类型,保险公司，保单号，购买渠道，保单成立日期，保单生效日期
	 *  保单终止日期,币种，份数,保费*/
	private TextView safetyProCode, safetyProName, safetyProType,
		safetyRiskCompany,safetyRiskBillCode,safetyBuyChannel,safetyRiskBillCredate,
		safetyRiskBillEffdate,safetyRiskBillEnddate,safetyCurrency,safetyDeal,
		safetyRiskFee;
	
	/** 投保人姓名，性别， 证件类型，证件号,出生日期，国籍，Email*/
	private TextView safetyApplicantName, safetyApplicantGender, safetyApplicantCredType,
		safetyApplicantCredNum, safetyApplicantBirthDate, safetyApplicantNational,
		safetyApplicantEmail;
	
	/** 被保人姓名，性别， 证件类型，证件号,出生日期，国籍，Email*/
	private TextView safetyApplByName, safetyApplByGender, safetyApplByCredType,
		safetyApplByCredNum, safetyApplByBirthDate, safetyApplByNational,safetyApplByEmail;
	
	/** 产品代码，产品名称， 产品类型,保险公司，保单号，购买渠道，保单成立日期，保单生效日期
	 *  保单终止日期,币种，份数,保费
	 *	投保人姓名，性别， 证件类型，证件号,出生日期，国籍，Email
	 *  被保人姓名，性别， 证件类型，证件号,出生日期，国籍，Email*/
	private String safetyProCodeStr, safetyProNameStr, safetyProTypeStr,
		safetyRiskCompanyStr,safetyRiskBillCodeStr,safetyBuyChannelStr,safetyRiskBillCredateStr,
		safetyRiskBillEffdateStr,safetyRiskBillEnddateStr,safetyCurrencyStr,safetyDealStr,
		safetyRiskFeeStr,safetyApplNameStr, safetyApplGenderStr, safetyApplCredTypeStr,
		safetyApplCredNumStr, safetyApplBirthDateStr, safetyApplNationalStr,
		safetyApplEmailStr,safetyApplByNameStr, safetyApplByGenderStr, safetyApplByCredTypeStr,
		safetyApplByCredNumStr, safetyApplByBirthDateStr, safetyApplByNationalStr,
		safetyApplByEmailStr;
	
	private Button btn1;
	private Button btn2;
	
	private Map<String, Object> safetyHoldProDetail;

	/** 　是否支持撤保, 是否支持退保, 是否支持续保, 是否支持续期*/
	private boolean cancelFlag, returnFlag, continueFlag, maintainFlag;
	private int funcFlag = 0;
	/**功能选择标志   撤保，退保，续保， 续期缴费*/
	private final int CANCEL_FLAG = 1, RETURN_FLAG = 2, 
			CONTINUE_FLAG = 3, CONTINUE_MAINTAIN = 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		getViewValue();
	}

	/** 初始化控件 */
	private void init() {
		addView(R.layout.safety_hold_prod_detail);
		setTitle(getString(R.string.safety_hold_pro_detail_title));
		//保单信息
		safetyProCode = (TextView) findViewById(R.id.safety_hold_pro_code);
		safetyProName = (TextView) findViewById(R.id.safety_hold_pro_name);
		safetyProType = (TextView) findViewById(R.id.safety_hold_pro_type);
		safetyRiskCompany = (TextView) findViewById(R.id.safety_hold_risk_company);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, safetyRiskCompany);
		safetyRiskBillCode = (TextView) findViewById(R.id.safety_hold_risk_bill_code);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, safetyRiskBillCode);
		safetyBuyChannel = (TextView) findViewById(R.id.safety_hold_buy_channel);
		safetyRiskBillCredate = (TextView) findViewById(R.id.safety_hold_risk_bill_credate);
		safetyRiskBillEffdate = (TextView) findViewById(R.id.safety_hold_risk_bill_effdate);
		safetyRiskBillEnddate = (TextView) findViewById(R.id.safety_hold_risk_bill_enddate);
		safetyCurrency = (TextView) findViewById(R.id.safety_hold_bizhong);
		safetyDeal = (TextView) findViewById(R.id.safety_hold_pro_detail_deal);
		safetyRiskFee = (TextView) findViewById(R.id.safety_hold_pro_detail_risk_fee);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, safetyRiskFee);
		//投保人
		safetyApplicantName = (TextView) findViewById(R.id.safety_hold_pro_detail_applicant_name);
		safetyApplicantGender = (TextView) findViewById(R.id.safety_hold_pro_detail_applicant_gender);
		safetyApplicantCredType = (TextView) findViewById(R.id.safety_hold_pro_detail_applicant_cred_type);
		safetyApplicantCredNum = (TextView) findViewById(R.id.safety_hold_pro_detail_applicant_cred_num);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, safetyApplicantCredNum);
		safetyApplicantBirthDate = (TextView) findViewById(R.id.safety_hold_pro_detail_applicant_birth_date);
		safetyApplicantNational = (TextView) findViewById(R.id.safety_hold_pro_detail_applicant_national);
		safetyApplicantEmail = (TextView) findViewById(R.id.safety_hold_pro_detail_applicant_email);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, safetyApplicantEmail);
		//被保人
		safetyApplByName = (TextView) findViewById(R.id.safety_hold_pro_detail_applicant_by_name);
		safetyApplByGender = (TextView) findViewById(R.id.safety_hold_pro_detail_applicant_by_gender);
		safetyApplByCredType = (TextView) findViewById(R.id.safety_hold_pro_detail_applicant_by_cred_type);
		safetyApplByCredNum = (TextView) findViewById(R.id.safety_hold_pro_detail_applicant_by_cred_num);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, safetyApplByCredNum);
		safetyApplByBirthDate = (TextView) findViewById(R.id.safety_hold_pro_detail_applicant_by_birth_date);
		safetyApplByNational = (TextView) findViewById(R.id.safety_hold_pro_detail_applicant_by_national);
		safetyApplByEmail = (TextView) findViewById(R.id.safety_hold_pro_detail_applicant_by_email);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, safetyApplByEmail);
		btn1 = (Button) findViewById(R.id.finc_btn1);
		btn2 = (Button) findViewById(R.id.finc_btn2);
	}

	/** 为控件赋值 */
	private void getViewValue() {
		safetyHoldProDetail = SafetyDataCenter.getInstance().getHoldProDetail();
		if (!StringUtil.isNullOrEmpty(safetyHoldProDetail)) {
			safetyProCodeStr = (String) safetyHoldProDetail
					.get(Safety.SAFETY_HOLD_INSU_CODE);
			safetyProNameStr = (String) safetyHoldProDetail.get(Safety.SAFETY_HOLD_RISK_NAME);
			safetyProTypeStr = (String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_RISK_TYPE);
			safetyRiskCompanyStr = (String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_INSU_NAME);
			safetyRiskBillCodeStr = (String) safetyHoldProDetail.get(Safety.SAFETY_HOLD_POLICY_NO);
			safetyBuyChannelStr = (String) safetyHoldProDetail.get(Safety.SAFETY_HOLD_CHANNEL);
			safetyRiskBillCredateStr = (String) safetyHoldProDetail.get(Safety.SAFETY_HOLD_APPL_DATE);
			safetyRiskBillEffdateStr = (String) safetyHoldProDetail
					.get(Safety.SAFETY_HOLD_POL_EFF_DATE);
			safetyRiskBillEnddateStr = (String) safetyHoldProDetail.get(Safety.SAFETY_HOLD_POL_END_DATE);
			safetyCurrencyStr = (String) safetyHoldProDetail.get(Safety.SAFETY_HOLD_CURRENCY);
			safetyDealStr = (String) safetyHoldProDetail.get(Safety.SAFETY_HOLD_RISK_UNIT);
			safetyRiskFeeStr = (String) safetyHoldProDetail.get(Safety.SAFETY_HOLD_RISK_PREM);
			
			safetyApplNameStr = (String) safetyHoldProDetail.get(Safety.SAFETY_HOLD_APPNAME);
			safetyApplGenderStr = (String) safetyHoldProDetail.get(Safety.SAFETY_HOLD_APPLSEX);
			safetyApplCredTypeStr = (String) safetyHoldProDetail.get(Safety.SAFETY_HOLD_APPLIDTYPE);
			safetyApplCredNumStr = (String) safetyHoldProDetail.get(Safety.SAFETY_HOLD_APPLIDNO);
			safetyApplBirthDateStr = (String) safetyHoldProDetail.get(Safety.SAFETY_HOLD_APPLBIRTH);
			safetyApplNationalStr = (String) safetyHoldProDetail.get(Safety.SAFETY_HOLD_APPLCTRYNO);
			safetyApplEmailStr = (String) safetyHoldProDetail.get(Safety.SAFETY_HOLD_APPLEMAIL);
			
//			benRelation = StringUtil.parseStrToBoolean(
//					(String) safetyHoldProDetail.get(Safety.SAFETY_HOLD_BEN_RELATION));
			safetyApplByNameStr = (String) safetyHoldProDetail.get(Safety.SAFETY_HOLD_BENNAME);
			safetyApplByGenderStr = (String) safetyHoldProDetail.get(Safety.SAFETY_HOLD_BENSEX);
			safetyApplByCredTypeStr = (String) safetyHoldProDetail.get(Safety.SAFETY_HOLD_BENIDTYPE);
			safetyApplByCredNumStr = (String) safetyHoldProDetail.get(Safety.SAFETY_HOLD_BENIDNO);
			safetyApplByBirthDateStr = (String) safetyHoldProDetail.get(Safety.SAFETY_HOLD_BENBIRTH);
			safetyApplByNationalStr = (String) safetyHoldProDetail.get(Safety.SAFETY_HOLD_BENCTRYNO);
			safetyApplByEmailStr = (String) safetyHoldProDetail.get(Safety.SAFETY_HOLD_BENEMAIL);
			
			cancelFlag = SafetyUtils.parseStrToBoolean((String)safetyHoldProDetail.
					get(Safety.SAFETY_HOLD_CANCEL_FLAG));
			returnFlag = SafetyUtils.parseStrToBoolean((String)safetyHoldProDetail.
					get(Safety.SAFETY_HOLD_RETURN_FLAG));
			continueFlag = SafetyUtils.parseStrToBoolean((String)safetyHoldProDetail.
					get(Safety.SAFETY_HOLD_CONTINUE_FLAG));
			maintainFlag = SafetyUtils.parseStrToBoolean((String)safetyHoldProDetail.
					get(Safety.SAFETY_HOLD_MAINTAIN_FLAG));
			setTextValues();
			setBtnsValueListener();
		}
	}
	
	private void setTextValues(){
		//保单信息
		safetyProCode.setText(StringUtil.valueOf1(safetyProCodeStr));
		safetyProName.setText(StringUtil.valueOf1(safetyProNameStr));
		safetyProType.setText(StringUtil.valueOf1(SafetyDataCenter.insuranceType.get(safetyProTypeStr)));
		safetyRiskCompany.setText(StringUtil.valueOf1(safetyRiskCompanyStr));
		safetyRiskBillCode.setText(StringUtil.valueOf1(safetyRiskBillCodeStr));
		safetyBuyChannel.setText(StringUtil.valueOf1(SafetyDataCenter.channelFlag.get(safetyBuyChannelStr)));
		safetyRiskBillCredate.setText(StringUtil.valueOf1(safetyRiskBillCredateStr));
		safetyRiskBillEffdate.setText(StringUtil.valueOf1(safetyRiskBillEffdateStr));
		safetyRiskBillEnddate.setText(StringUtil.valueOf1(safetyRiskBillEnddateStr));
		safetyCurrency.setText(StringUtil.valueOf1(LocalData.Currency.get(safetyCurrencyStr)));
		safetyDeal.setText(StringUtil.valueOf1(safetyDealStr));
		safetyRiskFee.setText(StringUtil.parseStringCodePattern(safetyCurrencyStr, safetyRiskFeeStr, 2));
		//投保人
		safetyApplicantName.setText(StringUtil.valueOf1(safetyApplNameStr));
		safetyApplicantGender.setText(StringUtil.valueOf1(SafetyDataCenter.gender.get(safetyApplGenderStr)));
		safetyApplicantCredType.setText(StringUtil.valueOf1(SafetyDataCenter.credType.get(safetyApplCredTypeStr)));
		safetyApplicantCredNum.setText(StringUtil.valueOf1(safetyApplCredNumStr));
		safetyApplicantBirthDate.setText(StringUtil.valueOf1(safetyApplBirthDateStr));
		safetyApplicantNational.setText(StringUtil.valueOf1(SafetyDataCenter.countryMap.get(safetyApplNationalStr)));
		safetyApplicantEmail.setText(StringUtil.valueOf1(safetyApplEmailStr));
		//被保人
		safetyApplByName.setText(StringUtil.valueOf1(safetyApplByNameStr));
		safetyApplByGender.setText(StringUtil.valueOf1(SafetyDataCenter.gender.get(safetyApplByGenderStr)));
		safetyApplByCredType.setText(StringUtil.valueOf1(SafetyDataCenter.credType.get(safetyApplByCredTypeStr)));
		safetyApplByCredNum.setText(StringUtil.valueOf1(safetyApplByCredNumStr));
		safetyApplByBirthDate.setText(StringUtil.valueOf1(safetyApplByBirthDateStr));
		safetyApplByNational.setText(StringUtil.valueOf1(SafetyDataCenter.countryMap.get(safetyApplByNationalStr)));
		safetyApplByEmail.setText(StringUtil.valueOf1(safetyApplByEmailStr));
	}
	
	private void setBtnsValueListener(){
		final String[] selectors = getSelectors(safetyHoldProDetail);
		OnClickListener btn1Listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//去产品介绍
				requestPsnInsuranceProductDetails();
			}
		};
		btn1.setOnClickListener(btn1Listener);
		if(selectors == null || selectors.length == 0){
			btn2.setVisibility(View.GONE);
			return;
		}
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				chooseFunction(SafetyHoldProductQueryDetailActivity.this.getBtnMap().get(
						selectors[0]));
			}
		};
		if (selectors.length == 1) {// 
			btn2.setText(selectors[0]);
			btn2.setOnClickListener(onClickListener);
			if(selectors[0] == null || selectors[0].equals(""))
				btn2.setVisibility(View.GONE);
		} else if(selectors.length > 1){
			btn2.setText(getString(R.string.more));
			PopupWindowUtils.getInstance().setshowMoreChooseUpListener(
					this, btn2, selectors, new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							Integer tag = (Integer) v.getTag();
							String selector = selectors[tag];
							chooseFunction(SafetyHoldProductQueryDetailActivity.this
									.getBtnMap().get(selector));
						}
					});
		}else{
			btn2.setVisibility(View.GONE);
		}
	}
	
	private void chooseFunction(int i){
		switch (i) {
		case 1:
			//撤保
			funcFlag = CANCEL_FLAG;
			insuranceCancel();
			break;
		case 2:
			//退保
			funcFlag = RETURN_FLAG;
			insuranceReturn();
			break;
		case 3:
			//续保
			funcFlag = CONTINUE_FLAG;
			insuranceContinue();
			break;
		case 4:
			//续期缴费
			funcFlag = CONTINUE_MAINTAIN;
			insuranceMaintain();
			break;
		default:
			break;
		}
	}
	
	//撤保
	private void insuranceCancel(){
		BaseDroidApp.getInstanse().showErrorDialog(
				getString(R.string.safety_sure_to_cancel), R.string.cancle, 
				R.string.confirm,new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						switch ((Integer) arg0.getTag()) {
						case CustomDialog.TAG_CANCLE:
							BaseDroidApp.getInstanse().dismissMessageDialog();
							break;
						case CustomDialog.TAG_SURE:
							BaseDroidApp.getInstanse().dismissMessageDialog();
							requestCommConversationId();
							break;
						}
					}
				});
	}
	
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID).toString());
	}
	
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		switch (funcFlag) {
		case CANCEL_FLAG:
			requestPsnInsuranceDetailsQuery();
			break;
		case RETURN_FLAG:
			requestPsnInsuranceReturn();
			break;
		case CONTINUE_FLAG:
			break;
		case CONTINUE_MAINTAIN:
			break;
		default:
			break;
		}
	}
	
	/**
	 * 退保
	 * @author fsm
	 */
	public void requestPsnInsuranceReturn() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID).toString());
		biiRequestBody.setMethod(Safety.SafetyInsuranceReturn);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Safety.SAFETY_HOLD_TRANS_DATE, 
				(String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_TRANS_DATE));
		paramsmap.put(Safety.SAFETY_HOLD_TRANS_ACCNO, 
				(String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_TRANS_ACCNO));
		paramsmap.put(Safety.SAFETY_HOLD_POLICY_NO, 
				(String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_POLICY_NO));
//		paramsmap.put(Safety.ACC_ID, (String)accDetail.get(Acc.ACC_ACCOUNTID_RES));
//		paramsmap.put(Safety.RECV_NAME, (String)accDetail.get(Safety.ACCOUNT_NAME));
		paramsmap.put(Safety.SAFETY_HOLD_INSU_ID, 
				(String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_INSU_ID));
		paramsmap.put(Safety.SAFETY_HOLD_INSU_NAME, 
				(String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_INSU_NAME));
		paramsmap.put(Safety.SAFETY_HOLD_RISK_CODE, 
				(String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_RISK_CODE));
		paramsmap.put(Safety.SAFETY_HOLD_RISK_NAME, 
				(String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_RISK_NAME));
		paramsmap.put(Safety.SAFETY_HOLD_APPNAME, 
				(String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_APPNAME));
		paramsmap.put(Safety.SAFETY_HOLD_APPLIDNO, 
				(String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_APPLIDNO));
		paramsmap.put(SBRemit.TOKEN, 
				BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID));
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnInsuranceReturnCallback");
	}
	
	/**
	 * 退保回调
	 * @author fsm
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnInsuranceReturnCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = (Map<String, Object>)this.getHttpTools().getResponseResult(resultObj);
		if(result != null){
			safetyHoldProDetail.put(Safety.BACK_PREM, result.get(Safety.BACK_PREM));
			safetyHoldProDetail.put(Safety.EFFECTIVE_DATA, result.get(Safety.EFFECTIVE_DATA));
//			safetyHoldProDetail.putAll(accDetail);
			startActivityForResult(new Intent(this, SafetyInsuranceReturnActivity.class), 1);
		}
	}
	
	/**
	 * 产品介绍
	 * @author fsm
	 */
	public void requestPsnInsuranceProductDetails() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Safety.METHOD_INSURANCE_PRODUCT_INFO);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Safety.SAFETY_HOLD_INSU_ID, 
				(String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_INSU_ID));
		paramsmap.put(Safety.SAFETY_HOLD_INSU_CODE, 
				(String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_INSU_CODE));
		paramsmap.put(Safety.SAFETY_HOLD_RISK_CODE, 
				(String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_RISK_CODE));
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnInsuranceProductDetailsCallback");
	}
	
	/**
	 * 产品介绍回调
	 * @param resultObj
	 */
	public void requestPsnInsuranceProductDetailsCallback(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		@SuppressWarnings("unchecked")
		Map<String, Object> productInfoMap = (Map<String, Object>)this.getHttpTools().
				getResponseResult(resultObj);
		SafetyDataCenter.getInstance().setProductInfoMap(productInfoMap);
		startActivity(new Intent(this, SafetyProductIntrActivity.class));
		overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
	}
	
	/**
	 * 撤保
	 * @author fsm
	 */
	public void requestPsnInsuranceDetailsQuery() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID).toString());
		biiRequestBody.setMethod(Safety.SafetyInsuranceCancel);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Safety.SAFETY_HOLD_TRANS_ACCNO, 
				(String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_TRANS_ACCNO));
		paramsmap.put(Safety.SAFETY_HOLD_POLICY_NO, 
				(String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_POLICY_NO));
		paramsmap.put(Safety.SAFETY_HOLD_INSU_NAME, 
				(String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_INSU_NAME));
		paramsmap.put(Safety.SAFETY_HOLD_INSU_ID, 
				(String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_INSU_ID));
		paramsmap.put(Safety.SAFETY_HOLD_RISK_CODE, 
				(String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_RISK_CODE));
		paramsmap.put(Safety.SAFETY_HOLD_RISK_NAME, 
				(String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_RISK_NAME));
		paramsmap.put(Safety.SAFETY_HOLD_APPNAME, 
				(String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_APPNAME));
		paramsmap.put(Safety.SAFETY_HOLD_APPLIDNO, 
				(String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_APPLIDNO));
		paramsmap.put(Safety.SAFETY_HOLD_RISK_PREM, 
				(String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_RISK_PREM));
		paramsmap.put(SBRemit.TOKEN, BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID));
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnInsuranceDetailsQueryCallback");
	}
	
	
	/**
	 * 撤保回调
	 * @param resultObj
	 */
	public void requestPsnInsuranceDetailsQueryCallback(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		BaseDroidApp.getInstanse().showMessageDialog(getString(R.string.safety_cancel_succss), 
				new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				BaseDroidApp.getInstanse().dismissMessageDialog();
				setResult(RESULT_OK);
				finish();
			}
		});
	}
	
	//退保
	private void insuranceReturn(){
		BaseDroidApp.getInstanse().showErrorDialog(
				getString(R.string.safety_sure_to_return), R.string.cancle, 
				R.string.confirm,new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						switch ((Integer) arg0.getTag()) {
						case CustomDialog.TAG_CANCLE:
							BaseDroidApp.getInstanse().dismissMessageDialog();
							break;
						case CustomDialog.TAG_SURE:
							BaseDroidApp.getInstanse().dismissMessageDialog();
//							requestBankAcctList();
							requestCommConversationId();
							break;
						}
					}
				});
	}
	
	
	@Override
	public void bankAccListCallBack(Object resultObj) {
		super.bankAccListCallBack(resultObj);
		switch (funcFlag) {
		case CANCEL_FLAG:
			requestPsnInsuranceDetailsQuery();
			break;
		case RETURN_FLAG:
//			startActivityForResult(new Intent(this, ChooseAccountActivity.class), 1);
			break;
		case CONTINUE_FLAG:
			BaseHttpEngine.showProgressDialog();
			requestSystemDateTime();
			break;
		case CONTINUE_MAINTAIN:
			break;
		default:
			break;
		}
	}
	
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.requestSystemDateTimeCallBack(resultObj);
		BaseDroidApp.getInstanse().getBizDataMap().put(Safety.DATE_TIME, dateTime);
		switch (funcFlag) {
		case CONTINUE_FLAG:
			startActivityForResult(new Intent(this, SafetyInsuContInputActivity.class), 1);
			break;
		case CONTINUE_MAINTAIN:
			break;
		}
	}
	
	//续保
	private void insuranceContinue(){
		BaseHttpEngine.showProgressDialog();
		requestBankAcctList(SafetyDataCenter.accountTypeList);
	}
	
	
	//续期
	private void insuranceMaintain(){
		
	}

	private String[] getSelectors(Map<String, Object> map) {
		String[] selectors = new String[6];
		int i = 0;
		if (cancelFlag) {//cancelFlag
			selectors[i] = getString(R.string.safety_hold_pro_detail_btn_withdraw);
			i += 1;
		}
		if (returnFlag) {//returnFlag
			selectors[i] = getString(R.string.safety_hold_pro_detail_btn_quit);
			i += 1;
		}
		if (continueFlag) {//continueFlag
			selectors[i] = getString(R.string.safety_hold_pro_detail_btn_continu);
			i += 1;
		}
//		if (false) {//maintainFlag
//			selectors[i] = getString(R.string.safety_hold_pro_detail_btn_continu_pay);
//			i += 1;
//		}

		String[] result = new String[i ];
		for (int j = 0; j < i; j++) {
			result[j] = selectors[j];
		}
		return result;
	}

	/**
	 * 所有按钮 匹配
	 * 
	 * @return
	 */
	private Map<String, Integer> getBtnMap() {
		Map<String, Integer> map = new HashMap<String, Integer>() {
			private static final long serialVersionUID = 1L;
			{
				put(getString(R.string.safety_hold_pro_detail_btn_withdraw), 1);
				put(getString(R.string.safety_hold_pro_detail_btn_quit), 2);
				put(getString(R.string.safety_hold_pro_detail_btn_continu), 3);
				put(getString(R.string.safety_hold_pro_detail_btn_continu_pay), 4);
			}
		};
		return map;

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
