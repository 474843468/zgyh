package com.chinamworld.bocmbci.biz.crcd.mycrcd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.BusinessModelControl;
import com.chinamworld.bocmbci.biz.MainActivity;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
//import com.chinamworld.bocmbci.biz.crcd.adapter.CrcdAccountAdapter;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup.CrcdPsnQueryCheckList;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup.CrcdServiceSetupListActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup.MyCrcdSetupListActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup.MySupplymentListActivity;
import com.chinamworld.bocmbci.biz.crcd.view.InflaterViewDialog;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran.TransferManagerActivity1;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.BaseSwipeListViewListener;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.bocmbci.widget.SwipeListView;
import com.chinamworld.bocmbci.widget.adapter.CommonAdapter;
import com.chinamworld.bocmbci.widget.adapter.ICommonAdapter;

/**
 * 我的信用卡列表
 * 
 * @author huangyuchao
 * 
 */
public class MyCreditCardActivity extends CrcdBaseActivity implements ICommonAdapter<Map<String, Object>> {

	private static final String TAG = "MyCreditCardActivity";

	/** 我的信用卡列表页 */
	private View view;
	/** 信用卡列表 */
	private SwipeListView myListView;

//	CrcdAccountAdapter adapter;
	
	CommonAdapter adapter;

	/** 取消关联选择的项 */
	private static int position = 0;

	/** 添加关联账户视图 */
	private View addTransferView;
	public static String cardType;
	/** 取消关联点击 */
	private boolean click = true;
	/** 信用卡列表View */
	private View has_acc_view = null;
	/** 关联账户View */
	private View no_acc_view = null;
	/** 关联新账户按钮 */
	private Button btn_description = null;
	/** 请求自主关联，1-没有信用卡时请求，2-点击+时请求 */
	private int requestTag = 1;
	/** 信用卡列表底部----自主关联 */
	private LinearLayout ll_footer = null;
	private List<Map<String, Object>> returnList = null;
	private List<Map<String, Object>> allAccountList = null;
	/** 初始化时查询所有的账户 1-初始化，2-关联账户，3-取消关联 */
	private int init = 1;

	
	String crcdPoint;
	String accountId;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLeftSelectedPosition("myCrcd_1");
		setTitle(this.getString(R.string.card_credit_tittle));
		// 包含向右滑动的listview 目的是屏蔽左侧菜单的滑动事件
		setContainsSwipeListView(true);
		view = addView(R.layout.crcd_mycrcd_list);
		back = (Button) findViewById(R.id.ib_back);
		btn_right.setVisibility(View.GONE);
		returnList = new ArrayList<Map<String, Object>>();
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		bankAccountList = new ArrayList<Map<String, Object>>();
		allAccountList = new ArrayList<Map<String, Object>>();
		setLeftSelectedPosition("myCrcd_1");
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
		if (init == 1) {
			// 第一次进入页面
			// 初始化界面
			init();
			getViewDate();
			BaseHttpEngine.dissMissProgressDialog();
			BaseHttpEngine.canGoBack = false;
			initOnClick();
		} else if (init == 2) {
			// 关联账户后
			BaseHttpEngine.dissMissProgressDialog();
			searchAcountAfterCalcel();
		} else if (init == 3) {
			// 取消关联后
			BaseHttpEngine.dissMissProgressDialog();
			init();
			getViewDate();
			initOnClick();
		}

	}

	/** 初始化界面 */
	private void init() {
		has_acc_view = view.findViewById(R.id.has_acc);
		no_acc_view = view.findViewById(R.id.no_acc);
		btn_description = (Button) view.findViewById(R.id.btn_description);
		btn_description.setVisibility(View.GONE); //屏蔽自助关联
		myListView = (SwipeListView) view.findViewById(R.id.crcd_mycrcdlist);
		addTransferView = LayoutInflater.from(this).inflate(R.layout.acc_mybankaccount_list_add_transfer, null);
		ll_footer = (LinearLayout) addTransferView.findViewById(R.id.ll_footer);
		// 详情按钮
		ImageView acc_btn_goitem = (ImageView) addTransferView.findViewById(R.id.acc_btn_goitem);
		// 设置列表底部视图
//		myListView.addFooterView(addTransferView); 屏蔽自助关联
		acc_btn_goitem.setVisibility(View.GONE);
		myListView.setLastPositionClickable(false);
		myListView.setAllPositionClickable(true);
		myListView.setSwipeListViewListener(swipeListListener);
	}

	/** 根据返结果加载布局 */
	private void getViewDate() {
		if (returnList == null || returnList.size() <= 0) {
			// 没有信用卡---关联信用卡
			no_acc_view.setVisibility(View.VISIBLE);
			//setTitle(this.getString(R.string.mycrcd1));
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
			//setTitle(this.getString(R.string.mycrcd1));
			btn_right.setVisibility(View.GONE);
//			setLeftButtonPopupGone();
//			setLeftGoneAfterVisiable();
			has_acc_view.setVisibility(View.GONE);
			return;
		} else {
			//setTitle(this.getString(R.string.mycrcd));
			no_acc_view.setVisibility(View.GONE);
			has_acc_view.setVisibility(View.VISIBLE);
			setListViewDate();
		}
	}

	/** 为listView赋值 */
	private void setListViewDate() {
		if (allAccountList.size() == 1) {
			// 只有一张卡并且这张卡是信用卡，不支持取消关联
			btn_right.setVisibility(View.GONE);
		} else {
			// 右上角按钮赋值--取消关联
			setText(this.getString(R.string.acc_main_right_btn));
		}
		conid = 1;
		
//		adapter = new CrcdAccountAdapter(this, bankAccountList, conid);
		adapter=new CommonAdapter<Map<String, Object>>(MyCreditCardActivity.this, bankAccountList, this);
		adapter.notifyDataSetChanged();
		myListView.setAdapter(adapter);
	}

	private void initOnClick() {
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityTaskManager.getInstance().removeAllActivity();
				finish();
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), MainActivity.class);
//				BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
//				goToMainActivity();
			}
		});
		// 没有信用卡---请求自主关联
		btn_description.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestTag = 1;
