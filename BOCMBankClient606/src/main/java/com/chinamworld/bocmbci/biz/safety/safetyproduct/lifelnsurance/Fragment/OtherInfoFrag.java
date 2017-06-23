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
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Inves;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyConstant;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.SafetyUtils;
import com.chinamworld.bocmbci.biz.safety.adapter.ChooseCardAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.PublicTools;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OtherInfoFrag extends LifeInsuranceBaseFragment {

	private static final String TAG = "OtherInfoFrag";
	/** 受益人法定 */
	private RadioButton rbLegal;
	/** 受益人指定 */
	private RadioButton rbAppoint;
	/** 受益人个数 */
	// private EditText etbnftNumber;
	/** 受益人类别 */
//	private Spinner spbnftType;
	/** 姓名 */
	private EditText etbnftName;
	/** 性别男 */
	private RadioButton rbMan;
	/** 性别女 */
	private RadioButton rbWoman;
	/** 出生日期 */
	private TextView tvbnftBirth;
	/** 受益人是被保人的 */
	private Spinner spbnftToBen;
	/** 受益人受益顺序 */
	// private EditText etbnftOrder;
	/** 受益比例 */
	// private EditText etbnftProportion;
	/** 证件类型 */
	private Spinner spbnftIdType;
	/** 证件号码 */
	private EditText etbnftIdNumber;
	/** 证件生效日 */
	private TextView tvbnftIdBeginDate;
	/** 证件到期日 */
	private TextView tvbnftIdEndDate;
	/** 受益人证件到期日是否长期 */
	private CheckBox cbIsForever;
	/** 省 */
	private Spinner spAdressProvince;
	/** 市 */
	private Spinner spAdressCity;
	/** 区县 */
	private Spinner spAdressCounty;
	/** 精确地址 */
	private EditText etAdressDetail;
	/** 缴费方式 */
//	private TextView tvBnftPaymentMethod;
	/** 健康告知是 */
	private RadioButton rbHealthInformY;
	/** 职业告知是 */
	private RadioButton rbProfessionalInformY;
	/** 合同争议处理方式 */
//	private Spinner spProcessingMode;
	/** 仲裁机构名称 */
//	private EditText etArbitrationName;
	/** 业务推荐号 */
	private EditText etServiceRecommNo;
	/** 红利领取方式 */
	private Spinner spBonusGetMode;
	/** 续期缴费方式 */
	private CheckBox cbMantainMethod;
	/** 银行账号 */
	private Spinner spBankAccount;
	/** 选择的银行账号是否有人民币子账户 */
	private boolean haveRMB = true;
	/** 年金/生存金领取方式 */
	private Spinner spGetYearFlag;
	/** 领取期限 */
	private EditText etGetYear;
	/** 投资时间选择 */
	private Spinner spInvestTimeType;
	/** 保费自动垫交是 */
	private RadioButton rbAutoPayFlagY;
	/** 投保人账户姓名 */
	private EditText etApplAccName;
	/** 开户行 */
	private TextView tvOpeningBank;
	/** 开户行 */
	private String strOpeningBank;
	/** 领取年龄 */
	private EditText etGetStartAge;
	/** 保单索取方式-电子保单 */
	private RadioButton rbPolicyHandFlag_Email;
	/** 保单索取方式-邮寄保单 */
	private RadioButton rbPolicyHandFlag_Mail;
	/** 保单邮寄地址-省 */
	private Spinner spPostAddrProvince;
	/** 保单邮寄地址-市 */
	private Spinner spPostAddrCity;
	/** 保单邮寄地址-区县 */
	private Spinner spPostAddrCounty;
	/** 保单邮寄地址-精确地址 */
	private EditText etPostAddrDetail;
	/** 通讯地址输入框长度监听 */
//	private TextWatcherLimit twlAdressDetail;
	/** 保单邮寄地址输入框长度监听 */
//	private TextWatcherLimit twlPostAddrDetail;
	/** 地址详情长度 */
//	private int adressDetailLength;
	/** 地址详情长度 */
//	private int postAdressDetailLength;
	/** 受益人信息控件是否可用标识，当受益人是被保人的选本人时受益人控件反显投保人信息且不可修改 */
	private boolean isEnabled = true;
	/** 此片段是否显示过 */
	private boolean isShowed = false;
	/** 银行账户下拉框上次的选择下标 */
	private int accSpSelectPosition = 0;
	/** 银行账户调用详情接口，如果报错，错误信息保存在这里 */
	private String errorInfo;
// 606 指定受益人增加国籍、职业代码、联系电话
	/** 国籍 */
	private Spinner spNationality;
	/** 国籍数据 */
	private List<Map<String, Object>> listCountry;
	/** 移动电话 */
	private EditText etPhone;
	/** 职业 */
	private Spinner spJob;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainView = inflater.inflate(R.layout.safety_life_input_other_info_frag, null);
		findView();
		return mMainView;
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			if (isShowed) {
				accSpSelectPosition = spBankAccount.getSelectedItemPosition();
				refreshView();
			} else {
				viewSet();
			}
		}
	}

	@Override
	protected void findView() {
		rbLegal = (RadioButton) mMainView.findViewById(R.id.rb_legal);
		rbAppoint = (RadioButton) mMainView.findViewById(R.id.rb_appoint);
		// etbnftNumber = (EditText) mMainView.findViewById(R.id.et_bnftNumber);
//		spbnftType = (Spinner) mMainView.findViewById(R.id.sp_bnftType);
		etbnftName = (EditText) mMainView.findViewById(R.id.et_bnftName);
		rbMan = (RadioButton) mMainView.findViewById(R.id.rb_man);
		rbWoman = (RadioButton) mMainView.findViewById(R.id.rb_woman);
		tvbnftBirth = (TextView) mMainView.findViewById(R.id.tv_bnftBirth);
		spbnftToBen = (Spinner) mMainView.findViewById(R.id.sp_bnftToBen);
		// etbnftOrder = (EditText) mMainView.findViewById(R.id.et_bnftOrder);
		// etbnftProportion = (EditText)
		// mMainView.findViewById(R.id.et_bnftProportion);
		spbnftIdType = (Spinner) mMainView.findViewById(R.id.sp_bnftIdType);
		etbnftIdNumber = (EditText) mMainView.findViewById(R.id.et_bnftIdNumber);
		tvbnftIdBeginDate = (TextView) mMainView.findViewById(R.id.tv_bnftIdBeginDate);
		tvbnftIdEndDate = (TextView) mMainView.findViewById(R.id.tv_bnftIdEndDate);
		cbIsForever = (CheckBox) mMainView.findViewById(R.id.cb_isForever);
		spAdressProvince = (Spinner) mMainView.findViewById(R.id.sp_adress_province);
		spAdressCity = (Spinner) mMainView.findViewById(R.id.sp_adress_city);
		spAdressCounty = (Spinner) mMainView.findViewById(R.id.sp_adress_county);
		etAdressDetail = (EditText) mMainView.findViewById(R.id.et_adress_detail);
//		tvBnftPaymentMethod = (TextView) mMainView.findViewById(R.id.tv_bnftPaymentMethod);
		rbHealthInformY = (RadioButton) mMainView.findViewById(R.id.rb_healthInformY);
		rbProfessionalInformY = (RadioButton) mMainView.findViewById(R.id.rb_professionalInformY);
//		spProcessingMode = (Spinner) mMainView.findViewById(R.id.sp_processingMode);
//		etArbitrationName = (EditText) mMainView.findViewById(R.id.et_arbitrationName);
		etServiceRecommNo = (EditText) mMainView.findViewById(R.id.et_serviceRecommNo);
		spBonusGetMode = (Spinner) mMainView.findViewById(R.id.sp_bonusGetMode);
		cbMantainMethod = (CheckBox) mMainView.findViewById(R.id.cb_mantainMethod);
		spBankAccount = (Spinner) mMainView.findViewById(R.id.sp_bankAccount);
		spGetYearFlag = (Spinner) mMainView.findViewById(R.id.sp_getYearFlag);
		etGetYear = (EditText) mMainView.findViewById(R.id.et_getYear);
		spInvestTimeType = (Spinner) mMainView.findViewById(R.id.sp_investTimeType);
		rbAutoPayFlagY = (RadioButton) mMainView.findViewById(R.id.rb_autoPayFlagY);
		etApplAccName = (EditText) mMainView.findViewById(R.id.et_applAccName);
		tvOpeningBank = (TextView) mMainView.findViewById(R.id.tv_openingBank);
		etGetStartAge = (EditText) mMainView.findViewById(R.id.et_getStartAge);
		rbPolicyHandFlag_Email = (RadioButton) mMainView.findViewById(R.id.rb_policyHandFlag1);
		rbPolicyHandFlag_Mail = (RadioButton) mMainView.findViewById(R.id.rb_policyHandFlag2);
		spPostAddrProvince = (Spinner) mMainView.findViewById(R.id.sp_postAddr_province);
		spPostAddrCity = (Spinner) mMainView.findViewById(R.id.sp_postAddr_city);
		spPostAddrCounty = (Spinner) mMainView.findViewById(R.id.sp_postAddr_county);
		etPostAddrDetail = (EditText) mMainView.findViewById(R.id.et_postAddr_detail);
		// 606 指定受益人增加国籍、职业代码、联系电话
		spNationality = (Spinner) mMainView.findViewById(R.id.sp_nationality);
		etPhone = (EditText) mMainView.findViewById(R.id.et_phone);
		spJob = (Spinner) mMainView.findViewById(R.id.sp_job);
	}

	@Override
	public void viewSet() {
		isShowed = true;
//		List<String[]> c = SafetyDataCenter.getInstance().getControlInfoC();
		List<String[]> e = SafetyDataCenter.getInstance().getControlInfoE();
//
//		for (int i = 0; i < c.size(); i++) {
//			String[] str = c.get(i);
//			String strCode = str[0];
//			if (strCode.equals("C1")) {
//				// 姓名
//				EditTextUtils.setLengthMatcher(getActivity(), etbnftName, Integer.valueOf(str[2]));
//			} else if (strCode.equals("C5")) {
//				// 证件号码
//				EditTextUtils.setLengthMatcher(getActivity(), etbnftIdNumber, Integer.valueOf(str[2]));
//			} else if (strCode.equals("C8")) {
//				// 通讯地址
//				adressDetailLength = Integer.valueOf(str[2]);
//				postAdressDetailLength = adressDetailLength;
//				twlAdressDetail = new TextWatcherLimit(getActivity(), etAdressDetail, adressDetailLength);
//				twlPostAddrDetail = new TextWatcherLimit(getActivity(), etPostAddrDetail, postAdressDetailLength);
//				etAdressDetail.addTextChangedListener(twlAdressDetail);
//				etPostAddrDetail.addTextChangedListener(twlPostAddrDetail);
//			}
//		}
//
		for (int i = 0; i < e.size(); i++) {
			String[] str = e.get(i);
			String strCode = str[0];
			String isMustInput = str[1];
			if (strCode.equals("E2")) {
				// 红利领取方式
				if (isMustInput.equals("0") /*|| isMustInput.equals("1")*/) {
					mMainView.findViewById(R.id.ll_bonusGetMode).setVisibility(View.VISIBLE);
					SafetyUtils.initSpinnerView(getActivity(), spBonusGetMode, Arrays.asList(getResources().getStringArray(R.array.bonusGetMode_CN)));
				}
			} else if (strCode.equals("E3")) {
				// 保费自动垫交
				if (isMustInput.equals("0") /*|| isMustInput.equals("1")*/) {
					mMainView.findViewById(R.id.ll_autoPayFlag).setVisibility(View.VISIBLE);
				}
			}
//			// else if (strCode.equals("E5")) {
//			// // 投保人账户姓名
//			// etApplAccName.setFilters(new InputFilter[] { new
//			// InputFilter.LengthFilter(Integer.valueOf(str[2])) });
//			// } else if (strCode.equals("E6")) {
//			// // 开户行
//			// etOpeningBank.setFilters(new InputFilter[] { new
//			// InputFilter.LengthFilter(Integer.valueOf(str[2])) });
//			// }
			else if (strCode.equals("E8")) {
				// 年金/生存金领取方式
				if (isMustInput.equals("0") /*|| isMustInput.equals("1")*/) {
					mMainView.findViewById(R.id.ll_getYearFlag).setVisibility(View.VISIBLE);
					SafetyUtils.initSpinnerView(getActivity(), spGetYearFlag, Arrays.asList(getResources().getStringArray(R.array.getYearFlag_CN)));
				}
			} else if (strCode.equals("E9")) {
				// 领取年龄
				if (isMustInput.equals("0") /*|| isMustInput.equals("1")*/) {
					mMainView.findViewById(R.id.ll_getStartAge).setVisibility(View.VISIBLE);
					EditTextUtils.setLengthMatcher(getActivity(), etGetStartAge, 4);
//					if (isMustInput.equals("0")) {
						etGetStartAge.setTag(true);
//					} else {
//						etGetStartAge.setTag(false);
//						etGetStartAge.setHint(getResources().getString(R.string.hint_memo));
//					}
				}
			} else if (strCode.equals("E10")) {
				// 领取期限
				if (isMustInput.equals("0") /*|| isMustInput.equals("1")*/) {
					mMainView.findViewById(R.id.ll_getYear).setVisibility(View.VISIBLE);
					EditTextUtils.setLengthMatcher(getActivity(), etGetYear, 4);
//					if (isMustInput.equals("0")) {
						etGetYear.setTag(true);
//					} else {
//						etGetYear.setTag(false);
//						etGetYear.setHint(getResources().getString(R.string.hint_memo));
//					}
				}
//			} else if (strCode.equals("E14")) {
//				// 仲裁机构名称
//				EditTextUtils.setLengthMatcher(getActivity(), etArbitrationName, Integer.valueOf(str[2]));
			} else if (strCode.equals("E15")) {
				// 投资时间选择
				if (isMustInput.equals("0")/* || isMustInput.equals("1")*/) {
					mMainView.findViewById(R.id.ll_investTimeType).setVisibility(View.VISIBLE);
					SafetyUtils.initSpinnerView(getActivity(), spInvestTimeType, Arrays.asList(getResources().getStringArray(R.array.investTimeType_CN)));
				}
			}
		}

		EditTextUtils.setLengthMatcher(getActivity(), etbnftName, 80);
		EditTextUtils.setLengthMatcher(getActivity(), etbnftIdNumber, 20);
		EditTextUtils.setLengthMatcher(getActivity(), etPhone, 18);
//		adressDetailLength = 80;
//		postAdressDetailLength = adressDetailLength;
//		twlAdressDetail = new TextWatcherLimit(getActivity(), etAdressDetail, adressDetailLength);
//		twlPostAddrDetail = new TextWatcherLimit(getActivity(), etPostAddrDetail, postAdressDetailLength);
//		etAdressDetail.addTextChangedListener(twlAdressDetail);
//		etPostAddrDetail.addTextChangedListener(twlPostAddrDetail);

//		EditTextUtils.setLengthMatcher(getActivity(), etArbitrationName, 80);
		EditTextUtils.setLengthMatcher(getActivity(), etServiceRecommNo, 5);

		setShowDateView(tvbnftBirth, null, SafetyDataCenter.getInstance().getSysTime(), null);
		setShowDateView(tvbnftIdBeginDate, null, null, null);
		setShowDateView(tvbnftIdEndDate, null, null, null);
		// 606 指定受益人增加国籍、职业代码、联系电话
		listCountry = SafetyUtils.getCountry(getActivity());
		SafetyUtils.initSpinnerView(getActivity(), spNationality, PublicTools.getSpinnerData(listCountry, Safety.NAME));
		spNationality.setSelection(46);
		SafetyUtils.initSpinnerView(getActivity(), spJob,Arrays.asList(getResources().getStringArray(R.array.Job_CN)));

//		SafetyUtils.initSpinnerView(getActivity(), spbnftType, Arrays.asList(getResources().getStringArray(R.array.bnftType_CN)));
		SafetyUtils.initSpinnerView(getActivity(), spbnftIdType, SafetyDataCenter.credTypeList);
		SafetyUtils.initSpinnerView(getActivity(), spAdressProvince, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getProviceList(), Safety.NAME));
		SafetyUtils.initSpinnerView(getActivity(), spPostAddrProvince, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getProviceList(), Safety.NAME));
		// SafetyUtils.initSpinnerView(getActivity(), spbnftPaymentMethod,
		// Arrays.asList(getResources().getStringArray(R.array.PaymentMethod_CN)));
