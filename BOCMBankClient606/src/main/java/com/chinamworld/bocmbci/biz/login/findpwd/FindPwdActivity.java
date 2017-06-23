package com.chinamworld.bocmbci.biz.login.findpwd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.chinamworld.bocmbci.utils.DeviceInfoTools;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.SharedPreUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.adapter.SecurityFactorAdapter;

/**
 * 重置密码
 * 
 * @author WJP
 * 
 */
public class FindPwdActivity extends LoginBaseAcitivity {
	private static final String TAG = "FindPwdActivity";
	/** 手机号 */
	private EditText etphoneNum;
	private String phoneNum;
	/** 银行卡类型 */
	private Spinner spidCty;
	private String strSpidCty;
	/** 银行卡号 */
	private EditText etbankNum;
	private String bankNum;
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
	/** 返回 */
	private Button ibBack;

	/** 加密控件 */
	private SipBox sipBox = null;
	/** 加密之后加密随机数 */
	private String password_RC = "";
	/** 输入框密码 */
	private String password = "";
	/** 加密控件里的随机数 */
	private String randomNumber;
	/** 会话id */
	private String conversationId;

	/** 手机号输入有误 错误信息 */
	private String codeNull;
	private String bankphonepasswordWrong;

	/** atm密码或者电话银行密码textview */
	private TextView atmOrPhonePsw;
	/** 验证码textview */
	private TextView codeTv;
	/** 绑定id */
	private String combineId;

	private String combinFlag;

	private List<Map<String, Object>> combineList;

	private SecurityFactorAdapter sfAdapter;

	private LinearLayout atmOrPhoneLayout;
	/** 当前证件类型 */
	private String curIdType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle(R.string.retrieve_pwd_title);

		View view = LayoutInflater.from(this).inflate(
				R.layout.findpwd_activity, null);
		tabcontent.addView(view);

		// 申请conversationId
		requsetFindpwdConversationId();
//		randomNumber = LoginDataCenter.getInstance().getRandomNumber();
//		conversationId = LoginDataCenter.getInstance().getConversationId();

