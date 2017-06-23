package com.chinamworld.bocmbci.biz.finc.fundacc;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 基金开户，
 * 
 * @author xyl
 * 
 */
public class FincRegistAccSuccessActivity extends FincBaseActivity {
	private static final String TAG = "FincRegistAccSuccessActivity";
	/**
	 * 确定按钮
	 */
	private Button cofirmBtn;
	/**
	 * 资金账户号码
	 */
	private TextView fundAccNumTv;
	private TextView fundAccTypeTv;
	private TextView addressTypeTv;
	private String fundAccTypeStr;
	private String fundAccNumStr;
	private String addressTypeStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settingbaseinit();
		init();
		initData();
	}

	/**
	 * 初始化布局
	 * 
	 * @Author xyl
	 */
	private void init() {
		settingbaseinit();
		View v = mainInflater.inflate(R.layout.finc_registacc_success, null);
		tabcontent.addView(v);
		StepTitleUtils.getInstance().initTitldStep(this,
				fincControl.getStepsForRegistAcc2());
		StepTitleUtils.getInstance().setTitleStep(3);
		setTitle(R.string.finc_title_registfundAcc);
		cofirmBtn = (Button) findViewById(R.id.finc_confirm_btn);
		fundAccNumTv = (TextView) findViewById(R.id.fund_accNum_tv);
		fundAccTypeTv = (TextView) findViewById(R.id.fund_accType_tv);
		addressTypeTv = (TextView) findViewById(R.id.fund_addresstype_tv);
		cofirmBtn.setOnClickListener(this);
		back.setVisibility(View.INVISIBLE);
		backImage.setVisibility(View.INVISIBLE);
		right.setVisibility(View.VISIBLE);
		right.setText(getResources().getString(R.string.switch_off));
		right.setOnClickListener(this);
	}

	private void initData() {
		if (fincControl.registAccFund != null) {
			fundAccNumStr = fincControl.registAccFund.get(Finc.I_FUNDACCNUM);
			fundAccTypeStr = fincControl.registAccFund.get(Finc.I_FUNDACCTYPE);
//			addressTypeStr = fincControl.registAccFund.get(Finc.I_ADDRESSTYPE);
		}
		fundAccNumTv.setText(StringUtil.getForSixForString(fundAccNumStr));
		fundAccTypeTv.setText(LocalData.AccountType.get(fundAccTypeStr));
		addressTypeTv.setText(StringUtil.getForSixForString(addressTypeStr));

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_confirm_btn:
			setResult(RESULT_OK);
			finish();
			break;
		case R.id.ib_top_right_btn:
//			setResult(RESULT_OK);
			setResult(ConstantGloble.FINC_CLOSE);
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