//		SafetyUtils.initSpinnerView(getActivity(), spProcessingMode, Arrays.asList(getResources().getStringArray(R.array.processingMode_CN)));

		// 保险公司方面说业务确认生产上销售的产品在缴费年期类型和缴费方式两个字典中的交集中，这里用缴费年期类型取缴费方式字典
//		tvBnftPaymentMethod.setText(getResources().getStringArray(R.array.PaymentMethod_CN)[Arrays.asList(getResources().getStringArray(R.array.PaymentMethod_code)).indexOf(SafetyDataCenter.getInstance().getMapUserInput().get(Safety.PAYYEARTYPE))]);
		// 缴费年期类型为01趸缴，续期缴费方式不显示
		if (SafetyDataCenter.getInstance().getMapUserInput().get(Safety.PAYYEARTYPE).equals("01")) {
			mMainView.findViewById(R.id.ll_mantainMethod).setVisibility(View.GONE);
			mMainView.findViewById(R.id.ll_applAccName).setVisibility(View.GONE);
			mMainView.findViewById(R.id.ll_openingBank).setVisibility(View.GONE);
			mMainView.findViewById(R.id.ll_bankAccount).setVisibility(View.GONE);
		} else {
			mMainView.findViewById(R.id.ll_mantainMethod).setVisibility(View.VISIBLE);
			cbMantainMethod.setOnCheckedChangeListener(manTainMethodListener);
			cbMantainMethod.setChecked(false);
		}

		// 保单提供方式
		String policyHandFlag = (String) SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.POLICYHANDFLAG);
		if (policyHandFlag.equals("1")) {
			rbPolicyHandFlag_Email.setVisibility(View.VISIBLE);
			rbPolicyHandFlag_Mail.setVisibility(View.GONE);
			rbPolicyHandFlag_Email.setSelected(true);
			rbPolicyHandFlag_Email.setChecked(true);
		} else if (policyHandFlag.equals("2")) {
			rbPolicyHandFlag_Mail.setVisibility(View.VISIBLE);
			rbPolicyHandFlag_Email.setVisibility(View.GONE);
			rbPolicyHandFlag_Mail.setSelected(true);
			rbPolicyHandFlag_Mail.setChecked(true);
			mMainView.findViewById(R.id.ll_postAddr).setVisibility(View.VISIBLE);
		} else if (policyHandFlag.equals("3")) {
			rbPolicyHandFlag_Email.setVisibility(View.VISIBLE);
			rbPolicyHandFlag_Mail.setVisibility(View.VISIBLE);
			rbPolicyHandFlag_Email.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mMainView.findViewById(R.id.ll_postAddr).setVisibility(View.GONE);
				}
			});

			rbPolicyHandFlag_Mail.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mMainView.findViewById(R.id.ll_postAddr).setVisibility(View.VISIBLE);
				}
			});
			rbPolicyHandFlag_Email.setChecked(true);
			rbPolicyHandFlag_Email.performClick();
		}
		
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

		cbIsForever.setOnCheckedChangeListener(isForeverListener);
		
		rbLegal.setOnClickListener(bnftTypeListener);
		rbAppoint.setOnClickListener(bnftTypeListener);
