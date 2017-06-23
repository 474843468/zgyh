package com.chinamworld.bocmbci.biz.dept.myreg;

import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.biz.dept.notmg.NotifyManageActivity;
import com.chinamworld.bocmbci.biz.dept.savereg.SaveRegularActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 存款管理 我的定期存款 "确认" & "成功页面"
 * 
 * @author luqp 2015年8月13日10:11:14
 */
public class MyPositDrawMoneConfirmActivity extends DeptBaseActivity {
	private static final String TAG = "MyPositDrawMoneConfirmActivity";
	private static final int CHOOSE_DATE_PICKER_ID = 0;
	private static final int START_DATE_PICKER_ID = 1;
	private static final int END_DATE_PICKER_ID = 2;

	private Context context = this;
	private LinearLayout tabcontent;// 主Activity显示
	private View view = null;
	/** 存单类型 */
	private String saveType = null;
	/** 上一步按钮 */
	// private Button btnLast;
	/** 确定按钮 */
	private Button btnConfirm;
	/** 确定信息内容Layout */
	private LinearLayout checkoutContentLayout;

	private Button toMainBtn;
	/** 当前卡 详情数据 */
	private Map<String, Object> curDetail;
	/** 通讯返回数据 */
	private Map<String, Object> checkoutCallBackMap;
	/** 标题 */
	private TextView titleTv;
	/** Layout 交易序列号 */
	private LinearLayout transationLayout;
	/** 存折册号 */
	private LinearLayout saveVolumeLayout;
	/** 存单号 */
	private LinearLayout banksheetLayout;
	/** Layout 转存方式 */
	private LinearLayout tranmodeLayout;
	/** Layout 钞汇标志 */
	private LinearLayout cashRemitLayout;
	/** Layout 预约支取日期 */
	private LinearLayout promiseExeDay;
	/** Layout 支取日期 */
	private LinearLayout execudeDayLayout;
	/** Layout 开始日期 */
	private LinearLayout startDayLayout;
	/** /** Layout 开始日期 */
	private LinearLayout endDayLayout;
	/** 周期Layout */
	private LinearLayout weekDayLayout;
	/** 执行次数 */
	private LinearLayout executeTimesLayout;
	/** 约定支取日期Layout */
	private LinearLayout promiseCheckOutDay;
	/** 执行方式标识 */
	private int executeWayFlag;

	/** 转出账户号 */
	private TextView accOutNoTv;
	/** 存折册号 */
	private TextView bankBookTv;
	/** 存单号 */
	private TextView bankSheetTv;
	/** 转出账户 */
	private TextView accInNoTv;
	/** 转存方式标签 */
	private TextView tranModeLabel;
	/** 转存方式 */
	private TextView tranModeTv;
	/** 币种标签 */
	private TextView label_moneyType;
	/** 币种 */
	private TextView currencyTv;
	/** 钞汇标志 */
	private TextView cashRemitTv;
	/** 支取金额或者续存金额 显示文字 */
	private TextView moneyTv;
	/** 支取金额或者续存金额 */
	private TextView checkoutMoneyTv;
	/** 附言 */
	private TextView messageTv;
	/** 执行方式 */
	private TextView executeWayTv;
	/** 执行日期 */
	private TextView executeDateTv;
	/** 开始日期 */
	private TextView startDateTv;
	/** 结束日期 */
	private TextView endDateTv;
	/** 周期 */
	private TextView weekDateTv;
	/** 执行次数 */
	private TextView executeTimesTv;

	/** 转出账户 */
	private String fromAccountId;
	/** 转入账户 */
	private String toAccountId;
	/** 币种 */
	private String currency = "";
	/** 金额 */
	private String amount;
	/** 钞汇标志 */
	private String cashRemit;
	/** 备注 */
	private String remark;
	/** 村折册号 */
	private String passBookNumber;
	/** 存单号 */
	private String cdNumber;
	/** 执行方式 */
	private String transMode;
	/** 约定方式 */
	private String promiseWay;
	/** 执行日期 */
	private String executeDate;
	/** 防重标识 */
	private String token;
	/** 周期 */
	private String cycleSelect;
	/** 开始日期 */
	private String startDate;
	/** 结束日期 */
	private String endDate;
	/** 转存方式 */
	private String convertType;
	/** 存期 */
	private String cdTerm;
	/** 通知编号 */
	private String noticeNo;
	/** 是否通知银行 */
	private String scheduled;
	/** 存取种类 */
	private String cdType;
	/** 约定支取日期 */
	private String preDrawDate;
	/** 续存金额 */
	private String continueSaveMoney;
	/** 日期选择 */
	private TextView chooseDateTv;
	/** 开始日期 */
	private TextView chooseStartDateTv;
	/** 结束日期 */
	private TextView chooseEndDateTv;
	/** 周期 */
	private Spinner weekSpinner;
	/** 转出账户号码 */
	private String strAccOutNumber;
	/** 转入账户号码 */
	private String strAccInNumber;
	/** 确定 */
	private String strConfirm;
	/** 当前系统时间一天后 */
	private String oneDateLater;
	/** 当前系统时间七天后 */
	private String sevenDateLater;
	/** 执行次数 */
	private int executeTimes;
	/** 支取方式 */
	private String strDrawMode;
	/** 到期日 */
	private String drawDate;
	/** 是否是通知管理跳转标示 */
	private int notifyFlag;
	/** 存单可用余额 */
	private String callDepositBalance;
	/** 是否到期支取 */
	private boolean isExpire;

	private int startYear, startMonth, startDay;
	private int endYear, endMonth, endDay;

	private String appointedAmount;
	/** 是否是从我要存定期跳转过来的 */
	private boolean isSaveRegEdu = false;
	/** 当前页面是否为完成页面 */
	private boolean isFinish = false;
	private boolean isPre = false;

	// ////////////////////////////////////////////
	/** 基准费用布局 */
	private LinearLayout ll_reference_cost;
	/** 优惠后费用布局 */
	private LinearLayout ll_discount_cost;
	/** 优惠后费用 提示信息 */
	private LinearLayout ll_discount_cost_prompted;
	/** 优惠后费用 提示信息 */
	private TextView tv_discount_cost_prompted;
	/** 汇款套餐 提示信息布局 */
	private LinearLayout ll_remit_flag;
	/** 汇款套餐 提示信息信息 */
	private TextView remit_flag;
	/** 基准费用 */
	private TextView reference_cost_tv;
	/** 优惠后费用 */
	private TextView discount_cost_tv;
	/** 基准费用 */
	private String needCommission = null;
	/** 优惠后费用 */
	private String preCommission = null;
	/** 试费查询是否成功标识位 1:查询成功 0:查询失败 */
	private String isChargeFlag = null;
	/** 是否签约汇款笔数套餐标识位 */
	private String remitSetMealFlag = null;
	/** 优惠后费用数据 */
	private Map<String, Object> flagData = null;
	/** 是否显示基准费用 当基准费用字段返回是为true 没有返回此字段则为false */
	private Boolean isShowBenchmark = false;
	/** 成功页面 优惠后费用*/
	private String preferential = null;
	// ///////////////////////////////////////////////////////
	// add by luqp 2015年12月11日14:51:50 优惠后费用修改
	// 预约周期显示 单笔基准费用 单笔优惠后费用\
	/** 单笔基准费用 */
	private LinearLayout single_reference_cost = null;
	/** 单笔基准费用 PopupWindow */
	private TextView single_reference = null;
	/** 单笔基准费用金额 */
	private TextView reference_cost = null;
	/** 单笔优惠后费用 */
	private LinearLayout single_discount_cost = null;
	/** 单笔优惠后费用 PopupWindow */
	private TextView single_discount = null;
	/** 单笔优惠后费用金额 */
	private TextView discount_cost = null;
	// //////////////////////////////////////////////////////

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setTitle(R.string.whole_save_checkout);

		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		LayoutInflater inflater = LayoutInflater.from(this); // 加载布局
		view = inflater.inflate(R.layout.dept_save_checkout_confirm, null);
		tabcontent.addView(view);

		// 步骤栏
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
		// 转出账户信息
		accOutInfoMap = DeptDataCenter.getInstance().getAccOutInfoMap();
		// 转入账户信息
		accInInfoMap = DeptDataCenter.getInstance().getAccInInfoMap();
		// 当前账户详情
		curDetail = DeptDataCenter.getInstance().getCurDetailContent();

		isSaveRegEdu = this.getIntent().getBooleanExtra("isSaveRegEdu", false);
		if (isSaveRegEdu) {
			curDetail = DeptDataCenter.getInstance().getAccInInfoMap();
		}

		transMode = this.getIntent().getStringExtra(Dept.NEW_NOTIFY_SAVE_TRANSMODE);
		continueSaveMoney = this.getIntent().getStringExtra(Dept.AMOUNT);
		amount = this.getIntent().getStringExtra(Dept.AMOUNT);
		remark = this.getIntent().getStringExtra(Dept.MEMO);
		noticeNo = this.getIntent().getStringExtra(Dept.SCHEDULE_NUMBER);
		strConfirm = this.getResources().getString(R.string.confirm);
		strDrawMode = this.getIntent().getStringExtra(Dept.DRAW_MODE);
		// preDrawDate = this.getIntent().getStringExtra(Dept.DRAW_DATE);
		isExpire = this.getIntent().getBooleanExtra("isExpire", true);

		// add 2015年11月9日19:20:44 lqp 约定支取日期
		List<Map<String, Object>> noticeDetail = DeptDataCenter.getInstance().getQueryNotifyCallBackList();
		if (!StringUtil.isNullOrEmpty(noticeDetail)) {
			preDrawDate = (String) noticeDetail.get(0).get(Dept.DRAW_DATE);
		} else {
			preDrawDate = this.getIntent().getStringExtra(Dept.DRAW_DATE);
		}

		convertType = (String) curDetail.get(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE);
		cdTerm = (String) curDetail.get(Dept.CD_PERIOD);
		saveType = (String) curDetail.get(Dept.TYPE);
		if (StringUtil.isNullOrEmpty(saveType)) {
			saveType = (String) curDetail.get(Comm.ACCOUNT_TYPE);
		}

