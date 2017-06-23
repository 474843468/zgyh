package com.chinamworld.bocmbci.biz.tran.remit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
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
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.remit.adapters.RemitQueryMealDetailAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.SearchView;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomGallery;

/** 汇款套餐明细查询结果页面 */
public class RemitQueryMealDetaiActivity extends TranBaseActivity implements OnClickListener {
	private static final String TAG = "RemitQueryMealDetaiActivity";
	// 查询条件
	private View conditionView = null;
	private CustomGallery gallary = null;
	// private Spinner codeSpinner;
	/** 修改codeSpinner 为TextView */
	private TextView et_loandate;
	private TextView startDateText = null;
	private TextView endDateText = null;
	private Button weekButton = null;
	private Button monthButton = null;
	private Button threeMonthButton = null;
	private Button searchButton = null;
	private View backgroundView = null;
	/** 收起 */
	private View downLayout = null;
	private ImageView acc_frame_left = null;
	private ImageView acc_btn_goitem = null;

	// 查询结果
	private View resultAllView = null;
	private TextView accNoText = null;
	private TextView accCodeText = null;
	private TextView searchTimeText = null;
	/** 下拉 */
	private View upLayout = null;
	private ListView resultListView = null;
	/** 更多按钮 */
	private View load_more = null;

	/** 用户选择的账户 */
	private int selectPosition = -1;
	/** 签约账户数据 */
	private List<Map<String, Object>> accountNoList = null;

	/** 每滑动一次卡片都会改变 */
	private Map<String, Object> currentMap = null;
	private Map<String, Object> beforeCurrentMap = null;
	private int currentPosition = -1;
	private String startThreeDate = null;

	private int pageSize = 10;
	private int currentIndex = 0;
	private boolean refresh = true;
	private int recordNumber = 0;
	private List<Map<String, String>> dateList = null;
	private RemitQueryMealDetailAdapter adapter = null;
	private View view = null;

	private String accountId = null;
	private String account = null;
	private String startDate = null;
	private String endDate = null;
	private String currenttime = null;
	private String shareCardNo = null;

	// ////////////////////////////用户选择账户////////////////////////////////
	private Spinner accSpinner = null;
	/** 得到共享账户列表 */
	private List<String> accListView = null;
	/** 共享账户/卡号 */
	private String shareCard = null;
	/** 账号list*/
	List<Map<String, Object>> accountList;
	
	private String accountNumber = null;
	private String conversationId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.trans_remit_menu_three_query));
		view = mInflater.inflate(R.layout.tran_remit_query_detail, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		toprightBtn();
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 0);
		}
		accountNoList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TRAN_REMIT_SIGN_RESULT);
		if (accountNoList == null || accountNoList.size() <= 0) {
			return;
		}
		
		selectPosition = getIntent().getIntExtra(ConstantGloble.FOREX_customerSelectedPosition, -1);
		if (selectPosition < 0) {
			return;
		}
		currentPosition = getIntent().getIntExtra(ConstantGloble.CURRENT_POSITION, -1);
		if (currentPosition < 0) {
			return;
		}
		// modify luqp 2015年11月20日10:02:49
		accountId = (String) accountNoList.get(currentPosition).get("accountId"); //TODO
		//////////////////////////////////////////////////////////////////////////////////////
		//明细页面显示账号信息
		Map<String, Object> allAccMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TRAN_REMIT_QUERY_RESULT);
		accountList = (List<Map<String, Object>>) allAccMap.get("validAccountList");
	
		dateList = new ArrayList<Map<String, String>>();
		// 请求系统时间
		BaseHttpEngine.showProgressDialogCanGoBack();
		requestSystemDateTime();
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		initCondition();
		init();
		initPageDate();
