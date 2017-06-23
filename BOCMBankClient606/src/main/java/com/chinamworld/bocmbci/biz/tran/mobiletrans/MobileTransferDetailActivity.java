package com.chinamworld.bocmbci.biz.tran.mobiletrans;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.Map;

/**
 * 手机号转账查询详情页
 * 
 * @author wangmengmeng
 * 
 */
public class MobileTransferDetailActivity extends TranBaseActivity {
	private View view;
	/** 指令流水号 */
	private TextView tv_transactionId;
	/** 初次提交日期 */
	private TextView tv_firstSubmitDate;
	/** 处理日期 */
	private TextView tv_handleDate;
	/** 转出账户 */
	private TextView tv_payerAccountNumber;
	/** 收款人姓名 */
	private TextView tv_payeeName;
	/** 收款人手机号码 */
	private TextView tv_payeeMobile;
	/** 收款人账户 */
	private TextView tv_payeeAccountNumber;
	/** 转账金额 */
	private TextView tv_amount;
	/** 币种 */
	private TextView tv_currency;
	/** 备注 */
	private TextView tv_furInfo;
	/** 失败原因 */
	private TextView tv_failReason;
	/** 状态 */
	private TextView tv_status;
	/** 详情 */
	private Map<String, Object> detailMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.tran_result_query));
		view = addView(R.layout.tran_mobile_records_detail_activity);
		// if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
		// ((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0,
		// 0, 0);
		// }
		setLeftSelectedPosition("tranManager_2");
		// 步骤栏没有显示 但是这个代码不要删除 会影响布局
		// StepTitleUtils.getInstance().initTitldStep(this,
		// this.getResources().getStringArray(R.array.tran_my_trans));
		// StepTitleUtils.getInstance().setTitleStep(2);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		mTopRightBtn.setText(this.getString(R.string.go_main));
		mTopRightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//						MainActivity.class);
//				BaseDroidApp.getInstanse().getCurrentAct()
//						.startActivity(intent);
				goToMainActivity();
			}
		});
		detailMap = TranDataCenter.getInstance().getMobileTransDetailMap();
		init();
	}

	public void init() {
		/** 指令流水号 */
		tv_transactionId = (TextView) view.findViewById(R.id.tv_transactionId);
		tv_transactionId.setText((String) detailMap
				.get(Tran.MOBILE_TRANSACTIONID_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_transactionId);
		/** 初次提交日期 */
		tv_firstSubmitDate = (TextView) view
				.findViewById(R.id.tv_firstSubmitDate);
		tv_firstSubmitDate.setText((String) detailMap
				.get(Tran.MOBILE_FIRSTSUBMITDATE_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_firstSubmitDate);
		/** 处理日期 */
		tv_handleDate = (TextView) view.findViewById(R.id.tv_handleDate);
		tv_handleDate.setText((String) detailMap
				.get(Tran.MOBILE_HANDLEDATE_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_handleDate);
		/** 转出账户 */
		tv_payerAccountNumber = (TextView) view
				.findViewById(R.id.tv_payerAccountNumber);
		String outNumber = (String) detailMap
				.get(Tran.MOBILE_PAYERACCOUNTNUMBER_RES);
		tv_payerAccountNumber.setText(StringUtil.getForSixForString(outNumber));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_payerAccountNumber);
		/** 收款人姓名 */
		tv_payeeName = (TextView) view.findViewById(R.id.tv_payeeName);
		tv_payeeName.setText((String) detailMap.get(Tran.MOBILE_PAYEENAME_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_payeeName);
		/** 收款人手机号码 */
		tv_payeeMobile = (TextView) view.findViewById(R.id.tv_payeeMobile);
		tv_payeeMobile.setText((String) detailMap
				.get(Tran.MOBILE_PAYEEMOBILE_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_payeeMobile);
		/** 收款人账户 */
		tv_payeeAccountNumber = (TextView) view
				.findViewById(R.id.tv_payeeAccountNumber);
		String inNumber = (String) detailMap
				.get(Tran.MOBILE_PAYEEACCOUNTNUMBER_RES);
		tv_payeeAccountNumber.setText(StringUtil.getForSixForString(inNumber));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_payeeAccountNumber);
		/** 转账金额 */
		tv_amount = (TextView) view.findViewById(R.id.tv_amount);
		String amount = (String) detailMap.get(Tran.MOBILE_AMOUNT_RES);
		tv_amount.setText(StringUtil.parseStringPattern(amount, 2));
		PopupWindowUtils.getInstance()
				.setOnShowAllTextListener(this, tv_amount);
		/** 币种 */
		tv_currency = (TextView) view.findViewById(R.id.tv_currency);
		String currency = (String) detailMap.get(Tran.MOBILE_CURRENCY_RES);
		tv_currency.setText(LocalData.Currency.get(currency));
		/** 备注 */
		tv_furInfo = (TextView) view.findViewById(R.id.tv_furInfo);
		String furInfo = (String) detailMap.get(Tran.MOBILE_FURINFO_RES);
		tv_furInfo
				.setText(StringUtil.isNull(furInfo) ? ConstantGloble.BOCINVT_DATE_ADD
						: furInfo);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_furInfo);
		/** 失败原因 */
		tv_failReason = (TextView) view.findViewById(R.id.tv_failReason);
		tv_failReason.setText((String) detailMap
				.get(Tran.MOBILE_FAILREASON_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_failReason);
		/** 状态 */
		tv_status = (TextView) view.findViewById(R.id.tv_status);
		String status = (String) detailMap.get(Tran.MOBILE_STATUS_RES);
		tv_status.setText(LocalData.mobileTranDownStatus.get(status));
		if (!StringUtil.isNull(LocalData.mobileTranDownStatus.get(status))) {
			if (LocalData.mobileTranDownStatus.get(status).equals(
					LocalData.mobileTransStatusUpList.get(2))) {
				// 交易失败显示失败原因
				LinearLayout ll_fail = (LinearLayout) view
						.findViewById(R.id.ll_fail);
				ll_fail.setVisibility(View.VISIBLE);
			} else {
				// 不显示失败原因
				LinearLayout ll_fail = (LinearLayout) view
						.findViewById(R.id.ll_fail);
				ll_fail.setVisibility(View.GONE);
			}
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