//		spProcessingMode.setOnItemSelectedListener(processingModeListener);
		spAdressProvince.setOnItemSelectedListener(adressListener);
		spAdressCity.setOnItemSelectedListener(adressListener);
		spPostAddrProvince.setOnItemSelectedListener(adressListener);
		spPostAddrCity.setOnItemSelectedListener(adressListener);
		spbnftToBen.setOnItemSelectedListener(bnftToBenListener);
		rbLegal.performClick();
		tvbnftBirth.setTag("noPopup");
		tvbnftIdBeginDate.setTag("noPopup");
		tvbnftIdEndDate.setTag("noPopup");
		mMainView.findViewById(R.id.tv_tip1).setTag("noPopup");
		mMainView.findViewById(R.id.tv_tip2).setTag("noPopup");
		
//		mMainView.findViewById(R.id.rb_man).setOnClickListener(genderClickListener);
//		mMainView.findViewById(R.id.rb_woman).setOnClickListener(genderClickListener);
		SafetyUtils.initSpinnerView(getActivity(), spbnftToBen, SafetyDataCenter.relation);
		rbMan.performClick();
		PopupWindowUtils.getInstance().setOnShowAllTextListenerByViewGroup(getActivity(), (LinearLayout) mMainView.findViewById(R.id.ll_content));

		rbLegal.setFocusable(true);
		rbLegal.setFocusableInTouchMode(true);
		rbLegal.requestFocus();
		rbLegal.requestFocusFromTouch();


	}
	
	private void refreshView() {
		// 保险公司方面说业务确认生产上销售的产品在缴费年期类型和缴费方式两个字典中的交集中，这里用缴费年期类型取缴费方式字典
//		tvBnftPaymentMethod.setText(getResources().getStringArray(R.array.PaymentMethod_CN)[Arrays.asList(getResources().getStringArray(R.array.PaymentMethod_code)).indexOf(SafetyDataCenter.getInstance().getMapUserInput().get(Safety.PAYYEARTYPE))]);
		// 缴费年期类型为01趸缴，续期缴费方式不显示
		if (SafetyDataCenter.getInstance().getMapUserInput().get(Safety.PAYYEARTYPE).equals("01")) {
			mMainView.findViewById(R.id.ll_mantainMethod).setVisibility(View.GONE);
			mMainView.findViewById(R.id.ll_applAccName).setVisibility(View.GONE);
			mMainView.findViewById(R.id.ll_openingBank).setVisibility(View.GONE);
			mMainView.findViewById(R.id.ll_bankAccount).setVisibility(View.GONE);
		} else {
			mMainView.findViewById(R.id.ll_mantainMethod).setVisibility(View.VISIBLE);
			cbMantainMethod.setOnCheckedChangeListener(manTainMethodListener);
			cbMantainMethod.setChecked(false);
			
			spBankAccount.setSelection(accSpSelectPosition);
		}

		if (rbAppoint.isChecked() && spbnftToBen.getSelectedItemPosition() == 0) {
			changeBnftViewState(isEnabled);
		}
	}

	/** 设置账户详情数据 */
	@SuppressWarnings("unchecked")
	public void setAccDetail(Map<String, Object> accDetailMap) {
//		strOpeningBank = (String) accDetailMap.get(Acc.ACCOPENBANK);
//		tvOpeningBank.setText(strOpeningBank);
//		if ("101".equals(SafetyDataCenter.getInstance().getAcctList().get(accSpSelectPosition).get(Acc.ACC_ACCOUNTTYPE))) {
			List<Map<String, Object>> accountDetailList = (List<Map<String, Object>>) accDetailMap.get(ConstantGloble.ACC_DETAILIST);
			int i = 0;
			for (i = 0; i < accountDetailList.size(); i++) {
				Map<String, Object> map = accountDetailList.get(i);
				if (map.get(Safety.CURRENCYCODE).equals("001")) {
					haveRMB = true;
					break;
				}
			}
			if (i >= accountDetailList.size()) {
				haveRMB = false;
				BaseDroidApp.getInstanse().showInfoMessageDialog("您当前选择的账户只支持外币，请选择其他人民币账户作为续期缴费账户。");
				return;
			}
//		}
	}
	
	/** 详情报错时接收报错信息 */
	public void doHttpErrorHandler(BiiError biiError) {
		errorInfo = biiError.getMessage();
	}

	@Override
	public boolean submit() {
		try {
			if (inputRegexp()) {
				Map<String, Object> userInput = SafetyDataCenter.getInstance().getMapUserInput();
				userInput.put(Safety.BNFINDICATOR, rbLegal.isChecked() ? "Y" : "N");
				userInput.put(Safety.BENCOUNT, rbLegal.isChecked() ? "0" : "1");
				// 受益人信息
				List<Map<String, Object>> bnftList = new ArrayList<Map<String, Object>>();
				if (rbAppoint.isChecked()) {
					Map<String, Object> bnftInfo = new HashMap<String, Object>();
					bnftInfo.put(Safety.BNFTNAME, etbnftName.getText().toString().trim());
					bnftInfo.put(Safety.BNFTSEX, rbMan.isChecked() ? "1" : "0");
					bnftInfo.put(Safety.BNFTBIRTH, tvbnftBirth.getText().toString());
					bnftInfo.put(Safety.BNFTORDER, "1");
					bnftInfo.put(Safety.PERCENT, "100");
					bnftInfo.put(Safety.BNFTIDTYPE, SafetyDataCenter.rqcredType.get(spbnftIdType.getSelectedItemPosition()));
					bnftInfo.put(Safety.BNFTIDNO, etbnftIdNumber.getText().toString().trim());
					bnftInfo.put(Safety.BNFTSTARTDATE, tvbnftIdBeginDate.getText().toString());
					bnftInfo.put(Safety.BNFTENDDATE, StringUtil.isNull(tvbnftIdEndDate.getText().toString()) ? "2100/12/31" : tvbnftIdEndDate.getText().toString());
					SafetyDataCenter.getInstance().getMapCarSafetyUserInput().put(Safety.BNFT_IS_FOREVER, cbIsForever.isChecked());
					bnftInfo.put(Safety.BNFTHOMEADDR, spAdressProvince.getSelectedItem().toString() + spAdressCity.getSelectedItem().toString() + spAdressCounty.getSelectedItem().toString() + etAdressDetail.getText().toString().trim());
					// 606 指定受益人增加国籍、职业代码、联系电话
					bnftInfo.put("bnftCountry", listCountry.get(spNationality.getSelectedItemPosition()).get(Safety.CODE));
					bnftInfo.put("bnftCountryName", listCountry.get(spNationality.getSelectedItemPosition()).get(Safety.NAME));
					bnftInfo.put("bnftMobilePhone", etPhone.getText().toString().trim());
					bnftInfo.put("bnftJobCode", getResources().getStringArray(R.array.Job_code)[spJob.getSelectedItemPosition()]);
//					bnftInfo.put(Safety.BNFTTYPE, getResources().getStringArray(R.array.bnftType_code)[spbnftType.getSelectedItemPosition()]);
//					if (rbMan.isChecked()) {
//						bnftInfo.put(Safety.BNFTRELATION, getResources().getStringArray(R.array.guanxi_M_Code)[spbnftToBen.getSelectedItemPosition()]);
//					} else {
//						bnftInfo.put(Safety.BNFTRELATION, getResources().getStringArray(R.array.guanxi_W_Code)[spbnftToBen.getSelectedItemPosition()]);
//					}
					bnftInfo.put(Safety.BNFTRELATION, SafetyDataCenter.relationrq.get(spbnftToBen.getSelectedItemPosition()));
					bnftList.add(bnftInfo);
				}
				userInput.put(Safety.BENFTLIST, bnftList);
				// 其他信息
				// userInput.put(Safety.TRADEWAY,
				// getResources().getStringArray(R.array.PaymentMethod_code)[spbnftPaymentMethod.getSelectedItemPosition()]);
				// 这里取缴费年期类型的数据
				userInput.put(Safety.TRADEWAY, userInput.get(Safety.PAYYEARTYPE));
				if (mMainView.findViewById(R.id.ll_investTimeType).getVisibility() == View.VISIBLE) {
					// 受产品栏位控制信息控制
					userInput.put(Safety.INVESTTIMETYPE, getResources().getStringArray(R.array.investTimeType_code)[spInvestTimeType.getSelectedItemPosition()]);
				}
				if (mMainView.findViewById(R.id.ll_bonusGetMode).getVisibility() == View.VISIBLE) {
					// 受产品栏位控制信息控制
					userInput.put(Safety.BONUSGETMODE, getResources().getStringArray(R.array.bonusGetMode_code)[spBonusGetMode.getSelectedItemPosition()]);
				}
				if (mMainView.findViewById(R.id.ll_autoPayFlag).getVisibility() == View.VISIBLE) {
					// 受产品栏位控制信息控制
					userInput.put(Safety.AUTOPAYFLAG, rbAutoPayFlagY.isChecked() ? "2" : "1");
				}
				if (mMainView.findViewById(R.id.ll_mantainMethod).getVisibility() == View.VISIBLE) {
					// 受缴费种类信息中缴费年期类型信息控制
					userInput.put(Safety.MANTAINMETHOD, cbMantainMethod.isChecked() ? "1" : "2");

					if (cbMantainMethod.isChecked()) {
						// 当续期缴费方式选择“1：银行转账”，则网银页面展示银行账号、投保人账户姓名、开户行；银行账号支持客户选择，投保人账户姓名、开户行可以根据银行账号选择进行回显
						userInput.put(Safety.APPLACCNAME, userInput.get(Safety.APPL_NAME));
						userInput.put(Safety.OPENINGBANK, strOpeningBank);
						userInput.put(Safety.BANKACCOUNT, SafetyDataCenter.getInstance().getAcctList().get(spBankAccount.getSelectedItemPosition()).get(Comm.ACCOUNTNUMBER));
						userInput.put(Safety.ACC_ID, SafetyDataCenter.getInstance().getAcctList().get(spBankAccount.getSelectedItemPosition()).get(Comm.ACCOUNT_ID));
					} else {
						userInput.remove(Safety.APPLACCNAME);
						userInput.remove(Safety.OPENINGBANK);
						userInput.remove(Safety.BANKACCOUNT);
						userInput.remove(Safety.ACC_ID);
					}
				}
				if (mMainView.findViewById(R.id.ll_getYearFlag).getVisibility() == View.VISIBLE) {
					// 受产品栏位控制信息控制
					userInput.put(Safety.GETYEARFLAG, getResources().getStringArray(R.array.getYearFlag_code)[spGetYearFlag.getSelectedItemPosition()]);
				}
				if (mMainView.findViewById(R.id.ll_getStartAge).getVisibility() == View.VISIBLE) {
					// 受产品栏位控制信息控制
					String getStartAge = etGetStartAge.getText().toString().trim();
					if ((Boolean) etGetStartAge.getTag()) {
						userInput.put(Safety.GETSTARTAGE, getStartAge);
					} else {
						if (StringUtil.isNull(getStartAge)) {
							SafetyDataCenter.getInstance().getMapCarSafetyUserInput().put(Safety.GETSTARTAGE, "-");
						} else {
							userInput.put(Safety.GETSTARTAGE, getStartAge);
						}
					}
				}
				if (mMainView.findViewById(R.id.ll_getYear).getVisibility() == View.VISIBLE) {
					// 受产品栏位控制信息控制
					String getYear = etGetYear.getText().toString().trim();
					if ((Boolean) etGetYear.getTag()) {
						userInput.put(Safety.GETYEAR, getYear);
					} else {
						if (StringUtil.isNull(getYear)) {
							SafetyDataCenter.getInstance().getMapCarSafetyUserInput().put(Safety.GETYEAR, "-");
						} else {
							userInput.put(Safety.GETYEAR, getYear);
						}
					}
				}
				userInput.put(Safety.HEALTHFLAG, rbHealthInformY.isChecked() ? "Y" : "N");
				userInput.put(Safety.OCCUPATIONFLAG, rbProfessionalInformY.isChecked() ? "Y" : "N");
				userInput.put(Safety.DISPUTESHANDLETYPE, "1");
//				if (spProcessingMode.getSelectedItemPosition() == 1) {
//					// 当合同争议处理方式为“2：仲裁”时必填（下拉框下标为1）
//					userInput.put(Safety.ARBITRATIONNAME, etArbitrationName.getText().toString().trim());
//				} else {
//					userInput.remove(Safety.ARBITRATIONNAME);
//				}
				userInput.put(Safety.ISEVALUATED, "Y");

				String businessNum = etServiceRecommNo.getText().toString().trim();
				if (!StringUtil.isNull(businessNum)) {
					// 业务推荐号选填
					userInput.put(Safety.BUSINESSNUM, businessNum);
				} else {
					userInput.remove(Safety.BUSINESSNUM);
				}

				if (SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.POLICYHANDFLAG).equals("3")) {
					if (rbPolicyHandFlag_Email.isChecked()) {
						userInput.put(Safety.POLICYHANDFLAG, "1");
					} else {
						userInput.put(Safety.POLICYHANDFLAG, "2");
						userInput.put(Safety.POSTADDR, getSpSelectItemStr(spPostAddrProvince) + getSpSelectItemStr(spPostAddrCity) + getSpSelectItemStr(spPostAddrCounty) + etPostAddrDetail.getText().toString().trim());
					}
				} else if (SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.POLICYHANDFLAG).equals("2")) {
					userInput.put(Safety.POLICYHANDFLAG, "2");
					userInput.put(Safety.POSTADDR, getSpSelectItemStr(spPostAddrProvince) + getSpSelectItemStr(spPostAddrCity) + getSpSelectItemStr(spPostAddrCounty) + etPostAddrDetail.getText().toString().trim());
				} else {
					userInput.put(Safety.POLICYHANDFLAG, "1");
				}
				return true;
			}
		} catch (Exception e) {
			LogGloble.e(TAG, e.getMessage());
		}
		return false;
	}

	/** 输入字段校验 */
	private boolean inputRegexp() {
		if (rbAppoint.isChecked()) {
			// 受益人指定时校验受益人信息
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			if (onlyRegular(true, etbnftName.getText().toString().trim())) {
				RegexpBean rb = new RegexpBean(SafetyConstant.BNFT_NAME, etbnftName.getText().toString().trim(), SafetyConstant.NOTEMPTY);
				rb.setExtentParam(etbnftName);
				lists.add(rb);
			}
			RegexpBean result1 = RegexpUtils.regexpDateWithRgexpBean(lists, null);
			if (result1 != null) {
				View v = (View) result1.getExtentParam();
				changeViewBg(v);
				return false;
			}

			if (spbnftIdType.getSelectedItemPosition() == 0 || spbnftIdType.getSelectedItemPosition() == 1 || spbnftIdType.getSelectedItemPosition() == 3 || spbnftIdType.getSelectedItemPosition() == 4) {
				if (onlyRegular(true, etbnftIdNumber.getText().toString().trim())) {
					ArrayList<RegexpBean> list = new ArrayList<RegexpBean>();
					RegexpBean rb = new RegexpBean(SafetyConstant.BNFT_IDENTITYNUM, etbnftIdNumber.getText().toString(), SafetyConstant.CAROWNER_ID);
					rb.setExtentParam(etbnftIdNumber);
					list.add(rb);
					RegexpBean result = RegexpUtils.regexpDateWithRgexpBean(list, null);
					if (result != null) {
						View v = (View) result.getExtentParam();
						changeViewBg(v);
						return false;
					}
				}
				if (spbnftIdType.getSelectedItemPosition() != 4) {
					String idNumber = etbnftIdNumber.getText().toString().trim();
					if (SafetyUtils.getGender(idNumber).equals("1") && !rbMan.isChecked() || SafetyUtils.getGender(idNumber).equals("0") && rbMan.isChecked()) {
						BaseDroidApp.getInstanse().showInfoMessageDialog("性别同身份证不一致,请修改");
						changeViewBg(etbnftIdNumber);
						return false;
					}
				}
			} else if (spbnftIdType.getSelectedItemPosition() == 5) {
				// 请输入一位汉字开头+8位数字
				if (onlyRegular(true, etbnftIdNumber.getText().toString().trim())) {
					ArrayList<RegexpBean> list = new ArrayList<RegexpBean>();
					RegexpBean rb = new RegexpBean(SafetyConstant.BNFT_IDENTITYNUM, etbnftIdNumber.getText().toString().trim(), SafetyConstant.LIFEWUJINGIDTYPE);
					rb.setExtentParam(etbnftIdNumber);
					list.add(rb);
					RegexpBean result = RegexpUtils.regexpDateWithRgexpBean(list, null);
					if (result != null) {
						View v = (View) result.getExtentParam();
						changeViewBg(v);
						return false;
					}
				}
			} else {
				if (onlyRegular(true, etbnftIdNumber.getText().toString().trim())) {
					ArrayList<RegexpBean> list = new ArrayList<RegexpBean>();
					RegexpBean rb = new RegexpBean(SafetyConstant.BNFT_IDENTITYNUM, etbnftIdNumber.getText().toString().trim(), SafetyConstant.CARSAFETY_OTHERIDTYPE);
					rb.setExtentParam(etbnftIdNumber);
					list.add(rb);
					RegexpBean result = RegexpUtils.regexpDateWithRgexpBean(list, null);
					if (result != null) {
						View v = (View) result.getExtentParam();
						changeViewBg(v);
						return false;
					}
				}
			}

			String beginDate = tvbnftIdBeginDate.getText().toString();
			String endDate = tvbnftIdEndDate.getText().toString();
			String birthDate = tvbnftBirth.getText().toString();

			if (QueryDateUtils.compareDate(beginDate, birthDate) || beginDate.equals(birthDate)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("证件生效日不能小于等于出生日期");
				changeViewBg(tvbnftIdBeginDate);
				if (StringUtil.isNull(tvbnftIdEndDate.getText().toString())) {
					tvbnftIdEndDate.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner_default));
				} else {
					tvbnftIdEndDate.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner));
				}
				return false;
			}

			if (!QueryDateUtils.compareDate(beginDate, SafetyDataCenter.getInstance().getSysTime()) || beginDate.equals(SafetyDataCenter.getInstance().getSysTime())) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("证件生效日不能大于等于系统日期");
				changeViewBg(tvbnftIdBeginDate);
				if (StringUtil.isNull(tvbnftIdEndDate.getText().toString())) {
					tvbnftIdEndDate.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner_default));
				} else {
					tvbnftIdEndDate.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner));
				}
				return false;
			}

