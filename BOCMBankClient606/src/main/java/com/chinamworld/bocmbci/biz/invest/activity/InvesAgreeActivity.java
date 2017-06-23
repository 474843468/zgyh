package com.chinamworld.bocmbci.biz.invest.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cfca.mobile.sip.SipBox;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Inves;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.invest.InverstBaseActivity;
import com.chinamworld.bocmbci.biz.invest.constant.InvestConstant;
import com.chinamworld.bocmbci.boc.ModelBoc;
import com.chinamworld.bocmbci.constant.CheckRegExp;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * ForexInvesAgreeActivity：开通投资理财服务页面，用户选择接受或不接受,输入动态口令可开通
 * 
 * @author xby
 * 
 */
public class InvesAgreeActivity extends InverstBaseActivity {
	private static final String TAG = "ForexInvesAgreeActivity";

	/**
	 * agreeButton：接受按钮
	 */
	private Button agreeButton = null;
	private boolean isReusltLayout = false;
	/**
	 * noAgreeButton：不接受按钮
	 */
	private Button noAgreeButton = null;

	/**
	 * sureButton：确定按钮
	 */
	private Button sureButton = null;
	/**
	 * 动态口令输入框
	 */
	private SipBox smsEdit;
	/**中银E盾*/
	private UsbKeyText usbKeyText;
	private String tokenId = null;
	/**
	 * otp:用户输入的动态口令
	 */
	private String sms;

	/** 是否需要动态口令 */
	private boolean isOtp = false;
	/** 是否需要手机验证码 */
	private boolean isSms = false;

	LinearLayout llSms, llOtp;
	/** 加密控件里的随机数 */
	private String randomNumber;
	SipBox sipBox;