//				startActivityForResult((new Intent(MyCreditCardActivity.this, AccInputRelevanceAccountActivity.class)),
//						ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
				BusinessModelControl.gotoAccRelevanceAccount(MyCreditCardActivity.this,  ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, null);
			}
		});
		// 自主关联
		ll_footer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestTag = 2;
//				startActivityForResult((new Intent(MyCreditCardActivity.this, AccInputRelevanceAccountActivity.class)),
//						ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
				
				BusinessModelControl.gotoAccRelevanceAccount(MyCreditCardActivity.this, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, null);
			}
		});
	}

	/** 右侧按钮点击事件----取消关联 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (click) {
				if (StringUtil.isNullOrEmpty(bankAccountList)) {
					// 没有账户不可进行取消关联
					return;
				}
				setText(MyCreditCardActivity.this.getString(R.string.finish));
				conid = 2;
				ll_footer.setClickable(false);
				// 取消关联
//				CrcdAccountAdapter adapter = new CrcdAccountAdapter(MyCreditCardActivity.this, bankAccountList, conid);
				CommonAdapter adapter=new CommonAdapter<Map<String, Object>>(MyCreditCardActivity.this, bankAccountList, MyCreditCardActivity.this);
				myListView.setAdapter(adapter);
//				adapter.setOnbanklistCancelRelationClickListener(onbanklistCancelRelationClickListener);
				click = false;
				myListView.setAllPositionClickable(click);
			} else {
				conid = 1;
				click = true;
				ll_footer.setClickable(true);
				// 右上角按钮赋值
				setText(MyCreditCardActivity.this.getString(R.string.acc_main_right_btn));
				CommonAdapter adapter=new CommonAdapter<Map<String, Object>>(MyCreditCardActivity.this, bankAccountList, MyCreditCardActivity.this);
//				CrcdAccountAdapter adapter = new CrcdAccountAdapter(MyCreditCardActivity.this, bankAccountList, conid);
				myListView.setAdapter(adapter);
				/** 详情点击事件 */
				myListView.setAllPositionClickable(click);
			}
		}

	};

	/** 取消关联点击事件 */
	OnItemClickListener onbanklistCancelRelationClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			MyCreditCardActivity.this.position = position;
			conid = 2;
			// 弹出对话框,是否取消
			BaseDroidApp.getInstanse().showErrorDialog(
					MyCreditCardActivity.this.getString(R.string.acc_cancelrelation_msg), R.string.cancle,
					R.string.confirm, new OnClickListener() {
						@Override
						public void onClick(View v) {
							switch (Integer.parseInt(v.getTag() + "")) {
							case CustomDialog.TAG_SURE:
								// 确定取消关联
								BaseDroidApp.getInstanse().dismissErrorDialog();
								BaseHttpEngine.showProgressDialog();
								requestCommConversationId();
								break;
							case CustomDialog.TAG_CANCLE:
								// 取消操作
								BaseDroidApp.getInstanse().dismissErrorDialog();
								break;
							}
						}
					});

		}
	};
	/** 卡片事件 */
	BaseSwipeListViewListener swipeListListener = new BaseSwipeListViewListener() {
		@Override
		public void onOpened(int position, boolean toRight) {

		}

		@Override
		public void onClosed(int position, boolean fromRight) {
		}

		@Override
		public void onListChanged() {
		}

		@Override
		public void onMove(int position, float x) {
		}

		// 滑动卡片----查询卡片信息，进入到未出账单查询页面
		@Override
		public void onStartOpen(int position, int action, boolean right) {
			LogGloble.d("swipe", String.format("onStartOpen %d - action %d", position, action));
		
			crcdAccount = bankAccountList.get(position);
			cardType = String.valueOf(crcdAccount.get(Crcd.CRCD_ACCOUNTTYPE_RES));
			TranDataCenter.getInstance().setAccInInfoMap(crcdAccount);
			Intent it = new Intent(MyCreditCardActivity.this, MyCrcdDetailActivity.class);
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.IS_EBANK_0, 2);
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.CRCD_BANKACCOUNTLIST, bankAccountList);
			it.putExtra(ConstantGloble.ACC_POSITION, position);
			it.putExtra(Crcd.CRCD_ACCOUNTID_RES, (String)crcdAccount.get(Crcd.CRCD_ACCOUNTID_RES));
			startActivity(it);
		}

		@Override
		public void onStartClose(int position, boolean right) {
			LogGloble.d("swipe", String.format("onStartClose %d", position));
		}

		/** ListView的点击事件-------信用卡详情监听事件 */
		@Override
		public void onClickFrontView(int position) {
			// ListView的点击事件
			LogGloble.d("swipe", String.format("onClickFrontView %d", position));
			if (click) {
				MyCreditCardActivity.this.position = position;
				crcdAccount = bankAccountList.get(position);
				cardType = String.valueOf(crcdAccount.get(Crcd.CRCD_ACCOUNTTYPE_RES));
				TranDataCenter.getInstance().setAccInInfoMap(crcdAccount);

				// 查询信用卡币种
//				psnCrcdCurrencyQuery();
				
				String accountId=String.valueOf(crcdAccount.get(Crcd.CRCD_ACCOUNTID_RES));	
				BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.IS_EBANK_0, 2);
				BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.CRCD_BANKACCOUNTLIST, bankAccountList);	
				// 请求信用卡积分 
				requestPsnQueryCrcdPoint(accountId);
			}
		}

		@Override
		public void onClickBackView(int position) {
			LogGloble.d("swipe", String.format("onClickBackView %d", position));
		}

		@Override
		public void onDismiss(int[] reverseSortedPositions) {
			adapter.notifyDataSetChanged();
		}
	};

	/**
	 * 请求查询信用卡详情
	 */
	private void requestPsnCrcdQueryAccountDetail() {
		List<BiiRequestBody> biiRequestBodyList = new ArrayList<BiiRequestBody>();
		LogGloble.v(Crcd.TAG, bankAccountList.size() + "<><><><><><>");
		for (int i = 0; i < bankAccountList.size(); i++) {
			BiiRequestBody biiRequestBody = new BiiRequestBody();
			biiRequestBody.setMethod(Crcd.CRCD_ACCOUNTDETAIL_API);
			String accountId = (String) bankAccountList.get(i).get(Crcd.CRCD_ACCOUNTID_RES);
			LogGloble.v(Crcd.TAG, accountId + "");
			Map<String, String> paramsmap = new HashMap<String, String>();
			paramsmap.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
			paramsmap.put(Crcd.CRCD_CURRENCY, currency);
			biiRequestBody.setParams(paramsmap);
			biiRequestBodyList.add(biiRequestBody);
		}
		HttpManager.requestBii(biiRequestBodyList, this, "requestPsnCrcdQueryAccountDetailCallBack");
	}

	/**
	 * 请求查询信用卡详情回调
	 * 
	 * @param resultObj
	 */
	public void requestPsnCrcdQueryAccountDetailCallBack(Object resultObj) {
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
		
		adapter=new CommonAdapter<Map<String, Object>>(MyCreditCardActivity.this, bankAccountList, MyCreditCardActivity.this);
//		adapter = new CrcdAccountAdapter(MyCreditCardActivity.this, bankAccountList, 1);
		myListView.setAdapter(adapter);
		LogGloble.v(Crcd.TAG, bankAccountList.get(0).get("currentBalance") + "");
		adapter.notifyDataSetChanged();

	}

	/**
	 * 请求查询信用卡详情
	 */
	public void requestPsnCrcdQueryAccountDetail(Map<String, Object> map, String value) {
		if (isShowDialog) {
			BaseHttpEngine.showProgressDialog();
		}
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_ACCOUNTDETAIL_API);
		String accountId = (String) map.get(Crcd.CRCD_ACCOUNTID_RES);
		LogGloble.v(Crcd.TAG, accountId + "");
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		paramsmap.put(Crcd.CRCD_CURRENCY, value);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestAccountDetailCallBack");
	}

	/** 用户选择的信用卡的币种1对应的详情 */
	public static Map<String, Object> resultDetail;

	/**
	 * 请求查询信用卡详情---回调
	 */
	public void requestAccountDetailCallBack(Object resultObj) {
		isShowDialog = false;
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		resultDetail = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(resultDetail)) {
			return;
		}
		List<Map<String, String>> crcdAccountDetailList = (List<Map<String, String>>) resultDetail
				.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
		if (StringUtil.isNullOrEmpty(crcdAccountDetailList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		TranDataCenter.getInstance().setCurrInDetail(resultDetail);
		
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.IS_EBANK_0, 2);
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.CRCD_BANKACCOUNTLIST, bankAccountList);	
		String accountId=String.valueOf(crcdAccount.get(Crcd.CRCD_ACCOUNTID_RES));	
		
		// 请求信用卡积分 
		requestPsnQueryCrcdPoint(accountId);
		
	
		
