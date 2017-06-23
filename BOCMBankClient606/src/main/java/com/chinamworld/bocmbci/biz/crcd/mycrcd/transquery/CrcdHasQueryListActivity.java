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
import com.chinamworld.bocmbci.biz.crcd.mycrcd.MyCardInstalmentActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 账单分期查询----选卡页面 长城信用卡、中信信用卡
 * 
 * @author huangyuchao
 * 
 */
public class CrcdHasQueryListActivity extends CrcdBaseActivity {

	private View view = null;

	Button sureButton;

	/** 信用卡列表 */
	private ListView myListView;

	CrcdSetupAdapter adapter;
	/** 账单分期----网银账户标志 */
	public static String accountId;
	/** 账单分期----账号 */
	public static String accountNumber;
	/** 账单分期---币种 */
	static String currencyCode;
	private int accNum = 0;

	TextView tv_title;

	RelativeLayout cardLayout;
	LinearLayout nocardLayout;

	Button btn_description;
	/** 账单分期信用卡list */
	public List<Map<String, Object>> bankHasqueryList = new ArrayList<Map<String, Object>>();
	/** 1-init, */
	private int ttag = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_account_divide));
		if (view == null) {
			view = addView(R.layout.crcd_mycard_setup_list);
		}
		back = (Button) findViewById(R.id.ib_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CrcdHasQueryListActivity.this, MyCardInstalmentActivity.class);
				startActivity(intent);
				finish();
			}
		});
		if (bankHasqueryList != null && bankHasqueryList.size() > 0) {
			bankHasqueryList.clear();
		}
		bankHasqueryList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CRCD_SEARCH_ACCLIST);
		if (bankHasqueryList != null && bankHasqueryList.size() > 0) {
			init();
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}		
	}

	/** 初始化界面 */
	private void init() {
		cardLayout = (RelativeLayout) findViewById(R.id.cardLayout);
		nocardLayout = (LinearLayout) findViewById(R.id.nocardLayout);
		nocardLayout.setVisibility(View.GONE);
		cardLayout.setVisibility(View.VISIBLE);
		btn_description = (Button) findViewById(R.id.btn_description);
		btn_description.setVisibility(View.GONE);//屏蔽自助关联
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
					Intent it = new Intent(CrcdHasQueryListActivity.this, CrcdHasQueryDetailActivity.class);
					BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.IS_EBANK_0, 3);
					it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
					it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
					it.putExtra(Crcd.CRCD_CURRENCYCODE, currencyCode);
					it.putExtra(Crcd.CRCD_NUM, ALL_RENANDFOREIGN);
					it.putExtra(ConstantGloble.ACC_POSITION, accNum - 1);
					BaseDroidApp.getInstanse().getBizDataMap()
							.put(ConstantGloble.CRCD_BANKHASQUERYLIST, bankHasqueryList);
					startActivity(it);
				}
			}
		});

		myListView = (ListView) view.findViewById(R.id.crcd_mycrcdlist);
		adapter = new CrcdSetupAdapter(this, bankHasqueryList, -1);
		myListView.setAdapter(adapter);
		myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				accNum = position + 1;
				accountId = (String) bankHasqueryList.get(position).get(Crcd.CRCD_ACCOUNTID_RES);
				accountNumber = (String) bankHasqueryList.get(position).get(Crcd.CRCD_ACCOUNTNUMBER_RES);
				currencyCode = (String) bankHasqueryList.get(position).get(Crcd.CRCD_CURRENCYCODE);

				adapter.setSelectedPosition(position);
			}
		});
	}

	/** 进行自助关联监听事件---此事件不在使用 */
  private OnClickListener goRelevanceClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
//			startActivityForResult((new Intent(CrcdHasQueryListActivity.this, AccInputRelevanceAccountActivity.class)),
//					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
			
			BusinessModelControl.gotoAccRelevanceAccount(CrcdHasQueryListActivity.this, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, null);
			// finish();
		}
	};

	private void requestCrcdList() {
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
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		List<Map<String, Object>> returnList = (List<Map<String, Object>>) body.getResult();
		if (returnList == null || returnList.size() <= 0) {
			return;
		}
		if (bankHasqueryList != null && bankHasqueryList.size() > 0) {
			bankHasqueryList.clear();
		}
		for (int i = 0; i < returnList.size(); i++) {
			if (ZHONGYIN.equals(returnList.get(i).get(Crcd.CRCD_ACCOUNTTYPE_RES))
					|| GREATWALL.equals(returnList.get(i).get(Crcd.CRCD_ACCOUNTTYPE_RES))) {
				bankHasqueryList.add(returnList.get(i));
			}

		}
		if (ttag == 1) {
			init();
		}
		if (StringUtil.isNullOrEmpty(bankHasqueryList)) {
			nocardLayout.setVisibility(View.VISIBLE);
			cardLayout.setVisibility(View.GONE);
		} else {
			nocardLayout.setVisibility(View.GONE);
			cardLayout.setVisibility(View.VISIBLE);
			adapter = new CrcdSetupAdapter(this, bankHasqueryList, -1);
			myListView.setAdapter(adapter);
		}
	}

//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
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
			adapter = new CrcdSetupAdapter(this, bankHasqueryList, -1);
			myListView.setAdapter(adapter);
		}
		accNum = 0;
		super.onNewIntent(intent);
	}

}
