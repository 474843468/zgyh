package com.chinamworld.bocmbci.biz.loan.loanApply;

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
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.biz.loan.LoanData;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 贷款申请--成功页面 */
public class LoanApplySuccessActivity extends LoanBaseActivity {
	private static final String TAG = "LoanApplySuccessActivity";
	private View detailView = null;

	/** 申请人姓名 */
	private TextView loan_apply_name = null;
	/** 贷款品种 */
	private TextView loan_apply_type = null;	
    /** 联系电话 */
	private TextView loan_apply_iphone = null;	
	/** Email */
	private TextView loan_apply_email = null;
	/** 贷款金额 */
	private TextView amount = null;
	/** 贷款期限 */
	private TextView input_loanPeriod = null;	
	/**币种*/
	private TextView loan_amount_currency = null;	
	/**企业名称*/
	private TextView loan_apply_entName = null;
	/**企业地址*/
	private TextView loan_apply_officeAddress = null;
	/**主营业务*/
	private TextView loan_apply_mainBusiness = null;
	/**负责人姓名*/
	private TextView loan_apply_principalName = null;
	//币种
	private String currency = null;	
	private String code = null;	
	
	/** 业务办理网点*/
	private TextView mOrdeName,mAddress;
	private TextView loan_apply_business_transaction_place_left;
	/** 用户选择的网点信息 */
	Map<String, String> bankMessageMap = null;;
	List<Map<String, String>> addressList;

	/** PsnOnLineLoanDetailQry 接口返回的结果集合 */
	List<Map<String, String>> loanApplyList = null;
	//用户输入的信息
	Map<String, String> resMap = null;
	
	LinearLayout loan_apply_name_title,loan_apply_gender_title,loan_apply_age_value_title,loan_apply_iphone_title,
	          loan_apply_entName_title,loan_apply_input_loanPeriod_title,
                 loan_apply_officeAddress_title,loan_apply_mainBusiness_title,loan_apply_principalName_title,
                  loan_apply_emali_value_title;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.loan_apply_loan_title));
		Button backButton = (Button) findViewById(R.id.ib_back);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		ibBack.setVisibility(View.GONE);
		resMap = (Map<String, String>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.LOAN_APPLY_RESULTMAP);
		bankMessageMap= (Map<String, String>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.LOAN_APPLY_BANKMESSAGEMAP);
		currency = resMap.get(Loan.LOAN_APPLY_CURRENCY_QRY);
