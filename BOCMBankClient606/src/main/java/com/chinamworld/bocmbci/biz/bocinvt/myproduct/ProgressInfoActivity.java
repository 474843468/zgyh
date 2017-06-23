package com.chinamworld.bocmbci.biz.bocinvt.myproduct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt.adapter.ProgressAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
/**
 * P603开始停用，使用ProductQueryAndBuyYearRateActivity
 * 收益累进信息
 * @author panwe
 *
 */
public class ProgressInfoActivity extends BociBaseActivity{
	private List<Map<String, Object>> mList = new ArrayList<Map<String,Object>>();
	private String index = ConstantGloble.LOAN_PAGESIZE_VALUE;
	private View mMainView;
	private View mFooterView;
	private ListView mListView;
	ProgressAdapter mAdapter;
	private String accountId;
	private String recordNumber;
	private String productCode;
	private String productName;
	private LinearLayout button_layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainView = addView(R.layout.bocinvt_progression);
		setTitle(R.string.progression_yearlyRR);
		tabcontent.setPadding(0, 0, 0, 0);
		getDataFromIntent();
		setUpView();
	}
	
	private void getDataFromIntent(){
		accountId = getIntent().getStringExtra(Comm.ACCOUNT_ID);
		recordNumber = getIntent().getStringExtra(BocInvt.PROGRESS_RECORDNUM);
		productCode = getIntent().getStringExtra(BocInvt.BOCI_PRODUCTCODE_REQ);
		productName = getIntent().getStringExtra(BocInvt.BOCI_DETAILPRODNAME_RES);
	}
	
	private void setUpView(){
		mFooterView = View.inflate(this, R.layout.load_more_footer, null);
		((TextView)mMainView.findViewById(R.id.product_Name)).setText(productName);
		((TextView)mMainView.findViewById(R.id.product_code)).setText(productCode);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView)mMainView.findViewById(R.id.product_Name));
		mListView = (ListView) mMainView.findViewById(R.id.progression_list);
		mList.addAll(BociDataCenter.getInstance().getProgressionList());
		addFooterView(Integer.valueOf(recordNumber));
		mAdapter = new ProgressAdapter(this, mList);
		mListView.setAdapter(mAdapter);
		button_layout= (LinearLayout) mMainView.findViewById(R.id.button_layout);
		button_layout.setVisibility(View.GONE);

	}
	
	/**
	 * 添加分页布局
	 * @param totalCount
	 */
	private void addFooterView(int totalCount) {
		if (totalCount > mList.size()) {
			if (mListView.getFooterViewsCount() <= 0) {
				mListView.addFooterView(mFooterView);
			}
		}else{
			if (mListView.getFooterViewsCount() > 0) {
				mListView.removeFooterView(mFooterView);
			}
		}
		(mFooterView.findViewById(R.id.layout_load_more)).
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BaseHttpEngine.showProgressDialog();
				requestProgress(accountId, productCode,index,false);
			}
		});
	}

	@Override
	public void progressQueryCallBack(Object resultObj) {
		super.progressQueryCallBack(resultObj);
		index = String.valueOf(Integer.valueOf(index)+Integer.valueOf(ConstantGloble.LOAN_PAGESIZE_VALUE));
		mList.addAll(BociDataCenter.getInstance().getProgressionList());
		addFooterView(Integer.valueOf(progressRecordNumber));
		mAdapter.setData(mList);
	}
}
