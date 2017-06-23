package com.chinamworld.bocmbci.biz.loan.loanRepay;


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
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 变更还款账户成功信息页面
 * 
 * @author wanbing
 * 
 */
public class LoanChangeLoanRepayAccountSuccessActivity extends LoanBaseActivity {
	private View contentView = null;

	/** 贷款品种 */
	private TextView tv_loan_type;
	/** 贷款账号 */
	private TextView tv_loan_acc_num;
	/** 原还款账号 */
	private TextView tv_loan_pay_acc_num;
	/**对应还款账户借记卡卡号 */
	private TextView tv_loan_old_pay_acc;
	/** 新的还款卡号/账号 */
	private TextView tv_loan_new_pay_acc;

	private TextView tv_loan_old_pay_acc_label;
	private TextView tv_loan_new_pay_acc_label;
	
    //用于判断是否是从完成界面返回
	public static boolean isBack=false;
	private Button nextBtn;
	/** 变更还款账户信息 */
	private Map<String, String> loanChangeRepayAccmap;
	/** 变更还款账户预交易请求信息 */
	private Map<String, Object> loanChangeRepayAccPremap;
	/** 变更还款账户预交易结果信息 */
	private Map<String, Object> loanChangeRepayAccPreResultmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.loan_change_repay_acc));
		Button btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setVisibility(View.VISIBLE);
		ibBack.setVisibility(View.GONE);
		setLeftSelectedPosition("loan_3");
		contentView = mInflater.inflate(R.layout.loan_change_repay_acc_success, null);
		tabcontentView.addView(contentView);
		init();
	}

	private void init() {
		loanChangeRepayAccmap = LoanDataCenter.getInstance().getLoanChangeRepayAccmap();
		loanChangeRepayAccPremap = LoanDataCenter.getInstance().getLoanChangeRepayAccPremap();
		loanChangeRepayAccPreResultmap = LoanDataCenter.getInstance().getLoanChangeRepayAccPreResultmap();

		tv_loan_type = (TextView) findViewById(R.id.tv_loan_type);
		tv_loan_acc_num = (TextView) findViewById(R.id.tv_loan_acc_num);
		
		tv_loan_pay_acc_num=(TextView) findViewById(R.id.tv_loan_pay_acc_num);
		tv_loan_old_pay_acc = (TextView) findViewById(R.id.tv_loan_old_pay_acc);
		tv_loan_new_pay_acc = (TextView) findViewById(R.id.tv_loan_new_pay_acc);
		tv_loan_old_pay_acc_label = (TextView) findViewById(R.id.tv_loan_old_pay_acc_label);
		tv_loan_new_pay_acc_label = (TextView) findViewById(R.id.tv_loan_new_pay_acc_label);
		nextBtn = (Button) findViewById(R.id.next_btn);

		String loan_type = (String) loanChangeRepayAccmap.get(Loan.LOANACC_LOAN_TYPE_RES);
		String loantype = null;
		if (StringUtil.isNull(loan_type)) {
			loantype = "-";
		} else {
			if (LoanData.loanTypeData.containsKey(loan_type)) {
				loantype = LoanData.loanTypeData.get(loan_type);
			} else {
				loantype = "-";
			}
		}
		tv_loan_type.setText(loantype);

		tv_loan_acc_num.setText(StringUtil.getForSixForString(String.valueOf(loanChangeRepayAccmap
				.get(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES))));
		String payNumber = String.valueOf(loanChangeRepayAccmap.get(Loan.LOAN_CYCLE_REPAYACCOUNT));
		String payNum = null;
	     if(StringUtil.isNullOrEmptyCaseNullString(payNumber)){
		         payNum = "-";
	       } else {
		         payNum = StringUtil.getForSixForString(payNumber);
	       }
	     //获取借记卡卡号,没处理过的
	     String cardNumsrc=LoanChangeLoanRepayAccountChooseActivity.cardNumsrc;
			//添加一个判断.如果原还款账户和借记卡卡号为同一个账号,则借记卡卡号显示"-"
			if(cardNumsrc.equals(payNumber)){
				tv_loan_old_pay_acc.setText(ConstantGloble.BOCINVT_DATE_ADD );
			}else{
				tv_loan_old_pay_acc.setText(StringUtil.getForSixForString(cardNumsrc));
			}
	     tv_loan_pay_acc_num.setText(payNum);
		tv_loan_new_pay_acc.setText(StringUtil.getForSixForString(String.valueOf(
				loanChangeRepayAccPremap.get(Loan.NEW_PAY_ACCOUNTNUM)
//				LoanChangeLoanRepayReadActivity.numbers
				)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(LoanChangeLoanRepayAccountSuccessActivity.this,
				tv_loan_old_pay_acc_label);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(LoanChangeLoanRepayAccountSuccessActivity.this,
				tv_loan_new_pay_acc_label);

		nextBtn.setOnClickListener(confirmClickListener);
	}

	/** 确定按钮点击事件 */
	OnClickListener confirmClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			isBack=true;
			requestLoanList();
			
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.onKeyDown(keyCode, event);

	}
	/** 变更还款账户--请求贷款账户列表信息 */
	public void requestLoanList() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_ACCOUNT_LIST_API);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Loan.LOANACC_E_LOAN_STATE, "10");
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "loanAccountListCallBack");
	}

	/**
	 * 变更还款账户------请求贷款账户列表回调
	 * 
	 * @param resultObj
	 */
	public void loanAccountListCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框		
		Map<String, Object> loanlistmap = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(loanlistmap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		List<Map<String, String>> resultList = (List<Map<String, String>>) (loanlistmap.get(Loan.LOANACC_LOAN_LIST_RES));
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		//过滤条件:payAccountFlag这个字段只保留"0"下的账户号
		
		for(int i=0;i<resultList.size();i++){
			Map<String, String> map=resultList.get(i);
			String acc=map.get(Loan.LOANACC_PAY_ACCUNTFLAG);
			if("1".equals(acc)){
				resultList.remove(i);
				i--;
			} 
		}
		
		if(resultList.size() <= 0){
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_nullId));
			return;
		}
		Intent intent = new Intent(LoanChangeLoanRepayAccountSuccessActivity.this, LoanChangeLoanRepayAccountChooseActivity.class);
		startActivity(intent);
         finish();
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.LOAN_ACCCHANGE_RESULTLIST, resultList);
		BaseHttpEngine.dissMissProgressDialog();

	}
}
