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
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;

/**
 * 信用卡挂失列表
 * 
 * @author huangyuchao
 * 
 */
public class CrcdGuashiListActivity extends CrcdBaseActivity {

	/** 信用卡列表页 */
	private View view = null;

	Button sureButton;

	/** 信用卡列表 */
	private ListView myListView;

	CrcdSetupAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_creditcard_guashi_title));
		if (view == null) {
			view = addView(R.layout.crcd_mycard_setup_list);
		}
		back = (Button) findViewById(R.id.ib_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CrcdGuashiListActivity.this, MyCardSetupMenuActivity.class);
				startActivity(intent);
				finish();
			}
		});
		// 获取信用卡列表
		requestCrcdList();
	}

	static String accountId;
	static String accountNumber;

	static String nickName;

	static String accountType;
	static String strAccountType;

	private int accNum = 0;
	TextView tv_service_title;

	/** 初始化界面 */
	private void init() {

		tv_service_title = (TextView) findViewById(R.id.tv_service_title);
		tv_service_title.setText(getString(R.string.mycrcd_creditcard_choise_card));

		sureButton = (Button) findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (accNum == 0) {
					String errorInfo = getResources().getString(R.string.crcd_notselectacard_error);
					BaseDroidApp.getInstanse().showInfoMessageDialog(errorInfo);
					return;
				} else {
					Intent it = new Intent(CrcdGuashiListActivity.this, CrcdGuashiInfoActivity.class);
					// startActivity(it);
					it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
					it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
					it.putExtra(Crcd.CRCD_NICKNAME_RES, nickName);
					it.putExtra(Crcd.CRCD_ACCOUNTTYPE_RES, strAccountType);
					startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
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
				accountId = String.valueOf(bankSetupList.get(position).get(Crcd.CRCD_ACCOUNTID_RES));
				accountNumber = String.valueOf(bankSetupList.get(position).get(Crcd.CRCD_ACCOUNTNUMBER_RES));
				nickName = String.valueOf(bankSetupList.get(position).get(Crcd.CRCD_NICKNAME_RES));
				accountType = String.valueOf(bankSetupList.get(position).get(Crcd.CRCD_ACCOUNTTYPE_RES));

				strAccountType = LocalData.AccountType.get(accountType);
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

	@Override
	protected void onNewIntent(Intent intent) {

		adapter.setSelectedPosition(-1);

		super.onNewIntent(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			switch (requestCode) {
			case ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE:
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
}