//		PopupWindowUtils.getInstance().showQueryPopupWindowFirst();
		SearchView.getInstance().addSearchView(upLayout, conditionView);	
		SearchView.getInstance().showSearchView();
		getSpinnerDate();
		BaseHttpEngine.dissMissProgressDialog();
		// 查询明细
		// requestRemitSetMealDetail(startDate, endDate, accountId, shareCardNo,
		// pageSize, currentIndex, refresh);
	}

	/** 初始化查询条件 */
	private void initCondition() {
		conditionView = LayoutInflater.from(this).inflate(R.layout.tran_remit_query_condition, null);
		gallary = (CustomGallery) conditionView.findViewById(R.id.viewPager);
		acc_frame_left = (ImageView) conditionView.findViewById(R.id.acc_frame_left);
		acc_btn_goitem = (ImageView) conditionView.findViewById(R.id.acc_btn_goitem);
		backgroundView = conditionView.findViewById(R.id.ll_query_condition);
		// codeSpinner = (Spinner) conditionView.findViewById(R.id.et_loandate);
		/** 修改codeSpinner 为TextView */
		et_loandate = (TextView) conditionView.findViewById(R.id.et_loandate);

		// ////////////////////////////用户选择账户////////////////////////////////
		accSpinner = (Spinner) conditionView.findViewById(R.id.spinner_acc);

		startDateText = (TextView) conditionView.findViewById(R.id.acc_query_transfer_startdate);
		startDateText.setOnClickListener(this);
		endDateText = (TextView) conditionView.findViewById(R.id.acc_query_transfer_enddate);
		endDateText.setOnClickListener(this);
		weekButton = (Button) conditionView.findViewById(R.id.btn_acc_onweek);
		weekButton.setOnClickListener(this);
		monthButton = (Button) conditionView.findViewById(R.id.btn_acc_onmonth);
		monthButton.setOnClickListener(this);
		threeMonthButton = (Button) conditionView.findViewById(R.id.btn_acc_threemonth);
		threeMonthButton.setOnClickListener(this);
		searchButton = (Button) conditionView.findViewById(R.id.btn_acc_query_transfer);
		searchButton.setOnClickListener(this);
		downLayout = conditionView.findViewById(R.id.ll_upLayout);
		downLayout.setOnClickListener(upOnClickListener);
//		PopupWindowUtils.getInstance().getQueryPopupWindow(conditionView, RemitQueryMealDetaiActivity.this);
		
	}

	/** 初始化查询结果页面 */
	private void init() {
		resultAllView = view.findViewById(R.id.acc_query_result_layout);
		accNoText = (TextView) view.findViewById(R.id.tv_acc_info_currency_value);
		accCodeText = (TextView) view.findViewById(R.id.tv_crcd_fushu);
		searchTimeText = (TextView) view.findViewById(R.id.tv_acc_query_date_value);
		upLayout = view.findViewById(R.id.acc_query_result_condition);
		upLayout.setOnClickListener(downOnClickListener);
		resultListView = (ListView) view.findViewById(R.id.result_listView);
		load_more = LayoutInflater.from(this).inflate(R.layout.tran_remit_query_detail_more, null);
		Button buttonView = (Button) load_more.findViewById(R.id.btn_more);
		// 更多查询
		buttonView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getValueDate(beforeCurrentMap);
				refresh = false;
				currentIndex += pageSize;
				BaseHttpEngine.showProgressDialog();
				// 查询明细 TODO
//				requestRemitSetMealDetail(startDate, endDate, accountId, shareCardNo, pageSize, currentIndex, refresh);
				requestCommConversationId();
				LogGloble.d(TAG + " currentIndex", currentIndex + "");
			}
		});
		resultListView.addFooterView(load_more);
	}

	//滑动卡片
	/** 初始化卡片位置 */
