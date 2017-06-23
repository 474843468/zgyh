package com.chinamworld.bocmbci.biz.plps.payment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;

public class PaymentPhoneSuccessActivity extends PlpsBaseActivity{
	private String phoneNumber;
	private TextView phoneTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(getString(R.string.plps_modify_phone));
		cityAdress.setVisibility(View.GONE);
		mRightButton.setVisibility(View.VISIBLE);
		mLeftButton.setVisibility(View.GONE);
		inflateLayout(R.layout.plps_payment_phone_success);
		phoneNumber = getIntent().getStringExtra(Plps.PHONENUMBER);
		init();
	}
	private void init(){
		phoneTextView = (TextView)findViewById(R.id.phone_name);
		phoneTextView.setText(phoneNumber+"！");
		
	}
	public void finishOnclick(View v){
		setResult(RESULT_OK);
		finish();
	}
}
