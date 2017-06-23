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
import com.chinamworld.bocmbci.biz.loan.LoanDataCenter;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 贷款申请--单笔账户详情页面 */
public class LoanApplyQueryDetailActivity extends LoanBaseActivity {
	private static final String TAG = "LoanApplyQueryDetailActivity";
	private View detailView = null;
	
	/** 申请人姓名 */
	private  LinearLayout  loan_apply_name_title;
	private TextView loan_apply_name = null;
	/** 性别*/
	private  LinearLayout  loan_apply_gender_title;
	private TextView loan_apply_gender = null;
	/** 年龄 */
	private  LinearLayout  loan_apply_age_title;
	private TextView loan_apply_age = null;
	/** 联系电话 */
	private  LinearLayout  loan_apply_iphone_title;
	private TextView loan_apply_iphone = null;
	/** Email */
	private  LinearLayout  loan_apply_email_title;
	private TextView loan_apply_email = null;
	/** 贷款产品*/
	private TextView loan_apply_product = null;
	/** 房屋交易价格 */
	private  LinearLayout  loan_apply_tradeprice_title;
	private TextView loan_apply_tradeprice=null;
	private TextView loan_apply_house_price_lift=null;
	/** 净车价 */
	private  LinearLayout  loan_apply_car_consume_price_title;
	private TextView loan_apply_car_consume_price=null;
	/** 学费生活费总额 */
	private  LinearLayout  loan_apply_tuitiona_alimony_title;
	private TextView loan_apply_tuitiona_alimony=null;
	/** 贷款 金额 */
	private  LinearLayout  loan_apply_amount_title;
	private TextView loan_apply_amount=null;
	/** 币种 */
	private  LinearLayout  loan_apply_forex_currency_title;
	private TextView loan_apply_forex_currency=null;
	/** 贷款期限 */
	private  LinearLayout  loan_apply_loanperiod_title;
	private TextView loan_apply_loanPeriod=null;
	/** 担保方式*/
	private  LinearLayout  loan_apply_guarantee_way_title;
	private TextView loan_apply_guarantee_way=null;
	
	/**企业名称*/
	private  LinearLayout  loan_apply_entName_title;
	private TextView loan_apply_entName=null;
	/**企业地址*/
	private  LinearLayout  loan_apply_officeAddress_title;
	private TextView loan_apply_officeAddress=null;
	/**主营业务*/
	private  LinearLayout  loan_apply_mainBusiness_title;
	private TextView loan_apply_mainBusiness=null;
	/**负责人姓名*/
	private  LinearLayout  loan_apply_principalName_title;
	private TextView loan_apply_principalName=null;
	
	/** 办理网点*/
	private TextView loan_apply_business=null;
	private TextView loan_apply_business_transaction_place_left=null;
	/** 办理状态*/
	private TextView loan_apply_status=null;
	/** 拒绝原因*/
	private TextView loan_apply_repulse=null;
	
	/**PsnOnLineLoanDetailQry 接口返回的结果集合*/
	List<Map<String, String>> loanApplyList=null ;
	Map<String, Object> loanApplyMap=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.loan_apply_three_new));
		Button backButton = (Button) findViewById(R.id.ib_back);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
