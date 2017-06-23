package com.chinamworld.bocmbci.biz.finc.myfund;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.biz.finc.fundprice.FundPricesActivityNew;
import com.chinamworld.bocmbci.biz.invest.constant.InvestConstant;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.ErrorCode;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.LoginTask.*;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 我的基金首页
 * 
 * @author 宁焰红
 * 
 */
public class MyFincMainActivity extends FincBaseActivity {
	private final String TAG = "MyFincMainActivity";
	/** 我的基金主view */
	private View myFincView = null;
	/** 基金持仓 */
	/** 我关注的基金 */
	/** 基金持仓View */
	private View fincInfoView = null;
	/** 基金转换View */
	private View fincTransView = null;
	/** 设置分红View */
	private View setBoundsView = null;
	/** 浮动盈亏测算　 */
	private View floatProfitAndLoss;
	/** 我的基金持仓 */
	private static final int FINCINFO = 1;
	/** floatProfitAndloss 浮动盈亏测算 */
	private static final int FLOATPL = 2;
	/** 基金转换 */
	private static final int FINCTRANS = 3;
	/** 设置分红 */
	private static final int SETBOUNDS = 4;
	/** 基金对账单 */
	private static final int FUNDSTATEMENTBALANCE = 5;
	/** 错误信息 */
	private String errMsg;
	private  String isFdyk ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		/**首页9宫格点击过来*/
		if(getIntent().getBooleanExtra(ConstantGloble.COM_FROM_MAIN, false)){
			fincInfoView.performClick();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		fincControl.cleanAllData();
		setLeftSelectedPosition("finc_2");
	}

	private void init() {
		myFincView = mainInflater.inflate(R.layout.finc_myfinc_main, null);
		tabcontent.addView(myFincView);
		setTitle(getResources().getString(R.string.forex_myfinc));
		fincInfoView = findViewById(R.id.layout1);
		fincTransView = findViewById(R.id.layout2);
		setBoundsView = findViewById(R.id.layout3);
		floatProfitAndLoss = findViewById(R.id.finc_FloatProfitAndLoss_ll);
		fincInfoView.setOnClickListener(this);
		fincTransView.setOnClickListener(this);
		setBoundsView.setOnClickListener(this);
		floatProfitAndLoss.setOnClickListener(this);
		right.setVisibility(View.GONE);
	}

