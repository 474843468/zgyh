package com.chinamworld.bocmbci.biz.crcd.mycrcd.transquery;

import java.util.ArrayList;
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
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 附属卡列表----选择附属卡的主信用卡
 * 
 * @author huangyuchao
 * 
 */
public class CrcdfuShuQueryListActivity extends CrcdBaseActivity {
    private static final String TAG="CrcdfuShuQueryListActivity";
	private View view = null;

	Button sureButton;

	/** 信用卡列表 */
	private ListView myListView;

	CrcdSetupAdapter adapter;

	static String accountId;
	static String accountNumber;
	static String currencyCode;
	private int accNum = 0;
	/** 附属卡账户list */
	public List<Map<String, Object>> bankfushuList = new ArrayList<Map<String, Object>>();
	TextView tv_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_trans_detail));
		if (view == null) {
			view = addView(R.layout.crcd_mycard_setup_list);
		}
		back = (Button) findViewById(R.id.ib_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CrcdfuShuQueryListActivity.this, MyCardTransMenuActivity.class);
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
		tv_title.setText(getString(R.string.mycrcd_select_fushu_service));

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
					// Intent it = new Intent(CrcdfuShuQueryListActivity.this,
					// CrcdFushuQueryDetailActivity.class);
					// it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
					// it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
					// it.putExtra(Crcd.CRCD_CURRENCYCODE, currencyCode);
					// it.putExtra(ConstantGloble.ACC_POSITION, accNum - 1);
					//
					// startActivity(it);
				}
			}
		});

		myListView = (ListView) view.findViewById(R.id.crcd_mycrcdlist);
		adapter = new CrcdSetupAdapter(this, bankfushuList, -1);

		myListView.setAdapter(adapter);
		myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				accNum = position + 1;
				accountId = (String) bankfushuList.get(position).get(Crcd.CRCD_ACCOUNTID_RES);
				accountNumber = (String) bankfushuList.get(position).get(Crcd.CRCD_ACCOUNTNUMBER_RES);
				currencyCode = (String) bankfushuList.get(position).get(Crcd.CRCD_CURRENCYCODE);

				adapter.setSelectedPosition(position);
			}
		});
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 附属卡查询
		psnCrcdAppertainTranSetQuery();
	}

	/** 查询信用卡对应的附属卡 */
	public void psnCrcdAppertainTranSetQuery() {
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDAPPERTAINNTRANQUERY);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdAppertainTranSetQueryCallBack");
	}

	/** 查询信用卡对应的附属卡----结果 */
	public static List<Map<String, Object>> returnList;

	/** 查询信用卡对应的附属卡----回调 */
	public void psnCrcdAppertainTranSetQueryCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
		returnList = (List<Map<String, Object>>) body.getResult();
		if (StringUtil.isNullOrEmpty(returnList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_ACTAVI_RESULT_KEY, returnList);
		LogGloble.d(TAG, "returnList========");
		Intent it = new Intent(CrcdfuShuQueryListActivity.this, CrcdFushuQueryDetailActivity.class);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(Crcd.CRCD_CURRENCYCODE, currencyCode);
		it.putExtra(ConstantGloble.ACC_POSITION, accNum - 1);
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.CRCD_BANKFUSHULIST, bankfushuList);
		BaseHttpEngine.dissMissProgressDialog();
		startActivity(it);
	}

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
		if (bankfushuList != null && bankfushuList.size() > 0) {
			bankfushuList.clear();
		}
		for (int i = 0; i < returnList.size(); i++) {
			if (ZHONGYIN.equals(returnList.get(i).get(Crcd.CRCD_ACCOUNTTYPE_RES))
					|| GREATWALL.equals(returnList.get(i).get(Crcd.CRCD_ACCOUNTTYPE_RES))
					|| SINGLEWAIBI.equals(returnList.get(i).get(Crcd.CRCD_ACCOUNTTYPE_RES))) {
				bankfushuList.add(returnList.get(i));
			}
		}
		if (StringUtil.isNullOrEmpty(bankfushuList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		BaseHttpEngine.dissMissProgressDialog();
		init();
	}

//	@Override
//	protected void onResume() {
//		super.onResume();
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
