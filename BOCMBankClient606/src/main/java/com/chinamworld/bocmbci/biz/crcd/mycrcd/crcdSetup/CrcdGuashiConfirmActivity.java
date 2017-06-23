package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

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
import cfca.mobile.sip.SipBox;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 
 * 信用卡挂失确认
 * 
 * @author huangyuchao
 * 
 */
public class CrcdGuashiConfirmActivity extends CrcdBaseActivity {

	/** 信用卡挂失 */
	private View view;

	Button nextBtn;

	TextView tv_cardNumber, tv_cardtype, tv_cardnickname;
	private String accountNumber = null;
	private String accountId = null;
	private String nickName = null;
	private String strAccountType = null;

	/** 挂失手续费试算 */
	private String getChargeFlag;
	/* 挂失*/
	private LinearLayout ll_lossfee;
	private String lossFeetv;
	private String lossFeeCurrency;
	private TextView crcd_guashi_lossFee;
	private TextView crcd_guashi_lossfeecurrency;
	/* 补卡*/	
	private LinearLayout ll_reportfee;
	private String reportFeetv;
	private String reportFeeCurrency;
	private TextView crcd_guashi_reportFee;
	private TextView crcd_guashi_portfeecurrency;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_creditcard_guashi_title));
		if (view == null) {
			view = addView(R.layout.crcd_guashi_info_confirm);
		}
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		nickName = getIntent().getStringExtra(Crcd.CRCD_NICKNAME_RES);
		strAccountType = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTTYPE_RES);
		requestForRandomNumber();
//		init();
		BaseHttpEngine.showProgressDialog();
//		requestForRandomNumber();
	}

	/**
	 * 请求密码控件随机数
	 */
	public void requestForRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "queryRandomNumberCallBack");
	}

	/** 加密控件里的随机数 */
	protected String randomNumber;

	/**
	 * 请求密码控件随机数 回调
	 * 
	 * @param resultObj
	 */
	public void queryRandomNumberCallBack(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		randomNumber = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(randomNumber)) {
			return;
		}
		// 加密控件设置随机数
