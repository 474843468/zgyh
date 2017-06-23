package com.chinamworld.bocmbci.biz.finc.fundprice;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 登记基金它账户
 * 
 * @author xyl
 * 
 */
public class FincFundTaSettingConfirmActivity extends FincBaseActivity {
	private static final String TAG = "FincFundTaSettingConfirmActivity";
	/**
	 * 基金交易账户
	 */
	private TextView fundAccTextView;
	/***
	 * 基金它账户显示
	 */
	private TextView fundTaAccTextView;
	/**
	 * 选择的公司
	 */
	private TextView fundCompanyTextView;
	/**
	 * 用户输入的基金他账户
	 */
	private String fundTaAccStr;
	private String fundCompanyCodeStr;
	private String fundCompanyNameStr;
	/**
	 * 下一步按钮
	 */
	private Button preBtn;
	private Button confirm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settingbaseinit();
		init();
		initData();
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId();
	}

	@Override
	public void requestPSNGetTokenIdCallback(Object resultObj) {
		super.requestPSNGetTokenIdCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(tokenId)) {
			return;
		}
		fundtaAccSetting(tokenId, fundTaAccStr, fundCompanyCodeStr);
	}

	@Override
	public void fundtaAccSettingCallback(Object resultObj) {
		super.fundtaAccSettingCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		biiResponseBody.getResult();
		BaseHttpEngine.dissMissProgressDialog();
		// 默认成功
		Intent intent = new Intent();
		intent.setClass(this, FincFundTaSettingSuccessActivity.class);
		intent.putExtra(Finc.I_FUNDCOMPANYNAME, fundCompanyNameStr);// 用于显示
		intent.putExtra(Finc.I_FUNDTAACC, fundTaAccStr);// 用于显示和提交
		startActivityForResult(intent, 1);
	}

	/**
	 * 初始化布局
	 * 
	 * @Author xyl
	 */
	private void init() {
		View childview = mainInflater.inflate(
				R.layout.finc_fundtasetting_confirm, null);
		tabcontent.addView(childview);
		setTitle(R.string.finc_title_registfundTA);
		StepTitleUtils.getInstance().initTitldStep(this,
				fincControl.getStepsForRegistFundTa());
		StepTitleUtils.getInstance().setTitleStep(2);
		fundAccTextView = (TextView) childview.findViewById(R.id.finc_fundacc);
		///add by fsm
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fundAccTextView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, 
				((TextView)findViewById(R.id.finc_fundcompany_textview_alert)));
		
		fundCompanyTextView = (TextView) childview
				.findViewById(R.id.finc_fundcompany_textview);
		fundTaAccTextView = (TextView) childview
				.findViewById(R.id.finc_fundtaacc_textview);
		preBtn = (Button) childview.findViewById(R.id.finc_pre);
		confirm = (Button) childview.findViewById(R.id.finc_confirm);
		// 基金账户
		if (!StringUtil.isNullOrEmpty(fincControl.invAccId)) {
			fundAccTextView.setText(fincControl.invAccId);
		}
		preBtn.setOnClickListener(this);
		confirm.setOnClickListener(this);
		right.setText(getResources().getString(R.string.close));
		right.setVisibility(View.VISIBLE);
		right.setOnClickListener(this);
	}

	private void initData() {
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		fundCompanyCodeStr = (String) extras.get(Finc.I_FUNDCOMPANYCODE);
		fundCompanyNameStr = (String) extras.get(Finc.I_FUNDCOMPANYNAME);
		fundTaAccStr = (String) extras.get(Finc.I_FUNDTAACC);
		fundCompanyTextView.setText(fundCompanyNameStr);
		fundTaAccTextView.setText(fundTaAccStr);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fundCompanyTextView);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case ConstantGloble.FINC_CLOSE:
			break;
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_pre:// 上一步
			finish();
			break;
		case R.id.finc_confirm:// 确定
			requestCommConversationId();
			BaseHttpEngine.showProgressDialog();
			break;
		case R.id.ib_top_right_btn:
			setResult(ConstantGloble.FINC_CLOSE);
			finish();
			break;
		default:
			break;
		}
	}
}