//	private void initPosition(int selectPosition) {
//		if (selectPosition == 0) {
//			acc_frame_left.setVisibility(View.INVISIBLE);
//			if (accountNoList.size() == 1) {
//				acc_btn_goitem.setVisibility(View.INVISIBLE);
//			} else if (accountNoList.size() > 1) {
//				acc_btn_goitem.setVisibility(View.VISIBLE);
//			}
//		} else if (selectPosition == accountNoList.size() - 1) {
//			acc_frame_left.setVisibility(View.VISIBLE);
//			acc_btn_goitem.setVisibility(View.INVISIBLE);
//		} else {
//			acc_frame_left.setVisibility(View.VISIBLE);
//			acc_btn_goitem.setVisibility(View.VISIBLE);
//		}
//	}

	/** 为查询条件赋值以及查询结果顶部赋值 */
	private void getSpinnerDate() {
		//滑动卡片
		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		// R.layout.dept_spinner, LocalData.rmbNameList);
		// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// codeSpinner.setText(adapter);
		// codeSpinner.setSelection(0);

		/** 修改codeSpinner 为TextView */
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dept_spinner);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 三天前的日期
		startThreeDate = QueryDateUtils.getlastthreeDate(dateTime);
		startDate = startThreeDate;
		// 系统当前时间格式化
		currenttime = QueryDateUtils.getcurrentDate(dateTime);
		endDate = currenttime;
		startDateText.setText(startDate);
		endDateText.setText(endDate);
		// 为顶部赋值
		setTopValue(startDate, endDate, accountId);
	}

	/**
	 * 汇款笔数套餐明细查询
	 * @param startDate:开始时间
	 * @param endDate:结束日期
	 * @param accountId:账户id
	 * @param pageSize:每页显示条数
	 * @param currentIndex:当前页
	 * @param _refresh:刷新标识
	 * @param conversation:conversationID
	 */
	private void requestRemitSetMealDetail(String startDate, String endDate, String accountId, String shareCardNo,
			int pageSize, int currentIndex, boolean _refresh ,String conversation) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setMethod(Tran.TRAN_REMITSETMEALDATAIL_API);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Tran.TRAN_STARTDATE_REQ, startDate);
		params.put(Tran.TRAN_ENDDATE_REQ, endDate);
		params.put(Tran.TRAN_ACCOUNTID_REQ, accountId);
		params.put(Tran.TRAN_SHARECARDNO_REQ, shareCardNo);
		params.put(Tran.TRAN_PAGESIZE_REQ, String.valueOf(pageSize));
		params.put(Tran.TRAN_CURRENTINDEX_REQ, String.valueOf(currentIndex));
		params.put(Tran.TRAN_REFRESH_REQ, String.valueOf(_refresh));
		// params.put("shareCardNo", shareCardNo);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestRemitSetMealDetailCallBack");
	}

	/** 汇款笔数套餐明细查询------回调 */
	public void requestRemitSetMealDetailCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		Map<String, Object> returnList = (Map<String, Object>) body.getResult();
		if (StringUtil.isNullOrEmpty(returnList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		String number = (String) returnList.get(Tran.TRAN_RECORDNUMBER_RES);
		if (StringUtil.isNull(number)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		// String shareCardNo = (String)
		// returnList.get(Tran.TRAN_SHARECARDNO_REQ);
		// if (StringUtil.isNull(shareCardNo)) {
		// BaseHttpEngine.dissMissProgressDialog();
		// BaseDroidApp.getInstanse().showInfoMessageDialog(
		// getResources().getString(R.string.acc_transferquery_shareCardNo_null));
		// return;
		// }
		recordNumber = Integer.valueOf(number);
		List<Map<String, String>> resultList = (List<Map<String, String>>) returnList.get(Tran.TRAN_RESULTLIST_RES);
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		dateList.addAll(resultList);
		// 查询条件不为空
//		PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
		SearchView.getInstance().hideSearchView();
		resultAllView.setVisibility(View.VISIBLE);
		backgroundView.setBackgroundResource(R.drawable.img_bg_query_no);
		if (dateList.size() < recordNumber) {
			load_more.setVisibility(View.VISIBLE);
		} else {
			load_more.setVisibility(View.INVISIBLE);
		}

		if (refresh) {
			adapter = new RemitQueryMealDetailAdapter(RemitQueryMealDetaiActivity.this, dateList);
			resultListView.setAdapter(adapter);
		} else {
			adapter.dataChanged(dateList);
		}

	}

	/** 为查询结果顶部赋值 */
	private void setTopValue(String startDate, String endDate, String account) {
		String number = StringUtil.getForSixForString(account);
		accNoText.setText(number);
		accCodeText.setText(ConstantGloble.ACC_RMB);
		searchTimeText.setText(startDate + "-" + endDate);
	}

	/** 清空数据 */
	private void cleanDate() {
		refresh = true;
		currentIndex = 0;
		searchTimeText.setText("");
		accNoText.setText("");
		// 移除更多按钮
		if (resultListView.getFooterViewsCount() > 0) {
			load_more.setVisibility(View.INVISIBLE);
		}
		if (dateList != null && !dateList.isEmpty()) {
			dateList.clear();
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.acc_query_transfer_startdate:// 开始时间
			Calendar c = Calendar.getInstance(); // 实例化一个日期与时间操作对象
			QueryDateUtils.checkDate(startDateText, c);
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息 DatePickerDialog
			DatePickerDialog dialog = new DatePickerDialog(RemitQueryMealDetaiActivity.this, new OnDateSetListener() {
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
					String startDate = date.toString().trim();
					startDateText.setText(startDate);

				}
			}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
			dialog.show();
			break;
		case R.id.acc_query_transfer_enddate:// 结束时间
			Calendar c1 = Calendar.getInstance(); // 实例化一个日期与时间操作对象
			QueryDateUtils.checkDate(endDateText, c1);
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			DatePickerDialog dialog1 = new DatePickerDialog(RemitQueryMealDetaiActivity.this, new OnDateSetListener() {

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
					String endDate = date.toString().trim();
					// 为EditText赋值
					endDateText.setText(endDate);
				}
			}, c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DAY_OF_MONTH));
			dialog1.show();
			break;
		case R.id.btn_acc_onweek:// 一周时间
			getCurrentMap();
			startDate = QueryDateUtils.getlastWeek(dateTime).trim();
			endDate = QueryDateUtils.getcurrentDate(dateTime).trim();
			cleanDate();
			setTopValue(startDate, endDate, account);
			BaseHttpEngine.showProgressDialog();
			// 查询明细 // 注   TODO
//			requestRemitSetMealDetail(startDate, endDate, accountId, shareCardNo, pageSize, currentIndex, refresh);
			requestCommConversationId();
			break;
		case R.id.btn_acc_onmonth:// 一月时间
			getCurrentMap();
			startDate = QueryDateUtils.getlastOneMonth(dateTime).trim();
			endDate = QueryDateUtils.getcurrentDate(dateTime).trim();
			cleanDate();
			setTopValue(startDate, endDate, account);
			BaseHttpEngine.showProgressDialog();
			// 查询明细 // 注   TODO
//			requestRemitSetMealDetail(startDate, endDate, accountId, shareCardNo, pageSize, currentIndex, refresh);
			requestCommConversationId();
			break;
		case R.id.btn_acc_threemonth:// 三月时间
			getCurrentMap();
			startDate = QueryDateUtils.getlastThreeMonth(dateTime).trim();
			endDate = QueryDateUtils.getcurrentDate(dateTime).trim();
			cleanDate();
			setTopValue(startDate, endDate, account);
			BaseHttpEngine.showProgressDialog();
			// 查询明细 // 注   TODO
//			requestRemitSetMealDetail(startDate, endDate, accountId, shareCardNo, pageSize, currentIndex, refresh);
			requestCommConversationId();
			break;
		case R.id.btn_acc_query_transfer:// 查询时间
			getCurrentMap();
			startDate = startDateText.getText().toString().trim();
			endDate = endDateText.getText().toString().trim();
			if (QueryDateUtils.compareDateOneYear(startDate, dateTime)) {
				// 开始日期在系统日期前一年以内
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						RemitQueryMealDetaiActivity.this.getString(R.string.acc_check_start_enddate));
				return;
			}
			if (QueryDateUtils.compareDate(endDate, dateTime)) {
				// 结束日期在系统日期之前
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						RemitQueryMealDetaiActivity.this.getString(R.string.acc_check_enddate));
				return;
			}

			if (QueryDateUtils.compareDate(startDate, endDate)) {
				// 开始日期在结束日期之前
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						RemitQueryMealDetaiActivity.this.getString(R.string.forex_query_times1));//
				return;
			}
			if (QueryDateUtils.compareDateThree(startDate, endDate)) {
				// 起始日期与结束日期最大间隔为三个自然月
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						RemitQueryMealDetaiActivity.this.getString(R.string.acc_check_start_end_date));
				return;
			}
			cleanDate();
			setTopValue(startDate, endDate, account);
			BaseHttpEngine.showProgressDialog();
			// 查询明细
			requestCommConversationId();
			// 注   TODO
