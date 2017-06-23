package com.chinamworld.bocmbci.biz.finc.fundacc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.adapter.MyFincBalanceResetAccAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 基金账户开户
 * 
 * @author xyl
 * 
 */
public class FincFundRegistAccMainActivity extends FincBaseActivity {
	private static final String TAG = "FincFundRegistAccMainActivity";

//	private RadioGroup addressRadioGroup;
//	private RadioButton homeAddressRadioButton;
//	private RadioButton companyAddressRadioButton;

	private ListView listView;
	private MyFincBalanceResetAccAdapter adapter;

	private String accountId;
	private Button nextBtn;
	private String nickNameStr;
	private String accNumStr;
	private String accTypeStr;
//	private String addressTypeStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settingbaseinit();
		init();
	}

	/**
	 * 初始化布局
	 */
	private void init() {
		View childView = mainInflater.inflate(R.layout.finc_acc_regist_main,
				null);
		tabcontent.addView(childView);
		StepTitleUtils.getInstance().initTitldStep(this,
				fincControl.getStepsForRegistAcc1());
		StepTitleUtils.getInstance().setTitleStep(1);
		setTitle(R.string.finc_title_registfundAcc);

//		addressRadioGroup = (RadioGroup) childView
//				.findViewById(R.id.radioGroup);
//		addressRadioGroup.setOnCheckedChangeListener(this);
//		homeAddressRadioButton = (RadioButton) childView
//				.findViewById(R.id.finc_home_address);
//		companyAddressRadioButton = (RadioButton) childView
//				.findViewById(R.id.finc_company_address);
		nextBtn = (Button) childView.findViewById(R.id.finc_next);
		listView = (ListView) childView.findViewById(R.id.finc_ListView);
		if (!StringUtil.isNullOrEmpty(fincControl.fundAccList)) {
			adapter = new MyFincBalanceResetAccAdapter(this,
					fincControl.fundAccList);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Map<String, Object> map = fincControl.fundAccList
							.get(position);
					adapter.setSelectedPosition(position);
					accountId = (String) map
							.get(Finc.FINC_QUERYACCLIST_ACCOUNTID);
					nickNameStr = (String) map
							.get(Finc.FINC_QUERYACCLIST_ACCOUNTNAME);
					accNumStr = (String) map
							.get(Finc.FINC_QUERYACCLIST_ACCOUNTNUMBER);
					accTypeStr = (String) map
							.get(Finc.FINC_QUERYACCLIST_ACCOUNTTYPE);
					adapter.notifyDataSetChanged();
				}
			});
		}
		nextBtn.setOnClickListener(this);
//		homeAddressRadioButton.setChecked(true);
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
		case R.id.finc_next:
			if (accountId != null) {
				fincControl.registAccFund = new HashMap<String, String>();
//				fincControl.registAccFund.put(Finc.I_ADDRESSTYPE,
//						addressTypeStr);
				fincControl.registAccFund.put(Finc.I_FUNDACCNUM, accNumStr);
				fincControl.registAccFund.put(Finc.I_ACCOUNTID, accountId);
				fincControl.registAccFund.put(Finc.I_FUNDACCTYPE, accTypeStr);
				Intent intent = new Intent();
				intent.setClass(this, FincRegistAccAgreeActivity.class);
				startActivityForResult(intent,
						ConstantGloble.ACTIVITY_REQUEST_REGISTFUNCACC_CODE);
			} else {// 请选择一张卡作为基金账户
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getString(R.string.finc_acc_checkINAcc_first_info2));
			}
			break;

		case R.id.ib_top_right_btn:
			finish();
			break;

		default:
			break;
		}
	}

//	@Override
//	public void onCheckedChanged(RadioGroup group, int checkedId) {
//		switch (group.getId()) {
//		case R.id.radioGroup://
//			switch (checkedId) {
//			case R.id.finc_home_address:
//				addressTypeStr = homeAddressRadioButton.getText().toString();
//				break;
//			case R.id.finc_company_address:
//				addressTypeStr = companyAddressRadioButton.getText().toString();
//				break;
//			default:
//				break;
//			}
//			break;
//		default:
//			break;
//		}
//
//	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;
		case RESULT_CANCELED:

			break;
		case ConstantGloble.FINC_CLOSE:
			setResult(ConstantGloble.FINC_CLOSE);
			finish();
			break;
		default:
			break;
		}
	}

}
