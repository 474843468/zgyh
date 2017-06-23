package com.chinamworld.bocmbci.biz.tran.collect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Collect;
import com.chinamworld.bocmbci.bii.constant.Collect.CollectStatusType;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.biz.tran.collect.adapter.AccountListAdapter;
import com.chinamworld.bocmbci.biz.tran.collect.setting.CollectSettingActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.BottomButtonUtils;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * @ClassName: CollectListActivity
 * @Description: 资金归集列表
 * @author luql
 * @date 2014-3-17 下午02:44:58
 */
public class CollectListActivity extends CollectBaseActivity {

	private static final int request_setting_Code = 10001;
	public static final int refresh_collect_list = 10002;
	public static final int refresh_uncollect_list = 10003;
	public static final int refresh_list = 10004;
	protected static final int request_account_Code = 1005;

	/** 全部归集 */
	public static final String ALL = "1";
	/** 部分归集 */
	public static final String SECTION = "2";

	private View viewContent;
	/** 以设置资金归集 */
	private ListView mCollectListView;
	/** 以设置归集账户布局 */
	private View mCollectLayout;
	/** 以设置归集账户空提示 */
	private View mCollectEmptyView;
	/** 未设置资金归集 */
	private ListView mUnCollectListView;
	/** 未设置归集账户布局 */
	private View mUnCollectLayout;
	/** 未设置归集账户空提示 */
	private View mUnCollectEmptyView;
	/** 资金归集布局 */
	private RadioGroup mRadioLayout;
	/** 以设置资金归集按钮 */
	private RadioButton mCollectView;
	/** 未设置资金归集按钮 */
	private RadioButton mUnCollectView;
	// /** 一键归集 */
	// private View mCollectAllView;
	/** 是否可以归集,归集成功两分钟内不能在归集 */
	private boolean isCollectFlag = true;
	private static final long COLLECT_FLAG_TIME = 60 * 2 * 1000;
	/** 选择归集 */
	private Button mCollectSelectView;
	/** 归集完成 */
	private Button mCollectFinishView;
	/** 归集取消 */
	private Button mCollectCancleView;
	/** 以被归集账户数据源 */
	private AccountListAdapter mCollectAdapter;
	/** 未被归集账户数据源 */
	private AccountListAdapter mUnCollectAdapter;
	// private int mCurrentIndex;
	/** 归集账户 */
	private String mPayeeAccount;
	/** 归集账户数据 */
	private Map<String, Object> mAccountData;
	/** 被归集账户个数 */
	private int collect_size = 10;
	/** 手机号 */
	private String cbMobileParam;

	private int selectedIndex = 0;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isCollectFlag = true;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		viewContent = addView(R.layout.collect_list_activity);
		setTitle(getString(R.string.collect_title));
		if (getIntentData()) {
			mPayeeAccount = (String) mAccountData
					.get(Acc.ACC_ACCOUNTNUMBER_RES);
			toprightBtn();
			findView();
			setListener();
			if(selectedIndex == 0) {
				mCollectView.setChecked(true);
				mUnCollectView.setChecked(false);
			}
			else {
				mUnCollectView.setChecked(true);
				mCollectView.setChecked(false);
			}
		} else {
			finish();
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		if (mCollectSelectView.getVisibility() == View.VISIBLE) {
			// 执行查询
			requestList();
		} else {
			// 执行归集
			requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
					.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		}
	}

	/**
	 * 归集二次确认对话框
	 */
	private void collectOnTimeDialog(List<Map<String, Object>> onTimeFlag) {
		BaseDroidApp.getInstanse().showErrorDialog(
				getString(R.string.collect_account_prompt), R.string.cancle,
				R.string.confirm, new OnClickListener() {
					@Override
					public void onClick(View v) {
						BaseDroidApp.getInstanse().dismissErrorDialog();
						if (v.getId() == R.id.retry_btn) {
							BiiHttpEngine.showProgressDialogCanGoBack();
							requestCommConversationId();
						}
					}
				});
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		// 部分归集
		requestPsnCBCollectOnTime(SECTION);
	}

