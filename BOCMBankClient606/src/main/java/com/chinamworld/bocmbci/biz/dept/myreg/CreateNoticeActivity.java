package com.chinamworld.bocmbci.biz.dept.myreg;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 约定转存协议 用户选择接受或不接受
 * 
 * @author wjp
 * 
 */
public class CreateNoticeActivity extends DeptBaseActivity {
	private static final String TAG = "CreateNoticeActivity";

	private LinearLayout tabcontent;// 主Activity显示
	private View view = null;
	/** 关闭 */
	private Button quickTrade = null;
	/** 上一步按钮 */
	// private Button lastBtn = null;
	/** 下一步按钮 */
	private Button nextBtn = null;
	/** 币种 */
	private int typeFlag = 0;
	// /** 币种 */
	// private String currencyCode = null;
	/** 存款金额 */
	private String saveAmount = null;
	/** 附言 */
	private String attachMessage = null;
	/** 执行方式 */
	private String excuteWayFlag = null;
	/** 约定方式 */
	private String promiseWay = null;
	/** 业务类型 */
	private String businessType = null;

	/** 建立通知content布局 */
	private LinearLayout noticeContentLayout;
	/** 填写信息Layout */
	private RelativeLayout writeLayout;
	/** 支取金额 */
	private EditText checkoutMoneyEt;
	/** 支取金额 */
	private TextView checkoutMoneyTv;
	/** 转入账户layout */
	private LinearLayout accInLayout;
	/** 转入账户下拉列表 */
	private Spinner accInSpinner;
	/** 转入账户账号列表 */
	List<String> accountIds;
	List<String> acountNumberList;

	private RadioGroup checkoutType;
	private RadioGroup checkoutWay;
	private RadioButton partRb;
	private RadioButton allRb;
	/** 现金 */
	private RadioButton cashRb;
	/** 手动转账 */
	private RadioButton selfRb;
	/** 自动转账 */
	private RadioButton autoRb;

	private LinearLayout checkoutMoneyEtLayout;
	private LinearLayout checkoutMoneyTvLayout;
	/** 支取金额 */
	private String avaliableMoney;

	private String fromAccountId;
	private String acountNumber;
	private String volumeNumber;
	private String cdNumber;
	private String cdTerm;// 必输1：1天 7：7天
	/** 提款类型 */
	private String redrawType;// 必输0：现金 1：手动转账 2：自动转账
	private String toAccountId;
	private String toAccountNumber;
	/** 支取方式 */
	private String drawMode;// 必输N：部分支取 Y：全部支取
	private String drawAmount;
	private String currency;
	private String convertType;
	private String cashRemit;
	private String depositBalance;
	private String drawDate;
	private String token;

	/** 一天后时间 */
	private String oneDayLater;
	/** 七天后时间 */
	private String sevenDayLater;

	/** 底部layout */
	private LinearLayout footLayout;
	/** 左侧菜单 */
	private Button showBtn;

	private LinearLayout cashRemitLayout;
	/** 自动转账查询到的转入账户列表 */
	List<Map<String, Object>> accountInList;
	/** 提款类别标识 */
	private int typeGetMoney = 0;
	/** 支取方式标识 */
	private int typeCheck = 0;
	/** 支取金额标识 */
	private String amountCheck = "";
	private int spinnerPosition = 0;
	private boolean isBack = false;
	private boolean isConfirm = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		LayoutInflater inflater = LayoutInflater.from(this); // 加载理财产品布局
		view = inflater.inflate(R.layout.dept_mysave_create_notice, null);
		tabcontent.addView(view);
		// 隐藏底部菜单
		footLayout = (LinearLayout) this.findViewById(R.id.foot_layout);
		footLayout.setVisibility(View.GONE);
		// 隐藏左侧菜单
		showBtn = (Button) this.findViewById(R.id.btn_show);
		showBtn.setVisibility(View.GONE);
		setLeftButtonPopupGone();
		((Button) this.findViewById(R.id.ib_back)).setVisibility(View.GONE);

