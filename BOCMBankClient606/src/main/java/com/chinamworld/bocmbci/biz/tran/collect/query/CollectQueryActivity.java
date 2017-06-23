package com.chinamworld.bocmbci.biz.tran.collect.query;

import java.util.ArrayList;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Collect;
import com.chinamworld.bocmbci.bii.constant.Collect.CollectResultType;
import com.chinamworld.bocmbci.bii.constant.GatherInitiative;
import com.chinamworld.bocmbci.bii.constant.Third;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.biz.tran.collect.CollectBaseActivity;
import com.chinamworld.bocmbci.biz.tran.collect.CollectData;
import com.chinamworld.bocmbci.biz.tran.collect.adapter.CollectQueryAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.SearchView;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: CollectQueryActivity
 * @Description: 跨行资金归集查询
 * @author luql
 * @date 2014-3-18 下午02:36:30
 */
public class CollectQueryActivity extends CollectBaseActivity {
	// private LayoutInflater inflater = null;
	private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

	/** 归集账户 */
	private String mPayeeAccount;
	/** 被归集账户 */
	private String mPayerAccount;
	/** 归集账户 */
	private Map<String, Object> mPayeeData;
	/** 被归集账户数据 */
	private Map<String, Object> mCollectData;

	/** 底部listview Layout */
	private LinearLayout listContentLayout;
	/** 未查询的时候提示语言 */
	private TextView textNotifyInfo;

	private ListView listView;
	/** 下拉箭头 */
	private LinearLayout ivPullDown;
	/** 收起箭头 */
	private LinearLayout ivPullUp;
	/** 列表数据的adapter */
	private CollectQueryAdapter adapter;

	/** * 最近一周 */
	private Button oneWeekBtn = null;
	/** * 最近一月 */
	private Button oneMonthBtn = null;
	/** * 最近三月 */
	private Button threeMonthBtn = null;
	/** 当期系统时间 */
	private String currentTime;
	/** 开始日期str */
	private String startDate;
	/** 结束日期str */
	private String endDate;
	/** 开始日期 */
	private TextView textStartDate;
	/** 结束日期 */
	private TextView textEndDate;
	/** 查询后的日期范围 */
	private TextView queryDateContent;
	/** 下拉布局 */
	private RelativeLayout popupLayout;
	/** 当前请求list数据的index */
	private int mCurrentIndex = 0;
	/** 总条目数 */
	private int totalCount = 0;
	/** listview的更多条目 */
	private View viewFooter;
	private Button btnMore;

	/** 记录当前的Pop窗口状态 */
	private boolean isPullDownPop = false;
	private View mViewContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewContent = addView(R.layout.collect_query_activity);
		setTitle(getString(R.string.collect_detail));
		if (getIntentData()) {
			findViewById(R.id.sliding_body).setPadding(0, 0, 0, 0);
			toprightBtn();
			BaseHttpEngine.showProgressDialogCanGoBack();
			requestCommConversationId();
		} else {
			finish();
		}
	}

	private boolean getIntentData() {
		CollectData data = CollectData.getInstance();
		Intent intent = getIntent();
		mPayeeAccount = intent.getStringExtra(Collect.cbAccCard);
		mPayerAccount = intent.getStringExtra(Collect.payerAccount);
		mPayeeData = data.getAccountData(mPayeeAccount);
		mCollectData = data.getCollectData(mPayeeAccount, mPayerAccount);
		return mCollectData != null && mPayeeData != null;
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestSystemDateTime();
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		currentTime = dateTime;
		View rootView = mViewContent.findViewById(R.id.collect_query_layout);
		rootView.setVisibility(View.VISIBLE);
		init();
	}

	/**
	 * @Title: init
	 * @Description: 初始化view和控件
	 * @param
	 * @return void
	 */
	private void init() {
		popupLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.gather_query_popwindow, null);
		LinearLayout collect_query=(LinearLayout) mViewContent.findViewById(R.id.collect_query);
		SearchView.getInstance().addSearchView(collect_query, popupLayout);
//		PopupWindowUtils.getInstance().getQueryPopupWindow(popupLayout, this);
		queryDateContent = (TextView) findViewById(R.id.collect_query_cdnumber_tv);
		// 收起箭头
		ivPullUp = (LinearLayout) popupLayout.findViewById(R.id.ll_pull_up_query_preandexe);
		initQueryBeforeLayout();
		initQueryAfterLayout();
