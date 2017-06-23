package com.chinamworld.bocmbci.biz.safety.safetyproduct.lifelnsurance.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Inves;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyConstant;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.SafetyUtils;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.PublicTools;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import net.tsz.afinal.core.Arrays;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplInfoFrag extends LifeInsuranceBaseFragment {

	private static final String TAG = "ApplInfoFrag";
	/** 投保人姓名输入框，当登录证件类型不为三证时姓名可输 */
	private EditText etName;
	/** 投保人姓名文本框，当登录证件类型为三证时姓名不可输 */
	private TextView tvName;
	/** 性别男 */
	private RadioButton rbMan;
	/** 选择出生日期控件 */
	private TextView tvChooseBirthDate;
	/** 国籍 */
	private Spinner spNationality;
	/** 投保人证件类型下拉框，当登录证件类型不为身份证、临时身份证和户口簿时用下拉框选择且可修改 */
	private Spinner spIdType;
	/** 投保人证件类型文本框，当登录证件类型为身份证、临时身份证和户口簿时用文本框显示且不可修改 */
	private TextView tvIdType;
	/** 投保人证件号码输入框，当登录证件类型不为身份证、临时身份证和户口簿时用输入框显示且可修改 */
	private EditText etIdNumber;
	/** 投保人证件号码文本框，当登录证件类型为身份证、临时身份证和户口簿时用文本框显示且不可修改 */
	private TextView tvIdNumber;
	/** 证件生效日期 */
	private TextView tvIdBeginDate;
	/** 证件失效日期 */
	private TextView tvIdEndDate;
	/** 证件到期日是否长期 */
	private CheckBox cbIsForever;
	/** 婚姻状态 */
	private Spinner spMaritalStatus;
	/** 居民类型 */
	private Spinner spResidentsType;
	/** 省 */
	private Spinner spAdressProvince;
	/** 市 */
	private Spinner spAdressCity;
	/** 区县 */
	private Spinner spAdressCounty;
	/** 精确地址 */
	private EditText etAdressDetail;
	/** 邮编 */
	private EditText etApplPost;

	/** 移动电话 */
	private EditText etPhone;

	/** 投保人年收入 */
	private EditText etApplIncome;

	/** 电子邮箱 */
	private EditText etEmail;
	/** 职业 */
	private Spinner spJob;
	/** 投保人是被保人的 */
	// private Spinner spBuyerguanxi;
	/** 国籍数据 */
	private List<Map<String, Object>> listCountry;
	/** 通讯地址输入框长度监听 */
//	private TextWatcherLimit twlAdressDetail;
	/** 地址详情长度 */
//	private int adressDetailLength;
// 606 家庭电话、办公电话、单位名称、 家庭年收入、个人保费预算栏位，根据BII参数控制是否显示
	/** 家庭电话 A9 */
	private LinearLayout ll_homeTel;
	private EditText etHomeTel;
	/** 办公电话  A10*/
	private LinearLayout ll_OfficeTel;
	private EditText etOfficeTel;
	/** 单位名称 A13*/
	private LinearLayout ll_OfficeName;
	private EditText etOfficeName;
	/** 家庭年收入  A18*/
	private LinearLayout ll_FamilyIncome;
	private EditText etFamilyIncome;
	/** 个人保费预算  A19*/
	private LinearLayout ll_premBudget;
	private EditText etPremBudget;
	/** 家庭年收入 个人保费预算  正则 */
	String 	regex="(?!^0$)(?!^[0]{2,})(?!^0[1-9]+)(?!^0.[0]*$)^\\d{1,9999}(\\.\\d{1,2})?$";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainView = inflater.inflate(R.layout.safety_life_input_appl_info_frag, null);
		findView();
		viewSet();
		return mMainView;
	}

	@Override
	protected void findView() {
		etName = (EditText) mMainView.findViewById(R.id.et_applName);
		tvName = (TextView) mMainView.findViewById(R.id.tv_applName);
		rbMan = (RadioButton) mMainView.findViewById(R.id.rb_man);
		tvChooseBirthDate = (TextView) mMainView.findViewById(R.id.tv_chooseBirthDate);
		spNationality = (Spinner) mMainView.findViewById(R.id.sp_nationality);
		spIdType = (Spinner) mMainView.findViewById(R.id.sp_idType);
		tvIdType = (TextView) mMainView.findViewById(R.id.tv_idType);
		etIdNumber = (EditText) mMainView.findViewById(R.id.et_idNumber);
		tvIdNumber = (TextView) mMainView.findViewById(R.id.tv_idNumber);
		tvIdBeginDate = (TextView) mMainView.findViewById(R.id.tv_documentBeginDate);
		tvIdEndDate = (TextView) mMainView.findViewById(R.id.tv_documentEndDate);
		cbIsForever = (CheckBox) mMainView.findViewById(R.id.cb_isForever);
		spMaritalStatus = (Spinner) mMainView.findViewById(R.id.sp_maritalStatus);
		spResidentsType = (Spinner) mMainView.findViewById(R.id.sp_residentsType);
		spAdressProvince = (Spinner) mMainView.findViewById(R.id.sp_adress_province);
		spAdressCity = (Spinner) mMainView.findViewById(R.id.sp_adress_city);
		spAdressCounty = (Spinner) mMainView.findViewById(R.id.sp_adress_county);
		etAdressDetail = (EditText) mMainView.findViewById(R.id.et_adress_detail);
		etApplPost = (EditText) mMainView.findViewById(R.id.et_applpost);
		etPhone = (EditText) mMainView.findViewById(R.id.et_phone);
		etApplIncome = (EditText) mMainView.findViewById(R.id.et_applIncome);
		etEmail = (EditText) mMainView.findViewById(R.id.et_email);
		spJob = (Spinner) mMainView.findViewById(R.id.sp_job);

		// 606 家庭电话、办公电话、单位名称、 家庭年收入、个人保费预算栏位，根据BII参数控制是否显示
		ll_homeTel=(LinearLayout) mMainView.findViewById(R.id.ll_homeTel);
		etHomeTel = (EditText) mMainView.findViewById(R.id.et_homeTel);
		ll_OfficeTel=(LinearLayout) mMainView.findViewById(R.id.ll_officeTel);
		etOfficeTel = (EditText) mMainView.findViewById(R.id.et_officeTel);
		ll_OfficeName=(LinearLayout) mMainView.findViewById(R.id.ll_officeName);
		etOfficeName = (EditText) mMainView.findViewById(R.id.et_officeName);
		ll_FamilyIncome=(LinearLayout) mMainView.findViewById(R.id.ll_familyIncome);
		etFamilyIncome = (EditText)mMainView.findViewById(R.id.et_familyIncome);
		ll_premBudget= (LinearLayout) mMainView.findViewById(R.id.ll_premBudget);
		etPremBudget = (EditText) mMainView.findViewById(R.id.et_premBudget);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void viewSet() {
		Map<String, Object> logInfo = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA);
		((TextView) mMainView.findViewById(R.id.tv_applName)).setText((String) logInfo.get(Inves.CUSTOMERNAME));
		// ((TextView)
		// mMainView.findViewById(R.id.tv_applGender)).setText(logInfo.get(Inves.GENDER).equals("1")
		// ? "男" : "女");
		((TextView) mMainView.findViewById(R.id.tv_guanxi)).setText("本人");
		// 国籍
		listCountry = SafetyUtils.getCountry(getActivity());
		SafetyUtils.initSpinnerView(getActivity(), spNationality, PublicTools.getSpinnerData(listCountry, Safety.NAME));
		spNationality.setSelection(46);
		// List<String> country = new ArrayList<String>();
		// country.add("中国");
		// SafetyUtils.initSpinnerView(getActivity(), spNationality, country);
		String identityType = (String) logInfo.get(Comm.IDENTITYTYPE);
		// 证件类型、证件号码
		if ("1".equals(identityType) || "2".equals(identityType) || "3".equals(identityType)) {
			etName.setVisibility(View.GONE);
			tvName.setVisibility(View.VISIBLE);
			spIdType.setVisibility(View.GONE);
			tvIdType.setVisibility(View.VISIBLE);
			etIdNumber.setVisibility(View.GONE);
			tvIdNumber.setVisibility(View.VISIBLE);
			tvName.setText((String) logInfo.get(Inves.CUSTOMERNAME));
			tvIdType.setText(SafetyDataCenter.IDENTITYTYPE.get(identityType));
			tvIdNumber.setText((String) logInfo.get(Comm.IDENTITYNUMBER));
		} else {
			etName.setVisibility(View.VISIBLE);
			tvName.setVisibility(View.GONE);
			spIdType.setVisibility(View.VISIBLE);
			tvIdType.setVisibility(View.GONE);
			etIdNumber.setVisibility(View.VISIBLE);
			tvIdNumber.setVisibility(View.GONE);
			etName.setText((String) logInfo.get(Inves.CUSTOMERNAME));
			SafetyUtils.initSpinnerView(getActivity(), spIdType, SafetyDataCenter.credTypeList);
			spIdType.setSelection(SafetyDataCenter.rqcredType.indexOf(SafetyDataCenter.IDENTITYTYPE_credType.get(identityType)));
			etIdNumber.setText((String) logInfo.get(Comm.IDENTITYNUMBER));
		}

		// 出生日期
		if (!StringUtil.isNullOrEmpty(SafetyDataCenter.getInstance().getMapSVRPsnInfoQuery().get(Safety.BIRTHDAY))) {
			String birthStr = (String) SafetyDataCenter.getInstance().getMapSVRPsnInfoQuery().get(Safety.BIRTHDAY);
			((TextView) mMainView.findViewById(R.id.tv_birthDate)).setText(birthStr);
		} else {
			if (identityType.equals("1") || identityType.equals("2") || identityType.equals("3")) {
				String idNumber = (String) logInfo.get(Comm.IDENTITYNUMBER);
				((TextView) mMainView.findViewById(R.id.tv_birthDate)).setText(idNumber.substring(6, 10) + "/" + idNumber.substring(10, 12) + "/" + idNumber.substring(12, 14));
			} else {
				mMainView.findViewById(R.id.tv_birthDate).setVisibility(View.GONE);
				tvChooseBirthDate.setVisibility(View.VISIBLE);
				String sysTime = SafetyDataCenter.getInstance().getSysTime();
				String year = sysTime.substring(0, 4);
				int yearInt = Integer.parseInt(year);

				setShowDateView(tvChooseBirthDate, (yearInt - 20) + "/01/01", SafetyDataCenter.getInstance().getSysTime(), null);
			}
		}

		setShowDateView(tvIdBeginDate, null, null, null);
		setShowDateView(tvIdEndDate, null, null, null);
		tvIdEndDate.setTag(SafetyDataCenter.getInstance().getSysTime());
		mMainView.findViewById(R.id.tv_isCheckTip).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (cbIsForever.isChecked()) {
					cbIsForever.setChecked(false);
				} else {
					cbIsForever.setChecked(true);
				}
			}
		});
		
		cbIsForever.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					String oldDate = tvIdEndDate.getText().toString();
					tvIdEndDate.setTag(oldDate);
					tvIdEndDate.setText("");
					tvIdEndDate.setEnabled(false);
					tvIdEndDate.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner_default));
				} else {
					String oldDate = (String) tvIdEndDate.getTag();
					tvIdEndDate.setText(oldDate);
					tvIdEndDate.setEnabled(true);
					tvIdEndDate.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner));
				}
			}
		});
		
