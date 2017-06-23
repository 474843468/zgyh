package com.chinamworld.bocmbci.biz.tran.remit;

import java.util.ArrayList;
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
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.remit.adapters.RemitSharedAccConfirmAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.Dictionary;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.LinearListView;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 套餐签约确认信息页面
 * 
 * @author wangmengmeng
 * 
 */
public class RemitSetMealConfirmActivity extends TranBaseActivity {
	/** 确认信息页面 */
	private View view;
	/** 签约账户 */
	private TextView tran_remit_account;
	/** 套餐属性 */
	private TextView remit_type_view;
	/** 自动续约套餐类型 */
	private TextView remit_extension_type_view;
	/** 优惠后费用 */
	private TextView coupon_amount_view;
	/** 收款付费起点金额布局 */
	private View ll_amount_layout;
	/** 收款付费起点金额 */
	private TextView tran_remit_amount_view;
	/** 是否自动续约布局 */
	private View extension_flag_layout;
	/** 是否自动续约 */
	private TextView rg_extension_flag_view;
	/** 手机号码 */
	private TextView tran_remit_phone;
	/** 生效日期 */
	private TextView tv_remit_valuedate;
	/** 共享账户列表 */
	private LinearListView lv_shareAcc;
	/** 下一步 */
	private Button nextButton;
	/** 加密控件——动态口令 */
	private SipBox sipBoxActiveCode = null;
	/** 加密控件——手机交易码 */
	private SipBox sipBoxSmc = null;
	/** 中银E盾 */
	private UsbKeyText usbKeyText;
	/** 手机交易码 */
	private LinearLayout ll_smc;
	/** 动态口令 */
	private LinearLayout ll_active_code;

