package com.chinamworld.bocmbci.biz.loan.loanPledge;

import java.math.BigDecimal;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.biz.loan.adapter.LoanPledgeCdnumberInfoAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 查询定一本下存单信息 */
public class LoanPledgeCdnumberInfoActivity extends LoanBaseActivity {

	private static final String TAG = "LoanPledgeCdnumberInfoActivity";
	private View layoutView = null;
	private Button mextButton = null;
	private GridView gridView = null;
	private List<Map<String, String>> cdNumberList = null;
	private LoanPledgeCdnumberInfoAdapter adapter = null;
	private List<Boolean> listFlag = null;
	private int number = 0;
	private String currencyCode1 = null;
	private String currencyCode2 = null;
	private String code1Name = "";
	private String code2Name = null;
	private double available = 0;
	private String accountId = null;
	private List<Map<String, String>> codeList = null;
	private double acailableBalance = 0;
	private List<Map<String, String>> resultAccList = null;
	/** 未进行格式化的账户列表 */
	private List<String> accountNumberList = null;
	/** 进行格式化的账户列表 */
	private List<String> dealAccountNumberList = null;
	private List<String> accountTypeList = null;
	private List<String> accountIdList = null;
	// private int position = 0;
	/** 浮动比 */
	public static  String floatingRate = null;
	/** 浮动值 */
	public static String floatingValue = null;
	/** 贷款利率 */
	private String loanRate = null;
	private String accountNumber = null;
	private String loanPeriodMax = null;
	private String loanPeriodMin = null;
	//单笔限额上限
	private String singleQuotaMax = null;
	//单笔限额下限
	private String singleQuotaMin = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		// if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
		// ((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0,
		// 0);
		// }
		setTitle(getResources().getString(R.string.loan_two_one));
		Button backButton = (Button) findViewById(R.id.ib_back);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		accountId = getIntent().getStringExtra(Loan.LOAN_ACCOUNTID_RES);
		accountNumber = getIntent().getStringExtra(Loan.LOAN_ACCOUNTNUMBER_RES);
		codeList = new ArrayList<Map<String, String>>();
		if (StringUtil.isNull(accountId)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.loan_no_cdnumber_title));
			return;
		}
		listFlag = new ArrayList<Boolean>();
		cdNumberList = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOAN_NORMALDATELIST);
		if (StringUtil.isNullOrEmpty(cdNumberList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.loan_no_cdnumber_title));
			return;
		}
		setListFlag();
		init();
		initOnClick();
	}

	/** 将listFlag的值全部设置为false */
	private void setListFlag() {
		int len = cdNumberList.size();
		for (int i = 0; i < len; i++) {
			listFlag.add(false);
		}
	}

	private void init() {
		layoutView = LayoutInflater.from(this).inflate(R.layout.loan_select_cdnumber, null);
		tabcontentView.addView(layoutView);
		mextButton = (Button) findViewById(R.id.make_sure);
		gridView = (GridView) findViewById(R.id.loan_rate_gridView);
		adapter = new LoanPledgeCdnumberInfoAdapter(this, cdNumberList, listFlag);
		gridView.setAdapter(adapter);
	}

	private void initOnClick() {
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {

				Map<String, String> map = cdNumberList.get(position);
				if (listFlag.get(position)) {
					number--;
					if (number <= 0) {
						number = 0;
					}
					listFlag.set(position, false);
					adapter.dateChanged(listFlag);
					String availables = map.get(Loan.LOAN_AVAILABLEBALANCE_RES);
					available -= Double.valueOf(availables);
				} else {
					if (number > 5) {
						/** 选择的货币对超过6对，弹出提示框 */
						BaseDroidApp.getInstanse().showInfoMessageDialog(
								LoanPledgeCdnumberInfoActivity.this.getString(R.string.loan_choise_no));
						listFlag.set(position, false);
						adapter.dateChanged(listFlag);
						return;
					} else {
						if (number == 0) {
							currencyCode1 = map.get(Loan.LOAN_CURRENCYCODE_RES);
							if (LocalData.Currency.containsKey(currencyCode1)) {
								code1Name = LocalData.Currency.get(currencyCode1);
							}

							number++;
							listFlag.set(position, true);
							adapter.dateChanged(listFlag);
							String availables = map.get(Loan.LOAN_AVAILABLEBALANCE_RES);
							available = Double.valueOf(availables);
							codeList.add(map);
						} else {
							currencyCode2 = map.get(Loan.LOAN_CURRENCYCODE_RES);
							if (LocalData.Currency.containsKey(currencyCode2)) {
								code2Name = LocalData.Currency.get(currencyCode2);
								if (code1Name.equals(code2Name)) {
									number++;
									listFlag.set(position, true);
									adapter.dateChanged(listFlag);
									String availables = map.get(Loan.LOAN_AVAILABLEBALANCE_RES);
									available += Double.valueOf(availables);
								} else {
									BaseDroidApp.getInstanse().showInfoMessageDialog(
											LoanPledgeCdnumberInfoActivity.this.getString(R.string.loan_choise_no));
									listFlag.set(position, false);
									adapter.dateChanged(listFlag);
									return;
								}
							}
						}

					}
				}
			}
		});
		mextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 存储用户选择的存单信息
				BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.LOAN_CDNUMBERLIST, cdNumberList);
				BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.LOAN_LISTFLAG, listFlag);
				if (number <= 0) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							LoanPledgeCdnumberInfoActivity.this.getString(R.string.loan_choise_no_newcd));
					return;
				}
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
			}
		});
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 综合查询（查询汇率、质押率、贷款期限上下限、单笔限额上下限）
		Map<String, String> map = new HashMap<String, String>();
		map.put(Loan.LOAN_CURRENCYCODE_RES, currencyCode1);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		list.add(map);
		requestPsnLOANMultipleQuery(list, accountId);
	}

	@Override
	public void requestPsnLOANMultipleQueryCallback(Object resultObj) {
		super.requestPsnLOANMultipleQueryCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> result = (Map<String, String>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_nullnew));
			return;
		}
		String exchangeRate = result.get(Loan.LOAN_EXCHANGERATE_RES);
		String pledgeRate_ZH =String.valueOf(result.get(Loan.LOAN_PLEDGERATE_ZH_RES));
		String pledgeRate_US = String.valueOf(result.get(Loan.LOAN_EPLEDGERATE_US_RES));
		String pledgeRate_OT = String.valueOf(result.get(Loan.LOAN_PLEDGERATE_OT_RES));
		floatingRate = result.get(Loan.LOAN_FLOATINGRATE_RES);
		floatingValue = result.get(Loan.LOAN_FLOATINGVALUE_RES);
		loanPeriodMin = result.get(Loan.LOAN_LOANPERIODMIN_RES);
		loanPeriodMax = result.get(Loan.LOAN_LOANPERIODMAX_RES);
		singleQuotaMax = result.get(Loan.LOAN_SINGLEQUOTAMAX_RES);
		singleQuotaMin = result.get(Loan.LOAN_SINGLEQUOTAMIN_RES);
		if (StringUtil.isNull(currencyCode1) || StringUtil.isNull(exchangeRate)) {
			return;
		} else {
			// 可贷款金额=∑存单面额*汇率*质押率
			if (LocalData.rmbCodeList.contains(currencyCode1)) {
				// 人民币
				if (!StringUtil.isNull(pledgeRate_ZH)) {
					getRMBAvailableBalance(pledgeRate_ZH);
					isRequestDate();
				} else {
					BaseHttpEngine.dissMissProgressDialog();
					return;
				}
			} else if (LocalData.myCodeList.contains(currencyCode1)) {
				// 美元
				if (!StringUtil.isNull(pledgeRate_US)) {
					getAvailableBalance(exchangeRate, pledgeRate_US);
					isRequestDate();

				} else {
					BaseHttpEngine.dissMissProgressDialog();
					return;
				}
			} else {
				// 其他币种
				if (!StringUtil.isNull(pledgeRate_OT)) {
					getAvailableBalance(exchangeRate, pledgeRate_OT);
					isRequestDate();
				} else {
					BaseHttpEngine.dissMissProgressDialog();
					return;
				}
			}
		}

	}

	/** 得到可贷款金额 */
	private void getAvailableBalance(String exchangeRate, String pledgeRate) {
		double exchange = Double.valueOf(exchangeRate);
		double zh = Double.valueOf(pledgeRate);
	//	2014.12.12   将*0.01  删除  acailableBalance = available * exchange * zh*0.01;
		acailableBalance = available * exchange * zh*0.01*0.01;
	}
	/**人民币的不用乘汇率了    得到可贷款金额*/
	private void getRMBAvailableBalance(String pledgeRate) {
//		double pledgeRate_RMB=(Double.parseDouble()/100;
		double zh = Double.valueOf(pledgeRate);
	//	2014.12.12   将*0.01  删除  acailableBalance = available * exchange * zh*0.01;
		acailableBalance = available *zh*0.01;
	}

	private void isRequestDate() {
		double singleQuotaMins=Double.parseDouble(singleQuotaMin);
		if (acailableBalance >= singleQuotaMins
				//2014.12.12 将最大限额为30W 的上限删除  &&acailableBalance<=300000
				) {
			// 可用金额>=1000才可以进行贷款
//			requestPsnClearanceAccountQuery(null)
			/**603去掉结算币种查询账户，变更为所有账户列表*/
			requestPsnCommonQueryAllChinaBankAccount();
		} else {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse()
					.showInfoMessageDialog(getResources().getString(R.string.loan_choise_input_money));
			return;
		}
	}

	/** 结算账户查询-------回调 */
