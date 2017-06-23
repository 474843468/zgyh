package com.chinamworld.bocmbci.biz.tran.atmremit;

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
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.atmremit.adapter.AtmQueryTransferAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.SearchView;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * atm交易查询
 * 
 * @author wangmengmeng
 * 
 */
public class AtmRemitQueryActivity extends TranBaseActivity {

	private View view;
	/** 查询开始日期 */
	private TextView tv_startdate;
	/** 查询结束日期 */
	private TextView tv_enddate;
	/** 查询状态 */
	private Spinner query_status;
	/** 查询一周 */
	private Button btn_query_onweek;
	/** 查询一个月 */
	private Button btn_query_onmonth;
	/** 查询三个月 */
	private Button btn_query_threemonth;
	/** 查询按钮 */
	private Button btn_query;
	/** 系统时间 */
	private String currenttime;
	/** 交易时间 */
	private TextView tv_acc_query_result_date;
	/** 交易结果列表信息 */
	private ListView lv_acc_query_result;
	/** 查询条件视图 */
	private LinearLayout query_condition;
	/** 查询条件收起三角 */
	private ImageView img_up;
	/** 查询条件 */
	private RelativeLayout acc_query_transfer_layout;
	/** 加载更多 */
	private RelativeLayout load_more;
	/** 更多按钮 */
	private Button btn_load_more;
	/** 总条数 */
	private int recordNumber = 0;
	/** 加载页数 */
	private int loadNumber = 1;
	/** 查询结果Layout */
	private RelativeLayout rl_query_transfer_result;
	/** 查询条件Map */
	private Map<String, String> queryMap = new HashMap<String, String>();
	/** 选择的开始日期 */
	private String startdate = null;
	/** 选择的结束日期 */
	private String enddate = null;
	/** 手机号转账明细信息 */
	private List<Map<String, Object>> transferList;
	/** 查询状态 */
	private String accountId;
	/** 查询状态 */
	private TextView tv_query_status;
	/** 查询适配器 */
	private AtmQueryTransferAdapter detailAdapter;
	private List<Map<String, Object>> bankList;
	// 查询账户
	private String query_number;
	/** 显示账户 */
	private List<Map<String, Object>> accountqueryList;
	/** 反显账户 */
	private List<String> accountqueryStringList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.trans_atm_two_menu));
		view = addView(R.layout.tran_atm_querytransfer);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 0);
		}
		setLeftSelectedPosition("tranManager_5");
		toprightBtn();
		// 请求系统时间
		requestSystemDateTime();
		BiiHttpEngine.showProgressDialogCanGoBack();
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BiiHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNull(dateTime)) {
			BiiHttpEngine.dissMissProgressDialog();
			return;
		}
		// 初始化界面
		init();
	}

	public void init() {
		bankList = TranDataCenter.getInstance().getAccountList();
		accountqueryList = new ArrayList<Map<String, Object>>();
		accountqueryStringList = new ArrayList<String>();
		for (Map<String, Object> map : bankList) {
			Map<String, Object> newmap = new HashMap<String, Object>();
			newmap.put(Acc.ACC_ACCOUNTID_RES, map.get(Acc.ACC_ACCOUNTID_RES));
			newmap.put(Acc.ACC_ACCOUNTNUMBER_RES, map.get(Acc.ACC_ACCOUNTNUMBER_RES));
			newmap.put(Acc.ACC_ACCOUNTTYPE_RES, LocalData.AccountType.get(map.get(Acc.ACC_ACCOUNTTYPE_RES)));
			accountqueryList.add(newmap);
			accountqueryStringList.add(StringUtil.getForSixForString((String) map.get(Acc.ACC_ACCOUNTNUMBER_RES)) + " "
					+ LocalData.AccountType.get(map.get(Acc.ACC_ACCOUNTTYPE_RES)));
		}
		accountId = (String) accountqueryList.get(0).get(Acc.ACC_ACCOUNTID_RES);
		query_number = accountqueryStringList.get(0);
		acc_query_transfer_layout = (RelativeLayout) LayoutInflater.from(BaseDroidApp.getInstanse().getCurrentAct())
				.inflate(R.layout.trans_mobile_record_condition, null);
		LinearLayout acc_query_result_condition = (LinearLayout) view.findViewById(R.id.acc_query_result_condition);
		acc_query_result_condition.setOnClickListener(backQueryClick);
		SearchView.getInstance().addSearchView(acc_query_result_condition, acc_query_transfer_layout);
//		PopupWindowUtils.getInstance().getQueryPopupWindow(acc_query_transfer_layout,
//				BaseDroidApp.getInstanse().getCurrentAct());
//		PopupWindowUtils.getInstance().showQueryPopupWindowFirst();
		// 条件视图
		query_condition = (LinearLayout) acc_query_transfer_layout.findViewById(R.id.ll_query_condition);
		query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
		// 查询条件页面初始化
		tv_startdate = (TextView) acc_query_transfer_layout.findViewById(R.id.acc_query_transfer_startdate);
		tv_enddate = (TextView) acc_query_transfer_layout.findViewById(R.id.acc_query_transfer_enddate);
		btn_query_onweek = (Button) acc_query_transfer_layout.findViewById(R.id.btn_acc_onweek);
		btn_query_onmonth = (Button) acc_query_transfer_layout.findViewById(R.id.btn_acc_onmonth);
		btn_query_threemonth = (Button) acc_query_transfer_layout.findViewById(R.id.btn_acc_threemonth);
		btn_query = (Button) acc_query_transfer_layout.findViewById(R.id.btn_acc_query_transfer);
		img_up = (ImageView) acc_query_transfer_layout.findViewById(R.id.acc_query_up);
		query_status = (Spinner) acc_query_transfer_layout.findViewById(R.id.sp_trans_cremit_manage_trans_records);
		// 三天前的日期
		String startThreeDate = QueryDateUtils.getlastthreeDate(dateTime);
		tv_startdate.setText(startThreeDate);
		// 系统当前时间格式化
		currenttime = QueryDateUtils.getcurrentDate(dateTime);
		// 初始结束时间赋值
		tv_enddate.setText(currenttime);
		tv_startdate.setOnClickListener(chooseDateClick);
		tv_enddate.setOnClickListener(chooseDateClick);
		btn_query_onweek.setOnClickListener(this);
		btn_query_onmonth.setOnClickListener(this);
		btn_query_threemonth.setOnClickListener(this);
		btn_query.setOnClickListener(this);
		// 收起事件监听
		LinearLayout ll_up = (LinearLayout) acc_query_transfer_layout.findViewById(R.id.ll_up);
		ll_up.setOnClickListener(upClick);
		img_up.setOnClickListener(upClick);
		
		spinnerInit();
		initfirst();
		// queryDetail();
	}

	public void spinnerInit() {

		// 查询账户
		ArrayAdapter<String> currencyAdapter = new ArrayAdapter<String>(this, R.layout.my_spinner_item,
				accountqueryStringList);
		currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		query_status.setAdapter(currencyAdapter);
		query_status.setBackgroundResource(R.drawable.bg_spinner);
		query_status.setEnabled(true);
		query_status.setSelection(0);
		query_status.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				accountId = (String) accountqueryList.get(position).get(Acc.ACC_ACCOUNTID_RES);
				query_number = accountqueryStringList.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				accountId = (String) accountqueryList.get(0).get(Acc.ACC_ACCOUNTID_RES);
				query_number = accountqueryStringList.get(0);
			}
		});

	}

	public void initfirst() {
		rl_query_transfer_result = (RelativeLayout) view.findViewById(R.id.acc_query_result_layout);
		tv_query_status = (TextView) view.findViewById(R.id.tv_acc_info_currency_value);
		tv_acc_query_result_date = (TextView) view.findViewById(R.id.tv_acc_query_date_value);
		lv_acc_query_result = (ListView) view.findViewById(R.id.lv_acc_query_result);
		load_more = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.acc_load_more, null);
		btn_load_more = (Button) load_more.findViewById(R.id.btn_load_more);
		btn_load_more.setBackgroundResource(R.color.transparent_00);
		btn_load_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestPsnPasswordRemitFreeTranQueryForMore();
			}
		});
		startdate = QueryDateUtils.getlastthreeDate(dateTime);
		enddate = QueryDateUtils.getcurrentDate(dateTime);
		tv_acc_query_result_date.setText(startdate + "-" + enddate);
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPsnPasswordRemitFreeTranQuery();
	}

	/**
	 * atm汇款转账交易查询回调
	 * */
	public void requestPsnPasswordRemitFreeTranQuery() {

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setMethod(Tran.TRAN_PSNPASSWORDREMITFREETRANQUERY_API);

		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.TRAN_ATM_QUERY_ACCOUNTID_REQ, accountId);
		// 交易类型
		map.put(Tran.TRAN_ATM_QUERY_FREEREMITTRSTYPE_REQ, OPENCHANGEBOOKING);
		// 起始日期
		map.put(Tran.TRAN_ATM_QUERY_STARTDATE_REQ, startdate);
		// 结束日期
		map.put(Tran.TRAN_ATM_QUERY_ENDDATE_REQ, enddate);
		// 每页显示条数
		map.put(Tran.TRAN_ATM_QUERY_PAGESIZE_REQ, ConstantGloble.LOAN_PAGESIZE_VALUE);
		// 当前记录索引
		map.put(Tran.TRAN_ATM_QUERY_CURRENTINDEX_REQ, ConstantGloble.LOAN_CURRENTINDEX_VALUE);
		map.put(Tran.TRAN_ATM_QUERY_REFRESH_REQ, ConstantGloble.REFRESH_FOR_MORE);
		biiRequestBody.setParams(map);
		queryMap = map;
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestPsnPasswordRemitFreeTranQueryCallBack");
	}

	/**
	 * 手机号转账交易查询回调
	 * 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnPasswordRemitFreeTranQueryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> transferback = (Map<String, Object>) biiResponseBody.getResult();
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(transferback)) {
			return;
		}
		recordNumber = Integer.valueOf((String) transferback.get(Acc.RECORDNUMBER_RES));
		transferList = (List<Map<String, Object>>) (transferback.get(Tran.MOBILE_LIST_RES));

		if (transferList == null || transferList.size() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.acc_transferquery_null));

			return;
		} else {
			if (recordNumber > 10) {
				if (lv_acc_query_result.getFooterViewsCount() > 0) {

				} else {
					lv_acc_query_result.addFooterView(load_more);
				}

			}
			loadNumber = transferList.size();
			// 按全部
			// 隐藏条件界面
//			PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
			SearchView.getInstance().hideSearchView();
			rl_query_transfer_result.setVisibility(View.VISIBLE);
			 view.findViewById(R.id.rl_top).setVisibility(View.VISIBLE);
			tv_query_status.setText(query_number);
			tv_acc_query_result_date.setText(startdate + "-" + enddate);
			detailAdapter = new AtmQueryTransferAdapter(this, transferList);
			lv_acc_query_result.setAdapter(detailAdapter);
			lv_acc_query_result.setOnItemClickListener(onListClickListener);
		}
	}

	/**
	 * 单条记录点击事件
	 */
	OnItemClickListener onListClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			TranDataCenter.getInstance().setMobileTransDetailMap(transferList.get(position));
			// TODO 查询详情
			requestPsnPasswordRemitFreeDetailQuery();
		}
	};

	/**
	 * atm汇款转账详情查询
	 * */
	public void requestPsnPasswordRemitFreeDetailQuery() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setMethod(Tran.TRAN_PSNPASSWORDREMITFREEDETAILQUERY_API);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.TRAN_ATM_DETAIL_REMITNO_REQ,
				(String) TranDataCenter.getInstance().getMobileTransDetailMap().get(Tran.TRAN_ATM_QUERY_REMITNUMBER_RES));
		map.put(Tran.TRAN_ATM_DETAIL_FREEREMITTRSTYPE_REQ, FREEREMITTRSTYPEQU);
		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestPsnPasswordRemitFreeDetailQueryCallBack");
	}

	/**
	 * atm汇款转账详情查询回调
	 * 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnPasswordRemitFreeDetailQueryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> transferback = (Map<String, Object>) biiResponseBody.getResult();
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(transferback)) {
			return;
		}
		TranDataCenter.getInstance().setMobileTransDetailMap(transferback);
		Intent intent = new Intent(this, AtmRecordDetailActivity.class);
		startActivityForResult(intent, 2);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2 && resultCode == RESULT_OK) {
			loadNumber = 1;
			if (!StringUtil.isNullOrEmpty(transferList)) {
				transferList.clear();
			}
			detailAdapter.notifyDataSetChanged();
			rl_query_transfer_result.setVisibility(View.GONE);
			if (lv_acc_query_result.getFooterViewsCount() > 0) {
				lv_acc_query_result.removeFooterView(load_more);
			}
			query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
//			PopupWindowUtils.getInstance().showQueryPopupWindow();
			SearchView.getInstance().showSearchView();
			
			requestCommConversationId();
			BiiHttpEngine.showProgressDialog();
		}
	}

	/**
	 * atm汇款转账交易查询回调
	 * */
	public void requestPsnPasswordRemitFreeTranQueryForMore() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.TRAN_PSNPASSWORDREMITFREETRANQUERY_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.TRAN_ATM_QUERY_ACCOUNTID_REQ, queryMap.get(Tran.TRAN_ATM_QUERY_ACCOUNTID_REQ));
		// 交易类型
		map.put(Tran.TRAN_ATM_QUERY_FREEREMITTRSTYPE_REQ, OPENCHANGEBOOKING);
		// 起始日期
		map.put(Tran.TRAN_ATM_QUERY_STARTDATE_REQ, startdate);
		// 结束日期
		map.put(Tran.TRAN_ATM_QUERY_ENDDATE_REQ, enddate);
		map.put(Tran.PAYEE_CURRENTINDEX_REQ, String.valueOf(loadNumber));
		map.put(Tran.PAYEE_PAGESIZE_REQ, ConstantGloble.LOAN_PAGESIZE_VALUE);
		map.put(Tran.TRAN_ATM_QUERY_REFRESH_REQ, ConstantGloble.REFRESH_FOR_MORE);
		biiRequestBody.setParams(map);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestPsnPasswordRemitFreeTranQueryForMoreCallBack");
	}

	/**
	 * atm汇款回调
	 * 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnPasswordRemitFreeTranQueryForMoreCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> transferback = (Map<String, Object>) biiResponseBody.getResult();
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(transferback)) {
			return;
		}
		recordNumber = Integer.valueOf((String) transferback.get(Acc.RECORDNUMBER_RES));
		List<Map<String, Object>> transferListMore = (List<Map<String, Object>>) (transferback.get(Tran.MOBILE_LIST_RES));

		if (transferListMore == null || transferListMore.size() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.acc_transferquery_null));

			return;
		} else {
			for (Map<String, Object> map : transferListMore) {
				loadNumber++;
				transferList.add(map);
			}
			if (loadNumber < recordNumber) {

			} else {
				lv_acc_query_result.removeFooterView(load_more);
			}
			// 按全部
			detailAdapter.notifyDataSetChanged();

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
			DatePickerDialog dialog = new DatePickerDialog(AtmRemitQueryActivity.this, new OnDateSetListener() {

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
	/** 查询结果头倒三角点击监听事件 */
	OnClickListener backQueryClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			query_condition.setBackgroundResource(R.drawable.img_bg_query_no);
