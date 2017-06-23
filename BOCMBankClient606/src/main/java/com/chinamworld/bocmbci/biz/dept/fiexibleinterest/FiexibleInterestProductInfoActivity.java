package com.chinamworld.bocmbci.biz.dept.fiexibleinterest;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;

public class FiexibleInterestProductInfoActivity extends DeptBaseActivity{
	private int interestProductType = 0;
	private View view;
	private TextView tv_info;
	private LinearLayout ll_list;
	private Button btn_finish;
	@Override	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getString(R.string.dept_fiexibleinterest_product_info_tittle));
		interestProductType = getIntent().getIntExtra(
				Dept.flexibleInterest_interestProductType, 15);
		view = addView(R.layout.dept_fiexibleinterest_product_layout);
		ibRight.setVisibility(View.GONE);
		tv_info=(TextView)view.findViewById(R.id.tv_info);
		ll_list=(LinearLayout)view.findViewById(R.id.ll_list);
		if(interestProductType==15){
			tv_info.setText(Html.fromHtml(getString(R.string.dept_fiexibleinterest_product_bbk_tittle)));
			View listview = LayoutInflater.from(this).inflate(R.layout.dept_fiexibleinterest_bbk_info, null);
			ll_list.addView(listview);
		}else{
			tv_info.setText(Html.fromHtml(getString(R.string.dept_fiexibleinterest_product_enrichment_tittle)));
			View listview = LayoutInflater.from(this).inflate(R.layout.dept_fiexibleinterest_enrichment_info, null);
			ll_list.addView(listview);
		}
		btn_finish = (Button) findViewById(R.id.btn_finish);
		btn_finish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
