package com.chinamworld.bocmbci.biz.loan.loanApply;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.bii.constant.Third;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.biz.loan.LoanData;
import com.chinamworld.bocmbci.biz.loan.adapter.LoanApplyadapterselectplace;
import com.chinamworld.bocmbci.biz.loan.inflaterDialogView.Loaninflaterdialog;
import com.chinamworld.bocmbci.biz.login.LoginActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 贷款申请--单笔账户详情页面 */
public class LoanApplyInfoClientMessageActivity extends LoanBaseActivity {
	private static final String TAG = "LoanApplyQueryDetailActivity";
	private View detailView = null;
	private ListView listView;
	private View view = null;
	/** 申请人姓名 */
	private TextView loan_apply_name = null;
	/** 显示地址 */
	private TextView tv_apply_visibility = null;
	/** 地址tetile */
	private TextView select_place_left = null;
	/** 币种 */
	private TextView tvCurrency = null;
	/** 是否能提供抵押担保 */
	private TextView loan_apply_pledge_guarantee_left = null;
	/** 所购住房房龄（年） */
	private TextView loan_apply_buy_house_age_left = null;
	private String mCurrency;
	/** 性别 */
	private RadioGroup loan_apply_gender = null;
	/** 男 */
	private RadioButton rb_boy;
	/** 女 */
	private RadioButton rb_girl;
	// 性别
	private String boyOrGilr = null;
	private Boolean bBoyOrGilr = false;
	private Boolean isBoyOrGilr = false;

	/** 年龄 */
	private EditText loan_apply_age = null;
	private String age;
	private int mAge=0;
	/** 联系电话 */
	private EditText loan_apply_iphone = null;
	private String iphone;
	/** Email */
	private EditText loan_apply_email = null;
	private String emial;
	/** 企业名称 */
	private EditText loan_apply_entName = null;
	private String entName;
	/** 办公地址 */
	private EditText loan_apply_officeAddress = null;
	private String officeAddress;
	/** 主营业务 */
	private EditText loan_apply_mainBusiness = null;
	private String mainBusiness;
	/** 负责人姓名 */
	private EditText loan_apply_principalName = null;
	private String principalName;
	/** 房屋交易价 */
	private EditText house_price = null;
	private String house_prices;
	private TextView loan_apply_house_price_left;
	/** 学费生活费总额 */
	private EditText tuitiona_alimony = null;
	private String tuitiona_alimonys;
	private TextView loan_apply_tuitiona_alimony_left;
	/** 净车价 */
	private EditText car_price = null;
	private String car_prices;
	/** 贷款金额 */
	private EditText amount = null;
	private String amounts;
	/** 贷款期限 */
	private EditText input_loanPeriod = null;
	private String input_loanPeriods;
	private int inputloanPeriod;
	private int  mInputloanPeriod;
	/** 所购住房房龄 */
	private EditText buy_house = null;
	private String buy_houses;
	/** 币种Spinner */
	private Spinner spinner_currency = null;
	/** 担保方式Spinner */
	private Spinner spinner_guarantee_way = null;
	private String guarantee_ways = null;
	/** 担保类别Spinner */
	private Spinner spinner_guarantee = null;
	private String stGuarantee = null;
	private Boolean btGuarantee = false;
	
	/** 存储用户输入的信息 */
	Map<String, String> resMap = null;;
	/** 用户选择的网点信息 */
	Map<String, String> bankMessageMap = null;;
	// 币种 list 集合
	// List<String> currencyList = null;
	// 担保方式 list 集合
	// List<String> guaranteeWayList = null;
	// 担保类别 list 集合
	// List<String> guaranteeList = null;
	/** 用来显示选择地址的bt */
	private Boolean tAndo = false;
	private int positions;
	/** 总条目数 */
	private int totalCount = 0;
	/** listview的更多条目 */
	private View viewFooter;
	private Button btnMore;
	/** 当前请求list数据的index */
	private int mCurrentIndex = 0;
	private String commConversationId;
	// 担保类别
	private RadioGroup pledge_guarantee;
	private RadioButton pledge_guarantee_yes;
	private RadioButton pledge_guarantee_no;
	private Boolean bGuarantee = false;
	// 存储查询后的数据
	List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
	// 每页显示页数
	// private int pageSize = 10;
	// 当前页
	private String sCurrentIndex = null;
	// 是否需要显示房屋交易价
	private String houseTradePriceNeed;
	// 是否需要显示学费生活费
	private String tuitionTradePriceNeed;
	// 是否需要显示净车价
	private String carTradePriceNeed;
	// 是否需要显示贷款金额
	private String loanAmountNeed;
	// 是否需要显示申请人姓名
	private String appNameNeed;
	// 是否需要显示年龄
	private String appAgeNeed;
	// 是否需要显示性别
	private String appSexNeed;
	// 是否需要显示电话
	private String appPhoneNeed;
	// 是否需要显示email 邮箱
	private String appEmailNeed;
	// 是否需要贷款期限
	private String loanTermNeed;
	// 是否需要币种
	private String currencyNeed;
	// 是否需要所购住房房龄
	private String houseAgeNeed;
	// 是否需要担保方式
	private String guaWayNeed;
	// 是否需要是否能提供抵押担保
	private String guaTypeFlagNeed;
	// 是否需要企业名称
	private String entNameNeed;
	// 是否需要办公地址
	private String officeAddressNeed;
	// 是否需要主营业务
	private String mainBusinessNeed;
	// 是否需要负责人姓名
	private String principalNameNeed;

	/** 列表数据的adapter */
	private LoanApplyadapterselectplace adapter;
	/** 选择网点，修改网点 */
	private Button select_place, select_edit;
	LinearLayout loan_apply_entName_title, loan_apply_officeAddress_title,
			loan_apply_mainBusiness_title, loan_apply_principalName_title,
			loan_apply_name_title, loan_apply_age_title,
			loan_apply_email_title, loan_apply_boyorgirl_title,
			loan_apply_iphone_title, loan_apply_house_price_title,
			loan_apply_tuitiona_alimony_title, loan_apply_car_price_title,
			loan_amount_title, loan_choise_input_loanPeriod_title,
			loan_apply_buy_house_age_title, loan_apply_guarantee_way_title,
			loan_apply_pledge_guarantee_title, loan_apply_guarantee_title,
			loan_apply_spinner_currency_title;

