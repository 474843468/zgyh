package com.chinamworld.bocmbci.biz.dept.largecd;

import java.util.HashMap;
import java.util.List;
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
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 已购买大额存单详情
 *
 * @author liuh
 *
 */
public class LargeCDPurchasedDetailActivity extends DeptBaseActivity {
	private LinearLayout tabcontent;
	/** 存单编号 */
	private TextView numberTv;
	/** 产品编码 */
	private TextView productCodeTv;
	/** 存款日期 */
	private TextView startDateTv;
	/** 到期日期 */
	private TextView endDateTv;
	/** 起息日期 */
	private TextView interestStartDateTv;
	/** 利率 */
	private TextView rateTv;
	/** 存单面额 */
	private TextView balanceTv;
	/** 存单状态 */
	private TextView statusTv;
	/** 存单定期账号 */
	private TextView accTimeNumTv;
	/** 支取 */
	private Button redeemBtn;
	/** 交易明细 */
	private Button detailBtn;
	/** 存单详情 */
	private Map<String, Object> purchasedDetail;
	/** 申购日期 */
	private String startDate;
	/** 到期日期 */
	private String endDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initViews();

		if (!getPurchasedDetail()) {
			setViews();
			setListeners();
		} else {
			finish();
		}
	}

	/** 设置按钮监听器*/
	private void setListeners() {
		redeemBtn.setOnClickListener(redeemClick);
		detailBtn.setOnClickListener(detailClick);
	}

	/** 初始化控件*/
	private void setViews() {
		numberTv = (TextView) findViewById(R.id.large_cd_number_tv);
		productCodeTv = (TextView) findViewById(R.id.large_cd_product_code_tv);
		startDateTv = (TextView) findViewById(R.id.large_cd_start_date);
		endDateTv = (TextView) findViewById(R.id.large_cd_end_date_tv);
		interestStartDateTv = (TextView) findViewById(R.id.large_cd_interest_start_date_tv);
		rateTv = (TextView) findViewById(R.id.large_cd_rate_tv);
		balanceTv = (TextView) findViewById(R.id.large_cd_balance_tv);
		statusTv = (TextView) findViewById(R.id.large_cd_status_tv);
		accTimeNumTv = (TextView) findViewById(R.id.large_cd_acc_time_number_tv);
		redeemBtn = (Button) findViewById(R.id.btn_redeem);
		detailBtn = (Button) findViewById(R.id.btn_detail);

		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, numberTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, productCodeTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, rateTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, balanceTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accTimeNumTv);

		numberTv.setText(StringUtil.valueOf1((String) purchasedDetail.get(Dept.CD_NUMBER)));
		productCodeTv.setText(StringUtil.valueOf1((String) purchasedDetail.get(Dept.PRODUCT_CODE)));

		startDate = StringUtil.valueOf1((String) purchasedDetail.get(Dept.BUY_DATE));
		endDate = StringUtil.valueOf1((String) purchasedDetail.get(Dept.BUY_END_DATE));
		String interestStartDate = StringUtil.valueOf1((String) purchasedDetail.get(Dept.INTEREST_START_DATE));
		String rate = StringUtil.valueOf1((String) purchasedDetail.get(Dept.RATE));
		String balance = StringUtil.valueOf1((String) purchasedDetail.get(Dept.CD_BALANCE));
		String status = StringUtil.valueOf1((String) purchasedDetail.get(Dept.CD_STATUS));

		startDateTv.setText(startDate);
		endDateTv.setText(endDate);
		interestStartDateTv.setText(interestStartDate);
		rateTv.setText(rate + "%"); // add by luqp 2016年3月4日 追加%号
		balanceTv.setText(StringUtil.parseStringPattern(balance, 2));

		if (status.equals(ConstantGloble.STATUS_ZERO)) {
			redeemBtn.setVisibility(View.VISIBLE);
			statusTv.setText(getString(R.string.large_cd_status_zero));
		} else {
			if (status.equals(ConstantGloble.STATUS_ONE)) {
				statusTv.setText(getString(R.string.large_cd_status_one));
			} else if (status.equals(ConstantGloble.STATUS_TWO)) {
				statusTv.setText(getString(R.string.large_cd_status_two));
			} else if (status.equals(ConstantGloble.STATUS_THREE)) {
				statusTv.setText(getString(R.string.large_cd_status_three));
			}
			redeemBtn.setVisibility(View.INVISIBLE);
		}
		accTimeNumTv.setText(String.valueOf(purchasedDetail.get(Dept.CD_ACC_NUMBER)));
	}

	private boolean getPurchasedDetail() {
		purchasedDetail = DeptDataCenter.getInstance().getPurchasedDetail();
		return StringUtil.isNullOrEmpty(purchasedDetail);
	}

	@SuppressLint("InflateParams")
	private void initViews() {
		setTitle(getString(R.string.large_cd_check_see));
		View view = mInflater.inflate(R.layout.large_cd_purchased_detail, null);
		tabcontent = (LinearLayout) findViewById(R.id.sliding_body);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 0);
		}
	}

	private OnClickListener redeemClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// BaseHttpEngine.showProgressDialog();
			// requestSystemDateTime();
			BaseDroidApp.getInstanse().showErrorDialog(getString(R.string.large_cd_not_expired), R.string.large_cd_cancel,
					R.string.large_cd_confirm, new OnClickListener() {

						@Override
						public void onClick(View v) {
							String txt = ((Button) v).getText().toString();
							if (txt.equals(getString(R.string.large_cd_cancel))) {
								BaseDroidApp.getInstanse().dismissErrorDialog();
							} else if (txt.equals(getString(R.string.large_cd_confirm))) {
								BaseDroidApp.getInstanse().dismissErrorDialog();
								BaseHttpEngine.showProgressDialog();
								requestSettledTrial();
							}
						}
					});
		}
	};

	private void requestSettledTrial() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.PSN_LARGE_CD_SETTLED_TRIAL);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Dept.CD_ACC_NUMBER, StringUtil.valueOf1((String) purchasedDetail.get(Dept.CD_ACC_NUMBER)));
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "settledTrialCallBack");
	}

	@SuppressWarnings("unchecked")
	public void settledTrialCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();

		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}

		// 利息
		double interest = Double.parseDouble((String) result.get(Dept.INTEREST));
		// 冲回利息税
		double backInterestTax = Double.parseDouble((String) result.get(Dept.BACK_INTEREST_TAX));
		// 利息税
		double interestTax = Double.parseDouble((String) result.get(Dept.INTEREST_TAX));
		// 冲回利息
		double backInterest = Double.parseDouble((String) result.get(Dept.BACK_INTEREST));

		double finalInterest = 0.0;
		finalInterest = interest + backInterestTax - interestTax - backInterest;
		showRedeemAct(finalInterest);
	}

	private OnClickListener detailClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(LargeCDPurchasedDetailActivity.this, LargeCDPurchasedTransDetailActivity.class);
			startActivity(intent);
		}
	};

	protected void showRedeemAct(double finalInterest) {
		Intent intent = new Intent();
		intent.setClass(LargeCDPurchasedDetailActivity.this, LargeCDRedeemActivity.class);
		intent.putExtra(Dept.INTEREST, finalInterest);
		startActivity(intent);
	}
}
