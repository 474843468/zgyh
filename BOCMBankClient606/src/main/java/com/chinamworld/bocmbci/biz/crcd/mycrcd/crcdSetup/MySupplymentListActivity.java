package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.biz.crcd.adapter.CrcdSetupAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;

/**
 * 
 * 附属卡服务定制列表
 * 
 * @author huangyuchao
 * 
 */
public class MySupplymentListActivity extends CrcdBaseActivity {

	private View view = null;

	Button sureButton;

	/** 信用卡列表 */
	private ListView myListView;

	CrcdSetupAdapter adapter;

	TextView tv_title;
	/** 用户选择的附属卡accountId */
	static String accountId;
	/** 账号 */
	static String accountNumber;

	private int accNum = 0;

	static int selectPosition;
	private String accountType = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_fushu_service_setup));
		if (view == null) {
			view = addView(R.layout.crcd_mycard_setup_list);
		}
		setLeftSelectedPosition("myCrcd_2");
		back = (Button) findViewById(R.id.ib_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MySupplymentListActivity.this, MyCardSetupMenuActivity.class);
				startActivity(intent);
				finish();
			}
		});
		// 获取信用卡列表
		requestCrcdList();

	}

	//** 初始化界面 *//*
	private void init() {

		tv_title = (TextView) findViewById(R.id.tv_service_title);
		tv_title.setText(getString(R.string.mycrcd_select_zhucard));

		sureButton = (Button) findViewById(R.id.sureButton);
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
		selectPosition = -1;
		myListView = (ListView) view.findViewById(R.id.crcd_mycrcdlist);
		adapter = new CrcdSetupAdapter(this, bankSetupList, selectPosition);

		myListView.setAdapter(adapter);

		myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				accNum = position + 1;
				accountId = (String) bankSetupList.get(position).get(Crcd.CRCD_ACCOUNTID_RES);
				accountNumber = (String) bankSetupList.get(position).get(Crcd.CRCD_ACCOUNTNUMBER_RES);
				accountType = (String) bankSetupList.get(position).get(Crcd.CRCD_ACCOUNTTYPE_RES);
				adapter.setSelectedPosition(position);
			}
		});
	}

	public void requestCrcdList() {
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialogCanGoBack();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.QRY_CRCD_LIST_API);
		Map<String, Object> params = new HashMap<String, Object>();
		String[] s = { ZHONGYIN, GREATWALL, SINGLEWAIBI };
		params.put(Crcd.CRCD_ACCOUNTTYPE_REQ, s);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestCrcdListCallBack");
	}

	/*
	 * 请求信用卡列表回调
	 */
	public void requestCrcdListCallBack(Object resultObj) {
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		List<Map<String, Object>> returnList = (List<Map<String, Object>>) body.getResult();
		if (bankSetupList != null && bankSetupList.size() > 0) {
			bankSetupList.clear();
		}
		for (int i = 0; i < returnList.size(); i++) {
			if (ZHONGYIN.equals(returnList.get(i).get(Crcd.CRCD_ACCOUNTTYPE_RES))
					|| GREATWALL.equals(returnList.get(i).get(Crcd.CRCD_ACCOUNTTYPE_RES))
					|| SINGLEWAIBI.equals(returnList.get(i).get(Crcd.CRCD_ACCOUNTTYPE_RES))) {
				bankSetupList.add(returnList.get(i));
			}
		}
		BaseHttpEngine.dissMissProgressDialog();
		init();
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 附属卡查询
		psnCrcdAppertainTranSetQuery();
	}

	public void psnCrcdAppertainTranSetQuery() {
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDAPPERTAINNTRANQUERY);
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdAppertainTranSetQueryCallBack");
	}

	/** 附属卡交易明细查询结果 */
	protected static List<Map<String, Object>> returnList;

	public void psnCrcdAppertainTranSetQueryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
		returnList = (List<Map<String, Object>>) body.getResult();
		if (returnList != null && returnList.size() > 0) {
			Intent it = new Intent(MySupplymentListActivity.this, MySupplymentDetailActivity.class);
			it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
			it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
			it.putExtra(Crcd.CRCD_ACCOUNTTYPE_RES, accountType);
			// startActivity(it);
			startActivityForResult(it, ConstantGloble.ACTIVITY_RESULT_CODE);
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			switch (requestCode) {
			case ConstantGloble.ACTIVITY_RESULT_CODE:// 消费服务设置
				adapter = new CrcdSetupAdapter(this, bankSetupList, -1);
				adapter.notifyDataSetChanged();
				myListView.setAdapter(adapter);
				accNum = 0;
				adapter.setSelectedPosition(-1);
				break;

			default:
				break;
			}
			break;
		case RESULT_CANCELED:

			break;
		default:
			break;
		}
	}

//	@Override
//	protected void onResume() {
//		super.onResume();
//		LayoutValue.LEWFTMENUINDEX = 1;
//	}

	@Override
	protected void onDestroy() {
		BaseHttpEngine.dissMissProgressDialog();
		super.onDestroy();
	}

	@Override
	protected void onNewIntent(Intent intent) {

		adapter.setSelectedPosition(-1);

		super.onNewIntent(intent);
	}

}
