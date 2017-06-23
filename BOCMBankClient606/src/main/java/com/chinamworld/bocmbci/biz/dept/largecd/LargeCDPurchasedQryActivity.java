package com.chinamworld.bocmbci.biz.dept.largecd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.bii.constant.GatherInitiative;
import com.chinamworld.bocmbci.bii.constant.Third;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.biz.dept.adapter.LargeCDPurchasedListAdapter;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 查询已购买大额存单
 * @author liuh
 */
public class LargeCDPurchasedQryActivity extends DeptBaseActivity implements OnClickListener {
	/** 存储查询后的数据 */
	private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
	/** 查询类型 */
	private String[] queryTranType;
	/** 底部listview Layout */
	private LinearLayout listContentLayout;
	private ListView listView;
	/** 下拉布局 */
	private LinearLayout popupLayout;
	/** 下拉箭头 */
	private LinearLayout ivPullDown;
	/** 收起箭头 */
	private LinearLayout ivPullUp;
	/** * 查询类型 */
	private Spinner tranTypeSp;
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
	/** 查询后的签约账户 */
	private TextView queryDateContent;
	/** listview的更多条目 */
	private View viewFooter;
	private Button btnMore;
	/** 当前请求list数据的index */
	private int mCurrentIndex = 0;
	/** 总条目数 */
	private int totalCount = 0;
	/** 记录当前的Pop窗口状态 */
	private boolean isPullDownPop = false;
	private Map<String, Object> signedAcc;
	/** 列表数据的adapter */
	private LargeCDPurchasedListAdapter adapter;
	/** 查询类型ArrayAdapter */
	private ArrayAdapter<String> tranTypeAdapter;
	private LinearLayout tabcontent;
	private View view;
	// add by luqp 2016年3月17日 修改tranType默认值为1
	/** 查询类型 */
	private int tranType = 1;
	private boolean isShowFirst = true;
	private TextView queryTransTypeContent;
	private View rootView;
	/** 查询结果页面 存单状态*/
	private TextView queryTypeTextView;
	/** 查询后布局 */
	private LinearLayout topupLayout;
	/** 查询类型 */
	private String queryType = null;
	/** 查询类型 */
	private String strQueryTypes = null;
	/** 修改提示信息   或选择起始日期查询*/
	private TextView promptInformation;
	/** 资金账户 */
	private String accountNumber;
	/** 资金账户 账户类型 */
	private String accountType;
	/** 资金账户 */
	private TextView capitalAccount;
	/** 查询后资金账户 */
	private TextView queryCapitalAccount;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!getIntentData()) {
			initViews();
			queryTranType = getResources().getStringArray(R.array.queryTranType);
			signedAcc = DeptDataCenter.getInstance().getSignedAcc();
			accountNumber = StringUtil.getForSixForString((String) signedAcc.get(Dept.ACCOUNT_NUMBER));
			accountType = LocalData.AccountType.get(signedAcc.get(Dept.LargeSign_accountType));
//			rootView = view.findViewById(R.id.large_cd_query_layout);
			// 返回按钮
			ibBack = (Button) this.findViewById(R.id.ib_back);
			ibBack.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(LargeCDPurchasedQryActivity.this, LargeCDMenuActivity.class);
				startActivity(intent);
				finish();
				}
			});
			init();
		}
	}

	private boolean getIntentData() {
		Intent intent = getIntent();
		dateTime = intent.getStringExtra("dateTime");
		currentTime = dateTime;
		return currentTime == null;
	}

	/** 初始化view和控件*/
	@SuppressLint("InflateParams")
	private void init() {
//		popupLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.large_cd_purchased_query_popwindow, null);
		// add by luqp 2016年4月6日  修改查询条件遮挡ListView
		popupLayout = (LinearLayout) view.findViewById(R.id.layout_notmg_pop);
		topupLayout = (LinearLayout) view.findViewById(R.id.layout_the_top);
		queryCapitalAccount = (TextView) findViewById(R.id.query_capital_account);
		queryTransTypeContent = (TextView) findViewById(R.id.large_cd_query_transtype_tv);
		queryDateContent = (TextView) findViewById(R.id.large_cd_query_date_tv);
		// 收起箭头
		ivPullUp = (LinearLayout) popupLayout.findViewById(R.id.img_arrow_up);
		//  ----------------------或选择起始日期查询-----------------------
		promptInformation = (TextView)findViewById(R.id.prompt_information);

		initQueryBeforeLayout();
		initQueryAfterLayout();
		ivPullUp.setClickable(false);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && isShowFirst) {
			isShowFirst = false;
//			PopupWindowUtils.getInstance().getQueryPopupWindow(popupLayout, BaseDroidApp.getInstanse().getCurrentAct());
//			PopupWindowUtils.getInstance().showQueryPopupWindowFirst();
		}
	}

	/** 查询后layout*/
	private void initQueryAfterLayout() {
		// 下拉箭头
		ivPullDown = (LinearLayout) findViewById(R.id.img_arrow_down);
		// =========================================================
		// add by luqp 2016年4月6日修改查询条件遮挡ListView
		ivPullDown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupLayout.setVisibility(View.VISIBLE);
				topupLayout.setVisibility(View.GONE);
			}
		});
		// =========================================================
		listContentLayout = (LinearLayout) this.findViewById(R.id.large_cd_list_layout);
		listView = (ListView) findViewById(R.id.dept_notmg_querylist);
		listView.setOnItemClickListener(itemClickListener);
		viewFooter = View.inflate(this, R.layout.acc_load_more, null);
		btnMore = (Button) viewFooter.findViewById(R.id.btn_load_more);
		// btnMore.setVisibility(View.GONE);
		btnMore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				requestForPurchasedRecordQuery(false);
			}
		});
		initAfterLayout();
	}

	/** 查询后layout*/
	private void initAfterLayout() {
//		queryTransTypeContent.setText(queryTranType[tranType]);
//		queryTransTypeContent.setText(strQueryTypes);
		queryDateContent.setText(startDate + "-" + endDate);
	}

	/** 初始化条件查询View 和 点击事件*/
	private void initQueryBeforeLayout() {
		capitalAccount = (TextView) popupLayout.findViewById(R.id.capital_account);
		capitalAccount.setText(accountNumber+" "+accountType);
		tranTypeSp = (Spinner) popupLayout.findViewById(R.id.large_cd_tran_type);
		textStartDate = (TextView) popupLayout.findViewById(R.id.tv_startdate_query_date_preandexe);
		textEndDate = (TextView) popupLayout.findViewById(R.id.tv_enddate_query_date_preandexe);
		textStartDate.setOnClickListener(chooseDateClick);
		textEndDate.setOnClickListener(chooseDateClick);
		// 三天前的日期
//		String startThreeDate = QueryDateUtils.getlastthreeDate(currentTime);
//		String startThreeDate = startDate = QueryDateUtils.getlastOneMonth(currentTime);
		String startThreeDate = null; //add by 2016年10月13日 默认上送查询时间为空
		textStartDate.setText(startThreeDate);
		String currenttime = QueryDateUtils.getcurrentDate(currentTime); // 系统当前时间格式化
		textEndDate.setText(currenttime); // 初始结束时间赋值
		// 选择Spinner {空：全部, 0：正常 , 1：已转让, 2: 已赎回, 3：止付}
		tranTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, queryTranTypeList);
		tranTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		tranTypeSp.setAdapter(tranTypeAdapter);
		tranTypeSp.setSelection(1); //查询类型默认查询正常
		// 查询按钮
		Button queryBtn = (Button) popupLayout.findViewById(R.id.btn_query_date_preandexe);
		oneWeekBtn = (Button) popupLayout.findViewById(R.id.btn_oneweek_query_date_preandexe);
		oneMonthBtn = (Button) popupLayout.findViewById(R.id.btn_onemonth_query_date_preandexe);
		threeMonthBtn = (Button) popupLayout.findViewById(R.id.btn_threemonth_query_date_preandexe);

		oneWeekBtn.setOnClickListener(this); // 查询最近一周
		oneMonthBtn.setOnClickListener(this); // 查询最近一月
		threeMonthBtn.setOnClickListener(this); // 查询最近三个月

		queryType = "0";
		// 查询类型
		tranTypeSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view, int position, long arg3) {
				strQueryTypes = queryTranTypeList.get(position);
				queryType = LocalData.LargeQueryTranTypeMap.get(strQueryTypes);
				queryTransTypeContent.setText(strQueryTypes);
				queryCapitalAccount.setText(accountNumber+" "+accountType);
				// add by luqp 存单状态默认值“正常”、修改显示“或选择起止日期查询”
				if ("0".equals(queryType)) {
					promptInformation.setText("或选择起止日期查询");
				} else {
					promptInformation.setText("或选择起始日期查询");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				strQueryTypes = queryTranTypeList.get(0);
				queryType = LocalData.LargeQueryTranTypeMap.get(strQueryTypes);
			}
		});

		queryBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
				popupLayout.setVisibility(View.GONE);
				topupLayout.setVisibility(View.VISIBLE);
				excuseQuery();
			}
		});

		startDate = startThreeDate;
		endDate = currenttime;
		//////////////////////////////////////////////////////////////////
		// add by luqp 2016年3月17日 默认显示查询信息
		excuseQuery();
		//////////////////////////////////////////////////////////////////
		// =========================================================
		// add by luqp 2016年4月6日修改查询条件遮挡ListView
		ivPullUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
				popupLayout.setVisibility(View.GONE);
				topupLayout.setVisibility(View.VISIBLE);
			}
		});
		// ========================================================
	}

	/** 执行查询操作*/
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
		listData.clear();
