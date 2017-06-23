package com.chinamworld.bocmbci.biz.epay.myPayService.bomobile.servOpen;

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
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice.VirtualBCServiceWriteConfirmActivity;
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
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 电子支付-开通确认页面
 * 
 * @author Administrator
 * 
 */
public class ServOpenConfirmActivity extends EPayBaseActivity {

	private View epayServiceOpenConfirm;

	private TextView tv_obligateMsg;
	private TextView tv_sysEachDayMaxAmount;
	private TextView tv_custEachDayMaxAmount;
	private TextView tv_eachMaxAmount;
	// private EditText et_dynamicCode;
	private SipBox sb_dynamic_code;
	private SipBox sb_note_code;
	private LinearLayout ll_otp;
	private LinearLayout ll_smc;
	private LinearLayout ll_selected_acclist;
	// private LinearLayout ll_obligate_msg;

	// private Button bt_back;
	private Button bt_smsbtn;
	private Button bt_submit;

	private Context bomTransContext;

	private String obligateMsg;
	private String dayMaxQuota;
	private String perMaxQuota;
	private String custMaxQuota;
	private String token;

	private List<Object> factorList;
	private List<Object> accNumberList;

	private PubHttpObserver httpObserver;

	private boolean confirmOtp;
	private boolean confirmSmc;

	private String randomKey;

	private String otp;
	private String otpRC;
	private String smc;
	private String smcRC;
	// 是否从电子支付直接进来
	private boolean acclistFlag;

	// TODO
	/** 中银E盾加密控件 */
	UsbKeyText usbKeytext;
	/** 标识是否有动态口令 */
	private boolean isOtp = false;
	/** 标识是否有短息验证码 */
	private boolean isSmc = false;
	/** 安全因子Map */
	private Map<String, Object> securityMap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		acclistFlag = getIntent().getBooleanExtra("acclistFlag", false);
		bomTransContext = TransContext.getBomContext();
		accNumberList = new ArrayList<Object>();
		// 获取交易数据
		httpObserver = PubHttpObserver.getInstance(this,
				PubConstants.CONTEXT_BOM);
		epayServiceOpenConfirm = LayoutInflater.from(this).inflate(
				R.layout.epay_bom_service_open_confirm, null);

		super.setType(0);
		super.setShowBackBtn(true);
		super.setTitleName(PubConstants.TITLE_BOANK_OF_MOBILE);
		super.setContentView(epayServiceOpenConfirm);
		super.onCreate(savedInstanceState);
		// 初始化导航条
		// EpayPubUtil.initStepBar(this, 1, new String[] { "确认信息", "开通成功", ""
		// });

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

	private void initCurPage() {

		// TODO
		securityMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.ISFOREX_PRERESULT_KEY);
		commConversationId = (String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);

		usbKeytext = (UsbKeyText) epayServiceOpenConfirm
				.findViewById(R.id.sip_usbkey);
		usbKeytext.Init(commConversationId, randomKey, securityMap, this);
		isOtp = usbKeytext.getIsOtp();
		isSmc = usbKeytext.getIsSmc();

		// ll_obligate_msg = (LinearLayout)
		// epayServiceOpenConfirm.findViewById(R.id.ll_obligate_msg);
		tv_obligateMsg = (TextView) epayServiceOpenConfirm
				.findViewById(R.id.tv_obligate_msg);
		tv_sysEachDayMaxAmount = (TextView) epayServiceOpenConfirm
				.findViewById(R.id.tv_sys_eachday_maxamount);
		tv_custEachDayMaxAmount = (TextView) epayServiceOpenConfirm
				.findViewById(R.id.tv_cust_eachday_maxamount);
		tv_eachMaxAmount = (TextView) epayServiceOpenConfirm
				.findViewById(R.id.tv_each_maxamount);

