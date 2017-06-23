package com.chinamworld.bocmbci.biz.loan.loanQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
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
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.biz.loan.adapter.LoanUseQueryResultAdapter;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 用款查询
 * 
 * @author wanbing
 * 
 */
public class LoanUseQueryActivity extends LoanBaseActivity implements OnClickListener {
	private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

	private Context context = this;
	private LinearLayout tabcontent;// 主Activity显示
	private View view = null;

	private ListView listView;
	/** 下拉箭头 */
	private LinearLayout ivPullDown;
	/** 收起箭头 */
	private LinearLayout ivPullUp;
	/** 列表数据的adapter */
	private LoanUseQueryResultAdapter adapter;

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
	/** 查询后的账户 */
	private TextView queryAccContent;
	/** 下拉布局 */
//	private RelativeLayout popupLayout;
	/** 当前请求list数据的index */
	private int mCurrentIndex = 1;
	/** 总条目数 */
	private int totalCount = 0;
	/** listview的更多条目 */
	private View viewFooter;
	private Button btnMore;
	private int pageSize = 10;
	/** 记录当前的Pop窗口状态 */
//	private boolean isPullDownPop = false;
	private String loanActNum;
	private View resultTopView = null;
//	private View conditionView = null;
	private String currency = null;
//	private View result_main_view = null;
	String sCurrentIndex=null;
	/**查询框*/
	private LinearLayout layoutQuery;
	/**查询详情页面*/
	private RelativeLayout layoutResult;
	/**第一次查询标识 用来清空数据刷新adaptet*/
	private Boolean isFirst =  true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.loan_use_query);
		// 添加布局
		setLeftSelectedPosition("loan_1");
//		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
//		view = mInflater.inflate(R.layout.loan_use_query, null);
//		Button btnRight = (Button) findViewById(R.id.ib_top_right_btn);
//		btnRight.setVisibility(View.VISIBLE);
//		tabcontent.addView(view);
//		tabcontent.setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.common_bottom_padding_new));

		loanActNum = getIntent().getStringExtra("loanActNum");
		currency = getIntent().getStringExtra(Loan.LOANACC_CURRENCYCODE_RES);
		BaseHttpEngine.showProgressDialogCanGoBack();
		requestSystemDateTime();
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		currentTime = dateTime;
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		view = mInflater.inflate(R.layout.loan_use_query, null);
		Button btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setVisibility(View.VISIBLE);
		tabcontent.addView(view);
		tabcontent.setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.common_bottom_padding_new));
		View rootView = findViewById(R.id.dept_after_query_layout);
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
//		popupLayout = (RelativeLayout) LayoutInflater.from(this)
//				.inflate(R.layout.loan_query_query_condition, null);
//		PopupWindowUtils.getInstance().getQueryPopupWindow(popupLayout, BaseDroidApp.getInstanse().getCurrentAct());
		
		
//		conditionView = popupLayout.findViewById(R.id.ll_query_condition);
		
		
//		result_main_view = findViewById(R.id.result_main);
		initQueryBeforeLayout();
		initQueryAfterLayout();
//		PopupWindowUtils.getInstance().showQueryPopupWindowFirst();
		ivPullUp.setClickable(false);
	}

	/**
	 * 查询后layout
	 */
	private void initQueryAfterLayout() {
		resultTopView = findViewById(R.id.layout_the_top);
		layoutResult = (RelativeLayout)findViewById(R.id.has_acc);
		queryDateContent = (TextView) findViewById(R.id.dept_query_cdnumber_tv);
		queryAccContent = (TextView) findViewById(R.id.tv_query_acc);
		// 下拉箭头
		ivPullDown = (LinearLayout) findViewById(R.id.img_arrow_down);
		ivPullDown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				conditionView.setBackgroundResource(R.drawable.img_bg_query_no);
//				PopupWindowUtils.getInstance().getQueryPopupWindow(popupLayout,
//						BaseDroidApp.getInstanse().getCurrentAct());
//				PopupWindowUtils.getInstance().showQueryPopupWindow();
				layoutQuery.setVisibility(View.VISIBLE);
				resultTopView.setVisibility(View.GONE);
				layoutResult.setVisibility(View.VISIBLE);
				
			}
		});

		listView = (ListView) findViewById(R.id.dept_notmg_querylist);
		viewFooter = mInflater.inflate(R.layout.acc_load_more, null);
		listView.addFooterView(viewFooter);
		btnMore = (Button) viewFooter.findViewById(R.id.btn_load_more);
		btnMore.setBackgroundColor(Color.TRANSPARENT);
		btnMore.setVisibility(View.GONE);
		// 更多按钮
		btnMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCurrentIndex++;
				sCurrentIndex=String.valueOf(mCurrentIndex);
				BaseHttpEngine.showProgressDialog();
				requestPsnLOANUseRecordsQuery();
			}
		});

