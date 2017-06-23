package com.chinamworld.bocmbci.biz.prms.myaccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.biz.prms.PrmsBaseActivity;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 贵金属交易历史查询详情页面
 * 
 * @author xyl
 * 
 */
public class PrmsAccSettingSuccessActivity extends PrmsBaseActivity {
	private static final String TAG = "PrmsAccSettingSuccessActivity";
	/**
	 * 账户贵金属交易账户
	 */
	private TextView prmsAcc;
	/**
	 * 账户类型 : accName
	 */
	private TextView accType;
	/**
	 * 账户别名 Nikename
	 */
	private TextView accAlias;
	/**
	 * 确定
	 */
	private Button confirmBtn;

	private String accAliasStr;
	private String prmsAccStr;
	private String accTypeStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initDate();

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
		View child = mainInflater.inflate(R.layout.prms_accsetting_success,
				null);
		tabcontent.addView(child);
		StepTitleUtils.getInstance().initTitldStep(this,
				prmsControl.getStepsForAccSetting());
		StepTitleUtils.getInstance().setTitleStep(3);
		prmsAcc = (TextView) findViewById(R.id.prms_acc);
		accType = (TextView) findViewById(R.id.prms_acctype);
		accAlias = (TextView) findViewById(R.id.prms_accalias);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accAlias);///add by fsm
		confirmBtn = (Button) findViewById(R.id.prms_acc_ok);
//		right.setText(getResources().getString(R.string.switch_off));
//		right.setVisibility(View.VISIBLE);
//		right.setOnClickListener(this);
		right.setVisibility(View.GONE);
		confirmBtn.setOnClickListener(this);
		back.setVisibility(View.INVISIBLE);
	}

	/**
	 * 初始化数据
	 * 
	 * @Author xyl
	 */
	private void initDate() {
		Intent intent = getIntent();
		Bundle extra = intent.getExtras();
		accAliasStr = extra.getString(Prms.PRMS_NIKENAME);
		prmsAccStr = extra.getString(Prms.PRMS_ACCOUNTNUMBER);
		accTypeStr = extra.getString(Prms.PRMS_ACCOUNTTYPE);
		prmsAcc.setText(StringUtil.getForSixForString(prmsAccStr));
		accAlias.setText(accAliasStr);
		accType.setText(LocalData.AccountType.get(accTypeStr));
		setTitle(getResources().getString(R.string.prms_title_accsetingconfirm));

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:// 返回按钮
			// setResult(RESULT_OK);
			// finish();
			break;
		case R.id.prms_acc_ok:// 确定按钮
			setResult(RESULT_OK);
			finish();
			break;
		case R.id.ib_top_right_btn:// 右上角关闭按钮
			setResult(RESULT_OK);
			finish();
			break;
		default:
			break;
		}
	}
}
