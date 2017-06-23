package com.chinamworld.bocmbci.biz.finc.accmanager;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.fundacc.FincFundAccMainActivity;
import com.chinamworld.bocmbci.biz.invest.constant.InvestConstant;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.ErrorCode;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.LoginTask.*;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 基金账户管理
 * @author xiaoyl
 *
 */
public class FincAccManagerMenuActivity extends FincBaseActivity {

	private static final String TAG = "FincAccManagerMenuActivity";

	/** 基金交易账户 , 基金TA账户*/
	private View fundAccView,fundTAAccView;
	/** 标记点击的账户类型*/
	private int flag;
	private final int FUNDACC = 1;//基金交易账户
	private final int FUNDTAACC = 2;//基金TA账户

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	@Override
	protected void onResume() {
		super.onResume();
		fincControl.cleanAllData();
		setLeftSelectedPosition("finc_3");
	}
	
	private void initView(){
		View childView = mainInflater.inflate(R.layout.finc_accmanager_menu, null);
		tabcontent.addView(childView);
		fundAccView = childView.findViewById(R.id.finc_fundAcc_view);
		fundTAAccView = childView.findViewById(R.id.finc_fundTAAcc_view);
		setTitle(R.string.mian_menu1);
		right.setVisibility(View.GONE);
	}
	private void initListenner(){
		fundAccView.setOnClickListener(this);
		fundTAAccView.setOnClickListener(this);
	}

