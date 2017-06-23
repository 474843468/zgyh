package com.chinamworld.bocmbci.biz.dept.largecd;

import java.util.ArrayList;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.MathUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 购买大额存单
 * 
 * @author liuh
 * 
 */
public class LargeCDBuyActivity extends DeptBaseActivity {
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
	/** 存款金额EditText */
	private EditText saveMoneyEt;
	/** 附言EditText */
	private EditText messageEt;
	/** 提示TextView */
	private TextView buyTipTv;
	/** 购买Button */
	private Button buyBtn;

	/** 申购金额 */
	private String amount;
	/** 附言 */
	private String memo;
	/** 起购金额 */
	private String beginMoney;
	/** 额度最小基数 */
	private String quotaBase;
	/** 提示信息 */
	private String tipStr;
	private Map<String, Object> result;
	/** 资金账户TextView */
	private TextView accNumber;
	/** 产品剩余金额TextView */
	private TextView surplusAmount;
	/** 利率TextView*/
	private TextView rateTv;
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
		buyBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				amount = saveMoneyEt.getText().toString().trim();
				memo = messageEt.getText().toString().trim();
				if (!checkAmount(amount)) {
					return;
				}
				Intent intent = new Intent();
				intent.setClass(LargeCDBuyActivity.this, LargeCDBuyConfirmActivity.class);
				intent.putExtra(Dept.AMOUNT, amount);
				intent.putExtra(Dept.MEMO, memo);
				startActivity(intent);
			}
		});
	}

	/**
	 * 校验存款金额 
	 * @param amount
	 * @return
	 */
	protected boolean checkAmount(String amount) {
		RegexpBean regAmount = new RegexpBean(this.getResources().getString(R.string.save_money_message), amount,
				"tranAmount");
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		lists.add(regAmount);
		if (!RegexpUtils.regexpDate(lists)) {// 校验不通过
			return false;
		}
		Double inPutMoney = Double.parseDouble(amount);
		Double floorMoney = Double.parseDouble(beginMoney);
		Double baseMoney = Double.parseDouble(quotaBase);
		// 存款金额小于起购金额则校验不通过
		if (inPutMoney < floorMoney) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(tipStr);
			return false;
		}
		// 校验是否按照quotaBase的整数倍递增 2016年3月3日17:40:17 luqp 注
