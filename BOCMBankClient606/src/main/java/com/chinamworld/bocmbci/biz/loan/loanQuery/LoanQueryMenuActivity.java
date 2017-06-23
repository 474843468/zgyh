package com.chinamworld.bocmbci.biz.loan.loanQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.loan.LoanAccountListActivity;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 贷款查询三级菜单
 * 
 * @author wanbing
 * 
 */
public class LoanQueryMenuActivity extends LoanBaseActivity {

	private Context context = this;
	private LinearLayout tabcontent;// 主Activity显示
	private View view = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.loan_left_one1);
		Button btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setVisibility(View.GONE);
		// 添加布局
		setLeftSelectedPosition("loan_1");
		init();
	}

	private void init() {
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		view = mInflater.inflate(R.layout.loan_query_menu, null);
		tabcontent.addView(view);
		// 我的贷款
		LinearLayout myloanView = (LinearLayout) findViewById(R.id.finc_allprices_imagebtn);
		myloanView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent(context, LoanAccountListActivity.class);
				startActivity(it);
			}
		});
		LinearLayout llyt_loan_use_query = (LinearLayout) findViewById(R.id.llyt_loan_use_query);
		// 用款查询
		llyt_loan_use_query.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				BaseHttpEngine.showProgressDialog();
				requestLoanList();

			}
		});
		// 额度查询
		LinearLayout edView = (LinearLayout) findViewById(R.id.ed_searchView);
		edView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseHttpEngine.showProgressDialog();
				requestPsnLOANQuotaQuery();
			}
		});
	}

	/** 额度查询 */
	private void requestPsnLOANQuotaQuery() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_PSNLOANQUOTAQUERY_API);
		biiRequestBody.setConversationId(null);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Loan.LOAN_ACTTYPE_REQ, ConstantGloble.LOAN_ACTTYPE);
		map.put(Loan.LOAN_QUERYTYPE_REQ, ConstantGloble.LOAN_QUERYTYPE);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnLOANQuotaQueryCallback");
	}

	public void requestPsnLOANQuotaQueryCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, String>> result = (List<Map<String, String>>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_no_limit_null));
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.LOAN_QUARY_RESULT, result);
		Intent intent = new Intent(LoanQueryMenuActivity.this, LoanQuotaQueryActivity.class);
		startActivity(intent);
		BaseHttpEngine.dissMissProgressDialog();
	}

	/** 用款查询------ 请求贷款账户列表信息 */
	public void requestLoanList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_ACCOUNT_LIST_API);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Loan.LOANACC_E_LOAN_STATE, "10");
		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "loanAccountListCallBack");
	}

	/**
	 * 请求贷款账户列表回调
	 * 
	 * @param resultObj
	 */
	public void loanAccountListCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框
		Map<String, Object> loanlistmap = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(loanlistmap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		List<Map<String, String>> resultList = (List<Map<String, String>>) (loanlistmap.get(Loan.LOANACC_LOAN_LIST_RES));
		if (resultList == null || resultList.size() == 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent(context, LoanUseQueryAccountActivity.class);
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.LOAN_USELOAN_RESULTLIST, resultList);
		startActivity(intent);
	}
	
	//TODO 拦截错误信息
	public boolean doBiihttpRequestCallBackPre(BiiResponse response) {
		List<BiiResponseBody> biiResponseBodyList = response.getResponse();
	
		if (!StringUtil.isNullOrEmpty(biiResponseBodyList)) {
			for (BiiResponseBody body : biiResponseBodyList) {
				if (!ConstantGloble.STATUS_SUCCESS.equals(body.getStatus())) {
					// 消除通信框
					BaseHttpEngine.dissMissProgressDialog();
					if (Login.LOGOUT_API.equals(body.getMethod())) {// 退出的请求
						return false;
					}
					BiiError biiError = body.getError();
					// 判断是否存在error
					if (biiError != null) {
						//过滤错误
						//LocalData.Code_Error_Message.errorToMessage(body);
						if(Loan.LOAN_ACCOUNT_LIST_API.equals(body.getMethod())){
							if("validation.no.relating.acc".equals(biiError.getCode())){
								biiError.setMessage("暂未查询到您名下相关贷款用款信息，请前往中行网点或咨询95566.");
							}
						}
						
						
						
						if (biiError.getCode() != null) {
							if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
																						// 要重新登录
								// TODO 错误码是否显示"("+biiError.getCode()+") "
								showTimeOutDialog(biiError.getMessage());
//
//								BaseDroidApp.getInstanse().showMessageDialog(biiError.getMessage(),
//										new OnClickListener() {
//
//											@Override
//											public void onClick(View v) {
//												BaseDroidApp.getInstanse().dismissErrorDialog();
//												ActivityTaskManager.getInstance().removeAllActivity();
////												Intent intent = new Intent();
////												intent.setClass(LoanQueryMenuActivity.this, LoginActivity.class);
////												startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
//												BaseActivity.getLoginUtils(LoanQueryMenuActivity.this).exe(new LoginTask.LoginCallback() {
//
//													@Override
//													public void loginStatua(boolean isLogin) {
//
//													}
//												});
//											}
//										});
							} else {// 非会话超时错误拦截
								BaseDroidApp.getInstanse().createDialog(biiError.getCode(), biiError.getMessage(),
										new OnClickListener() {

											@Override
											public void onClick(View v) {
												BaseDroidApp.getInstanse().dismissErrorDialog();
												if (BaseHttpEngine.canGoBack) {
													finish();
													BaseHttpEngine.canGoBack = false;
												}
											}
										});
							}
							return true;
						}
						// 弹出公共的错误框
						BaseDroidApp.getInstanse().dismissErrorDialog();
						BaseDroidApp.getInstanse().createDialog("", biiError.getMessage(), new OnClickListener() {
							@Override
							public void onClick(View v) {
								BaseDroidApp.getInstanse().dismissErrorDialog();
								if (BaseHttpEngine.canGoBack) {
									finish();
									BaseHttpEngine.canGoBack = false;
								}
							}
						});
					} else {
						BaseDroidApp.getInstanse().dismissErrorDialog();
						// 避免没有错误信息返回时给个默认的提示
						BaseDroidApp.getInstanse().createDialog("", getResources().getString(R.string.request_error),
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										BaseDroidApp.getInstanse().dismissErrorDialog();
										if (BaseHttpEngine.canGoBack) {
											finish();
											BaseHttpEngine.canGoBack = false;
										}
									}
								});
					}

					return true;
				}
			}
		}

		return false;
	}
}