//			if (!QueryDateUtils.compareDate(beginDate, endDate) || beginDate.equals(endDate)) {
//				BaseDroidApp.getInstanse().showInfoMessageDialog("证件生效日期不能大于等于失效日期");
//				changeViewBg(tvbnftIdEndDate);
//				tvbnftIdBeginDate.setBackground(getResources().getDrawable(R.drawable.bg_spinner));
//				return false;
//			}
			
			if (!StringUtil.isNull(endDate)) {
				if (QueryDateUtils.compareDate(endDate, SafetyDataCenter.getInstance().getSysTime()) || endDate.equals(SafetyDataCenter.getInstance().getSysTime())) {
					BaseDroidApp.getInstanse().showInfoMessageDialog("证件到期日不能小于等于系统日期");
					changeViewBg(tvbnftIdEndDate);
					tvbnftIdBeginDate.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner));
					return false;
				}
			}

			if (spbnftIdType.getSelectedItemPosition() == 0 || spbnftIdType.getSelectedItemPosition() == 1 || spbnftIdType.getSelectedItemPosition() == 3) {
				String idNumber = etbnftIdNumber.getText().toString();
				String idBirthDate = null;
				idBirthDate = idNumber.substring(6, 10) + "/" + idNumber.substring(10, 12) + "/" + idNumber.substring(12, 14);

				if (!StringUtil.isNull(idBirthDate) && !birthDate.equals(idBirthDate)) {
					BaseDroidApp.getInstanse().showInfoMessageDialog("受益人出生日期同身份证不一致，请修改");
					changeViewBg(tvbnftBirth);
					return false;
				}
			}

