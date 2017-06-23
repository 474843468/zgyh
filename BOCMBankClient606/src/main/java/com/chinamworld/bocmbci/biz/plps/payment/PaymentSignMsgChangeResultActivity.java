package com.chinamworld.bocmbci.biz.plps.payment;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 信息修改成功页面
 * @author panwe
 *
 */
public class PaymentSignMsgChangeResultActivity extends PlpsBaseActivity{
	private int position;
	private String customerAlias;
	private String accountNumber;
	private String nickName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.plps_payment_msgchange_result);
//		setTitle(PlpsDataCenter.payment[0]);
		setTitle(R.string.plps_payment_info_modify);
		mLeftButton.setVisibility(View.GONE);
		getIntentData();
		setUpViews();
	}
	
	private void getIntentData(){
		Intent intent = getIntent();
		position = intent.getIntExtra("p", -1);
		customerAlias = intent.getStringExtra(Plps.CUSTOMERALIAS);
		accountNumber = intent.getStringExtra(Plps.ACCOUNTNUMBER);
		nickName = intent.getStringExtra(Comm.NICKNAME);
	}
	
	private void setUpViews(){
		Map<String, Object> map = PlpsDataCenter.getInstance().getSignList().get(position);
		((TextView) findViewById(R.id.agent)).setText((String)map.get(Plps.AGENTNAME));
		((TextView) findViewById(R.id.customerName)).setText((String)map.get(Plps.CUSTNAME));
		((TextView) findViewById(R.id.payuserno)).setText((String)map.get(Plps.PAYUSERNO));
		((TextView) findViewById(R.id.remarks)).setText((String)map.get(Plps.REMARKS));
		if(!StringUtil.isNullOrEmpty(customerAlias)){
			((TextView) findViewById(R.id.nickname)).setText(customerAlias);
		}
		((TextView) findViewById(R.id.statue)).setText(PlpsDataCenter.signType.get(map.get(Plps.SIGNTYPE)));
		TextView signNacct = (TextView)findViewById(R.id.signacct);
		signNacct.setText(nickName+StringUtil.getForSixForString(accountNumber));
//		((TextView) findViewById(R.id.signacct)).setText(nickName+StringUtil.getForSixForString(accountNumber));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,signNacct);
	}
	
	/**
	 * 完成
	 * @param v
	 */
	public void btnFinishOnclick(View v){
		setResult(RESULT_OK);
		finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}

}
