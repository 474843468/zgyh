package com.chinamworld.bocmbci.biz.remittance.recordQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Remittance;
import com.chinamworld.bocmbci.biz.remittance.RemittanceBaseActivity;
import com.chinamworld.bocmbci.biz.remittance.RemittanceContent;
import com.chinamworld.bocmbci.biz.remittance.RemittanceDataCenter;
import com.chinamworld.bocmbci.biz.remittance.adapter.RecordQueryAdapter;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class RemittanceRecordQueryListActivity extends RemittanceBaseActivity {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 主显示视图 */
	private View mMainView;
	/** 查询条件弹出框 */
	private View queryConditionView;
	/** 起始日期 */
	private String startDate;
	/** 结束日期 */
	private String endDate;
	/** 最近一周 */
	private Button btnLastWeek;
	/** 最近一月 */
	private Button btnLastMonth;
	/** 最近最近三月 */
	private Button btnLastThreeMonth;
	/** 开始日期 */
	private TextView tvStartDate;
	/** 结束日期 */
	private TextView tvEndDate;
	/** 查询日期段文本框 */
	private TextView tvQueryDate;
	/** 汇款类型 */
	private TextView tvRemittanceModel;
	/** 底部更多视图 */
	private View mFooterView;
	/** 交易历史列表 */
	private List<Map<String, Object>> listRecordQuery=null;
	/** 列表适配器 */
	private RecordQueryAdapter mAdapter;
	/** 查询结果列表控件 */
	private ListView lvRecord;
	/** 收起 */
	private LinearLayout llUp;
	/**查询后的界面 */
	private LinearLayout query_result_condition_layout;
	/**查询前的界面*/
	private LinearLayout search_shouqi_up;
	/**判断是否第一次进入，第一次进入不能点收起按钮*/
	private boolean isShowfirst = true;
	/**汇款类型Spinner*/
	private Spinner sp_remittance_model;
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.temit_inquire));
		BaseHttpEngine.showProgressDialogCanGoBack();
		requestSystemDateTime();// 先请求系统时间
		listRecordQuery=new ArrayList<Map<String, Object>>();
		mMainView = addView(R.layout.remittance_record_query_list);
//		mMainView.setVisibility(View.INVISIBLE);
		//进来不显示listview
		mMainView.findViewById(R.id.sbremit_querydeal_listview_layout).setVisibility(View.INVISIBLE);
		
		query_result_condition_layout=(LinearLayout) mMainView.findViewById(R.id.query_result_condition_layout);
		search_shouqi_up=(LinearLayout) mMainView.findViewById(R.id.search_shouqi_up);
		
		initLeftSideList(this, LocalData.crossBorderRemitLeftListData);
		mFooterView = LayoutInflater.from(this).inflate(R.layout.epay_tq_list_more, null);

		sp_remittance_model = (Spinner) mMainView.findViewById(R.id.sp_remittance_cremit_manage_trans_records);

		ArrayAdapter<String> currencyAdapter = new ArrayAdapter<String>(this, R.layout.custom_spinner_item, RemittanceDataCenter.remittanceMold);
		currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_remittance_model.setAdapter(currencyAdapter);
		sp_remittance_model.setBackgroundResource(R.drawable.bg_spinner);
		sp_remittance_model.setEnabled(true);

		sp_remittance_model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//				Toast.makeText(RemittanceRecordQueryListActivity.this,i+"--",Toast.LENGTH_LONG).show();