//		Intent it = new Intent(this, CrcdDetailDialogActivity.class);
//		startActivityForResult(it, ConstantGloble.ACTIVITY_RESULT_CODE);
		conid = 1;
		adapter.notifyDataSetChanged();
	}

	
	
	// 请求信用卡积分 
	private void requestPsnQueryCrcdPoint(String accountId) {
		BaseHttpEngine.showProgressDialog();	
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNQUERYCRCDPOINT);		
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnQueryCrcdPointCallBack");	
	}

	// 请求信用卡积分 回调
	public void requestPsnQueryCrcdPointCallBack(Object resultObj) {
		
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		if(StringUtil.isNullOrEmpty(result)){
			crcdPoint="-";
		}else{
			crcdPoint=(String)result.get(Crcd.CRCD_CRCDPOINT);
		}
		
			
		requestPsnCrcdQueryGeneralInfo();
		
	
	}
	

	/** 信用卡综合信息查询 */
	private void requestPsnCrcdQueryGeneralInfo() {
		
		 accountId=String.valueOf(crcdAccount.get(Crcd.CRCD_ACCOUNTID_RES));
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
		BiiHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(result)) {			
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_transferquery_null));
			return;
		}
//		TranDataCenter.getInstance().getCrcdGeneralInfo().clear();
		TranDataCenter.getInstance().setCrcdGeneralInfo(result);
		
		Intent it = new Intent(MyCreditCardActivity.this, MyCardmessageAndSetActivity.class);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);// 账户ID
		it.putExtra(Crcd.CRCD_ACCOUNTTYPE_RES, cardType);//  卡类型	
		it.putExtra(Crcd.CRCD_CRCDPOINT, crcdPoint);//  积分			
		TranDataCenter.getInstance().setAccInInfoMap(crcdAccount);			
		it.putExtra(ConstantGloble.ACC_POSITION, position);	
		startActivity(it);
		
	}
	/** 修改账户别名弹出框视图 */
	private RelativeLayout modifyAccountAlias;

	/** 请求conversationid标志:id=1,请求修改别名;id=2,请求处理取消关联 */
	private int conid = 0;
	/** 用户选择的信用卡信息数据 */
	public static Map<String, Object> crcdAccount;
	/** 信用卡详情监听事件 */
	protected OnItemClickListener oncrcdDetailListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// 将转入账户信息保存
			crcdAccount = bankAccountList.get(position);
			TranDataCenter.getInstance().setAccInInfoMap(crcdAccount);
			// 查询信用卡币种
			psnCrcdCurrencyQuery();
		}
	};

	/** 查询信用卡币种 */
	public void psnCrcdCurrencyQuery() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDCURRENCYQUERY);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Crcd.CRCD_ACCOUNTNUMBER_RES, (String) (crcdAccount.get(Crcd.CRCD_ACCOUNTNUMBER_RES)));
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdCurrencyQueryCallBack");
	}

	/** 币种1代码 */
	public static String currency1 = "";
	/** 币种2代码 */
	public static String currency2 = "";
	/** 币种1 map */
	Map<String, Object> currencyMap1;
	/** 币种2 map */
	Map<String, Object> currencyMap2;

	/** 查询双币种---回调 */
	public void psnCrcdCurrencyQueryCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		currencyMap1 = (Map<String, Object>) resultMap.get(Crcd.CRCD_CURRENCY1);
		currencyMap2 = (Map<String, Object>) resultMap.get(Crcd.CRCD_CURRENCY2);
		if (!StringUtil.isNullOrEmpty(currencyMap1)) {
			currency1 = String.valueOf(currencyMap1.get(Crcd.CRCD_CODE));
		} else {
			currency1 = null;
		}
		if (!StringUtil.isNullOrEmpty(currencyMap2)) {
			currency2 = String.valueOf(currencyMap2.get(Crcd.CRCD_CODE));
		} else {
			currency2 = null;
		}

		// 先查币种,再查详情----查询币种1的详情
		requestPsnCrcdQueryAccountDetail(crcdAccount, currency1);
	}

	/** 修改账户别名返回 */
	protected String nickname = null;

	/** 修改别名监听事件 */
	public OnClickListener updateNicknameClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			EditText et_acc_accountnickname = (EditText) modifyAccountAlias.findViewById(R.id.et_acc_nickname);
			TextView tvNickName = (TextView) modifyAccountAlias.findViewById(R.id.acc_accountnickname_value);
			nickname = et_acc_accountnickname.getText().toString();
			tvNickName.setVisibility(View.VISIBLE);

			if (nickname.equals(tvNickName.getText().toString().trim())) {
				// 重新展示账户详情窗口
				closeInput();
				InflaterViewDialog inflaterViewDialog = new InflaterViewDialog(MyCreditCardActivity.this);
				modifyAccountAlias.removeAllViews();
				return;
			}
			// 验证账户别名
			RegexpBean reb = new RegexpBean(MyCreditCardActivity.this.getString(R.string.nickname_regex), nickname,
					"nickname");

			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb);
			if (RegexpUtils.regexpDate(lists)) {// 校验通过
				closeInput();
				requestCommConversationId();
				BaseHttpEngine.showProgressDialog();
			}
		}
	};

	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		if (conid == 1) {
			// 请求修改账户别名
			requestModifyAccountAlias();
		} else if (conid == 2) {
			// 获取TokenId
			pSNGetTokenId();
		}

	};

	/** 获取到的tokenId 保存 */
	private String tokenId;

	/**
	 * 获取tokenId
	 */
	public void pSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		HttpManager.requestBii(biiRequestBody, this, "aquirePSNGetTokenId");
	}

	/**
	 * 处理获取tokenId的数据得到tokenId
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void aquirePSNGetTokenId(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(tokenId)) {
			return;
		}
		// 请求取消关联
		requestCancleAccRelation();
	}

	/** 请求取消关联信息 */
	public void requestCancleAccRelation() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.ACC_CANCELACCRELATION_API);
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Acc.CANCELRELATION_ACC_ACCOUNTID,
				(String) (bankAccountList.get(position).get(Acc.ACC_ACCOUNTID_RES)));
		paramsmap.put(Acc.CANCELRELATION_ACC_ACCOUNTNUMBER,
				(String) (bankAccountList.get(position).get(Acc.ACC_ACCOUNTNUMBER_RES)));
		paramsmap.put(Acc.CANCELRELATION_ACC_TOKEN, tokenId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "cancleAccRelationCallBack");
	}

	/**
	 * 请求取消关联列表回调
	 * 
	 * @param resultObj
	 *            返回结果
	 */
	public void cancleAccRelationCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String result = (String) biiResponseBody.getResult();
		// 右上角按钮赋值
		setText(this.getString(R.string.acc_main_right_btn));
		click = true;
		CustomDialog.toastShow(this, this.getString(R.string.acc_cancelrelation_success));
		myListView.removeFooterView(addTransferView);
		init = 3;
		requestAllChinaBankAccount();
	}

	/** 取消关联后，查询信用卡数据 */
	public void requestAllChinaBankAccount() {
		// 通讯开始,展示通讯框
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.QRY_CRCD_LIST_API);
		Map<String, Object> params = new HashMap<String, Object>();
		String[] s = { ZHONGYIN, GREATWALL, SINGLEWAIBI };
		params.put(Crcd.CRCD_ACCOUNTTYPE_REQ, s);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestAllChinaBankAccountCallBack");
	}

	public void requestAllChinaBankAccountCallBack(Object resultObj) {
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		if (returnList != null && !returnList.isEmpty()) {
			returnList.clear();
		}
		returnList = (List<Map<String, Object>>) body.getResult();
		if (bankAccountList != null && !bankAccountList.isEmpty()) {
			bankAccountList.clear();
		}
		requestAllAccountList();

	}

	/** 请求修改账户别名 */
	public void requestModifyAccountAlias() {
		BaseHttpEngine.dissMissProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.ACC_MODIFYACCOUNT_API);
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Acc.MODIFY_ACC_ACCOUNTID_REQ, String.valueOf(crcdAccount.get(Crcd.CRCD_ACCOUNTID_RES)));
		paramsmap.put(Acc.MODIFY_ACC_ACCOUNTNICKNAME_REQ, nickname);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "modifyAccountAliasCallBack");
	}

	/**
	 * 请求修改账户别名回调
	 * 
	 * @param resultObj
	 *            返回结果
	 */
	public void modifyAccountAliasCallBack(Object resultObj) {

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String result = (String) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			return;
		}
		BaseHttpEngine.dissMissProgressDialog();
		CustomDialog.toastShow(this, this.getString(R.string.acc_modifyAccountAlias));
		// 修改显示的账户别名
		crcdAccount.put(Crcd.CRCD_NICKNAME_RES, nickname);
		// 重新展示账户详情窗口
		InflaterViewDialog inflaterViewDialog = new InflaterViewDialog(MyCreditCardActivity.this);
		adapter.notifyDataSetChanged();
		// 账户详情监听事件
	}

	OnClickListener crcdSetupClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().showCrcdSetupTypeDialog(onclickListener);
		}

	};

	OnClickListener onclickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch ((Integer) v.getTag()) {
			case CustomDialog.TAG_XIAOFEI_SETUP:
				Intent it = new Intent(MyCreditCardActivity.this, CrcdServiceSetupListActivity.class);
				it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(it);
				break;
			case CustomDialog.TAG_BILLSERVICE_SETUP:
				Intent it1 = new Intent(MyCreditCardActivity.this, CrcdPsnQueryCheckList.class);
				it1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(it1);
				break;
			case CustomDialog.TAG_MONEY_SETUP:
				Intent it2 = new Intent(MyCreditCardActivity.this, MyCrcdSetupListActivity.class);
				it2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(it2);
				break;
			case CustomDialog.TAG_FUSHUSERVICE_SETUP:
				Intent it3 = new Intent(MyCreditCardActivity.this, MySupplymentListActivity.class);
				it3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(it3);
				break;
			case CustomDialog.TAG_CANCLE:
				BaseDroidApp.getInstanse().dismissMessageDialog();
				break;
			}
		}
	};

	protected OnItemClickListener ontransDetailListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			MyCreditCardActivity.this.position = position;
			crcdAccount = bankAccountList.get(position);
			cardType = String.valueOf(crcdAccount.get(Crcd.CRCD_ACCOUNTTYPE_RES));
			TranDataCenter.getInstance().setAccInInfoMap(crcdAccount);
			String accountId=String.valueOf(crcdAccount.get(Crcd.CRCD_ACCOUNTID_RES));
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.IS_EBANK_0, 2);
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.CRCD_BANKACCOUNTLIST, bankAccountList);
			// 请求信用卡积分
			requestPsnQueryCrcdPoint(accountId);
		}
	};

	/** 退出账户详情监听事件 */
	protected OnClickListener exitAccDetailClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
		}
	};
	public boolean isShowDialog = false;

	/** 人民币监听事件 */
	protected OnClickListener renmibiClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			currency = currency1;
			isShowDialog = true;
			requestPsnCrcdQueryAccountDetail(crcdAccount, currency);
		}
	};

	/** 外币监听事件 */
	protected OnClickListener dollerClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			currency = currency2;
			isShowDialog = true;
			requestPsnCrcdQueryAccountDetail(crcdAccount, currency);
		}
	};

	/** 修改账户别名监听事件 */
	// public OnEditorActionListener updateNicknameClick = new
	// EditText.OnEditorActionListener() {
	// public boolean onEditorAction(android.widget.TextView v, int actionId,
	// android.view.KeyEvent event) {
	// return true;
	// };
	// };

	@Override
	protected void onDestroy() {
//		BaseDroidApp.getInstanse().dismissMessageDialog();
		super.onDestroy();
	}

	OnClickListener gotoTransfer = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent it = new Intent(MyCreditCardActivity.this, TransferManagerActivity1.class);
			it.putExtra(ConstantGloble.TRANS_ACCOUNT_TYPE, Crcd.TRANS_CREDIT);
			// 转账需要的map
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.TRANS_ACCOUNT_MAP, resultDetail);

			switch (v.getId()) {
			case R.id.btn_shoukuan:// 转入
				Crcd.tranState = Crcd.TRANS_IN;
				it.putExtra(ConstantGloble.TRANS_ACCOUNT_OPER_TYPE, Crcd.tranState);
				it.putExtra(ConstantGloble.TRANS_ACCOUNT_TRANSFER_TYPE, Crcd.TRANS_GUALIAN);
				it.putExtra(Crcd.TRANSTATE, Crcd.tranState);
				startActivity(it);
				break;
			case R.id.btn_fukuan:// 转出
				Crcd.tranState = Crcd.TRANS_OUT;
				it.putExtra(ConstantGloble.TRANS_ACCOUNT_OPER_TYPE, Crcd.tranState);
				it.putExtra(Crcd.TRANSTATE, Crcd.tranState);
				startActivity(it);
				break;
			case R.id.btn_xinyonghuan:// 转入
				Crcd.tranState = Crcd.TRANS_IN;
				it.putExtra(ConstantGloble.JUMP_TO_TRAN_FLAG, ConstantGloble.REL_CRCD_REPAY);
				startActivity(it);
				break;
			case R.id.btn_gohuihuan:// 购汇
				Crcd.tranState = Crcd.TRANS_GOHUAN;
				it.putExtra(ConstantGloble.JUMP_TO_TRAN_FLAG, ConstantGloble.REL_CRCD_BUY);
				startActivity(it);
				break;
			case R.id.btn_more:// 更多
				String[] selectors = null;
				String xiaofei = getString(R.string.mycrcd_xiaofei_fuwu_setup);
				String billservice = getString(R.string.mycrcd_to_bill_service_setup);
				String cutmoney = getString(R.string.mycrcd_cut_money_setup);
				String fushu = getString(R.string.mycrcd_fushu_service_setup);

				if (CrcdBaseActivity.ZHONGYIN.equals(v.getTag()) || CrcdBaseActivity.GREATWALL.equals(v.getTag())) {
					if (currency.equals(ConstantGloble.PRMS_CODE_RMB)) {
						selectors = new String[] { xiaofei, billservice, cutmoney, fushu };
					} else {
						selectors = new String[] { xiaofei, billservice, cutmoney, fushu };
					}

					PopupWindowUtils.getInstance().setshowMoreChooseUpListener(MyCreditCardActivity.this, v, selectors,
							greatwallListener);

				} else if (CrcdBaseActivity.SINGLEWAIBI.equals(v.getTag())) {
					selectors = new String[] { xiaofei, billservice, cutmoney, fushu };
					PopupWindowUtils.getInstance().setshowMoreChooseUpListener(MyCreditCardActivity.this, v, selectors,
							singlewaibiListener);
				}
				break;
			}

		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		BaseDroidApp.getInstanse().setCurrentAct(this);
		switch (resultCode) {
		case RESULT_OK:
			LogGloble.d(TAG, "requestTag=======" + requestTag);
			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE) {// 进入到我的信用卡详情页面
				adapter.notifyDataSetChanged();
			} else if (requestCode == ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE) {// 自主关联
				init = 2;
				BaseHttpEngine.showProgressDialog();
				if (requestTag == 1) {
					BaseHttpEngine.showProgressDialogCanGoBack();
					setLeftSelectedPosition("myCrcd_1");
					initLeftSideList(BaseDroidApp.getInstanse().getCurrentAct(), LocalData.myCrcdListData);
				}
				requestPsnCommonQueryAllChinaBankAccount();
			}
			break;
		case RESULT_CANCELED:
			LogGloble.d(TAG + "  onActivityResult", "fail");
			break;
		}
	};

	/** 再次查询信用卡账户 */
	public void requestPsnCommonQueryAllChinaBankAccount() {
		// 通讯开始,展示通讯框
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.QRY_CRCD_LIST_API);
		Map<String, Object> params = new HashMap<String, Object>();
		String[] s = { ZHONGYIN, GREATWALL, SINGLEWAIBI };
		params.put(Crcd.CRCD_ACCOUNTTYPE_REQ, s);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnCommonQueryAllChinaBankAccountCallBack");
	}

	/**
	 * 请求信用卡列表回调
	 */
	public void requestPsnCommonQueryAllChinaBankAccountCallBack(Object resultObj) {
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		if (returnList != null && !returnList.isEmpty()) {
			returnList.clear();
		}
		returnList = (List<Map<String, Object>>) body.getResult();
		// 查询所有的账户
		requestAllChinaBankAccount();
	}

	/** 取消关联后的操作 */
	private void searchAcountAfterCalcel() {
		BaseHttpEngine.dissMissProgressDialog();
		if (returnList == null || returnList.size() <= 0) {
			switch (requestTag) {
			case 1:// 没有信用卡时请求
				requestTag = 1;
//				Intent intent = new Intent(this, AccInputRelevanceAccountActivity.class);
//				startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
				
				BusinessModelControl.gotoAccRelevanceAccount(this, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, null);
				break;
			}
			return;
		}
		if (bankAccountList != null && !bankAccountList.isEmpty()) {
			bankAccountList.clear();
		}
		for (int i = 0; i < returnList.size(); i++) {
			if (ZHONGYIN.equals(returnList.get(i).get(Crcd.CRCD_ACCOUNTTYPE_RES))
					|| GREATWALL.equals(returnList.get(i).get(Crcd.CRCD_ACCOUNTTYPE_RES))
					|| SINGLEWAIBI.equals(returnList.get(i).get(Crcd.CRCD_ACCOUNTTYPE_RES))) {
				bankAccountList.add(returnList.get(i));
			}
		}
		dealHandle();
	}

	private void dealHandle() {
		switch (requestTag) {
		case 1:// 没有信用卡---关联信用卡
			if (StringUtil.isNullOrEmpty(bankAccountList)) {
				requestTag = 1;
//				Intent intent = new Intent(this, AccInputRelevanceAccountActivity.class);
//				startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
				
				BusinessModelControl.gotoAccRelevanceAccount(this, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, null);
				
			} else {
				no_acc_view.setVisibility(View.GONE);
				has_acc_view.setVisibility(View.VISIBLE);
				//setTitle(this.getString(R.string.mycrcd));
				setListViewDate();
			}
			break;
		case 2:// 自主关联信用卡
			setListViewDate();
			break;

		default:
			break;
		}
	}

	/** 长城信用卡 */
	OnClickListener greatwallListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Integer tag = (Integer) v.getTag();
			Intent it = new Intent();
			switch (tag) {
			case 0:
				it.setClass(MyCreditCardActivity.this, CrcdServiceSetupListActivity.class);
				break;
			case 1:
				it.setClass(MyCreditCardActivity.this, CrcdPsnQueryCheckList.class);
				break;
			case 2:
				it.setClass(MyCreditCardActivity.this, MyCrcdSetupListActivity.class);
				break;
			case 3:
				it.setClass(MyCreditCardActivity.this, MySupplymentListActivity.class);
				break;
			}
			startActivity(it);
		}

	};
	/** 单外币信用卡 */
	OnClickListener singlewaibiListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Integer tag = (Integer) v.getTag();
			Intent it = new Intent();
			switch (tag) {
			case 0:
				it.setClass(MyCreditCardActivity.this, CrcdServiceSetupListActivity.class);
				break;
			case 1:
				it.setClass(MyCreditCardActivity.this, CrcdPsnQueryCheckList.class);
				break;
			case 2:
				it.setClass(MyCreditCardActivity.this, MyCrcdSetupListActivity.class);
				break;
			case 3:
				it.setClass(MyCreditCardActivity.this, MySupplymentListActivity.class);
				break;
			}
			startActivity(it);
		}

	};

	@Override
	public View getView(final int arg0, Map<String, Object> currentItem,
			LayoutInflater inflater, View convertView, ViewGroup viewGroup) {
		CrcdViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.crcd_myaccount_list_item, null);
			holder = new CrcdViewHolder();
			holder.crcd_type_value = (TextView) convertView.findViewById(R.id.crcd_type_value);
			holder.crcd_account_nickname = (TextView) convertView.findViewById(R.id.crcd_account_nickname);
			holder.crcd_currencycode_value = (TextView) convertView.findViewById(R.id.crcd_currencycode_value);
			holder.crcd_account_num = (TextView) convertView.findViewById(R.id.crcd_account_num);
			holder.crcd_bookbalance_value = (TextView) convertView.findViewById(R.id.crcd_bookbalance_value);
			holder.ivdetail = (ImageView) convertView.findViewById(R.id.img_crcd_detail);
			holder.ivgoitem = (ImageView) convertView.findViewById(R.id.crcd_btn_goitem);
			holder.ivCancel = (ImageView) convertView.findViewById(R.id.crcd_btn_gocancelrelation);
			holder.img_crcd_currencycode = (ImageView) convertView.findViewById(R.id.img_crcd_currencycode);
			holder.crcd_currcycode = (TextView) convertView.findViewById(R.id.crcd_currcycode);

			convertView.setTag(holder);
		} else {
			holder = (CrcdViewHolder) convertView.getTag();
		}

