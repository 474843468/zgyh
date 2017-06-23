package com.chinamworld.bocmbci.biz.bond.allbond;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Bond;
import com.chinamworld.bocmbci.biz.SecondMainActivity;
import com.chinamworld.bocmbci.biz.bond.BondBaseActivity;
import com.chinamworld.bocmbci.biz.bond.BondDataCenter;
import com.chinamworld.bocmbci.biz.bond.acct.BankAcctListActivity;
import com.chinamworld.bocmbci.biz.bond.acct.NoBankAcctActivity;
import com.chinamworld.bocmbci.biz.bond.adapter.BondAdapter;
import com.chinamworld.bocmbci.biz.bond.bondtran.BuyBondMsgFillActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

import static com.chinamworld.bocmbci.utils.LoginTask.*;

/***
 * 债券行情列表
 * 
 * @author panwe
 * 
 */
public class AllBondListActivity extends BondBaseActivity {

	private static final String TAG = "AllBondListActivity";

	private final int INIT_LEFTEVALUE = 0;
	/** 主布局 */
	private View mainView;
	/** 行情列表 */
	private ListView lvBond;
	// private View vLine;
	/** 列表数据 */
	private List<Map<String, Object>> bondList;
	/** 选中条目 */
	private int mPostion;

	private BondAdapter mAdapter;
	private boolean isLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setLeftSelectedPosition("bond_1");
		btnRight.setVisibility(View.GONE);
		setTitle(R.string.bond_bond_title);
		init();
	}

	private void init() {
		mainView = (View) LayoutInflater.from(this).inflate(
				R.layout.bond_allbond_list, null);
		addView(mainView);
		layoutContent.setPadding(0, 0, 0, 0);
		lvBond = (ListView) mainView.findViewById(R.id.listview);
		lvBond.setOnItemClickListener(itemClick);
		btnRight.setVisibility(View.VISIBLE);
	}

	/** 初始化列表数据 */
	private void initList() {
		if (mAdapter == null) {
			mAdapter = new BondAdapter(this, bondList);
			lvBond.setAdapter(mAdapter);
		} else {
			mAdapter.changeData(bondList);
		}

	}
	/**
	 * 右侧按钮点击事件
	 */
	private OnClickListener rightBtnClick=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (isLogin) {
				Intent intent=new Intent(AllBondListActivity.this,BuyBondMsgFillActivity.class);
				intent.putExtra(Bond.RE_HISTORYTRAN_QUERY_TRANTYPE, Bond.BOND_TRANTYPE_BUY);
				startActivity(intent);
			} else {
				BaseActivity.getLoginUtils(AllBondListActivity.this).exe(new LoginCallback() {

					@Override
					public void loginStatua(boolean isLogin) {

					}
				});
			}
		}
	};
	/**
	 * 返回按钮点击事件
	 */
	private OnClickListener backBtnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String isFromModuleManager = (String)BaseDroidApp.getInstanse().getBizDataMap().get("isFromModuleManager");
			if(!StringUtil.isNullOrEmpty(isFromModuleManager)&&"true".equals(isFromModuleManager)){
				finish();
			}else{
//				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				Intent intent = new Intent(AllBondListActivity.this,
//						SecondMainActivity.class);
//				startActivity(intent);
				goToMainActivity();
			}

		}

	};

	/** 列表点击事件 */
	private OnItemClickListener itemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (view.getId() == R.id.tv_bond_name_and_code) {
				view.performClick();
			} else {
				mPostion = position;
				requestDetail(
						(String) bondList.get(position).get(Bond.BOND_CODE),
						(String) bondList.get(position).get(Bond.BOND_TYPE));
			}
		}
	};

	/** 系统时间 */
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		if (StringUtil.isNull(dateTime)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		BondDataCenter.getInstance().setSysTime(dateTime);
		// init();
		requestBondList();
	}

	/** 获取债券行情列表 */
	private void requestBondList() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bond.METHOD_ALLBOND_LIST);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(Bond.BOND_TYPE, BondDataCenter.bondType_re.get(1));
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "allbondListCallBack");

	}

	// private Handler handler = new Handler() {
	// public void handleMessage(android.os.Message msg) {
	// // http状态码
	// String resultHttpCode = (String) ((Map<String, Object>) msg.obj)
	// .get(ConstantGloble.HTTP_RESULT_CODE);
	//
	// // 返回数据
	// Object resultObj = ((Map<String, Object>) msg.obj)
	// .get(ConstantGloble.HTTP_RESULT_DATA);
	//
	// // 回调对象
	// HttpObserver callbackObject = (HttpObserver) ((Map<String, Object>)
	// msg.obj)
	// .get(ConstantGloble.HTTP_CALLBACK_OBJECT);
	//
	// // 回调方法
	// String callBackMethod = (String) ((Map<String, Object>) msg.obj)
	// .get(ConstantGloble.HTTP_CALLBACK_METHOD);
	//
	// switch (msg.what) {
	// // 正常http请求数据返回
	// case ConstantGloble.HTTP_STAGE_CONTENT:
	// /**
	// * 执行全局前拦截器
	// */
	// if (BaseDroidApp.getInstanse().httpRequestCallBackPre(resultObj)) {
	// LogGloble.i(TAG, "执行全局前拦截器");
	// break;
	// }
	//
	// /**
	// * 执行callbackObject回调前拦截器
	// */
	// if (httpRequestCallBackPre(resultObj)) {
	// LogGloble.i(TAG, "执行callbackObject回调前拦截器");
	// stopPolling();
	// break;
	// }
	// BaseHttpEngine.dissMissProgressDialog();
	// BiiResponse biiResponse = (BiiResponse) ((Map<String, Object>) msg.obj)
	// .get(ConstantGloble.HTTP_RESULT_DATA);
	// List<BiiResponseBody> biiResponseBodys = biiResponse
	// .getResponse();
	// BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
	// Map<String, Object> result = (Map<String, Object>) biiResponseBody
	// .getResult();
	// bondList = (List<Map<String, Object>>) result.get(Bond.ALLBOND_LIST);
	// if (StringUtil.isNullOrEmpty(bondList)) {
	// BaseDroidApp.getInstanse().showMessageDialog(
	// AllBondListActivity.this.getString(R.string.bond_comm_error),
	// errorClick);
	// return;
	// }
	// BondDataCenter.getInstance().setBondList(bondList);
	// BondDataCenter.getInstance().initBondName(bondList);
	// initList();
	// /**
	// * 执行callbackObject回调后拦截器
	// */
	// if (httpRequestCallBackAfter(resultObj)) {
	// LogGloble.i(TAG, "执行callbackObject回调后拦截器");
	// break;
	// }
	//
	// /**
	// * 执行全局后拦截器
	// */
	// if (BaseDroidApp.getInstanse().httpRequestCallBackAfter(resultObj)) {
	// LogGloble.i(TAG, "执行全局后拦截器");
	// break;
	// }
	// break;
	//
	// // 请求失败错误情况处理
	// case ConstantGloble.HTTP_STAGE_CODE:
	// /**
	// * 执行code error 全局前拦截器
	// */
	// if (BaseDroidApp.getInstanse().httpCodeErrorCallBackPre(
	// resultHttpCode)) {
	// break;
	// }
	//
	// /**
	// * 执行callbackObject error code 回调前拦截器
	// */
	// if (callbackObject.httpCodeErrorCallBackPre(resultHttpCode)) {
	// break;
	// }
	//
	// Method httpCodeCallbackMethod = null;
	// try {
	// // 回调
	// httpCodeCallbackMethod = callbackObject.getClass()
	// .getMethod(callBackMethod, String.class);
	//
	// httpCodeCallbackMethod.invoke(callbackObject,
	// resultHttpCode);
	// } catch (SecurityException e) {
	// LogGloble.e(TAG, "SecurityException ", e);
	// } catch (NoSuchMethodException e) {
	// LogGloble.e(TAG, "NoSuchMethodException ", e);
	// } catch (IllegalArgumentException e) {
	// LogGloble.e(TAG, "IllegalArgumentException ", e);
	// } catch (IllegalAccessException e) {
	// LogGloble.e(TAG, "IllegalAccessException ", e);
	// } catch (InvocationTargetException e) {
	// LogGloble.e(TAG, "InvocationTargetException ", e);
	// } catch (NullPointerException e) {
	// // add by wez 2012.11.06
	// LogGloble.e(TAG, "NullPointerException ", e);
	// throw e;
	// } catch (ClassCastException e) {
	// // add by wez 2012.11.06
	// LogGloble.e(TAG, "ClassCastException ", e);
	// throw e;
	// }
	//
	// /**
	// * 执行code error 全局后拦截器
	// */
	// if (BaseDroidApp.getInstanse().httpCodeErrorCallBackAfter(
	// resultHttpCode)) {
	// break;
	// }
	//
	// /**
	// * 执行callbackObject code error 后拦截器
	// */
	// if (callbackObject.httpCodeErrorCallBackAfter(resultHttpCode)) {
	// break;
	// }
	//
	// break;
	//
	// default:
	// break;
	// }
	//
	// }
	//
	// };

	/**
	 * 债券列表返回处理
	 */
	@SuppressWarnings("unchecked")
	public void allbondListCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		bondList = (List<Map<String, Object>>) result.get(Bond.ALLBOND_LIST);
		if (StringUtil.isNullOrEmpty(bondList)) {
			BaseDroidApp.getInstanse().showMessageDialog(
					this.getString(R.string.bond_comm_error), errorClick);
			return;
		}
		BondDataCenter.getInstance().setBondList(bondList);
		BondDataCenter.getInstance().initBondName(bondList);
		initList();
		// vLine.setVisibility(View.VISIBLE);
	}

	/**
	 * 获取债券详情
	 * 
	 * @param bondId
	 * @param bondType
	 */
	private void requestDetail(String bondId, String bondType) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put(Bond.BOND_CODE, bondId);
		parms.put(Bond.BOND_TYPE, BondDataCenter.bondType_re.get(1));
		biiRequestBody.setParams(parms);
		if (isLogin) {
			biiRequestBody.setMethod(Bond.EMTHOD_BOND_DETAIL);
			HttpManager.requestBii(biiRequestBody, this, "bondDetailCallBack");
		} else {
			biiRequestBody.setMethod(Bond.EMTHOD_BOND_DETAIL_OUTLAY);
			HttpManager.requestOutlayBii(biiRequestBody, this,
					"bondDetailCallBack");
		}

	}

	/** 债券详情返回处理 */
	public void bondDetailCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		if (result == null) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.bond_comm_error));
			return;
		}
		BondDataCenter.getInstance().setBondMap(result);
		BaseHttpEngine.dissMissProgressDialog();
		Intent it = new Intent(this, BondInfoActivity.class);
		it.putExtra(POSITION, mPostion);
		startActivity(it);
	}

	/** 资金账号列表返回处理 **/
	@Override
	public void bankAccListCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.bankAccListCallBack(resultObj);
		List<Map<String, Object>> bankAccList = BondDataCenter.getInstance()
				.getBankAccList();
		if (bankAccList == null) {
			// 提示关联
			startActivity(new Intent(this, NoBankAcctActivity.class));
		} else {
			Intent it = new Intent(this, BankAcctListActivity.class);
			it.putExtra(ISOPEN, false);
			startActivityForResult(it, ConstantGloble.ACTIVITY_RESULT_CODE);
			overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// P503功能外置
		isLogin = BaseDroidApp.getInstanse().isLogin();
		
		//登录时：显示快速买入      未登录：显示登录
		if (isLogin) {
			setText(this.getString(R.string.bond_fastbuy));
		} else {
			setText(this.getString(R.string.login));
		}
		setRightBtnClick(rightBtnClick);		
				
		if (isLogin) {
			// 开户/已登记成功返回
			boolean isSuccess = getIntent().getBooleanExtra(ISSUCCESS, false);
			if (isSuccess) {
				BaseHttpEngine.showProgressDialog();
				if (BondDataCenter.getInstance().getAccMap() != null) {
					// 请求系统时间
					requestSystemDateTime();
				} else {
					requestAccInfo();
				}
			} else {
				// 请求是否开通投资理财
				requestPsnInvestmentManageIsOpen();
			}

		} else {
			btnBack.setOnClickListener(backBtnClick);
			BaseHttpEngine.showProgressDialog();
			requestLoginPreOutlayConversationId();
		}
	}

	/**
	 * 登录之前的conversationId 返回
	 */
	public void requestLoginPreOutlayConversationIdCallBack(Object resultObj) {
		super.requestLoginPreOutlayConversationIdCallBack(resultObj);
		String loginPreOutlayConversationId = (String) BaseDroidApp
				.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOGIN_OUTLAY_PRECONVERSATIONID);

		requestBondListOutlay();
	}

	/**
	 * 查询未登录债券行情
	 */
	private void requestBondListOutlay() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bond.METHOD_ALLBOND_LIST_OUTLAY);
		Map<String, Object> param = new HashMap<String, Object>();
		// 只有记账式
		param.put(Bond.BOND_TYPE, BondDataCenter.bondType_re.get(1));
		biiRequestBody.setParams(param);
		HttpManager.requestOutlayBii(biiRequestBody, this,
				"requestBondListOutlayCallback");
	}

	public void requestBondListOutlayCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		bondList = (List<Map<String, Object>>) result.get(Bond.ALLBOND_LIST);
		if (StringUtil.isNullOrEmpty(bondList)) {
			BaseDroidApp.getInstanse().showMessageDialog(
					this.getString(R.string.bond_comm_error), errorClick);
			return;
		}
		BondDataCenter.getInstance().setBondList(bondList);
		BondDataCenter.getInstance().initBondName(bondList);
		initList();
	}

}
