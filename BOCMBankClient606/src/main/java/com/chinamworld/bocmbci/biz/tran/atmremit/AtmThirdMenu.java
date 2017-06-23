package com.chinamworld.bocmbci.biz.tran.atmremit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;

/**
 * ATM取款三级菜单页
 * 
 * @wangmengmeng
 * 
 */
public class AtmThirdMenu extends TranBaseActivity {
	private LinearLayout mobileTranLayout;
	private LinearLayout queryResultLayout;
	/** 点击标记 i==1 取款 ; i==2 查询 */
	private int i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.trans_atm_title));
		View view = mInflater.inflate(R.layout.tran_atm_third_menu, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);

		mTopRightBtn.setVisibility(View.INVISIBLE);
		setLeftSelectedPosition("tranManager_5");
		setupView();
	}

//	@Override
//	protected void onResume() {
//		super.onResume();
//		setLeftSelectedPosition("tranManager_5");
//	}

	/**
	 * 初始化加载XML里的控件
	 */
	private void setupView() {

		mobileTranLayout = (LinearLayout) findViewById(R.id.ll_2dimen_generate_trans);
		queryResultLayout = (LinearLayout) findViewById(R.id.ll_2dimen_scan_trans);

		mobileTranLayout.setOnClickListener(mobileTranListener);
		queryResultLayout.setOnClickListener(queryResultListener);

	}

	/**
	 * atm取款
	 */
	private OnClickListener mobileTranListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			i = 1;
			requestAccBankAccountList();
		}
	};
	/**
	 * 交易结果查询
	 */
	private OnClickListener queryResultListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			i = 2;
			requestAccBankAccountList();
		}
	};

	/** 请求所有借记卡账户列表信息 */
	public void requestAccBankAccountList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.QRY_ACC_LIST_API);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		List<String> paramslist = new ArrayList<String>();
		// 传递能作为转出账户的列表
		paramslist.add(ConstantGloble.ACC_TYPE_BRO);
		paramslist.add(ConstantGloble.ACC_TYPE_ORD);
		paramslist.add(ConstantGloble.ACC_TYPE_RAN);
		paramsmap.put(Acc.ACC_ACCOUNTTYPE_REQ, paramslist);
		biiRequestBody.setParams(paramsmap);
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"accBankAccountListCallBack");
	}

	/**
	 * 请求所有借记卡账户列表回调
	 * 
	 * @param resultObj
	 */
	public void accBankAccountListCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> bankAccountList = (List<Map<String, Object>>) (biiResponseBody
				.getResult());
		BaseHttpEngine.dissMissProgressDialog();
		if (i == 1) {
			if (bankAccountList == null || bankAccountList.size() == 0) {
				BaseDroidApp.getInstanse().showMessageDialog(
						this.getString(R.string.trans_no_atm_choose),
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								BaseDroidApp.getInstanse().dismissErrorDialog();
							}
						});
				return;
			}
			TranDataCenter.getInstance().setAccountList(bankAccountList);
			Intent intent = new Intent(AtmThirdMenu.this,
					AtmRemitChooseActivity.class);
			startActivity(intent);
		}
		if (i == 2) {
			if (bankAccountList == null || bankAccountList.size() == 0) {
				BaseDroidApp.getInstanse().showMessageDialog(
						this.getString(R.string.trans_no_atm_choose_query),
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								BaseDroidApp.getInstanse().dismissErrorDialog();
							}
						});
				return;
			}
			TranDataCenter.getInstance().setAccountList(bankAccountList);
			Intent intent = new Intent(this, AtmRemitQueryActivity.class);
			startActivity(intent);
		}

	}
}