//			if (spbnftType.getSelectedItemPosition() == 0 && spbnftToBen.getSelectedItemPosition() == 0) {
//				// 如果受益人类别选择身故受益人，受益人是被保人的不能选本人
//				BaseDroidApp.getInstanse().showInfoMessageDialog("受益人类别为身故受益人时，受益人不能为被保人本人");
//				changeViewBg(spbnftToBen);
//				return false;
//			}

			lists.clear();
			String adress = spAdressProvince.getSelectedItem().toString() + spAdressCity.getSelectedItem().toString() + spAdressCounty.getSelectedItem().toString() + etAdressDetail.getText().toString().trim();
			if (onlyRegular(true, adress)) {
				RegexpBean rb = new RegexpBean(SafetyConstant.BNFT_ADRESS, adress, SafetyConstant.LIFEADRESS);
				rb.setExtentParam(etAdressDetail);
				lists.add(rb);
			}

			if (onlyRegular(true, etAdressDetail.getText().toString().trim())) {
				RegexpBean rb = new RegexpBean(SafetyConstant.BNFT_ADRESS, etAdressDetail.getText().toString().trim(), SafetyConstant.LIFEADRESS);
				rb.setExtentParam(etAdressDetail);
				lists.add(rb);
			}

			// 606 指定受益人增加国籍、职业代码、联系电话
			if (onlyRegular(true, etPhone.getText().toString().trim())) {
				RegexpBean rb = new RegexpBean(SafetyConstant.MOBILE_PHONE_BUYER, etPhone.getText().toString().trim(), SafetyConstant.LIFEPHONE);
				rb.setExtentParam(etPhone);
				lists.add(rb);
			}


			try {
				String adressByte = new String(adress.getBytes("GB2312"), "ISO-8859-1");
				if (adressByte.length() > 80) {
					BaseDroidApp.getInstanse().showInfoMessageDialog("受益人通讯地址请输入中文、字母或数字，可包含',.-/()和空格，最大长度80个字符");
					return false;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			RegexpBean result2 = RegexpUtils.regexpDateWithRgexpBean(lists, null);
			if (result2 != null) {
				View v = (View) result2.getExtentParam();
				changeViewBg(v);
				return false;
			}
		}

//		if (rbHealthInformY.isChecked() || rbProfessionalInformY.isChecked()) {
//			// 有健康告知和职业告知的不能投保
//			BaseDroidApp.getInstanse().showInfoMessageDialog("对不起，您的健康状况或从事职业暂无法完成在线投保，详情请咨询相关客户服务电话。");
//			return false;
//		}

		if (mMainView.findViewById(R.id.ll_bankAccount).getVisibility() == View.VISIBLE) {
			if ((spBankAccount.getSelectedItemPosition() == 0) && (SafetyDataCenter.getInstance().getAcctList().size() != 1)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("请选择银行账户");
				RegexpBean rb = new RegexpBean(null, null, null);
				rb.setExtentParam(spBankAccount);
				return false;
			}
			
			if (!haveRMB) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("您当前选择的账户只支持外币，请选择其他人民币账户作为续期缴费账户。");
				RegexpBean rb = new RegexpBean(null, null, null);
				rb.setExtentParam(spBankAccount);
				return false;
			}
			
			if (!StringUtil.isNull(errorInfo)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(errorInfo);
				RegexpBean rb = new RegexpBean(null, null, null);
				rb.setExtentParam(spBankAccount);
				return false;
			}
		}

		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
