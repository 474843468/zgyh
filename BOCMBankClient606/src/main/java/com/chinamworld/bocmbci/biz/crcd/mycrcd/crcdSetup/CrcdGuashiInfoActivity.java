package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 
 * 信用卡挂失页面
 * 
 * @author huangyuchao
 * 
 */
public class CrcdGuashiInfoActivity extends CrcdBaseActivity {
	private static final String TAG = "CrcdGuashiInfoActivity";
	/** 信用卡挂失 */
	private View view;

	Button nextBtn;

	TextView tv_cardNumber;

	Spinner crcd_guashitype;
	static String mailAddress;
	static String mailAddressType;
	private String accountNumber = null;
	private String accountId = null;
	private String nickName = null;
	private String strAccountType = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_creditcard_guashi_title));
		if (view == null) {
			view = addView(R.layout.crcd_guashi_info);
		}
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		nickName = getIntent().getStringExtra(Crcd.CRCD_NICKNAME_RES);
		strAccountType = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTTYPE_RES);
		init();

	}

	String[] sArray = new String[] { "挂失", "挂失及补卡" };

	static String strGuashiType;
	static int guaShiType;

	public void init() {

		tv_cardNumber = (TextView) findViewById(R.id.tv_cardNumber);
		tv_cardNumber.setText(StringUtil.getForSixForString(accountNumber));

		crcd_guashitype = (Spinner) findViewById(R.id.crcd_guashitype);

		ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, sArray);
		typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		crcd_guashitype.setAdapter(typeAdapter);
		crcd_guashitype.setSelection(0);

		crcd_guashitype.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				guaShiType = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		nextBtn = (Button) findViewById(R.id.nextButton);
		nextBtn.setOnClickListener(nextClick);
	}

	OnClickListener nextClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			strGuashiType = crcd_guashitype.getSelectedItem().toString();
			// 信用卡挂失确认
			requestCommConversationId();
			BaseHttpEngine.showProgressDialog();
		}
	};

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 请求安全因子组合id
		requestGetSecurityFactor(psnGuashisecurityId);
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);

		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
			@Override
			public void onClick(View v) {
				psnCrcdReportLossConfirm();
			}
		});
	}

	public void psnCrcdReportLossConfirm() {
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDREPORTLOSSCONFIRM);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		params.put(Crcd.CRCD_COMBINID, BaseDroidApp.getInstanse().getSecurityChoosed());
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdReportLossConfirmCallBack");
	}

	static Map<String, Object> returnMap;

	public void psnCrcdReportLossConfirmCallBack(Object resultObj) {
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		returnMap = (Map<String, Object>) body.getResult();
		if (StringUtil.isNullOrEmpty(returnMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		
		//支持IC信用卡
		
		//挂失及补卡手续费查询	
		PsnCrcdReportLossFee();
		
	}

	
	
	private void PsnCrcdReportLossFee() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDREPORTLOSEFEE);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "PsnCrcdReportLossFeeCallBack");
		
	}
	public void PsnCrcdReportLossFeeCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		
		
		TranDataCenter.getInstance().setCommissionChargeMap(result);
		// 信用卡补卡寄送地址查询
	   psnCrcdQueryCardholderAddress();
	}
	public void psnCrcdQueryCardholderAddress() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDQUERYCARDHOLDERADDRESS);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdQueryCardholderAddressCallBack");
	}

	public void psnCrcdQueryCardholderAddressCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
		Map<String, Object> returnMap = (Map<String, Object>) body.getResult();
		if (StringUtil.isNullOrEmpty(returnMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		mailAddress = String.valueOf(returnMap.get(Crcd.CRCD_MAILADDRESS));
		mailAddressType = String.valueOf(returnMap.get(Crcd.CRCD_MAILADDRESSTYPE));
		BaseHttpEngine.dissMissProgressDialog();
		Intent it = new Intent(CrcdGuashiInfoActivity.this, CrcdGuashiConfirmActivity.class);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		it.putExtra(Crcd.CRCD_NICKNAME_RES, nickName);
		it.putExtra(Crcd.CRCD_ACCOUNTTYPE_RES, strAccountType);
		startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;

		default:
			break;
		}
	}
	
	/**
	 * 手续费试算异常拦截 本方法有待改进
	 */
	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		if (resultObj instanceof BiiResponse) {
			BiiResponse biiResponse = (BiiResponse) resultObj;
			List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
			BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
			if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码

				if (Crcd.CRCD_PSNCRCDREPORTLOSEFEE.equals(biiResponseBody.getMethod())) {// 行内手续费试算
					if (biiResponse.isBiiexception()) {// 代表返回数据异常
						BiiHttpEngine.dissMissProgressDialog();
						BiiError biiError = biiResponseBody.getError();
						// 判断是否存在error
						if (biiError != null) {
							if (biiError.getCode() != null) {
								Map<String, Object> chargeMissionMap = TranDataCenter.getInstance()
										.getCommissionChargeMap();
								if (!StringUtil.isNullOrEmpty(chargeMissionMap)) {
									chargeMissionMap.clear();
								}
								if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
									// 要重新登录
									showTimeOutDialog(biiError.getMessage());
								} else {
									// 信用卡补卡寄送地址查询
									   psnCrcdQueryCardholderAddress();
								}
							}
						}
						return true;
					}
					return false;// 没有异常
				} else {
					return super.httpRequestCallBackPre(resultObj);
				}
			}
			// 随机数获取异常
		}
		return super.httpRequestCallBackPre(resultObj);
	}
//	public void showTimeOutDialog(BiiError biiError) {
//		BaseDroidApp.getInstanse().showMessageDialog(biiError.getMessage(), new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				BaseDroidApp.getInstanse().dismissErrorDialog();
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(CrcdGuashiInfoActivity.this, LoginActivity.class);
//				startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
//			}
//		});
//	}
}
