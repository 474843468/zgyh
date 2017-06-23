package com.chinamworld.bocmbci.biz.acc.financeicaccount.transfer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import cfca.mobile.sip.SipBox;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.biz.acc.adapter.BankAccountAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 电子现金账户充值页
 * 
 * @author wangmengmeng
 * 
 */
public class FinanceIcTransferMainActivity extends AccBaseActivity implements
		OnCheckedChangeListener {
	/** 电子现金账户充值选择转出账户页 */
	private View view;
	/** 账户列表 */
	private List<Map<String, Object>> transAccountList;
	/** 账户列表 */
	private ListView lvBankAccountList;
	/** 查询切换 */
	private RadioGroup mRadioGroup;
	/** 为本人充值 */
	private RadioButton mRadioButton_trans;
	/** 为他人充值 */
	private RadioButton mRadioButton_trans_relevance;
	/** 选中记录项 */
	public int selectposition = -1;
	/** 下一步按钮 */
	private Button btnNext;
	/** 可更换视图 */
	private RelativeLayout changeView;
	/** 选择的转出账户信息 */
	private Map<String, Object> chooseTransAccount;
	/** 所有视图列表 */
	private List<View> viewList;
	// 为本人充值///////////////////////
	/** 选择账号视图 */
	private View chooseView;
	/** 输入信息视图 */
	private View inputView;
	/** 确认信息视图 */
	private View confirmView;
	/** 成功信息视图 */
	private View successView;
	/** 选择的电子现金账号 */
	private String chooseactnum;
	/** 选择的电子现金id */
	private String chooseactid;
	/** 输入的充值金额 */
	private String inputtransbanlance;
	/** 为本人充值 tokenid */
	private String trans_tokenId;
	/** 为他人充值 收款人姓名 */
	private String transrelPayName;
	/** 为本人充值回调信息 */
	private Map<String, Object> financeictransback;
	/** 为他人充值预交易回调信息 */
	private Map<String, Object> iCTransRelfermap;
	/** 页面标志 */
	private int i = 0;
	/** 安全因子数组 */
	public List<Map<String, Object>> factorList;
	/** 手机交易码 */
	private LinearLayout ll_smc;
	/** 动态口令 */
	private LinearLayout ll_active_code;
	/**中银E盾*/
	private UsbKeyText usbkeyText;
	/** 是否上传短信验证码 */
	private boolean isSms = false;
	/** 是否上传动态口令 */
	private boolean isOtp = false;
	/** 加密控件——动态口令 */
	private SipBox sipBoxActiveCode = null;
	/** 加密控件——手机交易码 */
	private SipBox sipBoxSmc = null;
	/** 加密控件里的随机数 */
	protected String randomNumber;
	/** 动态口令 */
	private String otpStr;
	/** 手机交易码 */
	private String smcStr;
	/** 加密之后加密随机数 */
	private String otp_password_RC = "";
	private String smc_password_RC = "";
	/** 账户详情及余额 */
	private Map<String, String> callbackmap;
	/** 输入页面最大可充值金额 */
	private TextView trans_max_input;
	private String maxAmount;
	private boolean isfirst = true;
	/**登录后的commConversationId*/
	private String commConversationId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.acc_finance_menu_2));
		// 右上角按钮赋值
		setText(this.getString(R.string.acc_rightbtn_go_main));
		// 添加布局
		view = addView(R.layout.acc_financeic_transfer_main);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		// 初始化界面
		init();
		// 初始化监听事件
		iniListener();
		mRadioButton_trans.setChecked(true);
		changeView.removeAllViews();
		changeView.addView(chooseView);
		// 隐藏所有视图
		goneAllView();
		mRadioGroup.setVisibility(View.INVISIBLE);
		requestAccountList();

		setBackBtnClick(backClickListener);
		setLeftSelectedPosition("accountManager_4");
	}

	/** 返回点击事件 */
	OnClickListener backClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			backListener();
		}
	};

	/** 初始化主页面 */
	private void init() {
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getResources().getString(
								R.string.acc_financeic_step1),
						this.getResources().getString(
								R.string.acc_financeic_step2),
						this.getResources().getString(
								R.string.acc_financeic_step3) });
		StepTitleUtils.getInstance().setTitleStep(1);
		// 电子现金账户信息
		financeIcAccountList = AccDataCenter.getInstance()
				.getFinanceIcAccountList();
		viewList = new ArrayList<View>();

		changeView = (RelativeLayout) view.findViewById(R.id.rl_change_view);
		mRadioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
		mRadioButton_trans = (RadioButton) view.findViewById(R.id.btn_transfer);
		mRadioButton_trans_relevance = (RadioButton) view
				.findViewById(R.id.btn_relevancetrans);
		chooseView = (View) getLayoutInflater().inflate(
				R.layout.acc_financeic_trans_choose, null);
		inputView = (View) getLayoutInflater().inflate(
				R.layout.acc_financeic_transfer_input, null);
		confirmView = (View) getLayoutInflater().inflate(
				R.layout.acc_financeic_transfer_confirm, null);
		successView = (View) getLayoutInflater().inflate(
				R.layout.acc_financeic_transfer_success, null);
		viewList.add(0, chooseView);
		viewList.add(1, inputView);
		viewList.add(2, confirmView);
		viewList.add(3, successView);
	}

	/** 初始化主界面监听事件 */
	private void iniListener() {
		mRadioGroup.setOnCheckedChangeListener(this);
	}

	/** 初始化选择账户界面 */
	private void initChooseView() {
		i = 0;
		back.setVisibility(View.VISIBLE);
		mRadioGroup.setVisibility(View.VISIBLE);
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getResources().getString(
								R.string.acc_financeic_step1),
						this.getResources().getString(
								R.string.acc_financeic_step2),
						this.getResources().getString(
								R.string.acc_financeic_step3) });
		StepTitleUtils.getInstance().setTitleStep(1);
		chooseView.setVisibility(View.VISIBLE);
		lvBankAccountList = (ListView) chooseView
				.findViewById(R.id.acc_accountlist);
		btnNext = (Button) chooseView.findViewById(R.id.btnNext);
		btnNext.setOnClickListener(nextBtnClick);

	}

	/** 初始化输入信息页面 */
	private void initInputView() {
		i = 1;
		back.setVisibility(View.VISIBLE);
		mRadioGroup.setVisibility(View.VISIBLE);
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getResources().getString(
								R.string.acc_financeic_step1),
						this.getResources().getString(
								R.string.acc_financeic_step2),
						this.getResources().getString(
								R.string.acc_financeic_step3) });
		StepTitleUtils.getInstance().setTitleStep(2);
		inputView.setVisibility(View.VISIBLE);
		// 为本人充值界面
		LinearLayout ll_trans = (LinearLayout) inputView
				.findViewById(R.id.ll_financeic_trans);
		// 为他人充值界面
		LinearLayout ll_transrel = (LinearLayout) inputView
				.findViewById(R.id.ll_financeic_transrel);
		if (mRadioButton_trans.isChecked()) {
			ll_trans.setVisibility(View.VISIBLE);
			ll_transrel.setVisibility(View.GONE);
			/** 电子现金账户账号列表 */
			final List<String> icNumberList = new ArrayList<String>();
			/** 电子现金账户id列表 */
			final List<String> icIdList = new ArrayList<String>();
			for (int i = 0; i < financeIcAccountList.size(); i++) {
				icNumberList.add((String) financeIcAccountList.get(i).get(
						Acc.ACC_ACCOUNTNUMBER_RES));
				icIdList.add((String) financeIcAccountList.get(i).get(
						Acc.ACC_ACCOUNTID_RES));
			}
			TextView trans_bankact = (TextView) inputView
					.findViewById(R.id.acc_financeic_trans_bankact_value);
			Spinner choose_icact = (Spinner) inputView
					.findViewById(R.id.spinner_choose_actnum);
			TextView trans_max = (TextView) inputView
					.findViewById(R.id.acc_financeic_trans_maxnum);
			trans_max_input = (TextView) inputView
					.findViewById(R.id.acc_financeic_trans_maxnum_value);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(
					FinanceIcTransferMainActivity.this, trans_max);
			final EditText trans_number = (EditText) inputView
					.findViewById(R.id.et_financeic_trans_num_value);
			trans_number.setText("");
			String bankact = (String) chooseTransAccount
					.get(Acc.ACC_ACCOUNTNUMBER_RES);
			trans_bankact.setText(StringUtil.getForSixForString(bankact));
			// 币种金额 选择的账户赋值操作
			// 币种、金额列表

			final List<String> icNumberParseList = new ArrayList<String>();
			for (String s : icNumberList) {
				icNumberParseList.add(StringUtil.getForSixForString(s));
			}
			ArrayAdapter<ArrayList<String>> adapter1 = new ArrayAdapter(this,
					R.layout.custom_spinner_item, icNumberParseList);
			adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			choose_icact.setAdapter(adapter1);
			choose_icact
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
							chooseactnum = icNumberList.get(position);
							chooseactid = icIdList.get(position);
							requestDetail(chooseactid);
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
							chooseactnum = icNumberList.get(0);
							chooseactid = icIdList.get(0);
							requestDetail(chooseactid);
						}
					});
			// 输入信息页下一步
			Button btnNext = (Button) inputView.findViewById(R.id.btnNext);
			btnNext.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					inputtransbanlance = trans_number.getText().toString()
							.trim();
					// 充值金额
					RegexpBean reb = new RegexpBean(
							FinanceIcTransferMainActivity.this
									.getString(R.string.financeic_trans_value_regex),
							inputtransbanlance, "financeIcAmount");

					ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
					lists.add(reb);
					if (RegexpUtils.regexpDate(lists)) {// 校验通过
						// 判断充值金额
						double maxvalue = Double.valueOf(maxAmount);
						if (maxvalue >= Double.valueOf(inputtransbanlance)) {
							// 最大可充值金额大于充值金额
							// 隐藏所有视图
							goneAllView();
							// 添加确认信息视图
							changeView.addView(confirmView);
							// 初始化确认信息页面
							initConfirmView();
							confirmView.setVisibility(View.VISIBLE);

						} else {
							BaseDroidApp
									.getInstanse()
									.showInfoMessageDialog(
											FinanceIcTransferMainActivity.this
													.getString(R.string.acc_error_transnumber));
						}

					}
				}
			});
		} else if (mRadioButton_trans_relevance.isChecked()) {
			ll_trans.setVisibility(View.GONE);
			ll_transrel.setVisibility(View.VISIBLE);
			/** 转出账户 */
			TextView transrel_bankact = (TextView) inputView
					.findViewById(R.id.acc_financeic_transrel_bankact_value);
			/** 收款人姓名 */
			final EditText transrel_payeeName = (EditText) inputView
					.findViewById(R.id.acc_financeic_transrel_payeeName_value);
			// 限制收款人姓名长度
			EditTextUtils.setLengthMatcher(FinanceIcTransferMainActivity.this,
					transrel_payeeName, 60);
			/** 电子现金账户 */
			final EditText transrel_icact = (EditText) inputView
					.findViewById(R.id.acc_financeic_transrel_icact_value);
			/** 重复电子现金账户 */
			final EditText transrel_icact_repeat = (EditText) inputView
					.findViewById(R.id.acc_financeic_transrel_icact_repeat_value);
			TextView icact_repeat = (TextView) inputView
					.findViewById(R.id.acc_financeic_transrel_icact_repeat);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
					icact_repeat);
			/** 转账金额 */
			final EditText transrel_num = (EditText) inputView
					.findViewById(R.id.et_financeic_transrel_num_value);
			transrel_payeeName.setText(transrelPayName);
			transrel_icact.setText(chooseactnum);
			transrel_icact_repeat.setText(chooseactnum);
			transrel_num.setText(inputtransbanlance);
			String bankact = (String) chooseTransAccount
					.get(Acc.ACC_ACCOUNTNUMBER_RES);
			transrel_bankact.setText(StringUtil.getForSixForString(bankact));
			// 为他人充值输入信息页下一步
			Button btnNext = (Button) inputView
					.findViewById(R.id.btntransrelNext);
			btnNext.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					transrelPayName = transrel_payeeName.getText().toString()
							.trim();
					chooseactnum = transrel_icact.getText().toString().trim();
					inputtransbanlance = transrel_num.getText().toString()
							.trim();
					// 以下为验证
					// 收款人姓名
					RegexpBean reb0 = new RegexpBean(
							FinanceIcTransferMainActivity.this
									.getString(R.string.acc_regex_act),
							transrelPayName, "payeeName_notEmpty");
					// 电子现金账户为空
					RegexpBean reb = new RegexpBean("",
							chooseactnum, "eCashAccout");
					// 重复电子现金账户
					RegexpBean reb1 = new RegexpBean("",
							transrel_icact_repeat.getText().toString().trim(),
							"repeateCashAccout");
					// 充值金额
					RegexpBean reb2 = new RegexpBean(
							FinanceIcTransferMainActivity.this
									.getString(R.string.financeic_trans_value_regex),
							inputtransbanlance, "financeIcAmount");
					ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
					lists.add(reb0);
					lists.add(reb);
					lists.add(reb1);
					lists.add(reb2);
					if (RegexpUtils.regexpDate(lists)) {// 校验通过
						if (chooseactnum.equalsIgnoreCase(transrel_icact_repeat
								.getText().toString().trim())) {
							// 两次输入相同
							// 为他人充值预交易
							requestCommConversationId();
							BaseHttpEngine.showProgressDialog();
						} else {
							BaseDroidApp
									.getInstanse()
									.showInfoMessageDialog(
											FinanceIcTransferMainActivity.this
													.getString(R.string.acc_error_transcommon));
							return;

						}
					}
				}
			});
		}
	}

	/** 初始化确认信息页面 */
	private void initConfirmView() {
		i = 2;
		back.setVisibility(View.VISIBLE);
		mRadioGroup.setVisibility(View.GONE);
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getResources().getString(
								R.string.acc_financeic_step2),
						this.getResources().getString(
								R.string.acc_financeic_step3),
						this.getResources().getString(
								R.string.acc_financeic_step4) });
		StepTitleUtils.getInstance().setTitleStep(2);
		confirmView.setVisibility(View.VISIBLE);
		// 为他人充值使用—默认隐藏
		/** 收款人姓名 */
		LinearLayout transfer_name = (LinearLayout) confirmView
				.findViewById(R.id.transfer_name);

		/** 转出账户 */
		TextView trans_bankact = (TextView) confirmView
				.findViewById(R.id.acc_financeic_trans_bankact_value);
		/** 电子现金账户 */
		TextView choose_icact = (TextView) confirmView
				.findViewById(R.id.acc_financeic_trans_icact_value);
		/** 最大充值金额 */
		TextView trans_max_value = (TextView) confirmView
				.findViewById(R.id.acc_financeic_trans_maxnum_value);
		LinearLayout maxtrans_ll = (LinearLayout) confirmView
				.findViewById(R.id.maxtrans_ll);
		TextView trans_max = (TextView) confirmView
				.findViewById(R.id.acc_financeic_trans_maxnum);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				BaseDroidApp.getInstanse().getCurrentAct(), trans_max);
		/** 金额 */
		TextView trans_number = (TextView) confirmView
				.findViewById(R.id.acc_financeic_trans_num_value);
		// 中银E盾
		usbkeyText = (UsbKeyText) view.findViewById(R.id.sip_usbkey);
		// 动态口令
		ll_active_code = (LinearLayout) view.findViewById(R.id.ll_active_code);
		// 手机交易码
		ll_smc = (LinearLayout) view.findViewById(R.id.ll_smc);
		String bankact = (String) chooseTransAccount
				.get(Acc.ACC_ACCOUNTNUMBER_RES);
		trans_bankact.setText(StringUtil.getForSixForString(bankact));
		choose_icact.setText(StringUtil.getForSixForString(chooseactnum));
		trans_number.setText(StringUtil.parseStringPattern(inputtransbanlance,
				2));
		if (mRadioButton_trans_relevance.isChecked()) {
			maxtrans_ll.setVisibility(View.GONE);
			trans_max.setVisibility(View.GONE);
			trans_max_value.setVisibility(View.GONE);
			transfer_name.setVisibility(View.VISIBLE);
			/** 收款人姓名 */
			TextView transrel_payeename = (TextView) confirmView
					.findViewById(R.id.acc_financeic_transrel_name_value);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(
					BaseDroidApp.getInstanse().getCurrentAct(),
					transrel_payeename);
			transrel_payeename.setText(transrelPayName);
			factorList = (List<Map<String, Object>>) iCTransRelfermap
					.get(Acc.RELEVANCEACCPRE_ACC_FACTORLIST_RES);
			// 动态口令
			// sipBoxActiveCode = (SipBox)
			// view.findViewById(R.id.sipbox_active);
			// sipBoxActiveCode
			// .setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
			// sipBoxActiveCode.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
			// sipBoxActiveCode.setId(10002);
			// sipBoxActiveCode.setBackgroundResource(R.drawable.bg_for_edittext);
			// sipBoxActiveCode.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
			// sipBoxActiveCode
			// .setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
			// sipBoxActiveCode.setSingleLine(true);
			// sipBoxActiveCode.setSipDelegator(this);
			// sipBoxActiveCode
			// .setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
			// 手机交易码
			// sipBoxSmc = (SipBox) view.findViewById(R.id.sipbox_smc);
			// sipBoxSmc.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
			// sipBoxSmc.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
			// sipBoxSmc.setId(10002);
			// sipBoxSmc.setBackgroundResource(R.drawable.bg_for_edittext);
			// sipBoxSmc.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
			// sipBoxSmc
			// .setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
			// sipBoxSmc.setSingleLine(true);
			// sipBoxSmc.setSipDelegator(this);
			// sipBoxSmc.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
			// Button smsBtn = (Button) view.findViewById(R.id.smsbtn);
			// SmsCodeUtils.getInstance().addSmsCodeListner(smsBtn,
			// new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// // 发送验证码到手机
			// sendMSCToMobile();
			// }
			// });
			// initSmcAndOtp();
			requestForRandomNumber();
			
		} else {
			ll_smc.setVisibility(View.GONE);
			ll_active_code.setVisibility(View.GONE);
			transfer_name.setVisibility(View.GONE);
			maxtrans_ll.setVisibility(View.VISIBLE);
			trans_max.setVisibility(View.VISIBLE);
			trans_max_value.setVisibility(View.VISIBLE);
			trans_max_value
					.setText(StringUtil.parseStringPattern(maxAmount, 2));
		}
		// 确认信息页确定
		Button btnSure = (Button) confirmView.findViewById(R.id.btnSure);
		btnSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mRadioButton_trans_relevance.isChecked()) {
//					// 动态口令
//					RegexpBean sipRegexpBean = new RegexpBean(
//							FinanceIcTransferMainActivity.this
//									.getString(R.string.active_code_regex),
//							sipBoxActiveCode.getText().toString(),
//							ConstantGloble.SIPOTPPSW);
//					RegexpBean sipSmcpBean = new RegexpBean(
//							FinanceIcTransferMainActivity.this
//									.getString(R.string.acc_smc_regex),
//							sipBoxSmc.getText().toString(),
//							ConstantGloble.SIPSMCPSW);
//					ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
//					if (isSms) {
//						lists.add(sipSmcpBean);
//					}
//					if (isOtp) {
//						lists.add(sipRegexpBean);
//					}
//					if (RegexpUtils.regexpDate(lists)) {// 校验通过
//
//						if (isSms) {
//							try {
//								smcStr = sipBoxSmc.getValue()
//										.getEncryptPassword();
//								smc_password_RC = sipBoxSmc.getValue()
//										.getEncryptRandomNum();
//							} catch (CodeException e) {
//								LogGloble.exceptionPrint(e);
//							}
//						}
//						if (isOtp) {
//							try {
//								otpStr = sipBoxActiveCode.getValue()
//										.getEncryptPassword();
//								otp_password_RC = sipBoxActiveCode.getValue()
//										.getEncryptRandomNum();
//							} catch (CodeException e) {
//								LogGloble.exceptionPrint(e);
//							}
//						}
//						
//					}
					BaseHttpEngine.showProgressDialog();
					checkDate();
				} else {
					// 请求conversationid
					requestCommConversationId();
					BaseHttpEngine.showProgressDialog();
				}

			}
		});
	}

	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox() {	
		commConversationId = (String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		usbkeyText.Init(commConversationId, randomNumber, iCTransRelfermap,
				this);
		isOtp = usbkeyText.getIsOtp();
		isSms = usbkeyText.getIsSmc();
		initOtpSipBox();
		initSmcSipBox();
	}
	/**初始化动态口令*/
	private void initOtpSipBox(){
		if (isOtp == false)
			return;
		ll_active_code.setVisibility(View.VISIBLE);
		sipBoxActiveCode = (SipBox) view.findViewById(R.id.sipbox_active);
		// add by 2016年9月12日 luqp 修改
		SipBoxUtils.initSipBoxWithTwoType(sipBoxActiveCode, ConstantGloble.MIN_LENGTH,
				ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//		sipBoxActiveCode.setCipherType(SystemConfig.CIPHERTYPE);
//		sipBoxActiveCode.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		sipBoxActiveCode.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		sipBoxActiveCode.setId(10002);
//		sipBoxActiveCode.setBackgroundResource(R.drawable.bg_for_edittext);
//		sipBoxActiveCode.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		sipBoxActiveCode.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//		sipBoxActiveCode.setSingleLine(true);
//		sipBoxActiveCode.setSipDelegator(this);
//		sipBoxActiveCode.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		sipBoxActiveCode.setRandomKey_S(randomNumber);
	}
	/**初始化smc---短信验证码*/
	private void initSmcSipBox() {
		if (isSms == false)
			return;
		ll_smc.setVisibility(View.VISIBLE);
		Button smsBtn = (Button) view.findViewById(R.id.smsbtn);
		SmsCodeUtils.getInstance().addSmsCodeListner(smsBtn,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 发送验证码到手机
						sendMSCToMobile();
					}
				});
		sipBoxSmc = (SipBox) view.findViewById(R.id.sipbox_smc);
		// add by 2016年9月12日 luqp 修改
		SipBoxUtils.initSipBoxWithTwoType(sipBoxSmc, ConstantGloble.MIN_LENGTH,
				ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//		sipBoxSmc.setCipherType(SystemConfig.CIPHERTYPE);
//		sipBoxSmc.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		sipBoxSmc.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		sipBoxSmc.setId(10002);
//		sipBoxSmc.setBackgroundResource(R.drawable.bg_for_edittext);
//		sipBoxSmc.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		sipBoxSmc.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//		sipBoxSmc.setSingleLine(true);
//		sipBoxSmc.setSipDelegator(this);
//		sipBoxSmc.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		sipBoxSmc.setRandomKey_S(randomNumber);
	}
	/**验证动态口令*/
	private void checkDate(){
		//音频Key安全工具认证
		usbkeyText.checkDataUsbKey(sipBoxActiveCode, sipBoxSmc, new IUsbKeyTextSuccess() {
			
			@Override
			public void SuccessCallBack(String result, int errorCode) {
				// TODO Auto-generated method stub
				BaseHttpEngine.showProgressDialog();
				// 为他人充值提交交易的tokenid
				pSNGetTokenId(0);
			}
		});
	}
	/** 判断是动态口令还是手机交易码 */
//	public void initSmcAndOtp() {
//		if (!StringUtil.isNullOrEmpty(factorList)) {
//			for (int i = 0; i < factorList.size(); i++) {
//				Map<String, Object> itemMap = factorList.get(i);
//				Map<String, String> securityMap = (Map<String, String>) itemMap
//						.get(Inves.FIELD);
//				String name = securityMap.get(Inves.NAME);
//				if (Inves.Smc.equals(name)) {
//					isSms = true;
//					ll_smc.setVisibility(View.VISIBLE);
//				} else if (Inves.Otp.equals(name)) {
//					isOtp = true;
//					ll_active_code.setVisibility(View.VISIBLE);
//				}
//			}
//		}
//	}

	/** 初始化成功信息页面 */
	private void initSuccessView() {
		i = 3;
		mRadioGroup.setVisibility(View.GONE);
		back.setVisibility(View.GONE);
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getResources().getString(
								R.string.acc_financeic_step2),
						this.getResources().getString(
								R.string.acc_financeic_step3),
						this.getResources().getString(
								R.string.acc_financeic_step4) });
		StepTitleUtils.getInstance().setTitleStep(3);
		successView.setVisibility(View.VISIBLE);
		/** 收款人姓名 */
		LinearLayout transrel_success_name = (LinearLayout) successView
				.findViewById(R.id.transrel_success_name);
		/** 交易序号 */
		TextView trans_action = (TextView) successView
				.findViewById(R.id.acc_financeic_trans_action_value);
		/** 转出账户 */
		TextView trans_bankact = (TextView) successView
				.findViewById(R.id.acc_financeic_trans_bankact_value);
		/** 电子现金账户 */
		TextView choose_icact = (TextView) successView
				.findViewById(R.id.acc_financeic_trans_icact_value);
		/** 最大可充值金额 */
		TextView trans_max_value = (TextView) successView
				.findViewById(R.id.acc_financeic_trans_maxnum_value);
		/** 充值金额 */
		TextView trans_number = (TextView) successView
				.findViewById(R.id.acc_financeic_trans_num_value);
		LinearLayout maxtrans_ll = (LinearLayout) successView
				.findViewById(R.id.maxtrans_ll);
		// 赋值操作
		trans_action.setText((String) financeictransback
				.get(Acc.ICTRANS_TRANSACTIONID_RES));
		String bankact = (String) chooseTransAccount
				.get(Acc.ACC_ACCOUNTNUMBER_RES);
		trans_bankact.setText(StringUtil.getForSixForString(bankact));
		choose_icact.setText(StringUtil.getForSixForString(chooseactnum));

		trans_number.setText(StringUtil.parseStringPattern(inputtransbanlance,
				2));
		TextView trans_max = (TextView) confirmView
				.findViewById(R.id.acc_financeic_trans_maxnum);

		if (mRadioButton_trans_relevance.isChecked()) {
			// 为他人充值
			maxtrans_ll.setVisibility(View.GONE);
			trans_max.setVisibility(View.GONE);
			trans_max_value.setVisibility(View.GONE);
			transrel_success_name.setVisibility(View.VISIBLE);
			TextView transrel_name = (TextView) successView
					.findViewById(R.id.acc_financeic_transrel_name_value);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(
					BaseDroidApp.getInstanse().getCurrentAct(), transrel_name);
			transrel_name.setText(transrelPayName);
		} else {
			PopupWindowUtils.getInstance().setOnShowAllTextListener(
					BaseDroidApp.getInstanse().getCurrentAct(), trans_max);
			maxtrans_ll.setVisibility(View.VISIBLE);
			trans_max.setVisibility(View.VISIBLE);
			trans_max_value.setVisibility(View.VISIBLE);
			trans_max_value
					.setText(StringUtil.parseStringPattern(maxAmount, 2));
			// 为本人充值
			transrel_success_name.setVisibility(View.GONE);
		}
		// 成功信息页确定
		Button btnSure = (Button) successView.findViewById(R.id.btnSure);
		btnSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changeView.removeAllViews();
				changeView.addView(chooseView);
				selectposition = -1;
				initFirstValue();
				// 初始化选择转出账户页面
				initChooseView();
				requestAccountList();
			}
		});
	}

	public void initFirstValue() {
		chooseactnum = "";
		chooseactid = "";
		inputtransbanlance = "";
		trans_tokenId = "";
		transrelPayName = "";
		if (!StringUtil.isNullOrEmpty(financeictransback)) {
			financeictransback.clear();
		}
		if (!StringUtil.isNullOrEmpty(iCTransRelfermap)) {
			iCTransRelfermap.clear();
		}
		i = 0;
		if (!StringUtil.isNullOrEmpty(factorList)) {
			factorList.clear();
		}
		isSms = false;
		isOtp = false;
		randomNumber = "";
		otpStr = "";
		smcStr = "";
		otp_password_RC = "";
		smc_password_RC = "";
		if (!StringUtil.isNullOrEmpty(callbackmap)) {
			callbackmap.clear();
		}
		maxAmount = "";
	}

	/**
	 * RadioGroup点击CheckedChanged监听
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (checkedId == R.id.btn_transfer) {
			if (isfirst) {
				isfirst = false;
			} else {
				initFirstValue();
				// 为本人充值
				closeInput();
				changeView.removeAllViews();
				changeView.addView(chooseView);
				selectposition = -1;
				refreshList();
			}

		} else if (checkedId == R.id.btn_relevancetrans) {
			initFirstValue();
			// 为他人充值
			closeInput();
			changeView.removeAllViews();
			changeView.addView(chooseView);
			selectposition = -1;
			refreshList();
		}

	}

	/**
	 * 刷新list
	 */
	public void refreshList() {
		initChooseView();
		chooseView.setVisibility(View.VISIBLE);
		final BankAccountAdapter adapter = new BankAccountAdapter(
				FinanceIcTransferMainActivity.this, transAccountList, 3);
		lvBankAccountList.setAdapter(adapter);
		if (selectposition == -1) {

		} else {
			adapter.setSelectedPosition(selectposition);
		}
		lvBankAccountList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (selectposition == position) {
					return;
				} else {
					selectposition = position;
					adapter.setSelectedPosition(position);
				}
			}
		});
	}

	/** 选择账户下一步按钮点击事件 */
	OnClickListener nextBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (selectposition == -1) {
				// 没有选择账户
				BaseDroidApp
						.getInstanse()
						.showInfoMessageDialog(
								FinanceIcTransferMainActivity.this
										.getString(R.string.acc_financeic_choose_title_null));

				return;
			}
			// 选择的转出账户信息
			chooseTransAccount = transAccountList.get(selectposition);
			if (mRadioButton_trans.isChecked()) {
				requestFinanceIcAccountList();
			} else {
				goneAllView();
				changeView.removeAllViews();
				// 添加输入信息视图
				changeView.addView(inputView);
				initFirstValue();
				initInputView();
				inputView.setVisibility(View.VISIBLE);
			}
		}
	};

	/** 筛选列表 */
	public List<Map<String, Object>> chooseList(List<Map<String, Object>> list) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			// 判断
			// isECashAccount 类型119、103、104这个值是1的话就是电子现金账户
			// 新建签约300的时候纯IC卡
			String acc_type = (String) list.get(i).get(Acc.ACC_ACCOUNTTYPE_RES);
			String isECashAccount = (String) list.get(i).get(
					Acc.ACC_ISECASHACCOUNT_RES);
			if (StringUtil.isNull(isECashAccount)) {
				continue;
			}
			if (acc_type.equalsIgnoreCase(AccBaseActivity.accountTypeList
					.get(3))
					&& isECashAccount
							.equals(ConstantGloble.ACC_FINANCEIC_ISECASH_ONE)) {
				// 119
				resultList.add(list.get(i));
			}
			if (acc_type.equals(AccBaseActivity.accountTypeList.get(1))
					&& isECashAccount
							.equals(ConstantGloble.ACC_FINANCEIC_ISECASH_ONE)) {
				// 103
				resultList.add(list.get(i));
			}
			if (acc_type.equalsIgnoreCase(AccBaseActivity.accountTypeList
					.get(2))
					&& isECashAccount
							.equals(ConstantGloble.ACC_FINANCEIC_ISECASH_ONE)) {
				// 104
				resultList.add(list.get(i));
			}
			if (acc_type.equals(AccBaseActivity.accountTypeList.get(13))) {
				// 300
				resultList.add(list.get(i));
			}
		}
		return resultList;
	}

	@Override
	public void requestFinanceIcAccountListCallBack(Object resultObj) {
		super.requestFinanceIcAccountListCallBack(resultObj);
		financeIcAccountList = chooseList(financeIcAccountList);
		if (financeIcAccountList == null || financeIcAccountList.size() == 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					FinanceIcTransferMainActivity.this
							.getString(R.string.acc_financeic_null));
			return;
		}
		AccDataCenter.getInstance().setFinanceIcAccountList(
				financeIcAccountList);
		goneAllView();
		// 添加输入信息视图
		if (mRadioButton_trans.isChecked()) {

		} else if (mRadioButton_trans_relevance.isChecked()) {
			changeView.removeAllViews();
			initFirstValue();
		}
		changeView.addView(inputView);
		initInputView();
		inputView.setVisibility(View.VISIBLE);
	}

	/** 隐藏所有视图 */
	public void goneAllView() {
		for (int i = 0; i < viewList.size(); i++) {
			viewList.get(i).setVisibility(View.GONE);
		}
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
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		if (mRadioButton_trans.isChecked()) {
			// 为本人充值
			pSNGetTokenId(1);
		} else if (mRadioButton_trans_relevance.isChecked()) {
			// 为他人充值——预交易
			requestGetSecurityFactor(financeIcTransferServiceId);
		}
	}

	/** 请求所有转出账户列表信息 */
	public void requestAccountList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.QRY_ACC_LIST_API);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		List<String> paramslist = new ArrayList<String>();
		paramslist.add(AccBaseActivity.accountTypeList.get(1));
		paramslist.add(AccBaseActivity.accountTypeList.get(2));
		paramslist.add(AccBaseActivity.accountTypeList.get(3));
		// paramslist.add(AccBaseActivity.accountTypeList.get(4));
		paramsmap.put(Acc.ACC_ACCOUNTTYPE_REQ, paramslist);
		biiRequestBody.setParams(paramsmap);
		BaseHttpEngine.showProgressDialogCanGoBack();
		HttpManager.requestBii(biiRequestBody, this,
				"requestAccountListCallBack");
	}

	/**
	 * 请求所有转出账户列表信息回调
	 * 
	 * @param resultObj
	 */
	public void requestAccountListCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		transAccountList = (List<Map<String, Object>>) (biiResponseBody
				.getResult());
		if (transAccountList == null || transAccountList.size() == 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showMessageDialog(
					this.getString(R.string.acc_null_choseact),
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							BaseDroidApp.getInstanse().dismissErrorDialog();
							finish();
						}
					});
			return;
		}
		mRadioGroup.setVisibility(View.VISIBLE);
		BiiHttpEngine.dissMissProgressDialog();
		// 初始化选择转出账户页面
		refreshList();
	}

	/**
	 * 获取tokenId
	 * 
	 * @param i
	 *            i==0 需要展示通讯框 否则不展示
	 */
	public void pSNGetTokenId(int i) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		if (i == 0) {
			BiiHttpEngine.showProgressDialog();
		}
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
		trans_tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(trans_tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		if (mRadioButton_trans.isChecked()) {
			// 请求为本人充值
			requestFinanceictransfer();
		} else if (mRadioButton_trans_relevance.isChecked()) {
			// 为他人充值提交交易
			requestICTransferNoRelevanceRes();
		}

	}

	/** 请求为本人充值 */
	public void requestFinanceictransfer() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.PSNFINANCEICTRANSFER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Acc.ICTRANS_BANKACCOUNTID_REQ,
				String.valueOf(chooseTransAccount.get(Acc.ACC_ACCOUNTID_RES)));
		paramsmap.put(Acc.ICTRANS_FINANCEICACCOUNTID_REQ, chooseactid);
		paramsmap.put(Acc.ICTRANS_AMOUNT_REQ, inputtransbanlance);
		paramsmap.put(Acc.ICTRANS_TOKEN_REQ, trans_tokenId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestFinanceictransferCallBack");
	}

	/**
	 * 请求为本人充值回调
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void requestFinanceictransferCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		financeictransback = (Map<String, Object>) (biiResponseBody.getResult());
		if (StringUtil.isNullOrEmpty(financeictransback)) {
			return;
		}
		// 隐藏所有视图
		goneAllView();
		// 添加确认信息视图
		changeView.addView(successView);
		// 初始化确认信息页面
		initSuccessView();
		successView.setVisibility(View.VISIBLE);
	}

	/** 请求账户详情 */
	public void requestDetail(String accountId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.ACC_ICACCOUNTDETAIL_API);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Acc.FINANCE_ACCOUNTID_REQ, accountId);
		biiRequestBody.setParams(paramsmap);
		// 通讯开始,开启通讯框
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestDetailCallBack");
	}

	/**
	 * 请求电子现金账户余额以及详细信息
	 * 
	 * @param resultObj
	 */
	public void requestDetailCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		callbackmap = (Map<String, String>) (biiResponseBody.getResult());
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(callbackmap)) {
			return;
		}
		maxAmount = callbackmap.get(Acc.FINANCEICDETAIL_MAXLOADINGAMOUNT_RES);
		trans_max_input.setText(StringUtil.parseStringPattern(maxAmount, 2));
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseDroidApp.getInstanse().showSeurityChooseDialog(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 预交易
						requestICTransferNoRelevance();
					}
				});
	}

	/** 为他人充值预交易请求 */
	public void requestICTransferNoRelevance() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.PSNICTRANSFERNORELEVANCE_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Acc.ICTRANSREL_BANKACCOUNTID_REQ,
				String.valueOf(chooseTransAccount.get(Acc.ACC_ACCOUNTID_RES)));
		paramsmap.put(Acc.ICTRANSREL_ICACTNUM_REQ, chooseactnum);
		paramsmap.put(Acc.ICTRANSREL_COMBINID_REQ, BaseDroidApp.getInstanse()
				.getSecurityChoosed());
		paramsmap.put(Acc.ICTRANSREL_PAYEENAME_REQ, transrelPayName);
		paramsmap.put(Acc.ICTRANSREL_AMOUNT_REQ, inputtransbanlance);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestICTransferNoRelevanceCallBack");
	}

	/**
	 * 为他人充值预交易请求回调
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void requestICTransferNoRelevanceCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		iCTransRelfermap = (Map<String, Object>) (biiResponseBody.getResult());
		if (StringUtil.isNullOrEmpty(iCTransRelfermap)) {
			return;
		}
		confirmView = (View) getLayoutInflater().inflate(
				R.layout.acc_financeic_transfer_confirm, null);
		// 隐藏所有视图
		goneAllView();
		confirmView.setVisibility(View.VISIBLE);
		// 添加确认信息视图
		changeView.removeAllViews();
		changeView.addView(confirmView);
		// 初始化确认信息页面
		initConfirmView();
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
		// sipBoxActiveCode.setRandomKey_S(randomNumber);
		// sipBoxSmc.setRandomKey_S(randomNumber);
		BiiHttpEngine.dissMissProgressDialog();
		// 跟据返回初始化中银E盾加密控件
		initSipBox();
	}

	public void sendMSCToMobile() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.SET_SENDMSC);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "sendMSCToMobileCallback");
	}

	public void sendMSCToMobileCallback(Object resultObj) {
		// 通讯结束,关闭通讯框
	}

	/** 请求为他人充值提交交易 */
	public void requestICTransferNoRelevanceRes() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.ICTRANSFERNORELEVANCERES_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Acc.ICTRANSRELRES_BANKACCOUNTID_REQ,
				String.valueOf(chooseTransAccount.get(Acc.ACC_ACCOUNTID_RES)));
		paramsmap.put(Acc.ICTRANSRELRES_ICACTNUM_REQ, chooseactnum);
		paramsmap.put(Acc.ICTRANSRELRES_PAYEENAME_REQ, transrelPayName);
