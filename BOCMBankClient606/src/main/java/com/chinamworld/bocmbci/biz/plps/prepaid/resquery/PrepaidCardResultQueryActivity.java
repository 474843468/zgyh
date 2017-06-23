package com.chinamworld.bocmbci.biz.plps.prepaid.resquery;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.PlpsUtils;
import com.chinamworld.bocmbci.biz.plps.adapter.AnnuitySpinnerAdapter;
import com.chinamworld.bocmbci.biz.plps.adapter.PrePaidCardResultQueryAdapter;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预付卡查询
 *
 * @author Zhi
 */
public class PrepaidCardResultQueryActivity extends PlpsBaseActivity {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** 选择账户 */
	private TextView tvAccount;
	/** 选择预付卡类型 */
	private TextView tvPrepaidCardType;
	/** 选择查询起始日期 */
	private TextView tvStartDate;
	/** 选择查询终止日期 */
	private TextView tvEndDate;
//	/** 查询视图 */
//	private View queryView;
	/** 收起 */
	private LinearLayout upLayout;
	/** 标识查询条件View是否第一次显示 */
	private boolean isShowfirst = true;
	/** 选择的账户下标 */
	private int accIndex = -1;
	/** 选择的预付卡类型下标 */
	private int prepaidCardTypeIndex = -1;
	/** 银行账户弹出框里ListView的id */
	private final int ACCID = 101;
	/** 预付卡类型弹出框里ListView的id */
	private final int PREPAIDCARDID = 102;

	/** 查询结果页面的银行账户 */
	private TextView tvAccountResult;
	/** 查询结果页面的预付卡类型 */
	private TextView tvPrepaidCardTypeResult;
	/** 查询结果页面的查询时间 */
	private TextView tvQueryDate;
	/** 查询结果列表 */
	private List<Map<String, Object>> listResult;
	/** 查询结果显示控件 */
	private ListView lv;
	/** 列表底部“更多”选项 */
	private View mFooterView;
	/** 请求列表时的下标 */
	private int currentIndex = 0;
	/** 列表数据适配器 */
	private PrePaidCardResultQueryAdapter mAdapter;
	private String startDate,endDate;
	/**是否是第一次*/
	private Boolean isFirst = true;
	/**银行账户*/
	private List<Map<String, Object>> mList = new ArrayList<Map<String,Object>>();
	/**没有信用卡的账户*/
	private List<Map<String, Object>> nocardList = new ArrayList<Map<String,Object>>();
//	/**账户是否需要刷新*/
//	private Boolean isRefresh = false;
	/**预付卡类型*/
	private String prepaidType = null;
	/**账户类型*/
	private String accountType = null;
	/**银行账户数据*/
	private List<String> maccountList = new ArrayList<String>();
//	/**是否点击银行账户*/
//	private Boolean isCreateDialog = true;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("充值结果查询");
//		inflateLayout(R.layout.plps_prepaid_card_query);
		((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 0);
		mList = PlpsDataCenter.getInstance().getAcctList();
		for(int i=0; i<mList.size(); i++){
			if(!mList.get(i).get(Comm.ACCOUNT_TYPE).equals("103")&&!mList.get(i).get(Comm.ACCOUNT_TYPE).equals("104")){
				nocardList.add(mList.get(i));
			}
		}
		initQueryConditionView();
		initQueryResultView();
		setUpGetMoreView();
	}

