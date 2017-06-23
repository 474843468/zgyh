package com.chinamworld.bocmbci.biz.thridmanage.openacct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import cfca.mobile.sip.SipBox;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Third;
import com.chinamworld.bocmbci.biz.thridmanage.BiiConstant.IdentifyType;
import com.chinamworld.bocmbci.biz.thridmanage.BiiConstant.ThridProvinceType;
import com.chinamworld.bocmbci.biz.thridmanage.ThirdManagerBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 开户信息确认
 * 
 * @author panwe
 * 
 */
public class AcctOpenMsgConfirmActivity extends ThirdManagerBaseActivity
		implements OnClickListener {

	private static final String TAG = AcctOpenMsgConfirmActivity.class
			.getSimpleName();
	/*** 主布局 **/
	private View viewContent;
	/** 证券公司 */
	private TextView tvCompany;
	/** 营业部所在地 */
	private TextView tvAsress;
	/** 营业部名 */
	private TextView tvCompanyPart;
	/** 账户名 */
	private TextView tvAcc;
	/** 客户名 */
	private TextView tvName;
	/** 证件类型 */
	private TextView tvIdType;
	/** 证件号 */
	private TextView tvIdNumber;
	/** 手机号 */
	private TextView tvMoblie;
	/** 通讯地址 */
	private TextView tvAdress;
	/** 邮编 */
	private TextView tvPostCode;
	/** 安全控件 */
	private ViewGroup smcLayout;
	private ViewGroup otpLayout;
	private ViewGroup usbLayout;
	private SipBox openAccSipbox;
	private SipBox openAccSmsbox;
	/** 中银E盾 */
	private UsbKeyText usbKeyText;
	/*** 从上一界面接收的信息 */
	/** 银行账户ID */
	private String accId;
	/** 银行账户 */
	private String accNum;
	/** 证券公司 */
	private String company;
	/** 通讯地址 */
	private String adress;
	/** 邮政编码 */
	private String provinceCod;
	/** 营业部 */
	private String branchName;
	/** 营业部地址 */
	private String branchAddress;
	/** 客户名称 */
	private String customName;
	/** 证件类型 */
	private String idType;
	/** 证件号码 */
	private String idNum;
	/** 手机号 */
	private String moblie;
	/** 证券公司代码 */
	private String stockCorpCode;
	/** 营业部代码 */
	private String stockBranchCode;

	private boolean isOtp = false;
	private boolean isSmc = false;
	private boolean isNeedUsbKey = false;
	/** 安全控件信息 */
	private String otpPassword = "";
	private String smcPassword = "";
	private String otpRandomNum = "";
	private String smcRandomNum = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 为界面标题赋值
		setTitle(this.getString(R.string.third_openacc_open));
		// 添加布局
		viewContent = LayoutInflater.from(this).inflate(
				R.layout.third_openacc_msgconfirm, null);
		addView(viewContent);
		init();
		getData();

		// BaseHttpEngine.showProgressDialogCanGoBack();
		// requestCommConversationId();
	}

	// @Override
	// public void requestCommConversationIdCallBack(Object resultObj) {
	// super.requestCommConversationIdCallBack(resultObj);
	// // 发送安全因子请求
	// requestGetSecurityFactor(Third.OPENACC_SERVICECODE);
	// }

	private void init() {
		// StepTitleUtils.getInstance().initTitldStep(
		// this,
		// new String[] { this.getString(R.string.third_openacc_step1),
		// this.getString(R.string.third_openacc_step2),
		// this.getString(R.string.third_openacc_step3) });
		// StepTitleUtils.getInstance().setTitleStep(2);
		// 右上角按钮赋值
		setTitleRightText(getString(R.string.go_main));
		setRightBtnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goMainActivity();
			}
		});

		tvCompany = (TextView) viewContent
				.findViewById(R.id.tv_openacc_company);
		tvAsress = (TextView) viewContent
				.findViewById(R.id.tv_openacc_province);
		tvCompanyPart = (TextView) viewContent
				.findViewById(R.id.tv_openacc_companypart);
		tvAcc = (TextView) viewContent.findViewById(R.id.tv_openacc_acc);
		tvName = (TextView) viewContent.findViewById(R.id.tv_openacc_name);
		tvIdType = (TextView) viewContent.findViewById(R.id.tv_openacc_idtype);
		tvIdNumber = (TextView) viewContent.findViewById(R.id.tv_openacc_idnum);
		tvMoblie = (TextView) viewContent.findViewById(R.id.tv_openacc_mobile);
		tvAdress = (TextView) viewContent.findViewById(R.id.tv_openacc_adress);
		tvPostCode = (TextView) viewContent
				.findViewById(R.id.tv_openacc_postcode);

		Button btnConfirm = (Button) viewContent.findViewById(R.id.btnconfirm);
		btnConfirm.setOnClickListener(this);
		// requestForRandomNumber();
		/** 跟据返回初始化中银E盾加密控件 */
		initSipBox();
		// smcLayout = (ViewGroup) viewContent.findViewById(R.id.layout_sms);
		if(isNeedUsbKey){
			usbLayout = (ViewGroup)viewContent.findViewById(R.id.layout_usb);
			usbLayout.setVisibility(View.VISIBLE);
		}
		if (isOtp) {
			otpLayout = (ViewGroup) viewContent.findViewById(R.id.layout_otp);
			otpLayout.setVisibility(View.VISIBLE);
			openAccSipbox = (SipBox) viewContent.findViewById(R.id.modify_pwd_sip);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(openAccSipbox, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, AcctOpenMsgFillActivity.randomNumber, this);
//			openAccSipbox.setCipherType(SystemConfig.CIPHERTYPE);
//			openAccSipbox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			openAccSipbox.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			openAccSipbox.setId(10002);
//			openAccSipbox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			openAccSipbox.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			openAccSipbox.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			openAccSipbox.setSipDelegator(this);
//			openAccSipbox.setRandomKey_S(AcctOpenMsgFillActivity.randomNumber);
		}
		if (isSmc) {
			smcLayout = (ViewGroup) viewContent.findViewById(R.id.layout_sms);
			smcLayout.setVisibility(View.VISIBLE);
			openAccSmsbox = (SipBox) viewContent.findViewById(R.id.sip_sms);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(openAccSmsbox, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, AcctOpenMsgFillActivity.randomNumber, this);
//			openAccSmsbox.setCipherType(SystemConfig.CIPHERTYPE);
//			openAccSmsbox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			openAccSmsbox.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			openAccSmsbox.setId(10002);
//			openAccSmsbox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			openAccSmsbox.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			openAccSmsbox.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			openAccSmsbox.setSipDelegator(this);
//			openAccSmsbox.setRandomKey_S(AcctOpenMsgFillActivity.randomNumber);
			// 获取验证码
			Button btnSmc = (Button) findViewById(R.id.smsbtn);
			SmsCodeUtils.getInstance().addSmsCodeListner(btnSmc,
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							sendSMSCToMobile();
						}
					});
		}
	}

	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox() {
		String mmconversationId = (String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		usbKeyText = (UsbKeyText) viewContent.findViewById(R.id.sip_usbkey);
		usbKeyText.Init(mmconversationId, AcctOpenMsgFillActivity.randomNumber,
				AcctOpenMsgFillActivity.result, this);
		isOtp = usbKeyText.getIsOtp();
		isSmc = usbKeyText.getIsSmc();
		isNeedUsbKey = usbKeyText.isNeedUsbKey();
	}

	// 获取上一页数据
	private void getData() {
		Bundle b = getIntent().getExtras();
		accId = b.getString("ACCID");
		accNum = b.getString("ACCNUM");
		company = b.getString("COMPANY");
		provinceCod = b.getString("POSTCODE");
		adress = b.getString("ADDRESS");
		branchName = b.getString("BRANNAME");
		branchAddress = b.getString("BRANCH_ADDRESS");
		customName = b.getString("COSUTNAME");
		idType = b.getString("IDTYPE");
		idNum = b.getString("IDNUM");
		moblie = b.getString("MOBLIE");
		stockBranchCode = b.getString("STOCK_BRANCH_CODE");
		stockCorpCode = b.getString("STOCK_CORP_CODE");

		// ArrayList<String> factors = (ArrayList<String>)
		// b.getSerializable("FACTORLIST");
		// sipInit(factors);
		tvCompany.setText(company);
		tvAsress.setText(ThridProvinceType.getOldProvincesName(branchAddress));
		tvCompanyPart.setText(branchName);
		tvAcc.setText(StringUtil.getForSixForString(accNum));
		tvName.setText(customName);
		tvIdType.setText(IdentifyType.getIdentifyTypeStr(idType));
		tvIdNumber.setText(idNum);
		tvMoblie.setText(moblie);
		tvAdress.setText(adress);
		tvPostCode.setText(provinceCod);

		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) findViewById(R.id.tip_one));
		PopupWindowUtils.getInstance()
				.setOnShowAllTextListener(this, tvCompany);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvAsress);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tvCompanyPart);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvAcc);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvIdType);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tvIdNumber);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvMoblie);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvAdress);
	}

	// -----------------------------------------------------------------------------------
	// request

	// 请求预交易
	// private void requestOpenAccConfirm(String combin) {
	// BiiRequestBody biiRequestBody = new BiiRequestBody();
	// biiRequestBody.setMethod(Third.METHOD_OPENACC_CONFIRM);
	// biiRequestBody.setConversationId((String)
	// BaseDroidApp.getInstanse().getBizDataMap()
	// .get(ConstantGloble.CONVERSATION_ID));
	// Map<String, Object> params = new HashMap<String, Object>();
	// params.put(Third.PLATFORACC_LIST_ACCID, accId);
	// params.put(Third.OPENACC_CONFIRM_ACC, accNum);
	// params.put(Third.OPENACC_CONFIRM_COMANY, company);
	// params.put(Third.OPENACC_STOCKBRANCH_ADR, branchAddress);
	// params.put(Third.OPENACC_CONFIRM_BRANCHNAME, branchName);
	// params.put(Third.OPENDACC_CONFIRM_CUETNAME, customName);
	// params.put(Third.OPENDACC_CONFIRM_IDTYPE, idType);
	// params.put(Third.OPENDACC_CONFIRM_IDNUM, idNum);
	// params.put(Third.OPENDACC_CONFIRM_MOBILE, moblie);
	// params.put(Third.OPENDACC_CONFIRM_COMBINID, combin);
	// biiRequestBody.setParams(params);
	// // 通讯开始,展示通讯框
	// BaseHttpEngine.showProgressDialog();
	// HttpManager.requestBii(biiRequestBody, this,
	// "getOpenAccConfirmCallBack");
	// }

	// // 请求密码控件随机数
	// public void requestForRandomNumber() {
	// BaseHttpEngine.showProgressDialogCanGoBack();
	// BiiRequestBody biiRequestBody = new BiiRequestBody();
	// biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
	// biiRequestBody.setConversationId((String)
	// BaseDroidApp.getInstanse().getBizDataMap()
	// .get(ConstantGloble.CONVERSATION_ID));
	// HttpManager.requestBii(biiRequestBody, this,
	// "queryRandomNumberCallBack");
	// }

	/**
	 * 开户提交
	 * 
	 * @param token
	 */
	private void requestOpenAccComit(String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Third.METHOD_OPENACC_RESULT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Comm.ACCOUNT_ID, accId);
		params.put(Third.OPENACC_CONFIRM_ACC, accNum);
		params.put(Third.OPENDACC_CONFIRM_MOBILE, moblie);
		params.put(Third.OPENACC_RESULT_ADDRESS, adress);
		params.put(Third.OPENACC_RESULT_POSTCODE, provinceCod);
		params.put(Third.OPENACC_CONFIRM_COMANY, company);
		params.put(Third.OPENACC_COMPANY_CODE, stockCorpCode);
		params.put(Third.OPENACC_STOCKBRANCH_ADR, branchAddress);
		params.put(Third.OPENACC_CONFIRM_BRANCHNAME, branchName);
		params.put(Third.OPENACC_STOCKCODE, stockBranchCode);
		params.put(Third.OPENACC_RESULT_TOKEN, token);

		params.put(Third.OPENDACC_CONFIRM_CUETNAME, customName);
		params.put(Third.OPENDACC_CONFIRM_IDTYPE, idType);
		params.put(Third.OPENDACC_CONFIRM_IDNUM, idNum);
		// if (isOtp) {
		// params.put(Comm.Otp, otpPassword);
		// params.put(Comm.Otp_Rc, otpRandomNum);
		// }
		// if (isSmc) {
		// params.put(Comm.Smc, smcPassword);
		// params.put(Comm.Smc_Rc, smcRandomNum);
		// }
		/** 安全工具参数获取 */
		usbKeyText.InitUsbKeyResult(params);
		SipBoxUtils.setSipBoxParams(params);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "openAccComitCallBack");
	}

	// ------------------------------------------------------------------------------------
	// callBack

	/*** 安全因子返回结果 ***/
	// public void requestGetSecurityFactorCallBack(Object resultObj) {
	// super.requestGetSecurityFactorCallBack(resultObj);
	// BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener()
	// {
	// @Override
	// public void onClick(View v) {
	// // 请求预交易
	// requestOpenAccConfirm(BaseDroidApp.getInstanse().getSecurityChoosed());
	// }
	// });
	// }

	/*** 开户预交易 返回 */
	// public void getOpenAccConfirmCallBack(Object resultObj) {
	// BiiResponse biiResponse = (BiiResponse) resultObj;
	// List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
	// BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
	// // 通讯结束,关闭通讯框
	// BaseHttpEngine.dissMissProgressDialog();
	// Map<String, Object> result = (Map<String, Object>)
	// biiResponseBody.getResult();
	// if (result == null) {
	// return;
	// }
	// List<Map<String, Object>> factorList = (List<Map<String, Object>>)
	// result.get(Third.OPENDACC_CONFIRM_FACLIST);
	// sipInit(factorList);
	// }
	// private String randomNumber ;
	// /*** 随机数返回 */
	// public void queryRandomNumberCallBack(Object resultObj) {
	// BaseHttpEngine.dissMissProgressDialog();
	// BiiResponse biiResponse = (BiiResponse) resultObj;
	// List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
	// BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
	// randomNumber = (String) biiResponseBody.getResult();
	// if (StringUtil.isNull(randomNumber)) {
	// return;
	// }
	// // 加密控件设置随机数
	// // if (isOtp) {
	// // otpLayout.setVisibility(View.VISIBLE);
	// // openAccSipbox.setRandomKey_S(randomNumber);
	// // }
	// // if (isSmc) {
	// // smcLayout.setVisibility(View.VISIBLE);
	// // openAccSmsbox.setRandomKey_S(randomNumber);
	// // }
	// init();
	// }

	// token返回
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String token = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		requestOpenAccComit(token);
	}

	/*** 开户提交返回 */
	public void openAccComitCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> result = (Map<String, String>) biiResponseBody
				.getResult();
		if (result == null) {
			return;
		}
		Intent it = new Intent(this, AcctOpenResultActivity.class);
		Bundle b = new Bundle();
		// b.putString("ACCID", accId);
		b.putString("accNum", accNum);
		b.putString("stockBranchName",
				result.get(Third.OPENDACC_RESULT_MERCHANTNAME));
		// b.putString("departmentRegion",
		// result.get(Third.OPENACC_STOCKBRANCH_ADR));
		b.putString("departmentRegion",
				ThridProvinceType.getOldProvincesName(branchAddress));
		// b.putString("departmentName",
		// result.get(Third.OPENACC_CONFIRM_BRANCHNAME));
		b.putString("departmentName",
				result.get(Third.OPENACC_RESULT_DEPARTMENT));
		// b.putString("departmentAddr", branchName);
		b.putString("departmentAddr", result.get(Third.OPENDACC_RESULT_DRESS));
		b.putString("departmentLinkman",
				result.get(Third.OPENDACC_RESULT_LINK_MAN));
		b.putString("departmentLinkMobile",
				result.get(Third.OPENDACC_RESULT__MOBILE));
		it.putExtras(b);
		this.startActivity(it);
	}

	// ------------------------------------------------------------------------------------
	// event
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnconfirm) {
			contentCheck();
		}
	}

	// -------------------------------------------------------------------------------------
	// method

	/** 跟据返回初始化加密控件 */
	// private void sipInit(ArrayList<String> facotrs) {
	// if (facotrs != null) {
	// requestForRandomNumber();
	// for (String facotr : facotrs) {
	// if (facotr.equals(Comm.Otp)) {
	// isOtp = true;
	// // otpLayout.setVisibility(View.VISIBLE);
	// } else if (facotr.equals(Comm.Smc)) {
	// isSmc = true;
	// // smcLayout.setVisibility(View.VISIBLE);
	// }
	// }
	// }
	//
	// }

	/** 提交校验 **/
	private void contentCheck() {
		// try {
		// ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		// if (isSmc) {
		// // 短信验证码
		// RegexpBean rebSms = new RegexpBean(getString(R.string.acc_smc_regex),
		// openAccSmsbox.getText()
		// .toString(), ConstantGloble.SIPSMCPSW);
		// lists.add(rebSms);
		// }
		// if (isOtp) {
		// // 动态口令
		// RegexpBean reb1 = new
		// RegexpBean(getString(R.string.active_code_regex),
		// openAccSipbox.getText()
		// .toString(), ConstantGloble.SIPOTPPSW);
		// lists.add(reb1);
		// }
		// if (RegexpUtils.regexpDate(lists)) {
		// if (isOtp) {
		// otpPassword = openAccSipbox.getValue().getEncryptPassword();
		// otpRandomNum = openAccSipbox.getValue().getEncryptRandomNum();
		// }
		// if (isSmc) {
		// smcPassword = openAccSmsbox.getValue().getEncryptPassword();
		// smcRandomNum = openAccSmsbox.getValue().getEncryptRandomNum();
		// }
		// // 发送获取token请求
		// // 通讯开始,展示通讯框
		// BaseHttpEngine.showProgressDialog();
		// requestPSNGetTokenId((String)
		// BaseDroidApp.getInstanse().getBizDataMap()
		// .get(ConstantGloble.CONVERSATION_ID));
		// }
		// } catch (CodeException e) {
		// BaseDroidApp.getInstanse().createDialog(null, e.getMessage());
		// LogGloble.e(TAG, e.getMessage(), e);
		// }
		/** 安全工具数据校验 */
		usbKeyText.checkDataUsbKey(openAccSipbox, openAccSmsbox,
				new IUsbKeyTextSuccess() {

					@Override
					public void SuccessCallBack(String result, int errorCode) {
						// TODO Auto-generated method stub
						// 发送获取token请求
						// 通讯开始,展示通讯框
						BaseHttpEngine.showProgressDialog();
						requestPSNGetTokenId((String) BaseDroidApp
								.getInstanse().getBizDataMap()
								.get(ConstantGloble.CONVERSATION_ID));
					}
				});

	}

}
