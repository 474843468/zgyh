package com.chinamworld.bocmbci.biz.acc.mybankaccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 关联账户转账
 * 
 * @author wangmengmeng
 * 
 */
public class AccRelTransManagerActivity extends AccBaseActivity {
	/** 账户转账页 */
	private View view;
	/** 转出账户类型 */
	private TextView acc_type_value_out;
	/** 转出账户账号 */
	private TextView acc_account_num_out;
	/** 转出账户别名 */
	private TextView acc_account_nickname_out;
	/** 转入账户类型 */
	private TextView acc_type_value_in;
	/** 转入账户账号 */
	private TextView acc_account_num_in;
	/** 转入账户别名 */
	private TextView acc_account_nickname_in;
	private int outposition;
	private int inposition;
	/** 转出账户信息 */
	private Map<String, Object> transOutMap;
	/** 转入账户信息 */
	private Map<String, Object> transInMap;
	/** 账户列表 */
	private List<Map<String, Object>> bankAccountList;
	/** 币种 */
	private Map<String, Object> currencyandbalance= new HashMap<String, Object>();
//	private String Balance;
	
	private String loanBalanceLimit;
	private String loanBalanceLimitFlag;
	public List<String> queryCurrencyList = new ArrayList<String>();
	public List<String> queryCodeList = new ArrayList<String>();	
	public List<String> CashRemitList = new ArrayList<String>();	
	public List<List<String>> queryCashRemitList = new ArrayList<List<String>>();
	public List<List<String>> queryCashRemitCodeList = new ArrayList<List<String>>();
	private int cashPosition = 0;
	/** 选择的币种 */
	private String currency;
	/** 选择的钞汇 */
	private String cashremit;
	/** 币种下拉菜单 */
	private Spinner currency_choose;
	/** 钞汇下拉菜单 */
	private Spinner cashRemit_choose;
	/** 钞汇适配 器 */
	private ArrayAdapter<ArrayList<String>> cashRemitadapter;
	/** 转账金额 */
	private EditText amountEt;
	private String amount;
	/** 附言 */
	private EditText remarkEt;
	/** 执行方式 */
	private String tranMode;
	/** 附言 */
	private String memo;
	/** 转账方式 */
	protected int tranTypeFlag;
	/** 转出账户可以用余额 */
	// private String availableBalance;
	private Map<String, Object> resultDetail;
	private List<Map<String, Object>> accountDetailList;
	private String typeOut;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.rela_acc_tran));
		// 添加布局
		view = addView(R.layout.acc_trans_view);
		setLeftSelectedPosition("accountManager_1");
		// 初始化界面
		init();
	}

	/** 初始化界面 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void init() {
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(2);
				finish();
			}
		});
		outposition = this.getIntent().getIntExtra(ConstantGloble.ACC_POSITION, 0);
		inposition = this.getIntent().getIntExtra(ConstantGloble.ACC_ISMY, 0);
		bankAccountList = AccDataCenter.getInstance().getBankAccountList();
		transOutMap = bankAccountList.get(outposition);
		transInMap = bankAccountList.get(inposition);

		acc_type_value_out = (TextView) view.findViewById(R.id.acc_type_value_out);
		acc_account_num_out = (TextView) view.findViewById(R.id.acc_account_num_out);
		acc_type_value_in = (TextView) view.findViewById(R.id.acc_type_value_in);
		acc_account_num_in = (TextView) view.findViewById(R.id.acc_account_num_in);
		// 赋值
		typeOut = (String) transOutMap.get(Acc.ACC_ACCOUNTTYPE_RES);
		acc_type_value_out.setText(LocalData.AccountType.get(typeOut));
		String numOut = (String) transOutMap.get(Acc.ACC_ACCOUNTNUMBER_RES);
		acc_account_num_out.setText(StringUtil.getForSixForString(numOut));
		String typeIn = (String) transInMap.get(Acc.ACC_ACCOUNTTYPE_RES);
		acc_type_value_in.setText(LocalData.AccountType.get(typeIn));
		String numIn = (String) transInMap.get(Acc.ACC_ACCOUNTNUMBER_RES);
		acc_account_num_in.setText(StringUtil.getForSixForString(numIn));
		currency_choose = (Spinner) view.findViewById(R.id.acc_currency_spinner);
		cashRemit_choose = (Spinner) view.findViewById(R.id.acc_cashRemit_spinner);

		initCardShowInfo(0, typeOut, typeIn);
		if (typeOut.equals(ConstantGloble.ACC_ACTYPEGRCAS)
			||typeOut.equals(ConstantGloble.ZHONGYIN)	
			||typeOut.equals(ConstantGloble.SINGLEWAIBI)// 601 转出增加 103  107
				) {
//			queryCurrencyList.add(ConstantGloble.ACC_RMB);
			
			ArrayAdapter<ArrayList<String>> currencyAdapter = new ArrayAdapter(this, R.layout.custom_spinner_item, queryCurrencyList);
			currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
			currency_choose.setAdapter(currencyAdapter);
			if(queryCurrencyList.size()==1){
				currency_choose.setBackgroundResource(R.drawable.bg_spinner_default);
				currency_choose.setEnabled(false);	
			}else if(queryCurrencyList.size()>1){
				currency_choose.setBackgroundResource(R.drawable.bg_spinner);
				currency_choose.setEnabled(true);	
			}
			
		
			currency_choose.setOnItemSelectedListener(new OnItemSelectedListener() {
				
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					currency = queryCodeList.get(position);
					Map<String, Object> map=(Map<String, Object>)currencyandbalance.get(currency);
//					Balance=(String)currencyandbalance.get(currency);
					loanBalanceLimit=(String)map.get(Crcd.CRCD_LOANBALANCELIMIT);
					loanBalanceLimitFlag=(String)map.get(Crcd.LOANBALANCELIMITFLAG);
					CashRemitList.clear();
					if(currency.equals("001")){
						CashRemitList.add(LocalData.CurrencyCashremit.get(ConstantGloble.CASHRMIT_RMB));
						cashRemitadapter = new ArrayAdapter(BaseDroidApp.getInstanse().getCurrentAct(), R.layout.custom_spinner_item,CashRemitList );
						cashRemit_choose.setAdapter(cashRemitadapter);
						cashRemit_choose.setBackgroundResource(R.drawable.bg_spinner_default);
						cashRemit_choose.setEnabled(false);
						cashremit=ConstantGloble.CASHRMIT_RMB;
					}else{
						CashRemitList.add(LocalData.CurrencyCashremit.get(ConstantGloble.CASHRMIT_CASH));
						cashRemitadapter = new ArrayAdapter(BaseDroidApp.getInstanse().getCurrentAct(), R.layout.custom_spinner_item,CashRemitList );
						cashRemit_choose.setAdapter(cashRemitadapter);
						cashRemit_choose.setBackgroundResource(R.drawable.bg_spinner_default);
						cashRemit_choose.setEnabled(false);
						cashremit=ConstantGloble.CASHRMIT_CASH;
					}
					
					
					
				}
				
				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					currency = queryCodeList.get(0);
					if(currency.equals("001")){
						CashRemitList.add(LocalData.CurrencyCashremit.get(ConstantGloble.CASHRMIT_RMB));
						cashRemitadapter = new ArrayAdapter(BaseDroidApp.getInstanse().getCurrentAct(), R.layout.custom_spinner_item,CashRemitList );
						cashRemit_choose.setAdapter(cashRemitadapter);
						cashRemit_choose.setBackgroundResource(R.drawable.bg_spinner_default);
						cashRemit_choose.setEnabled(false);
						cashremit=ConstantGloble.CASHRMIT_RMB;
					}else{
						CashRemitList.add(LocalData.CurrencyCashremit.get(ConstantGloble.CASHRMIT_CASH));
						cashRemitadapter = new ArrayAdapter(BaseDroidApp.getInstanse().getCurrentAct(), R.layout.custom_spinner_item,CashRemitList );
						cashRemit_choose.setAdapter(cashRemitadapter);
						cashRemit_choose.setBackgroundResource(R.drawable.bg_spinner_default);
						cashRemit_choose.setEnabled(false);
						cashremit=ConstantGloble.CASHRMIT_CASH;
					}
				}
			});
//			cashremit=ConstantGloble.CASHRMIT_CASH;
//			cashRemit_choose.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//				@Override
//				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//					// 选择的人民币
//					cashremit = LocalData.cashRemitMap.get(LocalData.nullcashremitList.get(0));
//				}
//
//				@Override
//				public void onNothingSelected(AdapterView<?> parent) {
//					// 选择的人民币
//					cashremit = LocalData.cashRemitMap.get(LocalData.nullcashremitList.get(0));
//				}
//			});
			resultDetail = AccDataCenter.getInstance().getResultDetail();
		} else {
			queryCodeList = AccDataCenter.getInstance().getQueryCodeList();
			queryCurrencyList = AccDataCenter.getInstance().getQueryCurrencyList();
			queryCashRemitCodeList = AccDataCenter.getInstance().getQueryCashRemitCodeList();
			queryCashRemitList = AccDataCenter.getInstance().getQueryCashRemitList();
			currency = queryCodeList.get(0);
			accountDetailList = (List<Map<String, Object>>) bankAccountList.get(outposition).get(ConstantGloble.ACC_DETAILIST);
			spinnerInit();
		}
		/** 用户输入转账金额 */
		amountEt = (EditText) view.findViewById(R.id.et_commBoc_transferAmount_tranSeting);
		/** 用户输入附言 */
		remarkEt = (EditText) view.findViewById(R.id.et_commBoc_remark_tranSeting);
		TextView tv_for_amount = (TextView) view.findViewById(R.id.tv_for_amount);
		EditTextUtils.relateNumInputToChineseShower(amountEt, tv_for_amount);
		EditTextUtils.setLengthMatcher(this, remarkEt, 50);
		initRelBottomBtn(view);
	}
	
	/**
	 * 初始化卡片显示数据
	 * 
	 * @param outOrIn
	 *            转出or转入 0-转出 1-转入
	 * @param cardType
	 *            卡类型
	 */
	@SuppressWarnings("unchecked")
	private void initCardShowInfo(int outOrIn, String outCardType, String inCardType) {
		if ((outOrIn == 0) && (outCardType.equals(ConstantGloble.ACC_ACTYPEGRCAS)
				||outCardType.equals(ConstantGloble.ZHONGYIN)
				||outCardType.equals(ConstantGloble.SINGLEWAIBI)
				) && (inCardType.equals(ConstantGloble.ACC_TYPE_BRO))) {
			// 如果是长城信用卡作为转出账户，转出账户卡片要显示信用卡币种信息
//			((TextView) view.findViewById(R.id.tv_keyForMY)).setText(getResources().getString(R.string.mycrcd_meiyuan));
			view.findViewById(R.id.ll_out_currency_MYXH).setVisibility(View.GONE);
			Map<String, Object> cardDetail = AccDataCenter.getInstance().getResultDetail();
			List<Map<String, Object>> outAccountDetailList = (List<Map<String, Object>>) cardDetail.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
			List<Map<String, Object>> inAccountDetailList = (List<Map<String, Object>>) transOutMap.get(ConstantGloble.ACC_DETAILIST);
			// 转出账户卡片显示
			if (outAccountDetailList.size() == 1) {
				view.findViewById(R.id.ll_out_currency_MYXC).setVisibility(View.GONE);
			}
			for (int i = 0; i < outAccountDetailList.size(); i++) {
				Map<String, Object> detailMap = outAccountDetailList.get(i);
				String flag = (String) detailMap.get(Crcd.LOANBALANCELIMITFLAG);
				String curreny=(String)detailMap.get(Crcd.CRCD_CURRENCY);
				Map<String, Object> currymap = new HashMap<String, Object>();
				currymap.put(Crcd.LOANBALANCELIMITFLAG, flag);
				currymap.put(Crcd.CRCD_LOANBALANCELIMIT, (String) detailMap.get(Crcd.CRCD_LOANBALANCELIMIT));				
				currencyandbalance.put(curreny, currymap);
				if(!queryCodeList.contains(ConstantGloble.FOREX_RMB_TAG1)){
					queryCurrencyList.add(0,ConstantGloble.ACC_RMB);	
					queryCodeList.add(0,ConstantGloble.FOREX_RMB_TAG1);
				}else{
					queryCurrencyList.add(LocalData.Currency.get(curreny));
					queryCodeList.add(curreny);
					
				}
				
					
				String	tip = flagConvert(flag);
				tip += StringUtil.parseStringPattern((String) detailMap.get(Crcd.CRCD_LOANBALANCELIMIT), 2);
				
				if (detailMap.get(Crcd.CRCD_CURRENCY).equals("001")) {
					// 人民币可用余额
					((TextView) view.findViewById(R.id.tv_out_currency_rmb)).setText(tip);


				} else {					
				// 外币可用余额
					((TextView) view.findViewById(R.id.tv_keyForMY)).setText(LocalData.Currency.get(detailMap.get(Crcd.CRCD_CURRENCY)));
					((TextView) view.findViewById(R.id.tv_out_currency_MYXC)).setText(tip);
			
				}
				// 转入账户根据转出的信用卡的币种类型来显示币种
				for (int j = 0; j < inAccountDetailList.size(); j++) {
					Map<String, Object> map = inAccountDetailList.get(j);
					if (map.get(ConstantGloble.FOREX_CURRENCYCODE).equals(ConstantGloble.FOREX_RMB_TAG1)) {
						((TextView) view.findViewById(R.id.tv_in_currency_rmb)).setText(
								StringUtil.parseStringPattern((String) map.get(ConstantGloble.FOREX_AVAILABLEBALANCE), 2));
//						if(!queryCurrencyList.contains(ConstantGloble.ACC_RMB)){
//							queryCurrencyList.add(0,ConstantGloble.ACC_RMB);	
//							queryCodeList.add(0,ConstantGloble.FOREX_RMB_TAG1);
//						}
												
					} else if (map.get(ConstantGloble.FOREX_CURRENCYCODE).equals(detailMap.get(Crcd.CRCD_CURRENCY))) {
//						if(!queryCurrencyList.contains(LocalData.Currency.get(detailMap.get(Crcd.CRCD_CURRENCY)))){
//							queryCodeList.add((String)detailMap.get(Crcd.CRCD_CURRENCY));
//							queryCurrencyList.add(LocalData.Currency.get(detailMap.get(Crcd.CRCD_CURRENCY)));	
//						}
						
						if (map.get(ConstantGloble.FOREX_CASEREMIT).equals(ConstantGloble.CASHRMIT_CASH)) {
							
							
							view.findViewById(R.id.ll_in_currency_MYXC).setVisibility(View.VISIBLE);
							// 设置币种现钞key
							String keyForCurrency = LocalData.Currency.get(map.get(ConstantGloble.FOREX_CURRENCYCODE)) + "现钞";
							((TextView) view.findViewById(R.id.tv_key_for_in_currency_MYXC)).setText(keyForCurrency);
							// 设置币种现钞value
							((TextView) view.findViewById(R.id.tv_in_currency_MYXC)).setText(
									StringUtil.parseStringPattern((String) map.get(ConstantGloble.FOREX_AVAILABLEBALANCE), 2));
						} else {
							
							
							// 设置币种现汇value
							view.findViewById(R.id.ll_in_currency_MYXH).setVisibility(View.VISIBLE);
							String keyForCurrency = LocalData.Currency.get(map.get(ConstantGloble.FOREX_CURRENCYCODE)) + "现汇";
							((TextView) view.findViewById(R.id.tv_key_for_in_currency_MYXH)).setText(keyForCurrency);
							((TextView) view.findViewById(R.id.tv_in_currency_MYXH)).setText(
									StringUtil.parseStringPattern((String) map.get(ConstantGloble.FOREX_AVAILABLEBALANCE), 2));
						}
					}
				}
			}
		} else {
			// 否则按照正常视图显示
			view.findViewById(R.id.ll_out_currency).setVisibility(View.GONE);
			view.findViewById(R.id.ll_account_nickname_out).setVisibility(View.VISIBLE);
			view.findViewById(R.id.ll_in_currency).setVisibility(View.GONE);
			view.findViewById(R.id.ll_account_nickname_in).setVisibility(View.VISIBLE);
			acc_account_nickname_out = (TextView) view.findViewById(R.id.acc_account_nickname_out);
			acc_account_nickname_in = (TextView) view.findViewById(R.id.acc_account_nickname_in);
			acc_account_nickname_out.setText((String) transOutMap.get(Acc.ACC_NICKNAME_RES));// 账户别名
			acc_account_nickname_in.setText((String) transInMap.get(Acc.ACC_NICKNAME_RES));// 账户别名
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void spinnerInit() {
		// 币种适配
		ArrayAdapter<ArrayList<String>> currencyAdapter = new ArrayAdapter(this, R.layout.custom_spinner_item, queryCurrencyList);
		currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		currency_choose.setAdapter(currencyAdapter);
		currency_choose.setBackgroundResource(R.drawable.bg_spinner);
		currency_choose.setEnabled(true);
		// 钞汇适配
		if (LocalData.Currency.get(currency).equals(ConstantGloble.ACC_RMB)) {
			// 人民币
			cashRemitadapter = new ArrayAdapter(this, R.layout.custom_spinner_item, LocalData.nullcashremitList);
		} else {
			cashRemitadapter = new ArrayAdapter(this, R.layout.custom_spinner_item, queryCashRemitList.get(0));
		}
		cashRemitadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cashRemit_choose.setAdapter(cashRemitadapter);
		currency_choose.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (!StringUtil.isNullOrEmpty(queryCodeList) && !StringUtil.isNullOrEmpty(queryCurrencyList)) {

					currency = queryCodeList.get(position);
					String mycurrency = queryCurrencyList.get(position);
					String rmb = ConstantGloble.ACC_RMB;
					cashPosition = position;
					if (mycurrency.equals(rmb)) {
						cashRemitadapter = new ArrayAdapter(BaseDroidApp.getInstanse().getCurrentAct(),
								R.layout.custom_spinner_item, LocalData.nullcashremitList);
						cashRemit_choose.setAdapter(cashRemitadapter);
						cashRemit_choose.setBackgroundResource(R.drawable.bg_spinner_default);
						cashRemit_choose.setEnabled(false);
					} else {
						cashRemit_choose.setEnabled(true);
						cashRemitadapter = new ArrayAdapter(BaseDroidApp.getInstanse().getCurrentAct(),
								R.layout.custom_spinner_item, queryCashRemitList.get(position));
						cashRemitadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						cashRemit_choose.setAdapter(cashRemitadapter);
						cashRemit_choose.setBackgroundResource(R.drawable.bg_spinner);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if (!StringUtil.isNullOrEmpty(queryCodeList) && !StringUtil.isNullOrEmpty(queryCurrencyList)) {
					cashPosition = 0;
					currency = queryCodeList.get(0);
					String mycurrency = queryCurrencyList.get(0);
					String rmb = ConstantGloble.ACC_RMB;
					if (mycurrency.equals(rmb)) {
						cashRemitadapter = new ArrayAdapter(BaseDroidApp.getInstanse().getCurrentAct(),
								R.layout.custom_spinner_item, LocalData.nullcashremitList);
						cashRemit_choose.setAdapter(cashRemitadapter);
						cashRemit_choose.setBackgroundResource(R.drawable.bg_spinner_default);
						cashRemit_choose.setEnabled(false);
					} else {

						cashRemit_choose.setEnabled(true);
						cashRemitadapter = new ArrayAdapter(BaseDroidApp.getInstanse().getCurrentAct(),
								R.layout.custom_spinner_item, queryCashRemitList.get(0));
						cashRemitadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						cashRemit_choose.setAdapter(cashRemitadapter);
						cashRemit_choose.setBackgroundResource(R.drawable.bg_spinner);
					}
				}
			}
		});

		cashRemit_choose.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (LocalData.Currency.get(currency).equals(ConstantGloble.ACC_RMB)) {
					// 选择的人民币
					cashremit = LocalData.cashRemitMap.get(LocalData.nullcashremitList.get(0));
					return;
				}
				if (!StringUtil.isNullOrEmpty(queryCashRemitCodeList) && !StringUtil.isNullOrEmpty(queryCashRemitList)) {
					cashremit = queryCashRemitCodeList.get(cashPosition).get(position);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if (LocalData.Currency.get(currency).equals(ConstantGloble.ACC_RMB)) {
					// 选择的人民币
					cashremit = LocalData.cashRemitMap.get(LocalData.nullcashremitList.get(0));
					return;
				}
				if (!StringUtil.isNullOrEmpty(queryCashRemitCodeList) && !StringUtil.isNullOrEmpty(queryCashRemitList)) {
					cashremit = queryCashRemitCodeList.get(cashPosition).get(0);
				}
			}
		});
	}

	/**
	 * 初始化底部按钮 立即执行 预约日期执行 预约周期执行
	 */
	private void initRelBottomBtn(View v) {
		tranTypeFlag = TRANTYPE_REL_ACCOUNT;
		/** 关联转账 立即执行 */
		Button relNowExeBtn = (Button) v.findViewById(R.id.btn_commBoc_nowExe_tranSeting);
		relNowExeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				amount = amountEt.getText().toString().trim();
				memo = remarkEt.getText().toString().trim();
				// 转账金额最大14位（小数点前最多11位数字、小数点后最多2位数字）由大于0的数据组成
				tranMode = ConstantGloble.NOWEXE;
				boolean flag = judgeUserData(amount, true);
				if (!flag) {
					return;
				}
				// 保存用户输入数据
				saveUserData();
				TranDataCenter.getInstance().setAccOutInfoMap(transOutMap);
				TranDataCenter.getInstance().setAccInInfoMap(transInMap);
				// 手续费试算接口
				requestForTransferCommissionCharge(ConstantGloble.PB021);
			}
		});
		/** 关联转账 预约日期执行 */
		Button relPreDateBtn = (Button) v.findViewById(R.id.btn_commBoc_preDateExe_tranSeting);
		relPreDateBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				amount = amountEt.getText().toString().trim();
				memo = remarkEt.getText().toString().trim();
				tranMode = ConstantGloble.PREDATEEXE;
				boolean flag = judgeUserData(amount,false);
				if (!flag) {
					return;
				}
				// 保存用户输入数据
				saveUserData();
				TranDataCenter.getInstance().setAccOutInfoMap(transOutMap);
				TranDataCenter.getInstance().setAccInInfoMap(transInMap);
				requestSystemDateTime();
				BiiHttpEngine.showProgressDialog();
			}
		});
		/** 关联转账 预约周期执行 */
		Button relPrePeriodBtn = (Button) v.findViewById(R.id.btn_commBoc_prePeriodExe_tranSeting);
		relPrePeriodBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				amount = amountEt.getText().toString().trim();
				memo = remarkEt.getText().toString().trim();
				tranMode = ConstantGloble.PREPERIODEXE;
				boolean flag = judgeUserData(amount, false);
				if (!flag) {
					return;
				}
				// 保存用户输入数据
				saveUserData();
				TranDataCenter.getInstance().setAccOutInfoMap(transOutMap);
				TranDataCenter.getInstance().setAccInInfoMap(transInMap);
				requestSystemDateTime();
				BiiHttpEngine.showProgressDialog();
			}
		});
	}

	/**
	 * 判断用户输入数据
	 * @param amount  金额
	* @param isMountCompare 是否比较金额有效余额
	 * @return true 合格 false 不合格
	 */
	private boolean judgeUserData(String amount,boolean  isMountCompare) {
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		RegexpBean transAmountReg = checkJapReg(currency, getResources().getString(R.string.reg_transferAmount), amount);
		lists.add(transAmountReg);

		if (!RegexpUtils.regexpDate(lists)) {
			return false;
		}
		if(isMountCompare){
			try{
				String availableBalance;
				if (typeOut.equals(ConstantGloble.ACC_ACTYPEGRCAS)
						||typeOut.equals(ConstantGloble.ZHONGYIN)	
						||typeOut.equals(ConstantGloble.SINGLEWAIBI)// 601 转出增加 103  107
						) {
					 availableBalance = loanBalanceLimit;
					 if (!loanBalanceLimitFlag.equals("1")) {
						 availableBalance = "0";
						}
				}else{
					 availableBalance = getBalance();	
				}
				
				// // 判断转出账户余额
				 if (Double.parseDouble(amount) > Double.parseDouble(availableBalance)) {
					 BaseDroidApp.getInstanse().showInfoMessageDialog( getString(R.string.amount_wrong_two));
					 return false;
				 }
			}catch(Exception ex){
				LogGloble.e("AccRelTransManagerActivity", ex.getMessage(), ex);
			}
		}
		return true;
	}

	public String getBalance() {
		String balance = "";
		if (acc_type_value_out.equals(ConstantGloble.ACC_ACTYPEGRCAS)
				||acc_type_value_out.equals(ConstantGloble.ZHONGYIN)	
				||acc_type_value_out.equals(ConstantGloble.SINGLEWAIBI)// 601 转出增加 103  107
				) {
			balance = (String) resultDetail.get(Crcd.CRCD_CURRENTBALANCE);
		} else {
			for (int i = 0; i < accountDetailList.size(); i++) {
				String bizhong = (String) accountDetailList.get(i).get(Acc.DETAIL_CURRENCYCODE_RES);
				String cash = (String) accountDetailList.get(i).get(Acc.DETAIL_CASHREMIT_RES);
				if (currency.equals(bizhong) && cash.equals(cashremit)) {
					balance = (String) accountDetailList.get(i).get(Acc.DETAIL_AVAILABLEBALANCE_RES);
					break;
				}
			}
		}
		return balance;
	}

	/**
	 * 保存用户输入数据
	 */
	private void saveUserData() {
		Map<String, String> userInputMap = new HashMap<String, String>();
		userInputMap.put(Tran.INPUT_CURRENCY_CODE, currency);
		userInputMap.put(Tran.INPUT_CASHREMIT_CODE, cashremit);
		userInputMap.put(Tran.INPUT_TRANSFER_AMOUNT, amount);
		userInputMap.put(Tran.INPUT_TRANSFER_REMARK, memo);
		userInputMap.put(Tran.MANAGE_PRE_transMode_RES, tranMode);
		TranDataCenter.getInstance().setUserInputMap(userInputMap);
	}

	/**
	 * 行内手续费试算返回
	 * 
	 * @param resultObj
	 */
	@Override
	public void requestForTransferCommissionChargeCallBack(Object resultObj) {
		super.requestForTransferCommissionChargeCallBack(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		TranDataCenter.getInstance().setCommissionChargeMap(result);
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent0 = new Intent(AccRelTransManagerActivity.this, RelConfirmInfoActivity1.class);
		startActivity(intent0);
	}

	/**
	 * 手续费试算异常拦截
	 */
	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
			if (Tran.TRANSFER_COMMISSIONCHARGE_API.equals(biiResponseBody.getMethod())) {
				if (biiResponse.isBiiexception()) {// 代表返回数据异常
					BiiHttpEngine.dissMissProgressDialog();
					BiiError biiError = biiResponseBody.getError();
					// 判断是否存在error
					if (biiError != null) {
						if (biiError.getCode() != null) {
							if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时 要重新登录
								showTimeOutDialog(biiError.getMessage());
							} else {// 非会话超时错误拦截
								Intent intent0 = new Intent(BaseDroidApp.getInstanse().getCurrentAct(), RelConfirmInfoActivity1.class);
								startActivity(intent0);
							}
						}
					}
					return true;
				}
				return false;// 没有异常
			} else {
				return super.httpRequestCallBackPre(resultObj);
			}
		}
		// 随机数获取异常

		return super.httpRequestCallBackPre(resultObj);
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		switch (Integer.parseInt(tranMode)) {
		case 1:// 预约日期执行
			Intent intent1 = new Intent(AccRelTransManagerActivity.this,
					PreDateExeActivity1.class);
			intent1.putExtra(ConstantGloble.DATE_TIEM, dateTime);
			intent1.putExtra(TRANS_TYPE, tranTypeFlag);
			startActivity(intent1);
			break;
		case 2:// 预约周期执行
			Intent intent2 = new Intent(AccRelTransManagerActivity.this, PrePeriodExeActivity1.class);
			intent2.putExtra(ConstantGloble.DATE_TIEM, dateTime);
			intent2.putExtra(TRANS_TYPE, tranTypeFlag);
			startActivity(intent2);
			break;

		default:
			break;
		}
	}


	
	private String flagConvert(String flag) {
		if ("0".equals(flag)) {
			flag = "欠款";
		} else if ("1".equals(flag)) {
			flag = "存款";
		} else if ("2".equals(flag)) {
			flag = "";
		}

		return flag;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(2);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
