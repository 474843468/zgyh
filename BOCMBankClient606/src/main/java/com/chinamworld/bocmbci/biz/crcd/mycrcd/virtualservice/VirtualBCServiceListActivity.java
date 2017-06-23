package com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice;

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
import com.chinamworld.bocmbci.biz.crcd.CrcdAccBaseActivity;
import com.chinamworld.bocmbci.biz.crcd.adapter.CrcdSetupAdapter;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.transquery.CrcdXiaofeiQueryListActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 虚拟银行卡申请列表
 * 
 * @author huangyuchao
 * 
 */
public class VirtualBCServiceListActivity extends CrcdAccBaseActivity {

	private View view = null;

	Button sureButton;

	/** 信用卡列表 */
	private ListView myListView;

	CrcdSetupAdapter adapter;
	static String accountId;
	static String accountNumber;
	static String currencyCode;
	static String nickName;
	static String accountName;

	static Map bankSetupMap;

	static String accountType;
	static String strAccountType;

	private int accNum = 0;
	RelativeLayout cardLayout;
	LinearLayout nocardLayout;

	Button btn_description;

	TextView tv_service_title;
	private boolean isShowView = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_virtual_shenqing));
		if (view == null) {
			view = addView(R.layout.crcd_mycard_service_list);
		}
		back = (Button) findViewById(R.id.ib_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
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
		BaseHttpEngine.dissMissProgressDialog();
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
			setTitle(this.getString(R.string.mycrcd_virtual_shenqing));
			nocardLayout.setVisibility(View.GONE);
			cardLayout.setVisibility(View.VISIBLE);
			adapter = new CrcdSetupAdapter(this, bankSetupList, -1);
			myListView.setAdapter(adapter);
			initOnClick();
		}

	}

	public static Map getBankSetupMap() {
		return bankSetupMap;
	}

	/** 初始化界面 */
	private void init() {
		tv_service_title = (TextView) findViewById(R.id.tv_service_title);
		tv_service_title.setText(this.getString(R.string.mycrcd_select_zhucard));
		cardLayout = (RelativeLayout) findViewById(R.id.cardLayout);
		nocardLayout = (LinearLayout) findViewById(R.id.nocardLayout);
		btn_description = (Button) findViewById(R.id.btn_description);
		btn_description.setVisibility(View.GONE);//屏蔽自助关联
		btn_description.setOnClickListener(goRelevanceClickListener);
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getResources().getString(R.string.mycrcd_virtual_select_card),
						this.getResources().getString(R.string.mycrcd_virtual_read_xieyi),
						this.getResources().getString(R.string.mycrcd_virtual_write_info) });
		StepTitleUtils.getInstance().setTitleStep(1);

		myListView = (ListView) view.findViewById(R.id.crcd_mycrcdlist);
		accNum = 0;
		sureButton = (Button) findViewById(R.id.sureButton);

	}

	private void initOnClick() {
		myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				accNum = position + 1;

				bankSetupMap = bankSetupList.get(position);
				accountId = String.valueOf(bankSetupMap.get(Crcd.CRCD_ACCOUNTID_RES));
				accountNumber = String.valueOf(bankSetupMap.get(Crcd.CRCD_ACCOUNTNUMBER_RES));
				currencyCode = String.valueOf(bankSetupMap.get(Crcd.CRCD_CURRENCYCODE));
				nickName = String.valueOf(bankSetupMap.get(Crcd.CRCD_NICKNAME_RES));

				accountName = String.valueOf(bankSetupMap.get(Crcd.CRCD_ACCOUNTNAME_RES));
				accountType = String.valueOf(bankSetupMap.get(Crcd.CRCD_ACCOUNTTYPE_RES));
				strAccountType = LocalData.AccountType.get(accountType);

				adapter.setSelectedPosition(position);

			}
		});
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (accNum == 0) {
					String errorInfo = getResources().getString(R.string.crcd_notselectacard_error);
					BaseDroidApp.getInstanse().showInfoMessageDialog(errorInfo);
					return;
				} else {
					Intent it = new Intent(VirtualBCServiceListActivity.this, VirtualBCServiceReadActivity.class);
					it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
					it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
					it.putExtra(Crcd.CRCD_CURRENCYCODE, currencyCode);
					startActivity(it);
					overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
				}
			}
		});
	}

	/** 进行自助关联监听事件 */
	OnClickListener goRelevanceClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

//			startActivityForResult((new Intent(VirtualBCServiceListActivity.this,
//					AccInputRelevanceAccountActivity.class)), ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
			
			BusinessModelControl.gotoAccRelevanceAccount(VirtualBCServiceListActivity.this,  ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, null);
		}
	};

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

}
