package com.chinamworld.bocmbci.biz.crcd.mycrcd;

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
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.BusinessModelControl;
import com.chinamworld.bocmbci.biz.MainActivity;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.biz.crcd.adapter.CrcdSetupAdapter;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran.TransferManagerActivity1;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 我的信用卡  信用卡还款
 * 
 * @author sunh
 * 
 */

public class MyCardPaymentActivity extends CrcdBaseActivity{
	
	private View view;
	private List<Map<String, Object>> returnList = null;
	private List<Map<String, Object>> allAccountList = null;
	/** 信用卡列表 */
	private ListView myListView;
	CrcdSetupAdapter adapter;
	/** 信用卡列表View */
	private View has_acc_view = null;
	/** 关联账户View */
	private View no_acc_view = null;
	/** 关联新账户按钮 */
	private Button btn_description = null;
	Button sureButton;
	TextView tv_title;
	public static int selectPostion = -1;
	/** 用户选择的信用卡信息数据 */
	public static Map<String, Object> crcdAccount;
	
	private String accountId=null;
	private String accountType=null;
	
	private String  accountIbkNum=null;
	private String  nickName=null;
	// 购汇还款账户币种
	private String currType = null;
	private String haveNotRepayAmout=null;	//本期未还款金额
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLeftSelectedPosition("myCrcd_2");
		// 为界面标题赋值
		setTitle(this.getString(R.string.card_payment_tittle));
		view = addView(R.layout.crcd_payment_list);
		btn_right.setVisibility(View.GONE);
		back = (Button) findViewById(R.id.ib_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				ActivityTaskManager.getInstance().closeAllActivityExceptOne("MainActivity");
//				Intent intent = new Intent(MyCardPaymentActivity.this, MainActivity.class);
//				startActivity(intent);
//				goToMainActivity();
				ActivityTaskManager.getInstance().removeAllActivity();
				finish();
			}
		});
		bankAccountList = new ArrayList<Map<String, Object>>();
		allAccountList = new ArrayList<Map<String, Object>>();
		// 获取信用卡列表
		BaseDroidApp.getInstanse().setCurrentAct(this);
		BaseHttpEngine.showProgressDialogCanGoBack();
		requestCrcdList();

	}
	
	public void requestCrcdList() {
		// 通讯开始,展示通讯框
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
		returnList = (List<Map<String, Object>>) body.getResult();
		// 查询所有的账户
		requestAllAccountList();
	}

	/** 查询所有的账户 */
	public void requestAllAccountList() {
		// 通讯开始,展示通讯框
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.QRY_CRCD_LIST_API);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "requestAllAccountListCallBack");
	}
	/**
	 * 查询所有的账户 回调
	 */
	public void requestAllAccountListCallBack(Object resultObj) {
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		if (allAccountList != null && !allAccountList.isEmpty()) {
			allAccountList.clear();
		}
		List<Map<String, Object>> returnList = (List<Map<String, Object>>) body.getResult();
		if (returnList == null || returnList.size() <= 0) {

		} else {
			for (int i = 0; i < returnList.size(); i++) {
				Map<String, Object> map = returnList.get(i);
				String accountTpe = (String) map.get(Crcd.CRCD_ACCOUNTTYPE_RES);
				if (!StringUtil.isNull(accountTpe) && !ConstantGloble.JIEJIXN.equals(accountTpe)) {
					allAccountList.add(map);
				}
			}
		}	
		init();
		getViewDate();
		BaseHttpEngine.dissMissProgressDialog();
		BaseHttpEngine.canGoBack = false;
		
		

	}
	
	/** 初始化界面 */
	private void init() {
		has_acc_view = view.findViewById(R.id.has_acc);
		no_acc_view = view.findViewById(R.id.no_acc);
		btn_description = (Button) view.findViewById(R.id.btn_description);
		sureButton= (Button) findViewById(R.id.sureButton);
		btn_description.setVisibility(View.GONE);//屏蔽自助关联
		btn_description.setOnClickListener(goRelevanceClickListener);
		myListView = (ListView) view.findViewById(R.id.crcd_mycrcdlist);
		tv_title = (TextView) findViewById(R.id.tv_service_title);
		tv_title.setText(getString(R.string.mycrcd_creditcard_choise_card));
		sureButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(adapter.getSelectedPosition()==-1){
					String errorInfo = getResources().getString(R.string.crcd_notselectacard_error);
					BaseDroidApp.getInstanse().showInfoMessageDialog(errorInfo);
					return;	
				}else{
					// sunh 信用卡还款 购汇 统一
					crcdAccount = bankAccountList.get(adapter.getSelectedPosition());
					 accountId = (String) crcdAccount.get(Crcd.CRCD_ACCOUNTID_RES);
					 accountType= (String) crcdAccount.get(Crcd.CRCD_ACCOUNTTYPE_RES);
					 accountIbkNum=(String) crcdAccount.get(Tran.ACCOUNTIBKNUM_RES);		
						nickName= (String) crcdAccount.get(Crcd.CRCD_NICKNAME_RES);
					haveNotRepayAmout=null;
					TranDataCenter.getInstance().setRelCrcdBuyCallBackMap(null);
					requestPsnCrcdQueryGeneralInfo();
				}
				
				
			
				
			}
		});
	}

	/** 根据返结果加载布局 */
	private void getViewDate() {
		if (returnList == null || returnList.size() <= 0) {
			// 没有信用卡---关联信用卡
			no_acc_view.setVisibility(View.VISIBLE);
		
			btn_right.setVisibility(View.GONE);
//			setLeftButtonPopupGone();
//			setLeftGoneAfterVisiable();
			has_acc_view.setVisibility(View.GONE);
			return;
		} else {
			for (int i = 0; i < returnList.size(); i++) {
				if (ZHONGYIN.equals(returnList.get(i).get(Crcd.CRCD_ACCOUNTTYPE_RES))
						|| GREATWALL.equals(returnList.get(i).get(Crcd.CRCD_ACCOUNTTYPE_RES))
						|| SINGLEWAIBI.equals(returnList.get(i).get(Crcd.CRCD_ACCOUNTTYPE_RES))) {
					bankAccountList.add(returnList.get(i));
				}
			}
		}

		if (StringUtil.isNullOrEmpty(bankAccountList)) {
			// 没有信用卡---关联信用卡
			no_acc_view.setVisibility(View.VISIBLE);
			
			btn_right.setVisibility(View.GONE);
//			setLeftButtonPopupGone();
//			setLeftGoneAfterVisiable();
			has_acc_view.setVisibility(View.GONE);
			return;
		} else {
			btn_right.setVisibility(View.GONE);
			no_acc_view.setVisibility(View.GONE);
			has_acc_view.setVisibility(View.VISIBLE);
			setListViewDate();
		}
	}
	
	/** 为listView赋值 */
	private void setListViewDate() {
	

		adapter = new CrcdSetupAdapter(this, bankAccountList, selectPostion);
		adapter.notifyDataSetChanged();
		myListView.setAdapter(adapter);
		myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


				adapter.setSelectedPosition(position);

			}
		});
	}
	

	/** 进行自助关联监听事件 */
	OnClickListener goRelevanceClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