		codeNull = this.getResources().getString(R.string.code_null);
		bankphonepasswordWrong = this.getResources().getString(
				R.string.bankphone_password_wrong);

//		init();
//		// 请求验证码
//		requestForImagecode();

		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getResources().getString(R.string.title_step1),
						this.getResources().getString(R.string.findpwd_step2),
						this.getResources().getString(R.string.title_step3) });
		StepTitleUtils.getInstance().setTitleStep(1);
	}

	/**
	 * 请求conversationId找回密码*/
	private void requsetFindpwdConversationId(){
		BaseHttpEngine.showProgressDialog();
		httpTools.requestHttp("PSNCreatConversationLoginPre", "requsetFindpwdConversationIdCallBack", null);
	}

	public void requsetFindpwdConversationIdCallBack(Object resultObj){
		String loginPreConversationId = (String)httpTools.getResponseResult(resultObj);
		LoginDataCenter.getInstance().setConversationId(loginPreConversationId);
		requestFindPwdRandomNumber();
	}

	/**
	 * 请求随机数--重置密码*/
	private void requestFindPwdRandomNumber(){
		String loginPreConversationId = (String)LoginDataCenter.getInstance().getConversationId();
		httpTools.requestHttpWithConversationId("PSNGetRandomLoginPre", null, loginPreConversationId, new IHttpResponseCallBack<String>() {

			@Override
			public void httpResponseSuccess(String result, String method) {
				// TODO Auto-generated method stub
				BaseHttpEngine.dissMissProgressDialog();
				String randomNumbert = (String)result;

				LoginDataCenter.getInstance().setRandomNumber(randomNumbert);
				randomNumber = LoginDataCenter.getInstance().getRandomNumber();
				conversationId = LoginDataCenter.getInstance().getConversationId();
				init();
				// 请求验证码
				requestForImagecode();
			}
		});
	}


	/**
	 * 初始化控件
	 */
	private void init() {
		etphoneNum = (EditText) findViewById(R.id.findpwd_et_phonenum);
		// 加密控件 -- start
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_sip);
		sipBox = new SipBox(this);
		sipBox.setCipherType(SystemConfig.CIPHERTYPE);
		LinearLayout.LayoutParams param = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		sipBox.setLayoutParams(param);
		// add by 2016年9月12日 luqp 修改
		sipBox.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
		sipBox.setTextColor(getResources().getColor(android.R.color.black));
		SipBoxUtils.initSipBoxWithTwoType(sipBox, ConstantGloble.MIN_LENGTH,
				ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//		sipBox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		sipBox.setTextColor(getResources().getColor(android.R.color.black));
//		sipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		sipBox.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		sipBox.setId(10002);
//		sipBox.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		sipBox.setBackgroundResource(R.drawable.bg_for_edittext);
//		sipBox.setPasswordRegularExpression(CheckRegExp.OTP);
//		sipBox.setRandomKey_S(randomNumber);
//		sipBox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		sipBox.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
//		sipBox.setSipDelegator(this);
		// sipBox.setHint(this.getResources().getString(R.string.hint_phone_pwd));
		linearLayout.addView(sipBox);
		// 加密控件 -- end

		// 证件类型
		spidCty = (Spinner) findViewById(R.id.findpwd_sp_idcty);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				R.layout.dept_spinner, LocalData.idList);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spidCty.setAdapter(adapter1);
		spidCty.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				curIdType = LocalData.idList.get(position);
				strSpidCty = LocalData.identityType.get(LocalData.idList
						.get(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				curIdType = LocalData.idList.get(0);
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
											FindPwdActivity.this
													.getResources()
													.getString(
															R.string.bankcard_num_count));
						} else {
							bankNum = str.trim();
							requestQueryCardTypeByCardNum();
						}
					}
				}
			}
		});
		// 银行卡类型
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
		atmOrPhoneLayout = (LinearLayout) findViewById(R.id.layout_atm_or_phone);
		atmOrPhonePsw = (TextView) findViewById(R.id.text_atm_or_phonebank);
		etidNum = (EditText) findViewById(R.id.findpwd_et_idnum);
		codeTv = (TextView) findViewById(R.id.ib_image_code_text);
		etCode = (EditText) findViewById(R.id.ed_image_code);
		ibCode = (ImageButton) findViewById(R.id.ib_image_code);
		ibCode.setOnClickListener(imageCodeClickListener);

		nextBtn = (Button) findViewById(R.id.findpwd_btn_next);
		ibBack = (Button) findViewById(R.id.ib_back);
		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		nextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					phoneNum = etphoneNum.getText().toString().trim();
					bankNum = etbankNum.getText().toString().trim();
					idNum = etidNum.getText().toString().trim();
					code = etCode.getText().toString().trim();

					ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
					// RegexpBean regTel = new
					// RegexpBean(FindPwdActivity.this.getString(R.string.tel_num_str),
					// phoneNum, "shoujiH_01_15");
					RegexpBean regTel = new RegexpBean(FindPwdActivity.this
							.getString(R.string.tel_num_str), phoneNum,
							"longMobile");
					lists.add(regTel);

					if (!RegexpUtils.regexpDate(lists)) // 校验手机号
						return;
					lists.clear();

					if (StringUtil.isNullOrEmpty(bankNum)) {
						BaseDroidApp.getInstanse().showInfoMessageDialog(
								"请输入银行卡号");
						return;
					} else {
						int length = bankNum.length();
						if (length != 15 && length != 16 && length != 19) {
							BaseDroidApp
									.getInstanse()
									.showInfoMessageDialog(
											FindPwdActivity.this
													.getResources()
													.getString(
															R.string.bankcard_num_count));
							return;
						}
					}

					// 如果是119 借记卡
					// 如果是103或者是107 104信用卡
					if (TextUtils.isEmpty(accountType)) {
						requestQueryCardTypeByCardNum();
						return;
					}

					RegexpBean regBankNum = new RegexpBean(FindPwdActivity.this
							.getString(R.string.card_number_str), bankNum,
							"chipCard");
					lists.add(regBankNum);
					if (!RegexpUtils.regexpDate(lists))
						return;
					lists.clear();

					if (StringUtil.isNullOrEmpty(idNum)) {
						BaseDroidApp.getInstanse().showInfoMessageDialog(
								FindPwdActivity.this.getResources().getString(
										R.string.to_input_id_number));
						return;
					} else if ("1".equals(strSpidCty)) {
						RegexpBean regIdNum2 = new RegexpBean("您输入的证件号码",
								idNum, "identityNumber");
						lists.add(regIdNum2);

						if (!RegexpUtils.regexpDate(lists))
							return;
					}
					lists.clear();

					String pwNullMsg = null;

					if (accountType.equals(ConstantGloble.ACC_TYPE_BRO)) {
						pwNullMsg = "请输入"
								+ getResources().getString(
										R.string.acc_atmpwd_regex);
						RegexpBean passReg = new RegexpBean(
								FindPwdActivity.this
										.getString(R.string.acc_atmpwd_regex),
								sipBox.getText().toString().trim(), "atmpass");
						lists.add(passReg);
					} else {
						pwNullMsg = "请输入"
								+ getResources().getString(
										R.string.acc_phonepwd_regex);
						RegexpBean passReg = new RegexpBean(
								FindPwdActivity.this
										.getString(R.string.acc_phonepwd_regex),
								sipBox.getText().toString().trim(), "atmpass");
						lists.add(passReg);
					}
					if (!RegexpUtils.regexpDate(lists)) // 校验手机号
						return;
					lists.clear();

					if (RegexpUtils.regexpDate(lists)) {// 校验通过

						if (StringUtil.isNullOrEmpty(sipBox.getText())
								|| 0 >= sipBox.getText().toString().length()) {
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									pwNullMsg);
							return;
						}
						password_RC = sipBox.getValue().getEncryptRandomNum();
						password = sipBox.getValue().getEncryptPassword();
						if (StringUtil.isNullOrEmpty(code)) {
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									codeNull);
							return;
						}
						if (4 != code.length()) {
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									"验证码无效");
							return;
						}
						requestForFindpwdNext();
					}
				} catch (CodeException e) {
					LogGloble.exceptionPrint(e);
					// TODO 信息提示待确认
					BaseDroidApp.getInstanse().createDialog(null,
							bankphonepasswordWrong);
				}
			}
		});
	}

	/**
	 * @Title: requestQueryCardTypeByCardNum
	 * @Description: 通过卡号查询卡类型
	 * @param
	 * @return void
	 */
	private void requestQueryCardTypeByCardNum() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.PSN_QUERY_CARDTYPE_BY_CARDNUM);
		biiRequestBody.setConversationId(conversationId);
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
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		accountType = (String) result.get(Login.ACCOUNT_TYPE);
		if (accountType.equals(ConstantGloble.ACC_TYPE_BRO)) {
			atmOrPhonePsw.setText(getResources().getString(
					R.string.acc_atmpwd_label));
			// sipBox.setHint(this.getResources().getString(R.string.hint_phone_pwd));
		} else {
			atmOrPhonePsw.setText(getResources().getString(
					R.string.phone_bank_password));
			// sipBox.setHint(this.getResources().getString(R.string.hint_phone_bank_pwd));
		}
		atmOrPhoneLayout.setVisibility(View.VISIBLE);
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
		codeTv.setVisibility(View.VISIBLE);
		ibCode.setVisibility(View.GONE);
		HttpManager.requestImage(Comm.AQUIRE_IMAGE_CODE,
				ConstantGloble.GO_METHOD_GET, null, this,
				"imagecodeCallBackMethod");
	}

	/**
	 * 请求验证码 回调
	 * 
	 * @param resultObj
	 *            返回结果
	 */
	public void imagecodeCallBackMethod(Object resultObj) {
		// 取消通讯框
		BaseHttpEngine.dissMissProgressDialog();
		Bitmap bitmap = (Bitmap) resultObj;
		BitmapDrawable imageCodeDrawable = new BitmapDrawable(bitmap);
		codeTv.setVisibility(View.GONE);
		ibCode.setVisibility(View.VISIBLE);
		ibCode.setBackgroundDrawable(imageCodeDrawable);
	}

	/**
	 * 找回密码
	 */
	private void requestForFindpwdNext() {
		// 展示通讯框
		BaseHttpEngine.showProgressDialog();

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.PWD_VERVIFY);
		biiRequestBody.setConversationId(conversationId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Login.LOGIN_NAME, phoneNum);
		map.put(Login.ACCOUNT_TYPE, accountType);
		map.put(Login.ACCOUNT_NUM, bankNum);
		if (accountType.equals(ConstantGloble.ACC_TYPE_BRO)) {
			map.put(Login.ATM_PASSWORD, password);
			map.put(Login.ATM_PASSWORD_RC, password_RC);
		} else {
			map.put(Login.PHONE_BANK_PASSWORD, password);
			map.put(Login.PHONE_BANK_PASSWORD_RC, password_RC);
		}
		map.put("devicePrint", "");
		map.put(Login.IDENTIFY_TYPE, strSpidCty);
		map.put(Login.IDENTIFY_NUM, idNum);
		map.put(Login.CHANNEL, "2");
		map.put(Login.VALIDATIONCHAR, code);
		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForFindpwdNextCallback");
	}

	/**
	 * 找回密码回调
	 * 
	 * @param resultObj
	 *            返回结果
	 */
	@SuppressWarnings("unchecked")
	public void requestForFindpwdNextCallback(Object resultObj) {

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		LogGloble.i(TAG, biiResponseBody.getResult().toString());
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		combinFlag = (String) result.get(Login.IS_COMBINFLAG);
		if (combinFlag.equals(ConstantGloble.COMBINE_FLAG_Y)) {// 有动态口令
			Map<String, Object> combineMap = (Map<String, Object>) result
					.get(Login.COMBIN_MAP);
			/**用户号，单手机交易码96用*/
	        BaseDroidApp.getInstanse().setOperatorId(result.get("userId")+"");
			// "_combinList":[
			// {"name":"数字安全证书","id":4,"safetyFactorList":[]},
			// {"name":"动态口令","id":8,"safetyFactorList":[]},
			// {"name":"动态口令和手机交易码","id":40,"safetyFactorList":[]}
			// ]
			// TODO Logon_retrievePassActive
			combineList = (List<Map<String, Object>>) combineMap
					.get(Login.COMBIN_LIST);
			if (StringUtil.isNullOrEmpty(combineList)) {
				// 取消通讯框
				BaseHttpEngine.dissMissProgressDialog();
				return;
			}
			/**过滤银音频key*/
			for (int i = 0; i < combineList.size(); i++) {
				if ("4".equals(combineList.get(i).get(Login.ID))) {
					combineList.remove(i);
					break;
				}
			}
			// 如果返回的combineList的size大于1，弹出选择框让用户选择绑定方式
			if (StringUtil.isNullOrEmpty(combineList)) {
				// 取消通讯框
				BaseHttpEngine.dissMissProgressDialog();
				return;
			}
			if (combineList.size() == 1) {
				combineId = (String) combineList.get(0).get(Login.ID);
				if("96".equals(combineId)){
					bindingDetection();
				}else{
					requestForRetrievePassActive();
				}
			} else if (combineList.size() > 1) {
				int index = -1;
				Map<String, Object> defaultCombine = (Map<String, Object>) combineMap
						.get(Login.DEFAULT_COMBIN);
				if (!StringUtil.isNullOrEmpty(defaultCombine)) {
				
					for (int i = 0; i < combineList.size(); i++) {
						if (combineList.get(i).get(Login.ID)
								.equals(defaultCombine.get(Login.ID))) {
							index = i;
							break;
						}
					}
				}
				sfAdapter = new SecurityFactorAdapter(FindPwdActivity.this,
						combineList);
				sfAdapter.setIndex(index);
				BaseDroidApp.getInstanse().showSeurityFactorDialog(
						onItemClickListener, sfAdapter);
			}

		} else {
			String securityLv = (String) result.get(Login.SECURITY_LVL);
			LogGloble.i(TAG, "securityLv = " + securityLv);
			jumpToNextActivity();
		}
	}
	
	/**
	 * 检查是否绑定本地
	 * */
	private void bindingDetection() {
		// 服务器返回未设定
		String localBindInfo = SharedPreUtils.getInstance().getString(
				ConstantGloble.SHARED_PREF_LOCAL_BIND_INFO, "");
			String cfcaString = DeviceInfoTools.getLocalCAOperatorId(this,BaseDroidApp.getInstanse().getOperatorId(),1);
			String localBindInfo_mac = SharedPreUtils.getInstance().getString(ConstantGloble.SHARED_PREF_LOCAL_BIND_INFO_MAC, "");// MAC
			String cfcamacString = DeviceInfoTools.getLocalCAOperatorId(this, BaseDroidApp.getInstanse().getOperatorId(),2);// mac
	
     
     if ("".equals(localBindInfo)) {
			// 当前账号已被他人绑定，解绑非本机设备
			BaseDroidApp
					.getInstanse()
					.showMessageDialog(
							"您未绑定当前手机设备，暂不能使用“手机交易码”进行业务操作，请选择其他安全工具。",
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									BaseDroidApp.getInstanse()
											.dismissMessageDialog();
								}
							});

		} else if (!"".equals(localBindInfo)
				&& cfcaString.equals(localBindInfo)) {
			// 已经绑定本机 请求服务器 currentDeviceFlag
			requestForRetrievePassActive();
		} else {
			if(cfcamacString.equals(localBindInfo_mac)){
				requestForRetrievePassActive();	
			}else{
				// 当前账号已被他人绑定，且本机设备已被其他账号绑定
				BaseDroidApp
				.getInstanse()
				.showMessageDialog(
						"您未绑定当前手机设备，暂不能使用“手机交易码”进行业务操作，请选择其他安全工具。",
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								BaseDroidApp.getInstanse()
										.dismissMessageDialog();
							}
						});	
			}
			
		}
	}

	/**
	 * 请求Logon_retrievePassActive
	 */
	private void requestForRetrievePassActive() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.PASS_ACTIVE_API);
		biiRequestBody.setConversationId(conversationId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Login.ACCOUNT_NUM, bankNum);
		map.put(Login.IDENTIFY_TYPE, strSpidCty);
		map.put(Login.IDENTIFY_NUM, idNum);
		map.put(Login.COMBINID, combineId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForRetrievePassActiveCallback");
	}

	/**
	 * 请求Logon_retrievePassActive返回
	 * 
	 * @param resultObj
	 */
	public void requestForRetrievePassActiveCallback(Object resultObj) {
		// 取消通讯框
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		LoginDataCenter.getInstance().setRetrievePassActive(result);

		jumpToNextActivity();
	}

	/**
	 * 跳转到下一个页面
	 */
	private void jumpToNextActivity() {
		Intent intent = new Intent();
		intent.putExtra(Login.COMBINE_FLAG, combinFlag);
		intent.putExtra(Login.ACCOUNT_NUM, bankNum);
		intent.putExtra(Login.IDENTIFY_TYPE, strSpidCty);
		intent.putExtra(Login.IDENTIFY_NUM, idNum);
		intent.setClass(FindPwdActivity.this, PwdResetActivity.class);
		startActivityForResult(intent, 0);
	}

	/**
	 * 安全因子list点击事件
	 */
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Map<String, Object> map = combineList.get(position);
			combineId = (String) map.get(Login.ID);
			sfAdapter.setIndex(position);
			if("96".equals(combineId)){
				bindingDetection();
			}else{
				requestForRetrievePassActive();
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// Intent intent = new Intent();
			// intent.setClass(this, LoginActivity.class);
			// startActivity(intent);
			finish();
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (UN_FORGET_PWD_SUCCESS == resultCode) {
			setResult(UN_FORGET_PWD_SUCCESS);
			finish();
		} else if (BACK_TO_THIS == resultCode) {
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

				if (Login.PWD_VERVIFY.equals(body.getMethod())) {

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

}
