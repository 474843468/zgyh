package com.chinamworld.bocmbci.biz.epay.myPayService.treaty.modifyQuota;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.epay.EPayBaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.constants.TreatyConstants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.observer.PubHttpObserver;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

public class ResultActivity extends EPayBaseActivity {

	private View result;

	private TextView tv_treaty_num;
	private TextView tv_merchant_name;
	private TextView tv_merchant_id;
	private TextView tv_contract_acc;
	private TextView tv_contract_date;
	private TextView tv_modify_date;
	private TextView tv_per_max_quota;
	private TextView tv_day_max_quota;
	private TextView tv_cust_max_quota;

	private Button bt_finish;

	private Context treatyContext;
	private PubHttpObserver httpObserver;

	private String merchantId;
	private String agreementId;
	private String merchantName;
	private String newDailyQuota;
	private String accountNumber;
	private String accountType;
	private String bankDailyQuota;
	private String bankSingleQuota;
	private String signDate;
	private String modifyDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		treatyContext = TransContext.getTreatyTransContext();
		httpObserver = PubHttpObserver.getInstance(this, PubConstants.CONTEXT_TREATY);
		result = LayoutInflater.from(this).inflate(R.layout.epay_treaty_modify_quota_result, null);

		super.setType(0);
		super.setShowBackBtn(false);
		super.setTitleName(PubConstants.TITLE_TREATY);
		super.setContentView(result);
		super.onCreate(savedInstanceState);
		setLeftButtonPopupGone();
		super.hideFoot();
		super.hideRightButton();
		getTransData();
	}

	private void getTransData() {
		Intent intent = getIntent();

		bankDailyQuota = intent.getStringExtra(TreatyConstants.METHOD_QUERY_TREATY_QUOTA_FIELD_DAY_MAX_QUOTA);
		bankSingleQuota = intent.getStringExtra(TreatyConstants.METHOD_QUERY_TREATY_QUOTA_FIELD_PER_MAX_QUOTA);
		newDailyQuota = intent.getStringExtra("newCustQuota");
		treatyContext.setData("newDailyQuota", newDailyQuota);
		Map<Object, Object> merchant = treatyContext.getMap(TreatyConstants.PUB_FEILD_SELECTED_MERCHANT);
		treatyContext.getMap(TreatyConstants.PUB_FEILD_SELECTED_MERCHANT).put(
				TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_DAILY_QUOTA, newDailyQuota);
		agreementId = EpayUtil.getString(
				merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_AGREEMENT_ID), "");
		merchantId = EpayUtil.getString(merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_HOLDER_MERID),
				"");
		merchantName = EpayUtil.getString(
				merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_MERCHANT_NAME), "");
		accountNumber = EpayUtil.getString(merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_CARD_NO),
				"");
		accountType = EpayUtil.getString(merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_CARD_TYPE),
				"");
		signDate = EpayUtil.getString(merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_SIGN_DATE), "");
		BiiHttpEngine.showProgressDialogCanGoBack();
		httpObserver.req_getSystemTime("getSystemTimeCallback");
	}

	public void getSystemTimeCallback(Object resultObj) {
		Map<Object, Object> resultMap = EpayUtil.getMap(httpObserver.getResult(resultObj));
		modifyDate = EpayUtil.getString(resultMap.get(PubConstants.METHOD_GET_SYSTEM_TIME_FIELD_DATE_TIME), "");
		modifyDate = EpayUtil.converSystemTime(modifyDate);
		// 初始化当前页
		initCurPage();
	}

	private void initCurPage() {
		tv_treaty_num = (TextView) result.findViewById(R.id.tv_treaty_num);
		tv_treaty_num.setText(agreementId);
		tv_merchant_name = (TextView) result.findViewById(R.id.tv_merchant_name);
		tv_merchant_name.setText(merchantName);
		tv_merchant_id = (TextView) result.findViewById(R.id.tv_merchant_id);
		tv_merchant_id.setText(merchantId);
		tv_contract_acc = (TextView) result.findViewById(R.id.tv_contract_acc);
		// tv_contract_acc.setText(LocalData.AccountType.get(accountType) + " "
		// + StringUtil.getForSixForString(accountNumber));
		tv_contract_acc.setText(StringUtil.getForSixForString(accountNumber));
		tv_contract_date = (TextView) result.findViewById(R.id.tv_contract_date);
		tv_contract_date.setText(signDate);
		tv_modify_date = (TextView) result.findViewById(R.id.tv_modify_date);
		tv_modify_date.setText(modifyDate);
		tv_per_max_quota = (TextView) result.findViewById(R.id.tv_per_max_quota);
		tv_per_max_quota.setText(StringUtil.parseStringPattern(bankSingleQuota, 2));
		tv_day_max_quota = (TextView) result.findViewById(R.id.tv_day_max_quota);
		tv_day_max_quota.setText(StringUtil.parseStringPattern(bankDailyQuota, 2));
		tv_cust_max_quota = (TextView) result.findViewById(R.id.tv_cust_max_quota);
		tv_cust_max_quota.setText(StringUtil.parseStringPattern(newDailyQuota, 2));

		// 币种显示
		TextView currencyView = (TextView) findViewById(R.id.tv_currency);
		String currency_code = getIntent().getStringExtra("currency_code");
		currencyView.setText(LocalData.Currency.get(currency_code));

		bt_finish = (Button) result.findViewById(R.id.bt_finish);
		bt_finish.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// treatyContext.setData("newDailyQuota", newDailyQuota);
				finish();
				// Intent intent = new Intent(ResultActivity.this,
				// MerchantDetailActivity.class);
				// ResultActivity.this.startActivity(intent);
			}
		});
		BiiHttpEngine.dissMissProgressDialog();
	}

	@Override
	public void finish() {
		setResult(RESULT_OK);
		super.finish();
		overridePendingTransition(R.anim.no_animation, R.anim.n_pop_exit_bottom_down);
	}
}
