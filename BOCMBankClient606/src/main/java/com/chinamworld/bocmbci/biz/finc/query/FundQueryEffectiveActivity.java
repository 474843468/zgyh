package com.chinamworld.bocmbci.biz.finc.query;

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
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.adapter.FundQueryEntrustListAdapter;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.ViewUtils;

/**
 * 在途交易查询 页面
 * 
 * @author xyl
 * 
 */
public class FundQueryEffectiveActivity extends FincBaseActivity {
	private static final String TAG = "FundQueryEffectiveActivity";

	private int currentIndex = 10;
	private int pageSize = 10;

	private ListView listView;
	private View footerView;

	private boolean isNewQuery = true;
	private OnClickListener footerOnclickListenner;
	private FundQueryEntrustListAdapter adapter;

	private Integer totalNum;

	private List<Map<String, Object>> resultList;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void fundQueryEnTrustCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.fundQueryEnTrustCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		currentIndex +=10;
		if (StringUtil.isNullOrEmpty((List<Map<String, Object>>) resultMap
				.get(Finc.FINC_QUERYEXTRADAY_RESULTLIST))) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.finc_query_noresult_error));
			return;
		}
		totalNum = Integer.valueOf((String) resultMap
				.get(Prms.PRMS_QUERY_DEAL_RECORDNUMBER));
		if (!isNewQuery && resultList != null && adapter != null) {
			resultList.addAll((List<Map<String, Object>>) resultMap
					.get(Finc.FINC_QUERYEXTRADAY_RESULTLIST));
			adapter.notifyDataSetChanged(resultList);
			// if(pageSize*currentIndex>=totalNum){//全部显示完鸟
			if (resultList.size() >= totalNum) {// 全部显示完鸟\
				removeFooterView(listView);
			}
		} else {
			resultList = (List<Map<String, Object>>) resultMap
					.get(Finc.FINC_QUERYEXTRADAY_RESULTLIST);
			adapter = new FundQueryEntrustListAdapter(this, resultList);
			if (totalNum > 10) {// 不能显示完
				addfooteeView(listView);
			} else {
				removeFooterView(listView);
			}
			listView.setAdapter(adapter);
		}
	}
	private void clearData(){
		if(resultList!=null&&listView!=null&&adapter!=null){
			currentIndex  =0;
			removeFooterView(listView);
			adapter.notifyDataSetChanged(new ArrayList<Map<String,Object>>());
		}
	}
	private void addfooteeView(ListView listView) {
		if (footerView != null) {
			removeFooterView(listView);
		}
		if (footerOnclickListenner == null) {
			footerOnclickListenner = new OnClickListener() {

				@Override
				public void onClick(View v) {
					isNewQuery = false;
					BaseHttpEngine.showProgressDialog();
					fundQueryEnTrust(currentIndex, pageSize, isNewQuery);
				}
			};
		}
		footerView = ViewUtils.getQuryListFooterView(this);
		footerView.setOnClickListener(footerOnclickListenner);
		listView.addFooterView(footerView, null, true);
	}

	private void removeFooterView(ListView listView) {
		listView.removeFooterView(footerView);
	}

	private void init() {
		View childView = mainInflater.inflate(R.layout.finc_query_today_list,
				null);
		tabcontent.addView(childView);
		setTitle(R.string.finc_title_verydayquery);
		listView = (ListView) childView.findViewById(R.id.query_list);
		footerView = ViewUtils.getQuryListFooterView(this);
		initListHeaderView(R.string.finc_entrustdate,
				R.string.finc_fundname, R.string.third_trade_type);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Map<String,Object>  detailMap = resultList.get(position);
				BaseDroidApp.getInstanse().getBizDataMap().put(Finc.D_ENTRUST_DETAILMAP,detailMap);
				Intent intent = new Intent();
				intent.setClass(FundQueryEffectiveActivity.this,
						FundQueryEffectiveDetailsActivity.class);
				startActivityForResult(intent, 1);
			}
		});
		Map<String, Object> resultMap = (Map<String, Object>) BaseDroidApp
				.getInstanse().getBizDataMap().get(Finc.D_ENTRUST_RESULTMAP);
		totalNum = Integer.valueOf((String) resultMap
				.get(Prms.PRMS_QUERY_DEAL_RECORDNUMBER));
		resultList = (List<Map<String, Object>>) resultMap
				.get(Finc.FINC_QUERYEXTRADAY_RESULTLIST);
		adapter = new FundQueryEntrustListAdapter(this, resultList);
		if (totalNum > 10) {// 不能显示完
			addfooteeView(listView);
		}
		listView.setAdapter(adapter);
		initRightBtnForMain();
		setTitle(R.string.fincn_query_effective);
	}

	@Override
	public void finish() {
		BaseDroidApp
		.getInstanse().getBizDataMap().remove(Finc.D_ENTRUST_RESULTMAP);
		super.finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			BaseHttpEngine.showProgressDialogCanGoBack();
			requestCommConversationId();
			break;

		default:
			break;
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		clearData();
		currentIndex = 0;
		isNewQuery = true;
		fundQueryEnTrust(currentIndex, pageSize, isNewQuery);
	}
	 
 
	
}
