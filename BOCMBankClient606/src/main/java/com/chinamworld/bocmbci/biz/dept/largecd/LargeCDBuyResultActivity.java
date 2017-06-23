package com.chinamworld.bocmbci.biz.dept.largecd;

import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 购买大额存单结果
 * 
 * @author liuh
 * 
 */
public class LargeCDBuyResultActivity extends DeptBaseActivity {

	private LinearLayout tabcontent;
	private View view;
	/** 产品编码TextView */
	private TextView productCodeTv;
	/** 起购金额TextView */
	private TextView beginMoneyTv;
	/** 存期TextView */
	private TextView saveDateTv;
	/** 签约账户TextView */
	private TextView signedAccNumTv;
	/** 存款金额TextView */
	private TextView saveMoneyTv;
	/** 附言TextView */
	private TextView messageTv;
	/** 交易序号TextView */
	private TextView tranSerialNumTv;
	/** 利率TextView */
	private TextView rateTv;
	/** 完成Button */
	private Button finishBtn;

	/** 签约账户 */
	private String signedAccNum;
	/** 附言 */
	private String memo;
	/** 申购金额 */
	private String amount;
	/** 交易状态 */
	// private String status;
	/** 网银交易序号 */
	private String transactionId;
	/** 转账批次号 */
	// private String batSeq;
	/** 起购金额 */
	private String beginMoney;

	private Map<String, Object> result;

	/** 资金账户TextView */
	private TextView accNumber;
	/** 产品剩余金额TextView */
	private TextView surplusAmount;
	/** 账户类型*/
	private TextView accType ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();

		if (!getIntentData()) {
			setViews();
			setListeners();
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
				Intent intent = new Intent(LargeCDBuyResultActivity.this, LargeCDMenuActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private void setViews() {
		tranSerialNumTv = (TextView) view.findViewById(R.id.tv_large_cd_tran_serial_number);
		productCodeTv = (TextView) view.findViewById(R.id.tv_large_cd_product_code);
		rateTv = (TextView) view.findViewById(R.id.tv_large_cd_rate_two);
		beginMoneyTv = (TextView) view.findViewById(R.id.tv_large_cd_begin_money_two);
		saveDateTv = (TextView) view.findViewById(R.id.tv_large_cd_save_date);
		signedAccNumTv = (TextView) view.findViewById(R.id.tv_large_cd_acc_number);
		saveMoneyTv = (TextView) view.findViewById(R.id.tv_large_cd_save_money);
		messageTv = (TextView) view.findViewById(R.id.tv_large_cd_message);

		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, messageTv);
		finishBtn = (Button) view.findViewById(R.id.btn_finish);

		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tranSerialNumTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, productCodeTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, rateTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, beginMoneyTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, signedAccNumTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, saveMoneyTv);

		signedAccNum = StringUtil.valueOf1((String) DeptDataCenter.getInstance().getSignedAcc().get(Dept.ACCOUNT_NUMBER));

		// //////////////////////////////////////////////////////////////////////////////
		// add by 2016年3月2日 资金账户
		accNumber = (TextView) view.findViewById(R.id.tv_large_cd_capital_acc);
		accNumber.setText(StringUtil.getForSixForString(signedAccNum));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accNumber);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, saveMoneyTv);
		
		// add by luqp 2016年5月3日   账户后面添加账户类型    如:1005****8561 活期一本通 
		accType = (TextView) view.findViewById(R.id.tv_acc_type);
		String accTypeStr  = StringUtil.valueOf1((String) DeptDataCenter.getInstance().getSignedAcc().get(Dept.LargeSign_accountType));
		String accountType = LocalData.AccountType.get(accTypeStr);
		accType.setText(accountType);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accType);
		
		// add by 2016年3月2日 产品剩余金额
		surplusAmount = (TextView) view.findViewById(R.id.tv_large_cd_surplus_amount);
		String availableLimit = StringUtil.valueOf1((String) result.get(Dept.AVAIL_QUOTA));
		// add by 2016年3月16日 剩余金额减去输入金额 显示产品剩余金额
		String surplusAmounts = getSurplusAmount(availableLimit, amount);
		surplusAmount.setText(StringUtil.parseStringPattern(surplusAmounts, 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, surplusAmount);
		// //////////////////////////////////////////////////////////////////////////////
		beginMoney = (String) result.get(Dept.BEGIN_MONEY);
		String rate = (String) result.get(Dept.RATE);
		// 基期
		String periodType = StringUtil.valueOf1((String) result.get(Dept.PERIOD_TYPE));

		signedAccNumTv.setText(StringUtil.getForSixForString(signedAccNum));
		tranSerialNumTv.setText(transactionId);
		productCodeTv.setText(StringUtil.valueOf1((String) result.get(Dept.PRODUCT_CODE)));
		rateTv.setText(rate+"%"); // add by luqp 2016年3月4日 追加%号
		beginMoneyTv.setText(StringUtil.parseStringPattern(beginMoney, 2));
		if (periodType.equalsIgnoreCase(ConstantGloble.PERIOD_TYPE_MONTH)) {
			saveDateTv.setText(String.valueOf(result.get(Dept.CD_TERM)) + getString(R.string.month));
		} else if (periodType.equalsIgnoreCase(ConstantGloble.PERIOD_TYPE_DAY)) {
			saveDateTv.setText(String.valueOf(result.get(Dept.CD_TERM)) + getString(R.string.day));
		} else {
			saveDateTv.setText("-");
		}
		saveMoneyTv.setText(StringUtil.parseStringPattern(amount, 2));
		if (!StringUtil.isNullOrEmpty(memo)) {
			messageTv.setText(memo);
		}
	}

	private boolean getIntentData() {
		DeptDataCenter data = DeptDataCenter.getInstance();
		Intent intent = getIntent();
		amount = intent.getStringExtra(Dept.AMOUNT);
		memo = intent.getStringExtra(Dept.MEMO);
		// batSeq = intent.getStringExtra(Dept.BATSEQ);
		transactionId = intent.getStringExtra(Dept.TRANSACTION_ID);
		// status = intent.getStringExtra(Dept.STATUS);
		result = (Map<String, Object>) data.getAvailableDetial();
		return StringUtil.isNullOrEmpty(result);
	}

	@SuppressLint("InflateParams")
	private void initViews() {
		setTitle(getString(R.string.large_cd_add_title));
		ibBack.setVisibility(View.GONE);
		LayoutInflater inflater = LayoutInflater.from(this);
		tabcontent = (LinearLayout) findViewById(R.id.sliding_body);
		view = inflater.inflate(R.layout.large_cd_buy_finish, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 0);
		}

	}

}