//				Toast.makeText(RemittanceRecordQueryListActivity.this,l+"--",Toast.LENGTH_LONG).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});




	}
	
	private void initViews() {
		//初始化查询框
		initQueryWindow();
//		PopupWindowUtils.getInstance().showQueryPopupWindowFirst();
		
		View titleOfLv = mMainView.findViewById(R.id.sbremit_listheader_layout);
		((TextView) titleOfLv.findViewById(R.id.list_header_tv1)).setText(getResources().getString(R.string.remitout_date));
		((TextView) titleOfLv.findViewById(R.id.list_header_tv2)).setText(getResources().getString(R.string.cashbank_trans_out_amt));
		((TextView) titleOfLv.findViewById(R.id.list_header_tv3)).setText(getResources().getString(R.string.acc_regex_act));
		lvRecord = (ListView) mMainView.findViewById(R.id.sbremit_querydeal_listview);
		lvRecord.setOnItemClickListener(detailListener);
		
		tvQueryDate = (TextView) mMainView.findViewById(R.id.tv_query_date);
		tvRemittanceModel = (TextView) mMainView.findViewById(R.id.tv_remittance_model);
		mMainView.findViewById(R.id.prms_down_LinearLayout).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isShowfirst){
					
				}else {
					if(query_result_condition_layout.isShown()){
						search_shouqi_up.setVisibility(View.VISIBLE);
						query_result_condition_layout.setVisibility(View.GONE);
						
					}else {
						query_result_condition_layout.setVisibility(View.VISIBLE);
						search_shouqi_up.setVisibility(View.GONE);
					}
				}
				
