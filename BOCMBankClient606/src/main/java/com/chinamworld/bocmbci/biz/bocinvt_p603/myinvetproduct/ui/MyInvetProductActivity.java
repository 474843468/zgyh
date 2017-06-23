package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.BusinessModelControl;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt.inflateView.InflaterViewDialog;
import com.chinamworld.bocmbci.biz.bocinvt.myproduct.MyProductListActivity;
import com.chinamworld.bocmbci.biz.bocinvt.productlist.InvtBindingChooseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.productlist.QueryProductActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.biz.invest.activity.InvesAgreeActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 我的理财产品页面
 * 
 * 
 * @author HVZHUNG
 *
 */
public class MyInvetProductActivity extends BocInvtBaseActivity implements
		OnClickListener {

	/** 持仓管理 */
	private Button tv_hold_manager;
	/** 委托交易查询 */
	private Button tv_query_for_entrust_trade;
	/** 过期产品查询 */
	private Button tv_query_for_due_product;

	/** 是否开通了投资理财服务 */
	private boolean isOpenBocinvt = false;
	/** 是否进行了风险评估 */
	private boolean isInvtEva = false;
	/** 理财账户列表 */
	private Map<String, Object> accountList = new HashMap<String, Object>();
	/** 右上角按钮 */
	protected Button btn_right;
	/** 任务弹出框视图 */
	private RelativeLayout dialogView;
	/** 判断是否从持仓管理点击而来 */
	public boolean isFrommyinveet = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestPsnInvestmentisOpenBefore();
		setTitle(getString(R.string.boci_myproduct_title));
		setContentView(R.layout.bocinvt_myinvetproduct_view);
		findViewById(R.id.tv_hold_manager).setOnClickListener(this);
		findViewById(R.id.tv_query_for_entrust_trade).setOnClickListener(this);
		findViewById(R.id.tv_query_for_due_product).setOnClickListener(this);
		BiiHttpEngine.showProgressDialogCanGoBack();
		btn_right = (Button) findViewById(R.id.ib_top_right_btn);
		setLeftSelectedPosition("bocinvtManager_2");
		gonerightBtn();

	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tv_hold_manager:// 持仓管理
//			ActivityIntentTools.intentToActivity(this,
//					MyProductListActivity.class);
			Intent intent = new Intent(MyInvetProductActivity.this,MyProductListActivity.class);
			intent.putExtra("isFrommyinveet", true);
			startActivity(intent);
			break;
		case R.id.tv_query_for_entrust_trade:// 委托交易查询
			startActivity(new Intent(this, CommissionDealQueryActivityNew.class));
			break;
		case R.id.tv_query_for_due_product:// 过期产品查询
			startActivity(new Intent(this, DeprecatedProductQueryActivityNew.class));
			break;

		default:
			break;
		}
	}

	/**
	 * 判断是否开通投资理财服务
	 */
	public void requestPsnInvestmentisOpenBefore() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_API);
		biiRequestBody.setParams(null);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnInvestmentisOpenBeforeCallback");
	}

	/**
	 * 判断是否开通投资理财服务---回调
	 * 
	 * @param resultObj
	 */
	public void requestPsnInvestmentisOpenBeforeCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String isOpenresult = String.valueOf(biiResponseBody.getResult());
		if (StringUtil.isNull(isOpenresult)) {
			BiiHttpEngine.dissMissProgressDialog();
			return;
		}
		isOpenBocinvt = Boolean.valueOf(isOpenresult);

		// 请求风险评估
//		requestInvtEva();//603二级菜单去掉风险评估
		requestBociAcctList("1", "0");
	}

	/**
	 * 请求是否进行过风险评估
	 */
//	public void requestInvtEva() {
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(BocInvt.PSNINVTEVALUATIONINIT_API);
//		HttpManager.requestBii(biiRequestBody, this, "requestInvtEvaCallback");
//	}

	/**
	 * 请求是否进行过风险评估---回调
	 * 
	 * @param resultObj
	 */
