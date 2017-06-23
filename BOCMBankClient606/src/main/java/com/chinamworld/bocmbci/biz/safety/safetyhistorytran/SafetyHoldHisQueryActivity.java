package com.chinamworld.bocmbci.biz.safety.safetyhistorytran;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.SBRemit;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyBaseActivity;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.adapter.SafetyHistoryListAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 历史交易查询
 * 
 * @author fsm
 *
 */
public class SafetyHoldHisQueryActivity extends SafetyBaseActivity implements OnClickListener, OnItemClickListener {

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
	private ListView listview;
	/** 查询更多，交易列表的Footer */
	private View list_foooter;
	/** 当前页索引 */
	private int curPageIndex = 1;
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
	/** 历史交易总记录数 */
	private int totalCount;
	/** 交易历史列表适配器 */
	private SafetyHistoryListAdapter hisTradeAdapter;
	private boolean isMore = false;
	private View view;
	private final int QUERY_HISTORY_TRADE = 11;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BaseHttpEngine.showProgressDialogCanGoBack();
		requestSystemDateTime();// 先请求系统时间
		setTitle(this.getString(R.string.history_trade_query));// 为界面标题赋值
		view = addView(R.layout.sbremit_query_history_main);// 添加布局
		initLeftSideList(this, LocalData.safetyLeftListData);
		setRightTopGone();
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
//		PopupWindowUtils.getInstance().showQueryPopupWindowFirst();
		// 实例化主页面控件
		sbremit_query_result_time = (TextView) findViewById(R.id.sbremit_query_result_time);
		prms_down_LinearLayout = (LinearLayout) findViewById(R.id.prms_down_LinearLayout);
		prms_down_LinearLayout.setOnClickListener(this);
		listview = (ListView) findViewById(R.id.sbremit_querydeal_listview);
		listview.setOnItemClickListener(this);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 0);
		}
		// listFooter实例化
		list_foooter = LayoutInflater.from(this).inflate(R.layout.epay_tq_list_more, null);
		((TextView) list_foooter.findViewById(R.id.tv_get_more)).setText("更多...");
		((RelativeLayout) list_foooter.findViewById(R.id.rl_get_more)).setBackgroundColor(getResources().getColor(R.color.transparent_00));
		// queryHisTrade();
		initQueryView();
	}

	/**
	 * 初始化查询框
	 */
	private void initQueryWindow() {
		sbremit_query_condition = view.findViewById(R.id.layout_queryView);
//		PopupWindowUtils.getInstance().getQueryPopupWindow(sbremit_query_condition, BaseDroidApp.getInstanse().getCurrentAct());
		// 实例化条件框控件
		sbremit_query_deal_startdate = (TextView) sbremit_query_condition.findViewById(R.id.sbremit_query_deal_startdate);
		sbremit_query_deal_startdate.setOnClickListener(this);
		sbremit_query_deal_enddate = (TextView) sbremit_query_condition.findViewById(R.id.sbremit_query_deal_enddate);
		sbremit_query_deal_enddate.setOnClickListener(this);
		// 查询按钮
		btn_acc_onweek = (Button) sbremit_query_condition.findViewById(R.id.btn_acc_onweek);
		btn_acc_onmonth = (Button) sbremit_query_condition.findViewById(R.id.btn_acc_onmonth);
		btn_acc_onthreemonth = (Button) sbremit_query_condition.findViewById(R.id.btn_acc_threemonth);
		sbremit_query_deal_query = (Button) sbremit_query_condition.findViewById(R.id.sbremit_query_deal_query);
		btn_acc_onweek.setOnClickListener(this);
		btn_acc_onmonth.setOnClickListener(this);
		btn_acc_onthreemonth.setOnClickListener(this);
		sbremit_query_deal_query.setOnClickListener(this);

		sbremit_query_up = (LinearLayout) sbremit_query_condition.findViewById(R.id.up);
		sbremit_query_up.setOnClickListener(this);
		sbremit_query_deal_startdate.setText(startDateEt);
		sbremit_query_deal_enddate.setText(endDateEt);
	}
	
	/** 显示为查询状态页面  */
	private void initQueryView() {
		sbremit_query_condition.setVisibility(View.VISIBLE);
		if (StringUtil.isNullOrEmpty(tradeListTotal)) {
			view.findViewById(R.id.query_reult_layout).setVisibility(View.GONE);
		} else {
			view.findViewById(R.id.query_result_condition_layout).setVisibility(View.GONE);
			view.findViewById(R.id.sbremit_querydeal_listview_layout).setVisibility(View.VISIBLE);
		}
	}
	
	/** 显示为信息展示状态页面 */
	private void  initShowDataView() {
		if (StringUtil.isNullOrEmpty(tradeListTotal)) {
			return;
		}
		view.findViewById(R.id.query_reult_layout).setVisibility(View.VISIBLE);
		sbremit_query_condition.setVisibility(View.GONE);
		view.findViewById(R.id.query_reult_layout).setVisibility(View.VISIBLE);
		view.findViewById(R.id.query_result_condition_layout).setVisibility(View.VISIBLE);
		view.findViewById(R.id.sbremit_querydeal_listview_layout).setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sbremit_query_deal_startdate:// 显示起始日期选择器
			sbremit_query_deal_startdate.setOnClickListener(SafetyChooseDateClick);
			break;
		case R.id.sbremit_query_deal_enddate:// 显示结束日期选择器
			sbremit_query_deal_enddate.setOnClickListener(SafetyChooseDateClick);
			break;
		case R.id.prms_down_LinearLayout:// 弹出查询框
			LogGloble.d("fsm", "弹出查询框");
			// initQueryWindow();
//			PopupWindowUtils.getInstance().showQueryPopupWindow();
			initQueryView();
			break;
		case R.id.up:// 隐藏查询框
			LogGloble.d("fsm", "隐藏查询框");
			initShowDataView();
//			PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
			break;
		case R.id.sbremit_query_deal_query:// 查询
			initQuery();
			startDate = sbremit_query_deal_startdate.getText().toString();
			endDate = sbremit_query_deal_enddate.getText().toString();
			startDateEt = startDate;
			endDateEt = endDate;
			queryHisTrade();
			break;
		case R.id.btn_acc_onweek:// 查询最近一周
			BaseHttpEngine.showProgressDialog();
			initQuery();
			startDate = QueryDateUtils.getlastWeek(dateTime);
			endDate = QueryDateUtils.getcurrentDate(dateTime);
			startDateEt = sbremit_query_deal_startdate.getText().toString();
			endDateEt = sbremit_query_deal_enddate.getText().toString();
			queryHisTrade();
			break;
		case R.id.btn_acc_onmonth:// 查询最近一月
			BaseHttpEngine.showProgressDialog();
			initQuery();
			startDate = QueryDateUtils.getlastOneMonth(dateTime);
			endDate = QueryDateUtils.getcurrentDate(dateTime);
			startDateEt = sbremit_query_deal_startdate.getText().toString();
			endDateEt = sbremit_query_deal_enddate.getText().toString();
			queryHisTrade();
			break;
		case R.id.btn_acc_threemonth:// 查询最近三月
			BaseHttpEngine.showProgressDialog();
			initQuery();
			startDate = QueryDateUtils.getlastThreeMonth(dateTime);
			endDate = QueryDateUtils.getcurrentDate(dateTime);
			startDateEt = sbremit_query_deal_startdate.getText().toString();
			endDateEt = sbremit_query_deal_enddate.getText().toString();
			queryHisTrade();
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
			hisTradeAdapter = null;
			initQueryView();
		}
	}

	/**
	 * 查询历史记录，先校验时间
	 */
	private void queryHisTrade() {
		// 校验日期
		if (!QueryDateUtils.commQueryStartAndEndateReg(this, startDate, endDate, dateTime))
			return;
		BaseHttpEngine.showProgressDialog();
		requestCommConversationId();
	}
	
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestForHisTrades();
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
		biiRequestBody.setConversationId(BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID).toString());
		biiRequestBody.setMethod(Safety.SafetyHisTranQuery);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(SBRemit.START_DATE, startDate);
		paramsmap.put(SBRemit.END_DATE, endDate);
		paramsmap.put(SBRemit.CURRENT_INDEX, curPageIndex + "");
		paramsmap.put(SBRemit.PAGE_SIZE, pageSize + "");
		paramsmap.put(Safety.REFRESH, !isMore + "");
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestForHisTradesCallBack");
	}

	/**
	 * @Title: requestForHisTradesCallBack
	 * @Description: 请求历史交易记录
	 * @param @param
	 *            resultObj
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void requestForHisTradesCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		resultMap = (Map<String, Object>) this.getHttpTools().getResponseResult(resultObj);
		tradeList = (List<Map<String, Object>>) resultMap.get(Safety.PRODUCT_LIST);
		totalCount = Integer.parseInt((String) resultMap.get(Safety.RECORDNUMBER));
		curPageIndex = curPageIndex + pageSize;
		communicationCallBack(QUERY_HISTORY_TRADE);
	}

	public void communicationCallBack(int flag) {
		switch (flag) {
		case QUERY_HISTORY_TRADE:
			if (tradeListTotal == null) {
				tradeListTotal = tradeList;
			} else {
				tradeListTotal.addAll(tradeList);
			}
			sbremit_query_result_time.setText(startDate + "-" + endDate);
			if ((!isMore && (tradeList != null && tradeList.size() > 0)) || isMore) {
//				PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
				setListView(tradeListTotal);
				initShowDataView();
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_null));
			}
			break;
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
			hisTradeAdapter = new SafetyHistoryListAdapter(this, tradeList);
			listview.setAdapter(hisTradeAdapter);
		} else {
			hisTradeAdapter.setAccountList(tradeList);
		}

	}

	/**
	 * 初始化列表头信息
	 */
	private void initListHead() {
		((TextView) findViewById(R.id.list_header_tv1)).setText(getString(R.string.safety_history_list_headertitle1));
		((TextView) findViewById(R.id.list_header_tv2)).setText(getString(R.string.prod_name));
		((TextView) findViewById(R.id.list_header_tv3)).setText(getString(R.string.finc_dealDate));
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		switch (arg0.getId()) {
		case R.id.sbremit_querydeal_listview:
			/** 获取历史交易的详细信息，传递给详细页面 */
			if (tradeListTotal != null && arg2 >= 0 && arg2 < tradeListTotal.size()) {
				SafetyDataCenter.getInstance().setProductInfoMap(tradeListTotal.get(arg2));
				startActivity(new Intent(this, SafetyHoldHisDetailActivity.class));
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 为ListView添加加载更多
	 */
	private void addFooterView() {
		int curRecordCount = tradeList.size() + (curPageIndex - pageSize);// 计算当前页最近一条记录索引
		if (curRecordCount < totalCount && curRecordCount > 0) {// 如果当前页不是最后一页则添加加载更多
			if (listview.getFooterViewsCount() <= 0) {
				listview.addFooterView(list_foooter);
			}
			list_foooter.setClickable(true);
		} else {
			if (listview.getFooterViewsCount() > 0) {
				listview.removeFooterView(list_foooter);
			}

		}
		list_foooter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isMore = true;
				queryHisTrade();
			}
		});
	}
}
