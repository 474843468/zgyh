package com.chinamworld.bocmbci.biz.crcd.mycrcd;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.acc.mybankaccount.AccManageActivity;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.transquery.CrcdHasQueryListActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.transquery.CrcdHasTransQueryListActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.transquery.CrcdNoTransQueryListActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 账单分期成功界面
 * 
 * @author huangyuchao
 * 
 */
public class CrcdAccountDividedSuccessActivity extends CrcdBaseActivity {
	private static final String TAG = "CrcdAccountDividedSuccessActivity";
	private View view;

	private TextView tv_card_type, tv_card_number, tv_card_step;

	private TextView mycrcd_selected_creditcard, mycrcd_accounted_money, rate_fix_sellCode, rate_currency_buyCode,
			rate_currency_type, rate_fix_papRate, rate_fix_comRate, crcd_current_money, crcd_current_lowmoney;

	private Button sureButton;
	protected static List<Map<String, Object>> transList;
	protected static String money;
	protected static String instmtCharge;

	protected static String chargeMode;
	protected static String firstAmount;
	protected static String restPerTimeInAmount;
	protected static String restAmount;
	protected static String minimalAmountNow;
	private TextView moneyText = null;
	private TextView firstText = null;
	private TextView everyText = null;
	private TextView leftText = null;
	private TextView lowText = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_account_divide));
		view = addView(R.layout.crcd_trans_account_divide_success);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		back.setVisibility(View.GONE);
		init();

	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

	public void init() {
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getResources().getString(R.string.mycrcd_bill_divide_setup),
						this.getResources().getString(R.string.mycrcd_divide_confirm),
						this.getResources().getString(R.string.mycrcd_divide_setup_success) });
		StepTitleUtils.getInstance().setTitleStep(3);

		tv_card_type = (TextView) view.findViewById(R.id.tv_card_type);
		tv_card_number = (TextView) view.findViewById(R.id.tv_card_number);
		tv_card_step = (TextView) view.findViewById(R.id.tv_card_step);

		crcd_current_money = (TextView) view.findViewById(R.id.crcd_current_money);
		crcd_current_lowmoney = (TextView) view.findViewById(R.id.crcd_current_lowmoney);

		tv_card_type.setText(String.valueOf(currentBankList.get(Crcd.CRCD_ACCOUNTNAME_RES)));
		tv_card_number.setText(StringUtil.getForSixForString(CrcdAccountDividedActivity.accountNum));
		tv_card_step.setText(String.valueOf(currentBankList.get(Crcd.CRCD_NICKNAME_RES)));

		mycrcd_selected_creditcard = (TextView) view.findViewById(R.id.mycrcd_selected_creditcard);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mycrcd_selected_creditcard);
		mycrcd_accounted_money = (TextView) view.findViewById(R.id.mycrcd_accounted_money);

		mycrcd_selected_creditcard.setText(CrcdAccountDividedActivity.strCurrencyCode);
		money = CrcdAccountDividedActivity.money;
		mycrcd_accounted_money.setText(StringUtil.parseStringPattern(money, 2));

		rate_fix_sellCode = (TextView) view.findViewById(R.id.rate_fix_sellCode);
		rate_currency_buyCode = (TextView) view.findViewById(R.id.rate_currency_buyCode);
		rate_currency_type = (TextView) view.findViewById(R.id.rate_currency_type);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, rate_currency_type);
		rate_fix_papRate = (TextView) view.findViewById(R.id.rate_fix_papRate);
		rate_fix_comRate = (TextView) view.findViewById(R.id.rate_fix_comRate);

		moneyText = (TextView) findViewById(R.id.money_text);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, moneyText);
		firstText = (TextView) findViewById(R.id.first);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, firstText);
		everyText = (TextView) findViewById(R.id.every);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, everyText);
		lowText = (TextView) findViewById(R.id.left);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, lowText);
		leftText = (TextView) findViewById(R.id.low);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, leftText);

		rate_fix_sellCode.setText(CrcdAccountDividedActivity.dividedNum);
		instmtCharge = String.valueOf(CrcdAccountDividedActivity.result.get(Crcd.CRCD_INSTMTCHARGE));

		rate_currency_buyCode.setText(StringUtil.parseStringPattern(instmtCharge, 2));
		chargeMode = String.valueOf(CrcdAccountDividedActivity.result.get(Crcd.CRCD_CHARGEMODE));

		rate_currency_type.setText(CrcdAccountDividedActivity.strDividedType);

		firstAmount = String.valueOf(CrcdAccountDividedActivity.result.get(Crcd.CRCD_FIRSTINAMOUNT));

		restPerTimeInAmount = String.valueOf(CrcdAccountDividedActivity.result.get(Crcd.CRCD_RESETPERTIEINAMOUNT));

		rate_fix_papRate.setText(StringUtil.parseStringPattern(firstAmount, 2));
		rate_fix_comRate.setText(StringUtil.parseStringPattern(restPerTimeInAmount, 2));

		restAmount = String.valueOf(CrcdAccountDividedActivity.result.get(Crcd.CRCD_RESTAMOUNT));
		minimalAmountNow = String.valueOf(CrcdAccountDividedActivity.result.get(Crcd.CRCD_MINIMALAMOUNTNOW));
		crcd_current_money.setText(StringUtil.parseStringPattern(restAmount, 2));
		// 本期账单最低还款额度
		crcd_current_lowmoney.setText(StringUtil.parseStringPattern(minimalAmountNow, 2));

		sureButton = (Button) view.findViewById(R.id.trade_sureButton);

		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (StringUtil.isNullOrEmpty(BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.IS_EBANK_0))) {
					Intent it = new Intent(CrcdAccountDividedSuccessActivity.this, CrcdHasQueryListActivity.class);
					it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(it);
					finish();
					return;
				}
				int tag = (Integer) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.IS_EBANK_0);
				if (tag == 0) {
					// 未出账单
					Intent it = new Intent(CrcdAccountDividedSuccessActivity.this, CrcdNoTransQueryListActivity.class);
					it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(it);
					finish();
				} else if (tag == 1) {
					// 已出账单
					Intent it = new Intent(CrcdAccountDividedSuccessActivity.this, CrcdHasTransQueryListActivity.class);
					it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(it);
					finish();
				} else if (tag == 2) {
					// 我的信用卡
					Intent it = new Intent(CrcdAccountDividedSuccessActivity.this, MyCreditCardActivity.class);
					it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(it);
					finish();
				} else if (tag == 3) {
					Intent it = new Intent(CrcdAccountDividedSuccessActivity.this, CrcdHasQueryListActivity.class);
					it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(it);
					finish();
				}else if(tag == 4){
					//账户管理
					ActivityTaskManager.getInstance().removeAllActivity();
					Intent it = new Intent(CrcdAccountDividedSuccessActivity.this, AccManageActivity.class);
					it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(it);
					finish();
				}
			}
		});

	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
	}

}
