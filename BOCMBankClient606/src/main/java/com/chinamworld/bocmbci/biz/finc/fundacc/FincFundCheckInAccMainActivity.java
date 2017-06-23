package com.chinamworld.bocmbci.biz.finc.fundacc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.adapter.MyFincBalanceResetAccAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.ErrorCode;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基金账户登记   选择账户
 * 
 * @author xyl
 * 
 */
public class FincFundCheckInAccMainActivity extends FincBaseActivity {
	private static final String TAG = "FincFundCheckInAccMainActivity";

	private ListView listView;
	private MyFincBalanceResetAccAdapter adapter;

	private String accountId;
	private Button nextBtn;
	private String nickNameStr;
	/** 资金账户 */
	private String accNumStr;
	private String accTypeStr;
	private String addressTypeStr;

	/** 基金交易账户 */
	private String fundAcc;

	private String accbalanceStr;

	private String currencyStr;

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
		View childView = mainInflater.inflate(R.layout.finc_acc_checkin_main,
				null);
		tabcontent.addView(childView);
		StepTitleUtils.getInstance().initTitldStep(this,
				fincControl.getStepsForCheckInAcc());
		StepTitleUtils.getInstance().setTitleStep(1);
		setTitle(R.string.finc_title_checkInfundAcc);

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
		right.setText(getResources().getString(R.string.close));
		right.setVisibility(View.VISIBLE);
		right.setOnClickListener(this);
		back.setVisibility(View.GONE);
		backImage.setVisibility(View.GONE);
	}

	@Override
	public void checkINAccConfirmCallback(Object resultObj) {
		super.checkINAccConfirmCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> map = (Map<String, String>) biiResponseBody
				.getResult();
		fundAcc = map.get(Finc.FINC_FINCACCOUNT);
		fincControl.registAccFund = new HashMap<String, String>();
//		fincControl.registAccFund.put(Finc.I_ADDRESSTYPE, addressTypeStr);
		fincControl.registAccFund.put(Finc.I_ACCOUNTID, accountId);
		fincControl.registAccFund.put(Finc.I_FUNDACCNUM, accNumStr);
		fincControl.registAccFund.put(Finc.I_FUNDACCTYPE, accTypeStr);
		fincControl.registAccFund.put(Finc.I_NICKNAME, nickNameStr);
		fincControl.registAccFund.put(Finc.I_FINCACCOUNT, fundAcc);
//		Intent intent = new Intent();
//		intent.setClass(this, FincCheckInAccConfirmActivity.class);
//		startActivityForResult(intent,
//				ConstantGloble.ACTIVITY_REQUEST_CHECKINFUNCACC_CODE);
		requestCommConversationId();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_next:
			if (accountId != null) {
				BaseHttpEngine.showProgressDialog();
				checkINAccConfirm(accountId);
			}else{
				BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_notselecte_acc_error));
			}
			break;

		case R.id.ib_top_right_btn:
			finish();
			break;
		default:
			break;
		}
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
			setResult(ConstantGloble.FINC_CLOSE);
			finish();
			break;

		default:
			break;
		}
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
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			checkINAccSuccess(accountId, fundAcc, tokenId);
		}

	}
	

	@Override
	public void checkINAccSuccessCallback(Object resultObj) {
		super.checkINAccSuccessCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
//		requestPsnAccountQueryAccountDetail(accountId);
		Intent intent = new Intent();
		intent.setClass(this, FincCheckInAccSuccessActivity.class);
		startActivityForResult(intent,
				ConstantGloble.ACTIVITY_REQUEST_CHECKINFUNCACC_CODE);
		
	}
//	@Override
//	public void requestPsnAccountQueryAccountDetailCallback(Object resultObj) {
//		super.requestPsnAccountQueryAccountDetailCallback(resultObj);
//		BaseHttpEngine.dissMissProgressDialog();
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		Map<String, Object> result = (Map<String, Object>) biiResponseBody
//				.getResult();
//		if (result == null) {
//			return;
//		} else {
//			List<Map<String, String>> accountDetaiList = (List<Map<String, String>>) result
//					.get(Finc.FINC_ACCOUNTDETAILLIST_RES);
//			if (accountDetaiList == null) {
//				return;
//			} else {
//				for (Map<String, String> map : accountDetaiList) {
//					if (map.get(Finc.FINC_CURRENCYCODE_RES).equals(
//							ConstantGloble.PRMS_CODE_RMB)) {
//						accbalanceStr = map.get(Finc.FINC_AVAILABLEBALANCE_RES);
//						currencyStr = map.get(Finc.FINC_CURRENCYCODE_RES);
//					}
//				}
//
//			}
//		}
//		fincControl.registAccFund.put(Finc.I_CURRENCYCODE, currencyStr);
//		fincControl.registAccFund.put(Finc.I_ACCBALANCE, accbalanceStr);
//		Intent intent = new Intent();
//		intent.setClass(this, FincCheckInAccSuccessActivity.class);
//		startActivityForResult(intent,
//				ConstantGloble.ACTIVITY_REQUEST_CHECKINFUNCACC_CODE);
//	}
	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
			if (biiResponseBody.getError().getCode()
					.equals(ErrorCode.FINC_ACCCHECIN_ERROR)) {
				BaseDroidApp.getInstanse().showMessageDialog(
						biiResponseBody.getError().getMessage(),
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								FincFundCheckInAccMainActivity.this.setResult(ConstantGloble.FINC_CLOSE);
								FincFundCheckInAccMainActivity.this.finish();
							}
						});
				return true;
			}

		}
		return super.httpRequestCallBackPre(resultObj);
		

	}
}
