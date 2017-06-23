package com.chinamworld.bocmbci.biz.loan.loanUse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.biz.loan.LoanDataCenter;
import com.chinamworld.bocmbci.biz.loan.adapter.LoanCycleAccountAdapter;
import com.chinamworld.bocmbci.biz.loan.inflaterDialogView.Loaninflaterdialog;
import com.chinamworld.bocmbci.biz.loan.loanQuery.LoanUseQueryActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 个人循环贷款--账户页面
 * 
 * @author wanbing
 * 
 */
public class LoanCycleAccountActivity extends LoanBaseActivity {
	private static final String TAG = "LoanCycleAccountActivity";
	private ListView listView = null;
	private View contentView = null;
	private List<Map<String, Object>> resultList = null;
	private List<Map<String, Object>> loanCycleList = null;
	private LoanCycleAccountAdapter adapter = null;
	private Map<String, Object> loanAccmap;
	//系统时间
	private String currentTime;
	//最低放款额
	String loanCycleMinAmount;
	/**贷款账户*/
	public static String  loan_accNum=null;
	
	/**点击item 是的币种   2014.12.11*/
	public static String currency=null;
	/** 收款账户列表 */
	public static  List<Map<String, Object>> toAccountList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.loan_cycle));
		Button btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setVisibility(View.VISIBLE);
		setLeftSelectedPosition("loan_2");
		resultList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOAN_CYC_RESULTLIST);
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		init();
		initOnClick();
	}

	private void init() {
		contentView = mInflater.inflate(R.layout.loan_cycle_account, null);
		tabcontentView.addView(contentView);
		listView = (ListView) findViewById(R.id.loan_listView);
		chooseLoanCycleAcc();
		if (loanCycleList == null || loanCycleList.size() == 0) {
			BaseDroidApp.getInstanse().showMessageDialog(this.getString(R.string.loan_cycle_null_toast),
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							BaseDroidApp.getInstanse().dismissErrorDialog();
						}
					});
			return;
		}
		adapter = new LoanCycleAccountAdapter(this, loanCycleList);
		listView.setAdapter(adapter);
	}

	private void initOnClick() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				loanAccmap = loanCycleList.get(position);
				BaseHttpEngine.showProgressDialog();
//				requestSystemDateTime();
				requestCommConversationId();
			}
		});
	}

	/**
	 * 仅展示1046和1047产品大类下，未结清的循环类型为“R-循环”的贷款账户，循环类型为“M-分次”和“F-单次”的贷款账户不展示
	 */
	private void chooseLoanCycleAcc() {
		loanCycleList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : resultList) {
			if ("R".equals((String) map.get(Loan.CYCLE_TYPE))) {
				loanCycleList.add(map);
			}
		}
	}

	/**
	 * 是否显示用款按钮
	 * A.系统对客户在我行全部未结清的贷款账户状态进行同步判断，如果存在任一贷款账户状态为“逾期”的情况，则客户不能发起新的用款，操作栏位不展示
	 * “用款”选项。 B. 如果不存在账户逾期情况，则对循环类型为“R-循环”的贷款账户进一步判断可用金额，可用金额为0的贷款账户不展示“用款”选项。
	 * C. 系统日期大于放款截止日的账户不展示“用款”选项。
	 * 
	 * @return
	 */
	private boolean isShowLoanUseButton(String currentTime,String loanCycleMinAmount) {
		for (Map<String, Object> map : resultList) {
			// 逾期
			if ("01".equals((String) map.get(Loan.OVER_DUE_STAT))) {
				return false;
			}
		}
		// 可用余额
		String loanCycleAvaAmount = (String) loanAccmap.get(Loan.LOAN_CYCLE_AVA_AMOUNT);
		double amt = Double.parseDouble(loanCycleAvaAmount);
		double loanCycleMinAmounts = Double.parseDouble(loanCycleMinAmount);
		if (amt < loanCycleMinAmounts) {
			return false;
		}
		if (amt <= 0) {
			return false;
		}
		// 放款截止日
		String loanCycleDrawdownDate = (String) loanAccmap.get(Loan.LOAN_CYCLE_DRAWDOWNDATE);
		String today = QueryDateUtils.getcurrentDate(currentTime);
		if (!QueryDateUtils.compareDate(today, loanCycleDrawdownDate)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		currentTime = dateTime;
		Loaninflaterdialog accmessagedialog = new Loaninflaterdialog(LoanCycleAccountActivity.this);
		View accmessageView;
		//可用金额<最低放款金额，不显示“用款”链接
		 currency=(String) loanAccmap.get(Loan.LOANACC_CURRENCYCODE_RES);
		if (isShowLoanUseButton(currentTime,loanCycleMinAmount)) {
			accmessageView = accmessagedialog.initLoanCycleMessageDialogView(loanAccmap, goLoanUseClick,
					goLoanUseQueryClick, exitDetailClick);
		} else {
			accmessageView = accmessagedialog.initLoanCycleMessageDialogView(loanAccmap, null, goLoanUseQueryClick,
					exitDetailClick);
		}
		BaseDroidApp.getInstanse().showLoanMessageDialog(accmessageView);

	}

	/** 退出项情况监听事件 */
	OnClickListener exitDetailClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
		}
	};
	/** 进入用款页 */
	OnClickListener goLoanUseClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseHttpEngine.showProgressDialog();