//	int	position = getIntent().getIntExtra(ConstantGloble.POSITION, -1);
		
		 loanApplyMap = LoanDataCenter.getInstance().getLoanApplymap();
	
		 if (StringUtil.isNullOrEmpty(loanApplyMap)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		
		init();
		getDate();
	}
   /**获得数据*/
	private void getDate() {
		String name=(String) loanApplyMap.get(Loan.LOAN_APPLY_APPNAME_QRY);
		String gender=(String) loanApplyMap.get(Loan.LOAN_APPLY_APPSEX_QRY);
		String age=(String) loanApplyMap.get(Loan.LOAN_APPLY_APPAGE_QRY);
		String iphone=(String) loanApplyMap.get(Loan.LOAN_APPLY_APPPHONE_QRY);
		String email=(String) loanApplyMap.get(Loan.LOAN_APPLY_APPEMAIL_QRY);
		String product=(String) loanApplyMap.get(Loan.LOAN_APPLY_PRODUCTNAME_QRY);
		String houseTradePrice=(String) loanApplyMap.get(Loan.LOAN_APPLY_HOUSETRADE_PRICE_QRY);
		String amount=(String) loanApplyMap.get(Loan.LOAN_APPLY_LOANAMOUNT_QRY);
		String loanPeriod =(String) loanApplyMap.get(Loan.LOAN_APPLY_LOANTERM_QRY);
		String guarantee_way=(String) loanApplyMap.get(Loan.LOAN_APPLY_GUAWAY_QRY);
		String carTradePrice=(String) loanApplyMap.get(Loan.LOAN_APPLY_CARTRADE_PRICE_QRY);
		String tuitionTradePrice=(String) loanApplyMap.get(Loan.LOAN_APPLY_TUITIONTEADE_PRICE_QRY);
		
		String business=(String) loanApplyMap.get(Loan.LOAN_APPLY_DEPTADDR_QRY);
		/**网点名称*/
		String deptName=(String) loanApplyMap.get(Loan.LOAN_APPLY_DEPTNAME_QRY);
		
		String status=(String) loanApplyMap.get(Loan.LOAN_APPLY_LOANSTATUS_QRY);
		String repulse=(String) loanApplyMap.get(Loan.LOAN_APPLY_REFUSEREASON_QRY);
		
		String entName=(String) loanApplyMap.get(Loan.LOAN_APPLY_ENTNAME_QRY);
		String officeAddress=(String) loanApplyMap.get(Loan.LOAN_APPLY_OFFICEADDRESS_QRY);
		String mainBusiness=(String) loanApplyMap.get(Loan.LOAN_APPLY_MAINBUSINESS_QRY);
		String principalName=(String) loanApplyMap.get(Loan.LOAN_APPLY_PRINCIPALNAME_QRY);
		
		/**币种*/
		String currency=(String) loanApplyMap.get(Loan.LOAN_APPLY_CURRENCY_QRY);
		String loanApplyCurency=LoanData.LoanApplyCurcodeMap.get(currency);
		String currencyCode=LoanData.bociCurcodeMap.get(loanApplyCurency);
       /**product 选择不知道是品种代码还是中文名称 */
	
		String amounts=StringUtil.parseStringCodePattern(currencyCode, amount, 2);
		String tradeprices=StringUtil.parseStringCodePattern(currencyCode, houseTradePrice, 2);
		String carTradePrices=StringUtil.parseStringCodePattern(currencyCode, carTradePrice, 2);
		String tuitionTradePrices=StringUtil.parseStringPattern(tuitionTradePrice, 2);
		
		if(!"微型企业贷款".equals(product)){
			if(!StringUtil.isNullOrEmpty(name)){
		           loan_apply_name_title.setVisibility(View.VISIBLE);			
					loan_apply_name.setText(name);
				}
				if(!StringUtil.isNullOrEmpty(gender)){
					if("1".equals(gender)||"2".equals(gender)){
						loan_apply_gender_title.setVisibility(View.VISIBLE);			
						loan_apply_gender.setText(LoanData.loanAppSexMap.get(gender));
					}
				}
				if(!StringUtil.isNullOrEmpty(age)){
					loan_apply_age_title.setVisibility(View.VISIBLE);
					loan_apply_age.setText(age);
				}
		}
		
		if(!StringUtil.isNullOrEmpty(iphone)){
			loan_apply_iphone_title.setVisibility(View.VISIBLE);			
			loan_apply_iphone.setText(iphone);
		}
		if(!StringUtil.isNullOrEmpty(email)){
			loan_apply_email_title.setVisibility(View.VISIBLE);
			loan_apply_email.setText(email);
		}
		//贷款期限
		if(!StringUtil.isNullOrEmpty(loanPeriod)){
			int loanPeriods=Integer.parseInt(loanPeriod);
			if(loanPeriods != 0){
				loan_apply_loanperiod_title.setVisibility(View.VISIBLE);
				loan_apply_loanPeriod.setText(loanPeriod);
			}
		}
		//担保方式
		if(!StringUtil.isNullOrEmpty(guarantee_way)){
			String guarantee_ways=LoanData.loanGuaWayMap.get(guarantee_way);
			if(!"0".equals(guarantee_way)){
				loan_apply_guarantee_way_title.setVisibility(View.VISIBLE);		
				loan_apply_guarantee_way.setText(guarantee_ways);
			}
		}
		
		
		if(!StringUtil.isNullOrEmpty(product)){
			loan_apply_product.setText(product);
			//"外汇留学贷款" 当时外汇留学贷款时，显示币种字段
			String forex=getResources().getString(R.string.loan_apply_forex);
			if(forex.equals(product)){
				loan_apply_forex_currency_title.setVisibility(View.VISIBLE);
				loan_apply_forex_currency.setText(loanApplyCurency);
			}
		}
		if(!StringUtil.isNullOrEmpty(entName)){
			loan_apply_entName_title.setVisibility(View.VISIBLE);
			loan_apply_entName.setText(entName);
		}
		if(!StringUtil.isNullOrEmpty(officeAddress)){
			loan_apply_officeAddress_title.setVisibility(View.VISIBLE);
			loan_apply_officeAddress.setText(officeAddress);
		}
		
		if(!StringUtil.isNullOrEmpty(mainBusiness)){
			loan_apply_mainBusiness_title.setVisibility(View.VISIBLE);
			loan_apply_mainBusiness.setText(mainBusiness);
		}
		
		if(!StringUtil.isNullOrEmpty(principalName)){
			loan_apply_principalName_title.setVisibility(View.VISIBLE);
			loan_apply_principalName.setText(principalName);
		}
		loan_apply_business.setText(deptName+"   "+business);
		if(!StringUtil.isNullOrEmpty(status)){
			loan_apply_status.setText(LoanData.loanStatusMap.get(status));
			String number=getResources().getString(R.string.loan_apply_number3);
			if(number.equals(status)){
				LinearLayout layout_repulse=(LinearLayout) findViewById(R.id.loan_apply_repulse_left);
				layout_repulse.setVisibility(View.VISIBLE);
				loan_apply_repulse.setText(LoanData.loanRepulseMap.get(repulse));
			}
			
		}
		if(!StringUtil.isNullOrEmpty(amount)){
			String forex=getResources().getString(R.string.loan_apply_forex);
			if(forex.equals(product)){
				TextView loan_apply_amount_left=(TextView) findViewById(R.id.loan_apply_amount_left);
				loan_apply_amount_left.setText(getResources().getString(R.string.loan_apply_amount_new));
			}
		
			double mAmount=Double.parseDouble(amount);
			if(mAmount != 0){
				loan_apply_amount_title.setVisibility(View.VISIBLE);
				loan_apply_amount.setText(amounts);
			}
		}
		if(!StringUtil.isNullOrEmpty(houseTradePrice)){
			double mTradeprices=Double.parseDouble(houseTradePrice);
			if(mTradeprices != 0){
				loan_apply_tradeprice_title.setVisibility(View.VISIBLE);
				loan_apply_tradeprice.setText(tradeprices);
			}
		}
		if(!StringUtil.isNullOrEmpty(carTradePrice)){
			double mTradeprices=Double.parseDouble(carTradePrice);
			if(mTradeprices != 0){
			loan_apply_car_consume_price_title.setVisibility(View.VISIBLE);
			loan_apply_car_consume_price.setText(carTradePrices);
			}
		}
		if(!StringUtil.isNullOrEmpty(tuitionTradePrice)){
			double mTradeprices=Double.parseDouble(tuitionTradePrice);
			if(mTradeprices != 0){
				loan_apply_tuitiona_alimony_title.setVisibility(View.VISIBLE);
				TextView loan_apply_tuitiona_alimony_left=
						(TextView) findViewById(R.id.loan_apply_tuitiona_alimony_left);
				PopupWindowUtils.getInstance().setOnShowAllTextListener(this, loan_apply_tuitiona_alimony_left);
		      loan_apply_tuitiona_alimony.setText(tuitionTradePrices);
			}
		}
	}

	private void init() {
		detailView=LayoutInflater.from(this).inflate(R.layout.loan_apply_query_detail, null);
		tabcontentView.addView(detailView);
		
		loan_apply_name=(TextView) findViewById(R.id.loan_apply_name);
		loan_apply_gender = (TextView) findViewById(R.id.loan_apply_gender);
		loan_apply_age=(TextView) findViewById(R.id.loan_apply_age);
		loan_apply_iphone=(TextView) findViewById(R.id.loan_apply_iphone);
		loan_apply_email = (TextView) findViewById(R.id.loan_apply_emali);
		loan_apply_product = (TextView) findViewById(R.id.loan_apply_product);
		loan_apply_tradeprice = (TextView) findViewById(R.id.loan_apply_tradeprice);
		loan_apply_house_price_lift = (TextView) findViewById(R.id.loan_apply_house_price_lift);
		//房屋交易价（元）
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, loan_apply_house_price_lift);
		loan_apply_car_consume_price = (TextView) findViewById(R.id.loan_apply_car_consume_price);
		loan_apply_tuitiona_alimony = (TextView) findViewById(R.id.loan_apply_tuitiona_alimony);
		loan_apply_amount = (TextView) findViewById(R.id.loan_apply_amount);
		loan_apply_loanPeriod = (TextView) findViewById(R.id.loan_apply_loanPeriod);
		loan_apply_guarantee_way = (TextView) findViewById(R.id.loan_apply_guarantee_way);
		loan_apply_business = (TextView) findViewById(R.id.loan_apply_business_transaction_place);
		loan_apply_business_transaction_place_left = (TextView) findViewById(R.id.loan_apply_business_transaction_place_left);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, loan_apply_business_transaction_place_left);
		
		loan_apply_status=(TextView) findViewById(R.id.loan_apply_status);
		loan_apply_repulse=(TextView) findViewById(R.id.loan_apply_repulse);  
		loan_apply_forex_currency=(TextView) findViewById(R.id.loan_apply_forex_currency);
		
		loan_apply_entName=(TextView) findViewById(R.id.loan_apply_entName);   
		loan_apply_officeAddress=(TextView) findViewById(R.id.loan_apply_officeAddress);   
		loan_apply_mainBusiness=(TextView) findViewById(R.id.loan_apply_mainBusiness);   
		loan_apply_principalName=(TextView) findViewById(R.id.loan_apply_principalName);   
	
		
		loan_apply_entName_title=(LinearLayout) findViewById(R.id.loan_apply_entName_title);
		loan_apply_officeAddress_title=(LinearLayout) findViewById(R.id.loan_apply_officeAddress_title);
		loan_apply_mainBusiness_title=(LinearLayout) findViewById(R.id.loan_apply_mainBusiness_title);
		loan_apply_principalName_title=(LinearLayout) findViewById(R.id.loan_apply_principalName_title);
         
		loan_apply_name_title=(LinearLayout) findViewById(R.id.loan_apply_name_title);
		loan_apply_gender_title=(LinearLayout) findViewById(R.id.loan_apply_gender_title);
		loan_apply_age_title=(LinearLayout) findViewById(R.id.loan_apply_age_title);
		loan_apply_iphone_title=(LinearLayout) findViewById(R.id.loan_apply_iphone_title);
		loan_apply_email_title=(LinearLayout) findViewById(R.id.loan_apply_emali_title);
		loan_apply_tradeprice_title=(LinearLayout) findViewById(R.id.loan_apply_tradeprice_title);
		loan_apply_car_consume_price_title=(LinearLayout) findViewById(R.id.loan_apply_car_consume_price_title);
		loan_apply_tuitiona_alimony_title=(LinearLayout) findViewById(R.id.loan_apply_tuitiona_alimony_title);
		loan_apply_amount_title=(LinearLayout) findViewById(R.id.loan_apply_amount_title);
		loan_apply_loanperiod_title=(LinearLayout) findViewById(R.id.loan_apply_loanPeriod_title);
		loan_apply_guarantee_way_title=(LinearLayout) findViewById(R.id.loan_apply_guarantee_way_title);
//		loan_apply_entName_title=(LinearLayout) findViewById(R.id.loan_apply_entName_title);
		loan_apply_forex_currency_title=(LinearLayout) findViewById(R.id.loan_apply_forex_currency_title);
		
//		Button loan_tradeButton=(Button) findViewById(R.id.loan_tradeButton); 
//		
//		
//		loan_tradeButton.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				gotoActivity();
//			}});
		}

	

	private void gotoActivity() {
		Intent intent = new Intent(LoanApplyQueryDetailActivity.this,
				LoanApplyMenuActivity.class);
		startActivity(intent);
		ActivityTaskManager.getInstance().removeAllActivityInFirstChange();
	}
		
}
