package com.chinamworld.bocmbci.biz.goldbonus.accountmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.GoldBonus;
import com.chinamworld.bocmbci.biz.goldbonus.GoldBonusBaseActivity;
import com.chinamworld.bocmbci.biz.goldbonus.GoldbonusLocalData;
import com.chinamworld.bocmbci.biz.goldbonus.busitrade.BusiTradeAvtivity;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.List;
import java.util.Map;


public class GoldbounsRegisterSusccessActivity extends GoldBonusBaseActivity {
		
	private Button bremit_confirm_info_ok;
	private TextView bremit_acc;
	private TextView money_type;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		GoldbonusLocalData.getInstance().psnGoldBonusSignInfoQuery.put(GoldBonus.LINKACCTFLAG, "1");
		if(getIntent().getStringExtra("isFirst").equals("2")){
//			setTitle(R.string.main_menu32);
			getBackgroundLayout().setTitleNewText(R.string.main_menu32);
		}else {
//			setTitle(R.string.goldbonus_account_manager);
			getBackgroundLayout().setTitleNewText(R.string.goldbonus_account_manager);

		}
		getBackgroundLayout().setRightButtonNewText("主界面");
		setContentView(R.layout.goldbouns_register_success_info);
		if(getIntent().getStringExtra("isFirst").equals("2")){
			((LinearLayout)findViewById(R.id.phonenumber_layout)).setVisibility(View.VISIBLE);
		}else {
			((LinearLayout)findViewById(R.id.phonenumber_layout)).setVisibility(View.GONE);
		}
		
		
		//返回按钮去掉
		Button back = (Button) findViewById(R.id.ib_back);
		back.setVisibility(View.GONE);
		getBackgroundLayout().setLeftButtomNewVisibility(View.GONE);
		bremit_confirm_info_ok=(Button) findViewById(R.id.bremit_confirm_info_ok);
		//"1"是变更账户
		if(getIntent().getStringExtra("isFirst").equals("2")){
			bremit_confirm_info_ok.setText("我要交易");
			
		}else {
			bremit_confirm_info_ok.setText("完成");
		}
		((TextView) findViewById(R.id.phone_number)).setText(GoldbonusLocalData
				.getInstance().phoneNumber);
		bremit_acc=(TextView) findViewById(R.id.bremit_acc);
		money_type=(TextView) findViewById(R.id.money_type);
		bremit_acc.setText(StringUtil.getForSixForString(getIntent().getStringExtra("accountNumber")));
		money_type.setText(GoldbonusLocalData.accountType.get(getIntent().getStringExtra("accountType")));
		bremit_confirm_info_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//再掉一次是否签约的接口，查看一下状态，状态满足进行交易
				getHttpTools().requestHttp(GoldBonus.PSNGOLDBONUSSIGNINFOQUERY,
						"requestPsnGoldBonusSignInfoQueryCallBack", null, false);
			}
		});
	}
	public void requestPsnGoldBonusSignInfoQueryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		GoldbonusLocalData.getInstance().psnGoldBonusSignInfoQuery = resultMap;
		String statueString = (String) resultMap.get(GoldBonus.LINKACCTFLAG);
		if (statueString.equals(0)) {
			// 状态为“0”未签约 跳转到账户选择界面

		} else if (statueString.equals(2)) {
			// 状态为“2”已签约未关联 跳转到统一界面
			Intent intent = new Intent(GoldbounsRegisterSusccessActivity.this, GoldbounsReminderActivity.class);
			startActivity(intent);

		} else {
			//状态为“1”跳入买入界面
			Intent intent = new Intent();
			//"1"是变更账户
			if(getIntent().getStringExtra("isFirst").equals("2")){
				bremit_confirm_info_ok.setText("我要交易");
				intent.setClass(GoldbounsRegisterSusccessActivity.this,
						BusiTradeAvtivity.class);
			}else {
				bremit_confirm_info_ok.setText("完成");
				intent.setClass(GoldbounsRegisterSusccessActivity.this,
						AccountManagerMainActivity.class);
			}
			startActivity(intent);
			}
		}

}
