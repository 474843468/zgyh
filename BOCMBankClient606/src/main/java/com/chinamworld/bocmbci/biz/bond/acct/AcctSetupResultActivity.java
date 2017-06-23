package com.chinamworld.bocmbci.biz.bond.acct;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.biz.bond.BondBaseActivity;
import com.chinamworld.bocmbci.biz.bond.BondDataCenter;
import com.chinamworld.bocmbci.biz.bond.allbond.AllBondListActivity;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 登记成功页
 * 
 * @author panwe
 * 
 */
public class AcctSetupResultActivity extends BondBaseActivity {
	/** 主布局 */
	private View mainView;
	private boolean isBuy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainView = (View) LayoutInflater.from(this).inflate(
				R.layout.bond_acctmanager, null);
		addView(mainView);
		setLeftButtonPopupGone();
		setBottomTabGone();
		btnRight.setVisibility(View.GONE);
		btnBack.setVisibility(View.GONE);
		setTitle(this.getString(R.string.bond_acct_stup_title));
		initView();
	}

	private void initView() {
		String accNum = getIntent().getStringExtra(BANKACCTNUM);
		String bondAcct = getIntent().getStringExtra(BONDACCT);
		isBuy = getIntent().getBooleanExtra(ISBUY, false);
		TextView tvTip = (TextView) mainView.findViewById(R.id.tv_bill_tip);
		tvTip.setText(R.string.bond_acct_setup_sutip);
		TextView tvBondAcct = (TextView) mainView.findViewById(R.id.tv_acc1);
		TextView tvBankAcct = (TextView) mainView.findViewById(R.id.tv_acc2);
		LinearLayout tvIdentityType = (LinearLayout) mainView
				.findViewById(R.id.layout_idtype);
		LinearLayout tvIdentityNum = (LinearLayout) mainView
				.findViewById(R.id.layout_idnum);
		tvIdentityType.setVisibility(View.GONE);
		tvIdentityNum.setVisibility(View.GONE);

		tvBankAcct.setText(StringUtil.getForSixForString(accNum));
		tvBondAcct.setText(bondAcct);

		Button btnConfirm = (Button) mainView.findViewById(R.id.btnConfirm);
		btnConfirm.setText(R.string.finish);
		btnConfirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent();
				if (!BondDataCenter.getInstance().isResetup()) {
					if (isBuy) {	// 登记成功
						setResult(RESULT_OK);
					} else {
						ActivityTaskManager.getInstance().removeAllSecondActivity();
						it.setClass(AcctSetupResultActivity.this,
								AllBondListActivity.class);
						it.putExtra(ISSUCCESS, true);
						startActivity(it);
					}
				}else {
					BondDataCenter.getInstance().finshActivity();
				}
				finish();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
}
