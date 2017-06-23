package com.chinamworld.bocmbci.biz.thridmanage.cecuritytrade;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.biz.thridmanage.BiiConstant.CurrencyType;
import com.chinamworld.bocmbci.biz.thridmanage.BiiConstant.TransferWayType;
import com.chinamworld.bocmbci.biz.thridmanage.ThirdManagerBaseActivity;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 银行资金转证券结果页
 * 
 * @author panwe
 * 
 */
public class CecurityTransferResultActivity extends ThirdManagerBaseActivity {

	/** 主布局 **/
	private View viewContent;
	/** 交易序号 */
	private TextView tvTradeNum;
	/** 资金账户 */
	private TextView tvBankAcc;
	/** 证券账户 */
	private TextView tvCecurityAcc;
	/** 转账方式 */
	private TextView tvCecType;
	/** 币种 */
	private TextView tvBiZhong;
	/** 转账金额 */
	private TextView tvAmount;

	// /** 资金账户信息 */
	// private String cecNum;
	// /** 银行账户信息 */
	// private String banNum;
	// /** 交易类型 */
	// private String type;
	// /** 交易金额 */
	// private String amout;
	// /** 币种 **/
	// private String currency;
	// /** 密码 */
	// private String password;
	// private String password_rc;
	// /** 商圈名 */
	// private String financeCompany;
	// /** 商圈代码 */
	// private String stockCode;
	// /** 资金账号 **/
	// private String capitalAcc;
	// /** 账户id **/
	// private String accId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 添加布局
		viewContent = LayoutInflater.from(this).inflate(R.layout.third_cecuritytrade_result, null);
		addView(viewContent);
		setTitle(R.string.third_cecuritytrade);
		init();
		getDate();
	}

	private void init() {
		// 右上角按钮赋值
		setRightBtnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goMainActivity();
			}
		});
		setTitleRightText(getString(R.string.go_main));
		// 隐藏返回
		findViewById(R.id.ib_back).setVisibility(View.INVISIBLE);

		tvTradeNum = (TextView) findViewById(R.id.tv_number);
		tvBankAcc = (TextView) findViewById(R.id.tv_cec_bankacc);
		tvCecurityAcc = (TextView) findViewById(R.id.tv_cec_eceacc);
		tvCecType = (TextView) findViewById(R.id.tv_cec_type);
		tvBiZhong = (TextView) findViewById(R.id.tv_cec_bizhong);
		tvAmount = (TextView) findViewById(R.id.tv_cec_acout);

		findViewById(R.id.btnconfirm).setOnClickListener(onClick);
	}

	private void getDate() {
		Bundle b = getIntent().getExtras();
		// String accId = b.getString("ACCID");
		String banNum = b.getString("BANKACCNUM");
		String cecNum = b.getString("CECACCNUM");
		String type = b.getString("TRADETYPE");
		String amout = b.getString("AMOUT");
		String currency = b.getString("CNCY");
		// String currency = b.getString("CNCY");
		// String financeCompany = b.getString("financeCompany");
		// String stockCode = b.getString("stockCode");
		// String capitalAcc = b.getString("capitalAcc");
		// if (b.containsKey("PAS")) {
		// String password = b.getString("PAS");
		// }
		// if (b.containsKey("PAS_RC")) {
		// String password_rc = b.getString("PAS_RC");
		// }
		String result = b.getString("result");

		tvBankAcc.setText(StringUtil.getForSixForString(banNum));
		tvCecurityAcc.setText(cecNum);
		tvBiZhong.setText(CurrencyType.getCurrencyTypeStr(currency));
		// if (type.equals(Third.TRADE_TYPE_BANKTOCEC)) {
		// tvCecType.setText(this.getString(R.string.third_btn_banktocecurity));
		// } else {
		// tvCecType.setText(this.getString(R.string.third_btn_cecuritytobank));
		// }
		tvCecType.setText(TransferWayType.getTransferWayTypeStr(type));
		tvAmount.setText(StringUtil.parseStringPattern(amout, 2));

		tvTradeNum.setText(result);
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvBankAcc);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvCecurityAcc);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvBiZhong);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvCecType);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvAmount);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvTradeNum);
	}

	private OnClickListener onClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			goCecurityTradeActivity();
		}
	};

	protected void titleBackClick() {
		goCecurityTradeActivity();
	};

	@Override
	public void onBackPressed() {
		goCecurityTradeActivity();
	}

	private void goCecurityTradeActivity() {
		ActivityTaskManager.getInstance().removeAllActivity();
		Intent it = new Intent(this, CecurityTradeActivity.class);
		it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(it);
		finish();
	}
}
