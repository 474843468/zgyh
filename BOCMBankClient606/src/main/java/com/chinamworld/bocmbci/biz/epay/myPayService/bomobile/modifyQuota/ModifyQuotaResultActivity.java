package com.chinamworld.bocmbci.biz.epay.myPayService.bomobile.modifyQuota;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.epay.EPayBaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.BomConstants;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 电子支付-限额修改结果页面
 * 
 */
public class ModifyQuotaResultActivity extends EPayBaseActivity {

	private String tag = "EPayModifyQuotaResultActivity";

	private View ePayModifyQuotaResult;

	private TextView tv_day_max_quota;
	private TextView tv_cust_max_quota;
	private TextView tv_per_max_quota;

	private Button bt_sure;

	private String dayMaxQuota;
	private String perMaxQuota;
	private String custMaxQuota;

	private Context bomTransContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		bomTransContext = TransContext.getBomContext();
		ePayModifyQuotaResult = LayoutInflater.from(this).inflate(R.layout.epay_bom_modify_quota_result, null);

		super.setType(0);
		super.setShowBackBtn(false);
		super.setTitleName(PubConstants.TITLE_BOANK_OF_MOBILE);
		super.setContentView(ePayModifyQuotaResult);
		super.onCreate(savedInstanceState);
		setLeftButtonPopupGone();
		super.hideFoot();
		super.hideRightButton();
		// //初始化导航条
		// EpayPubUtil.initStepBar(this, 3, new
		// String[]{"修改交易限额","确认信息","修改成功"});

		getTransData();
	}

	private void getTransData() {
		Intent intent = getIntent();
		dayMaxQuota = intent.getStringExtra(BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_DAY_MAX);
		perMaxQuota = intent.getStringExtra(BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_PER_MAX);
		custMaxQuota = intent.getStringExtra(BomConstants.METHOD_SET_MAX_QUOTA_PRE_FIELD_QUOTA);
		bomTransContext.clear(PubConstants.PUB_FIELD_CONVERSATION_ID);
		bomTransContext.clear(PubConstants.PUB_FIELD_FACTORLIST);

		// 初始化当前页
		initCurPage();
	}

	private void initCurPage() {
		tv_day_max_quota = (TextView) findViewById(R.id.tv_day_max_quota);
		tv_per_max_quota = (TextView) findViewById(R.id.tv_per_max_quota);
		tv_cust_max_quota = (TextView) findViewById(R.id.tv_cust_max_quota);

		bt_sure = (Button) findViewById(R.id.bt_ensure);
		bt_sure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		initDisplay();
	}

	private void initDisplay() {
		tv_day_max_quota.setText(StringUtil.parseStringPattern(dayMaxQuota, 2));
		tv_per_max_quota.setText(StringUtil.parseStringPattern(perMaxQuota, 2));
		tv_cust_max_quota.setText(StringUtil.parseStringPattern(custMaxQuota, 2));
	}

	@Override
	public void finish() {
		setResult(RESULT_OK);
		super.finish();
		overridePendingTransition(R.anim.no_animation, R.anim.n_pop_exit_bottom_down);
	}
}