		// 添加提示
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) epayServiceOpenConfirm.findViewById(R.id.tip_one));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) epayServiceOpenConfirm.findViewById(R.id.tip_two));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) epayServiceOpenConfirm.findViewById(R.id.tip_three));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_obligateMsg);

		sb_dynamic_code = (SipBox) epayServiceOpenConfirm.findViewById(R.id.sb_dynamic_code);
		// add by 2016年9月12日 luqp 修改
		SipBoxUtils.initSipBoxWithTwoType(sb_dynamic_code, ConstantGloble.MIN_LENGTH,
				ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomKey, this);
//		sb_dynamic_code.setCipherType(SystemConfig.CIPHERTYPE);
//		sb_dynamic_code.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		sb_dynamic_code.setId(10002);
//		sb_dynamic_code.setPasswordMinLength(6);
//		sb_dynamic_code.setPasswordMaxLength(6);
//		sb_dynamic_code.setPasswordRegularExpression(CheckRegExp.OTP);
//		sb_dynamic_code.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		sb_dynamic_code.setSipDelegator(this);
//		sb_dynamic_code.setRandomKey_S(randomKey);

		sb_note_code = (SipBox) epayServiceOpenConfirm.findViewById(R.id.sb_note_code);
		// add by 2016年9月12日 luqp 修改
		SipBoxUtils.initSipBoxWithTwoType(sb_note_code, ConstantGloble.MIN_LENGTH,
				ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomKey, this);
