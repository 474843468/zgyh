package com.chinamworld.bocmbci.biz.finc.fundacc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.ViewUtils;

import java.util.List;
import java.util.Map;

/**
 * 基金账户开户服务协议
 * 
 * @author xyl
 * 
 */
public class FincRegistAccAgreeActivity extends FincBaseActivity {
	private static final String TAG = "FincRegistAccAgreeActivity";
	/**
	 * agree
	 */
	private Button agreeBtn;
	/** no agree */
	private Button noAgreeBtn;
	private String accId;

	private String userName;
	private String identifyType;
	private String identifyNumber;

//	private String addresstypeStr;

	private TextView accTv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settingbaseinit();
		back.setVisibility(View.GONE);
		backImage.setVisibility(View.GONE);
		init();
	}

	/**
	 * 初始化布局
	 * 
	 * @Author xyl
	 */
	private void init() {
//		settingbaseinit();
		View v = mainInflater.inflate(R.layout.finc_registacc_agree, null);
		tabcontent.addView(v);
		StepTitleUtils.getInstance().initTitldStep(this,
				fincControl.getStepsForRegistAcc1());
		StepTitleUtils.getInstance().setTitleStep(2);
		setTitle(R.string.finc_title_registfundAcc);
		agreeBtn = (Button) findViewById(R.id.finc_accept);
		noAgreeBtn = (Button) findViewById(R.id.finc_noaccept);
		ViewUtils.initBtnParamsTwoLeft(noAgreeBtn, this);
		ViewUtils.initBtnParamsTwoRight(agreeBtn, this);
		accTv = (TextView)findViewById(R.id.finc_accName);
		
		Map<String, Object> resultMap = (Map<String, Object>) BaseDroidApp
				.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA);
		String customerName = (String)resultMap.get(Login.CUSTOMER_NAME);
		accTv.setText(customerName);
		agreeBtn.setOnClickListener(this);
		noAgreeBtn.setOnClickListener(this);
		if (fincControl.registAccFund != null) {
			accId = fincControl.registAccFund.get(Finc.I_ACCOUNTID);
//			addresstypeStr = fincControl.registAccFund.get(Finc.I_ADDRESSTYPE);
		}
		right.setText(getResources().getString(R.string.close));
		right.setVisibility(View.VISIBLE);
		right.setOnClickListener(this);
		// 协议内容 Tv
		TextView  infoTv = (TextView)findViewById(R.id.agree_info);
		infoTv.setText(StringUtil.ToDBC(getString(R.string.finc_acc_regisitAcc_agree_info)));
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseDroidApp.getInstanse().showSeurityChooseDialog(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						BaseHttpEngine.showProgressDialog();
						Map<String, Object> resultMap = (Map<String, Object>) BaseDroidApp
								.getInstanse().getBizDataMap()
								.get(ConstantGloble.BIZ_LOGIN_DATA);
						userName = String.valueOf(resultMap.get(Comm.LOGINNAME));
						identifyType = String.valueOf(resultMap
								.get(Comm.IDENTITYTYPE));
						identifyNumber = String.valueOf(resultMap
								.get(Comm.IDENTITYNUMBER));
						regisAccConfirm(accId, userName,
//								LocalData.FincAddressTypeValue
//										.get(addresstypeStr),
										identifyType,
								identifyNumber, BaseDroidApp.getInstanse()
										.getSecurityChoosed());
					}
				});
	}
	public static Map<String, Object> resultMap;
	@Override
	public void regisAccConfirmCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.regisAccConfirmCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		fincControl.factorList = (List<Map<String, Object>>) resultMap
				.get(Finc.FINC_REGISTACC_CONFIRM_FACTORLIST);
		if (!StringUtil.isNullOrEmpty(fincControl.factorList)) {
			Intent intent = new Intent();
			intent.setClass(this, FincRegistAccConfirmActivity.class);
			startActivityForResult(intent,
					ConstantGloble.ACTIVITY_REQUEST_REGISTFUNCACC_CODE);
		}

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_accept:// 同意
			requestCommConversationId();
			BaseHttpEngine.showProgressDialog();
			break;
		case R.id.ib_top_right_btn://关闭
			setResult(ConstantGloble.FINC_CLOSE);
			finish();
			break;
		case R.id.finc_noaccept:// 不接受
			setResult(RESULT_CANCELED);
//			setResult(ConstantGloble.FINC_CLOSE);
			finish();
			break;
			

		default:
			break;
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		fincControl.registAccConversationId = (String) BaseDroidApp
				.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID);
		requestGetSecurityFactor(ConstantGloble.FINC_SERVICE);
	}

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
			setResult( ConstantGloble.FINC_CLOSE);
			finish();
			break;
		default:
			break;
		}
	}

}
