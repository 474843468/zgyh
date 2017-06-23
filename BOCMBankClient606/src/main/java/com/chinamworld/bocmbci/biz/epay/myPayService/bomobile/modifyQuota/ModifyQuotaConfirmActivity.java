package com.chinamworld.bocmbci.biz.epay.myPayService.bomobile.modifyQuota;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cfca.mobile.sip.SipBox;

import com.cfca.mobile.log.CodeException;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Bond;
import com.chinamworld.bocmbci.biz.epay.EPayBaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.BomConstants;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.observer.PubHttpObserver;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.constant.CheckRegExp;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 电子支付-限额修改确认页面
 * 
 */
public class ModifyQuotaConfirmActivity extends EPayBaseActivity {

	private String tag = "EPayModifyQuotaConfirmActivity";

	private View ePayModifyQuotaConfirm;

	private TextView tv_day_max_quota;
	private TextView tv_per_max_quota;
	private TextView tv_cust_max_quota;
	private SipBox sb_dynamic_code;
	private SipBox sb_note_code;
	private LinearLayout ll_otp;
	private LinearLayout ll_smc;

	private Button bt_sure;
	private Button bt_smsbtn;
	// private Button bt_back;

	private String dayMaxQuota;
	private String perMaxQuota;
	private String custMaxQuota;

	private boolean confirmOtp;
	private boolean confirmSmc;

	private PubHttpObserver httpObserver;
	private Context bomTransContext;
	private List<Object> factorList;
	private String randomKey;

	private String otp;
	private String otpRC;
	private String smc;
	private String smcRC;

	// TODO
	/** 中银E盾加密控件 */
	UsbKeyText usbKeytext;
	/** 标识是否有动态口令 */
	private boolean isOtp = false;
	/** 标识是否有短息验证码 */
	private boolean isSmc = false;
	/** 安全因子Map */
	private Map<String, Object> securityMap = null;

	/** 加密控件 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		bomTransContext = TransContext.getBomContext();
		httpObserver = PubHttpObserver.getInstance(this,
				PubConstants.CONTEXT_BOM);
		ePayModifyQuotaConfirm = LayoutInflater.from(this).inflate(
				R.layout.epay_bom_modify_quota_confirm, null);

		super.setShowBackBtn(true);
		super.setTitleName(PubConstants.TITLE_BOANK_OF_MOBILE);
		super.setContentView(ePayModifyQuotaConfirm);
		super.onCreate(savedInstanceState);
		setLeftButtonPopupGone();
		initTitleRightButton("关闭", rBtncloseListener);
		hideFoot();
		// // 初始化导航条
		// EpayPubUtil.initStepBar(this, 2, new String[] { "修改限额", "确认信息",
		// "修改成功" });
		getTransData();
	}

	// TODO
	/** 验证动态口令 */
	private void checkDate() {
		/** 音频Key安全工具认证 */
		usbKeytext.checkDataUsbKey(sb_dynamic_code, sb_note_code,
				new IUsbKeyTextSuccess() {

					@Override
					public void SuccessCallBack(String result, int errorCode) {
						// TODO Auto-generated method stub

						BaseHttpEngine.showProgressDialog();

						httpObserver.req_getToken("getTokenCallback");
					}
				});

	}

	private void getTransData() {
		Intent intent = getIntent();
		dayMaxQuota = intent
				.getStringExtra(BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_DAY_MAX);
		perMaxQuota = intent
				.getStringExtra(BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_PER_MAX);
		custMaxQuota = intent
				.getStringExtra(BomConstants.METHOD_SET_MAX_QUOTA_PRE_FIELD_QUOTA);
		factorList = bomTransContext.getList(PubConstants.PUB_FIELD_FACTORLIST);
		BiiHttpEngine.showProgressDialogCanGoBack();
		httpObserver.req_randomKey("getRandomKeyCallback");
	}

	/**
	 * 请求加密控件随机数回调方法
	 * 
	 * @param resultObj
	 */
	public void getRandomKeyCallback(Object resultObj) {
		Object result = httpObserver.getResult(resultObj);
		randomKey = EpayUtil.getString(result, "");
		// 初始化当前页
		initCurPage();
	}

