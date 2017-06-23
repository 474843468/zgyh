package com.chinamworld.bocmbci.biz.safety.safetyproduct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Inves;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyBaseActivity;
import com.chinamworld.bocmbci.biz.safety.SafetyConstant;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.SafetyUtils;
import com.chinamworld.bocmbci.biz.safety.adapter.SafetyPagerAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.PublicTools;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.ViewUtils;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 投保信息填写页面
 * 
 * @author panwe
 * 
 */
@SuppressLint("NewApi")
public class SafetyProductBuyMsgFillActivity extends SafetyBaseActivity implements OnCheckedChangeListener {
	private static final String TAG = "SafetyProductBuyMsgFillActivity";
	/** 存放View */
	private List<View> mViewList = new ArrayList<View>();
	/** 主页面 */
	private View mMainView;
	private LinearLayout mMainLayoutw;
	private LinearLayout mMainLayout;
	private LinearLayout mFillBtnLayout;
	private LinearLayout mConfirmBtnLayout;
	private Button mBtnLast;
	private ViewPager mViewPager;
	/** 当前页 */
	private int pageIndex;
	/** 子页 */
	private View mFirstView;
	private View mSecondView;
	private View mThirdView;
	private View mFourthView;
	/*** firstView **/
	/** 公司名 */
	private TextView mFirstCompanyName;
	/** 子公司名 */
	private Spinner mFirstSubCompanyName;
	/** 产品名 */
	private TextView mFirstInsurName;
	/** 保险起期 */
	private TextView mFirstInsurStartDate;
	/** 份数 */
	private TextView mFirstInsurCount;
	/** secondView */
	/** 姓名 */
	private TextView mSecondName;
	/** 性别 */
	private RadioButton mSecondGenderBtn1;
	private RadioButton mSecondGenderBtn2;
	/** 出生日期 */
	private TextView mSecodBirthday;
	/** 国籍 */
	private TextView mSecodCount;
	/** 证件类型 */
	private TextView mSecodIdType;
	/** 证件号码 */
	private TextView mSecodIdNum;
	/** 通讯地址 */
	private Spinner mSecodAdressProv;
	private Spinner mSecodAdressCity;
	private Spinner mSecodAdressCounty;
	private EditText mSecodAdressOther;
	/** 邮编 */
	private EditText mSecodPost;
	/** 手机 */
	private TextView mSecodPhone;
	/** 电子邮箱 */
	private EditText mSecodEmail;
	/** 投保人关系 */
	private Spinner mSecodBuyerGX;
	/** 是否本人投保 */
	private RadioButton mSecodBuyerbtn1;
	private RadioButton mSecodBuyerbtn2;
	/** thirdViews */
	/** 被投保人 */
	private EditText mThirdName;
	/** 被投保人性别 */
	private RadioButton mThirdGenderBtn1;
	private RadioButton mThirdGenderBtn2;
	/** 被投保人生日 */
	private TextView mThirdBirthday;
	/** 被投保人国籍 */
	private TextView mThirdCounty;
	/** 被投保人证件类型 */
	private Spinner mThirdIdType;
	/** 被投保人证件号 */
	private EditText mThirdIdNum;
	/** 被投保人通讯地址 */
	private Spinner mThirdAdressProv;
	private Spinner mThirdAdressCity;
	private Spinner mThirdAdressCounty;
	private EditText mThirdAdressOther;
	/** 被保人邮编 */
	private EditText mThirdPost;
	/** 被保人电话 */
	private EditText mThirdPhone;
	/** 被保人电子邮箱 */
	private EditText mThirdEmail;
	/** 迁往目的地 */
//	private TextView mBtnmdd;
	private TextView mThirdAdressMudi;
	/** 房屋类型 */
	private TextView mThirdHouseType;
	/** 房屋前往地址 */
	private Spinner mThirdAdressFWPro;
	private Spinner mThirdAdressFWCity;
	private Spinner mThirdAdressFWCounty;
	private EditText mThirdAdressFWOther;
	/** fouthViews */
	/** 投保账户 */
	private Spinner mFourthAcct;
	/** 是否需要发票 */
	private RadioButton mFourthBtn1;
	private RadioButton mFourthBtn2;
	private LinearLayout mFourthLayout;
	/** 发票抬头 */
	private EditText mFourthInvoiceTitle;
	/** 发票邮寄地址 */
	private EditText mFourthInvoiceAdress;
	/** 邮编 */
	private EditText mFourthInvoicePost;
	/** 收件人姓名 */
	private EditText mFourthInvoiceHoldName;
	/** 收件人联系电话 */
	private EditText mFourthInvoiceHoldPhone;
	/** 备注 */
	private EditText mFourthInvoiceBeizhu;
	/** 推荐人 */
	private EditText mFourthReferrerName;
	/** 保单别名 */
	private EditText mSaveAlias;
	/** 标识 */
	private boolean fromListFlag;
	/** 公司名 */
	private String companyName;
	/** 子公司名 */
	private String subCompanyName;
	/** 公司id */
	private String insurId;
	/** 子公司id */
	private String subInsuId;
	/** 产品名称 */
	private String productName;
	/** 产品类型 */
	private String productRisktype;
	private boolean isFillView = true;
	/** 产品代码（保险公司） */
	private String insuCode;
	/** 加密类型 */
	private String otp;
	private String smc;
	/** 保单名 */
	private String alias;
	/** 修改标识 */
	private boolean isChange = false;
	/** 保存标识 */
	private boolean isSave = false;
	/** 保费 */
	private String riskPrem;
	private int houseTypeTag;
	/** 核心客户号 */
	private String cifId;
	private boolean secondProvice = false;
	private boolean secondCity = false;
	private boolean thirdProvice = false;
	private boolean thirdCity = false;
	private boolean thirdProviceH = false;
	private boolean thirdCityH = false;
	private boolean relation = false;
	private boolean benIdType = false;
	private boolean isBen = false;
	private boolean isShowguid = true;
	private boolean saveOK = false;
	/** 保存临时数据 */
	private Map<String, Object> tempMap = new HashMap<String, Object>();
	private String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainView = addView(R.layout.safety_product_buy_msgfill);
		findViewById(R.id.sliding_body).setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.fill_margin_bottom));
		setTitle(R.string.safety_msgfill_title);
		crearData();
		getDateForIntent();
		initPageView();
		initFirstViews();
		initSecondViews();
		initThirdViews();
		initFourthViews();
		initViewClickListener();
		setDateForFirstViews();
		setDateForSecondViews();
		setDateForThirdViews();
		setDateForFourthViews();
	}

	/** 接受上页面信息 */
	private void getDateForIntent() {
		fromListFlag = getIntent().getBooleanExtra(SafetyConstant.PRODUCTORSAVE, false);
		companyName = getIntent().getStringExtra(Safety.INSURANCE_COMANY);
		subCompanyName = getIntent().getStringExtra(Safety.SUBINSUNAME);
		productRisktype = getIntent().getStringExtra(Safety.RISKTYPE);
		insurId = getIntent().getStringExtra(Safety.INSURANCE_ID);
		subInsuId = getIntent().getStringExtra(Safety.SUBINSUID);
		productName = getIntent().getStringExtra(Safety.RISKNAME);
		insuCode = getIntent().getStringExtra(Safety.INSUCODE);
		houseTypeTag = getIntent().getIntExtra(Safety.HOUSETYPE, 0);
	}

	/** 初始化pageView */
	private void initPageView() {
		mMainView.findViewById(R.id.layout_step1).setOnClickListener(stepOnclick);
		mMainView.findViewById(R.id.layout_step2).setOnClickListener(stepOnclick);
		mMainView.findViewById(R.id.layout_step3).setOnClickListener(stepOnclick);
		mMainView.findViewById(R.id.layout_step4).setOnClickListener(stepOnclick);
		mMainLayoutw = (LinearLayout) mMainView.findViewById(R.id.layout_mainw);
		mMainLayout = (LinearLayout) mMainView.findViewById(R.id.layout_main);
		mFillBtnLayout = (LinearLayout) mMainView.findViewById(R.id.btn_layout);
		mConfirmBtnLayout = (LinearLayout) mMainView.findViewById(R.id.btn_layout_confirm);
		mBtnLast = (Button) mMainView.findViewById(R.id.btnLast);
		mViewPager = (ViewPager) mMainView.findViewById(R.id.viewpager);
		mFirstView = View.inflate(this, R.layout.safety_productmsgfill_first, null);
		mSecondView = View.inflate(this, R.layout.safety_productmsgfill_second, null);
		mThirdView = View.inflate(this, R.layout.safety_productmsgfill_third, null);
		mFourthView = View.inflate(this, R.layout.safety_productmsgfill_fourth, null);
		mViewList.add(mFirstView);
		mViewList.add(mSecondView);
		mViewList.add(mThirdView);
		mViewList.add(mFourthView);
		mViewPager.setAdapter(new SafetyPagerAdapter(mViewList));
	}

	/** 初始化第一个View */
	private void initFirstViews() {
		mFirstCompanyName = (TextView) mFirstView.findViewById(R.id.first_insurcompany);
		mFirstSubCompanyName = (Spinner) mFirstView.findViewById(R.id.first_subInsurcompany);
		mFirstInsurName = (TextView) mFirstView.findViewById(R.id.first_insurname);
		mFirstInsurStartDate = (TextView) mFirstView.findViewById(R.id.first_startdate);
		mFirstInsurCount = (TextView) mFirstView.findViewById(R.id.first_insurnumber);
	}

	/** 初始化第二个View */
	private void initSecondViews() {
		mSecondName = (TextView) mSecondView.findViewById(R.id.second_name);
		mSecondGenderBtn1 = (RadioButton) mSecondView.findViewById(R.id.second_gneder_btn1);
		mSecondGenderBtn2 = (RadioButton) mSecondView.findViewById(R.id.second_gneder_btn2);
		mSecodBirthday = (TextView) mSecondView.findViewById(R.id.second_birthday);
		mSecodCount = (TextView) mSecondView.findViewById(R.id.second_county);
		mSecodIdType = (TextView) mSecondView.findViewById(R.id.second_idtype);
		mSecodIdNum = (TextView) mSecondView.findViewById(R.id.second_idnumber);
		mSecodAdressProv = (Spinner) mSecondView.findViewById(R.id.second_adress_prov);
		mSecodAdressCity = (Spinner) mSecondView.findViewById(R.id.second_adress_city);
		mSecodAdressCounty = (Spinner) mSecondView.findViewById(R.id.second_adress_county);
		mSecodAdressOther = (EditText) mSecondView.findViewById(R.id.second_adress_other);
		mSecodPost = (EditText) mSecondView.findViewById(R.id.second_postcode);
		mSecodPhone = (TextView) mSecondView.findViewById(R.id.secoond_phone);
		mSecodEmail = (EditText) mSecondView.findViewById(R.id.secoond_email);
		mSecodBuyerGX = (Spinner) mSecondView.findViewById(R.id.second_buyerguanxi);
		mSecodBuyerbtn1 = (RadioButton) mSecondView.findViewById(R.id.second_buyer_btn1);
		mSecodBuyerbtn2 = (RadioButton) mSecondView.findViewById(R.id.second_buyer_btn2);
		EditTextUtils.setLengthMatcher(this, mSecodEmail, 30);
		((RadioGroup) mSecondView.findViewById(R.id.loan_advance_type)).setOnCheckedChangeListener(this);
		((RadioGroup) mSecondView.findViewById(R.id.loan_advan)).setOnCheckedChangeListener(this);
		if (!SafetyUtils.isHouseType(productRisktype)) {
			((LinearLayout) mSecondView.findViewById(R.id.layout_buyerrelation)).setVisibility(View.VISIBLE);
			((LinearLayout) mSecondView.findViewById(R.id.layout_buyerrelation_tishi)).setVisibility(View.VISIBLE);
		} else {
			((LinearLayout) mSecondView.findViewById(R.id.layout_buyer)).setVisibility(View.VISIBLE);
			mSecodBuyerbtn1.setChecked(true);
		}
		if (fromListFlag) {
			isBen = true;
		}
	}

	/** 初始化第三个View */
	private void initThirdViews() {
		mThirdName = (EditText) mThirdView.findViewById(R.id.second_name);
		mThirdGenderBtn1 = (RadioButton) mThirdView.findViewById(R.id.second_gneder_btn1);
		mThirdGenderBtn2 = (RadioButton) mThirdView.findViewById(R.id.second_gneder_btn2);
		mThirdBirthday = (TextView) mThirdView.findViewById(R.id.second_birthday);
//		mBtnmdd = (TextView) mThirdView.findViewById(R.id.btn_md);
		mThirdCounty = (TextView) mThirdView.findViewById(R.id.second_county);
		mThirdIdType = (Spinner) mThirdView.findViewById(R.id.second_idtype);
		mThirdIdNum = (EditText) mThirdView.findViewById(R.id.second_idnumber);
		mThirdAdressProv = (Spinner) mThirdView.findViewById(R.id.third_adress_prov);
		mThirdAdressCity = (Spinner) mThirdView.findViewById(R.id.third_adress_city);
		mThirdAdressCounty = (Spinner) mThirdView.findViewById(R.id.third_adress_county);
		mThirdAdressOther = (EditText) mThirdView.findViewById(R.id.second_adress_other);
		mThirdPost = (EditText) mThirdView.findViewById(R.id.second_postcode);
		mThirdPhone = (EditText) mThirdView.findViewById(R.id.secoond_phone);
		mThirdEmail = (EditText) mThirdView.findViewById(R.id.secoond_email);
		mThirdAdressMudi = (TextView) mThirdView.findViewById(R.id.second_adress_md);
		mThirdHouseType = (TextView) mThirdView.findViewById(R.id.third_housetype);
		mThirdAdressFWPro = (Spinner) mThirdView.findViewById(R.id.second_adress_fw_prov);
		mThirdAdressFWCity = (Spinner) mThirdView.findViewById(R.id.second_adress_fw_city);
		mThirdAdressFWCounty = (Spinner) mThirdView.findViewById(R.id.second_adress_fw_county);
		mThirdAdressFWOther = (EditText) mThirdView.findViewById(R.id.second_adress_fw_other);
		EditTextUtils.setLengthMatcher(this, mThirdEmail, 30);
		mThirdIdType.setOnItemSelectedListener(spinnerItemOnselect);
		if (SafetyUtils.isHouseType(productRisktype)) {
			((LinearLayout) mThirdView.findViewById(R.id.layout_house)).setVisibility(View.VISIBLE);
			((LinearLayout) mThirdView.findViewById(R.id.layoutshouyiren)).setVisibility(View.GONE);
		}
		if (checkDestination()) {
			((LinearLayout) mThirdView.findViewById(R.id.layout_md)).setVisibility(View.VISIBLE);
		}
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mBtnmdd);
	}

	/** 初始化第四个View */
	private void initFourthViews() {
		mFourthAcct = (Spinner) mFourthView.findViewById(R.id.fourth_acct);
		mFourthBtn1 = (RadioButton) mFourthView.findViewById(R.id.fourth_btn1);
		mFourthBtn2 = (RadioButton) mFourthView.findViewById(R.id.fourth_btn2);
		mFourthLayout = (LinearLayout) mFourthView.findViewById(R.id.layoutinvoice);
		mFourthInvoiceTitle = (EditText) mFourthView.findViewById(R.id.fourth_invoicetitle);
		mFourthInvoiceAdress = (EditText) mFourthView.findViewById(R.id.fourth_invoiceadress);
		mFourthInvoicePost = (EditText) mFourthView.findViewById(R.id.fourth_post);
		mFourthInvoiceHoldName = (EditText) mFourthView.findViewById(R.id.fourth_invoicehadname);
		mFourthInvoiceHoldPhone = (EditText) mFourthView.findViewById(R.id.fourth_invoicehadphone);
		mFourthInvoiceBeizhu = (EditText) mFourthView.findViewById(R.id.fourth_beizhu);
		mFourthReferrerName = (EditText) mFourthView.findViewById(R.id.referrerName);
		((RadioGroup) mFourthView.findViewById(R.id.fourth_raGroup)).setOnCheckedChangeListener(this);
	}

	/** 初始化事件 */
	private void initViewClickListener() {
		mLeftButton.setOnClickListener(backOnclick);
		mViewPager.setOnPageChangeListener(mViewPageListener);
		mFirstSubCompanyName.setOnItemSelectedListener(subCompanyListener);
		mFirstInsurStartDate.setOnClickListener(SafetyChooseDateClick);
		mSecodBuyerGX.setOnItemSelectedListener(spinnerItemOnselect);
		mSecodBirthday.setOnClickListener(SafetyChooseDateClick);
		mSecodAdressProv.setOnItemSelectedListener(spinnerItemOnselect);
		mSecodAdressCity.setOnItemSelectedListener(spinnerItemOnselect);
		// mBtnmdd.setOnClickListener(toMDDOnclick);
		mThirdAdressMudi.setOnClickListener(toMDDOnclick);
		mThirdBirthday.setOnClickListener(SafetyChooseDateClick);
		mThirdAdressProv.setOnItemSelectedListener(spinnerItemOnselect);
		mThirdAdressCity.setOnItemSelectedListener(spinnerItemOnselect);
		mThirdAdressFWPro.setOnItemSelectedListener(spinnerItemOnselect);
		mThirdAdressFWCity.setOnItemSelectedListener(spinnerItemOnselect);
	}

	/** 赋值第一个View */
	private void setDateForFirstViews() {
		mFirstInsurName.setText(productName);
		mFirstInsurCount.setText(SafetyConstant.RISKUNIT);
		mFirstInsurStartDate.setText(QueryDateUtils.getOneDayLater(SafetyDataCenter.getInstance().getSysTime()));
		SafetyUtils.initSpinnerView(this, mFirstSubCompanyName, Arrays.asList(getResources().getStringArray(R.array.subInsuId_CN)));
		if (!fromListFlag) {
			Map<String, Object> map = SafetyDataCenter.getInstance().getProductInfoMap();
			companyName = (String) map.get(Safety.INSURANCE_COMANY);
			subCompanyName = (String) map.get(Safety.SUBINSUNAME);
			subInsuId = (String) map.get(Safety.SUBINSUID);
			if (!StringUtil.isNull(subCompanyName)) {
				mFirstSubCompanyName.setSelection(Arrays.asList(getResources().getStringArray(R.array.subInsuId_code)).indexOf(subInsuId));
			}
			if (!StringUtil.isNull((String) map.get(Safety.POLEFFDATE))) {
				mFirstInsurStartDate.setText((String) map.get(Safety.POLEFFDATE));
			}
		}
		mFirstCompanyName.setText(companyName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mFirstCompanyName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mFirstInsurName);
	}

	/** 赋值第二个View */
	@SuppressWarnings("unchecked")
	private void setDateForSecondViews() {
		EditTextUtils.setLengthMatcher(this, mSecodAdressOther, 80);
		mSecodCount.setText(SafetyDataCenter.countryMap.get(SafetyConstant.COUNTRY));
		SafetyUtils.initSpinnerView(this, mSecodAdressProv, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getProviceList(), Safety.NAME));
		SafetyUtils.initSpinnerView(this, mSecodAdressCity, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getSecondCityList(), Safety.NAME));
		SafetyUtils.initSpinnerView(this, mSecodAdressCounty, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getSecondCountryList(), Safety.NAME));
		Map<String, Object> logInfo = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA);
		cifId = (String) logInfo.get(Safety.CIF_ID);
		initRelation();
		if (!fromListFlag) {
			Map<String, Object> map = SafetyDataCenter.getInstance().getProductInfoMap();
			mSecondName.setText((String) map.get(Safety.APPL_NAME));
			mSecodBirthday.setText((String) map.get(Safety.APPL_BIRTH));
			SafetyUtils.settRadioButton(mSecondGenderBtn1, mSecondGenderBtn2, (String) map.get(Safety.APPL_SEX));
			if (map.get(Safety.APPL_IDTYPE).equals("01")) {
				mSecodBirthday.setBackgroundDrawable(null);
				mSecodBirthday.setClickable(false);
				mSecondGenderBtn1.setClickable(false);
				mSecondGenderBtn2.setClickable(false);
			}
			mSecodIdType.setText(SafetyDataCenter.credType.get(map.get(Safety.APPL_IDTYPE)));
			mSecodIdNum.setText((String) map.get(Safety.APPL_IDNO));
			SafetyUtils.setCitySpinnerText(mSecodAdressProv, SafetyDataCenter.getInstance().getProviceList(), (String) map.get(Safety.APPL_PROVINCE));
			SafetyUtils.setCitySpinnerText(mSecodAdressCity, SafetyDataCenter.getInstance().getSecondCityList(), (String) map.get(Safety.APPL_CITY));
			SafetyUtils.setCitySpinnerText(mSecodAdressCounty, SafetyDataCenter.getInstance().getSecondCountryList(), (String) map.get(Safety.APPL_COUNTY));
			mSecodAdressOther.setText((String) map.get(Safety.APPL_ADRESS));
			mSecodPost.setText((String) map.get(Safety.APPL_POSTCODE));
			mSecodPhone.setText((String) map.get(Safety.APPL_PHONE));
			mSecodEmail.setText((String) map.get(Safety.APPL_EMAIL));
			relationSelect(map);
		} else {
			mSecondName.setText((String) logInfo.get(Inves.CUSTOMERNAME));
			String identityType = (String) logInfo.get(Comm.IDENTITYTYPE);
			String identityNum = (String) logInfo.get(Comm.IDENTITYNUMBER);
			String gender = (String) logInfo.get(Safety.GENDER);
			if (identityType.equals("47") || identityType.equals("48")) {
				mSecodIdType.setText(SafetyDataCenter.credType.get(logInfo.get(Comm.IDENTITYTYPE)));
			} else {
				mSecodIdType.setText(LocalData.IDENTITYTYPE.get(logInfo.get(Comm.IDENTITYTYPE)));
			}
			if (identityType.equals("1")) {// 证件类型为身份证
				String gender2 = SafetyUtils.getGender(identityNum);
				SafetyUtils.settRadioButton(mSecondGenderBtn1, mSecondGenderBtn2, gender2);
				mSecodBirthday.setText(SafetyUtils.getBirthday(identityNum));
				mSecodBirthday.setBackgroundDrawable(null);
				mSecodBirthday.setClickable(false);
				mSecondGenderBtn1.setClickable(false);
				mSecondGenderBtn2.setClickable(false);
			} else {
				showGenderForLoginInfo(gender);
				mSecodBirthday.setText(QueryDateUtils.getcurrentDate(SafetyDataCenter.getInstance().getSysTime()));
			}
			mSecodIdNum.setText((String) logInfo.get(Comm.IDENTITYNUMBER));
			mSecodPhone.setText((String) logInfo.get(Safety.MOBILE));
		}
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mSecodIdNum);
	}

	/** 赋值第三个View */
	private void setDateForThirdViews() {
		EditTextUtils.setLengthMatcher(this, mThirdName, 80);
		EditTextUtils.setLengthMatcher(this, mThirdAdressOther, 80);
		EditTextUtils.setLengthMatcher(this, mThirdAdressFWOther, 80);
		mThirdCounty.setText(SafetyDataCenter.countryMap.get(SafetyConstant.COUNTRY));
		mThirdBirthday.setText(QueryDateUtils.getcurrentDate(SafetyDataCenter.getInstance().getSysTime()));
		SafetyUtils.initSpinnerView(this, mThirdIdType, SafetyDataCenter.credTypeList);
		SafetyUtils.initSpinnerView(this, mThirdAdressProv, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getProviceList(), Safety.NAME));
		SafetyUtils.initSpinnerView(this, mThirdAdressCity, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getThirdCityList(), Safety.NAME));
		SafetyUtils.initSpinnerView(this, mThirdAdressCounty, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getThirdCountryList(), Safety.NAME));
		if (SafetyUtils.isHouseType(productRisktype)) {
			mThirdHouseType.setText(SafetyDataCenter.houseTypeList.get(houseTypeTag));
			SafetyUtils.initSpinnerView(this, mThirdAdressFWPro, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getProviceListHS(), Safety.NAME));
			SafetyUtils.initSpinnerView(this, mThirdAdressFWCity, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getThirdCityListHS(), Safety.NAME));
			SafetyUtils.initSpinnerView(this, mThirdAdressFWCounty, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getThirdCountryListHS(), Safety.NAME));
		}
		if (!fromListFlag) {
			Map<String, Object> map = SafetyDataCenter.getInstance().getProductInfoMap();
			mThirdName.setText((String) map.get(Safety.BEN_NAME));
			mThirdBirthday.setText((String) map.get(Safety.BEN_BIRTH));
			SafetyUtils.settRadioButton(mThirdGenderBtn1, mThirdGenderBtn2, (String) map.get(Safety.BEN_SEX));
			SafetyUtils.spinnerSetText(mThirdIdType, SafetyDataCenter.credTypeList, SafetyDataCenter.credType.get((String) map.get(Safety.BEN_IDTYPE)));
			mThirdIdNum.setText((String) map.get(Safety.BEN_IDNUM));
			SafetyUtils.setCitySpinnerText(mThirdAdressProv, SafetyDataCenter.getInstance().getProviceList(), (String) map.get(Safety.BEN_PROVINCE));
			SafetyUtils.setCitySpinnerText(mThirdAdressCity, SafetyDataCenter.getInstance().getThirdCityList(), (String) map.get(Safety.BEN_CITY));
			SafetyUtils.setCitySpinnerText(mThirdAdressCounty, SafetyDataCenter.getInstance().getThirdCountryList(), (String) map.get(Safety.BEN_COUNTY));
			mThirdAdressOther.setText((String) map.get(Safety.BEN_ADRESS));
			mThirdPost.setText((String) map.get(Safety.BEN_POSTCODE));
			mThirdPhone.setText((String) map.get(Safety.BEN_PHONE));
			mThirdEmail.setText((String) map.get(Safety.BEN_EMAIL));
			mThirdAdressMudi.setText(showDestinationInfo());
			if (SafetyUtils.isHouseType(productRisktype)) {
				int index = initData(SafetyDataCenter.houseTypeListrq, (String) map.get(Safety.HOUSETYPE));
				mThirdHouseType.setText(SafetyDataCenter.houseTypeList.get(index));
				SafetyUtils.setCitySpinnerText(mThirdAdressFWPro, SafetyDataCenter.getInstance().getProviceListHS(), (String) map.get(Safety.HOUSE_PROVINCE));
				SafetyUtils.setCitySpinnerText(mThirdAdressFWCity, SafetyDataCenter.getInstance().getThirdCityListHS(), (String) map.get(Safety.HOUSE_CITY));
				SafetyUtils.setCitySpinnerText(mThirdAdressFWCounty, SafetyDataCenter.getInstance().getThirdCountryListHS(), (String) map.get(Safety.HOUSE_COUNTY));
				mThirdAdressFWOther.setText((String) map.get(Safety.HOUSE_ADRESS));
			}
		}
	}

	/** 赋值第四个View */
	private void setDateForFourthViews() {
		EditTextUtils.setLengthMatcher(this, mFourthInvoiceTitle, 80);
		EditTextUtils.setLengthMatcher(this, mFourthInvoiceAdress, 80);
		EditTextUtils.setLengthMatcher(this, mFourthInvoiceHoldName, 80);
		EditTextUtils.setLengthMatcher(this, mFourthReferrerName, 80);
		EditTextUtils.setLengthMatcher(this, mFourthInvoiceBeizhu, 100);
		List<String> mList = PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getAcctList(), Comm.ACCOUNTNUMBER);
		SafetyUtils.initSpinnerView(this, mFourthAcct, mList);
		if (!fromListFlag) {
			Map<String, Object> map = SafetyDataCenter.getInstance().getProductInfoMap();
			for (int i = 0; i < SafetyDataCenter.getInstance().getAcctList().size(); i++) {
				if (!StringUtil.isNull((String) map.get(Safety.ACC_ID))) {
					if (map.get(Safety.ACC_ID).equals(SafetyDataCenter.getInstance().getAcctList().get(i).get(Comm.ACCOUNT_ID))) {
						mFourthAcct.setSelection(i);
					}
				} else {
					mFourthAcct.setSelection(0);
				}
			}
			if ((map.get(Safety.INV_FLAG)).equals("1")) {
				mFourthBtn1.setChecked(true);
				mFourthInvoiceTitle.setText((String) map.get(Safety.INV_TITLE));
				mFourthInvoiceAdress.setText((String) map.get(Safety.INV_ADRESS));
				mFourthInvoicePost.setText((String) map.get(Safety.INV_POSTCODE));
				mFourthInvoiceHoldName.setText((String) map.get(Safety.INV_NAME));
				mFourthInvoiceHoldPhone.setText((String) map.get(Safety.INV_PHONE));
				mFourthInvoiceBeizhu.setText((String) map.get(Safety.INV_REMARKS));
			}
			if (!StringUtil.isNull((String) map.get(Safety.SELL_NAME))) {
				mFourthReferrerName.setText((String) map.get(Safety.SELL_NAME));
			}
		}
	}

	/** 初始化信息展示页面 */
	private void initInfoshowView(View v) {
		LinearLayout layoutBuyer_yw = (LinearLayout) v.findViewById(R.id.buyerlayout_yiwai);
		LinearLayout layoutBuyer_jc = (LinearLayout) v.findViewById(R.id.buyerlayout_jiacai);
		LinearLayout layoutDestination = (LinearLayout) v.findViewById(R.id.layoutmudidi);
		LinearLayout layoutFWadress = (LinearLayout) v.findViewById(R.id.fwadresslayout);
		LinearLayout layoutFaPiao = (LinearLayout) v.findViewById(R.id.layoutfapiao);
		LinearLayout layoutsyr = (LinearLayout) v.findViewById(R.id.layoutshouyiren);
		controlInfoView(layoutBuyer_yw, layoutDestination, layoutFWadress, layoutFaPiao, layoutsyr, layoutBuyer_jc);
		// 产品信息
		((TextView) v.findViewById(R.id.companyname)).setText(companyName);
		((TextView) v.findViewById(R.id.subCompanyname)).setText(subCompanyName);
		((TextView) v.findViewById(R.id.productname)).setText(productName);
		((TextView) v.findViewById(R.id.insurstartdate)).setText(SafetyUtils.getText(mFirstInsurStartDate));
		((TextView) v.findViewById(R.id.insurnum)).setText(mFirstInsurCount.getText().toString());
		// 投保人信息
		((TextView) v.findViewById(R.id.buuyername)).setText(mSecondName.getText().toString());
		((TextView) v.findViewById(R.id.buuyergender)).setText(SafetyUtils.getTextFromRaBtn(mSecondGenderBtn1, mSecondGenderBtn2));
		((TextView) v.findViewById(R.id.buuyerbirthday)).setText(mSecodBirthday.getText().toString());
		((TextView) v.findViewById(R.id.buuyercounty)).setText(SafetyDataCenter.countryMap.get(SafetyConstant.COUNTRY));
		((TextView) v.findViewById(R.id.buuyeridtype)).setText(mSecodIdType.getText().toString());
		((TextView) v.findViewById(R.id.buuyeridnum)).setText(mSecodIdNum.getText().toString());
		String adress = mSecodAdressProv.getSelectedItem().toString() + mSecodAdressCity.getSelectedItem().toString() + mSecodAdressCounty.getSelectedItem().toString() + mSecodAdressOther.getText().toString().trim();
		((TextView) v.findViewById(R.id.buuyeradress)).setText(adress);
		((TextView) v.findViewById(R.id.buuyerpost)).setText(mSecodPost.getText().toString().trim());
		((TextView) v.findViewById(R.id.buuyerphone)).setText(mSecodPhone.getText().toString());
		((TextView) v.findViewById(R.id.buuyeremail)).setText(mSecodEmail.getText().toString().trim());
		((TextView) v.findViewById(R.id.buuyergx)).setText(mSecodBuyerGX.getSelectedItem().toString());
		((TextView) v.findViewById(R.id.buuyeror)).setText(SafetyUtils.getTextFromRaBtn(mSecodBuyerbtn1, mSecodBuyerbtn2));
		// 被保人信息
		((TextView) v.findViewById(R.id.holdname)).setText(mThirdName.getText().toString().trim());
		((TextView) v.findViewById(R.id.holdgender)).setText(SafetyUtils.getTextFromRaBtn(mThirdGenderBtn1, mThirdGenderBtn2));
		((TextView) v.findViewById(R.id.holdbirthday)).setText(mThirdBirthday.getText().toString());
		((TextView) v.findViewById(R.id.holdcounty)).setText(SafetyDataCenter.countryMap.get(SafetyConstant.COUNTRY));
		((TextView) v.findViewById(R.id.holdidtype)).setText(mThirdIdType.getSelectedItem().toString());
		((TextView) v.findViewById(R.id.holdidnum)).setText(mThirdIdNum.getText().toString().trim());
		// ((TextView)
		// v.findViewById(R.id.holdidlastdate)).setText(mThirdIdDate.getText().toString());
		String holdadress = mThirdAdressProv.getSelectedItem().toString() + mThirdAdressCity.getSelectedItem().toString() + mThirdAdressCounty.getSelectedItem().toString() + mThirdAdressOther.getText().toString();
		((TextView) v.findViewById(R.id.holdadress)).setText(holdadress);
		((TextView) v.findViewById(R.id.holdpost)).setText(mThirdPost.getText().toString().trim());
		((TextView) v.findViewById(R.id.holdphone)).setText(mThirdPhone.getText().toString().trim());
		((TextView) v.findViewById(R.id.holdemail)).setText(mThirdEmail.getText().toString().trim());
		((TextView) v.findViewById(R.id.destination)).setText(mThirdAdressMudi.getText().toString());
		((TextView) v.findViewById(R.id.holdfwtype)).setText(mThirdHouseType.getText().toString());
		if (SafetyUtils.isHouseType(productRisktype)) {
			String holdfwadress = mThirdAdressFWPro.getSelectedItem().toString() + mThirdAdressFWCity.getSelectedItem().toString() + mThirdAdressFWCounty.getSelectedItem().toString() + mThirdAdressFWOther.getText().toString().trim();
			((TextView) v.findViewById(R.id.holdfwadress)).setText(holdfwadress);
		}
		// 发票等其他信息
		((TextView) v.findViewById(R.id.payacct)).setText(mFourthAcct.getSelectedItem().toString());
		((TextView) v.findViewById(R.id.needfapiao)).setText(SafetyUtils.getTextFromRaBtn(mFourthBtn1, mFourthBtn2));
		((TextView) v.findViewById(R.id.fapiaotitle)).setText(mFourthInvoiceTitle.getText().toString().trim());
		((TextView) v.findViewById(R.id.fapiaoadress)).setText(mFourthInvoiceAdress.getText().toString().trim());
		((TextView) v.findViewById(R.id.fapiaopost)).setText(mFourthInvoicePost.getText().toString().trim());
		((TextView) v.findViewById(R.id.fapiaohold)).setText(mFourthInvoiceHoldName.getText().toString().trim());
		((TextView) v.findViewById(R.id.fapiaoholdphone)).setText(mFourthInvoiceHoldPhone.getText().toString().trim());
		((TextView) v.findViewById(R.id.fapiaobeizhu)).setText(StringUtil.valueOf1(mFourthInvoiceBeizhu.getText().toString().trim()));

		((TextView) v.findViewById(R.id.referrerName)).setText(StringUtil.valueOf1(mFourthReferrerName.getText().toString().trim()));

		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) v.findViewById(R.id.buuyeridnum));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) v.findViewById(R.id.buuyeradress));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) v.findViewById(R.id.buuyeremail));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) v.findViewById(R.id.holdidnum));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) v.findViewById(R.id.holdadress));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) v.findViewById(R.id.holdfwadress));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) v.findViewById(R.id.holdemail));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) v.findViewById(R.id.fapiaotitle));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) v.findViewById(R.id.fapiaohold));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) v.findViewById(R.id.text_phone));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) v.findViewById(R.id.fapiaoadress));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) v.findViewById(R.id.fapiaoholdphone));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) v.findViewById(R.id.fapiaobeizhu));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) v.findViewById(R.id.referrerName));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, layoutDestination);
	}

	/**
	 * 
	 * @param gender
	 */
	private void showGenderForLoginInfo(String gender) {
		if (StringUtil.isNull(gender)) {
			mSecondGenderBtn1.setChecked(false);
			mSecondGenderBtn2.setChecked(false);
			return;
		}
		if (gender.equals("1")) {
			mSecondGenderBtn1.setChecked(true);
		} else if (gender.equals("2")) {
			mSecondGenderBtn2.setClickable(true);
		} else {
			mSecondGenderBtn1.setChecked(false);
			mSecondGenderBtn2.setChecked(false);
		}
	}

	/**
	 * 控制信息展示页
	 * 
	 * @param v1
	 * @param v2
	 * @param v3
	 * @param v4
	 * @param v5
	 */
	private void controlInfoView(LinearLayout v1, LinearLayout v2, LinearLayout v3, LinearLayout v4, LinearLayout v5, LinearLayout v6) {
		if (!SafetyUtils.isHouseType(productRisktype)) {
			v1.setVisibility(View.VISIBLE);
			v5.setVisibility(View.VISIBLE);
			if (checkDestination()) {
				v2.setVisibility(View.VISIBLE);
			}
		}
		if (SafetyUtils.isHouseType(productRisktype)) {
			v3.setVisibility(View.VISIBLE);
			v6.setVisibility(View.VISIBLE);
		}
		if (mFourthBtn1.isChecked()) {
			v4.setVisibility(View.VISIBLE);
		}
	}

	/** 上一步 */
	public void btnLastOnclick(View v) {
		mViewPager.setCurrentItem(pageIndex - 1);
	}

	/** 保存/修改 */
	public void btnSaveOnclick(View v) {
		if (!submitCheck(false)) {
			return;
		}
		if (!checkReferrerName()) {
			return;
		}
		if (!StringUtil.isNull(alias) && saveOK) {
			isChange = true;
			requestTempInsuranceList();
		} else {
			if (fromListFlag) {
				showSaveDialog();
			} else {
				isChange = true;
				alias = (String) SafetyDataCenter.getInstance().getProductInfoMap().get(Safety.ALIAS_ID);
				id = (String) SafetyDataCenter.getInstance().getProductInfoMap().get(Safety.DEPETE_INSURID);
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
			}
		}
	}

	/** 提交 */
	public void btnSubmitOnclick(View v) {
		if (pageIndex != 3) {
			mBtnLast.setVisibility(View.VISIBLE);
			mViewPager.setCurrentItem(pageIndex + 1);
			return;
		}
		initDataForBen();
		if (submitCheck(true) && checkReferrerName()) {
			isFillView = false;
			mMainView.findViewById(R.id.layout_step).setVisibility(View.GONE);
			mViewPager.setVisibility(View.GONE);
			mFillBtnLayout.setVisibility(View.GONE);
			mMainLayoutw.setVisibility(View.VISIBLE);
			mConfirmBtnLayout.setVisibility(View.VISIBLE);
			setTitle(getString(R.string.safety_confirm_title));
			View infoView = View.inflate(this, R.layout.safety_product_infoshow, null);
			mMainLayout.addView(infoView);
			initInfoshowView(infoView);
		}
	}

	/** 下一步 */
	public void btnNextOnclick(View v) {
		isSave = false;
		isChange = false;
		BaseHttpEngine.showProgressDialog();
		requestCommConversationId();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.second_buyer_btn1:// 被保人是本人
			isBen = true;
			break;

		case R.id.second_buyer_btn2:// 被保人不是本人
			setViewClickable(true);
			restoreView2Editable();
			isBen = false;
			break;

		case R.id.second_gneder_btn1:// 性别 - 男
			initRelation();
			break;

		case R.id.second_gneder_btn2:// 性别 - 女
			initRelation();
			break;

		case R.id.fourth_btn1:
			closeInput();
			mFourthLayout.setVisibility(View.VISIBLE);
			break;

		case R.id.fourth_btn2:
			closeInput();
			mFourthLayout.setVisibility(View.GONE);
			break;
		}
	}
	
	/** 子公司选择事件 */
	private OnItemSelectedListener subCompanyListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			subCompanyName = mFirstSubCompanyName.getSelectedItem().toString();
			subInsuId = getResources().getStringArray(R.array.subInsuId_code)[mFirstSubCompanyName.getSelectedItemPosition()];
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};

	private void showSaveDialog() {
		View dialogView = View.inflate(this, R.layout.safety_save_dialog, null);
		mSaveAlias = (EditText) dialogView.findViewById(R.id.save_alias);
		if (SafetyUtils.isHouseType(productRisktype)) {
			mSaveAlias.setText("家财险" + SafetyDataCenter.getInstance().getSysTime());
		} else {
			mSaveAlias.setText("意外险" + SafetyDataCenter.getInstance().getSysTime());
		}
		BaseDroidApp.getInstanse().showDialog(dialogView);
	}

	public void dialogClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cance:
			BaseDroidApp.getInstanse().dismissErrorDialog();
			break;

		case R.id.btn_confirm:
			alias = mSaveAlias.getText().toString().trim();
			if (StringUtil.isNull(alias)) {
				CustomDialog.toastInCenter(this, SafetyConstant.ALIAS);
				return;
			}
			insurAliasCheck(alias);
			break;
		}
	}

	/** sipnner选中事件 */
	private OnItemSelectedListener spinnerItemOnselect = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			switch (parent.getId()) {
			case R.id.second_adress_prov:// 初始化城市
				if (!fromListFlag && !secondProvice) {
					secondProvice = true;
				} else {
					String soCityCode = (String) SafetyDataCenter.getInstance().getProviceList().get(position).get(Safety.CODE);
					SafetyDataCenter.getInstance().setSecondCityList(SafetyUtils.getCountryAndCity(SafetyProductBuyMsgFillActivity.this, soCityCode));
					String soCountryCode = (String) SafetyDataCenter.getInstance().getSecondCityList().get(0).get(Safety.CODE);
					SafetyDataCenter.getInstance().setSecondCountryList(SafetyUtils.getCountryAndCity(SafetyProductBuyMsgFillActivity.this, soCountryCode));
					SafetyUtils.initSpinnerView(SafetyProductBuyMsgFillActivity.this, mSecodAdressCity, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getSecondCityList(), Safety.NAME));
					SafetyUtils.initSpinnerView(SafetyProductBuyMsgFillActivity.this, mSecodAdressCounty, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getSecondCountryList(), Safety.NAME));
				}
				break;

			case R.id.second_adress_city:// 初始化城区
				if (!fromListFlag && !secondCity) {
					secondCity = true;
				} else {
					String stCountryCode = (String) SafetyDataCenter.getInstance().getSecondCityList().get(position).get(Safety.CODE);
					SafetyDataCenter.getInstance().setSecondCountryList(SafetyUtils.getCountryAndCity(SafetyProductBuyMsgFillActivity.this, stCountryCode));
					SafetyUtils.initSpinnerView(SafetyProductBuyMsgFillActivity.this, mSecodAdressCounty, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getSecondCountryList(), Safety.NAME));
				}
				break;

			case R.id.third_adress_prov:// 初始化城市
				if (!fromListFlag && !thirdProvice) {
					thirdProvice = true;
				} else {
					if (!isBen) {
						String toCityCode = (String) SafetyDataCenter.getInstance().getProviceList().get(position).get(Safety.CODE);
						SafetyDataCenter.getInstance().setThirdCityList(SafetyUtils.getCountryAndCity(SafetyProductBuyMsgFillActivity.this, toCityCode));
						String toCountCode = (String) SafetyDataCenter.getInstance().getThirdCityList().get(0).get(Safety.CODE);
						SafetyDataCenter.getInstance().setThirdCountryList(SafetyUtils.getCountryAndCity(SafetyProductBuyMsgFillActivity.this, toCountCode));
						SafetyUtils.initSpinnerView(SafetyProductBuyMsgFillActivity.this, mThirdAdressCity, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getThirdCityList(), Safety.NAME));
						SafetyUtils.initSpinnerView(SafetyProductBuyMsgFillActivity.this, mThirdAdressCounty, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getThirdCountryList(), Safety.NAME));
					}
				}
				break;

			case R.id.third_adress_city:// 初始化城区
				if (!fromListFlag && !thirdCity) {
					thirdCity = true;
				} else {
					if (!isBen) {
						String ttCityCode = (String) SafetyDataCenter.getInstance().getThirdCityList().get(position).get(Safety.CODE);
						SafetyDataCenter.getInstance().setThirdCountryList(SafetyUtils.getCountryAndCity(SafetyProductBuyMsgFillActivity.this, ttCityCode));
						SafetyUtils.initSpinnerView(SafetyProductBuyMsgFillActivity.this, mThirdAdressCounty, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getThirdCountryList(), Safety.NAME));
					}
				}
				break;

			case R.id.second_adress_fw_prov:// 初始化城市
				if (!fromListFlag && !thirdProviceH) {
					thirdProviceH = true;
				} else {
					String tofCityCode = (String) SafetyDataCenter.getInstance().getProviceListHS().get(position).get(Safety.CODE);
					SafetyDataCenter.getInstance().setThirdCityListHS(SafetyUtils.getCountryAndCity(SafetyProductBuyMsgFillActivity.this, tofCityCode));
					String tofCountryCode = (String) SafetyDataCenter.getInstance().getThirdCityListHS().get(0).get(Safety.CODE);
					SafetyDataCenter.getInstance().setThirdCountryListHS(SafetyUtils.getCountryAndCity(SafetyProductBuyMsgFillActivity.this, tofCountryCode));
					SafetyUtils.initSpinnerView(SafetyProductBuyMsgFillActivity.this, mThirdAdressFWCity, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getThirdCityListHS(), Safety.NAME));
					SafetyUtils.initSpinnerView(SafetyProductBuyMsgFillActivity.this, mThirdAdressFWCounty, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getThirdCountryListHS(), Safety.NAME));
				}
				break;

			case R.id.second_adress_fw_city:// 初始化城区
				if (!fromListFlag && !thirdCityH) {
					thirdCityH = true;
				} else {
					String ttfCityCode = (String) SafetyDataCenter.getInstance().getThirdCityListHS().get(position).get(Safety.CODE);
					SafetyDataCenter.getInstance().setThirdCountryListHS(SafetyUtils.getCountryAndCity(SafetyProductBuyMsgFillActivity.this, ttfCityCode));
					SafetyUtils.initSpinnerView(SafetyProductBuyMsgFillActivity.this, mThirdAdressFWCounty, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getThirdCountryListHS(), Safety.NAME));
				}
				break;

			case R.id.second_buyerguanxi:
				if (!relation) {
					relation = true;
					break;
				}
				if (relation) {
					if (mSecodBuyerGX.getSelectedItemPosition() != 0) {
						setViewClickable(true);
						restoreView2Editable();
						isBen = false;
						break;
					}
					isBen = true;
				}
				break;

			case R.id.second_idtype:
				if (!benIdType) {
					benIdType = true;
				} else {
					if (!isBen) {
						mThirdIdNum.setText("");
					}
				}
				if (mThirdIdType.getSelectedItemPosition() == 0) {
					mThirdIdNum.setFilters(new InputFilter[] { new InputFilter.LengthFilter(18) });
				} else {
					mThirdIdNum.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1000) });
				}
				break;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};

	private OnPageChangeListener mViewPageListener = new OnPageChangeListener() {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			pageIndex = arg0;
			controlStep(arg0);
			if (pageIndex == 0) {
				mBtnLast.setVisibility(View.GONE);
			} else {
				mBtnLast.setVisibility(View.VISIBLE);
			}
			if (arg0 == 2) {
				initDataForBen();
			}
		}

	};

	/**
	 * 重新设置当前页面ViewPager显示的选中项
	 * 
	 * @param nIndex
	 */
	private void resetViewPagerIndex(int nIndex) {
		mViewPager.setCurrentItem(nIndex);
	}

	private boolean simpleCheckFirstView() {
		// 校验保险起期时间
		if (!SafetyUtils.checkTime(SafetyDataCenter.getInstance().getSysTime(), mFirstInsurStartDate.getText().toString(), getString(R.string.safety_insu_time))) {
			RegexpBean result = new RegexpBean(null, null, null, false, mFirstInsurStartDate);
			doresult(result);
			return false;
		}
		return true;
	}

	/**
	 * 第二页数据校验
	 */
	private boolean simpleCheckTwoView(boolean required) {
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		// 投保人姓名
		if (onlyRegular(required, mSecondName.getText().toString())) {
			RegexpBean buyerName = new RegexpBean(SafetyConstant.NAME_BUYER, mSecondName.getText().toString(), SafetyConstant.NAME);
			buyerName.setExtentParam(mSecondName);
			lists.add(buyerName);
		}
		// 投保人身份证
		String AppIdType = mSecodIdType.getText().toString();
		if (AppIdType.equals(SafetyDataCenter.credTypeList.get(0))) {
			if (onlyRegular(required, mSecodIdNum.getText().toString())) {
				RegexpBean buyerIdType = new RegexpBean(SafetyConstant.IDENTITYNUM_BUYER, mSecodIdNum.getText().toString(), SafetyConstant.IDENTITYNUM);
				buyerIdType.setExtentParam(mSecodIdNum);
				lists.add(buyerIdType);
			}
		} else {
			if (onlyRegular(required, mSecodIdNum.getText().toString())) {
				RegexpBean buyerIdType = new RegexpBean(SafetyConstant.IDENTITYNUM_BUYER, mSecodIdNum.getText().toString(), "", false);
				buyerIdType.setExtentParam(mSecodIdNum);
				lists.add(buyerIdType);
			}
		}
		// 投保人地址
		if (onlyRegular(required, mSecodAdressOther.getText().toString().trim())) {
			RegexpBean buyerAdress = new RegexpBean(SafetyConstant.ADRESS_BUYER, mSecodAdressOther.getText().toString().trim(), SafetyConstant.ADRESS);
			buyerAdress.setExtentParam(mSecodAdressOther);
			lists.add(buyerAdress);
		}
		// 投保人邮编
		if (onlyRegular(required, mSecodPost.getText().toString().trim())) {
			RegexpBean buyerPost = new RegexpBean(SafetyConstant.POST_BUYER, mSecodPost.getText().toString().trim(), SafetyConstant.POST);
			buyerPost.setExtentParam(mSecodPost);
			lists.add(buyerPost);
		}
		// 投保人手机号
		if (onlyRegular(required, mSecodPhone.getText().toString())) {
			RegexpBean buyerPhone = new RegexpBean(SafetyConstant.PHONE_BUYER, mSecodPhone.getText().toString(), SafetyConstant.MOBILEPHONE);
			buyerPhone.setExtentParam(mSecodPhone);
			lists.add(buyerPhone);
		}
		// 投保人电子邮箱
		if (onlyRegular(required, mSecodEmail.getText().toString().trim())) {
			RegexpBean buyerEmail = new RegexpBean(SafetyConstant.EMAIL_BUYER, mSecodEmail.getText().toString().trim(), SafetyConstant.EMAIL);
			buyerEmail.setExtentParam(mSecodEmail);
			lists.add(buyerEmail);
		}
		RegexpBean result = RegexpUtils.regexpDateWithRgexpBean(lists, null);
		if (result != null) {
			doresult(result);
		}
		return result == null;
	}

	private boolean simpleCheckThreeView(boolean required) {
		if (checkBenIdType(required) == false)
			return false;
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		// 被保人姓名
		if (onlyRegular(required, mThirdName.getText().toString().trim())) {
			RegexpBean holdName = new RegexpBean(SafetyConstant.NAME_HOLD, mThirdName.getText().toString().trim(), SafetyConstant.NAME);
			holdName.setExtentParam(mThirdName);
			lists.add(holdName);
		}
		if (!QueryDateUtils.compareDate(mThirdBirthday.getText().toString(), SafetyDataCenter.getInstance().getSysTime())) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("被保人出生日期不能晚于系统当前日期");
			RegexpBean result = new RegexpBean(null, null, null, false, mThirdBirthday);
			doresult(result);
			return false;
		}
		// 被保人地址
		if (onlyRegular(required, mThirdAdressOther.getText().toString())) {
			RegexpBean holdAdress = new RegexpBean(SafetyConstant.ADRESS_HOLD, mThirdAdressOther.getText().toString(), SafetyConstant.ADRESS);
			holdAdress.setExtentParam(mThirdAdressOther);
			lists.add(holdAdress);
		}
		// 被保人邮编
		if (onlyRegular(required, mThirdPost.getText().toString().trim())) {
			RegexpBean holdPost = new RegexpBean(SafetyConstant.POST_HOLD, mThirdPost.getText().toString().trim(), SafetyConstant.POST);
			holdPost.setExtentParam(mThirdPost);
			lists.add(holdPost);
		}
		// 被保人手机号
		if (onlyRegular(required, mThirdPhone.getText().toString().trim())) {
			RegexpBean holdPhone = new RegexpBean(SafetyConstant.PHONE_HOLD, mThirdPhone.getText().toString().trim(), SafetyConstant.MOBILEPHONE);
			holdPhone.setExtentParam(mThirdPhone);
			lists.add(holdPhone);
		}
		// 被保人电子邮箱
		if (onlyRegular(required, mThirdEmail.getText().toString().trim())) {
			RegexpBean holdEmail = new RegexpBean(SafetyConstant.EMAIL_HOLD, mThirdEmail.getText().toString().trim(), SafetyConstant.EMAIL);
			holdEmail.setExtentParam(mThirdEmail);
			lists.add(holdEmail);
		}
		// 房屋地址
		if (SafetyUtils.isHouseType(productRisktype)) {
			if (onlyRegular(required, mThirdAdressFWOther.getText().toString().trim())) {
				RegexpBean fapiaoAdress = new RegexpBean(SafetyConstant.HSADRESS_HOLD, mThirdAdressFWOther.getText().toString().trim(), SafetyConstant.ADRESS);
				fapiaoAdress.setExtentParam(mThirdAdressFWOther);
				lists.add(fapiaoAdress);
			}
		}

		RegexpBean result = RegexpUtils.regexpDateWithRgexpBean(lists, null);
		if (result != null) {
			doresult(result);
			return false;
		}

		if (checkDestination()) {
			String destinationInfo = mThirdAdressMudi.getText().toString();
//			if (destinationInfo.equals(getString(R.string.safety_counrtychoose_tip))) {
			if (StringUtil.isNull(destinationInfo)) {
				if (onlyRegular(required, "")) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(SafetyConstant.MDADRESS_BUYER);
					RegexpBean mudidi = new RegexpBean(null, null, null);
					mudidi.setExtentParam(mThirdAdressMudi);
					doresult(mudidi);
//					mThirdAdressMudi.setBackground(getResources().getDrawable(R.drawable.bg_for_dittext_red));
					return false;
				}
			}
		}

		return true;
	}

	private boolean simpleCheckFourView(boolean required) {
		if (mFourthBtn1.isChecked()) {
			return checkFaPiao(required);
		}
		return true;
	}

	private void doresult(RegexpBean result) {
		if (result == null || result.getExtentParam() == null)
			return;
		final View view = (View) result.getExtentParam();
		ViewUtils.scrollShow(view);
		if (view instanceof EditText) {
			view.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_for_dittext_red));
			((EditText) view).addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
					view.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_for_edittext));
				}
			});
		} else if (view instanceof Spinner) {
			view.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner_red));
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					view.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner));
				}
			});
		} else if (view instanceof TextView) {
			// 生日 spinner 样式
			if (view.getId() == R.id.first_startdate || view.getId() == R.id.second_birthday || view.getId() == R.id.second_birthday) {
				view.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner_red));
				((TextView) view).addTextChangedListener(new TextWatcher() {
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						// TODO Auto-generated method stub

					}

					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
						// TODO Auto-generated method stub

					}

					@Override
					public void afterTextChanged(Editable s) {
						view.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner));

					}
				});
			} else {
				view.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_for_dittext_red));
				((TextView) view).addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						// TODO Auto-generated method stub

					}

					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
						// TODO Auto-generated method stub

					}

					@Override
					public void afterTextChanged(Editable s) {
						view.setBackgroundDrawable(null);

					}
				});
			}
		}

	}

	/** 提交校验 */
	private boolean submitCheck(boolean required) {
		if (!simpleCheckFirstView()) {
			resetViewPagerIndex(0);
			return false;
		}
		if (!simpleCheckTwoView(required)) {
			resetViewPagerIndex(1);
			return false;
		}
		if (!simpleCheckThreeView(required)) {
			resetViewPagerIndex(2);
			return false;
		}
		if (!simpleCheckFourView(required)) {
			resetViewPagerIndex(3);
			return false;
		}
		// 校验时间
		// if
		// (!SafetyUtils.checkTime(SafetyDataCenter.getInstance().getSysTime(),
		// mFirstInsurStartDate.getText().toString(),getString(R.string.safety_insu_time)))
		// {
		// resetViewPagerIndex(0);
		// return false;
		// }
		// ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		// //投保人姓名
		// if (onlyRegular(required, mSecondName.getText().toString())) {
		// RegexpBean buyerName = new RegexpBean(SafetyConstant.NAME_BUYER,
		// mSecondName.getText().toString(), SafetyConstant.NAME);
		// lists.add(buyerName);
		// }
		// //投保人身份证
		// String AppIdType = mSecodIdType.getText().toString();
		// if (AppIdType.equals(SafetyDataCenter.credTypeList.get(0))) {
		// if (onlyRegular(required, mSecodIdNum.getText().toString())) {
		// RegexpBean buyerIdType = new
		// RegexpBean(SafetyConstant.IDENTITYNUM_BUYER,
		// mSecodIdNum.getText().toString(), SafetyConstant.IDENTITYNUM);
		// lists.add(buyerIdType);
		// }
		// }else{
		// if (onlyRegular(required, mSecodIdNum.getText().toString())) {
		// RegexpBean buyerIdType = new
		// RegexpBean(SafetyConstant.IDENTITYNUM_BUYER,
		// mSecodIdNum.getText().toString(), "",false);
		// lists.add(buyerIdType);
		// }
		// }
		// //投保人地址
		// if (onlyRegular(required,
		// mSecodAdressOther.getText().toString().trim())) {
		// RegexpBean buyerAdress = new RegexpBean(SafetyConstant.ADRESS_BUYER,
		// mSecodAdressOther.getText().toString().trim(),
		// SafetyConstant.ADRESS);
		// lists.add(buyerAdress);
		// }
		// //投保人邮编
		// if (onlyRegular(required, mSecodPost.getText().toString().trim())) {
		// RegexpBean buyerPost = new RegexpBean(SafetyConstant.POST_BUYER,
		// mSecodPost.getText().toString().trim(), SafetyConstant.POST);
		// lists.add(buyerPost);
		// }
		// //投保人手机号
		// if (onlyRegular(required, mSecodPhone.getText().toString())) {
		// RegexpBean buyerPhone = new RegexpBean(SafetyConstant.PHONE_BUYER,
		// mSecodPhone.getText().toString(), SafetyConstant.MOBILEPHONE);
		// lists.add(buyerPhone);
		// }
		// //投保人电子邮箱
		// if (onlyRegular(required, mSecodEmail.getText().toString().trim())) {
		// RegexpBean buyerEmail = new RegexpBean(SafetyConstant.EMAIL_BUYER,
		// mSecodEmail.getText().toString().trim(), SafetyConstant.EMAIL);
		// lists.add(buyerEmail);
		// }
		// //被保人姓名
		// if (onlyRegular(required, mThirdName.getText().toString().trim())) {
		// RegexpBean holdName = new RegexpBean(SafetyConstant.NAME_HOLD,
		// mThirdName.getText().toString().trim(), SafetyConstant.NAME);
		// lists.add(holdName);
		// }
		// if (RegexpUtils.regexpDate(lists)) {
		// return checkThirdBirthday(required);
		// }
		return true;
	}