//		if (spProcessingMode.getSelectedItemPosition() == 1) {
//			if (onlyRegular(true, etArbitrationName.getText().toString().trim())) {
//				RegexpBean rb = new RegexpBean(SafetyConstant.ARBITRATIONNAME, etArbitrationName.getText().toString(), SafetyConstant.LIFEADRESS);
//				rb.setExtentParam(etArbitrationName);
//				lists.add(rb);
//			}
//		}
		if (onlyRegular(false, etServiceRecommNo.getText().toString().trim())) {
			RegexpBean rb = new RegexpBean(SafetyConstant.SERVICERECOMMNO, etServiceRecommNo.getText().toString(), SafetyConstant.SEVENNUMBER);
			rb.setExtentParam(etServiceRecommNo);
			lists.add(rb);
		}
		if (mMainView.findViewById(R.id.ll_getYear).getVisibility() == View.VISIBLE) {
			if (onlyRegular((Boolean) etGetYear.getTag(), etGetYear.getText().toString().trim())) {
				RegexpBean rb = new RegexpBean(SafetyConstant.GETYRAR, etGetYear.getText().toString(), SafetyConstant.FOURNUMBER);
				rb.setExtentParam(etGetYear);
				lists.add(rb);
			}
		}
		if (mMainView.findViewById(R.id.ll_getStartAge).getVisibility() == View.VISIBLE) {
			if (onlyRegular((Boolean) etGetStartAge.getTag(), etGetStartAge.getText().toString().trim())) {
				RegexpBean rb = new RegexpBean(SafetyConstant.GETSTARTAGE, etGetStartAge.getText().toString(), SafetyConstant.FOURNUMBER);
				rb.setExtentParam(etGetStartAge);
				lists.add(rb);
			}
		}
		if (mMainView.findViewById(R.id.ll_postAddr).getVisibility() == View.VISIBLE) {
			String postAddr = spPostAddrProvince.getSelectedItem().toString() + spPostAddrCity.getSelectedItem().toString() + spPostAddrCounty.getSelectedItem().toString() + etPostAddrDetail.getText().toString().trim();
			if (onlyRegular(true, postAddr)) {
				RegexpBean rb = new RegexpBean(SafetyConstant.POSTADDR, postAddr, SafetyConstant.LIFEADRESS);
				rb.setExtentParam(etPostAddrDetail);
				lists.add(rb);
			}
			if (onlyRegular(true, etPostAddrDetail.getText().toString().trim())) {
				RegexpBean rb = new RegexpBean(SafetyConstant.POSTADDR, etPostAddrDetail.getText().toString().trim(), SafetyConstant.LIFEADRESS);
				rb.setExtentParam(etPostAddrDetail);
				lists.add(rb);
			}

			try {
				String adressByte = new String(postAddr.getBytes("GB2312"), "ISO-8859-1");
				if (adressByte.length() > 80) {
					BaseDroidApp.getInstanse().showInfoMessageDialog("保单邮寄地址请输入中文、字母或数字，可包含',.-/()和空格，最大长度80个字符");
					return false;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		RegexpBean result = RegexpUtils.regexpDateWithRgexpBean(lists, null);
		if (result != null) {
			View v = (View) result.getExtentParam();
			changeViewBg(v);
			return false;
		}
		return true;
	}

	/** 如果做非空和正则校验，则传true，如果不做非空校验，只做正则校验，则传false */
	private boolean onlyRegular(Boolean required, String content) {
		if ((!required && !StringUtil.isNull(content)) || required) {
			return true;
		}
		return false;
	}

	/** 设置受益人各视图可用性及默认反显 */
	private void changeBnftViewState(boolean isEnabled) {
		if (isEnabled) {
			etbnftName.setText("");
			rbMan.setChecked(true);
			setShowDateView(tvbnftBirth, null, SafetyDataCenter.getInstance().getSysTime(), null);
			setShowDateView(tvbnftIdBeginDate, null, null, null);
			tvbnftIdEndDate.setText(SafetyDataCenter.getInstance().getSysTime());
			tvbnftIdEndDate.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner));
			cbIsForever.setChecked(false);
			cbIsForever.setEnabled(true);
			spbnftIdType.setSelection(0);
			etbnftIdNumber.setText("");
			spAdressProvince.setSelection(0);
			etAdressDetail.setText("");


			spNationality.setSelection(46);
			spNationality.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner));
			spJob.setSelection(0);
			spJob.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner));
			etPhone.setText("");

		} else {
			Map<String, Object> userInput = SafetyDataCenter.getInstance().getMapUserInput();
			etbnftName.setText((String) userInput.get(Safety.APPL_NAME));
			if (userInput.get(Safety.APPLSEX).equals("1")) {
				rbMan.setChecked(true);
			} else {
				rbWoman.setChecked(true);
			}
			tvbnftBirth.setText((String) userInput.get(Safety.APPL_BIRTH));
			spbnftIdType.setSelection(SafetyDataCenter.rqcredType.indexOf(userInput.get(Safety.APPL_IDTYPE)));
			etbnftIdNumber.setText((String) userInput.get(Safety.APPL_IDNO));
			tvbnftIdBeginDate.setText((String) userInput.get(Safety.APPLIDSTARTDATE));
			String applIdEndDate = (String) userInput.get(Safety.APPLIDENDDATE);
			if ((Boolean) SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.APPL_IS_FOREVER)) {
				tvbnftIdEndDate.setText("");
				tvbnftIdEndDate.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner_default));
				cbIsForever.setChecked(true);
			} else {
				if (StringUtil.isNull(applIdEndDate)) {
					tvbnftIdEndDate.setText(SafetyDataCenter.getInstance().getSysTime());
				} else {
					tvbnftIdEndDate.setText(applIdEndDate);
				}
				tvbnftIdEndDate.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner));
				cbIsForever.setChecked(false);
			}
			cbIsForever.setEnabled(false);
			int selectProvince = (Integer) SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.APPL_PROVINCE);
			int selectCity = (Integer) SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.APPL_CITY);
			int selectCountry = (Integer) SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.APPL_COUNTY);

			spAdressProvince.setSelection(selectProvince);
			spAdressCity.setSelection(selectCity);
			spAdressCounty.setSelection(selectCountry);
			
			etAdressDetail.setText((String) SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.APPL_ADRESS));

			//606 被保人的如果是本人  国籍、电话、职业都是不可修改的。
			int selectNationa= (Integer) SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get("NationaSelected");
			spNationality.setSelection(selectNationa);
			spNationality.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner_default));
			etPhone.setText((String) userInput.get(Safety.APPLMOBILE));
			int selectJob= (Integer) SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get("JobSelected");
			spJob.setSelection(selectJob);
			spJob.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner_default));

		}

		etbnftName.setEnabled(isEnabled);
		rbMan.setEnabled(isEnabled);
		rbWoman.setEnabled(isEnabled);
		tvbnftBirth.setEnabled(isEnabled);
		spbnftIdType.setEnabled(isEnabled);
		etbnftIdNumber.setEnabled(isEnabled);
		tvbnftIdBeginDate.setEnabled(isEnabled);
		tvbnftIdEndDate.setEnabled(isEnabled);
		spAdressProvince.setEnabled(isEnabled);
		spAdressCity.setEnabled(isEnabled);
		spAdressCounty.setEnabled(isEnabled);
		etAdressDetail.setEnabled(isEnabled);