//		adressDetailLength = 80;
//		twlAdressDetail = new TextWatcherLimit(getActivity(), etAdressDetail, adressDetailLength);
//		etAdressDetail.addTextChangedListener(twlAdressDetail);
		EditTextUtils.setLengthMatcher(getActivity(), etApplPost, 6);
		EditTextUtils.setLengthMatcher(getActivity(), etPhone, 18);
		EditTextUtils.setLengthMatcher(getActivity(), etApplIncome, 13);
		EditTextUtils.setLengthMatcher(getActivity(), etEmail, 30);


		// 606 家庭电话、办公电话、单位名称、 家庭年收入、个人保费预算栏位，根据BII参数控制是否显示
		List<String[]> a = SafetyDataCenter.getInstance().getControlInfoA();
		for (int i = 0; i < a.size(); i++) {
			String[] str = a.get(i);
			String strCode = str[0];
			String code=str[1];// 0 必输
			if(strCode.equals("A9")&&"0".equals(code)){
				// A9家庭电话
				ll_homeTel.setVisibility(View.VISIBLE);
				EditTextUtils.setLengthMatcher(getActivity(), etHomeTel, Integer.valueOf(str[2])/*18*/);
			}else if(strCode.equals("A10")&&"0".equals(code)){
				// A10办公电话
				ll_OfficeTel.setVisibility(View.VISIBLE);
				EditTextUtils.setLengthMatcher(getActivity(), etOfficeTel,Integer.valueOf(str[2]) /*18*/);
			}else if(strCode.equals("A13")&&"0".equals(code)){
				//A13	单位名称				;
				ll_OfficeName.setVisibility(View.VISIBLE);
				EditTextUtils.setLengthMatcher(getActivity(), etOfficeName, Integer.valueOf(str[2])/*80*/);
			}else if(strCode.equals("A18")&&"0".equals(code)){
				//A18 家庭年收入
				ll_FamilyIncome.setVisibility(View.VISIBLE);
				EditTextUtils.setLengthMatcher(getActivity(), etFamilyIncome, Integer.valueOf(str[2])/*13*/);
				Map msg = new HashMap<Object, Object>();
				msg.put("length", Integer.valueOf(str[2]));
				if(str.length>=4){
					msg.put("max",str[3]);
				}
				if(str.length>=5){
					msg.put("min",str[4]);
				}
				etFamilyIncome.setTag(msg);

			}else if(strCode.equals("A19")&&"0".equals(code)){
				/** 个人保费预算  A19*/
				ll_premBudget.setVisibility(View.VISIBLE);
				EditTextUtils.setLengthMatcher(getActivity(), etPremBudget, Integer.valueOf(str[2])/*13*/);
				Map msg = new HashMap<Object, Object>();
				msg.put("length", Integer.valueOf(str[2]));
				if(str.length>=4){
					msg.put("max",str[3]);
				}
				if(str.length>=5){
					msg.put("min",str[4]);
				}
				etPremBudget.setTag(msg);
			}
		}