//		PopupWindowUtils.getInstance().showQueryPopupWindowFirst();
		SearchView.getInstance().showSearchView();
		ivPullUp.setClickable(false);
		// excuseQuery();
	}

	/**
	 * 查询后layout
	 */
	private void initQueryAfterLayout() {
		// 下拉箭头
		ivPullDown = (LinearLayout) findViewById(R.id.img_arrow_down);
		ivPullDown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				PopupWindowUtils.getInstance().getQueryPopupWindow(popupLayout, CollectQueryActivity.this);
//				PopupWindowUtils.getInstance().showQueryPopupWindow();
				SearchView.getInstance().showSearchView();
			}
		});

		listContentLayout = (LinearLayout) this.findViewById(R.id.collect_account_list_layout);
		textNotifyInfo = (TextView) this.findViewById(R.id.text_collect_query_notify_content);
		// LinearLayout headerLayout = (LinearLayout)
		// LayoutInflater.from(this).inflate(R.layout.dept_notmg_list_item,
		// null);
		// TextView cdNumberTv = (TextView)
		// headerLayout.findViewById(R.id.dept_cd_number_tv);
		// TextView currencyTv = (TextView)
		// headerLayout.findViewById(R.id.dept_type_tv);
		// TextView availableBalanceTv = (TextView)
		// headerLayout.findViewById(R.id.dept_avaliable_balance_tv);
		// ImageView goDetailIv = (ImageView)
		// headerLayout.findViewById(R.id.dept_notify_detail_iv);
		// String strCdNumber =
		// this.getResources().getString(R.string.gather_creat_date);
		// cdNumberTv.setText(strCdNumber);
		// String strSaveType = this.getResources().getString(R.string.payer);
		// currencyTv.setText(strSaveType);
		// String strAvaiableBalance =
		// this.getResources().getString(R.string.isForex_query_result4);
		// availableBalanceTv.setText(strAvaiableBalance);
		// goDetailIv.setVisibility(View.INVISIBLE);
		// headerLayout.setClickable(false);
		// listContentLayout.addView(headerLayout, 0);
		listView = (ListView) findViewById(R.id.dept_notmg_querylist);
		viewFooter = View.inflate(this, R.layout.acc_load_more, null);
		//listView.addFooterView(viewFooter);
		btnMore = (Button) viewFooter.findViewById(R.id.btn_load_more);
		// btnMore.setBackgroundColor(Color.TRANSPARENT);
		//btnMore.setVisibility(View.GONE);
		btnMore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				requestPsnCBCollectTrsQueryList(false);
			}
		});

		initAfterLayout();
	}

	private void initQueryBeforeLayout() {
		textStartDate = (TextView) popupLayout.findViewById(R.id.tv_startdate_query_date_preandexe);
		textEndDate = (TextView) popupLayout.findViewById(R.id.tv_enddate_query_date_preandexe);
		textStartDate.setOnClickListener(chooseDateClick);
		textEndDate.setOnClickListener(chooseDateClick);
		// 三天前的日期
		String startThreeDate = QueryDateUtils.getlastthreeDate(currentTime);
		textStartDate.setText(startThreeDate);
		// 系统当前时间格式化
		String currenttime = QueryDateUtils.getcurrentDate(currentTime);
		// 初始结束时间赋值
		textEndDate.setText(currenttime);
		// 查询按钮
		Button queryBtn = (Button) popupLayout.findViewById(R.id.btn_query_date_preandexe);
		oneWeekBtn = (Button) popupLayout.findViewById(R.id.btn_oneweek_query_date_preandexe);
		oneMonthBtn = (Button) popupLayout.findViewById(R.id.btn_onemonth_query_date_preandexe);
		threeMonthBtn = (Button) popupLayout.findViewById(R.id.btn_threemonth_query_date_preandexe);
		// 查询最近一周
		oneWeekBtn.setOnClickListener(this);
		// 查询最近一月
		oneMonthBtn.setOnClickListener(this);
		// 查询最近三个月
		threeMonthBtn.setOnClickListener(this);
		queryBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				excuseQuery();
			}
		});

		ivPullUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
				SearchView.getInstance().hideSearchView();
			}
		});
		startDate = startThreeDate;
		endDate = currenttime;
	}

	/**
	 * @Title: excuseQuery
	 * @Description: 执行查询操作
	 * @param
	 * @return void
	 */
	private void excuseQuery() {
		String startDatePre = textStartDate.getText().toString().trim();
		String endDatePre = textEndDate.getText().toString().trim();
		if (QueryDateUtils.compareDateOneYear(startDatePre, currentTime)) {
			// 起始日期不能早于系统当前日期一年前
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.acc_check_start_enddate));
			return;
		}
		if (QueryDateUtils.compareDate(endDatePre, currentTime)) {
			// 结束日期在服务器日期之前
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.acc_check_enddate));
			return;
		}
		if (QueryDateUtils.compareDate(startDatePre, endDatePre)) {
			// 开始日期在结束日期之前

		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.acc_query_errordate));
			return;
		}
		if (!QueryDateUtils.compareDateThree(startDatePre, endDatePre)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.acc_check_start_end_date));
			return;
		}
		startDate = startDatePre;
		endDate = endDatePre;
		// mCurrentIndex = 0;
		listData.clear();
		//refreshListView(listData);
		requestPsnCBCollectTrsQueryList(true);
	}

	/** 设置查询日期 */
	OnClickListener chooseDateClick = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			TextView tv = (TextView) v;
			String time = tv.getText().toString();
			int startYear = Integer.parseInt(time.substring(0, 4));
			int startMonth = Integer.parseInt(time.substring(5, 7));
			int startDay = Integer.parseInt(time.substring(8, 10));
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			DatePickerDialog dialog = new DatePickerDialog(CollectQueryActivity.this, new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					StringBuilder date = new StringBuilder();
					date.append(String.valueOf(year));
					date.append("/");
					int month = monthOfYear + 1;
					date.append(((month < 10) ? ("0" + month) : (month + "")));
					date.append("/");
					date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth) : (dayOfMonth + "")));
					// 为日期赋值
					((TextView) v).setText(String.valueOf(date));
				}
			}, startYear, startMonth - 1, startDay);
			dialog.show();
		}
	};

	/**
	 * 查询后layout
	 * 
	 * @param view
	 */
	private void initAfterLayout() {
		queryDateContent.setText(startDate + "-" + endDate);
	}

	/**
	 * 列表
	 */
	private void refreshListView(List<Map<String, Object>> listData) {
		if (adapter == null) {
			adapter = new CollectQueryAdapter(this, listData);
			adapter.setOnClickListener(listener);
			listView.setAdapter(adapter);
			// listView.setOnItemClickListener(listViewItemClickListener);
		} else {
			adapter.setListData(listData);
			adapter.notifyDataSetChanged();
		}
	}

	private View.OnClickListener listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Map<String, Object> map = (Map<String, Object>) v.getTag();
			if (map != null) {
				Intent intent = new Intent(CollectQueryActivity.this, CollectQueryDetailActivity.class);
				intent.putExtra(Collect.workNo, (String) map.get(Collect.workNo));
				intent.putExtra(Collect.transDate, (String) map.get(Collect.transDate));
				intent.putExtra(Collect.sccCount, (String) map.get(Collect.sccCount));
				intent.putExtra(Collect.sumCount, (String) map.get(Collect.sumCount));
				intent.putExtra(Collect.amount, (String) map.get(Collect.transAmt));
				startActivity(intent);
			}
		}
	};

	/**
	 * 跨行资金归集执行明细查询
	 * @param isRefresh
	 * @return
	 */
	public void requestPsnCBCollectTrsQueryList(boolean isRefresh) {
		if (isRefresh) {
			mCurrentIndex = 0;
		} else {
			mCurrentIndex += Third.PAGESIZE;
		}
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Collect.PsnCBCollectTrsQuery);
		Map<String, Object> params = new HashMap<String, Object>();

		params.put(Collect.ruleNo, mCollectData.get(Collect.rusleNo));
		params.put(Collect.cbQueryStatus, CollectResultType.ALL);
		params.put(GatherInitiative.START_DATE, startDate);
		params.put(GatherInitiative.END_DATE, endDate);
		params.put(PubConstants.PUB_QUERY_FEILD_PAGE_SIZE, String.valueOf(Third.PAGESIZE));
		params.put(PubConstants.PUB_QUERY_FEILD_CURRENT_INDEX, EpayUtil.getString(mCurrentIndex, "0"));
		params.put(PubConstants.PUB_QUERY_FEILD_IS_REFRESH, EpayUtil.getString(isRefresh, "true"));
		biiRequestBody.setParams(params);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));

		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnCBCollectTrsQueryListListCallback");
	}

	/**
	 * @Title: requestPsnCBCollectTrsQueryListListCallback
	 * @Description: 请求“跨行资金归集执行明细查询”接口回调
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnCBCollectTrsQueryListListCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> map = (Map<String, Object>) biiResponseBody.getResult();
		String recordNumber = (String) map.get(Collect.totalItems);
		if (!StringUtil.isNullOrEmpty(recordNumber)) {
			totalCount = Integer.parseInt(recordNumber);
		}
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) map.get(Collect.list);
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getResources().getString(R.string.no_list_data));
			listData.clear();
			showQueryResultView(false);
			return;
		}
		for (int i = 0; i < resultList.size(); i++) {
			listData.add((Map<String, Object>) resultList.get(i));
		}
		if (listData.size() >= totalCount) {
			//btnMore.setVisibility(View.GONE);
			listView.removeFooterView(viewFooter);
		} else {
			// mCurrentIndex += Integer.parseInt(pageSize);
			//btnMore.setVisibility(View.VISIBLE);
			if (listView.getFooterViewsCount() > 0) {
				
			} else {
				listView.addFooterView(viewFooter);
			}
		}
		showQueryResultView(true);
	}

	@Override
	protected void onPause() {
//		isPullDownPop = PopupWindowUtils.getInstance().isQueryPopupShowing();
		super.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
//		if (isPullDownPop) {
//			ivPullDown.postDelayed(new Runnable() {
//				@Override
//				public void run() {
//					if (isPullDownPop) {
//						// ivPullDown.performClick();
//						PopupWindowUtils.getInstance().showQueryPopupWindow();
//						isPullDownPop = false;
//					}
//				}
//			}, 500);
//		}
	}

	/**
	 * @Title: showQueryResultView
	 * @Description: 展示查询后的页面
	 * @param
	 * @return void
	 */
	private void showQueryResultView(boolean isDissPop) {
		if (isDissPop) {
			isPullDownPop = false;
//			PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
			SearchView.getInstance().hideSearchView();
			listContentLayout.setVisibility(View.VISIBLE);
			textNotifyInfo.setVisibility(View.GONE);
			queryDateContent.setText(startDate + "-" + endDate);
			ivPullUp.setClickable(true);
			initAfterLayout();
			refreshListView(listData);
		} else {// 查询结果数据为空的时候
			ivPullUp.setClickable(false);
			listContentLayout.setVisibility(View.GONE);
			textNotifyInfo.setVisibility(View.VISIBLE);
			refreshListView(listData);
		}

	}

	/**
	 * @Title: changeDateText
	 * @Description: 重置开始日期和结束日期的值
	 * @param @param startDate
	 * @param @param endDate
	 * @return void
	 * @throws
	 */
	private void changeDateText(String startDate, String endDate) {
		// textStartDate.setText(startDate);
		// textEndDate.setText(endDate);
		queryDateContent.setText(startDate + "-" + endDate);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_oneweek_query_date_preandexe:// 最近一周
			startDate = QueryDateUtils.getlastWeek(dateTime).trim();
			endDate = QueryDateUtils.getcurrentDate(dateTime).trim();
			// mCurrentIndex = 0;
			listData.clear();
			refreshListView(listData);
			changeDateText(startDate, endDate);
			requestPsnCBCollectTrsQueryList(true);
			break;
		case R.id.btn_onemonth_query_date_preandexe:// 最近一月
			startDate = QueryDateUtils.getlastOneMonth(dateTime).trim();
			endDate = QueryDateUtils.getcurrentDate(dateTime).trim();
			// mCurrentIndex = 0;
			listData.clear();
			refreshListView(listData);
			changeDateText(startDate, endDate);
			requestPsnCBCollectTrsQueryList(true);
			break;
		case R.id.btn_threemonth_query_date_preandexe:// 最近三月
			startDate = QueryDateUtils.getlastThreeMonth(dateTime).trim();
			endDate = QueryDateUtils.getcurrentDate(dateTime).trim();
			// mCurrentIndex = 0;
			listData.clear();
			refreshListView(listData);
			changeDateText(startDate, endDate);
			requestPsnCBCollectTrsQueryList(true);
			break;
		default:
			break;
		}

	}
}
