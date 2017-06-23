package com.chinamworld.bocmbci.biz.crcd.mycrcd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 消费分期确认界面
 * 
 * @author huangyuchao
 * 
 */
public class CrcdTransDividedConfirmActivity extends CrcdBaseActivity {
	private static final String TAG = "CrcdTransDividedConfirmActivity";
	private View view;

	private TextView tv_card_type, tv_card_number, tv_card_step;

	private TextView mycrcd_accounted_type, mycrcd_selected_creditcard, mycrcd_accounted_money, rate_fix_sellCode,
			rate_currency_buyCode, rate_currency_type, rate_fix_papRate, rate_fix_comRate;

	private Button lastButton, sureButton;

	EditText et_pass;
	/** 消费账单查询结果 */
	protected static List<Map<String, Object>> transList;
	protected static String money;
	protected static String instmtCharge;
	/** 首期应还 */
	protected static String firstAmount;
	/** 每期应还 */
	protected static String restPerTimeInAmount;

	/** 安全因子数组 */
	public List<Map<String, Object>> factorList;
	/** 消费分期确认结果 */
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

	Map securitymap;
	/** 加密控件——动态口令 */
	private SipBox sipBoxActiveCode = null;
	/** 加密控件——手机交易码 */
	private SipBox sipBoxSmc = null;
	/**中银E盾**/
	private UsbKeyText usbKeyText;
	/** 是否上传动态口令 */
	private boolean isOtp = false;
	/** 是否上传短信验证码 */
	private boolean isSmc = false;
	private TextView moneyText = null;
	private TextView firstText = null;
	private TextView everyText = null;
	private String commConversationId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_sub_divide));
		// 右上角按钮赋值
		if (view == null) {
			view = addView(R.layout.crcd_trans_divide_confirm);
		}
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
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

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

	public void init() {

		factorMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CRCE_XIAOFEI_CONFIRM_RESULT);

		securitymap = (Map) factorMap.get(Crcd.SECURITYMAP);
		commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));

		if (!StringUtil.isNullOrEmpty(securitymap)) {
			factorList = (List<Map<String, Object>>) securitymap.get(Crcd.FACTORLIST);
		}

//		initOptAndSmc()
		/** 跟据返回初始化中银E盾加密控件 */
		initSipBox();

		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getResources().getString(R.string.mycrcd_divide_setup),
						this.getResources().getString(R.string.mycrcd_divide_confirm),
						this.getResources().getString(R.string.mycrcd_divide_setup_success) });
		StepTitleUtils.getInstance().setTitleStep(2);

		tv_card_type = (TextView) view.findViewById(R.id.tv_card_type);
		tv_card_number = (TextView) view.findViewById(R.id.tv_card_number);
		tv_card_step = (TextView) view.findViewById(R.id.tv_card_step);
		rate_fix_sellCode = (TextView) view.findViewById(R.id.rate_fix_sellCode);
		rate_currency_buyCode = (TextView) view.findViewById(R.id.rate_currency_buyCode);
		rate_currency_type = (TextView) view.findViewById(R.id.rate_currency_type);
		rate_fix_papRate = (TextView) view.findViewById(R.id.rate_fix_papRate);
		rate_fix_comRate = (TextView) view.findViewById(R.id.rate_fix_comRate);

		moneyText = (TextView) findViewById(R.id.money_text);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, moneyText);
		firstText = (TextView) findViewById(R.id.first);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, firstText);
		everyText = (TextView) findViewById(R.id.every);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, everyText);

		tv_card_type.setText(String.valueOf(currentBankList.get(Crcd.CRCD_ACCOUNTNAME_RES)));
		tv_card_number.setText(StringUtil.getForSixForString(String.valueOf(currentBankList
				.get(Crcd.CRCD_ACCOUNTNUMBER_RES))));
		tv_card_step.setText(String.valueOf(currentBankList.get(Crcd.CRCD_NICKNAME_RES)));

		mycrcd_accounted_type = (TextView) view.findViewById(R.id.mycrcd_accounted_type);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mycrcd_accounted_type);
		mycrcd_selected_creditcard = (TextView) view.findViewById(R.id.mycrcd_selected_creditcard);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mycrcd_selected_creditcard);
		mycrcd_accounted_money = (TextView) view.findViewById(R.id.mycrcd_accounted_money);

		transList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CRCE_XIAOFEI_RESULT);

		mycrcd_accounted_type.setText(String.valueOf(transList.get(CrcdTransDividedActivity.position).get(
				Crcd.CRCD_TRANSDESC)));
		mycrcd_selected_creditcard.setText(CrcdTransDividedActivity.strCurrencyCode);
		money = String.valueOf(transList.get(CrcdTransDividedActivity.position).get(Crcd.CRCD_CLEARINGAMOUNT));
		mycrcd_accounted_money.setText(StringUtil.parseStringPattern(money, 2));

		rate_fix_sellCode.setText(CrcdTransDividedActivity.dividedNum);
		instmtCharge = (String) factorMap.get(Crcd.CRCD_INSTMTCHARGE);

		rate_currency_buyCode.setText(StringUtil.parseStringPattern(instmtCharge, 2));
		rate_currency_type.setText(CrcdTransDividedActivity.strDividedType);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, rate_currency_type);
		firstAmount = (String) factorMap.get(Crcd.CRCD_FIRSTINAMOUNT);

		restPerTimeInAmount = (String) factorMap.get(Crcd.CRCD_RESETPERTIEINAMOUNT);

		rate_fix_papRate.setText(StringUtil.parseStringPattern(firstAmount, 2));
		rate_fix_comRate.setText(StringUtil.parseStringPattern(restPerTimeInAmount, 2));

		lastButton = (Button) view.findViewById(R.id.lastButton);
		sureButton = (Button) view.findViewById(R.id.sureButton);

		lastButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 动态口令