//		refreshListView(listData);
		requestForPurchasedRecordQuery(true);
	}

	/** 查询已购买大额存单产品*/
	public void requestForPurchasedRecordQuery(boolean isRefresh) {
		if (isRefresh) {
			mCurrentIndex = 0;
		} else {
			mCurrentIndex += Third.PAGESIZE;
		}
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.PSN_LARGE_CD_PURCHASED_QRY);
		String conversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(conversationId);
		Map<String, Object> params = new HashMap<String, Object>();
		String accountId = String.valueOf(signedAcc.get(Dept.ACCOUNT_ID));
		params.put(Dept.ACCOUNT_ID, accountId);
//		String tranTypeStr = tranType > 0 ? String.valueOf(tranType - 1) : "";
		params.put(Dept.QUERY_TYPE, queryType);
//		params.put(GatherInitiative.START_DATE, startDate);
//		params.put(GatherInitiative.END_DATE, endDate);
		params.put(PubConstants.PUB_QUERY_FEILD_PAGE_SIZE, String.valueOf(Third.PAGESIZE));
		params.put(PubConstants.PUB_QUERY_FEILD_CURRENT_INDEX, EpayUtil.getString(mCurrentIndex, "0"));
		params.put(PubConstants.PUB_QUERY_FEILD_IS_REFRESH, EpayUtil.getString(isRefresh, "true"));
		biiRequestBody.setParams(params);

		HttpManager.requestBii(biiRequestBody, this, "purchasedRecordQueryCallBack");
	}

	/**
	 * 回调 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void purchasedRecordQueryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> map = (Map<String, Object>) biiResponseBody.getResult();
		String recordNumber = (String) map.get(Dept.RECORD_NUMBER);
		if (!StringUtil.isNullOrEmpty(recordNumber)) {
			totalCount = Integer.parseInt(recordNumber);
		}
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) map.get(ConstantGloble.LIST);
		if (StringUtil.isNullOrEmpty(resultList)) {
			// 2016年3月4日 luqp 注
//			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getResources().getString(R.string.no_list_data));
			////////////////////////////////////////////////////////////////////////////////////////////////////
			// add by 2016年3月4日 提示用户:没有可支取的大额存单
			BaseDroidApp.getInstanse().ShowPromptDialog(this.getResources().getString(R.string.prompt_no_large),exitClick);
			popupLayout.setVisibility(View.VISIBLE);
			topupLayout.setVisibility(View.GONE);
			listData.clear();
			showQueryResultView(false);
			return;
		}
		for (int i = 0; i < resultList.size(); i++) {
			listData.add((Map<String, Object>) resultList.get(i));
		}
		if (listData.size() >= totalCount) {
			listView.removeFooterView(viewFooter);
		} else {
			if (listView.getFooterViewsCount() > 0) {

			} else {
				listView.addFooterView(viewFooter);
			}
		}
		showQueryResultView(true);
	}
	
	/** 提示框关闭按钮点击事件*/
	protected OnClickListener exitClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
			finish();
		}
	};

	/** 展示查询后的页面*/
	private void showQueryResultView(boolean isDissPop) {
//		if (rootView.getVisibility() == View.INVISIBLE) {
//			rootView.setVisibility(View.VISIBLE);
//		}
		if (isDissPop) {
			isPullDownPop = false;
			PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
			listContentLayout.setVisibility(View.VISIBLE);
			queryDateContent.setText(startDate + "-" + endDate);
			ivPullUp.setClickable(true);
			refreshListView(listData);
		} else {// 查询结果数据为空的时候
			ivPullUp.setClickable(false);
			listContentLayout.setVisibility(View.GONE);
			refreshListView(listData);
		}
	}

	/** 列表*/
	private void refreshListView(List<Map<String, Object>> listData) {
		if (adapter == null) {
			adapter = new LargeCDPurchasedListAdapter(this, listData);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(itemClickListener);
		} else {
			adapter.setListData(listData);
			adapter.notifyDataSetChanged();
		}
	}

	@SuppressLint("InflateParams")
	private void initViews() {
		setTitle(getString(R.string.large_cd_check_see));
		view = mInflater.inflate(R.layout.large_cd_purchased_qry, null);
		tabcontent = (LinearLayout) findViewById(R.id.sliding_body);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 0);
		}
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
			DatePickerDialog dialog = new DatePickerDialog(LargeCDPurchasedQryActivity.this, new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					StringBuilder date = new StringBuilder();
					date.append(String.valueOf(year));
					date.append("/");
					int month = monthOfYear + 1;
					date.append(((month < 10) ? ("0" + month) : (month + "")));
					date.append("/");
					date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth) : (dayOfMonth + "")));
					((TextView) v).setText(String.valueOf(date)); // 为日期赋值
				}
			}, startYear, startMonth - 1, startDay);
			dialog.show();
		}
	};

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
						PopupWindowUtils.getInstance().showQueryPopupWindow();
						isPullDownPop = false;
					}
				}
			}, 500);
		}
	}

	/** 重置开始日期和结束日期的值*/
	private void changeDateText(String startDate, String endDate) {
//		queryTransTypeContent.setText(queryTranType[tranType]);
		queryTransTypeContent.setText(strQueryTypes);
		queryCapitalAccount.setText(accountNumber+" "+accountType);
		queryDateContent.setText(startDate + "-" + endDate);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_oneweek_query_date_preandexe:// 最近一周
			PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
			popupLayout.setVisibility(View.GONE);
			topupLayout.setVisibility(View.VISIBLE);
			startDate = QueryDateUtils.getlastWeek(dateTime).trim();
			endDate = QueryDateUtils.getcurrentDate(dateTime).trim();
			listData.clear();
			// 2016年3月15日  luqp 注(修改查询最近一周时数据加载不全)
//			refreshListView(listData);
			changeDateText(startDate, endDate);
			requestForPurchasedRecordQuery(true);
			break;
		case R.id.btn_onemonth_query_date_preandexe:// 最近一月
			PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
			popupLayout.setVisibility(View.GONE);
			topupLayout.setVisibility(View.VISIBLE);
			startDate = QueryDateUtils.getlastOneMonth(dateTime).trim();
			endDate = QueryDateUtils.getcurrentDate(dateTime).trim();
			listData.clear();
			// 2016年3月15日  luqp 注(修改查询最近一月时数据加载不全)
//			refreshListView(listData);
			changeDateText(startDate, endDate);
			requestForPurchasedRecordQuery(true);
			break;
		case R.id.btn_threemonth_query_date_preandexe:// 最近三月
			PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
			popupLayout.setVisibility(View.GONE);
			topupLayout.setVisibility(View.VISIBLE);
			startDate = QueryDateUtils.getlastThreeMonth(dateTime).trim();
			endDate = QueryDateUtils.getcurrentDate(dateTime).trim();
			listData.clear();
			// 2016年3月15日  luqp 注(修改查询最近三月时数据加载不全)
//			refreshListView(listData);
			changeDateText(startDate, endDate);
			requestForPurchasedRecordQuery(true);
			break;
		default:
			break;
		}

	}

	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int position, long paramLong) {
			Map<String, Object> map = (Map<String, Object>) listData.get(position);
			DeptDataCenter.getInstance().setPurchasedDetail(map);
			Intent intent = new Intent(LargeCDPurchasedQryActivity.this, LargeCDPurchasedDetailActivity.class);
			startActivity(intent);
		}
	};
}
