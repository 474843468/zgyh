package com.chinamworld.bocmbci.biz.drawmoney.remitout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import cfca.mobile.sip.SipBox;

import com.cfca.mobile.log.CodeException;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.DrawMoney;
import com.chinamworld.bocmbci.biz.drawmoney.DrawBaseActivity;
import com.chinamworld.bocmbci.biz.drawmoney.DrawMoneyData;
import com.chinamworld.bocmbci.constant.CheckRegExp;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.DateUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * @ClassName: RemitConfirmActivity
 * @Description: 汇往取款人的确认信息和完成页面
 * @author JiangWei
 * @date 2013-7-17 上午11:34:11
 */
public class RemitConfirmActivity extends DrawBaseActivity {

	/** 汇出账户 */
	private TextView confirmAcount;
	/** 手机号码 */
	private TextView confirmPhone;
	/** 姓名 */
	private TextView confirmName;
	/** 汇款金额 */
	private TextView confirmAmount;
	/** 附言 */
	private TextView confirmFuyan;
	/** 获取交易码按钮 */
	private Button btnSmc;
	/** 下方按钮 */
	private Button btnConfirm;
	/** 第一行标题 */
	private TextView titleFirst;
	/** 第二行标题 */
	private TextView titleSecond;
	/** 取款密码布局 */
	private LinearLayout layoutPass1;
	/** 确定取款密码布局 */
	private LinearLayout layoutPass2;
	/** 手机交易码布局 */
	private LinearLayout layoutPhoneCode;
	/** 动态口令布局 */
	private LinearLayout layoutPass3;
	/** 有效期布局 */
	private LinearLayout dueDateLayout;
	/** 有效期内容 */
	private TextView dueDateText;

	private String acountId;
	private String acountNumber;
	private String phoneNumber;
	private String remitName;
	private String remitCurrencyCode;
	private String remitAmount;
	private String remark;
	private String dueDate;

	private boolean isNeedOtp;
	private boolean isNeedSmc;
	/** 加密控件 输入密码 */
	private SipBox sipBox_pw;
	/** 加密控件 确认密码 */
	private SipBox sipBox_pw_ensure;
	/** 手机交易码 */
	private SipBox sipBox_phone_code;
	/** 加密控件 动态口令 */
	private SipBox sipBox_otp;
	/** 中银E盾 */
	private UsbKeyText usbKeyText;

	private String password1;
	private String password1_RC;
	private String password2;
	private String password2_RC;
	private String smc;
	private String smc_RC;
	private String otp;
	private String otp_RC;
	/** tokenId */
	private String tokenId;
	/**
	 * T42取款页面添加提示信息
	 */
	private TextView tv_add_notice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view = LayoutInflater.from(this).inflate(
				R.layout.drawmoney_remit_confirm_info, null);
		tabcontent.addView(view);
		setTitle(R.string.remitout_title);