//				RegexpBean sipSmcpBean = new RegexpBean(getString(R.string.acc_smc_regex), sipBoxSmc.getText()
//						.toString(), ConstantGloble.SIPSMCPSW);
//				RegexpBean sipRegexpBean = new RegexpBean(getString(R.string.active_code_regex), sipBoxActiveCode
//						.getText().toString(), ConstantGloble.SIPOTPPSW);
//
//				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
//				if (isSmc) {
//					lists.add(sipSmcpBean);
//				}
//				if (isOtp) {
//					lists.add(sipRegexpBean);
//				}
//
//				if (RegexpUtils.regexpDate(lists)) {// 校验通过
//					if (isSmc) {
//						try {
//							smcStr = sipBoxSmc.getValue().getEncryptPassword();
//							smc_password_RC = sipBoxSmc.getValue().getEncryptRandomNum();
//						} catch (CodeException e) {
//							LogGloble.exceptionPrint(e);
//						}
//
//					}
//					if (isOtp) {
//						try {
//							otpStr = sipBoxActiveCode.getValue().getEncryptPassword();
//							otp_password_RC = sipBoxActiveCode.getValue().getEncryptRandomNum();
//						} catch (CodeException e) {
//							LogGloble.exceptionPrint(e);
//						}
//
//					}
//
//					pSNGetTokenId();
//				}
				/**安全工具数据校验*/
				usbKeyText.checkDataUsbKey(sipBoxActiveCode, sipBoxSmc, new IUsbKeyTextSuccess() {
					
					@Override
					public void SuccessCallBack(String result, int errorCode) {
						// TODO Auto-generated method stub
						BaseHttpEngine.showProgressDialog();
						pSNGetTokenId();
					}
				});
				
			}
		});
	}
	
	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox(){
		usbKeyText = (UsbKeyText)view.findViewById(R.id.sip_usbkey);
		usbKeyText.Init(commConversationId, randomNumber, factorMap, this);
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
//		sipBoxActiveCode.setCipherType(SystemConfig.CIPHERTYPE);
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
//		sipBoxActiveCode.setRandomKey_S(randomNumber);
		// ll_activecode_sip.addView(sipBoxActiveCode);
		}
		if(isSmc){
		// 手机交易码
		ll_smc = (LinearLayout) view.findViewById(R.id.ll_smc);
		ll_smc.setVisibility(View.VISIBLE);
		sipBoxSmc = (SipBox) view.findViewById(R.id.sipbox_smc);
//		sipBoxSmc.setCipherType(SystemConfig.CIPHERTYPE);
		// sipBoxSmc.setLayoutParams(param);
		// sipBoxSmc.setGravity(Gravity.CENTER_VERTICAL);
		sipBoxSmc.setTextColor(getResources().getColor(android.R.color.black));
		// add by 2016年9月12日 luqp 修改
		sipBoxSmc.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
		SipBoxUtils.initSipBoxWithTwoType(sipBoxSmc, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
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

	/**
	 * 获取tokenId
	 */
	public void pSNGetTokenId() {
		BiiHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
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
		// 办理消费分期处理
		psnCrcdDividedPayConsumeResult(tokenId);
	}

	public void psnCrcdDividedPayConsumeResult(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDDIVIDEDPAYCONSUME_API);
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_ACCOUNTID_RES, String.valueOf(currentBankList.get(Crcd.CRCD_ACCOUNTID_RES)));
		params.put(Crcd.CRCD_CURRENCYCODE, CrcdTransDividedActivity.currency);
		params.put(Crcd.CRCD_AMOUNT, money);

		// 消费分期要传数字类型
		params.put(Crcd.CRCD_DIVPERIOD, Integer.valueOf(CrcdTransDividedActivity.dividedNum));
		params.put(Crcd.CRCD_CHARGEMODE, CrcdTransDividedActivity.dividedType);

		params.put(Crcd.CRCD_INSTMTCHARGE, instmtCharge);
		params.put(Crcd.CRCD_FIRSTINAMOUNT, firstAmount);
		params.put(Crcd.CRCD_RESETPERTIEINAMOUNT, restPerTimeInAmount);

		params.put(Crcd.CRCD_CRCDFINALFOUR, CrcdTransDividedActivity.acctNumTail);

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

		HttpManager.requestBii(biiRequestBody, this, "psnCrcdDividedPayConsumeResultCallBack");
	}

	public void psnCrcdDividedPayConsumeResultCallBack(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		Intent it = new Intent(CrcdTransDividedConfirmActivity.this, CrcdTransDividedSuccessActivity.class);
		startActivity(it);
	}

}