//		initAfterLayout();
	}

	/** 点击查询时，背景处理 */
	private void clickQuery() {
//		result_main_view.setVisibility(View.INVISIBLE);
//		conditionView.setBackgroundResource(R.drawable.img_bg_query_j);
	}

	private void initQueryBeforeLayout() {
		System.out.println("currentTime====="+currentTime);
		layoutQuery = (LinearLayout)findViewById(R.id.layout_query);
		// 收起箭头
		ivPullUp = (LinearLayout) findViewById(R.id.ll_pull_up_query_preandexe);
		textStartDate = (TextView) findViewById(R.id.tv_startdate_query_date_preandexe);
		textEndDate = (TextView) findViewById(R.id.tv_enddate_query_date_preandexe);
		textStartDate.setOnClickListener(chooseDateClick);
		textEndDate.setOnClickListener(chooseDateClick);
		// 3天前的日期
		String startLastOneMonth = QueryDateUtils.getlastthreeDate(currentTime);
		textStartDate.setText(startLastOneMonth);
		// 系统当前时间格式化
		String currenttime = QueryDateUtils.getcurrentDate(currentTime);
		// 初始结束时间赋值
		textEndDate.setText(currenttime);
		// 查询按钮
		Button queryBtn = (Button) findViewById(R.id.btn_query_date_preandexe);
		oneWeekBtn = (Button) findViewById(R.id.btn_oneweek_query_date_preandexe);
		oneMonthBtn = (Button) findViewById(R.id.btn_onemonth_query_date_preandexe);
		threeMonthBtn = (Button) findViewById(R.id.btn_threemonth_query_date_preandexe);
		// 查询最近一周
		oneWeekBtn.setOnClickListener(this);
		// 查询最近一月
		oneMonthBtn.setOnClickListener(this);
		// 查询最近三个月
		threeMonthBtn.setOnClickListener(this);
		queryBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				clearDate();
				if(isFirst){
					isFirst = false;
				}else {
					if(adapter != null){
						adapter.notifyDataSetChanged();
					}
				}
				clickQuery();
				excuseQuery();
			}
		});

		ivPullUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
				layoutQuery.setVisibility(View.GONE);
				layoutResult.setVisibility(View.VISIBLE);
				resultTopView.setVisibility(View.VISIBLE);
			}
		});
		startDate = startLastOneMonth;
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

		mCurrentIndex = 1;
		listData.clear();
		BaseHttpEngine.showProgressDialog();
		btnMore.setVisibility(View.GONE);
		requestPsnLOANUseRecordsQuery();
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
			DatePickerDialog dialog = new DatePickerDialog(context, new OnDateSetListener() {

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
//	private void initAfterLayout() {
//		queryDateContent.setText(startDate + "-" + endDate);
//		queryAccContent.setText(StringUtil.getForSixForString(loanActNum));
//	}

	/**
	 * 刷新列表
	 */
	private void refreshListView(List<Map<String, Object>> listData) {
		if (adapter == null) {
			adapter = new LoanUseQueryResultAdapter(this, listData, currency);
			listView.setAdapter(adapter);
		} else {
			adapter.setListData(listData);
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * @Title: requestPsnLOANUseRecordsQuery
	 * @Description: 用款查询接口
	 * @param
	 * @return void
	 * @throws
	 */
	public void requestPsnLOANUseRecordsQuery() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.PSN_LOAN_USE_RECORDS_QUERY);
		Map<String, Object> map = new HashMap<String, Object>();
		sCurrentIndex=String.valueOf(mCurrentIndex);
         String spageSize=String.valueOf(pageSize);
		map.put(Loan.START_DATE, startDate);
		map.put(Loan.END_DATE, endDate);
		map.put(Loan.CURRENT_INDEX, sCurrentIndex);
		map.put(Loan.PAGE_SIZE, spageSize);
		map.put(Loan.LOAN_ACT_NUM, loanActNum);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnLOANUseRecordsQueryCallback");
	}

	/**
	 * @Title: requestPsnLOANUseRecordsQueryCallback
	 * @Description: 用款查询查询接口
	 * @param @param resultObj
	 * @return void
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnLOANUseRecordsQueryCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getResources().getString(R.string.no_list_data));
			listData.clear();
			showQueryResultView(false);
			return;
		}
		String recordNumber="";
		if(resultList.get(0).get(Loan.TOT_NUMQ)!=null){
		 recordNumber = (String) resultList.get(0).get(Loan.TOT_NUMQ);
		}
		if (!StringUtil.isNullOrEmpty(recordNumber)) {
			totalCount = Integer.parseInt(recordNumber);
		}
		
		for (int i = 0; i < resultList.size(); i++) {
			listData.add((Map<String, Object>) resultList.get(i));
		}
		if (listData.size() >= totalCount) {
			btnMore.setVisibility(View.GONE);
//			listView.removeFooterView(viewFooter);
		} else {
			btnMore.setVisibility(View.VISIBLE);
		}
//		result_main_view.setVisibility(View.VISIBLE);
		showQueryResultView(true);
	}

//	@Override
//	protected void onPause() {
//		isPullDownPop = PopupWindowUtils.getInstance().isQueryPopupShowing();
//		super.onPause();
//	}

//	@Override
//	protected void onRestart() {
//		super.onRestart();
//		if (isPullDownPop) {
//			ivPullDown.postDelayed(new Runnable() {
//				@Override
//				public void run() {
//					if (isPullDownPop) {
//						PopupWindowUtils.getInstance().showQueryPopupWindow();
//						isPullDownPop = false;
//					}
//				}
//			}, 500);
//		}
//	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPsnLOANUseRecordsQuery();
	}

	/**
	 * @Title: showQueryResultView
	 * @Description: 展示查询后的页面
	 * @param
	 * @return void
	 */
	private void showQueryResultView(boolean isDissPop) {
		if (isDissPop) {
			layoutQuery.setVisibility(View.GONE);
			resultTopView.setVisibility(View.VISIBLE);
			layoutResult.setVisibility(View.VISIBLE);
//			isPullDownPop = false;
//			PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
			queryDateContent.setText(startDate + "-" + endDate);
			queryAccContent.setText(StringUtil.getForSixForString(loanActNum));
			ivPullUp.setClickable(true);
//			initAfterLayout();
			refreshListView(listData);
		} else {// 查询结果数据为空的时候
			layoutQuery.setVisibility(View.VISIBLE);
			resultTopView.setVisibility(View.GONE);
			layoutResult.setVisibility(View.GONE);
			ivPullUp.setClickable(false);
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
		queryDateContent.setText(startDate + "-" + endDate);
		queryAccContent.setText(StringUtil.getForSixForString(loanActNum));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_oneweek_query_date_preandexe:// 最近一周
			startDate = QueryDateUtils.getlastWeek(dateTime).trim();
			endDate = QueryDateUtils.getcurrentDate(dateTime).trim();
			mCurrentIndex = 1;
			clearDate();
			if(isFirst){
				isFirst = false;
			}else {
				if(adapter != null){
					adapter.notifyDataSetChanged();
				}
			}
			clickQuery();
			changeDateText(startDate, endDate);
			BaseHttpEngine.showProgressDialog();
			btnMore.setVisibility(View.GONE);
			requestPsnLOANUseRecordsQuery();
			break;
		case R.id.btn_onemonth_query_date_preandexe:// 最近一月
			startDate = QueryDateUtils.getlastOneMonth(dateTime).trim();
			endDate = QueryDateUtils.getcurrentDate(dateTime).trim();
			mCurrentIndex = 1;
			clearDate();
			if(isFirst){
				isFirst = false;
			}else {
				if(adapter != null){
					adapter.notifyDataSetChanged();
				}
			}
			clickQuery();
			changeDateText(startDate, endDate);
			BaseHttpEngine.showProgressDialog();
			btnMore.setVisibility(View.GONE);
			requestPsnLOANUseRecordsQuery();
			break;
		case R.id.btn_threemonth_query_date_preandexe:// 最近三月
			startDate = QueryDateUtils.getlastThreeMonth(dateTime).trim();
			endDate = QueryDateUtils.getcurrentDate(dateTime).trim();
			mCurrentIndex = 1;
			clearDate();
			if(isFirst){
				isFirst = false;
			}else {
				if(adapter != null){
					adapter.notifyDataSetChanged();
				}
			}
			clickQuery();
			changeDateText(startDate, endDate);
			BaseHttpEngine.showProgressDialog();
			btnMore.setVisibility(View.GONE);
			requestPsnLOANUseRecordsQuery();
			break;
		default:
			break;
		}

	}

	/** 清空数据以及更多按钮 */
	private void clearDate() {
		if (listData != null && !listData.isEmpty()) {
			listData.clear();
		}
		if (btnMore.isShown()) {
			btnMore.setVisibility(View.GONE);
		}
	}
}
