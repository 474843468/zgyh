package com.chinamworld.bocmbci.biz.dept.ydzc;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 约定转存查询---已约定 */
public class DeptYdzcAppointQueryActivity extends DeptBaseActivity {
	private static final String TAG = "DeptYdzcAppointQueryActivity";
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
	private TextView liaccText = null;
	private String accountNumber = null;
	private int position = -1;
	private List<Map<String, String>> accdetailList = null;
	/** 定期约定转存查询结果 */
	private Map<String, String> resultMap = null;
	private View lx_layout = null;
	private View acc_view = null;
	private View money_layout = null;
	private View termExecuteType_view = null;
	private TextView money_text_left = null;
	private Button cancelButton = null;
	private String accountId = null;
	private String volumeNumber = null;
	private String cdNumber = null;
	private TextView interestStartsDateText = null;
	private TextView interestEndDatevolumAndText = null;
	private TextView interestRateText = null;
	private TextView appointStatusText = null;
	private View times_view = null;
	private View dept_appointType=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.dept_dqydzc_appoint_query));
		ibRight.setVisibility(View.VISIBLE);
		ibBack.setVisibility(View.VISIBLE);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		detailView = LayoutInflater.from(this).inflate(R.layout.dept_dqydzc_appoint_query, null);
		tabcontent.addView(detailView);
		position = getIntent().getIntExtra(ConstantGloble.POSITION, -1);
		accountNumber = getIntent().getStringExtra(Comm.ACCOUNTNUMBER);
		accdetailList = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.DEPT_ACCDETAILLIST);
		resultMap = (Map<String, String>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.DEPT_RESULTMAP);
		accountId = getIntent().getStringExtra(Comm.ACCOUNT_ID);
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
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, addMoneyText);
		dateText = (TextView) findViewById(R.id.dept_dqydzc_success_date);
		liaccText = (TextView) findViewById(R.id.dept_dqydzc_input_lxacc);
		cancelButton = (Button) findViewById(R.id.dept_dqyzc_detail_query);
		lx_layout = findViewById(R.id.lx_layout);
		acc_view = findViewById(R.id.acc_view);
		money_layout = findViewById(R.id.money_layout);
		termExecuteType_view = findViewById(R.id.termExecuteType_view);
		money_text_left = (TextView) findViewById(R.id.money_text_left);
		interestStartsDateText = (TextView) findViewById(R.id.dept_dqyzc_detail_interestStartsDate);
		interestEndDatevolumAndText = (TextView) findViewById(R.id.dept_dqyzc_detail_interestEndDate);
		interestRateText = (TextView) findViewById(R.id.dept_dqyzc_detail_interestRate);
		appointStatusText = (TextView) findViewById(R.id.dept_dqyzc_status);
		times_view=findViewById(R.id.times_view);
		dept_appointType=findViewById(R.id.dept_appointType);
	}

	private void initDate() {
		Map<String, String> map = accdetailList.get(position);
		volumeNumber = map.get(Dept.DEPT_VOLUMENUMBER_RES);
		cdNumber = map.get(Dept.DEPT_CDNUMBER_RES);
		String currencyCode = map.get(Dept.DEPT_CURRENCYCODE_RES);
		String cashRemit = map.get(Dept.DEPT_CASHREMIT_RES);
		String availableBalance = map.get(Dept.DEPT_BOOKBALANCE_RES);
		String cdPeriod = map.get(Dept.DEPT_CDPERIOD_RES);
		String type = map.get(Dept.DEPT_TYPE_RES);
		String interestStartsDate = map.get(Dept.DEPT_INTERESTSTARTSDATE_RES);
		String interestEndDate = map.get(Dept.DEPT_INTERESTENDDATE_RES);
		String interestRate = map.get(Dept.DEPT_INTERESTRATE_RES);
		String appointStatus = map.get(Dept.DEPT_APPOINTSTATUS_RES);
		String types = null;
		if (StringUtil.isNull(type)) {
			types = "-";
		} else {
			if (LocalData.fixAccTypeMap.containsKey(type)) {
				types = LocalData.fixAccTypeMap.get(type);
			} else {
				types = "-";
			}
		}
		String code = null;
		if (StringUtil.isNull(currencyCode)) {
			code = "-";
		} else {
			if (LocalData.Currency.containsKey(currencyCode)) {
				code = LocalData.Currency.get(currencyCode);
			} else {
				code = "-";
			}
		}
		String cash = null;
		if (StringUtil.isNull(cashRemit)) {
			cash = "-";
		} else {
			if (LocalData.CurrencyCashremit.containsKey(cashRemit)) {
				cash = LocalData.CurrencyCashremit.get(cashRemit);
			} else {
				cash = "-";
			}
		}
		String money = null;
		if (StringUtil.isNull(availableBalance)) {
			money = "-";
		} else {
			money = StringUtil.parseStringCodePattern(currencyCode, availableBalance, 2);
		}
		String cdPeriods = null;
		if (StringUtil.isNull(cdPeriod)) {
			cdPeriods = "-";
		} else {
			if (LocalData.cdperiodMap.containsKey(cdPeriod)) {
				cdPeriods = LocalData.cdperiodMap.get(cdPeriod);
			} else {
				cdPeriods = "-";
			}
		}
		String convertTypes = null;
		if (StringUtil.isNull(appointStatus)) {
			convertTypes = "-";
		} else {
			if (LocalData.appointStatusremitautoFlagMap.containsKey(appointStatus)) {
				convertTypes = LocalData.appointStatusremitautoFlagMap.get(appointStatus);
			} else {
				convertTypes = "-";
			}

		}
		String appointTerm = resultMap.get(Dept.DEPT_APPOINTTERM_REQ);
		String appointType = resultMap.get(Dept.DEPT_APPOINTTYPE_REQ);
		String interestType = resultMap.get(Dept.DEPT_INTERESTTYPE_REQ);
		String transferAmount = resultMap.get(Dept.DEPT_TRANSFERAMOUNT_REQ);
		String termExecuteType = resultMap.get(Dept.DEPT_TERMEXECUTETYPE_REQ);
		String executeDate = resultMap.get(Dept.DEPT_EXECUTEDATE_RES);
		String interestAccNum = resultMap.get(Dept.DEPT_INTERESTACCNUM_RES);
		String transferAccNum = resultMap.get(Dept.DEPT_TRANSFERACCNUM_RES);
		String appointTerms = null;
		if (StringUtil.isNull(appointTerm)) {
			times_view.setVisibility(View.GONE);
			
		} else {
			if (LocalData.cdperiodMap.containsKey(appointTerm)) {
				appointTerms = LocalData.cdperiodMap.get(appointTerm);
				times_view.setVisibility(View.VISIBLE);
				appointTermText.setText(appointTerms);
			} else {
				times_view.setVisibility(View.GONE);
			}

		}
		String appointTypes = null;
		if ((!StringUtil.isNull(appointType) && ConstantGloble.appointTypeList.contains(appointType))
				||ConstantGloble.DEPT_CL.equals(appointType)) {
		    	dept_appointType.setVisibility(View.VISIBLE);
		    	appointTypes=LocalData.appointTypeDateMap.get(appointType);
				appointTypeText.setText(appointTypes);
		} else {
			    dept_appointType.setVisibility(View.GONE);
		}
		String interestTypes = null;
		if (StringUtil.isNull(interestType)) {
			interestTypes = "-";
		} else {
			if (LocalData.interestTypeDateMap.containsKey(interestType)) {
				interestTypes = LocalData.interestTypeDateMap.get(interestType);
			} else {
				interestTypes = "-";
			}
		}
		String transferAmounts = null;
		if (StringUtil.isNull(transferAmount)) {
			transferAmounts = "-";
		} else {
			transferAmounts = StringUtil.parseStringCodePattern(currencyCode, transferAmount, 2);
		}
		String termExecuteTypes = null;
		if (StringUtil.isNull(termExecuteType)) {
			termExecuteTypes = "-";
		} else {
			if (LocalData.termExecuteTypegMap.containsKey(termExecuteType)) {
				termExecuteTypes = LocalData.termExecuteTypegMap.get(termExecuteType);
			} else {
				termExecuteTypes = "-";
			}
		}
		String interestAccNums = null;
		if (StringUtil.isNull(interestAccNum)) {
			interestAccNums = "-";
		} else {
			interestAccNums = StringUtil.getForSixForString(interestAccNum);
		}
		String transferAccNums = null;
		if (StringUtil.isNull(transferAccNum)) {
			transferAccNums = "-";
		} else {
			transferAccNums = StringUtil.getForSixForString(transferAccNum);
		}
		accToText.setText(accountNumber);
		volumberText.setText(volumeNumber + "/" + cdNumber);
		cdnumberText.setText(types);
		String codeAneCash = null;
		if (LocalData.rmbCodeList.contains(currencyCode)) {
			codeAneCash = code;
		} else {
			codeAneCash = code + "/" + cash;

		}
		if (!StringUtil.isNull(interestType) && (ConstantGloble.DEPT_T.equals(interestType))) {
			lx_layout.setVisibility(View.VISIBLE);
			liaccText.setText(interestAccNums);
		} else {
			lx_layout.setVisibility(View.GONE);
		}
		if ((!StringUtil.isNull(appointType) && ConstantGloble.appointTypeList.contains(appointType))
				||ConstantGloble.DEPT_CL.equals(appointType)) {
			if (ConstantGloble.DEPT_FR.equals(appointType)) {
				money_text_left.setText(getResources().getString(R.string.dept_dqydzc_input_addMoney));
			} else if (ConstantGloble.DEPT_TO.equals(appointType)||ConstantGloble.DEPT_CL.equals(appointType)) {
				money_text_left.setText(getResources().getString(R.string.dept_dqydzc_input_jsMoney));
			}
			acc_view.setVisibility(View.VISIBLE);
			money_layout.setVisibility(View.VISIBLE);
			termExecuteType_view.setVisibility(View.VISIBLE);
			acctoaccText.setText(transferAccNums);
			addMoneyText.setText(transferAmounts);
			termExecuteTypeText.setText(termExecuteTypes);
		} else {
			acc_view.setVisibility(View.GONE);
			money_layout.setVisibility(View.GONE);
			termExecuteType_view.setVisibility(View.GONE);
		}
		codeAndCashText.setText(codeAneCash);
		availableText.setText(money);
		cdproidText.setText(cdPeriods);				
		interestTypeText.setText(interestTypes);
		dateText.setText(executeDate);
		interestStartsDateText.setText(interestStartsDate);
		interestEndDatevolumAndText.setText(interestEndDate);
		interestRateText.setText(interestRate);
		appointStatusText.setText(convertTypes);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(DeptYdzcAppointQueryActivity.this, DeptYdzcAppointDeleteConfiemActivity.class);
				intent.putExtra(Comm.ACCOUNT_ID, accountId);
				intent.putExtra(ConstantGloble.POSITION, position);
				intent.putExtra(Comm.ACCOUNTNUMBER, accountNumber);
				BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.DEPT_RESULTMAP, resultMap);
				startActivity(intent);
			}
		});
	}
}
