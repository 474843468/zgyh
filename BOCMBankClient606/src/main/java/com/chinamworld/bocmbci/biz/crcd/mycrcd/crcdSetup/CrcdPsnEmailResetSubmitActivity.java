package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 电子邮件对账单---重置填写页面 */
public class CrcdPsnEmailResetSubmitActivity extends CrcdBaseActivity {
	private static final String TAG = "CrcdPsnEmailResetSubmitActivity";
	private View view = null;
	private String accountNumber = null;
	private String accountId = null;
	private String emaiAccress = null;
	private TextView accountNumberText = null;
	/** 账单类型 */
	private TextView typeText = null;
	private TextView emailText = null;
	private Spinner billDateSpinner = null;
	private Button nextButton = null;
	/** 时间数组 */
	private List<String> dateList = null;
	/** 账单日期 */
	private String dillDate = null;
	private String currentDate = null;
	/** 1--邮件对账单，2---手机对账单 */
	private int resetTag = 1;
	private String phone = null;
	private TextView type_text = null;
	private boolean isBillExist=false; // 当月账单是否已出
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		view = addView(R.layout.crcd_email_reset_submit);
		resetTag = getIntent().getIntExtra(ConstantGloble.FOREX_TAG, 1);
		if (1 == resetTag) {
			// 邮件对账单
			setTitle(getResources().getString(R.string.mycrcd_check_reset_email_title));
			emaiAccress = getIntent().getStringExtra(Crcd.CRCD_EMAIL);
			if (StringUtil.isNull(emaiAccress)) {
				return;
			}
		} else if (2 == resetTag) {
			// 手机号对账单
			setTitle(getResources().getString(R.string.mycrcd_check_reset_tell_title));
			phone = getIntent().getStringExtra(Crcd.CRCD_MOBILE);
			if (StringUtil.isNull(phone)) {
				return;
			}
		}
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		currentDate = getIntent().getStringExtra(Comm.DATETME);
		isBillExist = getIntent().getBooleanExtra(Crcd.CRCD_ISBILLEXIST, false);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		if (StringUtil.isNull(accountNumber) || StringUtil.isNull(accountId)) {
			return;
		}
		getBillDate();
		if (StringUtil.isNullOrEmpty(dateList)) {
			return;
		}
		init();
		initOnClick();
	}

	private void init() {
		accountNumberText = (TextView) view.findViewById(R.id.finc_accNumber);
		typeText = (TextView) view.findViewById(R.id.finc_accId);
		emailText = (TextView) view.findViewById(R.id.et_email);
		billDateSpinner = (Spinner) view.findViewById(R.id.bailDate);
		nextButton = (Button) view.findViewById(R.id.sureButton);
		type_text = (TextView) view.findViewById(R.id.type_text);
		if (1 == resetTag) {
			// 邮件对账单
			type_text.setText(getResources().getString(R.string.mycrcd_check_email_address));
			typeText.setText(getResources().getString(R.string.mycrcd_email_billdan));
			emailText.setText(emaiAccress);
		} else if (2 == resetTag) {
			// 手机对账单
			type_text.setText(getResources().getString(R.string.tel_num));
			typeText.setText(getResources().getString(R.string.mycrcd_phone_billdan));
			emailText.setText(phone);
		}
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accountNumberText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, typeText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, emailText);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, dateList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		billDateSpinner.setAdapter(adapter);
		billDateSpinner.setSelection(0);
		dillDate = billDateSpinner.getSelectedItem().toString();
		accountNumberText.setText(StringUtil.getForSixForString(accountNumber));
	}

	/** 计算账单日期 */
	private void getBillDate() {

		dateList = new ArrayList<String>();
		if(isBillExist){
			// 已出
			for (int i = 0; i < 12; i++) {
				String t = "-" + i;
				int j = Integer.valueOf(t);
				String times = QueryDateUtils.getLastNumMonthAgo(currentDate, j);
				dateList.add(times);
			}
		}else{
			for (int i = 1; i < 13; i++) {
				String t = "-" + i;
				int j = Integer.valueOf(t);
				String times = QueryDateUtils.getLastNumMonthAgo(currentDate, j);
				dateList.add(times);
			}
		}
		
	
		
//		dateList = new ArrayList<String>();
//		for (int i = 0; i < 12; i++) {
//			String t = "-" + i;
//			int j = Integer.valueOf(t);
//			String times = QueryDateUtils.getLastNumMonthAgo(currentDate, j);
//			dateList.add(times);
//		}
	}

	private void initOnClick() {
		// 账单日期
		billDateSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				billDateSpinner.setSelection(position);
				dillDate = billDateSpinner.getSelectedItem().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CrcdPsnEmailResetSubmitActivity.this, CrcdPsnEmailResetConfirmActivity.class);
				intent.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
				intent.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
				intent.putExtra(Comm.DATETME, dillDate);
				intent.putExtra(ConstantGloble.FOREX_TAG, resetTag);
				if (1 == resetTag) {
					intent.putExtra(Crcd.CRCD_EMAIL, emaiAccress);
				} else if (2 == resetTag) {
					intent.putExtra(Crcd.CRCD_MOBILE, phone);
				}
				startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
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
