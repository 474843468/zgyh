package com.chinamworld.bocmbci.biz.setting.limit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class EditLimitSuccessActivity extends LimitSettingBaseActivity {
	private static final String TAG = "EditLimitSuccessActivity";
	/**
	 * 币种显示框
	 */
	private TextView currencyTextView;
	/** 成功信息提示页面 */
	private TextView successInfoView;
	/**
	 * 用户个人设定每日交易限额显示框
	 */
	private TextView preDayMaxTextView;
	/**
	 * 最大交易限额显示框
	 */
	private TextView dayMaxTextView;
	/**
	 * 服务类型名称显示框
	 */
	private TextView serviceNameTextView;
	/**
	 * 确定按钮
	 */
	private Button confirmBtn;
	private String currency;
	private String preDayMax;
	private String dayMax;
	private String serviceId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		activityTaskManager.removeAllActivity();
		super.onCreate(savedInstanceState);
		init();
		initData();
	}

	@Override
	public void onBackPressed() {
	}

	/**
	 * 初始化布局
	 * 
	 * @Author xyl
	 */
	private void init() {
		View childView = mainInflater.inflate(
				R.layout.setting_editlimit_success, null);
		tabcontent.addView(childView);

		StepTitleUtils.getInstance().initTitldStep(this,
				settingControl.getStepsForEditLimit());
		StepTitleUtils.getInstance().setTitleStep(3);
		setTitle(getResources().getString(R.string.set_title_limitsetting));
		currencyTextView = (TextView) childView
				.findViewById(R.id.set_editlimitsuccess_currency);
		successInfoView = (TextView) childView
				.findViewById(R.id.set_editlimitsuccess_info);
		preDayMaxTextView = (TextView) childView
				.findViewById(R.id.set_editlimitsccess_predaymax);
		dayMaxTextView = (TextView) childView
				.findViewById(R.id.set_editlimitsuccess_daymax);
		serviceNameTextView = (TextView) childView
				.findViewById(R.id.set_editlimitsuccess_servicename);
		confirmBtn = (Button) childView.findViewById(R.id.set_confirm);
		confirmBtn.setOnClickListener(this);
		back.setVisibility(View.INVISIBLE);
		initRightBtnForMain();

		TextView tv = (TextView) findViewById(R.id.set_editlimitsccess_predaymax_pre);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv);
	}

	private void initData() {
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		serviceId = extras.getString(Setting.I_SERVICEID);
		preDayMax = extras.getString(Setting.I_PREMAX);
		dayMax = extras.getString(Setting.I_DAYMAX);
		currency = extras.getString(Setting.I_CURRENCY);
		currencyTextView.setText(currency);
		preDayMaxTextView.setText(StringUtil.parseStringPattern(preDayMax, 2));
		dayMaxTextView.setText(StringUtil.parseStringPattern(dayMax, 2));
		serviceNameTextView.setText(LocalData.serviceCodeMap.get(serviceId));
		successInfoView.setText(LocalData.serviceCodeMap.get(serviceId)
				+ getString(R.string.set_editalimitssuccess));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				serviceNameTextView);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.set_confirm:
			startActivity(new Intent(EditLimitSuccessActivity.this,
					LimitSettingActivity.class));
			finish();
			break;

		default:
			break;
		}
	}

}
