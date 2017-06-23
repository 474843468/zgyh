
package com.chinamworld.bocmbci.biz.quickOpen.quickopen;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cfca.mobile.sip.SipBox;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.QuickOpen;
import com.chinamworld.bocmbci.biz.quickOpen.QuickOpenDataCenter;
import com.chinamworld.bocmbci.biz.quickOpen.QuickOpenUtils;
import com.chinamworld.bocmbci.biz.quickOpen.StockThirdQuickOpenBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.SDcardLogUtil;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 中银证券开户提交页
 * 
 * @author Zhi
 */
public class StockThirdQuickOpenSubmitActivity extends StockThirdQuickOpenBaseActivity implements OnClickListener {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final String TAG = "StockThirdQuickOpenSubmitActivity";
	/** 用户选择的卡信息 */
	private Map<String, Object> cardInfo;
	/** 下一步按钮 */
	private Button btnNext;
	/** 随机数 */
	private String random;
	/** 手机验证码sip */
	private SipBox otpSipBxo;
	/** 短信验证码sip*/
	private SipBox smcSipBxo;
	/**中银E盾*/
	private UsbKeyText usbKeyText;
	/** 标识是否有短息验证码、动态口令 */
	private boolean isOtp = false;
	private boolean isSmc = false;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.quick_open_comfirm);
		setTitle(R.string.quickOpen_title_open);
		cardInfo = QuickOpenDataCenter.getInstance().getListCommonQueryAllChinaBankAccount().get(getIntent().getIntExtra(POSITION, 0));
		initView();
	}
	
	private void initView() {
		random = getIntent().getStringExtra(RANDOM);
		btnNext = (Button) findViewById(R.id.btnNext);
		btnNext.setText(getResources().getString(R.string.confirm));
		btnNext.setOnClickListener(this);
		((TextView) findViewById(R.id.tv_titleTip)).setText(getResources().getString(R.string.quickOpen_confirm_submitTitle));
		Map<String, Object> map = QuickOpenDataCenter.getInstance().getMapStockThirdQueryCustInfoExtend();
		((TextView) findViewById(R.id.tv_name)).setText((String) map.get(QuickOpen.CUSTNAME));
		((TextView) findViewById(R.id.tv_gender)).setText(QuickOpenUtils.getGender((String) map.get(QuickOpen.GENDER)));
		((TextView) findViewById(R.id.tv_identitytype)).setText("身份证");
		((TextView) findViewById(R.id.tv_identityactnum)).setText((String) map.get(QuickOpen.IDENTIFYNUMBER));
		((TextView) findViewById(R.id.tv_mobile)).setText((String) map.get(QuickOpen.MOBILENO));
		((TextView) findViewById(R.id.tv_acc)).setText(StringUtil.getForSixForString((String) cardInfo.get(Comm.ACCOUNTNUMBER)));
		isOtp = false;
		isSmc = false;

		findViewById(R.id.layout_sms).setVisibility(View.GONE);
		findViewById(R.id.layout_otp).setVisibility(View.GONE);
		findViewById(R.id.sip_usbkey).setVisibility(View.GONE);
		findViewById(R.id.tv_submitHint).setVisibility(View.GONE);
		
		initSipBox(QuickOpenDataCenter.getInstance().getMapStockThirdQuickOpenPre());
	}

	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox(Map<String, Object> result){
		
		findViewById(R.id.tv_submitHint).setVisibility(View.VISIBLE);
		usbKeyText = (UsbKeyText) findViewById(R.id.sip_usbkey);
		String mmconversationId = (String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		usbKeyText.Init(mmconversationId, random, result, this);
		isOtp = usbKeyText.getIsOtp();
		isSmc = usbKeyText.getIsSmc();
		initOtpSipBox();
		initSmcSipBox();
	}
	
	/** 初始化otp */
	private void initOtpSipBox() {
		if (isOtp) {
			findViewById(R.id.layout_otp).setVisibility(View.VISIBLE);
			otpSipBxo = (SipBox) findViewById(R.id.sip_otp);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(otpSipBxo, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, random, this);
//			otpSipBxo.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			otpSipBxo.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			otpSipBxo.setId(10002);
//			otpSipBxo.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			otpSipBxo.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			otpSipBxo.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			otpSipBxo.setSipDelegator(this);
//			otpSipBxo.setRandomKey_S(random);
		}
	}

	/** 初始化smc */
	private void initSmcSipBox() {
		if (isSmc) {
			findViewById(R.id.layout_sms).setVisibility(View.VISIBLE);
			Button btnSendSms = (Button) findViewById(R.id.smsbtn);
			SmsCodeUtils.getInstance().addSmsCodeListner(btnSendSms,smcOnclickListener);
			smcSipBxo = (SipBox) findViewById(R.id.sip_sms);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(smcSipBxo, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, random, this);
//			smcSipBxo.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			smcSipBxo.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			smcSipBxo.setId(10002);
//			smcSipBxo.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			smcSipBxo.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			smcSipBxo.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			smcSipBxo.setSipDelegator(this);
//			smcSipBxo.setRandomKey_S(random);
		}
	}
	
	/** 安全控件提交校验 */
	private void submitRegexp() {
		usbKeyText.checkDataUsbKey(otpSipBxo, smcSipBxo, new IUsbKeyTextSuccess() {
			
			@Override
			public void SuccessCallBack(String result, int errorCode) {
				// 获取token请求
				BaseHttpEngine.showProgressDialog();
				requestPSNGetTokenId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
			}
		});
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 4) {
			setResult(4);
			finish();
		}
	};

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------控件事件-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 获取手机交易码 */
	private OnClickListener smcOnclickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			sendSMSCToMobile();
		}
	};
	
	@Override
	public void onClick(View v) {
		submitRegexp();
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO ----------------------------------------网络请求与回调方法--------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(QuickOpen.STOCKACCOUNTNO, cardInfo.get(Comm.ACCOUNTNUMBER));
		params.put(QuickOpen.CUSTNAME, QuickOpenDataCenter.getInstance().getMapStockThirdQueryCustInfoExtend().get(QuickOpen.CUSTNAME));
		params.put(ConstantGloble.PUBLIC_TOKEN, BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID));
		/**安全工具参数获取*/
		usbKeyText.InitUsbKeyResult(params);
		SipBoxUtils.setSipBoxParams(params);
		SDcardLogUtil.logWriteToFile("params : " + params.toString(), "requestPSNGetTokenIdCallBack", "XPJ");
		requestHttp(QuickOpen.PSNSTOCKTHIRDQUICKOPENSUBMIT, "requestPsnStockThirdQuickOpenSubmitCallBack", params, true);
	}
	
	/** 开户提交回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnStockThirdQuickOpenSubmitCallBack(Object resultObj) {
		final Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}

		String postData = "";
		try {
			postData = URLEncoder.encode("merchant_no","UTF-8") 
							+ "=" + URLEncoder.encode(resultMap.get(QuickOpen.MERCHANT_NO).toString(), "UTF-8")
							+ "&" + URLEncoder.encode("param","UTF-8") 
							+ "=" + URLEncoder.encode(resultMap.get(QuickOpen.PARAM).toString(),"UTF-8")
							+ "&" + URLEncoder.encode("encrypt_str","UTF-8")
							+ "=" + URLEncoder.encode((String)resultMap.get(QuickOpen.ENCRYPT_STR).toString(),"UTF-8")
							+ "&" + URLEncoder.encode("ca_str","UTF-8")
							+ "=" + URLEncoder.encode((String)resultMap.get(QuickOpen.SIGNATURE_DATA).toString(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			LogGloble.e(TAG, "Encode exception!");
			e.printStackTrace(); 
		}
		// 退出登录状态
		BaseDroidApp.getInstanse().clientLogOut();
		// 销毁所有的界面
		ActivityTaskManager.getInstance().removeAllActivity();
		Intent intent = new Intent(StockThirdQuickOpenSubmitActivity.this, WebViewForUrl.class)
		.putExtra(WEB_URL_NAME, getResources().getString(R.string.quickOpen_title_open))
		.putExtra(WEB_POSTDATA, postData);
		startActivityForResult(intent, 4);

	}
}

