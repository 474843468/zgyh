package com.chinamworld.bocmbci.biz.tran.collect.query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Collect;
import com.chinamworld.bocmbci.biz.tran.collect.CollectBaseActivity;
import com.chinamworld.bocmbci.biz.tran.collect.adapter.CollectQueryDetailAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: CollectQueryDetailActivity
 * @Description: 资金归集详情
 * @author luql
 * @date 2014-3-31 上午09:45:58
 */
public class CollectQueryDetailActivity extends CollectBaseActivity {

	private View mViewContent;
	private TextView mCountView;
	private TextView mAmountView;
	private ListView mListView;

	private String mWorkNoParam;
	private String mTransDateParam;
	private String mAmount;
	private String mCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 添加布局
		mViewContent = addView(R.layout.collect_query_detail_activity);
		setTitle(getString(R.string.collect_trans_detail_title));
		if (getIntentData()) {
			findViewById(R.id.sliding_body).setPadding(0, 0, 0, 0);
			toprightBtn();
			findView();
			requestPsnCBCTrsSingleQuery();
		} else {
			finish();
		}
	}

	private void requestPsnCBCTrsSingleQuery() {
		BaseHttpEngine.showProgressDialogCanGoBack();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Collect.PsnCBCTrsSingleQuery);
		Map<String, Object> params = new HashMap<String, Object>();

		params.put(Collect.workNo, mWorkNoParam);
		params.put(Collect.transDate, mTransDateParam);
		biiRequestBody.setParams(params);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));

		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnCBCTrsSingleQueryCallback");
	}

	public void requestPsnCBCTrsSingleQueryCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> map = (Map<String, Object>) biiResponseBody.getResult();
		List<Map<String, Object>> listMap = (List<Map<String, Object>>) map.get(Collect.list);
		showQueryResultView(listMap);
	}

	private void showQueryResultView(List<Map<String, Object>> data) {
		mAmountView.setText(StringUtil.parseStringPattern(mAmount, 2));
		mCountView.setText(mCount);
		CollectQueryDetailAdapter adapter = new CollectQueryDetailAdapter(this, data);
		mListView.setAdapter(adapter);
		mViewContent.findViewById(R.id.query_layout).setVisibility(View.VISIBLE);
	}

	private void findView() {
		mCountView = (TextView) mViewContent.findViewById(R.id.tv_count);
		mAmountView = (TextView) mViewContent.findViewById(R.id.tv_amount);
		mListView = (ListView) mViewContent.findViewById(R.id.lv);
	}

	private boolean getIntentData() {
		Intent intent = getIntent();
		mWorkNoParam = intent.getStringExtra(Collect.workNo);
		mTransDateParam = intent.getStringExtra(Collect.transDate);
		String sccCount = intent.getStringExtra(Collect.sccCount);
		String sumCount = intent.getStringExtra(Collect.sumCount);
		mCount = sccCount + " / " + sumCount;
		mAmount = intent.getStringExtra(Collect.amount);
		return true;
	}

}
