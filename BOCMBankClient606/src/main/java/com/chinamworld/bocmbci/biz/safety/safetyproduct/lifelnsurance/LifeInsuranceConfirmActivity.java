package com.chinamworld.bocmbci.biz.safety.safetyproduct.lifelnsurance;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class LifeInsuranceConfirmActivity extends LifeInsuranceBaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainView = addView(R.layout.safety_life_product_info_confirm);
		setTitle(getString(R.string.safety_msgfill_title));
		findView();
		viewSet();
	}

	@Override
	protected void findView() {

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void viewSet() {
		SafetyDataCenter.getInstance().setMapSaveParams(new HashMap<String, Object>());
		Map<String, Object> userInput = SafetyDataCenter.getInstance().getMapUserInput();
		// 产品信息
		setTVText(R.id.tv_payYearType, SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.PAYYEARNAME));
		setTVText(R.id.tv_payYear, userInput.get(Safety.PAYYEAR));
		setTVText(R.id.tv_insuYearType, SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.INSUYEARNAME));
		setTVText(R.id.tv_insuYear, userInput.get(Safety.INSUYEAR));
		
		int selectPosition = (Integer) SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(SELECTPOSITION);
		Map<String, Object> selectInsur = SafetyDataCenter.getInstance().getListLifeInsuranceProductQuery().get(selectPosition);
		String calFlag = String.valueOf(selectInsur.get(Safety.CALFLAG) );
		if ("0".equals(calFlag)) {
			mMainView.findViewById(R.id.ll_copies).setVisibility(View.VISIBLE);
			((TextView) mMainView.findViewById(R.id.tv_copies)).setText((String) userInput.get(Safety.SAFETY_HOLD_RISK_UNIT));
		} else if ("1".equals(calFlag)) {
			mMainView.findViewById(R.id.ll_coverage).setVisibility(View.VISIBLE);
			((TextView) mMainView.findViewById(R.id.tv_coverage)).setText(StringUtil.parseStringPattern((String) userInput.get(Safety.SAFETY_HOLD_RISK_AMT), 2));
		} else if ("2".equals(calFlag)) {
			mMainView.findViewById(R.id.ll_riskPrem).setVisibility(View.VISIBLE);
			((TextView) mMainView.findViewById(R.id.tv_riskPrem)).setText(StringUtil.parseStringPattern((String) userInput.get(Safety.SAFETY_HOLD_RISK_PREM), 2));
		}
		// 投保人信息
		((TextView) mMainView.findViewById(R.id.tv_applName)).setText((String) userInput.get(Safety.APPL_NAME));
		((TextView) mMainView.findViewById(R.id.tv_applSex)).setText(((String) userInput.get(Safety.APPL_SEX)).equals("1") ? "男" : "女");
		((TextView) mMainView.findViewById(R.id.tv_applBirth)).setText((String) userInput.get(Safety.APPL_BIRTH));
		((TextView) mMainView.findViewById(R.id.tv_applCtryNo)).setText((String) SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.APPL_CTRYNAME));
		((TextView) mMainView.findViewById(R.id.tv_applIdType)).setText(SafetyDataCenter.credType.get(userInput.get(Safety.APPL_IDTYPE)));
		((TextView) mMainView.findViewById(R.id.tv_applIdNo)).setText((String) userInput.get(Safety.APPL_IDNO));
		((TextView) mMainView.findViewById(R.id.tv_applIdStartDate)).setText((String) userInput.get(Safety.APPLIDSTARTDATE));
		((TextView) mMainView.findViewById(R.id.tv_applIdEndDate)).setText((Boolean) SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.APPL_IS_FOREVER) ? "长期" : (String) userInput.get(Safety.APPLIDENDDATE));
		((TextView) mMainView.findViewById(R.id.tv_applMarriage)).setText(SafetyDataCenter.lifeMargiList.get(SafetyDataCenter.lifeMargiListrq.indexOf(userInput.get(Safety.APPL_MARRIAGE))));
		((TextView) mMainView.findViewById(R.id.tv_resiType)).setText(SafetyDataCenter.residentsType_CN.get(SafetyDataCenter.residentsType_CODE.indexOf(userInput.get(Safety.RESITYPE))));
		((TextView) mMainView.findViewById(R.id.tv_applHomeAddr)).setText((String) userInput.get(Safety.APPLHOMEADDR));
		((TextView) mMainView.findViewById(R.id.tv_applZipCode)).setText((String) userInput.get(Safety.APPL_POSTCODE));
		optionalShow((String) userInput.get(Safety.APPLEMPLOYER), R.id.ll_applEmployer, R.id.tv_applEmployer);//单位名称
		((TextView) mMainView.findViewById(R.id.tv_applMobile)).setText((String) userInput.get(Safety.APPLMOBILE));
		optionalShow((String) userInput.get(Safety.APPLHOMEPHONE), R.id.ll_applHomePhone, R.id.tv_applHomePhone);// 家庭电话
		optionalShow((String) userInput.get(Safety.APPLOFFICEPHONE), R.id.ll_applOfficePhone, R.id.tv_applOfficePhone);//办公电话
		optionalShow(StringUtil.parseStringPattern((String) userInput.get(Safety.APPLINCOME), 2), R.id.ll_applIncome, R.id.tv_applIncome);
		((TextView) mMainView.findViewById(R.id.tv_applEmail)).setText((String) userInput.get(Safety.APPL_EMAIL));
		//家庭年收入
		String familyincome=(String) userInput.get(Safety.FAMILYINCOME);
		if(StringUtil.isNull(familyincome)){
			mMainView.findViewById(R.id.ll_appfamilyIncome).setVisibility(View.GONE);
		}else{
			mMainView.findViewById(R.id.ll_appfamilyIncome).setVisibility(View.VISIBLE);
			((TextView) mMainView.findViewById(R.id.tv_appfamilyIncome)).setText(StringUtil.parseStringPattern(familyincome,2));
		}
		//个人保费预算
		String premBudget=(String) userInput.get(Safety.PREMBUDGET);
		if(StringUtil.isNull(premBudget)){
			mMainView.findViewById(R.id.ll_premBudget).setVisibility(View.GONE);
		}else{
			mMainView.findViewById(R.id.ll_premBudget).setVisibility(View.VISIBLE);
			((TextView) mMainView.findViewById(R.id.tv_premBudget)).setText(StringUtil.parseStringPattern(premBudget,2));
		}
		((TextView) mMainView.findViewById(R.id.tv_applJob)).setText(getResources().getStringArray(R.array.Job_CN)[Arrays.asList(getResources().getStringArray(R.array.Job_code)).indexOf(userInput.get(Safety.APPLJOBCODE))]);
		((TextView) mMainView.findViewById(R.id.tv_relationSured)).setText("本人");
		// 被保人信息
