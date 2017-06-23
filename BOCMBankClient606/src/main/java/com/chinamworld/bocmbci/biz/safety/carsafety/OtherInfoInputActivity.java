package com.chinamworld.bocmbci.biz.safety.carsafety;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Inves;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyConstant;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 投保人信息、被保人信息、保单及配送信息输入界面
 * 
 * @author Zhi
 */
public class OtherInfoInputActivity extends CarSafetyBaseActivity {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 主显示布局 */
	private View mMainView;
	/** 投保人手机号码 */
	private EditText etFirstphone;
	/** 投保人电子邮箱 */
	private EditText etFirstEmail;
	/** 被保人姓名 */
	private TextView tvSecondName;
	/** 被保人证件类型 */
	private TextView tvSecondIdType;
	/** 被保人证件号码 */
	private TextView tvSecondId;
	/** 被保人手机号码 */
	private EditText etSecondPhone;
	/** 发票抬头 */
	private EditText etThirdInvoicetitle;
	/** 发票邮寄地址 */
	private EditText etThirdInvoiceadress;
	/** 邮编 */
	private EditText etThirdPost;
	/** 收件人姓名*/
	private EditText etThirdInvoicehadname;
	/** 收件人联系电话 */
	private EditText etThirdInvoicehadphone;
	/** 下一步按钮 */
	private Button btnNext;
	/** 下一步按钮 */
	private Button btnNextBig;
	/** 暂存按钮 */
	private Button btnSave;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.safety_msgfill_title);
		mMainView = LayoutInflater.from(this).inflate(R.layout.safety_carsafety_otherinfo, null);
		findViewById(R.id.sliding_body).setPadding(0, 0, 0,getResources().getDimensionPixelSize(R.dimen.fill_margin_bottom));
		setStep3();
		initView();
		addView(mMainView);
	}
	
	/** 初始化布局元素 */
	private void initView() {
		mLeftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SafetyDataCenter.getInstance().isReturn = true;
				putSaveParams();
				finish();
			}
		});
		etFirstphone = (EditText) mMainView.findViewById(R.id.et_firstphone);
		etFirstEmail = (EditText) mMainView.findViewById(R.id.et_firstemail);
		
		tvSecondName = (TextView) mMainView.findViewById(R.id.tv_secondname);
		tvSecondIdType = (TextView) mMainView.findViewById(R.id.tv_secondidtype);
		tvSecondId = (TextView) mMainView.findViewById(R.id.tv_secondidnumber);
		etSecondPhone = (EditText) mMainView.findViewById(R.id.et_secondphone);
		
		etThirdInvoicetitle = (EditText) mMainView.findViewById(R.id.et_third_invoicetitle);
		EditTextUtils.setLengthMatcher(this, etThirdInvoicetitle, 20);
		etThirdInvoiceadress = (EditText) mMainView.findViewById(R.id.et_third_invoiceadress);
		EditTextUtils.setLengthMatcher(this, etThirdInvoiceadress, 80);
		etThirdPost = (EditText) mMainView.findViewById(R.id.et_third_post);
		etThirdInvoicehadname = (EditText) mMainView.findViewById(R.id.et_third_invoicehadname);
		EditTextUtils.setLengthMatcher(this, etThirdInvoicehadname, 20);
		etThirdInvoicehadphone = (EditText) mMainView.findViewById(R.id.et_third_invoicehadphone);
		
		btnNext = (Button) mMainView.findViewById(R.id.btnNext);
		btnNext.setOnClickListener(onClickListener);
		btnNextBig = (Button) mMainView.findViewById(R.id.btnNext_big);
		btnNextBig.setOnClickListener(onClickListener);

		btnSave = (Button) mMainView.findViewById(R.id.btnSave);
		if (!SafetyDataCenter.getInstance().isHoldToThere) {
			btnSave.setOnClickListener(saveClickListener);
		} else {
			btnSave.setVisibility(View.GONE);
			btnNext.setVisibility(View.GONE);
			btnNextBig.setVisibility(View.VISIBLE);
		}
		
		if (SafetyDataCenter.getInstance().isSaveToThere) {
			initTempViewShow();
		} else {
			initViewShow();
		}
	}
	
	/** 初始化信息显示 */
	@SuppressWarnings("unchecked")
	private void initViewShow() {
		Map<String, Object> logInfo = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA);
		// 投保人信息显示
		((TextView) mMainView.findViewById(R.id.tv_firstname)).setText((String) logInfo.get(Inves.CUSTOMERNAME));
		String identityType = (String) logInfo.get(Comm.IDENTITYTYPE);
		if (identityType.equals("47") || identityType.equals("48")) {
			((TextView) mMainView.findViewById(R.id.tv_firstidtype)).setText(SafetyDataCenter.credType.get(identityType));
		}else{
			((TextView) mMainView.findViewById(R.id.tv_firstidtype)).setText(SafetyDataCenter.IDENTITYTYPE.get(identityType));
		}
		((TextView) mMainView.findViewById(R.id.tv_firstidnumber)).setText((String) logInfo.get(Comm.IDENTITYNUMBER));
		etFirstphone.setText((String) logInfo.get(Safety.MOBILE));

		Map<String, Object> userInput = SafetyDataCenter.getInstance().getMapCarSafetyUserInput();
		// 被保人信息显示
		tvSecondName.setText((String) userInput.get(Safety.CAROWNERNAME));
		tvSecondIdType.setText(SafetyDataCenter.credType.get(userInput.get(Safety.CAROWNERIDENTIFYTYPE)));
		tvSecondId.setText((String) userInput.get(Safety.CAROWNERIDENTIFYNO));
		// 发票及配送信息显示
		etThirdInvoicetitle.setText(((TextView) mMainView.findViewById(R.id.tv_firstname)).getText());
		
		if (SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.CONTINUEFLAG).equals("true")) {
			Map<String, Object> holdProDetil = SafetyDataCenter.getInstance().getHoldProDetail();
			if (!StringUtil.isNullOrEmpty(holdProDetil.get(Safety.APPL_EMAIL))) {
				etFirstEmail.setText((String) holdProDetil.get(Safety.APPL_EMAIL));
			} else {
				etFirstEmail.setText((String) SafetyDataCenter.getInstance().getMapSVRPsnInfoQuery().get(Safety.EMAIL));
			}
			
			etThirdInvoiceadress.setText((String) SafetyDataCenter.getInstance().getMapSVRPsnInfoQuery().get(Safety.ADRESS));
			etThirdInvoicehadname.setText((String) SafetyDataCenter.getInstance().getMapSVRPsnInfoQuery().get(Safety.NAME));
			etThirdInvoicehadphone.setText((String) SafetyDataCenter.getInstance().getMapSVRPsnInfoQuery().get(Safety.MOBILE));
			etThirdPost.setText((String) SafetyDataCenter.getInstance().getMapSVRPsnInfoQuery().get(Safety.POSTCODE));
		}
		
		if (SafetyDataCenter.getInstance().isReturn) {
			initReturnShow();
		}
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(R.id.tv_firstidnumber));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvSecondId);
	}
	
	/** 初始化暂存保单继续投保时数据反显 */
	@SuppressWarnings("unchecked")
	private void initTempViewShow() {
		Map<String, Object> tempMap = SafetyDataCenter.getInstance().getMapAutoInsurPolicyDetailQry();
		Map<String, Object> logInfo = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA);
		Map<String, Object> userInput = SafetyDataCenter.getInstance().getMapCarSafetyUserInput();
		// 投保人信息显示
		((TextView) mMainView.findViewById(R.id.tv_firstname)).setText((String) logInfo.get(Inves.CUSTOMERNAME));
		String identityType = (String) logInfo.get(Comm.IDENTITYTYPE);
		if (identityType.equals("47") || identityType.equals("48")) {
			((TextView) mMainView.findViewById(R.id.tv_firstidtype)).setText(SafetyDataCenter.credType.get(identityType));
		}else{
			((TextView) mMainView.findViewById(R.id.tv_firstidtype)).setText(SafetyDataCenter.IDENTITYTYPE.get(identityType));
		}
		((TextView) mMainView.findViewById(R.id.tv_firstidnumber)).setText((String) logInfo.get(Comm.IDENTITYNUMBER));
		etFirstphone.setText((String) tempMap.get(Safety.APPLPHONENO));
		etFirstEmail.setText((String) tempMap.get(Safety.APPL_EMAIL));
		// 被保人信息显示
		etSecondPhone.setText((String) tempMap.get(Safety.INSUREDPHONENO));
		tvSecondName.setText((String) userInput.get(Safety.CAROWNERNAME));
		tvSecondIdType.setText(SafetyDataCenter.credType.get(userInput.get(Safety.CAROWNERIDENTIFYTYPE)));
		tvSecondId.setText((String) userInput.get(Safety.CAROWNERIDENTIFYNO));
		// 发票及配送信息显示
		etThirdInvoicetitle.setText((String) tempMap.get(Safety.INVTITLE));
		etThirdInvoicehadname.setText((String) tempMap.get(Safety.RECIPIENT));
		etThirdInvoicehadphone.setText((String) tempMap.get(Safety.LINKPHONENO));
		etThirdInvoiceadress.setText((String) tempMap.get(Safety.DISTRIBUTADDR));
		etThirdPost.setText((String) tempMap.get(Safety.POSTCODE_TEMP));
	}
	
	/** 如果之前从此页面返回过，要将之前的状态恢复过来 */
	private void initReturnShow() {
		Map<String, Object> map = SafetyDataCenter.getInstance().getMapSaveParams();
		etFirstphone.setText((String) map.get(Safety.APPLPHONENO));
		etFirstEmail.setText((String) map.get(Safety.APPL_EMAIL));
		etSecondPhone.setText((String) map.get(Safety.INSUREDPHONENO));
		etThirdInvoicetitle.setText((String) map.get(Safety.INV_TITLE));
		etThirdInvoicehadname.setText((String) map.get(Safety.RECIPIENT));
		etThirdInvoicehadphone.setText((String) map.get(Safety.LINKPHONENO));
		etThirdInvoiceadress.setText((String) map.get(Safety.DISTRIBUTADDR));
		etThirdPost.setText((String) map.get(Safety.POSTCODE));
	}
	
	/** 保存用户输入数据 */
	@SuppressWarnings("unchecked")
	private void saveUserInput() {
		Map<String, Object> userInput = SafetyDataCenter.getInstance().getMapCarSafetyUserInput();
		Map<String, Object> logInfo = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA);
		// 投保人信息
		userInput.put(Inves.CUSTOMERNAME, ((TextView) mMainView.findViewById(R.id.tv_firstname)).getText().toString().trim());
		userInput.put(Comm.IDENTITYTYPE, logInfo.get(Comm.IDENTITYTYPE));
		userInput.put(Comm.IDENTITYNUMBER, ((TextView) mMainView.findViewById(R.id.tv_firstidnumber)).getText().toString().trim());
		userInput.put(Safety.MOBILE, etFirstphone.getText().toString().trim());
		userInput.put(Safety.APPL_EMAIL, etFirstEmail.getText().toString().trim());
		// 被保人信息
		userInput.put(Safety.BEN_NAME, tvSecondName.getText().toString().trim());
		userInput.put(Safety.BEN_PHONE, etSecondPhone.getText().toString().trim());
		userInput.put(Safety.BEN_IDTYPE, userInput.get(Safety.CAROWNERIDENTIFYTYPE));
		userInput.put(Safety.BEN_IDNUM, tvSecondId.getText().toString().trim());
		// 保单及配送信息
		userInput.put(Safety.INV_TITLE, etThirdInvoicetitle.getText().toString().trim());
		userInput.put(Safety.INV_NAME, etThirdInvoicehadname.getText().toString().trim());
		userInput.put(Safety.INV_PHONE, etThirdInvoicehadphone.getText().toString().trim());
		userInput.put(Safety.INV_ADRESS, etThirdInvoiceadress.getText().toString().trim());
		userInput.put(Safety.INV_POSTCODE, etThirdPost.getText().toString().trim());
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
	
	/** 请求车险投保单生成接口 */
	@SuppressWarnings("unchecked")
	private void requestPsnAutoInsuranceCreatPolicy() {
		BaseHttpEngine.showProgressDialog();
		Map<String, Object> userInput = SafetyDataCenter.getInstance().getMapCarSafetyUserInput();
		Map<String, Object> jqxInfo = SafetyDataCenter.getInstance().getMapAutoInsuranceQueryCompulsory();
		Map<String, Object> calculation = SafetyDataCenter.getInstance().getMapAutoInsuranceCalculation();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Safety.ZONENO, userInput.get(Safety.ZONENO));
		params.put(Safety.CITYCODE, userInput.get(Safety.CITYCODE));
		params.put(Safety.CONTINUEFLAG, userInput.get(Safety.CONTINUEFLAG));
		params.put(Safety.INSUCOMID, "2001");
		
		params.put(Safety.JQXINSBEGINDATE, jqxInfo.get(Safety.JQXINSBEGINDATE));
		params.put(Safety.JQXINSENDDATE, jqxInfo.get(Safety.JQXINSENDDATE));
		params.put(Safety.JQXPREMIUM, jqxInfo.get(Safety.JQXPREMIUM));
		if (StringUtil.isNullOrEmpty(((List<Map<String, Object>>) userInput.get("bizList")))) {
			params.put(Safety.RISKFLAG, "0");
		} else {
			params.put(Safety.RISKFLAG, "1");
			params.put(Safety.BIZINSBEGINDATE, jqxInfo.get(Safety.BIZINSBEGINDATE));
			params.put(Safety.BIZINSENDDATE, jqxInfo.get(Safety.BIZINSENDDATE));
			params.put(Safety.TOTALBIZSTANDPREMIUM, calculation.get(Safety.TOTALBIZSTANDPREMIUM));
			params.put(Safety.TOTALBIZREALPREMIUM, calculation.get(Safety.TOTALBIZREALPREMIUM));
		}
		params.put(Safety.JQXINSCODE, jqxInfo.get(Safety.JQXINSCODE));
		params.put(Safety.JQXINSNAME, jqxInfo.get(Safety.JQXINSNAME));
		params.put(Safety.JQXINSAMOUNT, jqxInfo.get(Safety.JQXAMOUNT));
		params.put(Safety.TAXCURRENT, jqxInfo.get(Safety.TAXCURRENT));
		params.put(Safety.TAXFORMER, jqxInfo.get(Safety.TAXFORMER));
		params.put(Safety.TAXLATAFEE, jqxInfo.get(Safety.TAXLATAFEE));
		params.put(Safety.TOTALTAX, jqxInfo.get(Safety.TOTALTAX));
		if (userInput.get(Safety.CITYCODE).equals("120000")) {
			params.put(Safety.TRUNLNADDRESS, userInput.get(Safety.TRUNLNADDRESS));
		}
		
		params.put(Safety.APPNTNAME, userInput.get(Inves.CUSTOMERNAME));
		params.put(Safety.APPNTIDENTIFYTYPE, SafetyDataCenter.IDENTITYTYPE_credType.get(userInput.get(Comm.IDENTITYTYPE)));
		params.put(Safety.APPNTIDENTIFYNO, userInput.get(Comm.IDENTITYNUMBER));
		params.put(Safety.APPNTPHONENO, userInput.get(Safety.MOBILE));
		params.put(Safety.APPNTEMAIL, userInput.get(Safety.APPL_EMAIL));
		
		params.put(Safety.INSUREDNAME, userInput.get(Safety.BEN_NAME));
		params.put(Safety.INSUREDPHONENO, userInput.get(Safety.BEN_PHONE));
		params.put(Safety.INSUREDIDENTIFYTYPE, userInput.get(Safety.BEN_IDTYPE));
		params.put(Safety.INSUREDIDENTIFYNO, userInput.get(Safety.BEN_IDNUM));
		
		params.put(Safety.CAROWNERNAME, userInput.get(Safety.CAROWNERNAME));
		params.put(Safety.CAROWNERIDENTIFYTYPE, userInput.get(Safety.CAROWNERIDENTIFYTYPE));
		params.put(Safety.CAROWNERIDENTIFYNO, userInput.get(Safety.CAROWNERIDENTIFYNO));
		
		params.put(Safety.INVOICENAME, userInput.get(Safety.INV_TITLE));
		params.put(Safety.RECIPIENT, userInput.get(Safety.INV_NAME));
		params.put(Safety.LINKPHONENO, userInput.get(Safety.INV_PHONE));
		params.put(Safety.DISTRIBUTIONADDRESS, userInput.get(Safety.INV_ADRESS));
		params.put(Safety.POSTCODE, userInput.get(Safety.INV_POSTCODE));
		
		if (!StringUtil.isNullOrEmpty(userInput.get(Safety.LICENSENO))) {
			params.put(Safety.LICENSENO, userInput.get(Safety.LICENSENO));
		}
		if (!StringUtil.isNullOrEmpty(userInput.get(Safety.BRANDNAME))) {
			params.put(Safety.BRANDNAME, ((Map<String, Object>) userInput.get("UserChooseVehicle")).get(Safety.VEHICLEMODEL));
		}
		params.put(Safety.FRAMENO, userInput.get(Safety.FRAMENO));
		params.put(Safety.ENGINENO, userInput.get(Safety.ENGINENO));
		params.put(Safety.ENROLLDATE, userInput.get(Safety.ENROLLDATE));
		if (StringUtil.isNullOrEmpty(userInput.get("bizList"))) {
			params.put(Safety.INUM, "0");
		} else {
			params.put(Safety.INUM, String.valueOf(((List<Map<String, Object>>) userInput.get("bizList")).size()));
		}
		if (params.get(Safety.RISKFLAG).equals("1")) {
			params.put(Safety.BIZINSURINFOLIST, userInput.get("bizList"));
		} else {
			 List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			params.put(Safety.BIZINSURINFOLIST, list);
		}
		httpTools.requestHttp(Safety.METHOD_PSNAUTOINSURANCECREATPOLICY, "requestPsnAutoInsuranceCreatPolicyCallBack", params, true);
	}
	
	/** 输入数据判空及校验 */
	private boolean submitRegexp(boolean required) {
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		// 投保人手机号码
		if (onlyRegular(required, etFirstphone.getText().toString().trim())) {
			RegexpBean buyerEmail = new RegexpBean(SafetyConstant.PHONE_BUYER, etFirstphone.getText().toString().trim(), SafetyConstant.CARSAFETY_MOBILE);
			lists.add(buyerEmail);
		}
		// 投保人电子邮箱
		if (onlyRegular(required, etFirstEmail.getText().toString().trim())) {
			RegexpBean buyerEmail = new RegexpBean(SafetyConstant.BUY_EMAIL, etFirstEmail.getText().toString().trim(), SafetyConstant.CARSAFETYEMAIL);
			lists.add(buyerEmail);
		}
		// 被保人手机号码
		if (onlyRegular(required, etSecondPhone.getText().toString())) {
			RegexpBean bean = new RegexpBean(SafetyConstant.PHONE_HOLD, etSecondPhone.getText().toString(), SafetyConstant.CARSAFETY_MOBILE);
			lists.add(bean);
		}
//		// 发票抬头
//		if (onlyRegular(required, etThirdInvoicetitle.getText().toString().trim())) {
//			RegexpBean buyerPost = new RegexpBean(SafetyConstant.FAPIAO_TITLE, etThirdInvoicetitle.getText().toString().trim(), SafetyConstant.CARSAFETYFAPIAOTITLE);
//			lists.add(buyerPost);
//		}
		if (StringUtil.isNull(etThirdInvoicetitle.getText().toString()) && required) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请输入发票抬头");
			return false;
		}
		// 收件人姓名
		if (onlyRegular(required, etThirdInvoicehadname.getText().toString())) {
			RegexpBean buyerName = new RegexpBean(SafetyConstant.FAPIAO_NAME, etThirdInvoicehadname.getText().toString(), SafetyConstant.CARSAFETY_BYNAME);
			lists.add(buyerName);
		}
		// 收件人联系电话
		if (onlyRegular(required, etThirdInvoicehadphone.getText().toString())) {
			RegexpBean buyerName = new RegexpBean(SafetyConstant.FAPIAO_PHONE, etThirdInvoicehadphone.getText().toString(), SafetyConstant.MOBILE_PHONE);
			lists.add(buyerName);
		}
		// 地址
