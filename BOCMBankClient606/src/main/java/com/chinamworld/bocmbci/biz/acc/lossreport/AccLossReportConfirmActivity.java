package com.chinamworld.bocmbci.biz.acc.lossreport;

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
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Inves;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 临时挂失确认信息页
 * 
 * @author wangmengmeng
 * 
 */
public class AccLossReportConfirmActivity extends AccBaseActivity {
	/** 临时挂失确认信息页 */
	private View view;
	/** 加密之后加密随机数 */
	private String otp_password_RC = "";
	private String smc_password_RC = "";
	/** 加密控件——动态口令 */
	private SipBox sipBoxActiveCode = null;
	/** 加密控件——手机交易码 */
	private SipBox sipBoxSmc = null;
	/** 中银E盾 */
	private UsbKeyText usbKeytext;
	/** 加密控件里的随机数 */
	protected String randomNumber;
	/** 账户类型 */
	private TextView acc_loss_type;
	/** 账号 */
	private TextView acc_loss_actnum;
	/** 账户别名 */
	private TextView acc_loss_nickname;
	// 借记卡临时挂失优化
	/** 是否冻结主账户 */
	private TextView acc_flozen_value;
	/** 冻结主账户布局 */
	private View ll_acc_valid_date;
	/** 冻结主账户日期 */
	private TextView acc_flozen_data_value;

	private TextView tv_loss_day;

	/** 冻结主账户布局 */
	private TextView acc_flozen_card;

	/** 动态口令 */
	private String otpStr;
	/** 手机交易码 */
	private String smcStr;
	/** 上一步 */
	private Button btn_last;
	/** 确定 */
	private Button btn_confirm;
	/** 获取到的tokenId 保存 */
	private String tokenId;
	/** 手机交易码 */
	private LinearLayout ll_smc;
	/** 动态口令 */
	private LinearLayout ll_active_code;

	/** 进行临时挂失账户信息 */
	private Map<String, Object> lossReportMap;
	/** 临时挂失预交易返回内容 */
	private Map<String, Object> confirmResult;
	/** 安全因子数组 */
	public List<Map<String, Object>> factorList;
	/** 是否上传短信验证码 */
	private boolean isSms = false;
	/** 是否上传动态口令 */
	private boolean isOtp = false;
	/** 临时挂失期限 */
	private String lossDays;
	// 是否冻结主账户
	private boolean isAccFlozenFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.acc_lossreport_title));
		// 右上角按钮赋值
		setText(this.getString(R.string.acc_rightbtn_go_main));
		// 添加布局
		view = addView(R.layout.acc_lossreport_confirm);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		// 请求密码控件随机数
