package com.chinamworld.bocmbci.biz.plps.annuity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.adapter.AnnuityApplResultAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
/**
 * 变更申请查询
 * @author panwe
 *
 */
public class AnnuityApplyDetailActivity extends PlpsBaseActivity{
	private ListView mListView;
	private View mFooterView;
	private String planNo;
	private String recodeNum;
	private int index = 10;
	private AnnuityApplResultAdapter mAdapter;
	private List<Map<String, Object>> mList = new ArrayList<Map<String,Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.plps_annuity_acct_tran);
		setTitle(PlpsDataCenter.annuity[3]);
		getIntentData();
		setUpViews();
	}
	
	private void getIntentData(){
		planNo = getIntent().getStringExtra(Plps.PLANNO);
		recodeNum = getIntent().getStringExtra(Plps.RECORDNUMBER);
	}
	
	private void setUpViews(){
		mFooterView = inflateView(R.layout.epay_tq_list_more);
		mListView = (ListView) findViewById(R.id.listview);
		mList.addAll(PlpsDataCenter.getInstance().getAnnuityList());
		addFooterView(recodeNum);
		mAdapter = new AnnuityApplResultAdapter(this, mList);
		mListView.setAdapter(mAdapter);
	}
	
	/**
	 * 添加更多按钮
	 * @param totalCount
	 */
	private void addFooterView(String totalCount) {
		if (Integer.valueOf(totalCount) > mList.size()) {
			if (mListView.getFooterViewsCount() <= 0) {
				mListView.addFooterView(mFooterView);
			}
			mListView.setClickable(true);
		} else {
			if (mListView.getFooterViewsCount() > 0) {
				mListView.removeFooterView(mFooterView);
			}
		}
		mFooterView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				queryAnnuity(index);
			}
		});
	}
	
	/***
	 * 查询养老金 --类型固定为 "8"
	 * @param index
	 */
	private void queryAnnuity(int index){
		BaseHttpEngine.showProgressDialog();
		String startDate = QueryDateUtils.getlastOneMonth(PlpsDataCenter.getInstance().getSysDate());
		String endDate = QueryDateUtils.getcurrentDate(PlpsDataCenter.getInstance().getSysDate());
		requestAnnuity(String.valueOf(index), planNo, startDate, endDate, "8");
	}
	
	@Override
	public void annuityCallBack(Object resultObj) {
		super.annuityCallBack(resultObj);
		index = index+Integer.valueOf(ConstantGloble.FOREX_PAGESIZE);
		mList.addAll(PlpsDataCenter.getInstance().getAnnuityList());
		addFooterView(recordNumber);
		mAdapter.setData(mList);
	}
}