//		((TextView) mMainView.findViewById(R.id.tv_benName)).setText((String) userInput.get(Safety.BEN_NAME));
//		((TextView) mMainView.findViewById(R.id.tv_benSex)).setText(userInput.get(Safety.BEN_SEX).equals("1") ? "男" : "女");
//		((TextView) mMainView.findViewById(R.id.tv_benBirth)).setText((String) userInput.get(Safety.BEN_BIRTH));
//		((TextView) mMainView.findViewById(R.id.tv_benCtryNo)).setText((String) userInput.get(Safety.BEN_CTRYNAME));
//		((TextView) mMainView.findViewById(R.id.tv_benIdType)).setText(SafetyDataCenter.credTypeList.get(SafetyDataCenter.rqcredType.indexOf(userInput.get(Safety.BEN_IDTYPE))));
//		((TextView) mMainView.findViewById(R.id.tv_benIdNo)).setText((String) userInput.get(Safety.BEN_IDNUM));
//		((TextView) mMainView.findViewById(R.id.tv_benStartDate)).setText((String) userInput.get(Safety.BENSTARTDATE));
//		((TextView) mMainView.findViewById(R.id.tv_benEndDate)).setText((String) userInput.get(Safety.BENENDDATE));
//		((TextView) mMainView.findViewById(R.id.tv_benMarriage)).setText(SafetyDataCenter.margiList.get(SafetyDataCenter.margiListrq.indexOf(userInput.get(Safety.BEN_MARRAGE))));
//		((TextView) mMainView.findViewById(R.id.tv_benHomeAddr)).setText((String) userInput.get(Safety.BENHOMEADDR));
//		((TextView) mMainView.findViewById(R.id.tv_benZipCode)).setText((String) userInput.get(Safety.BEN_POSTCODE));
//		optionalShow((String) userInput.get(Safety.BENEMPLOYER), R.id.ll_benEmployer, R.id.tv_benEmployer);
//		((TextView) mMainView.findViewById(R.id.tv_benMobile)).setText((String) userInput.get(Safety.BENMOBILE));
//		optionalShow((String) userInput.get(Safety.BENHOMEPHONE), R.id.ll_benHomePhone, R.id.tv_benHomePhone);
//		optionalShow((String) userInput.get(Safety.BENOFFICEPHONE), R.id.ll_benOfficePhone, R.id.tv_benOfficePhone);
//		((TextView) mMainView.findViewById(R.id.tv_benIncome)).setText((String) userInput.get(Safety.BENINCOME));
//		((TextView) mMainView.findViewById(R.id.tv_benEmail)).setText((String) userInput.get(Safety.BEN_EMAIL));
//		((TextView) mMainView.findViewById(R.id.tv_benJob)).setText(getResources().getStringArray(R.array.Job_CN)[Arrays.asList(getResources().getStringArray(R.array.Job_code)).indexOf(userInput.get(Safety.BENJOBCODE))]);
		// 受益人信息
		((TextView) mMainView.findViewById(R.id.tv_bnfIndicator)).setText(userInput.get(Safety.BNFINDICATOR).equals("Y") ? "法定" : "指定");
		if (userInput.get(Safety.BNFINDICATOR).equals("N")) {
			mMainView.findViewById(R.id.ll_bnft).setVisibility(View.VISIBLE);
			List<Map<String, Object>> bnftList = (List<Map<String, Object>>) userInput.get(Safety.BENFTLIST);
			Map<String, Object> bnftInfo = bnftList.get(0);
//			((TextView) mMainView.findViewById(R.id.tv_bnftType)).setText(getResources().getStringArray(R.array.bnftType_CN)[Arrays.asList(getResources().getStringArray(R.array.bnftType_code)).indexOf(bnftInfo.get(Safety.BNFTTYPE))]);
			((TextView) mMainView.findViewById(R.id.tv_bnftName)).setText((String) bnftInfo.get(Safety.BNFTNAME));
			((TextView) mMainView.findViewById(R.id.tv_bnftSex)).setText(bnftInfo.get(Safety.BNFTSEX).equals("1") ? "男" : "女");
			((TextView) mMainView.findViewById(R.id.tv_bnftBirth)).setText((String) bnftInfo.get(Safety.BNFTBIRTH));
		// 606 指定受益人增加国籍、职业代码、联系电话
			((TextView) mMainView.findViewById(R.id.tv_nationality)).setText((String) bnftInfo.get("bnftCountryName"));
			((TextView) mMainView.findViewById(R.id.tv_phone)).setText((String) bnftInfo.get("bnftMobilePhone"));
			((TextView) mMainView.findViewById(R.id.tv_job)).setText(getResources().getStringArray(R.array.Job_CN)[Arrays.asList(getResources().getStringArray(R.array.Job_code)).indexOf(bnftInfo.get("bnftJobCode"))]);
				if(bnftInfo.containsKey("bnftCountryName")){
				bnftInfo.remove("bnftCountryName");
			}

//			if (bnftInfo.get(Safety.BNFTSEX).equals("1")) {
//				((TextView) mMainView.findViewById(R.id.tv_bnftRelation)).setText(getResources().getStringArray(R.array.guanxi_M_CN)[Arrays.asList(getResources().getStringArray(R.array.guanxi_M_Code)).indexOf(bnftInfo.get(Safety.BNFTRELATION))]);
//			} else {
//				((TextView) mMainView.findViewById(R.id.tv_bnftRelation)).setText(getResources().getStringArray(R.array.guanxi_W_CN)[Arrays.asList(getResources().getStringArray(R.array.guanxi_W_Code)).indexOf(bnftInfo.get(Safety.BNFTRELATION))]);
//			}
			((TextView) mMainView.findViewById(R.id.tv_bnftRelation)).setText(SafetyDataCenter.relation.get(SafetyDataCenter.relationrq.indexOf(bnftInfo.get(Safety.BNFTRELATION))));
			
			((TextView) mMainView.findViewById(R.id.tv_bnftIdType)).setText(SafetyDataCenter.credTypeList.get(SafetyDataCenter.rqcredType.indexOf(bnftInfo.get(Safety.BNFTIDTYPE))));
			((TextView) mMainView.findViewById(R.id.tv_bnftIdNo)).setText((String) bnftInfo.get(Safety.BNFTIDNO));
			((TextView) mMainView.findViewById(R.id.tv_bnftStartDate)).setText((String) bnftInfo.get(Safety.BNFTSTARTDATE));
			((TextView) mMainView.findViewById(R.id.tv_bnftEndDate)).setText((Boolean) SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.BNFT_IS_FOREVER) ? "长期" : (String) bnftInfo.get(Safety.BNFTENDDATE));
			((TextView) mMainView.findViewById(R.id.tv_bnftHomeAddr)).setText((String) bnftInfo.get(Safety.BNFTHOMEADDR));
		} else {
			mMainView.findViewById(R.id.ll_bnft).setVisibility(View.GONE);
		}
		// 其他信息