//		List<String[]> a = SafetyDataCenter.getInstance().getControlInfoA();
//
//		for (int i = 0; i < a.size(); i++) {
//			String[] str = a.get(i);
//			String strCode = str[0];
//			if (strCode.equals("A6")) {
//				// 通讯地址
//				adressDetailLength = Integer.valueOf(str[2]);
//				twlAdressDetail = new TextWatcherLimit(getActivity(), etAdressDetail, adressDetailLength);
//				etAdressDetail.addTextChangedListener(twlAdressDetail);
//			} else if (strCode.equals("A7")) {
//				// 邮编
//				EditTextUtils.setLengthMatcher(getActivity(), etApplPost, Integer.valueOf(str[2]));
//			} else if (strCode.equals("A8")) {
//				// 移动电话
//				EditTextUtils.setLengthMatcher(getActivity(), etPhone, Integer.valueOf(str[2]));
//			}
//			// else if (strCode.equals("A9")) {
//			// // 家庭电话
//			// EditTextUtils.setLengthMatcher(getActivity(), etHomeTel,
//			// Integer.valueOf(str[2]));
//			// } else if (strCode.equals("A10")) {
//			// // 办公电话
//			// EditTextUtils.setLengthMatcher(getActivity(), etOfficeTel,
//			// Integer.valueOf(str[2]));
//			// }
//			else if (strCode.equals("A12")) {
//				// 年收入，+1个小数点的位数
//				EditTextUtils.setLengthMatcher(getActivity(), etApplIncome, Integer.valueOf(str[2]) + 1);
//				// EditTextUtils.setLengthMatcher(getActivity(), etFamilyIncome,
//				// Integer.valueOf(str[2]));
//			}
//			// else if (strCode.equals("A13")) {
//			// // 单位名称
//			// EditTextUtils.setLengthMatcher(getActivity(), etOfficeName,
//			// Integer.valueOf(str[2]));
//			// }
//			else if (strCode.equals("A14")) {
//				// 电子邮件
//				EditTextUtils.setLengthMatcher(getActivity(), etEmail, Integer.valueOf(str[2]));
//			}
//		}

		etEmail.setText((String) SafetyDataCenter.getInstance().getMapSVRPsnInfoQuery().get(Safety.EMAIL));
		SafetyUtils.initSpinnerView(getActivity(), spMaritalStatus, SafetyDataCenter.lifeMargiList);
		SafetyUtils.initSpinnerView(getActivity(), spResidentsType, SafetyDataCenter.residentsType_CN);
		SafetyUtils.initSpinnerView(getActivity(), spAdressProvince, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getProviceList(), Safety.NAME));
		SafetyUtils.initSpinnerView(getActivity(), spJob, Arrays.asList(getResources().getStringArray(R.array.Job_CN)));

		spAdressProvince.setOnItemSelectedListener(adressListener);
		spAdressCity.setOnItemSelectedListener(adressListener);
		spAdressCounty.setOnItemSelectedListener(adressListener);
		PopupWindowUtils.getInstance().setOnShowAllTextListenert(getActivity(), (TextView) mMainView.findViewById(R.id.tv_keyForGuanxi));
		PopupWindowUtils.getInstance().setOnShowAllTextListenert(getActivity(), (TextView) mMainView.findViewById(R.id.tv_keyForApplIncome));
	}



	@SuppressWarnings("unchecked")
	@Override
	public boolean submit() {
		if (inputRegexp()) {
			Map<String, Object> userInput = SafetyDataCenter.getInstance().getMapUserInput();
			Map<String, Object> logInfo = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA);
			Map<String, Object> userInputTemp = SafetyDataCenter.getInstance().getMapCarSafetyUserInput();
			if (spIdType.getVisibility() == View.VISIBLE) {
				userInput.put(Safety.APPL_NAME, etName.getText().toString().trim());
				userInput.put(Safety.APPL_IDTYPE, SafetyDataCenter.rqcredType.get(spIdType.getSelectedItemPosition()));
				userInput.put(Safety.APPL_IDNO, etIdNumber.getText().toString().trim());
			} else {
				userInput.put(Safety.APPL_NAME, tvName.getText().toString().trim());
				userInput.put(Safety.APPL_IDTYPE, SafetyDataCenter.IDENTITYTYPE_credType.get(logInfo.get(Comm.IDENTITYTYPE)));
				userInput.put(Safety.APPL_IDNO, logInfo.get(Comm.IDENTITYNUMBER));
			}
			userInput.put(Safety.APPLIDSTARTDATE, tvIdBeginDate.getText().toString());
			userInput.put(Safety.APPLIDENDDATE, StringUtil.isNull(tvIdEndDate.getText().toString()) ? "2100/12/31" : tvIdEndDate.getText().toString());
			userInputTemp.put(Safety.APPL_IS_FOREVER, cbIsForever.isChecked());
			userInput.put(Safety.APPL_CTRYNO, listCountry.get(spNationality.getSelectedItemPosition()).get(Safety.CODE));

			userInputTemp.put(Safety.APPL_CTRYNAME, listCountry.get(spNationality.getSelectedItemPosition()).get(Safety.NAME));
			// userInput.put(Safety.APPL_CTRYNO, "CN");
			// userInput.put(Safety.APPL_CTRYNAME, "中国");
			if (tvChooseBirthDate.getVisibility() == View.VISIBLE) {
				userInput.put(Safety.APPL_BIRTH, tvChooseBirthDate.getText().toString());
			} else {
				userInput.put(Safety.APPL_BIRTH, ((TextView) mMainView.findViewById(R.id.tv_birthDate)).getText().toString());
			}
			userInput.put(Safety.APPL_SEX, rbMan.isChecked() ? "1" : "0");
			userInput.put(Safety.APPL_MARRIAGE, SafetyDataCenter.lifeMargiListrq.get(spMaritalStatus.getSelectedItemPosition()));
			userInput.put(Safety.APPLHOMEADDR, spAdressProvince.getSelectedItem().toString() + spAdressCity.getSelectedItem().toString() + spAdressCounty.getSelectedItem().toString() + etAdressDetail.getText().toString().trim());
			userInputTemp.put(Safety.APPL_PROVINCE, spAdressProvince.getSelectedItemPosition());
			userInputTemp.put(Safety.APPL_CITY, spAdressCity.getSelectedItemPosition());
			userInputTemp.put(Safety.APPL_COUNTY, spAdressCounty.getSelectedItemPosition());
			userInputTemp.put(Safety.APPL_ADRESS, etAdressDetail.getText().toString().trim());
			// 606 国籍，工作 选择的是第几个
			userInputTemp.put("NationaSelected", spNationality.getSelectedItemPosition());
			userInputTemp.put("JobSelected",spJob.getSelectedItemPosition());
			// 试算时这四个字段要删除，这里是为了给被保人信息赋值
			// userInput.put(Safety.BEN_PROVINCE,
			// spAdressProvince.getSelectedItemPosition());
			// userInput.put(Safety.BEN_CITY,
			// spAdressCity.getSelectedItemPosition());
			// userInput.put(Safety.BEN_COUNTY,
			// spAdressCounty.getSelectedItemPosition());
			// userInput.put(Safety.BEN_ADRESS,
			// etAdressDetail.getText().toString().trim());

			userInput.put(Safety.APPL_POSTCODE, etApplPost.getText().toString().trim());
			userInput.put(Safety.APPLMOBILE, etPhone.getText().toString().trim());

			// 606 家庭电话、办公电话、单位名称、 家庭年收入、个人保费预算栏位，根据BII参数控制是否显示
			if(ll_homeTel.getVisibility()==View.VISIBLE){
				String applHomePhone = etHomeTel.getText().toString().trim();
				userInput.put(Safety.APPLHOMEPHONE, applHomePhone);
			}
			if(ll_OfficeTel.getVisibility()==View.VISIBLE){
				String applOfficePhone = etOfficeTel.getText().toString().trim();
				userInput.put(Safety.APPLOFFICEPHONE, applOfficePhone);
			}
			if(ll_OfficeName.getVisibility()==View.VISIBLE){
				String applEmployer = etOfficeName.getText().toString().trim();
				userInput.put(Safety.APPLEMPLOYER, applEmployer);
			}
			if(ll_FamilyIncome.getVisibility()==View.VISIBLE){
				String familyIncome = etFamilyIncome.getText().toString().trim();
				userInput.put(Safety.FAMILYINCOME, familyIncome);
			}
			if(ll_premBudget.getVisibility()==View.VISIBLE){
				String premBudget = etPremBudget.getText().toString().trim();
				userInput.put(Safety.PREMBUDGET, premBudget);
			}
			// 选填项
			// String applHomePhone = etHomeTel.getText().toString().trim();
			// if (!StringUtil.isNull(applHomePhone)) {
			// userInput.put(Safety.APPLHOMEPHONE, applHomePhone);
			// }
			// String applOfficePhone = etOfficeTel.getText().toString().trim();
			// if (!StringUtil.isNull(applOfficePhone)) {
			// userInput.put(Safety.APPLOFFICEPHONE, applOfficePhone);
			// }
			// String familyIncome = etFamilyIncome.getText().toString().trim();
			// if (!StringUtil.isNull(familyIncome)) {
			// userInput.put(Safety.FAMILYINCOME, familyIncome);
			// }
			// String premBudget = etPremBudget.getText().toString().trim();
			// if (!StringUtil.isNull(premBudget)) {
			// userInput.put(Safety.PREMBUDGET, premBudget);
			// }
			// String applEmployer = etOfficeName.getText().toString().trim();
			// if (!StringUtil.isNull(applEmployer)) {
			// userInput.put(Safety.APPLEMPLOYER, applEmployer);
			// }

			userInput.put(Safety.APPLJOBCODE, getResources().getStringArray(R.array.Job_code)[spJob.getSelectedItemPosition()]);
			userInput.put(Safety.APPLINCOME, etApplIncome.getText().toString().trim());
			userInput.put(Safety.RESITYPE, SafetyDataCenter.residentsType_CODE.get(spResidentsType.getSelectedItemPosition()));
			userInput.put(Safety.APPL_EMAIL, etEmail.getText().toString().trim());
			userInput.put(Safety.RELATIONSURED, "01");

			userInput.put(Safety.BEN_IDTYPE, userInput.get(Safety.APPL_IDTYPE));
			userInput.put(Safety.BEN_IDNUM, userInput.get(Safety.APPL_IDNO));
			userInput.put(Safety.BENSTARTDATE, userInput.get(Safety.APPLIDSTARTDATE));
			userInput.put(Safety.BENENDDATE, userInput.get(Safety.APPLIDENDDATE));
			userInput.put(Safety.BEN_CTRYNO, userInput.get(Safety.APPL_CTRYNO));
			userInput.put(Safety.BEN_CTRYNAME, userInput.get(Safety.APPL_CTRYNAME));
			userInput.put(Safety.BEN_NAME, userInput.get(Safety.APPL_NAME));
			userInput.put(Safety.BEN_MARRAGE, userInput.get(Safety.APPL_MARRIAGE));
			userInput.put(Safety.BEN_BIRTH, userInput.get(Safety.APPL_BIRTH));
			userInput.put(Safety.BEN_SEX, userInput.get(Safety.APPL_SEX));
			userInput.put(Safety.BENHOMEADDR, userInput.get(Safety.APPLHOMEADDR));
			userInput.put(Safety.BEN_POSTCODE, userInput.get(Safety.APPL_POSTCODE));
			userInput.put(Safety.BENMOBILE, userInput.get(Safety.APPLMOBILE));

			// String applHomePhone = (String)
			// userInput.get(Safety.APPLHOMEPHONE);
			// if (!StringUtil.isNull(applHomePhone)) {
			// userInput.put(Safety.BENHOMEPHONE, applHomePhone);
			// }
			// String benOfficePhone = (String)
			// userInput.get(Safety.APPLOFFICEPHONE);
			// if (!StringUtil.isNull(benOfficePhone)) {
			// userInput.put(Safety.BENOFFICEPHONE, benOfficePhone);
			// }
			// String benEmployer = (String) userInput.get(Safety.APPLEMPLOYER);
			// if (!StringUtil.isNull(benEmployer)) {
			// userInput.put(Safety.BENEMPLOYER, benEmployer);
			// }

			userInput.put(Safety.BENJOBCODE, userInput.get(Safety.APPLJOBCODE));
			userInput.put(Safety.BENINCOME, userInput.get(Safety.APPLINCOME));
			userInput.put(Safety.BEN_EMAIL, userInput.get(Safety.APPL_EMAIL));

			LogGloble.i(TAG, userInput.toString());
			return true;
		}
		return false;
	}

	/** 输入字段校验 */
	@SuppressWarnings("unchecked")
	private boolean inputRegexp() {
		if (etName.getVisibility() == View.VISIBLE) {
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			if (onlyRegular(true, etName.getText().toString().trim())) {
				RegexpBean rb = new RegexpBean(SafetyConstant.BNFT_NAME, etName.getText().toString().trim(), SafetyConstant.NOTEMPTY);
				rb.setExtentParam(etName);
				lists.add(rb);
			}
			RegexpBean result1 = RegexpUtils.regexpDateWithRgexpBean(lists, null);
			if (result1 != null) {
				View v = (View) result1.getExtentParam();
				changeViewBg(v);
				return false;
			}
		}

		String beginDate = tvIdBeginDate.getText().toString();
		String endDate = tvIdEndDate.getText().toString();

		String birthDate = null;

		if (tvChooseBirthDate.getVisibility() == View.GONE) {
			birthDate = ((TextView) mMainView.findViewById(R.id.tv_birthDate)).getText().toString();
		} else {
			birthDate = tvChooseBirthDate.getText().toString();
		}

		if (QueryDateUtils.compareDate(beginDate, birthDate) || beginDate.equals(birthDate)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("证件生效日不能小于等于出生日期");
			changeViewBg(tvIdBeginDate);
			tvChooseBirthDate.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner));
			if (StringUtil.isNull(tvIdEndDate.getText().toString())) {
				tvIdEndDate.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner_default));
			} else {
				tvIdEndDate.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner));
			}
			return false;
		}

		if (!QueryDateUtils.compareDate(beginDate, SafetyDataCenter.getInstance().getSysTime()) || beginDate.equals(SafetyDataCenter.getInstance().getSysTime())) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("证件生效日不能大于等于系统日期");
			changeViewBg(tvIdBeginDate);
			tvChooseBirthDate.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner));
			if (StringUtil.isNull(tvIdEndDate.getText().toString())) {
				tvIdEndDate.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner_default));
			} else {
				tvIdEndDate.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner));
			}
			return false;
		}

