package com.chinamworld.bocmbci.biz.blpt.sign;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cfca.mobile.sip.SipBox;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Blpt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.blpt.BillPaymentBaseActivity;
import com.chinamworld.bocmbci.biz.blpt.BlptUtil;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 申请新服务信息确认
 * 
 * @author panwe
 * 
 */
public class BillNewApplyMsgConfirmActivity extends BillPaymentBaseActivity
		implements OnClickListener {

	/** 主布局 */
	private View viewContent;
	/** 上一步按钮 */
	private Button btnLast;
	/** 确定按钮 */
	private Button btnConfirm;
	/** 动态口令加密控件 **/
	private SipBox sipBox;
	/** 第几步 **/
	private String laststepNo;
	private String stepNo;
	/** 总步数 **/
	private String laststepNum;
	private String stepNum;
	/** 动态主界面 ***/
	private LinearLayout mainLayout;
	private String coombin;
	/*** 上一步接收的账号信息 **/
	private String accNum;
	private String accId;
	/** 标识是否有短息验证码、动态口令 */
	private boolean isOtp = false;
	private boolean isSmc = false;
	private SipBox sipSms;
	// 中银E盾
	private UsbKeyText usbKeyText;
	/** 提示信息 */
	private TextView tvTip;
	private TextView tvWarn;
	/** 请求result */
	private Map<String, Object> result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.blpt_newapply_title));
		viewContent = (View) LayoutInflater.from(this).inflate(
				R.layout.blpt_bill_newservice_msgconfirm, null);
		BlptUtil.getInstance().addActivity(this);
		addView(viewContent);
		// 右上角按钮赋值
		setText(this.getString(R.string.go_main));
		setRightBtnClick(rightBtnBackmainClick);
		getData();
		init();
		getSignAppConfirm(coombin);
	}

	/** 初始化 */
	private void init() {
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getString(R.string.blpt_applynewbill_step2),
						this.getString(R.string.blpt_applynewbill_step3),
						this.getString(R.string.blpt_applynewbill_step4) });
		StepTitleUtils.getInstance().setTitleStep(2);

		mainLayout = (LinearLayout) viewContent.findViewById(R.id.main_layout);		
		btnLast = (Button) viewContent.findViewById(R.id.btnLast);
		btnConfirm = (Button) viewContent.findViewById(R.id.btnconfirm);
		btnLast.setVisibility(View.GONE);
		btnLast.setOnClickListener(this);
		btnConfirm.setOnClickListener(this);
		tvTip = (TextView) viewContent.findViewById(R.id.tv_bill_tip);
		tvWarn = (TextView) viewContent.findViewById(R.id.tv_bill_warn);
	}

	/** 接受上个页面数据 **/
	private void getData() {
		Bundle b = getIntent().getBundleExtra(Blpt.KEY_BUNDLE);
		laststepNo = (String) b.get(Blpt.KEY_STEPNO);
		laststepNum = (String) b.get(Blpt.KEY_STEPNUMER);
		coombin = (String) b.get(Blpt.KEY_COMBINID);
		accNum = (String) b.get(Comm.ACCOUNTNUMBER);
		accId = (String) b.get(Comm.ACCOUNT_ID);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLast:
			finish();
			break;

		case R.id.btnconfirm:
			int index;
			List<Map<String, Object>> acctList = BlptUtil.getInstance()
					.getAcctList();
			if (!StringUtil.isNullOrEmpty(acctList)) {
				index = 1;
			} else {
				index = 0;
			}
			// 读取用户输入内容
			Map<String, String> mapfromLayout = getTextFromLayout(
					(LinearLayout) mainLayout.getChildAt(0), index);
			if (!StringUtil.isNullOrEmpty(acctList)) {
				accNum = (String) acctList.get(accCurPosition).get(
						Blpt.SIGN_PAY_ACC_NUM_RE);
				accId = (String) acctList.get(accCurPosition).get(
						Blpt.SIGN_PA_ACC_ID_RE);
			}
			BlptUtil.getInstance().setLayoutMap(mapfromLayout);
			checketDate();
			break;
		}
	}

	/**
	 * 请求预交易
	 * 
	 * @param comId
	 */
	private void getSignAppConfirm(String comId) {
		BaseHttpEngine.showProgressDialogCanGoBack();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setMethod(Blpt.METHOD_PSNPAYSSIGNAPPLYCONFIRM);
		Map<String, String> datamap = BlptUtil.getInstance().getMapData();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Blpt.NEWAPPLY_CONFIRM_METRCHANTID,
				datamap.get(Blpt.KEY_MERCHID));
		map.put(Blpt.NEWAPPLY_CONFIRM_PAYID, datamap.get(Blpt.KEY_PAYJNUM));
		map.put(Blpt.NEWAPPLY_CONFIRM_PRVNAME,
				datamap.get(Blpt.KEY_PROVICESHORTNAME));
		map.put(Blpt.NEWAPPLY_CONFIRM_STEPNO, laststepNo);
		map.put(Blpt.NEWAPPLY_RESULT_STEPNUM, laststepNum);
		map.put(Blpt.NEWAPPLY_CONFIRM_COMBINID_N, comId);
		map.put(Blpt.NEWAPPLY_CONFIRM_ACCNUMBER_DISP, accNum);
		map.put(Blpt.NEWAPPLY_CONFIRM_ACCNUMBER_REAL, accNum);
		map.put(Blpt.NEWAPPLY_CONFIRM_ACCID, accId);

		// 封装上传数据
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> exeList = (List<Map<String, Object>>) BlptUtil
				.getInstance().getApplyLastPagMap()
				.get(Blpt.NEWAPPLT_PRE_EXELIST);
		Map<String, String> layoutMap = BlptUtil.getInstance().getLayoutMap();
		putData(exeList, null, layoutMap, map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "applyConfirmCallBack");
	}

	/** 申请新服务预交易返回 **/
	@SuppressWarnings("unchecked")
	public void applyConfirmCallBack(Object resultObj) {
		result = HttpTools.getResponseResult(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(result))
			return;
		if (result.containsKey(Blpt.ERRORMSG)
				&& !StringUtil.isNull((String) result.get(Blpt.ERRORMSG))) {
			BaseDroidApp.getInstanse().showMessageDialog(
					(String) result.get(Blpt.ERRORMSG), errorClick);
			return;
		}
		Map<String, Object> map = (Map<String, Object>) result
				.get(Blpt.NEWAPPLY_CONFIRM_ENTITY_RP);
		List<Map<String, Object>> resexeList = (List<Map<String, Object>>) map
				.get(Blpt.NEWAPPLY_CONFIRM_EXELIST_RP);
		List<Map<String, Object>> acctList = (List<Map<String, Object>>) map
				.get(Blpt.NEWAPPLY_PRE_ACCLIST);
		stepNo = (String) map.get(Blpt.NEWAPPLY_PRE_STEPNO_RESPONSE);
		stepNum = (String) map.get(Blpt.NEWAPPLY_PRE_STEPNUM_RESPONSE);

		// 解析安全控件类型
		Map<String, Object> sipMap = (Map<String, Object>) result
				.get(Blpt.NEWAPPLY_CONFIRM_SIP_FAMAP);
		List<Map<String, Object>> sipList = (List<Map<String, Object>>) sipMap
				.get(Blpt.NEWAPPLY_CONFIRM_SIP_FALIST);
		// sipInit(sipList);
		initSipBox();

		// 临时存储数据
		BlptUtil.getInstance().setAcctList(acctList);
		BlptUtil.getInstance().setExeList(resexeList);
		LinearLayout childLayout = addViews(acctList, resexeList, null, false);
		mainLayout.addView(childLayout);
		tvTip.setVisibility(View.VISIBLE);
		tvTip.setText(this.getString(R.string.blpt_pay_cancer_info));
//		 if (!StringUtil.isNullOrEmpty((String) result
//		 .get(Blpt.NEWAPPLY_PRE_WARN))) {
//		 tvWarn.setVisibility(View.VISIBLE);
//		 tvWarn.setText((String) result.get(Blpt.NEWAPPLY_PRE_WARN));
//		 }
	}

	/** 跟据返回初始化加密控件 */
	// private void sipInit(List<Map<String, Object>> sipList) {
	// for (int i = 0; i < sipList.size(); i++) {
	// @SuppressWarnings("unchecked")
	// Map<String, Object> map = (Map<String, Object>) sipList.get(i).get(
	// Blpt.SIGN_PAY_CONFIRM_SIP_FIED);
	// String sipType = (String) map.get(Blpt.SIGN_PAY_CONFIRM_SIP_NAME);
	// if (sipType.equals(Comm.Otp)) {
	// isOtp = true;
	// initSipBox();
	// } else if (sipType.equals(Comm.Smc)) {
	// isSmc = true;
	// initSipSms();
	// }
	// }
	// }
	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox() {
		String mmconversationId = (String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		usbKeyText = (UsbKeyText) viewContent.findViewById(R.id.sip_usbkey);
		usbKeyText.Init(mmconversationId, BlptUtil.getInstance()
				.getRandomNumber(), result, this);
		isOtp = usbKeyText.getIsOtp();
		isSmc = usbKeyText.getIsSmc();
		initOtpSipBox();
		initSipSms();
	}

	private void initOtpSipBox() {
		if (isOtp) {
			// // 加密控件
			LinearLayout siplayout = (LinearLayout) viewContent
					.findViewById(R.id.sipbox);
			siplayout.setVisibility(View.VISIBLE);
			TextView tvReleance = (TextView) viewContent
					.findViewById(R.id.blpt_tv_relevance_phonepwd);
			tvReleance
					.setText(this.getString(R.string.active_code_regex) + "：");
			LinearLayout linearLayout = (LinearLayout) viewContent
					.findViewById(R.id.ll_sip);
			sipBox = new SipBox(this);
			sipBox.setCipherType(SystemConfig.CIPHERTYPE);
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			sipBox.setLayoutParams(param);
			sipBox.setTextColor(getResources().getColor(android.R.color.black));
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(sipBox, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, BlptUtil.getInstance().getRandomNumber(), this);
//			sipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			sipBox.setGravity(Gravity.CENTER_VERTICAL);
//			sipBox.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			sipBox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			sipBox.setId(10002);
//			sipBox.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			sipBox.setBackgroundResource(R.drawable.bg_for_edittext);
//			sipBox.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			sipBox.setSipDelegator(this);
//			sipBox.setRandomKey_S(BlptUtil.getInstance().getRandomNumber());
			linearLayout.addView(sipBox);
		}
	}

	// 初始化手机验证码
	private void initSipSms() {
		if (isSmc) {
			LinearLayout smsLayout = (LinearLayout) viewContent
					.findViewById(R.id.layout_sms);
			smsLayout.setVisibility(View.VISIBLE);
			Button btnSendSms = (Button) findViewById(R.id.smsbtn);
			SmsCodeUtils.getInstance().addSmsCodeListner(btnSendSms,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// 发送验证码到手机
							sendMSCToMobile();
						}
					});

			sipSms = (SipBox) viewContent.findViewById(R.id.sip_sms);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(sipSms, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, BlptUtil.getInstance().getRandomNumber(), this);
//			sipSms.setCipherType(SystemConfig.CIPHERTYPE);
//			sipSms.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			sipSms.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			sipSms.setId(10002);
//			sipSms.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			sipSms.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			sipSms.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			sipSms.setSipDelegator(this);
//			sipSms.setRandomKey_S(BlptUtil.getInstance().getRandomNumber());
		}
	}

	/** 提交校验 **/
	private void contentCheck() {
		String otpPassword = null;
		String smcPassword = null;
		String otpRandomNum = null;
		String smcRandomNum = null;
		// try {
		// ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		// // 短信验证码
		// if (isSmc) {
		// RegexpBean rebSms = new RegexpBean(
		// getString(R.string.acc_smc_regex), sipSms.getText()
		// .toString(), ConstantGloble.SIPSMCPSW);
		// lists.add(rebSms);
		// }
		// // 动态口令
		// if (isOtp) {
		// RegexpBean reb1 = new RegexpBean(
		// getString(R.string.active_code_regex), sipBox.getText()
		// .toString(), ConstantGloble.SIPOTPPSW);
		// lists.add(reb1);
		// }
		//
		// if (RegexpUtils.regexpDate(lists)) {
		// if (isOtp) {
		// otpPassword = sipBox.getValue().getEncryptPassword();
		// otpRandomNum = sipBox.getValue().getEncryptRandomNum();
		// }
		// if (isSmc) {
		// smcPassword = sipSms.getValue().getEncryptPassword();
		// smcRandomNum = sipSms.getValue().getEncryptRandomNum();
		// }

		// }
		// checketDate();
		Intent it = new Intent(this, BillNewApplySuccessActivity.class);
		it.putExtra(Comm.ACCOUNTNUMBER, accNum);
		it.putExtra(Comm.ACCOUNT_ID, accId);
		it.putExtra(Blpt.KEY_STEPNO, stepNo);
		it.putExtra(Blpt.KEY_STEPNUMER, stepNum);
		// it.putExtra(Blpt.KEY_OTPPASW, otpPassword);
		// it.putExtra(Blpt.KEY_OTPRANUM, otpRandomNum);
		// it.putExtra(Blpt.KEY_SMSPASW, smcPassword);
		// it.putExtra(Blpt.KEY_SMSRANUM, smcRandomNum);

		HashMap<String, Object> map = new HashMap<String, Object>();
		usbKeyText.InitUsbKeyResult(map);
		it.putExtra(Blpt.KEY_OTPPASW, map);

		startActivity(it);
		// } catch (CodeException e) {
		// BaseDroidApp.getInstanse().createDialog(null, e.getMessage());
		// LogGloble.exceptionPrint(e);
		// }
	}

	private void checketDate() {
		usbKeyText.checkDataUsbKey(sipBox, sipSms, new IUsbKeyTextSuccess() {

			@Override
			public void SuccessCallBack(String result, int errorCode) {
				// TODO Auto-generated method stub
				contentCheck();
			}
		});
	}

}