	/**
	 * 一键归集
	 * 
	 * @param 1-全部归集 2-部分归集
	 */
	private void requestPsnCBCollectOnTime(String onTimeFlag) {
		List<Map<String, Object>> data = mCollectAdapter.getSelectData();
		if (data != null) {
			String payeeAccCard = (String) mAccountData.get(Collect.cbAccCard);
			String payeeAccNum = (String) mAccountData.get(Collect.cbAccNum);
			String token = (String) BaseDroidApp.getInstanse().getBizDataMap()
					.get(ConstantGloble.TOKEN_ID);
			List<Map<String, String>> ruleNoList = new ArrayList<Map<String, String>>();
			for (Map<String, Object> map : data) {
				Map<String, String> payerAccount = new HashMap<String, String>();
				payerAccount.put(Collect.ruleNo,
						(String) map.get(Collect.rusleNo));
				payerAccount.put(Collect.payerAccount,
						(String) map.get(Collect.payerAccount));
				payerAccount.put(Collect.payerAccBankName,
						(String) map.get(Collect.payerAccBankName));
				ruleNoList.add(payerAccount);
			}

			BiiRequestBody biiRequestBody = new BiiRequestBody();
			biiRequestBody.setMethod(Collect.PSN_CBCOLLECT_ONTIME_API);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Collect.onTimeFlag, onTimeFlag);
			map.put(Collect.cbAccCard, payeeAccCard);
			map.put(Collect.cbAccNum, payeeAccNum);
			map.put(Collect.listNum, String.valueOf(ruleNoList.size()));
			map.put(ConstantGloble.PUBLIC_TOKEN, token);
			map.put(Collect.ruleNoList, ruleNoList);
			biiRequestBody.setParams(map);
			biiRequestBody.setConversationId((String) BaseDroidApp
					.getInstanse().getBizDataMap()
					.get(ConstantGloble.CONVERSATION_ID));
			HttpManager.requestBii(biiRequestBody, this,
					"requestPsnCBCollectOnTimeCallback");
		}
	}

	public void requestPsnCBCollectOnTimeCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		// BiiResponse biiResponse = (BiiResponse) resultObj;
		// List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// Map<String, String> resultData = (Map<String, String>)
		// biiResponseBody.getResult();
		// 显示提示对话框
		BaseDroidApp.getInstanse().createDialog(null,
				getString(R.string.collect_success));
		// 恢复选择模式
		isCollectFlag = false;
		mCollectCancleView.performClick();
		handler.sendEmptyMessageDelayed(0, COLLECT_FLAG_TIME);
	}

	/**
	 * @param
	 * @return
	 */

	private boolean getIntentData() {
		Intent intent = getIntent();
		int position = intent.getIntExtra("position", -1);
		selectedIndex = intent.getIntExtra("selectedIndex", 0);
		mAccountData = CollectData.getInstance().getAccountList().get(position);
		// SharedPreferences sp =
		// BaseDroidApp.getInstanse().getSharedPrefrences();
		// cbMobileParam = sp.getString("loginNameForLog", "");
		Map<String, Object> resultMap = (Map<String, Object>) BaseDroidApp
				.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA);
		cbMobileParam = String.valueOf(resultMap.get("mobile"));
		return mAccountData != null;
	}

	/**
	 * 获取被归集列表
	 * 
	 * @param isRefresh
	 */
	private void requestPsnCBCollectQuery(String status, int index, int page,
			boolean isRefresh) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Collect.PSN_CBCOLLECT_QUERY_API);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Collect.accountId, mAccountData.get(Comm.ACCOUNT_ID));
		params.put(Collect.queryType, status);
		params.put(PubConstants.PUB_QUERY_FEILD_PAGE_SIZE, String.valueOf(page));
		params.put(PubConstants.PUB_QUERY_FEILD_CURRENT_INDEX,
				EpayUtil.getString(index, "0"));
		params.put(PubConstants.PUB_QUERY_FEILD_IS_REFRESH,
				EpayUtil.getString(isRefresh, "true"));
		biiRequestBody.setParams(params);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this,
				"requestCollectQueryCallBack");
	}

	/**
	 * 获取被归集列表回调
	 */
	public void requestCollectQueryCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultData = (Map<String, Object>) biiResponseBody
				.getResult();
		String totalItems = (String) resultData.get(Collect.totalItems);
		int total = Integer.valueOf(totalItems);
		if (total > collect_size) {
			collect_size = total;
			requestPsnCBCollectQuery(CollectStatusType.ALL_STATUS, 0,
					collect_size, true);
		} else {
			// BiiHttpEngine.dissMissProgressDialog();
			mAccountData.put(Collect.cbAccBankNum,
					resultData.get(Collect.cbAccBankNum));
			mAccountData
					.put(Collect.cbAccNum, resultData.get(Collect.cbAccNum));
			mAccountData.put(Collect.cbAccCard,
					resultData.get(Collect.cbAccCard));
			mAccountData
					.put(Collect.cbMobile, resultData.get(Collect.cbMobile));
			mAccountData.put(Collect.cbAccName,
					resultData.get(Collect.cbAccName));
			// cbMobileParam = (String) resultData.get(Collect.cbMobile);
			// String cbMobile = (String) resultData.get(Collect.cbMobile);
			// CollectData.getInstance().addMobile(mPayeeAccount, cbMobile);
			List<Map<String, Object>> listData = (List<Map<String, Object>>) resultData
					.get(Collect.list);
			CollectData.getInstance()
					.setCollectAccount(mPayeeAccount, listData);
			requestPsnCBRestBankview();
		}
	}

	/**
	 * 根据中行账户查询协议信息
	 */
	public void requestPsnCBRestBankview() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Collect.PsnCBRestBankview);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Collect.accountId, mAccountData.get(Comm.ACCOUNT_ID));
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnCBRestBankviewCallback");
	}

	/**
	 * 中行账户查询协议信息回调
	 */
	public void requestPsnCBRestBankviewCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultData = (Map<String, Object>) biiResponseBody
				.getResult();
		// 设置已设置被归集账户
		List<Map<String, Object>> collectDatas = CollectData.getInstance()
				.getCollectAccount(mPayeeAccount);
		setCollectAdapter(collectDatas);

		List<Map<String, Object>> queryList = (List<Map<String, Object>>) resultData
				.get(Collect.queryList);
		List<Map<String, Object>> payList = (List<Map<String, Object>>) resultData
				.get(Collect.payList);
		// 过滤方式
		// 根据卡号的唯一性。
		// map<银行号，set<银行卡>>
		LinkedHashMap<String, Map<String, Object>> unCollectData = new LinkedHashMap<String, Map<String, Object>>();
		if(payList != null){
			for (Map<String, Object> pay : payList) {
				Map<String, Object> unCollect = new HashMap<String, Object>();
				String account = (String) pay.get(Collect.retAct);
				unCollect.put(Collect.payerAcctBankNo, pay.get(Collect.retBankNo));
				unCollect.put(Collect.payerAccBankName, pay.get(Collect.retBank));
				unCollect.put(Collect.payerAccount, account);
				unCollect.put(Collect.payerAccountName, pay.get(Collect.retName));
				unCollect.put(Collect.retActype, pay.get(Collect.retActype));
				unCollect.put(Collect.status, pay.get(Collect.payStatus));
				unCollect.put("ispay", Boolean.toString(true));
				unCollectData.put(account, unCollect);
			}
		}
		
		if(queryList != null){
			for (Map<String, Object> query : queryList) {
				String account = (String) query.get(Collect.rptAct);
				if (unCollectData.containsKey(account)) {
					Map<String, Object> unCollect = unCollectData.get(account);
					unCollect.put("isquery", Boolean.toString(true));
				} else {
					Map<String, Object> unCollect = new HashMap<String, Object>();
					unCollect.put(Collect.payerAcctBankNo,
							query.get(Collect.rptBankNo));
					unCollect.put(Collect.payerAccBankName,
							query.get(Collect.rptBank));
					unCollect.put(Collect.payerAccount, query.get(Collect.rptAct));
					unCollect.put(Collect.payerAccountName,
							query.get(Collect.rptName));
					unCollect.put(Collect.status, query.get(Collect.queryStatus));
					unCollect.put("isquery", Boolean.toString(true));
					unCollectData.put(account, unCollect);
				}
			}
		}

		if(collectDatas != null){
			for (Map<String, Object> collect : collectDatas) {
				String account = (String) collect.get(Collect.payerAccount);
				if (unCollectData.containsKey(account)) {
					unCollectData.remove(account);
				}
			}
		}

		Collection<Map<String, Object>> values = unCollectData.values();
		List<Map<String, Object>> unCollectList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : values) {
			unCollectList.add(map);
		}

		CollectData.getInstance().setQueryList(mPayeeAccount, queryList);
		CollectData.getInstance().setPayList(mPayeeAccount, payList);
		CollectData.getInstance().setUnCollectAccount(mPayeeAccount,
				unCollectList);
		setUnCollectAdapter(unCollectList);
	}

	private void setListener() {
		mCollectSelectView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isCollectFlag) {
					mCollectSelectView.setVisibility(View.GONE);
					mCollectCancleView.setVisibility(View.VISIBLE);
					mCollectFinishView.setVisibility(View.VISIBLE);
					mCollectAdapter.setSelect(true);
				} else {
					// BaseDroidApp.getInstanse().showInfoMessageDialog("为避免重复提交，2分钟内无法再次归集");
					CustomDialog.toastInCenter(CollectListActivity.this,
							"为避免重复提交，2分钟内无法再次归集");
				}
			}
		});

		mCollectCancleView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCollectSelectView.setVisibility(View.VISIBLE);
				mCollectCancleView.setVisibility(View.GONE);
				mCollectFinishView.setVisibility(View.GONE);
				mCollectAdapter.setSelect(false);
				BottomButtonUtils.setSingleLineStyleRed(mCollectSelectView);
			}
		});
		mCollectFinishView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<Map<String, Object>> selectData = mCollectAdapter
						.getSelectData();
				if (selectData != null) {
					if (selectData.isEmpty()) {
						// TODO 提示请选择被归集账户
						BaseDroidApp.getInstanse().createDialog(null,
								"请选择被归集账户");
					} else {
						collectOnTimeDialog(selectData);
					}
				}
			}
		});
		mRadioLayout.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				refreshView();
			}
		});
	}

	private void refreshView() {
		if (mCollectView.isChecked()) {// 以设置归集账户
			if (mCollectAdapter != null) {
				if (mCollectAdapter.isEmpty()) {
					findViewById(R.id.bottom_btn_layout).setVisibility(
							View.INVISIBLE);
					mCollectEmptyView.setVisibility(View.VISIBLE);
					mCollectListView.setVisibility(View.GONE);
				} else {
					findViewById(R.id.bottom_btn_layout).setVisibility(
							View.VISIBLE);
					mCollectEmptyView.setVisibility(View.GONE);
					mCollectListView.setVisibility(View.VISIBLE);
				}
				mCollectLayout.setVisibility(View.VISIBLE);
				mUnCollectLayout.setVisibility(View.GONE);
			} else {
				BiiHttpEngine.showProgressDialogCanGoBack();
				requestCommConversationId();
			}
		} else if (mUnCollectView.isChecked()) {// 未设置归集账户
			if (mCollectCancleView.getVisibility() == View.VISIBLE) {
				mCollectCancleView.performClick();
			}
			findViewById(R.id.bottom_btn_layout).setVisibility(View.INVISIBLE);
			if (mUnCollectAdapter != null) {
				if (mUnCollectAdapter.isEmpty()) {
					mUnCollectEmptyView.setVisibility(View.VISIBLE);
					mUnCollectListView.setVisibility(View.GONE);
				} else {
					mUnCollectEmptyView.setVisibility(View.GONE);
					mUnCollectListView.setVisibility(View.VISIBLE);
				}
				mCollectLayout.setVisibility(View.GONE);
				mUnCollectLayout.setVisibility(View.VISIBLE);
			} else {
				BiiHttpEngine.showProgressDialogCanGoBack();
				requestCommConversationId();
			}
		}
	}

	/** 请求被归集列表 */
	private void requestList() {
		requestPsnCBCollectQuery(CollectStatusType.ALL_STATUS, 0, collect_size,
				true);
	}

	/**
	 * 以设置归集账户列表事件
	 */
	private OnItemClickListener collectListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Map<String, Object> selectMap = mCollectAdapter.getItem(position);
			String payerAccount = (String) selectMap.get(Collect.payerAccount);
			Intent intent = new Intent(CollectListActivity.this,
					CollectAccountActivity.class);
			intent.putExtra(Collect.cbAccCard, mPayeeAccount);
			intent.putExtra(Collect.payerAccount, payerAccount);
			intent.putExtra(Collect.cbMobile, cbMobileParam);
			startActivityForResult(intent, request_account_Code);
		}
	};

	/**
	 * 未设置归集账户列表事件
	 */
	private OnItemClickListener unCollectListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Map<String, Object> selectMap = mUnCollectAdapter.getItem(position);
			String unaccount = (String) selectMap.get(Collect.payerAccount);

			String isquery = (String) selectMap.get("isquery");
			if (isquery == null) {
				BaseDroidApp.getInstanse().createDialog(null,
						"被归集账户须同时签约查询协议和支付协议，请到网银开通查询协议");
				return;
			}

			String ispay = (String) selectMap.get("ispay");
			if (ispay == null) {
				BaseDroidApp.getInstanse().createDialog(null,
						"被归集账户须同时签约查询协议和支付协议，请到网银开通支付协议");
				return;
			}

			Intent intent = new Intent(CollectListActivity.this,
					CollectSettingActivity.class);
			intent.putExtra(Collect.cbAccCard, mPayeeAccount);
			intent.putExtra(Collect.payerAccount, unaccount);
			intent.putExtra(Collect.cbMobile, cbMobileParam);
			startActivityForResult(intent, request_setting_Code);
		}
	};

	private void setCollectAdapter(List<Map<String, Object>> data) {
		if (mCollectAdapter == null) {
			mCollectAdapter = new AccountListAdapter(this, data);
			mCollectListView.setAdapter(mCollectAdapter);
		} else {
			mCollectAdapter.setData(data);
			mCollectAdapter.notifyDataSetChanged();
		}
		refreshView();
	}

	private void setUnCollectAdapter(List<Map<String, Object>> data) {
		if (mUnCollectAdapter == null) {
			mUnCollectAdapter = new AccountListAdapter(this, data);
			mUnCollectListView.setAdapter(mUnCollectAdapter);
		} else {
			mUnCollectAdapter.setData(data);
			mUnCollectAdapter.notifyDataSetChanged();
		}
		refreshView();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			if (requestCode == request_setting_Code) {
				// TODO 刷新页面
			}
		}

		if (resultCode == refresh_collect_list) {
			mCollectView.setChecked(true);
			BiiHttpEngine.showProgressDialogCanGoBack();
			requestList();
		} else if (resultCode == refresh_uncollect_list) {
			mUnCollectView.setChecked(true);
			BiiHttpEngine.showProgressDialogCanGoBack();
			requestList();
		} else if (resultCode == refresh_list) {
			BiiHttpEngine.showProgressDialogCanGoBack();
			requestList();
		}
	}

	private void findView() {
		mRadioLayout = (RadioGroup) viewContent.findViewById(R.id.rg_layout);
		mCollectView = (RadioButton) viewContent.findViewById(R.id.rb_collect);
		mUnCollectView = (RadioButton) viewContent
				.findViewById(R.id.rb_uncollect);

		mCollectListView = (ListView) viewContent.findViewById(R.id.lv_collect);
		mCollectLayout = viewContent.findViewById(R.id.collect_layout);
		mCollectEmptyView = viewContent.findViewById(R.id.tv_collect_prompt);
		mUnCollectListView = (ListView) viewContent
				.findViewById(R.id.lv_uncollect);
		mUnCollectLayout = viewContent.findViewById(R.id.uncollect_layout);
		mUnCollectEmptyView = viewContent
				.findViewById(R.id.tv_uncollect_prompt);

		// mCollectAllView = viewContent.findViewById(R.id.btn_all);
		mCollectSelectView = (Button) viewContent
				.findViewById(R.id.btn_collect);
		mCollectFinishView = (Button) viewContent
				.findViewById(R.id.btn_collect_finish);
		mCollectCancleView = (Button) viewContent
				.findViewById(R.id.btn_collect_cancle);

		mCollectListView.setOnItemClickListener(collectListener);
		mUnCollectListView.setOnItemClickListener(unCollectListener);

		BottomButtonUtils.setSingleLineStyleRed(mCollectSelectView);
	}

}
