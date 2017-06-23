package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

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

/**
 * 信用卡还款方式设定列表
 * 
 * @author huangyuchao
 * 
 */
public class MyCrcdSetupListActivity extends CrcdBaseActivity {
	private static final String TAG = "MyCrcdSetupListActivity";
	/** 信用卡还款方式设定列表页 */
	private View view = null;

	Button sureButton;

	/** 信用卡列表 */
	private ListView myListView;

	CrcdSetupAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_creditcard_money_setup_title));
		if (view == null) {
			view = addView(R.layout.crcd_mycard_setup_list);
		}
		setLeftSelectedPosition("myCrcd_2");
		back = (Button) findViewById(R.id.ib_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyCrcdSetupListActivity.this, MyCardSetupMenuActivity.class);
				startActivity(intent);
				finish();
			}
		});
		// 获取信用卡列表
		requestCrcdList();

	}

	static String accountId;
	static String accountNumber;

	private int accNum = 0;

	TextView tv_title;

	/** 初始化界面 */
	private void init() {

		tv_title = (TextView) findViewById(R.id.tv_service_title);
		tv_title.setText(getString(R.string.mycrcd_creditcard_choise_card));

		sureButton = (Button) findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (accNum == 0) {
					String errorInfo = getResources().getString(R.string.crcd_notselectacard_error);
					BaseDroidApp.getInstanse().showInfoMessageDialog(errorInfo);
					return;
				} else {
					// 再次查询一次详情
					psnCrcdQueryCrcdPaymentWay();
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

				adapter.setSelectedPosition(position);
				// adapter.notifyDataSetChanged();
				// myListView.setSelection(position);
			}
		});

	}

	/**
	 * 
	 */
	public void psnCrcdQueryCrcdPaymentWay() {
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDQUERYCRCDPAYMENTWAY_API);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "getqueryPaymentCallBack");
	}

	protected static Map<String, Object> returnList = new HashMap<String, Object>();

	public void getqueryPaymentCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		if (null != list && list.size() > 0) {
			BiiResponseBody body = list.get(0);
			returnList = (Map<String, Object>) body.getResult();
		}

		Intent it = new Intent(MyCrcdSetupListActivity.this, MyCrcdSetupDetailActivity.class);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		// startActivity(it);
		startActivityForResult(it, ConstantGloble.ACTIVITY_RESULT_CODE);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			switch (requestCode) {
			case ConstantGloble.ACTIVITY_RESULT_CODE:
				adapter = new CrcdSetupAdapter(this, bankSetupList, -1);
				myListView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
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

	/**
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
		// psnCrcdQueryCrcdPaymentWay();
	}

	/**
	 * 还款方式查询
	 */
	public void psnCrcdQueryCrcdPaymentWay(String test) {
		List<BiiRequestBody> biiRequestBodyList = new ArrayList<BiiRequestBody>();
		LogGloble.v(Crcd.TAG, bankSetupList.size() + "<><><><><><>");
		for (int i = 0; i < bankSetupList.size(); i++) {
			BiiRequestBody biiRequestBody = new BiiRequestBody();
			biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDQUERYCRCDPAYMENTWAY_API);
			accountId = (String) bankSetupList.get(i).get(Crcd.CRCD_ACCOUNTID_RES);
			LogGloble.v(Crcd.TAG, accountId + "");
			Map<String, String> paramsmap = new HashMap<String, String>();
			paramsmap.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
			biiRequestBody.setParams(paramsmap);
			biiRequestBodyList.add(biiRequestBody);
		}
		HttpManager.requestBii(biiRequestBodyList, this, "psnCrcdQueryCrcdPaymentWayCallBack");
	}

	/**
	 * 还款方式查询回调
	 */
	public void psnCrcdQueryCrcdPaymentWayCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		for (int j = 0; j < biiResponseBodys.size(); j++) {
			BiiResponseBody biiResponseBody = biiResponseBodys.get(j);
			Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
			// 把账户列表与详情列表拼接
			Map<String, Object> m = (Map<String, Object>) bankAccountList.get(j);
			m.put(Crcd.CRCD_DETAILIST, result);
			bankAccountList.add(m);
		}
		adapter.notifyDataSetChanged();

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
