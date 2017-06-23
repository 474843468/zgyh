package com.chinamworld.bocmbci.biz.login.reg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import cfca.mobile.sip.SipBox;

import com.cfca.mobile.log.CodeException;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.bocnet.BocnetDataCenter;
import com.chinamworld.bocmbci.biz.login.LoginBaseAcitivity;
import com.chinamworld.bocmbci.biz.login.LoginDataCenter;
import com.chinamworld.bocmbci.constant.CheckRegExp;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.IHttpResponseCallBack;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 注册---注册信息
 * 
 * @author WJP
 * 
 */
public class RegisteVerifyActivity extends LoginBaseAcitivity {
	/** 证件类型 */
	private Spinner spidCty;
	private String strSpidCty;
	/** 银行卡号 */
	private EditText etbankNum;
	private String bankNum;
	/** 注册名字 */
	private String name;
	/** 银行类型 */
	private Spinner spbankCty;
	private String strSpbankCty;
	private String accountType;
	/** 证件号码 */
	private EditText etidNum;
	private String idNum;
	/** 验证码 */
	private EditText etCode;
	private String code;
	/** 验证码图片 */
	private ImageButton ibCode;
	/** 下一步 */
	private Button nextBtn;

	/** 加密控件 */
	private SipBox sipBox = null;
	/** 加密之后加密随机数 */
	private String password_RC = "";
	/** 输入框密码 */
	private String password = "";
	/** 加密控件里的随机数 */
	protected String randomNumber;
	/** 服务条约 */
	private TextView tvServerInfo;

	private CheckBox checkBox;

	/** 错误信息 */
	private String codeNull;
	private String bankphonepasswordWrong;

	/** 是否为电子银行账户 */
	private String isEbank;
	/** 市场细分 */
	private int segmentId;

	/** atm密码或者电话银行密码view */
	private TextView atmOrPhonePsw;
	private LinearLayout atmOrPhoneLayout;

	
	/** 是否为电子卡 */
	private Boolean isEcard;
	/** 手机号 */
	private String mobile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.self_reg_title);

		View view = LayoutInflater.from(this).inflate(
				R.layout.register_verify_activity, null);
		tabcontent.addView(view);
		requsetRegisterConversationId();

//		randomNumber = LoginDataCenter.getInstance().getRandomNumber();
//		// 初始化控件
//		init();
//		// 请求验证码
//		requestForImagecode();

		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getResources().getString(R.string.title_step1),
						this.getResources().getString(R.string.register_step2),
						this.getResources().getString(R.string.title_step3) });
		StepTitleUtils.getInstance().setTitleStep(1);

		codeNull = this.getResources().getString(R.string.code_null);
		bankphonepasswordWrong = this.getResources().getString(
				R.string.bankphone_password_wrong);

