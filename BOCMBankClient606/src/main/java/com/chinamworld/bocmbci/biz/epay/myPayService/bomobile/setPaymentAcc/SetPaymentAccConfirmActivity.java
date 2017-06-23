package com.chinamworld.bocmbci.biz.epay.myPayService.bomobile.setPaymentAcc;

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

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Bond;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice.VirtualBCServiceListActivity;
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
import com.chinamworld.bocmbci.utils.PublicTools;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 电子支付 - 添加支付账户确认页面
 * 
 * @author Administrator
 * 
 */
public class SetPaymentAccConfirmActivity extends EPayBaseActivity {

	private View ePaySetPaymentAccConfirm;

	private SipBox sb_dynamic_code;
	private SipBox sb_note_code;

	private LinearLayout ll_selected_acclist;

	// private Button bt_back;
	private Button bt_smsbtn;
	private Button bt_submit;

	private LinearLayout ll_otp;
	private LinearLayout ll_smc;

	private Context bomTransContext;
	private List<Object> factorList;
	private List<Object> selectedAccIdList;
	private String tokenId;
	private List<Object> selectedAccList;

	private boolean confirmOtp;
	private boolean confirmSmc;

	private PubHttpObserver httpObserver;

	private String randomKey;

	private int excuteType;

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
		excuteType = getIntent().getIntExtra("excuteType", 0);
		bomTransContext = TransContext.getBomContext();
		httpObserver = PubHttpObserver.getInstance(this,
				PubConstants.CONTEXT_BOM);
		ePaySetPaymentAccConfirm = LayoutInflater.from(this).inflate(
				R.layout.epay_bom_spa_confirm, null);
		selectedAccIdList = new ArrayList<Object>();
		super.setType(0);
		super.setShowBackBtn(true);
		super.setTitleName(PubConstants.TITLE_BOANK_OF_MOBILE);
		super.setContentView(ePaySetPaymentAccConfirm);
		super.onCreate(savedInstanceState);

		// 初始化导航条
		// EpayPubUtil.initStepBar(this, 2, new String[] { "选择账户", "填写信息",
		// "开通成功" });
		// 初始化当前页

		if (acclistFlag) {
			getTransData();
		} else {
			getPaymentServiceAccPre();
		}

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

		ll_selected_acclist = (LinearLayout) ePaySetPaymentAccConfirm
				.findViewById(R.id.ll_selected_acclist);
		// ll_otp = (LinearLayout) ePaySetPaymentAccConfirm
		// .findViewById(R.id.ll_otp);
		// ll_smc = (LinearLayout) ePaySetPaymentAccConfirm
		// .findViewById(R.id.ll_smc);

		usbKeytext = (UsbKeyText) ePaySetPaymentAccConfirm
				.findViewById(R.id.sip_usbkey);
		usbKeytext.Init(commConversationId, randomKey, securityMap, this);
		isOtp = usbKeytext.getIsOtp();
		isSmc = usbKeytext.getIsSmc();

		if (isOtp) {
			ll_otp = (LinearLayout) ePaySetPaymentAccConfirm.findViewById(R.id.ll_otp);
			ll_otp.setVisibility(View.VISIBLE);
			sb_dynamic_code = (SipBox) ePaySetPaymentAccConfirm.findViewById(R.id.sb_dynamic_code);
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
		}

