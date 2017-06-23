package com.chinamworld.bocmbci.biz.dept.recordmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.managetrans.adapter.ManageRecordsAdapter1;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: RegularRecordActivity
 * @Description: 定期存款记录查询
 * @author JiangWei
 * @date 2013-7-1 上午11:54:00
 */
public class RegularRecordActivity extends DeptBaseActivity implements OnClickListener {

	private Spinner transTypeSp = null;

	/** * 开始日期 */
	private TextView startDateTv = null;
	/** * 结束日期 */
	private TextView endDateTv = null;
	/** * 查询按钮 */
	private Button queryBtn = null;
	/** * 最近一周 */
	private Button oneWeekBtn = null;
	/** * 最近一月 */
	private Button oneMonthBtn = null;
	/** * 最近三月 */
	private Button threeMonthBtn = null;
	/** 开始查询日期 */
	private String startDateStr = null;
	/** 结束查询日期 */
	private String endDateStr = null;
	/**
	 * 转账记录查询
	 */
	List<Map<String, Object>> queryTransferRecordList = new ArrayList<Map<String, Object>>();
	/** 系统时间 */
	// private String currenttime;
	/** 查询页面，向上收起 */
	private LinearLayout pullUpQueryLayoutLl = null;
	/** 查询条件整个布局 */
	private View searchLayout = null;
	/** 查询条件布局 */
	// private View searchCoditionLayout = null;
	/** 查询页面下拉布局 */
	// private View searchDownLayout = null;

	/** 查询结果页面全部布局 */
	private View searchResultLayout = null;
	/** 查询时间 */
	private TextView searchResultTimeText = null;
	// 查询范围
	private TextView serachRangeTV;
	/** 查询结果页面向下箭头 */
	// private ImageView searchResultDownImageView = null;
	/** 查询结果listView */
	// private HeadListView queryResultLv = null;
	private ListView queryResultLv;
	// private LayoutInflater lvHeaderInflater = null;
	// private View tranRecordsLvHeaderLayout = null;

	/** 查询结果顶部 */
	// private View searchResultTopView = null;
	/** 查询页面 下拉 */
	// private ImageView downImageView = null;
	/** 退出动画 */
	private Animation animation_up;
	/** 进入动画 */
	private Animation animation_down;

	private String transTypeCode = "";
	private String transTypeName = null;

	private int startYear, startMonth, startDay;
	private int endYear, endMonth, endDay;

	private int curIndex = 0;
	private int pageSize = 10;

	private Button moreBtn;

	private ManageRecordsAdapter1 preDateAdapter;
	/** 是否是点击的显示更多 */
	private boolean isPreClickMore = false;

	private LinearLayout pullDownLayout;
	private LinearLayout tabcontent;
	private LinearLayout ll_result_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.regular_record_query));
		View view = mInflater.inflate(R.layout.tran_manage_trans_records_activity, null);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		tabcontent.setPadding(0, 0, 0, this.getResources().getDimensionPixelSize(R.dimen.common_bottom_padding_new));
		ll_result_list = (LinearLayout) view.findViewById(R.id.ll_result_list);
