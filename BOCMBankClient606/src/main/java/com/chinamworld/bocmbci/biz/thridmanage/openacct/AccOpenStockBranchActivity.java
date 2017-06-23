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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Third;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.widget.LoadMoreListView;
import com.chinamworld.bocmbci.widget.LoadMoreListView.OnLoadMoreListener;
import com.chinamworld.bocmbci.widget.LoadMoreListView.Status;

/**
 * 开户营业部
 * 
 * @author panwe
 */
public class AccOpenStockBranchActivity extends BaseActivity {

	/** 空结果码 */
	public static final int EMPTY_RESULTCODE = 100;
	/** 主界面 */
	private LinearLayout mainLayout;
	/** 子界面 */
	private View viewContent;
	private LoadMoreListView mStockBranView;
	private SimpleAdapter mAdapter;
	/** 列表 */
	private List<Map<String, String>> stockBranList = null;
	/** 关闭按钮 */
	private Button btnClose;
	/** 地区 */
	private String province;
	/** 公司代码 */
	private String companyCode;
	private int mCurrentIndex = -1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.biz_activity_layout);
		setContentView(R.layout.biz_activity_606_layout);

		viewContent = LayoutInflater.from(this).inflate(R.layout.third_stockbran_list, null);
		// 为界面标题赋值
		setTitle(this.getString(R.string.third_openacc_company_department));
		// 隐藏左侧展示按钮、底部table
		invisible();
		// 初始化弹窗按钮
		initPulldownBtn();
		findView();
		initData();
		BaseHttpEngine.showProgressDialogCanGoBack();
		requestCommConversationId();
	}

	private void initData() {
		stockBranList = new ArrayList<Map<String, String>>();
	}

	// conversationid回调
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 获取营业部信息
		requestStockBranchInfo(true);
	}

	private void findView() {
		province = getIntent().getStringExtra("PROVICE");
		companyCode = getIntent().getStringExtra("COMPANY");
		mainLayout = (LinearLayout) this.findViewById(R.id.sliding_body);
		mainLayout.addView(viewContent);
		Button btnBack = (Button) findViewById(R.id.ib_back);
		btnBack.setVisibility(View.GONE);
		btnClose = (Button) findViewById(R.id.ib_top_right_btn);
		btnClose.setVisibility(View.VISIBLE);
		btnClose.setText(this.getString(R.string.close));
		btnClose.setOnClickListener(closeClick);

		mStockBranView = (LoadMoreListView) viewContent.findViewById(R.id.lv_stockbran);
		mStockBranView.setOnItemClickListener(itemClick);
		mStockBranView.setOnLoadMoreListener(loadMoreListener);
	}

	/** 加载更多 */
	private OnLoadMoreListener loadMoreListener = new OnLoadMoreListener() {
		@Override
		public void onLoadMore(Status status) {
			if (status == Status.LOADING) {
				requestStockBranchInfo(false);
			}
		}
	};

	// 获取营业部信息
	private void requestStockBranchInfo(boolean isRefresh) {
		if (isRefresh) {
			mCurrentIndex = 0;
		} else {
			mCurrentIndex += Third.PAGESIZE;
		}

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Third.METHOD_OPENACC_STOCKBRANCH);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Third.OPENACC_STOCKBRANCH_ADR, province);
		params.put(Third.OPENACC_STOCKBRANCH_ID, companyCode);
		params.put(Third.QUERY_CURPAG, String.valueOf(mCurrentIndex));
		params.put(Third.QUERY_PAGSIZE, String.valueOf(Third.PAGESIZE));
		biiRequestBody.setParams(params);
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "getStockBranchInfoCallBack");
	}

	/*** 营业部信息 返回 */
	public void getStockBranchInfoCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> map = (Map<String, Object>) (biiResponseBody.getResult());
		if (map == null) {
			emptyExit();
			return;
		}
		int recordNum = Integer.valueOf((String) map.get(Third.QUERY_RECODNUM));
		List<Map<String, String>> list = (List<Map<String, String>>) map.get(Third.QUERY_DATALIST);
		if (list == null) {
			emptyExit();
			return;
		}

		stockBranList.addAll(list);

		if (stockBranList.isEmpty()) {
			emptyExit();
			return;
		}

		if (mAdapter == null) {
			mAdapter = new SimpleAdapter(this, stockBranList, R.layout.third_province_item,
					new String[] { Third.OPENACC_STOVK_NAME }, new int[] { R.id.blpt_province_name });
			mStockBranView.setAdapter(mAdapter);
		} else {
			mAdapter.notifyDataSetChanged();
		}

		if (recordNum > stockBranList.size()) {
			mStockBranView.showLoadMoreView();
		} else {
			mStockBranView.hideLoadMoreView();
		}
	}

	/** List点击事件 */
	private OnItemClickListener itemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent it = new Intent();
			it.putExtra("data", (String) stockBranList.get(position).get(Third.OPENACC_STOVK_NAME));
			it.putExtra("code", (String) stockBranList.get(position).get(Third.DEPARTMENT_ID));
			setResult(RESULT_OK, it);
			finish();
		}
	};

	/** 隐藏侧面和底部按钮 */
	private void invisible() {
		Button btnhide = (Button) findViewById(R.id.btn_show);
		View view = findViewById(R.id.menu_popwindow);
		btnhide.setVisibility(View.GONE);
		view.setVisibility(View.GONE);
	}

	/** 返回主页Listener */
	private OnClickListener closeClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};

	private void emptyExit() {
		setResult(EMPTY_RESULTCODE);
		finish();
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	}
}
