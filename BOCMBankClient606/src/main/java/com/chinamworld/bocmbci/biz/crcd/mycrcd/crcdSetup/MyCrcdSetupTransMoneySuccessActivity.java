package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 设置交易限额成功
 * 
 * @author huangyuchao
 * 
 */
public class MyCrcdSetupTransMoneySuccessActivity extends CrcdBaseActivity {

	private View view;
	private String currencyCode = null;
	private String strCurrencyCode = null;
	
	private String accountNumber = null;
	/** 附属卡卡号 */
	private String subaccountNumber = null;
	private String amount = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_setup_jiaoe_money));
		view = addView(R.layout.crcd_setup_trans_money_success);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		back.setVisibility(View.GONE);
		currencyCode = getIntent().getStringExtra(ConstantGloble.CRCD_CODE);
		strCurrencyCode = getIntent().getStringExtra(Crcd.CRCD_CURRENCYCODE);
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		subaccountNumber= getIntent().getStringExtra(Crcd.CRCD_SUPPLYCARD_RES);
		amount = getIntent().getStringExtra(Crcd.CRCD_AMOUNT);
		init();

	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

	TextView finc_accNumber, finc_accId, tv_cardNumber, finc_fincName;
	Button sureButton;

	EditText et_finc_password;

	/** 初始化界面 */
	private void init() {
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getResources().getString(R.string.mycrcd_setup_jiaoe_money),
						this.getResources().getString(R.string.mycrcd_setup_info),
						this.getResources().getString(R.string.mycrcd_service_setup_success) });
		StepTitleUtils.getInstance().setTitleStep(3);

		finc_accNumber = (TextView) view.findViewById(R.id.finc_accNumber);
		finc_accId = (TextView) view.findViewById(R.id.finc_accId);
		tv_cardNumber = (TextView) view.findViewById(R.id.tv_cardNumber);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_cardNumber);
		finc_fincName = (TextView) view.findViewById(R.id.finc_fincName);

		finc_accNumber.setText(StringUtil.getForSixForString(accountNumber));
		finc_accId.setText(StringUtil.getForSixForString(subaccountNumber));
		tv_cardNumber.setText(strCurrencyCode);
		finc_fincName.setText(StringUtil.parseStringCodePattern(currencyCode, amount, 2));
		sureButton = (Button) view.findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if
				// (StringUtil.isNullOrEmpty(BaseDroidApp.getInstanse().getBizDataMap()
				// .get(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG))) {
				// Intent it = new
				// Intent(MyCrcdSetupTransMoneySuccessActivity.this,
				// MySupplymentListActivity.class);
				// it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// startActivity(it);
				// finish();
				// return;
				// }
				// int tag = (Integer)
				// BaseDroidApp.getInstanse().getBizDataMap()
				// .get(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG);
				//
				// if (tag == 4) {
				// Intent it = new
				// Intent(MyCrcdSetupTransMoneySuccessActivity.this,
				// MyCreditCardActivity.class);
				// it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// startActivity(it);
				// finish();
				// } else {
				// Intent it = new
				// Intent(MyCrcdSetupTransMoneySuccessActivity.this,
				// MySupplymentListActivity.class);
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
		// super.onBackPressed();
	}

}
