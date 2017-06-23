package com.chinamworld.bocmbci.biz.lsforex.manageacc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseActivity;
import com.chinamworld.bocmbci.biz.lsforex.adapter.IsForexProduceChoiseAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 签约----选择可签约保证金 */
public class IsForexProduceChoiseActivity extends IsForexBaseActivity {
	private final String TAG = "IsForexProduceChoiseActivity";
	private Button backButton = null;
	private View rateInfoView = null;
	private int pageSize = 10;
	private int currentIndex = 0;
	private boolean refresh = true;
	private int recordNumber = 0;
	private ListView signListView = null;
	private Button nextButton = null;
	/** 保证金产品列表 */
	private List<Map<String, String>> baillResultList = null;
	private IsForexProduceChoiseAdapter adapter = null;
	private RelativeLayout load_more = null;
	private int bailPosition = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.isForex_manage_right));
		LogGloble.d(TAG, "onCreate");
		IsForexProduceChoiseAdapter.selectedPosition = -1;
		backButton = (Button) findViewById(R.id.ib_back);
		rateInfoView = LayoutInflater.from(this).inflate(R.layout.isforex_manage_sign_choise, null);
		tabcontent.addView(rateInfoView);
		signListView = (ListView) findViewById(R.id.product_list);
		nextButton = (Button) findViewById(R.id.forex_nextButton);
		baillResultList = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(IsForex.ISFOREX_LIST);
		recordNumber = getIntent().getIntExtra(IsForex.ISFOREX_RECORDERNUMBER_RES, 0);
		if (baillResultList == null || baillResultList.size() <= 0) {
			return;
		}
		if (recordNumber <= 0) {
			return;
		}
		load_more = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.acc_load_more, null);
		Button btn_load_more = (Button) load_more.findViewById(R.id.btn_load_more);
		btn_load_more.setOnClickListener(goMoreClickListener);
		if (recordNumber > pageSize) {
			// 显示更多按钮
			signListView.addFooterView(load_more);
		}
		adapter = new IsForexProduceChoiseAdapter(IsForexProduceChoiseActivity.this, baillResultList);
		signListView.setAdapter(adapter);
		initOnClick();
	}

	private void initOnClick() {
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		signListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				LogGloble.d(TAG, "position======" + position);
				bailPosition = position;
				IsForexProduceChoiseAdapter.selectedPosition = position;
				// 更新数据显示
				adapter.notifyDataSetChanged();
			}
		});
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (bailPosition < 0) {
					// 未选择保证金产品
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							IsForexProduceChoiseActivity.this.getString(R.string.isForex_times_sign_bail_err));
					return;
				}
				Map<String, String> map = baillResultList.get(bailPosition);
				// Y:是否可签约,N-不可签约
				String isSign = map.get(IsForex.ISFOREX_ISSIGN_RES);
				if (!StringUtil.isNull(isSign) &&( ConstantGloble.ISFOREX_JCTAG_N1.equals(isSign)||ConstantGloble.ISFOREX_JCTAG_N2.equals(isSign))) {
					// 选择不可签约产品
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							IsForexProduceChoiseActivity.this.getString(R.string.isForex_times_sign_bail_no_err));
					return;
				} else if (!StringUtil.isNull(isSign) && ConstantGloble.ISFOREX_JCTAG_Y.equals(isSign)) {
					// 过滤出符合条件的借记卡账户
					BaseHttpEngine.showProgressDialog();
					requestPsnVFGFilterDebitCard();
				}
			}
		});
	}

	/** 更多按钮事件 */
	private OnClickListener goMoreClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			currentIndex += pageSize;
			refresh = false;
			recordNumber = 0;
			BaseHttpEngine.showProgressDialog();
			LogGloble.d(TAG, "goMoreClickListener======");
			PsnVFGBailProductQuery(currentIndex, pageSize, refresh);
		}
	};

	/** 查询可签约保证金产品 */
	private void PsnVFGBailProductQuery(int currentIndex, int pageSize, boolean _refresh) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGBAILPRODUCTQUERY_API);
		biiRequestBody.setConversationId(commConversationId);
		Map<String, String> map = new HashMap<String, String>();
		map.put(IsForex.ISFOREX_CURRENTINDEX_REQ, String.valueOf(currentIndex));
		map.put(IsForex.ISFOREX_PAGESIZE_REQ, String.valueOf(pageSize));
		map.put(IsForex.ISFOREX_REFRESH_REQ, String.valueOf(_refresh));
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "PsnVFGBailProductQueryCallback");
	}

	/** 查询可签约保证金产品-----回调 */
	public void PsnVFGBailProductQueryCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.isForex_times_manage_noproduce));
			return;
		}
		String number = (String) result.get(IsForex.ISFOREX_RECORDERNUMBER_RES);
		if (StringUtil.isNull(number)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.isForex_times_manage_noproduce));
			return;
		}
		recordNumber = Integer.valueOf(number);
		if (!result.containsKey(IsForex.ISFOREX_LIST) || StringUtil.isNullOrEmpty(result.get(IsForex.ISFOREX_LIST))) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.isForex_times_manage_noproduce));
			return;
		}
		List<Map<String, String>> list = (List<Map<String, String>>) result.get(IsForex.ISFOREX_LIST);
		if (list == null || list.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.isForex_times_manage_noproduce));
			return;
		}
		BaseHttpEngine.dissMissProgressDialog();
		baillResultList.addAll(list);
		adapter.dataChanged(baillResultList);
		if (currentIndex + pageSize >= recordNumber) {
			signListView.removeFooterView(load_more);
		}
	}

	/** 过滤出符合条件的借记卡账户-----回调 */
	public void requestPsnVFGFilterDebitCardCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, String>> result = (List<Map<String, String>>) biiResponseBody.getResult();
		if (result == null || result.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.isForex_times_change_noacc));
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(IsForex.ISFOREX_LIST_REQ, result);
		BaseDroidApp.getInstanse().getBizDataMap().put(IsForex.ISFOREX_LIST, baillResultList);
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent(IsForexProduceChoiseActivity.this, IsForexProduceSubmitActivity.class);
		intent.putExtra(ConstantGloble.ACC_POSITION, bailPosition);
		// startActivity(intent);
		startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_REGISTFUNCACC_CODE);// 10
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;
		default:
			break;
		}
	}
}
