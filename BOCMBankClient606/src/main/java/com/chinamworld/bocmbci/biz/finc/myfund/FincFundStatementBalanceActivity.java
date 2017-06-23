package com.chinamworld.bocmbci.biz.finc.myfund;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.FincUtils;
import com.chinamworld.bocmbci.biz.finc.adapter.FincFundStatementBalanceAdapter;
import com.chinamworld.bocmbci.biz.finc.adapter.FincFundTransSactionAdapter;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 基金对账单查询页面
 * @author fsm
 *
 */
public class FincFundStatementBalanceActivity extends FincBaseActivity implements OnCheckedChangeListener{
	
	private RadioGroup radioGroup;
	private RadioButton rb1, rb2;
	private Spinner times_spinner;
	private Button search;
	private LinearLayout ll_upLayout;
	
	private LinearLayout xiala_view;
	private ListView fundStateBalanResult, fundTransSaction;
	
	private List<Map<String, Object>> fundStatementBalance;//基金对账单持仓
	private FincFundStatementBalanceAdapter fundStatementBalanceAdapter;
	private List<Map<String, Object>> transSactionList;//交易流水
	private FincFundTransSactionAdapter fundTransSactionAdapter;

	private boolean isLastMonth1, isLastMonth2;
	private LinearLayout ll_query_condition;
	private String queryDate;
	private LinearLayout acc_query_result_condition;
	
	private RelativeLayout crcd_choserl;
	private LinearLayout list_view_finc;
	private boolean isfirst=true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tabcontent.setPadding(0, 0, 0, 0);
		tabcontent.addView(mainInflater.inflate(R.layout.finc_myfinc_fund_statement_banlance, null));
		setTitle(getResources().getString(R.string.finc_myfinc_fundStatementBalance));
		BaseHttpEngine.showProgressDialogCanGoBack();
		requestSystemDateTime();
		
		
		
	}
	
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		initQueryWindow();
		initViews();
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.search:
			BaseHttpEngine.showProgressDialog();
//			if(rb1.isChecked()){
				int position = times_spinner.getSelectedItemPosition();
				isLastMonth1 = position == 0 ? true : false;
				setCheckDefault();
				
					
				
				queryDate = times_spinner.getSelectedItem().toString();
				fundStatementBalance = null; transSactionList = null;
				requestFundStatementBalanceQuery(queryDate);
				findViewById(R.id.result_layout).setVisibility(View.VISIBLE);
				findViewById(R.id.mianze).setVisibility(View.GONE);
			break;
		case R.id.ll_upLayout:
			
			ll_query_condition.setVisibility(View.GONE);
			acc_query_result_condition.setVisibility(View.VISIBLE);
//			PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
			break;
		case R.id.xiala_view:
			ll_query_condition.setVisibility(View.VISIBLE);
			acc_query_result_condition.setVisibility(View.GONE);
//			PopupWindowUtils.getInstance().showQueryPopupWindow();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		super.onItemClick(arg0, arg1, arg2, arg3);
		switch (arg0.getId()) {
		case R.id.fundStateBalanResult:
			int size = fundStatementBalance == null ? 0 : fundStatementBalance.size();
			if(arg2 < size){
				fincControl.fundStatementBalanceDetail = fundStatementBalance.get(arg2);
				startActivity(new Intent(this, FincFundStatementBalanceDetailActivity.class));
			}
			break;
		case R.id.fundTransSaction:
			int size2 = transSactionList == null ? 0 : transSactionList.size();
			if(arg2 < size2){
				fincControl.transSactionDetail = transSactionList.get(arg2);
				startActivity(new Intent(this, FincFundTransSactionDetailActivity.class));
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public void fundStatementBalanceQueryCallback(Object resultObj) {
		super.fundStatementBalanceQueryCallback(resultObj);
		if(fundStatementBalanceAdapter != null){
			fundStatementBalanceAdapter.setDatas(new ArrayList<Map<String, Object>>());
		}
		if(StringUtil.isNullOrEmpty(fincControl.fundStatementBalance)){
			BaseHttpEngine.dissMissProgressDialog();
			if(ll_query_condition.isShown()){
				ll_query_condition.setVisibility(View.VISIBLE);
				acc_query_result_condition.setVisibility(View.GONE);
			}else {
				ll_query_condition.setVisibility(View.GONE);
				acc_query_result_condition.setVisibility(View.VISIBLE);
			}
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_query_noresult_error));
			return;
		}
		if(ll_query_condition.isShown()){
			ll_query_condition.setVisibility(View.GONE);
			acc_query_result_condition.setVisibility(View.VISIBLE);
		}else {
			
		}
		crcd_choserl.setVisibility(View.VISIBLE);
		list_view_finc.setVisibility(View.VISIBLE);
		/** 处理查询结果页数据外其它信息*/
		ll_upLayout.setOnClickListener(this);
		ll_query_condition.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_bg_query_no));
		setListHeaderView();
		String startTime = times_spinner.getSelectedItem().toString() + "/01";
		String endTime = QueryDateUtils.getMonthLastDay(startTime);
