package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;

/**
 * 信用卡还款方式设定成功
 * 
 * @author huangyuchao
 * 
 */
public class CrcdPaymentwaySetupSuccess extends CrcdBaseActivity {

	private View view;

	TextView mycrcd_accounted_type;
	TextView mycrcd_selected_creditcard;
	TextView mycrcd_accounted_money;
	TextView mycrcd_renmi_account;
	TextView mycrcd_foreign_account;

	Button sureButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_creditcard_money_setup_title));
		view = addView(R.layout.crcd_payment_setup_success);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);

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

	LinearLayout ll_bennumber, ll_wainumber;

	/** 初始化界面 */
	private void init() {
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getResources().getString(R.string.mycrcd_setup_style),
						this.getResources().getString(R.string.mycrcd_setup_info),
						this.getResources().getString(R.string.mycrcd_setup_success) });
		StepTitleUtils.getInstance().setTitleStep(3);

		mycrcd_accounted_type = (TextView) view.findViewById(R.id.mycrcd_accounted_type);
		mycrcd_selected_creditcard = (TextView) view.findViewById(R.id.mycrcd_selected_creditcard);
		mycrcd_accounted_money = (TextView) view.findViewById(R.id.mycrcd_accounted_money);
		mycrcd_renmi_account = (TextView) view.findViewById(R.id.mycrcd_renmi_account);
		mycrcd_foreign_account = (TextView) view.findViewById(R.id.mycrcd_foreign_account);

		mycrcd_accounted_type.setText(getString(R.string.mycrcd_auto_huanmoney));
		mycrcd_selected_creditcard.setText(CrcdPaymentwaySetup.strRepayCurSel);
		mycrcd_accounted_money.setText(CrcdPaymentwaySetup.strautoRepayMode);

		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mycrcd_selected_creditcard);

		ll_bennumber = (LinearLayout) view.findViewById(R.id.ll_bennumber);
		ll_wainumber = (LinearLayout) view.findViewById(R.id.ll_wainumber);

		ll_bennumber.setVisibility(View.GONE);
		ll_wainumber.setVisibility(View.GONE);

		String benNumber = CrcdPaymentwaySetup.getBenNumber();
		String waiNumber = CrcdPaymentwaySetup.getWaiNumber();

		if (CrcdPaymentwaySetup.ll_rmbShow.getVisibility() == View.VISIBLE) {
			ll_bennumber.setVisibility(View.VISIBLE);
			mycrcd_renmi_account.setText(benNumber);
		}
		if (CrcdPaymentwaySetup.ll_foreignShow.getVisibility() == View.VISIBLE) {
			ll_wainumber.setVisibility(View.VISIBLE);
			mycrcd_foreign_account.setText(waiNumber);
		}

		sureButton = (Button) findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if
				// (StringUtil.isNullOrEmpty(BaseDroidApp.getInstanse().getBizDataMap()
				// .get(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG))) {
				// Intent it = new Intent(CrcdPaymentwaySetupSuccess.this,
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
				// Intent it = new Intent(CrcdPaymentwaySetupSuccess.this,
				// MyCreditCardActivity.class);
				// it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// startActivity(it);
				// finish();
				// } else {
				// Intent it = new Intent(CrcdPaymentwaySetupSuccess.this,
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
		// super.onBackPressed();
	}

}
