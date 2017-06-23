package com.chinamworld.bocmbci.biz.dept.appointmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.managetrans.adapter.ManageDateAdapter;
import com.chinamworld.bocmbci.biz.tran.managetrans.adapter.ManageExeDateQueryAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 预约日期和执行日期查询
 * 
 * @author wangchao
 * 
 */
public class QueryDateActivity1 extends DeptBaseActivity implements
		OnClickListener {

	private RadioGroup queryRg = null;
	private RadioButton leftQueryRb;

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
	 * 存储查询后的信息
	 */
	// List<Map<String, Object>> queryTransferRecordList = new
	// ArrayList<Map<String,Object>>();

	// 预约日期查询数据
	List<Map<String, Object>> preRecordList = new ArrayList<Map<String, Object>>();
	// 执行日期查询数据
	List<Map<String, Object>> exeRecordList = new ArrayList<Map<String, Object>>();

	/** 系统时间 */
	private String currenttime;
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
	/** 查询结果页面向下箭头 */
	// private ImageView searchResultDownImageView = null;
	/** 预约日期查询结果listView */
	// private HeadListView queryResultLv = null;
	private ListView queryResultLv = null;
	/** 执行日期查询结果listView */
	private ListView exeDateQueryResultLv = null;

	// private boolean isExeDateQuery = false;
	/** 预约日期查询 linearLayout */
	private LinearLayout preDateQueryLl = null;
	/** 执行日期查询 linearLayout */
	private LinearLayout exeDateQueryLl = null;

	/** 查询结果顶部 */
	// private View searchResultTopView = null;
	/** 查询页面 下拉 */
	// private ImageView downImageView = null;
	/** 退出动画 */
	private Animation animation_up;
	/** 进入动画 */
	private Animation animation_down;
	/** 预约日期查询 */

	private int curPreIndex = 0;
	/** 执行日期查询 */

	private int curExeIndex = 0;

	private int pageSize = 10;
	/** 预约日期查询 执行日期查询标记 */
	private int QueryType;
	/** 预约日期adapter */
	private ManageDateAdapter preDateAdapter;
	/** 预约周期adapter */
	private ManageExeDateQueryAdapter exeDateApater;

	/** 是否是点击的显示更多 */
	private boolean isPreClickMore = false;
	private boolean isExeClickMore = false;

	private int startYear, startMonth, startDay;
	private int endYear, endMonth, endDay;

	/** 记录条数 */
	private int preRecordCount; // 预约日期总记录条数
	private int exeRecordCount; // 执行日期总记录条数

	private Button moreBtn;

	private LinearLayout pullDownLayout;

	private LinearLayout tabcontent;

	private String paymentDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.regular_record_pre_query));
		View view = mInflater.inflate(
				R.layout.tran_manage_query_preandexe_date_activity, null);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0,
					0, 0);
		}

		BaseHttpEngine.showProgressDialog();
		// 查询系统时间
		requestSystemDateTime();
		initanimation();
	}

	/**
	 * 请求系统时间
	 * 
	 */
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		setupView();
		setViewOnClickListener();
		pullUpQueryLayoutLl.setClickable(false);
	}

	/**
	 * 开始日期控件监听
	 */
	DatePickerDialog.OnDateSetListener onStartDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
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

	/**
	 * 结束日期控件监听
	 */
	DatePickerDialog.OnDateSetListener onEndDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
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

	/**
	 * 设置监听
	 */
	private void setViewOnClickListener() {
		startDateTv.setOnClickListener(this);
		endDateTv.setOnClickListener(this);

		queryBtn.setOnClickListener(this);
		// 查询最近一周
		oneWeekBtn.setOnClickListener(this);
		// 查询最近一月
		oneMonthBtn.setOnClickListener(this);
		// 查询最近三个月
		threeMonthBtn.setOnClickListener(this);

		queryRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				String text = getResources().getString(R.string.acc_query_date);
				switch (checkedId) {
				case R.id.rb_left_manage_query:
					// isExeClickMore = false;

					preDateQueryLl.setVisibility(View.VISIBLE);
					exeDateQueryLl.setVisibility(View.GONE);
					QueryType = PRE_DATE;

					startDateStr = QueryDateUtils.getlastthreeDate(dateTime);
					endDateStr = QueryDateUtils.getcurrentDate(dateTime);
					searchResultTimeText.setText(text + startDateStr + "-"
							+ endDateStr);
					setCurDatePickTime(startDateStr, endDateStr);

					resetData();
					BaseHttpEngine.dissMissProgressDialog();
					// 改变日期框的时间
					startDateTv.setText(startDateStr);
					endDateTv.setText(endDateStr);

					clearListView();
					pullUpQueryLayoutLl.setClickable(false);
					break;
				case R.id.rb_right_manage_query:
					// isPreClickMore = false;

					preDateQueryLl.setVisibility(View.GONE);
					exeDateQueryLl.setVisibility(View.VISIBLE);
					QueryType = EXE_DATE;

					startDateStr = QueryDateUtils.getcurrentDate(dateTime);
					endDateStr = QueryDateUtils.getNextthreeDate(dateTime);
					searchResultTimeText.setText(text + startDateStr + "-"
							+ endDateStr);
					setCurDatePickTime(startDateStr, endDateStr);
					// if (!isExeDateQuery) {
					resetData();
					// requestForTransPreRecordQuery(startDateStr, endDateStr);
					// isExeDateQuery = true;
					// }
					// 改变日期框的时间
					startDateTv.setText(startDateStr);
					endDateTv.setText(endDateStr);

					clearListView();
					pullUpQueryLayoutLl.setClickable(false);
					break;
				default:
					break;
				}
			}
		});
		leftQueryRb.setChecked(true);

		pullUpQueryLayoutLl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// // 有查询结果，隐藏整个查询界面searchLayout
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
				}
			});
			searchLayout.startAnimation(animation_down);
		}
	};

	/** 初始化动画 */
	@SuppressWarnings("static-access")
	private void initanimation() {
		animation_up = new AnimationUtils().loadAnimation(
				QueryDateActivity1.this, R.anim.query_exit);
		animation_down = new AnimationUtils().loadAnimation(
				QueryDateActivity1.this, R.anim.query_enter);
	}

	/**
	 * 初始化加载XML里的控件
	 */
	private void setupView() {

		queryRg = (RadioGroup) findViewById(R.id.rg_manage_query);
		leftQueryRb = (RadioButton) findViewById(R.id.rb_left_manage_query);
		// rightQueryRb = (RadioButton)
		// findViewById(R.id.rb_right_manage_query);

		startDateTv = (TextView) findViewById(R.id.tv_startdate_query_date_preandexe);
		endDateTv = (TextView) findViewById(R.id.tv_enddate_query_date_preandexe);

		queryBtn = (Button) findViewById(R.id.btn_query_date_preandexe);
		oneWeekBtn = (Button) findViewById(R.id.btn_oneweek_query_date_preandexe);
		oneMonthBtn = (Button) findViewById(R.id.btn_onemonth_query_date_preandexe);
		threeMonthBtn = (Button) findViewById(R.id.btn_threemonth_query_date_preandexe);

		// searchCoditionLayout = findViewById(R.id.ll_query_condition);
		// searchDownLayout = findViewById(R.id.querycondition_down);
		searchLayout = findViewById(R.id.forex_query_transfer_layout);
		pullUpQueryLayoutLl = (LinearLayout) findViewById(R.id.ll_pull_up_query_preandexe);
		searchResultLayout = findViewById(R.id.forex_query_result_layout);
		searchResultTimeText = (TextView) findViewById(R.id.trade_time);
		// searchResultDownImageView = (ImageView)
		// findViewById(R.id.img_acc_query_back);

		queryResultLv = (ListView) findViewById(R.id.lv_pre_date_query_result);
		exeDateQueryResultLv = (ListView) findViewById(R.id.lv_exe_date_query_result);

		preDateQueryLl = (LinearLayout) findViewById(R.id.ll_pre_date_query);
		exeDateQueryLl = (LinearLayout) findViewById(R.id.ll_exe_date_query);

		pullDownLayout = (LinearLayout) findViewById(R.id.pull_down_layout);
		// searchResultTopView =
		// findViewById(R.id.forex_query_result_condition);
		// downImageView = (ImageView)
		// findViewById(R.id.forex_querycondition_down);
		// 3天前的日期
		String startTwoDate = QueryDateUtils.getlastthreeDate(dateTime);
		startDateTv.setText(startTwoDate);
		// 系统当前时间格式化
		currenttime = QueryDateUtils.getcurrentDate(dateTime);
		// 初始结束时间赋值
		endDateTv.setText(currenttime);

		moreBtn = new Button(this);
		// LayoutParams lp = new LayoutParams(new LayoutParams(
		// LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		int padding = this.getResources().getDimensionPixelSize(
				R.dimen.fill_padding_top);
		moreBtn.setPadding(0, padding, 0, padding);
		// moreBtn.setLayoutParams(lp);
		moreBtn.setBackgroundColor(this.getResources().getColor(
				R.color.transparent_00));
		moreBtn.setTextColor(this.getResources().getColor(R.color.gray));
		moreBtn.setText(getString(R.string.query_more));

		initPreDateHeaderView();
		initExeDateHeaderView();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tv_startdate_query_date_preandexe:// 开始日期
			String startDate = startDateTv.getText().toString().trim();
			String endDate = endDateTv.getText().toString().trim();
			setCurDatePickTime(startDate, endDate);
			DatePickerDialog startDateDialog = new DatePickerDialog(
					QueryDateActivity1.this, onStartDateSetListener, startYear,
					startMonth - 1, startDay);
			startDateDialog.show();
			break;
		case R.id.tv_enddate_query_date_preandexe:// 结束日期
			String startDate1 = startDateTv.getText().toString().trim();
			String endDate1 = endDateTv.getText().toString().trim();
			setCurDatePickTime(startDate1, endDate1);
			DatePickerDialog endDateDialog = new DatePickerDialog(
					QueryDateActivity1.this, onEndDateSetListener, endYear,
					endMonth - 1, endDay);
			endDateDialog.show();
			break;
		case R.id.btn_oneweek_query_date_preandexe:// 最近一周
			resetData();
			if (QueryType == PRE_DATE) {
				startDateStr = QueryDateUtils.getlastWeek(dateTime).trim();
				endDateStr = QueryDateUtils.getcurrentDate(dateTime).trim();
			} else if (QueryType == EXE_DATE) {
				startDateStr = QueryDateUtils.getcurrentDate(dateTime).trim();
				endDateStr = QueryDateUtils.getNextWeek(dateTime).trim();
			}
			// changeDateText(startDateStr,endDateStr);
			String text = getResources().getString(R.string.acc_query_date);
			searchResultTimeText
					.setText(text + startDateStr + "-" + endDateStr);
			requestForTransPreRecordQuery(startDateStr, endDateStr);
			// 将listview数据情况
			clearListView();
			break;
		case R.id.btn_onemonth_query_date_preandexe:// 最近一月
			resetData();
			if (QueryType == PRE_DATE) {
				startDateStr = QueryDateUtils.getlastOneMonth(dateTime).trim();
				endDateStr = QueryDateUtils.getcurrentDate(dateTime).trim();
			} else if (QueryType == EXE_DATE) {
				startDateStr = QueryDateUtils.getcurrentDate(dateTime).trim();
				endDateStr = QueryDateUtils.getNextOneMonth(dateTime).trim();
			}
			// changeDateText(startDateStr,endDateStr);
			String text1 = getResources().getString(R.string.acc_query_date);
			searchResultTimeText.setText(text1 + startDateStr + "-"
					+ endDateStr);
			requestForTransPreRecordQuery(startDateStr, endDateStr);
			// 将listview数据情况
			clearListView();
			break;
		case R.id.btn_threemonth_query_date_preandexe:// 最近三月
			resetData();
			if (QueryType == PRE_DATE) {
				startDateStr = QueryDateUtils.getlastThreeMonth(dateTime)
						.trim();
				endDateStr = QueryDateUtils.getcurrentDate(dateTime).trim();
			} else if (QueryType == EXE_DATE) {
				startDateStr = QueryDateUtils.getcurrentDate(dateTime).trim();
				endDateStr = QueryDateUtils.getNextThreeMonth(dateTime).trim();
			}
			// changeDateText(startDateStr,endDateStr);
			String text2 = getResources().getString(R.string.acc_query_date);
			searchResultTimeText.setText(text2 + startDateStr + "-"
					+ endDateStr);
			requestForTransPreRecordQuery(startDateStr, endDateStr);
			// 将listview数据情况
			clearListView();
			break;
		case R.id.btn_query_date_preandexe:// 查询
			// resetData();
			String startDateStrPre = startDateTv.getText().toString().trim();
			String endDateStrPre = endDateTv.getText().toString().trim();
			// 判断时间
			if (!QueryDateUtils.compareDateOneYear(startDateStrPre, dateTime)) {
				// 开始日期在系统日期前一年以内
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						QueryDateActivity1.this
								.getString(R.string.acc_check_start_enddate));
				return;
			}
			// 在预约执行的时候 结束日期在系统日期之前
			if (QueryType == PRE_DATE) {
				if (!QueryDateUtils.compareDate(endDateStrPre, dateTime)) {
					// 结束日期在系统日期之前
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							QueryDateActivity1.this
									.getString(R.string.acc_check_enddate));
					return;
				}
			} else if (QueryType == EXE_DATE) {
				String sixMonthLater = QueryDateUtils.getNextSixMonth(dateTime);
				if (!QueryDateUtils.compareDate(endDateStrPre, sixMonthLater)) {
					// 结束日期在系统日期之前
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							QueryDateActivity1.this
									.getString(R.string.acc_query_enddate_six));
					return;
				}
			}

			if (!QueryDateUtils.compareDate(startDateStrPre, endDateStrPre)) {
				// 开始日期在结束日期之前
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						QueryDateActivity1.this
								.getString(R.string.acc_query_errordate));
				return;
			} else if (!QueryDateUtils.compareDateThree(startDateStrPre,
					endDateStrPre)) {
				// 起始日期与结束日期最大间隔为三个自然月
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						QueryDateActivity1.this
								.getString(R.string.acc_check_start_end_date));
				return;
			} else {
				startDateStr = startDateStrPre;
				endDateStr = endDateStrPre;
				resetData();
				changeDateText(startDateStr, endDateStr);
				// 清空数据，刷新listview
				requestForTransPreRecordQuery(startDateStr, endDateStr);
				// 将listview数据情况
				clearListView();
			}
			break;

		default:
			break;
		}

	}

	/**
	 * PsnTransPreRecordQuery预约交易查询 req
	 */
	public void requestForTransPreRecordQuery(String startDateStr,
			String endDateStr) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.TRANSPRERECORDQUERY);
		Map<String, String> map = new HashMap<String, String>();
		if (QueryType == PRE_DATE) {
			map.put(Tran.MANAGE_PRE_DATETYPE_REQ, QueryType + "");
			map.put(Tran.MANAGE_PRE_CURRENTINDEX_REQ, curPreIndex + "");
		} else if (QueryType == EXE_DATE) {
			map.put(Tran.MANAGE_PRE_CURRENTINDEX_REQ, curExeIndex + "");
			map.put(Tran.MANAGE_PRE_DATETYPE_REQ, QueryType + "");
		}
		map.put(Tran.MANAGE_PRE_STARTDATE_REQ, startDateStr);
		map.put(Tran.MANAGE_PRE_ENDDATE_REQ, endDateStr);
		map.put(Tran.MANAGE_PRE_PAGESIZE_REQ, pageSize + "");
		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"transPreRecordQueryCallBack");
	}

	/**
	 * PsnTransPreRecordQuery预约交易查询 res
	 */
	@SuppressWarnings("unchecked")
	public void transPreRecordQueryCallBack(Object resultObj) {

		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		List<Map<String, Object>> list = (List<Map<String, Object>>) result
				.get(ConstantGloble.LIST);
		// 如果返回结果为空提示
		if (StringUtil.isNullOrEmpty(list)) {
			// animation_up.setAnimationListener(new AnimationListener() {
			// @Override
			// public void onAnimationStart(Animation animation) {
			// }
			//
			// @Override
			// public void onAnimationRepeat(Animation animation) {
			// }
			//
			// @Override
			// public void onAnimationEnd(Animation animation) {
			// searchLayout.setVisibility(View.GONE);
			// searchResultLayout.setVisibility(View.VISIBLE);
			// }
			// });
			// searchLayout.startAnimation(animation_up);
			// // 将listview数据情况
			// if (QueryType == PRE_DATE) {
			// preRecordList.clear();
			// if(!StringUtil.isNullOrEmpty(preDateAdapter)){
			// preDateAdapter.setDate(preRecordList);
			// queryResultLv.removeFooterView(moreBtn);
			// }
			// } else if (QueryType == EXE_DATE) {
			// exeRecordList.clear();
			// if(!StringUtil.isNullOrEmpty(exeDateApater)){
			// exeDateApater.setDate(exeRecordList);
			// exeDateQueryResultLv.removeFooterView(moreBtn);
			//
			// }
			// }
			preDateQueryLl.setVisibility(View.GONE);
			exeDateQueryLl.setVisibility(View.GONE);

			pullUpQueryLayoutLl.setClickable(false);
			String message = this.getString(R.string.query_no_result);
			BaseDroidApp.getInstanse().createDialog(null, message);
			return;
		} else {
			pullUpQueryLayoutLl.setClickable(true);
			String recordCount = (String) result.get(Tran.RECORDCOUNT);
			// 根据显示的记录条数 判断“显示更多”按钮 是否显示
			if (QueryType == PRE_DATE) {
				preRecordList.addAll(list);
				preDateQueryLl.setVisibility(View.VISIBLE);
				preRecordCount = Integer.parseInt(recordCount);
				if (curPreIndex + pageSize < preRecordCount) {
					initPreListFootView();
				} else {
					queryResultLv.removeFooterView(moreBtn);
				}
				curPreIndex += pageSize;
			} else if (QueryType == EXE_DATE) {
				exeRecordList.addAll(list);

				exeDateQueryLl.setVisibility(View.VISIBLE);
				exeRecordCount = Integer.parseInt(recordCount);
				if (curExeIndex + pageSize < exeRecordCount) {
					initExeListFootView();
				} else {
					exeDateQueryResultLv.removeFooterView(moreBtn);
				}
				curExeIndex += pageSize;
			}

			// queryTransferRecordList.addAll(list);
			if (isPreClickMore) {
				preDateAdapter.setDate(preRecordList);
			} else if (isExeClickMore) {
				exeDateApater.setDate(exeRecordList);
			} else {
				showQueryResult(list);
			}
		}

	}

	/**
	 * 显示查询结果
	 * 
	 */
	private void showQueryResult(List<Map<String, Object>> list) {
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

		// 预约日期查询
		if (QueryType == PRE_DATE) {
			preDateAdapter = new ManageDateAdapter(QueryDateActivity1.this,
					list);
			// 预约日期查询
			queryResultLv.setAdapter(preDateAdapter);
			queryResultLv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long id) {
					Map<String, Object> map = preRecordList.get(position);
					paymentDate = (String) map
							.get(Tran.MANAGE_PRE_PAYMENTDATE_RES);
					requestForTransPreRecordDetailQuery(position);
				}
			});
		} else if (QueryType == EXE_DATE) {// 执行日期
			exeDateApater = new ManageExeDateQueryAdapter(
					QueryDateActivity1.this, list);
			// 执行日期查询
			exeDateQueryResultLv.setAdapter(exeDateApater);
			// 设置标题信息
			exeDateQueryResultLv
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int position, long id) {
							requestForTransPreRecordDetailQuery(position);
						}
					});
		}
	}

	/**
	 * PsnTransPreRecordDetailQuery预约交易详情查询req
	 */
	public void requestForTransPreRecordDetailQuery(int position) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.TRANSPRERECORDDETAILQUERY);
		Map<String, Object> queryTransferRecord = null;
		if (QueryType == PRE_DATE) {
			queryTransferRecord = preRecordList.get(position);
		} else if (QueryType == EXE_DATE) {
			queryTransferRecord = exeRecordList.get(position);
		}
		// 转账批次号
		String tranSeq = (String) queryTransferRecord
				.get(Tran.MANAGE_PRE_BATSEQ_RES);
		// 网银交易序号
		String transactionId = (String) queryTransferRecord
				.get(Tran.MANAGE_PRE_TRANSACTIONID_RES);

		Map<String, String> map = new HashMap<String, String>();
		if (QueryType == PRE_DATE) {// 预约日期
									// 查询
			map.put(Tran.MANAGE_PREDETAIL_DATETYPE_REQ,
					ConstantGloble.PREDATE_QUERY);
			map.put(Tran.MANAGE_PREDetail_BATSEQ_REQ, tranSeq);

		} else if (QueryType == EXE_DATE) {// 执行日期
											// 查询
			map.put(Tran.MANAGE_PREDETAIL_DATETYPE_REQ,
					ConstantGloble.EXEDATE_QUERY);
			map.put(Tran.MANAGE_PREDETAIL_TRANSACTIONID_REQ, transactionId);

		}
		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"transPreRecordDetailQueryCallBack");
	}

	/**
	 * PsnTransPreRecordDetailQuery预约交易详情查询 res
	 */
	@SuppressWarnings("unchecked")
	public void transPreRecordDetailQueryCallBack(Object resultObj) {

		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setQueryDetailCallBackMap(result);
		Intent intent = new Intent();
		intent.putExtra(ConstantGloble.TRAN_MANAGER_QUERY_TYPE, QueryType);
		intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
		if (QueryType == PRE_DATE) {
			intent.putExtra(ConstantGloble.PAYMENTDATE, paymentDate);
		}
		intent.setClass(QueryDateActivity1.this, QueryDateDetailActivity1.class);
		startActivityForResult(intent, 2);
	}

	/**
	 * 预约日期头部view
	 * 
	 */
	private void initPreDateHeaderView() {
		View view = LayoutInflater.from(this).inflate(
				R.layout.tran_manage_trans_recocrds_listview_header, null);
		TextView column_1 = (TextView) view.findViewById(R.id.tv_column_1);
		TextView column_2 = (TextView) view.findViewById(R.id.tv_column_2);
		TextView column_3 = (TextView) view.findViewById(R.id.tv_column_3);
		String column1 = this.getResources()
				.getString(R.string.manage_pre_date);
		String column2 = this.getResources().getString(
				R.string.manage_pre_transeq);
		String column3 = this.getResources().getString(
				R.string.manage_payee_list);
		column_1.setText(column1);
		column_2.setText(column2);
		column_3.setText(column3);
		preDateQueryLl.addView(view, 0);
	}

	private void initPreListFootView() {
		queryResultLv.removeFooterView(moreBtn);
		moreBtn.setOnClickListener(moreClickListener);
		queryResultLv.addFooterView(moreBtn);
	}

	private void initExeListFootView() {
		exeDateQueryResultLv.removeFooterView(moreBtn);
		moreBtn.setOnClickListener(exeMoreClickListener);
		exeDateQueryResultLv.addFooterView(moreBtn);
	}

	private void initExeDateHeaderView() {
		View view = LayoutInflater.from(this).inflate(
				R.layout.tran_manage_trans_recocrds_listview_header, null);
		TextView column_1 = (TextView) view.findViewById(R.id.tv_column_1);
		TextView column_2 = (TextView) view.findViewById(R.id.tv_column_2);
		TextView column_3 = (TextView) view.findViewById(R.id.tv_column_3);
		String column1 = this.getResources()
				.getString(R.string.manage_exe_date);
		String column2 = this.getResources().getString(
				R.string.manage_transaction_id);
		String column3 = this.getResources().getString(
				R.string.manage_payee_list);
		column_1.setText(column1);
		column_2.setText(column2);
		column_3.setText(column3);
		exeDateQueryLl.addView(view, 0);
	}

	private OnClickListener moreClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			isPreClickMore = true;
			requestForTransPreRecordQuery(startDateStr, endDateStr);
		}
	};

	private OnClickListener exeMoreClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			isExeClickMore = true;
			requestForTransPreRecordQuery(startDateStr, endDateStr);
		}
	};

	/**
	 * 重置状态数据
	 */
	private void resetData() {
		curPreIndex = 0;
		curExeIndex = 0;
		isPreClickMore = false;
		isExeClickMore = false;
		if (QueryType == PRE_DATE) {
			preRecordList.clear();
		} else if (QueryType == EXE_DATE) {
			exeRecordList.clear();
		}
		if(!searchLayout.isShown()){
			searchLayout.setVisibility(View.VISIBLE);
		}
		if(searchResultLayout.isShown()){
			searchResultLayout.setVisibility(View.GONE);
		}
		// queryTransferRecordList.clear();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2 && resultCode == 104) {
			QueryType = TranDataCenter.getInstance().getQueryType();
			resetData();
			requestForTransPreRecordQuery(startDateStr, endDateStr);
		}
	}

	private void changeDateText(String startDate, String endDate) {
		String text = getResources().getString(R.string.acc_query_date);
		startDateTv.setText(startDate);
		endDateTv.setText(endDate);
		searchResultTimeText.setText(text + startDate + "-" + endDate);
	}

	private void setCurDatePickTime(String strStartDate, String strEndDate) {
		startYear = Integer.parseInt(strStartDate.substring(0, 4));
		startMonth = Integer.parseInt(strStartDate.substring(5, 7));
		startDay = Integer.parseInt(strStartDate.substring(8, 10));
		endYear = Integer.parseInt(strEndDate.substring(0, 4));
		endMonth = Integer.parseInt(strEndDate.substring(5, 7));
		endDay = Integer.parseInt(strEndDate.substring(8, 10));
	}

	/**
	 * 清空listview数据 刷新listview视图
	 */
	private void clearListView() {
		if (QueryType == PRE_DATE) {
			preRecordList.clear();
			if (!StringUtil.isNullOrEmpty(preDateAdapter)) {
				preDateAdapter.setDate(preRecordList);
				queryResultLv.removeFooterView(moreBtn);
			}
		} else if (QueryType == EXE_DATE) {
			exeRecordList.clear();
			if (!StringUtil.isNullOrEmpty(exeDateApater)) {
				exeDateApater.setDate(exeRecordList);
				exeDateQueryResultLv.removeFooterView(moreBtn);

			}
		}
	}
}
