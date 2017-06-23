package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;

/**
 * 主动还款确认
 * 
 * @author huangyuchao
 * 
 */
public class CrcdpaymentSetupAutoSuccessActivity extends CrcdBaseActivity {

	private View view;

	TextView mycrcd_renmi_account, mycrcd_foreign_huan_type;

	Button sureButton;
	private View benBiView = null;
	private View WaiBiView = null;
	/**是否显示人民币*/
    private Boolean isShowRmb=false;
    /**是否显示外币*/
    private Boolean isShowWb=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_creditcard_paymentstatusclose));
		view = addView(R.layout.crcd_payment_setup_auto_success);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		isShowRmb = getIntent().getBooleanExtra(Crcd.CRCD_LOCALCURRENCYPAYMENT, false);
		isShowWb = getIntent().getBooleanExtra(Crcd.CRCD_FOREIGNCURRENCYPAYMENT,false);
		back.setVisibility(View.GONE);

		init();
	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

	/** 初始化界面 */
	private void init() {
		benBiView = findViewById(R.id.benbi_layout);
		WaiBiView = findViewById(R.id.waibi_layout);
		mycrcd_renmi_account = (TextView) findViewById(R.id.mycrcd_renmi_account);
		mycrcd_foreign_huan_type = (TextView) findViewById(R.id.mycrcd_foreign_huan_type);

		mycrcd_renmi_account.setText(getString(R.string.mycrcd_myself_huanmoney));
		mycrcd_foreign_huan_type.setText(getString(R.string.mycrcd_myself_huanmoney));
		if (!isShowWb) {
			WaiBiView.setVisibility(View.GONE);
		} else {
			WaiBiView.setVisibility(View.VISIBLE);
		}
		if (!isShowRmb) {
			benBiView.setVisibility(View.GONE);
		} else {
			benBiView.setVisibility(View.VISIBLE);
		}
		sureButton = (Button) view.findViewById(R.id.sureButton);

		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if
				// (StringUtil.isNullOrEmpty(BaseDroidApp.getInstanse().getBizDataMap()
				// .get(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG))) {
				// Intent it = new
				// Intent(CrcdpaymentSetupAutoSuccessActivity.this,
				// MyCrcdSetupListActivity.class);
				// it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// startActivity(it);
				// finish();
				// return;
				// }
				// int tag = (Integer)
				// BaseDroidApp.getInstanse().getBizDataMap()
				// .get(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG);
				//
				// if (tag == 3) {
				// Intent it = new
				// Intent(CrcdpaymentSetupAutoSuccessActivity.this,
				// MyCreditCardActivity.class);
				// it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// startActivity(it);
				// finish();
				// } else {
				// Intent it = new
				// Intent(CrcdpaymentSetupAutoSuccessActivity.this,
				// MyCrcdSetupListActivity.class);
				// it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// startActivity(it);
				// finish();
				// }
				setResult(RESULT_OK);
				finish();
			}
		});

	}

	@Override
	public void onBackPressed() {
	}

}
