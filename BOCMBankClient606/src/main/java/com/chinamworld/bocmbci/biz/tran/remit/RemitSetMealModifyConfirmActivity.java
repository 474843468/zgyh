package com.chinamworld.bocmbci.biz.tran.remit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.remit.adapters.RemitModifyConfirmAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.LinearListView;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 套餐签约修改确认信息页面
 * 
 * @author wangmengmeng
 * 
 */
public class RemitSetMealModifyConfirmActivity extends TranBaseActivity {
	/** 修改确认信息页面 */
	private View view;
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
	/** 是否自动续约布局 */
	private View extension_flag_layout;
	/** 是否自动续约 */
	private TextView rg_extension_flag_view;
	/** 手机号码 */
	private TextView tran_remit_phone;
	/** 生效日期 */
	private TextView startDateText;
	/** 截止日期 */
	private TextView endDateText;
	/** 共享待解除账户列表 */
	private LinearListView lv_deleteshareAcc;
	/** 共享待添加账户列表 */
	private LinearListView lv_addshareAcc;
	/** 下一步 */
	private Button nextButton;
	/** 上送账户列表 */
	private List<Map<String, String>> toDelList = new ArrayList<Map<String, String>>();
	/** 选择的签约账户 */
	private Map<String, Object> chooseMap;
	/** 查询结果 */
	private Map<String, Object> queryMap;
	/** 查询结果中的基本信息 */
	private Map<String, String> entity = new HashMap<String, String>();
	/** 预交易信息 */
	private Map<String, Object> remitPreMap;
	/** 手机号 */
	private String phoneNumberParam;
	/** 收款付费起点金额 */
	private String orignAmountParam;
	/** 是否续约 */
	private String extensionFlagParam;
	// /** 是否续约*/
	// private String shareStatusTypeParam;
	/** 加密控件——动态口令 */
	private SipBox sipBoxActiveCode = null;
	/** 加密控件——手机交易码 */
	private SipBox sipBoxSmc = null;
	/** 加密控件里的随机数 */
	protected String randomNumber;
	/** 中银E盾 */
	private UsbKeyText usbKeyText;
	/** 手机交易码 */
	private LinearLayout ll_smc;
	/** 动态口令 */
	private LinearLayout ll_active_code;
	/** 安全因子数组 */
	public List<Map<String, Object>> factorList;
	/** 是否上传短信验证码 */
	private boolean isSms = false;
	/** 是否上传动态口令 */
	private boolean isOtp = false;

	// /** 套餐起点金额币种 */
	// private String curCde;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.trans_remit_menu_two));
		toprightBtn();
		// 添加布局
		view = addView(R.layout.tran_remit_setmeal_modify_confirm);
		chooseMap = TranDataCenter.getInstance().getAtmChooseMap();
		queryMap = TranDataCenter.getInstance().getShareQueryMap();
		remitPreMap = TranDataCenter.getInstance().getAtmpremap();
		entity = (Map<String, String>) queryMap.get(Tran.TRAN_PAYSETMEALENTITY_RES);

		if (getIntent().getSerializableExtra("toDelList") != null) {
			toDelList = (List<Map<String, String>>) getIntent().getSerializableExtra("toDelList");
		}

		phoneNumberParam = this.getIntent().getStringExtra("phoneNumber");
		orignAmountParam = this.getIntent().getStringExtra("orignAmount");
		extensionFlagParam = this.getIntent().getStringExtra("extensionFlagParam");
		// shareStatusTypeParam =
		// this.getIntent().getStringExtra("shareStatusType");
		