//		if (isOtp) {
//			paramsmap.put(Comm.Otp, otpStr);
//			paramsmap.put(Comm.Otp_Rc, otp_password_RC);
//
//		}
//		if (isSms) {
//			paramsmap.put(Comm.Smc, smcStr);
//			paramsmap.put(Comm.Smc_Rc, smc_password_RC);
//		}
		// CA密文
//		paramsmap
//				.put(Acc.LOSSRESULT_ACC_SIGNEDDATA_REQ,
//						(String) iCTransRelfermap
//								.get(Acc.ICTRANSREL_ACC_PLAINDATA_RES));
		paramsmap.put(Acc.ICTRANSRELRES_AMOUNT_REQ, inputtransbanlance);
		paramsmap.put(Acc.ICTRANSRELRES_TOKEN_REQ, trans_tokenId);
		/**安全工具随机参数获取*/
		usbkeyText.InitUsbKeyResult(paramsmap);
		SipBoxUtils.setSipBoxParams(paramsmap);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestICTransferNoRelevanceResCallBack");
	}

	/**
	 * 请求为他人充值提交交易回调
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void requestICTransferNoRelevanceResCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		financeictransback = (Map<String, Object>) (biiResponseBody.getResult());
		if (StringUtil.isNullOrEmpty(financeictransback)) {
			return;
		}
		// 隐藏所有视图
		goneAllView();
		// 添加确认信息视图
		changeView.removeAllViews();
		changeView.addView(successView);
		// 初始化确认信息页面
		initSuccessView();
		successView.setVisibility(View.VISIBLE);
	}

	public void backListener() {
		switch (i) {
		case 0:
			// 选择页面
			closeInput();
			finish();
			break;
		case 1:
			// 输入页面
			i = 0;
			closeInput();
			if (mRadioButton_trans.isChecked()) {
				changeView.removeView(inputView);
				// 隐藏所有视图
				goneAllView();
				chooseView.setVisibility(View.VISIBLE);
				mRadioGroup.setVisibility(View.VISIBLE);
				StepTitleUtils
						.getInstance()
						.initTitldStep(
								FinanceIcTransferMainActivity.this,
								new String[] {
										FinanceIcTransferMainActivity.this
												.getResources()
												.getString(
														R.string.acc_financeic_step1),
										FinanceIcTransferMainActivity.this
												.getResources()
												.getString(
														R.string.acc_financeic_step2),
										FinanceIcTransferMainActivity.this
												.getResources()
												.getString(
														R.string.acc_financeic_step3) });
				StepTitleUtils.getInstance().setTitleStep(1);
			} else if (mRadioButton_trans_relevance.isChecked()) {
				changeView.removeAllViews();
				changeView.addView(chooseView);
				// 隐藏所有视图
				goneAllView();
				chooseView.setVisibility(View.VISIBLE);
				mRadioGroup.setVisibility(View.VISIBLE);
				StepTitleUtils
						.getInstance()
						.initTitldStep(
								FinanceIcTransferMainActivity.this,
								new String[] {
										FinanceIcTransferMainActivity.this
												.getResources()
												.getString(
														R.string.acc_financeic_step1),
										FinanceIcTransferMainActivity.this
												.getResources()
												.getString(
														R.string.acc_financeic_step2),
										FinanceIcTransferMainActivity.this
												.getResources()
												.getString(
														R.string.acc_financeic_step3) });
				StepTitleUtils.getInstance().setTitleStep(1);
			}

			break;
		case 2:
			// 确认页面
			i = 1;
			closeInput();
			if (mRadioButton_trans.isChecked()) {
				changeView.removeView(confirmView);
			} else if (mRadioButton_trans_relevance.isChecked()) {
				changeView.removeAllViews();
				changeView.addView(inputView);
			}
			// 隐藏所有视图
			goneAllView();
			inputView.setVisibility(View.VISIBLE);
			mRadioGroup.setVisibility(View.VISIBLE);
			StepTitleUtils.getInstance().initTitldStep(
					FinanceIcTransferMainActivity.this,
					new String[] {
							FinanceIcTransferMainActivity.this.getResources()
									.getString(R.string.acc_financeic_step1),
							FinanceIcTransferMainActivity.this.getResources()
									.getString(R.string.acc_financeic_step2),
							FinanceIcTransferMainActivity.this.getResources()
									.getString(R.string.acc_financeic_step3) });
			StepTitleUtils.getInstance().setTitleStep(2);

			break;
		case 3:
			// 成功页面
			closeInput();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			backListener();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