//			requestRemitSetMealDetail(startDate, endDate, accountId, shareCardNo, pageSize, currentIndex, refresh);
			break;
		default:
			break;
		}
	}
	
	public void requestCommConversationIdCallBack(Object resultObj){
		super.requestCommConversationIdCallBack(resultObj);
		conversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		requestRemitSetMealDetail(startDate, endDate, accountId, shareCardNo, pageSize, currentIndex, refresh ,conversationId);
	}

	/** 得到当前卡片Map以及accountId */
	private void getCurrentMap() {
		currentMap = accountList.get(selectPosition);
		beforeCurrentMap = accountList.get(selectPosition);
		getValueDate(currentMap);
	}
	
	/** 得到用户择卡片的值 */
	private void getValueDate(Map<String, Object> currentMap) {
		account =(String) currentMap.get("shareCardNo");
//		accountId =(String) currentMap.get("accountId");
		shareCardNo = (String) currentMap.get("shareCardNo");
	}
	
	

	/** 收起事件 */
	private OnClickListener upOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (dateList == null || dateList.size() <= 0) {

			} else {
//				PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
				SearchView.getInstance().hideSearchView();
				resultAllView.setVisibility(View.VISIBLE);
			}
		}
	};
	/** 下拉事件 */
	private OnClickListener downOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