//		sipBoxActiveCode.setRandomKey_S(randomNumber);
//		sipBoxSmc.setRandomKey_S(randomNumber);
		 init();
	}

	static String strGuashiType;
	static int guaShiType;

	/** 安全因子数组 */
	public List<Map<String, Object>> factorList;
	Map<String, Object> factorMap;

	/** 动态口令布局 */
	private LinearLayout ll_active_code;
	/** 手机交易码布局 */
	private LinearLayout ll_smc;

	/** 动态口令 */
	private String otpStr;
	/** 手机交易码 */
	private String smcStr;
	/** 加密之后加密随机数 */
	private String otp_password_RC = "";
	private String smc_password_RC = "";

	static String mailAddress;
	static String mailAddressType;

	LinearLayout cardsendtypeLayout, cardsendaddressLayout;
	TextView tv_cardsendtype, tv_cardsendaddress;
	private TextView titleText = null;

	public void init() {

		factorMap = CrcdGuashiInfoActivity.returnMap;
		factorList = (List<Map<String, Object>>) factorMap.get(Crcd.FACTORLIST);
		commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));

		mailAddress = CrcdGuashiInfoActivity.mailAddress;
		mailAddressType = CrcdGuashiInfoActivity.mailAddressType;

		guaShiType = CrcdGuashiInfoActivity.guaShiType;

		cardsendtypeLayout = (LinearLayout) findViewById(R.id.cardsendtypeLayout);
		cardsendaddressLayout = (LinearLayout) findViewById(R.id.cardsendaddressLayout);

		tv_cardsendtype = (TextView) findViewById(R.id.tv_cardsendtype);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_cardsendtype);

		tv_cardsendaddress = (TextView) findViewById(R.id.tv_cardsendaddress);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_cardsendaddress);
		tv_cardsendtype.setText(mailAddressType);
		tv_cardsendaddress.setText(mailAddress);
		titleText = (TextView) findViewById(R.id.top_title);		
		ll_lossfee = (LinearLayout) findViewById(R.id.ll_lossfee);	
		crcd_guashi_lossFee = (TextView) findViewById(R.id.crcd_guashi_lossfee);
		crcd_guashi_lossfeecurrency= (TextView) findViewById(R.id.crcd_guashi_lossfeecurrency);
		ll_reportfee = (LinearLayout) findViewById(R.id.ll_reportfee);	
		crcd_guashi_reportFee = (TextView) findViewById(R.id.crcd_guashi_portfee);
		crcd_guashi_portfeecurrency= (TextView) findViewById(R.id.crcd_guashi_portfeecurrency);

		/** 手续费试算 */
		Map<String, Object> chargeMissionMap = TranDataCenter.getInstance()
				.getCommissionChargeMap();

		if (!StringUtil.isNullOrEmpty(chargeMissionMap)) {
			getChargeFlag =(String)chargeMissionMap.get(Crcd.CRCD_GETCHARGEFLAG);
			 lossFeetv = (String) chargeMissionMap.get(Crcd.CRCD_LOSSFEE);
			 lossFeeCurrency = (String) chargeMissionMap
					.get(Crcd.CRCD_LOSSFEECURRENCY);
			if (!StringUtil.isNullOrEmpty(lossFeetv)) {
				lossFeetv = StringUtil.parseStringCodePattern(lossFeeCurrency,
						lossFeetv, 2);
			}
		     reportFeetv = (String) chargeMissionMap
					.get(Crcd.CRCD_REPORTFEE);
			 reportFeeCurrency = (String) chargeMissionMap
					.get(Crcd.CRCD_REPORTFEECURRENCY);			
			if (!StringUtil.isNullOrEmpty(reportFeetv)) {
				reportFeetv = StringUtil.parseStringCodePattern(reportFeeCurrency,
						reportFeetv, 2);
			}
			
		
		}
		crcd_guashi_lossFee.setText(lossFeetv);
		if(StringUtil.isNullOrEmpty(lossFeeCurrency)){
			crcd_guashi_lossfeecurrency.setText("-");	
		}else{
			crcd_guashi_lossfeecurrency.setText(LocalData.Currency.get(lossFeeCurrency));
		}
		
		crcd_guashi_reportFee.setText(reportFeetv);
		if(StringUtil.isNullOrEmpty(lossFeeCurrency)){
			crcd_guashi_portfeecurrency.setText("-");	
		}else{
			crcd_guashi_portfeecurrency.setText(LocalData.Currency.get(reportFeeCurrency));
		}		
		
		String text1 = getResources().getString(R.string.crcd_setup_guashi);
		String text2 = getResources().getString(R.string.crcd_setup_buka);
		// 0=挂失1=挂失及补卡
		if (0 == guaShiType) {
			cardsendtypeLayout.setVisibility(View.GONE);
			cardsendaddressLayout.setVisibility(View.GONE);
			ll_lossfee.setVisibility(View.VISIBLE);
			ll_reportfee.setVisibility(View.GONE);	
			titleText.setText(text1);
			
		
		} else if (1 == guaShiType) {
			cardsendtypeLayout.setVisibility(View.VISIBLE);
			cardsendaddressLayout.setVisibility(View.VISIBLE);
			ll_lossfee.setVisibility(View.VISIBLE);
			ll_reportfee.setVisibility(View.VISIBLE);
			titleText.setText(text2);	

		}

