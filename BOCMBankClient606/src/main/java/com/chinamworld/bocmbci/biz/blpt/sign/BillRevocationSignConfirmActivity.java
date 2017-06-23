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

/***
 * 账单缴费取消信息确认
 * 
 * @author panwe
 * 
 */
public class BillRevocationSignConfirmActivity extends BillPaymentBaseActivity
		implements OnClickListener {
	/** 主布局 */
	private View viewContent;
	/** 取消按钮 */
	private Button btnCancer;
	/** 确认按钮 */
	private Button btnConfirm;
	/** 加密控件 **/
	private SipBox sipBox;
	private SipBox sipSms;
	/** 中银E盾 */
	private UsbKeyText usbKeyText;
	/** 第几步 */
	private String laststepNo;
	private String stepNo;
	/** 总步数 **/
	private String laststepNum;
	private String stepNum;
	/** 动态主布局 **/
	private LinearLayout mainLayout;
	/** 缴费账号信息 **/
	private String accNum;
	private String accId;
	/** 安全因子 **/
	private String combin;
	/** 标识是否有短息验证码、动态口令 */
	private boolean isOtp = false;
	private boolean isSmc = false;
	/** 提示信息 */
	private TextView tvTip;
	private TextView tvWarn;
	/** 报文返回 */
	private Map<String, Object> result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.blpt_bill_cance));
		viewContent = (View) LayoutInflater.from(this).inflate(
				R.layout.blpt_bill_newservice_msgadd, null);
		BlptUtil.getInstance().addActivity(this);
		addView(viewContent);
		// 右上角按钮赋值
		setText(this.getString(R.string.go_main));
		setRightBtnClick(rightBtnBackmainClick);
		getData();
		init();
		signCancerConfirm(combin);
	}

	private void init() {
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getString(R.string.blpt_cancer_add),
						this.getString(R.string.blpt_cancer_confirm),
						this.getString(R.string.blpt_cancer_su) });
		StepTitleUtils.getInstance().setTitleStep(2);

		mainLayout = (LinearLayout) findViewById(R.id.blpt_main_layout);
		tvTip = (TextView) viewContent.findViewById(R.id.tv_bill_title);
		tvWarn = (TextView) findViewById(R.id.tv_bill_tip);
		btnCancer = (Button) findViewById(R.id.btnLast);
		btnConfirm = (Button) findViewById(R.id.btnnext);
		btnConfirm.setText(this.getString(R.string.confirm));
		btnCancer.setVisibility(View.GONE);
		btnConfirm.setOnClickListener(this);
		btnCancer.setOnClickListener(this);

	}

	/** 接受上个页面数据 **/
	private void getData() {
		Bundle b = getIntent().getBundleExtra(Blpt.KEY_BUNDLE);
		laststepNo = b.getString(Blpt.KEY_STEPNO);
		laststepNum = b.getString(Blpt.KEY_STEPNUMER);
		combin = b.getString(Blpt.KEY_COMBINID);
		accNum = b.getString(Comm.ACCOUNTNUMBER);
		accId = b.getString(Comm.ACCOUNT_ID);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLast:

			break;
		case R.id.btnnext:
			// 读取用户输入内容
			int index;
			List<Map<String, Object>> acctList = BlptUtil.getInstance()
					.getAcctList();
			if (!StringUtil.isNullOrEmpty(acctList)) {
				index = 1;
			} else {
				index = 0;
			}
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
	 * 撤销预交易
	 * 
	 * @param combin
	 */
	private void signCancerConfirm(String combin) {
		// 开启通讯框
		BaseHttpEngine.showProgressDialogCanGoBack();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setMethod(Blpt.METHOD_CANCER_CONFIRM);
		Map<String, String> dataMap = BlptUtil.getInstance().getMapData();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Blpt.CANCER_CONFIRM_MERCHANTID, dataMap.get(Blpt.KEY_MERCHID));
		map.put(Blpt.CANCER_CONFIRM_JNUM, dataMap.get(Blpt.KEY_PAYJNUM));
		map.put(Blpt.CANCER_CONFIRM_PRV, dataMap.get(Blpt.KEY_PROVICESHORTNAME));
		map.put(Blpt.BILL_DISNAME_CA, dataMap.get(Blpt.KEY_PAYEENAME));
		map.put(Blpt.CANCER_CONFIRM_STEPNO, laststepNo);
		map.put(Blpt.CANCER_STEPNUM, laststepNum);
		map.put(Blpt.NEWAPPLY_CONFIRM_COMBINID_N, combin);
		map.put(Blpt.NEWAPPLY_CONFIRM_ACCNUMBER_DISP, accNum);
		map.put(Blpt.NEWAPPLY_CONFIRM_ACCNUMBER_REAL, accNum);
		map.put(Blpt.NEWAPPLY_CONFIRM_ACCID, accId);

		// 封装上传数据
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> exeList = (List<Map<String, Object>>) BlptUtil
				.getInstance().getCancePayLastPagMap()
				.get(Blpt.CANCER_RE_EXECLIST);
		Map<String, String> layoutMap = BlptUtil.getInstance().getLayoutMap();
		BlptUtil.setDataName(exeList, map, Blpt.SIGN_PAY_USERCODE, BlptUtil
				.getInstance().getUserCode());
		putData(exeList, null, layoutMap, map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"signPayCancerConfirmCallBack");
	}

	/** 撤销预交易返回处理 **/
	@SuppressWarnings("unchecked")
	public void signPayCancerConfirmCallBack(Object resultObj) {
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
				.get(Blpt.CANCER_CONFIRM_ENTITY);
		List<Map<String, Object>> reexeList = (List<Map<String, Object>>) map
				.get(Blpt.CANCER_CONFIRM_EXELIST);
		List<Map<String, Object>> acctList = (List<Map<String, Object>>) result
				.get(Blpt.NEWAPPLY_PRE_ACCLIST);

		stepNo = (String) map.get(Blpt.CANCER_RE_STRPNO);
		stepNum = (String) map.get(Blpt.CANCER_RE_STEPNUM);

		// 解析安全控件类型
		Map<String, Object> sipMap = (Map<String, Object>) result
				.get(Blpt.NEWAPPLY_CONFIRM_SIP_FAMAP);
		List<Map<String, Object>> sipList = (List<Map<String, Object>>) sipMap
				.get(Blpt.SIGN_PAY_CONFIRM_SIP_LIST);
		// sipInit(sipList);
		/** 跟据返回初始化中银E盾加密控件 */
		initSipBox();
		// 存储临时数据
		BlptUtil.getInstance().setAcctList(acctList);
		BlptUtil.getInstance().setExeList(reexeList);
		LinearLayout childLayout = addViews(acctList, reexeList, null, false);
		mainLayout.addView(childLayout);

		tvTip.setVisibility(View.VISIBLE);
		tvTip.setText(this.getString(R.string.blpt_cance_payment_confirm_tip));
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
			LinearLayout sipLayout = (LinearLayout) viewContent
					.findViewById(R.id.sipbox);
			sipLayout.setVisibility(View.VISIBLE);
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
		String otppassword = null;
		String otprandomNum = null;
		String smcpassword = null;
		String smcrandomNum = null;
		// try {
		// ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		// // 手机交易码
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
		// if (RegexpUtils.regexpDate(lists)) {
		// if (isOtp) {
		// otppassword = sipBox.getValue().getEncryptPassword();
		// otprandomNum = sipBox.getValue().getEncryptRandomNum();
		// }
		// if (isSmc) {
		// smcpassword = sipSms.getValue().getEncryptPassword();
		// smcrandomNum = sipSms.getValue().getEncryptRandomNum();
		// }
		//
		// }
		//
		// } catch (CodeException e) {
		// BaseDroidApp.getInstanse().createDialog(null, e.getMessage());
		// LogGloble.exceptionPrint(e);
		// }
		/** 安全工具校验 */
		// checketDate();
		Intent it = new Intent(this, BillRevocationSignSuccessActivity.class);
		Bundle b = new Bundle();
		// b.putString(Blpt.KEY_OTPPASW, otppassword);
		// b.putString(Blpt.KEY_OTPRANUM, otprandomNum);
		// b.putString(Blpt.KEY_SMSPASW, smcpassword);
		// b.putString(Blpt.KEY_SMSRANUM, smcrandomNum);
		b.putString(Comm.ACCOUNTNUMBER, accNum);
		b.putString(Comm.ACCOUNT_ID, accId);
		b.putString(Blpt.KEY_STEPNO, stepNo);
		b.putString(Blpt.KEY_STEPNUMER, stepNum);
		it.putExtra(Blpt.KEY_BUNDLE, b);
		HashMap<String, Object> map = new HashMap<String, Object>();
		usbKeyText.InitUsbKeyResult(map);
		it.putExtra(Blpt.KEY_OTPPASW, map);
		startActivity(it);
	}

	/** 安全工具校验 */
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