	private String pswOtp, pswotpRC, pswSms, pswSmsRC;
	private int index = 0;
	private TextView textInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.biz_activity_606_layout);
		// 为界面标题赋值
		setTitle(this.getString(R.string.kttzlcfw));
		
		// 添加布局
		addView(R.layout.psninvestmentmanageopen_info);
		btn_right.setVisibility(View.GONE);
		back.setVisibility(View.GONE);
		setLeftButtonPopupGone();
		// 初始化界面
		(findViewById(R.id.foot_layout)).setVisibility(View.GONE);
		init();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		textInfo = (TextView)findViewById(R.id.textView);
		textInfo.setText(Html.fromHtml(getString(R.string.investxieyi)));
	}

	/**
	 * 初始化协议页面所有的控件
	 */
	@SuppressWarnings("unchecked")
	private void init() {
		index = 0;
		StepTitleUtils.getInstance().initTitldStep(this,
				getResources().getStringArray(R.array.inverst));
		StepTitleUtils.getInstance().setTitleStep(1);
		agreeButton = (Button) findViewById(R.id.btnNo);
		noAgreeButton = (Button) findViewById(R.id.btnYes);
		((TextView) findViewById(R.id.tvFirst))
				.setText((String) ((Map<String, Object>) BaseDroidApp
						.getInstanse().getBizDataMap()
						.get(ConstantGloble.BIZ_LOGIN_DATA))
						.get(Inves.CUSTOMERNAME));
		// 不接受按钮事件
		noAgreeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				
				if (getIntent().getBooleanExtra(InvestConstant.FROMMYSELF, false)) {
					setResult(RESULT_CANCELED);
					finish();
				
				}else{
					
					// 返回到外汇行情页面
					setResult(RESULT_CANCELED);
					finish();
					removeAllActivity();
				}
				


			}
		});
		// 接受按钮事件
		agreeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 进入开通投资理财服务确认页面
				// Intent intent = new Intent(InvesAgreeActivity.this,
				// InvesConfirmActivity.class);
				// startActivityForResult(intent,
				// ConstantGloble.ACTIVITY_RESULT_CODE);
				// 获取convertionId
				isOtp = false;
				isSms = false;
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();

			}
		});
		btn_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InvesAgreeActivity.this.finish();

			}
		});

		// if(!getIntent().getBooleanExtra(InvestConstant.FROMMYSELF,
		// false)){//从其他模块跳转过来的
		// ((LinearLayout) this.findViewById(R.id.sliding_body)).setPadding(
		// 0,
		// 0,
		// 0,
		// getResources().getDimensionPixelSize(
		// R.dimen.common_bottom_padding_new)*2);
		// }
	}
	
	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox(){
		String mmconversationId = (String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		usbKeyText = (UsbKeyText)findViewById(R.id.sip_usbkey);
		usbKeyText.Init(mmconversationId, randomNumber, map, this);
		isOtp = usbKeyText.getIsOtp();
		isSms = usbKeyText.getIsSmc();
		initComfirm();
	}

	/**
	 * 初始化确认页面所有的控件
	 */
	private void initComfirm() {
		index = 1;
		// isSms = false;
		// isOtp = false;
		StepTitleUtils.getInstance().initTitldStep(this,
				getResources().getStringArray(R.array.inverst));
		StepTitleUtils.getInstance().setTitleStep(2);
		sureButton = (Button) findViewById(R.id.btnConfim);
		smsEdit = (SipBox) findViewById(R.id.etsms);
		// add by 2016年9月12日 luqp 修改
		smsEdit.setTextColor(getResources().getColor(android.R.color.black));
		smsEdit.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
		SipBoxUtils.initSipBoxWithTwoType(smsEdit, ConstantGloble.MIN_LENGTH,
				ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//		smsEdit.setCipherType(SystemConfig.CIPHERTYPE);
//		smsEdit.setTextColor(getResources().getColor(android.R.color.black));
//		smsEdit.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		smsEdit.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		smsEdit.setId(10002);
//		smsEdit.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		smsEdit.setBackgroundResource(R.drawable.bg_for_edittext);
//		smsEdit.setPasswordRegularExpression(CheckRegExp.OTP);
//		smsEdit.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
//		smsEdit.setSipDelegator(this);
//		smsEdit.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		smsEdit.setRandomKey_S(randomNumber);
		llSms = (LinearLayout) findViewById(R.id.llSms);
		
		Button smsBtn = (Button) findViewById(R.id.smsbtn);
		SmsCodeUtils.getInstance().addSmsCodeListner(smsBtn,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 发送获取短信验证码的请求
						sendSMSCToMobile();
					}
				});
		if(isSms){
			llSms.setVisibility(View.VISIBLE);
		}
		
			// etsip
			llOtp = (LinearLayout) findViewById(R.id.llOtp);
			
			sipBox = (SipBox) findViewById(R.id.etsip);
			sipBox.setCipherType(SystemConfig.CIPHERTYPE);
			sipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
			sipBox.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
			sipBox.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
			sipBox.setBackgroundResource(R.drawable.bg_for_edittext);
			sipBox.setPasswordRegularExpression(CheckRegExp.OTP);
			sipBox.setTextSize(Integer.valueOf(getResources().getString(
					R.string.sipboxtextsize)));
			sipBox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);

			sipBox.setSipDelegator(this);
			sipBox.setRandomKey_S(randomNumber);
			
		if (isOtp) {
			llOtp.setVisibility(View.VISIBLE);
		}
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if (isback) {
				// back.setVisibility(View.GONE);
				// btn_right.setVisibility(View.GONE);
				// // InvesAgreeActivity.this.finish();
				// }else{
				// 返回到开通投资理财服务协议页面
				addView(R.layout.psninvestmentmanageopen_info, 1);
				textInfo = (TextView)findViewById(R.id.textView);
				textInfo.setText(Html.fromHtml(getString(R.string.investxieyi)));
				back.setVisibility(View.GONE);
				btn_right.setVisibility(View.GONE);
				sipBox.setFocusable(false);
				smsEdit.setFocusable(false);
				init();
				// }
			}
		});
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 得到用户输入的动态口令
//				sms = smsEdit.getText().toString().trim();
//				RegexpBean rbSms = new RegexpBean(getResources().getString(
//						R.string.smstlable), sms, ConstantGloble.SIPSMCPSW);
//				RegexpBean sipRegexpBean = new RegexpBean(getResources()
//						.getString(R.string.active_code_regex), sipBox
//						.getText().toString(), ConstantGloble.SIPOTPPSW);
//
//				ArrayList<RegexpBean> list = new ArrayList<RegexpBean>();
//				if (isSms) {
//					list.add(rbSms);
//				}
//				if (isOtp) {
//					list.add(sipRegexpBean);
//				}
//				if (RegexpUtils.regexpDate(list)) {
//					if (isOtp) {
//						try {
//							pswOtp = sipBox.getValue().getEncryptPassword();
//							pswotpRC = sipBox.getValue().getEncryptRandomNum();
//							// 获取tockenId
//							requestPSNTokenId();
//						} catch (Exception e) {
//							LogGloble.exceptionPrint(e);
//							BaseDroidApp.getInstanse().createDialog(null,
//									e.getMessage());
//						}
//					} else if (isSms) {
//						try {
//							pswSms = smsEdit.getValue().getEncryptPassword();
//							pswSmsRC = smsEdit.getValue().getEncryptRandomNum();
//							// 获取tockenId
//							requestPSNTokenId();
//						} catch (Exception e) {
//							LogGloble.exceptionPrint(e);
//							BaseDroidApp.getInstanse().createDialog(null,
//									e.getMessage());
//						}
//					}
//
//				}
				/**安全工具校验数据*/
				usbKeyText.checkDataUsbKey(sipBox, smsEdit, new IUsbKeyTextSuccess() {
					
					@Override
					public void SuccessCallBack(String result, int errorCode) {
						// TODO Auto-generated method stub
						// 获取tockenId
						requestPSNTokenId();
					}
				});
				
			}
		});
		// back.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // 返回到开通投资理财服务协议页面
		// addView(R.layout.psninvestmentmanageopen_info, 1);
		// init();
		//
		// }
		// });

		
	btn_right.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					
					
					if (getIntent().getBooleanExtra(InvestConstant.FROMMYSELF, false)) {
						InvesAgreeActivity.this.finish();
					
					}else{
						
						setResult(RESULT_CANCELED);
						finish();
						removeAllActivity();;
					}
				}
			});

	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 获取安全因子
		requestGetSecurityFactor("PB060C");

	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
