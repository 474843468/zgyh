package com.chinamworld.bocmbci.biz.acc.medical;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 医保账户菜单选择页面
 * 
 * @author wangmengmeng
 * 
 */
public class MedicalMenuActivity extends AccBaseActivity {
	/** 电子现金账户列表页 */
	private View view;
	// 点击三级菜单后判断进入哪个功能
	private int go_menu_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.acc_medical_menu_main));
		// 添加布局
		view = addView(R.layout.acc_medical_menu_view);
		gonerightBtn();
		// 初始化界面
		init();
		setLeftSelectedPosition("accountManager_6");
	}

	/** 初始化界面 */
	private void init() {

		/** 我的医保账户 */
		LinearLayout acc_financeic_account = (LinearLayout) view
				.findViewById(R.id.acc_financeic_account);
		acc_financeic_account.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 进入我的电子现金账户模块
				go_menu_id = 1;
				requestMedicalAccountList();
			}
		});
		/** 账户明细查询 */
		LinearLayout acc_financeic_transfer_detail = (LinearLayout) view
				.findViewById(R.id.acc_financeic_transfer_detail);
		acc_financeic_transfer_detail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 进入账户明细查询页面
				go_menu_id = 2;
				requestMedicalAccountList();
			}
		});
	}

	@Override
	public void requestMedicalAccountListCallBack(Object resultObj) {
		super.requestMedicalAccountListCallBack(resultObj);
		medicalAccountList = AccDataCenter.getInstance()
				.getMedicalAccountList();
		click();
	}

	/** 筛选列表——进入详细功能模块 */
	public void click() {
		if (StringUtil.isNullOrEmpty(medicalAccountList)) {
			// 通讯结束,关闭通讯框
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_medical_null));
			return;
		}
		// 过滤一卡双账户——医保账户
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : medicalAccountList) {
			String ismedical = (String) map.get(Acc.ACC_ISMEDICALACCOUNT_RES);
			if (StringUtil.isNull(ismedical)) {
				continue;
			}
			if (ismedical.equals(ConstantGloble.ACC_FINANCEIC_ISECASH_ONE)) {
				// 一卡双账户
				list.add(map);
			}
		}
		if (StringUtil.isNullOrEmpty(list)) {
			// 通讯结束,关闭通讯框
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_medical_null));
			return;
		}
		AccDataCenter.getInstance().setMedicalAccountList(list);
		if (go_menu_id == 1) {
			// 进入我的医保账户模块
			go_menu_id = 0;
			BiiHttpEngine.dissMissProgressDialog();
			Intent intent = new Intent(MedicalMenuActivity.this,
					MedicalAccountActivity.class);
			startActivity(intent);
		} else if (go_menu_id == 2) {
			// 进入账户明细查询页面
			go_menu_id = 0;
			Map<String, Object> bankmap = list.get(0);
			String accountId = (String) bankmap.get(Acc.ACC_ACCOUNTID_RES);

			requestDetail(accountId);

		}
	}

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
		Map<String, Object> callbackmap = (Map<String, Object>) (biiResponseBody
				.getResult());
		if (StringUtil.isNullOrEmpty(callbackmap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		// 明细页面
		AccDataCenter.getInstance().setMedicalbackmap(callbackmap);
		/**
		 * 账户详情列表信息
		 */
		List<Map<String, Object>> accountDetailList = (List<Map<String, Object>>) (callbackmap
				.get(ConstantGloble.ACC_DETAILIST));
		List<String> currencylist = new ArrayList<String>();
		List<String> codelist = new ArrayList<String>();
		List<List<String>> queryCashRemitList = new ArrayList<List<String>>();
		List<List<String>> queryCashRemitCodeList = new ArrayList<List<String>>();
		/** 币种 */
		List<String> queryCurrencyList = new ArrayList<String>();
		List<String> queryCodeList = new ArrayList<String>();
		if (!StringUtil.isNullOrEmpty(accountDetailList)) {
			for (int i = 0; i < accountDetailList.size(); i++) {
				String currencyname = (String) accountDetailList.get(i).get(
						Acc.DETAIL_CURRENCYCODE_RES);
				// 过滤
				if (StringUtil.isNull(LocalData.currencyboci.get(currencyname))) {
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
							if (currencylist.get(j).equals(
									LocalData.Currency.get(accountDetailList
											.get(k)
											.get(Acc.DETAIL_CURRENCYCODE_RES)))) {
								String cash = (String) accountDetailList.get(k)
										.get(Acc.DETAIL_CASHREMIT_RES);
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
		AccDataCenter.getInstance().setQueryCashRemitList(queryCashRemitList);
		AccDataCenter.getInstance().setQueryCodeList(queryCodeList);
		AccDataCenter.getInstance().setQueryCurrencyList(queryCurrencyList);
		requestSystemDateTime();

	}

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
		Intent intent = new Intent(MedicalMenuActivity.this,
				MyMedicalAccountTransferDetailActivity.class);
		intent.putExtra(ConstantGloble.ACC_POSITION, 0);
		intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
		startActivity(intent);
	}
}
