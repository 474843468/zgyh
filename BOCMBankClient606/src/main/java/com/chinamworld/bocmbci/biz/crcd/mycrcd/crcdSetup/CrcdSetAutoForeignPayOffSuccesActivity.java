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
 * 我的信用卡 外币账单自动购汇  完成界面
 * 
 * @author sunh
 * 
 */
public class CrcdSetAutoForeignPayOffSuccesActivity extends CrcdBaseActivity{
	private View view;	
	/** 账户ID accountId */
	private String accountId=null;
	/** 信用卡卡号 */
	private String accountNumber=null;	
	/** 开通标示   “1000”： 开通“0000”： 关闭*/
	private String openFlag=null;
	TextView card_accountNumber;
	TextView card_foreignpayoffstatus_openflag;
	TextView tv_acc_loss_actnum;
	Button sureButton;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_password_setup));
		view = addView(R.layout.mycrcd_setautoforeignpayoff_success_layout);
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		openFlag= getIntent().getStringExtra(Crcd.CRCD_GOUHUIOPENFLAG_RES);
		card_accountNumber = (TextView) view
				.findViewById(R.id.card_accountNumber);
		card_accountNumber
				.setText(StringUtil.getForSixForString(accountNumber));
		card_foreignpayoffstatus_openflag=(TextView) view
				.findViewById(R.id.card_foreignpayoffstatus_openflag);
		tv_acc_loss_actnum=(TextView) view
				.findViewById(R.id.tv_acc_loss_actnum);
		if(openFlag.equals("1000")){
			card_foreignpayoffstatus_openflag.setText("开通");
			tv_acc_loss_actnum.setText(getResources().getString(R.string.mycrcd_setautoforeignpayoff_success_open));
			// 为界面标题赋值
			setTitle(this.getString(R.string.mycrcd_setautoforeignpayoff_open));
			}else{
				card_foreignpayoffstatus_openflag.setText("关闭");	
				tv_acc_loss_actnum.setText(getResources().getString(R.string.mycrcd_setautoforeignpayoff_success_close));
				// 为界面标题赋值
				setTitle(this.getString(R.string.mycrcd_setautoforeignpayoff_close));
			}
		
		back.setVisibility(View.GONE);
		sureButton=(Button) view.findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				setResult(RESULT_OK);
				finish();
			}
		});

	}
	
}
