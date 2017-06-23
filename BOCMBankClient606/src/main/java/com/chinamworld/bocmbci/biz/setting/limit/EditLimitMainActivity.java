package com.chinamworld.bocmbci.biz.setting.limit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Inves;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.constant.RegCode;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class EditLimitMainActivity extends LimitSettingBaseActivity {
	private static final String TAG = "EditLimitMainActivity";
	/**
	 * 限额种类显示框
	 */
	private TextView serviceNameTextView;
	/**
	 * 币种显示框
	 */
	private TextView currencyTextView;
	/**
	 * 系统日限额显示框
	 */
	private TextView dayMaxTextView;
	/**
	 * 个人日限额显示框
	 */
	private TextView preDayMaxTextView;
	/**
	 * 设定限额输入框
	 */
	private EditText resetEdit;

	/**
	 * 确定按钮
	 */
	private Button confrimBtn;
	/**
	 * 取消按钮
	 */
	private Button consernBtn;

	private String resetpreDayMax;
	private String preDayMax;
	private String dayMax;
	private String currency;
	private String serviceId;
	private StringBuffer limit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initData();
		initRightBtnForMain();
	}

	/**
	 * 初始化布局
	 * 
	 * @Author xyl
	 */
	private void init() {
		View childView = mainInflater.inflate(R.layout.setting_editlimit_main,
				null);
		tabcontent.addView(childView);
		StepTitleUtils.getInstance().initTitldStep(this,
				settingControl.getStepsForEditLimit());
		StepTitleUtils.getInstance().setTitleStep(1);
		setTitle(getResources().getString(R.string.set_title_limitsetting));
		serviceNameTextView = (TextView) childView
				.findViewById(R.id.set_editlimitconfirm_servicename);
		currencyTextView = (TextView) childView
				.findViewById(R.id.set_editlimitconfirm_currency);
		dayMaxTextView = (TextView) childView
				.findViewById(R.id.set_editlimitconfirm_daymax);
		preDayMaxTextView = (TextView) childView
				.findViewById(R.id.set_editlimitconfirm_predaymax);
		resetEdit = (EditText) childView
				.findViewById(R.id.set_editlimitconfirm_resetpredaymax);
		confrimBtn = (Button) childView.findViewById(R.id.set_confirm);
		consernBtn = (Button) childView.findViewById(R.id.set_consern);
		confrimBtn.setOnClickListener(this);
		consernBtn.setOnClickListener(this);
		TextView tv = (TextView) findViewById(R.id.set_editlimitconfirm_predaymax_pre);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				serviceNameTextView);
	}

	/**
	 * 初始化数据
	 * 
	 * @Author xyl
	 */
	private void initData() {
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		currency = extras.getString(Setting.I_CURRENCY);
		serviceId = extras.getString(Setting.I_SERVICEID);
		dayMax = extras.getString(Setting.I_DAYMAX);
		preDayMax = extras.getString(Setting.I_PREMAX);
		currencyTextView.setText(currency);
		serviceNameTextView.setText(LocalData.serviceCodeMap.get(serviceId));
		preDayMaxTextView.setText(StringUtil.parseStringPattern(preDayMax, 2));
		dayMaxTextView.setText(StringUtil.parseStringPattern(dayMax, 2));
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseDroidApp.getInstanse().showSeurityChooseDialog(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						editLimitConfirm(limit.toString(), BaseDroidApp
								.getInstanse().getSecurityChoosed());
					}
				});
	}
	public static Map<String, Object> map;
	@Override
	public void editLimitConfirmCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.editLimitConfirmCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		map = (Map<String, Object>) biiResponseBody
				.getResult();
		settingControl.factorList = (List<Map<String, Object>>) map
				.get(Inves.FACTORLIST);
		if (!StringUtil.isNullOrEmpty(settingControl.factorList)) {
			Intent intent = new Intent();
			intent.setClass(EditLimitMainActivity.this,
					EditLimitConfirmActivity.class);
			intent.putExtra(Setting.I_COMBIN, BaseDroidApp.getInstanse()
					.getSecurityChoosed());
			intent.putExtra(Setting.I_PREMAX, preDayMax);
			intent.putExtra(Setting.I_DAYMAX, dayMax);
			intent.putExtra(Setting.I_RESETPREMAX, resetpreDayMax);
			intent.putExtra(Setting.I_SERVICEID, serviceId);
			intent.putExtra(Setting.I_CURRENCY, currency);
			
			
			startActivity(intent);
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.set_confirm:
			resetpreDayMax = StringUtil.trim(resetEdit.getText().toString());
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			RegexpBean regexresetpreDayMax = new RegexpBean(getResources()
					.getString(R.string.set_resetpredaymax_no), resetpreDayMax,
					RegCode.SETLIMITMAXAMOUNT);
			lists.add(regexresetpreDayMax);
			if (RegexpUtils.regexpDate(lists)) {
				// if (preDayMax.equals(resetpreDayMax)) {// 交易限额未修改
				// BaseDroidApp.getInstanse().showInfoMessageDialog(
				// getResources().getString(
				// R.string.set_predaymax_notedit));
				// return;
				// } else {
				BigDecimal tempBigDecimal = new BigDecimal(dayMax.toCharArray());
				BigDecimal resetPreDayMaxBig = new BigDecimal(
						resetpreDayMax.toCharArray());
				if (resetPreDayMaxBig.compareTo(tempBigDecimal) == 1) {// 每日超过最大限额
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							getResources().getString(
									R.string.set_daymax_set_big));
					return;
				} else {
					limit = new StringBuffer();
					limit = limit.append(serviceId);
					limit = limit.append("_");
					limit = limit.append(resetpreDayMax);

					requestGetSecurityFactor(ConstantGloble.SET_SERVICEID);
				}
				// }
			}
			break;

		case R.id.set_consern:
			finish();
			break;
		default:
			break;
		}
	}

}