	/** 初始化查询条件视图 */
	private void initQueryConditionView() {
//		queryView = LayoutInflater.from(this).inflate(R.layout.plps_prepaid_card_query, null);
		inflateLayout(R.layout.plps_prepaid_card_query_condition);
		upLayout = (LinearLayout) findViewById(R.id.ll_up);
		tvAccount = (TextView) findViewById(R.id.tv_account);
		tvPrepaidCardType = (TextView) findViewById(R.id.tv_prepaidCardType);
		tvStartDate = (TextView) findViewById(R.id.startdate);
		tvEndDate = (TextView) findViewById(R.id.enddate);
		findViewById(R.id.btn_query).setOnClickListener(this);
		Button btnWeek = (Button)findViewById(R.id.btn_onweek);
		Button btnMonth = (Button)findViewById(R.id.btn_onmonth);
		Button btnThreeMonth = (Button)findViewById(R.id.btn_threemonth);
		btnWeek.setOnClickListener(this);
		btnMonth.setOnClickListener(this);
		btnThreeMonth.setOnClickListener(this);
		setTextBackground(false, tvAccount);
//		tvAccount.setOnClickListener(this);
		tvPrepaidCardType.setOnClickListener(this);
		upLayout.setOnClickListener(this);
		upLayout.setClickable(false);
		tvStartDate.setText(QueryDateUtils.getlastWeek(PlpsDataCenter.getInstance().getSysDate()));
		tvEndDate.setText(QueryDateUtils.getcurrentDate(PlpsDataCenter.getInstance().getSysDate()));
		tvStartDate.setOnClickListener(plpsChooseDateClick);
		tvEndDate.setOnClickListener(plpsChooseDateClick);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	/** 初始化查询结果视图 */
	private void initQueryResultView() {
		tvAccountResult = (TextView) findViewById(R.id.tv_accountt);
		tvPrepaidCardTypeResult = (TextView) findViewById(R.id.tv_prepaidCardTypet);
		tvQueryDate = (TextView) findViewById(R.id.query_date);
		lv = (ListView) findViewById(R.id.listview);
		findViewById(R.id.down_layout).setOnClickListener(this);
	}

	/**
	 * 初始化分页布局
	 */
	private void setUpGetMoreView(){
		mFooterView = View.inflate(this, R.layout.plps_prepaid_card_query_footer, null);
//		((RelativeLayout) mFooterView.findViewById(R.id.rl_get_more)).setBackgroundColor(getResources().getColor(R.color.transparent_00));
	}

	/**
	 * 创建spinner弹窗框
	 *
	 * @param list
	 * @param title
	 * @param vId
	 * @param position
	 */
	private void createDialog(List<String> list,String title,int position, int id){
		ListView mListView = new ListView(this);
		mListView.setId(id);
		mListView.setFadingEdgeLength(0);
		mListView.setOnItemClickListener(this);
		mListView.setScrollingCacheEnabled(false);
		AnnuitySpinnerAdapter mAdapter = new AnnuitySpinnerAdapter(this, list,position);
		mListView.setAdapter(mAdapter);
		BaseDroidApp.getInstanse().showSpinnerDialog(mListView, title);
	}

	/**
	 * 添加分页布局
	 *
	 * @param totalCount
	 */
	private void addFooterView(String totalCount) {
		int productSize = 0;
		productSize = listResult.size();
		if (Integer.valueOf(totalCount) > productSize) {
			lv.setFooterDividersEnabled(false);
			if (lv.getFooterViewsCount() <= 0) {
				lv.addFooterView(mFooterView);
			}
		} else {
			lv.setFooterDividersEnabled(true);
			if (lv.getFooterViewsCount() > 0) {
				lv.removeFooterView(mFooterView);
			}
		}
		mFooterView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BaseHttpEngine.showProgressDialog();
				requestPsnPrepaidCardQueryReplenishmentList(false);
			}
		});
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------控件事件-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.ll_up:
				// 收起
				if(isFirst){
					upLayout.setClickable(false);
				}else {
					upLayout.setClickable(true);
					findViewById(R.id.result_condition).setVisibility(View.VISIBLE);
					findViewById(R.id.ll_query_condition).setVisibility(View.GONE);
				}
				break;

			case R.id.down_layout:
				// 下拉
				findViewById(R.id.ll_query_condition).setVisibility(View.VISIBLE);
				findViewById(R.id.result_condition).setVisibility(View.GONE);
				break;
			// 一周
			case R.id.btn_onweek:
				if (accIndex == -1) {
					BaseDroidApp.getInstanse().showInfoMessageDialog("请选择银行账户");
					return;
				}
				if (prepaidCardTypeIndex == -1) {
					BaseDroidApp.getInstanse().showInfoMessageDialog("请选择预付卡类型");
					return;
				}
				upLayout.setClickable(false);
				startDate = QueryDateUtils.getlastWeek(PlpsDataCenter.getInstance()
						.getSysDate());
				endDate = QueryDateUtils.getcurrentDate(PlpsDataCenter
						.getInstance().getSysDate());
				tvStartDate.setText(startDate);
				tvEndDate.setText(endDate);
				tvQueryDate.setText(startDate + "-" + endDate);
				tvAccountResult.setText(tvAccount.getText().toString());
				tvPrepaidCardTypeResult.setText(tvPrepaidCardType.getText().toString());
				if (!StringUtil.isNullOrEmpty(listResult)) {
					listResult.clear();
				}
				requestPsnPrepaidCardQueryReplenishmentList(true);
				break;
			// 一个月
			case R.id.btn_onmonth:
				if (accIndex == -1) {
					BaseDroidApp.getInstanse().showInfoMessageDialog("请选择银行账户");
					return;
				}
				if (prepaidCardTypeIndex == -1) {
					BaseDroidApp.getInstanse().showInfoMessageDialog("请选择预付卡类型");
					return;
				}
				upLayout.setClickable(false);
				startDate = QueryDateUtils.getlastOneMonth(PlpsDataCenter
						.getInstance().getSysDate());
				endDate = QueryDateUtils.getcurrentDate(PlpsDataCenter
						.getInstance().getSysDate());
				tvStartDate.setText(startDate);
				tvEndDate.setText(endDate);
				tvQueryDate.setText(startDate + "-" + endDate);
				tvAccountResult.setText(tvAccount.getText().toString());
				tvPrepaidCardTypeResult.setText(tvPrepaidCardType.getText().toString());
				if (!StringUtil.isNullOrEmpty(listResult)) {
					listResult.clear();
				}
				requestPsnPrepaidCardQueryReplenishmentList(true);
				break;
			// 三个月
			case R.id.btn_threemonth:
				if (accIndex == -1) {
					BaseDroidApp.getInstanse().showInfoMessageDialog("请选择银行账户");
					return;
				}
				if (prepaidCardTypeIndex == -1) {
					BaseDroidApp.getInstanse().showInfoMessageDialog("请选择预付卡类型");
					return;
				}
				upLayout.setClickable(false);
				startDate = QueryDateUtils.getlastThreeMonth(PlpsDataCenter
						.getInstance().getSysDate());
				endDate = QueryDateUtils.getcurrentDate(PlpsDataCenter
						.getInstance().getSysDate());
				tvStartDate.setText(startDate);
				tvEndDate.setText(endDate);
				tvQueryDate.setText(startDate + "-" + endDate);
				tvAccountResult.setText(tvAccount.getText().toString());
				tvPrepaidCardTypeResult.setText(tvPrepaidCardType.getText().toString());
				if (!StringUtil.isNullOrEmpty(listResult)) {
					listResult.clear();
				}
				requestPsnPrepaidCardQueryReplenishmentList(true);
				break;

			case R.id.btn_query:
				// 查询
				if (accIndex == -1) {
					BaseDroidApp.getInstanse().showInfoMessageDialog("请选择银行账户");
					return;
				}
				if (prepaidCardTypeIndex == -1) {
					BaseDroidApp.getInstanse().showInfoMessageDialog("请选择预付卡类型");
					return;
				}
				tvQueryDate.setText(tvStartDate.getText().toString() + "-" + tvEndDate.getText().toString());
				tvAccountResult.setText(tvAccount.getText().toString());
				tvPrepaidCardTypeResult.setText(tvPrepaidCardType.getText().toString());
				if (!StringUtil.isNullOrEmpty(listResult)) {
					listResult.clear();
				}
				currentIndex = 1;
				if(queryCheckTime(tvStartDate.getText().toString(),tvEndDate.getText().toString())){
					requestPsnPrepaidCardQueryReplenishmentList(true);
				}
				break;

			case R.id.tv_account:
