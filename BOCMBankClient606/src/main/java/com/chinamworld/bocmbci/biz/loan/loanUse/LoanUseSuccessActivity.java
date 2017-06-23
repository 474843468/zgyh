package com.chinamworld.bocmbci.biz.loan.loanUse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.biz.loan.LoanData;
import com.chinamworld.bocmbci.biz.loan.LoanDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 用款成功信息页面
 * 
 * @author wanbing
 * 
 */
public class LoanUseSuccessActivity extends LoanBaseActivity {
	private View contentView = null;
	/** 交易序号 */
	private TextView tv_trans_id;
	/** 贷款品种 */
	private TextView tv_loan_type;
	/** 可用金额 */
//	private TextView tv_loan_cycleAvaAmount;
	/** 本次用款金额 */
	private TextView tv_use_amt;
	/** 贷款期限 */
	private TextView tv_loan_CycleLifeTerm;
	/** 放款截止日 */
	private TextView tv_loan_cycleDrawdownDate;
	/** 贷款到期日 */
	private TextView tv_loan_cycleMatDate;
	/** 贷款利率 */
	private TextView tv_loan_CycleRate;
	/** 还款账户 */
	private TextView tv_loan_CycleRepayAccount;
	/** 收款账户 */
	private TextView tv_loan_toActNum;
	private Button nextBtn;
	/** 用款信息 */
	private Map<String, Object> loanUsemap;
	/** 用款预交易信息 */
	private Map<String, Object> loanUsePremap;
	/** 用款预交易结果信息 */
	private Map<String, Object> loanUsePreResultmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.loan_use_1));
		Button btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setVisibility(View.VISIBLE);
		ibBack.setVisibility(View.GONE);
		setLeftSelectedPosition("loan_2");
		contentView = mInflater.inflate(R.layout.loan_use_success, null);
		tabcontentView.addView(contentView);
		init();
	}

	private void init() {
		loanUsemap = LoanDataCenter.getInstance().getLoanUsemap();
		loanUsePremap = LoanDataCenter.getInstance().getLoanUsePremap();
		loanUsePreResultmap = LoanDataCenter.getInstance()
				.getLoanUsePreResultmap();

		tv_trans_id = (TextView) findViewById(R.id.tv_trans_id);
		tv_loan_type = (TextView) findViewById(R.id.tv_loan_type);
//		tv_loan_cycleAvaAmount = (TextView) findViewById(R.id.tv_loan_cycleAvaAmount);
		tv_use_amt = (TextView) findViewById(R.id.tv_use_amt);
		tv_loan_CycleLifeTerm = (TextView) findViewById(R.id.tv_loan_CycleLifeTerm);
		tv_loan_cycleDrawdownDate = (TextView) findViewById(R.id.tv_loan_cycleDrawdownDate);
		tv_loan_cycleMatDate = (TextView) findViewById(R.id.tv_loan_cycleMatDate);
		tv_loan_CycleRate = (TextView) findViewById(R.id.tv_loan_CycleRate);
		tv_loan_CycleRepayAccount = (TextView) findViewById(R.id.tv_loan_CycleRepayAccount);
		tv_loan_toActNum = (TextView) findViewById(R.id.tv_loan_toActNum);
		nextBtn = (Button) findViewById(R.id.next_btn);

		tv_trans_id.setText(getIntent().getStringExtra("transactionId"));
		String loan_type = (String) loanUsemap.get(Loan.LOANACC_LOAN_TYPE_RES);
		tv_loan_type.setText(LoanData.loanTypeData.get(loan_type));
		String loan_amount = (String.valueOf(loanUsemap
				.get(Loan.LOAN_CYCLE_AVA_AMOUNT)));
//		tv_loan_cycleAvaAmount.setText(StringUtil.parseStringPattern2(
//				loan_amount, 2));
		tv_loan_CycleLifeTerm.setText(String.valueOf(loanUsemap
				.get(Loan.LOAN_CYCLE_LIFETERM)) + "月");
		tv_loan_cycleDrawdownDate.setText(String.valueOf(loanUsemap
				.get(Loan.LOAN_CYCLE_DRAWDOWNDATE)));
		String tv_loan_cycleMatDates=String.valueOf(loanUsemap.get(Loan.LOAN_CYCLE_MATDATE));
		tv_loan_cycleMatDate.setText(StringUtil.isNullOrEmptyCaseNullString(tv_loan_cycleMatDates) ? 
                ConstantGloble.BOCINVT_DATE_ADD:tv_loan_cycleMatDates);
		String loanRate=(String) loanUsemap.get(Loan.LOAN_CYCLERATE);
		float  loanRate1 = Float.valueOf(loanRate);
		if(0.0 == loanRate1){
			tv_loan_CycleRate.setText(ConstantGloble.BOCINVT_DATE_ADD);
		}else{
			tv_loan_CycleRate.setText(StringUtil.isNullOrEmptyCaseNullString(loanRate) ?
					ConstantGloble.BOCINVT_DATE_ADD:loanRate +"%");
		}
				
		String loanCycleRepayAccount=(String) loanUsemap.get(Loan.LOAN_CYCLE_REPAYACCOUNT);
		if("0".equals(loanCycleRepayAccount)){
			tv_loan_CycleRepayAccount.setText(ConstantGloble.BOCINVT_DATE_ADD);
		}else{
			tv_loan_CycleRepayAccount.setText(
//		StringUtil.getForSixForString(String.valueOf(loanCycleRepayAccount))
					StringUtil.isNullOrEmptyCaseNullString(loanCycleRepayAccount)?ConstantGloble.BOCINVT_DATE_ADD:
						StringUtil.getForSixForString(loanCycleRepayAccount)
					);
		}
		tv_loan_toActNum.setText(StringUtil.getForSixForString(String
				.valueOf(loanUsePremap.get(Loan.LOAN_CYCLE_TOACTNUM_REQ))));
		String useAmt = (String
				.valueOf(loanUsePremap.get(Loan.LOAN_AMOUNT_REQ)));
		tv_use_amt.setText(StringUtil.parseStringCodePattern(LoanCycleAccountActivity.currency,useAmt, 2));
		nextBtn.setOnClickListener(confirmClickListener);
	}

	/** 确定按钮点击事件 */
	OnClickListener confirmClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseHttpEngine.showProgressDialog();
			requestPsnLOANCycleLoanAccountListQuery();
		
		}
	};
	/** 个人循环贷款的贷款账户列表信息 */
	public void requestPsnLOANCycleLoanAccountListQuery() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.PSN_LOAN_CYCLELOAN_ACCOUNTLISTQUERY);
		Map<String, String> params = new HashMap<String, String>();
		params.put(Loan.LOANACC_E_LOAN_STATE, "10");
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnLOANCycleLoanAccountListQueryCallBack");
	}

	/**
	 * 个人循环贷款的贷款账户列表回调
	 * 
	 * @param resultObj
	 */
	public void requestPsnLOANCycleLoanAccountListQueryCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_new_null));
			return;
		}
		ActivityTaskManager.getInstance().removeAllActivity();
		Intent intent = new Intent(LoanUseSuccessActivity.this,
				LoanCycleAccountActivity.class);
		startActivity(intent);
		finish();
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.LOAN_CYC_RESULTLIST, resultList);
		BaseHttpEngine.dissMissProgressDialog();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.onKeyDown(keyCode, event);

	}
}
