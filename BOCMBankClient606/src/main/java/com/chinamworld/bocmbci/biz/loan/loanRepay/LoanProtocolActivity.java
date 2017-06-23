package com.chinamworld.bocmbci.biz.loan.loanRepay;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;


/**提前还款协议界面*/

public class LoanProtocolActivity extends LoanBaseActivity {
	
	/**不接受按钮*/
	private Button lastButton;
	/**确认按钮*/
	private Button sureButton;
	
	/**用户姓名*/
	private String userName;
	/**居民身份证号*/
	private String  identityCard;
	/**身份证类型*/
	private String papersType;
	/**贷款帐号*/
	private String loanAccount;
	/**提前还款金额*/
	private String refundMoney;
	/**提前还款金额 币种*/
	private String currencyType;
	/**提前还款本金金额  币种*/
	private String currentCurrencyType;
	/**提前还款本金金额*/
	private String currentRefundMoney;
	/**提前还款利息 币种*/
	private String interestType;
	/**提前还款利息*/
	private String interestMoney;
	/**还款利息 月数*/
	private String month;
	/**手续费 */
	private String procedureMoney;
	/**优惠后 手续费金额*/
    private String privilegeProcedure;
    /**产品类型*/
    private String loanType = null;
    /**贷款人*/
    private TextView  loanChoiseInputLoanUser;
    /**借款人*/
    private TextView loanChoiseInputLoanType;
    /**身份证 类型*/
    private TextView loanChoiseReadTwo;
    /**身份证 号码*/
    private TextView  loanChoiseReadThree;
    private TextView headText = null;
	/** 线上标识*/
	String  onlineFlag=null;
	/**循环类型*/
	 private  String cycleType;
	
    Map<String, Object> preResult;
    private Map<String, Object> detailRasultMap = null;
    /** 预交易数据 */
	private Map<String, Object> securityMap = null;
	Map<String, String> loanRepayCount=null;
    
    private View readView = null;
    private Button refuseButton = null;
	private Button receptButton = null;
	
	private TextView refundMoneyView;
	private TextView currentRefundMoneyView;
	private TextView interestMoneyView;
	private TextView procedureMoneyView;
	private TextView privilegeProcedureView;
	private TextView text_pro;
	private TextView text_pro2;
	private TextView text_pro3;
	private TextView text_pro4;
	/**币种*/
	private String currency;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle(getResources().getString(R.string.loan_repay_protocol));
		Button backButton = (Button) findViewById(R.id.ib_back);
		backButton.setVisibility(View.GONE);
		preResult =(Map<String, Object>)BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOAN_FACTORLIST); 
		detailRasultMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOAN_RESULT);
		securityMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOAN_FACTORLIST);
		loanRepayCount = (Map<String, String>) securityMap.get(Loan.LOAN_LOANREPAYCOUNT_RES);
		init();
		getDate();
		initOnClick();
	}
	
	private void init (){
		readView = LayoutInflater.from(this).inflate(R.layout.loan_protocol_read, null);
		tabcontentView.addView(readView);
//		headText=(TextView) findViewById(R.id.headText);
//		loanChoiseInputLoanUser = (TextView)findViewById(R.id.loan_choise_input_loanUser);
//		loanChoiseInputLoanType = (TextView)findViewById(R.id.loan_choise_input_loanType);
//		loanChoiseReadTwo = (TextView)findViewById(R.id.loan_choise_read_two);
//		loanChoiseReadThree = (TextView)findViewById(R.id.loan_choise_read_three);
		refuseButton = (Button) findViewById(R.id.lastButton);
		receptButton = (Button) findViewById(R.id.sureButton);
		
		text_pro = (TextView) findViewById(R.id.text_pro);
		text_pro2 = (TextView) findViewById(R.id.text_pro2);
		text_pro3 = (TextView) findViewById(R.id.text_pro3);
		text_pro4 = (TextView) findViewById(R.id.text_pro4);
		refundMoneyView = (TextView) findViewById(R.id.refundMoney);
		currentRefundMoneyView = (TextView) findViewById(R.id.currentRefundMoney);
		interestMoneyView = (TextView) findViewById(R.id.interestMoney);
		procedureMoneyView = (TextView) findViewById(R.id.procedureMoney);
		privilegeProcedureView = (TextView) findViewById(R.id.privilegeProcedure);
	}
	
	private void initOnClick() {
		refuseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		receptButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				gotoActivity();
			
			}
		});
	}

	private  void getDate(){
		Map<String, Object> returnMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA);
		String loginName = String.valueOf(returnMap.get(Crcd.CRCD_CUSTOMERNAME));
		String identityType = String.valueOf(returnMap.get(Login.IDENTIFY_TYPE));
		String identityNumber = String.valueOf(returnMap.get(Login.IDENTIFY_NUM));
		String type = null;
		if (LocalData.IDENTITYTYPE.containsKey(identityType)) {
			type = LocalData.IDENTITYTYPE.get(identityType);
		} else {
			type = "-";
		}
