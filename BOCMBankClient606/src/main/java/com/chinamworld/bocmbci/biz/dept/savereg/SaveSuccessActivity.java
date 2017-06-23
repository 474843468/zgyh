package com.chinamworld.bocmbci.biz.dept.savereg;

import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 我要存定期 成功页面 [整存整取,定活两便,一天通知存款,七天通知存款]
 * 
 * @author Administrator luqp 修改2015年8月5日14:36:36
 */
public class SaveSuccessActivity extends DeptBaseActivity {

	private static final String TAG = "SaveSuccessActivity";

	private LinearLayout tabcontent;// 主Activity显示
	private Context context = this;
	private View view = null;
	/** title信息 */
	private TextView titleText;
	/** title字符 */
	private String titleStr;
	private String appointStr;
	/** 确定 */
	private Button btnConfirm;
	/** 转账批次号 */
	private TextView batseqTv;
	/** 转出账户号 */
	private TextView outNoTv;
	/** 转入账户号 */
	private TextView inNoTv;
	/** 币种 */
	private TextView currencyTv;
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
	/** 业务品种 */
	private TextView businessTypeTv;
	/** 约定方式 */
	private TextView promiseWayTv;

	private String strBatseq = null;
	private String strOutNo = null;
	private String strInNo = null;
	private String strCurrency = "";
	private String strCashRemit = null;
	private String strSaveAmount = null;
	private String strSaveTime = null;
	private String strAttachMessage = null;
	private String strExcuteWay = null;
	private String strStartDate = null;
	private String strEndDate = null;
	private String strWeek = null;
	private String strExcuteTimes = null;
	private String strExcuteDate = null;
	/** 业务品种 */
	private String strBusinessType;
	/** 约定方式 */
	private String strPromiseWay;

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
	/** 约定方式layout */
	// 只有在通知存款才显示
	private LinearLayout promiseWayLayout;
	// 交易序号 （预约执行的时候都不显示）
	private LinearLayout jiaoyiNumLayout;
	/** 转账批次号 */
	private LinearLayout ll_tran_batseq;

	/** 交易序列号 */
	private TextView transationIdTv;
	private String transactionId;

	/** 新增整存整取返回数据 */
	private Map<String, Object> requestCallBackMap;
	/** 新增通知存款 返回数据 */
	private Map<String, Object> requestNotifySaveCallBackMap;

	/** 存款类型标志 */
	private int typeFlag;
	private String excuteWayFlag;
	// /////////////////////////////
	/** 基准费用布局 */
	private LinearLayout ll_reference_cost;
	/** 优惠后费用布局 */
	private LinearLayout ll_discount_cost;
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
	/** 是否显示基准费用 当基准费用字段返回是为true 没有返回此字段则为false */
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
	/** 优惠后费用 预约日期 预约周期 试算费用提交交易接口获取 */
	private String finalCommissionCharge = null;
	// //////////////////////////////////////////////////////
	// 利率上浮1.3倍start========================================
	/** 约定转存布局 1.3倍*/
	private LinearLayout promiseWayLL;
	/** 1.3倍提示信息*/
	private TextView promptAcc;
	/** 1.3倍存期 Tag */
	private boolean isShowInterestRateTag;
	/** 1.3倍存期 Key*/
	private String interestRateKey = null;
	/** 存款类型利率上浮1.3倍标志 */
	private int interestRateFlag;
	// 利率上浮1.3倍end==========================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		LayoutInflater inflater = LayoutInflater.from(this); // 加载理财产品布局
		view = inflater.inflate(R.layout.dept_save_success, null);
		tabcontent.addView(view);

