package com.chinamworld.bocmbci.biz.gatherinitiative.payquery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.DrawMoney;
import com.chinamworld.bocmbci.bii.constant.GatherInitiative;
import com.chinamworld.bocmbci.biz.drawmoney.DrawMoneyData;
import com.chinamworld.bocmbci.biz.gatherinitiative.GatherBaseActivity;
import com.chinamworld.bocmbci.biz.gatherinitiative.GatherInitiativeData;
import com.chinamworld.bocmbci.biz.gatherinitiative.creatgather.CreatGatherInputInfoActivity;
import com.chinamworld.bocmbci.biz.gatherinitiative.gatherquery.GatherQueryResultAdapter;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: PayQueryActivity
 * @Description: 付款指令查询页面
 * @author JiangWei
 * @date 2013-8-20上午10:25:47
 */
public class PayQueryActivity extends GatherBaseActivity implements
		OnClickListener {
	private LayoutInflater inflater = null;
	private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
	/** 底部listview Layout */
	private LinearLayout listContentLayout;
	/** 未查询的时候提示语言 */
	private TextView textNotifyInfo;
	/** 排序的layout */
	private LinearLayout ll_sort;
	/**  */
	private ImageView img_sort_icon;
	/** 排序button */
	private Button sortTextBtn;

	private ListView listView;
	/** 下拉箭头 */
	private LinearLayout ivPullDown;
	/** 收起箭头 */
	private LinearLayout ivPullUp;
	/** 列表数据的adapter */
	private GatherQueryResultAdapter adapter;

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
	private String pageSize = "10";
	private boolean isNeedChooseAccount = true;
	/** 记录当前的Pop窗口状态 */
	private boolean isPullDownPop = false;
	private static final int REQUEST_INPUT_INFO_CODE = 10;
	private int curPosition = -1;
	/** 有查询按钮的界面 */
	private LinearLayout query_first;
	/** 没有查询 按钮的界面 */
	private RelativeLayout layout_the_top;
	/** 判断是否是第一次进入 */
	private boolean isfirstShow = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.pay_instruct_query);
		inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.gather_query_activity, null);
		tabcontent.addView(view);
		
		query_first = (LinearLayout) findViewById(R.id.query_first);
		layout_the_top = (RelativeLayout) findViewById(R.id.layout_the_top);
		
		setLeftSelectedPosition("GatherInitiative_2");
		Button btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setText(R.string.creat_new_gather);
		if (btnRight != null) {
			btnRight.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(PayQueryActivity.this,
							CreatGatherInputInfoActivity.class);
					// startActivity(intent);
					startActivityForResult(intent, REQUEST_INPUT_INFO_CODE);
				}
			});
		}
		isNeedChooseAccount = this.getIntent().getBooleanExtra(
				GatherBaseActivity.IS_NEED_CHOOSE_ACCOUNT, true);
		tabcontent.setPadding(
				0,
				0,
				0,
				getResources().getDimensionPixelSize(
						R.dimen.common_bottom_padding_new));
		BaseHttpEngine.showProgressDialogCanGoBack();
		requestSystemDateTime();
		initQueryWindow();
	}

	private TextView tv_startdate, tv_enddate;
	private View queryWin;

	/** 初始化查询框，只在进入页面后出现，之后则使用popupwindow */
	private void initQueryWindow() {
//		queryWin = findViewById(R.id.layout_query_window);
		tv_startdate = (TextView) findViewById(R.id.tv_startdate_query_date_preandexe);
		tv_enddate = (TextView) findViewById(R.id.tv_enddate_query_date_preandexe);
		tv_startdate.setOnClickListener(chooseDateClick);
		tv_enddate.setOnClickListener(chooseDateClick);
		Button queryBtn = (Button) findViewById(R.id.btn_query_date_preandexe);
		Button btn_oneweek_query = (Button) findViewById(R.id.btn_oneweek_query_date_preandexe);
		Button btn_onemonth_query = (Button) findViewById(R.id.btn_onemonth_query_date_preandexe);
		Button btn_threemonth_query = (Button) findViewById(R.id.btn_threemonth_query_date_preandexe);
		queryBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				excuseQuery();
//				onClickCondition();
			}
		});
		btn_oneweek_query.setOnClickListener(this);
		btn_onemonth_query.setOnClickListener(this);
		btn_threemonth_query.setOnClickListener(this);
	}

	/** 关闭查询框(布局) */
	private void closeQuerWindow() {
		if (queryWin.isShown()) {
			Animation animation = AnimationUtils.loadAnimation(this,
					R.anim.scale_out);
			animation.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					queryWin.setVisibility(View.GONE);
				}
			});
			queryWin.startAnimation(animation);
		}
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		currentTime = dateTime;

		View rootView = findViewById(R.id.dept_after_query_layout);
		rootView.setVisibility(View.VISIBLE);
