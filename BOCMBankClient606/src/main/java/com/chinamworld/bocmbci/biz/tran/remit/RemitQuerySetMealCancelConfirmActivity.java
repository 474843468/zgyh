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
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.remit.adapters.RemitQueryDetailAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.LinearListView;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/** 解除自动续约-确认页面 */
public class RemitQuerySetMealCancelConfirmActivity extends TranBaseActivity {
	private static final String TAG = "RemitQuerySetMealCancelConfirmActivity";
	private View view = null;
	/** 签约账户 */
	private TextView tran_remit_account;
	/** 套餐属性 */
	private TextView remit_type_view;
	/** 自动续约套餐类型 */
	private TextView remit_extension_type_view;
	/** 收款付费起点金额布局 */
	private View ll_amount_layout;
	/** 收款付费起点金额 */
	private TextView tran_remit_amount_view;
	/** 收款付费起点金额 弹窗*/
	private TextView remit_amount_lable;
	/** 是否自动续约布局 */
	private View extension_flag_layout;
	/** 是否自动续约 */
	private TextView rg_extension_flag_view;
	/** 手机号码 */
	private TextView tran_remit_phone;
	/** 生效日期 */
	private TextView startDateText = null;
	/** 截止日期 */
	private TextView endDateText = null;
	/** 共享账户列表 */
	private LinearListView lv_shareAcc;
	/** 共享账户布局*/
	private LinearLayout tran_valid_account;
	private Map<String, String> paySetMealEntity;
	
	/** 汇款笔数套餐查询结果 */
	private Map<String, Object> dateMap = null;
	private Button detailButton = null;
	/** 有效共享账户 */
	private List<Map<String, String>> validAccountList = new ArrayList<Map<String, String>>();
	private RemitQueryDetailAdapter adapter = null;
	private String accountId = null;
	private String accountNumber = null;
	/** 套餐类型 */
	private String reSignType = null;
	/** 随机数 */
	private String randomNumber = null;
	/** 预交易结果----factorList */
	private List<Map<String, Object>> factorList = null;
	/** 安全因子Map */
	private Map<String, Object> securityMap = null;
	/** 标识是否有动态口令 */
	private boolean isOtp = false;
	/** 标识是否有短息验证码 */
	private boolean isSmc = false;
	/** 加密控件 */
	private SipBox otpSipBxo;
	private SipBox smcSipBxo;
	/** 中银E盾 */
	private UsbKeyText usbKeyText;
	private String otpPassword;
	private String smcPassword;
	private String otpRandomNum;
	private String smcRandomNum;
	private String commConversationId = null;
	/** 返回的有效账户列表 */
//	private List<Map<String, String>> validAccountList = new ArrayList<Map<String, String>>();