//	public void requestInvtEvaCallback(Object resultObj) {
//		Map<String, Object> responseMap = HttpTools.getResponseResult(resultObj);
//		if (StringUtil.isNullOrEmpty(responseMap)) {
//			BaseHttpEngine.dissMissProgressDialog();
//			return;
//		}
//		BaseDroidApp.getInstanse().getBizDataMap()
//				.put(ConstantGloble.BOICNVT_ISBEFORE_RESULT, responseMap);
//		Map<String, String> inputMap = new HashMap<String, String>();
//		inputMap.put(BocInvt.BOCINVT_EVA_GENDER_REQ,
//				(String.valueOf(responseMap.get(BocInvt.BOCIEVA_GENDER_RES))));
//		inputMap.put(BocInvt.BOCINVT_EVA_RISKBIRTHDAY_REQ,
//				(String) responseMap.get(BocInvt.BOCIEVA_BIRTHDAY_RES));
//		inputMap.put(BocInvt.BOCINVT_EVA_ADDRESS_REQ,
//				(String) responseMap.get(BocInvt.BOCIEVA_ADDRESS_RES));
//		inputMap.put(BocInvt.BOCINVT_EVA_PHONE_REQ,
//				(String) responseMap.get(BocInvt.BOCIEVA_PHONE_RES));
//		inputMap.put(BocInvt.BOCINVT_EVA_RISKMOBILE_REQ,
//				(String) responseMap.get(BocInvt.BOCIEVA_MOBILE_RES));
//		inputMap.put(BocInvt.BOCINVT_EVA_POSTCODE_REQ,
//				(String) responseMap.get(BocInvt.BOCIEVA_POSTCODE_RES));
//		inputMap.put(BocInvt.BOCINVT_EVA_RISKEMAIL_REQ,
//				(String) responseMap.get(BocInvt.BOCIEVA_EMAIL_RES));
//		inputMap.put(BocInvt.BOCINVT_EVA_REVENUE_REQ,
//				(String) responseMap.get(BocInvt.BOCIEVA_REVENUE_RES));
//		inputMap.put(BocInvt.BOCINVT_EVA_CUSTNATIONALITY_REQ,
//				(String) responseMap.get(BocInvt.BOCIEVA_CUSTNATIONALITY_RES));
//		inputMap.put(BocInvt.BOCINVT_EVA_EDUCATION_REQ,
//				(String) responseMap.get(BocInvt.BOCIEVA_EDUCATION_RES));
//		inputMap.put(BocInvt.BOCINVT_EVA_OCCUPATION_REQ,
//				(String) responseMap.get(BocInvt.BOCIEVA_OCCUPATION_RES));
//		inputMap.put(BocInvt.BOCINVT_EVA_HASINVESTEXPERIENCE_REQ,
//				(String) responseMap
//						.get(BocInvt.BOCIEVA_HASINVESTEXPERIENCE_RES));
//		BaseDroidApp.getInstanse().getBizDataMap()
//				.put(ConstantGloble.BOCINVT_REQUESTMAP, inputMap);
//
//		String status = (String) responseMap.get(BocInvt.BOCIEVA_STATUS_RES);
//
//		isInvtEva = !StringUtil.isNull(status)
//				&& status.equals(ConstantGloble.BOCINVT_EVA_SUC_STATUS);
//
//		requestBociAcctList("1", "0");
//	}

	/**
	 * 请求理财账户信息
	 * 
	 * @param acctSatus
	 * @param acctType
	 */
	public void requestBociAcctList(String acctSatus, String acctType) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.BOCINVTACCTINFO);
		Map<String, String> map = new Hashtable<String, String>();
		map.put(BocInvt.ACCOUNTSATUS, acctSatus);
		map.put(BocInvt.QUERYTYPE, acctType);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "bocinvtAcctCallback");
	}

	@SuppressWarnings("unchecked")
	public void bocinvtAcctCallback(Object resultObj) {
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(map)) {

			return;
		}
		List<Map<String, Object>> investBinding = (List<Map<String, Object>>) map
				.get(BocInvt.BOCI_LIST_RES);
		BociDataCenter.getInstance().setBocinvtAcctList(investBinding);

		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.BOCINVT_XPADRESET_CHOOSE, investBinding);

//		if (isOpenBocinvt && isInvtEva && investBinding != null
//				&& investBinding.size() > 0) {
//			return;
//		}
		if (isOpenBocinvt && investBinding != null
				&& investBinding.size() > 0) {
			if(getIntent().getBooleanExtra(ConstantGloble.COMEFROMFOOTFAST, true)){
				return;
			}
			if (getIntent().getBooleanExtra(ConstantGloble.COM_FROM_MAIN, false)){
				requestPsnXpadHoldProductQueryList("true");

			}
			return;
		}
		
		showHintDialog();

	}

	/**
	 * 显示提示用户完成理财服务的前置条件，即开通投资理财服务，风险评估，登记理财账户
	 */
	private void showHintDialog() {
		// 显示完成开通理财服务，风险评估，登记账户的页面
		List<Map<String, Object>> investBinding = BociDataCenter.getInstance()
				.getBocinvtAcctList();
		InflaterViewDialog inflater = new InflaterViewDialog(this);
//		View view = (RelativeLayout) inflater.judgeViewDialog(isOpenBocinvt,
//				investBinding, isInvtEva, manageOpenClick, invtBindingClick,
//				invtEvaluationClick, exitClick);
		dialogView=(RelativeLayout)inflater.judgeViewDialog_choice(isOpenBocinvt,
				investBinding, false, manageOpenClick,
				invtBindingClick, null, exitClick);
		TextView tv = (TextView) dialogView
				.findViewById(R.id.tv_acc_account_accountState);
		// 查询
		tv.setText(this.getString(R.string.bocinvt_tv_76));

		BaseDroidApp.getInstanse().showAccountMessageDialog(dialogView);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		BiiHttpEngine.showProgressDialogCanGoBack();
		requestPsnInvestmentisOpenBefore();
	}

	// 不要问这个变量干毛用的,拷贝过来的，鬼知道干毛用的
	boolean isTask;
	/** 登记账户监听事件 */
	protected OnClickListener invtBindingClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(!isOpenBocinvt){
				BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.isForex_task_only_open_title));
				return;
			}
			// 请求登记资金账户信息
			isTask = true;
			BaseHttpEngine.showProgressDialog();
			List<String> acctype = new ArrayList<String>();
			acctype.add("119");
			acctype.add("188");
			requestBankAcctList(acctype);
		}
	};
	/** 开通投资理财监听事件 */
	protected OnClickListener manageOpenClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 跳转到投资理财服务协议页面
			Intent gotoIntent = new Intent(MyInvetProductActivity.this,
					InvesAgreeActivity.class);
			startActivityForResult(gotoIntent,
					ConstantGloble.ACTIVITY_RESULT_CODE);
			overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
		}
	};

	/** 风险评估监听事件 */
