package com.chinamworld.bocmbci.biz.crcd.mycrcd.transquery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cfca.mobile.sip.SipBox;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
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
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 提前结清入账确认页
 * 
 * @author huangyuchao
 * 
 */
public class CrcdDividedAndPreviewConfirm extends CrcdBaseActivity {
	private static final String TAG = "CrcdDividedAndPreviewConfirm";
	private View view;

	private TextView finc_accNumber, finc_fenqidate, finc_miaoshu, finc_bizhong, finc_fincName, finc_qinum,
			finc_fenqitype, finc_firstamount, finc_nextmoney, finc_hasrumoney, finc_remiannonum, finc_remiannomoney,
			finc_nextdate;

	private Button lastButton, sureButton;

	private EditText et_pass;
	/** 加密控件里的随机数 */
	protected String randomNumber;
	protected static Map<String, Object> detailMap;

	String instalmentPlan;

	String instmtdate, currrency, incomeTimeCount, incomeamount, resttimecount, restAmount, nextIncomeDate;

	/** 安全因子数组 */
	public List<Map<String, Object>> factorList;
	Map<String, Object> factorMap;

	/** 动态口令布局 */
	private LinearLayout ll_active_code;
	/** 手机交易码布局 */
	private LinearLayout ll_smc;

	/** 动态口令 */
	private String otpStr;
	/** 手机交易码 */
	private String smcStr;
	/** 加密之后加密随机数 */
	private String otp_password_RC = "";
	private String smc_password_RC = "";

	/** 加密控件——动态口令 */
	private SipBox sipBoxActiveCode = null;
	/** 加密控件——手机交易码 */
	private SipBox sipBoxSmc = null;
	/**中银E盾*/
	private UsbKeyText usbKeyText;
	/** 是否上传动态口令 */
	private boolean isOtp = false;
	/** 是否上传短信验证码 */
	private boolean isSmc = false;
	private TextView moneyText = null;
	private String commConversationId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_divided_jieqing_ruzhang));
		view = addView(R.layout.crcd_divided_info_detail_confirm);
		requestForRandomNumber();
//		init();
		BaseHttpEngine.showProgressDialog();
//		requestForRandomNumber();
	}

	/**
	 * 请求密码控件随机数
	 */
	public void requestForRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "queryRandomNumberCallBack");
	}

	/**
	 * 请求密码控件随机数 回调
	 * 
	 * @param resultObj
	 */
	public void queryRandomNumberCallBack(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		randomNumber = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(randomNumber)) {
			return;
		}
		// 加密控件设置随机数
//		sipBoxActiveCode.setRandomKey_S(randomNumber);
//		sipBoxSmc.setRandomKey_S(randomNumber);
		init();
	}

	/** 初始化界面 */
	public void init() {

		factorMap = CrcdDividedDetailListDetail.result;
		factorList = (List<Map<String, Object>>) factorMap.get(Crcd.FACTORLIST);
		commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));

//		initOptAndSmc()
		/** 跟据返回初始化中银E盾加密控件 */
		initSipBox();

		finc_accNumber = (TextView) view.findViewById(R.id.finc_accNumber);
		finc_fenqidate = (TextView) view.findViewById(R.id.finc_fenqidate);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_fenqidate);
		finc_miaoshu = (TextView) view.findViewById(R.id.finc_miaoshus);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_miaoshu);
		finc_bizhong = (TextView) view.findViewById(R.id.finc_bizhong);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_bizhong);
		finc_fincName = (TextView) view.findViewById(R.id.finc_fincName);
		finc_qinum = (TextView) view.findViewById(R.id.finc_qinum);
		finc_fenqitype = (TextView) view.findViewById(R.id.finc_fenqitype);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_fenqitype);
		finc_firstamount = (TextView) view.findViewById(R.id.finc_firstamount);
		finc_nextmoney = (TextView) view.findViewById(R.id.finc_nextmoney);
		finc_hasrumoney = (TextView) view.findViewById(R.id.finc_hasrumoney);
		finc_remiannonum = (TextView) view.findViewById(R.id.finc_remiannonum);
		finc_remiannomoney = (TextView) view.findViewById(R.id.finc_remiannomoney);
		finc_nextdate = (TextView) view.findViewById(R.id.finc_nextdate);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_nextdate);
		moneyText = (TextView) findViewById(R.id.money);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, moneyText);

		detailMap = CrcdDividedDetailListDetail.detailMap;

		finc_accNumber.setText(StringUtil.getForSixForString(CrcdDividedHistoryQueryList.accountNumber));
		instmtdate = String.valueOf(detailMap.get(Crcd.CRCD_INSTMTDATE));
		finc_fenqidate.setText(instmtdate);
		finc_miaoshu.setText(String.valueOf(detailMap.get(Crcd.CRCD_INSTMEDESCRIPTION)));
		currrency = String.valueOf(detailMap.get(Crcd.CRCD_CURRENCY));

		finc_bizhong.setText(CrcdDividedDetailListDetail.strCurrCode);
		finc_fincName.setText(StringUtil.parseStringPattern(String.valueOf(detailMap.get(Crcd.CRCD_AMOUNT)), 2));
		finc_qinum.setText(String.valueOf(detailMap.get(Crcd.CRCD_INSTMTCOUNT)));
		finc_fenqitype.setText(CrcdDividedDetailListDetail.strChargeMode);

		incomeTimeCount = String.valueOf(detailMap.get(Crcd.CRCD_INCOMETIMECOUNT));

		finc_firstamount.setText(StringUtil.parseStringPattern(String.valueOf(detailMap.get(Crcd.CRCD_FIRSTINAMOUNT)),
				2));
		finc_nextmoney
				.setText(StringUtil.parseStringPattern(String.valueOf(detailMap.get(Crcd.CRCD_NEXTTIMEAMOUNT)), 2));
		incomeamount = String.valueOf(detailMap.get(Crcd.CRCD_INCOMEAMOUNT));

		finc_hasrumoney.setText(StringUtil.parseStringPattern(incomeamount, 2));

		resttimecount = String.valueOf(detailMap.get(Crcd.CRCD_RESTTIMECOUNT));
		finc_remiannonum.setText(resttimecount);

		restAmount = String.valueOf(detailMap.get(Crcd.CRCD_RESTAMOUNT_RES));

		finc_remiannomoney.setText(StringUtil.parseStringPattern(restAmount, 2));
		nextIncomeDate = String.valueOf(detailMap.get(Crcd.CRCD_NEXTINCOMEDATE));
		finc_nextdate.setText(StringUtil.valueOf1(nextIncomeDate));
		instalmentPlan = String.valueOf(detailMap.get(Crcd.CRCD_INSTALMENTPLAN));

		lastButton = (Button) view.findViewById(R.id.lastButton);
		lastButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		// et_pass = (EditText) view.findViewById(R.id.et_pass);
		sureButton = (Button) view.findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 动态口令