	private void initCurPage() {

		securityMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.ISFOREX_PRERESULT_KEY);
		commConversationId = (String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);

		usbKeytext = (UsbKeyText) ePayModifyQuotaConfirm
				.findViewById(R.id.sip_usbkey);
		usbKeytext.Init(commConversationId, randomKey, securityMap, this);
		isOtp = usbKeytext.getIsOtp();
		isSmc = usbKeytext.getIsSmc();

		tv_day_max_quota = (TextView) ePayModifyQuotaConfirm
				.findViewById(R.id.tv_day_max_quota);
		tv_per_max_quota = (TextView) ePayModifyQuotaConfirm
				.findViewById(R.id.tv_per_max_quota);
		tv_cust_max_quota = (TextView) ePayModifyQuotaConfirm
				.findViewById(R.id.tv_cust_max_quota);

		if (isOtp) {
			sb_dynamic_code = (SipBox) ePayModifyQuotaConfirm.findViewById(R.id.sb_dynamic_code);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(sb_dynamic_code, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomKey, this);
//			sb_dynamic_code.setCipherType(SystemConfig.CIPHERTYPE);
//			sb_dynamic_code.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			sb_dynamic_code.setId(10002);
//			sb_dynamic_code.setPasswordMinLength(6);
//			sb_dynamic_code.setPasswordMaxLength(6);
//			sb_dynamic_code.setPasswordRegularExpression(CheckRegExp.OTP);
//			sb_dynamic_code.setSipDelegator(this);
//			sb_dynamic_code.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			sb_dynamic_code.setRandomKey_S(randomKey);
			ll_otp = (LinearLayout) ePayModifyQuotaConfirm.findViewById(R.id.ll_otp);
			ll_otp.setVisibility(View.VISIBLE);
		}
		if (isSmc) {
			sb_note_code = (SipBox) ePayModifyQuotaConfirm.findViewById(R.id.sb_note_code);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(sb_note_code, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomKey, this);
//			sb_note_code.setCipherType(SystemConfig.CIPHERTYPE);
//			sb_note_code.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			sb_note_code.setId(10002);
//			sb_note_code.setPasswordMinLength(6);
//			sb_note_code.setPasswordMaxLength(6);
//			sb_note_code.setPasswordRegularExpression(CheckRegExp.OTP);
//			sb_note_code.setSipDelegator(this);
//			sb_note_code.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			sb_note_code.setRandomKey_S(randomKey);
			ll_smc = (LinearLayout) ePayModifyQuotaConfirm.findViewById(R.id.ll_smc);
			ll_smc.setVisibility(View.VISIBLE);
		}

		// bt_back = (Button) ePayModifyQuotaConfirm.findViewById(R.id.bt_back);
		// bt_back.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// ModifyQuotaConfirmActivity.this.finish();
		// }
		// });

