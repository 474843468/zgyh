package com.chinamworld.bocmbci.biz.loan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.biz.loan.adapter.LoanHistoryAdapter;
import com.chinamworld.bocmbci.biz.loan.adapter.LoanOverDueAdapter;
import com.chinamworld.bocmbci.biz.loan.adapter.LoanRemainAdapter;
import com.chinamworld.bocmbci.biz.loan.loanApply.LoanApplyMenuActivity;
import com.chinamworld.bocmbci.biz.loan.loanQuery.LoanQueryMenuActivity;
import com.chinamworld.bocmbci.biz.loan.loanRepay.LoanRepayMenuActivity;
import com.chinamworld.bocmbci.biz.loan.loanUse.LoanUseMenuActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

/**
 * 贷款信息查询页
 * 
 * @author wangmengmeng
 * 
 */
public class LoanQueryActivity extends BaseActivity implements
		OnCheckedChangeListener {
	/** 主视图布局 */
	private LinearLayout tabcontent;// 主Activity显示
	/** 贷款账户查询页面 */
	private View view;
	/** 查询切换 */
	private RadioGroup mRadioGroup;
	/** 历史还款 */
	private RadioButton mRadioButton1;
	/** 逾期还款 */
	private RadioButton mRadioButton2;
	/** 剩余还款 */
	private RadioButton mRadioButton3;
	private LinearLayout mViewPager;
	/** 历史查询View */
	private View historyView;
	/** 逾期查询View */
	private View overDueView;
	/** 剩余查询View */
	private View remainView;
	/** 贷款账户信息 */
	private Map<String, Object> loanmap = new HashMap<String, Object>();
	/** 历史还款列表 */
	List<Map<String, Object>> historyList = null;
	/** 输入的要查询的截止日期 */

	/** 日期输入框 */
	private EditText et_enddate;
	/** 历史还款列表 */
	private ListView lvHistory;
	/** 累计逾期未还次数 */
	private TextView tvOverDueIssueSum;
	/** 累计逾期未还总额 */
	private TextView tvOverDueAmountSum;
	/** 累计逾期未还本金 */
	private TextView tvOverDueCapitalSum;
	/** 累计逾期未还利息 */
	private TextView tvOverDueInterestSum;
	/** 逾期未还列表信息 */
	private List<Map<String, Object>> overDueList = null;
	/** 逾期未还列表 */
	private ListView lvOverDue;
	/** 剩余还款列表信息 */
	private List<Map<String, Object>> remainList = null;
	/** 剩余还款列表 */
	private ListView lvRemain;
	/** 查询标志 */
	private int conid = 0;
	/** 系统时间 */
	private String currenttime;
	private RelativeLayout load_more;
	private Button btn_load_more;
	private int recordNumberHistory = 0;
	private int loadNumberHistory = 0;
	private LoanHistoryAdapter historyAdapter;
	private int recordNumberOverDue = 0;
	private int loadNumberOverDue = 0;
	private LoanOverDueAdapter overDueAdapter;
	private int recordNumberRemain = 0;
	private int loadNumberRemain = 0;
	private LoanRemainAdapter remainAdapter;
	private String conversationId;
	/** 查询时间 */
	private String querydate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		// 隐藏左侧展示按钮
		visible();
		view = (View) LayoutInflater.from(this).inflate(R.layout.loan_history,
				null);
		tabcontent.addView(view);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0,
					0, 0);
		}

		// 获取贷款账户信息
		loanmap = LoanDataCenter.getInstance().getLoanmap();
		// 初始化弹窗按钮
		initPulldownBtn();
		// 初始化底部菜单栏
		initFootMenu();
		// 初始化左边菜单
		initLeftSideList(this, LocalData.loanLeftMenuList);
		BaseHttpEngine.showProgressDialogCanGoBack();
		// 请求系统时间
		requestSystemDateTime();

	}
	@Override
	protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {
		super.selectedMenuItemHandler(context, menuItem);
		String menuId = menuItem.MenuID;
		if(menuId.equals("loan_1")){// 贷款管理
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LoanQueryMenuActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
						LoanQueryMenuActivity.class);
				context.startActivity(intent);
			}
		}
		else if(menuId.equals("loan_2")){// 贷款用款
				if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LoanUseMenuActivity)) {
					ActivityTaskManager.getInstance().removeAllActivity();
					Intent intent = new Intent();
					intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
							LoanUseMenuActivity.class);
					context.startActivity(intent);
				}
						
		}
		else if(menuId.equals("loan_3")){// 贷款还款
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LoanRepayMenuActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
						LoanRepayMenuActivity.class);
				context.startActivity(intent);
			}
			
		}
		else if(menuId.equals("loan_4")){// 贷款在线申请
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LoanApplyMenuActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), LoanApplyMenuActivity.class);
				context.startActivity(intent);
			}
			
		}
		return true;
		
		