//			startActivityForResult(
//					(new Intent(MyCardPaymentActivity.this, AccInputRelevanceAccountActivity.class)),
//					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
//			
			
			BusinessModelControl.gotoAccRelevanceAccount(MyCardPaymentActivity.this,  ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, null);
		
		}

	};
	
	
	

	/** 信用卡综合信息查询 */
	private void requestPsnCrcdQueryGeneralInfo() {
		
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDQUERYGENERALINFO);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnCrcdQueryGeneralInfoCallBack");

	}
	@SuppressWarnings("unchecked")
	public void requestPsnCrcdQueryGeneralInfoCallBack(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(result)) {
			BiiHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_transferquery_null));
			return;
		}

		// 信用卡详情，跳转到转账页面
		result.put(Crcd.CRCD_ACCOUNTTYPE_RES, accountType);
		result.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		result.put(Tran.ACCOUNTIBKNUM_RES, accountIbkNum);
		result.put(Crcd.CRCD_NICKNAME_RES, nickName);
		
		TranDataCenter.getInstance().setAccInInfoMap(result);
		List<Map<String, Object>> actlist = (List<Map<String, Object>>) result.get(Crcd.CRCD_ACTLIST);
		for (int i = 0; i < actlist.size(); i++) {
		
			if(!"001".equals(String.valueOf(actlist.get(i).get(Crcd.CRCD_CURRENCY_RES)))){	
				//得到外币 账单信息
				haveNotRepayAmout=(String)actlist.get(i).get(Crcd.CRCD_HAVENOTREPAYAMOUT);	//本期未还款金额
				currType=(String)actlist.get(i).get(Crcd.CRCD_CURRENCY);	//本期未还款金额	
			}					
		}
		if(!StringUtil.isNull(haveNotRepayAmout)&&
			Double.parseDouble(haveNotRepayAmout)>0){
			
				//外币有欠款 请求购汇信息
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
			}else{
				// 无欠款 直接到转账界面
				BiiHttpEngine.dissMissProgressDialog();
				Intent intent = new Intent(MyCardPaymentActivity.this, TransferManagerActivity1.class);
				TranDataCenter.getInstance().setModuleType(ConstantGloble.CRCE_TYPE1);
				intent.putExtra(ConstantGloble.JUMP_TO_TRAN_FLAG, ConstantGloble.CREDIT_TO_TRAN);	
				startActivity(intent);
			}
	

	}
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);		
			// 转账还款统一
			// 查询信用卡购汇还款信息
			requesPsnCrcdQueryForeignPayOff();
	}

	
	/**
	 * 信用卡查询购汇还款信息 sunh
	 */
	public void requesPsnCrcdQueryForeignPayOff() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNQUERYDOREIGNPAYOFF);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Crcd.CRCD_ACCOUNTID_RES, accountId);// 转入账户标识 accountId
		map.put(Crcd.CRCD_CURRTYPE_REQ, currType);// 购汇还款账户币种 currType
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requesPsnCrcdQueryForeignPayOffCallBack");
	}

	
	/**
	 * 信用卡查询购汇还款信息返回 sunh
	 * 
	 * @param resultObj
	 */
	public void requesPsnCrcdQueryForeignPayOffCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		TranDataCenter.getInstance().setRelCrcdBuyCallBackMap(result);
		TranDataCenter.getInstance().setModuleType(ConstantGloble.CRCE_TYPE1);
		Intent intent = new Intent(MyCardPaymentActivity.this, TransferManagerActivity1.class);
		intent.putExtra(ConstantGloble.JUMP_TO_TRAN_FLAG, ConstantGloble.CREDIT_TO_TRAN);	
		startActivity(intent);
	}
	
}
