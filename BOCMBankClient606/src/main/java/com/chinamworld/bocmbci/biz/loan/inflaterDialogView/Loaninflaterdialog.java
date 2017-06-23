package com.chinamworld.bocmbci.biz.loan.inflaterDialogView;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.biz.loan.LoanAccountListActivity;
import com.chinamworld.bocmbci.biz.loan.LoanData;
import com.chinamworld.bocmbci.biz.loan.adapter.LoanApplyadapterselectplace;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class Loaninflaterdialog {
	private Context currentContent;
	private View contentView;

	public Loaninflaterdialog(Context currentContent) {
		this.currentContent = currentContent;
	}

	/**
	 * 贷款账户详情弹出窗
	 * 
	 * @param map
	 *            贷款账户详情
	 * @param goAdvanceClick
	 *            提前还款测算监听事件
	 * @return
	 */
	public View initLoanMessageDialogView(Map<String, Object> map, View.OnClickListener goAdvanceClick,
			View.OnClickListener exitDetailClick) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.loan_accmessage, null);
		TextView tv_loan_type = (TextView) contentView.findViewById(R.id.loan_type_value);
//		TextView tv_loan_accNum = (TextView) contentView.findViewById(R.id.loan_actNum_value);
		TextView tv_loan_amount = (TextView) contentView.findViewById(R.id.loan_amount_value);
		TextView tv_loan_currencycode = (TextView) contentView.findViewById(R.id.loan_currencycode_value);
		TextView tv_loan_period = (TextView) contentView.findViewById(R.id.loan_period_value);
		TextView tv_loan_period_unit = (TextView) contentView.findViewById(R.id.loan_period_unit);
		TextView tv_loan_todate = (TextView) contentView.findViewById(R.id.loan_todate_value);
		TextView tv_loan_remaincapital = (TextView) contentView.findViewById(R.id.loan_remaincapital_value);
		TextView tv_loan_interestType = (TextView) contentView.findViewById(R.id.loan_interestType_value);
		TextView tv_loan_remainIssue = (TextView) contentView.findViewById(R.id.loan_remainIssue_value);
		TextView tv_loan_thisIssueRepayDate = (TextView) contentView.findViewById(R.id.loan_thisIssueRepayDate_value);
		TextView loan_debit_card_account=(TextView) contentView.findViewById(R.id.loan_debit_card_account);
		TextView tv_loan_thisIssueRepayAmount = (TextView) contentView
				.findViewById(R.id.loan_thisIssueRepayAmount_value);
		TextView tv_loan_thisIssueRepayInterest = (TextView) contentView
				.findViewById(R.id.loan_thisIssueRepayInterest_value);
		Button btnGoAdvanceRepay = (Button) contentView.findViewById(R.id.btnGoAdvanced);
		/**贷款利率*/
		TextView tv_loan_rate = (TextView) contentView.findViewById(R.id.loan_rate_value);
		/**实际还款账户*/
		TextView tv_loan_reality_account = (TextView) contentView.findViewById(R.id.loan_reality_account_value);
		/**对应还款账户借记卡卡号*/
		TextView tv_loan_debit_card_account = (TextView) contentView.findViewById(R.id.loan_debit_card_account_value);
		btnGoAdvanceRepay.setOnClickListener(goAdvanceClick);
		ImageView img_exit = (ImageView) contentView.findViewById(R.id.img_exit_loandetail);
		img_exit.setOnClickListener(exitDetailClick);
		// 进行赋值
		String currency = (String) map.get(Loan.LOANACC_CURRENCYCODE_RES);
		String loan_type = (String) map.get(Loan.LOANACC_LOAN_TYPE_RES);
		tv_loan_type.setText((StringUtil.isNull(loan_type)) ? ConstantGloble.BOCINVT_DATE_ADD :
			         LoanData.loanTypeData.get(loan_type));