//
//	private boolean checkSubmitDestination(boolean required) {
//		// 前往目的地
//		String destinationInfo = mThirdAdressMudi.getText().toString();
//		if (destinationInfo.equals(getString(R.string.safety_counrtychoose_tip))) {
//			if (onlyRegular(required, "")) {
//				BaseDroidApp.getInstanse().showInfoMessageDialog(SafetyConstant.MDADRESS_BUYER);
//				mThirdAdressMudi.setBackground(getResources().getDrawable(R.drawable.bg_for_dittext_red));
//				return false;
//			}
//		}
//		// if (mFourthBtn1.isChecked()) {
//		// return checkFaPiao(required);
//		// }
//		return true;
//	}

	private boolean checkFaPiao(boolean required) {
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		// 发票抬头
		if (onlyRegular(required, mFourthInvoiceTitle.getText().toString().trim())) {
			RegexpBean fapiaoTitle = new RegexpBean(SafetyConstant.FAPIAO_TITLE, mFourthInvoiceTitle.getText().toString().trim(), SafetyConstant.FAPIAOTITLE);
			fapiaoTitle.setExtentParam(mFourthInvoiceTitle);
			lists.add(fapiaoTitle);
		}
		// 发票地址
		if (onlyRegular(required, mFourthInvoiceAdress.getText().toString().trim())) {
			RegexpBean fapiaoAdress = new RegexpBean(SafetyConstant.FAPIAO_ADRESS, mFourthInvoiceAdress.getText().toString().trim(), SafetyConstant.ADRESS);
			fapiaoAdress.setExtentParam(mFourthInvoiceAdress);
			lists.add(fapiaoAdress);
		}
		// 发票邮编
		if (onlyRegular(required, mFourthInvoicePost.getText().toString().trim())) {
			RegexpBean fapiaoPost = new RegexpBean(SafetyConstant.FAPIAO_POST, mFourthInvoicePost.getText().toString().trim(), SafetyConstant.POST);
			fapiaoPost.setExtentParam(mFourthInvoicePost);
			lists.add(fapiaoPost);
		}
		// 收件人姓名
		if (onlyRegular(required, mFourthInvoiceHoldName.getText().toString().trim())) {
			RegexpBean fapiaoName = new RegexpBean(SafetyConstant.FAPIAO_NAME, mFourthInvoiceHoldName.getText().toString().trim(), SafetyConstant.NAME);
			fapiaoName.setExtentParam(mFourthInvoiceHoldName);
			lists.add(fapiaoName);
		}
		// 收件人手机号
		if (onlyRegular(required, mFourthInvoiceHoldPhone.getText().toString().trim())) {
			RegexpBean fapiaoPhone = new RegexpBean(SafetyConstant.FAPIAO_PHONE, mFourthInvoiceHoldPhone.getText().toString().trim(), SafetyConstant.MOBILEPHONE);
			fapiaoPhone.setExtentParam(mFourthInvoiceHoldPhone);
			lists.add(fapiaoPhone);
		}

		RegexpBean result = RegexpUtils.regexpDateWithRgexpBean(lists, null);
		if (result != null) {
			doresult(result);
		}
		return result == null;

		// if (RegexpUtils.regexpDate(lists)) {
		// return true;
		// }
		// return false;
	}

	private boolean checkReferrerName() {
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		String referrerName = mFourthReferrerName.getText().toString().trim();
		if (!StringUtil.isNull(referrerName)) {
			RegexpBean referrer = new RegexpBean(SafetyConstant.REFERRERNAME, referrerName, SafetyConstant.NAME);
			lists.add(referrer);
			if (RegexpUtils.regexpDate(lists)) {
				return true;
			}
			return false;
		}
		return true;
	}

	/** 被保人身份证检验 */
	private boolean checkBenIdType(boolean required) {
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		// 被保人身份证
		String benIdType = mThirdIdType.getSelectedItem().toString();
		if (benIdType.equals(SafetyDataCenter.credTypeList.get(0))) {
			if (onlyRegular(required, mThirdIdNum.getText().toString().trim())) {
				RegexpBean holdIdType = new RegexpBean(SafetyConstant.IDENTITYNUM_HOLD, mThirdIdNum.getText().toString().trim(), SafetyConstant.IDENTITYNUM);
				holdIdType.setExtentParam(mThirdIdNum);
				lists.add(holdIdType);

			}
		} else {
			if (onlyRegular(required, mThirdIdNum.getText().toString().trim())) {
				RegexpBean buyerIdType = new RegexpBean(SafetyConstant.IDENTITYNUM_HOLD, mThirdIdNum.getText().toString().trim(), "", false);
				buyerIdType.setExtentParam(mThirdIdNum);
				lists.add(buyerIdType);
			}
		}

		RegexpBean result = RegexpUtils.regexpDateWithRgexpBean(lists, null);
		if (result != null) {
			doresult(result);
			return false;
		} else {
			return checkBenInfo(required);
		}
		// if (RegexpUtils.regexpDate(lists)) {
		//
		// }
		// return false;
	}

	/** 被保人出生日期检验 */
	// private boolean checkThirdBirthday(boolean required) {
	// if (!QueryDateUtils.compareDate(mThirdBirthday.getText().toString(),
	// SafetyDataCenter.getInstance().getSysTime())) {
	// BaseDroidApp.getInstanse().showInfoMessageDialog("被保人出生日期不能晚于系统当前日期");
	// return false;
	// }
	// return checkBenIdType(required);
	// }

	/** 被保人性别，生日校验 */
	private boolean checkBenInfo(boolean required) {
		if (mThirdIdType.getSelectedItemPosition() == 0) {
			if (!isBen) {
				String benIdNum = mThirdIdNum.getText().toString().trim();
				if (!StringUtil.isNull(benIdNum)) {
					String gender = SafetyUtils.getGender(benIdNum);
					String birthday = SafetyUtils.getBirthday(benIdNum);
					if ((gender.equals("1") && !mThirdGenderBtn1.isChecked()) || (gender.equals("0") && !mThirdGenderBtn2.isChecked())) {
						BaseDroidApp.getInstanse().showInfoMessageDialog("被保人性别同身份证不匹配，请修改");
						RegexpBean result = new RegexpBean(null, null, null);
						result.setExtentParam(mThirdIdNum);
						resetViewPagerIndex(2);
						doresult(result);
						// 暂为处理 sunh
						return false;
					}
					if (!birthday.equals(mThirdBirthday.getText().toString().trim())) {
						BaseDroidApp.getInstanse().showInfoMessageDialog("被保人出生日期同身份证不一致，请修改");
						RegexpBean result = new RegexpBean(null, null, null);
						result.setExtentParam(mThirdBirthday);
						doresult(result);
						resetViewPagerIndex(2);
						// 暂为处理 sunh
						return false;
					}
				}
			}
		}
		return true;
//		return checkBenOtherInfo(required);
	}
