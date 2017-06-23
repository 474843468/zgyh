package com.chinamworld.bocmbci.biz.dept.savereg;

import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 我要存定期 确认页面 [整存整取,定活两便,一天通知存款,七天通知存款]
 * 
 * @author Administrator luqp 修改2015年8月5日14:36:36
 */
public class SaveConfirmActivity extends DeptBaseActivity {

	private LinearLayout tabcontent;// 主Activity显示
	private Context context = this;
	private View view = null;
	/** 上一步 */
	// private Button btnLast;
	/** 确定 */
	private Button btnConfirm;
	/** 转出账户号 */
	private TextView outNoTv;
	/** 转入账户号 */
	private TextView inNoTv;
	/** 币种 */
	private TextView currencyTv;
	/** 业务品种 */
	private TextView businessTypeTv;
	/** 钞汇标志 */
	private TextView cashRemitTv;
	/** 转账金额 */
	private TextView saveAmountTv;
	/** 存期 */
	private TextView saveTimeTv;
	/** 附言 */
	private TextView attachMessageTv;
	/** 执行方式 */
	private TextView excuteWayTv;
	/** 起始日期 */
	private TextView startDateTv;
	/** 结束日期 */
	private TextView endDateTv;
	/** 周期 */
	private TextView weekTv;
	/** 执行次数 */
	private TextView excuteTimeTv;
	/** 预约执行日期 */
	private TextView excuteDateTv;

	/** 钞汇标志layout */
	// 在定活两便的时候不显示
	private LinearLayout cashRemitLayout;
	/** 日期layout */
	// 只有在日期执行的时候才显示
	private LinearLayout dateLayout;
	/** 存期layout */
	// 只有在整存整取时候才显示
	private LinearLayout saveTimeLayout;
	/** 开始日期layout */
	// 预约预约周期执行时 显示 立即执行 预约日期执行 不显示
	private LinearLayout startDateLayout;
	/** 结束日期layout */
	// 预约预约周期执行时 显示 立即执行 预约日期执行 不显示
	private LinearLayout endDateLayout;
	/** 周期layout */
	// 预约预约周期执行时 显示 立即执行 预约日期执行 不显示
	private LinearLayout weekLayout;
	/** 执行次数layout */
	// 预约预约周期执行时 显示 立即执行 预约日期执行 不显示
	private LinearLayout excuteTimeLayout;
	/** 业务品种layout */
	// 只有在通知存款才显示
	private LinearLayout businessTypeLayout;
	/** 约定方式 */
	private LinearLayout promiseWayLayout;
	private TextView promiseWayTv;

	// private String strOutNickName = null;
	private String strOutNo = null;
	// private String strInNickName = null;
	private String strInNo = null;
	/** 转出账户ID*/
	private String fromAccountId = null;
	/** 转入账户ID*/
	private String toAccountId = null;
	/** 币种*/
	private String strCurrency = "";
	/** 钞汇*/
	private String strCashRemit = null;
	/** 金额*/
	private String strSaveAmount = null;
	/** 存期*/
	private String strSaveTime = null;
	/** 备注*/
	private String strAttachMessage = null;
	/**  3种执行方式的 文字*/
	private String strExcuteWay = null;
	/** 3种执行方式的通讯标示 “0” “1” “2”*/
	private String excuteWayFlag;
	/** 预约周期起始日期*/
	private String strStartDate = null;
	/** 预约周期结束日期*/
	private String strEndDate = null;
	/** 周期方式 W：单周 D：双周 M：月*/
	private String strWeek = null;
	private String token = null;
	/** 执行次数 */
	private String strExcuteTime = null;
	/** 执行日期 */
	private String strExcuteDate = null;
	/** 存款类型标志 */
	private int typeFlag;
	/** 业务品种 */
	private String strBusinessType = null;
	/** 转存方式 */
	private String strPromiseWay = null;
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
	private String needCommission = null;
	/** 优惠后费用 */
	private String preCommission = null;
	/** 基准费用 */
	private TextView reference_cost_tv;
	/** 优惠后费用 */
	private TextView discount_cost_tv;
	/** 试费查询是否成功标识位 1:查询成功 0:查询失败 */
	private String isChargeFlag = null;
	/** 是否签约汇款笔数套餐标识位 */
	private String remitSetMealFlag = null;
	/** 优惠后费用数据 */
	private Map<String, Object> flagData = null;
	/** 是否显示基准费用 当基准费用字段返回是为true 没有返回此字段则为false*/
	private Boolean isShowBenchmark = false;
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
	// 利率上浮1.3倍start========================================
	/** 约定转存布局 1.3倍*/
	private LinearLayout promiseWayLL = null;
	/** 1.3倍提示信息*/
	private TextView promptAcc;
	/** 1.3倍存期 Tag */
	private boolean isShowInterestRateTag;
	/** 1.3倍存期 Key*/
	private String interestRateKey = null;
	/** 存款类型利率上浮1.3倍标志 */
	private int interestRateFlag;
	//  利率上浮1.3倍end==========================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);

