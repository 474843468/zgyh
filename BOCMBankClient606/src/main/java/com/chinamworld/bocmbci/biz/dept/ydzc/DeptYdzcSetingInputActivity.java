package com.chinamworld.bocmbci.biz.dept.ydzc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 设置约定转存---填写页面 */
public class DeptYdzcSetingInputActivity extends DeptBaseActivity implements OnItemSelectedListener {
	private static final String TAG = "DeptYdzcSetingInputActivity";
	private LinearLayout tabcontent;// 主Activity显示
	private View detailView = null;
	private String accountId = null;
	private String accountNumber = null;
	private int position = -1;
	/** 账户详情数据 */
	private List<Map<String, String>> accdetailList = null;
	/** 转账账户数据 */
	private List<Map<String, String>> resultList = null;
	private TextView accToText = null;
	private TextView volumberText = null;
	private TextView cdnumberText = null;
	private TextView codeAndCashText = null;
	private TextView availableText = null;
	private TextView cdproidText = null;
	/** 约定存期Spinner */
	private Spinner appointTermSpinner = null;
	/** 约定方式Spinner */
	private Spinner appointTypeSpinner = null;
	/** 利息转存方式Spinner */
	private Spinner interestTypeSpinner = null;
	/** 转账账户Spinner */
	private Spinner transferAccountSpinner = null;
	private RadioGroup radioGroup = null;
	/** 执行方式Text */
	private TextView termExecuteTypeText = null;
	private EditText moneyEdit = null;
	/** 执行方式View */
	private View termExecuteTypeView = null;
	/** 执行方式文字描述 */
	private View red_text_view = null;
	/** 转账账户View */
	private View accView = null;
	/** 加减金额View */
	private View moneyView = null;
	private TextView moneyLeftText = null;
	private Button nextButton = null;
	private List<String> transferAccountNumberList = null;
	/** 执行方式上送参数 */
	private String termExecuteTypeReq = null;
	/** 执行方式--仅本次到期、历次到期 */
	private String termExecuteType = null;
	/** 约定存期--xx个月 */
	private String appointTerm = null;
	/** 约定方式 */
	private String appointType = null;
	/** 约定存期--存入本金、转出利息 */
	private String interestType = null;
	/** 转账账户ID */
	private String transferAccountId = null;
	/** 转账账户 */
	private String transferAccount = null;
	private String transferMoney = null;
	private String currencyCode = null;
	private String volumeNumber = null;
	private String cdNumber = null;
	private String cashRemit = null;
	private String codeAneCash = null;
	private String money = null;
	private String cdPeriods = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.dept_dqyzc_title));
		ibRight.setVisibility(View.VISIBLE);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		detailView = LayoutInflater.from(this).inflate(R.layout.dept_dqydzc_set_input, null);
		tabcontent.addView(detailView);
		position = getIntent().getIntExtra(ConstantGloble.POSITION, -1);
		accdetailList = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.DEPT_ACCDETAILLIST);
		resultList = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.DEPT_RESULTLIST);
		accountId = getIntent().getStringExtra(Comm.ACCOUNT_ID);
		accountNumber = getIntent().getStringExtra(Comm.ACCOUNTNUMBER);
		if (position < 0 || StringUtil.isNullOrEmpty(accdetailList)) {
			return;
		}
		getAccountNumberList();
		init();
		initDate();
		initRadioGroupDate();
		initOnClick();
	}

	/** 得到转账账户---账号 */
	private void getAccountNumberList() {
		transferAccountNumberList = new ArrayList<String>();
		int len = resultList.size();
		for (int i = 0; i < len; i++) {
			Map<String, String> map = resultList.get(i);
			String accountNumber = (String) map.get(Comm.ACCOUNTNUMBER);
			String accNum = StringUtil.getForSixForString(accountNumber);
			transferAccountNumberList.add(accNum);
		}
	}

	private void init() {
		accToText = (TextView) findViewById(R.id.dept_dqydzc_input_acc);
		volumberText = (TextView) findViewById(R.id.dept_dqydzc_input_volumber);
		cdnumberText = (TextView) findViewById(R.id.dept_dqydzc_input_cdnumber);
		codeAndCashText = (TextView) findViewById(R.id.dept_dqyzc_codeandcash);
		availableText = (TextView) findViewById(R.id.dept_dqydzc_input_money);
		cdproidText = (TextView) findViewById(R.id.dept_dqydzc_input_cdPeriod);
		termExecuteTypeView = findViewById(R.id.termExecuteType_view);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup_layout);
		termExecuteTypeText = (TextView) findViewById(R.id.termExecuteType_text);
		moneyLeftText = (TextView) findViewById(R.id.money_text_left);
		nextButton = (Button) findViewById(R.id.trade_nextButton);
		accView = findViewById(R.id.acc_view);
		moneyView = findViewById(R.id.money_layout);
		moneyEdit = (EditText) findViewById(R.id.dept_dqydzc_input_addMoney);
		red_text_view = findViewById(R.id.red_text_view);
		// 约定存期
		appointTermSpinner = (Spinner) findViewById(R.id.dept_dqydzc_input_appointTerm);
		appointTermSpinner.setOnItemSelectedListener(this);
		// 约定方式
		appointTypeSpinner = (Spinner) findViewById(R.id.dept_dqydzc_input_appointType);
		appointTypeSpinner.setOnItemSelectedListener(this);
		ArrayAdapter<String> appointTypeAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner,
				LocalData.appointTypeList);
		appointTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		appointTypeSpinner.setAdapter(appointTypeAdapter);
		appointTypeSpinner.setSelection(0);
		// 利息转存约定
		interestTypeSpinner = (Spinner) findViewById(R.id.dept_dqydzc_input_interestType);
		interestTypeSpinner.setOnItemSelectedListener(this);
		ArrayAdapter<String> interestType = new ArrayAdapter<String>(this, R.layout.dept_spinner,
				LocalData.interestTypeList);
		interestType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		interestTypeSpinner.setAdapter(interestType);
		interestTypeSpinner.setSelection(0);
		// 转账账户
		transferAccountSpinner = (Spinner) findViewById(R.id.dept_dqydzc_input_acctoacc);
		transferAccountSpinner.setOnItemSelectedListener(this);
		ArrayAdapter<String> transferAccountType = new ArrayAdapter<String>(this, R.layout.dept_spinner,
				transferAccountNumberList);
		transferAccountType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		transferAccountSpinner.setAdapter(transferAccountType);
		transferAccountSpinner.setSelection(0);
	}

	private void initDate() {
		Map<String, String> map = accdetailList.get(position);
		volumeNumber = map.get(Dept.DEPT_VOLUMENUMBER_RES);
		cdNumber = map.get(Dept.DEPT_CDNUMBER_RES);
		currencyCode = map.get(Dept.DEPT_CURRENCYCODE_RES);
		cashRemit = map.get(Dept.DEPT_CASHREMIT_RES);
		String availableBalance = map.get(Dept.DEPT_BOOKBALANCE_RES);
		String cdPeriod = map.get(Dept.DEPT_CDPERIOD_RES);
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

		if (StringUtil.isNull(availableBalance)) {
			money = "-";
		} else {
			money = StringUtil.parseStringCodePattern(currencyCode, availableBalance, 2);
		}

		if (StringUtil.isNull(cdPeriod)) {
			cdPeriods = "-";
		} else {
			if (LocalData.cdperiodMap.containsKey(cdPeriod)) {
				cdPeriods = LocalData.cdperiodMap.get(cdPeriod);
			} else {
				cdPeriods = "-";
			}
		}
		accToText.setText(accountNumber);
		volumberText.setText(volumeNumber);
		cdnumberText.setText(cdNumber);
		if (LocalData.rmbCodeList.contains(currencyCode)) {
			codeAneCash = code;
		} else {
			codeAneCash = code + "/" + cash;

		}
		codeAndCashText.setText(codeAneCash);
		availableText.setText(money);
		cdproidText.setText(cdPeriods);
		String select = getResources().getString(R.string.tran_please_choose);
		ArrayAdapter<String> appointTermAdapter = null;
		if (LocalData.rmbCodeList.contains(currencyCode)) {
			List<String> rmblist = new ArrayList<String>();
			rmblist.addAll(LocalData.appointTermList);
			List<String> rmbSelctList = new ArrayList<String>();
			if (rmblist.contains(cdPeriods)) {
				for (int i = 0; i < rmblist.size(); i++) {
					if (rmblist.get(i).equals(cdPeriods)) {
						rmblist.remove(i);
					}
				}
				rmbSelctList.add(select);
				rmbSelctList.addAll(rmblist);
			} else {
				rmbSelctList.addAll(rmblist);
			}
			appointTermAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, rmbSelctList);
		} else {
			List<String> wblist = new ArrayList<String>();
			if(LocalData.prmsTradeStyleCodeCurrencyList.contains(currencyCode)){
				wblist.addAll(LocalData.waappointTermListc);
			}else {
				wblist.addAll(LocalData.wbappointTermList);
			}
//			wblist.addAll(LocalData.wbappointTermList);
			List<String> wbSelctList = new ArrayList<String>();
			if (wblist.contains(cdPeriods)) {
				for (int i = 0; i < wblist.size(); i++) {
					if (wblist.get(i).equals(cdPeriods)) {
						wblist.remove(i);
					}
				}
				wbSelctList.add(select);
				wbSelctList.addAll(wblist);
			} else {
				wbSelctList.addAll(wblist);
			}
			appointTermAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, wbSelctList);
		}

		appointTermAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		appointTermSpinner.setAdapter(appointTermAdapter);
		appointTermSpinner.setSelection(0);
	}

	private void initOnClick() {
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				red_text_view.setVisibility(View.VISIBLE);
				switch (checkedId) {
				case R.id.dept_dqydzc_input_termExecuteType1:// 仅本次到期
					initRadioGroupDate();
					break;
				case R.id.dept_dqydzc_input_termExecuteType2:// 历次到期
					String text2 = getResources().getString(R.string.dept_dqydzc_input_term_text2);
					termExecuteType = getResources().getString(R.string.dept_dqydzc_input_termExecuteType2);
					termExecuteTypeText.setText(text2);
					termExecuteTypeReq = "2";
					break;
				default:
					break;
				}
			}
		});
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectCheck();
			}
		});
	}

	private void initRadioGroupDate() {
		String text1 = getResources().getString(R.string.dept_dqydzc_input_term_text1);
		termExecuteType = getResources().getString(R.string.dept_dqydzc_input_termExecuteType1);
		termExecuteTypeText.setText(text1);
		termExecuteTypeReq = "1";
	}

	private void selectCheck() {
		String select = getResources().getString(R.string.tran_please_choose);
		if (appointTerm.equals(select) && appointType.equals(select) && interestType.equals(select)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.dept_dqydzc_select));
			return;
		} else {
			if (moneyView.isShown()) {
				chackMoney();
			} else {
				requestDate();
			}
		}
	}

	// 验证金额
	private void chackMoney() {
		transferMoney = moneyEdit.getText().toString().trim();
		String text = moneyLeftText.getText().toString();
		if (text.contains(":") || text.contains("：")) {
			text = text.substring(0, text.length() - 1);
		}
		// 卖出时，无论买入币种、卖出币种含有特殊币种，限价汇率2位
		if (LocalData.codeNoNumber.contains(currencyCode)) {
			// 特殊币种验证方式spetialLimitRate----limitRate
			RegexpBean reb = new RegexpBean(text, transferMoney, "spetialAmounts", true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb);
			if (RegexpUtils.regexpDate(lists)) {
				requestDate();
			} else {
				return;
			}
			return;
		} else {
			// 一般币种验证方式
			RegexpBean reb = new RegexpBean(text, transferMoney, "amounts", true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb);
			if (RegexpUtils.regexpDate(lists)) {
				requestDate();
			} else {
				return;
			}
			return;
		}

	}

	/** 获取接口上送数据 */
	private void requestDate() {
		// 上送参数
		Map<String, String> map = new HashMap<String, String>();
		// 确认页面显示数据
		Map<String, String> showMap = new HashMap<String, String>();
		map.put(Comm.ACCOUNT_ID, accountId);// 转存账户id
		map.put(Dept.DEPT_VOLUMENUMBER_RES, volumeNumber);
		map.put(Dept.DEPT_CDNUMBER_RES, cdNumber);
		map.put(Dept.DEPT_CURRENCY_REQ, currencyCode);
		if (LocalData.rmbCodeList.contains(currencyCode)) {
			cashRemit = "00";
		}
		map.put(Dept.DEPT_CASHREMIT_RES, cashRemit);
		String select = getResources().getString(R.string.tran_please_choose);
		/** 1-显示，0-不显示 --约定存期 */
		String appointTermShowTag = "0";
		appointTerm = appointTermSpinner.getSelectedItem().toString();
		if (appointTerm.equals(select)) {
			// 请选择
			appointTerm = cdPeriods;
			appointTermShowTag = "0";
		} else {
			appointTermShowTag = "1";
			String appointTerms = LocalData.appointTermMap.get(appointTerm);
			map.put(Dept.DEPT_APPOINTTERM_REQ, appointTerms);
		}		
		String appointTypes = null;
		/** 1-显示，0-不显示----约定方式 */
		String appointTypeShowTag = "0";
		if (appointTypeSpinner.getSelectedItemPosition() == 0) {
			// 请选择
			appointTypeShowTag = "0";
		} else {
			appointTypeShowTag = "1";
			appointTypes = LocalData.appointTypeMap.get(appointType);
			map.put(Dept.DEPT_APPOINTTYPE_REQ, appointTypes);
		}
		String interestTypes = LocalData.interestTypeMap.get(interestType);
		map.put(Dept.DEPT_INTERESTTYPE_REQ, interestTypes);
		/** 1-显示，0-不显示 */
		String moneyViewShowTag = "0";
		if (moneyView.isShown()) {
			map.put(Dept.DEPT_TRANSFERAMOUNT_REQ, transferMoney);
			String transferMoneys = StringUtil.parseStringCodePattern(currencyCode, transferMoney, 2);
			showMap.put(Dept.DEPT_TRANSFERAMOUNT_REQ, transferMoneys);
			moneyViewShowTag = "1";
		} else {
			moneyViewShowTag = "0";
		}
		/** 1-显示，0-不显示 */
		String accViewShowTag = "0";
		if (accView.isShown()) {
			map.put(Dept.DEPT_TRANSFERACCOUNTID_REQ, transferAccountId);
			showMap.put(Dept.DEPT_TRANSFERACCOUNTID_REQ, transferAccount);
			accViewShowTag = "1";
		} else {
			accViewShowTag = "0";
		}
		/** 1-显示，0-不显示 */
		String termExecuteTypeViewShowTag = "0";
		if (termExecuteTypeView.isShown()) {
			termExecuteTypeViewShowTag = "1";
			map.put(Dept.DEPT_TERMEXECUTETYPE_REQ, termExecuteTypeReq);
			showMap.put(Dept.DEPT_TERMEXECUTETYPE_REQ, termExecuteType);
		} else {
			termExecuteTypeViewShowTag = "0";
			if (appointTypeSpinner.getSelectedItemPosition() == 0) {
				// 请选择
				termExecuteTypeReq = "1";
				map.put(Dept.DEPT_TERMEXECUTETYPE_REQ, termExecuteTypeReq);
			}

		}
		/** 1-显示，0-不显示 */
		String interestTypeShowTag = "0";
		if (interestTypeSpinner.getSelectedItemPosition() == 0) {
			// 请选择
			interestTypeShowTag = "0";
		} else {
			interestTypeShowTag = "1";
		}
		showMap.put(Comm.ACCOUNTNUMBER, accountNumber);
		showMap.put(Dept.DEPT_VOLUMENUMBER_RES, volumeNumber);
		showMap.put(Dept.DEPT_CDNUMBER_RES, cdNumber);
		showMap.put(Dept.DEPT_CURRENCYCODE_RES, codeAneCash);
		showMap.put(Dept.DEPT_AVAILABLEBALANCE_RES, money);
		showMap.put(Dept.DEPT_CDPERIOD_RES, cdPeriods);
		showMap.put(Dept.DEPT_APPOINTTERM_REQ, appointTerm);
		showMap.put(Dept.DEPT_APPOINTTYPE_REQ, appointType);
		showMap.put(Dept.DEPT_INTERESTTYPE_REQ, interestType);
		showMap.put("moneyViewShowTag", moneyViewShowTag);
		showMap.put("accViewShowTag", accViewShowTag);
		showMap.put("termExecuteTypeViewShowTag", termExecuteTypeViewShowTag);
		showMap.put("appointTermShowTag", appointTermShowTag);
		showMap.put("appointTypeShowTag", appointTypeShowTag);
		showMap.put("interestTypeShowTag", interestTypeShowTag);
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.DEPT_MAP, map);
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.DEPT_SHOWMAP, showMap);
		gotoNextActivity();
	}

	private void gotoNextActivity() {
		Intent intent = new Intent(DeptYdzcSetingInputActivity.this, DeptYdzcSetingReadActivity.class);
		String text = moneyLeftText.getText().toString();
		intent.putExtra("text", text);
		startActivity(intent);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		switch (parent.getId()) {
		case R.id.dept_dqydzc_input_appointTerm:// 约定存期
			appointTerm = appointTermSpinner.getSelectedItem().toString();
			break;
		case R.id.dept_dqydzc_input_appointType:// 约定方式
			moneyEdit.setText("");
			appointType = appointTypeSpinner.getSelectedItem().toString();
			showOrHide();
			break;
		case R.id.dept_dqydzc_input_interestType:// 利息转存方式
			interestType = interestTypeSpinner.getSelectedItem().toString();
			showOrHide();
			break;
		case R.id.dept_dqydzc_input_acctoacc:// 转账账户
			transferAccount = transferAccountSpinner.getSelectedItem().toString();
			Map<String, String> map = resultList.get(position);
			transferAccountId = (String) map.get(Comm.ACCOUNT_ID);
			break;

		default:
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	/** 根据约定方式以及利息转存约定控制布局显示 */
	private void showOrHide() {
		switch (appointTypeSpinner.getSelectedItemPosition()) {
		case 0:// 不约定
			if (interestTypeSpinner.getSelectedItemPosition() == 0) {
				// 利息转存约定: 不约定
				termExecuteTypeView.setVisibility(View.GONE);
				red_text_view.setVisibility(View.GONE);
				accView.setVisibility(View.GONE);
				moneyView.setVisibility(View.GONE);
			} else if (interestTypeSpinner.getSelectedItemPosition() == 1) {
				// 利息转存约定: 转出利息
				termExecuteTypeView.setVisibility(View.GONE);
				red_text_view.setVisibility(View.GONE);
				accView.setVisibility(View.VISIBLE);
				moneyView.setVisibility(View.GONE);
			}
			break;
		case 1:// 增加本金
			termExecuteTypeView.setVisibility(View.VISIBLE);
			red_text_view.setVisibility(View.VISIBLE);
			accView.setVisibility(View.VISIBLE);
			moneyView.setVisibility(View.VISIBLE);
			moneyLeftText.setText(getResources().getString(R.string.dept_dqydzc_input_addMoney));
			break;
		case 2:// 减少本金
			termExecuteTypeView.setVisibility(View.VISIBLE);
			red_text_view.setVisibility(View.VISIBLE);
			accView.setVisibility(View.VISIBLE);
			moneyView.setVisibility(View.VISIBLE);
			moneyLeftText.setText(getResources().getString(R.string.dept_dqydzc_input_jsMoney));
			break;

		default:
			break;
		}

	}
}