//		if("日元".equals(currency)){
//			code = "027";
//		}
		code=LoanData.bociCurcodeMap.get(currency);
		
		init();
	    setDate();
	}
    /**赋值*/
	private void setDate() {
		String sOrdeNames=bankMessageMap.get(Loan.LOAN_APPLY_DEPTNAME_QRY);
		String sAddress=bankMessageMap.get(Loan.LOAN_APPLY_DEPTADDR_QRY);
		//贷款品种
		String loan_apply_types=resMap.get(Loan.LOAN_APPLYPRODUCTNAME_QRY);
		loan_apply_type.setText(loan_apply_types);
//		String sPhone=bankMessageMap.get(Loan.LOAN_APPLY_DEPTPHONE_QRY);
		mOrdeName.setText(sOrdeNames+"\r"+sAddress);
//		mAddress.setText(sAddress);
		
		/*企业名称*/
		loan_apply_entName = (TextView) findViewById(R.id.loan_apply_entName);
		String entName=resMap.get(Loan.LOAN_APPLY_ENTNAME_QRY);
		if(!StringUtil.isNull(entName)){
			loan_apply_entName_title.setVisibility(View.VISIBLE);
			loan_apply_entName.setText(entName);
		}
		/*企业地址*/
//		loan_apply_officeAddress = (TextView) findViewById(R.id.loan_apply_officeAddress);
//		String officeAddress=resMap.get(Loan.LOAN_APPLY_OFFICEADDRESS_QRY);
//		if(!StringUtil.isNull(officeAddress)){
//			loan_apply_officeAddress_title.setVisibility(View.VISIBLE);
//			loan_apply_officeAddress.setText(officeAddress);
//		}
//		/*主营业务*/
//		loan_apply_mainBusiness = (TextView) findViewById(R.id.loan_apply_mainBusiness);
//		String mainBusiness=resMap.get(Loan.LOAN_APPLY_MAINBUSINESS_QRY);
//		if(!StringUtil.isNull(mainBusiness)){
//			loan_apply_mainBusiness_title.setVisibility(View.VISIBLE);
//			loan_apply_mainBusiness.setText(mainBusiness);
//		}
		/*负责人姓名*/
//		loan_apply_principalName = (TextView) findViewById(R.id.loan_apply_principalName);
//         String principalName=resMap.get(Loan.LOAN_APPLY_PRINCIPALNAME_QRY);
//         if(!StringUtil.isNull(principalName)){
//        	 loan_apply_principalName_title.setVisibility(View.VISIBLE);
//        	 loan_apply_principalName.setText(principalName);
//         }
		//联系电话 loan_apply_iphone
		String iphones=resMap.get(Loan.LOAN_APPLY_APPPHONE_QRY);
        if(!StringUtil.isNull(iphones)){
        	loan_apply_iphone_title.setVisibility(View.VISIBLE);
        	loan_apply_iphone.setText(iphones);
        }		
		//email地址   loan_apply_email
//		String loan_apply_emails=resMap.get(Loan.LOAN_APPLY_APPEMAIL_QRY);
//		if("微型企业贷款".equals(loan_apply_types)){
//			loan_apply_emali_value_title.setVisibility(View.VISIBLE);
//			loan_apply_email.setText(loan_apply_emails);
//		}
		//申请人姓名
		String loan_apply_names=resMap.get(Loan.LOAN_APPLY_APPNAME_QRY);
		if(!StringUtil.isNull(loan_apply_names)){
			loan_apply_name_title.setVisibility(View.VISIBLE);
			loan_apply_name.setText(loan_apply_names);
		}
		
		//贷款金额
		String amounts=resMap.get(Loan.LOAN_APPLY_LOANAMOUNT_QRY);
		if(!StringUtil.isNullOrEmptyCaseNullString(amounts)){
			if("外汇留学贷款".equals(loan_apply_types)){
				loan_amount_currency.setVisibility(View.VISIBLE);
				loan_amount_currency.setText(currency);
				TextView loan_amount_left=(TextView) findViewById(R.id.loan_amount_left);
				String loan_amount=getResources().getString(R.string.loan_apply_amount_new);
				loan_amount_left.setText(loan_amount);	
			}
			amount.setText(StringUtil.parseStringCodePattern(code, amounts, 2));
		}
		//贷款期限
		String input_loanPeriods=resMap.get(Loan.LOAN_APPLY_LOANTERM_QRY);
		if(!StringUtil.isNull(input_loanPeriods)){
			loan_apply_input_loanPeriod_title.setVisibility(View.VISIBLE);
			input_loanPeriod.setText(input_loanPeriods);
		}
		
		}

	private void init() {
		detailView = LayoutInflater.from(this).inflate(R.layout.loan_apply_success, null);
		tabcontentView.addView(detailView);
        //申请人姓名
		loan_apply_name = (TextView) findViewById(R.id.loan_apply_client_name);
		//贷款品种
		loan_apply_type=(TextView) findViewById(R.id.loan_apply_type);
		// 联系电话
		loan_apply_iphone = (TextView) findViewById(R.id.loan_apply_iphone_value);
		// email
		loan_apply_email = (TextView) findViewById(R.id.loan_apply_emali_value);
				
		// 贷款金额
		amount = (TextView) findViewById(R.id.loan_amount_value);
		// 贷款期限
		input_loanPeriod = (TextView) findViewById(R.id.loan_choise_input_loanPeriod_value);
		// 贷款币种
		loan_amount_currency = (TextView) findViewById(R.id.loan_amount_currency);
		
		/*企业名称*/
		loan_apply_entName = (TextView) findViewById(R.id.loan_apply_entName);
		/*企业地址*/
		loan_apply_officeAddress = (TextView) findViewById(R.id.loan_apply_officeAddress);
		/*主营业务*/
		loan_apply_mainBusiness = (TextView) findViewById(R.id.loan_apply_mainBusiness);
		/*负责人姓名*/
		loan_apply_principalName = (TextView) findViewById(R.id.loan_apply_principalName);
		
		//网点名称  mOrdeName,mAddress,mPhone
		mOrdeName=(TextView) findViewById(R.id.loan_apply_order_searcharea);
		//网点地址
		mAddress=(TextView) findViewById(R.id.loan_apply_address);
		loan_apply_business_transaction_place_left=(TextView) findViewById(R.id.loan_apply_business_transaction_place_left);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, loan_apply_business_transaction_place_left);
		
		
	
		loan_apply_name_title = (LinearLayout) findViewById(R.id.loan_apply_name_title);
		loan_apply_gender_title = (LinearLayout) findViewById(R.id.loan_apply_gender_title);
		loan_apply_age_value_title = (LinearLayout) findViewById(R.id.loan_apply_age_value_title);
		loan_apply_iphone_title = (LinearLayout) findViewById(R.id.loan_apply_iphone_title);
		loan_apply_emali_value_title = (LinearLayout) findViewById(R.id.loan_apply_emali_value_title);
		loan_apply_input_loanPeriod_title=(LinearLayout) findViewById(R.id.loan_apply_input_loanPeriod_title);
		
		loan_apply_entName_title = (LinearLayout) findViewById(R.id.loan_apply_entName_title);
		loan_apply_officeAddress_title = (LinearLayout) findViewById(R.id.loan_apply_officeAddress_title);
		loan_apply_mainBusiness_title = (LinearLayout) findViewById(R.id.loan_apply_mainBusiness_title);
		loan_apply_principalName_title = (LinearLayout) findViewById(R.id.loan_apply_principalName_title);
//		loan_apply_emali_value_title = (LinearLayout) findViewById(R.id.loan_apply_emali_value_title);
		
		Button loan_tradeButton1 = (Button) findViewById(R.id.loan_tradeButton1);
		
		loan_tradeButton1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				gotoActivity();
			}
		});
	
		Button loan_tradeButton2 = (Button) findViewById(R.id.loan_tradeButton2);

		loan_tradeButton2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent =new Intent(LoanApplySuccessActivity.this, LoanApplyQueryInfoActivity.class);
				startActivity(intent);
				finish();
				ActivityTaskManager.getInstance().removeAllActivityInFirstChange();
			}
		});
	}

	private void gotoActivity() {
		Intent intent = new Intent(LoanApplySuccessActivity.this,
				LoanApplyChooseActivity.class);
		startActivity(intent);
		finish();
		ActivityTaskManager.getInstance().removeAllActivityInFirstChange();
	}
	/**
	 * 屏蔽返回键
	 * */
	@Override
	public void onBackPressed() {
	}

}