//	@Override
//	public void requestPsnClearanceAccountQueryCallBack(Object resultObj) {
//		super.requestPsnClearanceAccountQueryCallBack(resultObj);
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		// 得到response
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		// 得到result
//		if (!StringUtil.isNullOrEmpty(resultAccList)) {
//			resultAccList.clear();
//		}
//		resultAccList = (List<Map<String, String>>) biiResponseBody.getResult();
//		if (StringUtil.isNullOrEmpty(resultAccList)) {
//			BaseHttpEngine.dissMissProgressDialog();
//			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_not_trade));
//			return;
//		}
//		getAccountNumbe();
//		if (StringUtil.isNullOrEmpty(accountNumberList)) {
//			BaseHttpEngine.dissMissProgressDialog();
//			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
//			return;
//		}
//		requestRate();
//	}
	/**所有账户查询回调*/
	@SuppressWarnings("unchecked")
	@Override
	public void requestPsnCommonQueryAllChinaBankAccountCallBack(
			Object resultObj) {
		// TODO Auto-generated method stub
		super.requestPsnCommonQueryAllChinaBankAccountCallBack(resultObj);
		BiiResponse biiResponse = (BiiResponse)resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if(!StringUtil.isNullOrEmpty(resultAccList)){
			resultAccList.clear();
		}
		resultAccList = (List<Map<String,String>>)biiResponseBody.getResult();
		if(StringUtil.isNullOrEmpty(resultAccList)){
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_not_trade));
			return;
		}
		getAccountNumbe();
		if (StringUtil.isNullOrEmpty(accountNumberList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		requestRate();
	}

	/** 得到账户信息 */
	private void getAccountNumbe() {
		accountNumberList = new ArrayList<String>();
		accountTypeList = new ArrayList<String>();
		accountIdList = new ArrayList<String>();
		dealAccountNumberList = new ArrayList<String>();
		int len = resultAccList.size();
		for (int i = 0; i < len; i++) {
			Map<String, String> map = resultAccList.get(i);
			String accountNumber = map.get(Loan.LOAN_ACCOUNTNUMBER_RES);
			String accountType = map.get(Loan.LOAN_ACCOUNTTYPE_RES);
			String accountId = map.get(Loan.LOAN_ACCOUNTID_RES);
			if (!StringUtil.isNull(accountNumber) && !StringUtil.isNull(accountId) && !StringUtil.isNull(accountType)) {
				accountNumberList.add(accountNumber);
				accountIdList.add(accountId);
				String type = LocalData.AccountType.get(accountType);
				accountTypeList.add(type);
				String number = StringUtil.getForSixForString(accountNumber);
				dealAccountNumberList.add(number);
			}
			if(accountNumberList.size()<0){
				BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_not_trade));
				return;
			}
		}
	}

	private void requestRate() {
		if (!StringUtil.isNull(floatingRate)) {
			// 浮动比不为空
			requestPsnLoanRateQuery(loanPeriodMin, floatingRate, floatingValue,
					ConstantGloble.REDRAWTYPE_AUTO);
		} else {
			if (!StringUtil.isNull(floatingValue)) {
				// 浮动值不为空
				requestPsnLoanRateQuery(loanPeriodMin, floatingRate, floatingValue,
						ConstantGloble.REDRAWTYPE_SELF);
			} else {
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getResources().getString(R.string.loan_choise_no_acc_status));
				return;
			}
		}
	}

	@Override
	public void requestPsnLoanRateQueryCallback(Object resultObj) {
		super.requestPsnLoanRateQueryCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		loanRate = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(loanRate)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		gotoActivity();
	}

	private void gotoActivity() {
		Intent intent = new Intent(LoanPledgeCdnumberInfoActivity.this, LoanPledgeInputActivity.class);
		intent.putExtra(Loan.LOAN_LOANRATE_RES, loanRate);		
		String avai = String.valueOf(acailableBalance);
		BigDecimal db = new BigDecimal(avai);
		String money = db.toPlainString();
		intent.putExtra(Loan.LOAN_AVAILABLEBALANCE_RES, money);
		intent.putExtra(Loan.LOAN_CURRENCYCODE_RES, currencyCode1);
		intent.putExtra(Loan.LOAN_FLOATINGRATE_RES, floatingRate);
		intent.putExtra(Loan.LOAN_FLOATINGVALUE_RES, floatingValue);
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.LOAN_ACCOUNTNUMBERLIST, accountNumberList);
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.LOAN_ACCOUNTTYPELIST, accountTypeList);
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.LOAN_ACCOUNTIDLIST, accountIdList);
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.LOAN_DEALACCOUNTNUMBERLIST, dealAccountNumberList);
		intent.putExtra(Loan.LOAN_ACCOUNTNUMBER_RES, accountNumber);
		intent.putExtra(Loan.LOAN_LOANPERIODMAX_RES, loanPeriodMax);
		intent.putExtra(Loan.LOAN_LOANPERIODMIN_RES, loanPeriodMin);
		intent.putExtra(Loan.LOAN_SINGLEQUOTAMAX_RES, singleQuotaMax);
		intent.putExtra(Loan.LOAN_SINGLEQUOTAMIN_RES, singleQuotaMin);
		BaseHttpEngine.dissMissProgressDialog();
		startActivity(intent);
	}

}
