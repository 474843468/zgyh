package com.chinamworld.bocmbci.biz.thridmanage.openacct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.thridmanage.BiiConstant.AccoutType;
import com.chinamworld.bocmbci.biz.thridmanage.ThirdDataCenter;
import com.chinamworld.bocmbci.biz.thridmanage.ThirdManagerBaseActivity;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;

/**
 * 预约开户
 * 
 * @author panwe
 * 
 */
public class OpenAccActivity extends ThirdManagerBaseActivity implements OnClickListener {

	/** 主布局 **/
	private View viewContent;
	/** 开户标识 */
	private boolean isOpenAcc = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 添加布局
		viewContent = LayoutInflater.from(this).inflate(R.layout.third_openacc, null);
		addView(viewContent);
		setTitle(this.getString(R.string.third_openacc_and_query));
		setLeftSelectedPosition("thirdMananger_4");
		btnRight.setVisibility(View.GONE);

		LinearLayout layoutAccOpen = (LinearLayout) viewContent.findViewById(R.id.layout_acc_open);
		LinearLayout layoutAccQuery = (LinearLayout) viewContent.findViewById(R.id.layout_acc_query);

		layoutAccOpen.setOnClickListener(this);
		layoutAccQuery.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 预约开户
		case R.id.layout_acc_open:
			isOpenAcc = true;
			break;
		// 预约开户历史查询
		case R.id.layout_acc_query:
			isOpenAcc = false;
			break;
		}
		getAccList();
	}

	// 获取卡列表
	private void getAccList() {
		List<String> paramslist = new ArrayList<String>();
//		paramslist.add(Third.accountTypeList.get(3));
		paramslist.add(AccoutType.CHANGCHENG_DIANZI_CARD_CODE);
		BaseHttpEngine.showProgressDialog();
		getAllBankList(paramslist/*, false*/);
	}

	/** 获取账户列表返回 */
	@Override
	public void allBankAccListCallBack(Object resultObj) {
		super.allBankAccListCallBack(resultObj);
		List<Map<String, Object>> bankAccountList = ThirdDataCenter.getInstance().getBankAccountList();
		if (bankAccountList == null || bankAccountList.isEmpty()) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.third_common_error));
		} else {
			Intent it = new Intent(this, AcctOpenCardListActivity.class);
			if (isOpenAcc) {
				it.putExtra("action", "open");
			} else {
				it.putExtra("action", "query");
			}
			startActivity(it);
		}
	}
}