//			PopupWindowUtils.getInstance().getQueryPopupWindow(acc_query_transfer_layout,
//					BaseDroidApp.getInstanse().getCurrentAct());
//			// 显示查询条件
//			PopupWindowUtils.getInstance().showQueryPopupWindow();
			SearchView.getInstance().showSearchView();
		}
	};

	/** 事件快捷方式点击事件 */
	@Override
	public void onClick(View v) {
		// 查询事件
		switch (v.getId()) {
		case R.id.btn_acc_query_transfer:
			loadNumber = 1;
			if (!StringUtil.isNullOrEmpty(transferList)) {
				transferList.clear();
			}
			query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
			rl_query_transfer_result.setVisibility(View.GONE);
			queryDetail();
			break;
		case R.id.btn_acc_onweek:
			// 选择一周
			loadNumber = 1;
			if (!StringUtil.isNullOrEmpty(transferList)) {
				transferList.clear();
			}
			query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
			rl_query_transfer_result.setVisibility(View.GONE);
			enddate = QueryDateUtils.getcurrentDate(dateTime).trim();
			startdate = QueryDateUtils.getlastWeek(dateTime).trim();
			if (lv_acc_query_result.getFooterViewsCount() > 0) {
				lv_acc_query_result.removeFooterView(load_more);
			}
			// 请求账户明细信息
			requestCommConversationId();
			BiiHttpEngine.showProgressDialog();
			break;
		case R.id.btn_acc_onmonth:
			// 选择一个月
			loadNumber = 1;
			if (!StringUtil.isNullOrEmpty(transferList)) {
				transferList.clear();
			}
			query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
			rl_query_transfer_result.setVisibility(View.GONE);
			enddate = QueryDateUtils.getcurrentDate(dateTime).trim();
			startdate = QueryDateUtils.getlastOneMonth(dateTime).trim();
			if (lv_acc_query_result.getFooterViewsCount() > 0) {
				lv_acc_query_result.removeFooterView(load_more);
			}
			// 请求账户明细信息
			requestCommConversationId();
			BiiHttpEngine.showProgressDialog();
			break;
		case R.id.btn_acc_threemonth:
			// 选择三个月
			loadNumber = 1;
			if (!StringUtil.isNullOrEmpty(transferList)) {
				transferList.clear();
			}
			query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
			rl_query_transfer_result.setVisibility(View.GONE);
			enddate = QueryDateUtils.getcurrentDate(dateTime).trim();
			startdate = QueryDateUtils.getlastThreeMonth(dateTime).trim();
			if (lv_acc_query_result.getFooterViewsCount() > 0) {
				lv_acc_query_result.removeFooterView(load_more);
			}
			// 请求账户明细信息
			requestCommConversationId();
			BiiHttpEngine.showProgressDialog();
			break;
		default:
			break;
		}

	}

	/** 直接查询 */
	public void queryDetail() {
		// 直接进行查询
		startdate = tv_startdate.getText().toString().trim();
		enddate = tv_enddate.getText().toString().trim();

		if (QueryDateUtils.compareDateOneYear(startdate, dateTime)) {
			// 起始日期不能早于系统当前日期一年前
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					AtmRemitQueryActivity.this.getString(R.string.acc_check_start_enddate));
			return;
		}
		if (QueryDateUtils.compareDate(enddate, dateTime)) {
			// 结束日期在系统日期之前
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					AtmRemitQueryActivity.this.getString(R.string.acc_check_enddate));
			return;
		}
		if (QueryDateUtils.compareDate(startdate, enddate)) {
			// 起始日期在系统当前日期前

		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					AtmRemitQueryActivity.this.getString(R.string.acc_query_errordate));
			return;
		}
		if (QueryDateUtils.compareDateThree(startdate, enddate)) {
			// 起始日期与结束日期最大间隔为三个自然月
			if (lv_acc_query_result.getFooterViewsCount() > 0) {
				lv_acc_query_result.removeFooterView(load_more);
			}
			// 请求账户明细信息
			requestCommConversationId();
			BiiHttpEngine.showProgressDialog();
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					AtmRemitQueryActivity.this.getString(R.string.acc_check_start_end_date));
			return;
		}

	}

	/** 收起监听事件 */
	OnClickListener upClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 判断是否已经进行了查询—如果已经进行:收起所有;否则收起查询条件区域
			if (transferList == null || transferList.size() == 0) {

			} else {
//				PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
				SearchView.getInstance().hideSearchView();
				rl_query_transfer_result.setVisibility(View.VISIBLE);
			}
		}
	};

}