	/** PsnOnLineLoanDetailQry 接口返回的结果集合 */
	List<Map<String, String>> loanApplyList = null;
	Map<String, Object> LoanFieldMap = null;
	private String cityCode, city, productCode, type;
	// 记录位置
	private int mLoanApplyMessageDialogSelectPosition = -1;
	private boolean needShow;
	private static Loaninflaterdialog accmessagedialog;

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

		// PsnOnLineLoanFieldQry 接口返回的数据
		LoanFieldMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.LOAN_RESULT);
		city = getIntent().getStringExtra(ConstantGloble.APPLY_CITY);
		type = getIntent().getStringExtra(ConstantGloble.APPLY_TYPE);
		cityCode = getIntent().getStringExtra(ConstantGloble.APPLY_CITYCODE);
		productCode = getIntent().getStringExtra(ConstantGloble.APPLY_TYPECODE);
		// currencyList = new ArrayList<String>();
		// guaranteeWayList = new ArrayList<String>();
		// guaranteeList = new ArrayList<String>();
		resMap = new HashMap<String, String>();
		bankMessageMap = new HashMap<String, String>();
		init();
		getVisibilityDate();
		setSpinnerDate();
	}

	private void setSpinnerDate() {
		// currencyList.add("请选择");
		// currencyList.add("美元");
		// currencyList.add("日元");
		// currencyList.add("欧元");
		// currencyList.add("港币");
		// currencyList.add("英镑");
		// currencyList.add("澳元");
		// currencyList.add("加元");
		setSpinnerDateAdapter(spinner_currency, LoanData.currencyList);

		// guaranteeWayList.add("请选择");
		// guaranteeWayList.add("房产抵押");
		// guaranteeWayList.add("有价权利质押");
		// guaranteeWayList.add("其他");
		setSpinnerDateAdapter(spinner_guarantee_way, LoanData.guaranteeWayList);

		// guaranteeList.add("请选择");
		// guaranteeList.add("住房");
		// guaranteeList.add("商铺");
		// guaranteeList.add("土地");
		// guaranteeList.add("其他固定资产");

	}

	// 为Spinner 赋值
	private void setSpinnerDateAdapter(Spinner mSpinner, List<String> mList) {

		ArrayAdapter<String> currencyAdapter = new ArrayAdapter<String>(this,
				R.layout.dept_spinner, mList);
		currencyAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(currencyAdapter);
		mSpinner.setSelection(0);

	}

	/** 获得数据 */
	private void getVisibilityDate() {
		appNameNeed = (String) LoanFieldMap
				.get(Loan.LOAN_APPLY_APPNAMENEED_QRY);
		appAgeNeed = (String) LoanFieldMap.get(Loan.LOAN_APPLY_APPAGENEED_QRY);
		appSexNeed = (String) LoanFieldMap.get(Loan.LOAN_APPLY_APPSEXNEED_QRY);
		appPhoneNeed = (String) LoanFieldMap
				.get(Loan.LOAN_APPYL_APPPHONENEED_QRY);
		appEmailNeed = (String) LoanFieldMap
				.get(Loan.LOAN_APPLY_APPEMAILNEED_QRY);
		houseTradePriceNeed = (String) LoanFieldMap
				.get(Loan.LOAN_APPLY_HOUSETRADEPRICENEED_QRY);
		tuitionTradePriceNeed = (String) LoanFieldMap
				.get(Loan.LOAN_APPLY_TUITIONTRADEPRICEENEED_QRY);
		carTradePriceNeed = (String) LoanFieldMap
				.get(Loan.LOAN_APPLY_CARTRADEPRICEENEED_QRY);
		loanAmountNeed = (String) LoanFieldMap
				.get(Loan.LOAN_APPLY_LOANAMOUNTNEED_QRY);
		loanTermNeed = (String) LoanFieldMap
				.get(Loan.LOAN_APPLY_LOANTERMNEED_QRY);
		currencyNeed = (String) LoanFieldMap
				.get(Loan.LOAN_APPLY_CURRENCYNEED_QRY);
		houseAgeNeed = (String) LoanFieldMap
				.get(Loan.LOAN_APPLY_HOUSEAGENEED_QRY);
		guaWayNeed = (String) LoanFieldMap.get(Loan.LOAN_APPLY_GUAWAYNEED_QRY);
		guaTypeFlagNeed = (String) LoanFieldMap
				.get(Loan.LOAN_APPLY_GUATYPEFLAGNEED_QRY);

		entNameNeed = (String) LoanFieldMap
				.get(Loan.LOAN_APPLY_ENTNAMENEED_QRY);
		officeAddressNeed = (String) LoanFieldMap
				.get(Loan.LOAN_APPLY_OFFICEADDRESSNEED_QRY);
		mainBusinessNeed = (String) LoanFieldMap
				.get(Loan.LOAN_APPLY_MAINBUSINESSNEED_QRY);
		principalNameNeed = (String) LoanFieldMap
				.get(Loan.LOAN_APPLY_PRINCIPALNAMENEED_QRY);

		loan_apply_entName_title = (LinearLayout) findViewById(R.id.loan_apply_entName_title);
		loan_apply_officeAddress_title = (LinearLayout) findViewById(R.id.loan_apply_officeAddress_title);
		loan_apply_mainBusiness_title = (LinearLayout) findViewById(R.id.loan_apply_mainBusiness_title);
		loan_apply_principalName_title = (LinearLayout) findViewById(R.id.loan_apply_principalName_title);

		loan_apply_name_title = (LinearLayout) findViewById(R.id.loan_apply_name_title);
		loan_apply_boyorgirl_title = (LinearLayout) findViewById(R.id.loan_apply_boyorgirl_title);
		loan_apply_age_title = (LinearLayout) findViewById(R.id.loan_apply_age_title);
		loan_apply_iphone_title = (LinearLayout) findViewById(R.id.loan_apply_iphone_title);
		loan_apply_email_title = (LinearLayout) findViewById(R.id.loan_apply_email_title);
		loan_apply_house_price_title = (LinearLayout) findViewById(R.id.loan_apply_house_price_title);
		loan_apply_tuitiona_alimony_title = (LinearLayout) findViewById(R.id.loan_apply_tuitiona_alimony_title);
		loan_apply_car_price_title = (LinearLayout) findViewById(R.id.loan_apply_car_price_title);
		loan_amount_title = (LinearLayout) findViewById(R.id.loan_amount_title);
		loan_choise_input_loanPeriod_title = (LinearLayout) findViewById(R.id.loan_choise_input_loanPeriod_title);
		loan_apply_buy_house_age_title = (LinearLayout) findViewById(R.id.loan_apply_buy_house_age_title);
		loan_apply_guarantee_way_title = (LinearLayout) findViewById(R.id.loan_apply_guarantee_way_title);
		loan_apply_pledge_guarantee_title = (LinearLayout) findViewById(R.id.loan_apply_pledge_guarantee_title);

		loan_apply_guarantee_title = (LinearLayout) findViewById(R.id.loan_apply_guarantee_title);
		loan_apply_spinner_currency_title = (LinearLayout) findViewById(R.id.loan_apply_spinner_currency_title);
		// 企业姓名
		if (ConstantGloble.APPLY_VISIBILITY.equals(entNameNeed)) {
			loan_apply_entName_title.setVisibility(View.VISIBLE);
			TextView tv = (TextView) findViewById(R.id.message_title);
			tv.setText(R.string.loan_apply_write_enterprise_message);
		}
		// 企业办公地址
		if (ConstantGloble.APPLY_VISIBILITY.equals(officeAddressNeed)) {
			loan_apply_officeAddress_title.setVisibility(View.VISIBLE);
		}
		// 主营业务
		if (ConstantGloble.APPLY_VISIBILITY.equals(mainBusinessNeed)) {
			loan_apply_mainBusiness_title.setVisibility(View.VISIBLE);
		}
		// 负责人姓名
		if (ConstantGloble.APPLY_VISIBILITY.equals(principalNameNeed)) {
			loan_apply_principalName_title.setVisibility(View.VISIBLE);
		}
		// 姓名
		if (ConstantGloble.APPLY_VISIBILITY.equals(appNameNeed)) {
			loan_apply_name_title.setVisibility(View.VISIBLE);
			loan_apply_name.setText(LoginActivity.loginNames);

		}
		// 年龄
		if (ConstantGloble.APPLY_VISIBILITY.equals(appAgeNeed)) {
			loan_apply_age_title.setVisibility(View.VISIBLE);
		}
		// 性别
		if (ConstantGloble.APPLY_VISIBILITY.equals(appSexNeed)) {
			loan_apply_boyorgirl_title.setVisibility(View.VISIBLE);
		}
		// 电话
		if (ConstantGloble.APPLY_VISIBILITY.equals(appPhoneNeed)) {
			loan_apply_iphone_title.setVisibility(View.VISIBLE);
		}
		// email
		if (ConstantGloble.APPLY_VISIBILITY.equals(appEmailNeed)) {
			loan_apply_email_title.setVisibility(View.VISIBLE);
		}
		// 房屋交易价
		if (ConstantGloble.APPLY_VISIBILITY.equals(houseTradePriceNeed)) {
			loan_apply_house_price_title.setVisibility(View.VISIBLE);
			loan_apply_house_price_left=(TextView) findViewById(R.id.loan_apply_house_price_left);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
					loan_apply_house_price_left);
		}
		// 学费生活费
		if (ConstantGloble.APPLY_VISIBILITY.equals(tuitionTradePriceNeed)) {
			loan_apply_tuitiona_alimony_title.setVisibility(View.VISIBLE);
			//学费生活费 title
			loan_apply_tuitiona_alimony_left=(TextView) findViewById(R.id.loan_apply_tuitiona_alimony_left);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
					loan_apply_tuitiona_alimony_left);
		}
		// 净车价
		if (ConstantGloble.APPLY_VISIBILITY.equals(carTradePriceNeed)) {
			loan_apply_car_price_title.setVisibility(View.VISIBLE);
		}
		// 贷款金额
		if (ConstantGloble.APPLY_VISIBILITY.equals(loanAmountNeed)) {
			loan_amount_title.setVisibility(View.VISIBLE);
		}
		// 贷款期限
		if (ConstantGloble.APPLY_VISIBILITY.equals(loanTermNeed)) {
			loan_choise_input_loanPeriod_title.setVisibility(View.VISIBLE);

		}
		// 所购住房房龄
		if (ConstantGloble.APPLY_VISIBILITY.equals(houseAgeNeed)) {
			loan_apply_buy_house_age_title.setVisibility(View.VISIBLE);
			loan_apply_buy_house_age_left = (TextView) findViewById(R.id.loan_apply_buy_house_age_left);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
					loan_apply_buy_house_age_left);
		}

		// 担保方式
		if (ConstantGloble.APPLY_VISIBILITY.equals(guaWayNeed)) {
			loan_apply_guarantee_way_title.setVisibility(View.VISIBLE);
		}
		// 是否能提供抵押担保
		if (ConstantGloble.APPLY_VISIBILITY.equals(guaTypeFlagNeed)) {
			loan_apply_pledge_guarantee_title.setVisibility(View.VISIBLE);
			loan_apply_pledge_guarantee_left = (TextView) findViewById(R.id.loan_apply_pledge_guarantee_left);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
					loan_apply_pledge_guarantee_left);

		}
		// 币种
		if (ConstantGloble.APPLY_VISIBILITY.equals(currencyNeed)) {
			loan_apply_spinner_currency_title.setVisibility(View.VISIBLE);
		}
		/*** 判断显示地址的按钮 */
		if (tAndo == false) {
			select_place.setVisibility(View.VISIBLE);
			tv_apply_visibility.setVisibility(View.GONE);
			select_edit.setVisibility(View.GONE);
		} else {
			select_place.setVisibility(View.GONE);
			tv_apply_visibility.setVisibility(View.VISIBLE);
			select_edit.setVisibility(View.VISIBLE);
		}
	}

	private void init() {
		detailView = LayoutInflater.from(this).inflate(
				R.layout.loan_apply_info_client_message, null);
		tabcontentView.addView(detailView);

		view = mInflater.inflate(
				R.layout.loan_apply_select_place_messagedialog_item, null);
		listView = (ListView) view
				.findViewById(R.id.loan_select_place_listview);
		
		
		select_place_left = (TextView) findViewById(R.id.select_place_left);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				select_place_left);
		loan_apply_name = (TextView) findViewById(R.id.loan_apply_client_name);
		//
		tv_apply_visibility = (TextView) findViewById(R.id.tv_apply_visibility);
		// 币种
		tvCurrency = (TextView) findViewById(R.id.text_spinner_loantype_currency);
		spinner_currency = (Spinner) findViewById(R.id.loan_apply_spinner_currency);
		spinner_currency.setOnItemSelectedListener(itemSelectedListener);
		// 男女
		loan_apply_gender = (RadioGroup) findViewById(R.id.boy_or_girl);
		rb_boy = (RadioButton) findViewById(R.id.boy);
		rb_girl = (RadioButton) findViewById(R.id.girl);
		// if(ConstantGloble.APPLY_VISIBILITY.equals(LoginActivity.gender)){
		// rb_boy.setChecked(true);
		// rb_girl.setChecked(false);
		// boyOrGilr=getResources().getString(R.string.boy);
		// }else{
		// rb_boy.setChecked(false);
		// rb_girl.setChecked(true);
		// boyOrGilr=getResources().getString(R.string.girl);
		// }
		loan_apply_gender.setOnCheckedChangeListener(boyOrGirlListener);
		// 担保方式
		spinner_guarantee_way = (Spinner) findViewById(R.id.loan_apply_spinner_guarantee_way);
		spinner_guarantee_way
				.setOnItemSelectedListener(itemGuaranteeWayListener);
		// 担保类别
		pledge_guarantee = (RadioGroup) findViewById(R.id.pledge_guarantee);
		pledge_guarantee_yes = (RadioButton) findViewById(R.id.yes);
		pledge_guarantee_no = (RadioButton) findViewById(R.id.no);
		pledge_guarantee.setOnCheckedChangeListener(guaranteeWayListener);
		spinner_guarantee = (Spinner) findViewById(R.id.loan_apply_spinner_guarantee);
		spinner_guarantee.setOnItemSelectedListener(itemGuaranteeListener);
		// 年龄
		loan_apply_age = (EditText) findViewById(R.id.et_loan_apply_age_value);
		// 联系电话
		loan_apply_iphone = (EditText) findViewById(R.id.loan_apply_iphone_value);
		loan_apply_iphone.setText(LoginActivity.mobile);
		// email
		loan_apply_email = (EditText) findViewById(R.id.loan_apply_emali_value);
		// 企业名称
		loan_apply_entName = (EditText) findViewById(R.id.loan_apply_entName);
		//对企业名称 的输入进行限制
		EditTextUtils.setLengthMatcher(this,loan_apply_entName, 60);
		// 企业地址
		loan_apply_officeAddress = (EditText) findViewById(R.id.loan_apply_officeAddress);
		//对企业地址的输入进行限制，一个中文占2个字节
		EditTextUtils.setLengthMatcher(this,loan_apply_officeAddress, 100);
		// 主营项目
		loan_apply_mainBusiness = (EditText) findViewById(R.id.loan_apply_mainBusiness);
		//对主营项目的输入进行限制，一个中文占2个字节
		EditTextUtils.setLengthMatcher(this,loan_apply_mainBusiness, 500);
		// 企业负责人姓名
		loan_apply_principalName = (EditText) findViewById(R.id.loan_apply_principalName);
		//对企业负责人姓名的输入进行限制，一个中文占2个字节
		EditTextUtils.setLengthMatcher(this,loan_apply_principalName, 30);
		// 房屋交易价
		house_price = (EditText) findViewById(R.id.loan_apply_house_price_value);
		// 学费生活费总额
		tuitiona_alimony = (EditText) findViewById(R.id.loan_apply_tuitiona_alimony_value);
		// 净车价
		car_price = (EditText) findViewById(R.id.loan_apply_car_price_value);
		/**判断贷款类型，如果是外汇留学贷款，则贷款金额（元） 显示为 贷款金额 */