//				PopupWindowUtils.getInstance().showQueryPopupWindow();
			}
		});
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 0);
		}
		((RelativeLayout) mFooterView.findViewById(R.id.rl_get_more)).setBackgroundColor(getResources().getColor(R.color.transparent_00));
	}
	
	private void initQueryWindow(){
//		queryConditionView = LayoutInflater.from(this).inflate(R.layout.sbremit_query_condition, null);
//		PopupWindowUtils.getInstance().getQueryPopupWindow(queryConditionView, BaseDroidApp.getInstanse().getCurrentAct());
		
		search_shouqi_up.setVisibility(View.VISIBLE);
		
		btnLastWeek = (Button) mMainView.findViewById(R.id.btn_acc_onweek);
		btnLastMonth = (Button)mMainView. findViewById(R.id.btn_acc_onmonth);
		btnLastThreeMonth = (Button)mMainView.findViewById(R.id.btn_acc_threemonth);
		btnLastWeek.setOnClickListener(lastDateBtnListener);
		btnLastMonth.setOnClickListener(lastDateBtnListener);
		btnLastThreeMonth.setOnClickListener(lastDateBtnListener);
		tvStartDate = (TextView) mMainView.findViewById(R.id.sbremit_query_deal_startdate);
		tvEndDate = (TextView) mMainView.findViewById(R.id.sbremit_query_deal_enddate);
		tvStartDate.setText(startDate);
		tvEndDate.setText(endDate);
		tvStartDate.setOnClickListener(chooseDateClick);
		tvEndDate.setOnClickListener(chooseDateClick);
		
		findViewById(R.id.sbremit_query_deal_query).setOnClickListener(new OnClickListener() {
			// 查询按钮监听
			@Override
			public void onClick(View v) {
				
				startDate = tvStartDate.getText().toString();
				endDate = tvEndDate.getText().toString();
				if(listRecordQuery.size()>0){
					listRecordQuery.clear();
				}
				queryHisTrade();
			}
		});
		llUp=(LinearLayout) findViewById(R.id.up);
		llUp.setOnClickListener(new OnClickListener() {
				// 收起监听
				@Override
				public void onClick(View v) {
//					if(mMainView.isShown())
//						PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
					if(isShowfirst == false){
						if(query_result_condition_layout.isShown()){
							search_shouqi_up.setVisibility(View.VISIBLE);
							query_result_condition_layout.setVisibility(View.GONE);
							
						}else {
							query_result_condition_layout.setVisibility(View.VISIBLE);
							search_shouqi_up.setVisibility(View.GONE);
						}
					}

					
				}
		});
	}
	
	/** 初始查询页面 */
	private void initQuery() {
		isShowfirst=false;
		if (!StringUtil.isNullOrEmpty(listRecordQuery)) {
//			listRecordQuery.clear();
			mAdapter.setData(listRecordQuery);
		}
		mMainView.findViewById(R.id.sbremit_querydeal_listview_layout).setVisibility(View.VISIBLE);
//		mMainView.setVisibility(View.INVISIBLE);
		if(query_result_condition_layout.isShown()){
			search_shouqi_up.setVisibility(View.VISIBLE);
			query_result_condition_layout.setVisibility(View.GONE);
			
		}else {
			query_result_condition_layout.setVisibility(View.VISIBLE);
			search_shouqi_up.setVisibility(View.GONE);
		}
		
	}

	/**
	 * 查询历史记录，先校验时间
	 */
	private void queryHisTrade() {
		//校验日期
		if(!QueryDateUtils.commQueryStartAndEndateReg(this, startDate, endDate, dateTime))
			return;
		BaseHttpEngine.showProgressDialog();
		Map<String, Object> params = new HashMap<String, Object>();
		int select = (int) sp_remittance_model.getSelectedItemId();
		if (select == 0) {
			params.put(Remittance.TRANSTYPE, "PB047");
		} else {
			params.put(Remittance.TRANSTYPE, Remittance.ServiceId);
		}
		params.put(Remittance.STARTDATE, startDate);
		params.put(Remittance.ENDDATE, endDate);
		if (StringUtil.isNullOrEmpty(listRecordQuery)) {
			params.put(Remittance.CURRENTINDEX, 0);
			Log.v("BiiHttpEngine", 0 + "-------");
		} else {
			params.put(Remittance.CURRENTINDEX, listRecordQuery.size());
			Log.v("BiiHttpEngine",  listRecordQuery.size() + "-------");
		}
		params.put(Remittance.PAGESIZE, RemittanceContent.PAGESIZE);
		getHttpTools().requestHttp(Remittance.PSNTRANSQUERYTRANSFERRECORD, "requestPsnTransQueryTransferRecordCallBack", params, false);
	}

	/**
	 * 添加分页布局
	 * 
	 * @param totalCount
	 *            后台的产品列表总长度
	 */
	private void addFooterView(String totalCount) {
		int listSize = listRecordQuery.size();
		if (Integer.valueOf(totalCount) > listSize) {
			if (lvRecord.getFooterViewsCount() <= 0) {
				lvRecord.addFooterView(mFooterView);
			}
		} else {
			if (lvRecord.getFooterViewsCount() > 0) {
				lvRecord.removeFooterView(mFooterView);
			}
		}
		mFooterView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BaseHttpEngine.showProgressDialog();
				// 请求更多
				Map<String, Object> params = new HashMap<String, Object>();

				int select = (int) sp_remittance_model.getSelectedItemId();
				if (select == 0) {
					params.put(Remittance.TRANSTYPE, "PB047");
				} else {
					params.put(Remittance.TRANSTYPE, Remittance.ServiceId);
				}

				params.put(Remittance.STARTDATE, startDate);
				params.put(Remittance.ENDDATE, endDate);
				params.put(Remittance.CURRENTINDEX, String.valueOf(listRecordQuery.size()));
				Log.v("BiiHttpEngine",  listRecordQuery.size() + "========");
				params.put(Remittance.PAGESIZE, RemittanceContent.PAGESIZE);
				getHttpTools().requestHttp(Remittance.PSNTRANSQUERYTRANSFERRECORD, "requestPsnTransQueryTransferRecordCallBack", params, false);
			}
		});
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------控件事件-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 最近XX时间查询的按钮点击事件 */
	private OnClickListener lastDateBtnListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			endDate = QueryDateUtils.getcurrentDate(dateTime);
			switch (v.getId()) {
			case R.id.btn_acc_onweek:
				// 查询最近一周
				BaseHttpEngine.showProgressDialog();
//				initQuery();
				startDate = QueryDateUtils.getlastWeek(dateTime);
				if(listRecordQuery.size()>0){
					listRecordQuery.clear();
				}
				queryHisTrade();
				break;

			case R.id.btn_acc_onmonth:
				// 查询最近一月
				BaseHttpEngine.showProgressDialog();
//				initQuery();
				startDate = QueryDateUtils.getlastOneMonth(dateTime);
				if(listRecordQuery.size()>0){
					listRecordQuery.clear();
				}
				queryHisTrade();
				break;
				
			case R.id.btn_acc_threemonth:
				// 查询最近三月
				BaseHttpEngine.showProgressDialog();
//				initQuery();
				startDate = QueryDateUtils.getlastThreeMonth(dateTime);
				if(listRecordQuery.size()>0){
					listRecordQuery.clear();
				}
				
				queryHisTrade();
				break;
			}