		titleStr = this.getResources().getString(R.string.save_trans_success_info);
		appointStr = this.getResources().getString(R.string.appoint_trans_success_info);
		// 步骤栏
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getResources().getString(R.string.save_confirm_title1),
						this.getResources().getString(R.string.save_confirm_title2),
						this.getResources().getString(R.string.save_confirm_title3) });
		StepTitleUtils.getInstance().setTitleStep(3);
		
		// 初始化控件
		init();
		// 获取intent里面的数据
		aquireData();
		// 初始化textView显示数据
		initTextViewData();

		if (typeFlag == WHOLE_SAVE_BOTTOM) {// 整存整取
			dispalyWholeSaveLayout();
		} else if (typeFlag == REGULAR_RANDOM_BOTTOM) {// 定活两便
			dispalyRegRandomLayout();
		} else if (typeFlag == NOTIFY_ONE_DAY_BOTTOM) {// 通知存款
			setTitle(R.string.save_one_day);
			displayNotifyLayout();
		} else if (typeFlag == NOTIFY_SEVEN_DAY_BOTTOM) {
			setTitle(R.string.save_seven_day);
			displayNotifyLayout();
		} else if (typeFlag == WHOLE_SAVE_BOTTOM_NEW) {// 整存整取 1.3
			dispalyWholeSaveLayout();
		}
	}

	/** 初始化控件*/
	private void init() {
		ibBack.setVisibility(View.INVISIBLE);
		// newTranBtn.setVisibility(View.VISIBLE);
		titleText = (TextView) findViewById(R.id.dept_success_title);
		batseqTv = (TextView) findViewById(R.id.dept_tran_batseq_tv);
		transationIdTv = (TextView) findViewById(R.id.dept_transaction_id_tv);
		outNoTv = (TextView) findViewById(R.id.dept_accout_no_tv);
		inNoTv = (TextView) findViewById(R.id.dept_accin_no_tv);
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
		businessTypeTv = (TextView) findViewById(R.id.dept_business_type_tv);
		promiseWayTv = (TextView) findViewById(R.id.dept_promise_way_tv);
		ll_tran_batseq = (LinearLayout) findViewById(R.id.dept_tran_batseq_layout);
		jiaoyiNumLayout = (LinearLayout) findViewById(R.id.jiaoyi_num_layout);
		cashRemitLayout = (LinearLayout) findViewById(R.id.dept_cashremit_layout);
		saveTimeLayout = (LinearLayout) findViewById(R.id.dept_save_time_layout);
		dateLayout = (LinearLayout) findViewById(R.id.dept_date_layout);
		startDateLayout = (LinearLayout) findViewById(R.id.dept_start_date_layout);
		endDateLayout = (LinearLayout) findViewById(R.id.dept_end_date_layout);
		weekLayout = (LinearLayout) findViewById(R.id.dept_week_layout);
		excuteTimeLayout = (LinearLayout) findViewById(R.id.dept_excute_time_layout);
		businessTypeLayout = (LinearLayout) findViewById(R.id.dept_business_type_layout);
		promiseWayLayout = (LinearLayout) findViewById(R.id.dept_promise_way_layout);
		//============================================================================
		// add by luqp 2016年3月20日 转存方式布局
		promiseWayLL = (LinearLayout) findViewById(R.id.ll_tran_mode);
		promptAcc = (TextView) findViewById(R.id.tv_tran_mode);
		//============================================================================
		// ////////////初始化 基准费用 和优惠后费用/////////////////
		ll_reference_cost = (LinearLayout) findViewById(R.id.ll_reference_cost);
		reference_cost_tv = (TextView) findViewById(R.id.reference_cost_tv);
		ll_discount_cost = (LinearLayout) findViewById(R.id.ll_discount_cost);
		discount_cost_tv = (TextView) findViewById(R.id.discount_cost_tv);
		tv_discount_cost_prompted = (TextView) findViewById(R.id.tv_discount_cost_prompted);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_discount_cost_prompted);
		ll_remit_flag = (LinearLayout) findViewById(R.id.ll_remit_flag);
		remit_flag = (TextView) findViewById(R.id.remit_flag);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, remit_flag);
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

		btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
		btnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(SaveSuccessActivity.this, SaveRegularActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	/** 获取intent里面的数据*/
	private void aquireData() {
		typeFlag = this.getIntent().getIntExtra(ConstantGloble.TYPE_FLAG, 0);
		excuteWayFlag = this.getIntent().getStringExtra(Dept.NEW_NOTIFY_SAVE_TRANSMODE);
		// 执行方式分为3中 0立即执行 1预约日期执行 2预约周期执行
		strCurrency = this.getIntent().getStringExtra(Dept.CURRENCY);
		strCashRemit = this.getIntent().getStringExtra(Dept.CASHREMIT);
		strSaveAmount = this.getIntent().getStringExtra(Dept.AMOUNT);
		strSaveTime = this.getIntent().getStringExtra(Dept.CD_TERM);
		strAttachMessage = this.getIntent().getStringExtra(Dept.MEMO);
		strStartDate = this.getIntent().getStringExtra(Dept.START_DATE);
		strEndDate = this.getIntent().getStringExtra(Dept.END_DATE);
		strWeek = this.getIntent().getStringExtra(Dept.CYCLE_SELECT);
		strBusinessType = this.getIntent().getStringExtra(Dept.NEW_NOTIFY_SAVE_BUSINESS_TYPE);
		strPromiseWay = this.getIntent().getStringExtra(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE);
		strExcuteDate = this.getIntent().getStringExtra(Dept.EXECUTE_DATE);
		// ==============================================================================================
		// add by luqp 2016年3月29日   利率上浮1.3 
		isShowInterestRateTag = this.getIntent().getBooleanExtra("IsShowInterestRateTag", false);
		interestRateKey = this.getIntent().getStringExtra("InterestRateFloatingKey");
		interestRateFlag = this.getIntent().getIntExtra(Dept.INTEREST_TYPE_FLAG, 0);
		// ==============================================================================================
		if (typeFlag == NOTIFY_ONE_DAY_BOTTOM || typeFlag == NOTIFY_SEVEN_DAY_BOTTOM) {// 如果是新增通知存款
			requestNotifySaveCallBackMap = DeptDataCenter.getInstance().getRequestNotifySaveCallBackMap();
			strOutNo = (String) requestNotifySaveCallBackMap.get(Dept.NEW_NOTIFY_SAVE_PAYERACCOUNT_NUMBER);
			strInNo = (String) requestNotifySaveCallBackMap.get(Dept.NEW_NOTIFY_SAVE_PAYEEACCOUNT_NUMBER);
			transactionId = (String) requestNotifySaveCallBackMap.get(Dept.NEW_NOTIFY_SAVE_TRANSACTION_ID);
			strBatseq = (String) requestNotifySaveCallBackMap.get(Dept.NEW_NOTIFY_SAVE_BATSEQ);
			strExcuteTimes = (String) requestNotifySaveCallBackMap.get(Dept.NEW_NOTIFY_SAVE_EXECUTETIMES);
			strAttachMessage = (String) requestNotifySaveCallBackMap.get(Dept.NEW_NOTIFY_SAVE_FURINFO);
		} else if (typeFlag == WHOLE_SAVE_BOTTOM_NEW) {
			// =============================================================================
			// add by luqp 2016年3月20日  整存整取1.3倍
			requestCallBackMap = DeptDataCenter.getInstance().getRequestCallBackMap();
			strOutNo = (String) requestCallBackMap.get(Dept.FROM_ACCOUNT_NUMBER);
			strInNo = (String) requestCallBackMap.get(Dept.TO_ACCOUNT_NUMBER);
			transactionId = (String) requestCallBackMap.get(Dept.TRANSACTION_ID);
			strBatseq = (String) requestCallBackMap.get(Dept.BATSEQ);
			strExcuteTimes = (String) requestCallBackMap.get(Dept.NEED_COUNT);
			// ============================================================================
		} else {
			requestCallBackMap = DeptDataCenter.getInstance().getRequestCallBackMap();
			strOutNo = (String) requestCallBackMap.get(Dept.FROM_ACCOUNT_NUMBER);
			strInNo = (String) requestCallBackMap.get(Dept.TO_ACCOUNT_NUMBER);
			transactionId = (String) requestCallBackMap.get(Dept.TRANSACTION_ID);
			strBatseq = (String) requestCallBackMap.get(Dept.BATSEQ);
			strExcuteTimes = (String) requestCallBackMap.get(Dept.NEED_COUNT);
		}

		// //////////////////////////////////////////////////////////////////////////////////
		// //                          手续费试算 start                                  /////
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
			// add by luqp 2015年12月11日10:45:02  优惠后费用修改
			Map<String, Object> youHuiMap = DeptDataCenter.getInstance().getRequestCallBackMap();
			String youhui = (String) youHuiMap.get(Dept.FinalCommissionCharge);
			// add by luqp 2015年12月15日10:33:06 预约日期 预约周期 优惠后费用 提交交易页面取值
			finalCommissionCharge = DeptBaseActivity.parseStringCodePattern(strCurrency, youhui, 2);
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
			Map<String, Object> youHuiMap = DeptDataCenter.getInstance().getRequestCallBackMap();
			// add by luqp 2015年12月11日10:45:02  优惠后费用修改
			String youhui = (String) youHuiMap.get(Dept.FinalCommissionCharge);
			// add by luqp 2015年12月15日10:33:06 预约日期 预约周期 优惠后费用 提交交易页面取值
			finalCommissionCharge = DeptBaseActivity.parseStringCodePattern(strCurrency, youhui, 2);
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
			Map<String, Object> youHuiMap = DeptDataCenter.getInstance().getRequestNotifySaveCallBackMap();
			// add by luqp 2015年12月11日10:45:02  优惠后费用修改
			String youhui = (String) youHuiMap.get(Dept.FinalCommissionCharge);
			// add by luqp 2015年12月15日10:33:06 预约日期 预约周期 优惠后费用 提交交易页面取值
			finalCommissionCharge = DeptBaseActivity.parseStringCodePattern(strCurrency, youhui, 2);
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
			Map<String, Object> youHuiMap = DeptDataCenter.getInstance().getRequestNotifySaveCallBackMap();
			// add by luqp 2015年12月11日10:45:02  优惠后费用修改
			String youhui = (String) youHuiMap.get(Dept.FinalCommissionCharge);
			// add by luqp 2015年12月15日10:33:06 预约日期 预约周期 优惠后费用 提交交易页面取值
			finalCommissionCharge = DeptBaseActivity.parseStringCodePattern(strCurrency, youhui, 2);
		} else if (typeFlag == WHOLE_SAVE_BOTTOM_NEW) { // 整存整取 利率上浮1.3倍数据&&定利多
			// ===================================================================================
			// add by luqp 2016年3月21日 利率上浮手续费试算 
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
			// add by luqp 2015年12月11日10:45:02  优惠后费用修改
			Map<String, Object> youHuiMap = DeptDataCenter.getInstance().getRequestCallBackMap();
			String youhui = (String) youHuiMap.get(Dept.FinalCommissionCharge);
			// add by luqp 2015年12月15日10:33:06 预约日期 预约周期 优惠后费用 提交交易页面取值
			finalCommissionCharge = DeptBaseActivity.parseStringCodePattern(strCurrency, youhui, 2);
			// ===================================================================================
		}
		// //////////////////////////////////////////////////////////////////////////////////
		// //                               手续费试算 end                                 /////
		// /////////////////////////////////////////////////////////////////////////////////
	}

	/** 初始化textview数据*/
	private void initTextViewData() {
		batseqTv.setText(strBatseq);
		transationIdTv.setText(transactionId);
		outNoTv.setText(StringUtil.getForSixForString(strOutNo));
		inNoTv.setText(StringUtil.getForSixForString(strInNo));
		currencyTv.setText(LocalData.Currency.get(strCurrency));

		// 如果币种是人名币 钞汇标志不显示
		if (strCurrency.equals(ConstantGloble.PRMS_CODE_RMB)) {
			cashRemitLayout.setVisibility(View.INVISIBLE);
		}

		cashRemitTv.setText(LocalData.CurrencyCashremit.get(strCashRemit));

		// 格式化小数位
		strSaveAmount = StringUtil.parseStringCodePattern(strCurrency, strSaveAmount, 2);

		saveAmountTv.setText(strSaveAmount);
		saveTimeTv.setText(LocalData.SaveRegularCDPeriod.get(strSaveTime));
		if (StringUtil.isNull(strAttachMessage)) {
			attachMessageTv.setText("-");
		} else {
			attachMessageTv.setText(strAttachMessage);
		}
		if (strAttachMessage.length() > 10) {// 如果附言大于10个字
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this, attachMessageTv);
		}
		excuteWayTv.setText(LocalData.TransModeDisplay.get(excuteWayFlag));
		excuteDateTv.setText(strExcuteDate);
		businessTypeTv.setText(LocalData.CallCDTerm.get(strBusinessType));
		promiseWayTv.setText(LocalData.ConventionConvertType.get(strPromiseWay));
		startDateTv.setText(strStartDate);
		endDateTv.setText(strEndDate);
		weekTv.setText(LocalData.Frequency.get(strWeek));
		excuteTimeTv.setText(strExcuteTimes);

		// ===================================================================================
		/** add by luqp 2016年8月26日 1.3倍与定利多起存金额提示 优惠利率提示信息 */
		final String strMysavePrpmptNew = this.getResources().getString(R.string.mysave_prpmpt_new);
		// add by luqp 2016年3月21日 利率上浮1.3倍 存期  转存方式
		if (interestRateFlag == INTEREST_TAG) { // 利率上浮1.3倍
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
			promiseWayLL.setVisibility(View.GONE);
			promptAcc.setVisibility(View.GONE);
		}

