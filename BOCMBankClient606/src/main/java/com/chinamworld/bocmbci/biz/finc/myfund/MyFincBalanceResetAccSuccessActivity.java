package com.chinamworld.bocmbci.biz.finc.myfund;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/***
 * 我的基金 重设基金账户 成功页面
 * 
 * @author Administrator
 * 
 */
public class MyFincBalanceResetAccSuccessActivity extends FincBaseActivity {
	private final String TAG = "MyFincBalanceResetAccSuccessActivity";
	/** 基金持仓主view */
	private View myFincView = null;
	/** 资金账户 */
	private TextView accNumberText = null;
	/** 基金账户 */
	private TextView numberText = null;
	private Button sureButton = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settingbaseinit();
		init();
		initData();
		initOnClick();
	}

	/** 初始化控件 */
	private void init() {
		myFincView = mainInflater.inflate(
				R.layout.finc_myfinc_balance_reset_success, null);
		tabcontent.addView(myFincView);
		setTitle(getResources().getString(R.string.bond_acct_reset));
		accNumberText = (TextView) findViewById(R.id.finc_accId);
		numberText = (TextView) findViewById(R.id.finc_accNumber);
		sureButton = (Button) findViewById(R.id.sureButton);

		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getResources()
								.getString(R.string.finc_myfinc_acc1),
						this.getResources()
								.getString(R.string.finc_myfinc_acc2),
						this.getResources()
								.getString(R.string.finc_myfinc_acc3) });
		StepTitleUtils.getInstance().setTitleStep(3);
		back.setVisibility(View.GONE);
	}

	/** 初始化数据 */
	private void initData() {
		Intent intent = getIntent();
		if (intent == null) {
			return;
		} else {
			String nickName = intent.getStringExtra(Finc.FINC_NICKNAME_RES);
			String accountNumber = intent
					.getStringExtra(Finc.FINC_ACCOUNTNUMBER_RES);
			String fincAccount = intent
					.getStringExtra(Finc.FINC_FINCACCOUNT_RES);
			LogGloble.d(TAG + " fincAccount", fincAccount);
			String c = accountNumber;
			c = StringUtil.getForSixForString(c);
			accNumberText.setText(c);
			numberText.setText(fincAccount);
		}
	}

	@Override
	public void onBackPressed() {
	}
	/** 初始化监听事件 */
	private void initOnClick() {
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	@Override
	public void finish() {
		setResult(RESULT_OK);
		super.finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