//		requestForRandomNumber();
		// 初始化界面
		init();
	}

	/** 初始化界面 */
	private void init() {
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getString(R.string.acc_debit_step1), this.getString(R.string.acc_debit_step2),
						this.getString(R.string.acc_debit_step3) });
		StepTitleUtils.getInstance().setTitleStep(2);
		
		// 得到选择的账户信息
		lossReportMap = AccDataCenter.getInstance().getLossReportMap();
		confirmResult = AccDataCenter.getInstance().getConfirmResult();
		/** 是否冻结主账户 */
		lossDays = this.getIntent().getStringExtra(Acc.LOSSCONFIRM_LOSSDAYS_REQ);
		/**随机数*/
		randomNumber = this.getIntent().getStringExtra(Acc.RANDOMNUMBER);
		isAccFlozenFlag = this.getIntent().getBooleanExtra(Acc.LOSSCONFIRM_ACC_FLOZENFLAG, false);
		factorList = (List<Map<String, Object>>) confirmResult.get(Acc.LOSSCONFIRM_ACC_FACTORLIST_RES);

		// 初始化中银E盾
		initSipBox();
		// System.out.println("========randomNumber=======================");
		// System.out.println(randomNumber);
		// System.out.println("========randomNumber=======================");
		if (isOtp) {
			// 动态口令
			ll_active_code = (LinearLayout) view.findViewById(R.id.ll_active_code);
			ll_active_code.setVisibility(View.VISIBLE);
			// 动态口令
			sipBoxActiveCode = (SipBox) view.findViewById(R.id.sipbox_active);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(sipBoxActiveCode, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//			sipBoxActiveCode.setCipherType(SystemConfig.CIPHERTYPE);
//			sipBoxActiveCode.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			sipBoxActiveCode.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			sipBoxActiveCode.setId(10002);
//			sipBoxActiveCode.setBackgroundResource(R.drawable.bg_for_edittext);
//			sipBoxActiveCode.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			sipBoxActiveCode.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			sipBoxActiveCode.setSingleLine(true);
//			sipBoxActiveCode.setSipDelegator(this);
//			sipBoxActiveCode.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			sipBoxActiveCode.setRandomKey_S(randomNumber);
		}
		if (isSms) {
			// 手机交易码
			ll_smc = (LinearLayout) view.findViewById(R.id.ll_smc);
			ll_smc.setVisibility(View.VISIBLE);
			sipBoxSmc = (SipBox) view.findViewById(R.id.sipbox_smc);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(sipBoxSmc, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//			sipBoxSmc.setCipherType(SystemConfig.CIPHERTYPE);
//			sipBoxSmc.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			sipBoxSmc.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			sipBoxSmc.setId(10002);
//			sipBoxSmc.setBackgroundResource(R.drawable.bg_for_edittext);
//			sipBoxSmc.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			sipBoxSmc.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			sipBoxSmc.setSingleLine(true);
//			sipBoxSmc.setSipDelegator(this);
//			sipBoxSmc.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			sipBoxSmc.setRandomKey_S(randomNumber);
			Button smsBtn = (Button) view.findViewById(R.id.smsbtn);
			SmsCodeUtils.getInstance().addSmsCodeListner(smsBtn, new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 发送验证码到手机
					sendMSCToMobile();
				}
			});
		}

		// 判断
		// initSmcAndOtp();
		acc_loss_type = (TextView) view.findViewById(R.id.tv_acc_loss_type_value);
		String loss_type = String.valueOf(lossReportMap.get(Acc.ACC_ACCOUNTTYPE_RES));
		acc_loss_type.setText(LocalData.AccountType.get(loss_type));
		acc_loss_actnum = (TextView) view.findViewById(R.id.tv_acc_loss_actnum_value);
		String acc_loss_number = String.valueOf(lossReportMap.get(Acc.ACC_ACCOUNTNUMBER_RES));
		acc_loss_actnum.setText(StringUtil.getForSixForString(acc_loss_number));

		/** 账户别名 */
		acc_loss_nickname = (TextView) view.findViewById(R.id.tv_acc_loss_nickname_value);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, acc_loss_nickname);
		acc_loss_nickname.setText(String.valueOf(lossReportMap.get(Acc.ACC_NICKNAME_RES)));

		tv_loss_day = (TextView) view.findViewById(R.id.tv_acc_loss_date_value);
		tv_loss_day.setText(lossDaysMap.get(lossDays));
		btn_last = (Button) view.findViewById(R.id.btnLast);
		btn_last.setOnClickListener(goLastClickListener);

		ll_acc_valid_date = view.findViewById(R.id.ll_acc_valid_date);
		acc_flozen_value = (TextView) view.findViewById(R.id.tv_acc_flozen_time);
		acc_flozen_data_value = (TextView) view.findViewById(R.id.tv_acc_flozen_date_value);

		if (isAccFlozenFlag) {
			ll_acc_valid_date.setVisibility(View.VISIBLE);
			acc_flozen_value.setText("是");
			acc_flozen_data_value.setText(lossDaysMap.get(lossDays));
		} else {
			ll_acc_valid_date.setVisibility(View.GONE);
			acc_flozen_value.setText("否");
			acc_flozen_data_value.setText(lossDaysMap.get(lossDays));
		}

		/** 有效期和是否冻结主账户的弹框设置 */
		TextView acc_flozen_often = (TextView) view.findViewById(R.id.tv_acc_flozen_often);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, acc_flozen_often);
		TextView tv_acc_flozen_date_label = (TextView) view.findViewById(R.id.tv_acc_flozen_date_label);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_acc_flozen_date_label);

		btn_confirm = (Button) view.findViewById(R.id.btnConfirm);
		btn_confirm.setOnClickListener(confirmClickListener);
		// // 请求密码控件随机数
		// requestForRandomNumber();
	}

	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox() {
		String mmconversationId = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID);
		// 中银E盾
		usbKeytext = (UsbKeyText) view.findViewById(R.id.sip_usbkey);
		usbKeytext.Init(mmconversationId, randomNumber, confirmResult, this);
		isOtp = usbKeytext.getIsOtp();
		isSms = usbKeytext.getIsSmc();
	}

	// /** 判断是动态口令还是手机交易码 */
	// public void initSmcAndOtp() {
	// if (!StringUtil.isNullOrEmpty(factorList)) {
	// for (int i = 0; i < factorList.size(); i++) {
	// Map<String, Object> itemMap = factorList.get(i);
	// Map<String, String> securityMap = (Map<String, String>) itemMap
	// .get(Inves.FIELD);
	// String name = securityMap.get(Inves.NAME);
	// if (Inves.Smc.equals(name)) {
	// isSms = true;
	// ll_smc.setVisibility(View.VISIBLE);
	// } else if (Inves.Otp.equals(name)) {
	// isOtp = true;
	// ll_active_code.setVisibility(View.VISIBLE);
	// }
	// }
	// }
	// }

	/**
	 * 请求密码控件随机数
	 */
