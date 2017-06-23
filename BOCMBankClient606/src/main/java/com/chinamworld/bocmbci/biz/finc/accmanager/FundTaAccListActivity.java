package com.chinamworld.bocmbci.biz.finc.accmanager;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.adapter.FundTaListAdapter;
import com.chinamworld.bocmbci.biz.finc.fundprice.FincFundTaSettingActivity;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 基金它账户管理列表页面
 * 
 * @author xiaoyl
 * 
 */
public class FundTaAccListActivity extends FincBaseActivity {
	private static final String TAG = "FundTaAccListActivity";

	private ListView accListView;// Ta账户列表
	private OnItemClickListener onItemClickListener;// 他账户列表
	private List<Map<String, Object>> dataList;
	private FundTaListAdapter adapter;

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
			adapter = new FundTaListAdapter(this, dataList);
			accListView.setAdapter(adapter);
		}else{
			//TODO 
		}
	}

	private void init() {
		initView();
		initListenner();
	}

	private void initView() {
		View childView = mainInflater.inflate(
				R.layout.finc_fundta_acc_list_activity, null);
		tabcontent.addView(childView);
		initListHeaderView(R.string.finc_ta_acc, R.string.finc_setfundcompany_nocolon,
				R.string.acc_state_nocolon);
		accListView = (ListView) findViewById(R.id.query_list);
		right.setText(R.string.finc_ta);
		setTitle(R.string.fincn_accmanner_fundTaacc);
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
				intent.setClass(FundTaAccListActivity.this,
						FundTaDetailActivity.class);
				startActivityForResult(intent, 1);
			}

		};
		accListView.setOnItemClickListener(onItemClickListener);
		right.setOnClickListener(this);
	}

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
			intent.setClass(FundTaAccListActivity.this,
					FincFundTaSettingActivity.class);
			startActivityForResult(intent, 1);
			break;

		default:
			break;
		}
	}
}
