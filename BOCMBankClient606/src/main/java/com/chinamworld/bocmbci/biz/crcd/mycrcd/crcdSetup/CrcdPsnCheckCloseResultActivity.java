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
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 对账单关闭结果
 * 
 * @author huangyuchao
 * 
 */
public class CrcdPsnCheckCloseResultActivity extends CrcdBaseActivity {
	private View view;

	static String zhuiText;
	private int billSetupId;
	private String strBillSetup;
	private String isEdit;
	private TextView leftText = null;
	private String accountNumber = null;
	private String email;
	/** 纸质账单地址 */
	private String paperAddress;
	private String mobile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
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
		isEdit = this.getIntent().getStringExtra(ConstantGloble.CRCD_ISOPENOREDIT);
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		paperAddress = getIntent().getStringExtra(Crcd.CRCD_PAPERADDRESS);
		mobile = getIntent().getStringExtra(Crcd.CRCD_MOBILE);
		email = getIntent().getStringExtra(Crcd.CRCD_EMAIL);
//		zhuiText = CrcdPsnQueryCheckDetail.strbillSetupId;

		// 为界面标题赋值
		setTitle(this.getString(R.string.close) + strBillSetup);
		view = addView(R.layout.crcd_query_check_result);
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

	TextView finc_accId, mycrd_service_type, finc_fincName;

	Button sureButton;

	/** 初始化界面 */
	private void init() {
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getResources().getString(R.string.mycrcd_write_step_info),
						this.getResources().getString(R.string.mycrcd_setup_info),
						this.getResources().getString(R.string.mycrcd_service_setup_success) });
		StepTitleUtils.getInstance().setTitleStep(3);

		finc_accId = (TextView) findViewById(R.id.finc_accId);
		mycrd_service_type = (TextView) findViewById(R.id.mycrd_service_type);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mycrd_service_type);
		finc_fincName = (TextView) findViewById(R.id.finc_fincName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_fincName);
		leftText = (TextView) findViewById(R.id.left_text);
		finc_accId.setText(StringUtil.getForSixForString(accountNumber));
		mycrd_service_type.setText(strBillSetup);
		finc_fincName.setText(CrcdPsnQueryCheckDetail.paperAddress);

		if (0 == billSetupId) {
			leftText.setText(getResources().getString(R.string.mycrcd_billdan_address));
			if (StringUtil.isNull(paperAddress)) {
				finc_fincName.setText("-");
			} else {
				finc_fincName.setText(paperAddress);
			}

		} else if (1 == billSetupId) {
			leftText.setText(getResources().getString(R.string.mycrcd_check_email_address));
			if (StringUtil.isNull(email)) {
				finc_fincName.setText("-");
			} else {
				finc_fincName.setText(email);
			}
		} else if (2 == billSetupId) {
			leftText.setText(getResources().getString(R.string.tel_num));
			if (StringUtil.isNull(mobile)) {
				finc_fincName.setText("-");
			} else {
				finc_fincName.setText(mobile);
			}
		}

		sureButton = (Button) findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if
				// (StringUtil.isNullOrEmpty(BaseDroidApp.getInstanse().getBizDataMap()
				// .get(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG))) {
				// Intent it = new Intent(CrcdPsnCheckCloseResultActivity.this,
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
				// Intent it = new Intent(CrcdPsnCheckCloseResultActivity.this,
				// MyCreditCardActivity.class);
				// it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// startActivity(it);
				// finish();
				// } else {
				// Intent it = new Intent(CrcdPsnCheckCloseResultActivity.this,
				// CrcdPsnQueryCheckList.class);
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
