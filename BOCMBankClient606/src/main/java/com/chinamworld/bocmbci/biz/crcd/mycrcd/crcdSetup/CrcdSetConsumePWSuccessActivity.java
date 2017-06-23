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
 * 我的信用卡 消费密码设置  完成界面
 * 
 * @author sunh
 * 
 */
public class CrcdSetConsumePWSuccessActivity extends CrcdBaseActivity {
	private View view;
	/** 账户ID accountId */
	private String accountId = null;
	/** 信用卡卡号 */
	private String accountNumber = null;

	TextView card_accountNumber;
	Button sureButton;

	private String payorsearch;

	private TextView tv_acc_loss_actnum;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
//		setTitle(this.getString(R.string.mycrcd_password_setup));
		view = addView(R.layout.mycrcd_password_success_layout);
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		payorsearch= getIntent().getStringExtra("payorsearch");

		tv_acc_loss_actnum=(TextView)view.findViewById(R.id.tv_acc_loss_actnum);
		if(payorsearch.equals("search")){
			setTitle(this.getString(R.string.mycrcd_search_setup));
			tv_acc_loss_actnum.setText(getResources().getString(R.string.mycrcd_searchpassword_success));
		
		}else{
						
				setTitle(this.getString(R.string.mycrcd_password_setup));				
				tv_acc_loss_actnum.setText(getResources().getString(R.string.mycrcd_paypassword_success));
			
			
		}
		card_accountNumber = (TextView) view
				.findViewById(R.id.card_accountNumber);
		card_accountNumber
				.setText(StringUtil.getForSixForString(accountNumber));
		back = (Button) findViewById(R.id.ib_back);
		back.setVisibility(View.INVISIBLE);
		sureButton=(Button)view.findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				setResult(RESULT_OK);
				finish();
			}
		});

	}
}