//		((RelativeLayout.LayoutParams) ll_result_list.getLayoutParams()).setMargins(
//				getResources().getDimensionPixelSize(R.dimen.fill_margin_left), 0,
//				getResources().getDimensionPixelSize(R.dimen.fill_margin_right), 0);
		initanimation();
		requestSystemDateTime();
		BiiHttpEngine.showProgressDialog();

	}

	/** 请求系统时间*/
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		// 3天前的日期
		startDateStr = QueryDateUtils.getlastthreeDate(dateTime);
		startYear = Integer.parseInt(startDateStr.substring(0, 4));
		startMonth = Integer.parseInt(startDateStr.substring(5, 7));
		startDay = Integer.parseInt(startDateStr.substring(8, 10));
		// 系统当前时间格式化
		endDateStr = QueryDateUtils.getcurrentDate(dateTime);
		endYear = Integer.parseInt(endDateStr.substring(0, 4));
		endMonth = Integer.parseInt(endDateStr.substring(5, 7));
		endDay = Integer.parseInt(endDateStr.substring(8, 10));

		setupView();

		setViewOnClickListener();
		// 默认查询最近三天的数据
		// transTypeName = LocalData.transTypeList.get(0);
		transTypeName = LocalData.transTypeList.get(4);
		transTypeCode = LocalData.transType.get(transTypeName);
		changeDateText(startDateStr, endDateStr);
		// RequestForTransQueryTransferRecord(startDateStr, endDateStr);
		BaseHttpEngine.dissMissProgressDialog();
		pullUpQueryLayoutLl.setClickable(false);
	}

	/** 开始日期控件监听*/
	DatePickerDialog.OnDateSetListener onStartDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			String month = String.valueOf(monthOfYear + 1);
			String day = String.valueOf(dayOfMonth);
			if (monthOfYear + 1 < 10) {// 如果月份小于10月 前面加“0”
				month = "0" + String.valueOf(monthOfYear + 1);
			}
			if (dayOfMonth < 10) {
				day = "0" + String.valueOf(dayOfMonth);
			}
			/** 选择的截止日期 */
			String dateStr = (String.valueOf(year) + "/" + month + "/" + day);
			/** 为EditText赋值 */
			startDateTv.setText(dateStr);
		}
	};

	/** 结束日期控件监听*/
	DatePickerDialog.OnDateSetListener onEndDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			String month = String.valueOf(monthOfYear + 1);
			String day = String.valueOf(dayOfMonth);
			if (monthOfYear + 1 < 10) {// 如果月份小于10月 前面加“0”
				month = "0" + String.valueOf(monthOfYear + 1);
			}
			if (dayOfMonth < 10) {
				day = "0" + String.valueOf(dayOfMonth);
			}
			/** 选择的截止日期 */
			String dateStr = (String.valueOf(year) + "/" + month + "/" + day);
			/** 为EditText赋值 */
			endDateTv.setText(dateStr);
		}
	};

	/** 设置监听*/
	private void setViewOnClickListener() {
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.dept_spinner, LocalData.transTypeList);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		transTypeSp.setAdapter(adapter1);
		transTypeSp.setSelection(4);
		transTypeSp.setClickable(false);
		transTypeSp.setBackgroundResource(R.drawable.bg_spinner_default);
		transTypeSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				transTypeName = LocalData.transTypeList.get(position);
				// transTypeCode = LocalData.transType.get(transTypeName);
				// 设置查询范围
				serachRangeTV.setText(transTypeName);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				transTypeName = LocalData.transTypeList.get(0);
				// transTypeCode = LocalData.transType.get(transTypeName);
				// 设置查询范围
				serachRangeTV.setText(transTypeName);
			}
		});

		startDateTv.setOnClickListener(this);
		endDateTv.setOnClickListener(this);

		queryBtn.setOnClickListener(this);
		// 查询最近一周
		oneWeekBtn.setOnClickListener(this);
		// 查询最近一月
		oneMonthBtn.setOnClickListener(this);
		// 查询最近三个月
		threeMonthBtn.setOnClickListener(this);

		pullUpQueryLayoutLl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 有查询结果，隐藏整个查询界面searchLayout
				animation_up.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						searchLayout.setVisibility(View.GONE);
						searchResultLayout.setVisibility(View.VISIBLE);
					}
				});
				searchLayout.startAnimation(animation_up);
			}
		});
		// 查询结果顶部View
		pullDownLayout.setOnClickListener(resultOnClickListener);
	}

	private OnClickListener resultOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
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
					searchLayout.setVisibility(View.VISIBLE);
					searchResultLayout.setVisibility(View.GONE);
				}
			});
			searchLayout.startAnimation(animation_down);
		}
	};

	/** 初始化动画 */
	@SuppressWarnings("static-access")
	private void initanimation() {
		animation_up = new AnimationUtils().loadAnimation(RegularRecordActivity.this, R.anim.query_exit);
		animation_down = new AnimationUtils().loadAnimation(RegularRecordActivity.this, R.anim.query_enter);
	}

	/**
	 * 初始化加载XML里的控件
	 */
	private void setupView() {
		transTypeSp = (Spinner) findViewById(R.id.sp_trans_cremit_manage_trans_records);

		queryResultLv = (ListView) findViewById(R.id.lv_query_result_trans_records);

		oneWeekBtn = (Button) findViewById(R.id.btn_oneweek_trans_records);
		oneMonthBtn = (Button) findViewById(R.id.btn_onemonth_trans_records);
		threeMonthBtn = (Button) findViewById(R.id.btn_threemonth_trans_records);
		queryBtn = (Button) findViewById(R.id.btn_query_trans_records);

		startDateTv = (TextView) findViewById(R.id.tv_startdate_trans_records);
		endDateTv = (TextView) findViewById(R.id.tv_enddate_trans_records);

		// searchCoditionLayout = findViewById(R.id.ll_query_condition);
		// searchDownLayout = findViewById(R.id.querycondition_down);
		searchLayout = findViewById(R.id.forex_query_transfer_layout);
		pullUpQueryLayoutLl = (LinearLayout) findViewById(R.id.ll_pull_up_query_preandexe);
		searchResultLayout = findViewById(R.id.forex_query_result_layout);
		searchResultTimeText = (TextView) findViewById(R.id.trade_time);
		serachRangeTV = (TextView) findViewById(R.id.tv_query_range);
		// searchResultDownImageView = (ImageView)
		// findViewById(R.id.img_acc_query_back);
		// searchResultTopView =
		// findViewById(R.id.forex_query_result_condition);

		pullDownLayout = (LinearLayout) findViewById(R.id.pull_down_layout);
		// downImageView = (ImageView)
		// findViewById(R.id.forex_querycondition_down);
		// 3天前的日期
		startDateTv.setText(startDateStr);
		// 系统当前时间格式化
		// 初始结束时间赋值
		endDateTv.setText(endDateStr);

		moreBtn = new Button(this);
		// LayoutParams lp = new LayoutParams(new LayoutParams(
		// LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		int padding = this.getResources().getDimensionPixelSize(R.dimen.fill_padding_top);
		moreBtn.setPadding(0, padding, 0, padding);
		// moreBtn.setLayoutParams(lp);
		moreBtn.setBackgroundColor(this.getResources().getColor(R.color.transparent_00));
		moreBtn.setText(getString(R.string.query_more));
		moreBtn.setTextColor(this.getResources().getColor(R.color.gray));
		moreBtn.setOnClickListener(moreClickListener);
	}

	private OnClickListener moreClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			isPreClickMore = true;
			RequestForTransQueryTransferRecord(startDateStr, endDateStr);
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_startdate_trans_records:
			String start = startDateTv.getText().toString();
			String end = endDateTv.getText().toString();
			setCurDatePickTime(start, end);
			DatePickerDialog startDateDialog = new DatePickerDialog(RegularRecordActivity.this, onStartDateSetListener,
					startYear, startMonth - 1, startDay);
			startDateDialog.show();
			break;
		case R.id.tv_enddate_trans_records:
			String start1 = startDateTv.getText().toString();
			String end1 = endDateTv.getText().toString();
			setCurDatePickTime(start1, end1);
			DatePickerDialog endDateDialog = new DatePickerDialog(RegularRecordActivity.this, onEndDateSetListener, endYear,
					endMonth - 1, endDay);
			endDateDialog.show();
			break;
		case R.id.btn_oneweek_trans_records:
			resetData();
			startDateStr = QueryDateUtils.getlastWeek(dateTime).trim();
			endDateStr = QueryDateUtils.getcurrentDate(dateTime).trim();

			// changeDateText(startDateStr,endDateStr);
			String text = getResources().getString(R.string.acc_query_date);
			searchResultTimeText.setText(text + startDateStr + "-" + endDateStr);
			RequestForTransQueryTransferRecord(startDateStr, endDateStr);
			// 将listview数据清空
			clearListView();
			break;
		case R.id.btn_onemonth_trans_records:
			resetData();
			startDateStr = QueryDateUtils.getlastOneMonth(dateTime).trim();
			endDateStr = QueryDateUtils.getcurrentDate(dateTime).trim();

			// changeDateText(startDateStr,endDateStr);
			String text1 = getResources().getString(R.string.acc_query_date);
			searchResultTimeText.setText(text1 + startDateStr + "-" + endDateStr);
			RequestForTransQueryTransferRecord(startDateStr, endDateStr);
			// 将listview数据清空
			clearListView();
			break;
		case R.id.btn_threemonth_trans_records:
			resetData();
			startDateStr = QueryDateUtils.getlastThreeMonth(dateTime).trim();
			endDateStr = QueryDateUtils.getcurrentDate(dateTime).trim();

			// changeDateText(startDateStr,endDateStr);
			String text2 = getResources().getString(R.string.acc_query_date);
			searchResultTimeText.setText(text2 + startDateStr + "-" + endDateStr);
			RequestForTransQueryTransferRecord(startDateStr, endDateStr);
			// 将listview数据清空
			clearListView();
			break;
		case R.id.btn_query_trans_records:
			// resetData();
			String startDateStrPre = startDateTv.getText().toString().trim();
			String endDateStrPre = endDateTv.getText().toString().trim();
			// 判断时间
			if (!QueryDateUtils.compareDateOneYear(startDateStrPre, dateTime)) {
				// 开始日期在系统日期前一年以内
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						RegularRecordActivity.this.getString(R.string.acc_check_start_enddate));
				return;
			} else if (!QueryDateUtils.compareDate(endDateStrPre, dateTime)) {
				// 结束日期在系统日期之前
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						RegularRecordActivity.this.getString(R.string.acc_check_enddate));
				return;
			} else if (!QueryDateUtils.compareDate(startDateStrPre, endDateStrPre)) {
				// 开始日期在结束日期之前
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						RegularRecordActivity.this.getString(R.string.acc_query_errordate));
				return;
			} else if (!QueryDateUtils.compareDateThree(startDateStrPre, endDateStrPre)) {
				// 起始日期与结束日期最大间隔为三个自然月
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						RegularRecordActivity.this.getString(R.string.acc_check_start_end_date));
				return;
			} else {
				transTypeCode = LocalData.transType.get(transTypeName);
				startDateStr = startDateStrPre;
				endDateStr = endDateStrPre;
				resetData();
				changeDateText(startDateStr, endDateStr);
				RequestForTransQueryTransferRecord(startDateStr, endDateStr);
				// 将listview数据清空
				clearListView();
			}
			break;

		default:
			break;
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////
	// 转账记录查询
	/** 转账记录查询 req*/
	private void RequestForTransQueryTransferRecord(String startDateStr, String endDateStr) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.TRANSQUERYTRANSFERRECORD);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.MANAGE_RECORDS_TRANSTYPE_REQ, transTypeCode);
		map.put(Tran.MANAGE_RECORDS_STARTDATE_REQ, startDateStr);
		map.put(Tran.MANAGE_RECORDS_ENDDATE_REQ, endDateStr);
		map.put(Tran.MANAGE_RECORDS_CURRENTINDEX_REQ, curIndex + "");
		map.put(Tran.MANAGE_RECORDS_PAGESIZE_REQ, pageSize + "");
		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "transQueryTransferRecordCallBack");
	}

	/** 转账记录查询 res*/
	@SuppressWarnings("unchecked")
	public void transQueryTransferRecordCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		String recordNumber = (String) result.get(Tran.RECORD_NUMBER);
		int preRecordCount = Integer.parseInt(recordNumber);
		if (curIndex + pageSize < preRecordCount) {
			queryResultLv.removeFooterView(moreBtn);
			queryResultLv.addFooterView(moreBtn);
			curIndex += pageSize;
		} else {
			queryResultLv.removeFooterView(moreBtn);
		}

		List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ConstantGloble.LIST);
		if (StringUtil.isNullOrEmpty(list)) {
			// dismissQueryLayout();
			// 将listview数据清空
			clearListView();
			ll_result_list.setVisibility(View.GONE);
			pullUpQueryLayoutLl.setClickable(false);
			String message = this.getString(R.string.query_no_result);
			BaseDroidApp.getInstanse().createDialog(null, message);
			return;
		}

		pullUpQueryLayoutLl.setClickable(true);
		ll_result_list.setVisibility(View.VISIBLE);
		queryTransferRecordList.addAll(list);
		if (isPreClickMore) {
			preDateAdapter.setDate(queryTransferRecordList);
		} else {
			showQueryResult(list);
		}
	}

	/** 显示查询结果*/
	private void showQueryResult(List<Map<String, Object>> list) {
		dismissQueryLayout();
		animation_up.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				searchLayout.setVisibility(View.GONE);
				searchResultLayout.setVisibility(View.VISIBLE);
			}
		});
		searchLayout.startAnimation(animation_up);
		String text = getResources().getString(R.string.acc_query_date);
		searchResultTimeText.setText(text + startDateStr + "-" + endDateStr);

		preDateAdapter = new ManageRecordsAdapter1(RegularRecordActivity.this, list);
		queryResultLv.setAdapter(preDateAdapter);
		queryResultLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				requestForTransQueryTransferRecordDetail(position);
			}
		});
	}

	/** PsnTransQueryTransferRecordDetail 转账记录详情 req*/
	public void requestForTransQueryTransferRecordDetail(int position) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.TRANSQUERYTRANSFERRECORDDETAIL);
		Map<String, Object> queryTransferRecord = queryTransferRecordList.get(position);
		String transId = (String) queryTransferRecord.get(Tran.MANAGE_RECORDS_TRANSACTIONID_RES);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.MANAGE_RECORDSDETAIL_transId_REQ, transId);
		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "transQueryTransferRecordDetailCallBack");
	}

	/** 转账记录详情 res*/
	@SuppressWarnings("unchecked")
	public void transQueryTransferRecordDetailCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		TranDataCenter.getInstance().setQueryDetailCallBackMap(result);

		Intent intent = new Intent(RegularRecordActivity.this, RegularRecordDetailActivity.class);
		startActivity(intent);
	}

	/** 重置状态数据*/
	private void resetData() {
		curIndex = 0;
		isPreClickMore = false;
		queryTransferRecordList.clear();
	}

	private void dismissQueryLayout() {
		animation_up.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				searchLayout.setVisibility(View.GONE);
//				searchResultLayout.setVisibility(View.VISIBLE);
			}
		});
		searchLayout.startAnimation(animation_up);
	}

	/**
	 * 改变textview的日期 现实
	 * @param startDate 起始日期
	 * @param endDate 结束日期
	 */
	private void changeDateText(String startDate, String endDate) {
		String text = getResources().getString(R.string.acc_query_date);
		startDateTv.setText(startDate);
		endDateTv.setText(endDate);
		searchResultTimeText.setText(text + startDate + "-" + endDate);
	}

	/**
	 * 设置当前时间 用于日期控件反显
	 * @param strStartDate
	 * @param strEndDate
	 */
	private void setCurDatePickTime(String strStartDate, String strEndDate) {
		startYear = Integer.parseInt(strStartDate.substring(0, 4));
		startMonth = Integer.parseInt(strStartDate.substring(5, 7));
		startDay = Integer.parseInt(strStartDate.substring(8, 10));
		endYear = Integer.parseInt(strEndDate.substring(0, 4));
		endMonth = Integer.parseInt(strEndDate.substring(5, 7));
		endDay = Integer.parseInt(strEndDate.substring(8, 10));
	}

	/** 清空listview数据 刷新listview视图*/
	private void clearListView() {
		// 将listview数据情况
		queryTransferRecordList.clear();
		if (!StringUtil.isNullOrEmpty(preDateAdapter)) {
			preDateAdapter.setDate(queryTransferRecordList);
			queryResultLv.removeFooterView(moreBtn);
		}
	}
}
