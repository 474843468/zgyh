package com.chinamworld.bocmbci.biz.loan.loanRepay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.biz.loan.LoanDataCenter;
import com.chinamworld.bocmbci.biz.loan.adapter.LoanChangeLoanRepayAccountAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 变更还款账户--账户选择页面
 * 
 * @author wanbing
 * 
 */
public class LoanChangeLoanRepayAccountChooseActivity extends LoanBaseActivity {
	private static final String TAG = "LoanChangeLoanRepayAccountChooseActivity";
	private ListView listView = null;
	private View contentView = null;
	private Button nextButton = null;
	private List<Map<String, String>> resultList = null;
	private LoanChangeLoanRepayAccountAdapter adapter = null;
	private Map<String, String> loanAct = null;
	private String currency = null;
	private Button btnRight = null;
	/** 1-人民币，2-外币 */
	private int tag = 0;
	/** 贷款账户 */
	String accountNumber = null;
	/** 原还款账号 */
	String payAccountNumber = null;
	/** 还款方式 */
	public static String interestType = null;
	/** 还款周期 */
	public static String loanRepayPeriod = null;
	/** 根据原还款账号查询后的想对应的借记卡卡号 没处理过的数据 */
	public static String cardNumsrc = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.loan_change_repay_acc));
		btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setVisibility(View.VISIBLE);
		setLeftSelectedPosition("loan_3");
		resultList = (List<Map<String, String>>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.LOAN_ACCCHANGE_RESULTLIST);
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		init();
		initOnClick();

	}

	private void init() {
		contentView = mInflater.inflate(R.layout.loan_use_query_account, null);
		tabcontentView.addView(contentView);
		listView = (ListView) findViewById(R.id.loan_listView);
		nextButton = (Button) findViewById(R.id.loan_nextButton);
		LoanChangeLoanRepayAccountAdapter.selection = -1;
		adapter = new LoanChangeLoanRepayAccountAdapter(this, resultList);
		listView.setAdapter(adapter);
		
	}

	private void initOnClick() {
		//2014-10-16   84--91
		ibBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent(
						LoanChangeLoanRepayAccountChooseActivity.this,
						LoanRepayMenuActivity.class);
			startActivity(intent);
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				LoanChangeLoanRepayAccountAdapter.selection = position;
				loanAct = resultList.get(position);
				currency = loanAct.get(Loan.LOANACC_CURRENCYCODE_RES);
				accountNumber = loanAct
						.get(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES);
				//605接口变更 还款账户字段修改
				payAccountNumber = loanAct.get(Loan.LOAN_CYCLE_REPAYACCOUNT);
				adapter.notifyDataSetChanged();

			}
		});
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (LoanChangeLoanRepayAccountAdapter.selection < 0) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							getResources().getString(
									R.string.loan_choose_query_acc));
					return;
				} else {

					if (StringUtil.isNullOrEmpty(payAccountNumber)) {
						BaseDroidApp.getInstanse().showInfoMessageDialog(
								getResources().getString(
										R.string.acc_transferquery_nullend));
						return;
					}
					if (StringUtil.isNullOrEmpty(accountNumber)) {
						BaseDroidApp.getInstanse().showInfoMessageDialog(
								getResources().getString(
										R.string.acc_transferquery_nullnub));
						return;
					}
					if (StringUtil.isNull(currency)) {
						BaseDroidApp.getInstanse().showInfoMessageDialog(
								getResources().getString(
										R.string.acc_transferquery_null));
						return;
					} else {
						BaseHttpEngine.showProgressDialog();
							// 人民币----查询结算账户接口
						requestPsnLOANAdvanceRepayAccountDetailQuery(accountNumber);

					}
				}

			}
		});
	}

	/** 查询结算账户-----回调 */
