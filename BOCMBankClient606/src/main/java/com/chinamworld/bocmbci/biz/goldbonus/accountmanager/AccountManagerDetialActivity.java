package com.chinamworld.bocmbci.biz.goldbonus.accountmanager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.GoldBonus;
import com.chinamworld.bocmbci.biz.goldbonus.GoldBonusBaseActivity;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;


public class AccountManagerDetialActivity extends GoldBonusBaseActivity{
	private TextView product_name;
	private TextView start_date;
	private TextView limit;
	private TextView end_date;
	private TextView amount;
	private TextView year_rate;
	private TextView should_premium;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_manager_detail);
//		setTitle(R.string.account_manager);
		getBackgroundLayout().setTitleNewText(R.string.account_manager);
		getBackgroundLayout().setRightButtonNewText(null);

		product_name=(TextView) findViewById(R.id.product_name);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, product_name);
		start_date=(TextView) findViewById(R.id.start_date);
		limit=(TextView) findViewById(R.id.limit);
		end_date=(TextView) findViewById(R.id.end_date);
		amount=(TextView) findViewById(R.id.amount);
		year_rate=(TextView) findViewById(R.id.year_rate);
		should_premium=(TextView) findViewById(R.id.should_premium);
		
		Intent intent=getIntent();
		product_name.setText(intent.getStringExtra(GoldBonus.ISSUENAME));
		start_date.setText(intent.getStringExtra(GoldBonus.TRADEDATE));
		//0天 1 周 2月
		if(intent.getStringExtra(GoldBonus.LIMITUNIT).equals("0")){
			limit.setText(intent.getStringExtra(GoldBonus.LINITTIME)+"天");
		}else if (intent.getStringExtra(GoldBonus.LIMITUNIT).equals("1")) {
			limit.setText(intent.getStringExtra(GoldBonus.LINITTIME)+"周");
		} else{
			limit.setText(intent.getStringExtra(GoldBonus.LINITTIME)+"个月");
		}
		
		end_date.setText(intent.getStringExtra(GoldBonus.EXPDATE));
		amount.setText(StringUtil.parseStringPattern(intent.getStringExtra(GoldBonus.TRADEWEIGHT),0)+" 克");
		year_rate.setText(paseEndZero(intent.getStringExtra(GoldBonus.ISSUERATE))+"%");
		if(Double.parseDouble(intent.getStringExtra(GoldBonus.REGBONUS))==0){
			should_premium.setText("-");
		}else {
			should_premium.setText("  "+StringUtil.parseStringPattern(intent.getStringExtra(GoldBonus.REGBONUS), 2));
		}
		
		should_premium.setTextColor(Color.RED);
		
	}

}
