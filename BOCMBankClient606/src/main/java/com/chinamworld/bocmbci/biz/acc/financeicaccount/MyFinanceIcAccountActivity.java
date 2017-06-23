package com.chinamworld.bocmbci.biz.acc.financeicaccount;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.chinamworld.bocmbci.biz.acc.dialogActivity.IcCardDetailDialog;
import com.chinamworld.bocmbci.biz.acc.financeicaccount.transfer.FinanceIcTransferMainActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.BaseSwipeListViewListener;
import com.chinamworld.bocmbci.widget.SwipeListView;

/**
 * 电子现金账户列表页
 * 
 * @author wangmengmeng
 * 
 */
public class MyFinanceIcAccountActivity extends AccBaseActivity {
	/** 电子现金账户列表页 */
	private View view;
	/** 电子现金账户列表 */
	private SwipeListView lv_acc_financeic_list;
	/** 选择的电子现金账户 */
	private Map<String, Object> bankmap;
	/** 账户详情及余额 */
	private Map<String, String> callbackmap;
	/** 账户accountId */
	private String accountId;
	/** 签约账户账号 */
	private String signnum;
	/** 加密控件里的随机数 */
	protected String randomNumber;
	private int detailPosition = 0;
	private FinanceIcAccountAdapter adapter;
	/** 向右滑动位置 */
	private int rightposition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//包含向右滑动的listview 目的是屏蔽左侧菜单的滑动事件
		setContainsSwipeListView(true);
		// 为界面标题赋值
		setTitle(this.getString(R.string.acc_finance_menu_1));
		// 右上角按钮赋值
		setText(this.getString(R.string.acc_rightbtn_trans));
		// 添加布局
		view = addView(R.layout.acc_financeic_account_list);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		setLeftSelectedPosition("accountManager_4");
		// 初始化界面
		init();
	}

	/** 初始化界面 */
	private void init() {
		// 取得账户列表信息
		financeIcAccountList = AccDataCenter.getInstance()
				.getFinanceIcAccountList();
		lv_acc_financeic_list = (SwipeListView) view
				.findViewById(R.id.lv_acc_financeic_account);
		adapter = new FinanceIcAccountAdapter(MyFinanceIcAccountActivity.this,
				financeIcAccountList);
		lv_acc_financeic_list.setLastPositionClickable(true);
		lv_acc_financeic_list.setAllPositionClickable(true);
		lv_acc_financeic_list.setAdapter(adapter);
		// 账户详情查询监听事件—点击编辑图片
		adapter.setOnbanklistItemDetailClickListener(onlistItemDetailClickListener);
		lv_acc_financeic_list.setSwipeListViewListener(swipeListViewListener);
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
				requestSystemDateTime();
				BiiHttpEngine.showProgressDialog();
			}
		}

		@Override
		public void onStartClose(int position, boolean right) {
		}

		@Override
		public void onClickFrontView(int position) {
			detailPosition = position;
			bankmap = financeIcAccountList.get(position);
			accountId = (String) bankmap.get(Acc.ACC_ACCOUNTID_RES);
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
		Intent intent = new Intent(MyFinanceIcAccountActivity.this,
				FinanceIcAccountTransferDetailActivity.class);
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
			Intent intent = new Intent(MyFinanceIcAccountActivity.this,
					FinanceIcAccountTransferDetailActivity.class);
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
			bankmap = financeIcAccountList.get(position);
			accountId = (String) bankmap.get(Acc.ACC_ACCOUNTID_RES);
			// 取得accountid,来获得详情及余额信息
			requestDetail(accountId);
		}
	};

	/** 请求账户详情 */
	public void requestDetail(String accountId) {
		BiiRequestBody biiRequestBody1 = new BiiRequestBody();
		biiRequestBody1.setMethod(Acc.ACC_ICACCOUNTDETAIL_API);
		Map<String, String> paramsmap1 = new HashMap<String, String>();
		paramsmap1.put(Acc.FINANCE_ACCOUNTID_REQ, accountId);
		biiRequestBody1.setParams(paramsmap1);
		// 通讯开始,开启通讯框
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody1, this, "requestDetailCallBack");
	}

	/**
	 * 请求电子现金账户余额以及详细信息
	 * 
	 * @param resultObj
	 */
	public void requestDetailCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		callbackmap = (Map<String, String>) (biiResponseBody.getResult());
		if (StringUtil.isNullOrEmpty(callbackmap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		String accfinancetype = (String) bankmap.get(Acc.ACC_ACCOUNTTYPE_RES);
		if (accfinancetype.equals(LocalData.cardcodeList.get(3))) {
			// 纯IC卡详情
			requestSignNumber(accountId);
		} else if (accfinancetype.equals(LocalData.cardcodeList.get(1))) {
			// 中银系列信用卡IC卡
			// 判断信用卡币种——有第二币种则信用卡购汇还款按钮显示
			BiiHttpEngine.dissMissProgressDialog();
			AccDataCenter.getInstance().setFinanceIcAccountList(
					financeIcAccountList);
			AccDataCenter.getInstance().setChooseBankAccount(bankmap);
			AccDataCenter.getInstance().setCallbackmap(callbackmap);
			Intent intent = new Intent(this, IcCardDetailDialog.class);
			intent.putExtra(ConstantGloble.ACC_POSITION, detailPosition);
			startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
		} else {
			BiiHttpEngine.dissMissProgressDialog();
			AccDataCenter.getInstance().setChooseBankAccount(bankmap);
			AccDataCenter.getInstance().setFinanceIcAccountList(
					financeIcAccountList);
			AccDataCenter.getInstance().setCallbackmap(callbackmap);
			Intent intent = new Intent(this, IcCardDetailDialog.class);
			intent.putExtra(ConstantGloble.ACC_POSITION, detailPosition);
			startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
		}
	}
	/** 请求电子现金账户签约账户账号 */
	public void requestSignNumber(String accountId) {
		BiiRequestBody biiRequestBody2 = new BiiRequestBody();
		biiRequestBody2.setMethod(Acc.ACC_FINANCEICISSIGN_API);
		Map<String, String> paramsmap2 = new HashMap<String, String>();
		paramsmap2.put(Acc.ISSIGN_ACCOUNTID_REQ, accountId);
		biiRequestBody2.setParams(paramsmap2);
		HttpManager.requestBii(biiRequestBody2, this,
				"requestSignNumberCallBack");
	}

	/**
	 * 请求签约账号回调
	 * 
	 * @param resultObj
	 */
	public void requestSignNumberCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		signnum = (String) (biiResponseBody.getResult());
		// // 显示详情页面
		BiiHttpEngine.dissMissProgressDialog();
		AccDataCenter.getInstance().setFinanceIcAccountList(
				financeIcAccountList);
		AccDataCenter.getInstance().setChooseBankAccount(bankmap);
		AccDataCenter.getInstance().setCallbackmap(callbackmap);
		Intent intent = new Intent(this, IcCardDetailDialog.class);
		intent.putExtra(ConstantGloble.ACC_ISMY, signnum);
		intent.putExtra(ConstantGloble.ACC_POSITION, detailPosition);
		startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (resultCode) {
		case RESULT_OK:
			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE) {
				financeIcAccountList = AccDataCenter.getInstance()
						.getFinanceIcAccountList();
				// 对列表内的数据进行刷新
				adapter.notifyDataSetChanged();
			}
			break;

		default:
			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE) {
				financeIcAccountList = AccDataCenter.getInstance()
						.getFinanceIcAccountList();
				// 对列表内的数据进行刷新
				adapter.notifyDataSetChanged();
			}
			break;
		}

	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 充值
			Intent intent = new Intent(MyFinanceIcAccountActivity.this,
					FinanceIcTransferMainActivity.class);
			startActivity(intent);
		}
	};

}
