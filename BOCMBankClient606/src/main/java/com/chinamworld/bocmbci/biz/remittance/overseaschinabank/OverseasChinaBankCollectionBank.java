package com.chinamworld.bocmbci.biz.remittance.overseaschinabank;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.remittance.RemittanceBaseActivity;
import com.chinamworld.bocmbci.biz.remittance.RemittanceContent;
import com.chinamworld.bocmbci.biz.remittance.RemittanceDataCenter;
import com.chinamworld.bocmbci.biz.remittance.adapter.model.RemittanceCollectionBankItem;
import com.chinamworld.bocmbci.widget.adapter.CommonAdapter;
import com.chinamworld.bocmbci.widget.adapter.ICommonAdapter;

public class OverseasChinaBankCollectionBank extends RemittanceBaseActivity{
	
	private ListView lv_overseaschianbank;
	private List<Map<String, Object>> list ;
	
	private List<RemittanceCollectionBankItem> bankList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setLeftSelectedPosition("crossBorderRemit_4");
//		setTitle(this.getString(R.string.remittance_apply_overseas_chian_bank));
		setTitle(this.getString(R.string.remittance_apply_overseas_chian_bank));
		addView(R.layout.remittance_overseas_collectionbank);
		initView();
	}
	
	private void initView() {
		
		lv_overseaschianbank = (ListView) findViewById(R.id.lv_overseaschianbank);
		
		//list = RemittanceDataCenter.getInstance().getCollectionBankList();
		list =  RemittanceDataCenter.getInstance().getlistBySear();
		bankList = new ArrayList<RemittanceCollectionBankItem>();
		
		for (int i = 0; i < list.size(); i++) {
			String countriesRegions = (String) list.get(i).get("bocPayeeBankRegionCN");
		    String cBankName = (String) list.get(i).get("bocPayeeBankNameCN");
			String eBankName = (String) list.get(i).get("bocPayeeBankNameEN");
		    String swift = (String) list.get(i).get("bocPayeeBankSwift");
		    RemittanceCollectionBankItem item = 
		    		new RemittanceCollectionBankItem(countriesRegions, cBankName, eBankName, swift);
		    bankList.add(item);
			
		}
		
		lvadapter = new CommonAdapter<RemittanceCollectionBankItem>(OverseasChinaBankCollectionBank.this, bankList, come);
		
		lv_overseaschianbank.setAdapter(lvadapter);
		
		lv_overseaschianbank.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String sear = getIntent().getStringExtra("sear");
				Intent intent = new Intent();
				intent.putExtra("position", position);
				intent.putExtra("sear",sear);
				setResult(RemittanceContent.RESULT_CODE_COLLECTION_BANK_RESULT, intent);
				finish();
			}
		});
		
	}
	
	private ICommonAdapter<RemittanceCollectionBankItem> come = new ICommonAdapter<RemittanceCollectionBankItem>() {

		@Override
		public View getView(int arg0,
				RemittanceCollectionBankItem currentItem,
				LayoutInflater inflater, View convertView, ViewGroup viewGroup) {
			
			convertView = inflater.inflate(R.layout.remittance_overseas_collectionbank_item, null);
			
			TextView tv_region = (TextView) convertView.findViewById(R.id.tv_remittance_collectionbank_region);
			TextView tv_bankname = (TextView) convertView.findViewById(R.id.tv_remittance_collectionbank_bankname);
			TextView tv_ebankname = (TextView) convertView.findViewById(R.id.tv_remittance_collectionbank_ebankname);
			TextView tv_swift = (TextView) convertView.findViewById(R.id.tv_remittance_collectionbank_swift);
			TextView tv_note = (TextView) convertView.findViewById(R.id.tv_remittance_collectionbank_note);
			
			tv_region.setText(currentItem.countriesRegions);
			tv_bankname.setText(currentItem.cBankName);
			tv_ebankname.setText(currentItem.eBankName);
			tv_swift.setText(currentItem.swift);
			if (currentItem.countriesRegions.equals("俄罗斯")) {
				tv_note.setVisibility(View.GONE);
			} else {
				tv_note.setVisibility(View.VISIBLE);
			}
			
			return convertView;
		}
	};
	
	private CommonAdapter<RemittanceCollectionBankItem> lvadapter;
	
	

}
