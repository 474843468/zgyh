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
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;

public class MyFincBoundsTypeSuccessActivity extends FincBaseActivity {
	private final String TAG = "MyFincBoundsTypeConfirmActivity";
	/** 分红方式view */
	private View myFincView = null;
	/** 基金代码 */
	private TextView fincCodeText = null;
	/** 基金名称 */
	private TextView fincNameText = null;
	/** 当前分红方式 */
	private TextView fincTypeText = null;
	/** 确定按钮 */
	private Button sureButton = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initData();
		initOnClick();
		initRightBtnForMain();
	}

	private void init() {
		myFincView = mainInflater.inflate(
				R.layout.finc_myfinc_bondstype_success, null);
		tabcontent.addView(myFincView);
		setTitle(getResources().getString(R.string.finc_myfinc_bound));
		fincCodeText = (TextView) findViewById(R.id.finc_fincCode);
		fincNameText = (TextView) findViewById(R.id.finc_fincName);
		fincTypeText = (TextView) findViewById(R.id.finc_boundsType);
		sureButton = (Button) findViewById(R.id.sureButton);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fincNameText);
		back.setVisibility(View.GONE);
		backImage.setVisibility(View.GONE);
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getResources().getString(
								R.string.finc_myfinc_foundOne),
						this.getResources().getString(R.string.finc_top_two),
						this.getResources()
								.getString(R.string.finc_myfinc_acc3) });
		StepTitleUtils.getInstance().setTitleStep(3);

	}

	private void initData() {
		Intent intent = getIntent();
		String foundCode = intent.getStringExtra(Finc.FINC_FUNDCODE_REQ);
		String foundName = intent.getStringExtra(Finc.FINC_FUNDNAME_REQ);
		String foundTypeCode = intent
				.getStringExtra(ConstantGloble.FINC_FOUNDTYPECODE_KEY);
		fincCodeText.setText(foundCode);
		fincNameText.setText(foundName);
		fincTypeText.setText(LocalData.bonusTypeMap.get(foundTypeCode));
	}

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
