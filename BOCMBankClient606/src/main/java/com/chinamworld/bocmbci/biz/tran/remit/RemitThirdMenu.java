package com.chinamworld.bocmbci.biz.tran.remit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;

/**
 * 汇款笔数套餐三级菜单页面
 * 
 * @author wangmengmeng
 * 
 */
public class RemitThirdMenu extends TranBaseActivity {
	
	/**查询*/
	public static final int Query_Tag = 1;
	/**解约*/
	public static final int Cancel_Tag = 2;
	
	/** 套餐签约 */
	private LinearLayout remitOneLayout;
	/** 套餐修改 */
	private LinearLayout remitTwoLayout;
	/** 套餐查询 */
	private LinearLayout remitThreeLayout;
	/** 套餐使用明细查询 */
	// private LinearLayout remitFourLayout;
	/** 解除自动续约 */
	private LinearLayout remitFiveLayout;
	/** 1-套餐明细查询 ,2-解除自动续约 */
	private int tag = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.trans_remit_title));
		View view = mInflater.inflate(R.layout.tran_remit_third_menu, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);

		mTopRightBtn.setVisibility(View.INVISIBLE);

		setupView();
		setLeftSelectedPosition("tranManager_6");
	}

	/**
	 * 初始化加载XML里的控件
	 */
	private void setupView() {

		remitOneLayout = (LinearLayout) findViewById(R.id.ll_remit_one);
		remitTwoLayout = (LinearLayout) findViewById(R.id.ll_remit_two);
		remitThreeLayout = (LinearLayout) findViewById(R.id.ll_remit_three);
		// remitFourLayout = (LinearLayout) findViewById(R.id.ll_remit_four);
		remitFiveLayout = (LinearLayout) findViewById(R.id.ll_remit_five);
		remitOneLayout.setOnClickListener(oneClickListener);
		remitTwoLayout.setOnClickListener(twoClickListener);
		remitThreeLayout.setOnClickListener(threeClickListener);
		// remitFourLayout.setOnClickListener(fourClickListener);
		remitFiveLayout.setOnClickListener(fiveClickListener);
	}

	/** 套餐签约 */
	OnClickListener oneClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 进入接受协议页面
			Intent intent = new Intent(RemitThirdMenu.this, RemitProtocolActivity.class);
			startActivity(intent);
		}
	};
	/** 套餐修改 */
	OnClickListener twoClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			requestRemitAccBankAccountList();
		}
	};
	/** 套餐查询 */
	OnClickListener threeClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 查询账户
			tag = Query_Tag;
			BaseHttpEngine.showProgressDialog();
			requestCrcdList();
		}
	};
//	/** 套餐使用明细查询 */
//	OnClickListener fourClickListener = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			// 查询账户
//			tag = 2;
//			BaseHttpEngine.showProgressDialog();
//			requestCrcdList();
//		}
//	};
	/** 解除自动续约 */
	OnClickListener fiveClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			tag = Cancel_Tag;
			BaseHttpEngine.showProgressDialog();
			requestCrcdList();
		}
	};

	/** 查询签约账户 */
	private void requestCrcdList() {
		// 通讯开始,展示通讯框
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.QRY_CRCD_LIST_API);
		Map<String, Object> params = new HashMap<String, Object>();
		String[] s = { ConstantGloble.ACC_TYPE_BRO, ConstantGloble.ACC_TYPE_ORD, ConstantGloble.ACC_TYPE_RAN };
		params.put(Crcd.CRCD_ACCOUNTTYPE_REQ, s);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestCrcdListCallBack");
	}

	/** 查询签约账户---回调 */
	public void requestCrcdListCallBack(Object resultObj) {
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		List<Map<String, String>> returnList = (List<Map<String, String>>) body.getResult();
		if (returnList == null || returnList.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent(RemitThirdMenu.this, RemitQueryChoiseAccActivity.class);
		intent.putExtra(ConstantGloble.FOREX_TYPE, tag);
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.TRAN_REMIT_SIGN_RESULT, returnList);
		startActivity(intent);
	}

	@Override
	public void requestRemitAccBankAccountListCallBack(Object resultObj) {
		super.requestRemitAccBankAccountListCallBack(resultObj);
		// 进入选择账户页面
		Intent intent = new Intent(this, RemitChooseAccountActivity.class);
		intent.putExtra(ConstantGloble.ACC_ISMY, true);
		startActivity(intent);
	}
}
