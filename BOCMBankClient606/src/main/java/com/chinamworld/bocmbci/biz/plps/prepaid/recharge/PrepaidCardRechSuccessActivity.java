package com.chinamworld.bocmbci.biz.plps.prepaid.recharge;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.Map;

/**
 * 预付卡充值成功页面
 * 
 * @author Zhi
 */
public class PrepaidCardRechSuccessActivity extends PlpsBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("预付卡充值");
		inflateLayout(R.layout.plps_prepaid_card_replenishment_success);
		mLeftButton.setVisibility(View.GONE);
		initView();
	}
	
	@SuppressWarnings("unchecked")
	private void initView() {
		Map<String, Object> map = PlpsDataCenter.getInstance().getMapPrepaidCardRechPre();
		
		((TextView) findViewById(R.id.tv_transaction)).setText((String) PlpsDataCenter.getInstance().getMapPrepaidCardQuerySupportCardType().get(Plps.TRANSACTIONID));
		((TextView) findViewById(R.id.tv_pripaidType)).setText((String) map.get(Plps.MERCHNAME));
		((TextView) findViewById(R.id.tv_pripaidCarNum)).setText((String) map.get(Plps.PREPARDQUERYNUMBER));
		String name = (String) map.get(Plps.NAME);
		if (!StringUtil.isNull(name)) {
			((TextView) findViewById(R.id.tv_name)).setText(name);
		} else {
//			findViewById(R.id.ll_name).setVisibility(View.GONE);
		}
		TextView account = (TextView)findViewById(R.id.tv_account);
		account.setText(StringUtil.getForSixForString((String) map.get(Comm.ACCOUNTNUMBER))+"（"+map.get(Comm.NICKNAME)+"）");
		((TextView) findViewById(R.id.tv_transValue)).setText((String) map.get(Plps.AMOUNT));
		
		findViewById(R.id.btnFinish).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(4);
				LogGloble.i("PrepaidCardRechSuccessActivity", "finish");
				finish();
			}
		});
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) findViewById(R.id.tv_transaction));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) findViewById(R.id.tv_pripaidCarNum));
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) findViewById(R.id.tv_account));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) findViewById(R.id.tv_transValue));
	}
}
