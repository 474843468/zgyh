package com.chinamworld.bocmbci.biz.infoserve;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Push;
import com.chinamworld.bocmbci.biz.infoserve.adapter.NonFixedProductRemindAccListAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: NonFixedProductRemindAccountListActivity
 * @Description: 理财产品到期提醒设置——初始页面
 * @author wanbing
 * @date 2013-11-19
 */
public class NonFixedProductRemindAccountListActivity extends InfoServeBaseActivity {
	/** 账户列表 */
	private ListView cardList;
	/** 列表adapter */
	private NonFixedProductRemindAccListAdapter accountListAdapter;
	/** 查询到的账户列表 */
	List<Map<String, Object>> listData;
	/** 账户id */
	private String accountId;
	/** 点击下标 */
	private int curPosition;
	/** 操作类型 */
	private String optFlag;
	private Map<String, Object> paramsmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.service_nonfixed_product_remind_account_list);
		setTitle(R.string.infoserve_daedaozhang_alarm);
		listData = InfoServeDataCenter.getInstance().getAccountList();
		btn_right.setVisibility(View.GONE);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		init();
	}

	/**
	 * @Title: init
	 * @Description: 初始化view和数据
	 * @param
	 * @return void
	 */
	private void init() {
		if (StringUtil.isNullOrEmpty(listData)) {
			BaseDroidApp.getInstanse().showMessageDialog(
					this.getResources().getString(R.string.infoserve_daedaozhang_no_account), new OnClickListener() {

						@Override
						public void onClick(View v) {
							finish();
						}
					});
			return;
		}
		cardList = (ListView) this.findViewById(R.id.dept_list_view);
		setListView(listData);
		cardList.setOnItemClickListener(listItemClickListener);
	}

	/**
	 * 账户详情监听
	 */
	private OnItemClickListener listItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// // 大额到账提醒签约情况查询
			// curPosition = position;
			// accountId = (String) listData.get(position).get(Comm.ACCOUNT_ID);
			// String nickName = (String) listData.get(position)
			// .get(Comm.NICKNAME);
			// requestPsnNonFixedProductRemindQuery(accountId);
		}
	};

	/**
	 * @Title: setListView
	 * @Description: 填充视图的数据
	 * @param
	 * @return void
	 */
	private void setListView(List<Map<String, Object>> accountList) {
		if (accountListAdapter == null) {
			accountListAdapter = new NonFixedProductRemindAccListAdapter(this, accountList);
			cardList.setAdapter(accountListAdapter);
		} else {
			accountListAdapter.setAccountList(accountList);
			accountListAdapter.notifyDataSetChanged();
		}

	}

	/**
	 * @Title: requestPsnNonFixedProductRemindQueryQuery
	 * @Description:大额到账提醒签约情况查询
	 * @param accountId
	 * @return void
	 */
	public void requestPsnNonFixedProductRemindQuery(String accountId, int position) {
		curPosition = position;
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Push.PSN_NON_FIXED_PRODUCT_REMIND_QUERY);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Comm.ACCOUNT_ID, accountId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnNonFixedProductRemindQueryCallback");
	}

	/**
	 * @Title: requestPsnNonFixedProductRemindQueryCallback
	 * @Description: 大额到账提醒签约情况查询的回调
	 * @param @param resultObj
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnNonFixedProductRemindQueryCallback(Object resultObj) {

		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> map = (Map<String, Object>) biiResponseBody.getResult();
		// InfoServeDataCenter.getInstance().setNonFixedProductDetail(map);
		// 刷新数据
		listData.get(curPosition).put(Push.NON_FIXED_DETAIL, map);
		setListView(listData);
		// BaseDroidApp.getInstanse().showNonFixedProductRemindAccDetailDialog(
		// curPosition, nonFixedProductSignListener,
		// nonFixedProductDeleteListener, nonFixedProductModifyListener);
	}

	/**
	 * 大额到账解约点击
	 */
	private View.OnClickListener nonFixedProductDeleteListener = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			BaseDroidApp.getInstanse().showErrorDialog(
					getResources().getString(R.string.infoserve_daedaozhang_delete_notice), R.string.cancle,
					R.string.confirm, new OnClickListener() {

						@Override
						public void onClick(View v) {
							// 请求token
							requestCommConversationId();
							BaseHttpEngine.showProgressDialog();
						}
					});
		}
	};

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String token = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
		if (paramsmap != null) {
			// token
			paramsmap.put(Comm.TOKEN_REQ, token);
			// accountId
			paramsmap.put(Comm.ACCOUNT_ID, accountId);
			paramsmap.put("conversationId",
					(String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));

			requestPsnNonFixedProductRemind(canClick, paramsmap);
		}

	}

	@Override
	public void requestPsnNonFixedProductRemindCallback(Object resultObj) {
		super.requestPsnNonFixedProductRemindCallback(resultObj);
		// 刷新数据
		String msg = "";
		((Map<String, Object>) listData.get(curPosition)).put(Push.NON_FIXED_DETAIL, paramsmap);
		if (optFlag.equals(Push.NON_FIXED_OPT_DELETE)) {
			((Map<String, Object>) listData.get(curPosition).get(Push.NON_FIXED_DETAIL)).put(Push.SIGN_FLAG,
					Push.NON_FIXED_UNSIGNED);
			msg = getResources().getString(R.string.infoserve_daedaozhang_delete_success_notice);
		} else if (optFlag.equals(Push.NON_FIXED_OPT_ADD)) {
			((Map<String, Object>) listData.get(curPosition).get(Push.NON_FIXED_DETAIL)).put(Push.SIGN_FLAG,
					Push.NON_FIXED_SIGNED);
			msg = getResources().getString(R.string.infoserve_daedaozhang_sign_success_notice);
		}
		setListView(listData);
		// BaseDroidApp.getInstanse().showMessageDialog(msg,
		// new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// BaseDroidApp.getInstanse().dismissErrorDialog();
		// BaseDroidApp.getInstanse().dismissMessageDialog();
		// }
		// });
	}

	/**
	 * 大额到帐提醒开通取消
	 */
	private View.OnClickListener canClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (accountListAdapter != null) {
				accountListAdapter.setShowState(curPosition, false);
				setListView(listData);
			}
		}
	};

	/**
	 * 大额到账修改点击
	 */
	private View.OnClickListener nonFixedProductModifyListener = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {

			// 跳转到修改界面
			Intent intent = new Intent(NonFixedProductRemindAccountListActivity.this,
					NonFixedProductRemindSetActivity.class);
			Map<String, Object> paramsmap = InfoServeDataCenter.getInstance().getNonFixedProductDetail();
			intent.putExtra(Push.NON_FIXED_OPT_FLAG, Push.NON_FIXED_OPT_MODIFY);
			intent.putExtra(Comm.ACCOUNT_ID, accountId);
			intent.putExtra(Push.NON_FIXED_CURRENCY, (String) paramsmap.get(Push.NON_FIXED_CURRENCY));
			intent.putExtra(Push.NON_FIXED_FROMDATE, (String) paramsmap.get(Push.NON_FIXED_FROMDATE));
			intent.putExtra(Push.NON_FIXED_FROMTIME, (String) paramsmap.get(Push.NON_FIXED_FROMTIME));
			intent.putExtra(Push.NON_FIXED_BEGINAMT, (String) paramsmap.get(Push.NON_FIXED_BEGINAMT));
			intent.putExtra(Push.NON_FIXED_ENDAMT, (String) paramsmap.get(Push.NON_FIXED_ENDAMT));
			startActivity(intent);

		}
	};

	/**
	 * 大额到账签约点击
	 */
	private View.OnClickListener nonFixedProductSignListener = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// 跳转到协议画面
			Intent intent = new Intent(NonFixedProductRemindAccountListActivity.this,
					NonFixedProductRemindAgreeActivity.class);
			intent.putExtra(Push.NON_FIXED_OPT_FLAG, Push.NON_FIXED_OPT_ADD);
			intent.putExtra(Comm.ACCOUNT_ID, accountId);
			startActivity(intent);
		}
	};

	/**
	 * 解约
	 * 
	 * @param accountId
	 * @param position
	 * @param paramsmap
	 */
	public void nonFixedProductRemindDelete(String accountId, int position, Map<String, Object> paramsmap) {
		this.optFlag = (String) paramsmap.get(Push.NON_FIXED_OPT_FLAG);
		this.paramsmap = paramsmap;
		this.accountId = accountId;
		this.curPosition = position;
		paramsmap.put(Push.NON_FIXED_BEGINAMT, StringUtil.parseStringPattern2("100000000", 2));
		paramsmap.put(Push.NON_FIXED_ENDAMT, StringUtil.parseStringPattern2("50000", 2));
		paramsmap.put(Push.NON_FIXED_NIGHTSIGN, Push.NON_FIXED_NIGHT_YES);
		paramsmap.put(Push.NON_FIXED_CURRENCY, "CNY");
		// 请求token
		BaseHttpEngine.showProgressDialog(canClick);
		requestCommConversationId();
	
	}

	/**
	 * 签约
	 * 
	 * @param accountId
	 * @param position
	 * @param paramsmap
	 */
	public void nonFixedProductRemindSign(String accountId, int position, Map<String, Object> paramsmap) {
		this.optFlag = (String) paramsmap.get(Push.NON_FIXED_OPT_FLAG);
		this.paramsmap = paramsmap;
		this.accountId = accountId;
		this.curPosition = position;
		paramsmap.put(Push.NON_FIXED_BEGINAMT, StringUtil.parseStringPattern2("100000000", 2));
		paramsmap.put(Push.NON_FIXED_ENDAMT, StringUtil.parseStringPattern2("50000", 2));
		paramsmap.put(Push.NON_FIXED_NIGHTSIGN, Push.NON_FIXED_NIGHT_YES);
		paramsmap.put(Push.NON_FIXED_CURRENCY, "CNY");
		// Intent intent = new Intent(this,
		// NonFixedProductRemindAgreeActivity.class);
		// startActivityForResult(intent, 100);
		// 签约
		// 获取系统时间
		BiiHttpEngine.showProgressDialog(canClick);
		requestSystemDateTime();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100 && resultCode == 101) {
			// 签约
			// 获取系统时间
			requestSystemDateTime();
			BiiHttpEngine.showProgressDialog();
		}
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		String curDate = dateTime.substring(0, 10);
		String curTime = dateTime.substring(dateTime.length() - 8, dateTime.length());
		paramsmap.put(Push.NON_FIXED_FROMDATE, curDate);
		paramsmap.put(Push.NON_FIXED_FROMTIME, curTime);
		// 请求token
		requestCommConversationId();
	}

}
