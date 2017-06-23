package com.chinamworld.bocmbci.biz.remittance.templateManagement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Remittance;
import com.chinamworld.bocmbci.biz.remittance.RemittanceBaseActivity;
import com.chinamworld.bocmbci.biz.remittance.RemittanceDataCenter;
import com.chinamworld.bocmbci.biz.remittance.applicationForRemittance.RemittanceConfirmActivity;
import com.chinamworld.bocmbci.biz.remittance.dialog.AccDetailDialogActivity;
import com.chinamworld.bocmbci.biz.remittance.fragment.TransationInfoFragment;
import com.chinamworld.bocmbci.biz.remittance.interfaces.AccDetailListnenr;
import com.chinamworld.bocmbci.biz.remittance.interfaces.NeedAccDetailListener;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 模板管理模块发起汇款页
 * 
 * @author Zhi
 */
public class StartRemittanceActivity extends RemittanceBaseActivity implements NeedAccDetailListener,AccDetailListnenr {

	/** 交易信息输入页面片段 */
	private TransationInfoFragment transationInfoFragment;
	/** 要获取卡详情通知的对象注册在这里 */
	private AccDetailListnenr needDetailView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.remittance_template_startremittance);
		initView();
	}
	
	private void initView() {
		Map<String, Object> detailMap = RemittanceDataCenter.getInstance().getMapPsnInternationalTransferTemplateDetailQuery();
		((TextView) findViewById(R.id.tv_swiftAccountNumber)).setText(StringUtil.getForSixForString((String) detailMap.get(Remittance.SWIFTACCOUNTNUMBER)));
		// 汇款人信息
		((TextView) findViewById(R.id.tv_remittorName)).setText((String) detailMap.get(Remittance.REMITTORNAME));
		((TextView) findViewById(R.id.tv_remittorAddress)).setText((String) detailMap.get(Remittance.REMITTORADDRESS));
		((TextView) findViewById(R.id.tv_remittersZip)).setText((String) detailMap.get(Remittance.REMITTERSZIP));
		((TextView) findViewById(R.id.tv_payerPhone)).setText((String) detailMap.get(Remittance.PAYERPHONE));
		// 收款人信息
		((TextView) findViewById(R.id.tv_gatheringArea)).setText((String) detailMap.get(Remittance.GATHERINGAREA));
		((TextView) findViewById(R.id.tv_payeePermanentCountry)).setText((String) detailMap.get(Remittance.PAYEEPERMANENTCOUNTRY));
		((TextView) findViewById(R.id.tv_payeeEnName)).setText((String) detailMap.get(Remittance.PAYEEENNAME));
		((TextView) findViewById(R.id.tv_payeeEnAddress)).setText((String) detailMap.get(Remittance.PAYEEENADDRESS));
		((TextView) findViewById(R.id.tv_payeeActno)).setText((String) detailMap.get(Remittance.PAYEEACTNO));
		((TextView) findViewById(R.id.tv_payeeBankSwift)).setText((String) detailMap.get(Remittance.PAYEEBANKSWIFT));
		((TextView) findViewById(R.id.tv_payeeBankName)).setText((String) detailMap.get(Remittance.PAYEEBANKNAME));
		((TextView) findViewById(R.id.tv_payeeBankNum)).setText((String) detailMap.get(Remittance.PAYEEBANKNUM));
		// 交易信息填写
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		if (transationInfoFragment == null) {
			transationInfoFragment = new TransationInfoFragment();
			transationInfoFragment.setNeedAccDetailListener(this);
		}
		ft.replace(R.id.ll_input3, transationInfoFragment);
		ft.commit();
		transationInfoFragment.accChange((String) detailMap.get(Remittance.SWIFTACCOUNTNUMBER));
		
		findViewById(R.id.btn_accDetail).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!StringUtil.isNullOrEmpty(RemittanceDataCenter.getInstance().getAccDetail())) {
					startActivity(new Intent(StartRemittanceActivity.this, AccDetailDialogActivity.class));
				} else {
					BaseHttpEngine.showProgressDialog();
					setNeedDetailView(StartRemittanceActivity.this);
					Map<String, Object> params = new HashMap<String, Object>();
					params.put(Comm.ACCOUNT_ID, RemittanceDataCenter.getInstance().getMapPsnInternationalTransferTemplateDetailQuery().get(Remittance.SWIFTACCOUNTNUMBER));
					getHttpTools().requestHttp(Acc.QRY_ACC_BALANCE_API, "requestPsnAccountQueryAccountDetailCallBack", params, false);
				}
			}
		});
		findViewById(R.id.btnNext).setOnClickListener(nextListener);
	}

	@Override
	public void detailCallBack(List<Map<String, Object>> detailList) {
		startActivity(new Intent(StartRemittanceActivity.this, AccDetailDialogActivity.class));
	}
	
	/** 下一步监听 */
	private OnClickListener nextListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO 检查提交交易时需要上送的参数列表
			BaseHttpEngine.showProgressDialog();
			requestGetSecurityFactor(Remittance.ServiceId);
		}
	};

	@Override
	public void setNeedDetailView(AccDetailListnenr needDetailView) {
		this.needDetailView = needDetailView;
	}
	
	/** 查询账户详情回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnAccountQueryAccountDetailCallBack(Object resultObj) {
		Map<String, Object> resultMap = (Map<String, Object>) getHttpTools().getResponseResult(resultObj);
		List<Map<String, Object>> accountDetailList = (List<Map<String, Object>>) (resultMap.get(ConstantGloble.ACC_DETAILIST));
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(accountDetailList)) {
			return;
		}
		RemittanceDataCenter.getInstance().setAccDetail(accountDetailList);
		if (needDetailView != null) {
			needDetailView.detailCallBack(accountDetailList);
		}
	}
	
	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseDroidApp.getInstanse().showSeurityChooseDialog(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Map<String, Object> params = new HashMap<String, Object>();
						getHttpTools().requestHttp(Remittance.PSNTRANSINTERNATIONALTRANSFERCONFIRM, "requestPsnTransInternationalTransferConfirmCallBack", params, true);
					}
				});
	}

	/** 跨境汇款预交易回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnTransInternationalTransferConfirmCallBack(Object resultObj) {
		Map<String, Object> resultMap = (Map<String, Object>) getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		RemittanceDataCenter.getInstance().setMapPsnTransInternationalTransferConfirm(resultMap);
		requestForRandomNumber();
	}

	/** 请求随机数  */
	private void requestForRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this,"queryRandomNumberCallBack");
	}
	
	public void queryRandomNumberCallBack(Object resultObj) {
		String randomNumber = (String) this.getHttpTools().getResponseResult(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNull(randomNumber)) return;
		startActivityForResult(new Intent(this, RemittanceConfirmActivity.class).putExtra(Remittance.RANDOM, randomNumber), QUIT_CODE);
	}
}