//		String loan_apply_types = resMap
//				.get(Loan.LOAN_APPLYPRODUCTNAME_QRY);
		if ("外汇留学贷款".equals(type)) {

			TextView loan_amount_left = (TextView) findViewById(R.id.loan_amount_left);
			String loan_amount = getResources().getString(
					R.string.loan_apply_amount_new);
			loan_amount_left.setText(loan_amount);
		}
		// 贷款金额
		amount = (EditText) findViewById(R.id.et_loan_amount_value);
		// 贷款期限
		input_loanPeriod = (EditText) findViewById(R.id.et_loan_choise_input_loanPeriod_value);
		// 所购住房房龄
		buy_house = (EditText) findViewById(R.id.et_loan_apply_buy_house_value);

		// 选择网点地点的button
		select_place = (Button) findViewById(R.id.loan_apply_select_place);
		select_edit = (Button) findViewById(R.id.loan_apply_select_edit);
		select_place.setOnClickListener(selectAndEnitClick);
		select_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showLoanMessageDialog(totalCount);
			}
		});

		Button loan_tradeButton = (Button) findViewById(R.id.loan_tradeButton);

		loan_tradeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gotoActivity();
			}
		});
	}

	/** 存储用户输入信息 */
	private void setInputCustomerMessage() {
		// 企业姓名
		if (ConstantGloble.APPLY_VISIBILITY.equals(entNameNeed)) {
			entName = loan_apply_entName.getText().toString();
			resMap.put(Loan.LOAN_APPLY_ENTNAME_QRY, entName);
		}
		// 企业办公地址
		if (ConstantGloble.APPLY_VISIBILITY.equals(officeAddressNeed)) {
			officeAddress = loan_apply_officeAddress.getText().toString();
			resMap.put(Loan.LOAN_APPLY_OFFICEADDRESS_QRY, officeAddress);
		}
		// 主营业务
		if (ConstantGloble.APPLY_VISIBILITY.equals(mainBusinessNeed)) {
			mainBusiness = loan_apply_mainBusiness.getText().toString();
			resMap.put(Loan.LOAN_APPLY_MAINBUSINESS_QRY, mainBusiness);
		}
		// 负责人姓名
		if (ConstantGloble.APPLY_VISIBILITY.equals(principalNameNeed)) {
			principalName = loan_apply_principalName.getText().toString();
			resMap.put(Loan.LOAN_APPLY_PRINCIPALNAME_QRY, principalName);
		}
		// 姓名
		if (ConstantGloble.APPLY_VISIBILITY.equals(appNameNeed)) {
			resMap.put(Loan.LOAN_APPLY_APPNAME_QRY, LoginActivity.loginNames);
		}
		// 年龄
		if (ConstantGloble.APPLY_VISIBILITY.equals(appAgeNeed)) {
			age = loan_apply_age.getText().toString();
			resMap.put(Loan.LOAN_APPLY_APPAGE_QRY, age);
		}
		// 性别
		if (ConstantGloble.APPLY_VISIBILITY.equals(appSexNeed)) {
			resMap.put(Loan.LOAN_APPLY_APPSEX_QRY, boyOrGilr);
		}
		// 电话
		if (ConstantGloble.APPLY_VISIBILITY.equals(appPhoneNeed)) {
			iphone = loan_apply_iphone.getText().toString();
			resMap.put(Loan.LOAN_APPLY_APPPHONE_QRY, iphone);
		}
		// email
		if (ConstantGloble.APPLY_VISIBILITY.equals(appEmailNeed)) {
			emial = loan_apply_email.getText().toString();
			resMap.put(Loan.LOAN_APPLY_APPEMAIL_QRY, emial);
		}
		// 房屋交易价
		if (ConstantGloble.APPLY_VISIBILITY.equals(houseTradePriceNeed)) {
			house_prices = house_price.getText().toString();
			resMap.put(Loan.LOAN_APPLY_HOUSETRADE_PRICE_QRY, house_prices);
		}
		// 学费生活费
		if (ConstantGloble.APPLY_VISIBILITY.equals(tuitionTradePriceNeed)) {
			tuitiona_alimonys = tuitiona_alimony.getText().toString();
			resMap.put(Loan.LOAN_APPLY_TUITIONTEADE_PRICE_QRY,
					tuitiona_alimonys);
		}
		// 净车价
		if (ConstantGloble.APPLY_VISIBILITY.equals(carTradePriceNeed)) {
			car_prices = car_price.getText().toString();
			resMap.put(Loan.LOAN_APPLY_CARTRADE_PRICE_QRY, car_prices);
		}
		// 贷款金额
		if (ConstantGloble.APPLY_VISIBILITY.equals(loanAmountNeed)) {
			amounts = amount.getText().toString();
			resMap.put(Loan.LOAN_APPLY_LOANAMOUNT_QRY, amounts);
		}
		// 贷款期限
		if (ConstantGloble.APPLY_VISIBILITY.equals(loanTermNeed)) {
			input_loanPeriods = input_loanPeriod.getText().toString();
			resMap.put(Loan.LOAN_APPLY_LOANTERM_QRY, input_loanPeriods);
		}
		// 房龄
		if (ConstantGloble.APPLY_VISIBILITY.equals(houseAgeNeed)) {
			buy_houses = buy_house.getText().toString();
			resMap.put(Loan.LOAN_APPLY_HOUSEAGE_QRY, buy_houses);
		}

		// 担保方式
		if (ConstantGloble.APPLY_VISIBILITY.equals(guaWayNeed)) {
			resMap.put(Loan.LOAN_APPLY_GUAWAY_QRY, guarantee_ways);
		}
		// 担保类别
		if (ConstantGloble.APPLY_VISIBILITY.equals(guaTypeFlagNeed)
				&& bGuarantee == true) {
			resMap.put("guarantee", stGuarantee);
		}
		// 币种
		if (ConstantGloble.APPLY_VISIBILITY.equals(currencyNeed)) {
			String currencys = mCurrency;
			resMap.put(Loan.LOAN_APPLY_CURRENCY_QRY, currencys);
		}
		resMap.put(Loan.LOAN_APPLYPRODUCTNAME_QRY, type);
		resMap.put(Loan.LOAN_APPLY_CITYNAME_QRY, city);
	}

	/**
	 * 请求conversationId 出来登录之外的conversationId
	 */
	public void requestCommConversationId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.CONVERSATION_ID_API);
		HttpManager.requestBii(biiRequestBody, this,
				"requestCommConversationIdCallBack");
	}

	/**
	 * 请求CommconversationId返回
	 * 
	 * @param resultObj
	 */
	public void requestCommConversationIdCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		commConversationId = (String) biiResponseBody.getResult();
		requestPsnOnLineLoanBranchQry(cityCode, productCode, true,
				commConversationId);
	}

	/**
	 * 4.5 005 PsnOnLineLoanBranchQry查询网点列表
	 * 
	 * @param cityCode
	 *            城市编号 productCode 产品编号
	 */
	private void requestPsnOnLineLoanBranchQry(String cityCode,
			String productCode, boolean isRefresh, String commConversationId) {
		if (isRefresh) {
			mCurrentIndex = 0;
		} else {
			mCurrentIndex += Third.PAGESIZE;
		}
		needShow = isRefresh;
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		BaseHttpEngine.showProgressDialog();
		biiRequestBody.setMethod(Loan.LOAN_APPLY_PSNONLINELOANBRANCH_QRY);
		biiRequestBody.setConversationId(commConversationId);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Loan.LOAN_APPLY_CITYCODE_QRY, cityCode);
		map.put(Loan.LOAN_APPLY_PRODUCTCODE_QRY, productCode);
		map.put(PubConstants.PUB_QUERY_FEILD_PAGE_SIZE,
				String.valueOf(Third.PAGESIZE));
		map.put(PubConstants.PUB_QUERY_FEILD_CURRENT_INDEX,
				EpayUtil.getString(mCurrentIndex, "0"));
		map.put(PubConstants.PUB_QUERY_FEILD_IS_REFRESH,
				EpayUtil.getString(isRefresh, "true"));
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnOnLineLoanBranchQryCallback");
	}

	public void requestPsnOnLineLoanBranchQryCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		String recordNumber = (String) resultMap
				.get(Loan.LOAN_APPLY_RECORDNUMBER_QRY);
		if (!StringUtil.isNullOrEmpty(recordNumber)) {
			totalCount = Integer.parseInt(recordNumber);
		}
		List<Map<String, Object>> loanBranchResult = new ArrayList<Map<String, Object>>();
		loanBranchResult = (List<Map<String, Object>>) resultMap.get("list");
		if (StringUtil.isNullOrEmpty(loanBranchResult)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.acc_transferquery_null));
			lists.clear();
			return;
		}

		for (int i = 0; i < loanBranchResult.size(); i++) {
			lists.add((Map<String, Object>) loanBranchResult.get(i));
		}
		BaseHttpEngine.dissMissProgressDialog();
		showLoanMessageDialog(totalCount);
		/** 构建一个adapter 用于显示所有的网点信息 */
	}

	/**
	 * 显示dialog窗口
	 * 
	 * @param totalCount
	 */
	private void showLoanMessageDialog(int totalCount) {
		if (accmessagedialog == null) {
			accmessagedialog = new Loaninflaterdialog(
					LoanApplyInfoClientMessageActivity.this);
		}
		View accmessageView = accmessagedialog.initLoanApplyMessageDialogView(
				LoanApplyInfoClientMessageActivity.this, lists, totalCount,
				mLoanApplyMessageDialogSelectPosition, itemListener,
				exitDetailClick, listener);
		BaseDroidApp.getInstanse().showLoanApplySelectDialog(accmessageView);

	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// mCurrentIndex++;
			// sCurrentIndex=String.valueOf(mCurrentIndex);
			BaseHttpEngine.showProgressDialog();
			requestPsnOnLineLoanBranchQry(cityCode, productCode, false,
					commConversationId);
		}
	};

	OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> paramAdapterView,
				View paramView, int paramInt, long paramLong) {
			// TODO Auto-generated method stub
			tAndo = true;
			if (tAndo == false) {
				select_place.setVisibility(View.VISIBLE);
				tv_apply_visibility.setVisibility(View.GONE);
				select_edit.setVisibility(View.GONE);
			} else {
				bankMessageMap.clear();
				mLoanApplyMessageDialogSelectPosition = paramInt;
				BaseDroidApp.getInstanse().dismissMessageDialog();
				select_place.setVisibility(View.GONE);
				tv_apply_visibility.setVisibility(View.VISIBLE);
				String sr = (String) lists.get(paramInt).get(
						Loan.LOAN_APPLY_DEPTNAME_QRY);
				String sr1 = (String) lists.get(paramInt).get(
						Loan.LOAN_APPLY_DEPTADDR_QRY);
				// String sr2 = (String) lists.get(paramInt).get(
				// Loan.LOAN_APPLY_DEPTPHONE_QRY);
				// 获得被选择的网点编号
				String deptID = (String) lists.get(paramInt).get(
						Loan.LOAN_APPLY_DEPTID_QRY);

				tv_apply_visibility.setText(sr + "\n" + sr1);
				select_edit.setVisibility(View.VISIBLE);
				bankMessageMap.put(Loan.LOAN_APPLY_DEPTNAME_QRY, sr);
				bankMessageMap.put(Loan.LOAN_APPLY_DEPTADDR_QRY, sr1);
				// bankMessageMap.put(Loan.LOAN_APPLY_DEPTPHONE_QRY, sr2);
				bankMessageMap.put(Loan.LOAN_APPLY_DEPTID_QRY, deptID);
			}
		}
	};
	/** 选择性别监听 */
	OnCheckedChangeListener boyOrGirlListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {

			switch (checkedId) {
			case R.id.boy:
				bBoyOrGilr = true;
				isBoyOrGilr=true;
				boyOrGilr = rb_boy.getText().toString().trim();
				break;
			case R.id.girl:
				bBoyOrGilr = true;
				isBoyOrGilr=false;
				boyOrGilr = rb_girl.getText().toString().trim();
				break;
			default:
				break;
			}
		}
	};
	// 选择币种
	private OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> adapter, View view,
				int position, long id) {
			if (position == 0) {
				mCurrency = "";
			} else {
				mCurrency = LoanData.currencyList.get(position);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> adapter) {

		}
	};
	// 担保方式 监听
	private OnItemSelectedListener itemGuaranteeWayListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> adapter, View view,
				int position, long id) {
			if (position == 0) {
				guarantee_ways = "";
			} else {
				guarantee_ways = LoanData.guaranteeWayList.get(position);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> adapter) {

		}
	};
	// 担保类别 监听
	private OnItemSelectedListener itemGuaranteeListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> adapter, View view,
				int position, long id) {
			if (position == 0) {
				stGuarantee = "";
			} else {
				stGuarantee = LoanData.guaranteeList.get(position);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> adapter) {

		}
	};
	/** 选择担类别式监听器 */
	OnCheckedChangeListener guaranteeWayListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {

			bGuarantee = true;
			switch (checkedId) {
			case R.id.yes:
				btGuarantee=true;
				loan_apply_guarantee_title.setVisibility(View.VISIBLE);
				setSpinnerDateAdapter(spinner_guarantee, LoanData.guaranteeList);
				break;
			case R.id.no:
				btGuarantee=false;
				loan_apply_guarantee_title.setVisibility(View.GONE);
				break;

			default:
				break;
			}

		}
	};

	/** 查询网点监听 */
	OnClickListener selectAndEnitClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			lists.clear();

			requestCommConversationId();
		}
	};
	/** 退出项情况监听事件 */
	OnClickListener exitDetailClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
		}
	};

	/**
	 * 刷新列表
	 */
	private void refreshListView(List<Map<String, Object>> listData) {
		if (adapter == null) {
			adapter = new LoanApplyadapterselectplace(this, listData);
			listView.setAdapter(adapter);
		} else {
			adapter.setListData(listData);
			adapter.notifyDataSetChanged();
		}
	}

	private void gotoActivity() {
		setInputCustomerMessage();
		// 性别验证
		if (ConstantGloble.APPLY_VISIBILITY.equals(appSexNeed)) {
			if (bBoyOrGilr == false) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getResources().getString(
								R.string.loan_apply_appSex_error));
				return;
			}
		}
		// 年龄的验证
		if (ConstantGloble.APPLY_VISIBILITY.equals(appAgeNeed)) {
			if (StringUtil.isNullOrEmpty(age)) {
				BaseDroidApp.getInstanse()
						.showInfoMessageDialog(
								getResources().getString(
										R.string.loan_apply_age_error));
				return;
			}
			 mAge = Integer.parseInt(age);
			if ( mAge < 18 || mAge > 65) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getResources().getString(
								R.string.loan_apply_age_error_one));
				return;
			}
		}
		// 企业姓名
		if (ConstantGloble.APPLY_VISIBILITY.equals(entNameNeed)) {
			if (StringUtil.isNull(entName)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getResources().getString(
								R.string.loan_apply_entName_error));
				return;
			}
			// 校验名称是否包含拼音和阿拉伯数字