//		BaseHttpEngine.dissMissProgressDialog();
		BaseDroidApp.getInstanse().showSeurityChooseDialog(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						/*
						 * addView(R.layout.psninvestmentmanageopen, 0); //
						 * isback = false; back.setVisibility(View.VISIBLE);
						 * btn_right.setVisibility(View.VISIBLE);
						 * btn_right.setText(InvesAgreeActivity.this
						 * .getString(R.string.close)); initComfirm();
						 */
						// 请求预交易接口
						requestPsnInvestmentManageOpenConfirm();
					}
				});

	}

	/**
	 * 获取tocken
	 */
	private void requestPSNTokenId() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPSNTokenIdCallback");
	}

	/**
	 * 获取tokenId----回调
	 * 
	 * @param resultObj
	 *            :返回结果
	 */
	public void requestPSNTokenIdCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		tokenId = (String) biiResponseBody.getResult();
		if ("".equals(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			Toast.makeText(InvesAgreeActivity.this, R.string.forex_inves_token,
					Toast.LENGTH_SHORT).show();
		} else {
			// 再去发送开通的请求
			requestPsnInvestmentManageOpen();
		}

	}

	/**
	 * 开通投资服务确认（预备交易）
	 */
	private void requestPsnInvestmentManageOpenConfirm() {
//		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Inves.PSNINVESTMENTMANAGEOPENCONFIRM);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Inves._COMBINID, BaseDroidApp.getInstanse()
				.getSecurityChoosed());
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnInvestmentManageOpenConfirmCallback");

	}

	private ArrayList<String> factorListName;

	/**
	 * 判断是否开通投资理财服务确认（预备交易）---回调
	 * 
	 * @param resultObj
	 */
	private Map<String, Object> map;
	@SuppressWarnings("unchecked")
	public void requestPsnInvestmentManageOpenConfirmCallback(Object resultObj) {

		factorListName = new ArrayList<String>();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		map = (Map<String, Object>) biiResponseBody
				.getResult();
		List<Map<String, Object>> factorList = (List<Map<String, Object>>) map
				.get(Inves.FACTORLIST);
//		for (int i = 0; i < factorList.size(); i++) {
//			Map<String, Object> itemMap = factorList.get(i);
//			Map<String, String> securityMap = (Map<String, String>) itemMap
//					.get(Inves.FIELD);
//			String name = securityMap.get(Inves.NAME);
//			if (Inves.Smc.equals(name)) {
//				isSms = true;
//				// llSms.setVisibility(View.VISIBLE);
//			} else if (Inves.Otp.equals(name)) {
//				isOtp = true;
//				// llOtp.setVisibility(View.VISIBLE);
//
//			}
//			factorListName.add(name);
//		}
//		if (isOtp || isSms) {
//			// 请求随机数
//			requestForRandomNumber();
//		}
		requestForRandomNumber();
	}

	/**
	 * 开通投资服务
	 */
	private void requestPsnInvestmentManageOpen() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEOPEN_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Comm.TOKEN_REQ, tokenId);
