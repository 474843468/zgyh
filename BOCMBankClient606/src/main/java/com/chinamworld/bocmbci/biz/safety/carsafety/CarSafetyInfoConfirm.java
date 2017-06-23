package com.chinamworld.bocmbci.biz.safety.carsafety;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Inves;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyBaseActivity;
import com.chinamworld.bocmbci.biz.safety.SafetyConstant;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 车险信息确认页面
 * 
 * @author Zhi
 */
public class CarSafetyInfoConfirm extends SafetyBaseActivity {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** 主显示视图 */
	private View mMainView;
	/** 城市代码 */
	private String cityCode = (String) SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.CITYCODE);
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.safety_msgfill_title);
		mMainView = LayoutInflater.from(this).inflate(R.layout.safety_carsafety_insurance_confirm, null);
		findViewById(R.id.sliding_body).setPadding(0, 0, 0,getResources().getDimensionPixelSize(R.dimen.fill_margin_bottom));
		initView();
		addView(mMainView);
	}
	
	/** 初始化界面组件 */
	private void initView() {
		setStep3();
		showSafetyList();
		((TextView) mMainView.findViewById(R.id.tv_safetytype)).setText("车险");
		((TextView) mMainView.findViewById(R.id.tv_company)).setText("中银保险有限公司");
		// 获取用户输入的数据
		Map<String, Object> userInput = SafetyDataCenter.getInstance().getMapCarSafetyUserInput();
		if (userInput.get(Safety.NEWCARFLAG).equals("1")) {
			((TextView) mMainView.findViewById(R.id.tv_licenseNo)).setText("新车无车牌号");
		} else {
			((TextView) mMainView.findViewById(R.id.tv_licenseNo)).setText(StringUtil.valueOf1((String) userInput.get(Safety.LICENSENO)));
		}
		((TextView) mMainView.findViewById(R.id.tv_enrollDate)).setText(StringUtil.valueOf1((String) userInput.get(Safety.ENROLLDATE)));
		((TextView) mMainView.findViewById(R.id.tv_brandName)).setText(StringUtil.valueOf1((String) userInput.get(Safety.BRANDNAME)));
		((TextView) mMainView.findViewById(R.id.tv_frameNo)).setText(StringUtil.valueOf1((String) userInput.get(Safety.FRAMENO)));
		((TextView) mMainView.findViewById(R.id.tv_engineNo)).setText(StringUtil.valueOf1((String) userInput.get(Safety.ENGINENO)));
		// 用户选择的车型信息
		Map<String, Object> carType = SafetyDataCenter.getInstance().getListAutoInsuranceQueryAutoType().get((Integer) userInput.get("selectPosition"));
		((TextView) mMainView.findViewById(R.id.tv_vehicleBrand)).setText(StringUtil.valueOf1((String) carType.get(Safety.VEHICLEBRAND)));
		((TextView) mMainView.findViewById(R.id.tv_vehicleModel)).setText(StringUtil.valueOf1((String) carType.get(Safety.VEHICLEMODEL)));
		((TextView) mMainView.findViewById(R.id.tv_modelYear)).setText(StringUtil.valueOf1((String) carType.get(Safety.MODELYEAR)));
		((TextView) mMainView.findViewById(R.id.tv_seatNum)).setText(StringUtil.valueOf1((String) carType.get(Safety.SEATNUM)));
		((TextView) mMainView.findViewById(R.id.tv_newCarPrice)).setText(StringUtil.valueOf1(StringUtil.parseStringPattern((String) carType.get(Safety.NEWCARPRICE), 2)));
		// 交强险返回数据
		Map<String, Object> jqxInfo = SafetyDataCenter.getInstance().getMapAutoInsuranceQueryCompulsory();
//		((TextView) mMainView.findViewById(R.id.tv_taxCurrent)).setText(StringUtil.parseStringPattern((String) jqxInfo.get(Safety.TAXCURRENT), 2));
//		((TextView) mMainView.findViewById(R.id.tv_taxFormer)).setText(StringUtil.parseStringPattern((String) jqxInfo.get(Safety.TAXFORMER), 2));
//		((TextView) mMainView.findViewById(R.id.tv_taxLatafee)).setText(StringUtil.parseStringPattern((String) jqxInfo.get(Safety.TAXLATAFEE), 2));
//		((TextView) mMainView.findViewById(R.id.tv_totalTax)).setText(StringUtil.parseStringPattern((String) jqxInfo.get(Safety.TOTALTAX), 2));
		((TextView) mMainView.findViewById(R.id.tv_jqxPremium)).setText(StringUtil.parseStringPattern((String) jqxInfo.get(Safety.JQXPREMIUM), 2));
		if (StringUtil.isNullOrEmpty(SafetyDataCenter.getInstance().getMapAutoInsuranceQueryCompulsory().get(Safety.BIZINSBEGINDATE))
				|| StringUtil.isNullOrEmpty(SafetyDataCenter.getInstance().getMapAutoInsuranceQueryCommercial().get(Safety.AVAINSURLIST))) {
			mMainView.findViewById(R.id.ll_totalBizRealPremium).setVisibility(View.GONE);
		} else {
			((TextView) mMainView.findViewById(R.id.tv_totalBizRealPremium)).setText(StringUtil.valueOf1(StringUtil.parseStringPattern((String) SafetyDataCenter.getInstance().getMapAutoInsuranceCalculation().get(Safety.TOTALBIZREALPREMIUM), 2)));
		}
		((TextView) mMainView.findViewById(R.id.tv_totalTax2)).setText(StringUtil.valueOf1(StringUtil.parseStringPattern((String) jqxInfo.get(Safety.TOTALTAX), 2)));
		((TextView) mMainView.findViewById(R.id.tv_Current)).setText(StringUtil.valueOf1(StringUtil.parseStringPattern((String) SafetyDataCenter.getInstance().getMapAutoInsuranceCalculation().get(Safety.TOTALREALPREMIUM), 2)));
		((TextView) mMainView.findViewById(R.id.tv_carOwnerName)).setText(StringUtil.valueOf1((String) userInput.get(Safety.CAROWNERNAME)));
		((TextView) mMainView.findViewById(R.id.tv_carOwnerIdType)).setText(StringUtil.valueOf1(SafetyDataCenter.credType.get(userInput.get(Safety.CAROWNERIDENTIFYTYPE))));
		((TextView) mMainView.findViewById(R.id.tv_carOwnerId)).setText(StringUtil.valueOf1((String) userInput.get(Safety.CAROWNERIDENTIFYNO)));
		((TextView) mMainView.findViewById(R.id.tv_applicantName)).setText(StringUtil.valueOf1((String) userInput.get(Inves.CUSTOMERNAME)));
		((TextView) mMainView.findViewById(R.id.tv_applicantPhone)).setText(StringUtil.valueOf1((String) userInput.get(Safety.MOBILE)));
		((TextView) mMainView.findViewById(R.id.tv_applicantIdType)).setText(StringUtil.valueOf1(SafetyDataCenter.IDENTITYTYPE.get(userInput.get(Comm.IDENTITYTYPE))));
		((TextView) mMainView.findViewById(R.id.tv_applicantId)).setText(StringUtil.valueOf1((String) userInput.get(Comm.IDENTITYNUMBER)));
		((TextView) mMainView.findViewById(R.id.tv_applicantEmail)).setText(StringUtil.valueOf1((String) userInput.get(Safety.APPL_EMAIL)));
		((TextView) mMainView.findViewById(R.id.tv_byApplicantName)).setText(StringUtil.valueOf1((String) userInput.get(Safety.BEN_NAME)));
		((TextView) mMainView.findViewById(R.id.tv_byApplicantPhone)).setText(StringUtil.valueOf1((String) userInput.get(Safety.BEN_PHONE)));
		((TextView) mMainView.findViewById(R.id.tv_byApplicantIdType)).setText(StringUtil.valueOf1(SafetyDataCenter.credType.get(userInput.get(Safety.BEN_IDTYPE))));
		((TextView) mMainView.findViewById(R.id.tv_byApplicantId)).setText(StringUtil.valueOf1((String) userInput.get(Safety.BEN_IDNUM)));
		((TextView) mMainView.findViewById(R.id.tv_invoice_title)).setText(StringUtil.valueOf1((String) userInput.get(Safety.INV_TITLE)));
		((TextView) mMainView.findViewById(R.id.tv_invoice_hadname)).setText(StringUtil.valueOf1((String) userInput.get(Safety.INV_NAME)));
		((TextView) mMainView.findViewById(R.id.tv_invoice_hadphone)).setText(StringUtil.valueOf1((String) userInput.get(Safety.INV_PHONE)));
		((TextView) mMainView.findViewById(R.id.tv_invoice_adress)).setText(StringUtil.valueOf1((String) userInput.get(Safety.INV_ADRESS)));
		((TextView) mMainView.findViewById(R.id.tv_invoice_postcode)).setText(StringUtil.valueOf1((String) userInput.get(Safety.INV_POSTCODE)));
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(R.id.tv_brandName));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(R.id.tv_frameNo));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(R.id.tv_engineNo));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(R.id.tv_carOwnerId));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(R.id.tv_applicantId));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(R.id.tv_applicantEmail));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(R.id.tv_byApplicantId));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(R.id.tv_invoice_hadphone));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(R.id.tv_vehicleBrand));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(R.id.tv_vehicleModel));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(R.id.tv_invoice_adress));
		
		mMainView.findViewById(R.id.tv_TouBaoShengMing).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(CarSafetyInfoConfirm.this, CarSafetyMustKnow.class), 1);
			}
		});
		
		mMainView.findViewById(R.id.btnNext).setOnClickListener(nextListener);
		/** 判断是否需要手机验证码 */
		if (!StringUtil.isNullOrEmpty(SafetyDataCenter.getInstance().getMapAutoInsuranceCreatPolicy().get(Safety.SMSFLAG))) {
			if (SafetyDataCenter.getInstance().getMapAutoInsuranceCreatPolicy().get(Safety.SMSFLAG).equals("1")) {
				initSms();
			}
		}
	}
	
	/** 初始化手机验证码布局 */
	private void initSms() {
		mMainView.findViewById(R.id.ll_sms).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.tv_tip).setVisibility(View.VISIBLE);
		SmsCodeUtils.getInstance().addSmsCodeListner2((Button) mMainView.findViewById(R.id.smsbtn),smsListener);
		mMainView.findViewById(R.id.smsbtn).performClick();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) {
			if (data.getStringExtra("agree").equals("1")) {
				((CheckBox) mMainView.findViewById(R.id.cb_isCheck)).setChecked(true);
			} else if (data.getStringExtra("agree").equals("0")) {
				((CheckBox) mMainView.findViewById(R.id.cb_isCheck)).setChecked(false);
			}
		} else if (resultCode == 4) {
			setResult(4);
			finish();
		}
	}
	
	/** 添加确认页面的险别 */
	private void showSafetyList() {
		// 为提高页面加载速度，使用多线程来加载
		new Thread(new Runnable() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				View jqxItem = LayoutInflater.from(CarSafetyInfoConfirm.this).inflate(R.layout.safety_carsafety_carbiz_tvitem, null);
				((TextView) jqxItem.findViewById(R.id.tv_insName)).setText("交通事故责任强制保险");
				((TextView) jqxItem.findViewById(R.id.tv_keyForAmount)).setText("赔偿限额/保额：");
				((TextView) jqxItem.findViewById(R.id.tv_Amount)).setText(StringUtil.parseStringPattern((String) SafetyDataCenter.getInstance().getMapAutoInsuranceQueryCompulsory().get(Safety.JQXAMOUNT), 2));
				jqxItem.findViewById(R.id.ll_isMp).setVisibility(View.GONE);
				((TextView) jqxItem.findViewById(R.id.tv_Premium)).setText(((TextView) mMainView.findViewById(R.id.tv_jqxPremium)).getText());
				((LinearLayout) mMainView.findViewById(R.id.ll_safetyItemInfo)).addView(jqxItem);
				
				PopupWindowUtils.getInstance().setOnShowAllTextListener(CarSafetyInfoConfirm.this, (TextView) jqxItem.findViewById(R.id.tv_insName));
				PopupWindowUtils.getInstance().setOnShowAllTextListener(CarSafetyInfoConfirm.this, (TextView) jqxItem.findViewById(R.id.tv_keyForAmount));
				PopupWindowUtils.getInstance().setOnShowAllTextListener(CarSafetyInfoConfirm.this, (TextView) jqxItem.findViewById(R.id.tv_keyForisMp));
				
				List<Map<String, Object>> bizList = (List<Map<String, Object>>) SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get("bizList");
				if (StringUtil.isNullOrEmpty(bizList)) {
					mMainView.findViewById(R.id.ll_bj).setVisibility(View.GONE);
					return;
				}
				for (int i = 0; i < bizList.size(); i++) {
					View bizItem = LayoutInflater.from(CarSafetyInfoConfirm.this).inflate(R.layout.safety_carsafety_carbiz_tvitem, null);
					Map<String, Object> bizMap = bizList.get(i);
					((TextView) bizItem.findViewById(R.id.tv_insName)).setText((String) bizMap.get(Safety.INSNAME));
					
					if (((String) bizMap.get(Safety.INSNAME)).equals("玻璃单独破碎险")) {
						if (bizMap.get(Safety.AMOUNTINFO).equals("0")) {
							((TextView) bizItem.findViewById(R.id.tv_Amount)).setText("国产");
						} else {
							((TextView) bizItem.findViewById(R.id.tv_Amount)).setText("进口");
						}
					} else if (((String) bizMap.get(Safety.INSNAME)).equals("不计免赔合计")) {
						((TextView) mMainView.findViewById(R.id.tv_bj)).setText(StringUtil.parseStringPattern((String) bizMap.get(Safety.REALPREMIUM), 2));
						continue;
					} else {
						if (StringUtil.isNullOrEmpty(bizMap.get(Safety.AMOUNTINFO))) {
							((TextView) bizItem.findViewById(R.id.tv_Amount)).setText("-");
						} else {
							((TextView) bizItem.findViewById(R.id.tv_Amount)).setText(StringUtil.parseStringPattern((String) bizMap.get(Safety.AMOUNTINFO), 2));
						}
					}

					if (StringUtil.isNullOrEmpty(bizMap.get(Safety.ISMP))) {
						((TextView) bizItem.findViewById(R.id.tv_isMp)).setText("-");
					} else {
						if (bizMap.get(Safety.ISMP).equals("1")) {
							((TextView) bizItem.findViewById(R.id.tv_isMp)).setText("是");
						} else if (bizMap.get(Safety.ISMP).equals("0")) {
							if (!StringUtil.isNullOrEmpty(bizMap.get("ISMPSHOW"))) {
								bizItem.findViewById(R.id.ll_isMp).setVisibility(View.GONE);
							} else {
								((TextView) bizItem.findViewById(R.id.tv_isMp)).setText("否");
							}
						}
					}
					if (StringUtil.isNullOrEmpty(bizMap.get(Safety.REALPREMIUM))) {
						((TextView) bizItem.findViewById(R.id.tv_Premium)).setText("-");
					} else {
						((TextView) bizItem.findViewById(R.id.tv_Premium)).setText(StringUtil.parseStringPattern((String) bizMap.get(Safety.REALPREMIUM), 2));
					}
					((LinearLayout) mMainView.findViewById(R.id.ll_safetyItemInfo)).addView(bizItem);
					
					PopupWindowUtils.getInstance().setOnShowAllTextListener(CarSafetyInfoConfirm.this, (TextView) bizItem.findViewById(R.id.tv_insName));
					PopupWindowUtils.getInstance().setOnShowAllTextListener(CarSafetyInfoConfirm.this, (TextView) bizItem.findViewById(R.id.tv_keyForAmount));
					PopupWindowUtils.getInstance().setOnShowAllTextListener(CarSafetyInfoConfirm.this, (TextView) bizItem.findViewById(R.id.tv_keyForisMp));
				}
			}
		}).start();
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
	
	/**
	 * 手机验证码校验
	 * @return
	 */
	private boolean submitCheck(boolean required) {
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		if(onlyRegular(required, ((EditText) mMainView.findViewById(R.id.et_sms)).getText().toString().trim())){
			RegexpBean rebSms = new RegexpBean(getString(R.string.safety_carsafety_smsRex),
					((EditText) mMainView.findViewById(R.id.et_sms)).getText().toString().trim(), SafetyConstant.SMS);
			lists.add(rebSms);
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
	
	/** 获取手机验证码 */
	private void getPhoneTestSms() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Safety.CITYCODE, cityCode);
		httpTools.requestHttp(Safety.METHOD_PSNAUTOINSURANCEGETSMS, "requestPsnAutoInsuranceGetSMSCallBack", params, true);
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------控件事件-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	OnClickListener nextListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (((CheckBox) mMainView.findViewById(R.id.cb_isCheck)).isChecked()) {
				if (!StringUtil.isNullOrEmpty(SafetyDataCenter.getInstance().getMapAutoInsuranceCreatPolicy().get(Safety.SMSFLAG))) {
					if (SafetyDataCenter.getInstance().getMapAutoInsuranceCreatPolicy().get(Safety.SMSFLAG).equals("1")) {
						if (!submitCheck(true)) {
							return;
						}
						BaseHttpEngine.showProgressDialog();
						Map<String, Object> params = new HashMap<String, Object>();
						params.put(Safety.CITYCODE, cityCode);
						params.put(Safety.PHONETESTCODE, ((EditText) mMainView.findViewById(R.id.et_sms)).getText().toString().trim());
						httpTools.requestHttp(Safety.METHOD_PSNAUTOINSURANCESMSCONFIRM, "requestPsnAutoInsuranceSMSConfirmCallBack", params, true);
					} else {
						requestBankAcctList(SafetyDataCenter.accountTypeList);
					}
				} else {
					requestBankAcctList(SafetyDataCenter.accountTypeList);
				}
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog("您没有接受投保声明");
				return;
			}
		}
	};
	
	/** 获取手机验证码按钮监听 */
	OnClickListener smsListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			getPhoneTestSms();
		}
	};
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO ---------------------------------------网络请求与回调方法--------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 获取手机验证码回调 */
	public void requestPsnAutoInsuranceGetSMSCallBack(Object resultObj) {}
	
	/** 验证手机验证码回调 */
	public void requestPsnAutoInsuranceSMSConfirmCallBack(Object resultObj) {
		requestBankAcctList(SafetyDataCenter.accountTypeList);
	}
	
	/** 银行卡列表返回  */
	@Override
	public void bankAccListCallBack(Object resultObj) {
		super.bankAccListCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		startActivityForResult((new Intent(this, CarSafetyPayVerify.class)), 4);
	}
}
