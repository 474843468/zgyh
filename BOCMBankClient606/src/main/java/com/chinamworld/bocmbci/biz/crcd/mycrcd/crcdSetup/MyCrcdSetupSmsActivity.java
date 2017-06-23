package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 设置交易短信
 * 
 * @author huangyuchao
 * 
 */
public class MyCrcdSetupSmsActivity extends CrcdBaseActivity {

	private View view;
	TextView finc_accNumber, finc_accId;

	RadioGroup rg_select;

	RadioButton rb_zhu, rb_supply, rb_zhuandsupply;

	Button sureButton;

	//** 编码： 0=发送主卡 ,1=发送附卡, 2=发送主卡&附卡 *//*
	static int sendMessMode;
	static String strSendMessMode;
	private String accountNumber = null;
	private String subaccountNumber = null;	
	private String accountId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_setup_jiaoyi_message));
		view = addView(R.layout.crcd_setup_sms_message);
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		subaccountNumber= getIntent().getStringExtra(Crcd.CRCD_SUPPLYCARD_RES);
		accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});

		init();

	}

	//** 右侧按钮点击事件 *//*
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

	//** 初始化界面 *//*
	private void init() {
		strSendMessMode = getString(R.string.mycrcd_send_zhucard);
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getResources().getString(R.string.mycrcd_write_step_info),
						this.getResources().getString(R.string.mycrcd_setup_info),
						this.getResources().getString(R.string.mycrcd_service_setup_success) });
		StepTitleUtils.getInstance().setTitleStep(1);

		finc_accNumber = (TextView) view.findViewById(R.id.finc_accNumber);
		finc_accId = (TextView) view.findViewById(R.id.finc_accId);

		finc_accNumber.setText(StringUtil.getForSixForString(accountNumber));
		finc_accId.setText(StringUtil.getForSixForString(MySupplymentDetailActivity.supplyCardNumber));

		rg_select = (RadioGroup) view.findViewById(R.id.rg_select);
		rb_zhu = (RadioButton) view.findViewById(R.id.rb_zhu);
		rb_supply = (RadioButton) view.findViewById(R.id.rb_supply);
		rb_zhuandsupply = (RadioButton) view.findViewById(R.id.rb_zhuandsupply);
		rb_zhu.setChecked(true);
		sendMessMode = 0;
		// 0=发送主卡 1=发送附卡 2=发送主卡&附卡
		rg_select.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_zhu:
					sendMessMode = 0;
					strSendMessMode = getString(R.string.mycrcd_send_zhucard);
					break;
				case R.id.rb_supply:
					sendMessMode = 1;
					strSendMessMode = getString(R.string.mycrcd_send_supplymentcard);
					break;
				case R.id.rb_zhuandsupply:
					sendMessMode = 2;
					strSendMessMode = getString(R.string.mycrcd_send_zhuandsupplycard);
					break;
				}
			}
		});

		sureButton = (Button) view.findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent(MyCrcdSetupSmsActivity.this, MyCrcdSetupSmsConfirmActivity.class);
				it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
				it.putExtra(Crcd.CRCD_SUPPLYCARD_RES, subaccountNumber);
				it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
				// startActivity(it);
				startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;

		default:
			break;
		}
	}
}