	private HashMap<String, Object> remitSetMealCancelPreMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getString(R.string.trans_remit_menu_five));
		view = mInflater.inflate(R.layout.tran_remit_search_cancel_confirm, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		toprightBtn();
		accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		remitSetMealCancelPreMap = (HashMap<String, Object>) getIntent().getSerializableExtra(
				Tran.TRAN_REMITSETMEALCANCELPRE_API);
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		if (StringUtil.isNull(accountNumber) && StringUtil.isNull(accountId)) {
			return;
		}
		dateMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TRAN_REMIT_QUERY_RESULT);
		
		if (!dateMap.containsKey(Tran.TRAN_VALIDACCOUNTLIST_RES)) {

		} else {
			// 未经修改的 账户相信
//			validAccountList = (List<Map<String, String>>) dateMap.get(Tran.TRAN_VALIDACCOUNTLIST_RES);
			// 过滤主账户  
			List<Map<String, String>> accList =  (List<Map<String, String>>) dateMap.get(Tran.TRAN_VALIDACCOUNTLIST_RES);
			for (int i = 0; i < accList.size(); i++) {
				Map<String, String> accListMap = accList.get(i);
				//S账户  M主账户  过滤主账户                                           "Y".equals(accListMap.get("shareStatus"))过滤有效无效
				if ("S".equals(accListMap.get("remitSetMealAccountType")) && "Y".equals(accListMap.get("shareStatus"))) {
					validAccountList.add(accListMap);
				}
			}
		}
		if (StringUtil.isNullOrEmpty(dateMap)) {
			return;
		}
		randomNumber = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TRAN_RANDOMNUMBER);
		securityMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TRAN_PRERESULT);
		init();
		// if (securityMap.containsKey(Tran.TRANSBOC_FACTORLIST)) {
		// factorList = (List<Map<String, Object>>)
		// securityMap.get(Tran.TRANSBOC_FACTORLIST);
		// initSipBox(factorList);
		// }
		/** 跟据返回初始化中银E盾加密控件 */
		initSipBox();
		setValue();
	}

	private void init() {
		tran_remit_account = (TextView) view.findViewById(R.id.tran_remit_account);
		remit_type_view = (TextView) view.findViewById(R.id.sp_remit_type);
		remit_extension_type_view = (TextView) view.findViewById(R.id.remit_extension_type);
//		coupon_amount_view = (TextView) view.findViewById(R.id.coupon_amount);
		ll_amount_layout = view.findViewById(R.id.ll_extension_amount);
		tran_remit_amount_view = (TextView) view.findViewById(R.id.tran_remit_amount);
		extension_flag_layout = view.findViewById(R.id.ll_extension_flag);
		rg_extension_flag_view = (TextView) view.findViewById(R.id.yes_or_no);
		tran_remit_phone = (TextView) view.findViewById(R.id.tran_remit_phone);
		startDateText = (TextView) view.findViewById(R.id.tv_remit_valuedate);
		endDateText = (TextView) view.findViewById(R.id.tv_remit_enddate);
		tran_valid_account = (LinearLayout) view.findViewById(R.id.tran_valid_account);
		remit_amount_lable = (TextView) view.findViewById(R.id.tran_remit_amount_lable);

		lv_shareAcc = (LinearListView) view.findViewById(R.id.lv_sharedAcc);
		detailButton = (Button) findViewById(R.id.trade_nextButton);
		detailButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (securityMap.containsKey(Tran.TRANSBOC_FACTORLIST)) {
					checkDate();
				} else {
					// 获取token请求
					BaseHttpEngine.showProgressDialog();
					requestPSNGetTokenId(commConversationId);
				}

			}
		});
	}

	private void setValue() {
		Map<String, String> PaySetMealEntity = (Map<String, String>) dateMap.get(Tran.TRAN_PAYSETMEALENTITY_RES);

		tran_remit_account.setText(StringUtil.getForSixForString(accountNumber));
		remit_type_view.setText(RemitSetMealProducDic.getKeyFromValue((String) PaySetMealEntity
				.get(Tran.TRAN_REMITSETMEALPRODUCPROPERTY)));
		String[] split = ((String) PaySetMealEntity.get(Tran.TRAN_SIGNTYPE_RES)).split("/");
		String amount = split[2];
		String mount = amount.substring(0, amount.lastIndexOf(".") + 3);
		String extension_type_str = String.format("%s笔/%s月-%s元", split[0], split[1], mount);
		remit_extension_type_view.setText(extension_type_str);
		// 优惠后费用
		// coupon_amount_view.setText(StringUtil.parseStringPattern(PaySetMealEntity.get(Tran.TRAN_ORIGNAMOUNT),
		// 2));
		ll_amount_layout.setVisibility(RemitSetMealProducDic.getTagFromValue((String) PaySetMealEntity
				.get(Tran.TRAN_REMITSETMEALPRODUCPROPERTY)) ? View.VISIBLE : View.GONE);
		if (RemitSetMealProducDic.getTagFromValue((String) PaySetMealEntity.get(Tran.TRAN_REMITSETMEALPRODUCPROPERTY))) {
			tran_valid_account.setVisibility(View.GONE);
			lv_shareAcc.setVisibility(View.GONE);
		}else {
			tran_valid_account.setVisibility(View.VISIBLE);
			lv_shareAcc.setVisibility(View.VISIBLE);
			RemitQueryDetailAdapter adapter = new RemitQueryDetailAdapter( this, validAccountList);
			lv_shareAcc.setAdapter(adapter);
		}
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, remit_amount_lable);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tran_remit_amount_view);
		
		tran_remit_amount_view.setText(StringUtil.parseStringPattern(PaySetMealEntity.get(Tran.TRAN_ORIGNAMOUNT), 2));
		// 续约布局根据账号是否支持续约进行显示隐藏
		extension_flag_layout.setVisibility("Y".equalsIgnoreCase(PaySetMealEntity.get("extensionFlag")) ? View.VISIBLE
				: View.GONE);
		// 用户是否选择续约
		rg_extension_flag_view.setText("Y".equalsIgnoreCase(PaySetMealEntity.get("extensionFlag")) ? "是" : "否");
		tran_remit_phone.setText(PaySetMealEntity.get("mobile"));
		startDateText.setText(PaySetMealEntity.get("startDate"));
		endDateText.setText(PaySetMealEntity.get("endDate"));

		/*RemitQueryDetailAdapter adapter = new RemitQueryDetailAdapter( this, validAccountList);
		lv_shareAcc.setAdapter(adapter);*/
	}

	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox() {
		usbKeyText = (UsbKeyText) view.findViewById(R.id.sip_usbkey);
		usbKeyText.Init(commConversationId, randomNumber, securityMap, this);
		isOtp = usbKeyText.getIsOtp();
		isSmc = usbKeyText.getIsSmc();
		initSmcSipBox();
		initOtpSipBox();
	}

	/** 跟据返回初始化加密控件 */
	// private void initSipBox(List<Map<String, Object>> sipList) {
	// for (int i = 0; i < sipList.size(); i++) {
	// Map<String, Object> map = (Map<String, Object>)
	// sipList.get(i).get(Tran.TRANSBOC_FACTORLIST_MAP_KEY);
	// String sipType = (String) map.get(Tran.TRANSBOC_FACTORLIST_KEY);
	// if (sipType.equals(Comm.Otp)) {
	// // 动态口令
	// isOtp = true;
	// initOtpSipBox();
	// } else if (sipType.equals(Comm.Smc)) {
	// // 手机验证码
	// isSmc = true;
	// initSmcSipBox();
	// }
	// }
	// // 加密控件设置随机数
	// if (otpSipBxo != null) {
	// otpSipBxo.setRandomKey_S(randomNumber);
	// }
	// if (smcSipBxo != null) {
	// smcSipBxo.setRandomKey_S(randomNumber);
	// }
	// }

	/** 初始化otp -----动态口令 */
	private void initOtpSipBox() {
		if (isOtp) {
			LinearLayout layoutSip = (LinearLayout) view.findViewById(R.id.ll_active_code);
			layoutSip.setVisibility(View.VISIBLE);
			otpSipBxo = (SipBox) view.findViewById(R.id.sipbox_active);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(otpSipBxo, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//			otpSipBxo.setCipherType(SystemConfig.CIPHERTYPE);
//			otpSipBxo.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			otpSipBxo.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			otpSipBxo.setId(10002);
//			otpSipBxo.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			otpSipBxo.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			otpSipBxo.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			otpSipBxo.setRandomKey_S(randomNumber);
//			otpSipBxo.setSipDelegator(this);
		}

	}

	/** 初始化smc---手机校验码 */
	private void initSmcSipBox() {
		if (isSmc) {
			LinearLayout layoutSmc = (LinearLayout) view.findViewById(R.id.ll_smc);
			layoutSmc.setVisibility(View.VISIBLE);
			Button btnSendSms = (Button) findViewById(R.id.smsbtn);
			SmsCodeUtils.getInstance().addSmsCodeListner(btnSendSms, new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 发送验证码请求
					sendSMSCToMobile();
				}
			});
			smcSipBxo = (SipBox) view.findViewById(R.id.sipbox_smc);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(smcSipBxo, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//			smcSipBxo.setCipherType(SystemConfig.CIPHERTYPE);
//			smcSipBxo.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			smcSipBxo.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			smcSipBxo.setId(10002);
//			smcSipBxo.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			smcSipBxo.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			smcSipBxo.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			smcSipBxo.setSipDelegator(this);
//			smcSipBxo.setRandomKey_S(randomNumber);
		}
	}

	/** 验证动态口令 */
	private void checkDate() {

		// try {
		// ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		// if (isSmc) {
		// // 短信验证码
		// RegexpBean rebSms = new RegexpBean(getString(R.string.acc_smc_regex),
		// smcSipBxo.getText().toString(),
		// ConstantGloble.SIPSMCPSW);
		// lists.add(rebSms);
		// }
		// if (isOtp) {
		// // 动态口令
		// RegexpBean rebOtp = new
		// RegexpBean(getString(R.string.active_code_regex), otpSipBxo.getText()
		// .toString(), ConstantGloble.SIPOTPPSW);
		// lists.add(rebOtp);
		// }
		// if (RegexpUtils.regexpDate(lists)) {
		// if (isOtp) {
		// otpPassword = otpSipBxo.getValue().getEncryptPassword();
		// otpRandomNum = otpSipBxo.getValue().getEncryptRandomNum();
		// }
		// if (isSmc) {
		// smcPassword = smcSipBxo.getValue().getEncryptPassword();
		// smcRandomNum = smcSipBxo.getValue().getEncryptRandomNum();
		// }
		// // 获取token请求
		// BaseHttpEngine.showProgressDialog();
		// requestPSNGetTokenId(commConversationId);
		// }
		// } catch (CodeException e) {
		// BaseDroidApp.getInstanse().createDialog(null, e.getMessage());
		// LogGloble.exceptionPrint(e);
		// }
		/** 安全工具数据校验 */
		usbKeyText.checkDataUsbKey(otpSipBxo, smcSipBxo, new IUsbKeyTextSuccess() {

			@Override
			public void SuccessCallBack(String result, int errorCode) {
				// TODO Auto-generated method stub
				// 获取token请求
				BaseHttpEngine.showProgressDialog();
				requestPSNGetTokenId(commConversationId);
			}
		});

	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String token = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
		requestRemitSetMealCancel(token);
	}

	/** 解除自动预约提交 */
	private void requestRemitSetMealCancel(String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.TRAN_REMITSETMEALCANCEL_API);
		biiRequestBody.setConversationId(commConversationId);
		//过滤主账户,过滤无效账户    toDelList  
