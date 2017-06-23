package com.chinamworld.bocmbci.biz.bocinvt.productlist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.invest.constant.InvestConstant;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 风险评估输入信息页
 * 
 * @author wangmengmeng
 * 
 */
public class InvtEvaluationInputActivity extends BociBaseActivity implements
		OnCheckedChangeListener {
	private static final String TAG = "InvtEvaluationInputActivity";
	/** 输入信息页 */
	private View view;
	/** 客户姓名 */
	private TextView bocinvt_eva_name;
	private String name;
	/** 证件类型 */
	private TextView bocinvt_eva_identitytype;
	private String identitytype;
	/** 证件号码 */
	private TextView bocinvt_eva_identityactnum;
	private String identityactnum;
	/** 性别 */
	private RadioGroup bocinvt_eva_gender;
	/** 男 */
	private RadioButton radio_boy;
	/** 女 */
	private RadioButton radio_girl;
	/** 选择的性别 */
	private String gender;
	/** 生日 */
	private TextView bocinvt_eva_birthday;
	/** 生日 */
	private String birthday;
	/** 地址 */
	private EditText bocinvt_eva_address;
	private String address;
	/** 邮编 */
	private EditText bocinvt_eva_postcode;
	private String postcode;
	/** 电话 */
	private EditText bocinvt_eva_phone;
	private String phone;
	/** 手机 */
	private EditText bocinvt_eva_mobile;
	private String mobile;
	/** Email */
	private EditText bocinvt_eva_email;
	private String email;
	/** 收入 */
	private Spinner bocinvt_eva_revenue;
	private String revenue;
	/** 教育程度 */
	private Spinner bocinvt_eva_edu;
	private String edu;
	/** 所属行业 */
	private Spinner bocinvt_eva_occ;
	private String occ;
	/** 下一步按钮 */
	private Button nextButton;
	/** 系统时间 */
	private String currenttime;
	/** 是否进行过风险评估结果 */
	private Map<String, Object> beforeMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.boci_evaluation_title));
		// 右上角按钮赋值
		setText(this.getString(R.string.close));
		// 添加布局
		view = addView(R.layout.bocinvt_evaluation_input);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		goneLeftView();
		if (getIntent().getBooleanExtra(InvestConstant.MAINSTASRT, false)) {// 从投资理财服务转进来的页面
																			// xby
			goneLeftView();// 屏蔽左侧二级菜单
		}
		init();
	}

	/** 界面初始化 */
	private void init() {
		View menuPopwindow = (View) findViewById(R.id.menu_popwindow);
		menuPopwindow.setVisibility(View.GONE);
		back.setVisibility(View.GONE);
		beforeMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOICNVT_ISBEFORE_RESULT);
		// 步骤条
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getResources().getString(
								R.string.bocinvt_eva_step1),
						this.getResources().getString(
								R.string.bocinvt_eva_step2),
						this.getResources().getString(
								R.string.bocinvt_eva_step3) });
		StepTitleUtils.getInstance().setTitleStep(1);

		/** 客户姓名 */
		bocinvt_eva_name = (TextView) view.findViewById(R.id.bocinvt_eva_name);
		name = String.valueOf(beforeMap.get(BocInvt.BOCIEVA_CUSTNAME_RES));
		bocinvt_eva_name.setText(name);
		/** 证件类型 */
		bocinvt_eva_identitytype = (TextView) view
				.findViewById(R.id.bocinvt_eva_identitytype);
		identitytype = (String) beforeMap.get(BocInvt.BOCIEVA_IDTYPE_RES);
		bocinvt_eva_identitytype.setText(myidentityType.get(identitytype));
		/** 证件号码 */
		bocinvt_eva_identityactnum = (TextView) view
				.findViewById(R.id.bocinvt_eva_identityactnum);
		identityactnum = (String) beforeMap.get(BocInvt.BOCIEVA_IDNUM_RES);
		bocinvt_eva_identityactnum.setText(identityactnum);
		/** 性别 */
		bocinvt_eva_gender = (RadioGroup) view
				.findViewById(R.id.bocinvt_eva_gender);
		/** 男 */
		radio_boy = (RadioButton) view.findViewById(R.id.radio_boy);
		/** 女 */
		radio_girl = (RadioButton) view.findViewById(R.id.radio_girl);
		/** 生日 */
		bocinvt_eva_birthday = (TextView) view
				.findViewById(R.id.bocinvt_eva_birthday);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		dateTime = sdf.format(new Date());
		// 系统当前时间格式化
		currenttime = QueryDateUtils.getcurrentDate(dateTime);
		if (!StringUtil.isNull(identitytype)
				&& !StringUtil.isNull(identityactnum)) {
			if (identitytype.equals(investTypeSubList.get(1))
					&& identityactnum.length() == 18) {
				int year = Integer.parseInt(identityactnum.substring(6, 10));
				int monthOfYear = Integer.parseInt(identityactnum.substring(10,
						12));
				int dayOfMonth = Integer.parseInt(identityactnum.substring(12,
						14));
				StringBuilder date = new StringBuilder();
				date.append(String.valueOf(year));
				date.append("/");
				date.append(monthOfYear + "");
				date.append("/");
				date.append(dayOfMonth + "");
				currenttime = String.valueOf(date);
				bocinvt_eva_birthday.setText(currenttime);
				bocinvt_eva_birthday.setEnabled(false);
				bocinvt_eva_birthday
						.setBackgroundResource(R.drawable.bg_spinner_default);
			} else {
				// 初始结束时间赋值
				bocinvt_eva_birthday.setText(currenttime);
				bocinvt_eva_birthday.setOnClickListener(chooseDateClick);
			}
		} else {
			// 初始结束时间赋值
			bocinvt_eva_birthday.setText(currenttime);
			bocinvt_eva_birthday.setOnClickListener(chooseDateClick);
		}

		/** 地址 */
		bocinvt_eva_address = (EditText) view
				.findViewById(R.id.bocinvt_eva_address);
		EditTextUtils.setLengthMatcher(BaseDroidApp.getInstanse()
				.getCurrentAct(), bocinvt_eva_address, 150);
		/** 邮编 */
		bocinvt_eva_postcode = (EditText) view
				.findViewById(R.id.bocinvt_eva_postcode);
		/** 电话 */
		bocinvt_eva_phone = (EditText) view
				.findViewById(R.id.bocinvt_eva_phone);
		/** 手机 */
		bocinvt_eva_mobile = (EditText) view
				.findViewById(R.id.bocinvt_eva_mobile);
		/** Email */
		bocinvt_eva_email = (EditText) view
				.findViewById(R.id.bocinvt_eva_email);
		/** 收入 */
		bocinvt_eva_revenue = (Spinner) view
				.findViewById(R.id.bocinvt_eva_revenue);
		/** 教育程度 */
		bocinvt_eva_edu = (Spinner) view.findViewById(R.id.bocinvt_eva_edu);
		/** 所属行业 */
		bocinvt_eva_occ = (Spinner) view.findViewById(R.id.bocinvt_eva_occ);
		spinnerInit();
		nextButton = (Button) view.findViewById(R.id.btn_relevance_next);
		nextButton.setOnClickListener(nextBtnClick);
		bocinvt_eva_gender.setOnCheckedChangeListener(this);
		radio_boy.setChecked(true);
	}

	/** Spinner初始化 */
	public void spinnerInit() {
		// 收入适配
		ArrayAdapter<ArrayList<String>> revenueAdapter = new ArrayAdapter(this,
				R.layout.custom_spinner_item, LocalData.bocirevenueList);
		revenueAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bocinvt_eva_revenue.setAdapter(revenueAdapter);
		bocinvt_eva_revenue
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						revenue = String.valueOf(position);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						revenue = String.valueOf(0);
					}
				});
		// 教育程度适配
		ArrayAdapter<ArrayList<String>> eduAdapter = new ArrayAdapter(this,
				R.layout.custom_spinner_item, LocalData.bocieduList);
		eduAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bocinvt_eva_edu.setAdapter(eduAdapter);
		bocinvt_eva_edu.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				edu = String.valueOf(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				edu = String.valueOf(0);
			}
		});
		// 所属行业适配
		ArrayAdapter<ArrayList<String>> occAdapter = new ArrayAdapter(this,
				R.layout.custom_spinner_item, LocalData.bocioccList);
		occAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bocinvt_eva_occ.setAdapter(occAdapter);
		bocinvt_eva_occ.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				occ = String.valueOf(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				occ = String.valueOf(0);
			}
		});
	}

	/** 设置查询日期 */
	OnClickListener chooseDateClick = new OnClickListener() {

		@Override
		public void onClick(final View v) {

			TextView tv = (TextView) v;
			String time = tv.getText().toString();
			int startYear = Integer.parseInt(time.substring(0, 4));
			int startMonth = Integer.parseInt(time.substring(5, 7));
			int startDay = Integer.parseInt(time.substring(8, 10));
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			DatePickerDialog dialog = new DatePickerDialog(
					InvtEvaluationInputActivity.this, new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							StringBuilder date = new StringBuilder();
							date.append(String.valueOf(year));
							date.append("/");
							int month = monthOfYear + 1;
							date.append(((month < 10) ? ("0" + month)
									: (month + "")));
							date.append("/");
							date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth)
									: (dayOfMonth + "")));
							// 为日期赋值
							((TextView) v).setText(String.valueOf(date));
						}
					}, startYear, startMonth - 1, startDay);
			dialog.show();
		}
	};
	/** 下一步按钮监听事件 */
	OnClickListener nextBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (radio_boy.isChecked()) {
				gender = LocalData.genderList.get(1);
			} else if (radio_girl.isChecked()) {
				gender = LocalData.genderList.get(0);
			}
			birthday = bocinvt_eva_birthday.getText().toString().trim();
			address = bocinvt_eva_address.getText().toString().trim();
			postcode = bocinvt_eva_postcode.getText().toString().trim();
			phone = bocinvt_eva_phone.getText().toString().trim();
			mobile = bocinvt_eva_mobile.getText().toString().trim();
			email = bocinvt_eva_email.getText().toString().trim();
			Map<String, String> inputMap = new HashMap<String, String>();
			inputMap.put(BocInvt.BOCINVT_EVA_GENDER_REQ, gender);
			inputMap.put(BocInvt.BOCINVT_EVA_RISKBIRTHDAY_REQ, birthday);
			inputMap.put(BocInvt.BOCINVT_EVA_ADDRESS_REQ, address);
			inputMap.put(BocInvt.BOCINVT_EVA_PHONE_REQ, phone);
			inputMap.put(BocInvt.BOCINVT_EVA_RISKMOBILE_REQ, mobile);
			inputMap.put(BocInvt.BOCINVT_EVA_POSTCODE_REQ, postcode);
			inputMap.put(BocInvt.BOCINVT_EVA_RISKEMAIL_REQ, email);
			inputMap.put(BocInvt.BOCINVT_EVA_REVENUE_REQ, revenue);
			inputMap.put(BocInvt.BOCINVT_EVA_CUSTNATIONALITY_REQ, "");
			inputMap.put(BocInvt.BOCINVT_EVA_EDUCATION_REQ, edu);
			inputMap.put(BocInvt.BOCINVT_EVA_OCCUPATION_REQ, occ);
			String ishaveExperience = (String) beforeMap
					.get(BocInvt.BOCIEVA_HASINVESTEXPERIENCE_RES);
			inputMap.put(
					BocInvt.BOCINVT_EVA_HASINVESTEXPERIENCE_REQ,
					(StringUtil.isNull(ishaveExperience)) ? ConstantGloble.BOCINVT_HASINVESTEXPERIENCE
							: ((String) beforeMap
									.get(BocInvt.BOCIEVA_HASINVESTEXPERIENCE_RES)));
			BaseDroidApp.getInstanse().getBizDataMap()
					.put(ConstantGloble.BOCINVT_REQUESTMAP, inputMap);
			// 以下为验证
			// 地址最长150个字符
			RegexpBean reb = new RegexpBean(
					InvtEvaluationInputActivity.this
							.getString(R.string.bocinvt_address_regex),
					address, "riskAddress");
			// 邮编格式不正确 ！邮编由6位数字组成
			RegexpBean reb1 = new RegexpBean(
					InvtEvaluationInputActivity.this
							.getString(R.string.bocinvt_postcode_regex),
					postcode, "postcode");
			// 电话格式不正确 ！电话最长20位由数字、横线、空格、左右括号组成，不能含有中英文或特殊字符
			RegexpBean reb2 = new RegexpBean(
					InvtEvaluationInputActivity.this
							.getString(R.string.bocinvt_phone_regex),
					phone, "phone");
			// 手机号由1-15位数字组成
			RegexpBean reb3 = new RegexpBean(
					InvtEvaluationInputActivity.this
							.getString(R.string.bocinvt_mobile_regex),
					mobile, "shoujiH_01_15");
			// 电子邮箱长度为6-40位（格式如：XX@X.X）
			RegexpBean reb4 = new RegexpBean(
					InvtEvaluationInputActivity.this
							.getString(R.string.bocinvt_email_regex),
					email, "email");
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb);
			lists.add(reb1);
			lists.add(reb2);
			lists.add(reb3);
			lists.add(reb4);
			if (RegexpUtils.regexpDate(lists)) {// 校验通过
				// 验证完毕之后进行答题
				Intent intent = new Intent(InvtEvaluationInputActivity.this,
						TestInvtEvaluationAnswerActivity.class);
				startActivityForResult(intent,
						ConstantGloble.ACTIVITY_REQUEST_INVTEVALUATION_CODE);
			}

		}
	};
	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 关闭
			setResult(RESULT_CANCELED);
			finish();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;
		case RESULT_CANCELED:
			// 关闭
			if (BociDataCenter.getInstance().getI() == 0) {

			} else {
				setResult(RESULT_CANCELED);
				finish();
			}

			break;
		}
	}


	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.radio_boy:
			gender = LocalData.genderList.get(1);
			break;
		case R.id.radio_girl:
			gender = LocalData.genderList.get(0);
			break;
		default:
			break;
		}

	}
}
