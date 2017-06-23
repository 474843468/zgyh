package com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt.acctmanager.BocinvtAcctListActivity;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 转出结果页面
 * 
 * @author panwe
 * 
 */
public class TransferResultActivity extends TranBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.tran_my_trans);
		addView(R.layout.bocinvt_transfer_result);
		back.setVisibility(View.GONE);
		toprightBtn();
		setUpViews();
		setLeftSelectedPosition("tranManager_1");
	}

//	@Override
//	protected void onResume() {
//		super.onResume();
//		setLeftSelectedPosition("tranManager_1");
//	}

	@SuppressWarnings("unchecked")
	private void setUpViews() {
		((TextView) findViewById(R.id.transactionId)).setText(getIntent()
				.getStringExtra(BocInvt.BOCINVT_BUYRES_TRANSACTIONID_RES));
		Map<String, Object> acctOFmap = BociDataCenter.getInstance()
				.getAcctOFmap();
		((TextView) findViewById(R.id.main_acct)).setText(StringUtil
				.getForSixForString((String) ((Map<String, Object>) acctOFmap
						.get(BocInvt.FINANCIALACCOUNT))
						.get(BocInvt.ACCOUNTNUMBER_OF)));
		((TextView) findViewById(R.id.bank_acct)).setText(StringUtil
				.getForSixForString((String) ((Map<String, Object>) acctOFmap
						.get(BocInvt.BANKACCOUNT)).get(Comm.ACCOUNTNUMBER)));
		((TextView) findViewById(R.id.transmoney)).setText(StringUtil
				.parseStringPattern(getIntent().getStringExtra(BocInvt.AMOUNT),
						2));
		((TextView) findViewById(R.id.tradestatus))
				.setText(LocalData.tranStatusMap.get(getIntent()
						.getStringExtra(BocInvt.TRANSSTATUS)));
		String currency = getIntent().getStringExtra(
				BocInvt.BOCINVT_CANCEL_CURRENCY_REQ);
		String cashRemit = getIntent().getStringExtra(
				BocInvt.BOCINVT_CANCEL_CASHREMIT_RES);
		if (LocalData.rmbCodeList.contains(currency)) {
			// 人民币
			((TextView) findViewById(R.id.currency)).setText(LocalData.Currency
					.get(currency));
		} else {
			((TextView) findViewById(R.id.currency))
					.setText(LocalData.Currency.get(currency)
							+ LocalData.CurrencyCashremit.get(cashRemit));
		}
	}

	/**
	 * 完成操作
	 * 
	 * @param v
	 */
	public void buttonOnclick(View v) {
		int moduleFlag = TranDataCenter.getInstance().getModuleType();
		switch (moduleFlag) {
		case ConstantGloble.BOCI_TYPE:// 理财模块
			ActivityTaskManager.getInstance().removeAllActivity();
			startActivity(new Intent(this, BocinvtAcctListActivity.class));
			finish();
			break;

		default:
			ActivityTaskManager.getInstance().removeAllActivity();
			startActivity(new Intent(this, TransferManagerActivity1.class));
			finish();
			break;
		}

	}

	@Override
	public void onBackPressed() {

	}
}
