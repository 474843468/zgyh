package com.chinamworld.bocmbci.biz.safety.safetyhistorytran;

import java.util.Map;

import android.os.Bundle;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyBaseActivity;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class SafetyHoldHisDetailActivity extends SafetyBaseActivity{

	/** 控件 保险公司名称，产品名称， 产品类型 ， 产品代码，保单号， 币种, 交易金额, 交易类型， 交易日期*/
	private TextView risk_company_name, product_name, product_type, product_code,
		bill_num, currency, trade_amount, trade_type, trade_date;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.safety_histoty_detail_title));// 为界面标题赋值
		addView(R.layout.safety_hold_his_detail);// 添加布局
		initViews();
		initParamsInfo();
	}
	
	private void initViews(){
		risk_company_name = (TextView)findViewById(R.id.risk_company_name);
		product_name = (TextView)findViewById(R.id.product_name);
		product_type = (TextView)findViewById(R.id.product_type);
		product_code = (TextView)findViewById(R.id.product_code);
		bill_num = (TextView)findViewById(R.id.bill_num);
		currency = (TextView)findViewById(R.id.currency);
		trade_amount = (TextView)findViewById(R.id.trade_amount);
		trade_type = (TextView)findViewById(R.id.trade_type);
		trade_date = (TextView)findViewById(R.id.trade_date);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, risk_company_name);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, product_name);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, product_code);
	}
	
	/**
	 * 接收调用页的信息，信息完整则初始本页面
	 */
	private void initParamsInfo(){
		Map<String, Object> map = SafetyDataCenter.getInstance().getProductInfoMap();
		if(map != null){
			try {
				risk_company_name.setText(StringUtil.valueOf1((String)map.get(Safety.SAFETY_HOLD_INSU_NAME)));
				product_name.setText(StringUtil.valueOf1((String)map.get(Safety.SAFETY_HOLD_RISK_NAME)));
				product_type.setText(StringUtil.valueOf1(SafetyDataCenter.insuranceType.get(
						(String)map.get(Safety.SAFETY_HOLD_RISK_TYPE))));
				product_code.setText(StringUtil.valueOf1((String)map.get(Safety.SAFETY_HOLD_INSU_CODE)));
				bill_num.setText(StringUtil.valueOf1((String)map.get(Safety.SAFETY_HOLD_POLICY_NO)));
				currency.setText(StringUtil.valueOf1(LocalData.Currency.
						get((String)map.get(Safety.SAFETY_HOLD_CURRENCY))));
				trade_amount.setText(StringUtil.parseStringCodePattern(LocalData.Currency.
						get((String)map.get(Safety.SAFETY_HOLD_CURRENCY)), 
						(String)map.get(Safety.RISKPAEM), 2));
				trade_type.setText(StringUtil.valueOf1(SafetyDataCenter.transType.
						get((String)map.get(Safety.HISTORY_TRADE_TYPE))));
				trade_date.setText(StringUtil.valueOf1((String)map.get(Safety.SAFETY_HOLD_TRANS_DATE)));
			} catch (Exception e) {
				LogGloble.exceptionPrint(e);
			}
		}
	}
}