//		if (!MathUtils.isMultiple(inPutMoney, floorMoney, baseMoney)) {
//			BaseDroidApp.getInstanse().showInfoMessageDialog(tipStr);
//			return false;
//		}
		
		// 校验是否按照quotaBase的整数倍递增 add by 2016年3月3日17:40:17 luqp
		if (!MathUtils.isInPutMoneyMultiple(inPutMoney, floorMoney, baseMoney)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(tipStr);
			return false;
		}
		
		return true;
	}

	private void setViews() {
		productCodeTv = (TextView) view.findViewById(R.id.tv_large_cd_product_code);
		saveDateTv = (TextView) view.findViewById(R.id.tv_large_cd_save_date);
		beginMoneyTv = (TextView) view.findViewById(R.id.tv_large_cd_begin_money_two);
		signedAccNumTv = (TextView) view.findViewById(R.id.tv_large_cd_acc_number);
		saveMoneyEt = (EditText) view.findViewById(R.id.et_large_cd_save_money);
		buyTipTv = (TextView) view.findViewById(R.id.large_cd_buy_tip);
		messageEt = (EditText) view.findViewById(R.id.et_large_cd_message);
		buyBtn = (Button) view.findViewById(R.id.btn_next);
		
		///////////////////////////////////////////////////////////////////////////
		//资金账户
//		String signedAccStr = String.valueOf(DeptDataCenter.getInstance().getSignedAcc().get(Dept.ACCOUNT_NUMBER));
//		accNumber = (TextView) view.findViewById(R.id.tv_large_cd_capital_acc);
//		accNumber.setText(StringUtil.getForSixForString(signedAccStr));
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, saveMoneyTv);
		
		// add by 2016年3月2日 添加资金账户
		Map<String, Object> signedAcc = DeptDataCenter.getInstance().getSignedAcc();
		accNumber = (TextView) view.findViewById(R.id.tv_large_cd_capital_acc);
		String number = (String) signedAcc.get(Dept.ACCOUNT_NUMBER);
		accNumber.setText(StringUtil.getForSixForString(number));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accNumber);
		
		// add by luqp 2016年5月3日   账户后面添加账户类型    如:1005****8561 活期一本通 
		accType = (TextView) view.findViewById(R.id.tv_acc_type);
		String accTypeStr = (String) signedAcc.get(Dept.LargeSign_accountType);
		String accountType =LocalData.AccountType.get(accTypeStr);
		accType.setText(accountType);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accType);
		
		// add by 2016年3月2日 产品剩余金额
		surplusAmount = (TextView) view.findViewById(R.id.tv_large_cd_surplus_amount);
		String availableLimit = StringUtil.valueOf1((String) result.get(Dept.AVAIL_QUOTA));
		surplusAmount.setText(StringUtil.parseStringPattern(availableLimit, 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, surplusAmount);
		
		// add by 2016年3月2日 利率
		rateTv = (TextView) view.findViewById(R.id.tv_large_cd_rate);
		String rate = StringUtil.valueOf1((String) result.get(Dept.RATE));
		rateTv.setText(rate+"%"); // add by luqp 2016年3月4日 追加%号
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, rateTv);
		///////////////////////////////////////////////////////////////////////////
		

		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, beginMoneyTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, productCodeTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, signedAccNumTv);
		EditTextUtils.setLengthMatcher(this, messageEt, 50);

		String signedAccStr = String.valueOf(DeptDataCenter.getInstance().getSignedAcc().get(Dept.ACCOUNT_NUMBER));
		// 基期
		String periodType = (String) result.get(Dept.PERIOD_TYPE);
		beginMoney = (String) result.get(Dept.BEGIN_MONEY);
		quotaBase = (String) result.get(Dept.QUOTA_BASE);
		signedAccNumTv.setText(StringUtil.getForSixForString(signedAccStr));
		productCodeTv.setText(StringUtil.valueOf1((String) result.get(Dept.PRODUCT_CODE)));
		beginMoneyTv.setText(StringUtil.parseStringPattern(beginMoney, 2));
		// 存款金额不能小于起购金额，且按照(quotaBase接口返回字段)的整数倍递增
		tipStr = getString(R.string.large_cd_buy_tip_new) + StringUtil.parseStringPattern(quotaBase, 2)
				+ getString(R.string.large_cd_buy_tip_end);
		buyTipTv.setText(tipStr);

		if (periodType.equalsIgnoreCase(ConstantGloble.PERIOD_TYPE_MONTH)) {
			saveDateTv.setText(String.valueOf(result.get(Dept.CD_TERM)) + getString(R.string.month));
		} else if (periodType.equalsIgnoreCase(ConstantGloble.PERIOD_TYPE_DAY)) {
			saveDateTv.setText(String.valueOf(result.get(Dept.CD_TERM)) + getString(R.string.day));
		} else {
			saveDateTv.setText("-");
		}
	}

	private boolean getIntentData() {
		DeptDataCenter data = DeptDataCenter.getInstance();
		result = (Map<String, Object>) data.getAvailableDetial();
		return StringUtil.isNullOrEmpty(result);
	}

	@SuppressLint("InflateParams")
	private void initViews() {
		setTitle(getString(R.string.large_cd_add_title));

		LayoutInflater inflater = LayoutInflater.from(this);
		tabcontent = (LinearLayout) findViewById(R.id.sliding_body);
		view = inflater.inflate(R.layout.large_cd_buy, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 0);
		}

	}
}
