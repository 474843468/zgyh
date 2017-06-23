package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.CommissionDealForGeneralInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.CommissionDealForGroupInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.ICommissionDealInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.fragment.CommissionDealDetailForGeneralFragment;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.fragment.CommissionDealDetailForGroupFragment;
import com.chinamworld.bocmbci.log.LogGloble;

/***
 * 委托交易详情
 * 
 * @author HVZHUNG
 *
 */
public class CommissionDealDetailActivity extends BocInvtBaseActivity {
	private static final String TAG = CommissionDealDetailActivity.class
			.getSimpleName();
	private String ibknum;
	private String typeOfAccount;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bocinvt_commission_deal_detail_activity_p603);
		setTitle("委托交易详情");
		//getBackgroundLayout().setRightButtonText(null);
		ICommissionDealInfo info = (ICommissionDealInfo) getIntent()
				.getSerializableExtra("info");
		ibknum=getIntent().getStringExtra("ibknum");
		typeOfAccount=getIntent().getStringExtra("typeOfAccount");
		if (info == null) {
			LogGloble.e(TAG, "intent have not extra for the key info");
			return;
		}
		Fragment f = null;
		if (info instanceof CommissionDealForGeneralInfo) {
			f = CommissionDealDetailForGeneralFragment
					.newInstance((CommissionDealForGeneralInfo) info);
		} else if (info instanceof CommissionDealForGroupInfo) {
//			f = CommissionDealDetailForGroupFragment
//					.newInstance((CommissionDealForGroupInfo) info);
			f = CommissionDealDetailForGroupFragment
					.newInstance((CommissionDealForGroupInfo) info,ibknum,typeOfAccount);	
			
		}
		if (f == null)
			return;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, f).commit();
	}
}
