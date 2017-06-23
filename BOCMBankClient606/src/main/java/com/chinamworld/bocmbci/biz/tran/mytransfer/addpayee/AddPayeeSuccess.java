package com.chinamworld.bocmbci.biz.tran.mytransfer.addpayee;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;

@SuppressLint("NewApi")
public class AddPayeeSuccess extends TranBaseActivity {
	
	
	private View inflaterView;
	private LinearLayout laccBankName;
	private LinearLayout laccBankNameNew;
	String name;
	String num;
	String mobile;
	String bankname;
	String banknamenew;
	
	TextView tname;
	TextView tnum;
	TextView tmobile;
	TextView tbankname;
	TextView tbanknamenew;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.tran_addNewPayee_head));
		inflaterView = mInflater.inflate(
				R.layout.addpayee_success_info, null);
		tabcontent.removeAllViews();
		tabcontent.addView(inflaterView);
		back.setVisibility(View.INVISIBLE);
		mTopRightBtn.setText(this.getString(R.string.close));
		mTopRightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(105);
				finish();
			}
		});
		
		inflaterView.findViewById(R.id.finish).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(105);
				finish();
			}
		});
		
		init();
		
		name=getIntent().getStringExtra(Tran.BOCADDPAYEE_PAYEENAME_REQ);
		num=getIntent().getStringExtra(Tran.SAVEEBPS_PAYEEACTNO_REQ);
		mobile=getIntent().getStringExtra(Tran.SAVEEBPS_PAYEEMOBILE_REQ);
		bankname=getIntent().getStringExtra(Tran.DIR_ADDPAYEE_BANKNAME_RES);
		banknamenew=getIntent().getStringExtra(Tran.ADD_NEW_PAYEE_ORG_NAME);
		tname.setText(name);
		tnum.setText(num);
		tmobile.setText(mobile);
		if(!banknamenew.isEmpty()){
			laccBankName.setVisibility(View.VISIBLE);
			laccBankNameNew.setVisibility(View.VISIBLE);
			tbankname.setText(bankname);
			tbanknamenew.setText(banknamenew);
		}else if(!bankname.isEmpty()&&banknamenew.isEmpty()){
			laccBankName.setVisibility(View.VISIBLE);
			tbankname.setText(bankname);
		}
		
	}
	private void init() {
		laccBankName=(LinearLayout) inflaterView.findViewById(R.id.accBankName);
		laccBankNameNew=(LinearLayout) inflaterView.findViewById(R.id.accBankNameNew);
		tname=(TextView)inflaterView.findViewById(R.id.name);
		tnum=(TextView)inflaterView.findViewById(R.id.num);
		tmobile=(TextView)inflaterView.findViewById(R.id.mobile);
		tbankname=(TextView) inflaterView.findViewById(R.id.accinbankname);
		tbanknamenew=(TextView) inflaterView.findViewById(R.id.accinbanknamenew);
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				AddPayeeSuccess.this, tbanknamenew);
//		tname.setText(name);
//		tnum.setText(num);
//		tmobile.setText(mobile);
		
	}
	

}
