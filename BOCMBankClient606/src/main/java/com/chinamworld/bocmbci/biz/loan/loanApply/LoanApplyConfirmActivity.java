package com.chinamworld.bocmbci.biz.loan.loanApply;

import java.util.HashMap;
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
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.biz.loan.LoanData;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 贷款申请--确认页面 */
public class LoanApplyConfirmActivity extends LoanBaseActivity {
	private static final String TAG = "LoanApplyQueryDetailActivity";
	private View detailView = null;

	/** 申请人姓名 */
	private TextView loan_apply_name = null;
	/** 所在的城市 */
	private TextView loan_apply_city = null;
	/** 贷款品种 */
	private TextView loan_apply_type = null;
	private String loan_apply_types ;
	/** 贷款币种 */
	private TextView loan_currency = null;
	private String currency = null;
	private String code = null;
	/** 地址tetile */
	private TextView select_place_left = null;
	/** 性别 */
	private TextView loan_apply_gender = null;
	/**企业名称*/
	private TextView loan_apply_entName = null;
	/**企业地址*/
	private TextView loan_apply_officeAddress = null;
	/**主营业务*/
	private TextView loan_apply_mainBusiness = null;
	/**负责人姓名*/
	private TextView loan_apply_principalName = null;
	/** 担保方式 */
	private TextView guarantee_way = null;
	/** 担保类别 */
	private TextView guarantee = null;
	/** 是否提供抵押担保 */
	private TextView loan_apply_guaTypeFlag_left = null;
	private TextView guaTypeFlag = null;
	/** 年龄 */
	private TextView loan_apply_age = null;
	/** 联系电话 */
	private TextView loan_apply_iphone = null;
	/** Email */
	private TextView loan_apply_email = null;
	/** 房屋交易价 */
	private TextView house_price = null;
	private TextView loan_apply_house_price_left = null;
	private String house_prices;
	/** 学费生活费总额 */
	private TextView tuitiona_alimony = null;
	private TextView tuitiona_alimony_left = null;
	private String tuitiona_alimonys;
	/** 净车价 */
	private TextView car_price = null;
	private String car_prices;
	/** 贷款金额 */
	private TextView amount = null;
	//未格式化的金额
	private String  amounts;
	/** 贷款期限 */
	private TextView input_loanPeriod = null;
	/** 所购住房房龄 */
	private TextView buy_house = null;
	private TextView buy_house_age_left = null;
	
	/** 币种Spinner */
	private TextView mCurrency = null;
	/** 业务办理网点 */
	private TextView business_transaction_place = null;
	private TextView loan_apply_business_transaction_place_left = null;
	/** 用户选择的网点信息 */
	Map<String, String> bankMessageMap = null;;
	List<Map<String, String>> addressList;
	private String token;
	//判断是否需要担保类别
	private String bGuarantee ;

	private int positions;

	LinearLayout loan_apply_client_name_title,loan_apply_gender_title,loan_apply_age_value_title,loan_apply_entName_title,
	           loan_apply_officeAddress_title,loan_apply_mainBusiness_title,loan_apply_principalName_title,
	           loan_apply_iphone_value_title,loan_apply_emali_value_title,
	         loan_apply_house_price_title,loan_apply_tuitiona_alimony_title, loan_apply_car_price_title,
			loan_amount_title, loan_choise_input_loanPeriod_title,loan_apply_buy_house_age_title, 
			loan_apply_guarantee_way_title,loan_apply_guarantee_title,loan_apply_spinner_currency_title,
			loan_apply_guaTypeFlag_title;

