package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

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
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 开通或修改账单确认
 * 
 * @author huangyuchao
 * 
 */
public class CrcdPsnCheckOpenConfirmActivity extends CrcdBaseActivity {
	private View view;

	String strBillSetup;
	int billSetupId;

	String strAddtrss;

	String isEdit;

	protected String prefix;
	TextView finc_accNumber;
	TextView finc_accId;

	EditText et_password;

	static String passType;

	Button lastButton, sureButton;

	LinearLayout ll_paper, ll_email, ll_phone;
	TextView tv_paper_confirm, tv_address_confirm, tv_phone_confirm;

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

	TextView tv_acc_loss_actnum;
	private String accountNumber = null;
	private String accountId = null;
	private String commConversationId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// strBillSetup = CrcdPsnQueryCheckDetail.strbillSetupId;
		billSetupId = this.getIntent().getIntExtra(
				ConstantGloble.CRCD_BILLSETUPID, -1);
		if (billSetupId == 0) {

			strBillSetup = this.getString(R.string.mycrcd_paper_billdan);
		}
		if (billSetupId == 1) {

			strBillSetup = this.getString(R.string.mycrcd_email_billdan);
		}
		if (billSetupId == 2) {

			strBillSetup = this.getString(R.string.mycrcd_phone_billdan);
		}
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		strAddtrss = getIntent().getStringExtra(Crcd.CRCD_PAPERADDRESS);
		isEdit = this.getIntent().getStringExtra(
				ConstantGloble.CRCD_ISOPENOREDIT);
	

		if ("open".equals(isEdit)) {
			prefix = this.getString(R.string.mycrcd_open);
		} else if ("edit".equals(isEdit)) {
			prefix = this.getString(R.string.edit);
		}

		// 为界面标题赋值
		setTitle(prefix + strBillSetup);
		view = addView(R.layout.crcd_psn_check_openconfirm);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		BaseHttpEngine.showProgressDialog();
		requestForRandomNumber();
		// init();
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
		// sipBoxActiveCode.setRandomKey_S(randomNumber);
		// sipBoxSmc.setRandomKey_S(randomNumber);
		init();
	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

	/** 初始化界面 */
	private void init() {

		factorMap = CrcdPsnCheckOpenActivity.returnMap;
		factorList = (List<Map<String, Object>>) factorMap.get(Crcd.FACTORLIST);
		commConversationId = String.valueOf(BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));

		// initOptAndSmc()
		/** 跟据返回初始化中银E盾加密控件 */
		initSipBox();

		tv_acc_loss_actnum = (TextView) findViewById(R.id.tv_acc_loss_actnum);

