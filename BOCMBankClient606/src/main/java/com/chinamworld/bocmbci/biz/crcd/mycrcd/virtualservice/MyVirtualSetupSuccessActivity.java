package com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdAccBaseActivity;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 虚拟卡设定成功
 * 
 * @author huangyuchao
 * 
 */
public class MyVirtualSetupSuccessActivity extends CrcdAccBaseActivity {
	private static final String TAG = "MyVirtualSetupSuccessActivity";
	private View view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_virtualcard_setup));
		view = addView(R.layout.crcd_virtualcard_setup_success);
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

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestSystemDateTimeCallBack(resultObj);
	}

	static String accountId;
	static String accountNumber;
	static String currencyCode;
	private int accNum = 0;

	TextView finc_accNumber, finc_fenqidate, finc_miaoshus;
	TextView finc_remiandate, finc_remiannomoney, finc_nextdate;
	EditText finc_et_pass;

	/** 系统时间 */
	private String currenttime;

	Map<String, Object> rtnMap;

	static String accountName;
	static String startDate;
	static String endDate;
	static String customerId;

	static String validateTime;
	static String currencyDate;
	static String atotaLeamt;

	static String singleMoney;
	static String totalMoney;
	static String virtualCardNo;
	Button sureButton;

	TextView crcd_total_money;
	TextView crcd_accountname, finc_startdate, finc_enddate;

	/** 初始化界面 */
	private void init() {

		rtnMap = VirtualBCListActivity.virCardItem;

		accountName = String.valueOf(rtnMap.get(Crcd.CRCD_ACCOUNTNAME_RES));
		customerId = String.valueOf(rtnMap.get(Crcd.CRCD_CUSTOMERID));
		currencyDate = String.valueOf(rtnMap.get(Crcd.CRCD_CURRENTDATE));
		virtualCardNo = String.valueOf(rtnMap.get(Crcd.CRCD_VIRTUALCARDNO));
		atotaLeamt = String.valueOf(rtnMap.get(Crcd.CRCD_ATOTALEAMT));

		crcd_accountname = (TextView) view.findViewById(R.id.crcd_accountname);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, crcd_accountname);
		finc_startdate = (TextView) view.findViewById(R.id.finc_startdate);
		finc_enddate = (TextView) view.findViewById(R.id.finc_enddate);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_startdate);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_enddate);
		crcd_accountname.setText(MyVirtualBCListActivity.accountName);
		startDate = getIntent().getStringExtra(Crcd.CRCD_VIRCARDSTARTDATE);
		endDate = getIntent().getStringExtra(Crcd.CRCD_VIRCARDENDDATE);
		finc_startdate.setText(startDate);
		finc_enddate.setText(endDate);

		// StepTitleUtils.getInstance().initTitldStep(
		// this,
		// new String[] {
		// this.getResources().getString(
		// R.string.mycrcd_virtual_write_info),
		// this.getResources().getString(
		// R.string.mycrcd_setup_info),
		// this.getResources().getString(
		// R.string.mycrcd_step_success) });
		// StepTitleUtils.getInstance().setTitleStep(3);
		crcd_total_money = (TextView) view.findViewById(R.id.crcd_total_money);

		finc_accNumber = (TextView) view.findViewById(R.id.finc_accNumber);
		finc_fenqidate = (TextView) view.findViewById(R.id.finc_fenqidate);
		finc_miaoshus = (TextView) view.findViewById(R.id.finc_miaoshus);

		finc_remiandate = (TextView) findViewById(R.id.finc_remiandate);
		finc_remiannomoney = (TextView) view.findViewById(R.id.finc_remiannomoney);
		finc_nextdate = (TextView) view.findViewById(R.id.finc_nextdate);

		finc_accNumber.setText(StringUtil.getForSixForString(MyVirtualBCListActivity.accountNumber));
		finc_fenqidate.setText(StringUtil.getForSixForString(VirtualBCListActivity.virtualCardNo));
		finc_miaoshus.setText(MyVirtualBCListActivity.strAccountType);

		finc_remiandate.setText(MyVirtualSetupActivity.validateTime);
		finc_remiannomoney.setText(StringUtil.parseStringPattern(MyVirtualSetupActivity.totalMoney, 2));
		finc_nextdate.setText(StringUtil.parseStringPattern(MyVirtualSetupActivity.singleMoney, 2));
		crcd_total_money.setText(StringUtil.parseStringPattern(MyVirtualSetupActivity.atotaLeamt, 2));

		finc_et_pass = (EditText) view.findViewById(R.id.finc_et_pass);

		sureButton = (Button) view.findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				singleMoney = finc_remiannomoney.getText().toString();
				totalMoney = finc_nextdate.getText().toString();
				validateTime = finc_remiandate.getText().toString();

				Intent it = new Intent(MyVirtualSetupSuccessActivity.this, MyVirtualBCListActivity.class);
				it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(it);
				finish();
			}
		});

	}

//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//	}

	@Override
	protected void onDestroy() {
		BaseHttpEngine.dissMissProgressDialog();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
	}

}