		getIntentData();
		findView();
		// initConfirmView()
		/** 跟据返回初始化中银E盾加密控件 */
		initSipBox();
	}

	/**
	 * @Title: getIntentData
	 * @Description: 获取intent带来的数据
	 * @param
	 * @return void
	 */
	private void getIntentData() {
		Intent intent = this.getIntent();
		acountId = intent.getStringExtra(Comm.ACCOUNT_ID);
		acountNumber = intent.getStringExtra(Comm.ACCOUNTNUMBER);
		phoneNumber = intent.getStringExtra(DrawMoney.PAYEE_MOBILE);
		remitName = intent.getStringExtra(DrawMoney.PAYEE_NAME);
		remitCurrencyCode = intent
				.getStringExtra(DrawMoney.REMIT_CURRENCY_CODE);
		remitAmount = intent.getStringExtra(DrawMoney.REMIT_AMOUNT);
		remark = intent.getStringExtra(DrawMoney.REMARK);
		// isNeedOtp = intent.getBooleanExtra(DrawMoney.ISNEEDOTP, false);
		// isNeedSmc = intent.getBooleanExtra(DrawMoney.ISNEEDSMC, false);
	}

	/**
	 * @Title: findView
	 * @Description: 初始化控件
	 * @param
	 * @return void
	 */
	private void findView() {
		confirmAcount = (TextView) this
				.findViewById(R.id.remitout_confirm_account);
		confirmPhone = (TextView) this.findViewById(R.id.remit_confirm_phone);
		confirmName = (TextView) this.findViewById(R.id.remit_confirm_name);
		confirmAmount = (TextView) this.findViewById(R.id.remit_confirm_amount);
		confirmFuyan = (TextView) this.findViewById(R.id.remit_confirm_fuyan);
		titleFirst = (TextView) this.findViewById(R.id.remit_title_tv);
		titleSecond = (TextView) this.findViewById(R.id.remit_title_second);

		layoutPass1 = (LinearLayout) this.findViewById(R.id.view_pass1);
		layoutPhoneCode = (LinearLayout) this
				.findViewById(R.id.view_phone_code);
		layoutPass2 = (LinearLayout) this.findViewById(R.id.view_pass2);
		layoutPass3 = (LinearLayout) this.findViewById(R.id.view_pass3);

		btnSmc = (Button) this.findViewById(R.id.btn_phone_check);
		btnConfirm = (Button) this.findViewById(R.id.remit_confirm_next_btn);
		tv_add_notice = (TextView) this.findViewById(R.id.tv_add_notice);

		dueDateLayout = (LinearLayout) this.findViewById(R.id.due_date_layout);
		dueDateText = (TextView) this.findViewById(R.id.due_date);

		TextView confirmPhoneLable = (TextView) findViewById(R.id.remit_confirm_phone_lable);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				confirmPhoneLable);
	}

	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox() {
		String mmconversationId = (String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		usbKeyText = (UsbKeyText) this.findViewById(R.id.sip_usbkey);
		usbKeyText.Init(mmconversationId, DrawMoneyData.getInstance()
				.getRandomNumber(), RemitInputInfoActivity.result, this);
		isNeedOtp = usbKeyText.getIsOtp();
		isNeedSmc = usbKeyText.getIsSmc();
		initConfirmView();
	}

	/**
	 * @Title: initConfirmView
	 * @Description: 初始化确定页面布局和数据
	 * @param
	 * @return void
	 */
	private void initConfirmView() {
		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				excuseConfirm();
			}
		});

		if (!isNeedSmc) {
			layoutPhoneCode.setVisibility(View.GONE);
		} else {
			// btnSmc.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// sendSMSCToMobile();
			// TimerTaskUtil.getInstance().startGet(
			// RemitConfirmActivity.this, btnSmc);
			// SmsCodeUtils.getInstance().addSmsCodeListner(btnSmc, new
			// OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// // TODO Auto-generated method stub
			//
			// }
			// });
			// }
			// });
			SmsCodeUtils.getInstance().addSmsCodeListner(btnSmc,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							sendSMSCToMobile();
						}
					});
		}
		if (!isNeedOtp) {
			layoutPass3.setVisibility(View.GONE);
		}

		confirmAcount.setText(StringUtil.getForSixForString(String
				.valueOf(acountNumber)));
		confirmPhone.setText(phoneNumber);
		confirmName.setText(remitName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				confirmName);
		confirmAmount.setText(StringUtil.parseStringPattern(remitAmount, 2));
		// add by xby
		if ("".equals(remark)) {
			confirmFuyan.setText(" - ");
		} else {
			confirmFuyan.setText(remark);
		}
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				confirmFuyan);
		LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.layout_pass_1);
		sipBox_pw = new SipBox(this);
		initPasswordSipBox(sipBox_pw);
		sipBox_pw.setHint(R.string.six_number);
		linearLayout1.addView(sipBox_pw);

		LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.layout_pass_2);
		sipBox_pw_ensure = new SipBox(this);
		initPasswordSipBox(sipBox_pw_ensure);
		sipBox_pw_ensure.setHint(R.string.six_number);
		linearLayout2.addView(sipBox_pw_ensure);

		LinearLayout layoutPhoneCode = (LinearLayout) findViewById(R.id.layout_phone_code);
		sipBox_phone_code = new SipBox(this);
		initPasswordSipBox(sipBox_phone_code);
		layoutPhoneCode.addView(sipBox_phone_code);

		LinearLayout linearLayout3 = (LinearLayout) findViewById(R.id.layout_pass_3);
		sipBox_otp = new SipBox(this);
		initPasswordSipBox(sipBox_otp);
		linearLayout3.addView(sipBox_otp);
	}

	/**
	 * @Title: showSuccessView
	 * @Description: 展示成功页面
	 * @param
	 * @return void
	 */
	private void showSuccessView() {
		ibBack.setVisibility(View.GONE);
		titleFirst.setText(this.getResources()
				.getString(R.string.remit_success));
		titleFirst.setVisibility(View.GONE);
		titleSecond.setVisibility(View.VISIBLE);
		dueDateLayout.setVisibility(View.VISIBLE);
		// titleSecond.setText(this.getResources().getString(R.string.remit_success)+this.getResources().getString(R.string.rememeber_get_remit_pwd));
		titleSecond.setText(getString(R.string.rememeber_get_remit_pwd));
		layoutPass1.setVisibility(View.GONE);
		layoutPhoneCode.setVisibility(View.GONE);
		usbKeyText.setVisibility(View.GONE);
		layoutPass2.setVisibility(View.GONE);
		layoutPass3.setVisibility(View.GONE);
		tv_add_notice.setVisibility(View.GONE);
		btnConfirm.setText(this.getResources().getString(R.string.finish));
		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
			}
		});
	}

	/**
	 * @Title: excuseConfirm
	 * @Description: 确定页面下一步按钮的执行
	 * @param
	 * @return void
	 */
	private void excuseConfirm() {
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		RegexpBean psw_rb = new RegexpBean(
				this.getString(R.string.acc_atmpwd_regex_setting), sipBox_pw
						.getText().toString().trim(), "atmpass");
		lists.add(psw_rb);
		RegexpBean psw_ensure_rb = new RegexpBean(
				this.getString(R.string.password_cut_confirm_no_label),
				sipBox_pw_ensure.getText().toString().trim(), "atmpass");
		lists.add(psw_ensure_rb);
		// if (isNeedSmc) {
		// RegexpBean phone_code_rb = new RegexpBean(
		// this.getString(R.string.acc_smc_regex), sipBox_phone_code
		// .getText().toString().trim(), "atmpass");
		// lists.add(phone_code_rb);
		// }
		// if (isNeedOtp) {
		// RegexpBean otpRB = new
		// RegexpBean(this.getString(R.string.set_otp_no),
		// sipBox_otp.getText().toString().trim(), "atmpass");
		// lists.add(otpRB);
		// }
		//
		if (!RegexpUtils.regexpDate(lists)) {
			return;
		}
		// try {
		// password1 = sipBox_pw.getValue().getEncryptPassword();
		// password1_RC = sipBox_pw.getValue().getEncryptRandomNum();
		// password2 = sipBox_pw_ensure.getValue().getEncryptPassword();
		// password2_RC = sipBox_pw_ensure.getValue().getEncryptRandomNum();
		// if (isNeedSmc) {
		// smc = sipBox_phone_code.getValue().getEncryptPassword();
		// smc_RC = sipBox_phone_code.getValue().getEncryptRandomNum();
		// }
		// if (isNeedOtp) {
		// otp = sipBox_otp.getValue().getEncryptPassword();
		// otp_RC = sipBox_otp.getValue().getEncryptRandomNum();
		// }
		// // 获取tokenId
		// BaseHttpEngine.showProgressDialog();
		// requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
		// .getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		// } catch (CodeException e) {
		// LogGloble.exceptionPrint(e);
		// }

		try {
			password1 = sipBox_pw.getValue().getEncryptPassword();
			password1_RC = sipBox_pw.getValue().getEncryptRandomNum();
			password2 = sipBox_pw_ensure.getValue().getEncryptPassword();
			password2_RC = sipBox_pw_ensure.getValue().getEncryptRandomNum();
		} catch (CodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/** 安全工具数据校验 sipBox_otp sipBox_phone_code */
		usbKeyText.checkDataUsbKey(sipBox_otp, sipBox_phone_code,
				new IUsbKeyTextSuccess() {

					@Override
					public void SuccessCallBack(String result, int errorCode) {
						// TODO Auto-generated method stub
						// 获取tokenId
						BaseHttpEngine.showProgressDialog();
						requestPSNGetTokenId((String) BaseDroidApp
								.getInstanse().getBizDataMap()
								.get(ConstantGloble.CONVERSATION_ID));
					}
				});

	}

	/**
	 * @Title: initPasswordSipBox
	 * @Description: 初始化加密控件
	 * @param @param sipBox
	 * @return void
	 */
	private void initPasswordSipBox(SipBox sipBox) {
		sipBox.setCipherType(SystemConfig.CIPHERTYPE);
		LinearLayout.LayoutParams param1 = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		sipBox.setLayoutParams(param1);
		// add by 2016年9月12日 luqp 修改
		sipBox.setTextColor(getResources().getColor(android.R.color.black));
		sipBox.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
		SipBoxUtils.initSipBoxWithTwoType(sipBox, ConstantGloble.MIN_LENGTH,
				ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, DrawMoneyData.getInstance().getRandomNumber(), this);
//		sipBox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		sipBox.setTextColor(getResources().getColor(android.R.color.black));
//		sipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		sipBox.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		sipBox.setId(10002);
//		sipBox.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		sipBox.setBackgroundResource(R.drawable.bg_for_edittext);
//		sipBox.setPasswordRegularExpression(CheckRegExp.PASSWORD);
//		sipBox.setRandomKey_S(DrawMoneyData.getInstance().getRandomNumber());
//		sipBox.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
//		sipBox.setSipDelegator(this);
		InputFilter[] filters1 = { new InputFilter.LengthFilter(20) };
		sipBox.setFilters(filters1);
	}

	/**
	 * @Title: requestPsnMobileRemitSubmit
	 * @Description: 汇往取款人提交
	 * @param
	 * @return void
	 */
	private void requestPsnMobileRemitSubmit() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(DrawMoney.PSN_MOBILE_REMIT_SUBMIT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Comm.ACCOUNT_ID, acountId);
		map.put(DrawMoney.PAYEE_MOBILE, phoneNumber);
		map.put(DrawMoney.PAYEE_NAME, remitName);
		map.put(DrawMoney.REMIT_CURRENCY_CODE, remitCurrencyCode);
		map.put(DrawMoney.REMIT_AMOUNT, remitAmount);
		map.put(DrawMoney.REMARK, remark);
		map.put(DrawMoney.WITH_DRAW_PWD, password1);
		map.put(DrawMoney.WITH_DRAW_PWD_RC, password1_RC);
		map.put(DrawMoney.WITH_DRAW_PWD_CONF, password2);
		map.put(DrawMoney.WITH_DRAW_PWD_CONF_RC, password2_RC);
		// map.put(DrawMoney.SMC, smc);
		// map.put(DrawMoney.SMC_RC, smc_RC);
		// map.put(DrawMoney.OTP, otp);
		// map.put(DrawMoney.OTP_RC, otp_RC);
		/** 安全工具参数获取 */
		usbKeyText.InitUsbKeyResult(map);
		map.put(DrawMoney.TOKEN, tokenId);
		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnMobileRemitSubmitCallback");
	}

	/**
	 * @Title: requestPsnMobileRemitSubmitCallback
	 * @Description: 请求"汇往取款人提交"的回调
	 * @param @param resultObj
	 * @return void
	 */
	public void requestPsnMobileRemitSubmitCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_SUCCESS)) {
			// 设置到期日期
			Map<String, String> map = (Map<String, String>) (biiResponseBody
					.getResult());
			if (!StringUtil.isNullOrEmpty(map)) {
				String dueDate = map.get(DrawMoney.DUE_DATE);
				dueDateText.setText(DateUtils.formatTime(dueDate));
				PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
						dueDateText);
			}
			showSuccessView();
		}
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestPSNGetTokenIdCallBack(resultObj);
		tokenId = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		requestPsnMobileRemitSubmit();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (titleSecond.isShown()) {
			if (KeyEvent.KEYCODE_BACK == keyCode) {
				btnConfirm.performClick();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