		LayoutInflater inflater = LayoutInflater.from(this); // 加载理财产品布局
		view = inflater.inflate(R.layout.dept_save_confirm, null);
		tabcontent.addView(view);
		
		newTranBtn = (Button) this.findViewById(R.id.ib_top_right_btn);
		newTranBtn.setText(this.getResources().getString(R.string.new_save));

		newTranBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), SaveRegularActivity.class);
				startActivity(intent);
				finish();
			}
		});
		// 步骤栏
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getResources().getString(R.string.save_confirm_title1),
						this.getResources().getString(R.string.save_confirm_title2),
						this.getResources().getString(R.string.save_confirm_title3) });
		StepTitleUtils.getInstance().setTitleStep(2);
		
		
		// 获取数据
		aquireData();
		// 初始化控件
		init();
		// 显示textview
		displayTextView();
	}

	/** 初始化控件 */
	private void init() {
		newTranBtn.setVisibility(View.VISIBLE);

		outNoTv = (TextView) findViewById(R.id.dept_accout_no_tv);
		inNoTv = (TextView) findViewById(R.id.dept_accin_no_tv);
		businessTypeTv = (TextView) findViewById(R.id.dept_business_type_tv);
		currencyTv = (TextView) findViewById(R.id.dept_currency_tv);
		cashRemitTv = (TextView) findViewById(R.id.dept_cashremit_tv);
		saveAmountTv = (TextView) findViewById(R.id.dept_save_amount_tv);
		saveTimeTv = (TextView) findViewById(R.id.dept_save_time_tv);
		attachMessageTv = (TextView) findViewById(R.id.dept_attach_message_tv);
		excuteWayTv = (TextView) findViewById(R.id.dept_excute_waty_tv);
		excuteDateTv = (TextView) findViewById(R.id.dept_date_tv);
		startDateTv = (TextView) findViewById(R.id.dept_start_date_tv);
		endDateTv = (TextView) findViewById(R.id.dept_end_date_tv);
		weekTv = (TextView) findViewById(R.id.dept_week_tv);
		excuteTimeTv = (TextView) findViewById(R.id.dept_excute_time_tv);

		businessTypeLayout = (LinearLayout) findViewById(R.id.dept_business_type_layout);
		cashRemitLayout = (LinearLayout) findViewById(R.id.dept_cashremit_layout);
		saveTimeLayout = (LinearLayout) findViewById(R.id.dept_save_time_layout);
		dateLayout = (LinearLayout) findViewById(R.id.dept_date_layout);
		startDateLayout = (LinearLayout) findViewById(R.id.dept_start_date_layout);
		endDateLayout = (LinearLayout) findViewById(R.id.dept_end_date_layout);
		weekLayout = (LinearLayout) findViewById(R.id.dept_week_layout);
		excuteTimeLayout = (LinearLayout) findViewById(R.id.dept_excute_time_layout);

		promiseWayLayout = (LinearLayout) findViewById(R.id.dept_promise_way_layout);
		promiseWayTv = (TextView) findViewById(R.id.dept_promise_way_tv);

		ll_reference_cost = (LinearLayout) findViewById(R.id.ll_reference_cost);
		reference_cost_tv = (TextView) findViewById(R.id.reference_cost_tv);
		ll_discount_cost = (LinearLayout) findViewById(R.id.ll_discount_cost);
		discount_cost_tv = (TextView) findViewById(R.id.discount_cost_tv);
		ll_discount_cost_prompted = (LinearLayout) findViewById(R.id.ll_discount_cost_prompted);
		tv_discount_cost_prompted = (TextView) findViewById(R.id.tv_discount_cost_prompted);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_discount_cost_prompted);
		ll_remit_flag = (LinearLayout) findViewById(R.id.ll_remit_flag);
		remit_flag = (TextView) findViewById(R.id.remit_flag);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, remit_flag);

		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, saveAmountTv);
		btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
		btnConfirm.setOnClickListener(comfirmListener);
		// //预约周期基准费用显示(单笔基准费用)优惠后费用显示(单笔优惠后费用)////
		// add by luqp 2015年12月11日15:02:50 TODO
		single_reference_cost = (LinearLayout) findViewById(R.id.ll_single_reference_cost);
		single_reference = (TextView) findViewById(R.id.tv_single_reference);
		reference_cost = (TextView) findViewById(R.id.tv_reference_cost);
		single_discount_cost = (LinearLayout) findViewById(R.id.ll_single_discount_cost);
		single_discount = (TextView) findViewById(R.id.tv_single_discount);
		discount_cost = (TextView) findViewById(R.id.tv_discount_cost);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, single_reference);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, single_discount);
		
		// 转存方式布局
		promiseWayLL = (LinearLayout) findViewById(R.id.ll_tran_mode);
		promptAcc = (TextView) findViewById(R.id.tv_tran_mode);
		
		// //////////////////////////////////////////////////////////////////////////////////
		// //                      手续费试算 start                                        /////
		// /////////////////////////////////////////////////////////////////////////////////
		// 如果 试算成功 优惠费用不为空 显示基准费用 优惠费用为空 不显示基准费用
		if (DeptBaseActivity.ONE.equals(isChargeFlag)) { // 查询手续费试算成功
			if (isShowBenchmark = true && !StringUtil.isNull(needCommission)) { // 基准费用不为空
				ll_discount_cost_prompted.setVisibility(View.VISIBLE);
				// add by luqp 2015年12月14日11:19:31 预约周期修改为 单笔基准费用 单笔优惠后费用
				if (excuteWayFlag.equals(ConstantGloble.PREPERIODEXE)) {
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
				// add by luqp 2015年12月14日11:19:36 预约周期修改为 单笔优惠后费用
				ll_discount_cost_prompted.setVisibility(View.VISIBLE);
				if (excuteWayFlag.equals(ConstantGloble.PREPERIODEXE)) {
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
			if (excuteWayFlag.equals(ConstantGloble.PREPERIODEXE)) {
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
		// //////////////////////////////////////////////////////////////////////////////////
		// //                      手续费试算end                                           /////
		// /////////////////////////////////////////////////////////////////////////////////
	}

	/** 整存整取立即执行 确认信息 确定按钮监听 */
	private OnClickListener comfirmListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 发送通讯 新增整存整取
			// 请求token
			requestCommConversationId();
			BaseHttpEngine.showProgressDialog();
		}
	};

	/**
	 * 展示整存整取确认页面
	 */
	private void dispalyWholeSaveLayout() {
		setTitle(R.string.save_whole);
		if (excuteWayFlag.equals(ConstantGloble.NOWEXE)) {// 立即执行
			dateLayout.setVisibility(View.GONE);
			startDateLayout.setVisibility(View.GONE);
			endDateLayout.setVisibility(View.GONE);
			weekLayout.setVisibility(View.GONE);
			excuteTimeLayout.setVisibility(View.GONE);
		} else if (excuteWayFlag.equals(ConstantGloble.PREDATEEXE)) {
			startDateLayout.setVisibility(View.GONE);
			endDateLayout.setVisibility(View.GONE);
			weekLayout.setVisibility(View.GONE);
			excuteTimeLayout.setVisibility(View.GONE);
		} else if (excuteWayFlag.equals(ConstantGloble.PREPERIODEXE)) {
			dateLayout.setVisibility(View.GONE);
			excuteTimeLayout.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 展示定活两便确认页面
	 */
	private void dispalyRegRandomLayout() {
		setTitle(R.string.save_random);
		strCurrency = (String) DeptDataCenter.getInstance().getAccInInfoMap().get(Comm.CURRENCYCODE);
		if (ConstantGloble.PRMS_CODE_RMB.equals(strCurrency))
			strCashRemit = ConstantGloble.CASHREMIT_00;
		else
			strCashRemit = (String) DeptDataCenter.getInstance().getAccInInfoMap().get(Dept.CASHREMIT);

		if (excuteWayFlag.equals(ConstantGloble.NOWEXE)) {// 立即执行
			cashRemitLayout.setVisibility(View.GONE);
			saveTimeLayout.setVisibility(View.GONE);
			dateLayout.setVisibility(View.GONE);
			startDateLayout.setVisibility(View.GONE);
			endDateLayout.setVisibility(View.GONE);
			weekLayout.setVisibility(View.GONE);
			excuteTimeLayout.setVisibility(View.GONE);
		} else if (excuteWayFlag.equals(ConstantGloble.PREDATEEXE)) {
			cashRemitLayout.setVisibility(View.GONE);
			saveTimeLayout.setVisibility(View.GONE);
			startDateLayout.setVisibility(View.GONE);
			endDateLayout.setVisibility(View.GONE);
			weekLayout.setVisibility(View.GONE);
			excuteTimeLayout.setVisibility(View.GONE);
		} else if (excuteWayFlag.equals(ConstantGloble.PREPERIODEXE)) {
			cashRemitLayout.setVisibility(View.GONE);
			saveTimeLayout.setVisibility(View.GONE);
			dateLayout.setVisibility(View.GONE);
			excuteTimeLayout.setVisibility(View.VISIBLE);
		}
	}

	/***
	 * 展示通知存款确定页面
	 */
	private void displayNotifyLayout() {
		// strCurrency = (String)
		// DeptDataCenter.getInstance().getAccInInfoMap().get(Comm.CURRENCYCODE);
		// strCashRemit = (String)
		// DeptDataCenter.getInstance().getAccInInfoMap().get(Dept.CASHREMIT);
		businessTypeLayout.setVisibility(View.VISIBLE);
		// 显示转存方式
		promiseWayLayout.setVisibility(View.VISIBLE);
		saveTimeLayout.setVisibility(View.GONE);

		if (excuteWayFlag.equals(ConstantGloble.NOWEXE)) {// 立即执行
			dateLayout.setVisibility(View.GONE);
			startDateLayout.setVisibility(View.GONE);
			endDateLayout.setVisibility(View.GONE);
			weekLayout.setVisibility(View.GONE);
			excuteTimeLayout.setVisibility(View.GONE);
		} else if (excuteWayFlag.equals(ConstantGloble.PREDATEEXE)) {
			startDateLayout.setVisibility(View.GONE);
			endDateLayout.setVisibility(View.GONE);
			weekLayout.setVisibility(View.GONE);
			excuteTimeLayout.setVisibility(View.GONE);
		} else if (excuteWayFlag.equals(ConstantGloble.PREPERIODEXE)) {
			dateLayout.setVisibility(View.GONE);
			excuteTimeLayout.setVisibility(View.VISIBLE);
		}
	}

	/** 获取数据 */
	private void aquireData() {
		strOutNo = (String) DeptDataCenter.getInstance().getAccOutInfoMap().get(Comm.ACCOUNTNUMBER);
		strInNo = (String) DeptDataCenter.getInstance().getAccInInfoMap().get(Comm.ACCOUNTNUMBER);

		fromAccountId = (String) DeptDataCenter.getInstance().getAccOutInfoMap().get(Comm.ACCOUNT_ID);
		toAccountId = (String) DeptDataCenter.getInstance().getAccInInfoMap().get(Comm.ACCOUNT_ID);

		strBusinessType = this.getIntent().getStringExtra(Dept.NEW_NOTIFY_SAVE_BUSINESS_TYPE);
		strSaveAmount = this.getIntent().getStringExtra(Dept.AMOUNT);
		strAttachMessage = this.getIntent().getStringExtra(Dept.MEMO);
		// 约定方式
		strPromiseWay = this.getIntent().getStringExtra(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE);

		// 根据typeFlag 和 excuteWay 显示不同的界面
		typeFlag = this.getIntent().getIntExtra(ConstantGloble.TYPE_FLAG, 0);
		strExcuteDate = this.getIntent().getStringExtra(Dept.EXECUTE_DATE);

		strCurrency = this.getIntent().getStringExtra(Dept.CURRENCY);

		if (ConstantGloble.PRMS_CODE_RMB.equals(strCurrency)) {
			strCashRemit = ConstantGloble.CASHREMIT_00;
		} else {
			strCashRemit = this.getIntent().getStringExtra(Dept.CASHREMIT);
		}
		strSaveTime = this.getIntent().getStringExtra(Dept.CD_TERM);
		// 执行方式分为3中 0立即执行 1预约日期执行 2预约周期执行
		excuteWayFlag = this.getIntent().getStringExtra(Dept.NEW_NOTIFY_SAVE_TRANSMODE);
		strExcuteWay = LocalData.TransModeDisplay.get(excuteWayFlag);

		strStartDate = this.getIntent().getStringExtra(Dept.START_DATE);
		strEndDate = this.getIntent().getStringExtra(Dept.END_DATE);
		strWeek = this.getIntent().getStringExtra(Dept.CYCLE_SELECT);
		strExcuteTime = this.getIntent().getStringExtra(Dept.NEED_COUNT);

		// ==============================================================================================
		// add by luqp 2016年3月29日   利率上浮1.3 
		isShowInterestRateTag = this.getIntent().getBooleanExtra("IsShowInterestRateTag", false);
		interestRateKey = this.getIntent().getStringExtra("InterestRateFloatingKey");
		interestRateFlag = this.getIntent().getIntExtra(Dept.INTEREST_TYPE_FLAG, 0);
		// ==============================================================================================
		
		// //////////////////////////////////////////////////////////////////////////////////
		// //                      手续费试算 start                                        /////
		// /////////////////////////////////////////////////////////////////////////////////
		if (typeFlag == WHOLE_SAVE_BOTTOM) { // 整存整取
			flagData = DeptDataCenter.getInstance().getCostCalculationMap();
			if (flagData.containsKey(Dept.Save_needCommissionCharge)) {
				isShowBenchmark = true;
			}
			isChargeFlag = (String) flagData.get(Dept.Save_getChargeFlag);
			remitSetMealFlag = (String) flagData.get(Dept.Save_remitSetMealFlag);
			String need = (String) flagData.get(Dept.Save_needCommissionCharge);
			String pre = (String) flagData.get(Dept.Save_preCommissionCharge);
			needCommission = DeptBaseActivity.parseStringCodePattern(strCurrency, need, 2);
			preCommission = DeptBaseActivity.parseStringCodePattern(strCurrency, pre, 2);
		} else if (typeFlag == REGULAR_RANDOM_BOTTOM) { // 定活两便
			flagData = DeptDataCenter.getInstance().getRegulaRandomMap();
			if (flagData.containsKey(Dept.Save_needCommissionCharge)) {
				isShowBenchmark = true;
			}
			isChargeFlag = (String) flagData.get(Dept.Save_getChargeFlag);
			remitSetMealFlag = (String) flagData.get(Dept.Save_remitSetMealFlag);
			String need = (String) flagData.get(Dept.Save_needCommissionCharge);
			String pre = (String) flagData.get(Dept.Save_preCommissionCharge);
			needCommission = DeptBaseActivity.parseStringCodePattern(strCurrency, need, 2);
			preCommission = DeptBaseActivity.parseStringCodePattern(strCurrency, pre, 2);
		} else if (typeFlag == NOTIFY_ONE_DAY_BOTTOM) { // 通知存款 <一天通知>
			flagData = DeptDataCenter.getInstance().getNotificationMap();
			if (flagData.containsKey(Dept.Save_needCommissionCharge)) {
				isShowBenchmark = true;
			}
			isChargeFlag = (String) flagData.get(Dept.Save_getChargeFlag);
			remitSetMealFlag = (String) flagData.get(Dept.Save_remitSetMealFlag);
			String need = (String) flagData.get(Dept.Save_needCommissionCharge);
			String pre = (String) flagData.get(Dept.Save_preCommissionCharge);
			needCommission = DeptBaseActivity.parseStringCodePattern(strCurrency, need, 2);
			preCommission = DeptBaseActivity.parseStringCodePattern(strCurrency, pre, 2);
		} else if (typeFlag == NOTIFY_SEVEN_DAY_BOTTOM) { // 通知存款 <7天通知>
			flagData = DeptDataCenter.getInstance().getNotificationMap();
			if (flagData.containsKey(Dept.Save_needCommissionCharge)) {
				isShowBenchmark = true;
			}
			isChargeFlag = (String) flagData.get(Dept.Save_getChargeFlag);
			remitSetMealFlag = (String) flagData.get(Dept.Save_remitSetMealFlag);
			String need = (String) flagData.get(Dept.Save_needCommissionCharge);
			String pre = (String) flagData.get(Dept.Save_preCommissionCharge);
			needCommission = DeptBaseActivity.parseStringCodePattern(strCurrency, need, 2);
			preCommission = DeptBaseActivity.parseStringCodePattern(strCurrency, pre, 2);
		} else if (typeFlag == WHOLE_SAVE_BOTTOM_NEW) { // 整存整取1.3倍底部标示
			// =================================================================================
			// add by luqp 2016年3月21日 利率上浮1.3倍手续费试算
			flagData = DeptDataCenter.getInstance().getInterestRateFloatingMap(); // 获取1.3倍数据
			if (flagData.containsKey(Dept.Save_needCommissionCharge)) {
				isShowBenchmark = true;
			}
			isChargeFlag = (String) flagData.get(Dept.Save_getChargeFlag);
			remitSetMealFlag = (String) flagData.get(Dept.Save_remitSetMealFlag);
			String need = (String) flagData.get(Dept.Save_needCommissionCharge);
			String pre = (String) flagData.get(Dept.Save_preCommissionCharge);
			needCommission = DeptBaseActivity.parseStringCodePattern(strCurrency, need, 2);
			preCommission = DeptBaseActivity.parseStringCodePattern(strCurrency, pre, 2);
			// =================================================================================
		}
		// //////////////////////////////////////////////////////////////////////////////////
		// //                      手续费试算 end                                          /////
		// /////////////////////////////////////////////////////////////////////////////////
	}

	/** 显示textivew */
	private void displayTextView() {
		// outNickNameTv.setText(strOutNickName);
		outNoTv.setText(StringUtil.getForSixForString(strOutNo));
		// inNickNameTv.setText(strInNickName);
		inNoTv.setText(StringUtil.getForSixForString(strInNo));
		currencyTv.setText(LocalData.Currency.get(strCurrency));
		businessTypeTv.setText(LocalData.CallCDTerm.get(strBusinessType));

		// 如果币种是人名币 钞汇标志不显示
		if (strCurrency.equals(ConstantGloble.PRMS_CODE_RMB)) {
			cashRemitLayout.setVisibility(View.INVISIBLE);
		}
		/** add by luqp 2016年8月26日 1.3倍与定利多起存金额提示 优惠利率提示信息*/
		final String strMysavePrpmptNew = this.getResources().getString(R.string.mysave_prpmpt_new);
		cashRemitTv.setText(LocalData.CurrencyCashremit.get(strCashRemit));
		// ========================================================================================
		// add by luqp 2016年3月21日 利率上浮1.3倍 存期  转存方式
		if (interestRateFlag == INTEREST_TAG) { // 利率上浮1.3倍&&定利多存期
			saveTimeTv.setText(interestRateKey); // 1.3倍存期
			// add by luqp 2016年8月26日 1.3倍与定利多起存金额提示
			if(interestRateKey.equals("六个月定利多升级版")||interestRateKey.equals("一年定利多升级版")
					||interestRateKey.equals("两年定利多升级版")){
				promptAcc.setText(strMysavePrpmptNew);
			}

			if(interestRateKey.equals("三个月优惠利率")||interestRateKey.equals("六个月优惠利率")||interestRateKey.equals("一年期优惠利率")){
				promiseWayLL.setVisibility(View.GONE);  // by luqp 2016年10月9日屏蔽1.3倍转存方式和提示信息
				promptAcc.setVisibility(View.GONE);
			}
		} else {
			saveTimeTv.setText(LocalData.SaveRegularCDPeriod.get(strSaveTime)); // 存期
		}
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, saveTimeTv);
		//========================================================================================

		// 如果是韩元或日元 不显示小数点
		strSaveAmount = StringUtil.parseStringCodePattern(strCurrency, strSaveAmount, 2);

		saveAmountTv.setText(strSaveAmount);
		if (StringUtil.isNull(strAttachMessage)) {
			attachMessageTv.setText("-");
		} else {
			attachMessageTv.setText(strAttachMessage);
		}
		if (strAttachMessage.length() > 10) {// 如果附言大于10个字
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this, attachMessageTv);
		}
		excuteWayTv.setText(strExcuteWay);
		excuteDateTv.setText(strExcuteDate);

		startDateTv.setText(strStartDate);
		endDateTv.setText(strEndDate);
		weekTv.setText(LocalData.Frequency.get(strWeek));
		excuteTimeTv.setText(strExcuteTime);
		promiseWayTv.setText(LocalData.ConventionConvertType.get(strPromiseWay));

		if (typeFlag == WHOLE_SAVE_BOTTOM) {// 整存整取
			dispalyWholeSaveLayout();
		} else if (typeFlag == REGULAR_RANDOM_BOTTOM) {// 定活两便
			dispalyRegRandomLayout();
		} else if (typeFlag == NOTIFY_ONE_DAY_BOTTOM) {// 通知存款 <一天通知>
			setTitle(R.string.save_one_day);
			displayNotifyLayout();
		} else if (typeFlag == NOTIFY_SEVEN_DAY_BOTTOM) {// 通知存款 <7天通知>
			setTitle(R.string.save_seven_day);
			displayNotifyLayout();
		} else if (typeFlag == WHOLE_SAVE_BOTTOM_NEW) { // 整存整取1.3倍
			// ==================================================
			// add by luqp 2016年3月21日 利率上浮1.3倍
			dispalyWholeSaveLayout();
			// ==================================================
		}
		// 根据不同的类型交易 CashRemit值得获取方式不同 定活两便没有钞汇标志
	}

	/** 通讯返回 */
	@Override
	public void communicationCallBack(int flag) {
		super.communicationCallBack(flag);
		Intent intent = new Intent();
		intent.putExtra(ConstantGloble.TYPE_FLAG, typeFlag);

		intent.putExtra(Dept.EXECUTE_DATE, strExcuteDate);
		intent.putExtra(Dept.START_DATE, strStartDate);
		intent.putExtra(Dept.END_DATE, strEndDate);
		intent.putExtra(Dept.CYCLE_SELECT, strWeek);
		intent.putExtra(Dept.NEW_NOTIFY_SAVE_BUSINESS_TYPE, strBusinessType);
		intent.putExtra(Dept.NEW_NOTIFY_SAVE_TRANSMODE, excuteWayFlag);// 1预约日期执行
		intent.putExtra(Dept.CURRENCY, strCurrency);
		intent.putExtra(Dept.CASHREMIT, strCashRemit);
		intent.putExtra(Dept.CD_TERM, strSaveTime);
		intent.putExtra(Dept.AMOUNT, strSaveAmount);
		intent.putExtra(Dept.MEMO, strAttachMessage);
		intent.putExtra(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE, strPromiseWay);
		if (interestRateFlag == INTEREST_TAG) {
			intent.putExtra("IsShowInterestRateTag", isShowInterestRateTag);
			intent.putExtra("InterestRateFloatingKey", interestRateKey);
			intent.putExtra(Dept.INTEREST_TYPE_FLAG , interestRateFlag);
		}
		intent.setClass(SaveConfirmActivity.this, SaveSuccessActivity.class);
		startActivity(intent);
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

		if (typeFlag == WHOLE_SAVE_BOTTOM) {// 整存整取
			requestNewWhoelSaveAndCheckout(fromAccountId, toAccountId, strCurrency, strCashRemit, strSaveTime,
					strSaveAmount, strAttachMessage, excuteWayFlag, strExcuteDate, strStartDate, strEndDate, strWeek, token);
		} else if (typeFlag == REGULAR_RANDOM_BOTTOM) {// 定活两便
			requestNewRegAndRandom(fromAccountId, toAccountId, strCurrency, strSaveAmount, strAttachMessage, excuteWayFlag,
					strExcuteDate, strStartDate, strEndDate, strWeek, token);
		} else if (typeFlag == NOTIFY_ONE_DAY_BOTTOM || typeFlag == NOTIFY_SEVEN_DAY_BOTTOM) {// 通知存款
			requestNewNotifySave(fromAccountId, toAccountId, strBusinessType, strPromiseWay, strCurrency, strCashRemit,
					strSaveAmount, excuteWayFlag, strAttachMessage, strExcuteDate, strWeek, strStartDate, strEndDate, token);
		} else if (typeFlag == WHOLE_SAVE_BOTTOM_NEW) {
			String type = ""; // 定利多
			if(interestRateKey.equals("六个月定利多升级版")||interestRateKey.equals("一年定利多升级版")
					||interestRateKey.equals("两年定利多升级版")){
				type = "14"; // 定利多
			}else{
				type = "13"; // 产品类型 1.3倍利率整存整取：13
			}
			requestPsnTransLumpMoreTimeDepositNew(fromAccountId, toAccountId, strCurrency, strCashRemit, strSaveTime,
					strSaveAmount,type,strAttachMessage,token);
		}
	}
}
