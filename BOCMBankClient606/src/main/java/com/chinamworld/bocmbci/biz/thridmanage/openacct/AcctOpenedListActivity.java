package com.chinamworld.bocmbci.biz.thridmanage.openacct;

import java.util.ArrayList;
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
import android.widget.AdapterView.OnItemSelectedListener;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Third;
import com.chinamworld.bocmbci.biz.thridmanage.ThirdDataCenter;
import com.chinamworld.bocmbci.biz.thridmanage.ThirdManagerBaseActivity;
import com.chinamworld.bocmbci.biz.thridmanage.openacct.adapter.AccOpenCardAdapter;
import com.chinamworld.bocmbci.biz.thridmanage.openacct.adapter.AccOpenedAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.widget.CustomGallery;
import com.chinamworld.bocmbci.widget.LoadMoreListView;
import com.chinamworld.bocmbci.widget.LoadMoreListView.OnLoadMoreListener;
import com.chinamworld.bocmbci.widget.LoadMoreListView.Status;

/***
 * 预约开户历史查询列表
 * 
 * @author panwe
 * 
 */
public class AcctOpenedListActivity extends ThirdManagerBaseActivity {

	/** 主布局 **/
	private View viewContent;
	/** 查询结果 */
	private LoadMoreListView mResultListView;
	/** 账户列表 */
	private CustomGallery mAccountGalleryView;
	/** 列表信息 */
	private List<Map<String, Object>> mResultData = new ArrayList<Map<String, Object>>();
	private AccOpenedAdapter mAdapter;

	private int mIntentPosition;
	/** 请求参数 分页 */
	private int mCurrentPagIndexParams;
	/** 账户列表 */
	private List<Map<String, Object>> mAccountList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getIntentData()) {
			// 添加布局
			viewContent = LayoutInflater.from(this).inflate(R.layout.third_openacc_query, null);
			addView(viewContent);
			if (findViewById(R.id.sliding_body) != null) {
				findViewById(R.id.sliding_body).setPadding(0, 0, 0, 0);
			}
			setTitle(R.string.third_openacc_query);
			initData();
			findView();

			// 获取会话id
			BaseHttpEngine.showProgressDialogCanGoBack();
			requestCommConversationId();
		}
	}

	/** 账户选择事件 */
	private OnItemSelectedListener galleryListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
			// 设置账户列表箭头状态
			View arrowLeft = findViewById(R.id.acc_frame_left);
			View arrowRight = findViewById(R.id.acc_frame_right);
			if (position == 0) {
				arrowLeft.setVisibility(View.INVISIBLE);
				if (mAccountList.size() == 1) {
					arrowRight.setVisibility(View.INVISIBLE);
				} else {
					arrowRight.setVisibility(View.VISIBLE);
				}
			} else if (position == mAccountList.size() - 1) {
				arrowLeft.setVisibility(View.VISIBLE);
				arrowRight.setVisibility(View.INVISIBLE);
			} else {
				arrowLeft.setVisibility(View.VISIBLE);
				arrowRight.setVisibility(View.VISIBLE);
			}

			getOpenedInfoList(true);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};

	private OnLoadMoreListener loadMoreListener = new OnLoadMoreListener() {
		@Override
		public void onLoadMore(Status status) {
			if (status == Status.LOADING) {
				getOpenedInfoList(false);
			}
		}
	};

	private void findView() {
		// 右上角按钮赋值
		setTitleRightText(getString(R.string.go_main));
		setRightBtnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goMainActivity();
			}
		});

		mAccountGalleryView = (CustomGallery) findViewById(R.id.galy_account);
		mResultListView = (LoadMoreListView) findViewById(R.id.lv_result_list);
		mResultListView.setOnLoadMoreListener(loadMoreListener);
		viewContent.setVisibility(View.INVISIBLE);
	}

	private boolean getIntentData() {
		mIntentPosition = getIntent().getIntExtra("position", 0);
		return true;
	}

	private void setAccountView() {
		// FinanceIcTransGalleryAdapter adapter = new
		// FinanceIcTransGalleryAdapter(this, mAccountList);
		AccOpenCardAdapter adapter = new AccOpenCardAdapter(this, mAccountList);
		mAccountGalleryView.setOnItemSelectedListener(galleryListener);
		mAccountGalleryView.setAdapter(adapter);
		mAccountGalleryView.setSelection(mIntentPosition);
	}

	private void initData() {
		mAccountList = ThirdDataCenter.getInstance().getBankAccountList();
	}

	/** 列表点击事件 **/
	private OnItemClickListener itemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Map<String, Object> map = (Map<String, Object>) mAdapter.getItem(position);
			if (map != null) {
				Intent it = new Intent(AcctOpenedListActivity.this, AcctOpenedInfoActivity.class);
				AcctOpenedInfoActivity.dataMap = map;
				AcctOpenedListActivity.this.startActivity(it);
			}
		}
	};

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		setAccountView();
		viewContent.setVisibility(View.VISIBLE);
	}

	/** 开户历史列表信息 **/
	private void getOpenedInfoList(boolean isRefresh) {
		if (isRefresh) {
			initResultView();
			mCurrentPagIndexParams = 0;
		} else {
			mCurrentPagIndexParams += Third.PAGESIZE;
		}
		// 通讯开始,展示通讯框
		int itemPosition = mAccountGalleryView.getSelectedItemPosition();
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Third.METHOD_OPENACC_HIQUERY_LIST);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Third.PLATFORACC_LIST_ACCID, mAccountList.get(itemPosition).get(Comm.ACCOUNT_ID));
		params.put(Third.QUERY_CURPAG, mCurrentPagIndexParams);
		params.put(Third.QUERY_PAGSIZE, Third.PAGESIZE);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "openedInfoListCallBack");
	}

	/**
	 * 开户信息返回处理
	 * 
	 * @param resultObj
	 */
	public void openedInfoListCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> map = (Map<String, Object>) (biiResponseBody.getResult());
		int totalNum = Integer.parseInt((String) map.get(Third.QUERY_RECODNUM));
		List<Map<String, Object>> list = (List<Map<String, Object>>) map.get(Third.QUERY_DATALIST);
		if (list != null) {
			mResultData.addAll(list);
		}

		// 空提示
		if (mResultData.isEmpty()) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.third_common_error));
			return;
		}
		if (totalNum > mResultData.size()) {
			mResultListView.showLoadMoreView();
		} else {
			mResultListView.hideLoadMoreView();
		}

		if (mAdapter == null) {
			mAdapter = new AccOpenedAdapter(this, mResultData);
			mResultListView.setAdapter(mAdapter);
			mResultListView.setOnItemClickListener(itemClick);
		} else {
			mAdapter.setData(mResultData);
			mAdapter.notifyDataSetChanged();
		}
	}

	private void initResultView() {
		mResultListView.hideLoadMoreView();
		mResultData.clear();
		if (mAdapter != null) {
			mAdapter.setData(mResultData);
			mAdapter.notifyDataSetChanged();
		}
	}
}