		if ("open".equals(isEdit)) {
			tv_acc_loss_actnum
					.setText(getString(R.string.mycrcd_open_zhangdan_confirm));
		} else if ("edit".equals(isEdit)) {
			tv_acc_loss_actnum
					.setText(getString(R.string.mycrcd_edit_zhangdan_confirm));
		}

		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getResources().getString(
								R.string.mycrcd_write_info_message),
						this.getResources().getString(
								R.string.mycrcd_setup_info),
						this.getResources().getString(
								R.string.mycrcd_service_setup_success) });
		StepTitleUtils.getInstance().setTitleStep(2);

		finc_accNumber = (TextView) view.findViewById(R.id.finc_accNumber);
		finc_accId = (TextView) view.findViewById(R.id.finc_accId);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				finc_accId);
		tv_address_confirm = (TextView) view
				.findViewById(R.id.tv_address_confirm);

		et_password = (EditText) view.findViewById(R.id.et_password);

		finc_accNumber.setText(StringUtil.getForSixForString(accountNumber));
		finc_accId.setText(strBillSetup);

		tv_address_confirm.setText(strAddtrss);

		ll_paper = (LinearLayout) view.findViewById(R.id.ll_paper);
		ll_email = (LinearLayout) view.findViewById(R.id.ll_email);
		ll_phone = (LinearLayout) view.findViewById(R.id.ll_phone);

		tv_paper_confirm = (TextView) view.findViewById(R.id.tv_paper_confirm);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_paper_confirm);
		tv_address_confirm = (TextView) view
				.findViewById(R.id.tv_address_confirm);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_address_confirm);
		tv_phone_confirm = (TextView) view.findViewById(R.id.tv_phone_confirm);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_phone_confirm);
		lastButton = (Button) view.findViewById(R.id.lastButton);
		lastButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		sureButton = (Button) view.findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 动态口令
				// RegexpBean sipSmcpBean = new
				// RegexpBean(getString(R.string.acc_smc_regex),
				// sipBoxSmc.getText()
				// .toString(), ConstantGloble.SIPSMCPSW);
				// RegexpBean sipRegexpBean = new
				// RegexpBean(getString(R.string.active_code_regex),
				// sipBoxActiveCode
				// .getText().toString(), ConstantGloble.SIPOTPPSW);
				//
				// ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				// if (isSmc) {
				// lists.add(sipSmcpBean);
				// }
				// if (isOtp) {
				// lists.add(sipRegexpBean);
				// }
				//
				// if (RegexpUtils.regexpDate(lists)) {// 校验通过
				// if (isSmc) {
				// try {
				// smcStr = sipBoxSmc.getValue().getEncryptPassword();
				// smc_password_RC = sipBoxSmc.getValue().getEncryptRandomNum();
				// } catch (CodeException e) {
				// LogGloble.exceptionPrint(e);
				// }
				//
				// }
				// if (isOtp) {
				// try {
				// otpStr = sipBoxActiveCode.getValue().getEncryptPassword();
				// otp_password_RC =
				// sipBoxActiveCode.getValue().getEncryptRandomNum();
				// } catch (CodeException e) {
				// LogGloble.exceptionPrint(e);
				// }
				//
				// }
				//
				// pSNGetTokenId();
				// }
				/** 安全工具密码校验 */
				checketDate();
			}
		});

		if (billSetupId == 0) {
			ll_paper.setVisibility(View.VISIBLE);
			ll_email.setVisibility(View.GONE);
			ll_phone.setVisibility(View.GONE);

			tv_paper_confirm.setText(strAddtrss);
		} else if (billSetupId == 1) {
			ll_paper.setVisibility(View.GONE);
			ll_email.setVisibility(View.VISIBLE);
			ll_phone.setVisibility(View.GONE);

			tv_address_confirm.setText(CrcdPsnCheckOpenActivity.strEmail);
		} else if (billSetupId == 2) {
			ll_paper.setVisibility(View.GONE);
			ll_email.setVisibility(View.GONE);
			ll_phone.setVisibility(View.VISIBLE);

			tv_phone_confirm.setText(CrcdPsnCheckOpenActivity.phoneNum);
		}

	}

	/** 安全工具密码校验 */
	private void checketDate() {
		usbKeyText.checkDataUsbKey(sipBoxActiveCode, sipBoxSmc,
				new IUsbKeyTextSuccess() {

					@Override
					public void SuccessCallBack(String result, int errorCode) {
						// TODO Auto-generated method stub
						pSNGetTokenId();
					}
				});
	}

	/** 加密控件——动态口令 */
	private SipBox sipBoxActiveCode = null;
	/** 加密控件——手机交易码 */
	private SipBox sipBoxSmc = null;
	/** 中银E盾 */
	private UsbKeyText usbKeyText;
	/** 是否上传动态口令 */
	private boolean isOtp = false;
	/** 是否上传短信验证码 */
	private boolean isSmc = false;

	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox() {
		usbKeyText = (UsbKeyText) view.findViewById(R.id.sip_usbkey);
		usbKeyText.Init(commConversationId, randomNumber, factorMap, this);
		isOtp = usbKeyText.getIsOtp();
		isSmc = usbKeyText.getIsSmc();
		initOptAndSmc();
	}

	public void initOptAndSmc() {
		if (isOtp) {
			// 动态口令
			ll_active_code = (LinearLayout) view
					.findViewById(R.id.ll_active_code);
			ll_active_code.setVisibility(View.VISIBLE);
			sipBoxActiveCode = (SipBox) view.findViewById(R.id.sipbox_active);
			// add by 2016年9月12日 luqp 修改
			sipBoxActiveCode.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
			sipBoxActiveCode.setTextColor(getResources().getColor(android.R.color.black));
			SipBoxUtils.initSipBoxWithTwoType(sipBoxActiveCode, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//			sipBoxActiveCode.setCipherType(SystemConfig.CIPHERTYPE);
//			// sipBoxActiveCode.setLayoutParams(param);
//			sipBoxActiveCode.setTextColor(getResources().getColor(android.R.color.black));
//			// sipBoxActiveCode.setGravity(Gravity.CENTER_VERTICAL);
//			sipBoxActiveCode.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			sipBoxActiveCode.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			sipBoxActiveCode.setId(10002);
//			sipBoxActiveCode.setBackgroundResource(R.drawable.bg_for_edittext);
//			sipBoxActiveCode.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			sipBoxActiveCode.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			sipBoxActiveCode.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
//			sipBoxActiveCode.setSingleLine(true);
//			sipBoxActiveCode.setSipDelegator(this);
//			sipBoxActiveCode.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			sipBoxActiveCode.setRandomKey_S(randomNumber);
			// ll_activecode_sip.addView(sipBoxActiveCode);
		}
		if (isSmc) {
			// 手机交易码
			ll_smc = (LinearLayout) view.findViewById(R.id.ll_smc);
			ll_smc.setVisibility(View.VISIBLE);
			sipBoxSmc = (SipBox) view.findViewById(R.id.sipbox_smc);
			// add by 2016年9月12日 luqp 修改
			sipBoxSmc.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
			sipBoxSmc.setTextColor(getResources().getColor(android.R.color.black));
			SipBoxUtils.initSipBoxWithTwoType(sipBoxSmc, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//			sipBoxSmc.setCipherType(SystemConfig.CIPHERTYPE);
//			// sipBoxSmc.setLayoutParams(param);
//			// sipBoxSmc.setGravity(Gravity.CENTER_VERTICAL);
//			sipBoxSmc.setTextColor(getResources().getColor(android.R.color.black));
//			sipBoxSmc.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			sipBoxSmc.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			sipBoxSmc.setId(10002);
//			sipBoxSmc.setBackgroundResource(R.drawable.bg_for_edittext);
//			sipBoxSmc.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			sipBoxSmc.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			sipBoxSmc.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
//			sipBoxSmc.setSingleLine(true);
//			sipBoxSmc.setSipDelegator(this);
//			sipBoxSmc.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			sipBoxSmc.setRandomKey_S(randomNumber);
			// ll_smc_sip.addView(sipBoxSmc);

			Button smsBtn = (Button) view.findViewById(R.id.smsbtn);
			SmsCodeUtils.getInstance().addSmsCodeListner(smsBtn,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// 发送验证码到手机
							sendMSCToMobile();
						}
					});
		}
		// showOptOrSmc();
	}

	// public void showOptOrSmc() {
	// /** 判断是动态口令还是手机交易码 */
	// if (!StringUtil.isNullOrEmpty(factorList)) {
	// for (int i = 0; i < factorList.size(); i++) {
	// Map<String, Object> itemMap = factorList.get(i);
	// Map<String, Object> securityMap = (Map<String, Object>)
	// itemMap.get(Crcd.FIELD);
	// String name = String.valueOf(securityMap.get(Crcd.NAME));
	// if (Comm.Otp.equals(name)) {
	// isOtp = true;
	// ll_active_code.setVisibility(View.VISIBLE);
	// } else if (Comm.Smc.equals(name)) {
	// isSmc = true;
	// ll_smc.setVisibility(View.VISIBLE);
	// }
	// }
	// }
	// }

	@Override
	public void aquirePSNGetTokenIdCallBack(Object resultObj) {
		super.aquirePSNGetTokenIdCallBack(resultObj);
		crcdServiceInfoSuccess();
	}

	public void crcdServiceInfoSuccess() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		commConversationId = String.valueOf(BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		if ("open".equals(isEdit)) {
			biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDPAPERCHECKOPENRESULT);
		} else if ("edit".equals(isEdit)) {
			biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDINFOSERVICEMODRESULT);
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		map.put(Crcd.CRCD_BILLSERVICEID, billSetupId + "");

		if (billSetupId == 0) {
			map.put(Crcd.CRCD_BILLADDRESS, strAddtrss);
		}
		if (billSetupId == 1) {
			map.put(Crcd.CRCD_BILLADDRESS, CrcdPsnCheckOpenActivity.strEmail);
		}
		if (billSetupId == 2) {
			map.put(Crcd.CRCD_BILLADDRESS, CrcdPsnCheckOpenActivity.phoneNum);
		}
		// if (isOtp) {
		// map.put(Comm.Otp, otpStr);
		// map.put(Comm.Otp_Rc, otp_password_RC);
		// }
		// if (isSmc) {
		// map.put(Comm.Smc, smcStr);
		// map.put(Comm.Smc_Rc, smc_password_RC);
		// }/
		/** 安全工具随机数据加载 */
		usbKeyText.InitUsbKeyResult(map);
		map.put(Crcd.CRCD_TOKEN, tokenId);
		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"crcdServiceInfoSuccessCallBack");
	}

	public static Map<String, Object> returnMap;

	public void crcdServiceInfoSuccessCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
		returnMap = (Map<String, Object>) body.getResult();
		BaseHttpEngine.dissMissProgressDialog();
		Intent it = new Intent(CrcdPsnCheckOpenConfirmActivity.this,
				CrcdPsnCheckOpenSuccessActivity.class);
		it.putExtra(ConstantGloble.CRCD_ISOPENOREDIT, isEdit);
		it.putExtra(ConstantGloble.CRCD_BILLSETUPID, billSetupId);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		it.putExtra(Crcd.CRCD_PAPERADDRESS, strAddtrss);
		// startActivity(it);
		startActivityForResult(it,
				ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)

	{
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
