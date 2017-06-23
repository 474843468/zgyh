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
 * 账单缴费信息确认
 * 
 * @author panwe
 * 
 */
public class BillPaymentSignConfirmActivity extends BillPaymentBaseActivity
		implements OnClickListener {
	/** 主布局 */
	private View viewContent;
	/** 上一步按钮 */
	private Button btnLast;
	/** 下一步按钮 */
	private Button btnNext;
	/** 加密控件 **/
	private SipBox sipBox;
	private SipBox sipSms;
	/** 中银E盾 */
	private UsbKeyText usbKeyText;
	/** 第几步 */
	private String lastpagstepNo;
	private String stepNo;
	/** 总步数 **/
	private String lastpagstepNum;
	private String stepNum;
	/** 动态主布局 **/
	private LinearLayout mainLayout;
	/** 调回主界面标识 */
	private int tag;
	private String combin;
	/** 缴费账号信息 **/
	private String accNum;
	private String accId;
	/** 标识是否有短息验证码、动态口令 */
	private boolean isOtp = false;
	private boolean isSmc = false;
	/** 报文返回信息 */
	private Map<String, Object> result;
	/** 提示语 **/
	private TextView tvWarn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.blpt_bill_paypent_title));
		viewContent = (View) LayoutInflater.from(this).inflate(
				R.layout.blpt_bill_newservice_msgadd, null);
		BlptUtil.getInstance().addActivity(this);
		addView(viewContent);
		// 右上角按钮赋值
		setText(this.getString(R.string.go_main));
		setRightBtnClick(rightBtnBackmainClick);
		getData();
		init();
		// 缴费预交易
		signPayConfirm(combin);
	}

	private void init() {
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getString(R.string.blpt_signbill_step1),
						this.getString(R.string.blpt_signbill_step2),
						this.getString(R.string.blpt_signbill_step3) });
		StepTitleUtils.getInstance().setTitleStep(2);

		mainLayout = (LinearLayout) viewContent
				.findViewById(R.id.blpt_main_layout);
		TextView tvTip = (TextView) viewContent
				.findViewById(R.id.tv_bill_title);
		tvTip.setVisibility(View.VISIBLE);
		tvTip.setText(this.getString(R.string.blpt_payment_confirm_tip));
		btnNext = (Button) viewContent.findViewById(R.id.btnnext);
		btnLast = (Button) viewContent.findViewById(R.id.btnLast);
		btnNext.setText(this.getString(R.string.confirm));
		btnLast.setVisibility(View.GONE);
		btnNext.setOnClickListener(this);
		btnLast.setOnClickListener(this);
		tvWarn = (TextView) findViewById(R.id.tv_bill_tip);
	}

	/** 接受上个页面数据 **/
	private void getData() {
		Bundle b = getIntent().getBundleExtra(Blpt.KEY_BUNDLE);
		lastpagstepNo = b.getString(Blpt.KEY_STEPNO);
		lastpagstepNum = b.getString(Blpt.KEY_STEPNUMER);
		combin = b.getString(Blpt.KEY_COMBINID);
		accNum = b.getString(Comm.ACCOUNTNUMBER);
		accId = b.getString(Comm.ACCOUNT_ID);
		tag = b.getInt(Blpt.KEY_TAG);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLast:
			finish();
			break;

		case R.id.btnnext:
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
						Blpt.ACC_NUMBER);
				accId = (String) acctList.get(accCurPosition).get(
						Blpt.SIGN_PA_ACC_ID_RE);
			}
			BlptUtil.getInstance().setLayoutMap(mapfromLayout);
			checketDate();
			break;
		}
	}

	/**
	 * 缴费预交易
	 * 
	 * @param combin
	 */
	private void signPayConfirm(String combin) {
		// 开启通讯框
		BaseHttpEngine.showProgressDialogCanGoBack();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Blpt.METHOD_SIGN_PAY_CONFIRM);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> dataMap = BlptUtil.getInstance().getMapData();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Blpt.SIGN_PAY_CONFIRM_MERTHANTID, dataMap.get(Blpt.KEY_MERCHID));
		map.put(Blpt.SIGN_PAY_CONFIRM_JNUM, dataMap.get(Blpt.KEY_PAYJNUM));
		map.put(Blpt.SIGN_PAY_CONFIRM_PRVNAME,
				dataMap.get(Blpt.KEY_PROVICESHORTNAME));
		map.put(Blpt.SIGN_PAY_CONFIRM_STEPNO, lastpagstepNo);
		map.put(Blpt.SIGN_PAY_CONFIRM_STEPNUM, lastpagstepNum);
		map.put(Blpt.NEWAPPLY_CONFIRM_ACCNUMBER_DISP, accNum);
		map.put(Blpt.NEWAPPLY_CONFIRM_ACCNUMBER_REAL, accNum);
		map.put(Blpt.NEWAPPLY_CONFIRM_ACCID, accId);
		map.put(Blpt.NEWAPPLY_CONFIRM_COMBINID_N, combin);

		// 封装上传数据
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> exeList = (List<Map<String, Object>>) BlptUtil
				.getInstance().getPayLastPagMap().get(Blpt.SIGN_PAY_EXELIST_RE);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> spcList = (List<Map<String, Object>>) BlptUtil
				.getInstance().getPayLastPagMap().get(Blpt.SIGN_PAY_SPELIST_RE);
		Map<String, String> layoutMap = BlptUtil.getInstance().getLayoutMap();
		BlptUtil.setDataName(exeList, map, Blpt.SIGN_PAY_USERCODE, BlptUtil
				.getInstance().getUserCode());
		BlptUtil.setDataName(spcList, map, Blpt.SIGN_PAY_USERCODE, BlptUtil
				.getInstance().getUserCode());
		putData(exeList, spcList, layoutMap, map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "signPayConfirmCallBack");
	}

	/*** 缴费预交易返回结果 **/
	@SuppressWarnings("unchecked")
	public void signPayConfirmCallBack(Object resultObj) {
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
		Map<String, Object> entiry = (Map<String, Object>) result
				.get(Blpt.SIGN_PAY_CONFIRM_ENTIRY);
		List<Map<String, Object>> accList = (List<Map<String, Object>>) entiry
				.get(Blpt.SIGN_PAY_CONFIRM_ACC_LIST);
		List<Map<String, Object>> reexeList = (List<Map<String, Object>>) entiry
				.get(Blpt.SIGN_PAY_CONFIRM_EXELIST);
		List<Map<String, Object>> respList = (List<Map<String, Object>>) entiry
				.get(Blpt.SIGN_PAY_CONFIRM_SPLIST);

		stepNo = (String) entiry.get(Blpt.SIGN_PAY_STEPNO_RE);
		stepNum = (String) entiry.get(Blpt.SIGN_PAY_STEPNUM_RE);

//		 if (!StringUtil.isNullOrEmpty((String) entiry
//		 .get(Blpt.NEWAPPLY_PRE_WARN))) {
//		 tvWarn.setVisibility(View.VISIBLE);
//		 tvWarn.setText((String) entiry.get(Blpt.NEWAPPLY_PRE_WARN));
//		 }
		// 解析安全控件类型
		Map<String, Object> sipMap;
		if (result.containsKey(Blpt.SIGN_PAY_CONFIRM_SIP_ENTRY)) {
			sipMap = (Map<String, Object>) result
					.get(Blpt.SIGN_PAY_CONFIRM_SIP_ENTRY);
		} else {
			sipMap = (Map<String, Object>) result
					.get(Blpt.NEWAPPLY_CONFIRM_SIP_FAMAP);
		}
		List<Map<String, Object>> sipList = (List<Map<String, Object>>) sipMap
				.get(Blpt.SIGN_PAY_CONFIRM_SIP_LIST);
		// sipInit(sipList);
		initSipBox();
		// 存储临时数据
		BlptUtil.getInstance().setAcctList(accList);
		BlptUtil.getInstance().setExeList(reexeList);
		BlptUtil.getInstance().setSpList(respList);
		LinearLayout view = addViews(accList, reexeList, respList, false);
		mainLayout.addView(view);
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
			// 加密控件
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
//			sipBox.setId(10002);
//			sipBox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
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
		String otpEncryptRandomNum = null;
		String smcPassword = null;
		String smcEncryptRandomNum = null;
		// try {
		// ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		// // 手机交易码
		// if (isSmc) {
		// RegexpBean rebSms = new RegexpBean(
		// getString(R.string.acc_smc_regex), sipSms.getText()
		// .toString(), ConstantGloble.SIPSMCPSW);
		// lists.add(rebSms);
		// }
		// // 验证动态口令
		// if (isOtp) {
		// RegexpBean rebPas = new RegexpBean(
		// getString(R.string.active_code_regex), sipBox.getText()
		// .toString(), ConstantGloble.SIPOTPPSW);
		// lists.add(rebPas);
		// }
		// if (RegexpUtils.regexpDate(lists)) {
		// if (isOtp) {
		// otpPassword = sipBox.getValue().getEncryptPassword();
		// otpEncryptRandomNum = sipBox.getValue()
		// .getEncryptRandomNum();
		// }
		// if (isSmc) {
		// smcPassword = sipSms.getValue().getEncryptPassword();
		// smcEncryptRandomNum = sipSms.getValue()
		// .getEncryptRandomNum();
		// }
		//
		//
		// }
		/* 安全工具数据校验* */
		// checketDate();
		Intent it = new Intent(this, BillPaymentSignSuccessActivity.class);
		// it.putExtra(Blpt.KEY_OTPPASW, otpPassword);
		// it.putExtra(Blpt.KEY_OTPRANUM, otpEncryptRandomNum);
		// it.putExtra(Blpt.KEY_SMSPASW, smcPassword);
		// it.putExtra(Blpt.KEY_SMSRANUM, smcEncryptRandomNum);
		it.putExtra(Comm.ACCOUNTNUMBER, accNum);
		it.putExtra(Comm.ACCOUNT_ID, accId);
		it.putExtra(Blpt.KEY_STEPNO, stepNo);
		it.putExtra(Blpt.KEY_STEPNUMER, stepNum);
		it.putExtra(Blpt.KEY_TAG, tag);
		HashMap<String, Object> map = new HashMap<String, Object>();
		usbKeyText.InitUsbKeyResult(map);
		it.putExtra(Blpt.KEY_OTPPASW, map);
		startActivity(it);
		// }
		// catch (CodeException e) {
		// BaseDroidApp.getInstanse().createDialog(null, e.getMessage());
		// LogGloble.exceptionPrint(e);
		// }
	}

	/* 安全工具数据校验* */
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