//		nextBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				excuseNext();
//			}
//		});
//
//		tvServerInfo.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO 服务条约
//				Intent intent = new Intent();
//				intent.setClass(RegisteVerifyActivity.this,
//						RegisterProtocolActivity.class);
//				startActivityForResult(intent, 1);
//				overridePendingTransition(R.anim.push_up_in,
//						R.anim.no_animation);
//			}
//		});
	}

	/**
	 * 请求conversationId ---找回密码
	 */
	private void requsetRegisterConversationId() {
		// 展示通信框
		BaseHttpEngine.showProgressDialog();
		httpTools.requestHttp(Login.AQUIRE_CONVERSATION_ID_API, "requsetRegisterConversationIdCallBack", null);
	}
	/**
	 * 请求conversationId返回 --- 找回密码
	 *
	 * @param resultObj
	 */
	public void requsetRegisterConversationIdCallBack(Object resultObj) {
		String loginPreConversationId = (String) httpTools.getResponseResult(resultObj);
		LoginDataCenter.getInstance().setConversationId(loginPreConversationId);
		requestRegisterRandomNumber();
	}

	private void requestRegisterRandomNumber() {
		String conversationId = (String)LoginDataCenter.getInstance()
				.getConversationId();
		// 获取 随机数
		httpTools.requestHttpWithConversationId(Login.AQUIRE_RANDOM_NUMBER_API, null, conversationId, new IHttpResponseCallBack<String>() {

			@Override
			public void httpResponseSuccess(String result,
											String method) {
				// TODO Auto-generated method stub
				BaseHttpEngine.dissMissProgressDialog();
				String recoderNumber = (String)result;
				LoginDataCenter.getInstance().setRandomNumber(recoderNumber);
				randomNumber = LoginDataCenter.getInstance().getRandomNumber();
				// 初始化控件
				init();
				// 请求验证码
				requestForImagecode();
				nextBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						excuseNext();
					}
				});

				tvServerInfo.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO 服务条约
						Intent intent = new Intent();
						intent.setClass(RegisteVerifyActivity.this,
								RegisterProtocolActivity.class);
						startActivityForResult(intent, 1);
						overridePendingTransition(R.anim.push_up_in,
								R.anim.no_animation);
					}
				});
			}
		});
	}

	/**
	 * @Title: excuseNext
	 * @Description: 执行下一步
	 * @param
	 * @return void
	 */
	private void excuseNext() {

		try {

			bankNum = etbankNum.getText().toString().trim();
			idNum = etidNum.getText().toString().trim();
			code = etCode.getText().toString().trim();

			if (StringUtil.isNullOrEmpty(bankNum)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("请输入银行卡号");
				return;
			}

			// 如果是119 借记卡
			// 如果是103或者是107 104信用卡
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();

			RegexpBean regBankNum = new RegexpBean(
					RegisteVerifyActivity.this
							.getString(R.string.card_number_str),
					bankNum, "chipCard");
			lists.add(regBankNum);
			if (!RegexpUtils.regexpDate(lists))
				return;
			if (TextUtils.isEmpty(accountType)) {
				requestQueryCardTypeByCardNum();
				return;
			}
			lists.clear();

			if (StringUtil.isNullOrEmpty(idNum)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						RegisteVerifyActivity.this.getResources().getString(
								R.string.to_input_id_number));
				return;
			} else if ("1".equals(strSpidCty)) {
				RegexpBean regIdNum2 = new RegexpBean("您输入的证件号码", idNum,
						"identityNumber");
				lists.add(regIdNum2);

				if (!RegexpUtils.regexpDate(lists))
					return;
			}
			lists.clear();

			if (accountType.equals(ConstantGloble.ACC_TYPE_BRO)) {
				RegexpBean passReg = new RegexpBean(
						RegisteVerifyActivity.this
								.getString(R.string.acc_atmpwd_regex),
						sipBox.getText().toString().trim(), "atmpass");
				lists.add(passReg);
			} else {
				RegexpBean passReg = new RegexpBean(
						RegisteVerifyActivity.this
								.getString(R.string.acc_phonepwd_regex),
						sipBox.getText().toString().trim(), "atmpass");
				lists.add(passReg);
			}
			if (!RegexpUtils.regexpDate(lists))
				return;
			lists.clear();

			// if (accountType.equals(ConstantGloble.ACC_TYPE_BRO)) {
			// RegexpBean regBankNum = new
			// RegexpBean(RegisteVerifyActivity.this.getString(R.string.card_number_str),
			// bankNum, "chipCard");
			// lists.add(regBankNum);
			// RegexpBean regIdNum = new RegexpBean("证件号码", idNum,
			// "noNameCard");
			// lists.add(regIdNum);
			// if("1".equals(strSpidCty)){
			// //证件类型为身份证的时候校验
			// RegexpBean regIdNum2 = new RegexpBean("您输入的证件号码", idNum,
			// "identityNumber");
			// lists.add(regIdNum2);
			// }else{
			// RegexpBean regIdNum2 = new RegexpBean("证件号码", idNum,
			// "noNameCard");
			// lists.add(regIdNum2);
			// }
			// RegexpBean passReg = new
			// RegexpBean(RegisteVerifyActivity.this.getString(R.string.acc_atmpwd_regex),
			// sipBox.getText().toString().trim(), "atmpass");
			// lists.add(passReg);
			// } else {
			// RegexpBean regBankNum = new
			// RegexpBean(RegisteVerifyActivity.this.getString(R.string.card_number_str),
			// bankNum, "chipCard");
			// lists.add(regBankNum);
			// RegexpBean regIdNum = new RegexpBean("证件号码", idNum,
			// "noNameCard");
			// lists.add(regIdNum);
			// if("1".equals(strSpidCty)){
			// //证件类型为身份证的时候校验
			// RegexpBean regIdNum2 = new RegexpBean("您输入的证件号码", idNum,
			// "identityNumber");
			// lists.add(regIdNum2);
			// }else{
			// RegexpBean regIdNum2 = new RegexpBean("证件号码", idNum,
			// "noNameCard");
			// lists.add(regIdNum2);
			// }
			// RegexpBean passReg = new
			// RegexpBean(RegisteVerifyActivity.this.getString(R.string.acc_phonepwd_regex),
			// sipBox.getText().toString().trim(), "atmpass");
			// lists.add(passReg);
			// }
			if (RegexpUtils.regexpDate(lists)) {// 校验通过
				password_RC = sipBox.getValue().getEncryptRandomNum();
				password = sipBox.getValue().getEncryptPassword();
				if (StringUtil.isNullOrEmpty(code)) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(codeNull);
					return;
				}

				if (4 > code.length()) {
					BaseDroidApp.getInstanse().showInfoMessageDialog("验证码无效");
					return;
				}

				if (checkBox.isChecked()) {
					
//					if(accountType.equals(ConstantGloble.ACC_TYPE_BRO)){
//					// 是借记卡判断 查询是否为电子卡 
//						
//						requstPsnQueryDebitCardType(); 
//					}else{
						requstForRegisterVerify();	
//					}
					
				} else {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							getResources().getString(
									R.string.register_server_info_toast_out));
				}

			}

		} catch (CodeException e) {
			BaseDroidApp.getInstanse().createDialog(null,
					bankphonepasswordWrong);
			LogGloble.exceptionPrint(e);
		}

	}

	
	/**
	 * 查询借记卡类型（是否为电子卡）
	 */
	
	private void requstPsnQueryDebitCardType() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.PSN_QUERY_DEBIT_CARD_TYPE);
