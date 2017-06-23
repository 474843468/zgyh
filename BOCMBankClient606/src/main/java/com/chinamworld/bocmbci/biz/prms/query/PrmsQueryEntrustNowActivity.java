package com.chinamworld.bocmbci.biz.prms.query;

import java.util.ArrayList;
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
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.biz.prms.PrmsBaseActivity;
import com.chinamworld.bocmbci.biz.prms.adapter.PrmsQueryEntrustNowListAdapter;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.ViewUtils;

/**
 * 贵金属当前有效委托交易查询 列表页面
 * 
 * @author xyl
 * 
 */
public class PrmsQueryEntrustNowActivity extends PrmsBaseActivity {
	private static final String TAG = "PrmsQueryEntrustNowActivity";

	private ListView queryListView;
	private View headerView, footerView;

	private OnClickListener footerOnclickListenner;
	private int totalNum;

	private boolean isNewQuery = false;
	private int currentIndex = 0;
	private int pageSize = 10;

	private PrmsQueryEntrustNowListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();

	}

	private void init() {
		initView();
		initListenner();
		initData();
	}

	private void initView() {
		View childView = mainInflater.inflate(
				R.layout.prms_query_entrust_now_main, null);
		tabcontent.addView(childView);
		queryListView = (ListView) findViewById(R.id.query_list);
		headerView = findViewById(R.id.prms_listheader_layout);
		initListHeaderView(headerView, R.string.prms_query_detailes_entrustId,
				R.string.prms_buycurrency_no, R.string.prms_salecurrency_no);
		setTitle(R.string.prms_query_entrust_now);
	}

	private void initListenner() {
		queryListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent intent = new Intent();
				intent.putExtra(Prms.I_ENTRUSTQUERY_TITLE, getResources()
						.getString(R.string.prms_query_entrust_now));
				intent.putExtra(Prms.I_POSITION, position);
				intent.setClass(PrmsQueryEntrustNowActivity.this,
						PrmsQueryEntrustNowDetailsActivity.class);
				startActivityForResult(intent, 1);
			}
		});
	}

	private void initData() {
		Intent intent = getIntent();
		totalNum = Integer.valueOf(intent
				.getStringExtra(Prms.PRMS_QUERY_DEAL_RECORDNUMBER));
		isNewQuery = false;
		if (totalNum > 10) {
			addfooteeView(queryListView);
		}
		if (!StringUtil.isNullOrEmpty(prmsControl.queryEntrustNowList)) {
			adapter = new PrmsQueryEntrustNowListAdapter(this,
					prmsControl.queryEntrustNowList);
			queryListView.setAdapter(adapter);
		}
	}

	private void addfooteeView(ListView listView) {
		if (footerView != null) {
			removeFooterView(listView);
		}
		footerView = ViewUtils.getQuryListFooterView(this);
		footerOnclickListenner = new OnClickListener() {

			@Override
			public void onClick(View v) {
				isNewQuery = false;
				currentIndex += 10;
				BaseHttpEngine.showProgressDialog();
				queryPrmsTradeEntrustNow(currentIndex, pageSize, isNewQuery);
			}
		};
		footerView.setOnClickListener(footerOnclickListenner);
		listView.addFooterView(footerView, null, true);
	}

	private void removeFooterView(ListView listView) {
		listView.removeFooterView(footerView);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
	}

	@Override
	public void queryPrmsTradeEntrustNowCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.queryPrmsTradeEntrustNowCallBack(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> map = (Map<String, Object>) biiResponseBody
				.getResult();
		if (!StringUtil.isNullOrEmpty(prmsControl.queryEntrustNowList)
				&& !isNewQuery) {
			prmsControl.queryEntrustNowList
					.addAll((List<Map<String, Object>>) map
							.get(Prms.PRMS_QUERY_DEAL_LIST));
			if (prmsControl.queryEntrustNowList.size() >= totalNum) {
				removeFooterView(queryListView);
			}
		} else {
			prmsControl.queryEntrustNowList = (List<Map<String, Object>>) map
					.get(Prms.PRMS_QUERY_DEAL_LIST);
		}
		adapter.notifyDataSetChanged(prmsControl.queryEntrustNowList);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			isNewQuery = true;
			currentIndex = 0;
			clearData();
			BaseHttpEngine.showProgressDialogCanGoBack();
			queryPrmsTradeEntrustNow(currentIndex, pageSize, isNewQuery);
			break;

		default:
			break;
		}
	}
	private void clearData(){
		if(prmsControl.queryEntrustNowList!=null&&queryListView!=null&&adapter!=null){
			removeFooterView(queryListView);
			adapter.notifyDataSetChanged(new ArrayList<Map<String,Object>>());
		}
	}
}