//		String loan_accNum = (String) map.get(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES);
//		tv_loan_accNum.setText((StringUtil.isNull(loan_accNum)) ? ConstantGloble.BOCINVT_DATE_ADD : StringUtil
//				.getForSixForString(loan_accNum));
		String amount = (String.valueOf(map.get(Loan.LOANACC_LOAN_AMOUNT_RES)));
		tv_loan_amount.setText((StringUtil.parseStringCodePattern(currency, amount, 2)));
		tv_loan_currencycode.setText((StringUtil.isNull(currency)) ? ConstantGloble.BOCINVT_DATE_ADD
				: LocalData.Currency.get(currency));
		String periodunit = (String) map.get(Loan.LOANACC_LOAN_PERIODUNIT_RES);
		String noValue = "(-)" + ConstantGloble.ACC_COLON;
		String value =  LoanData.loanUnit.get(periodunit) + ConstantGloble.ACC_COLON;
		tv_loan_period_unit.setText((StringUtil.isNull(periodunit)) ? noValue : value);
		String period = String.valueOf(map.get(Loan.LOANACC_LOAN_PERIOD_RES));
		tv_loan_period.setText((StringUtil.isNull(period)) ? ConstantGloble.BOCINVT_DATE_ADD : period);
		String todate = String.valueOf(map.get(Loan.LOANACC_LOAN_TODATE_RES));
		tv_loan_todate.setText((StringUtil.isNull(todate)) ? ConstantGloble.BOCINVT_DATE_ADD : todate);
		String remaincapital = String.valueOf(map.get(Loan.LOANACC_REMAINCAPITAL_RES));
		tv_loan_remaincapital.setText((StringUtil.isNull(remaincapital)) ? ConstantGloble.BOCINVT_DATE_ADD : 
			(StringUtil.parseStringCodePattern(currency, remaincapital, 2)));
		
		/**还款方式逻辑判断
		 * 还款方式	还款周期	渠道展示内容
           B:只还利息	98:到期时	到期一次还本付息
           B:只还利息	月\二月\季\四月\半年\年	按期还息到期还本
           N:协议还款	月\二月\季\四月\半年\年	按期还息灵活还本
           F:等额本息	双周\月\二月\季\四月\半年\年	等额本息
           G:等额本金	月\二月\季\四月\半年\年	等额本金
		 * */
		/**还款方式*/
		String interestType = (String) map.get(Loan.LOANACC_INTERESTTYPE_RES);
		//定义一个还款周期变量
		String loanRepayPeriod=LoanAccountListActivity.loanRepayPeriod;
		if("B".equals(interestType)){
			if("98".equals(loanRepayPeriod)){
				tv_loan_interestType.setText("到期一次还本付息");
			}else{
				tv_loan_interestType.setText("按期还息到期还本");
			}
		}else{
			tv_loan_interestType.setText((StringUtil.isNull(interestType)) ? ConstantGloble.BOCINVT_DATE_ADD
					: LoanData.loanInterestType_mondy.get(interestType));
		}
		//贷款剩余期数
		String remainIssue = (String) map.get(Loan.LOANACC_REMAINISSUE_RES);
		
		tv_loan_remainIssue.setText((StringUtil.isNull(remainIssue)) ? ConstantGloble.BOCINVT_DATE_ADD : remainIssue);
		String issuerepaydate = (String) map.get(Loan.LOANACC_THISISSUEREPAYDATE_RES);
		tv_loan_thisIssueRepayDate.setText((StringUtil.isNull(issuerepaydate)) ? ConstantGloble.BOCINVT_DATE_ADD
				: (issuerepaydate));
		String issuerepayAmount = (String.valueOf(map.get(Loan.LOANACC_THISISSUEREPAYAMOUNT_RES)));
		tv_loan_thisIssueRepayAmount.setText(StringUtil.isNull(issuerepayAmount) ? ConstantGloble.BOCINVT_DATE_ADD
				:(StringUtil.parseStringCodePattern(currency, issuerepayAmount, 2)));
		String issuerepayInterest = String.valueOf(map.get(Loan.LOANACC_THISISSUEREPAYINTEREST_RES));
		tv_loan_thisIssueRepayInterest.setText(StringUtil.isNull(issuerepayInterest) ? ConstantGloble.BOCINVT_DATE_ADD
				:(StringUtil.parseStringCodePattern(currency, issuerepayInterest, 2)));
	   /**贷款利率 605改造字段修改*/
		String loanRate = String.valueOf(map.get(Loan.LOAN_CYCLERATE));
		tv_loan_rate.setText(StringUtil.isNull(loanRate) ? ConstantGloble.BOCINVT_DATE_ADD:
			                   loanRate +"%");
		/**实际还款账户 605接口字段改造*/
		String payAccountNumber = String.valueOf(map.get(Loan.LOAN_CYCLE_REPAYACCOUNT));
		tv_loan_reality_account.setText(StringUtil.isNull(payAccountNumber)?ConstantGloble.BOCINVT_DATE_ADD
				: StringUtil.getForSixForString(payAccountNumber));
        /**获得对应还款账户借记卡卡号*/
		String cardAccunt=LoanAccountListActivity.cardAccunt;
		PopupWindowUtils.getInstance().setOnShowAllTextListener(currentContent, loan_debit_card_account);	
		if(cardAccunt.equals(payAccountNumber)){
			tv_loan_debit_card_account.setText( ConstantGloble.BOCINVT_DATE_ADD);
		}else{
		  tv_loan_debit_card_account.setText(StringUtil.isNull(cardAccunt) ? ConstantGloble.BOCINVT_DATE_ADD
				: StringUtil.getForSixForString(cardAccunt));
		}
		return contentView;
	}

	/**
	 * 个人循环贷款账户详情弹出窗
	 * 
	 * @return
	 */
	public View initLoanCycleMessageDialogView(Map<String, Object> map, View.OnClickListener goLoanUseClick,
			View.OnClickListener goLoanUseQueryClick, View.OnClickListener exitDetailClick) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.loan_cycle_accmessage, null);
		// 贷款品种
		TextView tv_loan_type = (TextView) contentView.findViewById(R.id.loan_type_value);
		String loan_type = (String) map.get(Loan.LOANACC_LOAN_TYPE_RES);
		tv_loan_type.setText(LoanData.loanTypeData.get(loan_type));
		// 贷款账号
		TextView tv_loan_accNum = (TextView) contentView.findViewById(R.id.loan_actNum_value);
		String loan_accNum = (String) map.get(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES);
		tv_loan_accNum.setText((StringUtil.isNull(loan_accNum)) ? ConstantGloble.BOCINVT_DATE_ADD : StringUtil
				.getForSixForString(loan_accNum));
		// 币种
		TextView loan_currencycode_value = (TextView) contentView.findViewById(R.id.loan_currencycode_value);
		String currency = (String) map.get(Loan.LOANACC_CURRENCYCODE_RES);
		loan_currencycode_value.setText((StringUtil.isNull(currency)) ? ConstantGloble.BOCINVT_DATE_ADD
				: LocalData.Currency.get(currency));
		// 核准金额
		TextView loan_cycleAppAmount_value = (TextView) contentView.findViewById(R.id.loan_cycleAppAmount_value);
		String cycleAppAmount = (String.valueOf(map.get(Loan.LOAN_CYCLE_APPAMOUNT)));
		loan_cycleAppAmount_value.setText(StringUtil.parseStringCodePattern(currency,cycleAppAmount, 2));
		// 累计放款金额
		TextView loan_cycleAdvVal_value = (TextView) contentView.findViewById(R.id.loan_cycleAdvVal_value);
		String cycleAdvVal = (String.valueOf(map.get(Loan.LOAN_CYCLE_ADVVAL)));
		loan_cycleAdvVal_value.setText(StringUtil.parseStringCodePattern(currency,cycleAdvVal, 2));
		// 贷款余额
		TextView loan_cycleBalance_value = (TextView) contentView.findViewById(R.id.loan_cycleBalance_value);
		String cycleBalance = (String.valueOf(map.get(Loan.LOAN_CYCLE_BALANCE)));
		loan_cycleBalance_value.setText(StringUtil.parseStringCodePattern(currency,cycleBalance, 2));
		// 可用金额
		TextView loan_cycleAvaAmount_value = (TextView) contentView.findViewById(R.id.loan_cycleAvaAmount_value);
		String cycleAvaAmount = (String.valueOf(map.get(Loan.LOAN_CYCLE_AVAAMOUNT)));
		loan_cycleAvaAmount_value.setText(StringUtil.parseStringCodePattern(currency,cycleAvaAmount, 2));
		// 放款截止日
		TextView loan_cycleDrawdownDate_value = (TextView) contentView.findViewById(R.id.loan_cycleDrawdownDate_value);
		String cycleDrawdownDate = (String.valueOf(map.get(Loan.LOAN_CYCLE_DRAWDOWNDATE)));
		loan_cycleDrawdownDate_value.setText(cycleDrawdownDate);
		// 到期日
		TextView loan_cycleMatDate_value = (TextView) contentView.findViewById(R.id.loan_cycleMatDate_value);
		String cycleMatDate = (String.valueOf(map.get(Loan.LOAN_CYCLE_MATDATE)));
		loan_cycleMatDate_value.setText(StringUtil.isNullOrEmptyCaseNullString(cycleMatDate)?ConstantGloble.BOCINVT_DATE_ADD:
			cycleMatDate);

		Button btnGoLoanUse = (Button) contentView.findViewById(R.id.btnGoLoanUse);
		if (goLoanUseClick == null) {
			btnGoLoanUse.setVisibility(View.GONE);
		} else {
			btnGoLoanUse.setVisibility(View.VISIBLE);
			btnGoLoanUse.setOnClickListener(goLoanUseClick);
		}
		Button btnGoLoanUseQuery = (Button) contentView.findViewById(R.id.btnGoLoanUseQuery);
		btnGoLoanUseQuery.setOnClickListener(goLoanUseQueryClick);
		ImageView img_exit = (ImageView) contentView.findViewById(R.id.img_exit_loandetail);
		img_exit.setOnClickListener(exitDetailClick);

		return contentView;
	}
	
	
	/**
	 * 贷款申请地址选择弹出款
	 * 
	 * @return
	 */
	public View initLoanApplyMessageDialogView(Context context,
			final List<Map<String, Object>> listMap,int totalCount,  int selectPosition,
			OnItemClickListener onItemClickListener,
			View.OnClickListener exitDetailClick,
			View.OnClickListener onClicklistener) {
		contentView = LayoutInflater.from(currentContent).inflate(
				R.layout.loan_apply_select_place_messagedialog_item, null);
		ListView listView = (ListView) contentView
				.findViewById(R.id.loan_select_place_listview);
		LoanApplyadapterselectplace adapter = new LoanApplyadapterselectplace(
				context, listMap);
		adapter.setSelectPosition(selectPosition);
		View viewFooter = LayoutInflater.from(context).inflate(
				R.layout.acc_load_more, null);
		viewFooter.setOnClickListener(onClicklistener);
		if (listMap.size() >= totalCount) {
			listView.removeFooterView(viewFooter);
		} else {
			if (listView.getFooterViewsCount() > 0) {

			} else {
				listView.addFooterView(viewFooter);
			}
		}
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(onItemClickListener);
		ImageView img_exit = (ImageView) contentView
				.findViewById(R.id.img_exit_loandetail);
		img_exit.setOnClickListener(exitDetailClick);

		return contentView;
	}

}