//		if (isOtp) {
//			map.put(Comm.Otp, pswOtp);
//			map.put(Comm.Otp_Rc, pswotpRC);
//		}
//		if (isSms) {
//			map.put(Comm.Smc, pswSms);
//			map.put(Comm.Smc_Rc, pswSmsRC);
//		}
		/**安全工具参数获取*/
		usbKeyText.InitUsbKeyResult(map);
		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnInvestmentManageOpenCallback");

	}

	/**
	 * 开通投资理财服务的请求回调
	 */
	public void requestPsnInvestmentManageOpenCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		ModelBoc.updateOpenWealthStatus(true);
		if (getIntent().getBooleanExtra(InvestConstant.FROMMYSELF, false)) {
			setResult(RESULT_OK);
			finish();
			return;
		}
		addView(R.layout.psninvestmentmanageopen_result, 0);
		isReusltLayout = true;
		back.setVisibility(View.GONE);
		btn_right.setVisibility(View.VISIBLE);
		btn_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (getIntent().getBooleanExtra(InvestConstant.FROMMYSELF, false)) {
					setResult(RESULT_OK);
					finish();
				
				}else{
					
					setResult(RESULT_OK);
					finish();
					removeAllActivity();;
				}
				
			}
		});
	}

	/** 完成点击监听事件 */
	public void finishOnclick(View v) {
		setResult(RESULT_OK);
		finish();
//		ActivityTaskManager.getInstance().removeAllActivity(); 点击完成 关闭当前界面 不清堆栈
	}

	/**
	 * 在slidingbody中引入自己布局文件
	 * 
	 * @param resource
	 *            新加入的View
	 * @param leftOrRight
	 *            向左还是向右滑动 0 代表向左 1代表向右
	 * @return 引入布局
	 */
	public View addView(int resource, int leftOrRight) {
		final View view = LayoutInflater.from(this).inflate(resource, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);

		return view;
	}

	/**
	 * 在slidingbody中引入自己布局文件
	 * 
	 * @param resource
	 * @return 引入布局
	 */
	public View addView(int resource) {

		View view = LayoutInflater.from(this).inflate(resource, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		return view;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (isReusltLayout) {
			return false;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			switch (index) {
			case 0:
				finish();
				break;
			case 1:
				addView(R.layout.psninvestmentmanageopen_info, 1);
				textInfo = (TextView)findViewById(R.id.textView);
				textInfo.setText(Html.fromHtml(getString(R.string.investxieyi)));
				back.setVisibility(View.GONE);
				btn_right.setVisibility(View.GONE);
				sipBox.setFocusable(false);
				smsEdit.setFocusable(false);
				init();
			default:
				break;
			}
		}
		return true;
	}

	/**
	 * 请求密码控件随机数
	 */
	public void requestForRandomNumber() {

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.AQUIRE_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		// 获取 随机数
		HttpManager.requestBii(biiRequestBody, this,
				"queryRandomNumberCallBack");
	}

	/**
	 * 请求密码控件随机数 回调
	 * 
	 * @param resultObj
	 */
	public void queryRandomNumberCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		randomNumber = (String) biiResponseBody.getResult();

		addView(R.layout.psninvestmentmanageopen_1, 0);
		back.setVisibility(View.VISIBLE);
		btn_right.setVisibility(View.VISIBLE);
		btn_right.setText(InvesAgreeActivity.this.getString(R.string.close));
		/** 跟据返回初始化中银E盾加密控件 */
		initSipBox();
//		initComfirm()
//		if (isOtp) {
//			llOtp.setVisibility(View.VISIBLE);
//		}
//		if (isSms) {
//			llSms.setVisibility(View.VISIBLE);
//		}
		// 加密控件设置随机数
//		sipBox.setRandomKey_S(randomNumber);
//		smsEdit.setRandomKey_S(randomNumber);
		BaseHttpEngine.dissMissProgressDialog();

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(R.anim.no_animation, R.anim.slide_down_out);
	}
	protected void removeAllActivity(){
		ActivityTaskManager.getInstance().removeAllSecondActivity();	
	}
	
	@Override
	public ActivityTaskType getActivityTaskType() {
		return ActivityTaskType.TwoTask;
	}
}