//		saveTimeTv.setText(LocalData.SaveRegularCDPeriod.get(strSaveTime)); // 存期
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, saveTimeTv);
		// ===================================================================================
		// //////////////////////////////////////////////////////////////////////////////////
		// // 手续费试算 start /////
		// /////////////////////////////////////////////////////////////////////////////////
		// 如果 试算成功 优惠费用不为空 显示基准费用 优惠费用为空 不显示基准费用
		if (DeptBaseActivity.ONE.equals(isChargeFlag)) { // 查询手续费试算成功
			if (isShowBenchmark = true && !StringUtil.isNull(needCommission)) { // 基准费用不为空
				// add by luqp 2015年12月14日11:24:05 预约周期修改为 单笔基准费用 单笔优惠后费用
				if (excuteWayFlag.equals(ConstantGloble.PREPERIODEXE)) { // 预约周期 提交交易接口取值
					single_reference_cost.setVisibility(View.VISIBLE); 
					single_discount_cost.setVisibility(View.VISIBLE);
					reference_cost.setText(needCommission); // 单笔基准费用 提交交易接口取值
					discount_cost.setText(preCommission); // 单笔优惠后费用 提交交易接口取值
				} else if (excuteWayFlag.equals(ConstantGloble.PREDATEEXE)){ // 预约日期执行 提交交易接口取值
					ll_reference_cost.setVisibility(View.VISIBLE);
					ll_discount_cost.setVisibility(View.VISIBLE);
					reference_cost_tv.setText(needCommission); // 提交交易接口取值
					discount_cost_tv.setText(preCommission); // 试算接口取值
				}else{ // 立即执行 试算接口取值
					ll_reference_cost.setVisibility(View.VISIBLE);
					ll_discount_cost.setVisibility(View.VISIBLE);
					reference_cost_tv.setText(needCommission); // 试算接口取值
					discount_cost_tv.setText(finalCommissionCharge); // 提交交易接口取值
				}
			} else { // 基准费用为空或0或0.00 只显示优惠后费用
				if (excuteWayFlag.equals(ConstantGloble.PREPERIODEXE)) {
					single_discount_cost.setVisibility(View.VISIBLE);
					discount_cost.setText(preCommission);
				} else if (excuteWayFlag.equals(ConstantGloble.PREDATEEXE)){ // 预约日期执行 提交交易接口取值
					ll_discount_cost.setVisibility(View.VISIBLE);
					discount_cost_tv.setText(preCommission); // 提交交易接口取值
				}else{ // 立即执行 试算接口取值
					ll_discount_cost.setVisibility(View.VISIBLE);
					discount_cost_tv.setText(finalCommissionCharge);
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
		// // 手续费试算 end /////
		// /////////////////////////////////////////////////////////////////////////////////
	}

	/** 展示整存整取成功页面*/
	private void dispalyWholeSaveLayout() {
		setTitle(R.string.save_whole);
		if (excuteWayFlag.equals(ConstantGloble.NOWEXE)) {// 立即执行
			// 立即执行页面不显示 转账批次号
			ll_tran_batseq.setVisibility(View.GONE);
			dateLayout.setVisibility(View.GONE);
			startDateLayout.setVisibility(View.GONE);
			endDateLayout.setVisibility(View.GONE);
			weekLayout.setVisibility(View.GONE);
			excuteTimeLayout.setVisibility(View.GONE);
		} else if (excuteWayFlag.equals(ConstantGloble.PREDATEEXE)) {
			titleText.setText(appointStr);
			startDateLayout.setVisibility(View.GONE);
			endDateLayout.setVisibility(View.GONE);
			weekLayout.setVisibility(View.GONE);
			excuteTimeLayout.setVisibility(View.GONE);
			jiaoyiNumLayout.setVisibility(View.GONE);
		} else if (excuteWayFlag.equals(ConstantGloble.PREPERIODEXE)) {
			titleText.setText(appointStr);
			dateLayout.setVisibility(View.GONE);
			excuteTimeLayout.setVisibility(View.VISIBLE);
			jiaoyiNumLayout.setVisibility(View.GONE);
		}
	}

	/** 展示定活两便成功页面*/
	private void dispalyRegRandomLayout() {
		setTitle(R.string.save_random);
		strCurrency = (String) DeptDataCenter.getInstance().getAccInInfoMap().get(Comm.CURRENCYCODE);
		strCashRemit = (String) DeptDataCenter.getInstance().getAccInInfoMap().get(Dept.CASHREMIT);
		if (excuteWayFlag.equals(ConstantGloble.NOWEXE)) {// 立即执行
			cashRemitLayout.setVisibility(View.GONE);
			saveTimeLayout.setVisibility(View.GONE);
			ll_tran_batseq.setVisibility(View.GONE);
			dateLayout.setVisibility(View.GONE);
			startDateLayout.setVisibility(View.GONE);
			endDateLayout.setVisibility(View.GONE);
			weekLayout.setVisibility(View.GONE);
			excuteTimeLayout.setVisibility(View.GONE);
		} else if (excuteWayFlag.equals(ConstantGloble.PREDATEEXE)) {
			titleText.setText(appointStr);
			cashRemitLayout.setVisibility(View.GONE);
			saveTimeLayout.setVisibility(View.GONE);
			startDateLayout.setVisibility(View.GONE);
			endDateLayout.setVisibility(View.GONE);
			weekLayout.setVisibility(View.GONE);
			excuteTimeLayout.setVisibility(View.GONE);
			jiaoyiNumLayout.setVisibility(View.GONE);
		} else if (excuteWayFlag.equals(ConstantGloble.PREPERIODEXE)) {
			titleText.setText(appointStr);
			cashRemitLayout.setVisibility(View.GONE);
			saveTimeLayout.setVisibility(View.GONE);
			dateLayout.setVisibility(View.GONE);
			excuteTimeLayout.setVisibility(View.VISIBLE);
			jiaoyiNumLayout.setVisibility(View.GONE);
		}
	}

	/** 展示通知存款成功页面*/
	private void displayNotifyLayout() {
		strCurrency = (String) DeptDataCenter.getInstance().getAccInInfoMap().get("currencyCode");
		strCashRemit = (String) DeptDataCenter.getInstance().getAccInInfoMap().get("cashRemit");

		promiseWayLayout.setVisibility(View.VISIBLE);
		businessTypeLayout.setVisibility(View.VISIBLE);
		saveTimeLayout.setVisibility(View.GONE);

		if (excuteWayFlag.equals(ConstantGloble.NOWEXE)) {// 立即执行
			dateLayout.setVisibility(View.GONE);
			startDateLayout.setVisibility(View.GONE);
			ll_tran_batseq.setVisibility(View.GONE);
			endDateLayout.setVisibility(View.GONE);
			weekLayout.setVisibility(View.GONE);
			excuteTimeLayout.setVisibility(View.GONE);
		} else if (excuteWayFlag.equals(ConstantGloble.PREDATEEXE)) {
			titleText.setText(appointStr);
			startDateLayout.setVisibility(View.GONE);
			endDateLayout.setVisibility(View.GONE);
			weekLayout.setVisibility(View.GONE);
			excuteTimeLayout.setVisibility(View.GONE);
			jiaoyiNumLayout.setVisibility(View.GONE);
		} else if (excuteWayFlag.equals(ConstantGloble.PREPERIODEXE)) {
			titleText.setText(appointStr);
			dateLayout.setVisibility(View.GONE);
			excuteTimeLayout.setVisibility(View.VISIBLE);
			jiaoyiNumLayout.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ActivityTaskManager.getInstance().removeAllActivity();
			Intent intent = new Intent();
			intent.setClass(SaveSuccessActivity.this, SaveRegularActivity.class);
			startActivity(intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
