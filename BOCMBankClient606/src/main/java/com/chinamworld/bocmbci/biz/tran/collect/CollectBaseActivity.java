package com.chinamworld.bocmbci.biz.tran.collect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Collect;
import com.chinamworld.bocmbci.bii.constant.Collect.CollectStatusType;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IHttpCallBack;
import com.chinamworld.bocmbci.biz.tran.atmremit.AtmThirdMenu;
import com.chinamworld.bocmbci.biz.tran.ecard.TransferEcardActivity;
import com.chinamworld.bocmbci.biz.tran.managetrans.ManageTransActivity;
import com.chinamworld.bocmbci.biz.tran.mobiletrans.MobileTransThirdMenu;
import com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran.TransferManagerActivity1;
import com.chinamworld.bocmbci.biz.tran.remit.RemitThirdMenu;
import com.chinamworld.bocmbci.biz.tran.twodimentrans.TwoDimenTransActivity1;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CollectBaseActivity extends BaseActivity implements
		OnClickListener {

	public LayoutInflater mInflater;
	/** 加载布局 */
	public LinearLayout tabcontent = null;
	/** 头部右边按钮 */
	protected Button mTopRightBtn = null;
	/** 头部返回按钮 */
	protected Button back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		initPulldownBtn(); // 加载上边下拉菜单
		initFootMenu(); // 加载底部菜单栏
		initLeftSideList(this, LocalData.tranManagerLeftList); // 加载左边菜单栏
		// goneLeftView();
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		mInflater = LayoutInflater.from(this);
		// 主界面按钮
		// mainBtn = (Button) findViewById(R.id.ib_top_right_btn);
		// 头部左边返回按钮
		back = (Button) findViewById(R.id.ib_back);
		back.setOnClickListener(this);
		// 显示头部右边按钮，发起新转账
		mTopRightBtn = (Button) findViewById(R.id.ib_top_right_btn);
		mTopRightBtn.setOnClickListener(this);
	}

	public void toprightBtn() {
		mTopRightBtn.setText(this.getString(R.string.go_main));
		mTopRightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(CollectBaseActivity.this, MainActivity.class);
//				startActivity(intent);
				goToMainActivity();
			}
		});
	}

	/**
	 * 在slidingbody中引入自己布局文件
	 * 
	 * @param resource
	 * @return 引入布局
	 */
	public View addView(int resource) {
		View view = LayoutInflater.from(this).inflate(resource, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		return view;
	}

	/**
	 * 头部左侧和右侧按钮
	 * 
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
			finish();
			break;
		}
	}

//	/**
//	 * 隐藏左侧菜单栏
//	 */
//	public void goneLeftView() {
//		// 隐藏左侧菜单
//		LinearLayout slidingTab = (LinearLayout) findViewById(R.id.sliding_tab);
//		Button btn_show = (Button) findViewById(R.id.btn_show);
//		Button btn_hide = (Button) findViewById(R.id.btn_hide);
//		Button btn_fill_show = (Button) findViewById(R.id.btn_fill_show);
//		slidingTab.setVisibility(View.GONE);
//		btn_show.setVisibility(View.GONE);
//		setLeftButtonPopupGone();
//		btn_hide.setVisibility(View.GONE);
//		btn_fill_show.setVisibility(View.GONE);
//	}

	/**
	 * 左边菜单栏
	 */
	@Override
	protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {
		super.selectedMenuItemHandler(context, menuItem);
		String menuId = menuItem.MenuID;
		if(menuId.equals("tranManager_1") ){
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof TransferManagerActivity1)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent0 = new Intent();
				intent0.setClass(this, TransferManagerActivity1.class);
				context.startActivity(intent0);
			}
		}
		else if(menuId.equals("tranManager_2")){
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof MobileTransThirdMenu)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent1 = new Intent();
				intent1.setClass(this, MobileTransThirdMenu.class);
				context.startActivity(intent1);
			}
		}
       else if(menuId.equals("tranManager_3")){
    	   if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof TwoDimenTransActivity1)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent2 = new Intent();
				intent2.setClass(this, TwoDimenTransActivity1.class);
				context.startActivity(intent2);
			}
		}
       else if(menuId.equals("tranManager_4")){
    	   if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof ManageTransActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent3 = new Intent();
				intent3.setClass(this, ManageTransActivity.class);
				context.startActivity(intent3);
			}
		}
       else if(menuId.equals("tranManager_5")){
    	   if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof AtmThirdMenu)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent4 = new Intent();
				intent4.setClass(this, AtmThirdMenu.class);
				context.startActivity(intent4);
			}
		}
       else if(menuId.equals("tranManager_6")){
    	   if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof RemitThirdMenu)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent5 = new Intent();
				intent5.setClass(this, RemitThirdMenu.class);
				context.startActivity(intent5);
			}
		}
       else if(menuId.equals("tranManager_7")){
    		if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof CollectMainActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent6 = new Intent();
				intent6.setClass(this, CollectMainActivity.class);
				context.startActivity(intent6);
			}
		}
       else if(menuId.equals("tranManager_8")){
    		if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof TransferEcardActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent7 = new Intent();
				intent7.setClass(this, TransferEcardActivity.class);
				context.startActivity(intent7);
			}
		}
     
		return true;