		notifyFlag = this.getIntent().getIntExtra(NOTIFY_FLAG, 0);

		callDepositBalance = (String) curDetail.get(Dept.AVAILABLE_BALANCE);
		callDepositBalance = StringUtil.parseStringCodePattern(currency, callDepositBalance, 2).replaceAll(",", "");// 解决三位小数的金额数字上送时报错

		appointedAmount = (String) curDetail.get(Dept.BOOKBALANCE);
		appointedAmount = StringUtil.parseStringCodePattern(currency, appointedAmount, 2).replaceAll(",", "");

		// //////////////////////////////////////////////////////////////////////////////////
		// // 手续费试算 start /////
		// /////////////////////////////////////////////////////////////////////////////////
		// add by luqp 2015年11月23日10:58:23
		if (saveType.equals(WHOE_SAVE)) { // 整存整取
			// 支取整存整取 获得手续费试算的map数据
			flagData = DeptDataCenter.getInstance().getDrawMoneyMap();
			if (flagData.containsKey(Dept.Save_needCommissionCharge)) {
				isShowBenchmark = true;
			}
			isChargeFlag = (String) flagData.get(Dept.Save_getChargeFlag);
			remitSetMealFlag = (String) flagData.get(Dept.Save_remitSetMealFlag);
			String need = (String) flagData.get(Dept.Save_needCommissionCharge);
			String pre = (String) flagData.get(Dept.Save_preCommissionCharge);
			needCommission = DeptBaseActivity.parseStringCodePattern(currency, need, 2);
			preCommission = DeptBaseActivity.parseStringCodePattern(currency, pre, 2);
		} else if (saveType.equals(RANDOM_SAVE)) { // 定活两便
			// 支取定活两便 获得手续费试算的map数据
			flagData = DeptDataCenter.getInstance().getDrawFixedMoneyMap();
			if (flagData.containsKey(Dept.Save_needCommissionCharge)) {
				isShowBenchmark = true;
			}
			isChargeFlag = (String) flagData.get(Dept.Save_getChargeFlag);
			remitSetMealFlag = (String) flagData.get(Dept.Save_remitSetMealFlag);
			String need = (String) flagData.get(Dept.Save_needCommissionCharge);
			String pre = (String) flagData.get(Dept.Save_preCommissionCharge);
			needCommission = DeptBaseActivity.parseStringCodePattern(currency, need, 2);
			preCommission = DeptBaseActivity.parseStringCodePattern(currency, pre, 2);
		} else if (saveType.equals(NOTIFY_SAVE)) { // 通知存款
			// 支取 通知存款 通知存款金额 存单金额-支取金额
			flagData = DeptDataCenter.getInstance().getInformMoneyMap();
			if (flagData.containsKey(Dept.Save_needCommissionCharge)) {
				isShowBenchmark = true;
			}
			isChargeFlag = (String) flagData.get(Dept.Save_getChargeFlag);
			remitSetMealFlag = (String) flagData.get(Dept.Save_remitSetMealFlag);
			String need = (String) flagData.get(Dept.Save_needCommissionCharge);
			String pre = (String) flagData.get(Dept.Save_preCommissionCharge);
			needCommission = DeptBaseActivity.parseStringCodePattern(currency, need, 2);
			preCommission = DeptBaseActivity.parseStringCodePattern(currency, pre, 2);
		} else if (saveType.equals(EDUCATION_SAVE1) || saveType.equals(EDUCATION_SAVE2)) {
			// 教育储蓄
		} else if (saveType.equals(ZERO_SAVE1) || saveType.equals(ZERO_SAVE2)) {
			// 零存整取 续存
		}

