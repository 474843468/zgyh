package com.chinamworld.bocmbci.biz.dept.largecd;

import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 支取大额存单结果页面
 * 
 * @author liuh
 * 
 */
public class LargeCDRedeemResultActivity extends DeptBaseActivity {
	private LinearLayout tabcontent;
	private Map<String, Object> signedAcc;
	/** 已购买大额存单明细 */
	private Map<String, Object> purchasedDetail;
	/** 存单编号 */
	private TextView cdNumberTv;
	/** 签约账户 */
	private TextView accNumberTv;
	/** 支取金额 */
	private TextView saveTv;
	/** 网银交易序号 */
	private TextView transNumberTv;
	/** 交易提示 */
	private TextView redeemResultTip;
	/** 网银交易序号 */
	private String transactionId;
	/** 交易状态 */
	private String status;
	private Button finishBtn;

	// add by luqp 2016年3月4日 确认页面修改
	/** 产品编码 */
	private TextView productCodeTv;
	/** 新增日期 */
	private TextView startDateTv;
	/** 新增日期 */
	private String startDate;
	/** 到期日期 */
	private TextView endDateTv;
	/** 到期日期 */
	private String endDate;
	/** 利率 */
	private TextView rateTv;
	/** 利息 */
	private TextView interestTv;
	/** 利息 */
	private String interest;
	/** 账户类型*/
	private TextView accType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();

		if (!getSignedAcc()) {
			if (!getPurchasedDetail()) {
				getIntentData();
				setViews();
				setListeners();
			} else {
				finish();
			}
		} else {
			finish();
		}
	}

	private void setListeners() {
		finishBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityTaskManager.getInstance().removeAllActivity();
				DeptDataCenter.getInstance().clearDeptData();
				Intent intent = new Intent(LargeCDRedeemResultActivity.this, LargeCDMenuActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private void setViews() {
		redeemResultTip = (TextView) findViewById(R.id.redeem_result_tip);
		transNumberTv = (TextView) findViewById(R.id.tv_trans_num);
		cdNumberTv = (TextView) findViewById(R.id.tv_large_cd_number);
		accNumberTv = (TextView) findViewById(R.id.tv_large_cd_acc_number);
		saveTv = (TextView) findViewById(R.id.tv_large_cd_save_money);
		finishBtn = (Button) findViewById(R.id.btn_finish);
		
		// add by luqp 2016年5月3日   账户后面添加账户类型    如:1005****8561 活期一本通 
		accType = (TextView) findViewById(R.id.tv_acc_type);
		String accTypeStr = StringUtil.valueOf1((String) signedAcc.get(Dept.LargeSign_accountType));
		String accountType =LocalData.AccountType.get(accTypeStr);
		accType.setText(accountType);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accType);

		// ////////////////////////////////////////////////////////////////////////
		// add by luqp 2016年3月4日 页面修改
		// 产品编码
		productCodeTv = (TextView) findViewById(R.id.large_cd_product_code_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, productCodeTv);
		productCodeTv.setText(StringUtil.valueOf1((String) purchasedDetail.get(Dept.PRODUCT_CODE)));
		// 新增日期
		startDateTv = (TextView) findViewById(R.id.large_cd_start_date);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, startDateTv);
		startDate = StringUtil.valueOf1((String) purchasedDetail.get(Dept.BUY_DATE));
		startDateTv.setText(startDate);
		// 到期日期
		endDateTv = (TextView) findViewById(R.id.large_cd_end_date_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, endDateTv);
		endDate = StringUtil.valueOf1((String) purchasedDetail.get(Dept.BUY_END_DATE));
		endDateTv.setText(endDate);
		// 利率
		rateTv = (TextView) findViewById(R.id.large_cd_rate_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, rateTv);
		String rate = StringUtil.valueOf1((String) purchasedDetail.get(Dept.RATE));
		rateTv.setText(rate + "%"); // add by luqp 2016年3月4日 追加%号
		
		interestTv = (TextView) findViewById(R.id.tv_large_cd_interest);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, interestTv);
		
		interestTv.setText(StringUtil.parseStringPattern(interest, 2));
		// ///////////////////////////////////////////////////////////////////////////

		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, transNumberTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, cdNumberTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accNumberTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, saveTv);

		String cdBalance = StringUtil.valueOf1((String) purchasedDetail.get(Dept.CD_BALANCE));

		if ("A".equals(status)) {
			redeemResultTip.setText(getString(R.string.large_cd_result_success));
		}
		transNumberTv.setText(transactionId);
		cdNumberTv.setText(StringUtil.valueOf1((String) purchasedDetail.get(Dept.CD_NUMBER)));
		String accNumber = StringUtil.getForSixForString(StringUtil.valueOf1((String) signedAcc.get(Dept.ACCOUNT_NUMBER)));
		accNumberTv.setText(accNumber);
		saveTv.setText(StringUtil.parseStringPattern(cdBalance, 2));
	}

	private void getIntentData() {
		Intent intent = getIntent();
		transactionId = intent.getStringExtra(Dept.TRANSACTION_ID);
		interest = intent.getStringExtra(Dept.INTEREST);
		status = intent.getStringExtra(Dept.STATUS);
	}

	/**
	 * 获取已购买大额存单明细
	 * 
	 * @return
	 */
	private boolean getPurchasedDetail() {
		purchasedDetail = DeptDataCenter.getInstance().getPurchasedDetail();
		return StringUtil.isNullOrEmpty(purchasedDetail);
	}

	/**
	 * 获取签约账户信息
	 * 
	 * @return
	 */
	private boolean getSignedAcc() {
		signedAcc = (Map<String, Object>) DeptDataCenter.getInstance().getSignedAcc();
		return StringUtil.isNullOrEmpty(signedAcc);
	}

	@SuppressLint("InflateParams")
	private void initViews() {
		setTitle(getString(R.string.large_cd_redeem_title));
		View view = mInflater.inflate(R.layout.large_cd_redeem_result, null);
		tabcontent = (LinearLayout) findViewById(R.id.sliding_body);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		ibBack.setVisibility(View.GONE);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 0);
		}

	}
}