//		initOptAndSmc()
		/** 跟据返回初始化中银E盾加密控件 */
		initSipBox();
		tv_cardNumber = (TextView) findViewById(R.id.tv_cardNumber);
		tv_cardNumber.setText(StringUtil.getForSixForString(accountNumber));

		tv_cardtype = (TextView) findViewById(R.id.tv_cardtype);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_cardtype);
		tv_cardtype.setText(strAccountType);

		tv_cardnickname = (TextView) findViewById(R.id.tv_cardnickname);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_cardnickname);

		tv_cardnickname.setText(nickName);

		nextBtn = (Button) findViewById(R.id.nextButton);
		nextBtn.setOnClickListener(nextClick);
	}

	/** 加密控件——动态口令 */
	private SipBox sipBoxActiveCode = null;
	/** 加密控件——手机交易码 */
	private SipBox sipBoxSmc = null;
	/**中银E盾*/
	private UsbKeyText usbKeyText;
	/** 是否上传动态口令 */
	private boolean isOtp = false;
	/** 是否上传短信验证码 */
	private boolean isSmc = false;
	
	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox(){
		String mmconversationId = (String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		usbKeyText = (UsbKeyText)view.findViewById(R.id.sip_usbkey);
		usbKeyText.Init(mmconversationId, randomNumber, factorMap, this);
		isOtp = usbKeyText.getIsOtp();
		isSmc = usbKeyText.getIsSmc();
		initOptAndSmc();
	}

	public void initOptAndSmc() {
		if(isOtp){
		// 动态口令
		ll_active_code = (LinearLayout) view.findViewById(R.id.ll_active_code);
		ll_active_code.setVisibility(View.VISIBLE);
		sipBoxActiveCode = (SipBox) view.findViewById(R.id.sipbox_active);
		sipBoxActiveCode.setCipherType(SystemConfig.CIPHERTYPE);
		// sipBoxActiveCode.setLayoutParams(param);
		sipBoxActiveCode.setTextColor(getResources().getColor(android.R.color.black));
		// sipBoxActiveCode.setGravity(Gravity.CENTER_VERTICAL);
		// add by 2016年9月12日 luqp 修改
		sipBoxActiveCode.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
		SipBoxUtils.initSipBoxWithTwoType(sipBoxActiveCode, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//		sipBoxActiveCode.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		sipBoxActiveCode.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		sipBoxActiveCode.setId(10002);
//		sipBoxActiveCode.setBackgroundResource(R.drawable.bg_for_edittext);
//		sipBoxActiveCode.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		sipBoxActiveCode.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//		sipBoxActiveCode.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
//		sipBoxActiveCode.setSingleLine(true);
//		sipBoxActiveCode.setSipDelegator(this);
//		sipBoxActiveCode.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		/**设置随机数*/
//		sipBoxActiveCode.setRandomKey_S(randomNumber);
		// ll_activecode_sip.addView(sipBoxActiveCode);
		}
		if(isSmc){
		// 手机交易码
		ll_smc = (LinearLayout) view.findViewById(R.id.ll_smc);
		ll_smc.setVisibility(View.VISIBLE);
		sipBoxSmc = (SipBox) view.findViewById(R.id.sipbox_smc);
		// add by 2016年9月12日 luqp 修改
		sipBoxSmc.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
		sipBoxSmc.setTextColor(getResources().getColor(android.R.color.black));
		SipBoxUtils.initSipBoxWithTwoType(sipBoxSmc, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//		sipBoxSmc.setCipherType(SystemConfig.CIPHERTYPE);
		// sipBoxSmc.setLayoutParams(param);
		// sipBoxSmc.setGravity(Gravity.CENTER_VERTICAL);
//		sipBoxSmc.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		sipBoxSmc.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		sipBoxSmc.setId(10002);
//		sipBoxSmc.setBackgroundResource(R.drawable.bg_for_edittext);
//		sipBoxSmc.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		sipBoxSmc.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//		sipBoxSmc.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
//		sipBoxSmc.setSingleLine(true);
//		sipBoxSmc.setSipDelegator(this);
//		sipBoxSmc.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		/**设置随机数*/
//		sipBoxSmc.setRandomKey_S(randomNumber);
		// ll_smc_sip.addView(sipBoxSmc);

		Button smsBtn = (Button) view.findViewById(R.id.smsbtn);
		SmsCodeUtils.getInstance().addSmsCodeListner(smsBtn, new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 发送验证码到手机
				sendMSCToMobile();
			}
		});
		}
//		showOptOrSmc();
	}

//	public void showOptOrSmc() {
//		/** 判断是动态口令还是手机交易码 */
//		if (!StringUtil.isNullOrEmpty(factorList)) {
//			for (int i = 0; i < factorList.size(); i++) {
//				Map<String, Object> itemMap = factorList.get(i);
//				Map<String, Object> securityMap = (Map<String, Object>) itemMap.get(Crcd.FIELD);
//				String name = String.valueOf(securityMap.get(Crcd.NAME));
//				if (Comm.Otp.equals(name)) {
//					isOtp = true;
//					ll_active_code.setVisibility(View.VISIBLE);
//				} else if (Comm.Smc.equals(name)) {
//					isSmc = true;
//					ll_smc.setVisibility(View.VISIBLE);
//				}
//			}
//		}
//	}

	OnClickListener nextClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// 动态口令
//			RegexpBean sipSmcpBean = new RegexpBean(getString(R.string.acc_smc_regex), sipBoxSmc.getText().toString(),
//					ConstantGloble.SIPSMCPSW);
//			RegexpBean sipRegexpBean = new RegexpBean(getString(R.string.active_code_regex), sipBoxActiveCode.getText()
//					.toString(), ConstantGloble.SIPOTPPSW);
//
//			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
//			if (isSmc) {
//				lists.add(sipSmcpBean);
//			}
//			if (isOtp) {
//				lists.add(sipRegexpBean);
//			}
//
//			if (RegexpUtils.regexpDate(lists)) {// 校验通过
//				if (isSmc) {
//					try {
//						smcStr = sipBoxSmc.getValue().getEncryptPassword();
//						smc_password_RC = sipBoxSmc.getValue().getEncryptRandomNum();
//					} catch (CodeException e) {
//						LogGloble.exceptionPrint(e);
//					}
//
//				}
//				if (isOtp) {
//					try {
//						otpStr = sipBoxActiveCode.getValue().getEncryptPassword();
//						otp_password_RC = sipBoxActiveCode.getValue().getEncryptRandomNum();
//					} catch (CodeException e) {
//						LogGloble.exceptionPrint(e);
//					}
//
//				}
//
//				pSNGetTokenId();
//			}
			usbKeyText.checkDataUsbKey(sipBoxActiveCode, sipBoxSmc, new IUsbKeyTextSuccess() {
				
				@Override
				public void SuccessCallBack(String result, int errorCode) {
					// TODO Auto-generated method stub
					pSNGetTokenId();
				}
			});
			
		}
	};

	/**
	 * 获取tokenId
	 */
	public void pSNGetTokenId() {
		BiiHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		HttpManager.requestBii(biiRequestBody, this, "aquirePSNGetTokenId");
	}

	/**
	 * 处理获取tokenId的数据得到tokenId
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void aquirePSNGetTokenId(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(tokenId)) {
			return;
		}

		// 信用卡挂失及补卡结果
		psnCrcdReportLossResult();
	}

	public void psnCrcdReportLossResult() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDREPORTLOSSRESULT);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_ACCOUNTID_RES,accountId);
		params.put(Crcd.CRCD_SELECTLOSSTYPE, String.valueOf(CrcdGuashiInfoActivity.guaShiType));
		params.put(Crcd.CRCD_MAILADDRESS, mailAddress);
		params.put(Crcd.CRCD_MAILADDRESSTYPE, mailAddressType);
		params.put(Crcd.CRCD_TOKEN, tokenId);
//		if (isOtp) {
//			params.put(Comm.Otp, otpStr);
//			params.put(Comm.Otp_Rc, otp_password_RC);
//		}
//		if (isSmc) {
//			params.put(Comm.Smc, smcStr);
//			params.put(Comm.Smc_Rc, smc_password_RC);
//		}
		/**安全工具参数获取*/
		usbKeyText.InitUsbKeyResult(params);
		SipBoxUtils.setSipBoxParams(params);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdReportLossResultCallBack");
	}

	public static Map<String, Object> returnMap;

	public void psnCrcdReportLossResultCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
		returnMap = (Map<String, Object>) body.getResult();
		BaseHttpEngine.dissMissProgressDialog();
		Intent it = new Intent(CrcdGuashiConfirmActivity.this, CrcdGuashiSuccessActivity.class);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		it.putExtra(Crcd.CRCD_NICKNAME_RES, nickName);
		it.putExtra(Crcd.CRCD_ACCOUNTTYPE_RES, strAccountType);
		startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
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