		// //////////////////////////////////////////////////////////////////////////////////
		// // 手续费试算 end /////
		// /////////////////////////////////////////////////////////////////////////////////
	}

	/**
	 * 初始化控件 140=存本取息 110=整存整取 150=零存整取 160=定活两便 166=通知存款 152=教育储蓄 912=零存整取
	 * 935=教育储蓄 913=存本取息
	 */
	private void init() {
		toMainBtn = (Button) this.findViewById(R.id.ib_top_right_btn);
		toMainBtn.setText(this.getResources().getString(R.string.go_main));
		toMainBtn.setVisibility(View.VISIBLE);
		toMainBtn.setTextColor(Color.WHITE);
		toMainBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ActivityTaskManager.getInstance().removeAllActivity();
			}
		});

		checkoutContentLayout = (LinearLayout) view.findViewById(R.id.dept_save_check_out_step);
		// btnLast = (Button) view.findViewById(R.id.btnLast);
		btnConfirm = (Button) view.findViewById(R.id.btnConfirm);

		switch (Integer.parseInt(transMode)) {
		case IMMEDIATELY_EXCUTE:// 不同存单类型 显示确定信息页面不同
			executeWayFlag = IMMEDIATELY_EXCUTE;
			showConfirmLayout();
			break;
		case PRE_DATE_EXCUTE:// 预约日期执行
			executeWayFlag = PRE_DATE_EXCUTE;
			BiiHttpEngine.showProgressDialogCanGoBack();
			requestSystemDateTime();
			break;
		case PRE_WEEK_EXCUTE:// 预约周期执行
			executeWayFlag = PRE_WEEK_EXCUTE;
			BiiHttpEngine.showProgressDialogCanGoBack();
			requestSystemDateTime();
			break;
		case ONE_DAY_EXCUTE:// 通知存款
			// 查询系统时间
			BiiHttpEngine.showProgressDialogCanGoBack();
			requestSystemDateTime();
			break;
		default:
			break;
		}
	}

	/** 初始化标题 */
	private void initTitle() {
		if (saveType.equals(EDUCATION_SAVE1) || saveType.equals(EDUCATION_SAVE2)) {
			setTitle(R.string.education_title);
		} else if (saveType.equals(ZERO_SAVE1) || saveType.equals(ZERO_SAVE2)) {
			setTitle(R.string.zero_save_title);
		} else if (saveType.equals(WHOE_SAVE)) {
			setTitle(R.string.whole_save_checkout);
		} else if (saveType.equals(RANDOM_SAVE)) {
			setTitle(R.string.random_save_title);
		}
	}

	/** 查询系统时间返回 */
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (!StringUtil.isNullOrEmpty(DeptDataCenter.getInstance().getCurNotifyDetail())) {// 通知存款支取日期
			drawDate = (String) DeptDataCenter.getInstance().getCurNotifyDetail().get(Dept.DRAW_DATE);
		} else {
			Map<String, Object> content = DeptDataCenter.getInstance().getCurDetailContent();
			drawDate = (String) content.get(Dept.INTEREST_ENDDATE);
		}

		oneDateLater = QueryDateUtils.getOneDayLater(dateTime);
		sevenDateLater = QueryDateUtils.getSevenDayLater(dateTime);
		if (executeWayFlag == PRE_DATE_EXCUTE) {
			showPredateLayout();
		} else if (executeWayFlag == PRE_WEEK_EXCUTE) {
			showPreweekLayout();
		} else { // 立即执行
			showOneDateLayout();
		}
	}

	/** 展示预约日期执行布局 */
	private void showPredateLayout() {
		initTitle();
		RelativeLayout stepLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.dept_pre_date, null);
		chooseDateTv = (TextView) stepLayout.findViewById(R.id.dept_choose_date_tv);
		startYear = Integer.parseInt(oneDateLater.substring(0, 4));
		startMonth = Integer.parseInt(oneDateLater.substring(5, 7));
		startDay = Integer.parseInt(oneDateLater.substring(8, 10));
		chooseDateTv.setText(oneDateLater);
		chooseDateTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 第二个参数为用户选择设置按钮后的响应事件
				// 最后的三个参数为缺省显示的年度，月份，及日期信息
				showDialog(CHOOSE_DATE_PICKER_ID);
			}
		});

		// btnLast.setOnClickListener(preDateLastBtnListener);
		ibBack.setOnClickListener(preDateLastBtnListener);
		btnConfirm.setText(this.getResources().getString(R.string.next));
		btnConfirm.setOnClickListener(preDateNextBtnListener);
		checkoutContentLayout.removeAllViews();
		checkoutContentLayout.addView(stepLayout);
		isPre = true;
	}

	/** 展示预约周期执行布局 */
	private void showPreweekLayout() {
		initTitle();
		RelativeLayout stepLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.dept_pre_week, null);

		chooseStartDateTv = (TextView) stepLayout.findViewById(R.id.dept_choose_start_date_tv);
		startYear = Integer.parseInt(oneDateLater.substring(0, 4));
		startMonth = Integer.parseInt(oneDateLater.substring(5, 7));
		startDay = Integer.parseInt(oneDateLater.substring(8, 10));
		chooseStartDateTv.setText(oneDateLater);
		chooseStartDateTv.setOnClickListener(chooseStartDateClicklistener);

		chooseEndDateTv = (TextView) stepLayout.findViewById(R.id.dept_choose_end_date_tv);
		endYear = Integer.parseInt(sevenDateLater.substring(0, 4));
		endMonth = Integer.parseInt(sevenDateLater.substring(5, 7));
		endDay = Integer.parseInt(sevenDateLater.substring(8, 10));
		chooseEndDateTv.setText(sevenDateLater);
		chooseEndDateTv.setOnClickListener(chooseEndDateClicklistener);
		// 如果是教育储蓄或者零存整存 周期值只显示月
		weekSpinner = (Spinner) stepLayout.findViewById(R.id.dept_week_spinner);
		if (saveType.equals(EDUCATION_SAVE1) || saveType.equals(EDUCATION_SAVE2) || saveType.equals(ZERO_SAVE1)
				|| saveType.equals(ZERO_SAVE2)) {
			String month = this.getResources().getString(R.string.month);
			ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.dept_spinner, new String[] { month });
			adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			weekSpinner.setAdapter(adapter1);
			cycleSelect = LocalData.FrequencyValue.get(month);
		} else {
			ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.dept_spinner, LocalData.weekListTrans);
			adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			weekSpinner.setAdapter(adapter1);
			weekSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					String week = LocalData.weekListTrans.get(position);
					cycleSelect = LocalData.FrequencyValue.get(week);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					String week = LocalData.weekListTrans.get(0);
					cycleSelect = LocalData.FrequencyValue.get(week);
				}
			});
		}

		ibBack.setOnClickListener(preDateLastBtnListener);
		btnConfirm.setText(this.getResources().getString(R.string.next));
		btnConfirm.setOnClickListener(preWeekNextBtnListener);
		checkoutContentLayout.removeAllViews();
		checkoutContentLayout.addView(stepLayout);
		isPre = true;
	}

	/** 展示确定信息布局 */
	private void showConfirmLayout() {
		initTitle();

		StepTitleUtils.getInstance().setTitleStep(2);
		RelativeLayout stepLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.dept_save_info_step2,
				null);

		initDisplayLayout(stepLayout);
		transationLayout.setVisibility(View.GONE);

		LinearLayout ll_checkout_way = (LinearLayout) stepLayout.findViewById(R.id.ll_checkout_way);
		TextView tv_checkout_way = (TextView) stepLayout.findViewById(R.id.tv_checkout_way);
		tv_checkout_way.setText(LocalData.drawMode.get(strDrawMode));

		fromAccountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
		strAccOutNumber = (String) accOutInfoMap.get(Comm.ACCOUNTNUMBER);
		accOutNoTv.setText(StringUtil.getForSixForString(strAccOutNumber));

		toAccountId = (String) accInInfoMap.get(Comm.ACCOUNT_ID);
		strAccInNumber = (String) accInInfoMap.get(Comm.ACCOUNTNUMBER);
		accInNoTv.setText(StringUtil.getForSixForString(strAccInNumber));

		currency = (String) curDetail.get(Comm.CURRENCYCODE);
		currencyTv.setText(LocalData.Currency.get(currency));

		cashRemit = (String) curDetail.get(Dept.CASHREMIT);
		cashRemitTv.setText(LocalData.CurrencyCashremit.get(cashRemit));

		if (currency.equals(ConstantGloble.PRMS_CODE_RMB)) {
			cashRemitLayout.setVisibility(View.INVISIBLE);
		}

		passBookNumber = (String) curDetail.get(Dept.VOLUME_NUMBER);
		bankBookTv.setText(passBookNumber);
		cdNumber = (String) curDetail.get(Dept.CD_NUMBER);
		bankSheetTv.setText(cdNumber);
		promiseWay = (String) curDetail.get(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE);
		tranmodeLayout = (LinearLayout) stepLayout.findViewById(R.id.dept_tranmode_layout);
		if (RANDOM_SAVE.equals(saveType)) // 定活两便 不显示转存方式
			tranmodeLayout.setVisibility(View.GONE);

		if (WHOE_SAVE.equals(saveType))
			ll_checkout_way.setVisibility(View.VISIBLE);

		if (saveType.equals(NOTIFY_SAVE)) {
			tranModeLabel.setText(this.getResources().getString(R.string.promise_way));
			tranModeTv.setText(LocalData.ConventionConvertType.get(promiseWay));
		} else {
			tranModeLabel.setText(this.getResources().getString(R.string.tran_mode));
			tranModeTv.setText(LocalData.ConvertType.get(promiseWay));
		}

		messageTv.setText(remark);
		if (remark.length() > 10) {// 如果附言大于10个字
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this, messageTv);
		}
		String strExecuteWay = transMode + "";
		executeWayTv.setText(LocalData.TransModeDisplay.get(strExecuteWay));
		executeTimesTv.setText(executeTimes + "");
		amount = StringUtil.parseStringCodePattern(currency, amount, 2);
		checkoutMoneyTv.setText(amount);
		promiseCheckOutDay = (LinearLayout) stepLayout.findViewById(R.id.dept_promise_checkout_day_layout);

		if (!isExpire) // 未到期支取不显示约定支取日期
			promiseCheckOutDay.setVisibility(View.GONE);

		if (saveType.equals(NOTIFY_SAVE)) {// 如果是一天或者七天通知存款 显示通知支取日期
			if (promiseWay.equals(ConstantGloble.CONVERTTYPE_N)) {// 非约定
				promiseCheckOutDay.setVisibility(View.VISIBLE);
			} else {
				promiseCheckOutDay.setVisibility(View.GONE);
			}
			promiseExeDay.setVisibility(View.GONE);
		} else if (saveType.equals(EDUCATION_SAVE1) || saveType.equals(EDUCATION_SAVE2) || saveType.equals(ZERO_SAVE1)
				|| saveType.equals(ZERO_SAVE2)) {
			String title = this.getResources().getString(R.string.continue_save_confirm);
			String continueSaveMoneyTx = this.getResources().getString(R.string.continue_save_money);
			titleTv.setText(title);
			label_moneyType.setText(this.getResources().getString(R.string.currency));
			saveVolumeLayout.setVisibility(View.GONE);
			banksheetLayout.setVisibility(View.GONE);
			tranmodeLayout.setVisibility(View.GONE);
			cashRemitLayout.setVisibility(View.GONE);
			moneyTv.setText(continueSaveMoneyTx);
			// 格式化币种
			continueSaveMoney = StringUtil.parseStringCodePattern(currency, continueSaveMoney, 2);
			checkoutMoneyTv.setText(continueSaveMoney);
			// continueSaveMoney.replaceAll(",", "");
			promiseExeDay.setVisibility(View.GONE);
		}
		if (executeWayFlag == PRE_DATE_EXCUTE) {// 预约日期执行
			execudeDayLayout.setVisibility(View.VISIBLE);
			executeDateTv.setText(chooseDateTv.getText().toString());
		} else if (executeWayFlag == PRE_WEEK_EXCUTE) {// 预约周期执行
			startDayLayout.setVisibility(View.VISIBLE);
			startDateTv.setText(chooseStartDateTv.getText().toString());
			endDayLayout.setVisibility(View.VISIBLE);
			endDateTv.setText(chooseEndDateTv.getText().toString());
			weekDayLayout.setVisibility(View.VISIBLE);
			weekDateTv.setText(LocalData.Frequency.get(cycleSelect));
			executeTimesLayout.setVisibility(View.VISIBLE);
		}

		// ////////////初始化 基准费用 和优惠后费用///////////////// TODO
		trialTrialCharge(isChargeFlag, needCommission, preCommission, remitSetMealFlag, isShowBenchmark);

		ibBack.setOnClickListener(onBackListener);
		btnConfirm.setOnClickListener(confirmConBtnListener);
		checkoutContentLayout.removeAllViews();
		checkoutContentLayout.addView(stepLayout);
	}

	/** 展示成功信息布局 */
	private void showSuccessLayout() {
		ibBack.setVisibility(View.INVISIBLE);
		StepTitleUtils.getInstance().setTitleStep(3);
		checkoutCallBackMap = DeptDataCenter.getInstance().getCheckoutCallBackMap();

		RelativeLayout stepLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.dept_save_info_step2,
				null);
		initDisplayLayout(stepLayout);

		LinearLayout ll_checkout_way = (LinearLayout) stepLayout.findViewById(R.id.ll_checkout_way);
		TextView tv_checkout_way = (TextView) stepLayout.findViewById(R.id.tv_checkout_way);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_checkout_way);
		tv_checkout_way.setText(LocalData.drawMode.get(strDrawMode));

		LinearLayout ll_dept_money = (LinearLayout) stepLayout.findViewById(R.id.ll_dept_money);

		// 交易序号
		Object objTradeNo = checkoutCallBackMap.get(Dept.TRANSACTION_ID);
		String strTradeNo = objTradeNo == null ? "" : (String) objTradeNo;
		// 转账批次号
		Object objBatSeq = checkoutCallBackMap.get(Dept.BATSEQ);
		String batseq = objBatSeq == null ? "" : (String) objBatSeq;

		// 交易序号 布局
		LinearLayout ll_trade_no = (LinearLayout) stepLayout.findViewById(R.id.dept_step_tradeno_layout);
		TextView transationIdTv = (TextView) stepLayout.findViewById(R.id.dept_step_tradeno_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, transationIdTv);
		transationIdTv.setText(strTradeNo);

		LinearLayout ll_tranmode = (LinearLayout) stepLayout.findViewById(R.id.dept_tranmode_layout);
		if (RANDOM_SAVE.equals(saveType))
			ll_tranmode.setVisibility(View.GONE);
		if (WHOE_SAVE.equals(saveType)) {// 整存整取不显示支取金额
			ll_checkout_way.setVisibility(View.VISIBLE);
			// ll_dept_money.setVisibility(View.GONE);
		}

		// 转账批次号 布局
		LinearLayout ll_pici = (LinearLayout) stepLayout.findViewById(R.id.dept_tans_pici_layout);
		TextView batSeqTv = (TextView) stepLayout.findViewById(R.id.dept_tans_pici_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, batSeqTv);
		batSeqTv.setText(batseq);

		fromAccountId = (String) checkoutCallBackMap.get(Dept.FROMACCOUNT_NUM);
		accOutNoTv.setText(StringUtil.getForSixForString(fromAccountId));
		toAccountId = (String) checkoutCallBackMap.get(Dept.TOACCOUNT_NUM);
		accInNoTv.setText(StringUtil.getForSixForString(toAccountId));
		currency = (String) checkoutCallBackMap.get(Dept.CURRENCY);
		currencyTv.setText(LocalData.Currency.get(currency));

		if (ConstantGloble.PRMS_CODE_RMB.equals(currency)) {
			cashRemitLayout.setVisibility(View.INVISIBLE);
		}

		String strAmount = (String) checkoutCallBackMap.get(Dept.AMOUNT);
		// 格式化小数点
		strAmount = StringUtil.parseStringCodePattern(currency, strAmount, 2);
		checkoutMoneyTv.setText(strAmount);
		cashRemit = (String) checkoutCallBackMap.get(Dept.CASHREMIT);
		cashRemitTv.setText(LocalData.CurrencyCashremit.get(cashRemit));
		passBookNumber = (String) curDetail.get(Dept.VOLUME_NUMBER);
		bankBookTv.setText(passBookNumber);
		cdNumber = (String) curDetail.get(Dept.CD_NUMBER);
		bankSheetTv.setText(cdNumber);
		promiseWay = (String) curDetail.get(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE);
		
		// add by luqp 2015年12月11日11:00:48  优惠后费用修改
		String youhui = (String) checkoutCallBackMap.get(Dept.FinalCommissionCharge);
		preferential  = DeptBaseActivity.parseStringCodePattern(currency, youhui, 2);
		// ////////////初始化 基准费用 和优惠后费用/////////////////
		// add by luqp 2015年12月15日10:44:47  预约日期 预约周期 优惠后费用 试算接口取值
		if(executeWayFlag == PRE_DATE_EXCUTE || executeWayFlag == PRE_WEEK_EXCUTE){
			trialTrialChargeNoPrompted(isChargeFlag, needCommission, preCommission, remitSetMealFlag, isShowBenchmark);
		} else { // 立即执行提交交易页面取值
			trialTrialChargeNoPrompted(isChargeFlag, needCommission, preferential, remitSetMealFlag, isShowBenchmark);
		}

		if (saveType.equals(NOTIFY_SAVE)) {
			tranModeLabel.setText(this.getResources().getString(R.string.promise_way));
			tranModeTv.setText(LocalData.ConventionConvertType.get(promiseWay));
		} else {
			tranModeLabel.setText(this.getResources().getString(R.string.tran_mode));
			tranModeTv.setText(LocalData.ConvertType.get(promiseWay));
		}

		messageTv.setText(remark);
		if (remark.length() > 10) {// 如果附言大于10个字
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this, messageTv);
		}
		String strExecuteWay = transMode + "";
		executeWayTv.setText(LocalData.TransModeDisplay.get(strExecuteWay));

		transationLayout.setVisibility(View.VISIBLE);

		promiseCheckOutDay = (LinearLayout) stepLayout.findViewById(R.id.dept_promise_checkout_day_layout);

		if (!isExpire)
			promiseCheckOutDay.setVisibility(View.GONE);

		if (saveType.equals(NOTIFY_SAVE)) {// 如果是一天或者七天通知存款 显示通知支取日期
			if (promiseWay.equals(ConstantGloble.CONVERTTYPE_N)) {// 非约定
				promiseCheckOutDay.setVisibility(View.VISIBLE);
			} else {
				promiseCheckOutDay.setVisibility(View.GONE);
			}
			titleTv.setText(this.getResources().getString(R.string.checkout_regular_business_success));
		} else if (saveType.equals(EDUCATION_SAVE1) || saveType.equals(EDUCATION_SAVE2) || saveType.equals(ZERO_SAVE1)
				|| saveType.equals(ZERO_SAVE2)) {
			titleTv.setText(this.getResources().getString(R.string.continue_save_confirm));
			saveVolumeLayout.setVisibility(View.GONE);
			banksheetLayout.setVisibility(View.GONE);
			tranmodeLayout.setVisibility(View.GONE);
			cashRemitLayout.setVisibility(View.GONE);
			// 续存金额
			label_moneyType.setText(this.getResources().getString(R.string.currency));
			moneyTv.setText(this.getResources().getString(R.string.continue_save_money));
			// 格式化小数点
			continueSaveMoney = StringUtil.parseStringCodePattern(currency, continueSaveMoney, 2);
			checkoutMoneyTv.setText(continueSaveMoney);
			promiseExeDay.setVisibility(View.GONE);
			titleTv.setText(this.getResources().getString(R.string.continue_regular_business_success));
		} else {
			titleTv.setText(this.getResources().getString(R.string.checkout_regular_business_success));
		}

		if (executeWayFlag == PRE_DATE_EXCUTE) {
			titleTv.setText(this.getResources().getString(R.string.appoint_trans_success_info));
			ll_trade_no.setVisibility(View.GONE);
			execudeDayLayout.setVisibility(View.VISIBLE);
			executeDateTv.setText(chooseDateTv.getText().toString());
			ll_pici.setVisibility(View.VISIBLE);
		} else if (executeWayFlag == PRE_WEEK_EXCUTE) {
			titleTv.setText(this.getResources().getString(R.string.appoint_trans_success_info));
			ll_pici.setVisibility(View.VISIBLE);
			ll_trade_no.setVisibility(View.GONE);
			startDayLayout.setVisibility(View.VISIBLE);
			startDateTv.setText(chooseStartDateTv.getText().toString());
			endDayLayout.setVisibility(View.VISIBLE);
			endDateTv.setText(chooseEndDateTv.getText().toString());
			weekDayLayout.setVisibility(View.VISIBLE);
			weekDateTv.setText(LocalData.Frequency.get(cycleSelect));
		} else {

		}

		// btnLast.setVisibility(View.GONE);
		btnConfirm.setText(this.getResources().getString(R.string.finish));
		btnConfirm.setOnClickListener(successConBtnListener);
		checkoutContentLayout.removeAllViews();
		checkoutContentLayout.addView(stepLayout);
		isFinish = true;
	}

	/**
	 * 初始化展示控件
	 * 
	 * @param view
	 */
	private void initDisplayLayout(View view) {
		// TextView 标题title
		titleTv = (TextView) view.findViewById(R.id.dept_step_title_tv);
		// Layout 交易序列号
		transationLayout = (LinearLayout) view.findViewById(R.id.dept_step_tradeno_layout);
		// Layout 存折册号
		saveVolumeLayout = (LinearLayout) view.findViewById(R.id.dept_save_volume_layout);
		// Layout 存单号
		banksheetLayout = (LinearLayout) view.findViewById(R.id.dept_banksheet_layout);
		// Layout 转存方式
		tranmodeLayout = (LinearLayout) view.findViewById(R.id.dept_tranmode_layout);
		// Layout 钞汇标志
		cashRemitLayout = (LinearLayout) view.findViewById(R.id.dept_cashremit_layout);
		// Layout 预约支取日期
		promiseExeDay = (LinearLayout) view.findViewById(R.id.dept_promise_checkout_day_layout);
		// Layout 支取日期
		execudeDayLayout = (LinearLayout) view.findViewById(R.id.dept_step_tradedate_layout);
		// Layout 开始日期
		startDayLayout = (LinearLayout) view.findViewById(R.id.dept_step_startdate_layout);
		// Layout 结束日期
		endDayLayout = (LinearLayout) view.findViewById(R.id.dept_step_enddate_layout);
		// Layout 周期
		weekDayLayout = (LinearLayout) view.findViewById(R.id.dept_step_week_layout);
		// layout 执行次数
		executeTimesLayout = (LinearLayout) view.findViewById(R.id.dept_step_exetimes_layout);
		// 如果是一天或者七天通知支取 显示约定支取日期
		promiseCheckOutDay = (LinearLayout) view.findViewById(R.id.dept_promise_checkout_day_layout);

		accOutNoTv = (TextView) view.findViewById(R.id.dept_accout_no_tv);

		bankBookTv = (TextView) view.findViewById(R.id.dept_bankbook_tv);

		bankSheetTv = (TextView) view.findViewById(R.id.dept_banksheet_tv);

		accInNoTv = (TextView) view.findViewById(R.id.dept_accin_no_tv);

		tranModeLabel = (TextView) view.findViewById(R.id.label_tranmode);
		tranModeTv = (TextView) view.findViewById(R.id.dept_tranmode_tv);

		label_moneyType = (TextView) view.findViewById(R.id.label_money_type);
		currencyTv = (TextView) view.findViewById(R.id.dept_currency_tv);
		cashRemitTv = (TextView) view.findViewById(R.id.dept_cashremit_tv);
		moneyTv = (TextView) view.findViewById(R.id.dept_money_tv);

		checkoutMoneyTv = (TextView) view.findViewById(R.id.dept_checkout_money_tv);

		messageTv = (TextView) view.findViewById(R.id.dept_attach_message_tv);

		executeWayTv = (TextView) view.findViewById(R.id.dept_excute_waty_tv);

		executeDateTv = (TextView) view.findViewById(R.id.dept_step_tradedate_tv);

		startDateTv = (TextView) view.findViewById(R.id.dept_step_startdate_tv);

		endDateTv = (TextView) view.findViewById(R.id.dept_step_enddate_tv);

		weekDateTv = (TextView) view.findViewById(R.id.dept_step_week_tv);

		executeTimesTv = (TextView) view.findViewById(R.id.dept_step_exetimes_tv);

		// ///////////初始化 基准费用 和优惠后费用/////////////////
		ll_reference_cost = (LinearLayout) view.findViewById(R.id.ll_reference_cost);
		reference_cost_tv = (TextView) view.findViewById(R.id.reference_cost_tv);
		ll_discount_cost = (LinearLayout) view.findViewById(R.id.ll_discount_cost);
		discount_cost_tv = (TextView) view.findViewById(R.id.discount_cost_tv);
		ll_discount_cost_prompted = (LinearLayout) view.findViewById(R.id.ll_discount_cost_prompted);
		tv_discount_cost_prompted = (TextView) view.findViewById(R.id.tv_discount_cost_prompted);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_discount_cost_prompted);
		ll_remit_flag = (LinearLayout) view.findViewById(R.id.ll_remit_flag);
		remit_flag = (TextView) view.findViewById(R.id.remit_flag);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, remit_flag);
		
		
		// //预约周期基准费用显示(单笔基准费用)优惠后费用显示(单笔优惠后费用)////
		// add by luqp 2015年12月11日15:02:50 TODO
		single_reference_cost = (LinearLayout) view.findViewById(R.id.ll_single_reference_cost);
		single_reference = (TextView) view.findViewById(R.id.tv_single_reference);
		reference_cost = (TextView) view.findViewById(R.id.tv_reference_cost);
		single_discount_cost = (LinearLayout) view.findViewById(R.id.ll_single_discount_cost);
		single_discount = (TextView) view.findViewById(R.id.tv_single_discount);
		discount_cost = (TextView) view.findViewById(R.id.tv_discount_cost);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, single_reference);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, single_discount);
	}

	/** 一天或者七天通知存款 确认信息内容 */
	private void showOneDateLayout() {
		StepTitleUtils.getInstance().setTitleStep(2);

		RelativeLayout stepLayout = (RelativeLayout) LayoutInflater.from(context).inflate(
				R.layout.dept_save_info_notice_step, null);
		initDisplayLayout(stepLayout);
		// 通知编号
		LinearLayout noticeLayout = (LinearLayout) stepLayout.findViewById(R.id.dept_notice_no_layout);
		TextView noticeNoTv = (TextView) stepLayout.findViewById(R.id.dept_notice_no_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, noticeNoTv);
		if (!StringUtil.isNullOrEmpty(noticeNo)) {
			scheduled = ConstantGloble.SCHEDULED_Y;// 有通知编号
			if (noticeNo.equals(this.getResources().getString(R.string.checkout_default))) {
				noticeLayout.setVisibility(View.GONE);
			} else {
				noticeNoTv.setText(noticeNo);
			}
		} else {
			scheduled = ConstantGloble.SCHEDULED_N;
			noticeLayout.setVisibility(View.GONE);
		}
		// 支取方式 如果是约定转存 就是全额支取 如果是非预定转存 就是部分支取
		TextView checkoutWayTv = (TextView) stepLayout.findViewById(R.id.dept_checkout_way_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, checkoutWayTv);
		checkoutWayTv.setText(LocalData.drawMode.get(strDrawMode));

		// String strCheckoutPart =
		// this.getResources().getString(R.string.checkout_part);
		promiseWay = (String) curDetail.get(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE);
		promiseCheckOutDay = (LinearLayout) stepLayout.findViewById(R.id.dept_promise_checkout_day_layout);

		// modify luqp 如果是非约定转存 不显示"约定支取日期"字段
		if (promiseWay.equals(ConstantGloble.CONVERTTYPE_R)) {// 非约定
			promiseCheckOutDay.setVisibility(View.GONE);
		} else {
			if (!isExpire) {
				promiseCheckOutDay.setVisibility(View.VISIBLE);
			} else {
				promiseCheckOutDay.setVisibility(View.GONE);
			}
		}
		// //////////////////////////////////////////////////////
		String strAmount = "";
		// String strAmount = StringUtil.parseStringPattern(amount, 2);
		// 业务品种 如果cdTerm是1 一天通知存款 如果cdTerm是7 7天通知存款
		TextView businessTypeTv = (TextView) stepLayout.findViewById(R.id.dept_business_type_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, businessTypeTv);
		String date = null;
		if (cdTerm.equals(BUSINESSTYPE_ONEDAY)) {
			businessTypeTv.setText(oneDayTitle);
			setTitle(R.string.one_day_title);
			// date = QueryDateUtils.getOneDayLater(dateTime);
		} else if (cdTerm.equals(BUSINESSTYPE_SEVENDAY)) {
			businessTypeTv.setText(sevenDayTitle);
			setTitle(R.string.seven_day_title);
			// date = QueryDateUtils.getSevenDayLater(dateTime);
		}
		TextView preDateTv = (TextView) stepLayout.findViewById(R.id.dept_step_tradedate_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, preDateTv);
		preDateTv.setText(preDrawDate);

		// 是否自动转存
		// String strConvertType = (String)
		// curDetail.get(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE);
		fromAccountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
		strAccOutNumber = (String) accOutInfoMap.get(Comm.ACCOUNTNUMBER);
		accOutNoTv.setText(StringUtil.getForSixForString(strAccOutNumber));

		toAccountId = (String) accInInfoMap.get(Comm.ACCOUNT_ID);
		strAccInNumber = (String) accInInfoMap.get(Comm.ACCOUNTNUMBER);
		accInNoTv.setText(StringUtil.getForSixForString(strAccInNumber));

		passBookNumber = (String) curDetail.get(Dept.VOLUME_NUMBER);
		bankBookTv.setText(passBookNumber);
		cdNumber = (String) curDetail.get(Dept.CD_NUMBER);
		bankSheetTv.setText(cdNumber);
		promiseWay = (String) curDetail.get(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE);
		tranModeTv.setText(LocalData.ConventionConvertType.get(promiseWay));
		currency = (String) curDetail.get(Comm.CURRENCYCODE);
		currencyTv.setText(LocalData.Currency.get(currency));

		if (currency.equals(ConstantGloble.PRMS_CODE_RMB)) {
			cashRemitLayout.setVisibility(View.INVISIBLE);
		}

		// 格式化小数点 如果是韩元或日元没有小数点位
		strAmount = StringUtil.parseStringCodePattern(currency, amount, 2);
		checkoutMoneyTv.setText(strAmount);

		cashRemit = (String) curDetail.get(Dept.CASHREMIT);
		cashRemitTv.setText(LocalData.CurrencyCashremit.get(cashRemit));
		// 支取通知存款 只能立即执行
		transMode = ConstantGloble.NOWEXE;

		messageTv.setText(remark);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, messageTv);
		executeWayTv.setText(LocalData.TransModeDisplay.get(ConstantGloble.NOWEXE));

		// //////////////////////////////////////////////
		// add by luqp 2015年12月3日15:18:28 格式化试算金额
		String need = StringUtil.splitStringwithCode(currency, needCommission, 2);
		String pre = StringUtil.splitStringwithCode(currency, preCommission, 2);

		// ////////////初始化 基准费用 和优惠后费用/////////////////
		trialTrialCharge(isChargeFlag, need, pre, remitSetMealFlag, isShowBenchmark);

		// btnLast.setOnClickListener(confirmLastBtnListener);
		btnConfirm.setOnClickListener(confirmConBtnListener);
		checkoutContentLayout.removeAllViews();
		checkoutContentLayout.addView(stepLayout);
	}

	/** 一天或者七天通知存款 交易成功页面 */
	private void showOneDateSuccessLayout() {
		ibBack.setVisibility(View.INVISIBLE);
		StepTitleUtils.getInstance().setTitleStep(3);

		checkoutCallBackMap = DeptDataCenter.getInstance().getCheckoutCallBackMap();

		RelativeLayout stepLayout = (RelativeLayout) LayoutInflater.from(context).inflate(
				R.layout.dept_save_info_notice_step, null);
		LinearLayout noticeLayout = (LinearLayout) stepLayout.findViewById(R.id.dept_notice_no_layout);
		if (StringUtil.isNullOrEmpty(noticeNo)) {
			noticeLayout.setVisibility(View.GONE);
		}

		String date = null;
		if (cdTerm.equals(BUSINESSTYPE_ONEDAY)) {
			setTitle(R.string.one_day_title);
			// date = QueryDateUtils.getOneDayLater(dateTime);
		} else if (cdTerm.equals(BUSINESSTYPE_SEVENDAY)) {
			setTitle(R.string.seven_day_title);
			// date = QueryDateUtils.getSevenDayLater(dateTime);
		}

		// LogGloble.d(TAG, "预约支取 日期 : " + date + " | Date Time : " + dateTime);
		initDisplayLayout(stepLayout);
		transationLayout.setVisibility(View.VISIBLE);

		TextView tradeTitleTv = (TextView) stepLayout.findViewById(R.id.dept_step_title_tv);
		// PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
		// tradeTitleTv);
		tradeTitleTv.setText(this.getResources().getString(R.string.checkout_regular_business_success));

		LinearLayout tradeNoLayout = (LinearLayout) stepLayout.findViewById(R.id.dept_step_tradeno_layout);
		tradeNoLayout.setVisibility(View.VISIBLE);
		TextView tradeNoTv = (TextView) tradeNoLayout.findViewById(R.id.dept_step_tradeno_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tradeNoTv);

		// 预约执行日期
		tradeNoTv.setText((String) checkoutCallBackMap.get(Dept.TRANSACTION_ID));
		// 转出账户
		strAccOutNumber = (String) checkoutCallBackMap.get(Dept.NEW_NOTIFY_SAVE_PAYERACCOUNT_NUMBER);
		accOutNoTv.setText(StringUtil.getForSixForString(strAccOutNumber));
		// 转入账户
		strAccInNumber = (String) checkoutCallBackMap.get(Dept.NEW_NOTIFY_SAVE_PAYEEACCOUNT_NUMBER);
		accInNoTv.setText(StringUtil.getForSixForString(strAccInNumber));
		// 存折号
		passBookNumber = (String) checkoutCallBackMap.get(Dept.PASSBOOK_NUMBER);
		bankBookTv.setText(passBookNumber);
		// 存单号
		cdNumber = (String) checkoutCallBackMap.get(Dept.CD_NUMBER);
		bankSheetTv.setText(cdNumber);
		// 业务品种
		TextView businessTypeTv = (TextView) stepLayout.findViewById(R.id.dept_business_type_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, businessTypeTv);
		String strBusiTy = (String) checkoutCallBackMap.get(Dept.NEW_NOTIFY_SAVE_BUSINESS_TYPE);
		businessTypeTv.setText(LocalData.CallCDTerm.get(strBusiTy));

		tranModeTv.setText(LocalData.ConventionConvertType.get(promiseWay));
		// 币种
		currency = (String) checkoutCallBackMap.get(Dept.CURRENCY);
		currencyTv.setText(LocalData.Currency.get(currency));

		if (currency.equals(ConstantGloble.PRMS_CODE_RMB)) {
			cashRemitLayout.setVisibility(View.INVISIBLE);
		}
		// 钞汇标志
		cashRemit = (String) checkoutCallBackMap.get(Dept.CASHREMIT);
		cashRemitTv.setText(LocalData.CurrencyCashremit.get(cashRemit));

		if (!isExpire) {
			noticeLayout.setVisibility(View.GONE);
		}
		// 通知编号
		noticeNo = (String) checkoutCallBackMap.get(Dept.SCHEDULE_NUMBER);

		// LinearLayout ll_notice_no =
		// (LinearLayout)stepLayout.findViewById(R.id.dept_notice_no_layout);
		TextView noticeNoTv = (TextView) stepLayout.findViewById(R.id.dept_notice_no_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, noticeNoTv);
		noticeNoTv.setText(noticeNo);
		// 支取方式
		TextView checkoutWayTv = (TextView) stepLayout.findViewById(R.id.dept_checkout_way_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, checkoutWayTv);
		checkoutWayTv.setText(LocalData.drawMode.get(strDrawMode));
		// String strCheckoutPart =
		// this.getResources().getString(R.string.checkout_part);
		promiseCheckOutDay = (LinearLayout) stepLayout.findViewById(R.id.dept_promise_checkout_day_layout);

		// modify luqp 如果是非约定转存 不显示"约定支取日期"字段
		if (promiseWay.equals(ConstantGloble.CONVERTTYPE_R)) {// 非约定
			promiseCheckOutDay.setVisibility(View.GONE);
		} else {
			if (!isExpire) {
				promiseCheckOutDay.setVisibility(View.VISIBLE);
			} else {
				promiseCheckOutDay.setVisibility(View.GONE);
			}
		}
		// //////////////////////////////////////////////////////

		// 格式化小数点
		amount = StringUtil.parseStringCodePattern(currency, amount, 2);
		checkoutMoneyTv.setText(amount);
		// 预约执行日期
		// String strAssignedDate = (String)
		// checkoutCallBackMap.get(Dept.NEW_NOTIFY_SAVE_ASSIGNEDDATE);
		//
		// LogGloble.d(TAG, "executeDateTv —— 预约执行日期 ：" + strAssignedDate);

		executeDateTv.setText(preDrawDate);
		// 附言
		String strMeno = (String) checkoutCallBackMap.get(Dept.NEW_NOTIFY_SAVE_FURINFO);
		messageTv.setText(strMeno);
		if (strMeno.length() > 10) {// 如果附言大于10个字
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this, messageTv);
		}
		// 执行方式
		promiseWay = (String) checkoutCallBackMap.get(Dept.NEW_NOTIFY_SAVE_TRANSMODE);
		executeWayTv.setText(LocalData.TransModeDisplay.get(promiseWay));

		// //////////////////////////////////////////////
		// add by luqp 2015年12月3日15:18:28 格式化试算金额
		String need = StringUtil.splitStringwithCode(currency, needCommission, 2);
		String pre = StringUtil.splitStringwithCode(currency, preCommission, 2);
		
		// add by luqp 2015年12月11日11:00:48  优惠后费用修改
		String youhui = (String) checkoutCallBackMap.get(Dept.FinalCommissionCharge);
		String youhuifeiyong  = DeptBaseActivity.parseStringCodePattern(currency, youhui, 2);
		preferential = StringUtil.splitStringwithCode(currency, youhuifeiyong, 2);
		// ////////////初始化 基准费用 和优惠后费用/////////////////