//		loanChoiseInputLoanUser.setText("中国银行股份有限公司");
//		loanChoiseInputLoanType.setText(loginName);
//		loanChoiseReadTwo.setText(type);
//		loanChoiseReadThree.setText(identityNumber);
		
        //线上标识
		onlineFlag=LoanRepayAccountActivity.onlineFlag;
		//循环类型
		cycleType=LoanRepayAccountActivity.cycleType;
		//贷款账户
		loanAccount = (String) detailRasultMap.get(Loan.LOAN_LOANACCOUNT_REQ);
		//产品类型
		loanType=(String) detailRasultMap.get(Loan.LOANACC_LOAN_TYPE_RES);
		/**提前还款币种*/
		currency = (String)detailRasultMap.get(Loan.LOAN_CURRENCY_REQ);
		//提前还款金额币种
		currencyType=LocalData.Currency.get((String) detailRasultMap.get(Loan.LOAN_CURRENCY_REQ));
		//提前还款金额
		refundMoney= getIntent().getStringExtra("repayAmount");
		refundMoney=StringUtil.parseStringCodePattern(currency,refundMoney , 2);
		//提前还款本金
		currentRefundMoney=loanRepayCount.get(Loan.LOAN_ADVANCEREPAYCAPITAL_RES);
		currentRefundMoney=StringUtil.parseStringCodePattern(currency,currentRefundMoney , 2);
		//提前还款利息金额
		interestMoney=loanRepayCount.get(Loan.LOAN_ADVANCEREPAYINTEREST_RES);
		interestMoney=StringUtil.parseStringCodePattern(currency,interestMoney , 2);
		//手续费金额
		procedureMoney=loanRepayCount.get(Loan.LOAN_CHARGES_RES);
		procedureMoney=StringUtil.parseStringCodePattern(currency,procedureMoney , 2);
		//优惠后手续费金额
			if("R".equals(cycleType)&&"1".equals(onlineFlag)){
				if(LocalData.codeNoNumber.contains(currency)){
					privilegeProcedure="0";
				}else {
					privilegeProcedure="0.00";
				}
			}else{
				privilegeProcedure=procedureMoney;
			}
//		String headPro = getResources().getString(R.string.loan_refund_before_pro2);
		String headPro2 = getResources().getString(R.string.loan_refund_before_pro2);
		headPro2 = headPro2.replace("userName", loginName);
		headPro2 = headPro2.replace("papersType", type);
		headPro2 = headPro2.replace("identityCard", identityNumber);
		headPro2 = headPro2.replace("currencyType", currencyType);
		headPro2 = headPro2.replace("loanAccount", StringUtil.getForSixForString(loanAccount));
		text_pro.setText(headPro2);
		
		String headPro3 = getResources().getString(R.string.loan_refund_before_pro3);
		headPro3 = headPro3.replace("currentCurrencyType", currencyType);
		text_pro2.setText(headPro3);
		
		String headPro4 = getResources().getString(R.string.loan_refund_before_pro4);
		headPro4 = headPro4.replace("interestType", currencyType);
		text_pro3.setText(headPro4);
		String moth=null;
		if("1044".equals(loanType)){
			moth="0";
		}else{
			moth="1";
		}
		String headPro5 = getResources().getString(R.string.loan_refund_before_pro5);
		headPro5 = headPro5.replace("moth", moth);
		text_pro4.setText(headPro5);
