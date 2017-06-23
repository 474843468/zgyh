package com.chinamworld.bocmbci.biz.acc.financeicaccount;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.biz.acc.financeicaccount.transfer.FinanceIcTransferMainActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 电子现金菜单选择页面
 * 
 * @author wangmengmeng
 * 
 */
public class FinanceMenuActivity extends AccBaseActivity {
	/** 电子现金账户列表页 */
	private View view;
	// 点击三级菜单后判断进入哪个功能
	private int go_menu_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.acc_finance_menu_main));
		// 添加布局
		view = addView(R.layout.acc_financeic_menu_view);
		setText(this.getString(R.string.acc_rightbtn_trans));
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		// 初始化界面
		init();
		setLeftSelectedPosition("accountManager_4");
	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 充值
			go_menu_id = 2;
			Intent intent = new Intent(FinanceMenuActivity.this,
					FinanceIcTransferMainActivity.class);
			startActivity(intent);
		}
	};

	/** 初始化界面 */
	private void init() {

		/** 我的电子现金账户 */
		LinearLayout acc_financeic_account = (LinearLayout) view
				.findViewById(R.id.acc_financeic_account);
		acc_financeic_account.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 进入我的电子现金账户模块
				go_menu_id = 1;
				requestFinanceIcAccountList();
			}
		});
		/** 电子现金账户充值 */
		LinearLayout acc_financeic_transfer = (LinearLayout) view
				.findViewById(R.id.acc_financeic_transfer);
		acc_financeic_transfer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 进入电子现金账户充值
				go_menu_id = 2;
				Intent intent = new Intent(FinanceMenuActivity.this,
						FinanceIcTransferMainActivity.class);
				startActivity(intent);
			}
		});
		/** 账户明细查询 */
		LinearLayout acc_financeic_transfer_detail = (LinearLayout) view
				.findViewById(R.id.acc_financeic_transfer_detail);
		acc_financeic_transfer_detail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 进入账户明细查询页面
				go_menu_id = 3;
				requestFinanceIcAccountList();
			}
		});
	}

	@Override
	public void requestFinanceIcAccountListCallBack(Object resultObj) {
		super.requestFinanceIcAccountListCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		financeIcAccountList = AccDataCenter.getInstance()
				.getFinanceIcAccountList();
		click();
	}

	/** 筛选列表——进入详细功能模块 */
	public void click() {
		financeIcAccountList = chooseList(financeIcAccountList);
		if (financeIcAccountList == null || financeIcAccountList.size() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					FinanceMenuActivity.this
							.getString(R.string.acc_financeic_null));
			return;
		}
		AccDataCenter.getInstance().setFinanceIcAccountList(
				financeIcAccountList);
		if (go_menu_id == 1) {
			// 进入我的电子现金账户模块
			go_menu_id = 0;
			Intent intent = new Intent(FinanceMenuActivity.this,
					MyFinanceIcAccountActivity.class);
			startActivity(intent);
		} else if (go_menu_id == 2) {
			// 进入电子现金账户充值
			go_menu_id = 0;

		} else if (go_menu_id == 3) {
			// 进入账户明细查询页面
			go_menu_id = 0;
			Intent intent = new Intent(FinanceMenuActivity.this,
					MyFinanceIcAccountTransferDetailActivity.class);
			intent.putExtra(ConstantGloble.ACC_POSITION, 0);
			startActivity(intent);
		}
	}

	/** 筛选列表 */
	public List<Map<String, Object>> chooseList(List<Map<String, Object>> list) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			// 判断
			// isECashAccount 类型119、103、104这个值是1的话就是电子现金账户
			// 新建签约300的时候纯IC卡
			String acc_type = (String) list.get(i).get(Acc.ACC_ACCOUNTTYPE_RES);
			String isECashAccount = (String) list.get(i).get(
					Acc.ACC_ISECASHACCOUNT_RES);
			if (StringUtil.isNull(isECashAccount)) {
				continue;
			}
			if (acc_type.equalsIgnoreCase(AccBaseActivity.accountTypeList
					.get(3))
					&& isECashAccount
							.equals(ConstantGloble.ACC_FINANCEIC_ISECASH_ONE)) {
				// 119
				resultList.add(list.get(i));
			}
			if (acc_type.equals(AccBaseActivity.accountTypeList.get(1))
					&& isECashAccount
							.equals(ConstantGloble.ACC_FINANCEIC_ISECASH_ONE)) {
				// 103
				resultList.add(list.get(i));
			}
			if (acc_type.equalsIgnoreCase(AccBaseActivity.accountTypeList
					.get(2))
					&& isECashAccount
							.equals(ConstantGloble.ACC_FINANCEIC_ISECASH_ONE)) {
				// 104
				resultList.add(list.get(i));
			}
			if (acc_type.equals(AccBaseActivity.accountTypeList.get(13))) {
				// 300
				resultList.add(list.get(i));
			}
		}
		return resultList;
	}
}