//		trialTrialChargeNoPrompted(isChargeFlag, need, pre, remitSetMealFlag, isShowBenchmark);
		// add by luqp 2015年12月15日10:44:47  预约日期 预约周期 优惠后费用 算接口取值
		if(executeWayFlag == PRE_DATE_EXCUTE || executeWayFlag == PRE_WEEK_EXCUTE){
			trialTrialChargeNoPrompted(isChargeFlag, need, pre, remitSetMealFlag, isShowBenchmark);
		} else { // 立即执行试提交交易页面取值
			trialTrialChargeNoPrompted(isChargeFlag, need, preferential, remitSetMealFlag, isShowBenchmark);
		}

		// btnLast.setVisibility(View.GONE);
		btnConfirm.setOnClickListener(successConBtnListener);
		btnConfirm.setText(this.getResources().getString(R.string.finish));
		checkoutContentLayout.removeAllViews();
		checkoutContentLayout.addView(stepLayout);
		isFinish = true;
	}

	/** 展示教育储蓄 续存成功信息布局 */
	private void showExtendEducationSuccessLayout() {
		ibBack.setVisibility(View.INVISIBLE);
		StepTitleUtils.getInstance().setTitleStep(3);

		checkoutCallBackMap = DeptDataCenter.getInstance().getCheckoutCallBackMap();

		RelativeLayout stepLayout = (RelativeLayout) LayoutInflater.from(context).inflate(
				R.layout.dept_extend_education_layout, null);
		// 标题title
		((TextView) stepLayout.findViewById(R.id.dept_step_title_tv)).setText(R.string.business_success_tip);
		((TextView) stepLayout.findViewById(R.id.label_money_type)).setText(R.string.currency);
		// 交易序列号
		LinearLayout tradeNoLayout = (LinearLayout) stepLayout.findViewById(R.id.dept_step_tradeno_layout);

		// Layout 支取日期
		LinearLayout execudeDayLayout = (LinearLayout) stepLayout.findViewById(R.id.dept_step_tradedate_layout);
		// Layout 开始日期
		LinearLayout startDayLayout = (LinearLayout) stepLayout.findViewById(R.id.dept_step_startdate_layout);
		// Layout 结束日期
		LinearLayout endDayLayout = (LinearLayout) stepLayout.findViewById(R.id.dept_step_enddate_layout);
		// Layout 周期
		LinearLayout weekDayLayout = (LinearLayout) stepLayout.findViewById(R.id.dept_step_week_layout);
		// Layout 执行次数
		LinearLayout executeTimesLayout = (LinearLayout) stepLayout.findViewById(R.id.dept_execute_times_layout);

		// tradeNoLayout.setVisibility(View.VISIBLE);
		TextView tradeNoTv = (TextView) tradeNoLayout.findViewById(R.id.dept_step_tradeno_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tradeNoTv);

		TextView accOutNoTv = (TextView) stepLayout.findViewById(R.id.dept_accout_no_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accOutNoTv);

		TextView accInNoTv = (TextView) stepLayout.findViewById(R.id.dept_accin_no_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accInNoTv);

		TextView currencyTv = (TextView) stepLayout.findViewById(R.id.dept_currency_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, currencyTv);

		TextView continueSaveMoneyTv = (TextView) stepLayout.findViewById(R.id.dept_continue_save_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, continueSaveMoneyTv);

		TextView messageTv = (TextView) stepLayout.findViewById(R.id.dept_attach_message_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, messageTv);

		TextView executeWayTv = (TextView) stepLayout.findViewById(R.id.dept_excute_way_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, executeWayTv);

		TextView executeDateTv = (TextView) stepLayout.findViewById(R.id.dept_step_tradedate_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, executeDateTv);

		TextView startDateTv = (TextView) stepLayout.findViewById(R.id.dept_step_startdate_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, startDateTv);

		TextView endDateTv = (TextView) stepLayout.findViewById(R.id.dept_step_enddate_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, endDateTv);

		TextView cycleDateTv = (TextView) stepLayout.findViewById(R.id.dept_step_week_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, cycleDateTv);

		TextView executeTimesTv = (TextView) stepLayout.findViewById(R.id.dept_execute_times_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, executeTimesTv);

		// 转账批次号 布局
		LinearLayout ll_pici = (LinearLayout) stepLayout.findViewById(R.id.dept_tans_pici_layout);
		// 交易序号 布局
		LinearLayout ll_trade_no = (LinearLayout) stepLayout.findViewById(R.id.dept_step_tradeno_layout);
		TextView batSeqTv = (TextView) stepLayout.findViewById(R.id.dept_tans_pici_tv);
		Object objBatSeq = checkoutCallBackMap.get(Dept.BATSEQ);
		String batseq = objBatSeq == null ? "" : (String) objBatSeq;
		batSeqTv.setText(batseq);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, batSeqTv);

		String transationId = (String) checkoutCallBackMap.get(Dept.TRANSACTION_ID);
		tradeNoTv.setText(transationId);

		accOutNoTv.setText(StringUtil.getForSixForString(strAccOutNumber));

		strAccInNumber = (String) checkoutCallBackMap.get(Dept.NEW_NOTIFY_SAVE_PAYEEACCOUNT_NUMBER);
		accInNoTv.setText(StringUtil.getForSixForString(strAccInNumber));

		currency = (String) checkoutCallBackMap.get(Dept.IN_CURRENCYCODE);
		currencyTv.setText(LocalData.Currency.get(currency));
		String strAmount = (String) checkoutCallBackMap.get(Dept.AMOUNT);
		if (!StringUtil.isNullOrEmpty(strAmount)) {
			amount = StringUtil.parseStringPattern(strAmount, 2).replaceAll(",", "");
		}
		continueSaveMoneyTv.setText(StringUtil.parseStringPattern(amount, 2));
		// remark = (String)
		// checkoutCallBackMap.get(Dept.NEW_NOTIFY_SAVE_FURINFO);
		messageTv.setText(remark);
		if (remark.length() > 10) {// 如果附言大于10个字
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this, messageTv);
		}

		executeDateTv.setText(executeDate);

		String strExecuteWay = transMode + "";
		executeWayTv.setText(LocalData.TransModeDisplay.get(strExecuteWay));

		startDate = (String) checkoutCallBackMap.get(Dept.START_DATE);
		startDateTv.setText(startDate);
		endDate = (String) checkoutCallBackMap.get(Dept.END_DATE);
		endDateTv.setText(endDate);
		cycleSelect = (String) checkoutCallBackMap.get(Dept.CYCLE_SELECT);
		cycleDateTv.setText(LocalData.Frequency.get(cycleSelect));
		// 执行次数
		executeTimesTv.setText((String) checkoutCallBackMap.get(Dept.NEW_NOTIFY_SAVE_EXECUTETIMES));
		
		// ////////////初始化 基准费用 和优惠后费用/////////////////
		ll_reference_cost = (LinearLayout) stepLayout.findViewById(R.id.ll_reference_cost);
		reference_cost_tv = (TextView) stepLayout.findViewById(R.id.reference_cost_tv);
		ll_discount_cost = (LinearLayout) stepLayout.findViewById(R.id.ll_discount_cost);
		discount_cost_tv = (TextView) stepLayout.findViewById(R.id.discount_cost_tv);
		ll_discount_cost_prompted = (LinearLayout) stepLayout.findViewById(R.id.ll_discount_cost_prompted);
		tv_discount_cost_prompted = (TextView) stepLayout.findViewById(R.id.tv_discount_cost_prompted);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_discount_cost_prompted);
		ll_remit_flag = (LinearLayout) stepLayout.findViewById(R.id.ll_remit_flag);
		remit_flag = (TextView) stepLayout.findViewById(R.id.remit_flag);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, remit_flag);
		
		// //预约周期基准费用显示(单笔基准费用)优惠后费用显示(单笔优惠后费用)////
		// add by luqp 2015年12月11日15:02:50 TODO
		single_reference_cost = (LinearLayout) stepLayout.findViewById(R.id.ll_single_reference_cost);
		single_reference = (TextView) stepLayout.findViewById(R.id.tv_single_reference);
		reference_cost = (TextView) stepLayout.findViewById(R.id.tv_reference_cost);
		single_discount_cost = (LinearLayout) stepLayout.findViewById(R.id.ll_single_discount_cost);
		single_discount = (TextView) stepLayout.findViewById(R.id.tv_single_discount);
		discount_cost = (TextView) stepLayout.findViewById(R.id.tv_discount_cost);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, single_reference);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, single_discount);
		
		// add by luqp 2015年12月11日11:00:48  优惠后费用修改
		String youhui = (String) checkoutCallBackMap.get(Dept.FinalCommissionCharge);
		preferential  = DeptBaseActivity.parseStringCodePattern(currency, youhui, 2);
		// 汇款套餐成功页面无汇款套餐提示信息
		// add by luqp 2015年12月15日10:44:47  预约日期 预约周期 优惠后费用 试算接口取值
		if(executeWayFlag == PRE_DATE_EXCUTE || executeWayFlag == PRE_WEEK_EXCUTE){
			trialTrialChargeNoPrompted(isChargeFlag, needCommission, preCommission, remitSetMealFlag, isShowBenchmark);
		} else { // 立即执行 提交交易页面取值
			trialTrialChargeNoPrompted(isChargeFlag, needCommission, preferential, remitSetMealFlag, isShowBenchmark);
		}

		if (Integer.parseInt(transMode) == PRE_DATE_EXCUTE) {
			((TextView) stepLayout.findViewById(R.id.dept_step_title_tv)).setText(R.string.appoint_trans_success_info);
			execudeDayLayout.setVisibility(View.VISIBLE);
			executeDateTv.setText(chooseDateTv.getText().toString());
			ll_pici.setVisibility(View.VISIBLE);
			ll_trade_no.setVisibility(View.GONE);
		} else if (Integer.parseInt(transMode) == PRE_WEEK_EXCUTE) {
			((TextView) stepLayout.findViewById(R.id.dept_step_title_tv)).setText(R.string.appoint_trans_success_info);
			startDayLayout.setVisibility(View.VISIBLE);
			startDateTv.setText(chooseStartDateTv.getText().toString());
			endDayLayout.setVisibility(View.VISIBLE);
			endDateTv.setText(chooseEndDateTv.getText().toString());
			weekDayLayout.setVisibility(View.VISIBLE);
			cycleDateTv.setText(LocalData.Frequency.get(cycleSelect));
			executeTimesLayout.setVisibility(View.VISIBLE);
			ll_pici.setVisibility(View.VISIBLE);
			ll_trade_no.setVisibility(View.GONE);
		}

		// btnLast.setVisibility(View.GONE);
		btnConfirm.setText(this.getResources().getString(R.string.finish));
		btnConfirm.setOnClickListener(successConBtnListener);
		checkoutContentLayout.removeAllViews();
		checkoutContentLayout.addView(stepLayout);
		isFinish = true;
	}

	/** 预约日期执行返回按钮 监听 */
	private OnClickListener preDateLastBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			isPre = false;
			finish();
		}
	};

	/** 预约日期执行下一步按钮 监听 */
	private OnClickListener preDateNextBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			executeDate = chooseDateTv.getText().toString().trim();
			boolean flag = QueryDateUtils.compareStartThreeMonthDate(executeDate, dateTime);
			if (!flag) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						MyPositDrawMoneConfirmActivity.this.getResources().getString(R.string.execute_day));
				return;
			}
			btnConfirm.setText(strConfirm);
			showConfirmLayout();
			isPre = false;
		}
	};

	/** 预约周期执行下一步按钮 监听 */
	private OnClickListener preWeekNextBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			startDate = chooseStartDateTv.getText().toString().trim();
			endDate = chooseEndDateTv.getText().toString().trim();
			if (!QueryDateUtils.compareStartDate(startDate, dateTime)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						MyPositDrawMoneConfirmActivity.this.getResources().getString(R.string.choose_start_day_info));
				return;
			}

			if (!QueryDateUtils.compareEndDate(endDate, dateTime)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						MyPositDrawMoneConfirmActivity.this.getResources().getString(R.string.choose_end_day_info));
				return;
			}
			boolean flag = QueryDateUtils.compareDate(startDate, endDate);
			if (!flag) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						MyPositDrawMoneConfirmActivity.this.getResources().getString(R.string.acc_query_errordate));
				return;
			}
			btnConfirm.setText(strConfirm);
			// 执行次数
			executeTimes = QueryDateUtils.initExecuteTimes(startDate, endDate, cycleSelect);
			showConfirmLayout();
			isPre = false;
		}
	};

	/** 预约确定信息上一步按钮 监听 */
	private OnClickListener onBackListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (Integer.parseInt(transMode) == PRE_DATE_EXCUTE) {
				showPredateLayout();
			} else if (Integer.parseInt(transMode) == PRE_WEEK_EXCUTE) {
				showPreweekLayout();
			} else {
				finish();
			}
		}
	};

	/** 预约确定信息确定按钮 监听 */
	private OnClickListener confirmConBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			checkout();

		}
	};

	/** 预约成功页面 确定按钮 监听 */
	private OnClickListener successConBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ActivityTaskManager.getInstance().removeAllActivity();
			// 从通知管理那边跳过来的 回通知存款首页
			Intent intent = new Intent();
			if (isSaveRegEdu) {
				intent.setClass(MyPositDrawMoneConfirmActivity.this, SaveRegularActivity.class);
			} else {
				if (notifyFlag == 1) {
					intent.setClass(MyPositDrawMoneConfirmActivity.this, NotifyManageActivity.class);
				} else {
					intent.setClass(MyPositDrawMoneConfirmActivity.this, MyRegSaveActivity.class);
				}
			}
			startActivity(intent);
			finish();
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isFinish) {
				isFinish = false;
				ActivityTaskManager.getInstance().removeAllActivity();
				// 从通知管理那边跳过来的 回通知存款首页
				Intent intent = new Intent();
				if (isSaveRegEdu) {
					intent.setClass(MyPositDrawMoneConfirmActivity.this, SaveRegularActivity.class);
				} else {
					if (notifyFlag == 1) {
						intent.setClass(MyPositDrawMoneConfirmActivity.this, NotifyManageActivity.class);
					} else {
						intent.setClass(MyPositDrawMoneConfirmActivity.this, MyRegSaveActivity.class);
					}
				}
				startActivity(intent);
				finish();
			} else {
				if (isPre) {
					isPre = false;
					finish();
				} else {
					if (Integer.parseInt(transMode) == PRE_DATE_EXCUTE) {
						showPredateLayout();
					} else if (Integer.parseInt(transMode) == PRE_WEEK_EXCUTE) {
						showPreweekLayout();
					} else {
						finish();
					}
				}
			}
		}
		return true;
	}

	/** 通讯返回 */
	@Override
	public void communicationCallBack(int flag) {
		switch (flag) {
		case SAVE_WHOLE_CHECKOU_CALLLBAK:// 支取整存整取返回
			showSuccessLayout();
			break;
		case EXTEND_EDUCATION_CALLLBAK: // 支取 教育储蓄续存 零存整取
			showExtendEducationSuccessLayout();
			break;
		case NOTIFY_SAVE_CHECKOUT_CALLBACK: // 查询通知存款详情
			showOneDateSuccessLayout();
			break;
		default:
			break;
		}
	}

	/** 未到期提醒框 按钮监听 */
	private OnClickListener onclicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch ((Integer) v.getTag()) {
			case CustomDialog.TAG_CANCLE:// 取消
				BaseDroidApp.getInstanse().dismissErrorDialog();
				break;
			case CustomDialog.TAG_SURE:// 确定
				BaseDroidApp.getInstanse().dismissErrorDialog();
				BaseDroidApp.getInstanse().dismissMessageDialog();
				checkout();
				break;
			default:
				break;
			}
		}
	};

	/** 支取 */
	private void checkout() {
		// 发送通讯
		// 根据不同存单类型不同 发送不同通讯
		// 发送通讯 请求token
		requestCommConversationId();
		BaseHttpEngine.showProgressDialog();
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

		if (saveType.equals(WHOE_SAVE)) {
			// 支取整存整取 //TODO 需要送上一个当前appointedAmount == bookBalance
			requestCheckoutWholeSave(fromAccountId, toAccountId, currency, amount, cashRemit, remark, passBookNumber,
					cdNumber, transMode, executeDate, token, appointedAmount);
		} else if (saveType.equals(RANDOM_SAVE)) {
			// 支取定活两便PsnTransTerminalOrCurrentDepositSubmit
			requestCheckoutRegAndRandom(fromAccountId, toAccountId, currency, amount, cashRemit, remark, passBookNumber,
					cdNumber, transMode, executeDate, token);
		} else if (saveType.equals(NOTIFY_SAVE)) {
			// 支取 通知存款 通知存款金额 存单金额-支取金额
			cdType = DeptBaseActivity.NOTIFY_SAVE;
			requestCheckoutNotifySave(fromAccountId, toAccountId, transMode, cdType, currency, amount, cashRemit, cdTerm,
					convertType, remark, passBookNumber, cdNumber, scheduled, noticeNo, callDepositBalance, token);
		} else if (saveType.equals(EDUCATION_SAVE1) || saveType.equals(EDUCATION_SAVE2)) {
			// 教育储蓄PsnTransExtendLingcunDeposite
			requestExtendEducation(fromAccountId, toAccountId, continueSaveMoney, remark, transMode, executeDate,
					cycleSelect, startDate, endDate, token);
		} else if (saveType.equals(ZERO_SAVE1) || saveType.equals(ZERO_SAVE2)) {
			// 零存整取 续存
			requestExtendZeroWhole(fromAccountId, toAccountId, continueSaveMoney, remark, transMode, executeDate,
					cycleSelect, startDate, endDate, token);
		}
	}

	/** 选择开始日期监听事件 */
	private OnClickListener chooseStartDateClicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			showDialog(START_DATE_PICKER_ID);
		}
	};

	/** 选择结束日期监听事件 */
	private OnClickListener chooseEndDateClicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			showDialog(END_DATE_PICKER_ID);
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case CHOOSE_DATE_PICKER_ID:
			DatePickerDialog chooseDateDialog = new DatePickerDialog(MyPositDrawMoneConfirmActivity.this,
					onChooseDateSetListener, startYear, startMonth - 1, startDay);
			return chooseDateDialog;
		case START_DATE_PICKER_ID:
			DatePickerDialog startDateDialog = new DatePickerDialog(MyPositDrawMoneConfirmActivity.this,
					onStartDateSetListener, startYear, startMonth - 1, startDay);
			return startDateDialog;
		case END_DATE_PICKER_ID:
			DatePickerDialog endDateDialog = new DatePickerDialog(MyPositDrawMoneConfirmActivity.this, onEndDateSetListener,
					endYear, endMonth - 1, endDay);
			return endDateDialog;
		default:
			break;
		}
		return null;
	}

	/** 开始日期控件监听 */
	DatePickerDialog.OnDateSetListener onChooseDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			String month = String.valueOf(monthOfYear + 1);
			String day = String.valueOf(dayOfMonth);
			if (monthOfYear + 1 < 10) {// 如果月份小于10月 前面加“0”
				month = "0" + String.valueOf(monthOfYear + 1);
			}
			if (dayOfMonth < 10) {
				day = "0" + String.valueOf(dayOfMonth);
			}
			/** 选择的截止日期 */
			String dateStr = (String.valueOf(year) + "/" + month + "/" + day);
			/** 为EditText赋值 */
			oneDateLater = dateStr;
			chooseDateTv.setText(dateStr);
		}
	};

	/** 开始日期控件监听 */
	DatePickerDialog.OnDateSetListener onStartDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			String month = String.valueOf(monthOfYear + 1);
			String day = String.valueOf(dayOfMonth);
			if (monthOfYear + 1 < 10) {// 如果月份小于10月 前面加“0”
				month = "0" + String.valueOf(monthOfYear + 1);
			}
			if (dayOfMonth < 10) {
				day = "0" + String.valueOf(dayOfMonth);
			}
			/** 选择的截止日期 */
			String dateStr = (String.valueOf(year) + "/" + month + "/" + day);
			/** 为EditText赋值 */
			oneDateLater = dateStr;
			chooseStartDateTv.setText(dateStr);
		}
	};

	/** 结束日期控件监听 */
	DatePickerDialog.OnDateSetListener onEndDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			String month = String.valueOf(monthOfYear + 1);
			String day = String.valueOf(dayOfMonth);
			if (monthOfYear + 1 < 10) {// 如果月份小于10月 前面加“0”
				month = "0" + String.valueOf(monthOfYear + 1);
			}
			if (dayOfMonth < 10) {
				day = "0" + String.valueOf(dayOfMonth);
			}
			/** 选择的截止日期 */
			String dateStr = (String.valueOf(year) + "/" + month + "/" + day);
			/** 为EditText赋值 */
			sevenDateLater = dateStr;
			chooseEndDateTv.setText(dateStr);
		}
	};

	// /////////////////////////试算手续费 start
	/**
	 * 试算手续费
	 * 
	 * @param isChargeFlag
	 *            试费查询是否成功标识位 1:查询成功 0:查询失败
	 * @param needCommission
	 *            应收费用
	 * @param preCommission
	 *            拟收费用
	 * @param remitSetMealFlag
	 *            是否签约汇款笔数套餐标识位 1:签约汇款笔数套餐 0:未签约汇款笔数套餐
	 * @param isShowBenchmark
	 *            是否显示基准费用 当基准费用字段返回是为true 没有返回此字段则为false
	 */
	private void trialTrialCharge(String isChargeFlag, String needCommission, String preCommission, String remitSetMealFlag,
			Boolean isShowBenchmark) {
		// 如果 试算成功 优惠费用不为空 显示基准费用 优惠费用为空 不显示基准费用
		if (DeptBaseActivity.ONE.equals(isChargeFlag)) { // 查询手续费试算成功
			if (isShowBenchmark = true && !StringUtil.isNull(needCommission)) { // 基准费用不为空
				ll_discount_cost_prompted.setVisibility(View.VISIBLE);
				// add by luqp 2015年12月14日10:55:16 预约周期修改为 单笔基准费用 单笔优惠后费用
				if (executeWayFlag == PRE_WEEK_EXCUTE) {
					single_reference_cost.setVisibility(View.VISIBLE);
					single_discount_cost.setVisibility(View.VISIBLE);
					reference_cost.setText(needCommission);
					discount_cost.setText(preCommission);
				} else {
					ll_reference_cost.setVisibility(View.VISIBLE);
					ll_discount_cost.setVisibility(View.VISIBLE);
					reference_cost_tv.setText(needCommission);
					discount_cost_tv.setText(preCommission);
				}
			} else { // 基准费用为空或0或0.00 只显示优惠后费用
				// add by luqp 2015年12月14日10:55:12预约周期修改为 单笔优惠后费用
				ll_discount_cost_prompted.setVisibility(View.VISIBLE);
				if (executeWayFlag == PRE_WEEK_EXCUTE) {
					ll_reference_cost.setVisibility(View.GONE);
					single_discount_cost.setVisibility(View.VISIBLE);
					discount_cost.setText(preCommission);
				} else {
					ll_reference_cost.setVisibility(View.GONE);
					ll_discount_cost.setVisibility(View.VISIBLE);
					discount_cost_tv.setText(preCommission);
				}
			}
			// 汇款套餐返回字段为1显示,0则不显示
			if (DeptBaseActivity.ONE.equals(remitSetMealFlag)) {
				ll_remit_flag.setVisibility(View.VISIBLE);
			}
		} else if (DeptBaseActivity.ZERO.equals(isChargeFlag)) { // 查询失败
			// 查询失败,基准费用&优惠费用 显示空
			if (executeWayFlag == PRE_WEEK_EXCUTE) {
				single_reference_cost.setVisibility(View.VISIBLE);
				single_discount_cost.setVisibility(View.VISIBLE);
				reference_cost.setText("");
				discount_cost.setText("");
			} else {
				ll_reference_cost.setVisibility(View.VISIBLE);
				ll_discount_cost.setVisibility(View.VISIBLE);
				reference_cost_tv.setText("");
				discount_cost_tv.setText("");
			}
		}
	}

	/**
	 * 试算手续费 成功页面无 优惠后费用提示信息
	 * 
	 * @param isChargeFlag
	 *            试费查询是否成功标识位 1:查询成功 0:查询失败
	 * @param needCommission
	 *            应收费用
	 * @param preCommission
	 *            拟收费用
	 * @param remitSetMealFlag
	 *            是否签约汇款笔数套餐标识位 1:签约汇款笔数套餐 0:未签约汇款笔数套餐
	 * @param isShowBenchmark
	 *            是否显示基准费用 当基准费用字段返回是为true 没有返回此字段则为false
	 */
	private void trialTrialChargeNoPrompted(String isChargeFlag, String needCommission, String preCommission,
			String remitSetMealFlag, Boolean isShowBenchmark) {
		// 如果 试算成功 优惠费用不为空 显示基准费用 优惠费用为空 不显示基准费用
		if (DeptBaseActivity.ONE.equals(isChargeFlag)) { // 查询手续费试算成功
			if (isShowBenchmark = true && !StringUtil.isNull(needCommission)) { // 基准费用不为空
				// add by luqp 2015年12月14日10:54:51 预约周期修改为 单笔基准费用 单笔优惠后费用
				if (executeWayFlag == PRE_WEEK_EXCUTE) {
					single_reference_cost.setVisibility(View.VISIBLE);
					single_discount_cost.setVisibility(View.VISIBLE);
					reference_cost.setText(needCommission);
					discount_cost.setText(preCommission);
				} else {
					ll_reference_cost.setVisibility(View.VISIBLE);
					ll_discount_cost.setVisibility(View.VISIBLE);
					reference_cost_tv.setText(needCommission);
					discount_cost_tv.setText(preCommission);
				}
			} else { // 基准费用为空或0或0.00 只显示优惠后费用
				if (executeWayFlag == PRE_WEEK_EXCUTE) {
					ll_reference_cost.setVisibility(View.GONE);
					single_discount_cost.setVisibility(View.VISIBLE);
					discount_cost.setText(preCommission);
				} else {
					ll_reference_cost.setVisibility(View.GONE);
					ll_discount_cost.setVisibility(View.VISIBLE);
					discount_cost_tv.setText(preCommission);
				}
			}
			if (DeptBaseActivity.ONE.equals(remitSetMealFlag)) {
				ll_remit_flag.setVisibility(View.VISIBLE);
			}
		} else if (DeptBaseActivity.ZERO.equals(isChargeFlag)) { // 查询失败
			// 查询失败,基准费用&优惠费用 显示空
			if (executeWayFlag == PRE_WEEK_EXCUTE) {
				single_reference_cost.setVisibility(View.VISIBLE);
				single_discount_cost.setVisibility(View.VISIBLE);
				reference_cost.setText("");
				discount_cost.setText("");
			} else {
				ll_reference_cost.setVisibility(View.VISIBLE);
				ll_discount_cost.setVisibility(View.VISIBLE);
				reference_cost_tv.setText("");
				discount_cost_tv.setText("");
			}
		}
	}
	// /////////////////////////试算手续费 end
}
