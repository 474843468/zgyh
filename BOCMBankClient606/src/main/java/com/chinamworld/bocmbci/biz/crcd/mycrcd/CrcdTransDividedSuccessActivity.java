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
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.transquery.CrcdXiaofeiQueryListActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.transquery.MyCardTransMenuActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 消费分期成功界面
 * 
 * @author huangyuchao
 * 
 */
public class CrcdTransDividedSuccessActivity extends CrcdBaseActivity {
	private static final String TAG = "CrcdTransDividedSuccessActivity";
	private View view;

	private TextView tv_card_type, tv_card_number, tv_card_step;

	private TextView mycrcd_accounted_type, mycrcd_selected_creditcard, mycrcd_accounted_money, rate_fix_sellCode,
			rate_currency_buyCode, rate_currency_type, rate_fix_papRate, rate_fix_comRate;

	private Button sureButton;
	private List<Map<String, Object>> transList;
	private String money;
	private String instmtCharge;
	private String firstAmount;
	private String restPerTimeInAmount;
	private TextView moneyText = null;
	private TextView firstText = null;
	private TextView everyText = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_sub_divide));
		// 右上角按钮赋值
		// setText(this.getString(R.string.close));

		view = addView(R.layout.crcd_trans_divide_success);
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
				new String[] { this.getResources().getString(R.string.mycrcd_divide_setup),
						this.getResources().getString(R.string.mycrcd_divide_confirm),
						this.getResources().getString(R.string.mycrcd_divide_setup_success) });
		StepTitleUtils.getInstance().setTitleStep(3);

		tv_card_type = (TextView) view.findViewById(R.id.tv_card_type);
		tv_card_number = (TextView) view.findViewById(R.id.tv_card_number);
		tv_card_step = (TextView) view.findViewById(R.id.tv_card_step);

		moneyText = (TextView) findViewById(R.id.money_text);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, moneyText);
		firstText = (TextView) findViewById(R.id.first);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, firstText);
		everyText = (TextView) findViewById(R.id.every);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, everyText);

		tv_card_type.setText(String.valueOf(currentBankList.get(Crcd.CRCD_ACCOUNTNAME_RES)));
		tv_card_number.setText(StringUtil.getForSixForString(String.valueOf(currentBankList
				.get(Crcd.CRCD_ACCOUNTNUMBER_RES))));
		tv_card_step.setText(String.valueOf(currentBankList.get(Crcd.CRCD_NICKNAME_RES)));

		mycrcd_accounted_type = (TextView) view.findViewById(R.id.mycrcd_accounted_type);
		mycrcd_selected_creditcard = (TextView) view.findViewById(R.id.mycrcd_selected_creditcard);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mycrcd_selected_creditcard);
		mycrcd_accounted_money = (TextView) view.findViewById(R.id.mycrcd_accounted_money);

		transList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CRCE_XIAOFEI_RESULT);

		mycrcd_accounted_type.setText(String.valueOf(transList.get(CrcdTransDividedActivity.position).get(
				Crcd.CRCD_TRANSDESC)));
		mycrcd_selected_creditcard.setText(CrcdTransDividedActivity.strCurrencyCode);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mycrcd_accounted_type);
		money = String.valueOf(transList.get(CrcdTransDividedActivity.position).get(Crcd.CRCD_CLEARINGAMOUNT));
		mycrcd_accounted_money.setText(StringUtil.parseStringPattern(money, 2));

		rate_fix_sellCode = (TextView) view.findViewById(R.id.rate_fix_sellCode);
		rate_currency_buyCode = (TextView) view.findViewById(R.id.rate_currency_buyCode);
		rate_currency_type = (TextView) view.findViewById(R.id.rate_currency_type);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, rate_currency_type);
		rate_fix_papRate = (TextView) view.findViewById(R.id.rate_fix_papRate);
		rate_fix_comRate = (TextView) view.findViewById(R.id.rate_fix_comRate);
		Map<String, Object> result = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CRCE_XIAOFEI_CONFIRM_RESULT);

		rate_fix_sellCode.setText(CrcdTransDividedActivity.dividedNum);
		instmtCharge = (String) result.get(Crcd.CRCD_INSTMTCHARGE);

		rate_currency_buyCode.setText(StringUtil.parseStringPattern(instmtCharge, 2));
		rate_currency_type.setText(CrcdTransDividedActivity.strDividedType);

		firstAmount = (String) result.get(Crcd.CRCD_FIRSTINAMOUNT);

		restPerTimeInAmount = (String) result.get(Crcd.CRCD_RESETPERTIEINAMOUNT);

		rate_fix_papRate.setText(StringUtil.parseStringPattern(firstAmount, 2));
		rate_fix_comRate.setText(StringUtil.parseStringPattern(restPerTimeInAmount, 2));

		sureButton = (Button) view.findViewById(R.id.trade_sureButton);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("fromXiaoFei".equals(MyCardTransMenuActivity.goType)) {
					Intent it = new Intent(CrcdTransDividedSuccessActivity.this, CrcdXiaofeiQueryListActivity.class);
					it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(it);
					// ActivityTaskManager.getInstance().removeAllActivity();
				} else {
					Intent it = new Intent(CrcdTransDividedSuccessActivity.this, MyCreditCardActivity.class);
					it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(it);
					// ActivityTaskManager.getInstance().removeAllActivity();
				}
			}
		});

	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
	}

}
