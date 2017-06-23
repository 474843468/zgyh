package com.chinamworld.bocmbci.biz.lsforex.query;

import java.util.ArrayList;
import java.util.Calendar;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseActivity;
import com.chinamworld.bocmbci.biz.lsforex.IsforexDataCenter;
import com.chinamworld.bocmbci.biz.lsforex.adapter.IsForexQueryResultAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 历史委托交易查询 页面 */
public class IsForexHistoryWTActivity extends IsForexBaseActivity implements OnClickListener {
	public static final String TAG = "IsForexHistoryWTActivity";
	/** activity View */
	private View queryView = null;
	/** backButton:返回按钮 */
	private Button backButton = null;
	/** 查询时间 */
	private TextView resultTime = null;
	/** 查询时间区域 */
	private View resultTimeView = null;
	/** 下拉图片按钮 */
	private ImageView downImg = null;
	/** 查询结果listView */
	private ListView resultListView = null;
	/** 委托序号 */
	private TextView titleNumber = null;
	/** 货币对 */
	private TextView titleCode = null;
	/** 委托类型 */
	private TextView titleType = null;
	/** 查询条件布局页面 */
	private View conditionView = null;
	/** startEdit:开始日期 */
	private TextView startEdit = null;
	/** 结束日期 */
	private TextView endEdit = null;
	/** 查询按钮 */
	private Button queryButton = null;
	/** 最近一周 */
	private Button weekButton = null;
	/** 最近一月 */
	private Button monthButton = null;
	/** 最近三月 */
	private Button threesButton = null;
	/** 开始查询日期 */
	private String startDateStr;
	/** 结束查询日期 */
	private String endDateStr;
	/** 开始日期 结束日期区域 */
	private View searchTimesView = null;
	/** 结算币种 */
	private Spinner codeSpinner = null;
	/** 收起图片按钮 */
	private ImageView searchUpImageView = null;
	/** 收起图片区域 */
	private View searchUpView = null;
	/** 前3天时间 */
	private String startTwoDate = null;
	/** 系统时间 */
	private String currenttime;
	/** 结算币种区域 */
	private View codeView = null;
	private IsForexQueryResultAdapter adapter = null;
	/** listView的title */
	private View titleView = null;
	/** 用户选择的结算币种代码 */
	private String vfgRegCode = null;
	/** 结算币种名称List */
	private List<String> vfgRegCodeNameList = null;
	/** 选择的位置 */
	private int selectedPosition = -1;
	/** 是否清空数据 true-是，fase-否 */
	private boolean isShow = false;
	/** 查询结果页面整个布局 */
	private View resultAllView = null;
	/** 查询条件整个页面 */
//	private View conditionAllView = null;
	/** 结算币种 */
	private TextView jsCodeNameText = null;
	/** 未平仓交易状况查询按钮 */
	private Button wpcButton = null;
	/** 当前页 */
	private int currentIndex = 0;
	private int pageSize = 10;
	/** 刷新标志 */
	private boolean isRefresh = true;
	/** 显示更多区域 */
	private RelativeLayout load_more;
	/** 显示更多 */
	private TextView moreButton = null;
	private View timesView = null;
	/** 1---初始化，2--点击查询按钮，3--撤单返回 */
	private int tagCon = 1;
	/** 选择的结算币种位置 */
	private int jsCodePositionBefore = -1;
	private String lastStartDate = null;
	private String lastEndDate = null;
	private String lastJsCode = null;
	private String lastStartEdit = null;
	private String lastEndEdit = null;
	
