package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 开通账单成功
 * 
 * @author huangyuchao
 * 
 */
public class CrcdPsnCheckOpenSuccessActivity extends CrcdBaseActivity {

	private View view;

	String strBillSetup;
	int billSetupId;

	String strAddtrss;

	String isEdit;

	protected String prefix;
	private String accountNumber = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

//		strBillSetup = CrcdPsnQueryCheckDetail.strbillSetupId;
		billSetupId = this.getIntent().getIntExtra(ConstantGloble.CRCD_BILLSETUPID, -1);
		if (billSetupId == 0) {

			strBillSetup = this.getString(R.string.mycrcd_paper_billdan);
		}
		if (billSetupId == 1) {

			strBillSetup = this.getString(R.string.mycrcd_email_billdan);
		}
		if (billSetupId == 2) {

			strBillSetup = this.getString(R.string.mycrcd_phone_billdan);
		}
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		isEdit = this.getIntent().getStringExtra(ConstantGloble.CRCD_ISOPENOREDIT);
		strAddtrss = getIntent().getStringExtra(Crcd.CRCD_PAPERADDRESS);
		if ("open".equals(isEdit)) {
			prefix = this.getString(R.string.mycrcd_open);
		} else if ("edit".equals(isEdit)) {
			prefix = this.getString(R.string.edit);
		}
		// 为界面标题赋值
		setTitle(prefix + strBillSetup);
		// 右上角按钮赋值
		// setText(this.getString(R.string.close));

		view = addView(R.layout.crcd_psn_check_opensuccess);
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

	TextView tv_acc_loss_actnum;

	TextView finc_accNumber;
	/** 账单类型 */
	TextView finc_accId;

	static String passType;

	Button sureButton;

	LinearLayout ll_paper, ll_email, ll_phone;
	TextView tv_paper_confirm, tv_address_confirm, tv_phone_confirm;

	/** 初始化界面 */
	private void init() {
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getResources().getString(R.string.mycrcd_write_info_message),
						this.getResources().getString(R.string.mycrcd_setup_info),
						this.getResources().getString(R.string.mycrcd_service_setup_success) });
		StepTitleUtils.getInstance().setTitleStep(3);

		tv_acc_loss_actnum = (TextView) view.findViewById(R.id.tv_acc_loss_actnum);

		if ("edit".equals(isEdit)) {
			tv_acc_loss_actnum.setText(this.getResources().getString(R.string.mycrcd_edit_zhangdan_success));
		}

		else {
			tv_acc_loss_actnum.setText(this.getResources().getString(R.string.mycrcd_open_zhangdan_success));
		}

		finc_accNumber = (TextView) view.findViewById(R.id.finc_accNumber);
		finc_accId = (TextView) view.findViewById(R.id.finc_accId);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_accId);
		tv_address_confirm = (TextView) view.findViewById(R.id.tv_address_confirm);

		finc_accNumber.setText(StringUtil.getForSixForString(accountNumber));
		finc_accId.setText(strBillSetup);

		sureButton = (Button) view.findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if
				// (StringUtil.isNullOrEmpty(BaseDroidApp.getInstanse().getBizDataMap()
				// .get(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG))) {
				// Intent it = new Intent(CrcdPsnCheckOpenSuccessActivity.this,
				// CrcdPsnQueryCheckList.class);
				// it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// startActivity(it);
				// finish();
				// return;
				// }
				// int tag = (Integer)
				// BaseDroidApp.getInstanse().getBizDataMap()
				// .get(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG);
				//
				// if (tag == 2) {
				// Intent it = new Intent(CrcdPsnCheckOpenSuccessActivity.this,
				// MyCreditCardActivity.class);
				// it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// startActivity(it);
				// finish();
				// } else {
				// Intent it = new Intent(CrcdPsnCheckOpenSuccessActivity.this,
				// CrcdPsnQueryCheckList.class);
				// it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// startActivity(it);
				// finish();
				// }
				setResult(RESULT_OK);
				finish();
			}
		});

		ll_paper = (LinearLayout) view.findViewById(R.id.ll_paper);
		ll_email = (LinearLayout) view.findViewById(R.id.ll_email);
		ll_phone = (LinearLayout) view.findViewById(R.id.ll_phone);

		tv_paper_confirm = (TextView) view.findViewById(R.id.tv_paper_confirm);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_paper_confirm);
		tv_address_confirm = (TextView) view.findViewById(R.id.tv_address_confirm);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_address_confirm);
		tv_phone_confirm = (TextView) view.findViewById(R.id.tv_phone_confirm);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_phone_confirm);
		if (billSetupId == 0) {
			ll_paper.setVisibility(View.VISIBLE);
			ll_email.setVisibility(View.GONE);
			ll_phone.setVisibility(View.GONE);

			tv_paper_confirm.setText(strAddtrss);
		} else if (billSetupId == 1) {
			ll_paper.setVisibility(View.GONE);
			ll_email.setVisibility(View.VISIBLE);
			ll_phone.setVisibility(View.GONE);

			tv_address_confirm.setText(CrcdPsnCheckOpenActivity.strEmail);
		} else if (billSetupId == 2) {
			ll_paper.setVisibility(View.GONE);
			ll_email.setVisibility(View.GONE);
			ll_phone.setVisibility(View.VISIBLE);

			tv_phone_confirm.setText(CrcdPsnCheckOpenActivity.phoneNum);
		}

	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
	}
}
