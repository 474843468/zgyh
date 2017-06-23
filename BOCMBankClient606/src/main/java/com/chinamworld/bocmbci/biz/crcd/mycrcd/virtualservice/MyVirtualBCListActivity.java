package com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.BusinessModelControl;
import com.chinamworld.bocmbci.biz.crcd.CrcdAccBaseActivity;
import com.chinamworld.bocmbci.biz.crcd.adapter.CrcdSetupAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 银行卡列表 -------我的虚拟卡-----选择信用卡页面
 * 
 * @author huangyuchao
 * 
 */
public class MyVirtualBCListActivity extends CrcdAccBaseActivity {
	private View view = null;

	Button sureButton;

	/** 信用卡列表 */
	private ListView myListView;

	CrcdSetupAdapter adapter;
	public static String accountId;
	static String accountNumber;
	static String accountName;
	static String accountType;
	static String currencyCode;
	static String nickName;
	static int accNum = 0;

	static String strAccountType;

	TextView tv_service_title;

	RelativeLayout cardLayout;
	LinearLayout nocardLayout;
	/** 关联新账户 */
	Button btn_description;
	/** 虚拟银行卡查询--结果result----virCardList */
	static List<Map<String, Object>> virCardList;
	/** 虚拟银行卡查询--结果result */
	static Map<String, Object> returnMap;
	static int maxNum;
	private boolean isShowView = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_virtual_myxunicard));
		setLeftSelectedPosition("myCrcd_4");
		if (view == null) {
			view = addView(R.layout.crcd_mycard_virtual_list);
		}
		back = (Button) findViewById(R.id.ib_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(6);
				finish();
			}
		});
		isShowView = false;
		requestCrcdList();
	}

	public void requestCrcdList() {
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialogCanGoBack();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.QRY_CRCD_LIST_API);
		Map<String, Object> params = new HashMap<String, Object>();
		String[] s = { ZHONGYIN, GREATWALL };
		params.put(Crcd.CRCD_ACCOUNTTYPE_REQ, s);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestCrcdListCallBack");
	}

	/**
	 * 请求信用卡列表回调
	 */
	public void requestCrcdListCallBack(Object resultObj) {
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		List<Map<String, Object>> returnList = (List<Map<String, Object>>) body.getResult();
		if (StringUtil.isNullOrEmpty(returnList)) {
			BaseHttpEngine.dissMissProgressDialog();
			if (isShowView) {
				// 请求信用卡列表
				removeAllViews(view);
				view = addView(R.layout.crcd_mycard_virtual_list);
			}
			init();
			nocardLayout.setVisibility(View.VISIBLE);
			setTitle(this.getString(R.string.mycrcd1));
			cardLayout.setVisibility(View.GONE);
			return;
		}
		BaseHttpEngine.dissMissProgressDialog();
		for (int i = 0; i < returnList.size(); i++) {
			if (ZHONGYIN.equals(returnList.get(i).get(Crcd.CRCD_ACCOUNTTYPE_RES))
					|| GREATWALL.equals(returnList.get(i).get(Crcd.CRCD_ACCOUNTTYPE_RES))) {
				bankSetupList.add(returnList.get(i));
			}
		}
		if (isShowView) {
			// 请求信用卡列表
			removeAllViews(view);
			view = addView(R.layout.crcd_mycard_virtual_list);
		}
		showInit();
	}

	private void showInit() {
		init();
		if (bankSetupList == null || bankSetupList.size() == 0) {
			nocardLayout.setVisibility(View.VISIBLE);
			setTitle(this.getString(R.string.mycrcd1));
			cardLayout.setVisibility(View.GONE);
		} else {
			setTitle(this.getString(R.string.mycrcd_virtual_myxunicard));
			nocardLayout.setVisibility(View.GONE);
			cardLayout.setVisibility(View.VISIBLE);
			adapter = new CrcdSetupAdapter(this, bankSetupList, -1);
			myListView.setAdapter(adapter);
			initOnClick();
		}

	}

	/** 初始化界面 */
	private void init() {
		cardLayout = (RelativeLayout) findViewById(R.id.cardLayout);
		nocardLayout = (LinearLayout) findViewById(R.id.nocardLayout);
		btn_description = (Button) findViewById(R.id.btn_description);
		btn_description.setVisibility(View.GONE);//屏蔽自助关联
		btn_description.setOnClickListener(goRelevanceClickListener);
		tv_service_title = (TextView) findViewById(R.id.tv_service_title);
		tv_service_title.setText(this.getString(R.string.mycrcd_select_zhucard));
		accNum = 0;
		sureButton = (Button) findViewById(R.id.sureButton);
		myListView = (ListView) view.findViewById(R.id.crcd_mycrcdlist);
	}

	private void initOnClick() {
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (accNum == 0) {
					String errorInfo = getResources().getString(R.string.crcd_notselectacard_error);
					BaseDroidApp.getInstanse().showInfoMessageDialog(errorInfo);
					return;
				} else {
					requestCommConversationId();
					BaseHttpEngine.showProgressDialog();
				}
			}
		});
		myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				accNum = position + 1;
				accountId = (String) bankSetupList.get(position).get(Crcd.CRCD_ACCOUNTID_RES);
				accountNumber = (String) bankSetupList.get(position).get(Crcd.CRCD_ACCOUNTNUMBER_RES);
				currencyCode = (String) bankSetupList.get(position).get(Crcd.CRCD_CURRENCYCODE);
				nickName = String.valueOf(bankSetupList.get(position).get(Crcd.CRCD_NICKNAME_RES));
				accountName = String.valueOf(bankSetupList.get(position).get(Crcd.CRCD_ACCOUNTNAME_RES));

				accountType = String.valueOf(bankSetupList.get(position).get(Crcd.CRCD_ACCOUNTTYPE_RES));

				strAccountType = LocalData.AccountType.get(accountType);

				adapter.setSelectedPosition(position);
			}
		});
	}

	/** 进行自助关联监听事件 */
	OnClickListener goRelevanceClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

