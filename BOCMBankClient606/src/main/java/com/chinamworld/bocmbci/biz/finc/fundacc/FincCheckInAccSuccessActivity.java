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
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 基金登记账户 成功
 * 
 * @author xyl
 * 
 */
public class FincCheckInAccSuccessActivity extends FincBaseActivity {
	private static final String TAG = "FincCheckInAccSuccessActivity";
	/**
	 * 确定按钮
	 */
	private Button cofirmBtn;
	/** 基金交易账户 */
	private TextView fundAccNumTv;
	/** 资金账户 */
	private TextView accNumTv;
	

	/** 基金交易账户 */
	private String fundAccNumStr;
	/** 资金账户 */
	private String accNumStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settingbaseinit();
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
		settingbaseinit();
		View v = mainInflater.inflate(R.layout.finc_checinacc_success, null);
		tabcontent.addView(v);
		StepTitleUtils.getInstance().initTitldStep(this,
				fincControl.getStepsForCheckInAcc());
		StepTitleUtils.getInstance().setTitleStep(3);
		setTitle(R.string.finc_title_checkInfundAcc);
		cofirmBtn = (Button) findViewById(R.id.finc_confirm_btn);
		fundAccNumTv = (TextView) findViewById(R.id.fund_accNum_tv);
		accNumTv = (TextView) findViewById(R.id.finc_monyacc_tv);
		cofirmBtn.setOnClickListener(this);
		back.setVisibility(View.GONE);
		backImage.setVisibility(View.GONE);
		right.setVisibility(View.VISIBLE);
		right.setText(getResources().getString(R.string.switch_off));
		right.setOnClickListener(this);
	}

	private void initData() {
		if (fincControl.registAccFund != null) {
			fundAccNumStr = fincControl.registAccFund.get(Finc.I_FINCACCOUNT);
			accNumStr = fincControl.registAccFund.get(Finc.I_FUNDACCNUM);
//			fundAccTypeStr = fincControl.registAccFund.get(Finc.I_FUNDACCTYPE);
//			nickNameStr = fincControl.registAccFund.get(Finc.I_NICKNAME);
//			currencyStr = fincControl.registAccFund.get(Finc.I_CURRENCYCODE);
//			accbalanceStr = fincControl.registAccFund.get(Finc.I_ACCBALANCE);
		}
		fundAccNumTv.setText(fundAccNumStr);
		accNumTv.setText(StringUtil.getForSixForString(accNumStr));

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
