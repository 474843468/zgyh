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
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 信用卡对账单服务列表
 * 
 * @author huangyuchao
 * 
 */
public class CrcdPsnQueryCheckList extends CrcdBaseActivity {
	private static final String TAG = "CrcdPsnQueryCheckList";
	private View view = null;

	Button sureButton;

	/** 信用卡列表 */
	private ListView myListView;

	CrcdSetupAdapter adapter;
	/** 用户选择的信用卡ID */
	static String accountId;
	static String accountNumber;
	static String currencyCode;
	private int accNum = 0;

	TextView tv_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_bill_zhangdan_service));
		if (view == null) {
			view = addView(R.layout.crcd_mycard_setup_list);
		}
		setLeftSelectedPosition("myCrcd_2");
		back = (Button) findViewById(R.id.ib_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CrcdPsnQueryCheckList.this, MyCardSetupMenuActivity.class);
				startActivity(intent);
				finish();
			}
		});
		// 获取信用卡列表
		requestCrcdList();

	}

	/** 初始化界面 */
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
					// 信用卡账单查询
					requestCommConversationId();
					BaseHttpEngine.showProgressDialog();
				}
			}
		});

		myListView = (ListView) view.findViewById(R.id.crcd_mycrcdlist);
		adapter = new CrcdSetupAdapter(this, bankSetupList, -1);
		myListView.setAdapter(adapter);

		myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				accNum = position + 1;
				accountId = (String) bankSetupList.get(position).get(Crcd.CRCD_ACCOUNTID_RES);
				accountNumber = (String) bankSetupList.get(position).get(Crcd.CRCD_ACCOUNTNUMBER_RES);
				currencyCode = (String) bankSetupList.get(position).get(Crcd.CRCD_CURRENCYCODE);

				adapter.setSelectedPosition(position);
			}
		});
	}

	/** 请求信用卡列表 */
	private void requestCrcdList() {
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

	/**
	 * 请求信用卡列表回调
	 */
	public void requestCrcdListCallBack(Object resultObj) {		
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		List<Map<String, Object>> returnList = (List<Map<String, Object>>) body.getResult();
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
		psnCrcdQueryCheck();
	}

	/** 信用卡账单查询 */
	private void psnCrcdQueryCheck() {
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDQUERYCHECK);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdQueryCheckCallBack");
	}

	/** 信用卡账单查询---回调结果 */
	protected static Map<String, Object> returnMap;

	/** 信用卡账单查询----回调 */
	public void psnCrcdQueryCheckCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
		returnMap = (Map<String, Object>) body.getResult();
		if (StringUtil.isNullOrEmpty(returnMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			BaseHttpEngine.dissMissProgressDialog();
		}
		Intent it = new Intent(CrcdPsnQueryCheckList.this, CrcdPsnQueryCheckDetail.class);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(Crcd.CRCD_CURRENCYCODE, currencyCode);
		// startActivity(it);
		startActivityForResult(it, ConstantGloble.ACTIVITY_RESULT_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			switch (requestCode) {
			case ConstantGloble.ACTIVITY_RESULT_CODE:// 对账单服务
				adapter = new CrcdSetupAdapter(this, bankSetupList, -1);
				myListView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				accNum = 0;
				adapter.setSelectedPosition(-1);
				break;

			}
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