	/** PsnOnLineLoanDetailQry 接口返回的结果集合 */
	List<Map<String, String>> loanApplyList = null;
	// 用户输入的信息
	Map<String, String> resMap = null;
	// PsnOnLineLoanFieldQry 接口返回的数据
	Map<String, Object> LoanFieldMap = null;
	private String cityCode, productCode, commConversationId;
	// 记录位置
	private int mLoanApplyMessageDialogSelectPosition = -1;
	/***/
	private String appNameNeed, appAgeNeed, appSexNeed, appPhoneNeed,appEmailNeed, entNameNeed,officeAddressNeed,
	               mainBusinessNeed,principalNameNeed, houseTradePriceNeed, tuitionTradePriceNeed,
			carTradePriceNeed, loanAmountNeed, loanTermNeed, currencyNeed,
			houseAgeNeed, guaWayNeed, guaTypeFlagNeed;

	@SuppressWarnings("unchecked")
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

		resMap = (Map<String, String>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.LOAN_APPLY_RESULTMAP);
		bankMessageMap = (Map<String, String>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.LOAN_APPLY_BANKMESSAGEMAP);
		// PsnOnLineLoanFieldQry 接口返回的数据
		LoanFieldMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.LOAN_RESULT);
		cityCode = getIntent().getStringExtra("cityCode");
		productCode = getIntent().getStringExtra("typeCode");
		//
		bGuarantee = getIntent().getStringExtra("btGuarantee");
		commConversationId = getIntent().getStringExtra("commConversationId");

		init();
		setDate();
		getVisibilityDate();
	}

	/** 获得数据 */
	private void getVisibilityDate() {
		appNameNeed = (String) LoanFieldMap.get(Loan.LOAN_APPLY_APPNAMENEED_QRY);
		appAgeNeed = (String) LoanFieldMap.get(Loan.LOAN_APPLY_APPAGENEED_QRY);
		appSexNeed = (String) LoanFieldMap.get(Loan.LOAN_APPLY_APPSEXNEED_QRY);
		appPhoneNeed = (String) LoanFieldMap.get(Loan.LOAN_APPYL_APPPHONENEED_QRY);
		appEmailNeed = (String) LoanFieldMap.get(Loan.LOAN_APPLY_APPEMAILNEED_QRY);
		
		entNameNeed = (String) LoanFieldMap.get(Loan.LOAN_APPLY_ENTNAMENEED_QRY);
		officeAddressNeed = (String) LoanFieldMap.get(Loan.LOAN_APPLY_OFFICEADDRESSNEED_QRY);
		mainBusinessNeed = (String) LoanFieldMap.get(Loan.LOAN_APPLY_MAINBUSINESSNEED_QRY);
		principalNameNeed = (String) LoanFieldMap.get(Loan.LOAN_APPLY_PRINCIPALNAMENEED_QRY);
		
        
		houseTradePriceNeed = (String) LoanFieldMap.get(Loan.LOAN_APPLY_HOUSETRADEPRICENEED_QRY);
		tuitionTradePriceNeed = (String) LoanFieldMap.get(Loan.LOAN_APPLY_TUITIONTRADEPRICEENEED_QRY);
		carTradePriceNeed = (String) LoanFieldMap.get(Loan.LOAN_APPLY_CARTRADEPRICEENEED_QRY);
		loanAmountNeed = (String) LoanFieldMap.get(Loan.LOAN_APPLY_LOANAMOUNTNEED_QRY);
		loanTermNeed = (String) LoanFieldMap.get(Loan.LOAN_APPLY_LOANTERMNEED_QRY);
		currencyNeed = (String) LoanFieldMap.get(Loan.LOAN_APPLY_CURRENCYNEED_QRY);
		houseAgeNeed = (String) LoanFieldMap.get(Loan.LOAN_APPLY_HOUSEAGENEED_QRY);
		guaWayNeed = (String) LoanFieldMap.get(Loan.LOAN_APPLY_GUAWAYNEED_QRY);
		guaTypeFlagNeed = (String) LoanFieldMap.get(Loan.LOAN_APPLY_GUATYPEFLAGNEED_QRY);

		loan_apply_client_name_title = (LinearLayout) findViewById(R.id.loan_apply_client_name_title);
		loan_apply_gender_title = (LinearLayout) findViewById(R.id.loan_apply_gender_title);
		loan_apply_age_value_title = (LinearLayout) findViewById(R.id.loan_apply_age_value_title);
		
		
		loan_apply_entName_title = (LinearLayout) findViewById(R.id.loan_apply_entName_title);
		loan_apply_officeAddress_title = (LinearLayout) findViewById(R.id.loan_apply_officeAddress_title);
		loan_apply_mainBusiness_title = (LinearLayout) findViewById(R.id.loan_apply_mainBusiness_title);
		loan_apply_principalName_title = (LinearLayout) findViewById(R.id.loan_apply_principalName_title);
		
		loan_apply_iphone_value_title = (LinearLayout) findViewById(R.id.loan_apply_iphone_value_title);
		loan_apply_emali_value_title = (LinearLayout) findViewById(R.id.loan_apply_emali_value_title);
		
		loan_apply_house_price_title = (LinearLayout) findViewById(R.id.loan_apply_house_price_title);
		loan_apply_tuitiona_alimony_title = (LinearLayout) findViewById(R.id.loan_apply_tuitiona_alimony_title);
		loan_apply_car_price_title = (LinearLayout) findViewById(R.id.loan_apply_car_price_title);
		loan_amount_title = (LinearLayout) findViewById(R.id.loan_amount_title);
		loan_choise_input_loanPeriod_title = (LinearLayout) findViewById(R.id.loan_choise_input_loanPeriod_title);
		loan_apply_buy_house_age_title = (LinearLayout) findViewById(R.id.loan_apply_buy_house_age_title);
		loan_apply_guarantee_way_title = (LinearLayout) findViewById(R.id.loan_apply_guarantee_way_title);
		loan_apply_guarantee_title = (LinearLayout) findViewById(R.id.loan_apply_guarantee_title);
		loan_apply_guaTypeFlag_title = (LinearLayout) findViewById(R.id.loan_apply_guaTypeFlag_title);
		loan_apply_spinner_currency_title = (LinearLayout) findViewById(R.id.loan_apply_currency_title);

		// 币种
		if (ConstantGloble.APPLY_VISIBILITY.equals(currencyNeed)) {
			loan_apply_spinner_currency_title.setVisibility(View.VISIBLE);
			currency = resMap.get(Loan.LOAN_APPLY_CURRENCY_QRY);
			//获取币种（string）所对应的币种码
			code=LoanData.bociCurcodeMap.get(currency);
			loan_currency.setText(currency);
		}
		// 申请人姓名
		if (ConstantGloble.APPLY_VISIBILITY.equals(appNameNeed)) {
			loan_apply_client_name_title.setVisibility(View.VISIBLE);
			String loan_apply_names = resMap.get(Loan.LOAN_APPLY_APPNAME_QRY);
			loan_apply_name.setText(loan_apply_names);
		}
		// 性别
		if (ConstantGloble.APPLY_VISIBILITY.equals(appSexNeed)) {
			loan_apply_gender_title.setVisibility(View.VISIBLE);
			String loan_apply_genders = resMap.get(Loan.LOAN_APPLY_APPSEX_QRY);
			loan_apply_gender.setText(loan_apply_genders);
		}
		// 年龄
		if (ConstantGloble.APPLY_VISIBILITY.equals(appAgeNeed)) {
			loan_apply_age_value_title.setVisibility(View.VISIBLE);
			String loan_apply_ages = resMap.get(Loan.LOAN_APPLY_APPAGE_QRY);
			loan_apply_age.setText(loan_apply_ages);
		}
		
		/*是否需要企业名称*/
		if (ConstantGloble.APPLY_VISIBILITY.equals(entNameNeed)) {
			loan_apply_entName_title.setVisibility(View.VISIBLE);
			String entName = resMap.get(Loan.LOAN_APPLY_ENTNAME_QRY);
			loan_apply_entName.setText(entName);
			TextView tv=(TextView) findViewById(R.id.loan_tv_title);
			tv.setText(R.string.loan_apply_select_enterprise_message);
		}
		/*是否需要办公地址*/
		if (ConstantGloble.APPLY_VISIBILITY.equals(officeAddressNeed)) {
			loan_apply_officeAddress_title.setVisibility(View.VISIBLE);
			String officeAddress = resMap.get(Loan.LOAN_APPLY_OFFICEADDRESS_QRY);
			loan_apply_officeAddress.setText(officeAddress);
		}
		/*是否需要主营业务*/
		if (ConstantGloble.APPLY_VISIBILITY.equals(mainBusinessNeed)) {
			loan_apply_mainBusiness_title.setVisibility(View.VISIBLE);
			String mainBusiness = resMap.get(Loan.LOAN_APPLY_MAINBUSINESS_QRY);
			loan_apply_mainBusiness.setText(mainBusiness);
		}
		/*是否需要负责人姓名*/
		if (ConstantGloble.APPLY_VISIBILITY.equals(principalNameNeed)) {
			loan_apply_principalName_title.setVisibility(View.VISIBLE);
			String principalName = resMap.get(Loan.LOAN_APPLY_PRINCIPALNAME_QRY);
			loan_apply_principalName.setText(principalName);
		}
		
		// 联系电话
		if (ConstantGloble.APPLY_VISIBILITY.equals(appPhoneNeed)) {
			loan_apply_iphone_value_title.setVisibility(View.VISIBLE);
			String loan_apply_iphones = resMap.get(Loan.LOAN_APPLY_APPPHONE_QRY);
			loan_apply_iphone.setText(loan_apply_iphones);
		}
		// email 地址
		if (ConstantGloble.APPLY_VISIBILITY.equals(appEmailNeed)) {
			loan_apply_emali_value_title.setVisibility(View.VISIBLE);
			String loan_apply_emails = resMap.get(Loan.LOAN_APPLY_APPEMAIL_QRY);
			loan_apply_email.setText(loan_apply_emails);
		}
		// 房屋交易价 house_price
		if (ConstantGloble.APPLY_VISIBILITY.equals(houseTradePriceNeed)) {
			loan_apply_house_price_title.setVisibility(View.VISIBLE);
			loan_apply_house_price_left=(TextView) findViewById(R.id.loan_apply_house_price_left);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
					loan_apply_house_price_left);
			 house_prices = resMap
					.get(Loan.LOAN_APPLY_HOUSETRADE_PRICE_QRY);
			house_price.setText(StringUtil.parseStringCodePattern(code,
					house_prices, 2));
		}
		// 学费生活费总额 tuitiona_alimony
		if (ConstantGloble.APPLY_VISIBILITY.equals(tuitionTradePriceNeed)) {
		
			loan_apply_tuitiona_alimony_title.setVisibility(View.VISIBLE);
			tuitiona_alimony_left=(TextView) findViewById(R.id.tuitiona_alimony_left);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
					tuitiona_alimony_left);
			
			tuitiona_alimonys = resMap
					.get(Loan.LOAN_APPLY_TUITIONTEADE_PRICE_QRY);
			tuitiona_alimony.setText(StringUtil.parseStringPattern(
				     tuitiona_alimonys, 2));
		}
		// 净车价 car_price
		if (ConstantGloble.APPLY_VISIBILITY.equals(carTradePriceNeed)) {
			loan_apply_car_price_title.setVisibility(View.VISIBLE);
			 car_prices = resMap.get(Loan.LOAN_APPLY_CARTRADE_PRICE_QRY);
			car_price.setText(StringUtil.parseStringCodePattern(code,
					car_prices, 2));
		}
		// 贷款金额 amount
		if (ConstantGloble.APPLY_VISIBILITY.equals(loanAmountNeed)) {
			if("外汇留学贷款".equals(loan_apply_types)){
				TextView loan_amount_left=(TextView) findViewById(R.id.loan_amount_left);
				String loan_amount=getResources().getString(R.string.loan_apply_amount_new);
				loan_amount_left.setText(loan_amount);	
			}
			loan_amount_title.setVisibility(View.VISIBLE);
			 amounts = resMap.get(Loan.LOAN_APPLY_LOANAMOUNT_QRY);
			amount.setText(StringUtil.parseStringCodePattern(code, amounts, 2));
		}
		// 贷款期限 input_loanPeriod
		if (ConstantGloble.APPLY_VISIBILITY.equals(loanTermNeed)) {
			loan_choise_input_loanPeriod_title.setVisibility(View.VISIBLE);
			String input_loanPeriods = resMap.get(Loan.LOAN_APPLY_LOANTERM_QRY);
			input_loanPeriod.setText(input_loanPeriods);
		}
		// 所购住房房龄
		if (ConstantGloble.APPLY_VISIBILITY.equals(houseAgeNeed)) {
			loan_apply_buy_house_age_title.setVisibility(View.VISIBLE);
			buy_house_age_left=(TextView) findViewById(R.id.loan_apply_buy_house_age_left);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this, buy_house_age_left);
			String buy_houses = resMap.get(Loan.LOAN_APPLY_HOUSEAGE_QRY);
			buy_house
					.setText(StringUtil.isNullOrEmpty(buy_houses) ? ConstantGloble.BOCINVT_DATE_ADD
							: buy_houses);
		}
		// 担保方式 guarantee_way
		if (ConstantGloble.APPLY_VISIBILITY.equals(guaWayNeed)) {
			loan_apply_guarantee_way_title.setVisibility(View.VISIBLE);
			String guarantee_ways = resMap.get(Loan.LOAN_APPLY_GUAWAY_QRY);
			guarantee_way.setText(guarantee_ways);
		}
		// 是否能提供抵押担保
		if (ConstantGloble.APPLY_VISIBILITY.equals(guaTypeFlagNeed)) {
			loan_apply_guaTypeFlag_title.setVisibility(View.VISIBLE);
			loan_apply_guaTypeFlag_left = (TextView) findViewById(R.id.loan_apply_guaTypeFlag_left);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
					loan_apply_guaTypeFlag_left);
			String guaTypeFlags=null;
			if("true".equals(bGuarantee)){
				 guaTypeFlags="是";
			}else{
				guaTypeFlags="否";
			}
			guaTypeFlag.setText(guaTypeFlags);
		}
		// 担保类别  guaTypeFlagNeed
		if (ConstantGloble.APPLY_VISIBILITY.equals(guaTypeFlagNeed)) {
			if("true".equals(bGuarantee)){
				loan_apply_guarantee_title.setVisibility(View.VISIBLE);
				String guarantees = resMap.get("guarantee");
				guarantee.setText(guarantees);
			}
		}

	}

	private void init() {
		detailView = LayoutInflater.from(this).inflate(R.layout.loan_apply_confirm, null);
		tabcontentView.addView(detailView);

		select_place_left = (TextView) findViewById(R.id.select_place_left);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				select_place_left);
		loan_apply_name = (TextView) findViewById(R.id.loan_apply_client_name);
		// 所在的城市
		loan_apply_city = (TextView) findViewById(R.id.loan_apply_city);
		// 贷款品种
		loan_apply_type = (TextView) findViewById(R.id.loan_apply_type);
		// 币种
		loan_currency = (TextView) findViewById(R.id.text_loan_currency);
		// 男女
		loan_apply_gender = (TextView) findViewById(R.id.loan_apply_gender);
		/*企业名称*/
		loan_apply_entName = (TextView) findViewById(R.id.loan_apply_entName);
		/*企业地址*/
		loan_apply_officeAddress = (TextView) findViewById(R.id.loan_apply_officeAddress);
		/*主营业务*/
		loan_apply_mainBusiness = (TextView) findViewById(R.id.loan_apply_mainBusiness);
		/*负责人姓名*/
		loan_apply_principalName = (TextView) findViewById(R.id.loan_apply_principalName);
		// 担保方式
		guarantee_way = (TextView) findViewById(R.id.loan_apply_guarantee_way);
		// 担保类别
		guarantee = (TextView) findViewById(R.id.loan_apply_guarantee);
		// 年龄
		loan_apply_age = (TextView) findViewById(R.id.loan_apply_age_value);
		// 是否提供抵押担保
		guaTypeFlag = (TextView) findViewById(R.id.loan_apply_guaTypeFlag);
		
		
		
		// 联系电话
		loan_apply_iphone = (TextView) findViewById(R.id.loan_apply_iphone_value);
		// email
		loan_apply_email = (TextView) findViewById(R.id.loan_apply_emali_value);
		// 房屋交易价
		house_price = (TextView) findViewById(R.id.loan_apply_house_price_value);
		// 学费生活费总额
		tuitiona_alimony = (TextView) findViewById(R.id.loan_apply_tuitiona_alimony_value);
		// 净车价
		car_price = (TextView) findViewById(R.id.loan_apply_car_price_value);
		// 贷款金额
		amount = (TextView) findViewById(R.id.loan_amount_value);
		// 贷款期限
		input_loanPeriod = (TextView) findViewById(R.id.loan_choise_input_loanPeriod_value);
		// 所购住房房龄
		buy_house = (TextView) findViewById(R.id.loan_apply_buy_house_value);
		// 业务办理网点
		business_transaction_place = (TextView) findViewById(R.id.loan_apply_business_transaction_place);
		loan_apply_business_transaction_place_left = (TextView) findViewById(R.id.loan_apply_business_transaction_place_left);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, loan_apply_business_transaction_place_left);
		
		Button loan_tradeButton = (Button) findViewById(R.id.loan_tradeButton);

		loan_tradeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseHttpEngine.showProgressDialog();
				requestPSNGetTokenId(commConversationId);
			}
		});
	}

	// 为控件赋值
	private void setDate() {
		// 所在的城市
		String loan_apply_citys = resMap.get(Loan.LOAN_APPLY_CITYNAME_QRY);
		loan_apply_city.setText(loan_apply_citys);
		// 贷款品种
		loan_apply_types = resMap.get(Loan.LOAN_APPLYPRODUCTNAME_QRY);
		loan_apply_type.setText(loan_apply_types);
	
		// 业务办理地点 business_transaction_place
		String deptName = bankMessageMap.get(Loan.LOAN_APPLY_DEPTNAME_QRY);
		String deptAddr = bankMessageMap.get(Loan.LOAN_APPLY_DEPTADDR_QRY);
//		String deptPhone = bankMessageMap.get(Loan.LOAN_APPLY_DEPTPHONE_QRY);
		business_transaction_place.setText(deptName + "\r" + deptAddr + "\n"
//				+ deptPhone
				);
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		token = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		requestPsnOnLineLoanSubmit(token, commConversationId);
	}

	/** 4.6 006 PsnOnLineLoanSubmit贷款申请提交交易 */
	private void requestPsnOnLineLoanSubmit(String token,
			String commConversationId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_APPLY_PSNONLINELOANSUBMIT_QRY);
		biiRequestBody.setConversationId(commConversationId);
		Map<String, Object> map = new HashMap<String, Object>();
		// 币种
		if (ConstantGloble.APPLY_VISIBILITY.equals(currencyNeed)) {
			loan_apply_spinner_currency_title.setVisibility(View.VISIBLE);
			currency = resMap.get(Loan.LOAN_APPLY_CURRENCY_QRY);
			//获取币种（string）所对应的币种码
			String mCode=LoanData.ApplyCurcodeMap.get(currency);
			loan_currency.setText(currency);
			map.put(Loan.LOAN_APPLY_CURRENCY_QRY, mCode);
		}
		// 房屋交易价 house_price
		if (ConstantGloble.APPLY_VISIBILITY.equals(houseTradePriceNeed)) {
			map.put(Loan.LOAN_APPLY_HOUSETRADE_PRICE_QRY, house_prices);
		}
		// 学费生活费总额 tuitiona_alimony
		if (ConstantGloble.APPLY_VISIBILITY.equals(tuitionTradePriceNeed)) {
			map.put(Loan.LOAN_APPLY_TUITIONTEADE_PRICE_QRY,tuitiona_alimonys);
		}
		// 净车价 car_price
		if (ConstantGloble.APPLY_VISIBILITY.equals(carTradePriceNeed)) {
			map.put(Loan.LOAN_APPLY_CARTRADE_PRICE_QRY,car_prices);
		}
		// 贷款金额 amount
		if (ConstantGloble.APPLY_VISIBILITY.equals(loanAmountNeed)) {
			map.put(Loan.LOAN_APPLY_LOANAMOUNT_QRY,amounts);
		}
		// 贷款期限 input_loanPeriod
		if (ConstantGloble.APPLY_VISIBILITY.equals(loanTermNeed)) {
			String input_loanPeriods = resMap.get(Loan.LOAN_APPLY_LOANTERM_QRY);
			map.put(Loan.LOAN_APPLY_LOANTERM_QRY, input_loanPeriods);
		}
		// 所购住房房龄
		if (ConstantGloble.APPLY_VISIBILITY.equals(houseAgeNeed)) {
			String buy_houses = resMap.get(Loan.LOAN_APPLY_HOUSEAGE_QRY);
			map.put(Loan.LOAN_APPLY_HOUSEAGE_QRY, buy_houses);
		}
		// 担保方式 guarantee_way
		// put("1", "房产抵押");
		// put("2", "有价权利质押");
		// put("3", "其他");
		if (ConstantGloble.APPLY_VISIBILITY.equals(guaWayNeed)) {
			String guarantee_ways = resMap.get(Loan.LOAN_APPLY_GUAWAY_QRY);
			String guaranteeWay = "";
			if ("房产抵押".equals(guarantee_ways)) {
				guaranteeWay = ConstantGloble.APPLY_HOUSE_PROPERTY_PLEDGE;
			} else if ("有价权利质押".equals(guarantee_ways)) {
				guaranteeWay = ConstantGloble.APPLY_HAVE_PLEDGE;
			} else {
				guaranteeWay = ConstantGloble.APPLY_ELSE;
			}
			map.put(Loan.LOAN_APPLY_GUAWAY_QRY, guaranteeWay);
		}
		// 姓名
		if (ConstantGloble.APPLY_VISIBILITY.equals(appNameNeed)) {
			map.put(Loan.LOAN_APPLY_APPNAME_QRY,
					resMap.get(Loan.LOAN_APPLY_APPNAME_QRY));
		}
		/*是否需要企业名称*/
		if (ConstantGloble.APPLY_VISIBILITY.equals(entNameNeed)) {
			String entName = resMap.get(Loan.LOAN_APPLY_ENTNAME_QRY);
			map.put(Loan.LOAN_APPLY_ENTNAME_QRY,entName);
			
		}
		/*是否需要办公地址*/
		if (ConstantGloble.APPLY_VISIBILITY.equals(officeAddressNeed)) {
			String officeAddress = resMap.get(Loan.LOAN_APPLY_OFFICEADDRESS_QRY);
			map.put(Loan.LOAN_APPLY_OFFICEADDRESS_QRY,officeAddress);
		}
		/*是否需要主营业务*/
		if (ConstantGloble.APPLY_VISIBILITY.equals(mainBusinessNeed)) {
			String mainBusiness = resMap.get(Loan.LOAN_APPLY_MAINBUSINESS_QRY);
			map.put(Loan.LOAN_APPLY_MAINBUSINESS_QRY,mainBusiness);
		}
		/*是否需要负责人姓名*/
		if (ConstantGloble.APPLY_VISIBILITY.equals(principalNameNeed)) {
			String principalName = resMap.get(Loan.LOAN_APPLY_PRINCIPALNAME_QRY);
			map.put(Loan.LOAN_APPLY_PRINCIPALNAME_QRY,principalName);
		}
		
		//是否上送提供抵押担保
		if (ConstantGloble.APPLY_VISIBILITY.equals(guaTypeFlagNeed)) {
			//显示
			if("true".equals(bGuarantee)){
				String gt=LoanData.guaranteeKeyList.get(resMap.get("guarantee"));
				map.put(Loan.LOAN_APPLY_GUATYPEFLAG_QRY,gt);
				map.put(Loan.LOAN_APPLY_GUATYPEF_QRY,"1");
			}else{
				map.put(Loan.LOAN_APPLY_GUATYPEF_QRY,"2");
			}
		}
		// 年龄
		if (ConstantGloble.APPLY_VISIBILITY.equals(appAgeNeed)) {
//			int age=Integer.parseInt(resMap.get(Loan.LOAN_APPLY_APPAGE_QRY));
			String age =resMap.get(Loan.LOAN_APPLY_APPAGE_QRY);
			map.put(Loan.LOAN_APPLY_APPAGE_QRY,age);
		}
		// 性别
		if (ConstantGloble.APPLY_VISIBILITY.equals(appSexNeed)) {
			String sex = resMap.get(Loan.LOAN_APPLY_APPSEX_QRY);
			String sexs = "";
			String boy=getResources().getString(R.string.boy);
			if (boy.equals(sex)) {
				sexs = ConstantGloble.APPLY_BOY;
			} else {
				sexs = ConstantGloble.APPLY_GIRL;
			}
			map.put(Loan.LOAN_APPLY_APPSEX_QRY, sexs);
		}
		// 联系电话
		if (ConstantGloble.APPLY_VISIBILITY.equals(appPhoneNeed)) {
			map.put(Loan.LOAN_APPLY_APPPHONE_QRY,
					resMap.get(Loan.LOAN_APPLY_APPPHONE_QRY));
		}
		// Email地址
		if (ConstantGloble.APPLY_VISIBILITY.equals(appEmailNeed)) {
			map.put(Loan.LOAN_APPLY_APPEMAIL_QRY,
					resMap.get(Loan.LOAN_APPLY_APPEMAIL_QRY));
		}
		// 产品编码
		map.put(Loan.LOAN_APPLY_PRODUCTCODE_QRY, productCode);
		// 产品名称
		map.put(Loan.LOAN_APPLYPRODUCTNAME_QRY,
				resMap.get(Loan.LOAN_APPLYPRODUCTNAME_QRY));
		// 网点编号
		map.put(Loan.LOAN_APPLY_DEPTID_QRY,
				bankMessageMap.get(Loan.LOAN_APPLY_DEPTID_QRY));
		//cityCode 
		map.put(Loan.LOAN_APPLY_CITYCODE_QRY, cityCode);
		
		// 防重机制 id
		map.put(Loan.LOAN_TOKEN_REQ, token);

		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnOnLineLoanSubmitCallback");
	}

	/** 4.6 006 PsnOnLineLoanSubmit贷款申请提交交易--- 回调 */
	public void requestPsnOnLineLoanSubmitCallback(Object resultObj) {
		super.requestPsnLoanRateQueryCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		BaseHttpEngine.dissMissProgressDialog();
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		Map<String, String> result = (Map<String, String>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		String applyResult = result.get(Loan.LOAN_APPLY_APPLYRESULT_QRY);
		 if (ConstantGloble.APPLY_VISIBILITY.equals(applyResult)) {
			gotoActivity();
		  } else {
			//跳转到失败界面
			gotofailActivity();
		}
	}

	// 跳转成功页面
	private void gotoActivity() {
		Intent intent = new Intent(LoanApplyConfirmActivity.this,
				LoanApplySuccessActivity.class);
		startActivity(intent);

	}
//	 跳转失败页面fail
		private void gotofailActivity() {
			Intent intent = new Intent(LoanApplyConfirmActivity.this,
					LoanApplyFailActivity.class);
			startActivity(intent);

		}

}