//			String mge = getResources().getString(
//					R.string.loan_apply_entname_new);
//			if (!setNameRegexpBean(entName, mge)) {
//				return;
//			}
//			;
		}
		// 企业办公地址
		if (ConstantGloble.APPLY_VISIBILITY.equals(officeAddressNeed)) {
			if (StringUtil.isNull(officeAddress)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getResources().getString(
								R.string.loan_apply_officeAddress_error));
				return;
			}
		}
		// 主营业务
		if (ConstantGloble.APPLY_VISIBILITY.equals(mainBusinessNeed)) {
			if (StringUtil.isNull(mainBusiness)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getResources().getString(
								R.string.loan_apply_mainBusiness_error));
				return;
			}
		}
		// 负责人姓名
		if (ConstantGloble.APPLY_VISIBILITY.equals(principalNameNeed)) {
			if (StringUtil.isNull(principalName)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getResources().getString(
								R.string.loan_apply_principalName_error));
				return;
			}
		}
		// 手机号码正则校验
		if (ConstantGloble.APPLY_VISIBILITY.equals(appPhoneNeed)) {
			if (!StringUtil.isNullOrEmpty(iphone)) {
				
			if(iphone.matches(".*\\d+.*")){
				
			}else{
				BaseDroidApp.getInstanse().showInfoMessageDialog("输入正确的联系电话。");
				return;
				
			}
//				 RegexpBean regmobileNo = null;
//				 regmobileNo = new RegexpBean("", iphone, "loanapplyphonenew");
//				 ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
//				 lists.add(regmobileNo);
//				 if (!RegexpUtils.regexpDate(lists)) {// 校验不通过
//				 return;
//				 }
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getResources().getString(
								R.string.loan_apply_iphone_error));
				return;
			}
		}
		// 邮箱正则校验
		if (ConstantGloble.APPLY_VISIBILITY.equals(appEmailNeed)) {

			if (!StringUtil.isNullOrEmpty(emial)) {
				RegexpBean regEmail = null;
				regEmail = new RegexpBean("", emial, "loanapplyemail");
				ArrayList<RegexpBean> list = new ArrayList<RegexpBean>();
				list.add(regEmail);
				if (!RegexpUtils.regexpDate(list)) {// 校验不通过
					return;
				}
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getResources().getString(
								R.string.loan_apply_email_error));
				return;
			}
		}

		String currencyCode = "";
		currencyCode = LoanData.bociCurcodeMap.get(mCurrency);
		// if ("日元".equals(mCurrency)) {
		// currencyCode = ConstantGloble.PRMS_CODE_YEN1;
		// }
		// 房屋交易价
		if (ConstantGloble.APPLY_VISIBILITY.equals(houseTradePriceNeed)) {
			if (StringUtil.isNull(house_prices)) {
				String mssage = getResources().getString(
						R.string.loan_apply_house_prices_error);
				BaseDroidApp.getInstanse().showInfoMessageDialog(mssage);
				return;
			}
			// else{
			// double housePrices = Double.parseDouble(house_prices);
			// if(0 == housePrices){
			// String mssage = getResources().getString(
			// R.string.loan_apply_house_prices_error);
			// BaseDroidApp.getInstanse().showInfoMessageDialog("输入不能等于0");
			// return;
			// }
			// }
			String massage = "";
			// getResources().getString(R.string.loan_apply_house_price_string);
			if (!setRegexpBean(currencyCode, house_prices, massage)) {
				return;
			}
			;
		}
		// PATINDEX('%[A-Za-z]%', 'ads23432')=0;
		// PATINDEX('%[0-9]%', ‘234sdf')=0;
		// 学费生活费
		if (ConstantGloble.APPLY_VISIBILITY.equals(tuitionTradePriceNeed)) {
			if (StringUtil.isNull(tuitiona_alimonys)) {

				String mssage = getResources().getString(
						R.string.loan_apply_tuitiona_alimonys_error);
				BaseDroidApp.getInstanse().showInfoMessageDialog(mssage);
				return;
			}
		

			// else{
			// double tuitionaAlimonys = Double.parseDouble(tuitiona_alimonys);
			// if(0 == tuitionaAlimonys){
			// BaseDroidApp.getInstanse().showInfoMessageDialog("输入的金额不能等于0");
			// return;
			// }
			// }
			
			String massage = "";
			// getResources().getString(R.string.loan_apply_tuitiona_alimony_String);
			if (!setRegexpBean(currencyCode, tuitiona_alimonys, massage)) {
				return;
			}
			;
		}
		// 净车价
		if (ConstantGloble.APPLY_VISIBILITY.equals(carTradePriceNeed)) {
			if (StringUtil.isNull(car_prices)) {

				String mssage = getResources().getString(
						R.string.loan_apply_car_prices_error);
				BaseDroidApp.getInstanse().showInfoMessageDialog(mssage);
				return;
			}
			// else{
			// double carPrices = Double.parseDouble(car_prices);
			// if(0 == carPrices){
			// BaseDroidApp.getInstanse().showInfoMessageDialog("输入的金额 不能等于0");
			// return;
			// }
			// }
			String massage = "";
			// getResources().getString(R.string.loan_apply_car_price_string);
			if (!setRegexpBean(currencyCode, car_prices, massage)) {
				return;
			}
			;
		}
		// 贷款金额
		if (ConstantGloble.APPLY_VISIBILITY.equals(loanAmountNeed)) {
			if (StringUtil.isNull(amounts)) {

				String mssage = getResources().getString(
						R.string.loan_apply_amounts_error);
				BaseDroidApp.getInstanse().showInfoMessageDialog(mssage);
				return;

			}
			
			// else{
			// double mAmounts = Double.parseDouble(amounts);
			// if(0 == mAmounts){
			// BaseDroidApp.getInstanse().showInfoMessageDialog("输入的 金额不能等于0");
			// return;
			// }

			String massage = "";
			// getResources().getString(R.string.loan_amount_string);
			if (!setRegexpBean(currencyCode, amounts, massage)) {
				return;
			}
			;
		}
		// 贷款期限
		if (ConstantGloble.APPLY_VISIBILITY.equals(loanTermNeed)) {
			if (StringUtil.isNull(input_loanPeriods)) {
				//
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getResources().getString(
								R.string.loan_apply_loanTermnew_error));
				return;
			} else {
				 inputloanPeriod = Integer.parseInt(input_loanPeriods);
				if (0 == inputloanPeriod) {
					String mage = getResources().getString(
							R.string.loan_apply_loanTerm_error);
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							getResources().getString(
									R.string.loan_apply_loanTerm_error));
					return;
				}

			}
		}
		//判断：如果年龄字段 为显示  则在判断年龄+贷款期限/12是否大于65周岁
		if (ConstantGloble.APPLY_VISIBILITY.equals(appAgeNeed)) {
		       mInputloanPeriod=inputloanPeriod/12;
			    if((mAge+mInputloanPeriod) >65){
			    	BaseDroidApp.getInstanse().showInfoMessageDialog(
						getResources().getString(
								R.string.loan_apply_age_error_two));
				return;
			}
		}
			
		
		// 房龄
		if (ConstantGloble.APPLY_VISIBILITY.equals(houseAgeNeed)) {
			if (StringUtil.isNull(buy_houses)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getResources().getString(
								R.string.loan_apply_buy_housesnew_error));
				return;
			} else {
				int buyHouses = Integer.parseInt(buy_houses);
				if (0 == buyHouses) {
					String mage = getResources().getString(
							R.string.loan_apply_buy_houses_error);
					BaseDroidApp.getInstanse().showInfoMessageDialog(mage);
					return;
				}
			}
		}

		// 担保方式
		if (ConstantGloble.APPLY_VISIBILITY.equals(guaWayNeed)
				&& StringUtil.isNull(guarantee_ways)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.loan_apply_guaWay_error));
			return;

		}
		// 担保类别
		if (ConstantGloble.APPLY_VISIBILITY.equals(guaTypeFlagNeed)) {
			if (bGuarantee == true) {
				if(btGuarantee==true){
					if(StringUtil.isNull(stGuarantee)){
						BaseDroidApp.getInstanse().showInfoMessageDialog(
								getResources().getString(
										R.string.loan_apply_guaTypeFlag_error));
						return;
					}
					
				}
			}else{
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getResources().getString(
								R.string.loan_apply_guaTypeFlag_tag_error));
				return;
			}
		}
		// 币种
		if (ConstantGloble.APPLY_VISIBILITY.equals(currencyNeed)) {
			if (StringUtil.isNull(mCurrency)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getResources().getString(
								R.string.loan_apply_currency_error));
				return;
			}
		}
		// 判断用户是否选择业务选择网点
		if (bankMessageMap.size() <= 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.loan_apply_deptid_error));
			return;
		}
		Intent intent = new Intent(LoanApplyInfoClientMessageActivity.this,
				LoanApplyConfirmActivity.class);
		intent.putExtra("cityCode", cityCode);
		intent.putExtra("typeCode", productCode);
		intent.putExtra("commConversationId", commConversationId);
		String mGuarantee = btGuarantee.toString();
		intent.putExtra("btGuarantee", mGuarantee);

		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.LOAN_APPLY_RESULTMAP, resMap);
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.LOAN_APPLY_BANKMESSAGEMAP, bankMessageMap);
		startActivity(intent);
	}

	// }

	/**
	 * currency 币种 repayAmount 用户输入的数值 massage 对应的提示
	 * */
	private boolean setRegexpBean(String currency, String repayAmount,
			String massage) {
		if (LocalData.codeNoNumber.contains(currency)) {
			// 日元
			RegexpBean reb1 = new RegexpBean(massage, repayAmount,
					"jpnAmountNew", true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb1);
			if (RegexpUtils.regexpDate(lists)) {
				return true;
			} else {
				return false;
			}
		} else {
			RegexpBean reb1 = new RegexpBean(massage, repayAmount,
					"loanapplyAmount", true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb1);
			if (RegexpUtils.regexpDate(lists)) {
				return true;
			} else {
				return false;
			}
		}
	}

	// 用于验证用户输入的企业名称。不能包含拼音，阿拉伯数字
	private boolean setNameRegexpBean(String repayAmount, String massage) {
		RegexpBean reb1 = new RegexpBean(massage, repayAmount, "entNameType");
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		lists.add(reb1);
		if (RegexpUtils.regexpDate(lists)) {
			return true;
		} else {
			return false;
		}
	}

}
