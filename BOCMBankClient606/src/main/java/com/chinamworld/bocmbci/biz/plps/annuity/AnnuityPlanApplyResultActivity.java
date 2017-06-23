package com.chinamworld.bocmbci.biz.plps.annuity;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.PlpsUtils;
import com.chinamworld.bocmbci.constant.LocalData;

/**
 * 变更申请成功页面
 * @author panwe
 *
 */
public class AnnuityPlanApplyResultActivity extends PlpsBaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.plps_annuity_apply_result);
		setTitle(PlpsDataCenter.annuity[2]);
		mLeftButton.setVisibility(View.GONE);
		cityAdress.setVisibility(View.GONE);
		setUpView();
	}
	
	private void setUpView(){
		Intent it = getIntent();
		Map<String, Object> map = PlpsDataCenter.getInstance().getAnnuityUserInfo();
		((TextView) findViewById(R.id.planno)).setText((String)map.get(Plps.PLANNO));
		((TextView) findViewById(R.id.username)).setText((String)map.get(Plps.NAME));
		((TextView) findViewById(R.id.idtype)).setText(LocalData.IDENTITYTYPE.get(map.get(Plps.CERTTYPE)));
		((TextView) findViewById(R.id.idnum)).setText((String)map.get(Plps.CERTNO));
		((TextView) findViewById(R.id.phone)).setText(it.getStringExtra(Plps.PHONE));
		((TextView) findViewById(R.id.mobile)).setText(it.getStringExtra(Plps.MOBILE));
		((TextView) findViewById(R.id.adress)).setText(it.getStringExtra(Plps.ADDRESS));
		((TextView) findViewById(R.id.postcode)).setText(it.getStringExtra(Plps.POSTCODE));
		((TextView) findViewById(R.id.email)).setText(it.getStringExtra(Plps.EMAIL));
		findViewById(R.id.btnfinish).setOnClickListener(this);
		PlpsUtils.setOnShowAllTextListener(this, (TextView) findViewById(R.id.planno)
				,(TextView) findViewById(R.id.idnum),(TextView) findViewById(R.id.adress)
				,(TextView) findViewById(R.id.email));
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		setResult(RESULT_OK);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
}
