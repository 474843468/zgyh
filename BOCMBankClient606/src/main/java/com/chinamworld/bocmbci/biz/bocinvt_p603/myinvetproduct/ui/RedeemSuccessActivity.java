package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt.myproduct.MyProductListActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.RedeemInfo;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.userwidget.LabelTextView.TextColor;
import com.chinamworld.bocmbci.utils.DateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 赎回成功页面
 * 
 * @author HVZHUNG
 *
 */
public class RedeemSuccessActivity extends BocInvtBaseActivity {

	private static final String KEY_REDEEM_INFO = "redeem_info";
	private static final String TAG = RedeemSuccessActivity.class
			.getSimpleName();
	/** 交易序号 */
	private LabelTextView ltv_transaction_code;
	/** 提交申请时间 */
	private LabelTextView ltv_request_date;
	/** 产品代码 */
	private LabelTextView ltv_product_code;
	/** 产品名称 */
	private LabelTextView ltv_product_name;
	/** 产品币种 */
	private LabelTextView ltv_product_currency;
	/** 赎回份额 */
	private LabelTextView ltv_redeem_quantity;
	/** 赎回金额 */
	private LabelTextView ltv_redeem_amount;
	/** 指定赎回日期 */
	private LabelTextView ltv_redeem_date;
	/** 钞汇 */
	private LabelTextView ltv_cashRemit;

	/** 完成按钮 */
	private Button bt_finish;
	private RedeemInfo redeemInfo;
	/** 提示信息*/
	private TextView tv_reminder;
	/** 钞汇 */
	private String cashRemit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bocinvt_redeem_success_activity_p603);
		setTitle(R.string.bocinvt_holding_detail_redeem);
//		getBackgroundLayout().setRightButtonText(null);
		getBackgroundLayout().setLeftButtonText(null);
		initView();
		redeemInfo = (RedeemInfo) getIntent().getSerializableExtra(
				KEY_REDEEM_INFO);
		cashRemit = getIntent().getStringExtra("cashRemit");
		if (redeemInfo == null) {
			LogGloble.e(TAG, "RedeemInfo is null !!!");
			return;

		}
		setViewContent(redeemInfo);
	}

	private void setViewContent(RedeemInfo redeemInfo) {
		ltv_transaction_code.setValueText(redeemInfo.transactionId);
		ltv_request_date.setValueText(redeemInfo.paymentDate);
		ltv_product_code.setValueText(redeemInfo.prodCode);
		ltv_product_name.setValueText(redeemInfo.prodName);
		ltv_product_currency.setValueText(redeemInfo.currencyInfo.name);
//		ltv_redeem_quantity.setValueText(StringUtil.parseStringCodePattern(redeemInfo.currencyInfo.numberCode,redeemInfo.trfAmount.toString(), 2));
		ltv_redeem_quantity.setValueText(StringUtil.parseStringPattern(redeemInfo.trfAmount.toString(), 2));
//		ltv_redeem_amount.setValueText(redeemInfo.redeemAmount);
		ltv_redeem_date.setValueText(redeemInfo.transDate);
//		ltv_cashRemit.setValueText("-");
		if (!StringUtil.isNull(LocalData.Currency.get(redeemInfo.currencyInfo.numberCode))) {
			if (LocalData.Currency.get(redeemInfo.currencyInfo.numberCode).equals(ConstantGloble.ACC_RMB)) {
				ltv_cashRemit.setValueText("-");
			}else{
				ltv_cashRemit.setValueText(BociDataCenter.cashRemitMapValue2.get(cashRemit));
			}
		}
		String transDate = (String)redeemInfo.transDate;
		if(!StringUtil.isNullOrEmpty(transDate)){
			transDate = DateUtils.formatStr(transDate);
		}
		String reminder = "根据您的选择，预计将于"+transDate+"发起赎回；如遇节假日，您的赎回交易申请将顺延至下一交易日进行。";
			
		tv_reminder.setText(reminder);
	}

	private void initView() {
		ltv_transaction_code = (LabelTextView) findViewById(R.id.ltv_transaction_code);
		ltv_request_date = (LabelTextView) findViewById(R.id.ltv_request_date);
		ltv_product_code = (LabelTextView) findViewById(R.id.ltv_product_code);
		ltv_product_name = (LabelTextView) findViewById(R.id.ltv_product_name);
		ltv_product_currency = (LabelTextView) findViewById(R.id.ltv_product_currency);
		ltv_redeem_quantity = (LabelTextView) findViewById(R.id.ltv_redeem_quantity);
		ltv_redeem_quantity.setValueTextColor(TextColor.Red);
		ltv_redeem_amount = (LabelTextView) findViewById(R.id.ltv_redeem_amount);
		ltv_redeem_date = (LabelTextView) findViewById(R.id.ltv_redeem_date);
		ltv_redeem_date.setVisibility(BociDataCenter.isRedeem ? View.VISIBLE : View.GONE);
		ltv_cashRemit = (LabelTextView) findViewById(R.id.ltv_cashRemit);
		bt_finish = (Button) findViewById(R.id.bt_finish);
		bt_finish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				Intent intent = new Intent();
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setClass(RedeemSuccessActivity.this,
						MyProductListActivity.class);
				startActivity(intent);

			}
		});
		tv_reminder = (TextView) findViewById(R.id.tv_reminder);
		tv_reminder.setVisibility(BociDataCenter.isRedeem ? View.GONE : View.VISIBLE);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 屏蔽返回键
			return false;
		}
		return super.onKeyDown(keyCode, event);

	}

	public static Intent getIntent(Context context, RedeemInfo redeemInfo,String cashRemit) {
		Intent intent = new Intent(context, RedeemSuccessActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(KEY_REDEEM_INFO, redeemInfo);
		intent.putExtra("cashRemit", cashRemit);
		return intent;
	}
}