	private void init() {
		initView();
		initListenner();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ConstantGloble.ACTIVITY_REQUEST_MANAGE_CODE:// 开通理财服务
			switch (resultCode) {
			case RESULT_OK:
				fincControl.ifInvestMent = true;
				if (!fincControl.ifhaveaccId) {// 如果还没有基金账户
					getPopup();
				}
				break;
			default:
				fincControl.ifInvestMent = false;
				getPopup();
				break;
			}
			break;
		case ConstantGloble.ACTIVITY_REQUEST_SETFINCSACC_CODE:// 开通基金账户
			switch (resultCode) {
			case RESULT_OK:
				fincControl.ifhaveaccId = true;
				switch (flag) {
				case FUNDACC: // 基金交易账户
//					BaseHttpEngine.dissMissProgressDialog();
					doCheckRequestAccInfo();
//					startActivity(new Intent(this,FincAccManagerActivity.class));
					break;
				case FUNDTAACC: // 基金TA账户
					BaseHttpEngine.dissMissProgressDialog();
					startActivity(new Intent(this, FundTaAccListActivity2.class));
					break;
				}
				break;

			default:
				fincControl.ifhaveaccId = false;
				getPopup();
				break;
			}
			break;
		case InvestConstant.FUNDRISK:// 基金风险评估
			switch (resultCode) {
			case RESULT_OK:
				fincControl.ifdorisk = true;
				switch (flag) {
				case FUNDACC: // 基金交易账户
//					BaseHttpEngine.dissMissProgressDialog();
					doCheckRequestAccInfo();
//					startActivity(new Intent(this,FincAccManagerActivity.class));
					break;
				case FUNDTAACC: // 基金TA账户
					BaseHttpEngine.dissMissProgressDialog();
					startActivity(new Intent(this, FundTaAccListActivity2.class));
					break;
				}
				break;
			default:
				fincControl.ifdorisk = false;
				getPopup();
				break;
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 查询基金账户check
	 */
	public void doCheckRequestAccInfo() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_QUERYINVTBINDINGINFO_API);
		biiRequestBody.setConversationId(null);
		Map<String, String> map = new Hashtable<String, String>();
		map.put(Finc.FINC_INVTTYPE_REQ, ConstantGloble.FINC_SERVICECODE);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"doCheckRequestAccInfoCallback");
	}

	public void doCheckRequestAccInfoCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		Map<String, String> map = (Map<String, String>) biiResponseBodys
				.get(0).getResult();
		if (!StringUtil.isNullOrEmpty(map.get(Finc.FINC_INVESTACCOUNT_RES))) {
			fincControl.accId = map.get(Finc.FINC_ACCOUNTID_RES);
			fincControl.invAccId = map.get(Finc.FINC_INVESTACCOUNT_RES);
			fincControl.accNum = map.get(Finc.FINC_ACCOUNT_RES);
			fincControl.accountType=map.get(Finc.FINC_ACCOUNTTYPE);
			fincControl.accDetailsMap = map;
			fincControl.ifhaveaccId = true;
			
			startActivity(new Intent(this,FincAccManagerActivity.class));
		}
	}
	@Override
	public void doCheckRequestQueryInvtBindingInfoCallback(
			Object resultObj) {
		super.doCheckRequestQueryInvtBindingInfoCallback(resultObj);
		if (fincControl.ifhaveaccId&&fincControl.ifInvestMent) {
			switch (flag) {
			case FUNDACC: // 基金交易账户
				/**是否做了风险评估*/
				BaseHttpEngine.showProgressDialog();
				doCheckRequestPsnFundRiskEvaluationQueryResult();
				break;
			case FUNDTAACC: // 基金TA账户
				BaseHttpEngine.dissMissProgressDialog();
				startActivity(new Intent(this, FundTaAccListActivity2.class));
				break;
			}
		}
	}
	
	/**
	 * 检查是否做了风险认证的回调处理
	 * 
	 * @param resultObj
	 */
	public void doCheckRequestPsnFundRiskEvaluationQueryResultCallback(
			Object resultObj) {
		super.doCheckRequestPsnFundRiskEvaluationQueryResultCallback(resultObj);
		if(fincControl.ifdorisk){
			doCheckRequestAccInfo();
			
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_fundAcc_view://基金交易账户
			if (!BaseDroidApp.getInstanse().isLogin()) {//没有登录就跳转到登录页面
//				Intent intent = new Intent(FincAccManagerMenuActivity.this,
//						LoginActivity.class);
//				startActivityForResult(intent,
//						ConstantGloble.ACTIVITY_RESULT_CODE);
				BaseActivity.getLoginUtils(FincAccManagerMenuActivity.this).exe(new LoginCallback() {

					@Override
					public void loginStatua(boolean isLogin) {

					}
				});
				return;
			}
			flag = FUNDACC;
			BaseHttpEngine.showProgressDialog();
			doCheckRequestPsnInvestmentManageIsOpen();
			break;
		case R.id.finc_fundTAAcc_view:// 基金TA账户
			if (!BaseDroidApp.getInstanse().isLogin()) {// 没有登录就跳转到登录页面
//				Intent intent = new Intent(FincAccManagerMenuActivity.this,
//						LoginActivity.class);
//				startActivityForResult(intent,
//						ConstantGloble.ACTIVITY_RESULT_CODE);
				BaseActivity.getLoginUtils(FincAccManagerMenuActivity.this).exe(new LoginCallback() {

					@Override
					public void loginStatua(boolean isLogin) {
						if(isLogin){

						}
					}
				});
				return;
			}
			flag = FUNDTAACC;
			BaseHttpEngine.showProgressDialog();
			doCheckRequestPsnInvestmentManageIsOpen();
			break;
		case R.id.investorRightsAttention:// 投资者权益须知
			startActivity(new Intent(this, FincAccInvestorRightsAttentionActivity.class));
			break;
		}
	}
	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		super.httpRequestCallBackPre(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
			if (biiResponseBody.getError().getCode()
					.equals(ErrorCode.FINC_ACCCHECIN_ERROR)) {
				fincControl.ifhaveaccId = false;
				fincControl.ifdorisk = false;
				BaseHttpEngine.dissMissProgressDialog();
				getPopup();
				return true;
			}
			if (Finc.FINC_RISKEVALUATIONQUERYRESULT_API.equals(biiResponseBody
					.getMethod())) {
				if (biiResponse.isBiiexception()) {//表示返回数据异常
					BiiHttpEngine.dissMissProgressDialog();
					BiiError biiError = biiResponseBody.getError();
					// 判断是否存在error和错误码
					if (biiError != null) {
						if (biiError.getCode() != null){
							String errorCode = biiResponseBody.getError().getCode();
							if(errorCode.equals(ErrorCode.FINC_ACCCHECIN_ERROR)){
										BaseDroidApp.getInstanse().showInfoMessageDialog(
												FincAccManagerMenuActivity.this.getResources().getString(
														R.string.finc_risk_check_error));
								Intent intent=new Intent(FincAccManagerMenuActivity.this, FincFundAccMainActivity.class);
								startActivity(intent);
								
								return true;
							}
							
						}
					}
				}
			}

		}
		return super.httpRequestCallBackPre(resultObj);

	}
}
