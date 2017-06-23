package com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdAccBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StepTitleUtils;

/**
 * 虚拟银行卡协议
 * 
 * @author huangyuchao
 * 
 */
public class VirtualBCServiceReadActivity extends CrcdAccBaseActivity {

	private View view = null;

	Button lastButton, sureButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_virtual_shenqing));
		if (view == null) {
			view = addView(R.layout.crcd_mycard_service_read);
		}
		init();
	}

	static String accountId;
	static String accountNumber;
	static String currencyCode;
	private int accNum = 0;

	TextView tv_jianfang;

	/** 初始化界面 */
	private void init() {
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getResources().getString(
								R.string.mycrcd_virtual_read_xieyi),
						this.getResources().getString(
								R.string.mycrcd_virtual_write_info),
						this.getResources().getString(
								R.string.mycrcd_setup_info) });
		StepTitleUtils.getInstance().setTitleStep(1);

		tv_jianfang = (TextView) findViewById(R.id.tv_jianfang);

		// String loginName=BaseDroidApp.getInstanse().getSharedPrefrences()
		// .getString(ConstantGloble.LOGIN_NAME, "");

		Map<String, Object> returnMap = (Map<String, Object>) BaseDroidApp
				.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA);
		String loginName = String
				.valueOf(returnMap.get(Crcd.CRCD_CUSTOMERNAME));

		String prefix = this.getString(R.string.mycrcd_jiafang);

		tv_jianfang.setText(prefix + loginName);

		lastButton = (Button) findViewById(R.id.lastButton);
		lastButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		sureButton = (Button) findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				psnCrcdVirtualCardApplyInit();

			}
		});

	}

	// 申请虚拟银行卡初始化
	public void psnCrcdVirtualCardApplyInit() {
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDVIRTUALCARDAPPLYINIT);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_ACCOUNTID_RES,
				VirtualBCServiceListActivity.accountId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this,
				"psnCrcdVirtualCardApplyInitCallBack");
	}

	static Map<String, Object> returnMap;

	public void psnCrcdVirtualCardApplyInitCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		returnMap = (Map<String, Object>) body.getResult();
		Intent it = new Intent(VirtualBCServiceReadActivity.this,
				VirtualBCServiceWriteActivity.class);
		startActivity(it);
	}



	@Override
	protected void onDestroy() {
		BaseHttpEngine.dissMissProgressDialog();
		super.onDestroy();
	}
}