		if (isSmc) {
			ll_smc = (LinearLayout) ePaySetPaymentAccConfirm
					.findViewById(R.id.ll_smc);
			ll_smc.setVisibility(View.VISIBLE);
			sb_note_code = (SipBox) ePaySetPaymentAccConfirm.findViewById(R.id.sb_note_code);
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

			// bt_back =
			// (Button)ePaySetPaymentAccConfirm.findViewById(R.id.bt_back);
			// bt_back.setOnClickListener(new View.OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// SetPaymentAccConfirmActivity.this.finish();
			// }
			// });

			bt_smsbtn = (Button) ePaySetPaymentAccConfirm
					.findViewById(R.id.bt_smsbtn);
			SmsCodeUtils.getInstance().addSmsCodeListner(bt_smsbtn,
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							// 发送手机交易码的请求
							sendSMSCToMobile();
						}
					});
		}

		bt_submit = (Button) ePaySetPaymentAccConfirm
				.findViewById(R.id.bt_submit);
		bt_submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//
				// if (!checkSubmitData()) {
				//
				// return;
				// }

				// 获取token请求
				// BaseHttpEngine.showProgressDialog();
				// httpObserver.req_getToken("getTokenCallback");
				//
				if (securityMap.containsKey(Bond.BUY_CONFIRM_FACTORLIST)) {
					checkDate();
				}

			}

		});

		initDisplay();
	}

	// private boolean checkSubmitData() {
	// boolean isSuccess = true;
	//
	// RegexpBean rbOtp = new RegexpBean(getResources().getString(
	// R.string.active_code_regex), EpayUtil.getString(
	// sb_dynamic_code.getText(), ""), "otp");
	// RegexpBean rbSms = new RegexpBean(getResources().getString(
	// R.string.set_smc_no), EpayUtil.getString(
	// sb_note_code.getText(), ""), "smc");
	// ArrayList<RegexpBean> list = new ArrayList<RegexpBean>();
	//
	// // if (confirmOtp) {
	// // list.add(rbOtp);
	// // }
	// if (confirmSmc) {
	// list.add(rbSms);
	// }
	// if (confirmOtp) {
	// list.add(rbOtp);
	// }
	//
	// if (RegexpUtils.regexpDate(list)) {
	// if (confirmOtp) {
	// try {
	// otp = sb_dynamic_code.getValue().getEncryptPassword();
	// otpRC = sb_dynamic_code.getValue().getEncryptRandomNum();
	// } catch (CodeException e) {
	// BaseDroidApp.getInstanse().showInfoMessageDialog(
	// e.getMessage());
	// isSuccess = false;
	// }
	// }
	// if (confirmSmc) {
	// try {
	// smc = sb_note_code.getValue().getEncryptPassword();
	// smcRC = sb_note_code.getValue().getEncryptRandomNum();
	// } catch (CodeException e) {
	// BaseDroidApp.getInstanse().showInfoMessageDialog(
	// e.getMessage());
	// isSuccess = false;
	// }
	// }
	// } else {
	// isSuccess = false;
	// }
	//
	// return isSuccess;
	// }

	private void initDisplay() {

		if (acclistFlag) {
			for (int i = 0; i < selectedAccList.size(); i++) {
				Map<Object, Object> temp = EpayUtil.getMap(selectedAccList
						.get(i));
				View view = LayoutInflater.from(this).inflate(
						R.layout.epay_bom_selected_acc_list_item, null);
				TextView tv_acc_number = (TextView) view
						.findViewById(R.id.item_acc_number);
				tv_acc_number
						.setText(StringUtil.getForSixForString(EpayUtil.getString(
								temp.get

								(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_NUMBER),
								"")));
				String accountId = EpayUtil.getString(temp.get

				(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_ID), "");
				selectedAccIdList.add(accountId);
				ll_selected_acclist.addView(view);
			}
		} else {
			// 虚拟卡
			String virtualNo = String
					.valueOf(VirtualBCServiceWriteConfirmActivity.vcardinfo
							.get(Crcd.CRCD_VIRTUALCARDNO));
			selectedAccIdList
					.add(VirtualBCServiceWriteConfirmActivity.vcardResultList
							.get(Crcd.CRCD_VIRTUALCARDID));
			View view = LayoutInflater.from(this).inflate(
					R.layout.epay_bom_selected_acc_list_item, null);
			TextView tv_acc_number = (TextView) view
					.findViewById(R.id.item_acc_number);
			tv_acc_number.setText(StringUtil.getForSixForString(virtualNo));
			ll_selected_acclist.addView(view);
		}

		// for (Object obj : factorList) {
		// String confirmType = EpayUtil.getString(obj, "");
		// if (PubConstants.PUB_FIELD_OTP.equals(confirmType)) {
		// ll_otp.setVisibility(View.VISIBLE);
		// confirmOtp = true;
		// }
		// if (PubConstants.PUB_FIELD_SMC.equals(confirmType)) {
		// ll_smc.setVisibility(View.VISIBLE);
		// confirmSmc = true;
		// }
		// }
		BiiHttpEngine.dissMissProgressDialog();
	}

	public void getTokenCallback(Object resultObj) throws Exception {
		tokenId = EpayUtil.getString(httpObserver.getResult(resultObj), "");

		Map<String, Object> params = new HashMap<String, Object>();
		// if (confirmOtp) {
		// params.put(PubConstants.PUB_FIELD_OTP, otp);
		// params.put(PubConstants.PUB_FIELD_OTP_RC, otpRC);
		// }
		// if (confirmSmc) {
		// params.put(PubConstants.PUB_FIELD_SMC, smc);
		// params.put(PubConstants.PUB_FIELD_SMC_RC, smcRC);
		// }
		params.put(PubConstants.PUB_FIELD_TOKEN_ID, tokenId);
		params.put(
				BomConstants.METHOD_SET_PAYMENT_SERVICE_ACC_FIELD_CHECK_ACCTS,
				selectedAccIdList);
		// TODO
		/** 安全工具参数获取 */
		usbKeytext.InitUsbKeyResult(params);

		SipBoxUtils.setSipBoxParams(params);
		httpObserver
				.req_setBomPaymentServiceAcc(params, "setPaymentServiceAcc");
	}

	public void setPaymentServiceAcc(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent(this, SetPaymentAccResultActivity.class);
		intent.putExtra("acclistFlag", acclistFlag);
		startActivityForResult(intent, 0);
	}

	/**
	 * 交易数据处理
	 */
	private void getTransData() {
		// 如果是电子支付
		excuteType = PublicTools.getInt(bomTransContext.getData("excuteType"), 0);
		factorList = bomTransContext.getList(PubConstants.PUB_FIELD_FACTORLIST);
		// selectedAccIdList = new ArrayList<String>();
		selectedAccList = bomTransContext
				.getList(PubConstants.CONTEXT_FIELD_SELECTED_ACCLIST);
		// for (Object obj : selectedAccList) {
		// Map<Object, Object> map = EpayUtil.getMap(obj);
		// String accoutnId =
		// EpayUtil.getString(map.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_ID),
		// "");
		// selectedAccIdList.add(accoutnId);
		// }
		BiiHttpEngine.showProgressDialogCanGoBack();
		// 请求加密随机数
		httpObserver.req_randomKey("getRandomKeyCallback");
	}

	// 虚拟卡需要的数据
	private void getPaymentServiceAccPre() {
		// 如果是虚拟卡
		List<Object> selectedAccList = new ArrayList<Object>();
		selectedAccList.add(VirtualBCServiceListActivity.getBankSetupMap());

		TransContext.getBomContext().setData("excuteType", excuteType);
		TransContext.getBomContext().setData(
				PubConstants.CONTEXT_FIELD_SELECTED_ACCLIST, selectedAccList);
		BaseHttpEngine.showProgressDialogCanGoBack();
		requestCommConversationId();
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		String conversationId = EpayUtil.getString(BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID), "");
		httpObserver.setConversationId(conversationId);
		bomTransContext.setData(PubConstants.PUB_FIELD_CONVERSATION_ID,
				conversationId);
		requestGetSecurityFactor("PB200C2");
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		final BaseDroidApp bdApp = BaseDroidApp.getInstanse();
		bdApp.showSeurityChooseDialog(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String combinId = bdApp.getSecurityChoosed();
				Map<Object, Object> params = new HashMap<Object, Object>();
				params.put(PubConstants.PUB_FIELD_COMBIN_ID, combinId);
				httpObserver.req_setBomPaymentServiceAccPre(params,
						"setPaymentServiceAccPre");
			}
		});

	}

	/**
	 * 设置支付账户预交易
	 * 
	 * @param resultObj
	 */
	public void setPaymentServiceAccPre(Object resultObj) {

		Object result = httpObserver.getResult(resultObj);
		Map<Object, Object> resultMap = EpayUtil.getMap(result);

		// TODO 安全因子
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.ISFOREX_PRERESULT_KEY, resultMap);

		List<Object> factorList = EpayUtil.getFactorList(resultMap);

		bomTransContext.setData(PubConstants.PUB_FIELD_FACTORLIST, factorList);
		// for (int i = 0; i <
		// bomTransContext.getList(PubConstants.CONTEXT_FIELD_ALL_ACCLIST).size();
		// i++) {
		// Map<Object, Object> accountTemp =
		// EpayUtil.getMap(bomTransContext.getList(PubConstants.CONTEXT_FIELD_ALL_ACCLIST).get(i));
		// accountTemp.put(PubConstants.PUB_FIELD_ISSELECTED, false);
		// }

		getTransData();
	}

	@Override
	public void finish() {
		if (excuteType == 2) // 信用卡流程
			setResult(RESULT_OK);
		super.finish();
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

	}
}