//606 被保人的如果是本人  国籍、电话、职业都是不可修改的。
		spNationality.setEnabled(isEnabled);
		etPhone.setEnabled(isEnabled);
		spJob.setEnabled(isEnabled);
	}

	/** 选择账户监听 */
	private OnItemSelectedListener chooseAccListener = new OnItemSelectedListener() {

		@SuppressWarnings("unchecked")
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			tvOpeningBank.setText("");
			etApplAccName.setText("");
			errorInfo = "";
			haveRMB = true;
			spBankAccount.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner));
			if (position == 0 && SafetyDataCenter.getInstance().getAcctList().size() != 1) {
				return;
			}
			accSpSelectPosition = position;
			Map<String, Object> logInfo = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA);
			etApplAccName.setText((String) logInfo.get(Inves.CUSTOMERNAME));
			strOpeningBank = (String) SafetyDataCenter.getInstance().getAcctList().get(position).get(Acc.ACC_BRANCHNAME_RES);
			tvOpeningBank.setText(strOpeningBank);
			if ("101".equals(SafetyDataCenter.getInstance().getAcctList().get(position).get(Acc.ACC_ACCOUNTTYPE))) {
				BaseHttpEngine.showProgressDialog();
				BaseHttpEngine.dissmissCloseOfProgressDialog();
				Map<String, Object> params = new HashMap<String, Object>();
				params.put(Comm.ACCOUNT_ID, SafetyDataCenter.getInstance().getAcctList().get(position).get(Comm.ACCOUNT_ID));
				((BaseActivity) getActivity()).getHttpTools().requestHttp(Safety.METHOD_PSNACCOUNTQUERYACCOUNTDETAIL, "requestPsnAccountQueryAccountDetailCallBack", params, false);
				((BaseActivity) getActivity()).getHttpTools().registAllErrorCode(Safety.METHOD_PSNACCOUNTQUERYACCOUNTDETAIL);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};
	
	private OnCheckedChangeListener isForeverListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (isChecked) {
				String oldDate = tvbnftIdEndDate.getText().toString();
				tvbnftIdEndDate.setTag(oldDate);
				tvbnftIdEndDate.setText("");
				tvbnftIdEndDate.setEnabled(false);
				tvbnftIdEndDate.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner_default));
			} else {
				String oldDate = (String) tvbnftIdEndDate.getTag();
				if (StringUtil.isNull(oldDate)) {
					tvbnftIdEndDate.setText(SafetyDataCenter.getInstance().getSysTime());
				} else {
					tvbnftIdEndDate.setText(oldDate);
				}
				tvbnftIdEndDate.setEnabled(true);
				tvbnftIdEndDate.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner));
			}
		}
	};

	/** 性别监听 */
