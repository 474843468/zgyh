package com.chinamworld.bocmbci.biz.plps.annuity;

import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.utils.StringUtil;

public class AnnuityApplyDetailInfoActivity extends PlpsBaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.plps_applay_detail);
		setTitle(PlpsDataCenter.annuity[2]);
		setUpViews();
	}
	private void setUpViews(){
		Map<String, Object> map = PlpsDataCenter.getInstance().getAnnuityListDetail();
		if(!StringUtil.isNullOrEmpty(map.get(Plps.SEQNO))){
			((TextView)findViewById(R.id.servicename)).setText((String)map.get(Plps.SEQNO));
		}
		if(!StringUtil.isNullOrEmpty(map.get(Plps.BUSSINESSTYPE))){
			((TextView)findViewById(R.id.agent)).setText((String)map.get(Plps.BUSSINESSTYPE));
		}
		if(!StringUtil.isNullOrEmpty(map.get(Plps.APPLYDATE))){
			((TextView)findViewById(R.id.nickname)).setText((String)map.get(Plps.APPLYDATE));
		}
		if(!StringUtil.isNullOrEmpty(map.get(Plps.DISPOSEDATE))){
			((TextView)findViewById(R.id.customerName)).setText((String)map.get(Plps.DISPOSEDATE));
		}
		if(!StringUtil.isNullOrEmpty(map.get(Plps.RESULT))){
			((TextView)findViewById(R.id.phone)).setText((String)map.get(Plps.RESULT));
		}
		if(!StringUtil.isNullOrEmpty(map.get(Plps.EXPLAIN))){
			((TextView)findViewById(R.id.payuserno)).setText((String)map.get(Plps.EXPLAIN));
		}
		Button button = (Button)findViewById(R.id.confirm);
		
	}
	/*
	 * 完成
	 */
	public void btnOkOnclick(View v){
		setResult(RESULT_OK);
		finish();
	}
}