//		if (onlyRegular(required, etThirdInvoiceadress.getText().toString().trim())) {
//			RegexpBean buyerAdress = new RegexpBean(SafetyConstant.SEND_ADRESS, etThirdInvoiceadress.getText().toString().trim(), SafetyConstant.CARSAFETY_ADRESS);
//			lists.add(buyerAdress);
//		}
		// 收件人邮编
//		if (onlyRegular(required, etThirdPost.getText().toString().trim())) {
//			RegexpBean buyerPost = new RegexpBean(SafetyConstant._POST, etThirdPost.getText().toString().trim(), SafetyConstant.CARSAFETY_POST);
//			lists.add(buyerPost);
//		}
		if (!RegexpUtils.regexpDate(lists)) {
			return false;
		}
		return true;
	}
	
	/** 输入数据判空及校验 */
	private boolean submitPostCodeRegexp(boolean required) {
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		// 收件人邮编
		if (onlyRegular(required, etThirdPost.getText().toString().trim())) {
			RegexpBean buyerPost = new RegexpBean(SafetyConstant._POST, etThirdPost.getText().toString().trim(), SafetyConstant.CARSAFETY_POST);
			lists.add(buyerPost);
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
	
	/** 准备暂存单保存/更新接口在此页面需要的参数 */
	private void putSaveParams() {
		Map<String, Object> params = SafetyDataCenter.getInstance().getMapSaveParams();
		params.put(Safety.APPLPHONENO, etFirstphone.getText().toString().trim());
		params.put(Safety.APPL_EMAIL, etFirstEmail.getText().toString().trim());
		params.put(Safety.INSUREDPHONENO, etSecondPhone.getText().toString().trim());
		params.put(Safety.INV_TITLE, etThirdInvoicetitle.getText().toString().trim());
		params.put(Safety.RECIPIENT, etThirdInvoicehadname.getText().toString().trim());
		params.put(Safety.LINKPHONENO, etThirdInvoicehadphone.getText().toString().trim());
		params.put(Safety.DISTRIBUTADDR, etThirdInvoiceadress.getText().toString().trim());
		params.put(Safety.POSTCODE, etThirdPost.getText().toString().trim());
		System.out.println("投保关系人信息录入页-暂存保单参数列表\n" + params);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 4) {
			setResult(4);
			finish();
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			SafetyDataCenter.getInstance().isReturn = true;
			putSaveParams();
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	};
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------控件事件-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 点击监听事件 */
	OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnNext:
				// 下一步按钮
				if (!submitRegexp(true)) {
					return;
				}
				try {
					String str = new String(etThirdInvoiceadress.getText().toString().getBytes("GB2312"), "ISO-8859-1");
					if (str.length() == 0) {
						BaseDroidApp.getInstanse().showInfoMessageDialog("请输入邮寄地址");
						return;
					} else if (str.length() < 6) {
						BaseDroidApp.getInstanse().showInfoMessageDialog("邮寄地址请输入最小长度6位、最大长度80位的字符");
						return;
					}
				} catch (UnsupportedEncodingException e) {
					LogGloble.e("Invoiceadress", e.toString());
				}
				if (!submitPostCodeRegexp(true)) {
					return;
				}
				saveUserInput();
				if (!SafetyDataCenter.getInstance().isHoldToThere) {
					putSaveParams();
				}
				requestPsnAutoInsuranceCreatPolicy();
				break;
			case R.id.btnNext_big:
				btnNext.performClick();
				break;
			}
		}
	};

	/** 暂存保单按钮点击事件 */
	OnClickListener saveClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (!submitRegexp(false)) {
				return;
			}
			try {
				String str = new String(etThirdInvoiceadress.getText().toString().getBytes("GB2312"), "ISO-8859-1");
				if ((str.length() > 0) && (str.length() < 6)) {
					BaseDroidApp.getInstanse().showInfoMessageDialog("邮寄地址请输入最小长度6位、最大长度80位的字符");
					return;
				}
			} catch (UnsupportedEncodingException e) {
				LogGloble.e("Invoiceadress", e.toString());
			}
			if (!submitPostCodeRegexp(false)) {
				return;
			}
			putSaveParams();
			showSaveDialog();
		}
	};
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO ---------------------------------------网络请求与回调方法--------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 车险投保单生成回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnAutoInsuranceCreatPolicyCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = (Map<String, Object>) this.getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		SafetyDataCenter.getInstance().setMapAutoInsuranceCreatPolicy(resultMap);
		Intent intent = new Intent(this, CarSafetyInfoConfirm.class);
		startActivityForResult(intent, 4);
	}
}
