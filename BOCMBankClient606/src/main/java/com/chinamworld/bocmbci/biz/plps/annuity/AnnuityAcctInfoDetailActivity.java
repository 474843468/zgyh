package com.chinamworld.bocmbci.biz.plps.annuity;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 养老金账户详情
 * @author panwe
 *
 */
public class AnnuityAcctInfoDetailActivity extends PlpsBaseActivity{
	private String planNo;
	private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.plps_annuity_acct_detail);
		setTitle(R.string.plps_annuity_acctitle);
		getIntentData();
		setUpView();
	}
	
	/**
	 * 接受页面数据
	 */
	private void getIntentData(){
		position = getIntent().getIntExtra("p", -1);
	}
	
	/**
	 * 初始化控件
	 */
	private void setUpView(){
		Map<String, Object> map = PlpsDataCenter.getInstance().getPlanList().get(position);
		planNo = (String) map.get(Plps.PLANNO);
		if(!StringUtil.isNullOrEmpty(map.get(Plps.SEQNO))){
			((TextView) findViewById(R.id.seqno)).setText((String)map.get(Plps.SEQNO));
		}
		if(!StringUtil.isNullOrEmpty(map.get(Plps.PRODUCTTYPE))){
			((TextView) findViewById(R.id.producttype)).setText((String)map.get(Plps.PRODUCTTYPE));
		}
		if(!StringUtil.isNullOrEmpty(map.get(Plps.PLANNAME))){
			TextView planName = (TextView)findViewById(R.id.planName);
			planName.setText((String)map.get(Plps.PLANNAME));
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this, planName);
		}
//		((TextView) findViewById(R.id.planName)).setText((String)map.get(Plps.PLANNAME));
		if(!StringUtil.isNullOrEmpty(map.get(Plps.JOINDATE))){
			((TextView) findViewById(R.id.planjoindate)).setText((String)map.get(Plps.JOINDATE));
		}
		if(!StringUtil.isNullOrEmpty(map.get(Plps.PLANNO))){
			TextView planNo = (TextView)findViewById(R.id.planno);
			planNo.setText((String)map.get(Plps.PLANNO));
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this, planNo);
		}
//		((TextView) findViewById(R.id.planno)).setText((String)map.get(Plps.PLANNO));
		if(!StringUtil.isNullOrEmpty(map.get(Plps.PLANSTATUS))){
			((TextView) findViewById(R.id.planstatus)).setText(PlpsDataCenter.planStatus.get(map.get(Plps.PLANSTATUS)));
		}
		if(!StringUtil.isNullOrEmpty(map.get(Plps.PLANTYPE))){
			((TextView) findViewById(R.id.plantype)).setText(PlpsDataCenter.planType.get(map.get(Plps.PLANTYPE)));
		}
		if(!StringUtil.isNullOrEmpty(map.get(Plps.ASSETAMOUNT))){
			((TextView) findViewById(R.id.assettotal)).setText((String)map.get(Plps.ASSETAMOUNT));
		}
	}
	
	/**
	 * 交易状况查询
	 * @param v
	 */
	public void annuityAcctTranOnclick(View v){
		requestAnnuityAcctInfoList("1", planNo);
	}

	@Override
	public void annuityAcctInfoListCallBack(Object resultObj) {
		super.annuityAcctInfoListCallBack(resultObj);
		startActivity(new Intent(this, AnnuityAcctInfoTranActivity.class)
		.putExtra(Plps.RECORDNUMBER, recordNumber)
		.putExtra(Plps.PLANNO, planNo));
	}
}