		setTitle(R.string.create_notify);

		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getResources().getString(R.string.write_message),
						this.getResources().getString(R.string.confirm_message),
						this.getResources().getString(R.string.operate_success) });
		// 获取数据
		aquireData();
		// 初始化控件
		init();
	}

	/** 获取数据 */
	private void aquireData() {
		// 存储转出账户信息
		accOutInfoMap = DeptDataCenter.getInstance().getAccountContent();
		fromAccountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
		acountNumber = (String) accOutInfoMap.get(Comm.ACCOUNTNUMBER);
		Map<String, Object> curDetailContent = DeptDataCenter.getInstance().getCurDetailContent();
		if (curDetailContent == null) {
			// TODO 待处理
		}
		volumeNumber = (String) curDetailContent.get(Dept.VOLUME_NUMBER);
		cdNumber = (String) curDetailContent.get(Dept.CD_NUMBER);
		cdTerm = (String) curDetailContent.get(Dept.CD_PERIOD);
		currency = (String) curDetailContent.get(Comm.CURRENCYCODE);
		cashRemit = (String) curDetailContent.get(Dept.CASHREMIT);
		convertType = (String) curDetailContent.get(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE);
		depositBalance = (String) curDetailContent.get(Dept.BOOKBALANCE);
		depositBalance = StringUtil.parseStringPattern(depositBalance, 2).replaceAll(",", "");
		// drawDate
		drawMode = DRAW_MODE_N;
		redrawType = ConstantGloble.REDRAWTYPE_CASH;// 现金
	}

	/** 初始化所有的控件 */
	private void init() {
		quickTrade = (Button) this.findViewById(R.id.ib_top_right_btn);
		// quickTrade.setVisibility(View.GONE);
		quickTrade.setText(this.getResources().getString(R.string.close));
		quickTrade.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { // 返回到跳转页面
				setResult(RESULT_CANCELED);
				finish();
				overridePendingTransition(R.anim.no_animation, R.anim.n_pop_exit_bottom_down);
			}
		});
		noticeContentLayout = (LinearLayout) view.findViewById(R.id.dept_create_notice_layout);
		showWriteLayout();
	}

	/** 填写信息页面 */
	private void showWriteLayout() {
		StepTitleUtils.getInstance().setTitleStep(1);
		ibBack.setVisibility(View.GONE);
		writeLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.dept_mysave_create_notice_write, null);
		initContentView(writeLayout);

		// RadioGroup 提款类型
		checkoutType = (RadioGroup) writeLayout.findViewById(R.id.dept_checkout_type_radiogroup);

		cashRb = (RadioButton) writeLayout.findViewById(R.id.dept_cash_rb);
		selfRb = (RadioButton) writeLayout.findViewById(R.id.dept_self_rb);
		autoRb = (RadioButton) writeLayout.findViewById(R.id.dept_auto_rb);

		checkoutType.setOnCheckedChangeListener(checkoutTypeRadioListener);
		// Layout 转入账户
		accInLayout = (LinearLayout) writeLayout.findViewById(R.id.dept_acc_in_layout);
		// Spinner 转入账户
		accInSpinner = (Spinner) writeLayout.findViewById(R.id.dept_acc_in_spinner);
		// RadioGroup 支取方式
		checkoutWay = (RadioGroup) writeLayout.findViewById(R.id.dept_checkout_way_radiogroup);
		partRb = (RadioButton) checkoutWay.getChildAt(0);
		allRb = (RadioButton) checkoutWay.getChildAt(1);
		checkoutWay.setOnCheckedChangeListener(checkoutWayRadioListener);
		checkoutMoneyEtLayout = (LinearLayout) writeLayout.findViewById(R.id.dept_checkout_money_etlayout);
		// EditText 支取金额
		checkoutMoneyEt = (EditText) writeLayout.findViewById(R.id.dept_checkout_money_et);
		// TextView 支取金额
		checkoutMoneyTvLayout = (LinearLayout) writeLayout.findViewById(R.id.dept_checkout_money_tvlayout);
		checkoutMoneyTv = (TextView) writeLayout.findViewById(R.id.dept_checkout_money_tv);
		avaliableMoney = (String) DeptDataCenter.getInstance().getCurDetailContent().get(Dept.AVAILABLE_BALANCE);
		// 格式化 小数点
		String strMoney = StringUtil.parseStringCodePattern(currency, avaliableMoney, 2);

		// avaliableMoney = StringUtil.parseStringPattern(avaliableMoney,
		// 2).replaceAll(",", "");
		checkoutMoneyTv.setText(strMoney);

		if (typeGetMoney == 0) {
			cashRb.setChecked(true);
			// accInLayout.setVisibility(View.GONE);
			// toAccountId = null;
			// // checkoutWay
			// // .setOnCheckedChangeListener(checkoutWayRadioListener);
			// allRb.setEnabled(true);
			// allRb.setVisibility(View.VISIBLE);
			// redrawType = ConstantGloble.REDRAWTYPE_CASH;// 现金
		} else if (typeGetMoney == 1) {
			selfRb.setChecked(true);
			// accInLayout.setVisibility(View.GONE);
			// toAccountId = null;
			// // checkoutWay
			// // .setOnCheckedChangeListener(checkoutWayRadioListener);
			// allRb.setEnabled(true);
			// allRb.setVisibility(View.VISIBLE);
			// redrawType = ConstantGloble.REDRAWTYPE_SELF;// 手动转账
			// typeGetMoney = 2;
		} else {
			autoRb.setChecked(true);
			accInLayout.setVisibility(View.VISIBLE);
			// 发送通讯请求转入账户信息
			// requestAutoInBankAccountList();
			/* edit by wanbing */
			// checkoutWay.setEnabled(false);
			// checkoutWay.setOnClickListener(null);
			// partRb.setChecked(true);
			// drawMode = DRAW_MODE_N;
			// allRb.setChecked(false);
			// allRb.setVisibility(View.GONE);
			allRb.setVisibility(View.VISIBLE);
			/* edit by wanbing */
			redrawType = ConstantGloble.REDRAWTYPE_AUTO;// 自动转账
			typeGetMoney = 3;
			accountInList = DeptDataCenter.getInstance().getAccountInList();
			if (StringUtil.isNullOrEmpty(accountInList)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						this.getResources().getString(R.string.no_have_relevant_account));
			}
			accountIds = new ArrayList<String>();
			acountNumberList = new ArrayList<String>();
			for (int i = 0; i < accountInList.size(); i++) {
				String accountId = (String) accountInList.get(i).get(Comm.ACCOUNT_ID);
				String accountNumer = (String) accountInList.get(i).get(Comm.ACCOUNTNUMBER);
				accountIds.add(accountId);
				acountNumberList.add(StringUtil.getForSixForString(String.valueOf(accountNumer)));
			}
			// 去掉转出账户
			for (int i = 0; i < accountIds.size(); i++) {
				if (fromAccountId.equals(accountIds.get(i))) {
					accountIds.remove(i);
					acountNumberList.remove(i);
					break;
				}
			}

			ArrayAdapter<String> accInAdapter = new ArrayAdapter<String>(BaseDroidApp.getInstanse().getCurrentAct(),
					R.layout.dept_spinner, acountNumberList);
			accInAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			accInSpinner.setAdapter(accInAdapter);
			accInSpinner.setOnItemSelectedListener(accInSpinnerListener);
			if (spinnerPosition < acountNumberList.size()) {
				accInSpinner.setSelection(spinnerPosition);
			}
		}

		if (typeCheck == 0) {
			partRb.setChecked(true);
			// checkoutMoneyEtLayout.setVisibility(View.VISIBLE);
			// checkoutMoneyTvLayout.setVisibility(View.GONE);
			// autoRb.setVisibility(View.VISIBLE);
			// drawMode = DRAW_MODE_N;
		} else {
			allRb.setChecked(true);
			// checkoutMoneyEtLayout.setVisibility(View.GONE);
			// autoRb.setVisibility(View.GONE);
			// if (null != redrawType &&
			// ConstantGloble.REDRAWTYPE_AUTO.equals(redrawType))
			// cashRb.setChecked(true);
			// checkoutMoneyTvLayout.setVisibility(View.VISIBLE);
			// drawMode = DRAW_MODE_Y;
		}
		checkoutMoneyEt.setText(amountCheck);

		// lastBtn = (Button) writeLayout.findViewById(R.id.btnLast);
		nextBtn = (Button) writeLayout.findViewById(R.id.btnNext);
		// 上一步按钮事件
		// lastBtn.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// finish();
		//
		// }
		// });
		// 下一步按钮事件
		nextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 判断输入
				// 如果是部分支取 用判断输入金额
				if (!allRb.isChecked()) {
					String amount = checkoutMoneyEt.getText().toString().trim();
					amountCheck = checkoutMoneyEt.getText().toString().trim();
					if (StringUtil.isNullOrEmpty(amount)) {
						BaseDroidApp.getInstanse().showInfoMessageDialog(
								CreateNoticeActivity.this.getResources().getString(R.string.input_checkout_money));
						return;
					} else if (!Pattern.matches("(?!^0$)(?!^[0]{2,})(?!^0[1-9]+)(?!^0\\.[0]*$)^\\d{1,11}(\\.\\d{1,2})?$",
							amount)) {
						BaseDroidApp.getInstanse().showInfoMessageDialog(
								getText(R.string.trade_amount_maximum_prompt).toString());
						return;
					} else if (Double.parseDouble(amount) > Double.parseDouble(avaliableMoney)) {
						BaseDroidApp.getInstanse().showInfoMessageDialog(
								CreateNoticeActivity.this.getResources().getString(R.string.amount_wrong));
						return;
					}

					// add by luqp 2015年11月12日15:21:12 用户输入金额和存单金额小于50000提示
					// 把存单金额 格式化成整数位
					if (currency.equals(ConstantGloble.PRMS_CODE_RMB)) {
						String strBalance = StringUtil.deleateNumber(avaliableMoney);
						if (strBalance.contains(",")) { // 去掉存单金额里面的逗号
							strBalance = strBalance.replaceAll(",", "");
						}
						// 账户余额减去用户输入金额
						String residualAmount = getResidualAmount(strBalance, amount);
						// 用户输入金额 转成double类型
						double inputAmount = Double.parseDouble(amount);
						double depositAmount = Double.parseDouble(residualAmount);
						if (inputAmount < 50000 || depositAmount < 50000) { // 如果输入金额和存单剩余金额小于50000 提示.
							String errorMsg = CreateNoticeActivity.this.getResources().getString(R.string.notice_save_money);
							BaseDroidApp.getInstanse().showInfoMessageDialog(errorMsg);
							return;
						}
					}
					/////////////end
					
				}
				// 查询系统时间
				requestSystemDateTime();
				BiiHttpEngine.showProgressDialog();
			}
		});
		noticeContentLayout.removeAllViews();
		noticeContentLayout.addView(writeLayout);
		isConfirm = false;
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		oneDayLater = QueryDateUtils.getOneDayLater(dateTime);
		sevenDayLater = QueryDateUtils.getSevenDayLater(dateTime);
		showConfirmLayout();
	}

	/** 确认页面 */
	private void showConfirmLayout() {
		StepTitleUtils.getInstance().setTitleStep(2);

		ibBack.setVisibility(View.VISIBLE);
		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isBack = true;
				showWriteLayout();
			}
		});
		RelativeLayout confirmLayout = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.dept_mysave_create_notice_confirm, null);
		initContentView(confirmLayout);
		// Layout 转入账户
		LinearLayout accInLayout = (LinearLayout) confirmLayout.findViewById(R.id.dept_acc_in_layout);
		// TextView 转入账户
		TextView accInTv = (TextView) confirmLayout.findViewById(R.id.dept_acc_in_tv);
		if (toAccountId != null) {
			accInLayout.setVisibility(View.VISIBLE);
			accInTv.setText(StringUtil.getForSixForString(toAccountNumber));
		}

		initMiddleView(confirmLayout);

		// lastBtn = (Button) confirmLayout.findViewById(R.id.btnLast);
		nextBtn = (Button) confirmLayout.findViewById(R.id.btnNext);
		// 上一步按钮事件
		// lastBtn.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // 返回到外汇行情页面
		// noticeContentLayout.removeAllViews();
		// noticeContentLayout.addView(writeLayout);
		// StepTitleUtils.getInstance().setTitleStep(1);
		// }
		// });
		// 下一步按钮事件
		nextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 发送通讯 建立通知存款 申请conversationId
				if (redrawType == ConstantGloble.REDRAWTYPE_AUTO) {
					if (StringUtil.isNullOrEmpty(accountInList)) {
						BaseDroidApp.getInstanse().showInfoMessageDialog(
								CreateNoticeActivity.this.getResources().getString(R.string.no_have_relevant_account));
						return;
					}
				}
				requestCommConversationId();
				BaseHttpEngine.showProgressDialog();
			}
		});

		noticeContentLayout.removeAllViews();
		noticeContentLayout.addView(confirmLayout);
		isConfirm = true;
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		token = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
		// 建立通知
		requestCreateNotice(fromAccountId, volumeNumber, cdNumber, cdTerm, redrawType, toAccountId, drawMode, drawAmount,
				currency, convertType, cashRemit, depositBalance, drawDate, token);
	}

	/** 成功页面 */
	private void showSuccessLayout() {
		StepTitleUtils.getInstance().setTitleStep(3);
		ibBack.setVisibility(View.GONE);

		RelativeLayout successLayout = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.dept_mysave_create_notice_success, null);
		initContentView(successLayout);

		initMiddleView(successLayout);
		// Layout 转入账户
		LinearLayout accInLayout = (LinearLayout) successLayout.findViewById(R.id.dept_acc_in_layout);
		// TextView 转入账户
		TextView accInTv = (TextView) successLayout.findViewById(R.id.dept_acc_in_tv);
		if (toAccountId != null) {
			accInLayout.setVisibility(View.VISIBLE);
			accInTv.setText(StringUtil.getForSixForString(toAccountNumber));
		}

		nextBtn = (Button) successLayout.findViewById(R.id.btnConfirm);
		nextBtn.setText(this.getResources().getString(R.string.finish));
		// 确定按钮事件
		nextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		noticeContentLayout.removeAllViews();
		noticeContentLayout.addView(successLayout);
		isConfirm = false;
	}

	private void initContentView(View layout) {
		// TextView 定期账户
		TextView accountNoTv = (TextView) layout.findViewById(R.id.dept_acc_no_tv);
		accountNoTv.setText(StringUtil.getForSixForString(acountNumber));
		// TextView 存折册号
		TextView volumeNumberTv = (TextView) layout.findViewById(R.id.dept_volume_number_tv);
		volumeNumberTv.setText(volumeNumber);
		// TextView 存单序号
		TextView cdNumberTv = (TextView) layout.findViewById(R.id.dept_cdnumber_tv);
		cdNumberTv.setText(cdNumber);
		// TextView 币种
		TextView currencyTv = (TextView) layout.findViewById(R.id.dept_currency_tv);
		currencyTv.setText(LocalData.Currency.get(currency));
		//
		// 炒汇标识
		TextView cashRemitTv = (TextView) layout.findViewById(R.id.dept_cashremit_tv);
		cashRemitTv.setText(LocalData.CurrencyCashremit.get(cashRemit));

		cashRemitLayout = (LinearLayout) layout.findViewById(R.id.dept_cashremit_layout);
		if (currency.equals(ConstantGloble.PRMS_CODE_RMB)) {
			cashRemitLayout.setVisibility(View.INVISIBLE);
		}
		// TextView 钞汇标志
		// TextView 业务品种 根据存期而来 1 为一天通知存款 7 为七天通知存款
		TextView businessTypeTv = (TextView) layout.findViewById(R.id.dept_business_type_tv);
		if (cdTerm.equalsIgnoreCase(BUSINESSTYPE_ONEDAY) || cdTerm.equalsIgnoreCase(BUSINESSTYPE_SEVENDAY)) {
			businessTypeTv.setText(LocalData.CallCDTerm.get(cdTerm));
		} else {
			businessTypeTv.setText(cdTerm);
		}
		// TextView 约定方式
		TextView promiseWayTv = (TextView) layout.findViewById(R.id.dept_promise_way_tv);
		promiseWayTv.setText(LocalData.ConventionConvertType.get(convertType));

	}

	private void initMiddleView(View layout) {
		// TextView 提款类型
		TextView checkoutTypeTv = (TextView) layout.findViewById(R.id.dept_checkout_type_tv);
		checkoutTypeTv.setText(LocalData.redrawType.get(redrawType));
		// TextView 支取方式
		TextView checkoutWayTv = (TextView) layout.findViewById(R.id.dept_checkout_way_tv);
		checkoutWayTv.setText(LocalData.drawMode.get(drawMode));
		// TextView 预约支取日期
		TextView checkoutDayTv = (TextView) layout.findViewById(R.id.dept_promise_checkout_day_tv);
		if (cdTerm.equalsIgnoreCase(BUSINESSTYPE_ONEDAY)) {
			drawDate = oneDayLater;
		} else if (cdTerm.equalsIgnoreCase(BUSINESSTYPE_SEVENDAY)) {
			drawDate = sevenDayLater;
		}
		checkoutDayTv.setText(drawDate);
		// TextView 存单金额
		TextView saveMoneyTv = (TextView) layout.findViewById(R.id.dept_avaliable_money_tv);
		// 格式化 小数点
		String avaMoney = StringUtil.parseStringCodePattern(currency, avaliableMoney, 2);
		// String avaMoney = StringUtil.parseStringPattern(avaliableMoney, 2);
		saveMoneyTv.setText(avaMoney);
		// TextView 支取金额
		TextView checkoutTv = (TextView) layout.findViewById(R.id.dept_checkout_money_tv);
		if (drawMode.equals(DRAW_MODE_N)) {
			drawAmount = checkoutMoneyEt.getText().toString().trim();
			// 修改部分支取金额确认页面和成功页面不显示小数点部分 add by 2016年4月21日 luqp
			drawAmount = StringUtil.parseStringCodePattern(currency, drawAmount, 2);
		} else {
			// drawAmount = avaliableMoney;
			// 格式化 小数点
			drawAmount = StringUtil.parseStringCodePattern(currency, avaliableMoney, 2);
		}
		checkoutTv.setText(drawAmount);
	}

	/** 提款类型监听 */
	private OnCheckedChangeListener checkoutTypeRadioListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.dept_cash_rb:// 现金
				accInLayout.setVisibility(View.GONE);
				toAccountId = null;
				// checkoutWay
				// .setOnCheckedChangeListener(checkoutWayRadioListener);
				allRb.setEnabled(true);
				allRb.setVisibility(View.VISIBLE);
				redrawType = ConstantGloble.REDRAWTYPE_CASH;// 现金
				typeGetMoney = 0;
				break;
			case R.id.dept_self_rb:// 收到转账
				accInLayout.setVisibility(View.GONE);
				toAccountId = null;
				// checkoutWay
				// .setOnCheckedChangeListener(checkoutWayRadioListener);
				allRb.setEnabled(true);
				allRb.setVisibility(View.VISIBLE);
				redrawType = ConstantGloble.REDRAWTYPE_SELF;// 手动转账
				typeGetMoney = 1;
				break;
			case R.id.dept_auto_rb:// 自动转账
				accInLayout.setVisibility(View.VISIBLE);
				allRb.setEnabled(true);
				allRb.setVisibility(View.VISIBLE);
				/* edit by wanbing */
				redrawType = ConstantGloble.REDRAWTYPE_AUTO;// 自动转账
				typeGetMoney = 2;
				if (!isBack) {
					// 发送通讯请求转入账户信息
					requestAutoInBankAccountList();
					// accInLayout.setVisibility(View.VISIBLE);
					// // 发送通讯请求转入账户信息
					// requestAutoInBankAccountList();
					/* edit by wanbing */
					// checkoutWay.setEnabled(false);
					// checkoutWay.setOnClickListener(null);
					// partRb.setChecked(true);
					// drawMode = DRAW_MODE_N;
					// allRb.setChecked(false);
					// allRb.setVisibility(View.GONE);
					// allRb.setEnabled(true);
					// allRb.setVisibility(View.VISIBLE);
					// /* edit by wanbing */
					// redrawType = ConstantGloble.REDRAWTYPE_AUTO;// 自动转账
					// typeGetMoney = 2;
				} else {
					isBack = false;
				}
				break;
			default:
				break;
			}
		}

	};

	/** 支取方式监听 */
	private OnCheckedChangeListener checkoutWayRadioListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.dept_checkout_part_rb:// 部分支取
				checkoutMoneyEtLayout.setVisibility(View.VISIBLE);
				checkoutMoneyTvLayout.setVisibility(View.GONE);
				autoRb.setVisibility(View.VISIBLE);
				drawMode = DRAW_MODE_N;
				typeCheck = 0;
				break;
			case R.id.dept_checkout_all_rb:// 全部支取
				checkoutMoneyEtLayout.setVisibility(View.GONE);
				/* edit by wanbing */
				// autoRb.setVisibility(View.GONE);
				// if (null != redrawType &&
				// ConstantGloble.REDRAWTYPE_AUTO.equals(redrawType))
				// cashRb.setChecked(true);
				autoRb.setVisibility(View.VISIBLE);
				/* edit by wanbing */

				checkoutMoneyTvLayout.setVisibility(View.VISIBLE);
				drawMode = DRAW_MODE_Y;
				typeCheck = 1;
				break;

			default:
				break;
			}
		}
	};

	/** 转入账户下路列表监听 */
	private OnItemSelectedListener accInSpinnerListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			toAccountId = accountIds.get(position);
			toAccountNumber = acountNumberList.get(position);
			spinnerPosition = position;
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			toAccountId = accountIds.get(0);
			toAccountNumber = acountNumberList.get(0);
			spinnerPosition = 0;
		}
	};

	@Override
	public void communicationCallBack(int flag) {
		switch (flag) {
		case CREATE_NOTICE_CALLLBAK:
			showSuccessLayout();
			break;
		case QUERY_ALL_IN_ACCOUNT_CALLBACK:// 查询转入账户通讯返回
			accountInList = DeptDataCenter.getInstance().getAccountInList();
			if (StringUtil.isNullOrEmpty(accountInList)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						this.getResources().getString(R.string.no_have_relevant_account));
			}
			accountIds = new ArrayList<String>();
			acountNumberList = new ArrayList<String>();
			for (int i = 0; i < accountInList.size(); i++) {
				String accountId = (String) accountInList.get(i).get(Comm.ACCOUNT_ID);
				String accountNumer = (String) accountInList.get(i).get(Comm.ACCOUNTNUMBER);
				accountIds.add(accountId);
				acountNumberList.add(StringUtil.getForSixForString(String.valueOf(accountNumer)));
			}
			// 去掉转出账户
			for (int i = 0; i < accountIds.size(); i++) {
				if (fromAccountId.equals(accountIds.get(i))) {
					accountIds.remove(i);
					acountNumberList.remove(i);
					break;
				}
			}

			ArrayAdapter<String> accInAdapter = new ArrayAdapter<String>(BaseDroidApp.getInstanse().getCurrentAct(),
					R.layout.dept_spinner, acountNumberList);
			accInAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			accInSpinner.setAdapter(accInAdapter);
			accInSpinner.setOnItemSelectedListener(accInSpinnerListener);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isConfirm) {
				showWriteLayout();
			} else {
				setResult(RESULT_CANCELED);
				finish();
				overridePendingTransition(R.anim.no_animation, R.anim.n_pop_exit_bottom_down);
			}
		}
		return true;
	}

	/**
	 * add by lqp 2015年11月12日11:37:24 得到剩余存款
	 * 
	 * @param blance
	 *            存单金额
	 * @param bb
	 *            输入金额
	 * @return 返回剩余金额
	 */
	private String getResidualAmount(String blance, String bb) {
		int b1 = Integer.parseInt(blance);
		int b2 = Integer.parseInt(bb);
		return String.valueOf(b1 - b2);
	}
}