//		if(isLastMonth1)
//			endTime = QueryDateUtils.getcurrentDate(dateTime);
		((TextView)findViewById(R.id.queryTime)).setText(startTime + "-" + endTime);
		PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
		
		fundStatementBalance = (List<Map<String, Object>>) fincControl.fundStatementBalance
				.get(Finc.FINC_FUNDTODAYQUERY_LIST);
		if(StringUtil.isNullOrEmpty(fundStatementBalance)){
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_query_noresult_error));
			return;
		}
		if(fundStatementBalanceAdapter == null){
			fundStatementBalanceAdapter = new FincFundStatementBalanceAdapter(this, fundStatementBalance);
			fundStateBalanResult.setAdapter(fundStatementBalanceAdapter);
//			findViewById(R.id.result_layout).setVisibility(View.VISIBLE);
//			findViewById(R.id.mianze).setVisibility(View.GONE);
		}else
			fundStatementBalanceAdapter.setDatas(fundStatementBalance);
		BaseHttpEngine.dissMissProgressDialog();
	}
	
	@Override
	public void persionalTransDetailQueryCallback(Object resultObj) {
		super.persionalTransDetailQueryCallback(resultObj);
		if(fundTransSactionAdapter != null){
			fundTransSactionAdapter.setDatas(new ArrayList<Map<String, Object>>());
		}
		if(StringUtil.isNullOrEmpty(fincControl.transSactionList)){
			if(ll_query_condition.isShown()){
				ll_query_condition.setVisibility(View.VISIBLE);
				acc_query_result_condition.setVisibility(View.GONE);
			}else {
				ll_query_condition.setVisibility(View.GONE);
				acc_query_result_condition.setVisibility(View.VISIBLE);
			}
			return;
		}
			
			
			
			
		transSactionList = fincControl.transSactionList;
		ll_upLayout.setOnClickListener(this);
		ll_query_condition.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_bg_query_no));
		setTransactionListHeaderView();
		if(fundTransSactionAdapter == null){
			fundTransSactionAdapter = new FincFundTransSactionAdapter(this, transSactionList);
			fundTransSaction.setAdapter(fundTransSactionAdapter);
			findViewById(R.id.result_layout).setVisibility(View.VISIBLE);
			findViewById(R.id.mianze).setVisibility(View.GONE);
		}else
			fundTransSactionAdapter.setDatas(transSactionList);
		String startTime = times_spinner.getSelectedItem().toString() + "/01";
		String endTime = QueryDateUtils.getMonthLastDay(startTime);
		if(isLastMonth2)
			endTime = QueryDateUtils.getcurrentDate(dateTime);
		((TextView)findViewById(R.id.queryTime)).setText(startTime + "-" + endTime);
		PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
		BaseHttpEngine.dissMissProgressDialog();
	}
	
	private void initQueryWindow(){
//		View view = LayoutInflater.from(this).inflate(
//				R.layout.finc_myfinc_statement_banlance_query_condition, null);
//		PopupWindowUtils.getInstance().getQueryPopupWindow(view, this);
		ll_query_condition = (LinearLayout) findViewById(R.id.ll_query_condition);
		ll_query_condition.setVisibility(View.VISIBLE);
		
		acc_query_result_condition=(LinearLayout) findViewById(R.id.acc_query_result_condition);
		acc_query_result_condition.setVisibility(View.GONE);
		
		crcd_choserl=(RelativeLayout) findViewById(R.id.crcd_choserl);
		crcd_choserl.setVisibility(View.GONE);
		
		list_view_finc=(LinearLayout)findViewById(R.id.list_view_finc);
		list_view_finc.setVisibility(View.GONE);
		times_spinner = (Spinner)findViewById(R.id.times_spinner);
		search = (Button)findViewById(R.id.search);search.setOnClickListener(this);
		ll_upLayout = (LinearLayout)findViewById(R.id.ll_upLayout);
		FincUtils.initSpinnerView(this, times_spinner, getQueryBillTime1());
//		PopupWindowUtils.getInstance().showQueryPopupWindowFirst();
	}
	
	private void initViews(){
		radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
		radioGroup.setOnCheckedChangeListener(this);
		rb1 = (RadioButton)findViewById(R.id.rb1);
		rb2 = (RadioButton)findViewById(R.id.rb2);
		xiala_view = (LinearLayout)findViewById(R.id.xiala_view);
		fundStateBalanResult = (ListView)findViewById(R.id.fundStateBalanResult);
		fundTransSaction = (ListView)findViewById(R.id.fundTransSaction);
		xiala_view.setOnClickListener(this);
		fundStateBalanResult.setOnItemClickListener(this);
		fundTransSaction.setOnItemClickListener(this);
	}
	
	private void setListHeaderView(){
		TextView tv1 = (TextView)findViewById(R.id.list_header_tv1);
		TextView tv2 = (TextView)findViewById(R.id.list_header_tv2);
		TextView tv3 = (TextView)findViewById(R.id.list_header_tv3);
		tv1.setText(getString(R.string.finc_fundname));
		tv2.setText(FincUtils.getNoColonStr(getString(R.string.forex_myfinc_blance_value)));
		tv3.setText(FincUtils.getNoColonStr(getString(R.string.finc_statement_floatLoss)));
	}
	
	private void setTransactionListHeaderView(){
		TextView tv1 = (TextView)findViewById(R.id.list_header_tv1);
		TextView tv2 = (TextView)findViewById(R.id.list_header_tv2);
		TextView tv3 = (TextView)findViewById(R.id.list_header_tv3);
		tv1.setText(getString(R.string.finc_fundname));
		tv2.setText(FincUtils.getNoColonStr(getString(R.string.finc_tradetype)));
		tv3.setText(FincUtils.getNoColonStr(getString(R.string.finc_tradestate_colon)));
	}
	
	/**
	 * 组合查询月份
	 * @return
	 */
	private List<String> getQueryBillTime(){
		List<String> dateList = new ArrayList<String>();
		for (int i = 0; i < 6; i++) {
			String t = "-" + i;
			int j = Integer.valueOf(t);
			String times = QueryDateUtils.getLastNumMonthAgo(dateTime, j);
			dateList.add(times);
		}
		return dateList;
	}
	
	
	/**
	 * 组合查询月份 --不包含本月
	 * @return
	 */
	private List<String> getQueryBillTime1(){
		List<String> dateList = new ArrayList<String>();
		for (int i = 0; i < 7; i++) {
			String t = "-" + i;
			int j = Integer.valueOf(t);
			String times = QueryDateUtils.getLastNumMonthAgo(dateTime, j);
			if(i == 0){
				continue;
			}
			dateList.add(times);
		}
		return dateList;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb1:
			fundStateBalanResult.setVisibility(View.VISIBLE);
			fundTransSaction.setVisibility(View.GONE);
			setListHeaderView();
			if(StringUtil.isNullOrEmpty(fundStatementBalance)){
				BaseHttpEngine.showProgressDialog();
				requestFundStatementBalanceQuery(queryDate);
			}
			break;
		case R.id.rb2:
			fundStateBalanResult.setVisibility(View.GONE);
			fundTransSaction.setVisibility(View.VISIBLE);
			setTransactionListHeaderView();
			if(StringUtil.isNullOrEmpty(transSactionList)){
				BaseHttpEngine.showProgressDialog();
				requestPersionalTransDetailQuery(queryDate, isLastMonth1,
						QueryDateUtils.getcurrentDate(dateTime));
			}
			break;
		default:
			break;
		}
	}
	
	private void setCheckDefault(){
		rb1.setChecked(true);
	
		
		
	}
}