	@Override
	public void requestPsnFundQueryFundBalanceCallback(Object resultObj) {
		super.requestPsnFundQueryFundBalanceCallback(resultObj);
		if(!fincControl.ifhaveaccId || !fincControl.ifInvestMent) return;
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		/**
		 * PsnFundQueryFund查询基金持仓信息
		 */
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		if (result == null || StringUtil.isNullOrEmpty(((List<Map<String,Object>>) result
				.get(Finc.FINC_FUNDBALANCE_REQ))) == true) {
			Intent intent = getIntent();
			fincControl.fundBalancList = null;
			if(intent!=null&&intent.getBooleanExtra(ConstantGloble.COM_FROM_MAIN, false)){
				/**P601改造  基金持仓没有信息直接跳到基金行情*/
				fincControl.cleanAllData();
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				startActivity(new Intent(this, FundPricesActivityNew.class));
				setLeftSelectedPosition("finc_1");
				return;
			}
		} else {
			List<Map<String, Object>>fundBalancList = (List<Map<String,Object>>) result
					.get(Finc.FINC_FUNDBALANCE_REQ);
			Map<String, Object> fundBalancList_new = (Map<String, Object>) fundBalancList.get(0)
					.get(Finc.FINC_FUNDINFO_REQ);
			isFdyk=(String) fundBalancList_new.get("isFdyk");
			fincControl.fundBalancList = (List<Map<String, Object>>) result
					.get(Finc.FINC_FUNDBALANCE_REQ);
		}
//			if(StringUtil.isNullOrEmpty(fincControl.fundBalancList)){
//				BaseDroidApp.getInstanse().showInfoMessageDialog(
//						getString(R.string.finc_query_noresult_error));
//				return;
//			}
			switch (httpFlag) {
			case FINCINFO:// 基金持仓
				FincControl.myfincOperationFlag = FincControl.FINCINFO;
				requestCommConversationId();
//				startActivity(new Intent(this, MyFincBalanceActivity.class));
				break;
			case FINCTRANS:// 基金转换
				isFundBalancListEmpty();
				BaseHttpEngine.dissMissProgressDialog();
				FincControl.myfincOperationFlag = FincControl.FINCTRANS;
				startActivity(new Intent(this, MyFincBalanceActivity.class));
				break;
			case SETBOUNDS:// 设置分红方式
				isFundBalancListEmpty();
				BaseHttpEngine.dissMissProgressDialog();
				FincControl.myfincOperationFlag = FincControl.SETBOUNDS;
				startActivity(new Intent(this, MyFincBalanceActivity.class));
				break;
			case FLOATPL:
				isFundBalancListEmpty();
				BaseHttpEngine.dissMissProgressDialog();
				if(isFdyk.equals("N")){
					LogGloble.e("asd", "+++aaaasddas");
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							MyFincMainActivity.this.getResources()
									.getString(R.string.not_enough_project));
					finish();
				}
				startActivity(new Intent(this, FinFdykcActivity.class));
				break;
			case FUNDSTATEMENTBALANCE:
				isFundBalancListEmpty();
				BaseHttpEngine.dissMissProgressDialog();
				startActivity(new Intent(this, FincFundStatementBalanceActivity.class));
				break;
			default:
				break;
			}

	}
	
	
	
	private void isFundBalancListEmpty() {
		if(StringUtil.isNullOrEmpty(fincControl.fundBalancList)){
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.finc_query_noresult_error));
			return;
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPsnOcrmProductQuery("01", "", ConstantGloble.FOREX_PAGESIZE, "0", true);
	}
	
	@Override
	public void ocrmProductQueryCallBack(Object resultObj) {
		super.ocrmProductQueryCallBack(resultObj);
		if(StringUtil.isNullOrEmpty(fincControl.fundBalancList)){
			if(StringUtil.isNullOrEmpty(fincControl.OcrmProductList)){
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getString(R.string.finc_query_noresult_error));
				return;
			}else{
				openActivity();
			}

		}else{
			openActivity();

		}

	}

	private void openActivity() {
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent(this, MyFincBalanceActivity.class);
		if(errMsg != null && !"".equals(errMsg)){
			intent.putExtra(Finc.ERROR_MSG, errMsg);
		}
		startActivity(intent);
	}

	@Override
	public void doCheckRequestQueryInvtBindingInfoCallback(
			Object resultObj) {
		super.doCheckRequestQueryInvtBindingInfoCallback(resultObj);
		if(!fincControl.ifhaveaccId || !fincControl.ifInvestMent) return;
			if(httpFlag == FUNDSTATEMENTBALANCE){
				BaseHttpEngine.dissMissProgressDialog();
				startActivity(new Intent(this, FincFundStatementBalanceActivity.class));
			} else {
				requestPsnFundQueryFundBalance();
			}
			// switch (httpFlag) {
			// case FINCINFO:// 基金持仓
			// // requestPsnFundQueryFundBalance();
			// BaseHttpEngine.dissMissProgressDialog();
			// startActivity(new Intent(this, MyFincBalanceActivity.class));
			// break;
			// case FLOATPL:
			// requestPsnFundQueryFundBalance();
			// break;
			// default:
			// break;
			// }
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ConstantGloble.ACTIVITY_REQUEST_MANAGE_CODE:// 开通理财服务
			switch (resultCode) {
			case RESULT_OK:
				fincControl.ifInvestMent = true;
				if (!fincControl.ifhaveaccId) {// 如果还没有基金账户 getPopup();
					getPopup();
				}
				break;

			default:
				fincControl.ifInvestMent = false;
				getPopup();
				break;
			}
			break;
		case ConstantGloble.ACTIVITY_REQUEST_SETFINCSACC_CODE:// 开通基金账户
			switch (resultCode) {
			case RESULT_OK:
				fincControl.ifhaveaccId = true;
				switch (httpFlag) {
				case FINCINFO:// 基金持仓
					requestPsnFundQueryFundBalance();
					break;
				case FLOATPL:
					requestPsnFundQueryFundBalance();
					break;
				default:
					break;
				}
				break;

			default:
				fincControl.ifhaveaccId = false;
				getPopup();
				break;
			}
			break;
		case InvestConstant.FUNDRISK:// 基金风险评估
			switch (resultCode) {
			case RESULT_OK:
				fincControl.ifdorisk = true;
				switch (httpFlag) {
				case FINCINFO:// 基金持仓
					requestPsnFundQueryFundBalance();
					break;
				case FLOATPL:
					requestPsnFundQueryFundBalance();
					break;
				default:
					break;
				}
				break;
			default:
				fincControl.ifdorisk = false;
				getPopup();
				break;
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.layout1:// 基金持仓
			if (!BaseDroidApp.getInstanse().isLogin()) {// 没有登录就跳转到登录页面
//				Intent intent = new Intent(MyFincMainActivity.this,
//						LoginActivity.class);
//				startActivityForResult(intent,
//						ConstantGloble.ACTIVITY_RESULT_CODE);
				BaseActivity.getLoginUtils(MyFincMainActivity.this).exe(new LoginCallback() {

					@Override
					public void loginStatua(boolean isLogin) {

					}
				});
				return;
			}
			httpFlag = FINCINFO;
			BaseHttpEngine.showProgressDialog();
			doCheckRequestPsnInvestmentManageIsOpen();
			break;
		case R.id.layout2:// 基金转换
			if (!BaseDroidApp.getInstanse().isLogin()) {// 没有登录就跳转到登录页面
//				Intent intent = new Intent(MyFincMainActivity.this,
//						LoginActivity.class);
//				startActivityForResult(intent,
//						ConstantGloble.ACTIVITY_RESULT_CODE);
				BaseActivity.getLoginUtils(MyFincMainActivity.this).exe(new LoginCallback() {

					@Override
					public void loginStatua(boolean isLogin) {

					}
				});
				return;
			}
			httpFlag = FINCTRANS;
			BaseHttpEngine.showProgressDialog();
			doCheckRequestPsnInvestmentManageIsOpen();
			break;
		case R.id.layout3:// 设置分红
			if (!BaseDroidApp.getInstanse().isLogin()) {// 没有登录就跳转到登录页面
//				Intent intent = new Intent(MyFincMainActivity.this,
//						LoginActivity.class);
//				startActivityForResult(intent,
//						ConstantGloble.ACTIVITY_RESULT_CODE);
				BaseActivity.getLoginUtils(MyFincMainActivity.this).exe(new LoginCallback() {

					@Override
					public void loginStatua(boolean isLogin) {

					}
				});
				return;
			}
			httpFlag = SETBOUNDS;
			BaseHttpEngine.showProgressDialog();
			doCheckRequestPsnInvestmentManageIsOpen();
			break;

		case R.id.finc_FloatProfitAndLoss_ll: // 浮动盈亏测算
			if (!BaseDroidApp.getInstanse().isLogin()) {// 没有登录就跳转到登录页面
//				Intent intent = new Intent(MyFincMainActivity.this,
//						LoginActivity.class);
//				startActivityForResult(intent,
//						ConstantGloble.ACTIVITY_RESULT_CODE);
				BaseActivity.getLoginUtils(MyFincMainActivity.this).exe(new LoginCallback() {

					@Override
					public void loginStatua(boolean isLogin) {

					}
				});
				return;
			}
			httpFlag = FLOATPL;
			BaseHttpEngine.showProgressDialog();
			doCheckRequestPsnInvestmentManageIsOpen();
			break;
		case R.id.finc_myfinc_fundStatementBalance: // 基金对账单
			if (!BaseDroidApp.getInstanse().isLogin()) {// 没有登录就跳转到登录页面
//				Intent intent = new Intent(MyFincMainActivity.this,
//						LoginActivity.class);
//				startActivityForResult(intent,
//						ConstantGloble.ACTIVITY_RESULT_CODE);
				BaseActivity.getLoginUtils(MyFincMainActivity.this).exe(new LoginCallback() {

					@Override
					public void loginStatua(boolean isLogin) {

					}
				});
				return;
			}
			httpFlag = FUNDSTATEMENTBALANCE;
			BaseHttpEngine.showProgressDialog();
			doCheckRequestPsnInvestmentManageIsOpen();
			break;
		default:
			break;
		}
	}

	@Override
	public void getAttentionedFundCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.getAttentionedFundCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.finc_query_noresult_error));
			return;
		} else {
			fincControl.attentionFundList = resultList;
			startActivity(new Intent(MyFincMainActivity.this,
					MyFincFollowActivity.class));
		}

	}

	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		super.httpRequestCallBackPre(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
			if (biiResponseBody.getError().getCode()
					.equals(ErrorCode.FINC_ACCCHECIN_ERROR)
					|| biiResponseBody.getError().getCode()
							.equals(ErrorCode.FINC_ACCNO_ERROR)||biiResponseBody.getError().getCode()
							.equals(ErrorCode.FINC_ACCNO_ERROR_2)) {
				fincControl.ifhaveaccId = false;
				fincControl.ifdorisk = false;
				BaseHttpEngine.dissMissProgressDialog();
				getPopup();
				return true;
			}

		}else if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
			 if(Finc.METHOD_PSNOCRMPRODUCTQUERY.equals(biiResponseBody.getMethod())) {
				BiiHttpEngine.dissMissProgressDialog();
				BiiError biiError = biiResponseBody.getError();
				// 判断是否存在error
				if (biiError != null) {
					findViewById(R.id.recommendation_link).setVisibility(View.GONE);
					
					if (biiError.getCode() != null) {
						if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
							showTimeOutDialog(biiError.getMessage());
//							BaseDroidApp.getInstanse().showMessageDialog(
//									biiError.getMessage(),
//									new OnClickListener() {
//										@Override
//										public void onClick(View v) {
//											BaseDroidApp.getInstanse()
//													.dismissErrorDialog();
//											ActivityTaskManager
//													.getInstance()
//													.removeAllSecondActivity();
////											Intent intent = new Intent();
////											intent.setClass(MyFincMainActivity.this,
////													LoginActivity.class);
////											startActivityForResult(
////													intent,
////													ConstantGloble.ACTIVITY_RESULT_CODE);
//											BaseActivity.getLoginUtils(MyFincMainActivity.this).exe(new LoginCallback() {
//
//												@Override
//												public void loginStatua(boolean isLogin) {
//
//												}
//											});
//										}
//									});
						}
					}
				}
			 }
		}
		return super.httpRequestCallBackPre(resultObj);

	}
	
	@Override
	public boolean doBiihttpRequestCallBackPre(BiiResponse response) {
		
		List<BiiResponseBody> biiResponseBodyList = response.getResponse();
//		List<BiiResponseBody> biiResponseBodys = response.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodyList.get(0);
		if (!StringUtil.isNullOrEmpty(biiResponseBodyList)) {
			for (BiiResponseBody body : biiResponseBodyList) {

				if (Finc.FINC_FUNDQUERYFUNDBALANCE_API.equals(body.getMethod())) {

					if (!ConstantGloble.STATUS_SUCCESS.equals(body.getStatus())) {
						if(httpFlag == FLOATPL){
							body.getError().setMessage("您当前未持有可进行浮动盈亏试算的基金");
							return super.doBiihttpRequestCallBackPre(response);
						}else if(httpFlag == FINCINFO){
							// 消除通信框
							BaseHttpEngine.dissMissProgressDialog();
							BiiError error = body.getError();
							if(error != null){
								errMsg = error.getMessage();
							}
							return false;     
						}else{
							return super.doBiihttpRequestCallBackPre(response);
						}
					}
				} else if(Finc.METHOD_PSNOCRMPRODUCTQUERY.equals(biiResponseBody.getMethod())) {
					BiiHttpEngine.dissMissProgressDialog();
					BiiError biiError = biiResponseBody.getError();
					// 判断是否存在error
					if (biiError != null && biiError.getCode() != null) {
//						largest_exchange_layout.setVisibility(View.GONE);
						
							if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
								return super.doBiihttpRequestCallBackPre(response);
							}
							if(StringUtil.isNullOrEmpty(fincControl.fundBalancList)){
								if(StringUtil.isNullOrEmpty(fincControl.OcrmProductList)){
									BaseDroidApp.getInstanse().showInfoMessageDialog(
											getString(R.string.finc_query_noresult_error));
								}else{
									openActivity();
								}
							}else{
								openActivity();
							
							}
							return true;
						}
				}
			} }else{
					return super.doBiihttpRequestCallBackPre(response);
				}
		


		return false;
	}
	

	
}
