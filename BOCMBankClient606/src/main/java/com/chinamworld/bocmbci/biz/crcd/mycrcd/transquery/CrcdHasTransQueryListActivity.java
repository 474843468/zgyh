package com.chinamworld.bocmbci.biz.crcd.mycrcd.transquery;

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
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.biz.crcd.adapter.CrcdSetupAdapter;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.MyCrcdDetailActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 已出账单查询列表
 * 
 * @author huangyuchao
 * 
 */
public class CrcdHasTransQueryListActivity extends CrcdBaseActivity {
	private static String TAG = "CrcdHasTransQueryListActivity";

	private View view = null;
	/** 下一步 */
	private Button sureButton;

	/** 信用卡列表 */
	private ListView myListView;

	CrcdSetupAdapter adapter;
	/** 已出账户查询---网银账户标志 */
	static String accountId;
	/** 已出账户查询---账号 */
	static String accountNumber;
	/** 已出账户查询---币种 */
	static String currencyCode;
	/** 已出账户查询---账户类型 */
	public static String accountType;
	private int accNum = 0;
	/** 顶部title */
	private TextView tv_title;

	public static String fromHasQuery;

	private RelativeLayout cardLayout;
	private LinearLayout nocardLayout;

	private Button btn_description;
	/** 1-初始化 ,2-onActivityResult */
	private int ttag = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_setup_yichu_query));
		if (view == null) {
			view = addView(R.layout.crcd_mycard_setup_list);
		}
		back = (Button) findViewById(R.id.ib_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CrcdHasTransQueryListActivity.this, MyCardTransMenuActivity.class);
				startActivity(intent);
				finish();
			}
		});
		// 获取信用卡列表
		requestCrcdList();
	}

	/** 初始化界面 */
	private void init() {

		cardLayout = (RelativeLayout) findViewById(R.id.cardLayout);
		nocardLayout = (LinearLayout) findViewById(R.id.nocardLayout);

		btn_description = (Button) findViewById(R.id.btn_description);
		btn_description.setVisibility(View.GONE); //屏蔽自助关联
		btn_description.setOnClickListener(goRelevanceClickListener);

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
					Intent it = new Intent(CrcdHasTransQueryListActivity.this, MyCrcdDetailActivity.class);
					it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
					it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
					it.putExtra(Crcd.CRCD_CURRENCYCODE, currencyCode);
					it.putExtra(Crcd.CRCD_NUM, ALL_FOREIGN);
					it.putExtra("fromQuery", "fromHasQuery");
					// 已出账单所有列表的详情
					BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.CRCD_QUERY_LIST, bankSetupList);
					BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.IS_EBANK_0, 1);
					it.putExtra(ConstantGloble.ACC_POSITION, accNum - 1);
					LogGloble.d(TAG + " ------", "accNum----" + accNum);
					startActivity(it);
				}
			}
		});

		myListView = (ListView) view.findViewById(R.id.crcd_mycrcdlist);

		myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				accNum = position + 1;
				accountId = (String) bankSetupList.get(position).get(Crcd.CRCD_ACCOUNTID_RES);
				accountNumber = (String) bankSetupList.get(position).get(Crcd.CRCD_ACCOUNTNUMBER_RES);
				currencyCode = (String) bankSetupList.get(position).get(Crcd.CRCD_CURRENCYCODE);
				accountType = (String) bankSetupList.get(position).get(Crcd.CRCD_ACCOUNTTYPE_RES);

				adapter = new CrcdSetupAdapter(CrcdHasTransQueryListActivity.this, bankSetupList, position);
				myListView.setAdapter(adapter);
				// myListView.setSelection(position);
			}
		});
	}

	/** 进行自助关联监听事件 */
	OnClickListener goRelevanceClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
//			startActivityForResult((new Intent(CrcdHasTransQueryListActivity.this,
//					AccInputRelevanceAccountActivity.class)), ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
			// finish();
			
			BusinessModelControl.gotoAccRelevanceAccount(CrcdHasTransQueryListActivity.this, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, null);
		}
	};

	/** 查询信用卡 */
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
		if (ttag == 1) {
			init();
		}
		if (StringUtil.isNullOrEmpty(bankSetupList)) {
			nocardLayout.setVisibility(View.VISIBLE);
			cardLayout.setVisibility(View.GONE);
		} else {
			nocardLayout.setVisibility(View.GONE);
			cardLayout.setVisibility(View.VISIBLE);
			adapter = new CrcdSetupAdapter(this, bankSetupList, -1);
			myListView.setAdapter(adapter);
		}
	}
//
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE && resultCode == RESULT_OK) {
			// 请求信用卡列表
			ttag = 2;
			requestCrcdList();
		}
		super.onActivityResult(requestCode, resultCode, data);
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
}
