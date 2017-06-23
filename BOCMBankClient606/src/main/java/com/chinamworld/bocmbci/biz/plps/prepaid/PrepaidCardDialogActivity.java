package com.chinamworld.bocmbci.biz.plps.prepaid;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.PlpsMenu;
import com.chinamworld.bocmbci.biz.plps.adapter.PaymentDialogAdapter;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrepaidCardDialogActivity extends PlpsBaseActivity {
	private ArrayList<HashMap<String, Object>> accountList;
	/** 预付卡充值图片 */
	private int[] imageIds = new int[] { R.drawable.prepaid_balance_query,
			R.drawable.prepaid_recharges, R.drawable.prepaid_recharge_query_result };
	/** 预付卡列表 */
	private String[] textIds = new String[]{
			"预付卡余额查询",
			"预付卡充值",
			"充值结果查询"
	};
	// 点击位置
	private int mPosition;
//	private RelativeLayout rl_bankt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		BaseDroidApp.getInstanse().setDialogAct(true);
		// 添加布局
//		setContentView(R.layout.plps_prepaid_carddialog);
//		getWindow().setLayout(LayoutParams.MATCH_PARENT,
//				LayoutParams.MATCH_PARENT);
		setTitle(R.string.plps_prepaid_card);
		inflateLayout(R.layout.plps_prepaid_carddialog);
		accountList = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < imageIds.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("image", imageIds[i]);
			map.put("text", textIds[i]);
			accountList.add(map);
		}
		setUpView();
	}

	private void setUpView() {
		GridView gridView = (GridView) findViewById(R.id.gridview);
//		rl_bankt = (RelativeLayout) findViewById(R.id.rl_bankt);
		PaymentDialogAdapter paymentProjectAdapter = new PaymentDialogAdapter(
				this, accountList);
		gridView.setOnItemClickListener(this);
		gridView.setAdapter(paymentProjectAdapter);
//		rl_bankt.setOnTouchListener(new OnTouchListener() {
//
//			@SuppressLint("ClickableViewAccessibility")
//			@Override
//			public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
//				// TODO Auto-generated method stub
//				finish();
//				return false;
//			}
//		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		super.onItemClick(parent, view, position, id);
		mPosition = position;
		if (mPosition == 0) {
			BaseHttpEngine.dissMissProgressDialog();
			requestCommConversationId();
//			requestHttp(Login.COMM_RANDOM_NUMBER_API, "requestPSNGetRandomCallBack", null, true);
//			annuityIntentAction(mPosition, PlpsMenu.PREPAID);
		} else {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(Comm.ACCOUNT_TYPE,
					PlpsDataCenter.plpsPrepaidCardAccountType);
			requestHttp(Comm.QRY_ALL_BANK_ACCOUNT,
					"requestPsnCommonQueryAllChinaBankAccountCallBack", params,
					false);
		}
//		requestPrepaidCardQuerySupportCardType();
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestHttp(Login.COMM_RANDOM_NUMBER_API, "requestPSNGetRandomCallBack", null, true);
	}
	/**
	 * 查询卡类型
	 */
	/*private void requestPrepaidCardQuerySupportCardType() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.PREPARDCARDTYPE);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPrepaidCardQuerySupportCardTypeCallBack");
	}*/
	
	/*@SuppressWarnings("unchecked")
	public void requestPrepaidCardQuerySupportCardTypeCallBack(Object resultObj) {
		List<Map<String, Object>> resultType = HttpTools.getResponseResult(resultObj);
		PlpsDataCenter.getInstance().setResultCardType(resultType);
		if (mPosition == 0) {
			BaseHttpEngine.dissMissProgressDialog();
			requestHttp(Login.COMM_RANDOM_NUMBER_API, "requestPSNGetRandomCallBack", null, true);
//			annuityIntentAction(mPosition, PlpsMenu.PREPAID);
		} else {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(Comm.ACCOUNT_TYPE,
					PlpsDataCenter.plpsPrepaidCardAccountType);
			requestHttp(Comm.QRY_ALL_BANK_ACCOUNT,
					"requestPsnCommonQueryAllChinaBankAccountCallBack", params,
					false);
		}
	}*/
	/** 请求随机数回调 */
	public void requestPSNGetRandomCallBack(Object resultObj) {
		String randomNumber =  HttpTools.getResponseResult(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNull(randomNumber)){
			return;
		}else {
			PlpsDataCenter.getInstance().setRandom(randomNumber);
			annuityIntentAction(mPosition, PlpsMenu.PREPAID);
		}
			
	}
	
	@SuppressWarnings("unchecked")
	public void requestPsnCommonQueryAllChinaBankAccountCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		List<Map<String, Object>> cardList = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(cardList)) {
			BaseDroidApp.getInstanse().showMessageDialog("没有可用的借记卡账户", new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					BaseDroidApp.getInstanse().dismissMessageDialog();
				}
			});
			return;
		}
		PlpsDataCenter.getInstance().setAcctList(cardList);
		if(mPosition == 2){
			requestSystemDateTime();
		}else {
			annuityIntentAction(mPosition, PlpsMenu.PREPAID);
		}
	}
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestSystemDateTimeCallBack(resultObj);
		PlpsDataCenter.getInstance().setSysDate(dateTime);
		annuityIntentAction(mPosition, PlpsMenu.PREPAID);
	}
}
