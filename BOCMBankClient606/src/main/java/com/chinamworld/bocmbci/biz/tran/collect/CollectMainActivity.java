package com.chinamworld.bocmbci.biz.tran.collect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Collect;
import com.chinamworld.bocmbci.bii.constant.Collect.CollectStatusType;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IHttpCallBack;
import com.chinamworld.bocmbci.biz.tran.collect.adapter.AccountCardAdapter;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;

/**
 * @ClassName: CollectMainActivity
 * @Description: 跨行资金归集入口
 * @author luql
 * @date 2014-3-18 下午03:33:29
 */
public class CollectMainActivity extends CollectBaseActivity {

	/** 卡列表 */
	private ListView lvAccCard;
	/** 银行卡适配器 */
	private AccountCardAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		getAllBankList();
	}

	private void initView() {
		View viewContent = addView(R.layout.collect_cardlist);
		((Button) viewContent.findViewById(R.id.btnconfirm))
				.setOnClickListener(this);
		lvAccCard = (ListView) viewContent.findViewById(R.id.cardlist);
		lvAccCard.setOnItemClickListener(cardItemClick);

		setTitle(getString(R.string.collect_title));
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		toprightBtn();
		mTopRightBtn.setVisibility(View.INVISIBLE);
		setLeftSelectedPosition("tranManager_7");
	}

	Map<String, Object> map = new HashMap<String, Object>();
	
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		int selectedPosition = mAdapter.getSelectedPosition();
		
		if (selectedPosition > -1) {
			CollectData.getInstance().curSelectedAccount = CollectData
					.getInstance().getAccountList().get(selectedPosition);
			map.clear();
			map.put("position", selectedPosition);
			this.gotoNextActivity();
			// Intent it = new Intent(this, CollectListActivity.class);
			// it.putExtra("position", selectedPosition);
			// startActivity(it);
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.collect_card_tip));// 请选择归集账户
		}
	}

	/** 列表点击事件 **/
	private OnItemClickListener cardItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			int selectedPosition = mAdapter.getSelectedPosition();
			if (selectedPosition != position) {
				mAdapter.setSelectedPosition(position);
			}
		}
	};

	/**
	 * 请求资金归集账户类型
	 * 
	 * @param acctype
	 *            跨行资金支持类型
	 */
	private void getAllBankList() {
		List<String> acctype = new ArrayList<String>();
		acctype.add("119"); // "长城电子借记卡"
		acctype.add("101"); // "普通活期"
		acctype.add("188"); // 活一本
		BaseHttpEngine.showProgressDialogCanGoBack();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.QRY_ALL_BANK_ACCOUNT);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Comm.ACCOUNT_TYPE, acctype);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "allBankAccListCallBack");
	}

	/** 获取账户列表返回 */
	@SuppressWarnings("unchecked")
	public void allBankAccListCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> cardList = (List<Map<String, Object>>) (biiResponseBody
				.getResult());
		if (cardList == null || cardList.size() == 0) {
			BaseDroidApp.getInstanse().showMessageDialog(
					this.getString(R.string.collect_account_error),
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							finish();
						}
					});
			return;
		}
		CollectData.getInstance().setAccountList(cardList);
		mAdapter = new AccountCardAdapter(this, cardList);
		lvAccCard.setAdapter(mAdapter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		CollectData.getInstance().clear();
	}

	private void gotoNextActivity() {
		this.requestPsnCBCollectQuery(
				CollectData.getInstance().curSelectedAccount,
				CollectStatusType.ALL_STATUS, 0, 10, true, new IHttpCallBack() {

					@Override
					public void requestHttpSuccess(Object result) {
						BiiHttpEngine.dissMissProgressDialog();
						List<Map<String, Object>> listData = CollectData
								.getInstance()
								.getCollectAccount(
										(String) CollectData.getInstance().curSelectedAccount
												.get(Acc.ACC_ACCOUNTNUMBER_RES));
//						Intent it = null;
						
						if (listData == null || listData.size() <= 0) { // 无归集账户，直接进入设置归集账户流程
							// it = new Intent(CollectMainActivity.this,
							// UnCollectAccountMainActivity.class);
							// ActivityIntentTools.intentToActivity(
							// CollectMainActivity.this,
							// UnCollectAccountMainActivity.class);
							requestPsnCBRestBankview();


						} else { // 有归集账户
							// it = new Intent(CollectMainActivity.this, /*
							// * CollectListActivity
							// * .class
							// */
							// UnCollectAccountMainActivity.class);
							// it.putExtra("position", position);

							ActivityIntentTools.intentToActivityWithData(
									CollectMainActivity.this,
									CollectListActivity.class, map);

						}

					}

				});
	}

	private String mPayeeAccount;
	
	/**
	 * 根据中行账户查询协议信息
	 */
	public void requestPsnCBRestBankview() {
		mPayeeAccount  = (String)CollectData.getInstance().curSelectedAccount.get(Acc.ACC_ACCOUNTNUMBER_RES);
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
	public void requestPsnCBRestBankviewCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultData = (Map<String, Object>) biiResponseBody
				.getResult();
		// 设置已设置被归集账户
		List<Map<String, Object>> collectDatas = CollectData.getInstance()
				.getCollectAccount(mPayeeAccount);
//		setCollectAdapter(collectDatas);

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
		
		if(unCollectList.size() <= 0){
			// 无归集账户，也无被归集账户
			map.put("selectedIndex", 1);
			ActivityIntentTools.intentToActivityWithData(
					CollectMainActivity.this,
					CollectListActivity.class, map);
		}
		else {
			ActivityIntentTools.intentToActivityWithData(
					CollectMainActivity.this,
					UnCollectAccountMainActivity.class, map);
		}

	}
}
