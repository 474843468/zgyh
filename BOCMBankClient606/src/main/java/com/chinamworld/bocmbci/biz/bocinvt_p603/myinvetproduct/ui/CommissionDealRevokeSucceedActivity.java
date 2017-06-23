package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui;

import java.io.Serializable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.CommissionDealForGeneralInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.CommissionDealForGroupInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.fragment.CommissionDealCancelForGeneralFragment;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.fragment.CommissionDealCancelForGroupFragment;

/**
 * 委托交易撤单成功
 * 
 * @author HVZHUNG
 *
 */
public class CommissionDealRevokeSucceedActivity extends BocInvtBaseActivity {

	private TextView tv_reminder;

	private Button finish;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bocinvt_commission_deal_revoke_activity_p603);
		setTitle("委托交易撤单");
		getBackgroundLayout().setLeftButtonText(null);
//		getBackgroundLayout().setRightButtonText(null);
		tv_reminder = (TextView) findViewById(R.id.tv_reminder);
//		tv_reminder.setText("您的撤单操作已成功，请牢记您的交易序号以便查询");
		tv_reminder.setText("委托撤销成功！请您记录交易序号，以便核实交易情况");
		finish = (Button) findViewById(R.id.bt_next);
		finish.setText("完成");
		Serializable sInfo = getIntent().getSerializableExtra("info");
		if (sInfo instanceof CommissionDealForGeneralInfo) {
			getSupportFragmentManager()
					.beginTransaction()
					.replace(
							R.id.container,
							CommissionDealCancelForGeneralFragment.newInstance(
									1, (CommissionDealForGeneralInfo) sInfo))
					.commit();
		} else if (sInfo instanceof CommissionDealForGroupInfo) {
			getSupportFragmentManager()
					.beginTransaction()
					.replace(
							R.id.container,
							CommissionDealCancelForGroupFragment.newInstance(1,
									(CommissionDealForGroupInfo) sInfo)).commit();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return true;
	}

	public void onClick(View v) {
		ActivityTaskManager.getInstance().removeAllSecondActivity();
		Intent intent = new Intent(this, CommissionDealQueryActivityNew.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	public static Intent getIntent(Context context, Serializable info) {
		Intent intent = new Intent(context,
				CommissionDealRevokeSucceedActivity.class);
		intent.putExtra("info", info);
		return intent;
	}
}