//		headPro = headPro.replace("loanAccount",StringUtil.getForSixForString(loanAccount) );
//		headPro = headPro.replace("currencyType", currencyType);
//		headPro = headPro.replace("refundMoney",StringUtil.parseStringCodePattern(currencyType, refundMoney, 2) );
//		headPro = headPro.replace("currentRefundMoney", Html.fromHtml("<font color='#FFFF0000'>"+currentRefundMoney+"</font>"));
//		headPro = headPro.replace("interestMoney", StringUtil.parseStringCodePattern(currencyType, interestMoney, 2));
//		headPro = headPro.replace("interestType", currencyType);
	
//		headPro = headPro.replace("name", loginName);
//		headPro = headPro.replace("procedureMoney",StringUtil.parseStringCodePattern(currencyType,procedureMoney , 2));// 手续费
//		headPro = headPro.replace("privilegeProcedure", StringUtil.parseStringCodePattern(currencyType,privilegeProcedure , 2));//优惠后手续费
//		headPro = headPro.replace("userName", loginName);//借款人
//		headPro = headPro.replace("papersType", type);// 身份证类型
//		headPro = headPro.replace("identityCard", identityNumber);// 身份证号
		
		

//		refundMoney
		String middle = getResources().getString(R.string.loan_refund_before_pro7);
		
		String end =getResources().getString(R.string.loan_refund_before_pro8);
		//生产问题修改 预交易repayAmount上送“提前还款本金”（用户输入）和“thisIssueRepayInterest截止当前应还利息”（详情接口返回数据）的总和。
		Double thisIssueRepayInterestss = (double) 0;
		if(!StringUtil.isNullOrEmpty((String) detailRasultMap.get(Loan.LOAN_THISISSUEREPAYINTEREST_REQ))){
			thisIssueRepayInterestss= Double.valueOf((String) detailRasultMap.get(Loan.LOAN_THISISSUEREPAYINTEREST_REQ));
		}
		String money = String.valueOf(Double.valueOf(refundMoney)+thisIssueRepayInterestss);
		money = StringUtil.parseStringCodePattern(currency, money, 2);
		SpannableString sp = new SpannableString(middle + money + end);
		sp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), middle.length(),
				middle.length() + money.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		refundMoneyView.setText(sp);
		///////////////////
		
		SpannableString sp1 = new SpannableString(middle + currentRefundMoney + end);
		sp1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), middle.length(),
				middle.length() + currentRefundMoney.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		currentRefundMoneyView.setText(sp1);
		///////////////////
		SpannableString sp2 = new SpannableString(middle + interestMoney + end);
		sp2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), middle.length(),
				middle.length() + interestMoney.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		interestMoneyView.setText(sp2);
		
		////////////////////
		
		String middle1 = "手续费金额为";
		
		SpannableString sp3 = new SpannableString(middle1 + procedureMoney +"，");
		sp3.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), middle1.length(),
				middle1.length() + procedureMoney.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		procedureMoneyView.setText(sp3);
		
		////////////////////////////
		
        String middle2 = "优惠后手续费金额为";
		
		SpannableString sp4 = new SpannableString(middle2 + privilegeProcedure  +"。");
		sp4.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), middle2.length(),
				middle2.length() + privilegeProcedure.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		privilegeProcedureView.setText(sp4);
		
		
		
	}
	
	private void gotoActivity() {
		Intent intent = new Intent(LoanProtocolActivity.this, LoanRepayAccountConfirmActivity.class);
		intent.putExtra(ConstantGloble.POSITION, getIntent().getIntExtra(ConstantGloble.POSITION, -1));
		intent.putExtra(ConstantGloble.FOREX_SELLINLIST_POSITION, getIntent().getIntExtra(ConstantGloble.FOREX_SELLINLIST_POSITION, -1));
		intent.putExtra("repayAmount", getIntent().getStringExtra("repayAmount"));
		intent.putExtra(Loan.LOAN_FROMACCOUNTID_RES, getIntent().getStringExtra(Loan.LOAN_FROMACCOUNTID_RES));
		intent.putExtra(Loan.LOAN_ACCOUNTNUMBERS_RES, getIntent().getStringExtra(Loan.LOAN_ACCOUNTNUMBERS_RES));
		intent.putExtra(Loan.LOAN_PRIVILEGE_PROCEDURE_RES,privilegeProcedure);
		BaseHttpEngine.dissMissProgressDialog();
		startActivity(intent);
		
	}
	
}
