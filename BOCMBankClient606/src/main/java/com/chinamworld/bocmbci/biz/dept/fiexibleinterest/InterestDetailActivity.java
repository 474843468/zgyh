package com.chinamworld.bocmbci.biz.dept.fiexibleinterest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.biz.dept.adapter.InterestDetailAdapter;

public class InterestDetailActivity extends DeptBaseActivity {
	private List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
	private View view;
	private TextView dept_holdtime;
	private int interestProductType = 0;
	private ListView mListView;
	private InterestDetailAdapter mAdapter;
	private Button btnconfirm;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getString(R.string.dept_interestdetail_tittle));
		view = addView(R.layout.dept_interestdetail_layout);
		interestProductType = getIntent().getIntExtra(
				Dept.flexibleInterest_interestProductType, 15);
		dept_holdtime = (TextView) view.findViewById(R.id.dept_holdtime);
		if (interestProductType == 15) {
			dept_holdtime.setText(getString(R.string.dept_interestdetail_days));
			mList.addAll((List<Map<String, Object>>) DeptDataCenter
					.getInstance().getBbkMap().get("interestDetail"));
			
		} else {
			dept_holdtime
					.setText(getString(R.string.dept_interestdetail_account));
			mList.addAll((List<Map<String, Object>>) DeptDataCenter
					.getInstance().getEnrichmentMap().get("interestDetail"));
		
		}
		mList.add(0,mList.get(0));
		mListView = (ListView) view.findViewById(R.id.progression_list);
		mAdapter = new InterestDetailAdapter(this, mList, interestProductType);
		mListView.setAdapter(mAdapter);
		btnconfirm = (Button) view.findViewById(R.id.btnconfirm);
		btnconfirm.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();

			}
		});
	}
}