//			startActivityForResult((new Intent(MyVirtualBCListActivity.this, AccInputRelevanceAccountActivity.class)),
//					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
			BusinessModelControl.gotoAccRelevanceAccount(MyVirtualBCListActivity.this,  ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, null);
		}
	};

	// 点击下一步，查询信用卡下的虚拟卡，需重新请求ConversationId
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 查询信用卡下的虚拟卡
		psnCrcdVirtualCardQuery();
	}

	/** 虚拟银行卡查询 */
	private void psnCrcdVirtualCardQuery() {
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDVIRTUALCARDQUERY);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		params.put(Crcd.CRCD_PAGESIZE, "10");
		params.put(Crcd.CRCD_CURRENTINDEX, "0");
		params.put(Crcd.CRCD_REFRESH, "true");
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdVirtualCardQueryCallBack");
	}

	/** 虚拟银行卡查询-----回调 */
	public void psnCrcdVirtualCardQueryCallBack(Object returnObj) {

		BiiResponse response = (BiiResponse) returnObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		if (!StringUtil.isNullOrEmpty(returnMap)) {
			returnMap.clear();
		}
		returnMap = (Map<String, Object>) body.getResult();
		if (StringUtil.isNullOrEmpty(returnMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.acc_transferquery_null));
			return;
		}
		if (!returnMap.containsKey(Crcd.CRCD_VIRCARDLIST)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.acc_transferquery_null));
			return;
		}
		if (virCardList != null && !virCardList.isEmpty()) {
			virCardList.clear();
		}
		virCardList = (List<Map<String, Object>>) returnMap.get(Crcd.CRCD_VIRCARDLIST);
		if (virCardList == null || virCardList.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.acc_transferquery_null));
			return;
		}
		String recordNum = String.valueOf(returnMap.get(Crcd.CRCD_RECORDNUMBER));
		if (StringUtil.isNull(recordNum)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.acc_transferquery_null));
			return;
		}
		maxNum = Integer.valueOf(recordNum);
		BaseHttpEngine.dissMissProgressDialog();
		Intent it = new Intent(MyVirtualBCListActivity.this, VirtualBCListActivity.class);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(Crcd.CRCD_CURRENCYCODE, currencyCode);
		it.putExtra(Crcd.CRCD_ACCOUNTNAME_RES, accountName);
		startActivity(it);
	}

//	@Override
//	protected void onResume() {
//		setLeftSelectedPosition("myCrcd_4");
//		super.onResume();
//	}

	@Override
	protected void onDestroy() {
		BaseHttpEngine.dissMissProgressDialog();
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE && resultCode == RESULT_OK) {
			isShowView = true;
			requestCrcdList();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (adapter != null) {
			adapter.setSelectedPosition(-1);
		} else {
			adapter = new CrcdSetupAdapter(this, bankSetupList, -1);
			myListView.setAdapter(adapter);
		}
		accNum = 0;
		super.onNewIntent(intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(6);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
