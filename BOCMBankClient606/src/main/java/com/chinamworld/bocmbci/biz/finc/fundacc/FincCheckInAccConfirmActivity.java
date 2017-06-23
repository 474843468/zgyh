package com.chinamworld.bocmbci.biz.finc.fundacc;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 基金开户，
 * 
 * @author xyl
 * 
 */
public class FincCheckInAccConfirmActivity extends FincBaseActivity {
	private static final String TAG = "FincCheckInAccConfirmActivity";
	/**
	 * 确定按钮
	 */
	private Button cofirmBtn;
	private Button lastBtn;

	/** 基金账户 */
	private TextView fundAccNumTv;
	/** 账户类型 */
	private TextView fundAccTypeTv;
	private TextView nickNameTv;
	private TextView currencyTv;
	private TextView accbalanceTv;

	private String fundAccNumStr;
	private String nickNameStr;
	private String fundAccTypeStr;
	private String accbalanceStr;
	private String currencyStr;
	private String accId;
	private String fincAccountStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settingbaseinit();
		init();
		initData();
		BaseHttpEngine.showProgressDialog();
		requestPsnAccountQueryAccountDetail(accId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void requestPsnAccountQueryAccountDetailCallback(Object resultObj) {
		super.requestPsnAccountQueryAccountDetailCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		if (result == null) {
			return;
		} else {
			List<Map<String, String>> accountDetaiList = (List<Map<String, String>>) result
					.get(Finc.FINC_ACCOUNTDETAILLIST_RES);
			if (accountDetaiList == null) {
				return;
			} else {
				for (Map<String, String> map : accountDetaiList) {
					if (map.get(Finc.FINC_CURRENCYCODE_RES).equals(
							ConstantGloble.PRMS_CODE_RMB)) {
						accbalanceStr = map.get(Finc.FINC_AVAILABLEBALANCE_RES);
						currencyStr = map.get(Finc.FINC_CURRENCYCODE_RES);
						accbalanceTv.setText(StringUtil.parseStringPattern(
								accbalanceStr, 2));
						FincControl.setTextColor(accbalanceTv, this);
						currencyTv.setText(LocalData.Currency.get(currencyStr));
					}
				}

			}
		}
	}

	/**
	 * 初始化布局
	 * 
	 * @Author xyl
	 */
	private void init() {
		settingbaseinit();
		View v = mainInflater.inflate(R.layout.finc_checinacc_confirm, null);
		tabcontent.addView(v);
		StepTitleUtils.getInstance().initTitldStep(this,
				fincControl.getStepsForCheckInAcc());
		StepTitleUtils.getInstance().setTitleStep(2);
		setTitle(R.string.finc_title_checkInfundAcc);
		cofirmBtn = (Button) findViewById(R.id.finc_confirm_btn);
		lastBtn = (Button) findViewById(R.id.finc_last_btn);
		fundAccNumTv = (TextView) findViewById(R.id.fund_accNum_tv);
		nickNameTv = (TextView) findViewById(R.id.fund_nickName_tv);
		currencyTv = (TextView) findViewById(R.id.fund_currency_tv);
		accbalanceTv = (TextView) findViewById(R.id.fund_accbalance_tv);
		fundAccTypeTv = (TextView) findViewById(R.id.fund_accType_tv);
		cofirmBtn.setOnClickListener(this);
		lastBtn.setOnClickListener(this);
		back.setVisibility(View.INVISIBLE);
		right.setText(getResources().getString(R.string.close));
		right.setVisibility(View.VISIBLE);
		right.setOnClickListener(this);
	}

	private void initData() {
		if (fincControl.registAccFund != null) {
			fundAccNumStr = fincControl.registAccFund.get(Finc.I_FINCACCOUNT);
			fundAccTypeStr = fincControl.registAccFund.get(Finc.I_FUNDACCTYPE);
			nickNameStr = fincControl.registAccFund.get(Finc.I_NICKNAME);
			accId = fincControl.registAccFund.get(Finc.I_ACCOUNTID);
			fincAccountStr = fincControl.registAccFund.get(Finc.I_FINCACCOUNT);
		}
		fundAccNumTv.setText(StringUtil.getForSixForString(fundAccNumStr));
		fundAccTypeTv.setText(LocalData.AccountType.get(fundAccTypeStr));
		nickNameTv.setText(nickNameStr);

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_confirm_btn:
			requestCommConversationId();
			BaseHttpEngine.showProgressDialog();
			break;
		case R.id.finc_last_btn:
			finish();
			break;
		case R.id.ib_top_right_btn:
			setResult(ConstantGloble.FINC_CLOSE);
			this.finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId();
	}

	@Override
	public void requestPSNGetTokenIdCallback(Object resultObj) {
		super.requestPSNGetTokenIdCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			checkINAccSuccess(accId, fincAccountStr, tokenId);
		}

	}

	@Override
	public void checkINAccSuccessCallback(Object resultObj) {
		super.checkINAccSuccessCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		fincControl.registAccFund.put(Finc.I_CURRENCYCODE, currencyStr);
		fincControl.registAccFund.put(Finc.I_ACCBALANCE, accbalanceStr);
		Intent intent = new Intent();
		intent.setClass(this, FincCheckInAccSuccessActivity.class);
		startActivityForResult(intent,
				ConstantGloble.ACTIVITY_REQUEST_CHECKINFUNCACC_CODE);
	}

	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ConstantGloble.ACTIVITY_REQUEST_CHECKINFUNCACC_CODE:
			switch (resultCode) {
			case RESULT_OK:
				setResult(RESULT_OK);
				finish();
				break;
			case RESULT_CANCELED:
				setResult(RESULT_CANCELED);
				finish();
				break;

			default:
				break;
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void finish() {
		super.finish();
	}

}