//		sb_note_code.setCipherType(SystemConfig.CIPHERTYPE);
//		sb_note_code.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		sb_note_code.setId(10002);
//		sb_note_code.setPasswordMinLength(6);
//		sb_note_code.setPasswordMaxLength(6);
//		sb_note_code.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		sb_note_code.setPasswordRegularExpression(CheckRegExp.OTP);
//		sb_note_code.setSipDelegator(this);
//		sb_note_code.setRandomKey_S(randomKey);

		ll_otp = (LinearLayout) epayServiceOpenConfirm
				.findViewById(R.id.ll_otp);
		ll_smc = (LinearLayout) epayServiceOpenConfirm
				.findViewById(R.id.ll_smc);
		ll_selected_acclist = (LinearLayout) epayServiceOpenConfirm
				.findViewById(R.id.ll_selected_acclist);

		// bt_back = (Button) epayServiceOpenConfirm.findViewById(R.id.bt_back);
		//
		// bt_back.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Intent intent = new Intent(ServOpenConfirmActivity.this,
		// ServOpenMsgInputActivity.class);
		// ServOpenConfirmActivity.this.startActivity(intent);
		// ServOpenConfirmActivity.this.finish();
		// }
		// });

		bt_smsbtn = (Button) epayServiceOpenConfirm
				.findViewById(R.id.bt_smsbtn);
		SmsCodeUtils.getInstance().addSmsCodeListner(bt_smsbtn,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// 发送手机交易码的请求
						sendSMSCToMobile();
					}
				});

		bt_submit = (Button) epayServiceOpenConfirm
				.findViewById(R.id.bt_submit);
		bt_submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (securityMap.containsKey(Bond.BUY_CONFIRM_FACTORLIST)) {
					checkDate();
				}
			}
		});

		initDisplay();
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
			// if (confirmOtp) {
			// try {
			// otp = sb_dynamic_code.getValue().getEncryptPassword();
			// otpRC = sb_dynamic_code.getValue().getEncryptRandomNum();
			// } catch (CodeException e) {
			// BaseDroidApp.getInstanse().showInfoMessageDialog(e.getMessage());
			// isSuccess = false;
			// }
			// }
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
		} else {
			isSuccess = false;
		}

		return isSuccess;
	}

	public void getTokenCallback(Object resultObj) {
		token = EpayUtil.getString(httpObserver.getResult(resultObj), "");
		// 组装开通交易数据
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(BomConstants.METHOD_OPEN_PAYMENT_SERVICE_FIELD_CHECK_ACCTS,
				accNumberList);
		params.put(BomConstants.METHOD_OPEN_PAYMENT_SERVICE_FIELD_LOGIN_HINT,
				obligateMsg);
		params.put(BomConstants.METHOD_OPEN_PAYMENT_SERVICE_FIELD_QUOTA,
				custMaxQuota);
		params.put(PubConstants.PUB_FIELD_TOKEN_ID, token);
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
		httpObserver.req_openBomPaymentService(params,
				"openBomPaymentServiceCallback");
	}

	/**
	 * 开通电子支付服务回调方法
	 * 
	 * @param resultObj
	 */
	public void openBomPaymentServiceCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent(this, ServOpenResultActivity.class);
		intent.putExtra("acclistFlag", acclistFlag);
		startActivityForResult(intent, 0);
	}

	/**
	 * 获取交易数据
	 */
	private void getTransData() {
		factorList = bomTransContext.getList(PubConstants.PUB_FIELD_FACTORLIST);
		bomTransContext.clear(PubConstants.PUB_FIELD_FACTORLIST);

		obligateMsg = bomTransContext.getString(
				BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_OBLIGATE_MSG, "");
		dayMaxQuota = bomTransContext.getString(
				BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_DAY_MAX, "");
		custMaxQuota = bomTransContext.getString(
				PubConstants.METHOD_QUERY_CUST_MAX_QUOTA_FIELD_AMOUNT, "");
		perMaxQuota = bomTransContext.getString(
				BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_PER_MAX, "");

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
		initCurPage();
		BiiHttpEngine.dissMissProgressDialog();
	}

	/**
	 * 初始显示内容
	 */
	private void initDisplay() {
		// 设置验证输入框
		for (Object obj : factorList) {
			String confirmType = EpayUtil.getString(obj, "");
			if (PubConstants.PUB_FIELD_OTP.equals(confirmType)) {
				ll_otp.setVisibility(View.VISIBLE);
				confirmOtp = true;

			} else if (PubConstants.PUB_FIELD_SMC.equals(confirmType)) {
				ll_smc.setVisibility(View.VISIBLE);
				confirmSmc = true;

			}
		}

		if (acclistFlag) {
			// 设置所选账户
			List<Object> selectedList = bomTransContext
					.getList(PubConstants.CONTEXT_FIELD_SELECTED_ACCLIST);
			for (int i = 0; i < selectedList.size(); i++) {
				Map<Object, Object> map = EpayUtil.getMap(selectedList.get(i));
				View view = LayoutInflater.from(this).inflate(
						R.layout.epay_bom_selected_acc_list_item, null);
				TextView tv_acc_number = (TextView) view
						.findViewById(R.id.item_acc_number);
				String acc_number = EpayUtil
						.getString(
								map.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_NUMBER),
								"");
				String accountId = EpayUtil
						.getString(
								map.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_ID),
								"");
				accNumberList.add(accountId);
				// accNumberList.add(acc_number);
				tv_acc_number
						.setText(StringUtil.getForSixForString(acc_number));
				ll_selected_acclist.addView(view);
			}
		} else {
			String virtualNo = String
					.valueOf(VirtualBCServiceWriteConfirmActivity.vcardinfo
							.get(Crcd.CRCD_VIRTUALCARDNO));
			accNumberList
					.add(VirtualBCServiceWriteConfirmActivity.vcardResultList
							.get(Crcd.CRCD_VIRTUALCARDID));
			View view = LayoutInflater.from(this).inflate(
					R.layout.epay_bom_selected_acc_list_item, null);
			TextView tv_acc_number = (TextView) view
					.findViewById(R.id.item_acc_number);
			tv_acc_number.setText(StringUtil.getForSixForString(virtualNo));
			ll_selected_acclist.addView(view);
		}

		tv_sysEachDayMaxAmount.setText(StringUtil.parseStringPattern(
				dayMaxQuota, 2));
		if (!StringUtil.isNullOrEmpty(obligateMsg)) {
			tv_obligateMsg.setText(obligateMsg);
		} else {
			// ll_obligate_msg.setVisibility(View.GONE);
			tv_obligateMsg.setText("-");
		}

		tv_custEachDayMaxAmount.setText(StringUtil.parseStringPattern(
				custMaxQuota, 2));
		tv_eachMaxAmount.setText(StringUtil.parseStringPattern(perMaxQuota, 2));
	}
}
