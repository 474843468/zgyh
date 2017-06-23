package com.chinamworld.bocmbci.biz.bond.mybond;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Bond;
import com.chinamworld.bocmbci.biz.bond.BondBaseActivity;
import com.chinamworld.bocmbci.biz.bond.BondDataCenter;
import com.chinamworld.bocmbci.biz.bond.adapter.BondGridViewAdapter;
import com.chinamworld.bocmbci.biz.bond.bondtran.SellBondMsgFillActivity;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 我的债券
 * 
 * @author panwe
 * 
 */
public class MyBondListActivity extends BondBaseActivity {

	/** 主布局 */
	private View mainView;
	/** 账户 */
	private LinearLayout accLayout;
	/** 债券账户 */
	private TextView tvBondAcc;
	private TextView tvtitle;
	/** listview */
	private ListView mListView;
	/** 资金账户 */
	private String accountNum;
	private String accountId;
	/*** 托管账号 */
	private String investAcc;
	/** 我的债券列表 */
	private List<Map<String, Object>> myBondList = new ArrayList<Map<String, Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setLeftSelectedPosition("bond_2");
		mainView = (View) LayoutInflater.from(this).inflate(
				R.layout.bond_mybond_list, null);
		btnRight.setVisibility(View.GONE);
		addView(mainView);
		setTitle(this.getString(R.string.bond_mybond_title));
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0,
					0, 0);
		}
		// 请求是否开通投资理财
		requestPsnInvestmentManageIsOpen();
	}

	private void init() {
		tvBondAcc = (TextView) mainView.findViewById(R.id.tv_bondacc);
		tvtitle = (TextView) mainView.findViewById(R.id.prms_acc_info);
		accLayout = (LinearLayout) mainView.findViewById(R.id.headerLayout);
		mListView = (ListView) mainView.findViewById(R.id.listview);
		accLayout.setOnClickListener(accOnclick);
		accLayout.setVisibility(View.VISIBLE);
		String str_tem=this.getString(R.string.bond_mybond_title1);
		String str_acc_num=str_tem+investAcc;
		int str_size=str_tem.length();
		SpannableStringBuilder span_str = new SpannableStringBuilder(str_acc_num);
		span_str.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.fonts_dark_gray)),0,str_size, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		span_str.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.fonts_black)),str_size-1,str_acc_num.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		债券托管账户，改成左灰右黑
//		tvBondAcc.setText(this.getString(R.string.bond_mybond_title1)+ investAcc);
		tvBondAcc.setText(span_str);
		requestMyBondList();
	}

	/** 账户点击事件 */
	private OnClickListener accOnclick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			requestBondActBanlance();
		}

	};

	/** 卖出点击事件 */
	private OnItemClickListener sellClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			BondDataCenter.getInstance().setMyBondDetailMap(
					myBondList.get(position));
			Intent it = new Intent(MyBondListActivity.this,
					SellBondMsgFillActivity.class);
			MyBondListActivity.this.startActivity(it);
		}

	};

	/** 系统时间 */
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		if (StringUtil.isNull(dateTime)) {
			return;
		}
		BondDataCenter.getInstance().setSysTime(dateTime);
		Map<String, Object> accMap = BondDataCenter.getInstance().getAccMap();
		accountNum = (String) accMap.get(Bond.ACCOUNT);
		accountId = (String) accMap.get(Bond.ACCOUNTID);
		investAcc = (String) accMap.get(Bond.INVESTACCOUNT);
		init();
	}
	
	/** 获取我的债券列表 */
	private void requestMyBondList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bond.METHOD_MYBOND_LIST);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "mybondListCallBack");
	}

	/** 债券列表返回处理 */
	public void mybondListCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BaseHttpEngine.dissMissProgressDialog();
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		@SuppressWarnings("unchecked")
		Map<String, Object> saveMap = (Map<String, Object>) result
				.get(Bond.MYBOND_SAVEENTY);
		@SuppressWarnings("unchecked")
		Map<String, Object> tallyMap = (Map<String, Object>) result
				.get(Bond.MYBOND_TALLYENTY);

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> saveList = (List<Map<String, Object>>) saveMap
				.get(Bond.MYBOND_BALANCELIST);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> tallyList = (List<Map<String, Object>>) tallyMap
				.get(Bond.MYBOND_BALANCELIST);

		if ((saveList == null || saveList.size() == 0)
				&& (tallyList == null || tallyList.size() == 0)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.bond_comm_error));
			return;
		}
		myBondList.clear();
		myBondList.addAll(saveList);
		myBondList.addAll(tallyList);
		tvtitle.setVisibility(View.VISIBLE);

		BondGridViewAdapter mAdapter = new BondGridViewAdapter(this, myBondList);
		mListView.setAdapter(mAdapter);
		mAdapter.setImageViewClick(sellClick);
	}

	/** 资金账户余额查询 */
	private void requestBondActBanlance() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bond.METHOD_BONDACC_BANLANCE);
		biiRequestBody.setParams(null);
		HttpManager
				.requestBii(biiRequestBody, this, "baondAccBanlanceCallBack");
	}

	/** 资金账户返回处理 */
	public void baondAccBanlanceCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		if (result == null) {
			return;
		}
		String avaiBanlance = (String) result.get(Bond.BOND_VAAILABLANCE);
		String isexist = (String) result.get(Bond.BOND_ISEXIST);

		Intent it = new Intent(this, AccInfoDialogActivity.class);
		it.putExtra(BANKACCTNUM, accountNum);
		it.putExtra(BANKACCTID, accountId);
		it.putExtra(BONDACCT, investAcc);
		it.putExtra("BALANCE", avaiBanlance);
		it.putExtra("ISEXIST", isexist);
		this.startActivity(it);
	}
}