//			PopupWindowUtils.getInstance().showQueryPopupWindow();
			SearchView.getInstance().showSearchView();
		}
	};

	// ////////////////////////////用户选择账户////////////////////////////////
	// 选择账户diane
	private void initPageDate() {
		Map<String, Object> mealQueryMaps = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TRAN_REMIT_QUERY_RESULT);
		List<Map<String, Object>> validAccountLists = (List<Map<String, Object>>) mealQueryMaps.get("validAccountList");
		accListView = new ArrayList<String>();
		for (int i = 0; i < validAccountLists.size(); i++) {
			Map<String, Object> map = validAccountLists.get(i);
			shareCard = (String) map.get("shareCardNo");
			accListView.add(StringUtil.getForSixForString(shareCard));
			
			// 卡号后面添加 是(签约账户)还是(共享账号)共享账号根据返回的结果后面追加 "有效"/"无效"
//			remitSetMealAccountType = (String) map.get("remitSetMealAccountType");
//			shareStatus = (String) map.get("shareStatus");
//			String shareAccCard = StringUtil.getForEightForString(shareCard);
//			if ("M".equalsIgnoreCase(remitSetMealAccountType)) {
//				accListView.add(shareAccCard + " " + "签约账户");
//			} else {
//				accListView.add(shareAccCard + "  "
//						+ ("Y".equalsIgnoreCase(shareStatus) ? "共享账号" + "/" + "有效" : "共享账号" + "/" + "无效"));
//			}
		}

		ArrayAdapter<String> adapters = new ArrayAdapter<String>(this, R.layout.dept_spinner, accListView);
		adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		accSpinner.setAdapter(adapters);
		accSpinner.setSelection(selectPosition);
		accountNumber = accSpinner.getSelectedItem().toString();
		
		//用户选择账户时更新套餐使用明细中账户
		accSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int positions, long id) {
				selectPosition = positions;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub	
			}
		});
	}
}
