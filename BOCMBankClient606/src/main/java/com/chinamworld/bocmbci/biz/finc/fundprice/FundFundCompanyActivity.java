package com.chinamworld.bocmbci.biz.finc.fundprice;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.adapter.FundCompanySpinnerAdapter;
import com.chinamworld.bocmbci.biz.finc.control.entity.FundCompany;
import com.chinamworld.bocmbci.widget.AlphabeticalView;
import com.chinamworld.bocmbci.widget.AlphabeticalView.OnAlphaChangedListener;

/***
 * 基金公司
 * @author Administrator
 *
 */
public class FundFundCompanyActivity extends FincBaseActivity implements OnAlphaChangedListener {
	
	private TextView all_company;
	private ListView mListView;
	private AlphabeticalView alphaView;
	private FundCompanySpinnerAdapter fundCompanyAdapter;
	/** 基金公司名称集合 */
	private ArrayList<FundCompany> companyList;
	private String flag ; /**是否显示全部*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		initView();
	}

	private void initData(){
		Intent current_intent = getIntent();
		companyList = current_intent.getParcelableArrayListExtra("companyList");
		flag = current_intent.getStringExtra("flag");
	}
	
	private void initView(){
		View mainView = mainInflater.inflate(R.layout.finc_company_name_spinner_lview,
				null);
		tabcontent.addView(mainView);
		tabcontent.setPadding(0, 0, 0, 0);
		findViewById(R.id.main_layout).setVisibility(View.GONE);
		
		all_company = (TextView)findViewById(R.id.all_company);
		if("all".equals(flag)){
			all_company.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.putExtra("fundCompanyPos", -1);
					setResult(RESULT_OK,intent);
					finish();
				}
			});
		}else{
			all_company.setVisibility(View.GONE);
		}
		mListView = (ListView) findViewById(R.id.lv_popup);
		alphaView = (AlphabeticalView)findViewById(R.id.alphaView);
		alphaView.setOnAlphaChangedListener(this);
		if (fundCompanyAdapter == null) {
			fundCompanyAdapter = new FundCompanySpinnerAdapter(this, companyList);
			mListView.setAdapter(fundCompanyAdapter);
		} else {
			fundCompanyAdapter.notifyDataSetChanged();
		}
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("fundCompanyPos", position);
				setResult(RESULT_OK,intent);
				finish();
			}
		});
		//返回
		findViewById(R.id.finc_company_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
	}

	@Override
	public void OnAlphaChanged(String s, int index) {
		if (s != null && s.trim().length() > 0) {
			HashMap<String, Integer> alphaIndexer = fundCompanyAdapter.getAlphaMap();
			if (alphaIndexer.get(s) != null) {
				int position = alphaIndexer.get(s);
				mListView.setSelection(position);
			}
		}
		
	}
	
//	@Override
//	public boolean onKeyDown(int keyCode,KeyEvent event){
//		switch(keyCode){
//		case KeyEvent.KEYCODE_BACK:return true;
//		}
//		return super.onKeyDown(keyCode, event);
//		}
}
