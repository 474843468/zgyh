package com.chinamworld.bocmbci.biz.safety.safetytemp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyBaseActivity;
import com.chinamworld.bocmbci.biz.safety.SafetyConstant;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.SafetyUtils;
import com.chinamworld.bocmbci.biz.safety.adapter.SafetyProductTempAdapter;
import com.chinamworld.bocmbci.biz.safety.safetyproduct.SafetyProductInfoActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.BaseSwipeListViewListener;
import com.chinamworld.bocmbci.widget.SwipeListView;

/**
 * 暂存保单信息列表
 * 
 * @author sunz
 * 
 */
public class SafetyTempProductListActivity extends SafetyBaseActivity {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static final String TAG = "SafetyTempProductListActivity";
	/** 左侧菜单选中项 */
//	private final int INIT_LEFTEVALUE = 1;
	/** 主显示布局 */
	private View mMainView;
	/** 产品类型下拉框 */
	private Spinner spSafetyType;
	/** 暂存单列表显示控件 */
	private SwipeListView mSwipeListView;
	/** 适配器 */
	private SafetyProductTempAdapter mAdapter;
	/** 产品列表，装载全部产品，装载前就将列表项按产品类型分离 */
	private List<Map<String, Object>> productList = new ArrayList<Map<String, Object>>();
	/** 车险总数 */
	private int carSafetyTotal = 0;
	/** 家财险总数 */
	private int familySafetyTotal = 0;
	/** 意外险总数 */
	private int accSafetyTotal = 0;
	/** 当前滑动条目  */
	private int mPosition;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findViewById(R.id.sliding_body).setPadding(0, 15, 0,0);
		mMainView = View.inflate(this, R.layout.safety_temp_product_list, null);
		//setLeftSelectedPosition(INIT_LEFTEVALUE);
		setTitle(getString(R.string.safety_tempproduct_list_title));
		setRightTopGone();
		addView(mMainView);
		setupViews();
		getIntentDate();
	}
	
	private void setupViews() {
		spSafetyType = (Spinner) mMainView.findViewById(R.id.sp_safetyType);
		List<String> listSafetyType = new ArrayList<String>();
		listSafetyType.add("全部");
		listSafetyType.addAll(SafetyDataCenter.safetyType);
		if(listSafetyType.contains("寿险")){
			listSafetyType.remove("寿险");
		}
		SafetyUtils.initSpinnerView(this, spSafetyType, listSafetyType);
		spSafetyType.setSelection(0, true);
		spSafetyType.setOnItemSelectedListener(selectSafetyTypeListener);
		mSwipeListView = (SwipeListView) mMainView.findViewById(R.id.safety_listview);
		mSwipeListView.setSwipeListViewListener(swipeListViewListener);
		setContainsSwipeListView(true);
		mSwipeListView.setLastPositionClickable(true);
		mSwipeListView.setAllPositionClickable(true);
	}
	
	/** 获取Intent数据*/
	private void getIntentDate(){
		BaseHttpEngine.showProgressDialog();
		if(getIntent().getBooleanExtra(SafetyConstant.NEEDDELETE, false)){
			requestCommConversationId();
			return;
		}
		BaseHttpEngine.showProgressDialog();
		httpTools.requestHttp(Safety.PSNAUTOINSURPOLICYLISTQRY, "requestPsnAutoInsurPolicyListQryCallBack", null, false);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (SafetyDataCenter.getInstance().isChange()) {
			productList.clear();
			SafetyDataCenter.getInstance().setChange(false);
			BaseHttpEngine.showProgressDialog();
			httpTools.requestHttp(Safety.PSNAUTOINSURPOLICYLISTQRY, "requestPsnAutoInsurPolicyListQryCallBack", null, false);
		}
	}
	
	/**
	 * 定位算法，作用是当用户选择当前页面列表的条目后用当前选择的列表下标在总列表中定位到该条数据所在位置的下标
	 * 
	 * @param position
	 *            用户选择的当前显示列表的下标
	 * @return 当前选择的条目在总数据列表中的位置
	 */
	private int getProductTotalPosition(int position) {
		if ((spSafetyType.getSelectedItemPosition() == 0) || (spSafetyType.getSelectedItemPosition() == 1)) {
			return position;
		} else if (spSafetyType.getSelectedItemPosition() == 2) {
			return carSafetyTotal + position;
		} else {
			return carSafetyTotal + familySafetyTotal + position;
		}
	}
	
	/** 查询暂存保单详情 */
	@SuppressWarnings("unchecked")
	private void toProductDetail(int position){
		BaseHttpEngine.showProgressDialog();
		Map<String, Object> map = productList.get(getProductTotalPosition(position));
		if (StringUtil.isNull((String) map.get(Safety.TRANTYPE_FLAG))) {
			// 如果没有该字段，说明是车险
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put(Safety.POLICYID, map.get(Safety.POLICYID));
			
			Map<String, Object> logInfo = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA);
			params.put(Safety.APPLIDENTITYTYPE, SafetyDataCenter.IDENTITYTYPE_credType.get(logInfo.get(Comm.IDENTITYTYPE)));
			params.put(Safety.APPLIDENTITYNUM, logInfo.get(Comm.IDENTITYNUMBER));
			
			httpTools.requestHttp(Safety.PSNAUTOINSURPOLICYDETAILQRY, "requestPsnAutoInsurPolicyDetailQryCallBack", params, false);
		} else {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put(Safety.INSURANCE_ID, map.get(Safety.INSURANCE_ID));
			params.put(Safety.INSUCODE, map.get(Safety.INSUCODE));
			params.put(Safety.RISKCODE, map.get(Safety.RISKCODE));
			httpTools.requestHttp(Safety.METHOD_INSURANCE_PRODUCT_INFO, "requestPsnInsuranceProductDetailsCallBack", params, false);
		}
	}
	
	@Override
	public boolean doBiihttpRequestCallBackPre(BiiResponse response) {
		List<BiiResponseBody> biiResponseBodyList = response.getResponse();
		if (!StringUtil.isNullOrEmpty(biiResponseBodyList)) {
			for (BiiResponseBody body : biiResponseBodyList) {
				if (!ConstantGloble.STATUS_SUCCESS.equals(body.getStatus())) {
					if (body.getMethod().equals(Safety.PSNAUTOINSURPOLICYLISTQRY)) {
						// 如果是车险接口报错，按查询无数据处理，接着请求家财险意外险接口
						httpTools.requestHttp(Safety.METHOD_INSURANCE_TEMP_PRODUCT, "requestPsnInsuranceSavedQueryCallBack", null, false);
						return false;
					} else if (body.getMethod().equals(Safety.METHOD_INSURANCE_TEMP_PRODUCT)) {
						// 如果是家财险意外险列表接口报错，按查询无数据处理
						if (StringUtil.isNullOrEmpty(productList)) {
							//如果车险也没有请求回来，报错
							BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.bond_comm_error));
						} else {
							// 按照产品类型下拉框动态为列表赋值
							if ((spSafetyType.getSelectedItemPosition() == 0) || (spSafetyType.getSelectedItemPosition() == 1)) {
								mAdapter.setData(productList);
							} else {
								mAdapter.setData(new ArrayList<Map<String,Object>>());
							}
						}
						return true;
					}
					// 消除通信框
					BaseHttpEngine.dissMissProgressDialog();
					if (Login.LOGOUT_API.equals(body.getMethod())) {// 退出的请求
						return false;
					}
					BiiError biiError = body.getError();
					// 判断是否存在error
					if (biiError != null) {
						//过滤错误
						LocalData.Code_Error_Message.errorToMessage(body);
						if (biiError.getCode() != null) {
							if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
								showTimeOutDialog(biiError.getMessage());
//								BaseDroidApp.getInstanse().showMessageDialog(biiError.getMessage(), new OnClickListener() {
//									@Override
//									public void onClick(View v) {
//										BaseDroidApp.getInstanse().dismissErrorDialog();
//										ActivityTaskManager.getInstance().removeAllActivity();
////										Intent intent = new Intent();
////										intent.setClass(SafetyTempProductListActivity.this, LoginActivity.class);
////										startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
//										BaseActivity.getLoginUtils(SafetyTempProductListActivity.this).exe(new LoginTask.LoginCallback() {
//
//											@Override
//											public void loginStatua(boolean isLogin) {
//
//											}
//										});
//									}
//								});
							} else {// 非会话超时错误拦截
								BaseDroidApp.getInstanse().createDialog(biiError.getCode(), biiError.getMessage(), new OnClickListener() {
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

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------控件事件-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 选择产品类型事件 */
	private OnItemSelectedListener selectSafetyTypeListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			// 按照产品类型下拉框动态为列表赋值
			if (productList.size() == 0) {
				return;
			}
			List<Map<String, Object>> tempList = null;
			if (position == 0) {
				tempList = productList;
			} else if (position == 1) {
				tempList = new ArrayList<Map<String,Object>>(productList.subList(0, carSafetyTotal));
			} else if (position == 2) {
				tempList = new ArrayList<Map<String,Object>>(productList.subList(carSafetyTotal, carSafetyTotal + familySafetyTotal));
			} else if (position == 3) {
				tempList = new ArrayList<Map<String,Object>>(productList.subList(carSafetyTotal + familySafetyTotal, carSafetyTotal + familySafetyTotal + accSafetyTotal));
			}
			if (StringUtil.isNullOrEmpty(tempList)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.bond_comm_error));
			}
			LogGloble.i(TAG, tempList.toString());
			if (mAdapter != null) {
				mAdapter.setData(tempList);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	};

	/** 列表向右滑动事件 */
	BaseSwipeListViewListener swipeListViewListener = new BaseSwipeListViewListener() {
		@Override
		public void onStartOpen(int position, int action, boolean right) {
			if (action == 0) {
				mPosition = position;
				toProductDetail(position);
			}
		}
		
		@Override
		public void onClickFrontView(int position) {
			super.onClickFrontView(position);
			mPosition = position;
			toProductDetail(position);
		}
	};

	/** 数据返回异常 **/
	public OnClickListener errorClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissErrorDialog();
			finish();
		}
	};
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO ---------------------------------------网络请求与回调方法--------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 请求暂存保单列表返回 */
	@SuppressWarnings("unchecked")
	public void requestPsnInsuranceSavedQueryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		List<Map<String, Object>> mList = (List<Map<String, Object>>) result.get(Safety.PRODUCTTEMP_LIST);
		
		if (StringUtil.isNullOrEmpty(mList)) {
			if (StringUtil.isNullOrEmpty(productList)) {
				BaseDroidApp.getInstanse().showMessageDialog(this.getString(R.string.bond_comm_error), errorClick);
				return;
			} else {
				// 按照产品类型下拉框动态为列表赋值
				if (mAdapter == null) {
					mAdapter = new SafetyProductTempAdapter(this, productList);
					mSwipeListView.setAdapter(mAdapter);
					return;
				}
				if ((spSafetyType.getSelectedItemPosition() == 0) || (spSafetyType.getSelectedItemPosition() == 1)) {
					mAdapter.setData(productList);
				} else {
					mAdapter.setData(new ArrayList<Map<String,Object>>());
				}
			}
		} else {
			List<Map<String, Object>> productListTemp = new ArrayList<Map<String,Object>>();
			// 将家财险意外险分离，当这个循环执行完成后，mList中剩下的是意外险，productListTemp中是家财险
			for (int i = 0; i < mList.size(); i++) {
				Map<String, Object> map = mList.get(i);
				if (map.get(Safety.TRANTYPE_FLAG).equals("0")) {
					productListTemp.add(map);
					mList.remove(i);
					i --;
				}
			}
			familySafetyTotal = productListTemp.size();
			accSafetyTotal = mList.size();
			// 先把家财险添加到大列表中
			productList.addAll(productListTemp);
			// 再添加意外险
			productList.addAll(mList);
			// 由于类型是ArrayList，现在productList中按照车险、家财险、意外险排列
			if (mAdapter == null) {
				mAdapter = new SafetyProductTempAdapter(this, productList);
				mSwipeListView.setAdapter(mAdapter);
				return;
			}
			// 按照产品类型下拉框动态为列表赋值
			List<Map<String, Object>> tempList = null;
			if (spSafetyType.getSelectedItemPosition() == 0) {
				tempList = productList;
			} else if (spSafetyType.getSelectedItemPosition() == 1) {
				tempList = new ArrayList<Map<String,Object>>(productList.subList(0, carSafetyTotal));
			} else if (spSafetyType.getSelectedItemPosition() == 2) {
				tempList = new ArrayList<Map<String,Object>>(productList.subList(carSafetyTotal, carSafetyTotal + familySafetyTotal));
			} else if (spSafetyType.getSelectedItemPosition() == 3) {
				tempList = new ArrayList<Map<String,Object>>(productList.subList(carSafetyTotal + familySafetyTotal, carSafetyTotal + familySafetyTotal + accSafetyTotal));
			}
			if (mAdapter != null) {
				mAdapter.setData(tempList);
			}
		}
	}
	
	/** 车险暂存单列表查询回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnAutoInsurPolicyListQryCallBack(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		List<Map<String, Object>> mList = (List<Map<String, Object>>) result.get(Safety.PRODUCT_LIST);
		
		if (!StringUtil.isNullOrEmpty(mList)) {
			productList.addAll(mList);
			carSafetyTotal = mList.size();
		} else {
			carSafetyTotal = 0;
		}
		httpTools.requestHttp(Safety.METHOD_INSURANCE_TEMP_PRODUCT, "requestPsnInsuranceSavedQueryCallBack", null, false);
	}
	
	/** 保险产品信息回调（保险详情） */
	public void requestPsnInsuranceProductDetailsCallBack(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(result)) return;
		result.putAll(productList.get(getProductTotalPosition(mPosition)));
		SafetyDataCenter.getInstance().setProductInfoMap(result);
		startActivity(new Intent(SafetyTempProductListActivity.this, SafetyProductInfoActivity.class)
		.putExtra(SafetyConstant.PRODUCTORSAVE, false)
		.putExtra(Safety.ITEM_URL, (String)result.get(Safety.ITEM_URL))
		.putExtra(Safety.PROD_INFO, (String)result.get(Safety.PROD_INFO)));
	}
	
	/** 车险暂存单详情返回 */
	public void requestPsnAutoInsurPolicyDetailQryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		resultMap.putAll(productList.get(getProductTotalPosition(mPosition)));
		SafetyDataCenter.getInstance().setMapAutoInsurPolicyDetailQry(resultMap);
		startActivity(new Intent(SafetyTempProductListActivity.this, CarSafetyTempDetailActivity.class)
		.putExtra(SafetyConstant.PRODUCTORSAVE, false));
	}
	
	/** 请求conversationId回调*/
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		httpTools.requestHttp(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API, "requestPSNGetTokenIdCallBack", null, true);
	}
	
	/** 获取token回调*/
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		String tokenId = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		HashMap<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> infoMap = SafetyDataCenter.getInstance().getProductInfoMap();
		params.put(Safety.TOKEN, tokenId);
		params.put(Safety.ALIAS_ID, (String)infoMap.get(Safety.ALIAS_ID));
		params.put(Safety.DEPETE_INSURID, (String)infoMap.get(Safety.DEPETE_INSURID));
		httpTools.requestHttp(Safety.METHOD_DELETEINSUR, "requestPsnInsuranceSavedDeleteCallBack", params, true);
	}
	
	/** 请求删除暂存保单回调 */
	public void requestPsnInsuranceSavedDeleteCallBack(Object resultObj) {
		httpTools.requestHttp(Safety.PSNAUTOINSURPOLICYLISTQRY, "requestPsnAutoInsurPolicyListQryCallBack", null, false);
	}
}
