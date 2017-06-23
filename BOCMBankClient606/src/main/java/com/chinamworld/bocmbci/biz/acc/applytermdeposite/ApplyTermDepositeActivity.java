package com.chinamworld.bocmbci.biz.acc.applytermdeposite;

import java.util.ArrayList;
import java.util.HashMap;
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

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.biz.acc.adapter.ApplyTermDepositeAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;

/**
 * 申请定期活期账户页面
 * 
 * @author Administrator
 *
 */
public class ApplyTermDepositeActivity extends AccBaseActivity implements
		OnClickListener {

	/** 主布局 */
	private View mainView;
	/** 卡列表 */
	private ListView lvBank;
	/** 下一步 */
	private Button btnNext;
	/** 列表选中条目 */
	private int selectedPosition = -1;
	private ApplyTermDepositeAdapter mAdapter;
	/** 所有的账户数据 */
	private List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
	/** 绑定的介质账户数据 */
	private List<Map<String, Object>> list;
	/** 选中绑定的介质账户信息 */
	private Map<String, Object> mMap = new HashMap<String, Object>();
	/** 存款管理申请账户标示 */
	private int interestRateFlag;
	private boolean isManageFlag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(R.string.acc_apply_title);
		// 添加布局
		mainView = addView(R.layout.acc_apply_term_deposite);
		gonerightBtn();
		setLeftSelectedPosition("accountManager_7");
		
		init();
		interestRateFlag = this.getIntent().getIntExtra(Dept.APPLICATION_ACCOUNT_FLAG, 0);
		isManageFlag = this.getIntent().getBooleanExtra("isManageFlag", false);
		AccDataCenter.getInstance().setManageFlag(isManageFlag);
		requestAccBankList();
	}

	public void init() {
		lvBank = (ListView) mainView.findViewById(R.id.cardlist);
		btnNext = (Button) mainView.findViewById(R.id.btnnext);
		lvBank.setOnItemClickListener(cardItemLis);
		btnNext.setOnClickListener(this);
	}

	public void initList() {
		if (mAdapter == null) {
			mAdapter = new ApplyTermDepositeAdapter(this, list);
			lvBank.setAdapter(mAdapter);
		} else {
			mAdapter.changeData(list);
		}
	}

	/**
	 * 请求绑定介质账户的列表信息
	 */
	public void requestAccBankList() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.QRY_ACC_LIST_API);
		// Map<String,Object> parms=new HashMap<String, Object>();
		// List<String> accountT=new ArrayList<String>();
		// accountT.add(AccBaseActivity.accountTypeList.get(3));
		// parms.put(Acc.ACC_ACCOUNTTYPE_REQ, accountT);
		// biiRequestBody.setParams(parms);
		HttpManager.requestBii(biiRequestBody, this,
				"responseAccBankListCallBack");
	}

	/**
	 * 绑定介质账户返回信息
	 */
	@SuppressWarnings("unchecked")
	public void responseAccBankListCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		mList = (List<Map<String, Object>>) biiResponseBody.getResult();
		BaseHttpEngine.dissMissProgressDialog();
		
		list= new ArrayList<Map<String, Object>>();
		for (int i = 0; i < mList.size(); i++) {
			String accountType = (String) mList.get(i).get(
					Acc.ACC_ACCOUNTTYPE_RES);
			if (accountType.equals("119")) {
				list.add(mList.get(i));
			}
		}
		// 通讯结束,关闭通讯框
				if (list == null || list.size() == 0) {
					BaseDroidApp.getInstanse().showMessageDialog(
							BaseDroidApp.getInstanse().getCurrentAct()
									.getString(R.string.acc_load_null),
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									BaseDroidApp.getInstanse().dismissErrorDialog();
									ActivityTaskManager.getInstance()
											.removeAllActivity();
								}
							});
					return;
				}
		AccDataCenter.getInstance().setBankAccountList(mList);
		initList();
	}

	public OnItemClickListener cardItemLis = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (selectedPosition == position) {
				mMap = list.get(selectedPosition);
				initList();
				AccDataCenter.getInstance().setChooseBankAccount(mMap);
				return;
			} else {
				selectedPosition = position;
				mAdapter.setSelectedPosition(selectedPosition);
				mMap = list.get(selectedPosition);
				initList();
				AccDataCenter.getInstance().setChooseBankAccount(mMap);
			}

		}

	};

	@Override
	public void onClick(View v) {
		if (selectedPosition > -1) {
			if (interestRateFlag == APPLICATION_ACCOUNT) { // 存款管理申请账户
				Intent intent = new Intent(ApplyTermDepositeActivity.this,
						ApplyUserKnownActivity.class);
				intent.putExtra(Dept.APPLICATION_ACCOUNT_FLAG , interestRateFlag);
				startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
			} else if (isManageFlag){
				Intent intent = new Intent(ApplyTermDepositeActivity.this,
						ApplyUserKnownActivity.class);
				startActivityForResult(intent, 10023);
			} else {
				Intent intent = new Intent(ApplyTermDepositeActivity.this,
						ApplyUserKnownActivity.class);
				startActivity(intent);
			}
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_choose_info));
		}
	}
	
}
