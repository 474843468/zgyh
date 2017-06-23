package com.chinamworld.bocmbci.biz.plps.payment.project.dialogactivity;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.PlpsMenu;
import com.chinamworld.bocmbci.biz.plps.adapter.PaymentDialogAdapter;
import com.chinamworld.bocmbci.biz.plps.annuity.AnnuityAcctSigneConfirmActivity;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;

/*
 * 养老金服务弹出框
 *  @ zxj
 */
public class PensionServiceDialogActivity extends PlpsBaseActivity {

	private RelativeLayout bank;
	/** 养老金服务图片 */
	private int[] imageIds = new int[] { R.drawable.pension_account,
			R.drawable.business_infor_query, R.drawable.pension_apply_result };
	/** 养老金列表 */
	private String[] textIds = new String[] { "养老金账户", "业务信息查询", "申请结果查询" };
	private ArrayList<HashMap<String, Object>> accountList;
	// 点击位置
	private int mPosition;
//	private RelativeLayout rl_bankt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		BaseDroidApp.getInstanse().setDialogAct(true);
		// 添加布局
//		setContentView(R.layout.plps_pension_dialog);
		inflateLayout(R.layout.plps_pension_dialog);
		setTitle(R.string.plps_annuity_title);
//		getWindow().setLayout(LayoutParams.MATCH_PARENT,
//				LayoutParams.MATCH_PARENT);
		bank = (RelativeLayout) findViewById(R.id.rl_bank);
		accountList = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < imageIds.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("image", imageIds[i]);
			map.put("text", textIds[i]);
			accountList.add(map);
		}
		setUpView();
	}
	
	/** 填充view */
	private void setUpView() {
		GridView gridView = (GridView) findViewById(R.id.gridview);
//		rl_bankt = (RelativeLayout)findViewById(R.id.rl_bankt);
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
		requestAnnuityAccIsSigned();
	}

	/** 判断养老金是否签约 */
	private void requestAnnuityAccIsSigned() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.METHODANNUITYACCISSIGNED);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this,
				"requestAnnuityAccIsSignedCallBack");
	}

	/** 判断养老金是否签约回调 */
	public void requestAnnuityAccIsSignedCallBack(Object resultObj) {
		String isSigned = (String) HttpTools.getResponseResult(resultObj);
		if (isSigned.equals("true")) {
			if (mPosition == 1||mPosition == 3) {
				requestSystemDateTime();
				return;
			}
			/** 获取养老金计划列表 */
			requestAnnuityPlanList();
			return;
		}else {
			if(mPosition==1||mPosition==2){
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showMessageDialog("个人未建立账户", new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						BaseDroidApp.getInstanse().dismissMessageDialog();
					}
				});
			}else {
				BaseHttpEngine.dissMissProgressDialog();
				startActivity(new Intent(this, AnnuityAcctSigneConfirmActivity.class)
						.putExtra("p", mPosition));
			}
		}
		
	}

	/** 获取养老金计划列表回调 */
	@Override
	public void annuityPlanListCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.annuityPlanListCallBack(resultObj);
		annuityIntentAction(mPosition, PlpsMenu.ANNUITY);
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestSystemDateTimeCallBack(resultObj);
		PlpsDataCenter.getInstance().setSysDate(dateTime);
		requestAnnuityPlanList();
	}
	
	
}
