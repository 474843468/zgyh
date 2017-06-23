package com.chinamworld.bocmbci.biz.forex.strike;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.biz.forex.ForexBaseActivity;
import com.chinamworld.bocmbci.biz.forex.adapter.ForexStrikeQueryInfoAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.thread.PollingRequestThread;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

//import com.chinamworld.bocmbci.utils.PopupWindowUtils;

/**
 * 成交状况查询页面，选择查询日期，选择查询方式
 * 
 * @author Administrator
 * 
 */
public class ForexStrikeQueryActivity extends ForexBaseActivity implements OnClickListener {
	private static final String TAG = "ForexStrikeQueryActivity";
	/**
	 * ForexStrikeQueryActivity的主布局
	 */
	private View rateInfoView = null;
	/**
	 * backButton:返回按钮
	 */
	private Button backButton = null;
	/**
	 * startEdit:开始日期
	 */
	private TextView startEdit = null;
	/**
	 * 结束日期
	 */
	private TextView endEdit = null;
	/**
	 * 查询按钮
	 */
	private Button queryButton = null;
	/**
	 * 最近一周
	 */
	private RadioButton weekButton = null;
	/**
	 * 最近一月
	 */
	private RadioButton monthButton = null;
	/**
	 * 最近三月
	 */
	private RadioButton threesButton = null;
	/**
	 * 开始查询日期
	 */
	private String startDateStr;
	/**
	 * 结束查询日期
	 */
	private String endDateStr;
	List<Map<String, Object>> tradeList = null;

	/** 系统时间 */
	private String currenttime;
	/** 查询页面，向上收起 */
	private ImageView searchUpImageView = null;
	/** 查询时间 */
	private TextView searchResultTimeText = null;
	/** 查询结果页面向下箭头 */
	private ImageView searchResultDownImageView = null;
	/** 查询结果listView */
	private ListView searchResultListView = null;
	/** 查询结果顶部 */
	private View searchResultTopView = null;
	/** 查询页面收起箭头区域 */
	private View searchUpView = null;
	/** 查询条件 */
	private View searchConditionView = null;
	/** 成交时间 */
	private String realTransTime = null;
	private ForexStrikeQueryInfoAdapter adapter = null;
	private String startTwoDate = null;
//	private String seven = LocalData.forexTradeStyleTransList.get(1);
//	private String eight = LocalData.forexTradeStyleTransList.get(0);
	/** 是否显示更多 */
	private boolean isClean = false;
	/** 刷新标志 */
	private boolean isRefresh = true;
	/** 显示更多 */
	private TextView moreButton = null;
	/** 记录总数 */
	private String recordNumber = null;
	/** 记录总数 */
	private int number = 0;
	/** 次数 */
//	private int count = 1;
	/** 当前页 */
	private int currentIndex = 0;
	private int pageSize = 10;
	/** 显示更多区域 */
	private RelativeLayout load_more;
	/** 主界面 */
	private Button btnRight = null;
	private LinearLayout searchResultAllView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout view_slid=(LinearLayout) findViewById(R.id.sliding_body);
		if ( view_slid!= null) {
			view_slid.setPadding(0, 0, 0, 0);
		}
		LogGloble.d(TAG, "onCreate");
		stopPollingFlag();
		btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setVisibility(View.GONE);
		backButton = (Button) findViewById(R.id.ib_back);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		/** 为界面标题赋值 */
		setTitle(getResources().getString(R.string.forex_trade));
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		rateInfoView = LayoutInflater.from(ForexStrikeQueryActivity.this).inflate(R.layout.forex_trade_query, null);
		rateInfoView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,0));
		tabcontent.addView(rateInfoView);