//		View layout_query_window = findViewById(R.id.layout_query_window);
//		layout_query_window.setVisibility(View.VISIBLE);
		init();
	}

	/**
	 * @Title: init
	 * @Description: 初始化view和控件
	 * @param
	 * @return void
	 */
	private void init() {
//		popupLayout = (RelativeLayout) LayoutInflater.from(this).inflate(
//				R.layout.gather_query_popwindow, null);
//		PopupWindowUtils.getInstance().getQueryPopupWindow(popupLayout,
//				BaseDroidApp.getInstanse().getCurrentAct());

		sortTextBtn = (Button) findViewById(R.id.sort_text);
		sortTextBtn.setText(LocalData.filterRemitStatus[0]);
		ll_sort = (LinearLayout) findViewById(R.id.dept_ll_sort);
		// ll_sort.setVisibility(View.VISIBLE);
		img_sort_icon = (ImageView) findViewById(R.id.img_sort_icon);
		img_sort_icon.setBackgroundResource(R.drawable.icon_paixu_time);
		// 初始化排序框
//		PopupWindowUtils.getInstance().setOnPullSelecterListener(this, ll_sort,
//				LocalData.filterRemitStatus, null, sortClick);
		queryDateContent = (TextView) findViewById(R.id.dept_query_cdnumber_tv);

		// 收起箭头
		ivPullUp = (LinearLayout) findViewById(R.id.ll_pull_up_query_preandexe);
		initQueryBeforeLayout();
		initQueryAfterLayout();
		// PopupWindowUtils.getInstance().showQueryPopupWindowFirst();//小米、华为随机bug
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
				if (isfirstShow == false) {
					if (query_first.isShown()) {
						layout_the_top.setVisibility(View.VISIBLE);
						query_first.setVisibility(View.GONE);

					} else {
						query_first.setVisibility(View.VISIBLE);
						layout_the_top.setVisibility(View.GONE);
					}
				}

//				PopupWindowUtils.getInstance().getQueryPopupWindow(popupLayout,
//						BaseDroidApp.getInstanse().getCurrentAct());
//				PopupWindowUtils.getInstance().showQueryPopupWindow();
			}
		});

		listContentLayout = (LinearLayout) this
				.findViewById(R.id.dept_account_list_layout);
		textNotifyInfo = (TextView) this
				.findViewById(R.id.text_gather_query_notify_content);
		textNotifyInfo.setText(R.string.pay_query_notify_content);
		LinearLayout headerLayout = (LinearLayout) LayoutInflater.from(this)
				.inflate(R.layout.dept_notmg_list_item, null);
		TextView cdNumberTv = (TextView) headerLayout
				.findViewById(R.id.dept_cd_number_tv);
		TextView currencyTv = (TextView) headerLayout
				.findViewById(R.id.dept_type_tv);
		TextView availableBalanceTv = (TextView) headerLayout
				.findViewById(R.id.dept_avaliable_balance_tv);
		ImageView goDetailIv = (ImageView) headerLayout
				.findViewById(R.id.dept_notify_detail_iv);
		String strCdNumber = this.getResources().getString(
				R.string.gather_creat_date);
		cdNumberTv.setText(strCdNumber);
		String strSaveType = this.getResources().getString(
				R.string.payee_no_label);
		currencyTv.setText(strSaveType);
		String strAvaiableBalance = this.getResources().getString(
				R.string.isForex_query_result4);
		availableBalanceTv.setText(strAvaiableBalance);
		goDetailIv.setVisibility(View.INVISIBLE);
		headerLayout.setClickable(false);
		listContentLayout.addView(headerLayout, 0);
		listView = (ListView) findViewById(R.id.dept_notmg_querylist);
		viewFooter = inflater.inflate(R.layout.acc_load_more, null);
		listView.addFooterView(viewFooter);
		btnMore = (Button) viewFooter.findViewById(R.id.btn_load_more);
		btnMore.setBackgroundColor(Color.TRANSPARENT);
		viewFooter.setVisibility(View.GONE);
		btnMore.setVisibility(View.GONE);
		btnMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestPsnTransActQueryPaymentOrderList();
			}
		});

		initAfterLayout();
	}

	private void initQueryBeforeLayout() {
		textStartDate = (TextView) findViewById(R.id.tv_startdate_query_date_preandexe);
		textEndDate = (TextView)findViewById(R.id.tv_enddate_query_date_preandexe);
		textStartDate.setOnClickListener(chooseDateClick);
		textEndDate.setOnClickListener(chooseDateClick);
		// 三天前的日期
		String startThreeDate = QueryDateUtils.getlastthreeDate(currentTime);
		textStartDate.setText(startThreeDate);
		tv_startdate.setText(startThreeDate);
		// 系统当前时间格式化
		String currenttime = QueryDateUtils.getcurrentDate(currentTime);
		// 初始结束时间赋值
		textEndDate.setText(currenttime);
		tv_enddate.setText(currenttime);
		// 查询按钮
		Button queryBtn = (Button) findViewById(R.id.btn_query_date_preandexe);
		oneWeekBtn = (Button) findViewById(R.id.btn_oneweek_query_date_preandexe);
		oneMonthBtn = (Button)findViewById(R.id.btn_onemonth_query_date_preandexe);
		threeMonthBtn = (Button)findViewById(R.id.btn_threemonth_query_date_preandexe);
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
//				onClickCondition();
			}
		});

		ivPullUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
				if (isfirstShow == false) {
					if (query_first.isShown()) {
						layout_the_top.setVisibility(View.VISIBLE);
						query_first.setVisibility(View.GONE);

					} else {
						query_first.setVisibility(View.VISIBLE);
						layout_the_top.setVisibility(View.GONE);
					}
				}
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
		String startDatePre = "", endDatePre = "";
		
		if (query_first.isShown()) {
			startDatePre = tv_startdate.getText().toString().trim();
			endDatePre = tv_enddate.getText().toString().trim();
			textStartDate.setText(startDatePre);
			textEndDate.setText(endDatePre);
		} else {
			startDatePre = textStartDate.getText().toString().trim();
			endDatePre = textEndDate.getText().toString().trim();
		}
		if (QueryDateUtils.compareDateOneYear(startDatePre, currentTime)) {
			// 起始日期不能早于系统当前日期一年前
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_check_start_enddate));
			return;
		}
		if (QueryDateUtils.compareDate(endDatePre, currentTime)) {
			// 结束日期在服务器日期之前
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_check_enddate));
			return;
		}
		if (QueryDateUtils.compareDate(startDatePre, endDatePre)) {
			// 开始日期在结束日期之前

		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_query_errordate));
			return;
		}
		if (!QueryDateUtils.compareDateThree(startDatePre, endDatePre)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_check_start_end_date));
			return;
		}
		startDate = startDatePre;
		endDate = endDatePre;

		mCurrentIndex = 0;
		listData.clear();
		onClickCondition();
		requestPsnTransActQueryPaymentOrderList();
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
			DatePickerDialog dialog = new DatePickerDialog(
					PayQueryActivity.this, new OnDateSetListener() {

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
	 * 刷新通知存款 列表
	 */
	private void refreshListView(List<Map<String, Object>> listData) {
		if (adapter == null) {
			adapter = new GatherQueryResultAdapter(this, listData);
			adapter.setIsPayQuery(true);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(listViewItemClickListener);
		} else {
			adapter.setListData(listData);
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * listView条目点击事件
	 */
	private OnItemClickListener listViewItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			requestPsnTransActPaymentOrderDetail(position);
			curPosition = position;
		}
	};

	/**
	 * @Title: requestPsnTransActQueryReminderOrderList
	 * @Description: 请求“付款指令查询”接口
	 * @param
	 * @return void
	 * @throws
	 */
	public void requestPsnTransActQueryPaymentOrderList() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody
				.setMethod(GatherInitiative.PSN_TRANS_ACT_QUERY_PAYMENT_ORDER_LIST);
		Map<String, Object> map = new HashMap<String, Object>();

		map.put(GatherInitiative.START_DATE, startDate);
		map.put(GatherInitiative.END_DATE, endDate);
		map.put(GatherInitiative.CURRENT_INDEX, mCurrentIndex);
		map.put(GatherInitiative.PAGE_SIZE, pageSize);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnTransActQueryPaymentOrderListCallback");
	}

	/**
	 * @Title: requestPsnTransActQueryReminderOrderListCallback
	 * @Description: 请求“付款指令查询”接口回调
	 * @param @param resultObj
	 * @return void
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnTransActQueryPaymentOrderListCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> map = (Map<String, Object>) biiResponseBody
				.getResult();
		String recordNumber = (String) map.get(GatherInitiative.RECORD_NUM);
		if (!StringUtil.isNullOrEmpty(recordNumber)) {
			totalCount = Integer.parseInt(recordNumber);
		}
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) map
				.get(GatherInitiative.ACTIVE_REMINDER_LIST);
		if (StringUtil.isNullOrEmpty(resultList)) {
			resultList = (List<Map<String, Object>>) map
					.get(GatherInitiative.PAYMENT_RECORD_LIST);
			if (StringUtil.isNullOrEmpty(resultList)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						this.getResources().getString(R.string.no_list_data));
				listData.clear();
				showQueryResultView(false);
				return;
			}
		}
//		closeQuerWindow();
		for (int i = 0; i < resultList.size(); i++) {
			listData.add((Map<String, Object>) resultList.get(i));
		}
		if (listData.size() >= totalCount) {
			viewFooter.setVisibility(View.GONE);
			btnMore.setVisibility(View.GONE);

		} else {
			mCurrentIndex += Integer.parseInt(pageSize);
			viewFooter.setVisibility(View.VISIBLE);
			btnMore.setVisibility(View.VISIBLE);

		}
		DrawMoneyData.getInstance().setQueryCallBackList(listData);
		showQueryResultView(true);
	}

	/**
	 * @Title: requestPsnTransActReminderOrderDetail
	 * @Description: 请求“付款指令详情”接口
	 * @param position
	 *            当前需要查询条目的position
	 * @return void
	 * @throws
	 */
	private void requestPsnTransActPaymentOrderDetail(int position) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody
				.setMethod(GatherInitiative.PSN_TRANS_ACT_PAYMENT_ORDER_DETAIL);
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtil.isNullOrEmpty(listData)
				|| position > listData.size() - 1
				|| StringUtil.isNullOrEmpty(listData.get(position))) {
			return;
		}
		Map<String, Object> itemData = listData.get(position);
		map.put(GatherInitiative.NOTIFY_ID,
				itemData.get(GatherInitiative.NOTIFY_ID));
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnTransActPaymentOrderDetailCallback");
	}

	/**
	 * @Title: requestPsnTransActReminderOrderDetailCallback
	 * @Description: 请求“付款指令详情”接口的回调
	 * @param @param resultObj
	 * @return void
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnTransActPaymentOrderDetailCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> map = (Map<String, Object>) biiResponseBody
				.getResult();
		GatherInitiativeData.getInstance().setDetailInfo(map);

		// 判断该收款状态是否已变化
		String newStatus = (String) map.get(GatherInitiative.STATUS);
		Map<String, Object> itemData = listData.get(curPosition);
		String oldStatus = (String) itemData.get(GatherInitiative.STATUS);
		if (!newStatus.equals(oldStatus)) {
			// 状态变化，重新刷新数据
			listData.get(curPosition).put(GatherInitiative.STATUS, newStatus);
			if (adapter != null) {
				adapter.setListData(listData);
				adapter.notifyDataSetChanged();
			}
		}

		Intent intent = new Intent(this, PayQueryDetailActivity.class);
		intent.putExtra(GatherBaseActivity.IS_NEED_CHOOSE_ACCOUNT,
				isNeedChooseAccount);
		startActivityForResult(intent, 1001);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1001 && resultCode == RESULT_OK) {
			// excuseQuery();
			mCurrentIndex = 0;
			listData.clear();
			requestPsnTransActQueryPaymentOrderList();
		}
	}

	@Override
	protected void onPause() {
		isPullDownPop = PopupWindowUtils.getInstance().isQueryPopupShowing();
		super.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (isPullDownPop) {
			ivPullDown.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (isPullDownPop) {
						// ivPullDown.performClick();
						PopupWindowUtils.getInstance().showQueryPopupWindow();
						isPullDownPop = false;
					}
				}
			}, 500);
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestCommConversationIdCallBack(resultObj);
		excuseQuery();
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
			PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
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

	/** 排序点击事件 */
	OnClickListener sortClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_text1:
				sortTextBtn.setText(LocalData.filterRemitStatus[0]);
				refreshListView(listData);
				break;
			case R.id.tv_text2:
				sortTextBtn.setText(LocalData.filterRemitStatus[1]);
				List<Map<String, Object>> otherData = new ArrayList<Map<String, Object>>();
				// 汇款成功
				for (int i = 0; i < listData.size(); i++) {
					String remitStatus = (String) listData.get(i).get(
							DrawMoney.REMIT_STATUS);
					if (remitStatus.equals("A")) {
						otherData.add(listData.get(i));
					}
				}
				refreshListView(otherData);
				break;
			case R.id.tv_text3:
				sortTextBtn.setText(LocalData.filterRemitStatus[2]);
				List<Map<String, Object>> otherData2 = new ArrayList<Map<String, Object>>();
				// 汇款失败
				for (int i = 0; i < listData.size(); i++) {
					String remitStatus = (String) listData.get(i).get(
							DrawMoney.REMIT_STATUS);
					if (remitStatus.equals("B")) {
						otherData2.add(listData.get(i));
					}
				}
				refreshListView(otherData2);
				break;
			default:
				break;
			}
		}
	};

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
		switch (v.getId()) {
		case R.id.btn_oneweek_query_date_preandexe:// 最近一周
			startDate = QueryDateUtils.getlastWeek(dateTime).trim();
			endDate = QueryDateUtils.getcurrentDate(dateTime).trim();
			mCurrentIndex = 0;
			onClickCondition();
			listData.clear();
			changeDateText(startDate, endDate);
			requestPsnTransActQueryPaymentOrderList();
			break;
		case R.id.btn_onemonth_query_date_preandexe:// 最近一月
			startDate = QueryDateUtils.getlastOneMonth(dateTime).trim();
			endDate = QueryDateUtils.getcurrentDate(dateTime).trim();
			mCurrentIndex = 0;
			onClickCondition();
			listData.clear();
			changeDateText(startDate, endDate);
			requestPsnTransActQueryPaymentOrderList();
			break;
		case R.id.btn_threemonth_query_date_preandexe:// 最近三月
			startDate = QueryDateUtils.getlastThreeMonth(dateTime).trim();
			endDate = QueryDateUtils.getcurrentDate(dateTime).trim();
			mCurrentIndex = 0;
			onClickCondition();
			listData.clear();
			changeDateText(startDate, endDate);
			requestPsnTransActQueryPaymentOrderList();
			break;
		default:
			break;
		}

	}

	private void onClickCondition() {
		isfirstShow = false;
		// wangdk
		if (query_first.isShown()) {
			layout_the_top.setVisibility(View.VISIBLE);
			query_first.setVisibility(View.GONE);

		} else {
			query_first.setVisibility(View.VISIBLE);
			layout_the_top.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		isfirstShow = true;
		super.onDestroy();
	}
}