//		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
//				.getBizDataMap().get(ConstantGloble.LOGIN_PRECONVERSATIONID));
		biiRequestBody.setConversationId(LoginDataCenter.getInstance().getConversationId());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Login.ACCOUNT_NUM, bankNum);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requstPsnQueryDebitCardTypeCallBack");
		
	}
	
	/**
	 * 查询借记卡类型（是否为电子卡） 回调
	 * 
	 * @param resultObj
	 */
	public void requstPsnQueryDebitCardTypeCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		
		String cardType=(String)result.get(Login.CARD_TYPE);
		//	"D":电子卡
		if(cardType.equals("D")){
			isEcard=true;
			accountType = ConstantGloble.ACC_TYPE_BRO;			
			atmOrPhonePsw.setText(this.getResources().getString(
						R.string.acc_atmpwd_label));
			atmOrPhoneLayout.setVisibility(View.VISIBLE);
		}else{
			requestQueryCardTypeByCardNum();
		}
		

		
//		requstForRegisterVerify();	
		
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		// 加密控件
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_sip);

		atmOrPhoneLayout = (LinearLayout) findViewById(R.id.layout_atm_or_phone);
		atmOrPhonePsw = (TextView) findViewById(R.id.text_atm_or_phonebank);
		etidNum = (EditText) findViewById(R.id.findpwd_et_idnum);
		etCode = (EditText) findViewById(R.id.findpwd_et_code);
		ibCode = (ImageButton) findViewById(R.id.findpwd_code);
		tvServerInfo = (TextView) findViewById(R.id.register_server_info);
		checkBox = (CheckBox) findViewById(R.id.checkbox_register_info);

		sipBox = new SipBox(this);
		sipBox.setCipherType(SystemConfig.CIPHERTYPE);
		LinearLayout.LayoutParams param = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		sipBox.setLayoutParams(param);
		sipBox.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
		sipBox.setPadding(getResources().getDimensionPixelSize(R.dimen.edittext_paddinglr),0,0,0);
		sipBox.setTextColor(getResources().getColor(android.R.color.black));
		SipBoxUtils.initSipBoxWithTwoType(sipBox, ConstantGloble.MIN_LENGTH,
				ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//		sipBox.setTextColor(getResources().getColor(android.R.color.black));
//		sipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		sipBox.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		sipBox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		sipBox.setId(10002);
//		sipBox.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		sipBox.setPasswordRegularExpression(CheckRegExp.OTP);
//		// sipBox.setHint(this.getResources().getString(R.string.hint_phone_pwd));
//		sipBox.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.bg_for_edittext));
//		sipBox.setRandomKey_S(randomNumber);
//		sipBox.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
//		sipBox.setSipDelegator(this);
//		sipBox.setPadding(getResources().getDimensionPixelSize(R.dimen.edittext_paddinglr), 0,0, 0);
		linearLayout.addView(sipBox);

		spidCty = (Spinner) findViewById(R.id.findpwd_sp_idcty);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				R.layout.dept_spinner, LocalData.idList);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spidCty.setAdapter(adapter1);
		spidCty.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				strSpidCty = LocalData.identityType.get(LocalData.idList
						.get(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				strSpidCty = LocalData.identityType.get(LocalData.idList.get(0));
			}
		});

		etbankNum = (EditText) findViewById(R.id.findpwd_et_banknum);
		etbankNum.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					String str = etbankNum.getText().toString();
					if (!TextUtils.isEmpty(str)) {
						int length = str.trim().length();
						if (length != 15 && length != 16 && length != 19) {
							BaseDroidApp
									.getInstanse()
									.showInfoMessageDialog(
											RegisteVerifyActivity.this
													.getResources()
													.getString(
															R.string.bankcard_num_count));
						} else {
							bankNum = str.trim();
							requstPsnQueryDebitCardType(); 
//							requestQueryCardTypeByCardNum();
						}
					}
				}
			}
		});

		spbankCty = (Spinner) findViewById(R.id.findpwd_sp_bankcty);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
				R.layout.dept_spinner, LocalData.cardList);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spbankCty.setAdapter(adapter2);
		spbankCty.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				strSpbankCty = LocalData.cardMap.get(LocalData.cardList
						.get(position));
				if (strSpbankCty.equals(ConstantGloble.ACC_TYPE_BRO)) {
					InputFilter[] filters = { new InputFilter.LengthFilter(19) };
					etbankNum.setFilters(filters);
				} else {
					InputFilter[] filters = { new InputFilter.LengthFilter(16) };
					etbankNum.setFilters(filters);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				strSpbankCty = LocalData.cardMap.get(LocalData.cardList.get(0));
			}
		});

		ibCode.setOnClickListener(imageCodeClickListener);

		tvServerInfo.setText(Html.fromHtml("<u>"
				+ this.getResources().getString(R.string.regiser_server_info)
				+ "</u>"));

		nextBtn = (Button) findViewById(R.id.findpwd_btn_next);
		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent intent = new Intent();
				// intent.setClass(RegisteVerifyActivity.this,
				// LoginActivity.class);
				// startActivity(intent);
				finish();
			}
		});

	}

	/**
	 * 验证码
	 */
	private OnClickListener imageCodeClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			requestForImagecode();
		}
	};

	/**
	 * 请求验证码
	 */
	public void requestForImagecode() {
		// 展示通讯框
		BaseHttpEngine.showProgressDialog();

		HttpManager.requestImage(Comm.AQUIRE_IMAGE_CODE,
				ConstantGloble.GO_METHOD_GET, null, this,
				"imagecodeCallBackMethod");
	}

	/**
	 * 请求验证码回调
	 * 
	 * @param resultObj
	 */
	public void imagecodeCallBackMethod(Object resultObj) {
		// 取消通讯框
		BaseHttpEngine.dissMissProgressDialog();

		Bitmap bitmap = (Bitmap) resultObj;
		BitmapDrawable imageCodeDrawable = new BitmapDrawable(bitmap);
		ibCode.setBackgroundDrawable(imageCodeDrawable);
	}

	/**
	 * @Title: requestQueryCardTypeByCardNum
	 * @Description: 通过卡号查询卡类型
	 * @param
	 * @return void
	 */
	private void requestQueryCardTypeByCardNum() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.PSN_QUERY_CARDTYPE_BY_CARDNUM);
		biiRequestBody.setConversationId(LoginDataCenter.getInstance().getConversationId());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Login.ACCOUNT_NUM, bankNum);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestQueryCardTypeByCardNumCallBack");
	}

	/**
	 * @Title: requestQueryCardTypeByCardNumCallBack
	 * @Description: 通过卡号查询卡类型回调
	 * @param @param resultObj
	 * @return void
	 */
	public void requestQueryCardTypeByCardNumCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		accountType = (String) result.get(Login.ACCOUNT_TYPE);
		if (accountType.equals(ConstantGloble.ACC_TYPE_BRO)) {
			atmOrPhonePsw.setText(this.getResources().getString(
					R.string.acc_atmpwd_label));
		} else {
			atmOrPhonePsw.setText(this.getResources().getString(
					R.string.phone_bank_password));
		}
		atmOrPhoneLayout.setVisibility(View.VISIBLE);
	}

	/**
	 * 请求注册
	 */
	private void requstForRegisterVerify() {
		
//		if(!accountType.equals(ConstantGloble.ACC_TYPE_BRO)){
			BaseHttpEngine.showProgressDialog();
//		}
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.REGISTER_VERIFY_API);
		biiRequestBody.setConversationId(LoginDataCenter.getInstance()
				.getConversationId());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Login.ACCOUNT_TYPE, accountType);
		map.put(Login.ACCOUNT_NUM, bankNum);
		if (accountType.equals(ConstantGloble.ACC_TYPE_BRO)) {
			map.put(Login.ATM_PASSWORD, password);
			map.put(Login.ATM_PASSWORD_RC, password_RC);
		} else {
			map.put(Login.PHONE_BANK_PASSWORD, password);
			map.put(Login.PHONE_BANK_PASSWORD_RC, password_RC);
		}
		map.put(Login.IDENTIFY_TYPE, strSpidCty);
		map.put(Login.IDENTIFY_NUM, idNum);
		map.put(Login.VALIDATIONCHAR, code);
		SipBoxUtils.setSipBoxParams(map);// 添加密码控件需要送上字段
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requstForRegisterVerifyCallback");
	}

	/**
	 * 请求注册 回调
	 * 
	 * @param resultObj
	 */
	public void requstForRegisterVerifyCallback(Object resultObj) {
		// 取消通讯框
		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		isEbank = (String) result.get(Login.IS_EBANK);
		BaseDroidApp.getInstanse().getBizDataMap()
				.put("registerVerifyCallBack", result);
		name = (String) result.get(Login.REGISTER_NAME);
	    mobile=(String) result.get(Login.REGISTER_MOBILE);
		if (ConstantGloble.IS_EBANK_1.equals(isEbank)) {
			// BaseDroidApp.getInstanse().showInfoMessageDialog(
			// RegisteVerifyActivity.this.getResources(R.string.already_open));
			// return;
		} else if (ConstantGloble.IS_EBANK_2.equals(isEbank)) {
			requestRegistedDebitCardResult();
			return;
		}

		Intent intent = new Intent();
		intent.putExtra(Login.REGISTER_NAME, name);
		intent.putExtra(Login.ACCOUNT_TYPE, accountType);
		intent.putExtra(Login.ACCOUNT_NUM, bankNum);
		intent.putExtra(Login.ISECARD, isEcard);
		intent.putExtra(Login.REGISTER_MOBILE, mobile);
		intent.putExtra(RegisterSetNamePwdActivity.IS_NEED_ACTIVE, false);
		intent.putExtra(BocnetDataCenter.ModleName, getIntent()
				.getBooleanExtra(BocnetDataCenter.ModleName, false));
		// sipBox.clearText();
		intent.setClass(RegisteVerifyActivity.this,
				RegisterSetNamePwdActivity.class);
		startActivityForResult(intent, 0);
	}

	/**
	 * @Title: requestRegistedDebitCardResult
	 * @Description: 是否为关联账户请求
	 * @param
	 * @return void
	 */
	private void requestRegistedDebitCardResult() {
		BaseHttpEngine.showProgressDialog();

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.PSN_IS_REGISTED_DEBITCARD);
		biiRequestBody.setConversationId(LoginDataCenter.getInstance()
				.getConversationId());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Login.ACCOUNT_TYPE, accountType);
		map.put(Login.ACCOUNT_NUM, bankNum);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requstRegistedDebitCardResultCallback");
	}

	/**
	 * @Title: requstRegistedDebitCardResultCallback
	 * @Description: 是否为关联用户的接口回调
	 * @param @param resultObj
	 * @return void
	 */
	public void requstRegistedDebitCardResultCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		String strSegment = (String) result.get(Login.SEGMENT_ID);
		if (!StringUtil.isNullOrEmpty(strSegment)) {
			segmentId = Integer.parseInt(strSegment);
		}
		String merchId = (String) result.get(Login.MERCH_ID);
		String tokenId = (String) result.get(Login.TOKEN_ID);
		String issueTokenDate = (String) result.get(Login.ISSUE_TOKEN_DATE);
		// update by xby start 20140227 只有贵宾版才去校验是否有安全因子
		if (segmentId == 10) {// 查询版
			Intent intent = new Intent();
			intent.putExtra(Login.REGISTER_NAME, name);
			intent.putExtra(Login.ACCOUNT_TYPE, accountType);
			intent.putExtra(Login.ACCOUNT_NUM, bankNum);
			intent.putExtra(Login.ISECARD, isEcard);
			intent.putExtra(Login.REGISTER_MOBILE, mobile);
			intent.putExtra(RegisterSetNamePwdActivity.IS_NEED_ACTIVE, false);
			intent.putExtra(BocnetDataCenter.ModleName, getIntent()
					.getBooleanExtra(BocnetDataCenter.ModleName, false));
			// sipBox.clearText();
			intent.setClass(RegisteVerifyActivity.this,
					RegisterSetNamePwdActivity.class);
			startActivityForResult(intent, 0);
		} else {// 贵宾版
			if (TextUtils.isEmpty(merchId) && TextUtils.isEmpty(tokenId)
					&& TextUtils.isEmpty(issueTokenDate)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						this.getResources().getString(R.string.no_token));
				return;
			}
			Intent intent = new Intent();
			intent.putExtra(Login.REGISTER_NAME, name);
			intent.putExtra(Login.ACCOUNT_TYPE, accountType);
			intent.putExtra(Login.ACCOUNT_NUM, bankNum);
			intent.putExtra(Login.ISECARD, isEcard);
			intent.putExtra(Login.REGISTER_MOBILE, mobile);
			intent.putExtra(RegisterSetNamePwdActivity.IS_NEED_ACTIVE, true);
			intent.putExtra(BocnetDataCenter.ModleName, getIntent()
					.getBooleanExtra(BocnetDataCenter.ModleName, false));
			// sipBox.clearText();
			intent.setClass(RegisteVerifyActivity.this,
					RegisterSetNamePwdActivity.class);
			startActivityForResult(intent, 0);
		}
		// update by xby end 20140227 只有贵宾版才去校验是否有安全因子
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == 100) {
			checkBox.setChecked(true);
		} else if (requestCode == 1 && resultCode == 101) {
			checkBox.setChecked(false);
		}

		if (REGIST_SUCCESS == resultCode) {
			setResult(REGIST_SUCCESS);
			finish();
		}
		if (BACK_TO_THIS == resultCode) {
			etCode.setText("");
			requestForImagecode();
		}
	}

	/**
	 * Bii请求前拦截-可处理统一的错误弹出框 有返回数据（response） 对于包含exception 的数据进行统一弹框
	 * 
	 * @param BiiResponse
	 *            resultObj
	 * @return 是否终止业务流程
	 */
	@Override
	public boolean doBiihttpRequestCallBackPre(BiiResponse response) {
		List<BiiResponseBody> biiResponseBodyList = response.getResponse();
		if (!StringUtil.isNullOrEmpty(biiResponseBodyList)) {
			for (BiiResponseBody body : biiResponseBodyList) {

				if (Login.REGISTER_VERIFY_API.equals(body.getMethod())) {

					if (!ConstantGloble.STATUS_SUCCESS.equals(body.getStatus())) {
						// 消除通信框
						BaseHttpEngine.dissMissProgressDialog();

						BiiError biiError = body.getError();
						// 判断是否存在error
						if (biiError != null) {
							if (biiError.getCode() != null) {
								// 非会话超时错误拦截
								BaseDroidApp.getInstanse().createDialog(
										biiError.getCode(),
										biiError.getMessage(),
										new OnClickListener() {

											@Override
											public void onClick(View v) {
												BaseDroidApp.getInstanse()
														.dismissErrorDialog();
												requestForImagecode();
												etCode.setText("");
											}
										});

								return true;
							}
							// 弹出公共的错误框
							BaseDroidApp.getInstanse().dismissErrorDialog();
							BaseDroidApp.getInstanse().createDialog("",
									biiError.getMessage(),
									new OnClickListener() {
										@Override
										public void onClick(View v) {
											BaseDroidApp.getInstanse()
													.dismissErrorDialog();
											requestForImagecode();
											etCode.setText("");
										}
									});
						} else {
							BaseDroidApp.getInstanse().dismissErrorDialog();
							// 避免没有错误信息返回时给个默认的提示
							BaseDroidApp.getInstanse().createDialog(
									"",
									getResources().getString(
											R.string.request_error),
									new OnClickListener() {

										@Override
										public void onClick(View v) {
											BaseDroidApp.getInstanse()
													.dismissErrorDialog();
											requestForImagecode();
											etCode.setText("");
										}
									});
						}

						return true;
					}
				} else {
					return super.doBiihttpRequestCallBackPre(response);
				}
			}
		}

		return false;
	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// Intent intent = new Intent();
	// intent.setClass(this, LoginActivity.class);
	// startActivity(intent);
	// finish();
	// }
	// return true;
	// }

}
