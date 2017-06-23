package com.chinamworld.bocmbci.biz.epay.myPayService.treaty.setMerchantRelation;

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
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.DateUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class ResultActivity extends EPayBaseActivity {

	private View result;

	private TextView tv_treaty_num;
	private TextView tv_cust_name;
	private TextView tv_merchant_name;
	private TextView tv_merchant_id;
	private TextView tv_account;
	private TextView tv_acc_nickname;
	private TextView tv_contract_date;
	private TextView tv_contract_channel;
	private TextView tv_contract_client;
	private TextView tv_per_quota;
	private TextView tv_addup_quota;
	private TextView tv_day_quota;

	private Button bt_finish;

	private String treatyNum;
	private String custName;
	private String merchantName;
	private String merchantId;
	private String accountNumber;
	private String nickname;
	/** 签名日期 */
	private String contractDate;
	// private String contractChannel;
	private String perQuota;
	private String addUpQuota;
	private String dayQuota;
	/** 签名途径 */
	private String signChannel;

	// private PubHttpObserver httpObserver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// httpObserver = PubHttpObserver.getInstance(this,
		// PubConstants.CONTEXT_TREATY);
		result = LayoutInflater.from(this).inflate(R.layout.epay_treaty_add_result, null);
		super.setType(0);
		super.setShowBackBtn(false);
		super.setTitleName(PubConstants.TITLE_TREATY);
		super.setContentView(result);
		super.onCreate(savedInstanceState);
		// 初始化当前页
		getTransData();
	}

	private void getTransData() {
		Intent intent = getIntent();
		treatyNum = intent.getStringExtra("treatyNum");
		contractDate = intent.getStringExtra("signDate");
		signChannel = intent.getStringExtra("signChannel");
		accountNumber = intent.getStringExtra(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_NUMBER);
		nickname = intent.getStringExtra(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_NICKNAME);
		merchantId = intent.getStringExtra(TreatyConstants.METHOD_QUERY_MERCHANTS_FIELD_MERCHANT_ID);
		merchantName = intent.getStringExtra(TreatyConstants.METHOD_QUERY_MERCHANTS_FIELD_MERCHANT_NAME);
		custName = intent.getStringExtra(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_USER_NAME);
		addUpQuota = intent.getStringExtra(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_DAY_QUOTA);
		dayQuota = intent.getStringExtra(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_INPUT_QUOTA);
		perQuota = intent.getStringExtra(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_SINGLE_QUOTA);
		initCurPage();
	}

	private void initCurPage() {
		tv_treaty_num = (TextView) result.findViewById(R.id.tv_treaty_num);
		tv_cust_name = (TextView) result.findViewById(R.id.tv_cust_name);
		tv_merchant_name = (TextView) result.findViewById(R.id.tv_merchant_name);
		tv_merchant_id = (TextView) result.findViewById(R.id.tv_merchant_id);
		tv_account = (TextView) result.findViewById(R.id.tv_account);
		tv_acc_nickname = (TextView) result.findViewById(R.id.tv_acc_nickname);
		tv_contract_date = (TextView) result.findViewById(R.id.tv_contract_date);
		tv_contract_channel = (TextView) result.findViewById(R.id.tv_contract_channel);
		tv_contract_client = (TextView) result.findViewById(R.id.tv_contract_client);
		tv_per_quota = (TextView) result.findViewById(R.id.tv_per_quota);
		tv_addup_quota = (TextView) result.findViewById(R.id.tv_addup_quota);
		tv_day_quota = (TextView) result.findViewById(R.id.tv_day_quota);

		bt_finish = (Button) result.findViewById(R.id.bt_finish);
		bt_finish.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				// Intent intent = new Intent(ResultActivity.this,
				// TreatyActivity.class);
				// ResultActivity.this.startActivity(intent);
			}
		});
		// httpObserver.req_getSystemTime("getSystemTimeCallback");
		TransContext.getTreatyTransContext().clear("newMerchant");
		// contractChannel = "手机银行";
		initDisplay();
	}

	// public void getSystemTimeCallback(Object resultObj) {
	// Map<Object, Object> resultMap =
	// EpayUtil.getMap(httpObserver.getResult(resultObj));
	// contractDate =
	// EpayUtil.getString(resultMap.get(PubConstants.METHOD_GET_SYSTEM_TIME_FIELD_DATE_TIME),
	// "");
	// // Map<Object, Object> merchant =
	// TransContext.getTreatyTransContext().getMap("newMerchant");
	// //
	// merchant.put(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_SIGN_DATE,
	// contractDate);
	//
	// //
	// TransContext.getTreatyTransContext().getList(TreatyConstants.PUB_FEILD_TREATY_MERCHANTS).add(merchant);
	// TransContext.getTreatyTransContext().clear("newMerchant");
	// contractChannel = "手机银行";
	// initDisplay();
	// }

	private void initDisplay() {
		tv_treaty_num.setText(treatyNum);
		tv_cust_name.setText(custName);
		tv_merchant_name.setText(merchantName);

		tv_merchant_id.setText(merchantId);
		tv_account.setText(StringUtil.getForSixForString(accountNumber));
		tv_acc_nickname.setText(nickname);
		tv_contract_date.setText(DateUtils.signDateFormatter(contractDate));
		tv_contract_channel.setText(TreatyConstants.TREATY_CHANNEL.get(signChannel));
		tv_contract_client.setText(TreatyConstants.TREATY_AGENT.get(ConstantGloble.APP_USER_AGENT_PREFIX));

		tv_per_quota.setText(StringUtil.parseStringPattern(perQuota, 2));
		tv_addup_quota.setText(StringUtil.parseStringPattern(addUpQuota, 2));
		tv_day_quota.setText(StringUtil.parseStringPattern(dayQuota, 2));

		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_treaty_num);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_cust_name);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_merchant_name);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_merchant_id);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_account);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_acc_nickname);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_contract_date);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_contract_channel);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_contract_client);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_per_quota);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_addup_quota);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_day_quota);
	}

	@Override
	public void finish() {
		setResult(RESULT_OK);
		super.finish();
	}
}