//	public void requestForRandomNumber() {
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
//		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
//				.get(ConstantGloble.CONVERSATION_ID));
//		BiiHttpEngine.showProgressDialog();
//		HttpManager.requestBii(biiRequestBody, this, "queryRandomNumberCallBack");
//	}

//	/**
//	 * 请求密码控件随机数 回调
//	 *
//	 * @param resultObj
//	 */
//	public void queryRandomNumberCallBack(Object resultObj) {
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		randomNumber = (String) biiResponseBody.getResult();
//		if (StringUtil.isNull(randomNumber)) {
//			return;
//		}
//		// // 加密控件设置随机数
//		// sipBoxActiveCode.setRandomKey_S(randomNumber);
//		// sipBoxSmc.setRandomKey_S(randomNumber);
//		BiiHttpEngine.dissMissProgressDialog();
//		// 初始化界面
//		init();
//
//	}

	public void sendMSCToMobile() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.SET_SENDMSC);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "sendMSCToMobileCallback");
	}

	public void sendMSCToMobileCallback(Object resultObj) {
	}

	/**
	 * 获取tokenId
	 */
	public void pSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		BaseHttpEngine.showProgressDialog();
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
		if (StringUtil.isNull(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		// 进行临时挂失请求
		requestLossReportResult();
	}

	/**
	 * 请求临时挂失提交交易
	 */
	public void requestLossReportResult() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.ACC_LOSSREPORTRESULT_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Acc.LOSSRESULT_ACTNUM_REQ, (String) lossReportMap.get(Acc.ACC_ACCOUNTNUMBER_RES));
		map.put(Acc.LOSSRESULT_ACC_ACCOUNTID_REQ, (String) lossReportMap.get(Acc.ACC_ACCOUNTID_RES));
		map.put(Acc.LOSSRESULT_ACC_LOSSDAYS_REQ, lossDays);
		map.put(Acc.LOSSCONFIRM_ACC_FLOZENFLAG, isAccFlozenFlag ? "Y" : "N");

		// if (isOtp) {
		// map.put(Comm.Otp, otpStr);
		// map.put(Comm.Otp_Rc, otp_password_RC);
		//
		// }
		// if (isSms) {
		// map.put(Comm.Smc, smcStr);
		// map.put(Comm.Smc_Rc, smc_password_RC);
		// }
		// 安全工具参数获取
		usbKeytext.InitUsbKeyResult(map);
		// CA密文
		// map.put(Acc.LOSSRESULT_ACC_SIGNEDDATA_REQ, (String)
		// confirmResult.get(Acc.LOSSCONFIRM_ACC_PLAINDATA_RES));
		map.put(Acc.LOSSRESULT_ACC_TOKEN_REQ, tokenId);
		map.put(Acc.LOSSRESULT_NAME_REQ,
				((Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA))
						.get(Inves.CUSTOMERNAME));
		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestLossReportResultCallBack");
	}

	/**
	 * 请求临时挂失提交交易
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void requestLossReportResultCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		// String result = (String) biiResponseBody.getResult();
		// 借记卡挂失改造
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		boolean reportLossStatus = Boolean.valueOf((String) result.get(Acc.LOSSRESULT_ACC_REPORTLOSS_STATUS));
		boolean accFrozenStatus = Boolean.valueOf((String) result.get(Acc.LOSSRESULT_ACC_FROZEN_STATUS));
		// 进入临时挂失成功页面
		Intent intent = new Intent(AccLossReportConfirmActivity.this, AccLossReportSuccessActivity.class);
		intent.putExtra(Acc.LOSSCONFIRM_LOSSDAYS_REQ, lossDays);
		intent.putExtra(Acc.LOSSCONFIRM_ACC_FLOZENFLAG, isAccFlozenFlag);
		intent.putExtra(Acc.LOSSRESULT_ACC_REPORTLOSS_STATUS, reportLossStatus);
		intent.putExtra(Acc.LOSSRESULT_ACC_FROZEN_STATUS, accFrozenStatus);
		startActivity(intent);
	}

	/** 上一步按钮点击事件 */
	OnClickListener goLastClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};
	/** 确定按钮点击事件 */
	OnClickListener confirmClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// // 动态口令
			// RegexpBean sipRegexpBean = new RegexpBean(
			// AccLossReportConfirmActivity.this
			// .getString(R.string.active_code_regex),
			// sipBoxActiveCode.getText().toString(),
			// ConstantGloble.SIPOTPPSW);
			// RegexpBean sipSmcpBean = new RegexpBean(
			// AccLossReportConfirmActivity.this
			// .getString(R.string.acc_smc_regex),
			// sipBoxSmc.getText().toString(), ConstantGloble.SIPSMCPSW);
			// ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			//
			// if (isSms) {
			// lists.add(sipSmcpBean);
			// }
			// if (isOtp) {
			// lists.add(sipRegexpBean);
			// }
			// if (RegexpUtils.regexpDate(lists)) {// 校验通过
			//
			// if (isSms) {
			// try {
			// smcStr = sipBoxSmc.getValue().getEncryptPassword();
			// smc_password_RC = sipBoxSmc.getValue()
			// .getEncryptRandomNum();
			// } catch (CodeException e) {
			// LogGloble.exceptionPrint(e);
			// }
			//
			// }
			// if (isOtp) {
			// try {
			// otpStr = sipBoxActiveCode.getValue()
			// .getEncryptPassword();
			// otp_password_RC = sipBoxActiveCode.getValue()
			// .getEncryptRandomNum();
			// } catch (CodeException e) {
			// LogGloble.exceptionPrint(e);
			// }
			//
			// }
			//
			// }
			if (confirmResult.containsKey(Acc.LOSSCONFIRM_ACC_FACTORLIST_RES)) {
				BaseHttpEngine.showProgressDialog();
				checkDate();
			} else {
				BaseHttpEngine.showProgressDialog();
				pSNGetTokenId();
			}
		}
	};

	/** 验证动态口令 */
	private void checkDate() {
		/** 音频Key安全认证工具 */
		usbKeytext.checkDataUsbKey(sipBoxActiveCode, sipBoxSmc, new IUsbKeyTextSuccess() {

			@Override
			public void SuccessCallBack(String result, int errorCode) {
				// TODO Auto-generated method stub
				BaseHttpEngine.showProgressDialog();
				pSNGetTokenId();
			}
		});
	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 回主页面
			ActivityTaskManager.getInstance().removeAllActivity();
		}
	};
}
