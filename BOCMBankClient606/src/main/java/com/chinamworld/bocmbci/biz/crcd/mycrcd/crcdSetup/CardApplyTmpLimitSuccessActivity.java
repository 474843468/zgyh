package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 我的信用卡 临时额度调整 完成
 * 
 * @author sunh
 * 
 */
public class CardApplyTmpLimitSuccessActivity extends CrcdBaseActivity{

	private View view;	
	/** 账户ID accountId */
	private String accountId=null;
	/** 信用卡卡号 */
	private String accountNumber=null;	
	TextView card_accountNumber;
	Button sureButton;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_applytmplimit));
		view = addView(R.layout.crcd_applytmplimit_success_layout);
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		
		card_accountNumber = (TextView) view
				.findViewById(R.id.card_accountNumber);
		card_accountNumber
				.setText(StringUtil.getForSixForString(accountNumber));
		
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				setResult(RESULT_OK);
				finish();
			}
		});

	}
}
