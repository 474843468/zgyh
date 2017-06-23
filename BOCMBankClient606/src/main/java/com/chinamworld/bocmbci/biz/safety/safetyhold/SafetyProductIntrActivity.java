package com.chinamworld.bocmbci.biz.safety.safetyhold;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyBaseActivity;
import com.chinamworld.bocmbci.biz.safety.SafetyConstant;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.utils.StringUtil;

public class SafetyProductIntrActivity extends SafetyBaseActivity {

	/** 保险公司名称， 部品名称， 产品介绍， 条款明细*/
	private TextView safety_company, product_name, safety_prod_intr, item_detail;
	/** 条款明细地址 */
	private String itemUrl;
//	private String google = "http://docs.google.com/gview?embedded=true&url=";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.safety_product_intro);
//		int padding = (int)getResources().getDimension(R.dimen.two_spinner_height);
//		((LinearLayout) this.findViewById(R.id.sliding_body)).setPadding(0, 0, 0, padding);
		setTitle(R.string.safety_hold_pro_detail_btn_prod_intr);
		goneBottomMenu();
		goneLeftView();
		setLeftTopGone();
		setRightText(getString(R.string.close));
		setRightBtnClick(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		initViews();
		setViewValues();
		getIntentData();
	}
	
	private void getIntentData(){
		Intent it = getIntent();
		if (it.getBooleanExtra(SafetyConstant.PRODUCTORSAVE, false)) {
			safety_company.setText(it.getStringExtra(Safety.SAFETY_HOLD_INSU_NAME));
			product_name.setText(it.getStringExtra(Safety.RISKNAME));
			safety_prod_intr.setText(it.getStringExtra(Safety.PROD_INFO));
		}
	}
	
	private void initViews(){
		safety_company = (TextView)findViewById(R.id.safety_company);
		product_name = (TextView)findViewById(R.id.product_name); 
		safety_prod_intr = (TextView)findViewById(R.id.safety_prod_intr);
		item_detail = (TextView)findViewById(R.id.item_detail);
		item_detail.setOnClickListener(itemDetailClick);
	}
	
	private void setViewValues(){
		Map<String, Object> map = SafetyDataCenter.getInstance().getProductInfoMap();
		Map<String, Object> info = SafetyDataCenter.getInstance().getHoldProDetail();
		if(map != null){
			safety_prod_intr.setText(StringUtil.valueOf1((String)map.get(Safety.PROD_INFO)));
			itemUrl = (String)map.get(Safety.ITEM_URL);
		}
		if(info != null){
			safety_company.setText(StringUtil.valueOf1((String)info.get(Safety.SAFETY_HOLD_INSU_NAME)));
			product_name.setText(StringUtil.valueOf1((String)info.get(Safety.SAFETY_HOLD_RISK_NAME)));
		}
	}
	
	OnClickListener itemDetailClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			productDescription(itemUrl);
		}
	};
}
