package com.chinamworld.bocmbci.biz.acc.relevanceaccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import cfca.mobile.sip.SipBox;

import com.cfca.mobile.log.CodeException;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.GetPhoneInfo;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 填写关联账户信息页面
 * 
 * @author wangmengmeng
 * 
 */
public class AccInputRelevanceAccountActivity extends AccBaseActivity {
	/** 填写关联账户信息页 */
	private View view;
	/** 加密之后加密随机数 */
	private String password_RC = "";
	/** 输入框密码 */
	private String password = "";
	/** 验证码图片 */
	private ImageButton imageCodeButton;
	/** 验证码 */
	private String imageCode;
	/** 验证码输入框 */
	protected EditText etImageCode;
	/** 加密控件 */
	private SipBox sipBox_phone = null;
	private SipBox sipBox_atm = null;
	/** 加密控件里的随机数 */
	protected String randomNumber;
	/** 待关联账号 */
	private EditText et_acc_relevance_actnum;
	/** 待关联账号 */
	private String acc_relevance_actnum;
	/** 下一步按钮 */
	private Button btn_relevance_next;
	/** 是否是自己界面跳转 */
	private boolean isMy = false;
	private String bankphonepasswordWrong;
	/** 验证码textview */
	private TextView codeTv;
	private TextView tv_relevance_num;
	private LinearLayout ll_phone;
	private LinearLayout ll_atm;
	private LinearLayout ll_image;
	/** 根据卡Bin查询类型 */
	private Map<String, Object> queryCardTypemap;
	/** 是否正在请求验证码 */
	private boolean isRequestingCode = false;
	private boolean isfalse = false;

