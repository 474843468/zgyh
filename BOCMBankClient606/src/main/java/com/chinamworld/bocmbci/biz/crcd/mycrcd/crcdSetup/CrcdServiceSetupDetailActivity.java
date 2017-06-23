package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import java.util.Map;

import android.content.Intent;
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
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 信用卡服务设置详情
 * 
 * @author huangyuchao
 * 
 */
public class CrcdServiceSetupDetailActivity extends CrcdBaseActivity {
	private static final String TAG = "CrcdServiceSetupDetailActivity";

	private View view;
	private View posView = null;

	Button sureButton;
	TextView tv_cardNumber, finc_accNumber, finc_accId, finc_throw, finc_fincName;

	protected static String shortMsgLimitAmount;
	protected static String postLimitAmount;
	protected static String postFlag;

	protected static String strCurrencyCode;
	protected static String strpostFlag;
	private String codeCode = null;// 币种代码
	private String accountId = null;
	private String accNumber = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_service_setup));
		view = addView(R.layout.crcd_service_setup_detail);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		init();
	}

	/** 初始化界面 */
	private void init() {
		tv_cardNumber = (TextView) view.findViewById(R.id.tv_cardNumber);
		finc_accNumber = (TextView) view.findViewById(R.id.finc_accNumber);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_accNumber);
		finc_accId = (TextView) view.findViewById(R.id.finc_accId);
		finc_throw = (TextView) view.findViewById(R.id.finc_throw);
		finc_fincName = (TextView) view.findViewById(R.id.finc_fincName);
		posView = findViewById(R.id.posView);
		int tag = getIntent().getIntExtra(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG, 0);
		if (tag == 1) {
			// 我的信用卡详情页面
			accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
			accNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
			codeCode = getIntent().getStringExtra(Crcd.CRCD_CURRENCYCODE);
			tv_cardNumber.setText(StringUtil.getForSixForString(accNumber));
			strCurrencyCode = LocalData.Currency.get(codeCode);
			finc_accNumber.setText(strCurrencyCode);
			shortMsgLimitAmount = getIntent().getStringExtra(Crcd.CRCD_SHORTMSGLIMITAMOUNT);
			postLimitAmount = getIntent().getStringExtra(Crcd.CRCD_POSLIMITAMOUNT);
			postFlag = getIntent().getStringExtra(Crcd.CRCD_POSFLAG);

		} else {
			// 消费服务设置选卡页面
			accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
			accNumber = CrcdServiceSetupListActivity.accountNumber;
			tv_cardNumber.setText(StringUtil.getForSixForString(accNumber));
			codeCode = CrcdServiceSetupListActivity.currencyCode;
			strCurrencyCode = LocalData.Currency.get(CrcdServiceSetupListActivity.currencyCode);
			finc_accNumber.setText(strCurrencyCode);
			Map<String, Object> map = CrcdServiceSetupListActivity.returnMap;
			shortMsgLimitAmount = String.valueOf(map.get(Crcd.CRCD_SHORTMSGLIMITAMOUNT));
			postLimitAmount = String.valueOf(map.get(Crcd.CRCD_POSLIMITAMOUNT));
			postFlag = String.valueOf(map.get(Crcd.CRCD_POSFLAG));

		}

		if (ConstantGloble.IS_EBANK_0.equals(postFlag)) {
			posView.setVisibility(View.GONE);
		} else {
			posView.setVisibility(View.VISIBLE);
		}

		strpostFlag = LocalData.posValidateType.get(postFlag);

		finc_accId.setText(StringUtil.parseStringCodePattern(codeCode, shortMsgLimitAmount, 2));
		// 0：签名,1：密码+签名
		finc_throw.setText(strpostFlag);
		finc_fincName.setText(StringUtil.parseStringCodePattern(codeCode, postLimitAmount, 2));

		sureButton = (Button) view.findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent(CrcdServiceSetupDetailActivity.this, CrcdServiceInfoActivity.class);
				it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
				it.putExtra(Crcd.CRCD_CURRENCYCODE, codeCode);
				it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accNumber);
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