//		if (!QueryDateUtils.compareDate(beginDate, endDate) || beginDate.equals(endDate)) {
//			BaseDroidApp.getInstanse().showInfoMessageDialog("证件生效日期不能大于等于失效日期");
//			changeViewBg(tvIdBeginDate);
//			tvIdEndDate.setBackground(getResources().getDrawable(R.drawable.bg_spinner));
//			tvChooseBirthDate.setBackground(getResources().getDrawable(R.drawable.bg_spinner));
//			return false;
//		}

		if (!StringUtil.isNull(endDate)) {
			if (QueryDateUtils.compareDate(endDate, SafetyDataCenter.getInstance().getSysTime()) || endDate.equals(SafetyDataCenter.getInstance().getSysTime())) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("证件到期日不能小于等于系统日期");
				changeViewBg(tvIdEndDate);
				tvChooseBirthDate.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner));
				tvIdBeginDate.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner));
				return false;
			}
		}

		Map<String, Object> logInfo = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA);
		String loginIdentityType = (String) logInfo.get(Comm.IDENTITYTYPE);

		if (loginIdentityType.equals("1") || loginIdentityType.equals("2") || loginIdentityType.equals("3")) {
			// 这种情况下是反显不可修改
			String idNumber;
			String idBirthDate = null;
			idNumber = (String) logInfo.get(Comm.IDENTITYNUMBER);
			idBirthDate = idNumber.substring(6, 10) + "/" + idNumber.substring(10, 12) + "/" + idNumber.substring(12, 14);
			if (!birthDate.equals(idBirthDate)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("投保人出生日期与证件出生日期不符");
				if (tvChooseBirthDate.getVisibility() == View.VISIBLE) {
					changeViewBg(tvChooseBirthDate);
				}
				return false;
			}

			if (SafetyUtils.getGender(idNumber).equals("1") && !rbMan.isChecked() || SafetyUtils.getGender(idNumber).equals("0") && rbMan.isChecked()) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("性别同身份证不一致,请修改");
				return false;
			}
		} else {
			// 这种情况是用户选择证件类型，输入证件号码
			if (spIdType.getSelectedItemPosition() == 0 || spIdType.getSelectedItemPosition() == 1 || spIdType.getSelectedItemPosition() == 3 || spIdType.getSelectedItemPosition() == 4) {
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				if (onlyRegular(true, etIdNumber.getText().toString().trim())) {
					RegexpBean rb = new RegexpBean(SafetyConstant.BNFT_IDENTITYNUM, etIdNumber.getText().toString(), SafetyConstant.CAROWNER_ID);
					rb.setExtentParam(etIdNumber);
					lists.add(rb);
					RegexpBean result = RegexpUtils.regexpDateWithRgexpBean(lists, null);
					if (result != null) {
						View v = (View) result.getExtentParam();
						changeViewBg(v);
						return false;
					}
				}
				if (spIdType.getSelectedItemPosition() != 4) {
					String idNumber = etIdNumber.getText().toString().trim();
					String idBirthDate = idNumber.substring(6, 10) + "/" + idNumber.substring(10, 12) + "/" + idNumber.substring(12, 14);
					if (!birthDate.equals(idBirthDate)) {
						BaseDroidApp.getInstanse().showInfoMessageDialog("投保人出生日期与证件出生日期不符");
						if (tvChooseBirthDate.getVisibility() == View.VISIBLE) {
							changeViewBg(tvChooseBirthDate);
						} else {
							changeViewBg(etIdNumber);
						}
						return false;
					}

					if (SafetyUtils.getGender(idNumber).equals("1") && !rbMan.isChecked() || SafetyUtils.getGender(idNumber).equals("0") && rbMan.isChecked()) {
						BaseDroidApp.getInstanse().showInfoMessageDialog("性别同身份证不一致,请修改");
						changeViewBg(etIdNumber);
						return false;
					}
				}
			} else if (spIdType.getSelectedItemPosition() == 5) {
				// 请输入一位汉字开头+8位数字
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				RegexpBean rb = new RegexpBean(SafetyConstant.IDENTITYNUM_BUYER, etIdNumber.getText().toString().trim(), SafetyConstant.LIFEWUJINGIDTYPE);
				rb.setExtentParam(etIdNumber);
				lists.add(rb);
				RegexpBean result = RegexpUtils.regexpDateWithRgexpBean(lists, null);
				if (result != null) {
					View v = (View) result.getExtentParam();
					changeViewBg(v);
					return false;
				}
			} else {
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				RegexpBean rb = new RegexpBean(SafetyConstant.IDENTITYNUM_BUYER, etIdNumber.getText().toString().trim(), SafetyConstant.CARSAFETY_OTHERIDTYPE);
				rb.setExtentParam(etIdNumber);
				lists.add(rb);
				RegexpBean result = RegexpUtils.regexpDateWithRgexpBean(lists, null);
				if (result != null) {
					View v = (View) result.getExtentParam();
					changeViewBg(v);
					return false;
				}
			}
		}

		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		// 地址
		String adress = spAdressProvince.getSelectedItem().toString() + spAdressCity.getSelectedItem().toString() + spAdressCounty.getSelectedItem().toString() + etAdressDetail.getText().toString().trim();
		if (onlyRegular(true, adress)) {
			RegexpBean rb = new RegexpBean(SafetyConstant.ADRESS_NO_BUYER, adress, SafetyConstant.LIFEADRESS);
			rb.setExtentParam(etAdressDetail);
			lists.add(rb);
		}
		if (onlyRegular(true, etAdressDetail.getText().toString().trim())) {
			RegexpBean rb = new RegexpBean(SafetyConstant.ADRESS_NO_BUYER, etAdressDetail.getText().toString().trim(), SafetyConstant.LIFEADRESS);
			rb.setExtentParam(etAdressDetail);
			lists.add(rb);
		}
		try {
			String adressByte = new String(adress.getBytes("GB2312"), "ISO-8859-1");
			if (adressByte.length() > 80) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("通讯地址请输入中文、字母或数字，可包含',.-/()和空格，最大长度80个字符");
				return false;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 邮编
		if (onlyRegular(true, etApplPost.getText().toString().trim())) {
			RegexpBean rb = new RegexpBean(SafetyConstant._POST, etApplPost.getText().toString().trim(), SafetyConstant.POST);
			rb.setExtentParam(etApplPost);
			lists.add(rb);
		}

		// 606 家庭电话、办公电话、单位名称、 家庭年收入、个人保费预算栏位，根据BII参数控制是否显示

		// 家庭电话
		if(ll_homeTel.getVisibility()==View.VISIBLE){

			if (onlyRegular(true, etHomeTel.getText().toString())) {
				RegexpBean rb = new RegexpBean(SafetyConstant.HOMETEL,
						etHomeTel.getText().toString(), SafetyConstant.HOMEANDOFFICE);
				rb.setExtentParam(etHomeTel);
				lists.add(rb);
			}
		}
		// 办公电话
		if(ll_OfficeTel.getVisibility()==View.VISIBLE){

			if (onlyRegular(true, etOfficeTel.getText().toString())) {
				RegexpBean rb = new RegexpBean(SafetyConstant.OFFICETEL,
						etOfficeTel.getText().toString(), SafetyConstant.HOMEANDOFFICE);
				rb.setExtentParam(etOfficeTel);
				lists.add(rb);
			}
		}
		// 单位名称
		if(ll_OfficeName.getVisibility()==View.VISIBLE){

			if (onlyRegular(true, etOfficeName.getText().toString())) {
				RegexpBean rb = new RegexpBean(SafetyConstant.OFFICENAME,
						etOfficeName.getText().toString(), SafetyConstant.ADRESS_k);
				rb.setExtentParam(etOfficeName);
				lists.add(rb);
			}
		}
		// 投保人手机号码
		if (onlyRegular(true, etPhone.getText().toString().trim())) {
			RegexpBean rb = new RegexpBean(SafetyConstant.MOBILE_PHONE_BUYER, etPhone.getText().toString().trim(), SafetyConstant.LIFEPHONE);
			rb.setExtentParam(etPhone);
			lists.add(rb);
		}
		// 投保人年收入
		if (onlyRegular(true, etApplIncome.getText().toString())) {
			RegexpBean rb = new RegexpBean(SafetyConstant.APPLINCOME, etApplIncome.getText().toString(), SafetyConstant.LIFEINSURAMOUNT);
			rb.setExtentParam(etApplIncome);
			lists.add(rb);
		}
		RegexpBean result = RegexpUtils.regexpDateWithRgexpBean(lists, null);
		if (result != null) {
			View v = (View) result.getExtentParam();
			changeViewBg(v);
			return false;
		}





		// 家庭年收入
		if(ll_FamilyIncome.getVisibility()==View.VISIBLE){
			if (onlyRegular(true, etFamilyIncome.getText().toString())) {

				if("".equals(etFamilyIncome.getText().toString().trim()) ||etFamilyIncome.getText().toString().trim() == null) {
					BaseDroidApp.getInstanse().showInfoMessageDialog("请输入" +SafetyConstant.FAMILYINCOME);
					changeViewBg(etFamilyIncome);
					return false;
				}

				Map msg=new HashMap<Object, Object>();
				msg=(HashMap<Object, Object>)etFamilyIncome.getTag();
				int length=(Integer) msg.get("length");
				if(etFamilyIncome.getText().toString().trim().matches(regex)){
					String text=etFamilyIncome.getText().toString().trim();
					if(text.contains(".")){
						if(text.indexOf(".")>(length-3)){
							BaseDroidApp.getInstanse().showInfoMessageDialog(SafetyConstant.FAMILYINCOME+"请输入不超过"+(length-3)+"位整数，2位小数的金额");
							changeViewBg(etFamilyIncome);
							return false;
						}
					}else{
						if(text.length()>(length-3)){
							BaseDroidApp.getInstanse().showInfoMessageDialog(SafetyConstant.FAMILYINCOME+"请输入不超过"+(length-3)+"位整数，2位小数的金额");
							changeViewBg(etFamilyIncome);
							return false;
						}
					}
					if(checkMinandMax(SafetyConstant.FAMILYINCOME,etFamilyIncome)==false){
						return false;
					}
				}else{

					BaseDroidApp.getInstanse().showInfoMessageDialog(SafetyConstant.FAMILYINCOME+"请输入不超过"+(length-3)+"位整数，2位小数的金额");
					changeViewBg(etFamilyIncome);
					return false;
				}
			}

//			if (onlyRegular(true, etFamilyIncome.getText().toString())) {
//				RegexpBean rb = new RegexpBean(SafetyConstant.FAMILYINCOME,
//						etFamilyIncome.getText().toString(), SafetyConstant.LIFEINSURAMOUNT_K);
//				rb.setExtentParam(etFamilyIncome);
//				lists_k.add(rb);
//			}
		}
		 // 个人保费预算
		if(ll_premBudget.getVisibility()==View.VISIBLE){

			if (onlyRegular(true, etPremBudget.getText().toString())) {
				if("".equals(etPremBudget.getText().toString().trim()) ||etPremBudget.getText().toString().trim() == null) {
					BaseDroidApp.getInstanse().showInfoMessageDialog("请输入" +SafetyConstant.PREMBUDGET);
					changeViewBg(etPremBudget);
					return false;
				}

				Map msg=new HashMap<Object, Object>();
				msg=(HashMap<Object, Object>)etPremBudget.getTag();
				int length=(Integer) msg.get("length");
				if(etPremBudget.getText().toString().trim().matches(regex)){
					String text=etPremBudget.getText().toString().trim();
					if(text.contains(".")){
						if(text.indexOf(".")>(length-3)){
							BaseDroidApp.getInstanse().showInfoMessageDialog(SafetyConstant.PREMBUDGET+"请输入不超过"+(length-3)+"位整数，2位小数的金额");
							changeViewBg(etPremBudget);
							return false;
						}
					}else{
						if(text.length()>(length-3)){
							BaseDroidApp.getInstanse().showInfoMessageDialog(SafetyConstant.PREMBUDGET+"请输入不超过"+(length-3)+"位整数，2位小数的金额");
							changeViewBg(etPremBudget);
							return false;
						}
					}

					if(checkMinandMax(SafetyConstant.PREMBUDGET,etPremBudget)==false){
						return false;
					}
				}else{

					BaseDroidApp.getInstanse().showInfoMessageDialog(SafetyConstant.PREMBUDGET+"请输入不超过"+(length-3)+"位整数，2位小数的金额");
					changeViewBg(etPremBudget);
					return false;
				}
			}
//			if (onlyRegular(true, etPremBudget.getText().toString())) {
//				RegexpBean rb = new RegexpBean(SafetyConstant.PREMBUDGET,
//						etPremBudget.getText().toString(), SafetyConstant.LIFEINSURAMOUNT_K);
//				rb.setExtentParam(etPremBudget);
//				lists_k.add(rb);
//			}
		}
		ArrayList<RegexpBean> lists_k = new ArrayList<RegexpBean>();
		// 投保人电子邮箱
		if (onlyRegular(true, etEmail.getText().toString().trim())) {
			RegexpBean rb = new RegexpBean(SafetyConstant.BUY_EMAIL, etEmail.getText().toString().trim(), SafetyConstant.SAFETYEMAIL2);
			rb.setExtentParam(etEmail);
			lists_k.add(rb);
		}
		// if (!RegexpUtils.regexpDate(lists)) {
		// return false;
		// }
		RegexpBean result_k = RegexpUtils.regexpDateWithRgexpBean(lists_k, null);
		if (result_k != null) {
			View v = (View) result_k.getExtentParam();
			changeViewBg(v);
			return false;
		}
//		// 家庭年收入
//		if(ll_FamilyIncome.getVisibility()==View.VISIBLE){
//			if(checkMinandMax(SafetyConstant.FAMILYINCOME,etFamilyIncome)==false){
//				return false;
//			}
//
//		}
//		// 个人保费预算
//		if(ll_premBudget.getVisibility()==View.VISIBLE){
//			if(checkMinandMax(SafetyConstant.PREMBUDGET,etPremBudget)==false){
//				return false;
//			}
//		}


		return true;
	}

	/** 输入值是否在最大最小值范围 */
	private boolean checkMinandMax(String text,EditText et) {

		Map msg=new HashMap<Object, Object>();
		msg=(HashMap<Object, Object>)et.getTag();
		if(msg.containsKey("min")||msg.containsKey("max")){
			String min=null;
			String max=null;
			if(msg.containsKey("max")){
				max=(String)msg.get("max");
			}
			if(msg.containsKey("min")){
				min=(String)msg.get("min");
			}
			String value=et.getText().toString();
			if(StringUtil.isNull(max)){
				// 最大值不控制
				if(StringUtil.isNull(min)){
					// 最小值不控制
					return true;
				}else{
					// 控制最小值
					if(Double.valueOf(min) <= Double.valueOf(value)){
						return true;
					}else{
						BaseDroidApp.getInstanse().showInfoMessageDialog(text+"最小输入"+min+"元");
						changeViewBg(et);
						return false;
					}

				}

			}else{
				// 控制最大值
				if(StringUtil.isNull(min)){
					// 最小值不控制
					if(Double.valueOf(value)<= Double.valueOf(max)){
						return true;
					}else{
						BaseDroidApp.getInstanse().showInfoMessageDialog(text+"最大输入"+max+"元");
						changeViewBg(et);
						return false;
					}

				}else{
					// 控制最小值
					if (Double.valueOf(min) <= Double.valueOf(value) &&Double.valueOf(value)<= Double.valueOf(max)){
						return true;
					}
					BaseDroidApp.getInstanse().showInfoMessageDialog(text+"可输入"+min+"-"+max+"元");
					changeViewBg(et);
					return false;
				}
			}
		}
		return true;
	}

	/** 是否只作正则校验 */
	private boolean onlyRegular(Boolean required, String content) {
		if ((!required && !StringUtil.isNull(content)) || required) {
			return true;
		}
		return false;
	}

	/** 地址下拉框选择事件 */
	private OnItemSelectedListener adressListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			switch (parent.getId()) {
			case R.id.sp_adress_province:
				// 初始化城市
				String soCityCode = (String) SafetyDataCenter.getInstance().getProviceList().get(position).get(Safety.CODE);
				SafetyDataCenter.getInstance().setSecondCityList(SafetyUtils.getCountryAndCity(getActivity(), soCityCode));
				String soCountryCode = (String) SafetyDataCenter.getInstance().getSecondCityList().get(0).get(Safety.CODE);
				SafetyDataCenter.getInstance().setSecondCountryList(SafetyUtils.getCountryAndCity(getActivity(), soCountryCode));

				SafetyUtils.initSpinnerView(getActivity(), spAdressCity, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getSecondCityList(), Safety.NAME));
				SafetyUtils.initSpinnerView(getActivity(), spAdressCounty, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getSecondCountryList(), Safety.NAME));
				break;
			case R.id.sp_adress_city:
				// 初始化区县
				String stCountryCode = (String) SafetyDataCenter.getInstance().getSecondCityList().get(position).get(Safety.CODE);
				SafetyDataCenter.getInstance().setSecondCountryList(SafetyUtils.getCountryAndCity(getActivity(), stCountryCode));
				SafetyUtils.initSpinnerView(getActivity(), spAdressCounty, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getSecondCountryList(), Safety.NAME));
				break;
			case R.id.sp_adress_county:
//				etAdressDetail.removeTextChangedListener(twlAdressDetail);
//				try {
//					String str = new String((spAdressProvince.getSelectedItem().toString() + spAdressCity.getSelectedItem().toString() + spAdressCounty.getSelectedItem().toString()).getBytes("GB2312"), "ISO-8859-1");
//					twlAdressDetail = new TextWatcherLimit(getActivity(), etAdressDetail, adressDetailLength - str.length());
//				} catch (UnsupportedEncodingException e) {
//					LogGloble.e("Invoiceadress", e.toString());
//				}
//				etAdressDetail.addTextChangedListener(twlAdressDetail);
				break;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};
}
