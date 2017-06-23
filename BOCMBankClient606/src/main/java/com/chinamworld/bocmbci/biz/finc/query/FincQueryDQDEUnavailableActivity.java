package com.chinamworld.bocmbci.biz.finc.query;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.FincUtils;
import com.chinamworld.bocmbci.biz.finc.adapter.FundQueryDQDEUnavailableAdapter;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.ViewUtils;

/**
 * 基金已失效定期定额查询
 * 
 * @author fsm
 * 
 */
public class FincQueryDQDEUnavailableActivity extends FincBaseActivity {
	private ListView listView;
	private OnItemClickListener onItemClickListener;
	private List<Map<String, Object>> resultList;
	private FundQueryDQDEUnavailableAdapter apdater;

	private boolean isNewQuery;
	private int totalNum;
	private int currentIndex = 10;
	private View footerView;
	private OnClickListener footerOnclickListenner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	@Override
	public void scheduledFundUnavailableQueryCallback(Object resultObj) {
		super.scheduledFundUnavailableQueryCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if(StringUtil.isNullOrEmpty(fincControl.fundUnavailableList))
			return;
		currentIndex += 10;
		resultList.addAll(fincControl.fundUnavailableList);
//		resultList = fincControl.sortListByKey(resultList, Finc.INVALIDATIONDATE);
		initData();
	}

	/** 添加显示更多的 功能 */
	private void addfooteeView(ListView listView) {
		removeFooterView(listView);
		footerView = ViewUtils.getQuryListFooterView(this);
		if (footerOnclickListenner == null) {
			footerOnclickListenner = new OnClickListener() {

				@Override
				public void onClick(View v) {
					isNewQuery = false;
					BaseHttpEngine.showProgressDialog();
					requestScheduledFundUnavailableQuery(currentIndex, isNewQuery);
				}
			};
		}
		footerView.setOnClickListener(footerOnclickListenner);
		listView.addFooterView(footerView, null, true);
	}

	private void removeFooterView(ListView listView) {
		if(listView != null && listView.getFooterViewsCount() > 0 && footerView != null)
			listView.removeFooterView(footerView);
	}

	private void init() {
		View childView = mainInflater.inflate(R.layout.finc_query_dqde_list,
				null);
		tabcontent.addView(childView);
		tabcontent.setPadding(0,
				getResources().getDimensionPixelSize(R.dimen.fill_margin_left),
				0, 0);
		findViewById(R.id.searchResult).setVisibility(View.GONE);
		findViewById(R.id.condition_layout).setVisibility(View.GONE);
		findViewById(R.id.result).setVisibility(View.VISIBLE);
		setTitle(R.string.finc_scheduled_unavailable);
		//申请日期，基金名称，交易类型
		initListHeaderView(R.string.finc_applayDate, R.string.finc_fundname,
				R.string.finc_tradetype);
		listView = (ListView) childView.findViewById(R.id.query_list);
		onItemClickListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				BaseHttpEngine.showProgressDialog();
				fincControl.fundUnavailableMap = resultList.get(position);
				if (!StringUtil.isNullOrEmpty(fincControl.fundUnavailableMap)) {
					//请求定投定赎明细
					String applyType = (String)fincControl.fundUnavailableMap.get(Finc.APPLYTYPE);
					String fundScheduleDate = (String)fincControl.fundUnavailableMap.get(Finc.I_APPLYDATE);
					String scheduleBuyNum = (String)fincControl.fundUnavailableMap.get(Finc.ORNSCHEDULENUM);
					if(FincUtils.isStrEquals(applyType, "1")){//定投
						requestFundScheduledBuyDetailQuery(fundScheduleDate, scheduleBuyNum);
					}else{//定赎
						requestFundScheduledSellDetailQuery(fundScheduleDate, scheduleBuyNum);
					}
				}
			}
		};
		listView.setOnItemClickListener(onItemClickListener);
//		resultList = fincControl.sortListByKey(fincControl.fundUnavailableList, Finc.INVALIDATIONDATE);
		resultList = fincControl.fundUnavailableList;
		initData();
	}
	
	@Override
	public void fundScheduledBuyDetailQueryCallback(Object resultObj) {
		super.fundScheduledBuyDetailQueryCallback(resultObj);
		startDetailActivity();
	}
	
	@Override
	public void fundScheduledSellDetailQueryCallback(Object resultObj) {
		super.fundScheduledSellDetailQueryCallback(resultObj);
		startDetailActivity();
	}
	
	private void startDetailActivity(){
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent();
		intent.setClass(FincQueryDQDEUnavailableActivity.this,
				FincQueryDQDEUnavailableDetailActivity.class);
		startActivityForResult(intent, 1);
	}
	
	private void initData(){
		totalNum = Integer.valueOf((String) fincControl.fundUnavailableResult
				.get(Prms.PRMS_QUERY_DEAL_RECORDNUMBER));
		if(apdater == null){
			apdater = new FundQueryDQDEUnavailableAdapter(this, resultList);
			if (totalNum > 10) {// 不能显示完
				addfooteeView(listView);
			} else {
				removeFooterView(listView);
			}
			listView.setAdapter(apdater);
		}else{
			apdater.notifyDataSetChanged(resultList);
			if (resultList.size() >= totalNum) {// 全部显示完鸟
				removeFooterView(listView);
			}
		}
	}

}
