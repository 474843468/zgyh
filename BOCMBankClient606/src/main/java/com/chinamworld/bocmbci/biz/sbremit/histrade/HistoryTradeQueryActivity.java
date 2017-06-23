package com.chinamworld.bocmbci.biz.sbremit.histrade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.SBRemit;
import com.chinamworld.bocmbci.biz.sbremit.SBRemitBaseActivity;
import com.chinamworld.bocmbci.biz.sbremit.SBRemitDataCenter;
import com.chinamworld.bocmbci.biz.sbremit.histrade.adapter.HistoyTradeListAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.QueryDateUtils;

public class HistoryTradeQueryActivity extends SBRemitBaseActivity implements
		OnClickListener, OnItemClickListener {

	/** 查询下拉按钮 */
	private LinearLayout prms_down_LinearLayout;
	/** 最近一次的查询时间 */
	private TextView sbremit_query_result_time;
	/** 条件查询框 */
	private View sbremit_query_condition;
	/** 条件查询 开始日期 */
	private TextView sbremit_query_deal_startdate;
	/** 条件查询 结束日期 */
	private TextView sbremit_query_deal_enddate;
	/** 条件查询 查询一周 */
	private Button btn_acc_onweek;
	/** 条件查询 查询一月 */
	private Button btn_acc_onmonth;
	/** 条件查询 查询三月 */
	private Button btn_acc_onthreemonth;
	/** 条件查询 查询按钮 */
	private Button sbremit_query_deal_query;
	/** 条件查询 收起按钮 */
	private LinearLayout sbremit_query_up;
	/** 历史交易列表 */
	private ListView sbremit_querydeal_listview;
	/** 查询更多，交易列表的Footer */
	private View list_foooter;
	/** 查询更多，显示更多控件 */
	private TextView tv_get_more;
	/** 查询更多，Loading框 */
	private ProgressBar progressBar;
	/** 当前页索引 */
	private int curPageIndex = 0;
	/** 页面大小 */
	private final int pageSize = 10;
	/** 查询条件 起始时间 */
	private String startDate;
	/** 查询条件 终止时间 */
	private String endDate;
	/** 控件初始化起始时间 */
	private String startDateEt;
	/** 控件初始化终止时间 */
	private String endDateEt;
	/** 交易历史列表 */
	private List<Map<String, Object>> tradeList;
	/** 交易历史列表 */
	private List<Map<String, Object>> tradeListTotal;
	/** 访问历史交易 返回的结果对象 */
	private Map<String, Object> resultMap;
	/** 访问历史交易详情 */
	private Map<String, Object> tradeDetail;
	/** 历史交易总记录数 */
	private int totalCount;
	/** 交易历史列表适配器 */
	private HistoyTradeListAdapter hisTradeAdapter;
	/** 显示标志，是否第一次显示 */
	private boolean isfirst = true;
	private boolean isMore = false;
	// 查询交易信息列表最后一个字段。开始为true点击更多为false
	private String isMore_new = "true";
	private View view;
	// 是否交易成功
	private String status;
	/** 查询界面 */
	private LinearLayout search_shouqi_up;
	/** 查询后的界面 */
	private LinearLayout query_result_condition_layout;
	/** 列表的layout */
	private RelativeLayout sbremit_querydeal_listview_layout;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BaseHttpEngine.showProgressDialogCanGoBack();
		requestSystemDateTime();// 先请求系统时间
		setTitle(this.getString(R.string.history_trade_query));// 为界面标题赋值
		setLeftSelectedPosition("sbRemit_2");
		view = addView(R.layout.sbremit_query_history_main_new);// 添加布局
		// view.setVisibility(View.GONE);
		search_shouqi_up = (LinearLayout)view. findViewById(R.id.search_shouqi_up);
		query_result_condition_layout = (LinearLayout)view.findViewById(R.id.query_result_condition_layout);
		sbremit_querydeal_listview_layout = (RelativeLayout)view. findViewById(R.id.sbremit_querydeal_listview_layout);

		btn_right.setVisibility(View.INVISIBLE);// 隐藏顶部右边按钮
		back.setOnClickListener(this);
//		ActivityTaskManager.getInstance().addActivit(this);
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		startDate = QueryDateUtils.getlastthreeDate(dateTime);
		endDate = QueryDateUtils.getcurrentDate(dateTime);
		startDateEt = startDate;
		endDateEt = endDate;
		initViews();
	}

	private void initViews() {
		// 初始化条件查询窗体
		initQueryWindow();
		// PopupWindowUtils.getInstance().showQueryPopupWindowFirst();
		// 查询前界面显示，查询后界面不现实，列表不现实
		search_shouqi_up.setVisibility(View.VISIBLE);
		query_result_condition_layout.setVisibility(View.GONE);
		sbremit_querydeal_listview_layout.setVisibility(View.GONE);
		// 实例化主页面控件
		sbremit_query_result_time = (TextView) findViewById(R.id.sbremit_query_result_time);
		prms_down_LinearLayout = (LinearLayout) findViewById(R.id.prms_down_LinearLayout);
		prms_down_LinearLayout.setOnClickListener(this);
		sbremit_querydeal_listview = (ListView) findViewById(R.id.sbremit_querydeal_listview);
		sbremit_querydeal_listview.setOnItemClickListener(this);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0,
					0, 0);
		}
		// listFooter实例化
		list_foooter = LayoutInflater.from(this).inflate(
				R.layout.epay_tq_list_more, null);
		((RelativeLayout) list_foooter.findViewById(R.id.rl_get_more))
				.setBackgroundColor(getResources().getColor(
						R.color.transparent_00));
		tv_get_more = (TextView) list_foooter.findViewById(R.id.tv_get_more);
		progressBar = (ProgressBar) list_foooter.findViewById(R.id.progressBar);

		// queryHisTrade();
	}

	/**
	 * 初始化查询框
	 */
	private void initQueryWindow() {
		// sbremit_query_condition = LayoutInflater.from(this).inflate(
		// R.layout.sbremit_query_condition, null);
		// PopupWindowUtils.getInstance().getQueryPopupWindow(
		// sbremit_query_condition,
		// BaseDroidApp.getInstanse().getCurrentAct());

		// 实例化条件框控件
		sbremit_query_deal_startdate = (TextView) findViewById(R.id.sbremit_query_deal_startdate);
		sbremit_query_deal_startdate.setOnClickListener(this);
		sbremit_query_deal_enddate = (TextView) findViewById(R.id.sbremit_query_deal_enddate);
		sbremit_query_deal_enddate.setOnClickListener(this);
		// 查询按钮
		btn_acc_onweek = (Button) findViewById(R.id.btn_acc_onweek);
		btn_acc_onmonth = (Button) findViewById(R.id.btn_acc_onmonth);
		btn_acc_onthreemonth = (Button) findViewById(R.id.btn_acc_threemonth);
		sbremit_query_deal_query = (Button) findViewById(R.id.sbremit_query_deal_query);
		btn_acc_onweek.setOnClickListener(this);
		btn_acc_onmonth.setOnClickListener(this);
		btn_acc_onthreemonth.setOnClickListener(this);
		sbremit_query_deal_query.setOnClickListener(this);

		sbremit_query_up = (LinearLayout) findViewById(R.id.up);
		sbremit_query_up.setOnClickListener(this);
		sbremit_query_deal_startdate.setText(startDateEt);
		sbremit_query_deal_enddate.setText(endDateEt);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
			ActivityTaskManager.getInstance().removeAllActivity();
			finish();
			break;
		case R.id.sbremit_query_deal_startdate:// 显示起始日期选择器
			showTime(sbremit_query_deal_startdate);
			break;
		case R.id.sbremit_query_deal_enddate:// 显示结束日期选择器
			showTime(sbremit_query_deal_enddate);
			break;
		case R.id.prms_down_LinearLayout:// 弹出查询框
			LogGloble.d("fsm", "弹出查询框");
			checkCondition();
			// initQueryWindow();
			// PopupWindowUtils.getInstance().showQueryPopupWindow();
			break;
		case R.id.up:// 隐藏查询框
			LogGloble.d("fsm", "隐藏查询框");
			if (view.isShown())
				// PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
				if (isfirst == false) {
					checkCondition();
				}

			break;
		case R.id.sbremit_query_deal_query:// 查询
			initQuery();
			startDate = sbremit_query_deal_startdate.getText().toString();
			endDate = sbremit_query_deal_enddate.getText().toString();
			startDateEt = startDate;
			endDateEt = endDate;
			if(queryChecks(startDate, endDate)){
				queryHisTrade();
			}
			break;
		case R.id.btn_acc_onweek:// 查询最近一周
			initQuery();
			startDate = QueryDateUtils.getlastWeek(dateTime);
			endDate = QueryDateUtils.getcurrentDate(dateTime);
			startDateEt = sbremit_query_deal_startdate.getText().toString();
			endDateEt = sbremit_query_deal_enddate.getText().toString();
//			checkCondition();
			if(queryChecks(startDate, endDate)){
				queryHisTrade();
			}
			break;
		case R.id.btn_acc_onmonth:// 查询最近一月
			initQuery();
			startDate = QueryDateUtils.getlastOneMonth(dateTime);
			endDate = QueryDateUtils.getcurrentDate(dateTime);
			startDateEt = sbremit_query_deal_startdate.getText().toString();
			endDateEt = sbremit_query_deal_enddate.getText().toString();
//			checkCondition();
			if(queryChecks(startDate, endDate)){
				queryHisTrade();
			}
			break;
		case R.id.btn_acc_threemonth:// 查询最近三月
			initQuery();
			startDate = QueryDateUtils.getlastThreeMonth(dateTime);
			endDate = QueryDateUtils.getcurrentDate(dateTime);
			startDateEt = sbremit_query_deal_startdate.getText().toString();
			endDateEt = sbremit_query_deal_enddate.getText().toString();
//			checkCondition();
			if(queryChecks(startDate, endDate)){
				queryHisTrade();
			}
			
			break;
		default:
			break;
		}
	}

	/**
	 * 初始化查询，初始页索引置0，查询结果列表清空
	 */
	private void initQuery() {
		if (tradeListTotal != null)
			tradeListTotal.clear();
		if (tradeList != null)
			tradeList.clear();
		isMore = false;
		if (!isMore) {
			curPageIndex = 0;// 重新查询，初始化索引页
//			view.setVisibility(View.INVISIBLE);
			hisTradeAdapter = null;
		}
	}

	/**
	 * 显示日期选择器
	 * 
	 * @param v
	 *            显示时间的TextView
	 */
	private void showTime(final View v) {
		String dateTime = ((TextView) v).getText().toString();
		int startYear = Integer.parseInt(dateTime.substring(0, 4));
		int startMonth = Integer.parseInt(dateTime.substring(5, 7));
		int startDay = Integer.parseInt(dateTime.substring(8, 10));
		// 第二个参数为用户选择设置按钮后的响应事件
		// 最后的三个参数为缺省显示的年度，月份，及日期信息
		DatePickerDialog dialog = new DatePickerDialog(this,
				new OnDateSetListener() {

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
						((TextView) v).setText(String.valueOf(date));
					}
				}, startYear, startMonth - 1, startDay);
		dialog.show();
	}

	/**
	 * 查询历史记录，先校验时间
	 */
	private void queryHisTrade() {
		// 校验日期
//		if (!QueryDateUtils.commQueryStartAndEndateReg(this, startDate,
//				endDate, dateTime))
//			return;
		// BaseHttpEngine.showProgressDialogCanGoBack();
		// tv_get_more.setVisibility(View.INVISIBLE);
		// progressBar.setVisibility(View.VISIBLE);
		BaseHttpEngine.showProgressDialog();
		requestSbremitCommConversationId();

	}

	/**
	 * @Title: requestForHisTrades
	 * @Description: 请求历史交易记录
	 * @param
	 * @return void
	 */
	public void requestForHisTrades() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody
				.setConversationId(BaseDroidApp.getInstanse().getBizDataMap()
						.get(ConstantGloble.CONVERSATION_ID).toString());
		biiRequestBody.setMethod(SBRemit.SBREMIT_TRADE_INFO_NEW);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(SBRemit.FESSFLAG, "00");
		paramsmap.put(SBRemit.START_DATE, startDate);
		paramsmap.put(SBRemit.END_DATE, endDate);
		paramsmap.put(SBRemit.CURRENT_INDEX, curPageIndex + "");
		paramsmap.put(SBRemit.PAGE_SIZE, pageSize + "");
		paramsmap.put(SBRemit.REFRESH, isMore_new);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForHisTradesCallBack");
	}

	/**
	 * @Title: requestForHisTradesCallBack
	 * @Description: 请求历史交易记录
	 * @param @param resultObj
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void requestForHisTradesCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		resultMap = (Map<String, Object>) biiResponseBody.getResult();
		LogGloble.e("asd", "resultMap" + resultMap);
		tradeList = (List<Map<String, Object>>) resultMap
				.get(SBRemit.FESS_EXCHANGE_TRANS_LIST);
		totalCount = Integer.parseInt(resultMap.get(SBRemit.RECORD_TOTAL_COUNT)
				.toString());
		// if(isMore)
		if (tradeList.isEmpty()) {
			search_shouqi_up.setVisibility(View.VISIBLE);
			query_result_condition_layout.setVisibility(View.GONE);
			sbremit_querydeal_listview_layout.setVisibility(View.GONE);
			sbremit_query_up.setClickable(false);
		}
		checkCondition();

		if (isfirst) {
			sbremit_querydeal_listview_layout.setVisibility(View.GONE);
		} else {
			sbremit_querydeal_listview_layout.setVisibility(View.VISIBLE);
		}
		curPageIndex = curPageIndex + pageSize;
		communicationCallBack(QUERY_HISTORY_TRADE);
	}

	@Override
	public void communicationCallBack(int flag) {
		switch (flag) {
		case QUERY_HISTORY_TRADE:
			if (tradeListTotal == null) {
				tradeListTotal = tradeList;
			} else {
				tradeListTotal.addAll(tradeList);
			}
			sbremit_query_result_time.setText(startDate + "-" + endDate);
			if ((!isMore && (tradeList != null && tradeList.size() > 0))
					|| isMore) {
				// PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
				 view.setVisibility(View.VISIBLE);
				sbremit_querydeal_listview_layout.setVisibility(View.VISIBLE);
				setListView(tradeListTotal);
			} else {
				sbremit_querydeal_listview_layout.setVisibility(View.GONE);
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getString(R.string.acc_transferquery_null));
			}
			break;
		case QUERY_HISTORY_TRADE_DETAIL:
			if (tradeDetail != null) {
				try {
					ensureNext();
				} catch (Exception e) {
					LogGloble.exceptionPrint(e);
				}
			}
		default:
			break;
		}
	}

	/**
	 * @Title: setListView
	 * @Description: 填充视图的数据
	 * @param
	 * @return void
	 */
	private void setListView(List<Map<String, Object>> tradeList) {
		addFooterView();
		if (hisTradeAdapter == null) {
			initListHead();// 有历史交易数据，添加列表表头
			hisTradeAdapter = new HistoyTradeListAdapter(this, tradeList);
			sbremit_querydeal_listview.setAdapter(hisTradeAdapter);
		} else {
			hisTradeAdapter.setAccountList(tradeList);
		}

	}

	/**
	 * 初始化列表头信息
	 */
	private void initListHead() {
		((TextView) findViewById(R.id.list_header_tv1))
				.setText(getString(R.string.sbremit_listheader_tv1));
		((TextView) findViewById(R.id.list_header_tv2))
				.setText(getString(R.string.sbremit_listheader_tv2));
		((TextView) findViewById(R.id.list_header_tv3))
				.setText(getString(R.string.sbremit_listheader_tv3));
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		switch (arg0.getId()) {
		case R.id.sbremit_querydeal_listview:
			/** 获取历史交易的详细信息，传递给详细页面 */
			TextView tv1 = (TextView) arg1.findViewById(R.id.list_header_tv1);
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) tv1.getTag();
			String transId = (String) map.get(SBRemit.BANKSELFNUM);
			String paymentDate = (String) map.get(SBRemit.PAYMENTDATE);
			String refNum = (String) map.get(SBRemit.REFNUM);

			SBRemitDataCenter.getInstance().setTradeListTotal(
					tradeListTotal.get(arg2));

			if (Integer.valueOf((String) tradeListTotal.get(arg2).get(
					SBRemit.STATUS)) == 0) {
				requestForHisTradesDetail(transId, paymentDate, refNum);
			} else {
				tradeDetail = map;
				ensureNext();
			}

			break;
		default:
			break;
		}
	}

	/**
	 * @Title: requestForHisTradesDetail
	 * @Description: 请求历史交易详情
	 * @param
	 * @return void
	 */
	public void requestForHisTradesDetail(String transId, String paymentDate,
			String refNum) {
		BaseHttpEngine.showProgressDialogCanGoBack();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(SBRemit.SBREMIT_TRADE_DETAIL_NEW);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(SBRemit.BANKSELFNUM, transId);
		paramsmap.put(SBRemit.PAYMENTDATE, paymentDate);
		paramsmap.put(SBRemit.REFNUM, refNum);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForHisTradesDetailCallBack");
	}

	/**
	 * @Title: requestForHisTradesDetailCallBack
	 * @Description: 请求历史交易详情回调
	 * @param @param resultObj
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void requestForHisTradesDetailCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		tradeDetail = (Map<String, Object>) biiResponseBody.getResult();
		communicationCallBack(QUERY_HISTORY_TRADE_DETAIL);
	}

	/**
	 * 跳至详情页
	 */
	private void ensureNext() {
		Intent intent = new Intent();
		// @SuppressWarnings("unchecked")
		// Map<String, Object> map = (Map<String, Object>) tradeDetail
		// .get(SBRemit.FUND_ACCOUNT);
		SBRemitDataCenter.getInstance().setInfoDetail(tradeDetail);
		SBRemitDataCenter
				.getInstance()
				.getInfoDetail()
				.put(SBRemit.ACCOUNT_NUMBER,
						isObjNull(tradeDetail.get(SBRemit.ACCOUNT_NUMBER)));
		intent.setClass(this, HistoryTradeDetailActivity.class);
		startActivity(intent);
	}

	/**
	 * 为ListView添加加载更多
	 */
	private void addFooterView() {
		int curRecordCount = tradeList.size() + (curPageIndex - pageSize);// 计算当前页最近一条记录索引
		if (curRecordCount < totalCount && curRecordCount > 0) {// 如果当前页不是最后一页则添加加载更多
			if (sbremit_querydeal_listview.getFooterViewsCount() <= 0) {
				sbremit_querydeal_listview.addFooterView(list_foooter);
			}
			list_foooter.setClickable(true);
			// tv_get_more.setVisibility(View.VISIBLE);
			// progressBar.setVisibility(View.INVISIBLE);
		} else {
			if (sbremit_querydeal_listview.getFooterViewsCount() > 0) {
				sbremit_querydeal_listview.removeFooterView(list_foooter);
			}

		}
		list_foooter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isMore = true;
				isMore_new = "false";
				queryHisTrade();
			}
		});
	}

	/**
	 * 请求CommconversationId返回
	 * 
	 * @param resultObj
	 */
	public void requestSbremitCommConversationIdCallBack(Object resultObj) {
		// BiiResponse biiResponse = (BiiResponse) resultObj;
		// List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// String commConversationId = (String) biiResponseBody.getResult();
		// BaseDroidApp.getInstanse().getBizDataMap()
		// .put(ConstantGloble.CONVERSATION_ID, commConversationId);
		// communicationCallBack(RESULT_OK);
		super.requestSbremitCommConversationIdCallBack(resultObj);
		requestForHisTrades();
	}

	public void checkCondition() {
		view.findViewById(R.id.sbremit_querydeal_listview_layout).setVisibility(View.VISIBLE);
		if (query_result_condition_layout.isShown()) {
			query_result_condition_layout.setVisibility(View.GONE);
			search_shouqi_up.setVisibility(View.VISIBLE);
		} else {
			query_result_condition_layout.setVisibility(View.VISIBLE);
			search_shouqi_up.setVisibility(View.GONE);
		}
		isfirst = false;
	}
	/**
	 * 查询日期校验
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public boolean queryChecks(String startDate,String endDate){
		// 起始日期不能早于系统当前日期一年前
		if (!QueryDateUtils.compareDateOneYear(startDate, dateTime)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_check_start_enddate));
			return false;
		}
		// 结束日期在服务器日期之前
		if (!QueryDateUtils.compareDate(endDate, dateTime)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_check_enddate));
			return false;
		}
		// 开始日期在结束日期之前
		if (!QueryDateUtils.compareDate(startDate, endDate)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_query_errordate));
			return false;
		}
		// 起始日期与结束日期最大间隔为三个自然月
//		if (!QueryDateUtils.compareDateThree(startDate, endDate)) {
//			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_check_start_end_date));
//			return false;
//		}
		return true;
	}

}
