package com.chinamworld.bocmbci.biz.tran.collect;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Collect;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.tran.collect.adapter.AccountListAdapter;
import com.chinamworld.bocmbci.biz.tran.collect.setting.CollectSettingActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;

/**
 * 选择账户后，没有归集账户时，进入此页面
 * @author yuht
 *
 */
public class UnCollectAccountMainActivity extends CollectBaseActivity {
	
	private static final int request_setting_Code = 10001;
	
	/** 归集账户 */
	private String mPayeeAccount;
	/** 未被归集账户数据源 */
	private AccountListAdapter mUnCollectAdapter;
	/** 未设置资金归集 */
	private ListView mUnCollectListView;
	/** 未设置归集账户空提示 */
	private View mUnCollectEmptyView;
	private int position = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tabcontent.removeAllViews();
		View view = LayoutInflater.from(this).inflate(R.layout.uncollect_account_main_layout, tabcontent,false);
		tabcontent.addView(view);
		setTitle(getString(R.string.collect_title));
		
		mUnCollectEmptyView = view.findViewById(R.id.tv_uncollect_prompt);
		mUnCollectListView = (ListView) view.findViewById(R.id.lv_uncollect);
		mUnCollectListView.setOnItemClickListener(unCollectListener);
		
		mPayeeAccount = (String)CollectData.getInstance().curSelectedAccount.get(Acc.ACC_ACCOUNTNUMBER_RES);
		position = this.getIntent().getIntExtra("position", 0);
		requestPsnCBRestBankview();
	}
	
	/**
	 * 未设置归集账户列表事件
	 */
	private OnItemClickListener unCollectListener = new OnItemClickListener() {
		@SuppressWarnings("unchecked")
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Map<String, Object> selectMap = mUnCollectAdapter.getItem(position);
			String unaccount = (String) selectMap.get(Collect.payerAccount);

			String isquery = (String) selectMap.get("isquery");
			if (isquery == null) {
				BaseDroidApp.getInstanse().createDialog(null,
						"被归集账户须同时签约查询协议和支付协议，请到网银开通查询协议");
				return;
			}

			String ispay = (String) selectMap.get("ispay");
			if (ispay == null) {
				BaseDroidApp.getInstanse().createDialog(null,
						"被归集账户须同时签约查询协议和支付协议，请到网银开通支付协议");
				return;
			}

			Map<String, Object> resultMap = (Map<String, Object>) BaseDroidApp
					.getInstanse().getBizDataMap()
					.get(ConstantGloble.BIZ_LOGIN_DATA);
			String cbMobileParam = String.valueOf(resultMap.get("mobile"));
			Intent intent = new Intent(UnCollectAccountMainActivity.this,
					CollectSettingActivity.class);
			intent.putExtra(Collect.cbAccCard, mPayeeAccount);
			intent.putExtra(Collect.payerAccount, unaccount);
			intent.putExtra(Collect.cbMobile, cbMobileParam);
			startActivityForResult(intent, request_setting_Code);
		}
	};

	
	/**
	 * 根据中行账户查询协议信息
	 */
	public void requestPsnCBRestBankview() {
		BiiHttpEngine.showProgressDialogCanGoBack();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Collect.PsnCBRestBankview);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Collect.accountId, CollectData.getInstance().curSelectedAccount.get(Comm.ACCOUNT_ID));
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnCBRestBankviewCallback");
	}

	/**
	 * 中行账户查询协议信息回调
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnCBRestBankviewCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultData = (Map<String, Object>) biiResponseBody
				.getResult();
		// 设置已设置被归集账户
		List<Map<String, Object>> collectDatas = CollectData.getInstance()
				.getCollectAccount((String)CollectData.getInstance().curSelectedAccount.get(Acc.ACC_ACCOUNTNUMBER_RES));

		List<Map<String, Object>> queryList = (List<Map<String, Object>>) resultData
				.get(Collect.queryList);
		List<Map<String, Object>> payList = (List<Map<String, Object>>) resultData
				.get(Collect.payList);
		// 过滤方式
		// 根据卡号的唯一性。
		// map<银行号，set<银行卡>>
		LinkedHashMap<String, Map<String, Object>> unCollectData = new LinkedHashMap<String, Map<String, Object>>();
		if(payList != null){
			for (Map<String, Object> pay : payList) {
				Map<String, Object> unCollect = new HashMap<String, Object>();
				String account = (String) pay.get(Collect.retAct);
				unCollect.put(Collect.payerAcctBankNo, pay.get(Collect.retBankNo));
				unCollect.put(Collect.payerAccBankName, pay.get(Collect.retBank));
				unCollect.put(Collect.payerAccount, account);
				unCollect.put(Collect.payerAccountName, pay.get(Collect.retName));
				unCollect.put(Collect.retActype, pay.get(Collect.retActype));
				unCollect.put(Collect.status, pay.get(Collect.payStatus));
				unCollect.put("ispay", Boolean.toString(true));
				unCollectData.put(account, unCollect);
			}
		}
		
		if(queryList != null){
			for (Map<String, Object> query : queryList) {
				String account = (String) query.get(Collect.rptAct);
				if (unCollectData.containsKey(account)) {
					Map<String, Object> unCollect = unCollectData.get(account);
					unCollect.put("isquery", Boolean.toString(true));
				} else {
					Map<String, Object> unCollect = new HashMap<String, Object>();
					unCollect.put(Collect.payerAcctBankNo,
							query.get(Collect.rptBankNo));
					unCollect.put(Collect.payerAccBankName,
							query.get(Collect.rptBank));
					unCollect.put(Collect.payerAccount, query.get(Collect.rptAct));
					unCollect.put(Collect.payerAccountName,
							query.get(Collect.rptName));
					unCollect.put(Collect.status, query.get(Collect.queryStatus));
					unCollect.put("isquery", Boolean.toString(true));
					unCollectData.put(account, unCollect);
				}
			}
		}

		if(collectDatas != null){
			for (Map<String, Object> collect : collectDatas) {
				String account = (String) collect.get(Collect.payerAccount);
				if (unCollectData.containsKey(account)) {
					unCollectData.remove(account);
				}
			}
		}

		Collection<Map<String, Object>> values = unCollectData.values();
		List<Map<String, Object>> unCollectList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : values) {
			unCollectList.add(map);
		}

		CollectData.getInstance().setQueryList(mPayeeAccount, queryList);
		CollectData.getInstance().setPayList(mPayeeAccount, payList);
		CollectData.getInstance().setUnCollectAccount(mPayeeAccount,
				unCollectList);
		setUnCollectAdapter(unCollectList);
	}
	
	private void setUnCollectAdapter(List<Map<String, Object>> data) {
		if (mUnCollectAdapter == null) {
			mUnCollectAdapter = new AccountListAdapter(this, data);
			mUnCollectListView.setAdapter(mUnCollectAdapter);
		} else {
			mUnCollectAdapter.setData(data);
			mUnCollectAdapter.notifyDataSetChanged();
		}
		if (mUnCollectAdapter.isEmpty()) {
			mUnCollectEmptyView.setVisibility(View.VISIBLE);
			mUnCollectListView.setVisibility(View.GONE);
		} else {
			mUnCollectEmptyView.setVisibility(View.GONE);
			mUnCollectListView.setVisibility(View.VISIBLE);
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// 归集账户
		if(resultCode == CollectListActivity.refresh_list && requestCode == UnCollectAccountMainActivity.request_setting_Code){
			finish();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("position", position);
			ActivityIntentTools.intentToActivityWithData(UnCollectAccountMainActivity.this, CollectListActivity.class, map);
		}
	}


}