//		List<Map<String, String>> accList = (List<Map<String, String>>) dateMap.get(Tran.TRAN_VALIDACCOUNTLIST_RES);
//		for (int i = 0; i < accList.size(); i++) {
//			Map<String, String> accListMap = accList.get(i);
//			if ("S".equals(accListMap.get("remitSetMealAccountType")) && "Y".equals(accListMap.get("shareStatus"))) {
//				validAccountList.add(accListMap);
//			}
//		}
		
		
		Map<String, Object> maps = remitSetMealCancelPreMap; 
		maps.put(Tran.TRAN_REMIT_APPPRE_SHARESTATUS_REQ, "shareStatus");
		maps.put(Tran.MANAGE_TOKEN_REQ,token);
		maps.put(Tran.MANAGE_CURCDE_REQ, "001");
		// if (isOtp) {
		// maps.put(Comm.Otp, otpPassword);
		// maps.put(Comm.Otp_Rc, otpRandomNum);
		// }
		// if (isSmc) {
		// maps.put(Comm.Smc, smcPassword);
		// maps.put(Comm.Smc_Rc, smcRandomNum);
		// }
		/** 安全工具参数获取 */
		usbKeyText.InitUsbKeyResult(maps);
		SipBoxUtils.setSipBoxParams(maps);
		biiRequestBody.setParams(maps);
		HttpManager.requestBii(biiRequestBody, this, "requestRemitSetMealCancelCallback");
	}

	/** 解除自动预约提交----回调 */
	public void requestRemitSetMealCancelCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		CustomDialog.toastInCenter(this, getString(R.string.tran_error_cancel_confirm_n));
		ActivityTaskManager.getInstance().removeAllActivity();
		Intent intent = new Intent(RemitQuerySetMealCancelConfirmActivity.this, RemitThirdMenu.class);
		startActivity(intent);
	}
}
