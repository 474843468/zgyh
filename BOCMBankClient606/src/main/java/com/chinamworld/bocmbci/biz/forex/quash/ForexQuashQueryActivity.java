package com.chinamworld.bocmbci.biz.forex.quash;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
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
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.biz.forex.ForexBaseActivity;
import com.chinamworld.bocmbci.biz.forex.adapter.ForexQuashQueryAdapter;
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
import java.util.List;
import java.util.Map;

/** 委托交易状况查询 */
public class ForexQuashQueryActivity extends ForexBaseActivity implements OnClickListener {
	private static final String TAG = "ForexQuashQueryActivity";
	/** ForexStrikeQueryActivity的主布局 */
	private View rateInfoView = null;
	/** backButton:返回按钮 */
	private Button backButton = null;
	/** startEdit:开始日期 */
	private TextView startEdit = null;
	/** 结束日期 */
	private TextView endEdit = null;
	/** 查询按钮 */
	private Button queryButton = null;
	/** 最近一周 */
	private RadioButton weekButton = null;
	/** 最近一月 */
	private RadioButton monthButton = null;
	/** 最近三月 */
	private RadioButton threesButton = null;
	/** 开始查询日期 */
	private String startDateStr;
	/** 结束查询日期 */
	private String endDateStr;
	/** 查询页面，向上收起 */
	private ImageView searchUpImageView = null;
	/** 查询时间 */
	private TextView searchResultTimeText = null;
	/** 查询结果页面向下箭头 */
	private ImageView searchResultDownImageView = null;
	/** 查询结果listView */
	private ListView searchResultListView = null;
	/** 查询结果顶部---整个时间区域 */
	private View searchResultTopView = null;
	/** 查询页面收起箭头区域 */
	private View searchUpView = null;
	/** 查询条件---整个查询条件区域 */
	private View searchConditionView = null;
	/** 显示更多区域 */
	private RelativeLayout load_more;
	/** 主界面 */
	private Button btnRight = null;
	private String startTwoDate = null;
	/** 系统时间 */
	private String currenttime;
	// private RelativeLayout searchResultAllView = null;
	/** 显示更多 */
	private TextView moreButton = null;
	/** 当前有效委托 */
	private RadioButton currentQuashButton = null;
	/** 历史有效委托 */
	private RadioButton historyQuashButton = null;
	/** 查询结果整个区域 */
	private View allResultView = null;
	// /** 1-当前有效，2-历史有效 */
	// private int currentOrHistory = 1;
	/** 记录总数 */
	private String recordNumber = null;
	/** 记录总数 */
	private int number = 0;
	/** 当前页 */
	private int currentIndex = 0;
	private int pageSize = 10;
	/** 刷新标志 */
	private boolean isRefresh = true;
	private List<Map<String, Object>> currentQueryList = null;
	private ForexQuashQueryAdapter adapter = null;
	/** 退出动画 */
	private Animation animation_up;
	/** 进入动画 */
	private Animation animation_down;
	private String lastStartDate = null;
	private String lastEndDate = null;
	private String lastStartEdit = null;
	private String lastEndEdit = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		stopPollingFlag();
		taskTag = 7;
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
		setTitle(getResources().getString(R.string.forex_quash));
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		rateInfoView = LayoutInflater.from(ForexQuashQueryActivity.this).inflate(R.layout.forex_quash_query, null);
		tabcontent.addView(rateInfoView);
		setLeftButtonPopupGone();
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 0);
		}
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		currentOrHistory = 1;
		currentQueryList = new ArrayList<Map<String, Object>>();
		initanimation();
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
		searchConditionView = rateInfoView.findViewById(R.id.condition_layout);
		// 查询条件
		startEdit = (TextView) rateInfoView.findViewById(R.id.forex_query_deal_startdate);
		endEdit = (TextView) rateInfoView.findViewById(R.id.forex_query_deal_enddate);
		queryButton = (Button) rateInfoView.findViewById(R.id.forex_query_deal_query);
		weekButton = (RadioButton) rateInfoView.findViewById(R.id.forex_queryquery_deal_queryoneweek);
		monthButton = (RadioButton) rateInfoView.findViewById(R.id.forex_queryquery_deal_queryonemouth);
		threesButton = (RadioButton) rateInfoView.findViewById(R.id.forex_queryquery_deal_querythreemouths);
		// 查询条件页面-收起图片
		searchUpImageView = (ImageView) rateInfoView.findViewById(R.id.forex_query_up);
		// 查询条件页面-收起区域
		searchUpView = rateInfoView.findViewById(R.id.search_shouqi_up);
		// // 3天前的日期
		// startTwoDate = QueryDateUtils.getlastthreeDate(dateTime);
		// startEdit.setText(startTwoDate);
		// // 系统当前时间格式化
		// currenttime = QueryDateUtils.getcurrentDate(dateTime);
		// // 初始结束时间赋值
		// endEdit.setText(currenttime);
		searchConditionView.setVisibility(View.GONE);
	}

	/** 初始化查询结果页面的布局 */
	private void initResultView() {
		allResultView = findViewById(R.id.all_layout);
		// searchResultAllView = (RelativeLayout)
		// rateInfoView.findViewById(R.id.forex_query_result_layout);
		// 下拉图片
		searchResultDownImageView = (ImageView) rateInfoView.findViewById(R.id.img_acc_query_back);
		// 查询时间整个区域
		searchResultTopView = rateInfoView.findViewById(R.id.forex_query_result_condition);
		searchResultListView = (ListView) rateInfoView.findViewById(R.id.rate_listView);
		searchResultTimeText = (TextView) rateInfoView.findViewById(R.id.trade_time);
		currentQuashButton = (RadioButton) findViewById(R.id.rate_allButton);
		historyQuashButton = (RadioButton) findViewById(R.id.rate_customerButton);
		load_more = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.query_list_footer, null);
		moreButton = (TextView) load_more.findViewById(R.id.finc_listiterm_tv1);

	}

	private void initOnClick() {
		startEdit.setOnClickListener(this);
		endEdit.setOnClickListener(this);
		currentQuashButton.setOnClickListener(this);
		historyQuashButton.setOnClickListener(this);
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

	@SuppressWarnings("static-access")
	private void initanimation() {
		animation_up = new AnimationUtils().loadAnimation(ForexQuashQueryActivity.this, R.anim.scale_out);
		animation_down = new AnimationUtils().loadAnimation(ForexQuashQueryActivity.this, R.anim.scale_in);
	}

	/** 下拉事件 */
	private OnClickListener resultOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {// 下拉
			searchConditionView.setBackgroundResource(R.drawable.img_bg_query_no);
			// 显示查询条件
			animation_down.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					searchConditionView.setVisibility(View.VISIBLE);
					searchResultTopView.setVisibility(View.GONE);
				}
			});
			searchConditionView.startAnimation(animation_down);
			searchResultTopView.startAnimation(animation_up);
		}
	};

	/** 收起事件 */
	private OnClickListener searchUpOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 关闭查询条件
			if (currentQueryList == null || currentQueryList.size() <= 0) {

			} else {
				animation_up.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						searchConditionView.setVisibility(View.GONE);
						searchResultTopView.setVisibility(View.VISIBLE);
					}
				});
				searchConditionView.startAnimation(animation_up);
				searchResultTopView.startAnimation(animation_down);
			}
		}
	};

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		iscomformFootFastOrOther();
	}

	/** 用于区分是否是快捷键启动 */
	private void iscomformFootFastOrOther() {
		if (getIntent().getBooleanExtra(ConstantGloble.COMEFROMFOOTFAST, false)) {
			// 快捷键启动
			// 判断用户是否开通投资理财服务
			requestPsnInvestmentManageIsOpen();
		} else {
			if (isOpen && isSettingAcc) {
				// 请求系统时间
				initRequest();
			} else {
				// 判断用户是否开通投资理财服务
				requestPsnInvestmentManageIsOpen();
			}
		}
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
	@SuppressWarnings("unchecked")
	public void requestPsnForexActIssetCallback(Object resultObj) {
		Map<String, Object> result = getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			isSettingAcc = false;
		} else {
			if (StringUtil.isNullOrEmpty(result.get(Forex.FOREX_RATE_INVESTBINDINGINFO_RES))) {
				isSettingAcc = false;
			} else {
				investBindingInfo = (Map<String, String>) result.get(Forex.FOREX_RATE_INVESTBINDINGINFO_RES);
				BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_RATE_INVESTBINDINGINFO_RES, investBindingInfo);

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
			initRequest();
		}
	}

	private void initRequest() {
		BaseHttpEngine.canGoBack = false;
		// 初始化查询结果页面
		initResultView();
		// 初始化查询条件页面
		init();
		initOnClick();
		choiceLeftButton();
		requestSystemDateTime();
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		Map<String, Object> resultMap = getHttpTools().getResponseResult(resultObj);
		dateTime = (String) resultMap.get(Comm.DATETME);
		if (StringUtil.isNullOrEmpty(dateTime)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			// 3天前的日期
			startTwoDate = QueryDateUtils.getlastthreeDate(dateTime);
			startEdit.setText(startTwoDate);
			// 系统当前时间格式化
			currenttime = QueryDateUtils.getcurrentDate(dateTime);
			// 初始结束时间赋值
			endEdit.setText(currenttime);
			requestPsnForexAllTransQuery1(null, null, ConstantGloble.FOREX_CURRENTTYPE, String.valueOf(pageSize), String.valueOf(currentIndex), String.valueOf(isRefresh));
		}
	}

	/** 历史委托查询 */
	private void weituoQuery() {
		// 查询三天前的交易
		startDateStr = startTwoDate;
		endDateStr = currenttime;
		startEdit.setText(startTwoDate);
		endEdit.setText(endDateStr);
		setTimeValue();
		// requestPsnForexAllTransQuery1(startDateStr, endDateStr,
		// ConstantGloble.FOREX_HISTORYTYPE,
		// String.valueOf(pageSize), String.valueOf(currentIndex),
		// String.valueOf(isRefresh));
	}

	/** 选中当前有效委托状况查询 */
	private void choiceLeftButton() {
//		currentQuashButton.setBackgroundResource(R.drawable.btn_2_red_left);
//		currentQuashButton.setTextColor(getResources().getColor(R.color.white));
//		historyQuashButton.setBackgroundResource(R.drawable.btn_2_gray_right);
//		historyQuashButton.setTextColor(getResources().getColor(R.color.black));
//		currentQuashButton.setFocusable(true);
//		currentQuashButton.setPressed(true);
		currentQuashButton.setChecked(true);
//		historyQuashButton.setPressed(false);
//		historyQuashButton.setFocusable(false);
//		currentQuashButton.setChecked(false);
		searchResultTopView.setVisibility(View.GONE);
		allResultView.setVisibility(View.VISIBLE);
		searchConditionView.setVisibility(View.GONE);
		currentOrHistory = 1;

	}

	/** 选中历史委托状况查询 */
	private void choiseHiatoryButton() {
		searchConditionView.setBackgroundResource(R.drawable.img_bg_query_j);
//		currentQuashButton.setBackgroundResource(R.drawable.btn_2_gray_left);
//		historyQuashButton.setBackgroundResource(R.drawable.btn_2_red_right);
//		historyQuashButton.setTextColor(getResources().getColor(R.color.white));
//		currentQuashButton.setTextColor(getResources().getColor(R.color.black));
//		historyQuashButton.setFocusable(true);
//		historyQuashButton.setPressed(true);
		historyQuashButton.setChecked(true);
//		currentQuashButton.setFocusable(false);
//		currentQuashButton.setPressed(false);
//		currentQuashButton.setChecked(false);
		currentOrHistory = 2;
		animation_down.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				searchResultTopView.setVisibility(View.GONE);
				allResultView.setVisibility(View.INVISIBLE);
				searchConditionView.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
			}
		});
		searchConditionView.startAnimation(animation_down);
	}

	/** 查询结果页面-查询时间赋值 */
	private void setTimeValue() {
//		String text = getResources().getString(R.string.acc_query_date);
		searchResultTimeText.setText(/*text + */startDateStr + "-" + endDateStr);
	}

	/**
	 * 当前有效委托状况查询--回调
	 * 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnCurrentTransQueryCallback(Object resultObj) {
		Map<String, Object> data = getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(data)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		} else {
			if (!data.containsKey(Forex.FOREX_TRADE_LIST_RES)) {
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
				return;
			}
			List<Map<String, Object>> lists = (List<Map<String, Object>>) data.get(Forex.FOREX_TRADE_LIST_RES);
			if (lists == null || lists.size() <= 0) {
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
				return;
			}
			currentQueryList.addAll(lists);

			recordNumber = (String) data.get(Forex.FOREX_RECORDNUMBER_RES);
			if (StringUtil.isNull(recordNumber)) {
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
				return;
			}
			LogGloble.d(TAG + " recordNumber", recordNumber);
			number = Integer.valueOf(recordNumber);
			if (number > pageSize && isRefresh) {
				// 显示更多按钮
				searchResultListView.addFooterView(load_more);
			}
			if (!isRefresh) {
				if (currentIndex + pageSize >= number) {
					searchResultListView.removeFooterView(load_more);
				}
			}
			currentQueryList = dealQueryDate(currentQueryList);
			if (currentQueryList == null || currentQueryList.size() <= 0) {
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
				return;
			}
			BaseHttpEngine.dissMissProgressDialog();
			if (!isRefresh) {
				// 数据往后加载
				adapter.dataChanged(currentQueryList);
			} else {
				adapter = new ForexQuashQueryAdapter(ForexQuashQueryActivity.this, currentQueryList, currentOrHistory);
				searchResultListView.setAdapter(adapter);
			}
			adapter.setOnItemClickListener(listener);
		}
	}

	private OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			Map<String, Object> map = currentQueryList.get(position);
			Intent intent = new Intent(ForexQuashQueryActivity.this, ForexQuashQueryDateilActivity.class);
			intent.putExtra(ConstantGloble.FOREX_TAG, currentOrHistory);
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_TRADE_LIST_KEY, map);
			if (currentOrHistory == 1) {
				// 当前有效委托
				startActivity(intent);
			} else if (currentOrHistory == 2) {
				// 历史有效委托
				lastStartDate = startDateStr;
				lastEndDate = endDateStr;
				lastStartEdit = startEdit.getText().toString().trim();
				lastEndEdit = endEdit.getText().toString().trim();
				startActivityForResult(intent, ConstantGloble.ISFOREX_WT_ACTIVITY);
			}
		}
	};

	/**
	 * 历史有效委托状况查询--回调
	 * 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnHistoryTransQueryCallback(Object resultObj) {
		Map<String, Object> data = getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(data)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		} else {
			if (!data.containsKey(Forex.FOREX_TRADE_LIST_RES)) {
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
				return;
			}
			List<Map<String, Object>> lists = (List<Map<String, Object>>) data.get(Forex.FOREX_TRADE_LIST_RES);
			if (lists == null || lists.size() <= 0) {
				// 没有数据
				BaseHttpEngine.dissMissProgressDialog();
				searchConditionView.setVisibility(View.VISIBLE);
				allResultView.setVisibility(View.GONE);
				BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
				return;
			}
			currentQueryList.addAll(lists);
			recordNumber = (String) data.get(Forex.FOREX_RECORDNUMBER_RES);
			if (StringUtil.isNull(recordNumber)) {
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
				return;
			}
			LogGloble.d(TAG + " recordNumber", recordNumber);
			number = Integer.valueOf(recordNumber);
			if (number > pageSize && isRefresh) {
				// 显示更多按钮
				searchResultListView.addFooterView(load_more);
			}
			if (!isRefresh) {
				if (currentIndex + pageSize >= number) {
					searchResultListView.removeFooterView(load_more);
				}
			}

			currentQueryList = dealQueryDate(currentQueryList);

			if (currentQueryList == null || currentQueryList.size() <= 0) {
				BaseHttpEngine.dissMissProgressDialog();
				searchConditionView.setVisibility(View.VISIBLE);
				allResultView.setVisibility(View.INVISIBLE);
				BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
				return;
			} else {
				animation_up.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						searchConditionView.setVisibility(View.GONE);
						searchResultTopView.setVisibility(View.VISIBLE);
						allResultView.setVisibility(View.VISIBLE);
					}
				});
				searchConditionView.startAnimation(animation_up);
				BaseHttpEngine.dissMissProgressDialog();
				if (!isRefresh) {
					// 数据往后加载
					adapter.dataChanged(currentQueryList);
				} else {
					adapter = new ForexQuashQueryAdapter(ForexQuashQueryActivity.this, currentQueryList, currentOrHistory);
					searchResultListView.setAdapter(adapter);
				}
				adapter.setOnItemClickListener(listener);
			}
		}
	}

	/** 处理查询结果 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> dealQueryDate(List<Map<String, Object>> list) {
		int len = list.size();
		List<Map<String, Object>> dealList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < len; i++) {
			Map<String, Object> map = list.get(i);
			Map<String, String> firstBuyCurrency = (Map<String, String>) map.get(Forex.FOREX_FIRSTBUYCURRENY_RES);
			if (StringUtil.isNullOrEmpty(firstBuyCurrency)) {
				continue;
			}
			// 买入币种
			String buyCode = firstBuyCurrency.get(Forex.FOREX_CODE_RES);
			Map<String, String> firstSellCurrency = (Map<String, String>) map.get(Forex.FOREX_FIRESTSELLCURRENCY_RES);
			if (StringUtil.isNullOrEmpty(firstSellCurrency)) {
				continue;
			}
			// 卖出币种
			String sellCode = firstSellCurrency.get(Forex.FOREX_CODE_RES);
			if (StringUtil.isNull(buyCode) || !LocalData.Currency.containsKey(buyCode) || StringUtil.isNull(sellCode) || !LocalData.Currency.containsKey(sellCode)) {
				continue;
			}
			dealList.add(map);
		}
		return dealList;
	}

	/** 设置查询条件的时间 */
	private void setConditionTimes() {
		startEdit.setText(startDateStr);
		endEdit.setText(endDateStr);
	}

	private void clearTimes() {
		startDateStr = null;
		endDateStr = null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.forex_query_deal_startdate:// 开始日期

			Calendar c = Calendar.getInstance(); // 实例化一个日期与时间操作对象
			QueryDateUtils.checkDate(startEdit, c);
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息 DatePickerDialog
			DatePickerDialog dialog = new DatePickerDialog(ForexQuashQueryActivity.this, new OnDateSetListener() {
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
			DatePickerDialog dialog1 = new DatePickerDialog(ForexQuashQueryActivity.this, new OnDateSetListener() {

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
			if (StringUtil.isNull(startDateStr) || StringUtil.isNull(endDateStr)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(ForexQuashQueryActivity.this.getString(R.string.isForex_query_inputdate));
				return;
			}
			if (QueryDateUtils.compareDateOneYear(startDateStr, dateTime)) {
				// 开始日期在系统日期前一年以内
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(ForexQuashQueryActivity.this.getString(R.string.acc_check_start_enddate));
				return;
			}
			if (QueryDateUtils.compareDate(endDateStr, dateTime)) {
				// 结束日期在系统日期之前
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(ForexQuashQueryActivity.this.getString(R.string.acc_check_enddate));
				return;
			}

			if (QueryDateUtils.compareDate(startDateStr, endDateStr)) {
				// 开始日期在结束日期之前
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(ForexQuashQueryActivity.this.getString(R.string.forex_query_times1));//
				return;
			}
			if (QueryDateUtils.compareDateThree(startDateStr, endDateStr)) {
				// 起始日期与结束日期最大间隔为三个自然月
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(ForexQuashQueryActivity.this.getString(R.string.acc_check_start_end_date));
				return;
			}
			setConditionTimes();
			setTimeValue();
			BaseHttpEngine.showProgressDialog();
			requestPsnForexAllTransQuery1(startDateStr, endDateStr, ConstantGloble.FOREX_HISTORYTYPE, String.valueOf(pageSize), String.valueOf(currentIndex), String.valueOf(isRefresh));

			break;
		case R.id.forex_queryquery_deal_queryoneweek:// 最近一周
			showCondition();
			cleanDate();
			startDateStr = QueryDateUtils.getlastWeek(dateTime).trim();
			endDateStr = QueryDateUtils.getcurrentDate(dateTime).trim();
			setTimeValue();
			BaseHttpEngine.showProgressDialog();
			requestPsnForexAllTransQuery1(startDateStr, endDateStr, ConstantGloble.FOREX_HISTORYTYPE, String.valueOf(pageSize), String.valueOf(currentIndex), String.valueOf(isRefresh));
			break;
		case R.id.forex_queryquery_deal_queryonemouth:// 最近一月
			showCondition();
			cleanDate();
			startDateStr = QueryDateUtils.getlastOneMonth(dateTime).trim();
			endDateStr = QueryDateUtils.getcurrentDate(dateTime).trim();
			setTimeValue();
			BaseHttpEngine.showProgressDialog();
			requestPsnForexAllTransQuery1(startDateStr, endDateStr, ConstantGloble.FOREX_HISTORYTYPE, String.valueOf(pageSize), String.valueOf(currentIndex), String.valueOf(isRefresh));

			break;
		case R.id.forex_queryquery_deal_querythreemouths:// 最近三月
			showCondition();
			cleanDate();
			startDateStr = QueryDateUtils.getlastThreeMonth(dateTime).trim();
			endDateStr = QueryDateUtils.getcurrentDate(dateTime).trim();
			setTimeValue();
			BaseHttpEngine.showProgressDialog();
			requestPsnForexAllTransQuery1(startDateStr, endDateStr, ConstantGloble.FOREX_HISTORYTYPE, String.valueOf(pageSize), String.valueOf(currentIndex), String.valueOf(isRefresh));
			break;
		case R.id.finc_listiterm_tv1:// 显示更多
			isRefresh = false;
			currentIndex += pageSize;
			if (currentOrHistory == 1) {
				BaseHttpEngine.showProgressDialog();
				requestPsnForexAllTransQuery1(null, null, ConstantGloble.FOREX_CURRENTTYPE, String.valueOf(pageSize), String.valueOf(currentIndex), String.valueOf(isRefresh));
			} else if (currentOrHistory == 2) {
				BaseHttpEngine.showProgressDialog();
				requestPsnForexAllTransQuery1(startDateStr, endDateStr, ConstantGloble.FOREX_HISTORYTYPE, String.valueOf(pageSize), String.valueOf(currentIndex), String.valueOf(isRefresh));
				LogGloble.d(TAG + " currentIndex", currentIndex + "");
			}
			break;
		case R.id.rate_allButton:// 当前
			choiceLeftButton();
			cleanDate();
			clearTimes();
			BaseHttpEngine.showProgressDialog();
			requestPsnForexAllTransQuery1(null, null, ConstantGloble.FOREX_CURRENTTYPE, ConstantGloble.FOREX_PAGESIZE, ConstantGloble.FOREX_CURRENTINDEX, ConstantGloble.FOREX_REFRESH);
			break;
		case R.id.rate_customerButton:// 历史

			choiseHiatoryButton();
			cleanDate();
			weituoQuery();
			// BaseHttpEngine.showProgressDialog();
			break;
		default:
			break;
		}
	}

	/** 隐藏查询结果页面 */
	private void showCondition() {
		allResultView.setVisibility(View.INVISIBLE);
		searchConditionView.setBackgroundResource(R.drawable.img_bg_query_j);
	}

	private void cleanDate() {
		currentIndex = 0;
		isRefresh = true;
		recordNumber = null;
		number = 0;
		if (currentQueryList != null && !currentQueryList.isEmpty()) {
			currentQueryList.clear();
			if (adapter != null) {
				adapter.dataChanged(new ArrayList<Map<String, Object>>());
			}

		}
		// 移除更多按钮
		if (searchResultListView.getFooterViewsCount() > 0) {
			searchResultListView.removeFooterView(load_more);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:// 成功
			if (requestCode == ConstantGloble.ISFOREX_WT_ACTIVITY) {
				// 历史委托交易状况查询
				// 重新加载页面数据
				LogGloble.d(TAG, "onActivityResult===");
				BaseHttpEngine.dissMissProgressDialog();
				choiseHiatoryButton();
				cleanDate();
				startDateStr = lastStartDate;
				endDateStr = lastEndDate;
				startEdit.setText(lastStartEdit);
				endEdit.setText(lastEndEdit);
				setTimeValue();
				searchConditionView.setVisibility(View.GONE);
				BaseHttpEngine.showProgressDialog();
				requestPsnForexAllTransQuery1(startDateStr, endDateStr, ConstantGloble.FOREX_HISTORYTYPE, String.valueOf(pageSize), String.valueOf(currentIndex), String.valueOf(isRefresh));

			}
			break;
		case RESULT_CANCELED:
			break;
		default:
			break;
		}
	}

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (KeyEvent.KEYCODE_BACK == keyCode) {
////			ActivityTaskManager.getInstance().removeAllSecondActivity();
////			Intent intent = new Intent(ForexQuashQueryActivity.this, SecondMainActivity.class);
////			BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
//			goToMainActivity();
//		}
//		return super.onKeyDown(keyCode, event);
//	}
}
