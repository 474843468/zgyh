package com.chinamworld.bocmbci.biz.remittance.applicationForRemittance;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cfca.mobile.sip.SipBox;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Inves;
import com.chinamworld.bocmbci.bii.constant.Remittance;
import com.chinamworld.bocmbci.biz.remittance.RemittanceBaseActivity;
import com.chinamworld.bocmbci.biz.remittance.RemittanceDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.GetPhoneInfo;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 跨境汇款确认页，需要上一页通过intent传random，intent字段为Remittance.RANDOM<br>
 * 需要上一页调用跨境汇款预交易接口，接口返回数据放到RemittanceDataCenter.setMapPsnTransInternationalTransferConfirm中
 * @author Zhi
 *
 */
@SuppressWarnings("ResourceType")
public class RemittanceConfirmActivity extends RemittanceBaseActivity {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/** 登录人证件类型 */
	private String identityType;
	/** 随机数 */
	private String random;
	/** 手机验证码sip */
	private SipBox otpSipBxo;
	/** 短信验证码sip*/
	private SipBox smcSipBxo;
	/** 中银E盾 */
	private UsbKeyText usbKeyText;
	/** 标识是否有短息验证码、动态口令 */
	private boolean isOtp = false;
	private boolean isSmc = false;
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.remittance_apply);
		addView(R.layout.remittance_confirm);
		initView();
	}
	
	private void initView() {
		initViewData();
		random = getIntent().getStringExtra(Remittance.RANDOM);
		otpSipBxo = (SipBox) findViewById(R.id.et_cecurity_ps);
		smcSipBxo = (SipBox) findViewById(R.id.sip_sms);
		isOtp = false;
		isSmc = false;
		initSipBox(RemittanceDataCenter.getInstance().getMapPsnTransInternationalTransferConfirm());
		findViewById(R.id.btnConfirm).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				submitRegexp();
			}
		});
	}
	
	/** 填充视图数据 */
	@SuppressWarnings("unchecked")
	private void initViewData() {
		identityType = (String) ((Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA)).get(Comm.IDENTITYTYPE);
		Map<String, Object> map = RemittanceDataCenter.getInstance().getSubmitParams();
		Map<String, Object> userInput = RemittanceDataCenter.getInstance().getUserInput();
		Map<String, Object> shiSuan = RemittanceDataCenter.getInstance().getMapPsnTransGetInternationalTransferCommissionCharge();
		((TextView) findViewById(R.id.tv_swiftAccountNumber)).setText(StringUtil.getForSixForString((String) map.get(Remittance.SWIFTACCOUNTNUMBER)));
		((TextView) findViewById(R.id.tv_remitName)).setText((String) ((Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA)).get(Inves.CUSTOMERNAME));
		((TextView) findViewById(R.id.tv_remittorName)).setText((String) userInput.get(Remittance.REMITTORNAME));
		((TextView) findViewById(R.id.tv_remittorAddress)).setText((String) userInput.get(Remittance.REMITTORADDRESS));
		((TextView) findViewById(R.id.tv_remittersZip)).setText((String) map.get(Remittance.REMITTERSZIP));
		((TextView) findViewById(R.id.tv_payerPhone)).setText((String) userInput.get(Remittance.PAYERPHONE));

//		((TextView) findViewById(R.id.tv_gatheringArea)).setText(RemittanceDataCenter.payeeArea.
//				get(RemittanceDataCenter.payeeAreaCode.indexOf(map.get(Remittance.GATHERINGAREA))));
		List<Map<String, String>> list = RemittanceDataCenter.getInstance().getListPsnQryInternationalTrans4CNYCountry();
		for (Map<String, String> tempMap : list) {
			if (tempMap.get(Remittance.COUNTRYCODE).equals(map.get(Remittance.PAYEEPERMANENTCOUNTRY))) {
				((TextView) findViewById(R.id.tv_payeePermanentCountry)).setText(tempMap.get(Remittance.NAME_CN));
				break;
			}
		}
		if (map.get(Remittance.GATHERINGAREA).equals("JP")) {
			findViewById(R.id.ll_rbPhone).setVisibility(View.VISIBLE);
			findViewById(R.id.ll_payeeBankAdd).setVisibility(View.VISIBLE);
			findViewById(R.id.ll_payeeEnAddress).setVisibility(View.GONE);
			// 日本的时候日本的联系电话是存储在收款人地址字段中的
			((TextView) findViewById(R.id.tv_rbPhone)).setText((String) userInput.get(Remittance.PAYEEENADDRESS));
			((TextView) findViewById(R.id.tv_payeeBankAdd)).setText((String) userInput.get(Remittance.PAYEEBANKADD));
		} else {
			((TextView) findViewById(R.id.tv_payeeEnAddress)).setText((String) userInput.get(Remittance.PAYEEENADDRESS));
		}
		((TextView) findViewById(R.id.tv_payeeEnName)).setText((String) userInput.get(Remittance.PAYEEENNAME));
		((TextView) findViewById(R.id.tv_payeeActno)).setText(StringUtil.getForSixForString((String)userInput.get(Remittance.PAYEEACTNO)));
		((TextView) findViewById(R.id.tv_payeeBankSwift)).setText((String) userInput.get(Remittance.PAYEEBANKSWIFT));
		((TextView) findViewById(R.id.tv_payeeBankName)).setText((String) userInput.get(Remittance.PAYEEBANKFULLNAME));
		if (!StringUtil.isNull((String) userInput.get(Remittance.PAYEEBANKNUM))) {
			findViewById(R.id.ll_payeeBankNum).setVisibility(View.VISIBLE);;
			((TextView) findViewById(R.id.tv_payeeBankNum)).setText((String) userInput.get(Remittance.PAYEEBANKNUM));
		}

			((TextView) findViewById(R.id.tv_gatheringArea)).setText(RemittanceDataCenter.payeeArea.
					get(RemittanceDataCenter.payeeAreaCode.indexOf(map.get(Remittance.GATHERINGAREA))));
			((TextView) findViewById(R.id.tv_remitCurrencyCode)).setText(LocalData.Currency.get(map.get(Remittance.REMITCURRENCYCODE)));
			((TextView) findViewById(R.id.tv_cashRemit)).setText(map.get(Remittance.CASHREMIT).equals("01") ? "现钞" : "现汇");


		if (map.get(Remittance.CASHREMIT).equals("02")) {
			findViewById(R.id.ll_cashRemitExchange).setVisibility(View.GONE);
		} else {
			if(shiSuan.get(Remittance.GETCHARGEFLAG) .equals("0")){
				
			} else {
				((TextView) findViewById(R.id.tv_cashRemitExchange)).setText(StringUtil.parseStringCodePattern((String) map.get(Remittance.PAYCUR),(String) shiSuan.get(Remittance.CASHREMITEXCHANGE),2));
			}
		}
		((TextView) findViewById(R.id.tv_zhuanzhang_num)).setText(StringUtil.parseStringCodePattern((String) map.get(Remittance.REMITCURRENCYCODE), (String) map.get(Remittance.REMITAMOUNT), 2));
		if(shiSuan.get(Remittance.GETCHARGEFLAG).equals("0")){
			
		}else{
			BigDecimal needCommissionCharge = new BigDecimal((String) shiSuan.get(Remittance.NEEDCOMMISSIONCHARGE));
			BigDecimal preCommissionCharge = new BigDecimal((String) shiSuan.get(Remittance.PRECOMMISSIONCHARGE));
			BigDecimal needPostage = new BigDecimal((String) shiSuan.get(Remittance.NEEDPOSTAGE));
			BigDecimal prePostage = new BigDecimal((String) shiSuan.get(Remittance.PREPOSTAGE));
			BigDecimal benchmarkCost = needCommissionCharge.add(needPostage);
			BigDecimal favourable = preCommissionCharge.add(prePostage);
			((TextView) findViewById(R.id.tv_benchmarkCost)).setText(StringUtil.parseStringCodePattern((String) map.get(Remittance.PAYCUR), benchmarkCost.toString(), 2));
			((TextView) findViewById(R.id.tv_favourable)).setText(StringUtil.parseStringCodePattern((String) map.get(Remittance.PAYCUR), favourable.toString(), 2));
		}
		
		((TextView) findViewById(R.id.tv_payBiZhong)).setText(LocalData.Currency.get(map.get(Remittance.PAYCUR)));
		((TextView) findViewById(R.id.tv_chengDanFangShi)).setText("SHA 共同承担");
//		((TextView) findViewById(R.id.tv_toPayeeMessage)).setText(StringUtil.valueOf1((String) userInput.get(Remittance.REMITFURINFO2PAYEE)));
		((TextView) findViewById(R.id.tv_toPayeeMessage)).setText((String)userInput.get(Remittance.REMITFURINFO2PAYEE));
		if(RemittanceDataCenter.FResident.contains(identityType)
				|| identityType.equals("11")) {
			((LinearLayout) findViewById(R.id.ll_payeeUse)).setVisibility(View.GONE);
		} else {
			((TextView) findViewById(R.id.tv_payeeUse))
			.setText(RemittanceDataCenter.listUseCN
					.get(RemittanceDataCenter.listUseCode.indexOf(map
							.get(Remittance.REMITTANCEINFO))));
		}
		
		if(RemittanceDataCenter.FResident.contains(identityType)
				|| identityType.equals("11")){
			((LinearLayout) findViewById(R.id.ll_payeeUseFull)).setVisibility(View.GONE);
		} else {
			((TextView) findViewById(R.id.tv_payeeUseFull)).setText((String) userInput.get(Remittance.REMITTANCEDESCRIPTION));
		}
		
		PopupWindowUtils.getInstance().setOnShowAllTextListenerByViewGroup(this, (LinearLayout) findViewById(R.id.ll));
		
		// TODO 日本时收款银行地址没有
	}
	
	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox(Map<String, Object> result){
		usbKeyText = (UsbKeyText) findViewById(R.id.sip_usbkey);
		String mmconversationId = (String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		usbKeyText.Init(mmconversationId, random, result, this);
		isOtp = usbKeyText.getIsOtp();
		isSmc = usbKeyText.getIsSmc();
		initOtpSipBox();
		initSmcSipBox();
	}
	
	/** 初始化otp */
	private void initOtpSipBox() {
		if (isOtp) {
			findViewById(R.id.layout_sip).setVisibility(View.VISIBLE);
			otpSipBxo.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
			otpSipBxo.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
			otpSipBxo.setId(10002);
			otpSipBxo.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
			otpSipBxo.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
			otpSipBxo.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
			otpSipBxo.setSipDelegator(this);
			otpSipBxo.setRandomKey_S(random);
		}
	}

	/** 初始化smc */
	private void initSmcSipBox() {
		if (isSmc) {
			findViewById(R.id.layout_sms).setVisibility(View.VISIBLE);
			Button btnSendSms = (Button) findViewById(R.id.smsbtn);
			SmsCodeUtils.getInstance().addSmsCodeListner(btnSendSms, smcOnclickListener);
			smcSipBxo.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
			smcSipBxo.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
			smcSipBxo.setId(10002);
			smcSipBxo.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
			smcSipBxo.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
			smcSipBxo.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
			smcSipBxo.setSipDelegator(this);
			smcSipBxo.setRandomKey_S(random);
		}
	}

	/** 安全控件提交校验 */
	private void submitRegexp() {
		usbKeyText.checkDataUsbKey(otpSipBxo, smcSipBxo, new IUsbKeyTextSuccess() {
			
			@Override
			public void SuccessCallBack(String result, int errorCode) {
				// 获取token请求
				BaseHttpEngine.showProgressDialog();
				requestPSNGetTokenId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
			}
		});
	}
	
	private OnClickListener smcOnclickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			sendSMSCToMobile();
		}
	};
	
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		Map<String, Object> params = RemittanceDataCenter.getInstance().getSubmitParams();
		params.put(ConstantGloble.PUBLIC_TOKEN, BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.TOKEN_ID));
		GetPhoneInfo.initActFirst(this);
		GetPhoneInfo.addPhoneInfo(params);
		/**安全工具参数获取*/
		usbKeyText.InitUsbKeyResult(params);
		SipBoxUtils.setSipBoxParams(params);
		getHttpTools().requestHttp(Remittance.PSNTRANSINTERNATIONALTRANSFERSUBMIT, "requestPsnTransInternationalTransferSubmitCallBack", params, true);
	}
	
	/** 汇款提交返回 */
	@SuppressWarnings("unchecked")
	public void requestPsnTransInternationalTransferSubmitCallBack(Object resultObj) {
		Map<String, Object> resultMap = (Map<String, Object>) getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		RemittanceDataCenter.getInstance().setMapPsnTransInternationalTransferSubmit(resultMap);
		startActivityForResult(new Intent(this, RemittanceSuccessActivity.class), QUIT_CODE);
	}
}