//				RegexpBean sipSmcpBean = new RegexpBean(getString(R.string.acc_smc_regex), sipBoxSmc.getText()
//						.toString(), ConstantGloble.SIPSMCPSW);
//				RegexpBean sipRegexpBean = new RegexpBean(getString(R.string.active_code_regex), sipBoxActiveCode
//						.getText().toString(), ConstantGloble.SIPOTPPSW);
//
//				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
//				if (isSmc) {
//					lists.add(sipSmcpBean);
//				}
//				if (isOtp) {
//					lists.add(sipRegexpBean);
//				}
//
//				if (RegexpUtils.regexpDate(lists)) {// 校验通过
//					if (isSmc) {
//						try {
//							smcStr = sipBoxSmc.getValue().getEncryptPassword();
//							smc_password_RC = sipBoxSmc.getValue().getEncryptRandomNum();
//						} catch (CodeException e) {
//							LogGloble.exceptionPrint(e);
//						}
//
//					}
//					if (isOtp) {
//						try {
//							otpStr = sipBoxActiveCode.getValue().getEncryptPassword();
//							otp_password_RC = sipBoxActiveCode.getValue().getEncryptRandomNum();
//						} catch (CodeException e) {
//							LogGloble.exceptionPrint(e);
//						}
//
//					}
//
//					BaseHttpEngine.showProgressDialog();
//					pSNGetTokenId();
//				}
				/**安全工具数据校验*/
				usbKeyText.checkDataUsbKey(sipBoxActiveCode, sipBoxSmc, new IUsbKeyTextSuccess() {
					
					@Override
					public void SuccessCallBack(String result, int errorCode) {
						// TODO Auto-generated method stub
						BaseHttpEngine.showProgressDialog();
						pSNGetTokenId();
					}
				});
				
			}
		});

	}
	
	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox(){
		usbKeyText = (UsbKeyText)view.findViewById(R.id.sip_usbkey);
		usbKeyText.Init(commConversationId, randomNumber, factorMap, this);
		isOtp = usbKeyText.getIsOtp();
		isSmc = usbKeyText.getIsSmc();
		initOptAndSmc();
	}
	
	public void initOptAndSmc() {
		if(isOtp){
		// 动态口令
		ll_active_code = (LinearLayout) view.findViewById(R.id.ll_active_code);
		ll_active_code.setVisibility(View.VISIBLE);
		sipBoxActiveCode = (SipBox) view.findViewById(R.id.sipbox_active);
			// add by 2016年9月12日 luqp 修改
			sipBoxActiveCode.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
			sipBoxActiveCode.setTextColor(getResources().getColor(android.R.color.black));
			SipBoxUtils.initSipBoxWithTwoType(sipBoxActiveCode, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//		sipBoxActiveCode.setCipherType(SystemConfig.CIPHERTYPE);
//		// sipBoxActiveCode.setLayoutParams(param);
//		sipBoxActiveCode.setTextColor(getResources().getColor(android.R.color.black));
//		// sipBoxActiveCode.setGravity(Gravity.CENTER_VERTICAL);
//		sipBoxActiveCode.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		sipBoxActiveCode.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		sipBoxActiveCode.setId(10002);
//		sipBoxActiveCode.setBackgroundResource(R.drawable.bg_for_edittext);
//		sipBoxActiveCode.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		sipBoxActiveCode.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//		sipBoxActiveCode.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
//		sipBoxActiveCode.setSingleLine(true);
//		sipBoxActiveCode.setSipDelegator(this);
//		sipBoxActiveCode.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		sipBoxActiveCode.setRandomKey_S(randomNumber);
		// ll_activecode_sip.addView(sipBoxActiveCode);
		}
		if(isSmc){
		// 手机交易码
		ll_smc = (LinearLayout) view.findViewById(R.id.ll_smc);
		ll_smc.setVisibility(View.VISIBLE);
		sipBoxSmc = (SipBox) view.findViewById(R.id.sipbox_smc);
			// add by 2016年9月12日 luqp 修改
			sipBoxSmc.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
			sipBoxSmc.setTextColor(getResources().getColor(android.R.color.black));
			SipBoxUtils.initSipBoxWithTwoType(sipBoxSmc, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//		sipBoxSmc.setCipherType(SystemConfig.CIPHERTYPE);
//		// sipBoxSmc.setLayoutParams(param);
//		// sipBoxSmc.setGravity(Gravity.CENTER_VERTICAL);
//		sipBoxSmc.setTextColor(getResources().getColor(android.R.color.black));
//		sipBoxSmc.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		sipBoxSmc.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		sipBoxSmc.setId(10002);
//		sipBoxSmc.setBackgroundResource(R.drawable.bg_for_edittext);
//		sipBoxSmc.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		sipBoxSmc.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//		sipBoxSmc.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
//		sipBoxSmc.setSingleLine(true);
//		sipBoxSmc.setSipDelegator(this);
//		sipBoxSmc.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		sipBoxSmc.setRandomKey_S(randomNumber);
		// ll_smc_sip.addView(sipBoxSmc);

		Button smsBtn = (Button) view.findViewById(R.id.smsbtn);
		SmsCodeUtils.getInstance().addSmsCodeListner(smsBtn, new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 发送验证码到手机
				sendMSCToMobile();
			}
		});
		}
//		showOptOrSmc();
	}

