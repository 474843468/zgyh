package com.chinamworld.bocmbci.biz.finc.myfund;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.adapter.MyFincMainAdapter;
import com.chinamworld.bocmbci.biz.finc.adapter.TotalValueAdapter;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.biz.finc.orcm.OrcmProductListActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.StringUtil;

/***
 * 我的基金 基金持仓首页
 * 
 * @author 宁焰红
 * 
 */
public class MyFincBalanceActivity extends FincBaseActivity {   
	
	private final String TAG = "MyFincBalanceActivity";
	/** 基金持仓主view */
	private View myFincView = null;
	// /** 风险等级 */
	// private TextView RiskEvaluationText = null;
	// /** 基金账户的View */
	// private View fincAcctView = null;
	// /** 基金账户 */
	// private TextView fincAccinfoText = null;
	/** 持仓信息 */
	private ListView balanceListView = null;
//	/** 基金账户详细信息 */
//	private List<Map<String, String>> accountDetaiList = null;
	private OnItemClickListener itermClickListener;
	private MyFincMainAdapter adapter;
	private List<Map<String, Object>> fundBalanceList;
	private Map<String, String> resultMap;
	private String errMsg;
    private String fundCode;
	// private float summation = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 初始化控件
		getIntentData();
		init();
		initOnClick();
		initData();
		// 查询风险评估等级
		// BaseHttpEngine.showProgressDialogCanGoBack();
		// requestPsnFundRiskEvaluationQueryResult();
//		BaseHttpEngine.showProgressDialogCanGoBack();
//		requestPsnFundQueryFundBalance();
	}
	
	private void getIntentData() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		errMsg = intent.getStringExtra(Finc.ERROR_MSG);
	}


	@Override
	protected void onResume() {
		super.onResume();
		if(StringUtil.isNullOrEmpty(fundBalanceList)){
			if(errMsg != null && !"".equals(errMsg)){
				BaseDroidApp.getInstanse().showInfoMessageDialog(errMsg);
				errMsg = null;
			}		
		}
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		tabcontent.setPadding(0, 0, 0, 0);
		myFincView = mainInflater.inflate(R.layout.finc_myfinc_balance_main,
				null);
		tabcontent.addView(myFincView);
		setTitle(getResources().getString(R.string.finc_myfinc_balance));
		initListHeaderView(R.string.finc_fundname,
				R.string.fincn_availableBalance, R.string.finc_netvalue);
		if(FincControl.myfincOperationFlag == FincControl.FINCINFO){
			//显示推荐基金链接
			if (StringUtil.isNullOrEmpty(fincControl.OcrmProductMap)
					|| StringUtil.isNullOrEmpty(fincControl.OcrmProductList)) {
			}else{
				findViewById(R.id.recommendation_link).setVisibility(View.VISIBLE);
				TextView text=(TextView)	findViewById(R.id.text_title2);
				text.setText(getString(R.string.finc_recommendation_link2));
			}
		}
		// fincAccinfoText = (TextView) findViewById(R.id.finc_accText);
		balanceListView = (ListView) findViewById(R.id.finc_listView);
		// fincAcctView = findViewById(R.id.accView);
		// RiskEvaluationText = (TextView) findViewById(R.id.finc_riskText);
		itermClickListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (!StringUtil.isNullOrEmpty(fundBalanceList)) {
					fincControl.fundDetails = fundBalanceList.get(position);
					
					fundCode=(String)fincControl.fundDetails.get(Finc.FINC_FUNDCODE);
					BaseHttpEngine.showProgressDialog();
					getFundDetailByFundCode(fundCode);
//					getFastSellFund(fundCode);
//					BaseHttpEngine.dissMissProgressDialog();
//					Intent intent = new Intent(MyFincBalanceActivity.this,
//							MyFincBalanceDetailActivity.class);
//					startActivityForResult(intent, 1);
				}

			}
		};
		right.setVisibility(View.VISIBLE);
		right.setText(getString(R.string.finc_myfinc_topText));
		// if (FincControl.is401) {
		// RiskEvaluationText.setVisibility(View.GONE);
		// fincAcctView.setVisibility(View.GONE);
		// }
	}

	
	
	@Override
	public void getFundDetailByFundCodeCallback(Object resultObj) {
		super.getFundDetailByFundCodeCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		fincControl.fincFundBasicDetails= (Map<String, Object>) biiResponseBody
				.getResult();
//		if (StringUtil.isNullOrEmpty(fincControl.fincFundBasicDetails)) {
//			BaseHttpEngine.dissMissProgressDialog();
//			return;
//		}
//		//如果是短期理财产品需要查询短期理财产品信息在下一个界面显示 fntype "15" 是短期理财产品
//		if("15".equals(fincControl.fincFundBasicDetails.get(Finc.FINC_FNTYPE))){
//			String fundCode =(String) fincControl.fincFundBasicDetails.get(Finc.FINC_FUNDCODE);
//			getFundFundDueDate(fundCode) ;
//			
//		}else{
		
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent(MyFincBalanceActivity.this,
				MyFincBalanceDetailActivity.class);
		startActivityForResult(intent, 1);
		
	
	}
	


	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ib_top_right_btn:
			showTotalVale();
			break;
		default:
			break;
		}
	}
	
	public void buttonOnclick(View v){
		BaseHttpEngine.showProgressDialog();
		requestCommConversationId();
	}
   /**
    * 基金快速赎回额度查询 回调处理
    */
	@Override
	public void getFastSellFundCallback(Object resultObj) {    
		// TODO Auto-generated method stub
		super.getFastSellFundCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		fincControl.fastSellFundDetails= (Map<String, Object>) biiResponseBody
				.getResult();
		getFincFund(fundCode);
	}
	
	   /**
	    * 基金基本信息查询  回调处理
	    */
		@Override
		public void getFincFundCallback(Object resultObj) {
			super.getFincFundCallback(resultObj);
			BiiResponse biiResponse = (BiiResponse) resultObj;
			List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
			BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
			fincControl.fincFundBasicDetails= (Map<String, Object>) biiResponseBody
					.getResult();
			
			BaseHttpEngine.dissMissProgressDialog();
			Intent intent = new Intent(MyFincBalanceActivity.this,
					MyFincBalanceDetailActivity.class);
			startActivityForResult(intent, 1);
			
		}
	
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPsnOcrmProductQuery("01", "", ConstantGloble.FOREX_PAGESIZE, "0", true);
	}
	
	@Override
	public void ocrmProductQueryCallBack(Object resultObj) {
		super.ocrmProductQueryCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(fincControl.OcrmProductMap)
				|| StringUtil.isNullOrEmpty(fincControl.OcrmProductList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.finc_query_noresult_error));
			return;
		}
		FincControl.isRecommend=true;
		startActivityForResult(new Intent(this, OrcmProductListActivity.class), 1);
	}

	private void initOnClick() {
		right.setOnClickListener(this);
		// fincAcctView.setOnClickListener(this);
		// RiskEvaluationText.setOnClickListener(this);
	}

	// /**
	// * 基金持仓--查询风险评估等级---回调
	// *
	// * @author 宁焰红 为RiskEvaluationText赋值
	// * @param resultObj
	// */
	// public void requestPsnFundRiskEvaluationQueryResultCallback(Object
	// resultObj) {
	// super.requestPsnFundRiskEvaluationQueryResultCallback(resultObj);
	// BiiResponse biiResponse = (BiiResponse) resultObj;
	// List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
	// // 得到response
	// BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
	// // 得到result
	// resultMap = (Map<String, String>) biiResponseBody
	// .getResult();
	// if (resultMap == null) {
	// BaseHttpEngine.dissMissProgressDialog();
	// return;
	// } else {
	// // String risk = resultMap.get(Finc.FINC_RISKLEVEL_RES);
	// // String riskLevel = LocalData.fincRiskLevelList.get(risk);
	// // RiskEvaluationText.setText(String.valueOf(riskLevel));
	// // 查询基金账户
	// requestQueryInvtBindingInfo();
	// }
	//
	// }

	// /**
	// * 查询基金账户---回调 为fincAccinfoText赋值
	// *
	// * @author 宁焰红
	// * @param resultObj
	// */
	// public void requestQueryInvtBindingInfoCallback(Object resultObj) {
	// super.requestQueryInvtBindingInfoCallback(resultObj);
	// BiiResponse biiResponse = (BiiResponse) resultObj;
	// List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
	// // 得到response
	// BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
	// // 得到result
	// fincControl.accDetailsMap = (Map<String, String>) biiResponseBody
	// .getResult();
	// BaseDroidApp.getInstanse().getBizDataMap()
	// .put(Finc.FINC_RESULT_RES, fincControl.accDetailsMap);
	// if (fincControl.accDetailsMap == null) {
	// BaseHttpEngine.dissMissProgressDialog();
	// return;
	// } else {
	// // 资金账号
	// fincControl.accNum = fincControl.accDetailsMap
	// .get(Finc.FINC_ACCOUNT_RES);
	// fincControl.accId = fincControl.accDetailsMap
	// .get(Finc.FINC_ACCOUNTID_RES);
	// fincAccinfoText.setText(StringUtil
	// .getForSixForString(fincControl.accNum));
	// // 查询基金持仓信息
	// BaseHttpEngine.showProgressDialog();
	// requestPsnFundQueryFundBalance();
	// }
	// }

	/**
	 * @author 宁焰红 基金持仓--查询基金持仓信息--回调 topValueText赋值
	 */
	@Override
	public void requestPsnFundQueryFundBalanceCallback(Object resultObj) {
		super.requestPsnFundQueryFundBalanceCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();

		BaseHttpEngine.dissMissProgressDialog();
		if (result == null) {
			// 用户没有持仓信息 系统自己报错
		} else {
			fincControl.fundBalancList = (List<Map<String, Object>>) result
					.get(Finc.FINC_FUNDBALANCE_REQ);
			initData();
		}
	}
	
	private void initData(){
		fundBalanceList = fincControl.fundBalancList;
		// 存储基金详细信息
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(Finc.FINC_FUNDBALANCE_REQ, fundBalanceList);
		if (fundBalanceList == null) {
			return;
		} else {
			// for (Map<String, Object> map : fundBalanceList) {
			// summation += Float.valueOf((String) map
			// .get(Finc.FINC_CURRENTCAPITALISATION_REQ));
			// }
			fincControl.fundBalancList = fundBalanceList;
			// 设置适配器
			adapter = new MyFincMainAdapter(this, fundBalanceList);
			balanceListView.setAdapter(adapter);
			balanceListView.setOnItemClickListener(itermClickListener);
		}
	}
	
 	private void clearData(){
		if (fundBalanceList != null && balanceListView != null
				&& adapter != null) {
			adapter = new MyFincMainAdapter(this,
					new ArrayList<Map<String, Object>>());
			balanceListView.setAdapter(adapter);
		}
	}

	// /**
	// * 根据账户标志，查询账户详细信息----回调
	// *
	// * @author 宁焰红
	// * @param resultObj
	// */
	// @Override
	// public void requestPsnAccountQueryAccountDetailCallback(Object resultObj)
	// {
	// BaseHttpEngine.dissMissProgressDialog();
	// super.requestPsnAccountQueryAccountDetailCallback(resultObj);
	// BiiResponse biiResponse = (BiiResponse) resultObj;
	// List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
	// // 得到response
	// BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
	// // 得到result
	// Map<String, Object> result = (Map<String, Object>) biiResponseBody
	// .getResult();
	// if (result == null) {
	// return;
	// } else {
	// accountDetaiList = (List<Map<String, String>>) result
	// .get(Finc.FINC_ACCOUNTDETAILLIST_RES);
	// if (accountDetaiList == null) {
	// return;
	// } else {
	// // 显示账户详细信息
	// showAccDetailInfo();
	// }
	// }
	//
	// }

	/** 显示累计市值 */
	private void showTotalVale() {
		if (!StringUtil.isNullOrEmpty(fundBalanceList)) {
			View totalvalueView = LayoutInflater.from(
					MyFincBalanceActivity.this).inflate(
					R.layout.finc_myfinc_totalvalue, null);
			// 累计市值显示的ListView
			ListView totalValueListView = (ListView) totalvalueView
					.findViewById(R.id.finc_totalValeListView);
			// 确定按钮
			ImageView exitIv = (ImageView) totalvalueView
					.findViewById(R.id.img_exit_accdetail);

			// 加载适配器
			initListHeaderView(totalvalueView, R.string.finc_title_jybz,
					R.string.finc_myfinc_topText);

			totalValueListView.setAdapter(new TotalValueAdapter(this,
					fundBalanceList));
			totalValueListView.setDividerHeight(0);
			
			LogGloble.d(TAG, "fundBalanceList" + fundBalanceList.size());
			exitIv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					BaseDroidApp.getInstanse().dismissMessageDialog();
				}
			});
			BaseDroidApp.getInstanse().showAccountMessageDialog(totalvalueView);
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog("当前账户没有持仓信息");
		}
	}

	//
	// /**
	// * 点击投资基金，显示账户详细信息 加载新布局
	// */
	// public void showAccDetailInfo() {
	// View accDetailInfoView = LayoutInflater
	// .from(MyFincBalanceActivity.this).inflate(
	// R.layout.finc_myfinc_balance_accset, null);
	// // 基金账户
	// TextView accIdText = (TextView) accDetailInfoView
	// .findViewById(R.id.finc_accountId);
	// // 基金账户详细信息
	// ListView accListView = (ListView) accDetailInfoView
	// .findViewById(R.id.finc_accListView);
	// // 重设按钮
	// Button resetButton = (Button) accDetailInfoView
	// .findViewById(R.id.finc_accSetButton);
	// ImageView closeBtn = (ImageView)
	// accDetailInfoView.findViewById(R.id.img_exit_accdetail);
	// closeBtn.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// BaseDroidApp.getInstanse().dismissMessageDialog();
	// }
	// });
	// String funcAccNum = fincControl.accNum;
	// if (!StringUtil.isNullOrEmpty(fincControl.accNum)) {
	// accIdText.setText(StringUtil.getForSixForString(funcAccNum));
	// }
	//
	// if (accountDetaiList == null) {
	// return;
	// } else {
	// // 加载适配器
	// accListView.setAdapter(new MyFincBalanceAccAdapter(
	// MyFincBalanceActivity.this, accountDetaiList));
	// }
	// resetButton.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // 跳转到账户设置页面
	// Intent intent = new Intent(MyFincBalanceActivity.this,
	// MyFincBalanceResetAccSubmitActivity.class);
	// startActivityForResult(intent,
	// ConstantGloble.ACTIVITY_REQUEST_SETFINCSACC_CODE);
	// BaseDroidApp.getInstanse().dismissMessageDialog();
	// }
	// });
	// BaseDroidApp.getInstanse().showAccountMessageDialog(accDetailInfoView);
	// }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// switch (requestCode) {
		// case ConstantGloble.ACTIVITY_REQUEST_SETFINCSACC_CODE:
		// switch (resultCode) {
		// case RESULT_OK:
		// BaseHttpEngine.showProgressDialogCanGoBack();
		// requestPsnFundRiskEvaluationQueryResult();
		// break;
		//
		// default:
		// showAccDetailInfo();
		// break;
		// }
		// break;

		// default:
		switch (resultCode) {
		case RESULT_OK:
			BaseHttpEngine.showProgressDialogCanGoBack();
			clearData();
			requestPsnFundQueryFundBalance();
			break;

		default:
			break;
		}
		// break;
		// }
	}
	//调用Finc.METHOD_PSNOCRMPRODUCTQUERY接口的时候，报错也好、要继续进行,
		@Override
		public boolean httpRequestCallBackPre(Object resultObj) {
			BiiResponse biiResponse = (BiiResponse) resultObj;
			List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
			BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
			if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
				 if(Finc.METHOD_PSNOCRMPRODUCTQUERY.equals(biiResponseBody.getMethod())) {
					BiiHttpEngine.dissMissProgressDialog();
					BiiError biiError = biiResponseBody.getError();
					// 判断是否存在error
					if (biiError != null) {
						findViewById(R.id.recommendation_link).setVisibility(View.GONE);
						if (StringUtil.isNullOrEmpty(fincControl.OcrmProductMap)
								|| StringUtil.isNullOrEmpty(fincControl.OcrmProductList)) {
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									getString(R.string.finc_query_noresult_error));
							return true;
						}
						FincControl.isRecommend=true;
						startActivityForResult(new Intent(this, OrcmProductListActivity.class), 1);
						if (biiError.getCode() != null) {
							if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
								showTimeOutDialog(biiError.getMessage());
//								BaseDroidApp.getInstanse().showMessageDialog(
//										biiError.getMessage(),
//										new OnClickListener() {
//											@Override
//											public void onClick(View v) {
//												BaseDroidApp.getInstanse()
//														.dismissErrorDialog();
//												ActivityTaskManager
//														.getInstance()
//														.removeAllSecondActivity();
////												Intent intent = new Intent();
////												intent.setClass(MyFincBalanceActivity.this,
////														LoginActivity.class);
////												startActivityForResult(
////														intent,
////														ConstantGloble.ACTIVITY_RESULT_CODE);
//												BaseActivity.getLoginUtils(MyFincBalanceActivity.this).exe(new LoginTask.LoginCallback() {
//
//													@Override
//													public void loginStatua(boolean isLogin) {
//                                                        if(isLogin){
//															BaseHttpEngine.showProgressDialogCanGoBack();
//															clearData();
//															requestPsnFundQueryFundBalance();
//														}
//													}
//												});
//											}
//										});
							}
						}else {
//							largest_exchange_layout.setVisibility(View.VISIBLE);
						}
					}
			
//					initViewInfos();
//					BaseDroidApp.getInstanse().createDialog("",
//							biiError.getMessage(), new OnClickListener() {
//								@Override
//								public void onClick(View v) {
//									BaseDroidApp.getInstanse()
//											.dismissErrorDialog();
//									
//								}
//							});
					return true;
				 }
			}
			return super.httpRequestCallBackPre(resultObj);
		}


}
