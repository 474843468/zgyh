package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 消费服务设置成功
 * 
 * @author huangyuchao
 * 
 */
public class CrcdServiceInfoSuccessActivity extends CrcdBaseActivity {

	private View view;
	private String codeCode = null;
	TextView finc_accNumber, finc_accId, mycrd_service_type, finc_fincName;

	Button sureButton;

	protected static String shortMsgLimitAmount;
	protected static String postLimitAmount;

	TextView tv_cardNumber;
	private View posMoneyView = null;
	private String passStyleCode = null;
	private String accountNuber = null;
	private  String strCurrencyCode = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_service_setup));
		view = addView(R.layout.crcd_service_setup_info_success);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);

		back.setVisibility(View.GONE);
		codeCode = getIntent().getStringExtra(ConstantGloble.CRCD_CODE);
		passStyleCode = getIntent().getStringExtra(Crcd.CRCD_SETPOSVERIFYMODE);
		accountNuber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
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

		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getResources().getString(R.string.mycrcd_service_tiaozheng_setup),
						this.getResources().getString(R.string.mycrcd_setup_info),
						this.getResources().getString(R.string.mycrcd_service_setup_success) });
		StepTitleUtils.getInstance().setTitleStep(3);
		posMoneyView = findViewById(R.id.pos_layout);
		if (!StringUtil.isNull(passStyleCode) && ConstantGloble.IS_EBANK_1.equals(passStyleCode)) {
			posMoneyView.setVisibility(View.VISIBLE);
		} else {
			posMoneyView.setVisibility(View.GONE);
		}
		tv_cardNumber = (TextView) view.findViewById(R.id.tv_cardNumber);
		tv_cardNumber.setText(StringUtil.getForSixForString(accountNuber));

		finc_accNumber = (TextView) findViewById(R.id.finc_accNumber);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_accNumber);
		finc_accId = (TextView) findViewById(R.id.finc_accId);
		mycrd_service_type = (TextView) findViewById(R.id.mycrd_service_type);
		finc_fincName = (TextView) findViewById(R.id.finc_fincName);

		shortMsgLimitAmount = CrcdServiceInfoActivity.msgVerifyLimit;
		postLimitAmount = CrcdServiceInfoActivity.posVerifyLimit;

		strCurrencyCode = LocalData.Currency.get(codeCode);
		finc_accNumber.setText(strCurrencyCode);
		finc_accId.setText(StringUtil.parseStringCodePattern(codeCode, shortMsgLimitAmount, 2));
		mycrd_service_type.setText(CrcdServiceInfoActivity.passType);
		finc_fincName.setText(StringUtil.parseStringCodePattern(codeCode, postLimitAmount, 2));

		sureButton = (Button) findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if
				// (StringUtil.isNullOrEmpty(BaseDroidApp.getInstanse().getBizDataMap()
				// .get(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG))) {
				// Intent it = new Intent(CrcdServiceInfoSuccessActivity.this,
				// CrcdServiceSetupListActivity.class);
				// it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// startActivity(it);
				// finish();
				// return;
				// }
				// int tag = (Integer)
				// BaseDroidApp.getInstanse().getBizDataMap()
				// .get(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG);
				//
				// if (tag == 1) {
				// Intent it = new Intent(CrcdServiceInfoSuccessActivity.this,
				// MyCreditCardActivity.class);
				// it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// startActivity(it);
				// finish();
				// } else {
				// Intent it = new Intent(CrcdServiceInfoSuccessActivity.this,
				// CrcdServiceSetupListActivity.class);
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
