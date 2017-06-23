package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 信用卡还款方式
 * 
 * @author huangyuchao
 * 
 */
public class CrcdPaymentwaySetup extends CrcdBaseActivity {

	private View view;
	RadioGroup rg_bizhong_setup;
	/** 所有结欠均以人民币还款 */
	RadioButton rb_renminbihuan;
	/** 所有结欠均以外币还款 */
	RadioButton rb_waibihuan;
	/** 人民币和外币结欠分别以相应账户还款 */
	RadioButton rb_renminbiandwaibi;
	/** 人民币还款账户 Spinner */
	Spinner forex_rate_currency_buylCode;
	/** 外汇还款账户 Spinner */
	Spinner forex_rate_currency_type;
	RadioGroup rg_money_setup;
	RadioButton rb_allmoney;
	RadioButton rb_lowmoney;

	Button nextButton;
	/** 人民币还款账户区域 */
	static LinearLayout ll_rmbShow;
	/** 外币还款账户区域 */
	static LinearLayout ll_foreignShow;
	/** FULL = 全额还款,MINP = 最低还款额还款 */
	static String autoRepayMode;
	/** 0 = 所有结欠均以人民币还款,1 =人民币与外币结欠分别以相应账户还款,2=外币结欠以相应账户还款 */
	static int repayCurSel;
	static String strRepayCurSel;
	static String strautoRepayMode;

	static String benNumber;
	static String waiNumber;
	ArrayAdapter<String> benAdapter;
	ArrayAdapter<String> waiAdapter;

