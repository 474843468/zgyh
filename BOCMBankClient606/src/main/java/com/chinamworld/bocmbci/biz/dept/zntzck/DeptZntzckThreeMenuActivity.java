package com.chinamworld.bocmbci.biz.dept.zntzck;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 智能通知存款---三级菜单页面 */
public class DeptZntzckThreeMenuActivity extends DeptBaseActivity {
	private static final String TAG = "DeptYdzcQueryActivity";
	private LinearLayout tabcontent;// 主Activity显示
	private View queryView = null;
	private View queryLayout = null;
	private View signLayout = null;
	/** 1-查询，2-签约 */
	private int tag = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.dept_zntzck_menu));
		LogGloble.d(TAG, "onCreate");
		ibRight.setVisibility(View.GONE);
		ibBack.setVisibility(View.VISIBLE);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		queryView = LayoutInflater.from(this).inflate(R.layout.dept_zntzck_menu, null);
		tabcontent.addView(queryView);
		queryLayout = findViewById(R.id.menu11);
		signLayout = findViewById(R.id.menu4);
		// 查询
		queryLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 查询签约账户
				tag = 1;
				BaseHttpEngine.showProgressDialog();
				String[] s = { ConstantGloble.ACC_TYPE_BRO, ConstantGloble.ACC_TYPE_ORD, ConstantGloble.ACC_TYPE_RAN };
				requestPsnCommonQueryAllChinaBankAccount(s);
			}
		});
		// 签约
		signLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseHttpEngine.showProgressDialog();
				tag = 2;
				String[] s = { ConstantGloble.ACC_TYPE_BRO, ConstantGloble.ACC_TYPE_ORD, ConstantGloble.ACC_TYPE_RAN };
				requestPsnCommonQueryAllChinaBankAccount(s);
			}
		});
		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent(DeptZntzckThreeMenuActivity.this, MainActivity.class);
//				startActivity(intent);
				goToMainActivity();
			}
		});
	}

	/** 查询转账账户列表---回调 */
	@Override
	public void requestPsnCommonQueryAllChinaBankAccountCallBack(Object resultObj) {
		super.requestPsnCommonQueryAllChinaBankAccountCallBack(resultObj);
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = response.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
		List<Map<String, String>> resultList = (List<Map<String, String>>) body.getResult();
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		List<Map<String, String>> list = getRMBAccList(resultList);
		if (StringUtil.isNullOrEmpty(list)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		Intent intent = new Intent();
		if (tag == 1) {
			// 查询
			intent.setClass(DeptZntzckThreeMenuActivity.this, DeptZntzckQueryActivity.class);
		} else if (tag == 2) {
			// 签约
			intent.setClass(DeptZntzckThreeMenuActivity.this, DeptZntzckSignSelectActivity.class);
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.DEPT_RESULTLIST_LIST, list);
		startActivity(intent);
		BaseHttpEngine.dissMissProgressDialog();
	}

	/** 得到人民币账户列表 */
	private List<Map<String, String>> getRMBAccList(List<Map<String, String>> resultList) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		int len = resultList.size();
		for (int i = 0; i < len; i++) {
			Map<String, String> map = resultList.get(i);
			String currencyCode = map.get(Dept.DEPT_CURRENCYCODE_RES);
			if (!StringUtil.isNull(currencyCode) && LocalData.rmbCodeList.contains(currencyCode)) {
				list.add(map);
			}
		}
		return list;
	}
}