//	private OnClickListener genderClickListener = new OnClickListener() {
//		
//		@Override
//		public void onClick(View v) {
//			switch (v.getId()) {
//			case R.id.rb_man:
//				SafetyUtils.initSpinnerView(getActivity(), spbnftToBen, Arrays.asList(getResources().getStringArray(R.array.guanxi_M_CN)));
//				break;
//
//			case R.id.rb_woman:
//				SafetyUtils.initSpinnerView(getActivity(), spbnftToBen, Arrays.asList(getResources().getStringArray(R.array.guanxi_W_CN)));
//				break;
//			}
//		}
//	};
	
	/** 受益人类型rb点击事件 */
	private OnClickListener bnftTypeListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.rb_legal:
				mMainView.findViewById(R.id.ll_bnftInfo).setVisibility(View.GONE);
				break;

			case R.id.rb_appoint:
				mMainView.findViewById(R.id.ll_bnftInfo).setVisibility(View.VISIBLE);
				break;
			}
		}
	};

	/** 选择续期缴费方式监听 */
	private OnCheckedChangeListener manTainMethodListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (isChecked) {
				mMainView.findViewById(R.id.ll_mantainMethod_tip).setVisibility(View.VISIBLE);
				mMainView.findViewById(R.id.ll_applAccName).setVisibility(View.VISIBLE);
				mMainView.findViewById(R.id.ll_openingBank).setVisibility(View.VISIBLE);
				mMainView.findViewById(R.id.ll_bankAccount).setVisibility(View.VISIBLE);

				List<Map<String, Object>> accList = SafetyDataCenter.getInstance().getAcctList();
				ChooseCardAdapter mAdapter = new ChooseCardAdapter(getActivity(), accList);
				spBankAccount.setAdapter(mAdapter);
				spBankAccount.setOnItemSelectedListener(chooseAccListener);
			} else {
				mMainView.findViewById(R.id.ll_mantainMethod_tip).setVisibility(View.GONE);
				mMainView.findViewById(R.id.ll_applAccName).setVisibility(View.GONE);
				mMainView.findViewById(R.id.ll_openingBank).setVisibility(View.GONE);
				mMainView.findViewById(R.id.ll_bankAccount).setVisibility(View.GONE);
			}
		}
	};

	/** 合同争议处理方式监听 */
//	private OnItemSelectedListener processingModeListener = new OnItemSelectedListener() {
//
//		@Override
//		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//			if (position == 0) {
//				mMainView.findViewById(R.id.ll_arbitrationName).setVisibility(View.GONE);
//			} else {
//				mMainView.findViewById(R.id.ll_arbitrationName).setVisibility(View.VISIBLE);
//			}
//		}
//
//		@Override
//		public void onNothingSelected(AdapterView<?> parent) {
//		}
//	};

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
				if (!isEnabled) {
					spAdressCity.setSelection((Integer) SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.APPL_CITY));
				} else {
					spAdressCity.setSelection(0);
				}
				SafetyUtils.initSpinnerView(getActivity(), spAdressCounty, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getSecondCountryList(), Safety.NAME));
				break;
			case R.id.sp_adress_city:
				// 初始化区县
				String stCountryCode = (String) SafetyDataCenter.getInstance().getSecondCityList().get(position).get(Safety.CODE);
				SafetyDataCenter.getInstance().setSecondCountryList(SafetyUtils.getCountryAndCity(getActivity(), stCountryCode));
				SafetyUtils.initSpinnerView(getActivity(), spAdressCounty, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getSecondCountryList(), Safety.NAME));
				break;
			case R.id.sp_adress_county:
				if (!isEnabled) {
					spAdressCounty.setSelection((Integer) SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.APPL_COUNTY));
				} else {
					spAdressCounty.setSelection(0);
				}
//				etAdressDetail.removeTextChangedListener(twlAdressDetail);
//				try {
//					String str = new String((spAdressProvince.getSelectedItem().toString() + spAdressCity.getSelectedItem().toString() + spAdressCounty.getSelectedItem().toString()).getBytes("GB2312"), "ISO-8859-1");
//					twlAdressDetail = new TextWatcherLimit(getActivity(), etAdressDetail, adressDetailLength - str.length());
//				} catch (UnsupportedEncodingException e) {
//					e.printStackTrace();
//				}
//				etAdressDetail.addTextChangedListener(twlAdressDetail);
				break;
			case R.id.sp_postAddr_province:
				// 初始化城市
				String postAddrCityCode = (String) SafetyDataCenter.getInstance().getProviceList().get(position).get(Safety.CODE);
				SafetyDataCenter.getInstance().setSecondCityList(SafetyUtils.getCountryAndCity(getActivity(), postAddrCityCode));
				String postAddrCountryCode = (String) SafetyDataCenter.getInstance().getSecondCityList().get(0).get(Safety.CODE);
				SafetyDataCenter.getInstance().setSecondCountryList(SafetyUtils.getCountryAndCity(getActivity(), postAddrCountryCode));

				SafetyUtils.initSpinnerView(getActivity(), spPostAddrCity, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getSecondCityList(), Safety.NAME));
				SafetyUtils.initSpinnerView(getActivity(), spPostAddrCounty, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getSecondCountryList(), Safety.NAME));
				break;
			case R.id.sp_postAddr_city:
				// 初始化区县
				String stPostAddrCountryCode = (String) SafetyDataCenter.getInstance().getSecondCityList().get(position).get(Safety.CODE);
				SafetyDataCenter.getInstance().setSecondCountryList(SafetyUtils.getCountryAndCity(getActivity(), stPostAddrCountryCode));
				SafetyUtils.initSpinnerView(getActivity(), spPostAddrCounty, PublicTools.getSpinnerData(SafetyDataCenter.getInstance().getSecondCountryList(), Safety.NAME));
				break;
			case R.id.sp_postAddr_county:
//				etAdressDetail.removeTextChangedListener(twlPostAddrDetail);
//				try {
//					String str = new String((spPostAddrProvince.getSelectedItem().toString() + spPostAddrCity.getSelectedItem().toString() + spPostAddrProvince.getSelectedItem().toString()).getBytes("GB2312"), "ISO-8859-1");
//					twlPostAddrDetail = new TextWatcherLimit(getActivity(), etPostAddrDetail, postAdressDetailLength - str.length());
//				} catch (UnsupportedEncodingException e) {
//					e.printStackTrace();
//				}
//				etAdressDetail.addTextChangedListener(twlPostAddrDetail);
				break;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};

	/** 受益人是被保人的 监听 */
	private OnItemSelectedListener bnftToBenListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			spbnftToBen.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner));
			if (position == 0) {
				isEnabled = false;
			} else {
				isEnabled = true;
			}
			changeBnftViewState(isEnabled);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};
}