//	protected OnClickListener invtEvaluationClick = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			Intent intent = new Intent(MyInvetProductActivity.this,
//					InvtEvaluationInputActivity.class);
//			startActivityForResult(intent,
//					ConstantGloble.ACTIVITY_REQUEST_INVTEVALUATION_CODE);
//			overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
//		}
//	};

	protected OnClickListener exitClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
			finish();
		}
	};

	/**
	 * 请求账户
	 * 
	 * @param acctype
	 */
	public void requestBankAcctList(List<String> acctype) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.QRY_ALL_BANK_ACCOUNT);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Comm.ACCOUNT_TYPE, acctype);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "bankAccListCallBack");
	}

	@SuppressWarnings("unchecked")
	public void bankAccListCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		List<Map<String, Object>> list = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(list)) {
			if (isTask) {
				CustomDialog.toastInCenter(this,
						getString(R.string.bocinvt_binding_relevance));
				return;
			}
			showNotiDialog();
			return;
		}
		if (StringUtil.isNullOrEmpty(BociDataCenter.getInstance()
				.getBocinvtAcctList())) {
			BociDataCenter.getInstance().setUnSetAcctList(list);
		} else {
			ArrayList<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
			StringBuffer b = new StringBuffer();
			for (Map<String, Object> map : BociDataCenter.getInstance()
					.getBocinvtAcctList()) {
				b.append(map.get(BocInvt.ACCOUNTNO));
			}
			for (int i = 0; i < list.size(); i++) {
				String allAcctNum = (String) list.get(i)
						.get(Comm.ACCOUNTNUMBER);
				if (!b.toString().contains(allAcctNum)) {
					newList.add(list.get(i));
				}
			}
			if (StringUtil.isNullOrEmpty(newList)) {
				showNotiDialog();
				return;
			}
			BociDataCenter.getInstance().setUnSetAcctList(newList);
		}
		startActivityForResult(
				new Intent(this, InvtBindingChooseActivity.class),
				ConstantGloble.ACTIVITY_REQUEST_INVTBINDING_CODE);
		overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
	}

	private void showNotiDialog() {
		BaseDroidApp.getInstanse().showErrorDialog(
				getString(R.string.bocinvt_binding_relevance), R.string.cancle,
				R.string.acc_myaccount_relevance_title, new OnClickListener() {
					@Override
					public void onClick(View v) {
						switch (Integer.parseInt(v.getTag() + "")) {
						case CustomDialog.TAG_SURE:
							BaseDroidApp.getInstanse().dismissErrorDialog();
//							startActivityForResult(
//									new Intent(
//											MyInvetProductActivity.this,
//											AccInputRelevanceAccountActivity.class),
//									ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
							BusinessModelControl.gotoAccRelevanceAccount(MyInvetProductActivity.this, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, null);
							break;
						case CustomDialog.TAG_CANCLE:
							BaseDroidApp.getInstanse().dismissErrorDialog();
							break;
						}
					}
				});
	}
	/**
	 * 隐藏右侧按钮
	 */
	public void gonerightBtn() {
		btn_right.setVisibility(View.GONE);
	}
	/**
	 * 请求持有产品查询
	 * 
	 * @param refresh
	 */
	public void requestPsnXpadHoldProductQueryList(String refresh) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.PRODUCTBALANCEQUERY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("issueType", "0");
		params.put(BocInvt.BOCI_REFRESH_REQ, refresh);
		biiRequestBody.setParams(params);
//		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnXpadHoldProductQueryListCallback");
	}
	/** 请求持有产品查询回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnXpadHoldProductQueryListCallback(Object resultObj) {
//		BaseHttpEngine.dissMissProgressDialog();
		List<Map<String, Object>> list = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(list)) {
			BaseHttpEngine.dissMissProgressDialog();
			ActivityTaskManager.getInstance().removeAllSecondActivity();
			Intent intent = new Intent(MyInvetProductActivity.this, QueryProductActivity.class);
			startActivity(intent);
		}else{
			Intent intent = new Intent(MyInvetProductActivity.this,MyProductListActivity.class);
			intent.putExtra("isFrommyinveet", false);
			startActivity(intent);
		}
	}
}