	String currency1 = "";
	String currency2 = "";
	private String accountId = null;
	private String accountNumber = null;
	private String codeCode1 = null;
	private String codeCode2 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_creditcard_money_setup_title));
		view = addView(R.layout.crcd_payment_setup);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		codeCode1 = getIntent().getStringExtra(Crcd.CRCD_CURRENCY1);
		codeCode2 = getIntent().getStringExtra(Crcd.CRCD_CURRENCY2);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		init();

	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

	public static String getBenNumber() {
		return benNumber;
	}

	public static String getWaiNumber() {
		return waiNumber;
	}

	/** 初始化界面 */
	private void init() {
		autoRepayMode = "FULL";
		strautoRepayMode = getString(R.string.mycrcd_allmoney_huan);
		currency1 = MyCrcdSetupReadActivity.currency1;
		currency2 = MyCrcdSetupReadActivity.currency2;
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getResources().getString(R.string.mycrcd_setup_style),
						this.getResources().getString(R.string.mycrcd_setup_info),
						this.getResources().getString(R.string.mycrcd_setup_success) });
		StepTitleUtils.getInstance().setTitleStep(1);
		ll_foreignShow = (LinearLayout) view.findViewById(R.id.ll_foreignShow);
		ll_rmbShow = (LinearLayout) view.findViewById(R.id.ll_rmbShow);
		rg_bizhong_setup = (RadioGroup) view.findViewById(R.id.rg_bizhong_setup);
		rb_renminbihuan = (RadioButton) view.findViewById(R.id.rb_renminbihuan);
		rb_waibihuan = (RadioButton) view.findViewById(R.id.rb_waibihuan);
		rb_renminbiandwaibi = (RadioButton) view.findViewById(R.id.rb_renminbiandwaibi);
		if (!StringUtil.isNullOrEmpty(currency1) && StringUtil.isNullOrEmpty(currency2)) {
			if (!StringUtil.isNull(codeCode1) && LocalData.rmbCodeList.contains(codeCode1)) {
				// 人民币存在，外币不存在
				rb_renminbihuan.setChecked(true);
				rb_renminbihuan.setVisibility(View.VISIBLE);
				rb_waibihuan.setVisibility(View.GONE);
				rb_renminbiandwaibi.setVisibility(View.GONE);
				ll_rmbShow.setVisibility(View.VISIBLE);
				ll_foreignShow.setVisibility(View.GONE);
				repayCurSel = ALL_REN;
				strRepayCurSel = getString(R.string.mycrcd_all_setup_renhuan);
			} else if (!StringUtil.isNull(codeCode1) && !LocalData.rmbCodeList.contains(codeCode1)) {
				// 单外币
				// 外币存在
				rb_renminbihuan.setVisibility(View.VISIBLE);
				rb_waibihuan.setVisibility(View.VISIBLE);
				rb_renminbiandwaibi.setVisibility(View.GONE);
				rb_renminbihuan.setChecked(true);
				ll_rmbShow.setVisibility(View.VISIBLE);
				ll_foreignShow.setVisibility(View.GONE);
				repayCurSel = ALL_REN;
				strRepayCurSel = getString(R.string.mycrcd_all_setup_renhuan);
			}

		} else if (!StringUtil.isNullOrEmpty(currency1) && !StringUtil.isNullOrEmpty(currency2)) {
			// 双币种存在
			rb_renminbihuan.setVisibility(View.VISIBLE);
			rb_waibihuan.setVisibility(View.GONE);
			rb_renminbiandwaibi.setVisibility(View.VISIBLE);
			rb_renminbihuan.setChecked(true);
			ll_rmbShow.setVisibility(View.VISIBLE);
			ll_foreignShow.setVisibility(View.GONE);
			repayCurSel = ALL_REN;
			strRepayCurSel = getString(R.string.mycrcd_all_setup_renhuan);

		}

		rg_bizhong_setup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_renminbihuan:
					ll_rmbShow.setVisibility(View.VISIBLE);
					ll_foreignShow.setVisibility(View.GONE);
					repayCurSel = ALL_REN;
					strRepayCurSel = getString(R.string.mycrcd_all_setup_renhuan);
					break;
				case R.id.rb_waibihuan:
					ll_rmbShow.setVisibility(View.GONE);
					ll_foreignShow.setVisibility(View.VISIBLE);
					repayCurSel = ALL_RENANDFOREIGN;
					strRepayCurSel = getString(R.string.mycrcd_all_setup_foreignhuan);
					break;
				case R.id.rb_renminbiandwaibi:
					ll_rmbShow.setVisibility(View.VISIBLE);
					ll_foreignShow.setVisibility(View.VISIBLE);
					repayCurSel = ALL_FOREIGN;
					strRepayCurSel = getString(R.string.mycrcd_setup_ren_and_wai);
					break;
				}
			}
		});

		forex_rate_currency_buylCode = (Spinner) findViewById(R.id.forex_rate_currency_buylCode);
		forex_rate_currency_type = (Spinner) findViewById(R.id.forex_rate_currency_type);

		benAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, MyCrcdSetupReadActivity.benNumberArray);
		benAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		forex_rate_currency_buylCode.setAdapter(benAdapter);
		forex_rate_currency_buylCode.setSelection(0);

		waiAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, MyCrcdSetupReadActivity.waiNumberArray);
		waiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		forex_rate_currency_type.setAdapter(waiAdapter);
		forex_rate_currency_type.setSelection(0);

		rg_money_setup = (RadioGroup) findViewById(R.id.rg_money_setup);
		rb_allmoney = (RadioButton) findViewById(R.id.rb_allmoney);
		rb_allmoney.setChecked(true);
		rb_lowmoney = (RadioButton) findViewById(R.id.rb_lowmoney);

		rg_money_setup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_allmoney:
					autoRepayMode = "FULL";
					strautoRepayMode = getString(R.string.mycrcd_allmoney_huan);
					break;
				case R.id.rb_lowmoney:
					autoRepayMode = "MINP";
					strautoRepayMode = getString(R.string.mycrcd_minmoney_huan);
					break;
				}
			}
		});

		nextButton = (Button) findViewById(R.id.nextButton);
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int rmbPosition = -1;
				int wbPosition = -1;
				if (ll_rmbShow.getVisibility() == View.VISIBLE) {
					benNumber = String.valueOf(forex_rate_currency_buylCode.getSelectedItem());
					rmbPosition = forex_rate_currency_buylCode.getSelectedItemPosition();
				}
				if (ll_foreignShow.getVisibility() == View.VISIBLE) {
					waiNumber = forex_rate_currency_type.getSelectedItem().toString();
					wbPosition = forex_rate_currency_type.getSelectedItemPosition();
				}
				Intent it = new Intent(CrcdPaymentwaySetup.this, CrcdPaymentwaySetupConfirm.class);
				it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
				it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
				it.putExtra(Crcd.CRCD_AUTOREPAYMODE, autoRepayMode);
				it.putExtra(Crcd.CRCD_REPAYCURSEL, repayCurSel);
				it.putExtra(Crcd.CRCD_RMBREPAYACCID, rmbPosition);
				it.putExtra(Crcd.CRCD_REPAYACCID, wbPosition);
				// startActivity(it);
				startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;

		default:
			break;
		}
	}
}