		bt_smsbtn = (Button) ePayModifyQuotaConfirm
				.findViewById(R.id.bt_smsbtn);
		SmsCodeUtils.getInstance().addSmsCodeListner(bt_smsbtn,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						sendSMSCToMobile();
					}
				});

		bt_sure = (Button) ePayModifyQuotaConfirm.findViewById(R.id.bt_sure);
		bt_sure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/*
				 * if (!checkSubmitData()) return;
				 */
				if (securityMap.containsKey(Bond.BUY_CONFIRM_FACTORLIST)) {
					checkDate();
				}

			}
		});

		initDisplay();
	}

	private void initDisplay() {
		/*
		 * for (Object obj : factorList) { String confirmType =
		 * EpayUtil.getString(obj, ""); if
		 * (PubConstants.PUB_FIELD_OTP.equals(confirmType)) { confirmOtp = true;
		 * ll_otp.setVisibility(View.VISIBLE); } else if
		 * (PubConstants.PUB_FIELD_SMC.equals(confirmType)) { confirmSmc = true;
		 * ll_smc.setVisibility(View.VISIBLE); } }
		 */
		tv_cust_max_quota.setText(StringUtil
				.parseStringPattern(custMaxQuota, 2));
		tv_per_max_quota.setText(StringUtil.parseStringPattern(perMaxQuota, 2));
		tv_day_max_quota.setText(StringUtil.parseStringPattern(dayMaxQuota, 2));
		BiiHttpEngine.dissMissProgressDialog();
	}

	/**
	 * 检测提交数据
	 * 
	 * @return
	 */
	private boolean checkSubmitData() {
		boolean isSuccess = true;

		RegexpBean rbOtp = new RegexpBean(getResources().getString(
				R.string.active_code_regex), EpayUtil.getString(
				sb_dynamic_code.getText(), ""), "otp");
		RegexpBean rbSms = new RegexpBean(getResources().getString(
				R.string.set_smc_no), EpayUtil.getString(
				sb_note_code.getText(), ""), "smc");
		ArrayList<RegexpBean> list = new ArrayList<RegexpBean>();

		// if (confirmOtp) {
		// list.add(rbOtp);
		// }
		if (confirmSmc) {
			list.add(rbSms);
		}
		if (confirmOtp) {
			list.add(rbOtp);
		}

		if (RegexpUtils.regexpDate(list)) {
			if (confirmOtp) {
				try {
					otp = sb_dynamic_code.getValue().getEncryptPassword();
					otpRC = sb_dynamic_code.getValue().getEncryptRandomNum();
				} catch (CodeException e) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							e.getMessage());
					isSuccess = false;
				}
			}
			if (confirmSmc) {
				try {
					smc = sb_note_code.getValue().getEncryptPassword();
					smcRC = sb_note_code.getValue().getEncryptRandomNum();
				} catch (CodeException e) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							e.getMessage());
					isSuccess = false;
				}
			}
		} else {
			isSuccess = false;
		}

		return isSuccess;
	}

	/**
	 * 获取ConversationId 回调方法
	 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		String conversationId = EpayUtil.getString(
				httpObserver.getResult(resultObj), "");
		httpObserver.setConversationId(conversationId);
		httpObserver.req_getToken("getTokenCallback");
	}

	/**
	 * 获取tokenId回调方法
	 * 
	 * @param resultObj
	 * @throws Exception
	 */
	public void getTokenCallback(Object resultObj) throws Exception {
		Object result = httpObserver.getResult(resultObj);
		String token = EpayUtil.getString(result, "");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(BomConstants.METHOD_SET_MAX_QUOTA_FIELD_QUOTA, custMaxQuota);
		params.put(PubConstants.PUB_FIELD_TOKEN_ID, token);
		params.put(PubConstants.PUB_FIELD_SERVICE_ID, "P200");
		/*
		 * if (confirmOtp) { params.put(PubConstants.PUB_FIELD_OTP, otp);
		 * params.put(PubConstants.PUB_FIELD_OTP_RC, otpRC); } if (confirmSmc) {
		 * params.put(PubConstants.PUB_FIELD_SMC, smc);
		 * params.put(PubConstants.PUB_FIELD_SMC_RC, smcRC); }
		 */

		// TODO
		/** 安全工具参数获取 */
		usbKeytext.InitUsbKeyResult(params);

		SipBoxUtils.setSipBoxParams(params);
		httpObserver.req_setPaymentQuota(params, "setPaymentQuotaCallback");
	}

	/**
	 * 修改限额预交易回调方法
	 * 
	 * @param resultObj
	 */
	public void setPaymentQuotaCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		// Object result = httpObserver.getResult(resultObj);
		Intent intent = new Intent(ModifyQuotaConfirmActivity.this,
				ModifyQuotaResultActivity.class);
		intent.putExtra(BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_DAY_MAX,
				dayMaxQuota);
		intent.putExtra(BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_PER_MAX,
				perMaxQuota);
		intent.putExtra(BomConstants.METHOD_SET_MAX_QUOTA_PRE_FIELD_QUOTA,
				custMaxQuota);
		bomTransContext.setData(
				PubConstants.METHOD_QUERY_CUST_MAX_QUOTA_FIELD_AMOUNT,
				custMaxQuota);
		startActivityForResult(intent, 0);
	}

}