//			if(prepaidCardTypeIndex == -1){
//				setTextBackground(false, tvAccount);
//			}else {
//				setTextBackground(true, tvAccount);
//				tvAccount.setOnClickListener(this);
//			}
				// 银行账户
				if(prepaidCardTypeIndex == -1){
					List<String> mList = PlpsUtils.initSpinnerCardData(PlpsDataCenter.getInstance().getAcctList(), Comm.ACCOUNTNUMBER,Comm.NICKNAME, null);
					createDialog(mList, "请选择", accIndex, ACCID);
				}else {
					createDialog(maccountList, "请选择", -1, ACCID);
				}

				break;
			// 预付卡类型
			case R.id.tv_prepaidCardType:
				List<String> list = PlpsDataCenter.plpsPrepaidCardType;
				createDialog(list, "请选择", prepaidCardTypeIndex, PREPAIDCARDID);
				break;
		}
	}
	/**
	 * 设置Text背景
	 * @param isdefault
	 * @param v
	 */
	private void setTextBackground(boolean isdefault,View... v){
		if(v.length > 0){
			for(View sp : v){
				if(sp != null){
					if (isdefault){
						sp.setClickable(true);
						sp.setBackgroundResource(R.drawable.bg_spinner);
					}else{
						sp.setClickable(false);
						sp.setBackgroundResource(R.drawable.bg_spinner_default);
					}
				}
			}
		}
	}



	/**
	 * 查询日期校验
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public boolean queryCheckTime(String startDate,String endDate){
		String dateTime = PlpsDataCenter.getInstance().getSysDate();
		// 起始日期不能早于系统当前日期一年前
		if (!QueryDateUtils.compareDateOneYear(startDate, dateTime)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.plps_check_start_date));
			return false;
		}
		// 结束日期在服务器日期之前
		if (!QueryDateUtils.compareDate(endDate, dateTime)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.plps_check_end_date));
			return false;
		}
		// 开始日期在结束日期之前
		if (!QueryDateUtils.compareDate(startDate, endDate)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.plps_check_start_dates));
			return false;
		}
		// 起始日期与结束日期最大间隔为三个自然月
		if (!QueryDateUtils.compareDateThree(startDate, endDate)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.plps_check_start_end_date));
			return false;
		}
		return true;
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		super.onItemClick(parent, view, position, id);
		BaseDroidApp.getInstanse().dismissMessageDialog();
		switch (parent.getId()) {
			case ACCID:
				accIndex = position;

				if(prepaidCardTypeIndex == -1||prepaidType.equals("中铁银通")){
					tvAccount.setText(PlpsUtils.initSpinnerCardData(PlpsDataCenter.getInstance().getAcctList(), Comm.ACCOUNTNUMBER,Comm.NICKNAME, null).get(position));
					accountType = (String)PlpsDataCenter.getInstance().getAcctList().get(accIndex).get(Comm.ACCOUNT_TYPE);
//				if(accountType.equals("103")||accountType.equals("104")){
//					tvPrepaidCardType.setText("中铁银通");
//					List<String> prepadidList = (List<String>) PlpsDataCenter.plpsPrepaidCardType;
//					for(int i=0; i< prepadidList.size(); i++){
//						if(prepadidList.get(i).equals("中铁银通")){
//							prepaidCardTypeIndex = i;
//							break;
//						}
//					}
//					prepaidType = PlpsDataCenter.plpsPrepaidCardType.get(prepaidCardTypeIndex);
//				}
				}else {
					tvAccount.setText(PlpsUtils.initSpinnerCardData(nocardList, Comm.ACCOUNTNUMBER,Comm.NICKNAME, null).get(position));
				}
				break;
			case PREPAIDCARDID:
				prepaidCardTypeIndex = position;
				tvPrepaidCardType.setText(PlpsDataCenter.plpsPrepaidCardType.get(position));
				prepaidType = PlpsDataCenter.plpsPrepaidCardType.get(position);
				if(prepaidCardTypeIndex == -1|| prepaidCardTypeIndex == 0){
					setTextBackground(false, tvAccount);
				}else {
					setTextBackground(true, tvAccount);
					tvAccount.setOnClickListener(this);
				}
				if(prepaidType.equals("中铁银通")){
//				if(isRefresh){
					maccountList = PlpsUtils.initSpinnerCardData(PlpsDataCenter.getInstance().getAcctList(), Comm.ACCOUNTNUMBER,Comm.NICKNAME, null);
//					isCreateDialog = false;
					tvAccount.setText("请选择");
//				}else {
//					isRefresh = true;
//				}
				}else {
					maccountList = PlpsUtils.initSpinnerCardData(nocardList, Comm.ACCOUNTNUMBER,Comm.NICKNAME, null);
//				isCreateDialog = false;
					tvAccount.setText("请选择");
				}
				break;
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO ---------------------------------------网络请求与回调方法--------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 查询充值交易明细
	 *
	 * @param refresh
	 *            刷新标识
	 */
	private void requestPsnPrepaidCardQueryReplenishmentList(boolean refresh) {
		BaseHttpEngine.showProgressDialog();
		isFirst = false;
		if(mAdapter != null){
			mAdapter.notifyDataSetChanged();
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Plps.STARTDATE, tvStartDate.getText().toString());
		params.put(Plps.ENDDATE, tvEndDate.getText().toString());
		params.put(Comm.ACCOUNT_ID, PlpsDataCenter.getInstance().getAcctList().get(accIndex).get(Comm.ACCOUNT_ID));
		params.put(Plps.MERCHNO,PlpsDataCenter.plpsPrepaidCardNo.get(prepaidCardTypeIndex-1));
		params.put(Plps.MERCHNAME, PlpsDataCenter.plpsPrepaidCardType.get(prepaidCardTypeIndex));
		params.put(Plps.REFRESH, String.valueOf(refresh));
		params.put(Plps.CURRENTINDEX, String.valueOf(currentIndex));
		params.put(Plps.PAGESIZE, Plps.PAGESIZENUM);
		requestHttp(Plps.PSNPREPAIDCARDQUERYREPLENISHMENTLIST, "requestPsnPrepaidCardQueryReplenishmentListCallBack", params, true);
	}

	/** 查询充值交易明细回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnPrepaidCardQueryReplenishmentListCallBack(Object resultObj) {
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		List<Map<String, Object>> list = (List<Map<String, Object>>) resultMap.get(Plps.LIST);
		if (StringUtil.isNullOrEmpty(list)) {
			BaseHttpEngine.dissMissProgressDialog();
			upLayout.setClickable(false);
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.plps_error));
			return;
		}
		if (StringUtil.isNullOrEmpty(listResult)) {
			listResult = list;
		} else {
			listResult.addAll(list);
		}
		currentIndex += list.size();
		upLayout.setClickable(true);
		findViewById(R.id.ll_query_condition).setVisibility(View.GONE);
		findViewById(R.id.result_condition).setVisibility(View.VISIBLE);
		findViewById(R.id.layout_result).setVisibility(View.VISIBLE);
		String totalNumber = (String) resultMap.get(Plps.RECORDNUMBER);
		addFooterView(totalNumber);
		BaseHttpEngine.dissMissProgressDialog();
		PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
		if (mAdapter == null) {
			mAdapter = new PrePaidCardResultQueryAdapter(this, listResult);
			lv.setAdapter(mAdapter);
		} else {
			mAdapter.setData(listResult);
		}
	}
}