	/** 汇款笔数套餐签约预交易返回数据 */
	private Map<String, Object> remitPreMap;
	/** 共享账户 */
	private Map<String, Object> remitinputMap;
	/** 选择的签约账户 */
	private Map<String, Object> chooseMap;
	/** 优惠后费用 */
	private String afterThePreferentialFee;
	/** 套餐类型 */
	private Dictionary<String, String, Map<String, Object>> remitSetMealTypeResDic;
	/** 共享账户 */
	private List<Map<String, String>> shareAccList = new ArrayList<Map<String, String>>();
	/** 加密控件里的随机数 */
	protected String randomNumber;
	/** 安全因子数组 */
	public List<Map<String, Object>> factorList;
	/** 是否上传短信验证码 */
	private boolean isSms = false;
	/** 是否上传动态口令 */
	private boolean isOtp = false;
	/** 加密之后加密随机数 */
	private String otp_password_RC = "";
	private String smc_password_RC = "";
	/** 动态口令 */
	private String otpStr;
	/** 手机交易码 */
	private String smcStr;
	/** 是否上传共享账户 */
	private boolean isShowShareAccount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.trans_remit_menu_one));
		toprightBtn();
		// 添加布局
		view = addView(R.layout.tran_remit_setmeal_confirm);
		afterThePreferentialFee = getIntent().getStringExtra(Tran.AfterThePreferentialFee_preCommissionCharge);
		isShowShareAccount = getIntent().getBooleanExtra("isShowShareAccount", false);
		shareAccList = TranDataCenter.getInstance().getShareAccountList();
		chooseMap = TranDataCenter.getInstance().getAtmChooseMap();
		remitPreMap = TranDataCenter.getInstance().getRemitPreMap();
		remitinputMap = TranDataCenter.getInstance().getShareInputMap();
		remitSetMealTypeResDic = TranDataCenter.getInstance().getMealTypeResDic();
		// 请求随机数

		BiiHttpEngine.showProgressDialogCanGoBack();
		requestRandomNumber();
	}

	public void requestRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestRandomNumberCallBack");
	}

	public void requestRandomNumberCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String randomNumber = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(randomNumber)) {
			return;
		}
		BiiHttpEngine.dissMissProgressDialog();
		this.randomNumber = randomNumber;
		init();
	}

	/** 初始化界面 */
	private void init() {
		factorList = (List<Map<String, Object>>) remitPreMap.get(Acc.LOSSCONFIRM_ACC_FACTORLIST_RES);

		tran_remit_account = (TextView) view.findViewById(R.id.tran_remit_account);
		remit_type_view = (TextView) view.findViewById(R.id.sp_remit_type);
		remit_extension_type_view = (TextView) view.findViewById(R.id.tv_remit_config_attribute);
		coupon_amount_view = (TextView) view.findViewById(R.id.coupon_amount);
		ll_amount_layout = view.findViewById(R.id.ll_extension_amount);
		tran_remit_amount_view = (TextView) view.findViewById(R.id.tran_remit_amount);
		extension_flag_layout = view.findViewById(R.id.ll_extension_flag);
		rg_extension_flag_view = (TextView) view.findViewById(R.id.yes_or_no);
		tran_remit_phone = (TextView) view.findViewById(R.id.tran_remit_phone);
		tv_remit_valuedate = (TextView) view.findViewById(R.id.tv_remit_valuedate);
		lv_shareAcc = (LinearListView) view.findViewById(R.id.lv_sharedAcc);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) findViewById(R.id.tran_remit_amount_lable));
		nextButton = (Button) view.findViewById(R.id.remit_input_next_btn);

		// 赋值
		String account = (String) chooseMap.get(Acc.ACC_ACCOUNTNUMBER_RES);
		tran_remit_account.setText(StringUtil.getForSixForString(account));
		remit_type_view.setText(RemitSetMealProducDic.getKeyFromValue((String) remitinputMap
				.get(Tran.TRAN_REMIT_APP_REMITSETMEALPRODUCPROPERTY_REQ)));
		String extension_type_str = remitSetMealTypeResDic.getKeyFromValue((String) remitinputMap
				.get(Tran.TRAN_REMIT_APP_REMITSETMEALPRODUCTTYPE_REQ));
		remit_extension_type_view.setText(extension_type_str);
		if (remitinputMap.get(Tran.TRAN_REMIT_APPPRE_ORIGNAMOUNT_REQ) != null
				&& !remitinputMap.get(Tran.TRAN_REMIT_APPPRE_ORIGNAMOUNT_REQ).equals("0")) {
			tran_remit_amount_view.setText(StringUtil.parseStringPattern(
					(String) remitinputMap.get(Tran.TRAN_REMIT_APPPRE_ORIGNAMOUNT_REQ), 2));
		} else {
			tran_remit_amount_view.setText("0.00");
		}

		// 续约布局根据账号是否支持续约进行显示隐藏
		Map<String, Object> tag2 = (Map<String, Object>) remitSetMealTypeResDic.getTagFromValue((String) remitinputMap
				.get(Tran.TRAN_REMIT_APP_REMITSETMEALPRODUCTTYPE_REQ));
		String flag = (String) tag2.get(Tran.MealTypeQuery_remitSetMealautoFlag);
		extension_flag_layout.setVisibility("Y".equalsIgnoreCase(flag) ? View.VISIBLE : View.GONE);
		// 用户是否选择续约
		rg_extension_flag_view
				.setText(extensionTypeFlag.get((String) remitinputMap.get(Tran.TRAN_REMIT_APP_EXTENSIONFLAG_REQ)));
		tran_remit_phone.setText((String) remitinputMap.get(Tran.TRAN_REMIT_APP_PHONENUMBER_REQ));
		tv_remit_valuedate.setText((String) remitinputMap.get(Tran.TRAN_REMIT_APP_EFFECTTIVEDATE_REQ));

		/** 优惠后的费用 */
		if (!StringUtil.isNull(afterThePreferentialFee)) {
			coupon_amount_view.setText(StringUtil.parseStringPattern(afterThePreferentialFee, 2));		
		}else{
			coupon_amount_view.setText("0.00");
		}

		/** 收款付费起点金额布局 */
		ll_amount_layout.setVisibility(RemitSetMealProducDic.getTagFromValue((String) remitinputMap
				.get(Tran.TRAN_REMIT_APP_REMITSETMEALPRODUCPROPERTY_REQ)) ? View.VISIBLE : View.GONE);

		LinearLayout ll_sharedAcc = (LinearLayout) view.findViewById(R.id.ll_sharedAcc);
		/** 共享账户列表 (根据返回结果反显 付款方套餐 并且不等于空显示 收款方套餐和双向套餐则不显示) */
		if (RemitSetMealProducDic.getTagFromValue((String) remitinputMap
				.get(Tran.TRAN_REMIT_APP_REMITSETMEALPRODUCPROPERTY_REQ))) {
			ll_sharedAcc.setVisibility(View.GONE);
		} else {
			if (!StringUtil.isNullOrEmpty(shareAccList) && isShowShareAccount) {
				RemitSharedAccConfirmAdapter adapter = new RemitSharedAccConfirmAdapter(RemitSetMealConfirmActivity.this,
						shareAccList);
				lv_shareAcc.setAdapter(adapter);
				ll_sharedAcc.setVisibility(View.VISIBLE);
			} else {
				ll_sharedAcc.setVisibility(View.GONE);
			}
		}
		nextButton.setOnClickListener(nextListener);
		initSipBox(); // 跟据返回初始化中银E盾加密控件
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
			// 手机交易码
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
					sendSMSCToMobile();
				}
			});
		}
	}

	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox() {
		usbKeyText = (UsbKeyText) view.findViewById(R.id.sip_usbkey);
		String mmconversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		usbKeyText.Init(mmconversationId, randomNumber, remitPreMap, this);
		isOtp = usbKeyText.getIsOtp();
		isSms = usbKeyText.getIsSmc();
	}

	OnClickListener nextListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			
			/** 安全工具数据校验 */
			usbKeyText.checkDataUsbKey(sipBoxActiveCode, sipBoxSmc, new IUsbKeyTextSuccess() {

				@Override
				public void SuccessCallBack(String result, int errorCode) {
					// Auto-generated method stub
					BiiHttpEngine.showProgressDialog();
					requestPSNGetTokenId((String) BaseDroidApp.getInstanse().getBizDataMap()
							.get(ConstantGloble.CONVERSATION_ID));
				}
			});

		}
	};

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		requestSetMeal();
	}

	/**
	 * 请求签约交易
	 */
	public void requestSetMeal() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.REMITSETMEALAPPLY_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap = remitinputMap;
		paramsmap.put(Tran.TRAN_ATM_SUB_SIGNEDDATA_REQ, remitPreMap.get(Tran.TRAN_ATM_PLAINDATA_RES));
		paramsmap.put(Tran.TRAN_ATM_SUB_TOKEN_REQ, BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID));

		/** 安全工具参数获取 */
		usbKeyText.InitUsbKeyResult(paramsmap);
		SipBoxUtils.setSipBoxParams(paramsmap);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestSetMealCallBack");
	}

	/**
	 * 请求签约交易回调
	 * 
	 * @param resultObj
	 */
	public void requestSetMealCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BiiHttpEngine.dissMissProgressDialog();
		// String result = (String) biiResponseBody.getResult();

		// 进入成功页面
		Intent intent = new Intent(this, RemitSetMealSuccessActivity.class);
		// intent.putExtra(Tran.TRAN_REMIT_APP_FINALCOMMISSIONCHARGE_RES,
		// result);
		intent.putExtra(Tran.AfterThePreferentialFee_preCommissionCharge, afterThePreferentialFee);
		intent.putExtra(Tran.AfterThePreferentialFee_isShowShareAccount, isShowShareAccount);
		startActivity(intent);
	}
}