//		setLeftButtonPopupGone();

		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		tradeList = new ArrayList<Map<String, Object>>();
		taskTag = 3;
		menuOrTrade = 1;
		BaseHttpEngine.showProgressDialogCanGoBack();
		requestCommConversationId();

	}

	/** 停止轮询 */
	private void stopPollingFlag() {
		if (HttpManager.mPollingRequestThread != null && PollingRequestThread.pollingFlag) {
			LogGloble.e(TAG, "onPause() mPollingRequestThread  stopPolling()  ");
			HttpManager.stopPolling();
		}
	}

	/**
	 * 初始化所有的控件---查询条件页面
	 */
	private void init() {
		searchConditionView = rateInfoView.findViewById(R.id.ll_query_condition);
		searchConditionView.setBackgroundResource(R.drawable.img_bg_query_j);
		// 查询条件
		startEdit = (TextView) searchConditionView.findViewById(R.id.forex_query_deal_startdate);
		endEdit = (TextView) searchConditionView.findViewById(R.id.forex_query_deal_enddate);
		queryButton = (Button) searchConditionView.findViewById(R.id.forex_query_deal_query);
		weekButton = (RadioButton) searchConditionView.findViewById(R.id.forex_queryquery_deal_queryoneweek);
		monthButton = (RadioButton) searchConditionView.findViewById(R.id.forex_queryquery_deal_queryonemouth);
		threesButton = (RadioButton) searchConditionView.findViewById(R.id.forex_queryquery_deal_querythreemouths);
		// 查询条件页面-收起图片
		searchUpImageView = (ImageView) searchConditionView.findViewById(R.id.finc_up_imageView);
		// 查询条件页面-收起区域
		searchUpView = searchConditionView.findViewById(R.id.search_shouqi_up);
		// 3天前的日期
		startTwoDate = QueryDateUtils.getlastthreeDate(dateTime);
		startEdit.setText(startTwoDate);
		// 系统当前时间格式化
		currenttime = QueryDateUtils.getcurrentDate(dateTime);
		// 初始结束时间赋值
		endEdit.setText(currenttime);
		// 显示查询条件
//		PopupWindowUtils.getInstance().getQueryPopupWindow(searchConditionView, ForexStrikeQueryActivity.this);
	}
	
	/**
	 * 设置页面状态 0-查询状态 1-数据展示状态
	 * @param state
	 */
	private void setViewShow(int state) {
		switch (state) {
		case 0:
			searchConditionView.setVisibility(View.VISIBLE);
			if (StringUtil.isNull(recordNumber)) {
				searchResultAllView.setVisibility(View.GONE);
			} else {
				searchResultTopView.setVisibility(View.GONE);
			}
			break;

		case 1:
			if (!StringUtil.isNullOrEmpty(tradeList)) {
				searchConditionView.setVisibility(View.GONE);
				searchResultAllView.setVisibility(View.VISIBLE);
				searchResultTopView.setVisibility(View.VISIBLE);
			}
			break;
		}
	}

	/** 初始化查询结果页面的布局 */
	private void initResultView() {
		searchResultAllView = (LinearLayout) rateInfoView.findViewById(R.id.forex_query_result_layout);
		// 下拉图片
		searchResultDownImageView = (ImageView) rateInfoView.findViewById(R.id.img_acc_query_back);
		// 查询时间整个区域
		searchResultTopView = rateInfoView.findViewById(R.id.forex_query_result_condition);
		searchResultListView = (ListView) rateInfoView.findViewById(R.id.rate_listView);
		searchResultTimeText = (TextView) rateInfoView.findViewById(R.id.trade_time);

		load_more = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.query_list_footer, null);
		moreButton = (TextView) load_more.findViewById(R.id.finc_listiterm_tv1);
	}

	private void initOnClick() {
		startEdit.setOnClickListener(this);
		endEdit.setOnClickListener(this);

		queryButton.setOnClickListener(this);
		// 查询最近一周
		weekButton.setOnClickListener(this);
		// 查询最近一月
		monthButton.setOnClickListener(this);
		// 查询最近三个月
		threesButton.setOnClickListener(this);
		moreButton.setOnClickListener(this);
		// 收起
		searchUpImageView.setOnClickListener(searchUpOnClickListener);
		searchUpView.setOnClickListener(searchUpOnClickListener);
		// 查询结果顶部View
		searchResultTopView.setOnClickListener(resultOnClickListener);
		// 查询结果页面的向下箭头
		searchResultDownImageView.setOnClickListener(resultOnClickListener);
	}

	/** 下拉事件 */
	private OnClickListener resultOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {// 下拉
			// 显示查询条件
			// PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
			// PopupWindowUtils.getInstance().getQueryPopupWindow(searchConditionView,
			// ForexStrikeQueryActivity.this);
//			PopupWindowUtils.getInstance().showQueryPopupWindow();
			setViewShow(0);
		}
	};
	/** 收起事件 */
	private OnClickListener searchUpOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 关闭查询条件
			setViewShow(1);
		}
	};

	/** 用于区分是否是快捷键启动 */
	private void iscomformFootFastOrOther() {
		if (getIntent().getBooleanExtra(ConstantGloble.COMEFROMFOOTFAST, false)) {
			// 快捷键启动
			// 判断用户是否开通投资理财服务
			requestPsnInvestmentManageIsOpen();
		} else {
			if (isOpen && isSettingAcc) {
				// 请求系统时间
				requestSystemDateTime();
			} else {
				// 判断用户是否开通投资理财服务
				requestPsnInvestmentManageIsOpen();
			}
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		iscomformFootFastOrOther();
	}

	/**
	 * 判断是否开通投资理财服务---回调 快速交易
	 * 
	 * @param resultObj
	 */
	public void requestPsnInvestmentManageIsOpenCallback(Object resultObj) {
		super.requestPsnInvestmentManageIsOpenCallback(resultObj);
		// 设定账户的请求
		requestPsnForexActIsset();
	}

	/**
	 * 交易条件
	 * 
	 * @任务提示框----判断是否设置账户
	 * @param resultObj
	 *            :返回结果
	 */
	public void requestPsnForexActIssetCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			isSettingAcc = false;
		} else {
			if (StringUtil.isNullOrEmpty(result.get(Forex.FOREX_RATE_INVESTBINDINGINFO_RES))) {
				isSettingAcc = false;
			} else {
				investBindingInfo = (Map<String, String>) result.get(Forex.FOREX_RATE_INVESTBINDINGINFO_RES);
				BaseDroidApp.getInstanse().getBizDataMap()
						.put(Forex.FOREX_RATE_INVESTBINDINGINFO_RES, investBindingInfo);

				if (StringUtil.isNullOrEmpty(investBindingInfo)) {
					isSettingAcc = false;
				} else {
					isSettingAcc = true;
				}
			}
		}
		if (!isOpen || !isSettingAcc) {
			BaseHttpEngine.dissMissProgressDialog();
			getPopup();
			return;
		}
		if (isOpen && isSettingAcc) {
			// 请求系统时间
			requestSystemDateTime();
		}

	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();

		dateTime = (String) resultMap.get(Comm.DATETME);
		if (StringUtil.isNullOrEmpty(dateTime)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			BaseHttpEngine.canGoBack = false;
			// 初始化界面
			initResultView();
			init();
			initOnClick();
			// 查询三天前的交易
			startDateStr = startTwoDate;
			endDateStr = currenttime;
			setTimeValue();
			setViewShow(0);
			BaseHttpEngine.dissMissProgressDialog();
			// requestPsnForexAllTransQuery(startDateStr, endDateStr,
			// ConstantGloble.FOREX_QUERYTYPE,
			// ConstantGloble.FOREX_PAGESIZE, ConstantGloble.FOREX_CURRENTINDEX,
			// ConstantGloble.FOREX_REFRESH);
		}

	}

	/** 查询结果页面-查询时间赋值 */
	private void setTimeValue() {
//		String text = getResources().getString(R.string.acc_query_date);
		searchResultTimeText.setText(/*text +*/ startDateStr + "-" + endDateStr);
	}

	/** 清空数据 */
	private void cleanDate() {
		recordNumber = null;
		isRefresh = true;
		currentIndex = 0;
		if (tradeList != null && !tradeList.isEmpty()) {
			tradeList.clear();
		}
		if (adapter != null) {
			adapter.dataChanged(new ArrayList<Map<String, Object>>());
		}
		// 移除更多按钮
		if (isClean) {
			if (searchResultListView.getFooterViewsCount() > 0) {
				searchResultListView.removeFooterView(load_more);
			}

		}

	}

	/**
	 * 成交状况查询
	 */
	private void requestPsnForexAllTransQuery(String startDate, String endDate, String queryType, String pageSize,
			String currentIndex, String refresh) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Forex.FOREX_PSNFOREXALLTRANSQUERY_API);
		biiRequestBody.setConversationId(commConversationId);
		Map<String, String> map = new Hashtable<String, String>();
		map.put(Forex.FOREX_ENDDATE_REQ, endDate);
		map.put(Forex.FOREX_STARTDATE_REQ, startDate);
		map.put(Forex.FOREX_QUERYTYPE_REQ, queryType);
		map.put(Forex.FOREX_PAGESIZE_REQ, pageSize);
		map.put(Forex.FOREX_CURRENTINDEX_REQ, currentIndex);
		map.put(Forex.FOREX_REFRESH_REQ, refresh);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnForexAllTransQueryCallback");

	}

	/**
	 * 成交状况查询--回调
	 * 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnForexAllTransQueryCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> data = (Map<String, Object>) (biiResponseBody.getResult());
		if (StringUtil.isNullOrEmpty(data)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		} else {
			recordNumber = (String) data.get(Forex.FOREX_RECORDNUMBER_RES);
			if (StringUtil.isNull(recordNumber)) {
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getResources().getString(R.string.acc_transferquery_null));
				return;
			}
			LogGloble.d(TAG + " recordNumber", recordNumber);
			number = Integer.valueOf(recordNumber);
			if (number > pageSize && isRefresh) {
				// 显示更多按钮
				isClean = true;
				searchResultListView.addFooterView(load_more);
			}
			if (!isRefresh) {
				if (currentIndex + pageSize >= number) {
					isClean = false;
					searchResultListView.removeFooterView(load_more);
				}
			}
			tradeList = (List<Map<String, Object>>) data.get(Forex.FOREX_TRADE_LIST_RES);
			if (StringUtil.isNullOrEmpty(tradeList)) {
				// 查询条件为空
				BaseHttpEngine.dissMissProgressDialog();
				setViewShow(0);
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getResources().getString(R.string.acc_transferquery_null));
				return;
			} else {
				// 查询条件不为空
				setViewShow(1);
				searchResultAllView.setVisibility(View.VISIBLE);
				searchConditionView.setBackgroundResource(R.drawable.img_bg_query_no);
				BaseHttpEngine.dissMissProgressDialog();
				translateCode();
			}
		}
	}

	/** 将买入币种、卖出币种翻译为对应的币种名称 */
	public void translateCode() {
		int len = tradeList.size();
		for (int i = 0; i < len; i++) {
			Map<String, Object> trdeMap = tradeList.get(i);
			Map<String, String> firstBuyCurrency = (Map<String, String>) trdeMap.get(Forex.FOREX_FIRSTBUYCURRENY_RES);
			String buyCode = firstBuyCurrency.get(Forex.FOREX_CODE_RES);
			Map<String, String> firstSellCurrency = (Map<String, String>) trdeMap
					.get(Forex.FOREX_FIRESTSELLCURRENCY_RES);
			String sellCode = firstSellCurrency.get(Forex.FOREX_CODE_RES);

			if (LocalData.Currency.containsKey(buyCode) && LocalData.Currency.containsKey(sellCode)) {
				String currencyCode = LocalData.Currency.get(buyCode);
				firstBuyCurrency.put(Forex.FOREX_CODE_RES, currencyCode);
				trdeMap.put(Forex.FOREX_FIRSTBUYCURRENY_RES, firstBuyCurrency);
				tradeList.set(i, trdeMap);
			}
			if (LocalData.Currency.containsKey(sellCode)) {
				String currencyCode = LocalData.Currency.get(sellCode);
				firstSellCurrency.put(Forex.FOREX_CODE_RES, currencyCode);
				trdeMap.put(Forex.FOREX_FIRESTSELLCURRENCY_RES, firstSellCurrency);
				tradeList.set(i, trdeMap);
			}
		}
		if (StringUtil.isNullOrEmpty(tradeList)) {
			return;
		} else {
			if (!isRefresh) {
				// 数据往后加载
				adapter.dataChanged(tradeList);
			} else {
				adapter = new ForexStrikeQueryInfoAdapter(ForexStrikeQueryActivity.this, tradeList);
				searchResultListView.setAdapter(adapter);
			}
			adapter.setOnItemClickListener(onItemClickListener);

		}
	}

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
			Map<String, Object> map = tradeList.get(position);
			LogGloble.d(TAG + " position", position + "");
			String sellAmount = null;// 卖出金额
			String buyAmount=null;//买入金额
			String rate = null;// 成交汇率
			String consignNumber = (String) map.get(Forex.FOREX_CONSIGNNUMBER_RES);
			// 买入币种
			Map<String, String> firstBuyCurrency = (Map<String, String>) map.get(Forex.FOREX_FIRSTBUYCURRENY_RES);
			String buyCode = firstBuyCurrency.get(Forex.FOREX_CODE_RES);
			// 卖出币种
			Map<String, String> firstSellCurrency = (Map<String, String>) map.get(Forex.FOREX_FIRESTSELLCURRENCY_RES);
			String sellCode = firstSellCurrency.get(Forex.FOREX_CODE_RES);

			String firstStatus = (String) map.get(Forex.FOREX_FRESTSTATUS_RES);
			String secondStatus = (String) map.get(Forex.FOREX_SECONDSTATUS_RES);
			// 成交时间
			realTransTime = (String) map.get(Forex.FOREX_REALTRANSTIME);
			// 成交类型
			String exchangeTranType = (String) map.get(Forex.FOREX_EXCHANGETRANTYPE_RES);
			if (!StringUtil.isNull(firstStatus)) {

				if (firstStatus.equalsIgnoreCase(ConstantGloble.FOREX_STATUS)) {
					sellAmount = (String) map.get(Forex.FOREX_FIRSTSELLAMOUNT_RES);
					rate = (String) map.get(Forex.FOREX_FRESTRATE_RES);
					buyAmount=(String) map.get(Forex.FOREX_FIRSTBUYAMOUNT_RES);
				} else {
					if (StringUtil.isNull(secondStatus)) {
						sellAmount = "-";
						rate = "-";
						buyAmount="-";
					} else {
						if (secondStatus.equalsIgnoreCase(ConstantGloble.FOREX_STATUS)) {
							sellAmount = (String) map.get(Forex.FOREX_SECONDSELLAMOUNT_RES);
							rate = (String) map.get(Forex.FOREX_SECONDRATE_RES);
							buyAmount=(String) map.get(Forex.FOREX_SECONDBUYAMOUNT_RES);
						} else {
							sellAmount = "-";
							rate = "-";
							buyAmount="-";
						}
					}
				}
			} else {
				if (StringUtil.isNull(secondStatus)) {
					sellAmount = "-";
					rate = "-";
					buyAmount="-";
				} else {
					if (secondStatus.equalsIgnoreCase(ConstantGloble.FOREX_STATUS)) {
						sellAmount = (String) map.get(Forex.FOREX_SECONDSELLAMOUNT_RES);
						rate = (String) map.get(Forex.FOREX_SECONDRATE_RES);
						buyAmount=(String) map.get(Forex.FOREX_SECONDBUYAMOUNT_RES);
					} else {
						sellAmount = "-";
						rate = "-";
						buyAmount="-";
					}
				}
			}
			Intent intent = new Intent(ForexStrikeQueryActivity.this, ForexStrikeQueryDetailActivity.class);
			intent.putExtra(Forex.FOREX_CONSIGNNUMBER_RES, consignNumber);
			intent.putExtra(Forex.FOREX_FIRESTSELLCURRENCY_RES, sellCode);
			intent.putExtra(ConstantGloble.FOREX_SELLAMOUNT_KEY, sellAmount);
			intent.putExtra(Forex.FOREX_FIRSTBUYCURRENY_RES, buyCode);
			intent.putExtra(ConstantGloble.FOREX_RATE_KEY, rate);
			intent.putExtra(Forex.FOREX_EXCHANGETRANTYPE_RES, exchangeTranType);
			intent.putExtra(Forex.FOREX_REALTRANSTIME, realTransTime);
			intent.putExtra(Forex.FOREX_SECONDBUYAMOUNT_RES, buyAmount);
			startActivity(intent);
		}
	};

	/** 时间排序 */
	public List<Map<String, Object>> datesort(List<Map<String, Object>> list) {
		if (!list.isEmpty()) {
			Collections.sort(list, new Comparator<Map<String, Object>>() {

				@Override
				public int compare(Map<String, Object> object1, Map<String, Object> object2) {
					return (((String) object2.get(Forex.FOREX_REALTRANSTIME)).compareTo((String) object1
							.get(Forex.FOREX_REALTRANSTIME)));
				}
			});
		}
		return list;
	}

	/** 设置查询条件的时间 */
	private void setConditionTimes() {
		startEdit.setText(startDateStr);
		endEdit.setText(endDateStr);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.forex_query_deal_startdate:// 开始日期

			Calendar c = Calendar.getInstance(); // 实例化一个日期与时间操作对象
			QueryDateUtils.checkDate(startEdit, c);
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息 DatePickerDialog
			DatePickerDialog dialog = new DatePickerDialog(ForexStrikeQueryActivity.this, new OnDateSetListener() {
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
			DatePickerDialog dialog1 = new DatePickerDialog(ForexStrikeQueryActivity.this, new OnDateSetListener() {

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
			cleanDate();
			startDateStr = startEdit.getText().toString().trim();
			endDateStr = endEdit.getText().toString().trim();
			if (QueryDateUtils.compareDateOneYear(startDateStr, dateTime)) {
				// 开始日期在系统日期前一年以内
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						ForexStrikeQueryActivity.this.getString(R.string.acc_check_start_enddate));
				return;
			}
			if (QueryDateUtils.compareDate(endDateStr, dateTime)) {
				// 结束日期在系统日期之前
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						ForexStrikeQueryActivity.this.getString(R.string.acc_check_enddate));
				return;
			}

			if (QueryDateUtils.compareDate(startDateStr, endDateStr)) {
				// 开始日期在结束日期之前
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						ForexStrikeQueryActivity.this.getString(R.string.forex_query_times1));//
				return;
			}
			if (QueryDateUtils.compareDateThree(startDateStr, endDateStr)) {
				// 起始日期与结束日期最大间隔为三个自然月
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						ForexStrikeQueryActivity.this.getString(R.string.acc_check_start_end_date));
				return;
			}
			setConditionTimes();
			setTimeValue();
			BaseHttpEngine.showProgressDialog();
			requestPsnForexAllTransQuery(startDateStr, endDateStr, ConstantGloble.FOREX_QUERYTYPE,
					ConstantGloble.FOREX_PAGESIZE, ConstantGloble.FOREX_CURRENTINDEX, ConstantGloble.FOREX_REFRESH);

			break;
		case R.id.forex_queryquery_deal_queryoneweek:// 最近一周
			showCondition();
			cleanDate();
			startDateStr = QueryDateUtils.getlastWeek(dateTime).trim();
			endDateStr = QueryDateUtils.getcurrentDate(dateTime).trim();
			setTimeValue();
			BaseHttpEngine.showProgressDialog();
			requestPsnForexAllTransQuery(startDateStr, endDateStr, ConstantGloble.FOREX_QUERYTYPE,
					ConstantGloble.FOREX_PAGESIZE, ConstantGloble.FOREX_CURRENTINDEX, ConstantGloble.FOREX_REFRESH);
			break;
		case R.id.forex_queryquery_deal_queryonemouth:// 最近一月
			showCondition();
			cleanDate();
			startDateStr = QueryDateUtils.getlastOneMonth(dateTime).trim();
			endDateStr = QueryDateUtils.getcurrentDate(dateTime).trim();
			setTimeValue();
			BaseHttpEngine.showProgressDialog();
			requestPsnForexAllTransQuery(startDateStr, endDateStr, ConstantGloble.FOREX_QUERYTYPE,
					ConstantGloble.FOREX_PAGESIZE, ConstantGloble.FOREX_CURRENTINDEX, ConstantGloble.FOREX_REFRESH);

			break;
		case R.id.forex_queryquery_deal_querythreemouths:// 最近三月
			showCondition();
			cleanDate();
			startDateStr = QueryDateUtils.getlastThreeMonth(dateTime).trim();
			endDateStr = QueryDateUtils.getcurrentDate(dateTime).trim();
			setTimeValue();
			BaseHttpEngine.showProgressDialog();
			requestPsnForexAllTransQuery(startDateStr, endDateStr, ConstantGloble.FOREX_QUERYTYPE,
					ConstantGloble.FOREX_PAGESIZE, ConstantGloble.FOREX_CURRENTINDEX, ConstantGloble.FOREX_REFRESH);
			break;
		case R.id.finc_listiterm_tv1:// 显示更多
			isRefresh = false;
			currentIndex += pageSize;
			if (currentIndex < number) {
				BaseHttpEngine.showProgressDialog();
				requestPsnForexAllTransQuery(startDateStr, endDateStr, ConstantGloble.FOREX_QUERYTYPE,
						String.valueOf(pageSize), String.valueOf(currentIndex), String.valueOf(isRefresh));
				LogGloble.d(TAG + " currentIndex", currentIndex + "");
			}
			break;
		default:
			break;
		}
	}

	/** 隐藏查询结果页面 */
	private void showCondition() {
		searchResultAllView.setVisibility(View.INVISIBLE);
		searchConditionView.setBackgroundResource(R.drawable.img_bg_query_j);
	}
}