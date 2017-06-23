package com.chinamworld.bocmbci.biz.servicerecord;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class ServiceRecordHistoryDetailsActivity extends ServiceRecordBaseActivity {

	/** 处理状态*/
	private TextView fincn_fundTaAccNum_tv;
	/** 业务类型*/
	private TextView finc_dealDate_tv;
	/** 日期*/
	private TextView finc_tradetype_tv;
	/** 交易渠道*/
	private TextView finc_fundstate_tv;
	/** 币种*/
	private TextView fincn_regfundCommpanyName_tv;
	/** 金额*/
	private TextView fincn_fail_reason_tv;
	/** 概要*/
	private TextView fincn_appoint_trade_date_tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.service_recor_query_history_details);
		View childView = mainInflater.inflate(
				R.layout.service_recor_query_history_details, null);
		tabcontent.addView(childView);
		setTitle(R.string.servicerecord_query_history);
//		right.setVisibility(View.GONE);
		
		
		
		fincn_fundTaAccNum_tv=(TextView) findViewById(R.id.fincn_fundTaAccNum_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fincn_fundTaAccNum_tv);
		finc_dealDate_tv=(TextView) findViewById(R.id.finc_dealDate_tv);
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_dealDate_tv);
		finc_tradetype_tv=(TextView) findViewById(R.id.finc_tradetype_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_tradetype_tv);
		finc_fundstate_tv=(TextView) findViewById(R.id.finc_fundstate_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_fundstate_tv);
		
		fincn_regfundCommpanyName_tv=(TextView) findViewById(R.id.fincn_regfundCommpanyName_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fincn_regfundCommpanyName_tv);
		
		fincn_fail_reason_tv=(TextView) findViewById(R.id.fincn_fail_reason_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fincn_fail_reason_tv);
		fincn_appoint_trade_date_tv=(TextView) findViewById(R.id.fincn_appoint_trade_date_tv);
		Intent intent=getIntent();
		
		
		fincn_fundTaAccNum_tv.setText(LocalData.service_type.get(intent.getStringExtra("TransStatus")));
		//业务种类没有返现
		finc_dealDate_tv.setText(LocalData.service_record.get(intent.getStringExtra("SvrRecType")));
		
		
		finc_tradetype_tv.setText(intent.getStringExtra("CreatTime"));
		
		finc_fundstate_tv.setText(LocalData.service_channle.get(intent.getStringExtra("Channel")));
//		if((intent.getStringExtra("CurrencyCode").equals(""))){
//			fincn_regfundCommpanyName_tv.setText("-");
//		}else {
			fincn_regfundCommpanyName_tv.setText(StringUtil.valueOf1(LocalData.Currency.get(intent.getStringExtra("CurrencyCode"))));
//		}
		
		fincn_fail_reason_tv.setText(
						intent.getStringExtra("Amount"));
		fincn_appoint_trade_date_tv.setText((intent.getStringExtra("Summary")));
	}

		
	
	
}
