package com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdAccBaseActivity;
import com.chinamworld.bocmbci.biz.crcd.adapter.CrcdVirtualCardAdapter;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.MyCrcdDetailActivity;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;

/**
 * 虚拟银行卡详情
 * 
 * @author huangyuchao
 * 
 */
public class VirtualBCDetailActivity extends CrcdAccBaseActivity {
	private View view = null;

	Button sureButton;

	/** 信用卡列表 */
	private ListView myListView;

	CrcdVirtualCardAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_virtual_myxunicard));
		if (view == null) {
			view = addView(R.layout.crcd_virtual_list);
		}
		init();
	}

	static String accountId;
	static String accountNumber;
	static String currencyCode;
	static String nickName;
	private int accNum = 0;

	TextView tv_service_title;

	static List<Map<String, Object>> virCardList;

	/** 初始化界面 */
	private void init() {

		tv_service_title = (TextView) findViewById(R.id.tv_service_title);
		tv_service_title.setText(this
				.getString(R.string.mycrcd_select_chakan_xuni_creditcard));

		sureButton = (Button) findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (accNum == 0) {
					String errorInfo = getResources().getString(
							R.string.crcd_notselectacard_error);
					BaseDroidApp.getInstanse().showInfoMessageDialog(errorInfo);
					return;
				} else {
					Intent it = new Intent(VirtualBCDetailActivity.this,
							MyCrcdDetailActivity.class);
					it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
					it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
					it.putExtra(Crcd.CRCD_CURRENCYCODE, currencyCode);
					startActivity(it);
				}
			}
		});

		virCardList = MyVirtualBCListActivity.virCardList;
		myListView = (ListView) view.findViewById(R.id.crcd_mycrcdlist);

		adapter.setOncrcdDetailListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent it = new Intent(VirtualBCDetailActivity.this,
						VirtualBCDetailActivity.class);
				startActivity(it);
			}
		});
	}

//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//	}

	@Override
	protected void onDestroy() {
		BaseHttpEngine.dissMissProgressDialog();
		super.onDestroy();
	}
}