//		Map<String, Object> map = bankAccountList.get(position);
		Map<String, Object> mapDetail = (Map<String, Object>) currentItem.get(Crcd.CRCD_DETAILIST);


//		String accountType = String.valueOf(map.get(Crcd.CRCD_ACCOUNTTYPE_RES));
//		String strAccountType = LocalData.AccountType.get(accountType);
//		holder.crcd_type_value.setText(strAccountType);
		String cardDescription=String.valueOf(currentItem.get(Crcd.CRCD_CARDDESCRIPTION));
		
		if(!StringUtil.isNullOrEmptyCaseNullString(cardDescription)){
			holder.crcd_type_value.setText(cardDescription);
		}else{
			holder.crcd_type_value.setText(ConstantGloble.BOCINVT_DATE_ADD);	
		}
//		holder.crcd_type_value.setText(cardDescription);

		holder.crcd_account_nickname.setText(String.valueOf(currentItem.get(Crcd.CRCD_NICKNAME_RES)));
		holder.crcd_currencycode_value.setText(getString(R.string.mycrcd_bill));
		holder.crcd_account_num.setText(StringUtil.getForSixForString(String.valueOf(currentItem
				.get(Crcd.CRCD_ACCOUNTNUMBER_RES))));

		String currency = LocalData.Currency.get(String.valueOf(currentItem.get(Crcd.CRCD_CURRENCYCODE)));

		holder.crcd_currcycode.setText(currency);
		if (!StringUtil.isNullOrEmpty(mapDetail)) {
			holder.crcd_bookbalance_value.setText(" : " + String.valueOf(mapDetail.get(Crcd.CRCD_TOTALBALANCE)));
		}

		holder.img_crcd_currencycode.setVisibility(View.GONE);

		if (conid == 1) {
			holder.ivgoitem.setVisibility(View.VISIBLE);
			holder.ivCancel.setVisibility(View.GONE);
		} else if (conid == 2) {
			holder.ivgoitem.setVisibility(View.GONE);
			holder.ivCancel.setVisibility(View.VISIBLE);
		}

		holder.ivdetail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (oncrcdDetailListener != null) {
					oncrcdDetailListener.onItemClick(null, v, arg0, arg0);
				}
				// Intent it = new Intent(context, MyCrcdDetailActivity.class);
				// context.startActivity(it);
			}
		});

		holder.ivgoitem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ontransDetailListener != null) {
					ontransDetailListener.onItemClick(null, v, arg0, arg0);
				}
			}
		});

		holder.ivCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onbanklistCancelRelationClickListener != null) {
					onbanklistCancelRelationClickListener.onItemClick(null, v, position, position);
				}
			}
		});

		return convertView;
	}
	class CrcdViewHolder {
		TextView crcd_type_value;
		TextView crcd_account_nickname;
		TextView crcd_currencycode_value;
		TextView crcd_account_num;
		TextView crcd_bookbalance_value;

		TextView crcd_currcycode;

		ImageView ivdetail;
		ImageView ivgoitem;
		ImageView ivCancel;

		ImageView img_crcd_currencycode;
	}
}
