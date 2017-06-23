package com.chinamworld.bocmbci.biz.finc.fundprice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 登记基金它账户
 * 
 * @author xyl
 * 
 */
public class FincFundTaSettingActivity extends FincBaseActivity implements
		OnItemSelectedListener {
	private static final String TAG = "FincFundTaSettingActivity";
	/**
	 * 基金交易账户
	 */
	private TextView fundAccTextView;
	/**
	 * 基金公司下拉列表
	 */
	private Spinner fundCompanySpinner;
	/***
	 * 基金它账户输入框
	 */
	private EditText fundTaAccEditText;
	/**
	 * 用户输入的基金他账户
	 */
	private String fundTaAccStr;
	private String fundCompanyCodeStr;
	private String fundCompanyNameStr;
	/**
	 * 下一步按钮
	 */
	private Button nextBtn;

	private List<String> fincFundCountry = new ArrayList<String>();
	private List<String> fincFundCountryCode = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settingbaseinit();
		init();
		BiiHttpEngine.showProgressDialogCanGoBack();
		getFundRegCompanyList();
	}

	@Override
	public void getFundRegCompanyListCallback(Object resultObj) {
		super.getFundCompanyListCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, String>> list = (List<Map<String, String>>) biiResponseBody
				.getResult();
		BiiHttpEngine.dissMissProgressDialog();
		for (Map<String, String> map : list) {
			fincFundCountry.add(map
					.get(Finc.FINC_GETFUNDREGCOMPANYLIST_FUNDREGNAME));
			fincFundCountryCode.add(map
					.get(Finc.FINC_GETFUNDREGCOMPANYLIST_FUNDREGCODE));

		}
		// 给基金公司 列表初始化
		fundCompanySpinner.setAdapter(new ArrayAdapter<String>(this,
				R.layout.dept_spinner, fincFundCountry));
	}

	/**
	 * 初始化布局
	 * 
	 * @Author xyl
	 */
	private void init() {
		View childview = mainInflater.inflate(R.layout.finc_fundtaset_main,
				null);
		tabcontent.addView(childview);
		setTitle(R.string.finc_title_registfundTA);
		StepTitleUtils.getInstance().initTitldStep(this,
				fincControl.getStepsForRegistFundTa());
		StepTitleUtils.getInstance().setTitleStep(1);
		fundAccTextView = (TextView) childview.findViewById(R.id.finc_fundacc);
		fundCompanySpinner = (Spinner) childview
				.findViewById(R.id.finc_fundcompany_spinner);
		fundTaAccEditText = (EditText) childview
				.findViewById(R.id.finc_fundtaacc_edit);
		nextBtn = (Button) childview.findViewById(R.id.finc_next);
		if (!StringUtil.isNullOrEmpty(fincControl.invAccId)) {
			fundAccTextView.setText(fincControl.invAccId);
		}
		nextBtn.setOnClickListener(this);
		fundCompanySpinner.setOnItemSelectedListener(this);
		right.setText(getResources().getString(R.string.close));
		right.setVisibility(View.VISIBLE);
		right.setOnClickListener(this);
		back.setVisibility(View.GONE);
		backImage.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_next:// 下一步基金TA账户
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			RegexpBean taAccRegexp = new RegexpBean(getString(R.string.finc_fundtaacc), fundTaAccEditText
					.getText().toString(), ConstantGloble.TAACCOUNT);
			lists.add(taAccRegexp);
			if (RegexpUtils.regexpDate(lists)) {
				fundTaAccStr = StringUtil.trim(fundTaAccEditText.getText()
						.toString());
				Intent intent = new Intent();
				intent.setClass(this, FincFundTaSettingConfirmActivity.class);
				intent.putExtra(Finc.I_FUNDCOMPANYCODE, fundCompanyCodeStr);// 用于提交
				intent.putExtra(Finc.I_FUNDCOMPANYNAME, fundCompanyNameStr);// 用于显示
				intent.putExtra(Finc.I_FUNDTAACC, fundTaAccStr);// 用于显示和提交
				startActivityForResult(intent, 1);
			}
			break;
		case R.id.ib_top_right_btn:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;
		case ConstantGloble.FINC_CLOSE:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.finc_fundcompany_spinner:// 公司列表
			fundCompanyCodeStr = fincFundCountryCode.get(position);
			fundCompanyNameStr = fincFundCountry.get(position);
			break;

		default:
			break;
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}
}