//			tvStartDate.setText(startDate);
//			tvEndDate.setText(endDate);
		}
	};
	
	/** 列表项点击事件 */
	private OnItemClickListener detailListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			BaseHttpEngine.showProgressDialog();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(Remittance.TRANSID, listRecordQuery.get(arg2).get(Remittance.TRANSACTIONID));
			getHttpTools().requestHttp(Remittance.PSNTRANSQUERYTRANSFERRECORDDETAIL, "requestPsnTransQueryTransferRecordDetailCallBack", params, false);
		}
	};

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO ---------------------------------------网络请求与回调方法--------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		startDate = QueryDateUtils.getlastthreeDate(dateTime);
		endDate = QueryDateUtils.getcurrentDate(dateTime);
		initViews();
	}
	
	@SuppressWarnings("unchecked")
	public void requestPsnTransQueryTransferRecordCallBack(Object resultObj) {
		mMainView.setVisibility(View.VISIBLE);
		BaseHttpEngine.dissMissProgressDialog();
		
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
//		Map<String, Object> resultMap = (Map<String, Object>) getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		List<Map<String, Object>> mList = (List<Map<String, Object>>) resultMap.get(Remittance.LIST);

		String recordnumber = (String) resultMap.get("recordnumber");

		Log.v("BiiHttpEngine",resultMap.get("recordnumber") +" requestPsnTransQueryTransferRecordCallBack");
		Log.v("BiiHttpEngine",mList.size() +" requestPsnTransQueryTransferRecordCallBack");

		if(StringUtil.isNullOrEmpty(mList)){
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.remittance_no_transferlist));
			mMainView.findViewById(R.id.sbremit_querydeal_listview_layout).setVisibility(View.GONE);
//			mMainView.setVisibility(View.GONE);
			llUp.setClickable(false);
			search_shouqi_up.setVisibility(View.VISIBLE);
			query_result_condition_layout.setVisibility(View.GONE);
			return;
		}else {
			initQuery();
			mMainView.findViewById(R.id.sbremit_querydeal_listview_layout).setVisibility(View.VISIBLE);
			mMainView.setVisibility(View.VISIBLE);
			search_shouqi_up.setVisibility(View.GONE);
			query_result_condition_layout.setVisibility(View.VISIBLE);
		}
		if (listRecordQuery == null) {
			listRecordQuery = mList;
		} else {
			listRecordQuery.addAll(mList);
		}
		addFooterView((String) resultMap.get(Remittance.RECORDNUMBER_RECORD));
		if (mAdapter == null) {
			mAdapter = new RecordQueryAdapter(this, listRecordQuery);
			lvRecord.setAdapter(mAdapter);
		} else {
			mAdapter.setData(listRecordQuery);
		}
		tvQueryDate.setText(startDate + "-" + endDate);

		Log.v("BiiHttpEngine",startDate + "-" + endDate);
		tvRemittanceModel.setText((CharSequence) sp_remittance_model.getSelectedItem());
		Log.v("BiiHttpEngine",sp_remittance_model.getSelectedItem()+"---");
		PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
	}
	
	/** 汇款记录详情回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnTransQueryTransferRecordDetailCallBack(Object resultObj) {
		Map<String, Object> resultMap = (Map<String, Object>) getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		RemittanceDataCenter.getInstance().setMapPsnTransQueryTransferRecordDetail(resultMap);
		BaseHttpEngine.dissMissProgressDialog();
		startActivityForResult(new Intent(this, RemittanceRecordQueryDetailActivity.class), QUIT_CODE);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		isShowfirst=true;
		super.onDestroy();
	}
}
