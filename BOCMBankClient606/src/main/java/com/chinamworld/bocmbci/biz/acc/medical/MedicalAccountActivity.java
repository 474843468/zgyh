package com.chinamworld.bocmbci.biz.acc.medical;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.biz.acc.adapter.FinanceIcAccountAdapter;
import com.chinamworld.bocmbci.biz.acc.dialogActivity.MedicalDetailDialog;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.BaseSwipeListViewListener;
import com.chinamworld.bocmbci.widget.SwipeListView;

/**
 * 医保账户列表页
 * 
 * @author wangmengmeng
 * 
 */
public class MedicalAccountActivity extends AccBaseActivity {
	/** 医保账户列表页 */
	private View view;
	/** 医保账户列表 */
	private SwipeListView lv_acc_medical_list;
	/** 选择的医保账户 */
	private Map<String, Object> bankmap;
	/** 账户详情及余额 */
	private Map<String, Object> callbackmap;
	/** 账户accountId */
	private String accountId;
	private int detailPosition = 0;
	private FinanceIcAccountAdapter adapter;
	/** 向右滑动位置 */
	private int rightposition = 0;
	private boolean isright = false;
	public List<List<String>> queryCashRemitList = new ArrayList<List<String>>();
	public List<List<String>> queryCashRemitCodeList = new ArrayList<List<String>>();
	/** 币种 */
	public final List<String> queryCurrencyList = new ArrayList<String>();
	public final List<String> queryCodeList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 包含向右滑动的listview 目的是屏蔽左侧菜单的滑动事件
		setContainsSwipeListView(true);
		// 为界面标题赋值
		setTitle(this.getString(R.string.acc_medical_menu_1));
		// 添加布局
		view = addView(R.layout.acc_financeic_account_list);
		// 右上角按钮点击事件
		setLeftSelectedPosition("accountManager_6");
		// 初始化界面
		init();
	}

	/** 初始化界面 */
	private void init() {
		// 取得账户列表信息
		medicalAccountList = AccDataCenter.getInstance()
				.getMedicalAccountList();
		lv_acc_medical_list = (SwipeListView) view
				.findViewById(R.id.lv_acc_financeic_account);
		adapter = new FinanceIcAccountAdapter(MedicalAccountActivity.this,
				medicalAccountList);
		lv_acc_medical_list.setLastPositionClickable(true);
		lv_acc_medical_list.setAllPositionClickable(true);
		lv_acc_medical_list.setAdapter(adapter);
		// 账户详情查询监听事件—点击编辑图片
		adapter.setOnbanklistItemDetailClickListener(onlistItemDetailClickListener);
		lv_acc_medical_list.setSwipeListViewListener(swipeListViewListener);
	}

	/** 列表向右滑动事件 */
	BaseSwipeListViewListener swipeListViewListener = new BaseSwipeListViewListener() {
		@Override
		public void onOpened(int position, boolean toRight) {
		}

		@Override
		public void onClosed(int position, boolean fromRight) {
		}

		@Override
		public void onListChanged() {
		}

		@Override
		public void onMove(int position, float x) {
		}

		@Override
		public void onStartOpen(int position, int action, boolean right) {

			if (action == 0) {
				rightposition = position;
				Map<String, Object> bankmap = medicalAccountList.get(position);
				String accountId = (String) bankmap.get(Acc.ACC_ACCOUNTID_RES);
				isright = true;
				requestDetail(accountId);
			}
		}

		@Override
		public void onStartClose(int position, boolean right) {
		}

		@Override
		public void onClickFrontView(int position) {
			detailPosition = position;
			bankmap = medicalAccountList.get(position);
			accountId = (String) bankmap.get(Acc.ACC_ACCOUNTID_RES);
			isright = false;
			// 取得accountid,来获得详情及余额信息
			requestDetail(accountId);
		}

		@Override
		public void onClickBackView(int position) {
		}

		@Override
		public void onDismiss(int[] reverseSortedPositions) {

		}
	};

	/**
	 * 请求系统时间回调
	 */
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BiiHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNull(dateTime)) {
			return;
		}
		Intent intent = new Intent(MedicalAccountActivity.this,
				MedicalAccountTransferDetailActivity.class);
		intent.putExtra(ConstantGloble.ACC_POSITION, rightposition);
		intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
		startActivity(intent);
	}

	/**
	 * 进入账户明细点击事件
	 */
	OnItemClickListener onlistTransferClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(MedicalAccountActivity.this,
					MedicalAccountTransferDetailActivity.class);
			intent.putExtra(ConstantGloble.ACC_POSITION, position);
			startActivity(intent);
		}
	};
	/** 账户详情查询监听事件 */
	protected OnItemClickListener onlistItemDetailClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			detailPosition = position;
			bankmap = medicalAccountList.get(position);
			accountId = (String) bankmap.get(Acc.ACC_ACCOUNTID_RES);
			// 取得accountid,来获得详情及余额信息
			requestDetail(accountId);
		}
	};

	/** 请求账户详情 */
	public void requestDetail(String accountId) {
		BiiRequestBody biiRequestBody1 = new BiiRequestBody();
		biiRequestBody1.setMethod(Acc.ACC_MEDICALACCOUNTDETAIL_API);
		Map<String, String> paramsmap1 = new HashMap<String, String>();
		paramsmap1.put(Acc.MEDICAL_ACCOUNTID_REQ, accountId);
		biiRequestBody1.setParams(paramsmap1);
		// 通讯开始,开启通讯框
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody1, this, "requestDetailCallBack");
	}

	/**
	 * 请求医保账户余额以及详细信息
	 * 
	 * @param resultObj
	 */
	public void requestDetailCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		callbackmap = (Map<String, Object>) (biiResponseBody.getResult());
		if (StringUtil.isNullOrEmpty(callbackmap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		if (isright) {
			// 明细页面
			AccDataCenter.getInstance().setMedicalAccountList(
					medicalAccountList);
			AccDataCenter.getInstance().setMedicalbackmap(callbackmap);
			clearList();
			/**
			 * 账户详情列表信息
			 */
			List<Map<String, Object>> accountDetailList = (List<Map<String, Object>>) (callbackmap
					.get(ConstantGloble.ACC_DETAILIST));
			List<String> currencylist = new ArrayList<String>();
			List<String> codelist = new ArrayList<String>();
			if (!StringUtil.isNullOrEmpty(accountDetailList)) {
				for (int i = 0; i < accountDetailList.size(); i++) {
					String currencyname = (String) accountDetailList.get(i)
							.get(Acc.DETAIL_CURRENCYCODE_RES);
					// 过滤
					if (StringUtil.isNull(LocalData.currencyboci
							.get(currencyname))) {
						currencylist.add(LocalData.Currency.get(currencyname));
						codelist.add(currencyname);
					}
				}
				for (int i = 0; i < accCurrencyList.size(); i++) {
					for (int j = 0; j < currencylist.size(); j++) {
						if (currencylist.get(j).equals(accCurrencyList.get(i))) {
							queryCurrencyList.add(currencylist.get(j));
							queryCodeList.add(codelist.get(j));
							List<String> cashRemitList = new ArrayList<String>();
							List<String> cashRemitCodeList = new ArrayList<String>();
							for (int k = 0; k < accountDetailList.size(); k++) {
								if (currencylist
										.get(j)
										.equals(LocalData.Currency
												.get(accountDetailList
														.get(k)
														.get(Acc.DETAIL_CURRENCYCODE_RES)))) {
									String cash = (String) accountDetailList
											.get(k).get(
													Acc.DETAIL_CASHREMIT_RES);
									cashRemitCodeList.add(cash);
									cashRemitList.add(LocalData.cashMapValue
											.get(cash));

								}

							}
							queryCashRemitList.add(cashRemitList);
							queryCashRemitCodeList.add(cashRemitCodeList);
							break;
						}
					}
				}

			}
			AccDataCenter.getInstance().setQueryCashRemitCodeList(
					queryCashRemitCodeList);
			AccDataCenter.getInstance().setQueryCashRemitList(
					queryCashRemitList);
			AccDataCenter.getInstance().setQueryCodeList(queryCodeList);
			AccDataCenter.getInstance().setQueryCurrencyList(queryCurrencyList);
			requestSystemDateTime();
		} else {
			// 详情页面
			lv_acc_medical_list.setSwipeListViewListener(null);
			BaseHttpEngine.dissMissProgressDialog();
			AccDataCenter.getInstance().setChooseBankAccount(bankmap);
			AccDataCenter.getInstance().setMedicalAccountList(
					medicalAccountList);
			AccDataCenter.getInstance().setMedicalbackmap(callbackmap);
			Intent intent = new Intent(this, MedicalDetailDialog.class);
			intent.putExtra(ConstantGloble.ACC_POSITION, detailPosition);
			startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
		}

	}

	// 清除数据
	public void clearList() {
		if (!StringUtil.isNullOrEmpty(queryCurrencyList)) {
			queryCurrencyList.clear();
		}
		if (!StringUtil.isNullOrEmpty(queryCodeList)) {
			queryCodeList.clear();
		}
		if (!StringUtil.isNullOrEmpty(queryCashRemitCodeList)) {
			queryCashRemitCodeList.clear();
		}
		if (!StringUtil.isNullOrEmpty(queryCashRemitList)) {
			queryCashRemitList.clear();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (resultCode) {
		case RESULT_OK:
			lv_acc_medical_list.setSwipeListViewListener(swipeListViewListener);
			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE) {
				medicalAccountList = AccDataCenter.getInstance()
						.getMedicalAccountList();
				// 对列表内的数据进行刷新
				adapter.notifyDataSetChanged();
			}
			break;

		default:
			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE) {
				medicalAccountList = AccDataCenter.getInstance()
						.getMedicalAccountList();
				// 对列表内的数据进行刷新
				adapter.notifyDataSetChanged();
			}
			break;
		}

	}
}
