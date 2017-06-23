package com.chinamworld.bocmbci.biz.bond.mybond;

import java.util.Map;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.bii.constant.Bond;
import com.chinamworld.bocmbci.biz.bond.BondBaseActivity;
import com.chinamworld.bocmbci.biz.bond.BondDataCenter;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 托管账户注销/查询密码重置-成功页
 * 
 * @author panwe
 * 
 */
public class BondAcctManagerResultActivity extends BondBaseActivity {
	/** 主布局 */
	private View mainView;
	/** 标识注销/密码重置 */
	private boolean isCanceAcct;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 添加布局
		mainView = (View) LayoutInflater.from(this).inflate(
				R.layout.bond_acctmanager, null);
		addView(mainView);
		// 隐藏左侧菜单
		setLeftButtonPopupGone();
		// 隐藏底部tab
		setBottomTabGone();
		btnRight.setVisibility(View.GONE);
		btnBack.setVisibility(View.GONE);
		init();
	}

	private void init() {
		isCanceAcct = getIntent().getBooleanExtra(ISCANCEACCT, false);
		TextView tvTip = (TextView) mainView.findViewById(R.id.tv_bill_tip);
		String title = null;
		String tip = null;
		if (isCanceAcct) {
			setLeftButtonPopupGone();
			title = this.getString(R.string.bond_acct_cance_title);
			tip = this.getString(R.string.bond_acct_cance_sutip);
		} else {
			title = this.getString(R.string.bond_acct_pasreset);
			tip = this.getString(R.string.bond_acct_pasresetresult_tip);
			TextView tvtip = (TextView) mainView
					.findViewById(R.id.tv_bottom_tip);
			tvtip.setVisibility(View.VISIBLE);
		}
		setTitle(title);
		tvTip.setText(tip);

		TextView tvBondAcct = (TextView) mainView.findViewById(R.id.tv_acc1);
		TextView tvBankAcct = (TextView) mainView.findViewById(R.id.tv_acc2);
		TextView tvIdentityType = (TextView) mainView
				.findViewById(R.id.tv_idtype);
		TextView tvIdentityNum = (TextView) mainView
				.findViewById(R.id.tv_idnum);

		LinearLayout layoutBank = (LinearLayout) mainView
				.findViewById(R.id.layout_bank);

		Button btnFinish = (Button) mainView.findViewById(R.id.btnConfirm);
		btnFinish.setText(this.getString(R.string.finish));
		btnFinish.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				BondDataCenter.getInstance().finshActivity();
				if (isCanceAcct) {
					BondDataCenter.getInstance().clearAllData();
					ActivityTaskManager.getInstance().removeAllSecondActivity();
				}
				finish();
			}
		});

		Map<String, Object> customInfoMap = BondDataCenter.getInstance()
				.getCustomInfoMap();
		tvBondAcct
				.setText((String) customInfoMap.get(Bond.BOND_RESULT_BONDACC));
		if (isCanceAcct) {
			tvBankAcct.setText(StringUtil
					.getForSixForString((String) customInfoMap
							.get(Bond.CUSTOMER_BANKNUM)));
		} else {
			layoutBank.setVisibility(View.GONE);
		}
		tvIdentityType.setText(BondDataCenter.identityType.get(customInfoMap
				.get(Bond.IDENTITYPE)));
		tvIdentityNum.setText((String) customInfoMap.get(Bond.IDENTINUM));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}

}