//	public void showOptOrSmc() {
//		/** 判断是动态口令还是手机交易码 */
//		if (!StringUtil.isNullOrEmpty(factorList)) {
//			for (int i = 0; i < factorList.size(); i++) {
//				Map<String, Object> itemMap = factorList.get(i);
//				Map<String, Object> securityMap = (Map<String, Object>) itemMap.get(Crcd.FIELD);
//				String name = String.valueOf(securityMap.get(Crcd.NAME));
//				if (Comm.Otp.equals(name)) {
//					isOtp = true;
//					ll_active_code.setVisibility(View.VISIBLE);
//				} else if (Comm.Smc.equals(name)) {
//					isSmc = true;
//					ll_smc.setVisibility(View.VISIBLE);
//				}
//			}
//		}
//	}

	public void aquirePSNGetTokenIdCallBack(Object resultObj) {
		super.aquirePSNGetTokenIdCallBack(resultObj);
		psnCrcdDividedPayAdvanceResult();
	};

	public void psnCrcdDividedPayAdvanceResult() {
		// 通讯开始,展示通讯框
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDIVIDEDPAYADVANCERESULT);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Crcd.CRCD_ACCOUNTID_RES, CrcdDividedHistoryQueryList.accountId);
		map.put(Crcd.CRCD_CURRENCYCODE, currrency);
		map.put(Crcd.CRCD_INSTALMENTPLAN, instalmentPlan);
		map.put(Crcd.CRCD_INCOMETIMECOUNT, incomeTimeCount);
		map.put(Crcd.CRCD_INCOMEAMOUNT, incomeamount);
		map.put(Crcd.CRCD_RESTTIMECOUNT, resttimecount);
		map.put(Crcd.CRCD_RESTAMOUNT, restAmount);
		// 正常情况，不会为空，方便测试
		// if (StringUtil.isNull(nextIncomeDate)) {
		// nextIncomeDate = "2010/01/01";
		// }
		map.put(Crcd.CRCD_NEXTINCOMEDATE, nextIncomeDate);
		map.put(Crcd.CRCD_TOKEN, tokenId);
//		if (isOtp) {
//			map.put(Comm.Otp, otpStr);
//			map.put(Comm.Otp_Rc, otp_password_RC);
//		}
//		if (isSmc) {
//			map.put(Comm.Smc, smcStr);
//			map.put(Comm.Smc_Rc, smc_password_RC);
//		}
		/**安全工具参数获取*/
		usbKeyText.InitUsbKeyResult(map);
		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdDividedPayAdvanceResultCallBack");
	}

	public static Map<String, Object> result;

	public void psnCrcdDividedPayAdvanceResultCallBack(Object resultObj) {
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		result = (Map<String, Object>) biiResponseBody.getResult();
		// if (StringUtil.isNullOrEmpty(result)) {
		// return;
		// }
		Intent it = new Intent(CrcdDividedAndPreviewConfirm.this, CrcdDividedAndPreviewResult.class);
		startActivity(it);
	}

}