//	@Override
//	public void requestPsnClearanceAccountQueryCallBack(Object resultObj) {
//		super.requestPsnClearanceAccountQueryCallBack(resultObj);
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		List<Map<String, Object>> toAccountList = (List<Map<String, Object>>) (biiResponseBody
//				.getResult());
//		if (StringUtil.isNullOrEmpty(toAccountList)) {
//			BaseHttpEngine.dissMissProgressDialog();
//			BaseDroidApp.getInstanse().showInfoMessageDialog(
//					getResources().getString(
//							R.string.acc_transferquery_null_account));
//			return;
//		}
//		String accountNumber1 = null;
//		// 过滤活一本 accountType LOAN_ACCOUNTTYPE_RES 188
//		String accountType = null;
//		for (int i = 0; i < toAccountList.size(); i++) {
//			accountNumber1 = (String) toAccountList.get(i).get(
//					Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES);
//			accountType = (String) toAccountList.get(i).get(
//					Loan.LOAN_ACCOUNTTYPE_RES);
//			if (payAccountNumber.equals(accountNumber1)|| cardNumsrc.equals(accountNumber1)
//			// ||"188".equals(accountType)
//			) {
//				toAccountList.remove(toAccountList.get(i));
//				i--;
//			}
//		}
//		if (StringUtil.isNullOrEmpty(toAccountList)) {
//			BaseHttpEngine.dissMissProgressDialog();
//			BaseDroidApp.getInstanse().showInfoMessageDialog(
//					getResources().getString(
//							R.string.acc_transferquery_null_account));
//			return;
//		}
//		BaseHttpEngine.dissMissProgressDialog();
//		BaseDroidApp.getInstanse().getBizDataMap()
//				.put(ConstantGloble.LOAN_RMB_TOACCOUNTLIST, toAccountList);
//		gotoNextActivity();
//	}

	/** 请求账户列表接口---------回调 */
	@Override
	public void requestPsnCommonQueryAllChinaBankAccountCallBack(
			Object resultObj) {
		super.requestPsnCommonQueryAllChinaBankAccountCallBack(resultObj);
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = response.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) body
				.getResult();
		String accountNumber1 = null;
		for (int i = 0; i < resultList.size(); i++) {
			accountNumber1 = (String) resultList.get(i).get(
					Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES);
			if (payAccountNumber.equals(accountNumber1)
					|| cardNumsrc.equals(accountNumber1)) {
				resultList.remove(resultList.get(i));
				i--;
			}
		}
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(
							R.string.acc_transferquery_null_account));
			return;
		}
		// 过滤条件 1:
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.LOAN_WB_RESULTLIST, resultList);
		BaseHttpEngine.dissMissProgressDialog();
		gotoNextActivity();
	}

	private void gotoNextActivity() {
		Intent intent = new Intent(
				LoanChangeLoanRepayAccountChooseActivity.this,
				LoanChangeLoanRepayAccountInputActivity.class);
		LoanDataCenter.getInstance().setLoanChangeRepayAccmap(loanAct);
		intent.putExtra(ConstantGloble.FOREX_TAG, tag);
		startActivity(intent);
		// BaseHttpEngine.dissMissProgressDialog();
	}

	/*
	 * 2014-10-16 返回3级菜单 (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onKeyDown(int,
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ibBack.performClick();
			return false;
		}

		return super.onKeyDown(keyCode, event);
	}

	/** 提前还款贷款账户详情查询 */
	private void requestPsnLOANAdvanceRepayAccountDetailQuery(String loanAccount) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody
				.setMethod(Loan.LOAN_PSNLOANADVANCEREPAYACCOUNTDETAILQUERY_API);
		biiRequestBody.setConversationId(null);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Loan.LOAN_LOANACCOUNT_REQ, loanAccount);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnLOANAdvanceRepayAccountDetailQueryCallback");

	}

	/** 提前还款贷款账户详情查询-----回调 */
	public void requestPsnLOANAdvanceRepayAccountDetailQueryCallback(
			Object resultObj) {
		// BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.acc_transferquery_null));
			return;
		} else {
			loanRepayPeriod = (String) result.get(Loan.LOAN_REPAYPERIOD_REO);
			interestType = (String) result.get(Loan.LOAN_INTTYPETERES_REQ);
			payAccountNumber = (String) result
					.get(Loan.LOAN_PAYACCOUNTNUMBER_REQ);
		}
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.LOAN_RESULT, result);

		requestPsnQueryCardNumByAcctNum(payAccountNumber);
	}

	/** 根据主账户查询对应的借记卡卡号---回调 */
	public void requestPsnQueryCardNumByAcctNumCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框
		cardNumsrc = (String) (biiResponseBody.getResult());
		String cardNum = StringUtil.getForSixForString(cardNumsrc);

		// 判断是人民币还是外币，
		if (LocalData.rmbCodeList.contains(currency)) {
			// 人民币----查询结算账户接口
			tag = 2;
//			requestPsnClearanceAccountQuery(currency);
			/**603需求变更调用查询账户列表接口*/
			requestPsnCommonQueryAllChinaBankAccount();
		} else {
			// 外币----查询账户列表接口
			tag = 2;
			requestPsnCommonQueryAllChinaBankAccount();
		}
	}
}