	//wuhanP603
	private Button isforex_xiankaixianping;
	/** 交易序号 */
	public String transactionNumber = null;
	/** 内部序号 */
	public String internaNumber = null;
	private String vfgType = "";
	private View layout_main =null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		queryTag = (Integer) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.ISFOREX_QUERYTAG);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 20);
		}
		setTitle(getResources().getString(R.string.query_title_six));
		tagCon = 1;
		// 查询需要重新获取commConversationId
		BaseHttpEngine.showProgressDialogCanGoBack();
		requestCommConversationId();
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		if (tagCon == 1) {
			// 请求系统时间
			requestSystemDateTime();
		} else if (tagCon == 2) {
			requestPsnVFGTradeInfoQuery(vfgRegCode, ConstantGloble.ISFOREX_ACCSEARCH_TYPE6, startDateStr, endDateStr,
					String.valueOf(pageSize), String.valueOf(currentIndex), String.valueOf(isRefresh));

		} else if (tagCon == 3) {
			vfgRegCode = lastJsCode;
			startDateStr = lastStartDate;
			endDateStr = lastEndDate;
			startEdit.setText(lastStartEdit);
			// 初始结束时间赋值
			endEdit.setText(lastEndEdit);
			// codeSpinner.setSelection(jsCodePositionBefore);
			requestPsnVFGTradeInfoQuery(vfgRegCode, ConstantGloble.ISFOREX_ACCSEARCH_TYPE6, startDateStr, endDateStr,
					String.valueOf(pageSize), String.valueOf(currentIndex), String.valueOf(isRefresh));
		}
	}

	// 请求时间
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		if (StringUtil.isNull(dateTime)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		// 3天前的日期
		startTwoDate = QueryDateUtils.getlastthreeDate(dateTime);
		// 系统当前时间格式化
		currenttime = QueryDateUtils.getcurrentDate(dateTime);
		// 获取结算币种
		requestPsnVFGGetRegCurrency(vfgType);
	}

	/** 获得结算币种---回调 */
	@Override
	public void requestPsnVFGGetRegCurrencyCallback(Object resultObj) {
		super.requestPsnVFGGetRegCurrencyCallback(resultObj);
		if (vfgRegCurrencyList == null || vfgRegCurrencyList.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					IsForexHistoryWTActivity.this.getResources().getString(R.string.isForex_trade_jcCode));
			return;
		} else {
			vfgRegCurrencyList = dealVfgRegCode(vfgRegCurrencyList);
			if (vfgRegCurrencyList == null || vfgRegCurrencyList.size() <= 0) {
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						IsForexHistoryWTActivity.this.getResources().getString(R.string.isForex_trade_jcCode));
				return;
			}
			vfgRegCode = vfgRegCurrencyList.get(0);
			BaseHttpEngine.canGoBack = false;
			if (tagCon == 1) {
				init();
				initCondition();
				
				initOnClick();
			}
			getCodeNameList();
			searchData();
		}
	}

	/** 调用查询方法 */
	private void searchData() {
		startDateStr = startTwoDate;
		endDateStr = currenttime;
		if (StringUtil.isNull(startDateStr) || StringUtil.isNull(endDateStr)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		conditionView.setVisibility(View.VISIBLE);
		resultAllView.setVisibility(View.GONE);
//		PopupWindowUtils.getInstance().showQueryPopupWindowFirst();
		if (tagCon == 1) {
			BaseHttpEngine.dissMissProgressDialog();
		}
	}

	@Override
	public void requestPsnVFGTradeInfoQueryCallback(Object resultObj) {
		super.requestPsnVFGTradeInfoQueryCallback(resultObj);
		if (StringUtil.isNullOrEmpty(queryResultList) || recordNumber <= 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		} else {
			showLoadMore();
			getView();
			setTimeValue();
			isShow = true;
			setJsCode();
			titleType.setText(getResources().getString(R.string.forex_emg_money));
			titleView.setVisibility(View.VISIBLE);
			if (isRefresh) {
				adapter = new IsForexQueryResultAdapter(IsForexHistoryWTActivity.this, queryResultList, queryTag, vfgRegCode);
				resultListView.setAdapter(adapter);
			} else {
				adapter.dataChange(queryResultList);
			}
			adapter.setOnItemClickListener(listener);
		}
	}

	/** 关闭查询条件，显示查询结果 */
	private void getView() {
		conditionView.setVisibility(View.GONE);
		resultAllView.setVisibility(View.VISIBLE);
		resultTimeView.setVisibility(View.VISIBLE);
		layout_main.setVisibility(View.VISIBLE);
		conditionView.setBackgroundResource(R.drawable.img_bg_query_no);
	}

	/** 是否显示更多按钮 */
	private void showLoadMore() {
		if (recordNumber > pageSize && isRefresh) {
			resultListView.addFooterView(load_more);
		}
		if (!isRefresh) {
			if (currentIndex + pageSize >= recordNumber) {
				resultListView.removeFooterView(load_more);
			}
		}
	}

	/** 处理结算币种 ,币种代码一定要含有币种名称 */
	private List<String> dealVfgRegCode(List<String> list) {
		int len = list.size();
		List<String> dateList = new ArrayList<String>();
		for (int i = 0; i < len; i++) {
			String s1 = list.get(i);
			if (!StringUtil.isNull(s1) && LocalData.Currency.containsKey(s1)) {
				dateList.add(s1);
			}
		}
		return dateList;
	}

	/** 查询结果页面-查询时间赋值 */
	private void setTimeValue() {
		String text = getResources().getString(R.string.acc_query_date);
		resultTime.setText(text + startDateStr + "-" + endDateStr);
		setJsCode();
	}

	/** 查询结果页面-结算币种赋值 */
	private void setJsCode() {
		String jsCode = getResources().getString(R.string.isForex_vfgRegCurrency1);
		String code = LocalData.Currency.get(vfgRegCode);
		jsCodeNameText.setText(jsCode + code);
	}

	/** 得到结算币种的名称 */
	private void getCodeNameList() {
		if (vfgRegCodeNameList != null && vfgRegCodeNameList.size() > 0) {
			vfgRegCodeNameList.clear();
		}
		int len = vfgRegCurrencyList.size();
		String code1 = null;
		for (int i = 0; i < len; i++) {
			String code = vfgRegCurrencyList.get(i);
			if (LocalData.Currency.containsKey(code)) {
				code1 = LocalData.Currency.get(code);
				vfgRegCodeNameList.add(code1);
			}
		}
		if (vfgRegCodeNameList.size() <= 0 || vfgRegCodeNameList == null) {
			return;
		} else {
			ArrayAdapter<String> currencyAdapter = new ArrayAdapter<String>(this, R.layout.custom_spinner_item,
					vfgRegCodeNameList);
			currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			codeSpinner.setAdapter(currencyAdapter);
			codeSpinner.setSelection(0);
			jsCodePositionBefore = 0;
		}
	}

	/** 收起事件 */
	private OnClickListener searchUpOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 关闭查询条件
			if (queryResultList != null && queryResultList.size() > 0) {
//				PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
				conditionView.setVisibility(View.GONE);
				resultAllView.setVisibility(View.VISIBLE);
				resultTimeView.setVisibility(View.VISIBLE);
				layout_main.setVisibility(View.VISIBLE);
			} else {
				conditionView.setBackgroundResource(R.drawable.img_bg_query_j);
				resultAllView.setVisibility(View.INVISIBLE);
				resultTimeView.setVisibility(View.GONE);
				layout_main.setVisibility(View.VISIBLE);
				conditionView.setVisibility(View.VISIBLE);
			}
		}
	};
	/** 下拉事件 */
	private OnClickListener resultOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {// 下拉
			// 显示查询条件
//			PopupWindowUtils.getInstance().showQueryPopupWindow();
			conditionView.setVisibility(View.VISIBLE);
			resultAllView.setVisibility(View.VISIBLE);
			resultTimeView.setVisibility(View.GONE);
			layout_main.setVisibility(View.VISIBLE);
		}
	};
	/** listView 的事件 */
	private OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
			selectedPosition = position;
			//当追击止损委托的时候，增加PsnVFGTradeDetailQuery
			
			Map<String, Object> result = queryResultList.get(position);
			transactionNumber = (String) result.get(IsForex.ISFOREX_CONSIGNNUMBER_REQ);
			internaNumber = (String) result.get(IsForex.ISFOREX_internalSeq_REQ);
			requestPsnVFGTradeDetailQuery(transactionNumber, internaNumber);
			
			
			
		}
	};

	public void requestPsnVFGTradeDetailQueryCallback(Object resultObj) {
		super.requestPsnVFGTradeDetailQueryCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
//		String foSet = result.get("foSet").toString();
		
		IsforexDataCenter.getInstance().setLradeDetailMap(result);
//		queryResultList.get(selectedPosition).put("foSet", foSet);
		BaseDroidApp.getInstanse().getBizDataMap().put(IsForex.ISFOREX_LIST_REQ, queryResultList);
		Intent intent = new Intent(IsForexHistoryWTActivity.this, IsForexWTDetaiActivity.class);
		intent.putExtra(ConstantGloble.ISFOREX_SELECTEDPOSITION, selectedPosition);
		vfgRegCode = vfgRegCurrencyList.get(jsCodePositionBefore);
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_QUERYTAG, queryTag);
		intent.putExtra(ConstantGloble.ISFOREX_VFGREGCODE, vfgRegCode);
		lastStartDate = startDateStr;
		lastEndDate = endDateStr;
		lastJsCode = vfgRegCode;
		lastStartEdit = startEdit.getText().toString().trim();
		lastEndEdit = endEdit.getText().toString().trim();
		startActivityForResult(intent, ConstantGloble.ISFOREX_WT_ACTIVITY);
		
	};
	
	
	
	
	/** 初始化查询条件页面 */
	private void initCondition() {
		// 查询条件
//		conditionView = LayoutInflater.from(IsForexHistoryWTActivity.this).inflate(R.layout.isforex_query_condition, null);
//		conditionAllView = conditionView.findViewById(R.id.ll_query_condition);
		conditionView = findViewById(R.id.isforex_query_cond);
		startEdit = (TextView) findViewById(R.id.forex_query_deal_startdate);
		endEdit = (TextView) findViewById(R.id.forex_query_deal_enddate);
		searchTimesView =findViewById(R.id.acc_query_date);
		queryButton = (Button) findViewById(R.id.forex_query_deal_query);
		wpcButton = (Button) findViewById(R.id.isforex_query);
		weekButton = (Button) findViewById(R.id.forex_queryquery_deal_queryoneweek);
		monthButton = (Button) findViewById(R.id.forex_queryquery_deal_queryonemouth);
		threesButton = (Button)findViewById(R.id.forex_queryquery_deal_querythreemouths);
		codeView = findViewById(R.id.code_layout);
		codeSpinner = (Spinner) findViewById(R.id.isForex_vfgReg);
		View timesAndSearchView = findViewById(R.id.acc_query_choosedate);
		wpcButton.setVisibility(View.INVISIBLE);
		searchTimesView.setVisibility(View.VISIBLE);
		timesAndSearchView.setVisibility(View.VISIBLE);
		// 查询条件页面-收起图片
		searchUpImageView = (ImageView) findViewById(R.id.forex_query_up);
		// 查询条件页面-收起区域
		searchUpView =findViewById(R.id.search_shouqi_up);
		startEdit.setText(startTwoDate);
		// 初始结束时间赋值
		endEdit.setText(currenttime);
		conditionView.setVisibility(View.VISIBLE);
		resultAllView.setVisibility(View.GONE);
//		PopupWindowUtils.getInstance().getQueryPopupWindow(conditionView, this);
	}

	/** 初始化控件 */
	private void init() {
		queryView = LayoutInflater.from(IsForexHistoryWTActivity.this).inflate(R.layout.isforex_query_result, null);
		tabcontent.addView(queryView);
		resultAllView = findViewById(R.id.forex_query_result_layout);
		layout_main = findViewById(R.id.layout_main);
		isforex_xiankaixianping = (Button) findViewById(R.id.isforex_xiankaixianping);
		isforex_xiankaixianping.setVisibility(View.GONE);
		
		
		backButton = (Button) findViewById(R.id.ib_back);
		// 查询结果页面
		resultTime = (TextView) findViewById(R.id.trade_time);
		timesView = findViewById(R.id.times_layout);
		timesView.setVisibility(View.VISIBLE);
		jsCodeNameText = (TextView) findViewById(R.id.trade_jsCode);
		resultTimeView = findViewById(R.id.forex_query_result_condition);
		// 下拉
		downImg = (ImageView) findViewById(R.id.img_acc_query_back);
		resultListView = (ListView) findViewById(R.id.rate_listView);
		titleNumber = (TextView) findViewById(R.id.title_number);
		titleCode = (TextView) findViewById(R.id.title_code);
		titleType = (TextView) findViewById(R.id.title_type);
		titleView = findViewById(R.id.listView_title);
		titleView.setVisibility(View.GONE);
		load_more = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.query_list_footer, null);
		moreButton = (TextView) load_more.findViewById(R.id.finc_listiterm_tv1);
		moreButton.setOnClickListener(this);
		vfgRegCodeNameList = new ArrayList<String>();
	}

	private void initOnClick() {
		startEdit.setOnClickListener(this);
		endEdit.setOnClickListener(this);
		queryButton.setOnClickListener(this);
		wpcButton.setOnClickListener(this);
		// 查询最近一周
		weekButton.setOnClickListener(this);
		// 查询最近一月
		monthButton.setOnClickListener(this);
		// 查询最近三个月
		threesButton.setOnClickListener(this);
		backButton.setOnClickListener(this);
		codeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				codeSpinner.setSelection(position, true);
				vfgRegCode = vfgRegCurrencyList.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		// 收起
		searchUpImageView.setOnClickListener(searchUpOnClickListener);
		searchUpView.setOnClickListener(searchUpOnClickListener);
		// 下拉
		downImg.setOnClickListener(resultOnClickListener);
		resultTimeView.setOnClickListener(resultOnClickListener);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.forex_query_deal_startdate:// 开始日期
			Calendar c = Calendar.getInstance(); // 实例化一个日期与时间操作对象
			QueryDateUtils.checkDate(startEdit, c);
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息 DatePickerDialog
			DatePickerDialog dialog = new DatePickerDialog(IsForexHistoryWTActivity.this, new OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					// ** 选择的截止日期 *//*
					StringBuilder date = new StringBuilder();
					date.append(String.valueOf(year));
					date.append("/");
					int month = monthOfYear + 1;
					date.append(((month < 10) ? ("0" + month) : (month + "")));
					date.append("/");
					date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth) : (dayOfMonth + "")));
					String startDateStr = date.toString().trim();
					startEdit.setText(startDateStr);
				}
			}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
			dialog.show();
			break;
		case R.id.forex_query_deal_enddate:// 结束日期
			Calendar c1 = Calendar.getInstance(); // 实例化一个日期与时间操作对象
			QueryDateUtils.checkDate(endEdit, c1);
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			DatePickerDialog dialog1 = new DatePickerDialog(IsForexHistoryWTActivity.this, new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					// 选择的截止日期
					StringBuilder date = new StringBuilder();
					date.append(String.valueOf(year));
					date.append("/");
					int month = monthOfYear + 1;
					date.append(((month < 10) ? ("0" + month) : (month + "")));
					date.append("/");
					date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth) : (dayOfMonth + "")));
					// 结束日期在系统日期之前
					String endDateStr = date.toString().trim();
					// 为EditText赋值
					endEdit.setText(endDateStr);
				}
			}, c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DAY_OF_MONTH));
			dialog1.show();
			break;
		case R.id.forex_query_deal_query:// 查询按钮
			showCondition();
			cleanData();
			cleanMoreButton();
			tagCon = 2;
			startDateStr = startEdit.getText().toString().trim();
			endDateStr = endEdit.getText().toString().trim();
			jsCodePositionBefore = codeSpinner.getSelectedItemPosition();
			vfgRegCode = vfgRegCurrencyList.get(jsCodePositionBefore);
			if (QueryDateUtils.compareDateOneYear(startDateStr, dateTime)) {
				// 开始日期在系统日期前一年以内
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						IsForexHistoryWTActivity.this.getString(R.string.acc_check_start_enddate));
				return;
			}
			if (QueryDateUtils.compareDate(endDateStr, dateTime)) {
				// 结束日期在系统日期之前
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						IsForexHistoryWTActivity.this.getString(R.string.acc_check_enddate));
				return;
			}
			if (QueryDateUtils.compareDate(startDateStr, endDateStr)) {
				// 开始日期在结束日期之前
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						IsForexHistoryWTActivity.this.getString(R.string.forex_query_times1));
				return;
			}
			if (QueryDateUtils.compareDateThree(startDateStr, endDateStr)) {
				// 起始日期与结束日期最大间隔为三个自然月
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						IsForexHistoryWTActivity.this.getString(R.string.acc_check_start_end_date));
				return;
			}
			setConditionTimes();
			setTimeValue();
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
			break;
		case R.id.forex_queryquery_deal_queryoneweek:// 最近一周
			showCondition();
			cleanData();
			cleanMoreButton();
			tagCon = 2;
			startDateStr = QueryDateUtils.getlastWeek(dateTime).trim();
			endDateStr = QueryDateUtils.getcurrentDate(dateTime).trim();
			jsCodePositionBefore = codeSpinner.getSelectedItemPosition();
			vfgRegCode = vfgRegCurrencyList.get(jsCodePositionBefore);
			setTimeValue();
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
			break;
		case R.id.forex_queryquery_deal_queryonemouth:// 最近一月
			showCondition();
			cleanData();
			cleanMoreButton();
			tagCon = 2;
			startDateStr = QueryDateUtils.getlastOneMonth(dateTime).trim();
			endDateStr = QueryDateUtils.getcurrentDate(dateTime).trim();
			jsCodePositionBefore = codeSpinner.getSelectedItemPosition();
			vfgRegCode = vfgRegCurrencyList.get(jsCodePositionBefore);
			setTimeValue();
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
			break;
		case R.id.forex_queryquery_deal_querythreemouths:// 最近三月
			showCondition();
			cleanData();
			cleanMoreButton();
			tagCon = 2;
			startDateStr = QueryDateUtils.getlastThreeMonth(dateTime).trim();
			endDateStr = QueryDateUtils.getcurrentDate(dateTime).trim();
			jsCodePositionBefore = codeSpinner.getSelectedItemPosition();
			vfgRegCode = vfgRegCurrencyList.get(jsCodePositionBefore);
			setTimeValue();
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
			break;
		case R.id.ib_back:
			finish();
			break;
		case R.id.finc_listiterm_tv1:// 更多按钮
			vfgRegCode = vfgRegCurrencyList.get(jsCodePositionBefore);
			isRefresh = false;
			currentIndex += pageSize;
			BaseHttpEngine.showProgressDialog();
			requestPsnVFGTradeInfoQuery(vfgRegCode, ConstantGloble.ISFOREX_ACCSEARCH_TYPE6, startDateStr, endDateStr,
					String.valueOf(pageSize), String.valueOf(currentIndex), String.valueOf(isRefresh));
			break;

		default:
			break;
		}

	}

	/** 隐藏查询结果页面 */
	private void showCondition() {
		conditionView.setBackgroundResource(R.drawable.img_bg_query_j);
		conditionView.setVisibility(View.VISIBLE);
		resultAllView.setVisibility(View.INVISIBLE);
	}

	/** 每次查询前清空上一次查询的数据 */
	private void cleanData() {
		currentIndex = 0;
		isRefresh = true;
		if (isShow) {
			if (queryResultList != null && !queryResultList.isEmpty()) {
				queryResultList.clear();
			}
			adapter.dataChange(queryResultList);
		}
	}

	/** 设置查询条件的时间 */
	private void setConditionTimes() {
		startEdit.setText(startDateStr);
		endEdit.setText(endDateStr);
	}

	/** 移除更多按钮 */
	private void cleanMoreButton() {
		if (resultListView.getFooterViewsCount() > 0) {
			resultListView.removeFooterView(load_more);
		}
	}

	/** 重新请求数据 */
	private void onActivityCleanDate() {
		resultAllView.setVisibility(View.INVISIBLE);
		currentIndex = 0;
		isRefresh = true;
		cleanMoreButton();
		if (queryResultList != null && !queryResultList.isEmpty()) {
			queryResultList.clear();
		}
		if (adapter != null) {
			adapter.dataChange(new ArrayList<Map<String, Object>>());
		}
	}

	// Login界面结束 回调
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:// 成功
			if (requestCode == ConstantGloble.ISFOREX_WT_ACTIVITY) {
				// 重新加载页面数据------获取结算币种
				LogGloble.d(TAG, "onActivityResult===");
				BaseHttpEngine.dissMissProgressDialog();
				onActivityCleanDate();
				tagCon = 3;
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
			}
			break;
		case RESULT_CANCELED:
			break;
		default:
			break;
		}
	};
}
