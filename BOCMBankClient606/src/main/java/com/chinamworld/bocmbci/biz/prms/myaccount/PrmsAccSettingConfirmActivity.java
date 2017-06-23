package com.chinamworld.bocmbci.biz.prms.myaccount;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.biz.prms.PrmsBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 贵金属交易历史查询详情页面
 * 
 * @author xyl
 * 
 */
public class PrmsAccSettingConfirmActivity extends PrmsBaseActivity {
	private static final String TAG = "PrmsAccSettingConfirmActivity";
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
	 * 上一步
	 */
	private Button lastBtn;
	/**
	 * 确定
	 */
	private Button confirmBtn;

	private String accAliasStr;
	private String prmsAccStr;
	private String accTypeStr;
	private String accountId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initDate();
	}

	@Override
	public void requestPSNGetTokenIdCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(tokenId)) {
			return;
		}
		prmsAccSetting(accountId, tokenId);
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId();
	}

	/**
	 * 初始化布局
	 * 
	 * @Author xyl
	 */
	private void init() {
		settingbaseinit();
		View child = mainInflater.inflate(R.layout.prms_accsetting_confirm,
				null);
		tabcontent.addView(child);
		String title = getResources().getString(R.string.prms_title_price);
		StepTitleUtils.getInstance().initTitldStep(this,
				prmsControl.getStepsForAccSetting());
		StepTitleUtils.getInstance().setTitleStep(2);
		setTitle(title);
		prmsAcc = (TextView) findViewById(R.id.prms_acc);
		accType = (TextView) findViewById(R.id.prms_acctype);
		accAlias = (TextView) findViewById(R.id.prms_accalias);
		confirmBtn = (Button) findViewById(R.id.prms_acc_ok);
		lastBtn = (Button) findViewById(R.id.prms_acc_last);
		right.setText(getResources().getString(R.string.switch_off));
		right.setVisibility(View.VISIBLE);
		right.setOnClickListener(this);
		confirmBtn.setOnClickListener(this);
		lastBtn.setOnClickListener(this);

	}

	@Override
	public void prmsAccSettingCallBack(Object resultObj) {
		super.prmsAccSettingCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent();
		intent.setClass(PrmsAccSettingConfirmActivity.this,
				PrmsAccSettingSuccessActivity.class);
		intent.putExtra(Prms.PRMS_ACCOUNTNUMBER, prmsAccStr);
		intent.putExtra(Prms.PRMS_ACCOUNTTYPE, accTypeStr);
		intent.putExtra(Prms.PRMS_NIKENAME, accAliasStr);
		startActivityForResult(intent,
				ConstantGloble.ACTIVITY_REQUEST_SETPRMSACC_CODE);

	}

	/**
	 * 初始化数据
	 * 
	 * @Author xyl
	 */
	private void initDate() {
		Intent intent = getIntent();
		Bundle extra = intent.getExtras();
		accountId = extra.getString(Prms.PRMS_ACCOUNTID);
		accAliasStr = extra.getString(Prms.PRMS_NIKENAME);
		prmsAccStr = extra.getString(Prms.PRMS_ACCOUNTNUMBER);
		accTypeStr = extra.getString(Prms.PRMS_ACCOUNTTYPE);

		prmsAcc.setText(StringUtil.getForSixForString(prmsAccStr));
		accAlias.setText(accAliasStr);
		accType.setText(LocalData.AccountType.get(accTypeStr));
		setTitle(getResources().getString(R.string.prms_title_accsetingconfirm));
		StepTitleUtils.getInstance().initTitldStep(this,
				prmsControl.getStepsForAccSetting());
		StepTitleUtils.getInstance().setTitleStep(2);

	}

	/**
	 * 启动的页面结束时的回调
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:// 代表开通成功
			setResult(RESULT_OK);
			finish();
			break;

		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.prms_acc_ok:// 确定
			requestCommConversationId();BaseHttpEngine.showProgressDialog();
			break;
		case R.id.prms_acc_last:
			finish();
			break;
		case R.id.ib_top_right_btn:
			finish();
			break;
		default:
			break;
		}
	}

}