//		
//		switch (clickIndex) {
//		case ConstantGloble.MYTRANS:
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof TransferManagerActivity1)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent0 = new Intent();
//				intent0.setClass(CollectBaseActivity.this,
//						TransferManagerActivity1.class);
//				startActivity(intent0);
//			}
//			break;
//		case ConstantGloble.MOBILETRANS:
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof MobileTransThirdMenu)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent1 = new Intent();
//				intent1.setClass(CollectBaseActivity.this,
//						MobileTransThirdMenu.class);
//				startActivity(intent1);
//			}
//
//			break;
//		case ConstantGloble.TWODIMENTRANSA:
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof TwoDimenTransActivity1)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent2 = new Intent();
//				intent2.setClass(CollectBaseActivity.this,
//						TwoDimenTransActivity1.class);
//				startActivity(intent2);
//			}
//			break;
//		case ConstantGloble.MANAGETRANS:
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof ManageTransActivity)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent3 = new Intent();
//				intent3.setClass(CollectBaseActivity.this,
//						ManageTransActivity.class);
//				startActivity(intent3);
//			}
//
//			break;
//		case 4:
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof AtmThirdMenu)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent4 = new Intent();
//				intent4.setClass(CollectBaseActivity.this, AtmThirdMenu.class);
//				startActivity(intent4);
//			}
//			break;
//		case 5:
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof RemitThirdMenu)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent5 = new Intent();
//				intent5.setClass(CollectBaseActivity.this, RemitThirdMenu.class);
//				startActivity(intent5);
//			}
//			break;
//		case 6:
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof CollectMainActivity)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent6 = new Intent();
//				intent6.setClass(CollectBaseActivity.this,
//						CollectMainActivity.class);
//				startActivity(intent6);
//			}
//			break;
//
//		case 7:
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof TransferEcardActivity)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent7 = new Intent();
//				intent7.setClass(CollectBaseActivity.this,
//						TransferEcardActivity.class);
//				startActivity(intent7);
//			}
//			break;
//		default:
//			break;
//		}

	}

	public void goneBackView() {
		View backView = findViewById(R.id.ib_back);
		if (backView != null) {
			backView.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	}

	private Map<String, Object> curSelectedAccount;
	private int totalSize = 0;

	IHttpCallBack psnCBCollectQueryCallBack = null;

	protected void requestPsnCBCollectQuery(final Map<String, Object> account,
			final String status, final int index, final int page,
			final boolean isRefresh, IHttpCallBack callBack) {
		psnCBCollectQueryCallBack = callBack;
		curSelectedAccount = account;
		totalSize = page;
		BiiHttpEngine.showProgressDialog();
		requestCommConversationId(new IHttpCallBack() {

			@Override
			public void requestHttpSuccess(Object result) {
				CollectBaseActivity.this.sendRequestCollectQuery(account,
						status, index, page, isRefresh);
			}

		});
	}

	private void sendRequestCollectQuery(Map<String, Object> account,
			String status, int index, int page, final boolean isRefresh) {
		totalSize = page;
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Collect.PSN_CBCOLLECT_QUERY_API);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Collect.accountId, account.get(Comm.ACCOUNT_ID));
		params.put(Collect.queryType, status);
		params.put(PubConstants.PUB_QUERY_FEILD_PAGE_SIZE, String.valueOf(page));
		params.put(PubConstants.PUB_QUERY_FEILD_CURRENT_INDEX,
				EpayUtil.getString(index, "0"));
		params.put(PubConstants.PUB_QUERY_FEILD_IS_REFRESH,                        
				EpayUtil.getString(isRefresh, "true"));
		biiRequestBody.setParams(params);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, CollectBaseActivity.this,
				"requestCollectQueryCallBack");
	}

	/**
	 * 获取被归集列表回调
	 */
	@SuppressWarnings("unchecked")
	public void requestCollectQueryCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultData = (Map<String, Object>) biiResponseBody
				.getResult();
		String totalItems = (String) resultData.get(Collect.totalItems);
		int total = Integer.valueOf(totalItems);
		if (total > totalSize) {
			sendRequestCollectQuery(curSelectedAccount,
					CollectStatusType.ALL_STATUS, 0, total, true);
			return;
		}
		// BiiHttpEngine.dissMissProgressDialog();
		curSelectedAccount.put(Collect.cbAccBankNum,
				resultData.get(Collect.cbAccBankNum));
		curSelectedAccount.put(Collect.cbAccNum,
				resultData.get(Collect.cbAccNum));
		curSelectedAccount.put(Collect.cbAccCard,
				resultData.get(Collect.cbAccCard));
		curSelectedAccount.put(Collect.cbMobile,
				resultData.get(Collect.cbMobile));
		curSelectedAccount.put(Collect.cbAccName,
				resultData.get(Collect.cbAccName));
		List<Map<String, Object>> listData = (List<Map<String, Object>>) resultData
				.get(Collect.list);
		CollectData.getInstance().setCollectAccount(
				(String) curSelectedAccount.get(Acc.ACC_ACCOUNTNUMBER_RES),
				listData);
		
		if (psnCBCollectQueryCallBack != null) {
			psnCBCollectQueryCallBack.requestHttpSuccess(resultObj);
			psnCBCollectQueryCallBack = null;
		}
		
	}

	
	/**
	 * 根据中行账户查询协议信息
	 */
	protected void requestPsnCBRestBankview(IHttpCallBack callBack) {
		psnCBCollectQueryCallBack = callBack;
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Collect.PsnCBRestBankview);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Collect.accountId, CollectData.getInstance().curSelectedAccount.get(Comm.ACCOUNT_ID));
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnCBRestBankviewCallback");
	}
	
	/**
	 * 中行账户查询协议信息回调
	 */
	public void requestPsnCBRestBankviewCallback(Object resultObj) {
		String mPayeeAccount  = (String)CollectData.getInstance().curSelectedAccount.get(Acc.ACC_ACCOUNTNUMBER_RES);
		BiiHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultData = (Map<String, Object>) biiResponseBody
				.getResult();
		// 设置已设置被归集账户
		List<Map<String, Object>> collectDatas = CollectData.getInstance()
				.getCollectAccount(mPayeeAccount);
//		setCollectAdapter(collectDatas);

		List<Map<String, Object>> queryList = (List<Map<String, Object>>) resultData
				.get(Collect.queryList);
		List<Map<String, Object>> payList = (List<Map<String, Object>>) resultData
				.get(Collect.payList);
		// 过滤方式
		// 根据卡号的唯一性。
		// map<银行号，set<银行卡>>
		LinkedHashMap<String, Map<String, Object>> unCollectData = new LinkedHashMap<String, Map<String, Object>>();
		if(payList != null){
			for (Map<String, Object> pay : payList) {
				Map<String, Object> unCollect = new HashMap<String, Object>();
				String account = (String) pay.get(Collect.retAct);
				unCollect.put(Collect.payerAcctBankNo, pay.get(Collect.retBankNo));
				unCollect.put(Collect.payerAccBankName, pay.get(Collect.retBank));
				unCollect.put(Collect.payerAccount, account);
				unCollect.put(Collect.payerAccountName, pay.get(Collect.retName));
				unCollect.put(Collect.retActype, pay.get(Collect.retActype));
				unCollect.put(Collect.status, pay.get(Collect.payStatus));
				unCollect.put("ispay", Boolean.toString(true));
				unCollectData.put(account, unCollect);
			}
		}
		
		if(queryList != null){
			for (Map<String, Object> query : queryList) {
				String account = (String) query.get(Collect.rptAct);
				if (unCollectData.containsKey(account)) {
					Map<String, Object> unCollect = unCollectData.get(account);
					unCollect.put("isquery", Boolean.toString(true));
				} else {
					Map<String, Object> unCollect = new HashMap<String, Object>();
					unCollect.put(Collect.payerAcctBankNo,
							query.get(Collect.rptBankNo));
					unCollect.put(Collect.payerAccBankName,
							query.get(Collect.rptBank));
					unCollect.put(Collect.payerAccount, query.get(Collect.rptAct));
					unCollect.put(Collect.payerAccountName,
							query.get(Collect.rptName));
					unCollect.put(Collect.status, query.get(Collect.queryStatus));
					unCollect.put("isquery", Boolean.toString(true));
					unCollectData.put(account, unCollect);
				}
			}
		}

		if(collectDatas != null){
			for (Map<String, Object> collect : collectDatas) {
				String account = (String) collect.get(Collect.payerAccount);
				if (unCollectData.containsKey(account)) {
					unCollectData.remove(account);
				}
			}
		}

		Collection<Map<String, Object>> values = unCollectData.values();
		List<Map<String, Object>> unCollectList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : values) {
			unCollectList.add(map);
		}

		CollectData.getInstance().setQueryList(mPayeeAccount, queryList);
		CollectData.getInstance().setPayList(mPayeeAccount, payList);
		CollectData.getInstance().setUnCollectAccount(mPayeeAccount,
				unCollectList);
		
		if (psnCBCollectQueryCallBack != null) {
			psnCBCollectQueryCallBack.requestHttpSuccess(resultObj);
			psnCBCollectQueryCallBack = null;
		}

	}
	
}
