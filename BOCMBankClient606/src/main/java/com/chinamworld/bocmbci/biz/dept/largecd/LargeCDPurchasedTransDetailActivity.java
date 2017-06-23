package com.chinamworld.bocmbci.biz.dept.largecd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.bii.constant.Third;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.biz.dept.adapter.LargeCDTransListAdapter;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 查询大额存单交易明细
 * @author liuh
 */
public class LargeCDPurchasedTransDetailActivity extends DeptBaseActivity {

	/** 存储查询后的数据 */
	private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
	private LinearLayout tabcontent;
	private ListView lv;
	/** ListView的更多条目 */
	private View viewFooter;
	/** 更多Button */
	private Button btnMore;
	/** 当前请求list数据的index */
	private int mCurrentIndex = 0;
	/** 总条目数 */
	private int totalCount = 0;
	private boolean isRefresh = true;
	private LargeCDTransListAdapter adapter;
	/** 签约账户 */
	private Map<String, Object> signedAcc;
	/** 已购买大额存单明细 */
	private Map<String, Object> purchasedDetail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initViews();

		if (!getSignedAcc()) {
			if (!getPurchasedDetail()) {
				isRefresh = true;
				listData.clear();
				// refreshListView(listData);
				BaseHttpEngine.showProgressDialogCanGoBack();
				requestCommConversationId();
			} else {
				finish();
			}
		} else {
			finish();
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		BaseHttpEngine.showProgressDialogCanGoBack();
		requestForTransDetailQuery();
	}

	/** PsnLargeCDTransDetailQry查询大额存单交易明细 req*/
	private void requestForTransDetailQuery() {
		if (isRefresh) {
			mCurrentIndex = 0;
		} else {
			mCurrentIndex += Third.PAGESIZE;
		}
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.PSN_LARGE_CD_TRANS_DETAIL_QRY);
		String conversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(conversationId);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Dept.ACCOUNT_ID, String.valueOf(signedAcc.get(Dept.ACCOUNT_ID)));
		map.put(Dept.CD_NUMBER, String.valueOf(purchasedDetail.get(Dept.CD_NUMBER)));
		map.put(PubConstants.PUB_QUERY_FEILD_PAGE_SIZE, String.valueOf(Third.PAGESIZE));
		map.put(PubConstants.PUB_QUERY_FEILD_CURRENT_INDEX, EpayUtil.getString(mCurrentIndex, "0"));
		map.put(PubConstants.PUB_QUERY_FEILD_IS_REFRESH, EpayUtil.getString(isRefresh, "true"));
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "transDetailQueryCallBack");
	}

	/** PsnLargeCDTransDetailQry查询大额存单交易明细 res*/
	@SuppressWarnings("unchecked")
	public void transDetailQueryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> map = (Map<String, Object>) biiResponseBody.getResult();
		String recordNumber = (String) map.get(Dept.RECORD_NUMBER);
		if (!StringUtil.isNullOrEmpty(recordNumber)) {
			totalCount = Integer.parseInt(recordNumber);
		}
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) map.get(Dept.TRANS_LIST);
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getResources().getString(R.string.no_list_data));
			listData.clear();
			refreshListView(listData);
			return;
		}
		DeptDataCenter.getInstance().setPurchasedTransDetail(map);
		for (int i = 0; i < resultList.size(); i++) {
			listData.add((Map<String, Object>) resultList.get(i));
		}
		if (listData.size() >= totalCount) {
			// btnMore.setVisibility(View.GONE);
			lv.removeFooterView(viewFooter);
		} else {
			// btnMore.setVisibility(View.VISIBLE);
			if (lv.getFooterViewsCount() > 0) {

			} else {
				lv.addFooterView(viewFooter);
			}
		}
		refreshListView(listData);
	}

	/** 列表*/
	private void refreshListView(List<Map<String, Object>> listData) {
		if (adapter == null) {
			adapter = new LargeCDTransListAdapter(this, listData);
			lv.setAdapter(adapter);
		} else {
			adapter.changeData(listData);
		}
	}

	/**
	 * 获取已购买大额存单明细
	 * @return
	 */
	private boolean getPurchasedDetail() {
		purchasedDetail = DeptDataCenter.getInstance().getPurchasedDetail();
		return StringUtil.isNullOrEmpty(purchasedDetail);
	}

	/**
	 * 获取签约账户信息
	 * @return
	 */
	private boolean getSignedAcc() {
		signedAcc = (Map<String, Object>) DeptDataCenter.getInstance().getSignedAcc();
		return StringUtil.isNullOrEmpty(signedAcc);
	}

	@SuppressLint("InflateParams")
	private void initViews() {
		setTitle(getString(R.string.large_cd_details_view));
		View view = mInflater.inflate(R.layout.large_cd_purchased_trans_detail, null);
		tabcontent = (LinearLayout) findViewById(R.id.sliding_body);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 0);
		}

		lv = (ListView) findViewById(R.id.large_cd_trans_list);
		viewFooter = View.inflate(this, R.layout.acc_load_more, null);
		// lv.addFooterView(viewFooter);
		btnMore = (Button) viewFooter.findViewById(R.id.btn_load_more);
		// btnMore.setVisibility(View.GONE);
		btnMore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isRefresh = false;
				BaseHttpEngine.showProgressDialogCanGoBack();
				requestCommConversationId();
			}
		});
	}
}