//
//	private boolean checkBenOtherInfo(boolean required) {
//		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
//		// 被保人地址
//		if (onlyRegular(required, mThirdAdressOther.getText().toString())) {
//			RegexpBean holdAdress = new RegexpBean(SafetyConstant.ADRESS_HOLD, mThirdAdressOther.getText().toString(), SafetyConstant.ADRESS);
//			holdAdress.setExtentParam(mThirdAdressOther);
//			lists.add(holdAdress);
//		}
//		// 被保人邮编
//		if (onlyRegular(required, mThirdPost.getText().toString().trim())) {
//			RegexpBean holdPost = new RegexpBean(SafetyConstant.POST_HOLD, mThirdPost.getText().toString().trim(), SafetyConstant.POST);
//			holdPost.setExtentParam(mThirdPost);
//			lists.add(holdPost);
//		}
//		// 被保人手机号
//		if (onlyRegular(required, mThirdPhone.getText().toString().trim())) {
//			RegexpBean holdPhone = new RegexpBean(SafetyConstant.PHONE_HOLD, mThirdPhone.getText().toString().trim(), SafetyConstant.MOBILEPHONE);
//			holdPhone.setExtentParam(mThirdPhone);
//			lists.add(holdPhone);
//		}
//		// 被保人电子邮箱
//		if (onlyRegular(required, mThirdEmail.getText().toString().trim())) {
//			RegexpBean holdEmail = new RegexpBean(SafetyConstant.EMAIL_HOLD, mThirdEmail.getText().toString().trim(), SafetyConstant.EMAIL);
//			holdEmail.setExtentParam(mThirdEmail);
//			lists.add(holdEmail);
//		}
//		// 房屋地址
//		if (SafetyUtils.isHouseType(productRisktype)) {
//			if (onlyRegular(required, mThirdAdressFWOther.getText().toString().trim())) {
//				RegexpBean fapiaoAdress = new RegexpBean(SafetyConstant.HSADRESS_HOLD, mThirdAdressFWOther.getText().toString().trim(), SafetyConstant.ADRESS);
//				fapiaoAdress.setExtentParam(mThirdAdressFWOther);
//				lists.add(fapiaoAdress);
//			}
//		}
//
//		RegexpBean result = RegexpUtils.regexpDateWithRgexpBean(lists, null);
//		if (result != null) {
//			doresult(result);
//			return false;
//		} else {
//
//			if (checkDestination()) {
//				return checkSubmitDestination(required);
//			}
//			// if (mFourthBtn1.isChecked()) {
//			// return checkFaPiao(required);
//			// }
//			return true;
//
//		}
//		// if (RegexpUtils.regexpDate(lists)) {
//		// if (checkDestination()) {
//		// return checkSubmitDestination(required);
//		// }
//		// if (mFourthBtn1.isChecked()) {
//		// return checkFaPiao(required);
//		// }
//		// return true;
//		// }
//		// return false;
//	}

	/** 只作正则校验 */
	private boolean onlyRegular(Boolean required, String content) {
		if ((!required && !StringUtil.isNull(content)) || required) {
			return true;
		}
		return false;
	}

	/** 投保人与被保人是本人 */
	private void initDataForBen() {
		if (SafetyUtils.isHouseType(productRisktype) && isBen || !SafetyUtils.isHouseType(productRisktype) && isBen) {
			SafetyUtils.initSpinnerView(this, mThirdAdressCity, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getSecondCityList(), Safety.NAME));
			SafetyUtils.initSpinnerView(this, mThirdAdressCounty, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getSecondCountryList(), Safety.NAME));
			mThirdName.setText(mSecondName.getText().toString().trim());
			mThirdGenderBtn1.setChecked(mSecondGenderBtn1.isChecked());
			mThirdGenderBtn1.setClickable(false);
			mThirdGenderBtn2.setClickable(false);
			mThirdGenderBtn2.setChecked(mSecondGenderBtn2.isChecked());
			mThirdBirthday.setText(mSecodBirthday.getText().toString());
			mThirdIdType.setSelection(initData(SafetyDataCenter.credTypeList, mSecodIdType.getText().toString()));
			mThirdIdNum.setText(mSecodIdNum.getText().toString().trim());
			// mThirdIdDate.setText(mSecodIdDate.getText().toString());
			// mThirdMrigStatu.setSelection(mSecodMrigStatu.getSelectedItemPosition());
			mThirdAdressProv.setSelection(mSecodAdressProv.getSelectedItemPosition());
			mThirdAdressCity.setSelection(mSecodAdressCity.getSelectedItemPosition());
			mThirdAdressCounty.setSelection(mSecodAdressCounty.getSelectedItemPosition());
			mThirdAdressOther.setText(mSecodAdressOther.getText().toString().trim());
			mThirdPost.setText(mSecodPost.getText().toString().trim());
			mThirdPhone.setText(mSecodPhone.getText().toString().trim());
			mThirdEmail.setText(mSecodEmail.getText().toString().trim());
			SafetyUtils.setSpinnerBackground(mThirdBirthday, false);
			SafetyUtils.setSpinnerBackground(mThirdIdType, false);
			SafetyUtils.setSpinnerBackground(mThirdAdressProv, false);
			SafetyUtils.setSpinnerBackground(mThirdAdressCity, false);
			SafetyUtils.setSpinnerBackground(mThirdAdressCounty, false);
			setViewClickable(false);
		}
	}

	/** 设置View 2 可编辑状态 */
	private void restoreView2Editable() {
		SafetyUtils.initSpinnerView(this, mThirdAdressCity, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getThirdCityList(), Safety.NAME));
		SafetyUtils.initSpinnerView(this, mThirdAdressCounty, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getThirdCountryList(), Safety.NAME));
		LogGloble.i(TAG, "ThirdCityList" + SafetyDataCenter.getInstance().getThirdCityList());
		LogGloble.i(TAG, "ThirdCountryList" + SafetyDataCenter.getInstance().getThirdCountryList());
		mThirdName.setFocusableInTouchMode(true);
		mThirdName.setText("");
		mThirdGenderBtn1.setChecked(true);
		mThirdBirthday.setText(QueryDateUtils.getcurrentDate(SafetyDataCenter.getInstance().getSysTime()));
		mThirdIdType.setSelection(0);
		mThirdIdNum.setFocusableInTouchMode(true);
		mThirdIdNum.setText("");
		mThirdAdressProv.setSelection(0);
		mThirdAdressCity.setSelection(0);
		mThirdAdressCounty.setSelection(0);
		mThirdAdressOther.setFocusableInTouchMode(true);
		mThirdPost.setFocusableInTouchMode(true);
		mThirdPhone.setFocusableInTouchMode(true);
		mThirdEmail.setFocusableInTouchMode(true);
		mThirdAdressOther.setText("");
		mThirdPost.setText("");
		mThirdPhone.setText("");
		mThirdEmail.setText("");
		SafetyUtils.setSpinnerBackground(mThirdBirthday, true);
		SafetyUtils.setSpinnerBackground(mThirdIdType, true);
		SafetyUtils.setSpinnerBackground(mThirdAdressProv, true);
		SafetyUtils.setSpinnerBackground(mThirdAdressCity, true);
		SafetyUtils.setSpinnerBackground(mThirdAdressCounty, true);
	}

	/** 设置View 2 可点击状态 */
	private void setViewClickable(boolean isab) {
		mThirdName.setFocusable(isab);
		mThirdBirthday.setClickable(isab);
		mThirdIdType.setClickable(isab);
		mThirdIdNum.setFocusable(isab);
		mThirdAdressProv.setClickable(isab);
		mThirdAdressCity.setClickable(isab);
		mThirdAdressCounty.setClickable(isab);
		mThirdAdressOther.setFocusable(isab);
		mThirdPost.setFocusable(isab);
		mThirdPhone.setFocusable(isab);
		mThirdEmail.setFocusable(isab);
		mThirdGenderBtn1.setClickable(isab);
		mThirdGenderBtn2.setClickable(isab);
	}

	private int initData(List<String> list, String applIdType) {
		for (int i = 0; i < SafetyDataCenter.credTypeList.size(); i++) {
			if (applIdType.equals(SafetyDataCenter.credTypeList.get(i))) {
				return i;
			}
		}
		return 0;
	}

	/** 显示前往目的地 */
	private boolean checkDestination() {
		for (int i = 0; i < SafetyDataCenter.destination.size(); i++) {
			if (insuCode.equals(SafetyDataCenter.destination.get(i))) {
				return true;
			}
		}
		return false;
	}

	/** 返回事件 */
	private OnClickListener backOnclick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			controlView();
		}
	};

	/** 目的地选择页 */
	private OnClickListener toMDDOnclick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// mThirdAdressMudi.setBackground(null);
			Intent mIntent = new Intent(SafetyProductBuyMsgFillActivity.this, SafetyCountyChooseActivity.class);
			mIntent.putExtra(SafetyConstant.LASTSELECTED, mThirdAdressMudi.getText().toString());
			SafetyProductBuyMsgFillActivity.this.startActivityForResult(mIntent, 100);
			overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
		}
	};

	/** 页面切换 */
	private void controlView() {
		if (isFillView) {
			finish();
			return;
		}
		isFillView = true;
		mMainView.findViewById(R.id.layout_step).setVisibility(View.VISIBLE);
		setTitle(R.string.safety_msgfill_title);
		mViewPager.setVisibility(View.VISIBLE);
		mFillBtnLayout.setVisibility(View.VISIBLE);
		mMainLayout.removeAllViews();
		mMainLayoutw.setVisibility(View.GONE);
		mConfirmBtnLayout.setVisibility(View.GONE);
	}

	/** 反显上传保存的国家信息 */
	private String showDestinationInfo() {
		String destination = (String) SafetyDataCenter.getInstance().getProductInfoMap().get(Safety.DESINATION);
		if (!StringUtil.isNull(destination)) {
			List<Map<String, Object>> mList = SafetyUtils.getCountry(this);
			StringBuffer b = new StringBuffer();
			for (int i = 0; i < mList.size(); i++) {
				if (destination.contains((String) mList.get(i).get(Safety.CODE))) {
					b.append((String) mList.get(i).get(Safety.NAME));
					b.append(",");
				}
			}
			mThirdAdressMudi.setTextColor(getResources().getColor(R.color.black));
			if (StringUtil.isNull(b.toString())) {
				return getString(R.string.safety_counrtychoose_tip);
			}
			return (b.toString()).substring(0, b.toString().length() - 1);
		}
		mThirdAdressMudi.setTextColor(getResources().getColor(R.color.gray));
		return getString(R.string.safety_counrtychoose_tip);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1001) {
			String destination = SafetyUtils.getDestinationInfo(Safety.NAME);
			if (StringUtil.isNullOrEmpty(destination)) {
				mThirdAdressMudi.setTextColor(getResources().getColor(R.color.gray));
				mThirdAdressMudi.setText(getString(R.string.safety_counrtychoose_tip));
				return;
			}
			mThirdAdressMudi.setTextColor(getResources().getColor(R.color.black));
			mThirdAdressMudi.setText(destination);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			controlView();
		}
		return true;
	}

	private void crearData() {
		if (!StringUtil.isNullOrEmpty(SafetyDataCenter.getInstance().getCountyList())) {
			SafetyDataCenter.getInstance().getCountyList().clear();
		}
	}

	/** 相同字段公共封装 */
	private void initRqData(Map<String, Object> param) {
		param.put(Safety.TRANTYPE_FLAG, getTranType(fromListFlag, productRisktype));
		param.put(Safety.INSURANCE_COMANY, companyName);
		param.put(Safety.SUBINSUNAME, subCompanyName);
		param.put(Safety.INSURANCE_ID, insurId);
		param.put(Safety.SUBINSUID, subInsuId);
		param.put(Safety.SELL_NAME, mFourthReferrerName.getText().toString().trim());
		// param.put(Safety.SELL_TELLER,
		// mFirstMarketerNum.getText().toString());
		param.put(Safety.BUSIBELONG, SafetyConstant.BUSIBELONG);
		param.put(Safety.RISKNAME, productName);
		param.put(Safety.INSUCODE, insuCode);
		param.put(Safety.RISKCODE, SafetyDataCenter.getInstance().getProductInfoMap().get(Safety.RISKCODE));
		param.put(Safety.SAFETY_HOLD_RISK_UNIT, SafetyConstant.RISKUNIT);
		param.put(Safety.POLEFFDATE, SafetyUtils.getText(mFirstInsurStartDate));
		param.put(Safety.APPL_NAME, mSecondName.getText().toString());
		param.put(Safety.APPL_CTRYNO, SafetyConstant.COUNTRY);
		param.put(Safety.APPL_IDTYPE, SafetyDataCenter.rqcredType.get(initData(SafetyDataCenter.credTypeList, mSecodIdType.getText().toString())));
		param.put(Safety.APPL_IDNO, mSecodIdNum.getText().toString());
		param.put(Safety.APPL_IDVALID, SafetyConstant.IDVALID);
		param.put(Safety.ACC_ID, (SafetyDataCenter.getInstance().getAcctList().get(mFourthAcct.getSelectedItemPosition())).get(Comm.ACCOUNT_ID));
		param.put(Safety.APPL_SEX, SafetyUtils.getGenderTorq(mSecondGenderBtn1));
		param.put(Safety.APPL_BIRTH, mSecodBirthday.getText().toString());
		param.put(Safety.APPL_MARRIAGE, "");
		param.put(Safety.APPL_PHONE, mSecodPhone.getText().toString());
		param.put(Safety.APPL_PROVINCE, (SafetyDataCenter.getInstance().getProviceList().get(mSecodAdressProv.getSelectedItemPosition())).get(Safety.CODE));
		param.put(Safety.APPL_CITY, (SafetyDataCenter.getInstance().getSecondCityList().get(mSecodAdressCity.getSelectedItemPosition())).get(Safety.CODE));
		if (StringUtil.isNullOrEmpty(SafetyDataCenter.getInstance().getSecondCountryList())) {
			param.put(Safety.APPL_COUNTY, "");
		} else {
			param.put(Safety.APPL_COUNTY, (SafetyDataCenter.getInstance().getSecondCountryList().get(mSecodAdressCounty.getSelectedItemPosition())).get(Safety.CODE));
		}
		param.put(Safety.APPL_ADRESS, mSecodAdressOther.getText().toString().trim());
		param.put(Safety.APPL_POSTCODE, mSecodPost.getText().toString().trim());
		param.put(Safety.APPL_EMAIL, mSecodEmail.getText().toString().trim());
		param.put(Safety.BEN_NAME, mThirdName.getText().toString().trim());
		param.put(Safety.BEN_CTRYNO, SafetyConstant.COUNTRY);
		param.put(Safety.BEN_IDTYPE, SafetyDataCenter.rqcredType.get(mThirdIdType.getSelectedItemPosition()));
		param.put(Safety.BEN_IDNUM, mThirdIdNum.getText().toString().trim());
		param.put(Safety.BEN_IDVALID, SafetyConstant.IDVALID);
		param.put(Safety.BEN_MARRAGE, "");
		param.put(Safety.BEN_SEX, SafetyUtils.getGenderTorq(mThirdGenderBtn1));
		param.put(Safety.BEN_BIRTH, mThirdBirthday.getText().toString());
		param.put(Safety.BEN_PHONE, mThirdPhone.getText().toString().trim());
		param.put(Safety.BEN_EMAIL, mThirdEmail.getText().toString().trim());
		param.put(Safety.BEN_PROVINCE, (SafetyDataCenter.getInstance().getProviceList().get(mThirdAdressProv.getSelectedItemPosition())).get(Safety.CODE));
		if (isBen) {
			param.put(Safety.BEN_RELATION, "1");
			param.put(Safety.BEN_CITY, (SafetyDataCenter.getInstance().getSecondCityList().get(mThirdAdressCity.getSelectedItemPosition())).get(Safety.CODE));
			if (StringUtil.isNullOrEmpty(SafetyDataCenter.getInstance().getSecondCountryList())) {
				param.put(Safety.BEN_COUNTY, "");
			} else {
				param.put(Safety.BEN_COUNTY, (SafetyDataCenter.getInstance().getSecondCountryList().get(mThirdAdressCounty.getSelectedItemPosition())).get(Safety.CODE));
			}
		} else {
			param.put(Safety.BEN_RELATION, "0");
			param.put(Safety.BEN_CITY, (SafetyDataCenter.getInstance().getThirdCityList().get(mThirdAdressCity.getSelectedItemPosition())).get(Safety.CODE));
			if (StringUtil.isNullOrEmpty(SafetyDataCenter.getInstance().getThirdCountryList())) {
				param.put(Safety.BEN_COUNTY, "");
			} else {
				param.put(Safety.BEN_COUNTY, (SafetyDataCenter.getInstance().getThirdCountryList().get(mThirdAdressCounty.getSelectedItemPosition())).get(Safety.CODE));
			}
		}
		param.put(Safety.BEN_ADRESS, mThirdAdressOther.getText().toString());
		param.put(Safety.BEN_POSTCODE, mThirdPost.getText().toString().trim());
		param.put(Safety.INV_FLAG, SafetyUtils.getGenderTorq(mFourthBtn1));// fp
		if (mFourthBtn1.isChecked()) {
			param.put(Safety.INV_TITLE, mFourthInvoiceTitle.getText().toString().trim());
			param.put(Safety.INV_ADRESS, mFourthInvoiceAdress.getText().toString().trim());
			param.put(Safety.INV_POSTCODE, mFourthInvoicePost.getText().toString().trim());
			param.put(Safety.INV_NAME, mFourthInvoiceHoldName.getText().toString().trim());
			param.put(Safety.INV_PHONE, mFourthInvoiceHoldPhone.getText().toString().trim());
			param.put(Safety.INV_REMARKS, mFourthInvoiceBeizhu.getText().toString().trim());
		}
		if (!SafetyUtils.isHouseType(productRisktype)) {// 意外险
			param.put(Safety.APPL_RELATION, relationRequest());
			param.put(Safety.ITFLAG, SafetyConstant.ITFLAG);
			param.put(Safety.BEN_ITFLAG, SafetyConstant.ITFLAG);
			param.put(Safety.DESINATION, getRequestDestination(fromListFlag));
			param.put(Safety.BENIFBNFFLAG, SafetyConstant.BENIFBNFFLAG);
			param.put(Safety.BENCOUNT, SafetyConstant.BENIFBNFFLAG);
		} else {// 家财险
			param.put(Safety.APPL_RELATION, SafetyUtils.getGenderTorq(mSecodBuyerbtn1));
			int index = initData(SafetyDataCenter.houseTypeList, mThirdHouseType.getText().toString());
			param.put(Safety.HOUSETYPE, SafetyDataCenter.houseTypeListrq.get(index));
			param.put(Safety.HOUSE_PROVINCE, (SafetyDataCenter.getInstance().getProviceListHS().get(mThirdAdressFWPro.getSelectedItemPosition())).get(Safety.CODE));
			param.put(Safety.HOUSE_CITY, (SafetyDataCenter.getInstance().getThirdCityListHS().get(mThirdAdressFWCity.getSelectedItemPosition())).get(Safety.CODE));
			if (StringUtil.isNullOrEmpty(SafetyDataCenter.getInstance().getThirdCountryListHS())) {
				param.put(Safety.HOUSE_COUNTY, "");
			} else {
				param.put(Safety.HOUSE_COUNTY, (SafetyDataCenter.getInstance().getThirdCountryListHS().get(mThirdAdressFWCounty.getSelectedItemPosition())).get(Safety.CODE));
			}
			param.put(Safety.HOUSE_ADRESS, mThirdAdressFWOther.getText().toString().trim());
		}
	}

	/** 交易类型标识 */
	private String getTranType(boolean tag, String type) {
		Map<String, String> tranTypeMap = null;
		if (tag) {
			if (tranTypeMap == null) {
				tranTypeMap = new HashMap<String, String>();
			}
			tranTypeMap.put("4", "0");
			tranTypeMap.put("5", "1");
			return tranTypeMap.get(type);
		}
		return type;
	}

	/** 上传前往目的地 */
	private String getRequestDestination(boolean tag) {
		String destinationInfo = mThirdAdressMudi.getText().toString().trim();
		if (destinationInfo.equals(getString(R.string.safety_counrtychoose_tip))) {
			return "";
		}
		if (tag) {
			return SafetyUtils.getDestinationInfo(Safety.CODE);
		} else {
			if (!StringUtil.isNull((String) SafetyDataCenter.getInstance().getProductInfoMap().get(Safety.DESINATION))) {
				return (String) SafetyDataCenter.getInstance().getProductInfoMap().get(Safety.DESINATION);
			}
			return SafetyUtils.getDestinationInfo(Safety.CODE);
		}
	}

	/** 保存本地数据 */
	private void saveTempData() {
		Map<String, Object> map = SafetyDataCenter.getInstance().getProductInfoMap();
		if (map.containsKey(Safety.POLEFFDATE)) {
			map.put(Safety.POLEFFDATE, tempMap.get(Safety.POLEFFDATE));
		}
		if (map.containsKey(Safety.APPL_SEX)) {
			map.put(Safety.APPL_SEX, tempMap.get(Safety.APPL_SEX));
		}
		if (map.containsKey(Safety.APPL_BIRTH)) {
			map.put(Safety.APPL_BIRTH, tempMap.get(Safety.APPL_BIRTH));
		}
		if (map.containsKey(Safety.APPL_IDVALID)) {
			map.put(Safety.APPL_IDVALID, tempMap.get(Safety.APPL_IDVALID));
		}
		if (map.containsKey(Safety.APPL_MARRIAGE)) {
			map.put(Safety.APPL_MARRIAGE, tempMap.get(Safety.APPL_MARRIAGE));
		}
		if (map.containsKey(Safety.APPL_PROVINCE)) {
			map.put(Safety.APPL_PROVINCE, tempMap.get(Safety.APPL_PROVINCE));
		}
		if (map.containsKey(Safety.APPL_CITY)) {
			map.put(Safety.APPL_CITY, tempMap.get(Safety.APPL_CITY));
		}
		if (map.containsKey(Safety.APPL_COUNTY)) {
			map.put(Safety.APPL_COUNTY, tempMap.get(Safety.APPL_COUNTY));
		}
		if (map.containsKey(Safety.APPL_ADRESS)) {
			map.put(Safety.APPL_ADRESS, tempMap.get(Safety.APPL_ADRESS));
		}
		if (map.containsKey(Safety.APPL_POSTCODE)) {
			map.put(Safety.APPL_POSTCODE, tempMap.get(Safety.APPL_POSTCODE));
		}
		if (map.containsKey(Safety.APPL_EMAIL)) {
			map.put(Safety.APPL_EMAIL, tempMap.get(Safety.APPL_EMAIL));
		}
		if (map.containsKey(Safety.APPL_RELATION)) {
			map.put(Safety.APPL_RELATION, tempMap.get(Safety.APPL_RELATION));
		}
		if (map.containsKey(Safety.BEN_RELATION)) {
			map.put(Safety.BEN_RELATION, tempMap.get(Safety.BEN_RELATION));
		}
		if (map.containsKey(Safety.BEN_NAME)) {
			map.put(Safety.BEN_NAME, tempMap.get(Safety.BEN_NAME));
		}
		if (map.containsKey(Safety.BEN_SEX)) {
			map.put(Safety.BEN_SEX, tempMap.get(Safety.BEN_SEX));
		}
		if (map.containsKey(Safety.BEN_BIRTH)) {
			map.put(Safety.BEN_BIRTH, tempMap.get(Safety.BEN_BIRTH));
		}
		if (map.containsKey(Safety.BEN_IDTYPE)) {
			map.put(Safety.BEN_IDTYPE, tempMap.get(Safety.BEN_IDTYPE));
		}
		if (map.containsKey(Safety.BEN_IDNUM)) {
			map.put(Safety.BEN_IDNUM, tempMap.get(Safety.BEN_IDNUM));
		}
		if (map.containsKey(Safety.BEN_IDVALID)) {
			map.put(Safety.BEN_IDVALID, tempMap.get(Safety.BEN_IDVALID));
		}
		if (map.containsKey(Safety.BEN_MARRAGE)) {
			map.put(Safety.BEN_MARRAGE, tempMap.get(Safety.BEN_MARRAGE));
		}
		if (map.containsKey(Safety.BEN_PROVINCE)) {
			map.put(Safety.BEN_PROVINCE, tempMap.get(Safety.BEN_PROVINCE));
		}
		if (map.containsKey(Safety.BEN_CITY)) {
			map.put(Safety.BEN_CITY, tempMap.get(Safety.BEN_CITY));
		}
		if (map.containsKey(Safety.BEN_COUNTY)) {
			map.put(Safety.BEN_COUNTY, tempMap.get(Safety.BEN_COUNTY));
		}
		if (map.containsKey(Safety.BEN_ADRESS)) {
			map.put(Safety.BEN_ADRESS, tempMap.get(Safety.BEN_ADRESS));
		}
		if (map.containsKey(Safety.BEN_POSTCODE)) {
			map.put(Safety.BEN_POSTCODE, tempMap.get(Safety.BEN_POSTCODE));
		}
		if (map.containsKey(Safety.BEN_PHONE)) {
			map.put(Safety.BEN_PHONE, tempMap.get(Safety.BEN_PHONE));
		}
		if (map.containsKey(Safety.BEN_EMAIL)) {
			map.put(Safety.BEN_EMAIL, tempMap.get(Safety.BEN_EMAIL));
		}
		if (map.containsKey(Safety.DESINATION)) {
			map.put(Safety.DESINATION, tempMap.get(Safety.DESINATION));
		}
		if (map.containsKey(Safety.HOUSETYPE)) {
			map.put(Safety.HOUSETYPE, tempMap.get(Safety.HOUSETYPE));
		}
		if (map.containsKey(Safety.HOUSE_PROVINCE)) {
			map.put(Safety.HOUSE_PROVINCE, tempMap.get(Safety.HOUSE_PROVINCE));
		}
		if (map.containsKey(Safety.HOUSE_CITY)) {
			map.put(Safety.HOUSE_CITY, tempMap.get(Safety.HOUSE_CITY));
		}
		if (map.containsKey(Safety.HOUSE_COUNTY)) {
			map.put(Safety.HOUSE_COUNTY, tempMap.get(Safety.HOUSE_COUNTY));
		}
		if (map.containsKey(Safety.HOUSE_ADRESS)) {
			map.put(Safety.HOUSE_ADRESS, tempMap.get(Safety.HOUSE_ADRESS));
		}
		if (map.containsKey(Safety.ACC_ID)) {
			map.put(Safety.ACC_ID, tempMap.get(Safety.ACC_ID));
		}
		if (map.containsKey(Safety.INV_FLAG)) {
			map.put(Safety.INV_FLAG, tempMap.get(Safety.INV_FLAG));
		}
		if (map.containsKey(Safety.INV_TITLE)) {
			map.put(Safety.INV_TITLE, tempMap.get(Safety.INV_TITLE));
		}
		if (map.containsKey(Safety.INV_ADRESS)) {
			map.put(Safety.INV_ADRESS, tempMap.get(Safety.INV_ADRESS));
		}
		if (map.containsKey(Safety.INV_POSTCODE)) {
			map.put(Safety.INV_POSTCODE, tempMap.get(Safety.INV_POSTCODE));
		}
		if (map.containsKey(Safety.INV_NAME)) {
			map.put(Safety.INV_NAME, tempMap.get(Safety.INV_NAME));
		}
		if (map.containsKey(Safety.INV_PHONE)) {
			map.put(Safety.INV_PHONE, tempMap.get(Safety.INV_PHONE));
		}
		if (map.containsKey(Safety.INV_REMARKS)) {
			map.put(Safety.INV_REMARKS, tempMap.get(Safety.INV_REMARKS));
		}
		if (map.containsKey(Safety.SELL_NAME)) {
			map.put(Safety.SELL_NAME, tempMap.get(Safety.SELL_NAME));
		}
		SafetyDataCenter.getInstance().setProductInfoMap(map);
	}

	/** 投保人与被保人关系 */
	private void initRelation() {
		if (mSecondGenderBtn1.isChecked()) {
			SafetyUtils.initSpinnerView(this, mSecodBuyerGX, SafetyDataCenter.relation_M);
		} else if (mSecondGenderBtn2.isChecked()) {
			SafetyUtils.initSpinnerView(this, mSecodBuyerGX, SafetyDataCenter.relation_W);
		} else {
			SafetyUtils.initSpinnerView(this, mSecodBuyerGX, SafetyDataCenter.relation);
		}
	}

	/** 投保人与被保人关系 反显 */
	private void relationSelect(Map<String, Object> map) {
		String appRelation = "";
		if (map.containsKey(Safety.APPL_RELATION)) {
			appRelation = (String) map.get(Safety.APPL_RELATION);
		}
		if (!StringUtil.isNull(appRelation)) {
			if (appRelation.equals("01") || appRelation.equals("1")) {
				isBen = true;
			}
			if (SafetyUtils.isHouseType(productRisktype)) {
				SafetyUtils.settRadioButton(mSecodBuyerbtn1, mSecodBuyerbtn2, appRelation);
				return;
			}
			if (mSecondGenderBtn1.isChecked()) {
				SafetyUtils.spinnerSetText(mSecodBuyerGX, SafetyDataCenter.relationrq_M, (String) map.get(Safety.APPL_RELATION));
			} else if (mSecondGenderBtn2.isChecked()) {
				SafetyUtils.spinnerSetText(mSecodBuyerGX, SafetyDataCenter.relationrq_W, (String) map.get(Safety.APPL_RELATION));
			} else {
				SafetyUtils.spinnerSetText(mSecodBuyerGX, SafetyDataCenter.relationrq, (String) map.get(Safety.APPL_RELATION));
			}
		}
	}

	/** 获取被保人上传数据 */
	private String relationRequest() {
		String relation = "";
		if (mSecondGenderBtn1.isChecked()) {
			relation = SafetyDataCenter.relationrq_M.get(mSecodBuyerGX.getSelectedItemPosition());
		} else if (mSecondGenderBtn2.isChecked()) {
			relation = SafetyDataCenter.relationrq_W.get(mSecodBuyerGX.getSelectedItemPosition());
		} else {
			relation = SafetyDataCenter.relationrq.get(mSecodBuyerGX.getSelectedItemPosition());
		}
		return relation;
	}

	/** 步骤跳转 */
	private OnClickListener stepOnclick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.layout_step1:
				mViewPager.setCurrentItem(0);
				break;
			case R.id.layout_step2:
				mViewPager.setCurrentItem(1);
				break;
			case R.id.layout_step3:
				mViewPager.setCurrentItem(2);
				break;
			case R.id.layout_step4:
				mViewPager.setCurrentItem(3);
				break;
			}
		}
	};

	/** 控制步骤条 */
	private void controlStep(int index) {
		View v1 = mMainView.findViewById(R.id.layout_step1);
		TextView indexView1 = (TextView) mMainView.findViewById(R.id.index1);
		TextView textView1 = (TextView) mMainView.findViewById(R.id.text1);
		View v2 = mMainView.findViewById(R.id.layout_step2);
		TextView indexView2 = (TextView) mMainView.findViewById(R.id.index2);
		TextView textView2 = (TextView) mMainView.findViewById(R.id.text2);
		View v3 = mMainView.findViewById(R.id.layout_step3);
		TextView indexView3 = (TextView) mMainView.findViewById(R.id.index3);
		TextView textView3 = (TextView) mMainView.findViewById(R.id.text3);
		View v4 = mMainView.findViewById(R.id.layout_step4);
		TextView indexView4 = (TextView) mMainView.findViewById(R.id.index4);
		TextView textView4 = (TextView) mMainView.findViewById(R.id.text4);
		if (index == 0) {
			v1.setBackgroundResource(R.drawable.safety_step1);
			indexView1.setTextColor(this.getResources().getColor(R.color.red));
			textView1.setTextColor(this.getResources().getColor(R.color.red));
			v2.setBackgroundResource(R.drawable.safety_step3);
			indexView2.setTextColor(this.getResources().getColor(R.color.gray));
			textView2.setTextColor(this.getResources().getColor(R.color.gray));
			v3.setBackgroundResource(R.drawable.safety_step3);
			indexView3.setTextColor(this.getResources().getColor(R.color.gray));
			textView3.setTextColor(this.getResources().getColor(R.color.gray));
			v4.setBackgroundResource(R.drawable.safety_step4);
			indexView4.setTextColor(this.getResources().getColor(R.color.gray));
			textView4.setTextColor(this.getResources().getColor(R.color.gray));
		} else if (index == 1) {
			v1.setBackgroundResource(R.drawable.safety_step2);
			indexView1.setTextColor(this.getResources().getColor(R.color.gray));
			textView1.setTextColor(this.getResources().getColor(R.color.gray));
			v2.setBackgroundResource(R.drawable.safety_step1);
			indexView2.setTextColor(this.getResources().getColor(R.color.red));
			textView2.setTextColor(this.getResources().getColor(R.color.red));
			v3.setBackgroundResource(R.drawable.safety_step3);
			indexView3.setTextColor(this.getResources().getColor(R.color.gray));
			textView3.setTextColor(this.getResources().getColor(R.color.gray));
			v4.setBackgroundResource(R.drawable.safety_step4);
			indexView4.setTextColor(this.getResources().getColor(R.color.gray));
			textView4.setTextColor(this.getResources().getColor(R.color.gray));
		} else if (index == 2) {
			v1.setBackgroundResource(R.drawable.safety_step3);
			indexView1.setTextColor(this.getResources().getColor(R.color.gray));
			textView1.setTextColor(this.getResources().getColor(R.color.gray));
			v2.setBackgroundResource(R.drawable.safety_step2);
			indexView2.setTextColor(this.getResources().getColor(R.color.gray));
			textView2.setTextColor(this.getResources().getColor(R.color.gray));
			v3.setBackgroundResource(R.drawable.safety_step1);
			indexView3.setTextColor(this.getResources().getColor(R.color.red));
			textView3.setTextColor(this.getResources().getColor(R.color.red));
			v4.setBackgroundResource(R.drawable.safety_step4);
			indexView4.setTextColor(this.getResources().getColor(R.color.gray));
			textView4.setTextColor(this.getResources().getColor(R.color.gray));
		} else if (index == 3) {
			v1.setBackgroundResource(R.drawable.safety_step3);
			indexView1.setTextColor(this.getResources().getColor(R.color.gray));
			textView1.setTextColor(this.getResources().getColor(R.color.gray));
			v2.setBackgroundResource(R.drawable.safety_step3);
			indexView2.setTextColor(this.getResources().getColor(R.color.gray));
			textView2.setTextColor(this.getResources().getColor(R.color.gray));
			v3.setBackgroundResource(R.drawable.safety_step2);
			indexView3.setTextColor(this.getResources().getColor(R.color.gray));
			textView3.setTextColor(this.getResources().getColor(R.color.gray));
			v4.setBackgroundResource(R.drawable.safety_step5);
			indexView4.setTextColor(this.getResources().getColor(R.color.red));
			textView4.setTextColor(this.getResources().getColor(R.color.red));
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && isShowguid) {
			isShowguid = false;
			showCardPriceGuid(ConstantGloble.SAFETYGUIDE);
		}
	}

	/**
	 * 保单别名验证
	 * 
	 * @param alias
	 */
	private void insurAliasCheck(String alias) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Safety.METHOD_ALIAS_CHECK);
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put(Safety.ALIAS_ID, alias);
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "insuranceAliasCheckCallBack");
	}

	/** 保单别名验证返回 */
	public void insuranceAliasCheckCallBack(Object resultObj) {
		String isexist = HttpTools.getResponseResult(resultObj);
		if (isexist.equals("true")) {
			BaseHttpEngine.dissMissProgressDialog();
			CustomDialog.toastInCenter(this, getString(R.string.safety_savename_tip));
			return;
		}
		isSave = true;
		requestCommConversationId();
	}

	/** 保单暂存 */
	private void insurAliasSave(String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Safety.METHOD_ALIAS_SAVE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put(Safety.TOKEN, token);
		param.put(Safety.ALIAS_ID, alias);
		param.put(Safety.SAVE_DATE, QueryDateUtils.getcurrentDate(SafetyDataCenter.getInstance().getSysTime()));
		initRqData(param);
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "insuranceAliasSaveCallBack");
	}

	/** 保单保存返回 */
	public void insuranceAliasSaveCallBack(Object resultObj) {
		BaseDroidApp.getInstanse().dismissErrorDialog();
		BaseHttpEngine.dissMissProgressDialog();
		saveOK = true;
		isSave = false;
		CustomDialog.toastShow(this, getString(R.string.safety_save_tip));
	}

	/** conversationid回调 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		if (isChange || isSave) {
			// 获取token请求
			BaseHttpEngine.showProgressDialog();
			requestPSNGetTokenId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
			return;
		}
		requestInsurCount();
	}

	/** 保费计算请求 --分家财型、意外型 */
	private void requestInsurCount() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(SafetyDataCenter.method.get(productRisktype));
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put(Safety.APPLCUSTID, cifId);
		initRqData(param);
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "insuranceCountCallBack");
	}

	/** 保费计算返回 */
	public void insuranceCountCallBack(Object resultObj) {
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(map))
			return;
		riskPrem = (String) map.get(Safety.RISKPAEM);
		map.put(Safety.SUBINSUID, getResources().getStringArray(R.array.subInsuId_CN)[mFirstSubCompanyName.getSelectedItemPosition()]);
		SafetyDataCenter.getInstance().setCountMap(map);
		requestGetSecurityFactor(Safety.ServiceId);
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String token = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
		if (StringUtil.isNull(token))
			return;
		if (isChange) {
			requestInsurChange(token);
		}
		if (isSave) {
			insurAliasSave(token);
		}
	}

	/** 保单修改 */
	private void requestInsurChange(String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Safety.METHOD_INSURCHANGE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put(Safety.TOKEN, token);
		param.put(Safety.ALIAS_ID, alias);
		param.put(Safety.DEPETE_INSURID, id);
		initRqData(param);
		tempMap = param;
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "insuranceChangeCallBack");
	}

	public void insuranceChangeCallBack(Object resultObj) {
		isChange = false;
		SafetyDataCenter.getInstance().setChange(true);
		BaseHttpEngine.dissMissProgressDialog();
		CustomDialog.toastShow(this, getString(R.string.safety_save_tip));
		saveTempData();
	}

	/** 安全因子返回结果 */
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
			@Override
			public void onClick(View v) {
				requestInsurBuyConfirm();
			}
		});
	}

	/** 请求投保预交易 */
	private void requestInsurBuyConfirm() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Safety.METHOD_BUY_CONFIRM);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put(Safety.COMBINID, BaseDroidApp.getInstanse().getSecurityChoosed());
		param.put(Safety.INSURANCE_COMANY, companyName);
		param.put(Safety.SAFETY_HOLD_CURRENCY, SafetyConstant.CURRENCY);
		param.put(Safety.BEN_NAME, SafetyUtils.getText(mThirdName));
		param.put(Safety.BEN_IDNUM, SafetyUtils.getText(mThirdIdNum));
		param.put(Safety.RISKNAME, productName);
		param.put(Safety.RISKPAEM, riskPrem);
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "insuranceConfirmCallBack");
	}

	/** 投保预交易返回 */
	public void insuranceConfirmCallBack(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		///////////////////////////
		SafetyDataCenter.getInstance().setHoldProDetail(result);
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		getCFCtype(result);
	}

	/** 解析加密类型数据 */
	@SuppressWarnings("unchecked")
	private void getCFCtype(Map<String, Object> resultData) {
		List<Map<String, Object>> sipList = (List<Map<String, Object>>) resultData.get(Safety.FACTOR_LIST);
		for (int i = 0; i < sipList.size(); i++) {
			Map<String, Object> map = (Map<String, Object>) sipList.get(i).get(Safety.SIP_FIELD);
			String sipType = (String) map.get(Safety.SIP_NAME);
			if (sipType.equals(Comm.Otp)) {
				otp = sipType;
			} else if (sipType.equals(Comm.Smc)) {
				smc = sipType;
			}
		}
		if (StringUtil.isNull(otp) && StringUtil.isNull(smc)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		requestForRandomNumber();
	}

	/** 请求随机数 */
	private void requestForRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "queryRandomNumberCallBack");
	}

	public void queryRandomNumberCallBack(Object resultObj) {
		String randomNumber = HttpTools.getResponseResult(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNull(randomNumber))
			return;
		String acctId = (String) (SafetyDataCenter.getInstance().getAcctList().get(mFourthAcct.getSelectedItemPosition())).get(Comm.ACCOUNT_ID);
		String acctNum = (String) (SafetyDataCenter.getInstance().getAcctList().get(mFourthAcct.getSelectedItemPosition())).get(Comm.ACCOUNTNUMBER);
		String acctType = (String) (SafetyDataCenter.getInstance().getAcctList().get(mFourthAcct.getSelectedItemPosition())).get(Comm.ACCOUNT_TYPE);
		startActivity(new Intent(this, SafetyProductBuyMsgConfirmActivity.class).putExtra(Comm.Otp, otp).putExtra(Comm.Smc, smc).putExtra(SafetyConstant.PRODUCTORSAVE, fromListFlag).putExtra(SafetyConstant.RANDOMNUMBER, randomNumber).putExtra(Safety.POLEFFDATE, SafetyUtils.getText(mFirstInsurStartDate)).putExtra(Safety.BEN_NAME, SafetyUtils.getText(mThirdName)).putExtra(Safety.BEN_IDNUM, SafetyUtils.getText(mThirdIdNum)).putExtra(Safety.PAY_METHOD, SafetyDataCenter.payMethod.get(acctType)).putExtra(Comm.ACCOUNTNUMBER, acctNum).putExtra(Comm.ACCOUNT_ID, acctId));
	}

	/**
	 * 请求暂存保单列表
	 */
	private void requestTempInsuranceList() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Safety.METHOD_INSURANCE_TEMP_PRODUCT);
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put(Safety.CURRENT_INDEX, "1");
		param.put(Safety.PAGESIZE, SafetyConstant.PAGESIZE);
		param.put(Safety.REFRESH, String.valueOf(true));
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "insuranceTempListCallBack");
	}

	@SuppressWarnings("unchecked")
	public void insuranceTempListCallBack(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		List<Map<String, Object>> mList = (List<Map<String, Object>>) result.get(Safety.PRODUCTTEMP_LIST);
		if (StringUtil.isNullOrEmpty(mList)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		for (int i = 0; i < mList.size(); i++) {
			if (mList.get(i).get(Safety.ALIAS_ID).equals(alias)) {
				id = (String) mList.get(i).get(Safety.DEPETE_INSURID);
			}
		}
		requestCommConversationId();
	}
}
