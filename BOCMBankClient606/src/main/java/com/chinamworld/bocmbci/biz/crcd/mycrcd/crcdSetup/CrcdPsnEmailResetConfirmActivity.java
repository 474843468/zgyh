package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 电子邮件对账单---重置确认页面 */
public class CrcdPsnEmailResetConfirmActivity extends CrcdBaseActivity {
	private static final String TAG = "CrcdPsnEmailResetConfirmActivity";
	private View view = null;
	private String accountNumber = null;
	private String accountId = null;
	private String emaiAccress = null;
	/** 账单日期 */
	private String billDate = null;
	private TextView accountNumberText = null;
	private TextView typeText = null;
	private TextView emailText = null;
	private TextView billDateText = null;
	private Button nextButton = null;
	/** 1--邮件对账单，2---手机对账单 */
	private int resetTag = 1;
	private String phone = null;
	private TextView type_text = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
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
		view = addView(R.layout.crcd_email_reset_confirm);
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		billDate = getIntent().getStringExtra(Comm.DATETME);
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
		init();
	}

	private void init() {
		accountNumberText = (TextView) view.findViewById(R.id.finc_accNumber);
		typeText = (TextView) view.findViewById(R.id.finc_accId);
		emailText = (TextView) view.findViewById(R.id.et_email);
		billDateText = (TextView) view.findViewById(R.id.bailDate);
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
		accountNumberText.setText(StringUtil.getForSixForString(accountNumber));
		billDateText.setText(billDate);
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseHttpEngine.showProgressDialog();
				if (1 == resetTag) {
					requestPsnReSetEmailPaperCheck();
				} else if (2 == resetTag) {
					requestPsnReSetSmsPaperCheck();
				}

			}
		});
	}

	/** 重置邮件电子对账单 */
	private void requestPsnReSetEmailPaperCheck() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNRESETEMAILPAPERCHECK_API);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		map.put(Crcd.CRCD_BILLDATE_REQ, billDate);
		map.put(Crcd.CRCD_BILLADDRESS_REQ, emaiAccress);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnReSetEmailPaperCheckCallback");
	}

	public void requestPsnReSetEmailPaperCheckCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
		Object result = body.getResult();
		Intent intent = new Intent(this, CrcdPsnEmailResetSuccessActivity.class);
		intent.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		intent.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		intent.putExtra(Crcd.CRCD_EMAIL, emaiAccress);
		intent.putExtra(Comm.DATETME, billDate);
		intent.putExtra(ConstantGloble.FOREX_TAG, resetTag);
		startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
		BaseHttpEngine.dissMissProgressDialog();

	}

	/** 重置邮件电子对账单 */
	private void requestPsnReSetSmsPaperCheck() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNRESETSMSPAPERCHECK_API);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		map.put(Crcd.CRCD_BILLDATE_REQ, billDate);
		map.put(Crcd.CRCD_BILLADDRESS_REQ, phone);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnReSetSmsPaperCheckCallback");
	}

	public void requestPsnReSetSmsPaperCheckCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
		Object result = body.getResult();
		Intent intent = new Intent(this, CrcdPsnEmailResetSuccessActivity.class);
		intent.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		intent.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		intent.putExtra(Crcd.CRCD_MOBILE, phone);
		intent.putExtra(Comm.DATETME, billDate);
		intent.putExtra(ConstantGloble.FOREX_TAG, resetTag);
		startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
		BaseHttpEngine.dissMissProgressDialog();

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