//		super.setSelectedMenu(clickIndex);
//		switch (clickIndex) {
//		case 0:// 贷款管理
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LoanQueryMenuActivity)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//						LoanQueryMenuActivity.class);
//				startActivity(intent);
//			}
//			break;
//		case 1:// 贷款用款
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LoanUseMenuActivity)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//						LoanUseMenuActivity.class);
//				startActivity(intent);
//			}
//			break;
//		case 2:// 贷款还款
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LoanRepayMenuActivity)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//						LoanRepayMenuActivity.class);
//				startActivity(intent);
//			}
//			break;
//
//		default:
//			break;
//		}
	}
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BiiHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNull(dateTime)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		// 界面初始化
		initPre();

	}

	/** 隐藏左侧展示按钮 */
	private void visible() {
		Button btnhide = (Button) findViewById(R.id.btn_show);
		btnhide.setVisibility(View.GONE);
		Button back = (Button) findViewById(R.id.ib_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	public void initPre() {
		mRadioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
		mRadioButton1 = (RadioButton) view.findViewById(R.id.btn1);
		mRadioButton2 = (RadioButton) view.findViewById(R.id.btn2);
		mRadioButton3 = (RadioButton) view.findViewById(R.id.btn3);
		mViewPager = (LinearLayout) view.findViewById(R.id.pager);
		setTitle(this.getString(R.string.loan_history_title));
		// 初始化监听事件
		iniListener();
		mRadioButton1.setChecked(true);
	}

	/**
	 * RadioGroup点击CheckedChanged监听
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.btn1:
			// 历史还款视图
			recordNumberHistory = 0;
			loadNumberHistory = 0;
			historyView = (View) this.getLayoutInflater().inflate(
					R.layout.loan_history_query_result, null);

			mViewPager.removeAllViews();
			mViewPager.addView(historyView);

			setTitle(this.getString(R.string.loan_history_title));
			inithistory(historyView);
			break;
		case R.id.btn2:
			// 逾期还款视图
			recordNumberOverDue = 0;
			loadNumberOverDue = 0;
			overDueView = (View) getLayoutInflater().inflate(
					R.layout.loan_overdue_message, null);
			mViewPager.removeAllViews();
			mViewPager.addView(overDueView);
			// 初始化逾期还款页面
			initoverdue(overDueView);
			break;
		case R.id.btn3:
			// 剩余还款视图
			recordNumberRemain = 0;
			loadNumberRemain = 0;
			remainView = (View) getLayoutInflater().inflate(
					R.layout.loan_remain_message, null);
			mViewPager.removeAllViews();
			mViewPager.addView(remainView);
			// 初始化剩余还款页面
			initremain(remainView);
			break;
		default:
			break;
		}

	}

	/** 初始化历史还款页面 */
	private void inithistory(View view) {
		setTitle(this.getString(R.string.loan_history_title));
		et_enddate = (EditText) view.findViewById(R.id.et_loandate);
		et_enddate.setInputType(InputType.TYPE_NULL);
		et_enddate.setOnClickListener(chooseDateClick);
		Button btnLoanHistoryQuery = (Button) view
				.findViewById(R.id.btnLoanHistoryQuery);
		btnLoanHistoryQuery.setOnClickListener(loanHistoryQueryClick);
		lvHistory = (ListView) view.findViewById(R.id.lv_loan_history);
		load_more = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.acc_load_more, null);
		btn_load_more = (Button) load_more.findViewById(R.id.btn_load_more);
		btn_load_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestHistoryForMore();
			}
		});

		// 系统当前时间格式化
		currenttime = QueryDateUtils.getcurrentDate(dateTime);
		et_enddate.setText(currenttime);
		conid = 1;
		querydate = currenttime;
		// requestCommConversationId();
		// BaseHttpEngine.showProgressDialog();
	}

	/** 初始化逾期还款页面 */
	private void initoverdue(View view) {
		setTitle(this.getString(R.string.loan_overDue_title));
		tvOverDueIssueSum = (TextView) view
				.findViewById(R.id.loan_overdueIssueSum_value);
		tvOverDueAmountSum = (TextView) view
				.findViewById(R.id.loan_overdueAmountSum_value);
		tvOverDueCapitalSum = (TextView) view
				.findViewById(R.id.loan_overdueCapitalSum_value);
		tvOverDueInterestSum = (TextView) view
				.findViewById(R.id.loan_overdueInterestSum_value);
		lvOverDue = (ListView) view.findViewById(R.id.lv_loan_overdue);
		load_more = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.acc_load_more, null);
		btn_load_more = (Button) load_more.findViewById(R.id.btn_load_more);

		btn_load_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestOverDueForMore();
			}
		});
		conid = 2;
		requestCommConversationId();
		BaseHttpEngine.showProgressDialog();
	}

	/** 初始化剩余还款页面 */
	private void initremain(View view) {
		setTitle(this.getString(R.string.loan_remain_title));
		lvRemain = (ListView) view.findViewById(R.id.lv_loan_remain);
		load_more = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.acc_load_more, null);
		btn_load_more = (Button) load_more.findViewById(R.id.btn_load_more);

		btn_load_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestRemainForMore();
			}
		});
		conid = 3;
		requestCommConversationId();
		BaseHttpEngine.showProgressDialog();

	}

	/** 初始化监听事件 */
	private void iniListener() {
		mRadioGroup.setOnCheckedChangeListener(this);
	}

	/** 查询历史还款信息监听事件 */
	OnClickListener loanHistoryQueryClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (StringUtil.isNullOrEmpty(historyList)) {

			} else {
				historyList.clear();
				historyAdapter.notifyDataSetChanged();
			}
			if (lvHistory.getFooterViewsCount() > 0) {
				lvHistory.removeFooterView(load_more);
			}
			recordNumberHistory = 0;
			loadNumberHistory = 0;
			lvHistory.setVisibility(View.GONE);
			querydate = et_enddate.getText().toString().trim();
			if (QueryDateUtils.compareDate(querydate, dateTime)) {
				// 结束日期在服务器日期之前
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						LoanQueryActivity.this
								.getString(R.string.loan_check_enddate));
				return;
			}
			// if (QueryDateUtils.compareDateOneYear(querydate, dateTime)) {
			// // 起始日期不能早于系统当前日期一年前
			// } else {
			// BaseDroidApp.getInstanse().showInfoMessageDialog(
			// LoanQueryActivity.this
			// .getString(R.string.acc_check_start_enddate));
			// return;
			// }

			conid = 1;
			requestCommConversationId();
			BaseHttpEngine.showProgressDialog();
		}
	};

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		if (conid == 1) {
			conversationId = (String) BaseDroidApp.getInstanse()
					.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
			// 请求历史还款信息
			requestHistory();
		} else if (conid == 2) {
			// 请求逾期还款信息
			requestOverDue();
		} else if (conid == 3) {
			// 请求剩余还款信息
			requestRemain();
		}
	}

	/** 请求历史还款信息 */
	public void requestHistory() {

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_HISTORY_API);
		biiRequestBody.setConversationId(conversationId);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Loan.HISTORY_LOAN_ACTNUM_REQ, String.valueOf(loanmap
				.get(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES)));
		paramsmap.put(Loan.HISTORY_LOAN_ENDDATE_REQ, querydate);
		paramsmap.put(Loan.HISTORY_LOAN_CURRENTINDEX_REQ,
				String.valueOf(ConstantGloble.LOAN_CURRENTINDEX_VALUE));
		paramsmap.put(Loan.HISTORY_LOAN_PAGESIZE_REQ,
				String.valueOf(ConstantGloble.LOAN_PAGESIZE_VALUE));
		paramsmap.put(Loan.HISTORY_LOAN_REFRESH_REQ,
				String.valueOf(ConstantGloble.LOAN_REFRESH_FALSE));
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestHistoryCallBack");
	}

	/**
	 * 请求历史还款信息回调
	 * 
	 * @param resultObj
	 */
	public void requestHistoryCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> historyMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(historyMap)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_transferquery_null));
			return;
		}
		recordNumberHistory = Integer.valueOf((String) historyMap
				.get(Acc.RECORDNUMBER_RES));
		historyList = (List<Map<String, Object>>) (historyMap
				.get(Loan.HISTORY_LOAN_LIST_RES));
		if (historyList == null || historyList.size() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_transferquery_null));
			return;
		}
		if (recordNumberHistory > 10) {
			if (lvHistory.getFooterViewsCount() > 0) {

			} else {
				lvHistory.addFooterView(load_more);
			}

		}
		lvHistory.setVisibility(View.VISIBLE);
		loadNumberHistory = historyList.size();
		historyAdapter = new LoanHistoryAdapter(this, historyList);
		// 历史还款查询信息列表
		lvHistory.setAdapter(historyAdapter);

	}

	/** 请求历史还款信息 */
	public void requestHistoryForMore() {

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_HISTORY_API);
		biiRequestBody.setConversationId(conversationId);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Loan.HISTORY_LOAN_ACTNUM_REQ, String.valueOf(loanmap
				.get(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES)));
		paramsmap.put(Loan.HISTORY_LOAN_ENDDATE_REQ, querydate);
		paramsmap.put(Loan.HISTORY_LOAN_CURRENTINDEX_REQ,
				String.valueOf(loadNumberHistory));
		paramsmap.put(Loan.HISTORY_LOAN_PAGESIZE_REQ,
				String.valueOf(ConstantGloble.LOAN_PAGESIZE_VALUE));
		paramsmap.put(Loan.HISTORY_LOAN_REFRESH_REQ,
				String.valueOf(ConstantGloble.REFRESH_FOR_MORE));
		biiRequestBody.setParams(paramsmap);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestHistoryForMoreCallBack");
	}

	/**
	 * 请求历史还款信息回调
	 * 
	 * @param resultObj
	 */
	public void requestHistoryForMoreCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> historyMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(historyMap)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_transferquery_null));
			return;
		}
		recordNumberHistory = Integer.valueOf((String) historyMap
				.get(Acc.RECORDNUMBER_RES));
		List<Map<String, Object>> historyListForMore = (List<Map<String, Object>>) (historyMap
				.get(Loan.HISTORY_LOAN_LIST_RES));
		if (historyList == null || historyList.size() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_transferquery_null));
			return;
		}
		for (Map<String, Object> map : historyListForMore) {
			loadNumberHistory++;
			historyList.add(map);
		}
		if (loadNumberHistory < recordNumberHistory) {

		} else {
			lvHistory.removeFooterView(load_more);
		}
		// 历史还款查询信息列表
		historyAdapter.notifyDataSetChanged();

	}

	/** 设置查询日期 */
	OnClickListener chooseDateClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			EditText tv = (EditText) v;
			String time = tv.getText().toString();
			int startYear = Integer.parseInt(time.substring(0, 4));
			int startMonth = Integer.parseInt(time.substring(5, 7));
			int startDay = Integer.parseInt(time.substring(8, 10));
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			DatePickerDialog dialog = new DatePickerDialog(
					LoanQueryActivity.this, new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							StringBuilder date = new StringBuilder();
							date.append(String.valueOf(year));
							date.append("/");
							int month = monthOfYear + 1;
							date.append(((month < 10) ? ("0" + month)
									: (month + "")));
							date.append("/");
							date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth)
									: (dayOfMonth + "")));
							// 为日期赋值
							// 为截止日期EditText赋值
							et_enddate.setText(date);
							et_enddate.setSelection(date.length());
							currenttime = et_enddate.getText().toString();
						}
					}, startYear, startMonth - 1, startDay);
			dialog.show();
		}
	};

	/** 请求逾期还款信息 */
	public void requestOverDue() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_OVERDUE_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Loan.OVERDUE_LOAN_ACTNUM_REQ, String.valueOf(loanmap
				.get(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES)));
		paramsmap.put(Loan.OVERDUE_LOAN_CURRENTINDEX_REQ,
				String.valueOf(ConstantGloble.LOAN_CURRENTINDEX_VALUE));
		paramsmap.put(Loan.OVERDUE_LOAN_PAGESIZE_REQ,
				String.valueOf(ConstantGloble.LOAN_PAGESIZE_VALUE));
		paramsmap.put(Loan.OVERDUE_LOAN_REFRESH_REQ,
				String.valueOf(ConstantGloble.LOAN_REFRESH_FALSE));
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestOverDueCallBack");
	}

	/**
	 * 请求逾期还款信息
	 * 
	 * @param resultObj
	 */
	public void requestOverDueCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_transferquery_null));
			return;
		}
		mRadioButton2.setChecked(true);
		Map<String, Object> overduemap = (Map<String, Object>) resultMap
				.get(Loan.OVERDUE_LOAN_OVERDUELOANOBJ_RES);
		if (StringUtil.isNullOrEmpty(overduemap)) {

		} else {
			/** 累计逾期未还次数 */
			String overdueIssueSum = (String) overduemap
					.get(Loan.OVERDUE_LOAN_OVERDUEISSUESUM_RES);
			tvOverDueIssueSum
					.setText((StringUtil.isNullOrEmpty(overdueIssueSum)) ? ConstantGloble.BOCINVT_DATE_ADD
							: overdueIssueSum);
			/** 累计逾期未还总额 */

			String overdueAmountSum = (String) overduemap
					.get(Loan.OVERDUE_LOAN_OVERDUEAMOUNTSUM_RES);
			tvOverDueAmountSum.setText(StringUtil.parseStringPattern(
					overdueAmountSum, 2));
			/** 累计逾期未还本金 */

			String overdueCapitalSum = String.valueOf(overduemap
					.get(Loan.OVERDUE_LOAN_OVERDUECAPITALSUM_RES));
			tvOverDueCapitalSum.setText(StringUtil.parseStringPattern(
					overdueCapitalSum, 2));
			/** 累计逾期未还利息 */

			String overdueInterestSum = (String) overduemap
					.get(Loan.OVERDUE_LOAN_OVERDUEINTERESTSUM_RES);
			tvOverDueInterestSum.setText(StringUtil.parseStringPattern(
					overdueInterestSum, 2));
		}

		recordNumberOverDue = Integer.valueOf((String) resultMap
				.get(Acc.RECORDNUMBER_RES));

		overDueList = (List<Map<String, Object>>) (resultMap
				.get(Loan.HISTORY_LOAN_LIST_RES));
		loadNumberOverDue = overDueList.size();
		if (overDueList == null || overDueList.size() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_transferquery_null));
			return;
		}
		if (recordNumberOverDue > 10) {
			lvOverDue.addFooterView(load_more);
		}

		overDueAdapter = new LoanOverDueAdapter(this, overDueList);
		lvOverDue.setAdapter(overDueAdapter);
	}

	/** 请求逾期还款信息 */
	public void requestOverDueForMore() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_OVERDUE_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Loan.OVERDUE_LOAN_ACTNUM_REQ, String.valueOf(loanmap
				.get(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES)));
		paramsmap.put(Loan.OVERDUE_LOAN_CURRENTINDEX_REQ,
				String.valueOf(loadNumberOverDue));
		paramsmap.put(Loan.OVERDUE_LOAN_PAGESIZE_REQ,
				String.valueOf(ConstantGloble.LOAN_PAGESIZE_VALUE));
		paramsmap.put(Loan.OVERDUE_LOAN_REFRESH_REQ,
				String.valueOf(ConstantGloble.REFRESH_FOR_MORE));
		biiRequestBody.setParams(paramsmap);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestOverDueForMoreCallBack");
	}

	/**
	 * 请求逾期还款信息
	 * 
	 * @param resultObj
	 */
	public void requestOverDueForMoreCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_transferquery_null));
			return;
		}
		mRadioButton2.setChecked(true);
		Map<String, Object> overduemap = (Map<String, Object>) resultMap
				.get(Loan.OVERDUE_LOAN_OVERDUELOANOBJ_RES);
		if (StringUtil.isNullOrEmpty(overduemap)) {

		} else {
			/** 累计逾期未还次数 */
			String overdueIssueSum = (String) overduemap
					.get(Loan.OVERDUE_LOAN_OVERDUEISSUESUM_RES);
			tvOverDueIssueSum
					.setText((StringUtil.isNullOrEmpty(overdueIssueSum)) ? ConstantGloble.BOCINVT_DATE_ADD
							: overdueIssueSum);
			/** 累计逾期未还总额 */

			String overdueAmountSum = (String) overduemap
					.get(Loan.OVERDUE_LOAN_OVERDUEAMOUNTSUM_RES);
			tvOverDueAmountSum.setText(StringUtil.parseStringPattern(
					overdueAmountSum, 2));
			/** 累计逾期未还本金 */

			String overdueCapitalSum = String.valueOf(overduemap
					.get(Loan.OVERDUE_LOAN_OVERDUECAPITALSUM_RES));
			tvOverDueCapitalSum.setText(StringUtil.parseStringPattern(
					overdueCapitalSum, 2));
			/** 累计逾期未还利息 */

			String overdueInterestSum = (String) overduemap
					.get(Loan.OVERDUE_LOAN_OVERDUEINTERESTSUM_RES);
			tvOverDueInterestSum.setText(StringUtil.parseStringPattern(
					overdueInterestSum, 2));
		}
		recordNumberOverDue = Integer.valueOf((String) resultMap
				.get(Acc.RECORDNUMBER_RES));
		List<Map<String, Object>> overDueListForMore = (List<Map<String, Object>>) (resultMap
				.get(Loan.HISTORY_LOAN_LIST_RES));
		if (overDueList == null || overDueList.size() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_transferquery_null));
			return;
		}
		for (Map<String, Object> map : overDueListForMore) {
			loadNumberOverDue++;
			overDueList.add(map);
		}
		if (loadNumberOverDue < recordNumberOverDue) {

		} else {
			lvOverDue.removeFooterView(load_more);
		}

		overDueAdapter.notifyDataSetChanged();
	}

	/** 请求剩余还款信息 */
	public void requestRemain() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_REMAIN_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Loan.REMAIN_LOAN_ACTNUM_REQ, String.valueOf(loanmap
				.get(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES)));
		paramsmap.put(Loan.REMAIN_LOAN_CURRENTINDEX_REQ,
				String.valueOf(ConstantGloble.LOAN_CURRENTINDEX_VALUE));
		paramsmap.put(Loan.REMAIN_LOAN_PAGESIZE_REQ,
				String.valueOf(ConstantGloble.LOAN_PAGESIZE_VALUE));
		paramsmap.put(Loan.REMAIN_LOAN_REFRESH_REQ,
				String.valueOf(ConstantGloble.LOAN_REFRESH_FALSE));
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestRemainCallBack");
	}

	/**
	 * 请求剩余还款信息
	 * 
	 * @param resultObj
	 */
	public void requestRemainCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> remainMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(remainMap)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_transferquery_null));
			return;
		}
		mRadioButton3.setChecked(true);
		recordNumberRemain = Integer.valueOf((String) remainMap
				.get(Acc.RECORDNUMBER_RES));
		remainList = (List<Map<String, Object>>) (remainMap
				.get(Loan.HISTORY_LOAN_LIST_RES));

		loadNumberRemain = remainList.size();
		if (remainList == null || remainList.size() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_transferquery_null));
			return;
		}
		if (recordNumberRemain > 10) {
			lvRemain.addFooterView(load_more);
		}
		remainAdapter = new LoanRemainAdapter(this, remainList);
		lvRemain.setAdapter(remainAdapter);
	}

	/** 请求剩余还款信息 */
	public void requestRemainForMore() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_REMAIN_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Loan.REMAIN_LOAN_ACTNUM_REQ, String.valueOf(loanmap
				.get(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES)));
		paramsmap.put(Loan.REMAIN_LOAN_CURRENTINDEX_REQ,
				String.valueOf(loadNumberRemain));
		paramsmap.put(Loan.REMAIN_LOAN_PAGESIZE_REQ,
				String.valueOf(ConstantGloble.LOAN_PAGESIZE_VALUE));
		paramsmap.put(Loan.REMAIN_LOAN_REFRESH_REQ,
				String.valueOf(ConstantGloble.REFRESH_FOR_MORE));
		biiRequestBody.setParams(paramsmap);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestRemainForMoreCallBack");
	}

	/**
	 * 请求剩余还款信息
	 * 
	 * @param resultObj
	 */
	public void requestRemainForMoreCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> remainMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(remainMap)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_transferquery_null));
			return;
		}
		mRadioButton3.setChecked(true);
		recordNumberRemain = Integer.valueOf((String) remainMap
				.get(Acc.RECORDNUMBER_RES));
		List<Map<String, Object>> remainListForMore = (List<Map<String, Object>>) (remainMap
				.get(Loan.HISTORY_LOAN_LIST_RES));
		if (remainList == null || remainList.size() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_transferquery_null));
			return;
		}
		for (Map<String, Object> map : remainListForMore) {
			loadNumberRemain++;
			remainList.add(map);
		}
		if (loadNumberRemain < recordNumberRemain) {

		} else {
			lvRemain.removeFooterView(load_more);
		}

		remainAdapter.notifyDataSetChanged();
	}

	/** 退出项情况监听事件 */
	OnClickListener exitDetailClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
		}
	};
	/** 进入提前还款试算页 */
	OnClickListener goAdvanceClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
			Intent intent = new Intent(LoanQueryActivity.this,
					LoanAdvanceCountActivity.class);
			startActivity(intent);

		}
	};

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	}

}