//			requestPsnClearanceAccountQuery(null);
			/**603批次变更 改为查询账户列表*/
			requestPsnCommonQueryAllChinaBankAccount();
		}
	};

	/** 进入用款查询页 */
	OnClickListener goLoanUseQueryClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String loanActNum = (String) loanAccmap.get(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES);
			Intent intent = new Intent(LoanCycleAccountActivity.this, LoanUseQueryActivity.class);
			intent.putExtra("loanActNum", loanActNum);
			startActivity(intent);
		}
	};
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String commConversationId = (String) biiResponseBody.getResult();
//	String 	conversationId=(String)
//				BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		requestPsnLOANCycleLoanMinAmountQuery(commConversationId);
	}
	
	/** 查询个人循环贷款最低放款金额接口 -------回调 */
	public void requestPsnLOANCycleLoanMinAmountQueryCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		loanCycleMinAmount=(String) biiResponseBody.getResult();
		requestSystemDateTime();
	}
//	@Override
//	public void requestPsnClearanceAccountQueryCallBack(Object resultObj) {
//		super.requestPsnClearanceAccountQueryCallBack(resultObj);
//		BaseHttpEngine.dissMissProgressDialog();
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		toAccountList = (List<Map<String, Object>>) (biiResponseBody.getResult());
//		if (StringUtil.isNullOrEmpty(toAccountList)) {
//			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_not_trade));
//			return;
//		}
//		// 用款
//		Intent intent = new Intent(LoanCycleAccountActivity.this, LoanUseInputActivity.class);
//		LoanDataCenter.getInstance().setLoanUsemap(loanAccmap);
//        loan_accNum = (String) loanAccmap.get(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES);
//		String currency = (String) loanAccmap.get(Loan.LOANACC_CURRENCYCODE_RES);
//		intent.putExtra(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES, loan_accNum);
//		intent.putExtra(Loan.LOANACC_CURRENCYCODE_RES, currency);
//		intent.putExtra(Loan.LOAN_CYCLE_MINAMOUNT, loanCycleMinAmount);
//		startActivity(intent);
//	}
	@SuppressWarnings("unchecked")
	@Override
	public void requestPsnCommonQueryAllChinaBankAccountCallBack(
			Object resultObj) {
		// TODO Auto-generated method stub
		super.requestPsnCommonQueryAllChinaBankAccountCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse)resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		toAccountList = (List<Map<String,Object>>)biiResponseBody.getResult();
		if(StringUtil.isNullOrEmpty(toAccountList)){
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_not_trade));
			return;
		}
		// 用款
		Intent intent = new Intent(LoanCycleAccountActivity.this, LoanUseInputActivity.class);
		LoanDataCenter.getInstance().setLoanUsemap(loanAccmap);
        loan_accNum = (String) loanAccmap.get(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES);
		String currency = (String) loanAccmap.get(Loan.LOANACC_CURRENCYCODE_RES);
		intent.putExtra(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES, loan_accNum);
		intent.putExtra(Loan.LOANACC_CURRENCYCODE_RES, currency);
		intent.putExtra(Loan.LOAN_CYCLE_MINAMOUNT, loanCycleMinAmount);
		startActivity(intent);
	}

}
