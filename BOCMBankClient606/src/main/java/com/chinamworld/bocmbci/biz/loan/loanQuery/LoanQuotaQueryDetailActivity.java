package com.chinamworld.bocmbci.biz.loan.loanQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.biz.loan.LoanData;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 额度查询详情页面 */
public class LoanQuotaQueryDetailActivity extends LoanBaseActivity {
	private static final String TAG = "LoanQuotaQueryDetailActivity";
	private View detailView = null;
	private int position = -1;
	private List<Map<String, String>> listDate = null;
	private TextView loanTypeText = null;
	private TextView numberText = null;
	private TextView timesText = null;
	private TextView currencyText = null;
	private TextView moneyText = null;
	private TextView usedText = null;
	private TextView availableText = null;
	private TextView statusText = null;
	private Button nextButton = null;
	private String loanType = null;
	private String quotaNumber=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.loan_left_three1));
		Button backButton = (Button) findViewById(R.id.ib_back);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		listDate = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOAN_QUARY_RESULT);
		position = getIntent().getIntExtra(ConstantGloble.FOREX_customerSelectedPosition, -1);
		if (StringUtil.isNullOrEmpty(listDate) || position < 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		init();
		setValue();
	}

	private void init() {
		detailView = LayoutInflater.from(this).inflate(R.layout.loan_quato_query_detail, null);
		tabcontentView.addView(detailView);
		loanTypeText = (TextView) findViewById(R.id.loan_choise_input_loanType);
		numberText = (TextView) findViewById(R.id.loan_query_quotaNumber);
		timesText = (TextView) findViewById(R.id.loan_query_loanToDate);
		currencyText = (TextView) findViewById(R.id.loan_query_currencyCode);
		moneyText = (TextView) findViewById(R.id.loan_query_quota);
		usedText = (TextView) findViewById(R.id.loan_query_quotaUsed);
		availableText = (TextView) findViewById(R.id.loan_query_availableQuota);
		statusText = (TextView) findViewById(R.id.loan_query_quotaStatus);
		nextButton = (Button) findViewById(R.id.loan_tradeButton);
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseHttpEngine.showProgressDialog();
				requestPsnLOANAccountListAndDetailQuery(quotaNumber);
			}
		});
	}

	private void setValue() {
		Map<String, String> map = listDate.get(position);
		loanType = map.get(Loan.LOAN_LOANTYPE_REQ);
		quotaNumber = map.get(Loan.LOAN_QUOTANUMBER_RES);
		String loanToDate = map.get(Loan.LOAN_LOANTODATE_RES);
		String availableQuota = map.get(Loan.LOAN_AVAILABLEQUOTA_RES);
		String currencyCode = map.get(Loan.LOAN_CURRENCYCODES_RES);
		String code = null;
		if (!StringUtil.isNull(currencyCode) && LocalData.Currency.containsKey(currencyCode)) {
			code = LocalData.Currency.get(currencyCode);
		} else {
			code = "-";
		}
		String quota = map.get(Loan.LOAN_QUOTA_RES);
		String quotas = null;
		if (!StringUtil.isNull(quota) && !StringUtil.isNull(currencyCode)) {
			quotas = StringUtil.parseStringCodePattern(currencyCode, quota, 2);
		} else {
			quotas = "-";
		}
		String quotaUsed = map.get(Loan.LOAN_QUOTAUSED_RES);
		String quotaUseds = null;

		if (!StringUtil.isNull(quotaUsed) && !StringUtil.isNull(currencyCode)) {
			quotaUseds = StringUtil.parseStringCodePattern(currencyCode, quotaUsed, 2);
		} else {
			quotaUseds = "-";
		}

		String money = null;
		if (!StringUtil.isNull(availableQuota) && !StringUtil.isNull(currencyCode)) {
			money = StringUtil.parseStringCodePattern(currencyCode, availableQuota, 2);
		} else {
			money = "-";
		}
		String quotaStatus = map.get(Loan.LOAN_QUOTASTATUS_RES);
		String quotaStatu = null;
		if (!StringUtil.isNull(quotaStatus) && LocalData.quotaStatusMap.containsKey(quotaStatus)) {
			quotaStatu = LocalData.quotaStatusMap.get(quotaStatus);
		} else {
			quotaStatu = "-";
		}
		String loantype = null;
		if (StringUtil.isNull(loanType)) {
			loantype = "-";
		} else {
			if (LoanData.loanTypeDataLimit.containsKey(loanType)) {
				loantype = LoanData.loanTypeDataLimit.get(loanType);
			} else {
				loantype = "-";
			}
		}
		loanTypeText.setText(loantype);
		numberText.setText(quotaNumber);
		timesText.setText(StringUtil.isNullOrEmptyCaseNullString(loanToDate) ? 
				ConstantGloble.BOCINVT_DATE_ADD :loanToDate);
		currencyText.setText(code);
		moneyText.setText(quotas);
		usedText.setText(quotaUseds);
		availableText.setText(money);
		statusText.setText(quotaStatu);
	}

	/**
	 * 额度用款列表和详情查询
	 * 
	 * @param quotaNumber
	 *            :额度号码
	 */
	private void requestPsnLOANAccountListAndDetailQuery(String quotaNumber) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_PSNLOANACCOUNTLISTANDDETAILQUERY_API);
		biiRequestBody.setConversationId(null);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Loan.LOAN_QUOTANUMBER_RES, quotaNumber);
		map.put(Loan.LOAN_QUERYTYPE_REQ, ConstantGloble.LOAN_QUERYTYPE);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnLOANAccountListAndDetailQueryCallback");
	}

	public void requestPsnLOANAccountListAndDetailQueryCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, String>> result = (List<Map<String, String>>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(Loan.LOAN_ACCOUNTLIST_RES, result);
		gotoActivity();
	}

	private void gotoActivity() {
		Intent intent = new Intent(LoanQuotaQueryDetailActivity.this, LoanQuotaQueryQueryActivity.class);
		startActivity(intent);
		BaseHttpEngine.dissMissProgressDialog();
	}
}
