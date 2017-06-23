package com.chinamworld.bocmbci.biz.finc.accmanager;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.adapter.FincTaAccountAdapter;
import com.chinamworld.bocmbci.biz.finc.fundprice.FincFundTaSettingActivity;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.BaseSwipeListViewListener;
import com.chinamworld.bocmbci.widget.SwipeListView;

/**
 * 基金它账户管理列表页面
 * 
 * @author xiaoyl
 * 
 */
public class FundTaAccListActivity2 extends FincBaseActivity {
	private static final String TAG = "FundTaAccListActivity";

	private SwipeListView accListView;// Ta账户列表
	private OnItemClickListener onItemClickListener;// 他账户列表
	private List<Map<String, Object>> dataList;
	private FincTaAccountAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		BaseHttpEngine.showProgressDialogCanGoBack();
		queryFundTaAccList();
	}

	@Override
	public void queryFundTaAccListCallback(Object resultObj) {
		super.queryFundTaAccListCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		dataList = (List<Map<String, Object>>) biiResponseBody
				.getResult();
		if (!StringUtil.isNullOrEmpty(dataList)) {
			((TextView)findViewById(R.id.no_ta_acc)).setVisibility(View.GONE);
			adapter = new FincTaAccountAdapter(this, dataList);
			accListView.setAdapter(adapter);
			initListenner();
			accListView.setSwipeListViewListener(swipeListViewListener);
		}else{
			//TODO 
			((TextView)findViewById(R.id.no_ta_acc)).setVisibility(View.VISIBLE);
		}
	}

	private void init() {
		// 包含向右滑动的listview 目的是屏蔽左侧菜单的滑动事件
		setContainsSwipeListView(true);
		initView();
		initListenner();
		right.setOnClickListener(this);
	}

	private void initView() {
		setTitle(R.string.fincn_accmanner_fundTaacc);
		right.setText(R.string.finc_ta);
		View childView = mainInflater.inflate(
				R.layout.acc_mybankaccount_list, null);
		tabcontent.addView(childView);
		accListView = (SwipeListView) findViewById(R.id.acc_accountlist);
		accListView.setSwipeListViewListener(swipeListViewListener);
		accListView.setLastPositionClickable(true);
		accListView.setAllPositionClickable(true);
	}

	private void initListenner() {
		onItemClickListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Map<String, Object> map = dataList.get(position);
				BaseDroidApp.getInstanse().getBizDataMap()
						.put(Finc.D_FUNDTADAIL, map);
				Intent intent = new Intent();
				intent.setClass(FundTaAccListActivity2.this,
						FundTaDetailActivity.class);
				startActivityForResult(intent, 2);
			}

		};
	}
	
	/** 列表向右滑动事件 */
	BaseSwipeListViewListener swipeListViewListener = new BaseSwipeListViewListener() {

		@Override
		public void onStartOpen(int position, int action, boolean right) {
			if(action == 0){
				if (position >= dataList.size()) {
					return;
				}else {
					onItemClickListener.onItemClick(null,
							accListView, position, position);
				}
			}
		}

	};
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			BaseHttpEngine.showProgressDialogCanGoBack();
			queryFundTaAccList();
			break;
		default:
			
			break;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ib_top_right_btn:
			Intent intent = new Intent();
			intent.setClass(FundTaAccListActivity2.this,
					FincFundTaSettingActivity.class);
			startActivityForResult(intent, 1);
			break;

		default:
			break;
		}
	}
}