	/** 405理财进入需要带入账号 */
	private String bankAccount;
	/** 601申请定期活期账户带入的账号*/
	private String account;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 为界面标题赋值
		setTitle(this.getString(R.string.acc_myaccount_relevance_title));
		// 右上角按钮赋值
		// setText(this.getString(R.string.acc_rightbtn_go_main));
		gonerightBtn();
		// 添加布局
		view = addView(R.layout.acc_relevanceaccount_inputmessage);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(8);
				finish();
			}
		});
		setLeftSelectedPosition("accountManager_2");
		hineLeftSideMenu();
		bankphonepasswordWrong = this.getResources().getString(
				R.string.bankphone_password_wrong);
		// 初始化界面
		init();
	}

	/** 初始化界面 */
	private void init() {
		isMy = getIntent().getBooleanExtra(ConstantGloble.ACC_ISMY, false);
		bankAccount = this.getIntent().getStringExtra(Acc.ACC_ACCOUNTNUMBER_RES);
		account=getIntent().getStringExtra("account");
		StepTitleUtils.getInstance().initTitldStep(this,new String[] { this.getString(R.string.acc_relevance_step1),
						this.getString(R.string.acc_relevance_step2),
						this.getString(R.string.acc_relevance_step3) });
		StepTitleUtils.getInstance().setTitleStep(1);
		// // 加密控件

		sipBox_phone = (SipBox) view.findViewById(R.id.sipbox);
		sipBox_phone.setHint(this.getString(R.string.acc_relevance_phonepwd_input));
		// add by 2016年9月12日 luqp 修改
		SipBoxUtils.initSipBoxWithTwoType(sipBox_phone, ConstantGloble.MIN_LENGTH,
				ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//		sipBox_phone.setCipherType(SystemConfig.CIPHERTYPE);
//		sipBox_phone.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		sipBox_phone.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		sipBox_phone.setId(10002);
//		sipBox_phone.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		sipBox_phone.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//		sipBox_phone.setSipDelegator(this);
//		sipBox_phone.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);

		sipBox_atm = (SipBox) view.findViewById(R.id.sipbox_atm);
		// add by 2016年9月12日 luqp 修改
		SipBoxUtils.initSipBoxWithTwoType(sipBox_atm, ConstantGloble.MIN_LENGTH,
				ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//		sipBox_atm.setCipherType(SystemConfig.CIPHERTYPE);
//		sipBox_atm.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		sipBox_atm.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		sipBox_atm.setId(10002);
//		sipBox_atm.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		sipBox_atm.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//		sipBox_atm.setSipDelegator(this);
//		sipBox_atm.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);

		etImageCode = (EditText) view.findViewById(R.id.ed_image_code);
		etImageCode.setFocusable(true);
		tv_relevance_num = (TextView) view.findViewById(R.id.tv_relevance_actnum);
		ll_phone = (LinearLayout) view.findViewById(R.id.ll_phone);
		ll_atm = (LinearLayout) view.findViewById(R.id.ll_atm);
		ll_image = (LinearLayout) view.findViewById(R.id.ll_image);
		imageCodeButton = (ImageButton) view.findViewById(R.id.ib_image_code);
		imageCodeButton.setOnClickListener(imageCodeClickListener);

		btn_relevance_next = (Button) view.findViewById(R.id.btn_relevance_next);
		btn_relevance_next.setOnClickListener(goNextClickListener);
		codeTv = (TextView) view.findViewById(R.id.ib_image_code_text);
		codeTv.setOnClickListener(imageCodeClickListener);
		et_acc_relevance_actnum = (EditText) findViewById(R.id.et_acc_relevance_actnum);
		/** 405理财自助关联带入账号 */
		if(!StringUtil.isNullOrEmpty(bankAccount)){
			et_acc_relevance_actnum.setText(bankAccount);
		}
		/** 601申请定期活期账户带入的账号*/
		if(!StringUtil.isNullOrEmpty(account)){
			et_acc_relevance_actnum.setText(account);
		}
	}

	// conversationid回调
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {

		super.requestCommConversationIdCallBack(resultObj);
		String accountType = (String) queryCardTypemap
				.get(Acc.ACC_QUERYCARD_ACCOUNTTYPE_RES);

		if (accountType.equals(accountTypeList.get(3))) {
			requestForRandomNumber();

		} else if (accountType.equals(accountTypeList.get(13))) {
			// 电子现金账户
			ll_atm.setVisibility(View.GONE);
			ll_phone.setVisibility(View.GONE);
			requestForImagecode();
		} else if (accountType.equals(accountTypeList.get(1))
				|| accountType.equals(accountTypeList.get(2))
				|| accountType.equals(accountTypeList.get(4))) {
			requestForRandomNumber();
		}

	}

	/**
	 * 请求密码控件随机数
	 */
	public void requestForRandomNumber() {
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
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		randomNumber = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(randomNumber)) {
			return;
		}
		// 加密控件设置随机数

		String accountType = (String) queryCardTypemap
				.get(Acc.ACC_QUERYCARD_ACCOUNTTYPE_RES);

		if (accountType.equals(accountTypeList.get(3))) {
			sipBox_atm.setRandomKey_S(randomNumber);
			// 借记卡
			requestForImagecode();
		} else if (accountType.equals(accountTypeList.get(13))) {
			// 电子现金账户
			BiiHttpEngine.dissMissProgressDialog();
		} else if (accountType.equals(accountTypeList.get(1))
				|| accountType.equals(accountTypeList.get(2))
				|| accountType.equals(accountTypeList.get(4))) {
			sipBox_phone.setRandomKey_S(randomNumber);
			// 信用卡
			requestForImagecode();
		}
	}

	/**
	 * 请求验证码图片
	 */
	public void requestForImagecode() {
		isfalse = true;
		isRequestingCode = false;
		codeTv.setVisibility(View.VISIBLE);
		imageCodeButton.setVisibility(View.GONE);
		HttpManager.requestImage(Comm.AQUIRE_IMAGE_CODE,
				ConstantGloble.GO_METHOD_GET, null, this,
				"imagecodeCallBackMethod");
	}

	/**
	 * 验证码请求回调
	 * 
	 * @param resultObj
	 *            请求成功 返回参数
	 */
	public void imagecodeCallBackMethod(Object resultObj) {
		Bitmap bitmap = (Bitmap) resultObj;
		BitmapDrawable imageCodeDrawable = new BitmapDrawable(bitmap);
		imageCodeButton.setBackgroundDrawable(imageCodeDrawable);
		imageCodeButton.setVisibility(View.VISIBLE);
		codeTv.setVisibility(View.GONE);
		isRequestingCode = false;
		isfalse = false;
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BaseDroidApp.getInstanse().showSeurityChooseDialog(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// // 请求预交易
						BaseHttpEngine.showProgressDialog();
						pSNGetTokenId();
					}
				});
	}

	/**
	 * 获取tokenId
	 */
	public void pSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "aquirePSNGetTokenId");
	}

	/**
	 * 处理获取tokenId的数据得到tokenId
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void aquirePSNGetTokenId(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		// 请求预交易
		requestPsnRelevanceAccountPre(tokenId);
	}

	/** 请求自助关联预交易 */
	public void requestPsnRelevanceAccountPre(String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.ACC_PSNRELEVANCEACCOUNTPRE_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Acc.RELEVANCEACCPRE_ACC_ACCOUNTNUMBER_REQ, acc_relevance_actnum);
		AccDataCenter.getInstance().setAcc_relevance_actnum(
				acc_relevance_actnum);
		map.put(Acc.RELEVANCEACCPRE_ACC_VALIDATIONCHAR_REQ, imageCode);
		map.put(Acc.RELEVANCEACCPRE_ACC_COMBINID_REQ, BaseDroidApp
				.getInstanse().getSecurityChoosed());
		final String accountType = (String) queryCardTypemap
				.get(Acc.ACC_QUERYCARD_ACCOUNTTYPE_RES);
		if (accountType.equals(accountTypeList.get(3))) {
			// 借记卡
			// atm取款密码
			map.put(Acc.RELEVANCEACCPRE_ACC_ATMPASSWORDRC_REQ, password_RC);
			map.put(Acc.RELEVANCEACCPRE_ACC_ATMPASSWORD_REQ, password);
		} else if (accountType.equals(accountTypeList.get(13))) {
			// 电子现金账户
		} else if (accountType.equals(accountTypeList.get(1))
				|| accountType.equals(accountTypeList.get(2))
				|| accountType.equals(accountTypeList.get(4))) {
			// 信用卡
			map.put(Acc.RELEVANCEACCPRE_ACC_PHONEBANKPASSWORD_REQ, password);
			map.put(Acc.RELEVANCEACCPRE_ACC_PHONEBANKPASSWORDRC_REQ,
					password_RC);
		}
		map.put(Acc.RELEVANCEACCPRE_ACC_TOKEN_REQ, token);
		SipBoxUtils.setSipBoxParams(map);
		GetPhoneInfo.initActFirst(this);
		GetPhoneInfo.addPhoneInfo(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnRelevanceAccountPreCallback");
	}

	/**
	 * 请求账户自助关联预交易回调
	 * 
	 * @param resultObj
	 */
	public void requestPsnRelevanceAccountPreCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		// 通讯返回result
		Map<String, Object> relevancePremap = (Map<String, Object>) (biiResponseBody
				.getResult());
		if (StringUtil.isNullOrEmpty(relevancePremap)) {
			return;
		}
		// 储存返回信息
		AccDataCenter.getInstance().setRelevancePremap(relevancePremap);
		String accountType = (String) relevancePremap
				.get(Acc.RELEVANCEACCPRE_ACC_ACCOUNTTYPE_RES);

		if (accountType.equals(accountTypeList.get(3))) {
			// 借记卡
			etImageCode.setText(ConstantGloble.BOCINVT_NULL_STRING);
			Intent intent = new Intent(AccInputRelevanceAccountActivity.this,
					AccDebitCardChooseActivity.class);
			startActivityForResult(intent,
					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);

		} else if (accountType.equals(accountTypeList.get(13))) {
			// 电子现金账户
			etImageCode.setText(ConstantGloble.BOCINVT_NULL_STRING);
			Intent intent = new Intent(AccInputRelevanceAccountActivity.this,
					AccICCardConfirmActivity.class);
			startActivityForResult(intent,
					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
		} else if (accountType.equals(accountTypeList.get(1))
				|| accountType.equals(accountTypeList.get(2))
				|| accountType.equals(accountTypeList.get(4))) {
			// 信用卡
			etImageCode.setText(ConstantGloble.BOCINVT_NULL_STRING);
			Intent intent = new Intent(AccInputRelevanceAccountActivity.this,
					AccCreditCardConfirmActivity.class);
			startActivityForResult(intent,
					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
		}
	}

	/**
	 * 验证码监听事件
	 */
	OnClickListener imageCodeClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!isRequestingCode) {
				isRequestingCode = true;
				requestForImagecode();
				BiiHttpEngine.showProgressDialog();
			}
		}
	};

	/**
	 * 下一步监听事件
	 */
	OnClickListener goNextClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			acc_relevance_actnum = et_acc_relevance_actnum.getText().toString()
					.trim();
			if (et_acc_relevance_actnum.getVisibility() == View.VISIBLE) {
				RegexpBean reb2 = new RegexpBean(
						AccInputRelevanceAccountActivity.this
								.getString(R.string.acc_relevance_actnum_regex),
						acc_relevance_actnum, "chipCard");
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(reb2);
				if (RegexpUtils.regexpDate(lists)) {// 校验通过
					requestPsnQueryCardTypeByCardBin();
				}
			} else if (tv_relevance_num.getVisibility() == View.VISIBLE) {
				try {

					imageCode = etImageCode.getText().toString().trim();
					// 待关联账号
					ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
					final String accountType = (String) queryCardTypemap
							.get(Acc.ACC_QUERYCARD_ACCOUNTTYPE_RES);
					RegexpBean sipRegexpBean;
					if (accountType.equals(accountTypeList.get(3))) {
						// 借记卡
						// atm取款密码
						sipRegexpBean = new RegexpBean(
								AccInputRelevanceAccountActivity.this
										.getString(R.string.acc_atmpwd_regex),
								sipBox_atm.getText().toString(),
								ConstantGloble.ATMPSW);
						lists.add(sipRegexpBean);
					} else if (accountType.equals(accountTypeList.get(13))) {
						// 电子现金账户
					} else if (accountType.equals(accountTypeList.get(1))
							|| accountType.equals(accountTypeList.get(2))
							|| accountType.equals(accountTypeList.get(4))) {
						// 信用卡
						// 电话银行密码
						sipRegexpBean = new RegexpBean(
								AccInputRelevanceAccountActivity.this
										.getString(R.string.acc_phonepwd_regex),
								sipBox_phone.getText().toString(),
								ConstantGloble.SIPPHONEPSW);
						lists.add(sipRegexpBean);
					}
					// 图片验证码
					RegexpBean reb3 = new RegexpBean(
							AccInputRelevanceAccountActivity.this
									.getString(R.string.imgcode_regex),
							imageCode, "validateCode");
					lists.add(reb3);
					if (RegexpUtils.regexpDate(lists)) {// 校验通过
						if (accountType.equals(accountTypeList.get(3))) {
							// 借记卡
							password_RC = sipBox_atm.getValue()
									.getEncryptRandomNum();
							password = sipBox_atm.getValue()
									.getEncryptPassword();
						} else if (accountType.equals(accountTypeList.get(1))
								|| accountType.equals(accountTypeList.get(2))
								|| accountType.equals(accountTypeList.get(4))) {
							// 信用卡
							password_RC = sipBox_phone.getValue()
									.getEncryptRandomNum();
							password = sipBox_phone.getValue()
									.getEncryptPassword();
						}

						// 请求获取安全因子组合id
						requestGetSecurityFactor(relevanceServiceId);
						BiiHttpEngine.showProgressDialog();
					}

				} catch (CodeException e) {
					BaseDroidApp.getInstanse().createDialog(null,
							bankphonepasswordWrong);
					LogGloble.exceptionPrint(e);
				}
			}

		}
	};

	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		if (tv_relevance_num.getVisibility() == View.VISIBLE) {
			if (!isfalse) {
				BiiResponse biiResponse = (BiiResponse) resultObj;
				List<BiiResponseBody> biiResponseBodys = biiResponse
						.getResponse();
				BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
				if (biiResponseBody.getStatus().equals(
						ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
					if (Acc.ACC_GETSECURITYFACTOR_API.equals(biiResponseBody
							.getMethod())
							|| Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API
									.equals(biiResponseBody.getMethod())
							|| Acc.ACC_PSNRELEVANCEACCOUNTPRE_API
									.equals(biiResponseBody.getMethod())) {
						if (biiResponse.isBiiexception()) {// 代表返回数据异常
							BiiHttpEngine.dissMissProgressDialog();
							BiiError biiError = biiResponseBody.getError();
							// 判断是否存在error
							if (biiError != null) {
								if (biiError.getCode() != null) {
									if (LocalData.timeOutCode.contains(biiError
											.getCode())) {// 表示回话超时 要重新登录
										showTimeOutDialog(biiError.getMessage());

									} else {// 非会话超时错误拦截
										BaseDroidApp.getInstanse()
												.createDialog("",
														biiError.getMessage(),
														new OnClickListener() {

															@Override
															public void onClick(
																	View v) {
																BaseDroidApp
																		.getInstanse()
																		.dismissErrorDialog();
																nobackClick
																		.onClick(v);
															}
														});

									}
								}
							}
							return true;
						}
						return false;// 没有异常
					} else {

						return super.httpRequestCallBackPre(resultObj);
					}
				}
				// 随机数获取异常

				return super.httpRequestCallBackPre(resultObj);
			}

		}
		return super.httpRequestCallBackPre(resultObj);
	}

	@Override
	public void commonHttpErrorCallBack(String requestMethod) {
		// 只需要把验证码状态改成获取失败就行，不需要弹出对话框
		if (Acc.ACC_GETSECURITYFACTOR_API.equals(requestMethod)
				|| Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API
						.equals(requestMethod)
				|| Acc.ACC_PSNRELEVANCEACCOUNTPRE_API.equals(requestMethod)) {
			requestForImagecode();
			BiiHttpEngine.showProgressDialog();
			etImageCode.setText(ConstantGloble.BOCINVT_NULL_STRING);
			isRequestingCode = false;
		}
		super.commonHttpErrorCallBack(requestMethod);

	}

	/** 错误返回 */
	OnClickListener nobackClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			isRequestingCode = false;
			requestForImagecode();
			BiiHttpEngine.showProgressDialog();
			etImageCode.setText(ConstantGloble.BOCINVT_NULL_STRING);
		}
	};

	/**
	 * 根据卡Bin查询卡类型
	 */
	public void requestPsnQueryCardTypeByCardBin() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.ACC_PSNQUERYCARDTYPEBYCARDBIN_API);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Acc.ACC_QUERYCARD_ACCOUNTNUMBER_REQ, acc_relevance_actnum);
		BiiHttpEngine.showProgressDialog();
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnQueryCardTypeByCardBinCallback");
	}

	/**
	 * 根据卡Bin查询卡类型回调
	 * 
	 * @param resultObj
	 */
	public void requestPsnQueryCardTypeByCardBinCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		queryCardTypemap = (Map<String, Object>) (biiResponseBody.getResult());
		if (StringUtil.isNullOrEmpty(queryCardTypemap)) {
			BiiHttpEngine.dissMissProgressDialog();
			return;
		}
		String accountType = (String) queryCardTypemap
				.get(Acc.ACC_QUERYCARD_ACCOUNTTYPE_RES);
		et_acc_relevance_actnum.setVisibility(View.GONE);
		tv_relevance_num.setVisibility(View.VISIBLE);
		tv_relevance_num.setText(acc_relevance_actnum);
		if (accountType.equals(accountTypeList.get(3))) {
			// 借记卡
			ll_atm.setVisibility(View.VISIBLE);
			ll_phone.setVisibility(View.GONE);

		} else if (accountType.equals(accountTypeList.get(13))) {
			// 电子现金账户
			ll_atm.setVisibility(View.GONE);
			ll_phone.setVisibility(View.GONE);
		} else if (accountType.equals(accountTypeList.get(1))
				|| accountType.equals(accountTypeList.get(2))
				|| accountType.equals(accountTypeList.get(4))) {
			// 信用卡
			ll_atm.setVisibility(View.GONE);
			ll_phone.setVisibility(View.VISIBLE);
		}
		ll_image.setVisibility(View.VISIBLE);
		requestCommConversationId();
		BaseHttpEngine.showProgressDialog();
	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 回主页面
			ActivityTaskManager.getInstance().removeAllActivity();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (resultCode) {
		case RESULT_OK:
			if (isMy) {
				view = addView(R.layout.acc_relevanceaccount_inputmessage);
				init();
			} else {
				setResult(RESULT_OK);
				finish();
			}

			break;
		case RESULT_CANCELED:
			requestForImagecode();
			BiiHttpEngine.showProgressDialog();
			break;
		default:
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(8);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
