package com.chinamworld.bocmbci.biz.dept.ydzc;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.log.LogGloble;

/** 设置约定转存--完成页面 */
public class DeptYdzcSetingSuccessActivity extends DeptBaseActivity {
	private static final String TAG = "DeptYdzcSetingConfirmActivity";
	private LinearLayout tabcontent;// 主Activity显示
	private View detailView = null;
	private TextView accToText = null;
	private TextView volumberText = null;
	private TextView cdnumberText = null;
	private TextView codeAndCashText = null;
	private TextView availableText = null;
	private TextView cdproidText = null;
	private TextView appointTermText = null;
	private TextView appointTypeText = null;
	private TextView termExecuteTypeText = null;
	private TextView interestTypeText = null;
	private TextView acctoaccText = null;
	private TextView addMoneyText = null;
	private TextView dateText = null;
	/** 执行方式View */
	private View termExecuteTypeView = null;
	private Button sureButton = null;
	/** 转账账户View */
	private View accView = null;
	/** 加减金额View */
	private View moneyView = null;
	private TextView moneyLeftText = null;
	// 确认页面显示数据
	private Map<String, String> showMap = null;
	private View times_view = null;
	private View appointType_view = null;
	private View interestType_view = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.dept_dqyzc_title));
		ibRight.setVisibility(View.VISIBLE);
		ibBack.setVisibility(View.GONE);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		detailView = LayoutInflater.from(this).inflate(R.layout.dept_dqydzc_set_success, null);
		tabcontent.addView(detailView);
		showMap = (Map<String, String>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.DEPT_SHOWMAP);
		init();
		initDate();
	}

	private void init() {
		accToText = (TextView) findViewById(R.id.dept_dqydzc_input_acc);
		volumberText = (TextView) findViewById(R.id.dept_dqydzc_input_volumber);
		cdnumberText = (TextView) findViewById(R.id.dept_dqydzc_input_cdnumber);
		codeAndCashText = (TextView) findViewById(R.id.dept_dqyzc_codeandcash);
		availableText = (TextView) findViewById(R.id.dept_dqydzc_input_money);
		cdproidText = (TextView) findViewById(R.id.dept_dqydzc_input_cdPeriod);
		appointTermText = (TextView) findViewById(R.id.dept_dqydzc_input_appointTerm);
		appointTypeText = (TextView) findViewById(R.id.dept_dqydzc_input_appointType);
		termExecuteTypeText = (TextView) findViewById(R.id.dept_dqydzc_input_termExecuteType);
		interestTypeText = (TextView) findViewById(R.id.dept_dqydzc_input_interestType);
		acctoaccText = (TextView) findViewById(R.id.dept_dqydzc_input_acctoacc);
		addMoneyText = (TextView) findViewById(R.id.dept_dqydzc_input_addMoney);
		termExecuteTypeView = findViewById(R.id.termExecuteType_view);
		moneyLeftText = (TextView) findViewById(R.id.money_text_left);
		accView = findViewById(R.id.acc_view);
		moneyView = findViewById(R.id.money_layout);
		times_view = findViewById(R.id.times_view);
		appointType_view = findViewById(R.id.appointType_view);
		interestType_view = findViewById(R.id.interestType_view);
		String text = getIntent().getStringExtra("text");
		dateText = (TextView) findViewById(R.id.dept_dqydzc_success_date);
		moneyLeftText.setText(text);
		sureButton = (Button) findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent(DeptYdzcSetingSuccessActivity.this, DeptYdzcQueryActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private void initDate() {
		String accountNumber = showMap.get(Comm.ACCOUNTNUMBER);
		String volumeNumber = showMap.get(Dept.DEPT_VOLUMENUMBER_RES);
		String cdNumber = showMap.get(Dept.DEPT_CDNUMBER_RES);
		String codeAneCash = showMap.get(Dept.DEPT_CURRENCYCODE_RES);
		String money = showMap.get(Dept.DEPT_AVAILABLEBALANCE_RES);
		String cdPeriods = showMap.get(Dept.DEPT_CDPERIOD_RES);
		String appointTerm = showMap.get(Dept.DEPT_APPOINTTERM_REQ);
		String appointType = showMap.get(Dept.DEPT_APPOINTTYPE_REQ);
		String interestType = showMap.get(Dept.DEPT_INTERESTTYPE_REQ);
		String moneyViewShowTag = showMap.get("moneyViewShowTag");
		String accViewShowTag = showMap.get("accViewShowTag");
		String termExecuteTypeViewShowTag = showMap.get("termExecuteTypeViewShowTag");
		String appointTermShowTag = showMap.get("appointTermShowTag");
		String appointTypeShowTag = showMap.get("appointTypeShowTag");
		String interestTypeShowTag = showMap.get("interestTypeShowTag");
		accToText.setText(accountNumber);
		volumberText.setText(volumeNumber);
		cdnumberText.setText(cdNumber);
		codeAndCashText.setText(codeAneCash);
		availableText.setText(money);
		cdproidText.setText(cdPeriods);
		if ("1".equals(interestTypeShowTag)) {
			interestType_view.setVisibility(View.VISIBLE);
			interestTypeText.setText(interestType);
		} else if ("0".equals(interestTypeShowTag)) {
			interestType_view.setVisibility(View.GONE);
		}

		if ("1".equals(appointTypeShowTag)) {
			appointType_view.setVisibility(View.VISIBLE);
			appointTypeText.setText(appointType);
		} else if ("0".equals(appointTypeShowTag)) {
			appointType_view.setVisibility(View.GONE);
		}
		if ("1".equals(appointTermShowTag)) {
			times_view.setVisibility(View.VISIBLE);
			appointTermText.setText(appointTerm);
		} else if ("0".equals(appointTermShowTag)) {
			times_view.setVisibility(View.GONE);
		}
		if ("1".equals(moneyViewShowTag)) {
			moneyView.setVisibility(View.VISIBLE);
			String transferMoneys = showMap.get(Dept.DEPT_TRANSFERAMOUNT_REQ);
			addMoneyText.setText(transferMoneys);
		} else if ("0".equals(moneyViewShowTag)) {
			moneyView.setVisibility(View.GONE);
		}
		if ("1".equals(accViewShowTag)) {
			accView.setVisibility(View.VISIBLE);
			String transferAccount = showMap.get(Dept.DEPT_TRANSFERACCOUNTID_REQ);
			acctoaccText.setText(transferAccount);
		} else if ("0".equals(accViewShowTag)) {
			accView.setVisibility(View.GONE);
		}
		if ("1".equals(termExecuteTypeViewShowTag)) {
			termExecuteTypeView.setVisibility(View.VISIBLE);
			String termExecuteType = showMap.get(Dept.DEPT_TERMEXECUTETYPE_REQ);
			termExecuteTypeText.setText(termExecuteType);
		} else if ("0".equals(termExecuteTypeViewShowTag)) {
			termExecuteTypeView.setVisibility(View.GONE);
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
