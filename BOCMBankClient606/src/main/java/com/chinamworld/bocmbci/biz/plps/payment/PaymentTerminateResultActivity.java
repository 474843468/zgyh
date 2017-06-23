package com.chinamworld.bocmbci.biz.plps.payment;

import java.util.Map;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.PlpsUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 解约成功页
 * @author panwe
 *
 */
public class PaymentTerminateResultActivity extends PlpsBaseActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.plps_payment_terminate_result);
//		setTitle(PlpsDataCenter.payment[0]);
		setTitle(R.string.plps_payment_termination);
		mLeftButton.setVisibility(View.GONE);
//		setUpViews();
	}
	
	private void setUpViews(){
		int position = getIntent().getIntExtra("p", -1);
		Map<String, Object> map = PlpsDataCenter.getInstance().getSignList().get(position);
		((TextView) findViewById(R.id.serviceName)).setText((String)map.get(Plps.SERVICENAME));
		((TextView) findViewById(R.id.nickname)).setText((String)map.get(Plps.CUSTOMERALIAS));
		((TextView) findViewById(R.id.agent)).setText((String)map.get(Plps.AGENTNAME));
		((TextView) findViewById(R.id.customerName)).setText((String)map.get(Plps.CUSTNAME));
		((TextView) findViewById(R.id.payuserno)).setText((String)map.get(Plps.PAYUSERNO));
		((TextView) findViewById(R.id.signacct)).setText(StringUtil.getForSixForString((String)map.get(Plps.ACCOUNTNUMBER)));
		((TextView) findViewById(R.id.remarks)).setText((String)map.get(Plps.REMARKS));
		findViewById(R.id.btn_cancle).setVisibility(View.GONE);
		findViewById(R.id.btn_confirm).setVisibility(View.GONE);
		findViewById(R.id.btn_finish).setVisibility(View.VISIBLE);
		PlpsUtils.setOnShowAllTextListener(this, (TextView) findViewById(R.id.serviceName)
				,(TextView) findViewById(R.id.nickname),(TextView) findViewById(R.id.agent)
				,(TextView) findViewById(R.id.customerName),(TextView) findViewById(R.id.payuserno)
				,(TextView) findViewById(R.id.remarks));
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