		// modify luqp 2015年11月19日15:37:55
		// 请求随机数
		BiiHttpEngine.showProgressDialogCanGoBack();
		requestRandomNumber();
	}

	/** 请求随机数*/ // modify luqp 2015年11月19日15:37:55
	public void requestRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestRandomNumberCallBack");
	}

	/** 请求随机数回调*/ // modify luqp 2015年11月19日15:37:55
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
		// 初始化界面
		init();
	}
	
	/** 初始化界面 */
	private void init() {
		// modify luqp 2015年11月19日15:37:55
		factorList = (List<Map<String, Object>>) remitPreMap.get(Acc.LOSSCONFIRM_ACC_FACTORLIST_RES);
		
		tran_remit_account = (TextView) view.findViewById(R.id.tran_remit_account);
		remit_type_view = (TextView) view.findViewById(R.id.sp_remit_type);
		remit_extension_type_view = (TextView) view.findViewById(R.id.remit_extension_type);
		ll_amount_layout = view.findViewById(R.id.ll_extension_amount);
		tran_remit_amount_view = (TextView) view.findViewById(R.id.tran_remit_amount);
		extension_flag_layout = view.findViewById(R.id.ll_extension_flag);
		rg_extension_flag_view = (TextView) view.findViewById(R.id.yes_or_no);
		tran_remit_phone = (TextView) view.findViewById(R.id.tran_remit_phone);
		startDateText = (TextView) view.findViewById(R.id.tv_remit_valuedate);
		endDateText = (TextView) view.findViewById(R.id.tv_remit_enddate);

		lv_addshareAcc = (LinearListView) view.findViewById(R.id.lv_addsharedAcc);
		lv_deleteshareAcc = (LinearListView) view.findViewById(R.id.lv_deletesharedAcc);
		nextButton = (Button) view.findViewById(R.id.remit_input_next_btn);

		/** 收款付费起点金额 */
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) findViewById(R.id.tran_remit_amount_lable));

		// 赋值
		String account = (String) chooseMap.get(Acc.ACC_ACCOUNTNUMBER_RES);
		tran_remit_account.setText(StringUtil.getForSixForString(account));
		remit_type_view.setText(RemitSetMealProducDic.getKeyFromValue((String) entity
				.get(Tran.TRAN_REMITSETMEALPRODUCPROPERTY)));
		remit_extension_type_view.setText(encodeMealTypEcutout2(entity.get(Tran.TRAN_SIGNTYPE_RES)));
		ll_amount_layout.setVisibility(RemitSetMealProducDic.getTagFromValue(entity
				.get(Tran.TRAN_REMITSETMEALPRODUCPROPERTY)) ? View.VISIBLE : View.GONE);
		if (!TextUtils.isEmpty(tran_remit_amount_view.getText().toString().trim())) {
			tran_remit_amount_view.setText("0.00");
		} else {
			tran_remit_amount_view.setText(StringUtil.parseStringPattern(orignAmountParam, 2));
		}
		// 续约布局根据账号是否支持续约进行显示隐藏
		extension_flag_layout.setVisibility("Y".equalsIgnoreCase(entity.get("remitSetMealautoFlag")) ? View.VISIBLE
				: View.GONE);
		// 用户是否选择续约

		rg_extension_flag_view.setText("Y".equalsIgnoreCase(extensionFlagParam) ? "是" : "否");

		tran_remit_phone.setText(phoneNumberParam);
		startDateText.setText(entity.get("startDate"));
		endDateText.setText(entity.get("endDate"));
		// tran_remit_phone.setText(entity.get("mobile"));
		startDateText.setText(entity.get("startDate"));
		endDateText.setText(entity.get("endDate"));

		List<Map<String, String>> deleteList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> addList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < toDelList.size(); i++) {
			Map<String, String> map = toDelList.get(i);
			if ("A".equalsIgnoreCase(map.get("shareOpType"))) {
				addList.add(map);
			} else {
				deleteList.add(map);
			}
		}

		View deleteshareAcc_layout = view.findViewById(R.id.ll_acc_delete);
		View addshareAcc_layout = view.findViewById(R.id.ll_acc_add);

		// 共享账户列表 (根据返回结果反显 付款方套餐 并且不等于空显示 收款方套餐和双向套餐则不显示)
		if (RemitSetMealProducDic.getTagFromValue((String) entity.get(Tran.TRAN_REMIT_APP_REMITSETMEALPRODUCPROPERTY_REQ))) {
			deleteshareAcc_layout.setVisibility(View.GONE);
			addshareAcc_layout.setVisibility(View.GONE);
		} else {
			if (deleteList.isEmpty()) {
				deleteshareAcc_layout.setVisibility(View.VISIBLE);
			} else {
				RemitModifyConfirmAdapter deleteAdapter = new RemitModifyConfirmAdapter(this, deleteList);
				lv_deleteshareAcc.setAdapter(deleteAdapter);
				deleteshareAcc_layout.setVisibility(View.VISIBLE);
			}

			if (addList.isEmpty()) {
				addshareAcc_layout.setVisibility(View.VISIBLE);
			} else {
				addshareAcc_layout.setVisibility(View.VISIBLE);
				RemitModifyConfirmAdapter addAdapter = new RemitModifyConfirmAdapter(this, addList);
				lv_addshareAcc.setAdapter(addAdapter);
			}
		}

		// modify luqp 2015年11月19日15:37:55
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

	// modify luqp 2015年11月19日15:37:55
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
			/** 安全工具数据校验 */ // modify luqp 2015年11月19日15:37:55
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
		requestSetMealModify();
	}

	/**
	 * 请求修改签约交易
	 */
	public void requestSetMealModify() {

		// toDelList
		List<Map<String, String>> uplist = new ArrayList<Map<String, String>>();
		List<Map<String, String>> accList = (List<Map<String, String>>) queryMap.get(Tran.TRAN_VALIDACCOUNTLIST_RES);
		for (int i = 0; i < toDelList.size(); i++) {
			Map<String, String> toMap = toDelList.get(i);
			for (int j = 0; j < accList.size(); j++) {
				Map<String, String> accMap = accList.get(i);
				if (toMap.get("shareCardNo").equals(accMap.get("shareCardNo"))
						&& !toMap.get("shareOpType").equals(accMap.get("shareOpType"))) {
					// 账号相同,状态不同
					uplist.add(toMap);
				}
			}
		}

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.TRAN_REMITSETMEALMODIFY_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		String accountID = (String) chooseMap.get(Acc.ACC_ACCOUNTID_RES);
		paramsmap.put(Tran.TRAN_REMIT_APPPRE_ACCOUNTID_REQ, accountID);
		paramsmap.put("remitSetMealProductType", entity.get(Tran.TRAN_SIGNTYPE_RES));
		paramsmap.put(Tran.TRAN_MODIFY_TODELLIST_REQ, toDelList);
		paramsmap.put(Tran.TRAN_MODIFY_SHARECARDNO_REQ, entity.get("shareCardNo"));
		paramsmap.put(Tran.TRAN_MODIFY_SHAREOPTYPE_REQ, entity.get("shareOpType"));
		paramsmap.put(Tran.TRAN_REMIT_APPPRE_SHARESTATUS_REQ, entity.get("shareStatus"));
		paramsmap.put(Tran.TRAN_MODIFY_PHONENUMBER_REQ, phoneNumberParam);
		paramsmap.put(Tran.TRAN_MODIFY_REMITSETMEALPRODUCPROPERTY_REQ, entity.get("remitSetMealProducProperty"));
		paramsmap.put(Tran.TRAN_MODIFY_ORIGNAMOUNT_REQ, orignAmountParam);
		paramsmap.put(Tran.TRAN_MODIFY_CURCDE_REQ, "001");
		paramsmap.put(Tran.TRAN_MODIFY_REMITSETMEALPRODUCPROPERTYID_REQ, entity.get("remitSetMealProductId"));
		paramsmap.put(Tran.TRAN_MODIFY_REMITSETMEALPRODUCTIDESC_REQ, entity.get("remitSetMealProductIDesc"));
		paramsmap.put(Tran.TRAN_MODIFY_EXTENSIONFLAG_REQ, extensionFlagParam);
		paramsmap.put(Tran.TRAN_REMIT_APP_EFFECTTIVEDATE_REQ, (String) entity.get(Tran.TRAN_STARTDATE_RES));
		paramsmap.put(Tran.TRAN_ATM_SUB_SIGNEDDATA_REQ, remitPreMap.get(Tran.TRAN_ATM_PLAINDATA_RES));

		Map<String, Object> loginMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA);
		String cusName = (String) loginMap.get(Login.CUSTOMER_NAME);
		paramsmap.put(Tran.TRAN_REMIT_APPPRE_NAME_REQ, cusName);

		paramsmap.put(Tran.TRAN_ATM_SUB_TOKEN_REQ, BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID));
		/** 安全工具参数获取 */
		usbKeyText.InitUsbKeyResult(paramsmap);
		SipBoxUtils.setSipBoxParams(paramsmap);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestSetMealModifyCallBack");
	}

	/**
	 * 请求修改签约交易回调
	 * 
	 * @param resultObj
	 */
	public void requestSetMealModifyCallBack(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		// BiiResponse biiResponse = (BiiResponse) resultObj;
		// List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// String result = (String) biiResponseBody.getResult();

		CustomDialog.toastShow(this, this.getString(R.string.trans_remit_modify_success));
		ActivityTaskManager.getInstance().removeAllActivity();
		Intent intent = new Intent(this, RemitThirdMenu.class);
		startActivity(intent);
	}
}