//		((TextView) mMainView.findViewById(R.id.tv_paymentMethod)).setText(getResources().getStringArray(R.array.PaymentMethod_CN)[Arrays.asList(getResources().getStringArray(R.array.PaymentMethod_code)).indexOf(userInput.get(Safety.TRADEWAY))]);
		((TextView) mMainView.findViewById(R.id.tv_healthInform)).setText(userInput.get(Safety.HEALTHFLAG).equals("Y") ? "有健康告知" : "无健康告知");
		((TextView) mMainView.findViewById(R.id.tv_professionalInform)).setText(userInput.get(Safety.OCCUPATIONFLAG).equals("Y") ? "有职业告知" : "无职业告知");
//		((TextView) mMainView.findViewById(R.id.tv_processingMode)).setText(getResources().getStringArray(R.array.processingMode_CN)[Arrays.asList(getResources().getStringArray(R.array.processingMode_code)).indexOf(userInput.get(Safety.DISPUTESHANDLETYPE))]);
//		optionalShow((String) userInput.get(Safety.ARBITRATIONNAME), R.id.ll_arbitrationName, R.id.tv_arbitrationName);
		((TextView) mMainView.findViewById(R.id.tv_serviceRecommNo)).setText(StringUtil.isNull((String) userInput.get(Safety.BUSINESSNUM)) ? "-" : (String) userInput.get(Safety.BUSINESSNUM));
		
		String bonusGetMode = (String) userInput.get(Safety.BONUSGETMODE);
		if (!StringUtil.isNull(bonusGetMode)) {
			((TextView) mMainView.findViewById(R.id.tv_bonusGetMode)).setText(getResources().getStringArray(R.array.bonusGetMode_CN)[Arrays.asList(getResources().getStringArray(R.array.bonusGetMode_code)).indexOf(bonusGetMode)]);
		} else {
			mMainView.findViewById(R.id.ll_bonusGetMode).setVisibility(View.GONE);
		}
		
		String mantainMethod = (String) userInput.get(Safety.MANTAINMETHOD);
		if (StringUtil.isNull(mantainMethod)) {
			mMainView.findViewById(R.id.ll_mantainMethod).setVisibility(View.GONE);
		} else if ("2".equals(mantainMethod)) {
			((TextView) mMainView.findViewById(R.id.tv_mantainMethod)).setText("-");
		} else {
			((TextView) mMainView.findViewById(R.id.tv_mantainMethod)).setText(getResources().getStringArray(R.array.mantainMethod_CN)[Arrays.asList(getResources().getStringArray(R.array.mantainMethod_code)).indexOf(mantainMethod)]);
		}
		
		String getYearFlag = (String) userInput.get(Safety.GETYEARFLAG);
		if (!StringUtil.isNull(getYearFlag)) {
			((TextView) mMainView.findViewById(R.id.tv_getYearFlag)).setText(getResources().getStringArray(R.array.getYearFlag_CN)[Arrays.asList(getResources().getStringArray(R.array.getYearFlag_code)).indexOf(getYearFlag)]);
		} else {
			mMainView.findViewById(R.id.ll_getYearFlag).setVisibility(View.GONE);
		}
		String bankAccount = (String) userInput.get(Safety.BANKACCOUNT);
		if (StringUtil.isNull(bankAccount)) {
			mMainView.findViewById(R.id.ll_bankAccount).setVisibility(View.GONE);
		} else {
			mMainView.findViewById(R.id.ll_bankAccount).setVisibility(View.VISIBLE);
			((TextView) mMainView.findViewById(R.id.tv_bankAccount)).setText(StringUtil.getForSixForString(bankAccount));
		}
		optionalShow((String) userInput.get(Safety.OPENINGBANK), R.id.ll_openingBank, R.id.tv_openingBank);
		optionalShow((String) userInput.get(Safety.APPLACCNAME), R.id.ll_applAccName, R.id.tv_applAccName);

		String getYearAge = (String) userInput.get(Safety.GETSTARTAGE);
		String getYearAgeTemp = (String) SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.GETSTARTAGE);
		if (StringUtil.isNull(getYearAge) && StringUtil.isNull(getYearAgeTemp)) {
			mMainView.findViewById(R.id.ll_getStartAge).setVisibility(View.GONE);
		} else {
			mMainView.findViewById(R.id.ll_getStartAge).setVisibility(View.VISIBLE);
			((TextView) mMainView.findViewById(R.id.tv_getStartAge)).setText(StringUtil.isNull(getYearAge) ? getYearAgeTemp : getYearAge);
		}
		
		String getYear = (String) userInput.get(Safety.GETYEAR);
		String getYearTemp = (String) SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.GETYEAR);
		if (StringUtil.isNull(getYear) && StringUtil.isNull(getYearTemp)) {
			mMainView.findViewById(R.id.ll_getYear).setVisibility(View.GONE);
		} else {
			mMainView.findViewById(R.id.ll_getYear).setVisibility(View.VISIBLE);
			((TextView) mMainView.findViewById(R.id.tv_getYear)).setText(StringUtil.isNull(getYear) ? getYearTemp : getYear);
		}

		String investTimeType = (String) userInput.get(Safety.INVESTTIMETYPE);
		if (!StringUtil.isNull(investTimeType)) {
			((TextView) mMainView.findViewById(R.id.tv_investTimeType)).setText(getResources().getStringArray(R.array.investTimeType_CN)[Arrays.asList(getResources().getStringArray(R.array.investTimeType_code)).indexOf(investTimeType)]);
		} else {
			mMainView.findViewById(R.id.ll_investTimeType).setVisibility(View.GONE);
		}
		
		String autoPayFlag = (String) userInput.get(Safety.AUTOPAYFLAG);
		if (StringUtil.isNull(autoPayFlag)) {
			mMainView.findViewById(R.id.ll_autoPayFlag).setVisibility(View.GONE);
		} else {
			((TextView) mMainView.findViewById(R.id.tv_autoPayFlag)).setText(autoPayFlag.equals("2") ? "自动垫交" : "不垫交");
		}
		
		String policyHandFlag = (String) userInput.get(Safety.POLICYHANDFLAG);
		if (!StringUtil.isNull(policyHandFlag)) {
			((TextView) mMainView.findViewById(R.id.tv_policyHandFlag)).setText(policyHandFlag.equals("1") ? "电子保单" : "纸质保单");
		} else {
			((TextView) mMainView.findViewById(R.id.tv_policyHandFlag)).setText("-");
		}
		if ("2".equals(policyHandFlag)) {
			// 邮寄保单显示地址
			mMainView.findViewById(R.id.ll_postAddr).setVisibility(View.VISIBLE);
			((TextView) mMainView.findViewById(R.id.tv_postAddr)).setText((String) userInput.get(Safety.POSTADDR));
		}
		
		mMainView.findViewById(R.id.btnNext).setOnClickListener(nextListener);
		PopupWindowUtils.getInstance().setOnShowAllTextListenerByViewGroup(this, (LinearLayout) mMainView.findViewById(R.id.ll_vg));
	}
	
	/** 下一步事件 */
	private OnClickListener nextListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			BaseHttpEngine.showProgressDialog();
			Map<String, Object> params = SafetyDataCenter.getInstance().getMapUserInput();
			getHttpTools().requestHttp(Safety.PSNLIFEINSURANCECALCULATION, "requestPsnLifeInsuranceCalculationCallBack", params, true);
		}
	};
	
	/** 寿险保费试算回调 */
	public void requestPsnLifeInsuranceCalculationCallBack(Object resultObj) {
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		if ("IBAS.T1029".equals(resultMap.get(Safety.RETURNCODE))) {
			// 后台报错
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(Safety.TRANDATE, resultMap.get(Safety.TRSDATE));
			params.put(Safety.TRANSEQ, resultMap.get(Safety.TRANSEQ));
			getHttpTools().requestHttp(Safety.PSNINSURANCEERRORINFOQUERY, "requestPsnInsuranceErrorInfoQueryCallBack", params);
		} else {
			BaseHttpEngine.dissMissProgressDialog();
			SafetyDataCenter.getInstance().setMapLifeInsuranceCalculation(resultMap);
			Intent intent = new Intent(this, LifeInsurancePayVerifyActivity.class);
			startActivityForResult(intent, 4444);
		}
	}

	/** 保险公司失败返回信息查询回调 */
	public void requestPsnInsuranceErrorInfoQueryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		String loseInfo = (String) resultMap.get(Safety.ERRORMSG);
//		Intent intent = new Intent(this, LifeInsurancePayLoseActivity.class);
//		intent.putExtra(Safety.ERRORMSG, loseInfo);
//		startActivityForResult(intent, 1);
		BaseDroidApp.getInstanse().showInfoMessageDialog(loseInfo);
		return;
	}
}
