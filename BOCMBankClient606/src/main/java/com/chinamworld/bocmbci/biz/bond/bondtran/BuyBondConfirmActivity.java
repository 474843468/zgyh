package com.chinamworld.bocmbci.biz.bond.bondtran;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import com.chinamworld.bocmbci.bii.constant.Bond;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.bond.BondBaseActivity;
import com.chinamworld.bocmbci.biz.bond.BondDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 买债券信息确认页
 * 
 * @author panwe
 * 
 */
public class BuyBondConfirmActivity extends BondBaseActivity implements
		OnClickListener {

	/** 主布局 **/
	private View mainView;
	/** 加密控件 */
	private SipBox otpSipBxo;
	private SipBox smcSipBxo;
	/** 中银E盾 */
	private UsbKeyText usbKeyText;
	/** 标识是否有短息验证码、动态口令 */
	private boolean isOtp = false;
	private boolean isSmc = false;
	/** 当前显示条目 */
	private int mPostion;
	/** 交易类型 */
	private String tranType;
	/** 交易面额 */
	private String tranAmount;
	/** 交易金额 */
	private String tranBanlan;
	/** 交易价格 */
	private String tranPrice;
	/** 是否操作释放额度 */
	private boolean isRelease;
	private String otpPassword;
	private String smcPassword;
	private String otpRandomNum;
	private String smcRandomNum;
	/** 流水号 */
	private String tranSeq;
	// 预交易返回数据
	Map<String, Object> resultMap;
	private String randomNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 添加布局
		mainView = (View) LayoutInflater.from(this).inflate(
				R.layout.bond_buy_confirm, null);
		addView(mainView);
		setTitle(this.getString(R.string.bond_tran_title));
		initData();
		// 获取随机数
		requestForRandomNumber();
		// initView();
	}

	private void initData() {
		mPostion = getIntent().getIntExtra(POSITION, 0);
		tranAmount = getIntent().getStringExtra(TRANAMOUNT);
		tranType = getIntent().getStringExtra(TRANTYPE);
	}

	@SuppressWarnings("unchecked")
	private void initView() {
		TextView tvBondType = (TextView) mainView
				.findViewById(R.id.tv_bond_type);
		TextView tvBondName = (TextView) mainView
				.findViewById(R.id.tv_bond_name);
		TextView tvTranType = (TextView) mainView
				.findViewById(R.id.tv_trantype);
		TextView tvENey = (TextView) mainView.findViewById(R.id.tv_bizhong);
		TextView tvBanlan = (TextView) mainView.findViewById(R.id.tv_money1);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvBanlan);
		TextView tvPrice = (TextView) mainView.findViewById(R.id.tv_money2);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvPrice);
		TextView tvAmount = (TextView) mainView.findViewById(R.id.tv_money3);

		LinearLayout layoutPrice = (LinearLayout) mainView
				.findViewById(R.id.layout_price);
		LinearLayout layoutAmount = (LinearLayout) mainView
				.findViewById(R.id.layout_amount);

		TextView tvTip = (TextView) mainView.findViewById(R.id.tv_bill_tip);
		if (tranType.equals(Bond.BOND_TRANTYPE_BUY)) {
			tvTip.setText(this.getString(R.string.bond_buy_confirm_tip1));
		} else {
			tvTip.setText(this.getString(R.string.bond_buy_confirm_tip2));
		}

		btnBack.setOnClickListener(this);
		Button btnConfirm = (Button) mainView.findViewById(R.id.btnConfirm);
		btnConfirm.setOnClickListener(this);

		// 行情数据
		Map<String, Object> bondMap = BondDataCenter.getInstance()
				.getBondList().get(mPostion);

		// 预交易返回数据
		resultMap = (Map<String, Object>) BondDataCenter.getInstance()
				.getBuyConfirmResult().get(Bond.BUY_CONFIRM_RESLUTMAP);

		tvBondName.setText(commSetText((String) bondMap
				.get(Bond.BOND_SHORTNAME)));
		if (StringUtil.isNull((String) bondMap.get(Bond.BOND_TYPE))) {
			tvBondType.setText(BondDataCenter.bondType_hq.get("2"));
		} else {
			tvBondType
					.setText(commSetText((String) bondMap.get(Bond.BOND_TYPE)));
		}
		tvTranType.setText(commSetText(BondDataCenter.tranType.get(tranType)));
		tvENey.setText("人民币元");
		tvBanlan.setText(tranAmount);
		if (tranType.equals(Bond.BOND_TRANTYPE_BUY)) {
			tranPrice = (String) resultMap.get(Bond.SELL_CONFIRM_TRANPRICE);
			tranBanlan = (String) resultMap.get(Bond.SELL_CONFIRM_TRANAMOUT);
			tranSeq = (String) resultMap.get(Bond.SELL_COONFIRM_TRANSEQ);
			String str_tem=StringUtil.parseStringPattern(tranPrice, 2);
			String str_tem_1=str_tem+"人民币元/每100元面额";
			SpannableStringBuilder str_span = new SpannableStringBuilder(str_tem_1);
			if (!StringUtil.isNullOrEmpty(str_tem)){
				str_span.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.fonts_pink)),0,str_tem.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				str_span.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.fonts_black)),str_tem.length(),str_tem_1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			tvPrice.setText(str_span);
			tvAmount.setText(StringUtil.parseStringPattern(tranBanlan, 2));
		} else {
			layoutPrice.setVisibility(View.GONE);
			layoutAmount.setVisibility(View.GONE);
		}

		// 加密类型
		// sipInit((List<Map<String, Object>>) BondDataCenter.getInstance()
		// .getBuyConfirmResult().get(Bond.BUY_CONFIRM_FACTORLIST));
		/** 跟据返回初始化中银E盾加密控件 */
		initSipBox();
	}

	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox() {
		usbKeyText = (UsbKeyText) mainView.findViewById(R.id.sip_usbkey);
		String mmconversationId = (String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		Map<String, Object> resultResponse = (Map<String, Object>) BondDataCenter
				.getInstance().getBuyConfirmResult();
		usbKeyText.Init(mmconversationId, randomNumber, resultResponse, this);
		isOtp = usbKeyText.getIsOtp();
		isSmc = usbKeyText.getIsSmc();
		initOtpSipBox();
		initSmcSipBox();
	}

	/** 跟据返回初始化加密控件 */
	// private void sipInit(List<Map<String, Object>> sipList) {
	// for (int i = 0; i < sipList.size(); i++) {
	// @SuppressWarnings("unchecked")
	// Map<String, Object> map = (Map<String, Object>) sipList.get(i).get(
	// Bond.SIGN_PAY_CONFIRM_SIP_FIED);
	// String sipType = (String) map.get(Bond.SIGN_PAY_CONFIRM_SIP_NAME);
	// if (sipType.equals(Comm.Otp)) {
	// isOtp = true;
	// initOtpSipBox();
	// } else if (sipType.equals(Comm.Smc)) {
	// isSmc = true;
	// initSmcSipBox();
	// }
	// }
	// // 获取随机数
	// requestForRandomNumber();
	// }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// 确定
		case R.id.btnConfirm:
			contentCheck();
			break;

		// 返回
		case R.id.ib_back:
			if (tranType.equals(Bond.BOND_TRANTYPE_BUY)) {
				isRelease = true;
				// 获取token请求
				requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
						.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
			} else {
				finish();
			}

			break;
		}
	}

	/** 初始化otp */
	private void initOtpSipBox() {
		if (isOtp) {
			LinearLayout layoutSip = (LinearLayout) mainView
					.findViewById(R.id.layout_sip);
			layoutSip.setVisibility(View.VISIBLE);
			otpSipBxo = (SipBox) mainView.findViewById(R.id.et_cecurity_ps);
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
//			otpSipBxo.setSipDelegator(this);
//			otpSipBxo.setRandomKey_S(randomNumber);
		}

	}

	/** 初始化smc */
	private void initSmcSipBox() {
		if (isSmc) {
			LinearLayout layoutSmc = (LinearLayout) mainView
					.findViewById(R.id.layout_sms);
			layoutSmc.setVisibility(View.VISIBLE);
			Button btnSendSms = (Button) findViewById(R.id.smsbtn);
			SmsCodeUtils.getInstance().addSmsCodeListner(btnSendSms,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// 发送验证码请求
							sendSMSCToMobile();
						}
					});
			smcSipBxo = (SipBox) mainView.findViewById(R.id.sip_sms);
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

	/** 提交校验 **/
	private void contentCheck() {
		// try {
		// ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		// if (isSmc) {
		// // 短信验证码
		// RegexpBean rebSms = new RegexpBean(
		// getString(R.string.acc_smc_regex), smcSipBxo.getText()
		// .toString(), ConstantGloble.SIPSMCPSW);
		// lists.add(rebSms);
		// }
		// if (isOtp) {
		// // 动态口令
		// RegexpBean rebOtp = new RegexpBean(
		// getString(R.string.active_code_regex), otpSipBxo
		// .getText().toString(), ConstantGloble.SIPOTPPSW);
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
		// isRelease = false;
		// // 获取token请求
		// BaseHttpEngine.showProgressDialog();
		// requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
		// .getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		// }
		// } catch (CodeException e) {
		// BaseDroidApp.getInstanse().createDialog(null, e.getMessage());
		// LogGloble.exceptionPrint(e);
		// }
		usbKeyText.checkDataUsbKey(otpSipBxo, smcSipBxo,
				new IUsbKeyTextSuccess() {

					@Override
					public void SuccessCallBack(String result, int errorCode) {
						// TODO Auto-generated method stub
						// 获取token请求
						BaseHttpEngine.showProgressDialog();
						requestPSNGetTokenId((String) BaseDroidApp
								.getInstanse().getBizDataMap()
								.get(ConstantGloble.CONVERSATION_ID));
					}
				});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			if (tranType.equals(Bond.BOND_TRANTYPE_BUY)) {
				isRelease = true;
				// 获取token请求
				requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
						.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
			} else {
				finish();
			}
			break;
		}
		return true;
	}

	// token返回
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String token = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		if (isRelease) {
			requestBuyAmountRelease(token);
		} else {
			requestBondBuyResult(token);
		}
	}

	/**
	 * 买入提交请求
	 * 
	 * @param token
	 */
	private void requestBondBuyResult(String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bond.METHOD_BOND_BUYRESULT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Bond.BOND_BUYRESULT_TOKEN, token);
		map.put(Bond.BOND_CODE,
				BondDataCenter.getInstance().getBondList().get(mPostion)
						.get(Bond.BOND_CODE));
		map.put(Bond.SELL_CONFIRM_TRANMONEY, tranAmount);
		map.put(Bond.BOND_BUY_RESULT_PRICE, tranPrice);
		map.put(Bond.SELL_CONFIRM_TRANAMOUT, tranBanlan);
		map.put(Bond.SELL_COONFIRM_TRANSEQ, tranSeq);
		map.put(Bond.RE_HISTORYTRAN_QUERY_TRANTYPE, tranType);
		map.put(Bond.BOND_RESULT_BONDACC, BondDataCenter.getInstance()
				.getAccMap().get(Bond.INVESTACCOUNT));
		map.put(Bond.BONDBUY_RESULT_ACCID, BondDataCenter.getInstance()
				.getAccMap().get(Bond.ACCOUNTID));
		map.put(Bond.BOND_TYPE, BondDataCenter.bondType_re.get(1));
		map.put(Bond.BONDBUY_RESULT_DATE, QueryDateUtils
				.getcurrentDate(BondDataCenter.getInstance().getSysTime()));

		// if (!StringUtil.isNullOrEmpty(otpPassword)) {
		// map.put(Comm.Otp, otpPassword);
		// }
		// if (!StringUtil.isNullOrEmpty(otpRandomNum)) {
		// map.put(Comm.Otp_Rc, otpRandomNum);
		// }
		// if (!StringUtil.isNullOrEmpty(smcPassword)) {
		// map.put(Comm.Smc, smcPassword);
		// }
		// if (!StringUtil.isNullOrEmpty(smcRandomNum)) {
		// map.put(Comm.Smc_Rc, smcRandomNum);
		// }
		/** 安全工具参数获取 */
		usbKeyText.InitUsbKeyResult(map);

		SipBoxUtils.setSipBoxParams(map);

		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"bondBuyResultComitCallBack");
	}

	/** 购买提交返回处理 **/
	public void bondBuyResultComitCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.bond_tran_error));
			return;
		}
		BondDataCenter.getInstance().setBuyComitResult(result);
		BaseHttpEngine.dissMissProgressDialog();
		Intent it = new Intent(this, BuyBondResultActivity.class);
		it.putExtra(POSITION, mPostion);
		it.putExtra(TRANAMOUNT, tranAmount);
		it.putExtra(TRANTYPE, tranType);
		startActivity(it);
		this.finish();
	}

	/**
	 * 请求密码控件随机数
	 */
	public void requestForRandomNumber() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this,
				"queryRandomNumberCallBack");
	}

	/**
	 * 请求密码控件随机数 回调
	 * 
	 * @param resultObj
	 */
	public void queryRandomNumberCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		randomNumber = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(randomNumber)) {
			return;
		}
		// 加密控件设置随机数
		// if (otpSipBxo != null) {
		// otpSipBxo.setRandomKey_S(randomNumber);
		// }
		// if (smcSipBxo != null) {
		// smcSipBxo.setRandomKey_S(randomNumber);
		// }
		initView();
	}

	/**
	 * 额度释放
	 * 
	 * @param token
	 */
	private void requestBuyAmountRelease(String token) {
		// 开启通讯框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bond.METHOD_BUYRELEASE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Bond.BOND_BUYRESULT_TOKEN, token);
		map.put(Bond.BOND_CODE,
				BondDataCenter.getInstance().getBondList().get(mPostion)
						.get(Bond.BOND_CODE));
		map.put(Bond.SELL_CONFIRM_TRANMONEY, tranAmount);
		map.put(Bond.SELL_CONFIRM_TRANPRICE,
				resultMap.get(Bond.SELL_CONFIRM_TRANPRICE));
		map.put(Bond.SELL_COONFIRM_TRANSEQ,
				resultMap.get(Bond.SELL_COONFIRM_TRANSEQ));
		map.put(Bond.RE_HISTORYTRAN_QUERY_TRANTYPE, tranType);

		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "buyReleaseCallBack");
	}

	/** 额度释放返回处理 **/
	public void buyReleaseCallBack(Object resultObj) {
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		finish();
	}
}
