package com.chinamworld.bocmbci.biz.plps.annuity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.adapter.PlpsCommAdapter;
import com.chinamworld.bocmbci.biz.plps.adapter.PlpsCommAdapter.Count;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 养老金账户查询列表
 * @author panwe
 *
 */
public class AnnuityAcctInfoListActivity extends PlpsBaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.plps_annuity_acct_list);
		setTitle(R.string.plps_annuity_acctitle);
		setUpView();
	}
	
	private void setUpView(){
		String assetTotal = getIntent().getStringExtra(Plps.ASSETTOTAL);
//		((TextView) findViewById(R.id.assetTotal)).setText(Html.fromHtml("总计：<font color=\"#ba001d\">"+StringUtil.parseStringPattern(assetTotal, 2)+"</font>"));
		((TextView)findViewById(R.id.assetTotal)).setText("总计：");
		((TextView)findViewById(R.id.assetTotalt)).setText(StringUtil.parseStringPattern(assetTotal, 2));
		ListView mListView = (ListView) findViewById(R.id.listview);
		mListView.setOnItemClickListener(this);
		mListView.setAdapter(new PlpsCommAdapter(this, PlpsDataCenter.getInstance().getPlanList(), Plps.PRODUCTTYPE, Plps.JOINDATE, Plps.PLANSTATUS, Count.THREE));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		super.onItemClick(parent, view, position, id);
		startActivity(new Intent(this, AnnuityAcctInfoDetailActivity.class)
		.putExtra("p", position));
	}
}
