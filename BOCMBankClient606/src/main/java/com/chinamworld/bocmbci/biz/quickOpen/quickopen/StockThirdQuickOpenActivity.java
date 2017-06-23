package com.chinamworld.bocmbci.biz.quickOpen.quickopen;

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
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.QuickOpen;
import com.chinamworld.bocmbci.biz.drawmoney.remitout.RemitOutListAdapter;
import com.chinamworld.bocmbci.biz.quickOpen.QuickOpenDataCenter;
import com.chinamworld.bocmbci.biz.quickOpen.StockThirdQuickOpenBaseActivity;
import com.chinamworld.bocmbci.biz.quickOpen.openQuery.QuickOpenResultActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 中银国际证券开户/查询选卡页
 * 
 * @author Zhi
 */
public class StockThirdQuickOpenActivity extends StockThirdQuickOpenBaseActivity implements OnClickListener{

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 账户列表控件 */
	private ListView lvCardList;
	/** 列表adapter */
	private RemitOutListAdapter remitListAdapter;
	/** 当前选中账户的位置 */
	private int selectPosition = -1;
	/** 下一步按钮 */
	private Button btnNext;
	/** 标识开户还是进度查询 true-进度查询 false-开户 */
	private boolean openOrQuery = false;
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.drawmoney_remitout_list);
		openOrQuery = getIntent().getBooleanExtra(ISQUERY, false);
		if (openOrQuery) {
			((TextView) findViewById(R.id.tv_service_title)).setText(getResources().getString(R.string.quickOpen_query_cardTip));
			setTitle(R.string.quickOpen_query_title);
			setTopRightGone();
		} else {
			((TextView) findViewById(R.id.tv_service_title)).setText(getResources().getString(R.string.quickOpen_open_cardTip));
			setTitle(R.string.quickOpen_title_open);
		}
		if (StringUtil.isNullOrEmpty(QuickOpenDataCenter.getInstance().getListCommonQueryAllChinaBankAccount())) {
			BaseHttpEngine.showProgressDialogCanGoBack();
			Map<String, Object> params = new HashMap<String, Object>();
			List<String> accountTypeList = new ArrayList<String>();
			accountTypeList.add("119");
			params.put(Comm.ACCOUNT_TYPE, accountTypeList);
			requestHttp(Comm.QRY_ALL_BANK_ACCOUNT, "requestPsnCommonQueryAllChinaBankAccountCallBack", params, false);
		} else {
			initView();
		}
	}
	
	/**初始化页面 */
	private void initView() {
		lvCardList = (ListView) findViewById(R.id.remit_choose_cardlist);
		setListView(QuickOpenDataCenter.getInstance().getListCommonQueryAllChinaBankAccount());
		lvCardList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				/**ListView点击选中，再点击取消*/
				if (selectPosition == position) {
					selectPosition = -1;
					remitListAdapter.setSelectedPosition(selectPosition);
				} else {
					selectPosition = position;
					remitListAdapter.setSelectedPosition(selectPosition);
				}
			}
		});
		btnNext = (Button) findViewById(R.id.sureButton);
		// 根据标识判断下一步按钮注册哪个监听
		if (openOrQuery) {
			setTopLeftListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					QuickOpenDataCenter.getInstance().clearAllData();
					finish();
				}
			});
			btnNext.setText(getResources().getString(R.string.query));
			btnNext.setOnClickListener(queryListener);
		} else {
			btnNext.setText(getResources().getString(R.string.next));
			btnNext.setOnClickListener(this);
		}
	}
	
	/**
	 * 填充列表数据
	 * 
	 * @param accountList
	 *            卡列表数据
	 */
	private void setListView(List<Map<String, Object>> accountList) {
		if (remitListAdapter == null) {
			remitListAdapter = new RemitOutListAdapter(this, accountList);
			lvCardList.setAdapter(remitListAdapter);
		} else {
			remitListAdapter.setData(accountList);
		}
	}
	
	@Override
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
						if (biiError.getCode() != null) {
							if (LocalData.timeOutCode.contains(biiError.getCode())) {
								// 表示回话超时
								// 要重新登录
								// 错误码是否显示"("+biiError.getCode()+") "
								showTimeOutDialog(biiError.getMessage());
//								BaseDroidApp.getInstanse().showMessageDialog(biiError.getMessage(),
//										new OnClickListener() {
//
//											@Override
//											public void onClick(View v) {
//												BaseDroidApp.getInstanse().dismissErrorDialog();
//												ActivityTaskManager.getInstance().removeAllSecondActivity();
////												Intent intent = new Intent();
////												intent.setClass(StockThirdQuickOpenActivity.this, LoginActivity.class);
////												startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
//												BaseActivity.getLoginUtils(StockThirdQuickOpenActivity.this).exe(new LoginTask.LoginCallback() {
//
//													@Override
//													public void loginStatua(boolean isLogin) {
//
//													}
//												});
//											}
//										});

							} else if (biiError.getCode().equals("TPCC.M8")) {
								Intent intent = new Intent(this, QuickOpenResultActivity.class).putExtra(ISSUCCESS, false);
								startActivityForResult(intent, 4);
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
						BaseDroidApp.getInstanse().createDialog("", getResources().getString(R.string.request_error), new OnClickListener() {
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 4) {
			setResult(4);
			finish();
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------控件事件-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onClick(View v) {
		if (selectPosition < 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请选择银行账户");
		} else {
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
		}
	}
	
	/** 开户进度查询的页面状态下下一步按钮监听 */
	OnClickListener queryListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (selectPosition < 0) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("请选择银行账户");
			} else {
				BaseHttpEngine.showProgressDialog();
				Map<String, Object> params = new HashMap<String, Object>();
				params.put(QuickOpen.STOCKACCOUNTNO, QuickOpenDataCenter.getInstance().getListCommonQueryAllChinaBankAccount().get(selectPosition).get(Comm.ACCOUNTNUMBER));
				requestHttp(QuickOpen.PSNSTOCKTHIRDQUICKOPENCAUTIONLISTQUERY, "requestPsnStockThirdQuickOpenCautionListQueryCallBack", params, false);
			}
		}
	};
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO ----------------------------------------网络请求与回调方法--------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 查询中行账户列表回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnCommonQueryAllChinaBankAccountCallBack(Object resultObj) {
		List<Map<String, Object>> resultList =  HttpTools.getResponseResult(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(resultList)) {
			String info = "";
			if (openOrQuery) {
				info = "您的电子渠道暂时没有任何关联借记卡账户，请关联合适的借记卡后再来尝试。";
			} else {
				info = "您没有适合开通第三方存管的借记卡。请首先申请借记卡并关联进网银后再来尝试。";
			}
			BaseDroidApp.getInstanse().showMessageDialog(info, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					QuickOpenDataCenter.getInstance().clearAllData();
					finish();
				}
			});
			return;
		}
		QuickOpenDataCenter.getInstance().setListCommonQueryAllChinaBankAccount(resultList);
		initView();
	}
	
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Comm.ACCOUNT_ID, QuickOpenDataCenter.getInstance().getListCommonQueryAllChinaBankAccount().get(selectPosition).get(Comm.ACCOUNT_ID));
		requestHttp(QuickOpen.PSNSTOCKTHIRDQUERYCUSTINFOEXTEND, "requestPsnStockThirdQueryCustInfoExtendCallBack", params, true);
	}
	
	/** 查询客户信息回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnStockThirdQueryCustInfoExtendCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		String idType = (String) resultMap.get(QuickOpen.IDENTIFYTYPE);
		String idNumber = (String) resultMap.get(QuickOpen.IDENTIFYNUMBER);
		if (!idType.equals("01") || idNumber.length() != 18) {
			// 如果不是身份证或者是身份证但是不是18位(非二代身份证)
			BaseDroidApp.getInstanse().showInfoMessageDialog("您的身份证件不是二代身份证，不能进行网上继续开户，详情请咨询95566");
			return;
		}
		QuickOpenDataCenter.getInstance().setMapStockThirdQueryCustInfoExtend(resultMap);
		Intent intent = new Intent(this, StockThirdQuickOpenConfirmActivity.class).putExtra(POSITION, selectPosition);
		startActivityForResult(intent, 4);
	}
	
	@SuppressWarnings("unchecked")
	public void requestPsnStockThirdQuickOpenCautionListQueryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		List<Map<String, Object>> resultList =HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultList)) {
			Intent intent = new Intent(this, QuickOpenResultActivity.class).putExtra(ISSUCCESS, false);
			startActivityForResult(intent, 4);
			return;
		}
		for (int i = 0; i < resultList.size(); i++) {
			Map<String, Object> map = resultList.get(i);
			if (map.get(QuickOpen.STOCKNO).equals("13190000")) {
				if (!StringUtil.isNull((String) map.get(QuickOpen.BAILACCOUNTNUMBER))) {
					Intent intent = new Intent(this, QuickOpenResultActivity.class).putExtra(ISSUCCESS, true);
					startActivityForResult(intent, 4);
					return;
				}
			}
		}
		Intent intent = new Intent(this, QuickOpenResultActivity.class).putExtra(ISSUCCESS, false);
		startActivityForResult(intent, 4);
	}
}
