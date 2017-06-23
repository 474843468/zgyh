package com.chinamworld.bocmbci.biz.tran.managetrans.managepayee;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.ManageTransBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.managetrans.ManageTransActivity;
import com.chinamworld.bocmbci.biz.tran.managetrans.adapter.ManageBocPayeeAdapter;
import com.chinamworld.bocmbci.biz.tran.managetrans.adapter.ManageEbpsBankPayeeAdapter;
import com.chinamworld.bocmbci.biz.tran.managetrans.adapter.ManageOtherBankPayeeAdapter;
import com.chinamworld.bocmbci.biz.tran.mytransfer.addpayee.WritePayeeInfoActivity1;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 收款人管理列表
 * 
 * @author
 * 
 */
public class PayeeListActivity extends ManageTransBaseActivity {

	private RadioGroup payeeRg = null;
	private RadioButton bocPayeeRb, otherBankPayeeRb, otherBankEbpsPayeeRb;

	private RelativeLayout bocLayout;
	private RelativeLayout otherLayout;
	private RelativeLayout ebpsLayout;

	private ListView bocPayeeLv, otherBankPayeeLv, ebpsPayeeLv;

	List<Map<String, Object>> queryPayeeResult = null;
	/** 中行 收款人详情 */
	private List<Map<String, Object>> bocPayeeResultList = null;
	/** 他行 收款人详情 */
	private List<Map<String, Object>> otherBankPayeeResultList = null;
	/** 实时 收款人详情 */
	private List<Map<String, Object>> ebpsPayeeResultList = null;
	private ManageBocPayeeAdapter bocAdapter;
	private ManageOtherBankPayeeAdapter otherBankAdapter;
	private ManageEbpsBankPayeeAdapter ebpsAdapter;
	/** 是否修改完进入 */
	private String isModify = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.manage_payee));
		View view = mInflater.inflate(R.layout.tran_manage_payee_list_activity,
				null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		Button mTopRightBtn = (Button) findViewById(R.id.ib_top_right_btn);
		mTopRightBtn.setVisibility(View.VISIBLE);
		mTopRightBtn.setText(this.getString(R.string.tran_acc_in_top_right));
		mTopRightBtn.setOnClickListener(rightClick);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent(PayeeListActivity.this,
						ManageTransActivity.class);
				startActivity(intent);
				finish();
			}
		});
		setupView();
		initData();
//		requestForQueryPayeeList();
	}

	/**
	 * 新增收款人
	 */
	OnClickListener rightClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {

			Intent intent2 = new Intent(PayeeListActivity.this,
					WritePayeeInfoActivity1.class);
			intent2.putExtra(ISFROMMANAGE, true);
			startActivityForResult(intent2, 1);

		}
	};

	private void initData() {

		payeeRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (bocPayeeRb.isChecked()) {// 中行收款人
					bocLayout.setVisibility(View.VISIBLE);
					otherLayout.setVisibility(View.GONE);
					ebpsLayout.setVisibility(View.GONE);
					bocPayeeLv.setVisibility(View.INVISIBLE);
					requestForQueryPayeeList();

				} else if (otherBankPayeeRb.isChecked()) {// 跨行收款人
					bocLayout.setVisibility(View.GONE);
					otherLayout.setVisibility(View.VISIBLE);
					ebpsLayout.setVisibility(View.GONE);
					otherBankPayeeLv.setVisibility(View.INVISIBLE);
					requestForQueryPayeeList();
				} else if (otherBankEbpsPayeeRb.isChecked()) {// 跨行实时收款人
					bocLayout.setVisibility(View.GONE);
					otherLayout.setVisibility(View.GONE);
					ebpsLayout.setVisibility(View.VISIBLE);
					ebpsPayeeLv.setVisibility(View.INVISIBLE);
					requestBIIForTran(
							Tran.PSNEBPSREALTIMEPAYMENTQUERYPAYEE_API,
							new HashMap<String, Object>(),
							"PsnEbpsRealTimePaymentQueryPayeeCallBack");

				}

			}
		});
		isModify = this.getIntent().getStringExtra(ISMODIFYPAYEE);
		if (!StringUtil.isNullOrEmpty(isModify)) {
			if (isModify.equals("bocIn")) {
				bocPayeeRb.setChecked(true);
			} else if (isModify.equals("bocOther")) {
				otherBankPayeeRb.setChecked(true);
			} else if (isModify.equals("bocEbps")) {
				otherBankEbpsPayeeRb.setChecked(true);
			}
		} else {
			bocPayeeRb.setChecked(true);
		}
	}

	private OnItemClickListener bocPayeeListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (view != null) {
				Map<String, Object> bocPayeeResulMap = bocPayeeResultList
						.get(position);
				TranDataCenter.getInstance().setCurPayeeMap(bocPayeeResulMap);
				Intent intent = new Intent(PayeeListActivity.this,
						PayeeDetailActivity1.class);
				startActivityForResult(intent, 1);
			}
		}
	};
	private OnItemClickListener otherBankPayeeListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (view != null) {
				Map<String, Object> otherBankPayeeResultMap = otherBankPayeeResultList
						.get(position);
				TranDataCenter.getInstance().setCurPayeeMap(
						otherBankPayeeResultMap);
				Intent intent = new Intent(PayeeListActivity.this,
						PayeeDetailActivity1.class);
				startActivityForResult(intent, 1);
			}
		}
	};
	private OnItemClickListener ebpsPayeeListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (view != null) {
				Map<String, Object> otherBankPayeeResultMap = ebpsPayeeResultList
						.get(position);
				TranDataCenter.getInstance().setCurPayeeMap(
						otherBankPayeeResultMap);
				Intent intent = new Intent(PayeeListActivity.this,
						EbpsPayeeDetailActivity1.class);
				startActivityForResult(intent, 1);
			}
		}
	};

	/**
	 * 初始化加载XML里的控件
	 */
	private void setupView() {

		payeeRg = (RadioGroup) findViewById(R.id.rg_manage_payee);
		bocPayeeRb = (RadioButton) findViewById(R.id.rb_left_manage_payee);
		otherBankPayeeRb = (RadioButton) findViewById(R.id.rb_middle_manage_payee);
		otherBankEbpsPayeeRb = (RadioButton) findViewById(R.id.rb_right_manage_payee);
		bocLayout = (RelativeLayout) findViewById(R.id.manage_boc_layout);
		otherLayout = (RelativeLayout) findViewById(R.id.manage_other_layout);
		ebpsLayout = (RelativeLayout) findViewById(R.id.manage_otherebps_layout);
		bocPayeeLv = (ListView) findViewById(R.id.lv_query_result_trans_records);
		otherBankPayeeLv = (ListView) findViewById(R.id.lv_other_bank_manage_payee);
		ebpsPayeeLv = (ListView) findViewById(R.id.lv_other_ebps_manage_payee);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {// 新增、删除收款人返回
			switch (resultCode) {
			case 105:
				if (otherBankEbpsPayeeRb.isChecked()) {
					requestBIIForTran(
							Tran.PSNEBPSREALTIMEPAYMENTQUERYPAYEE_API,
							new HashMap<String, Object>(),
							"PsnEbpsRealTimePaymentQueryPayeeCallBack");
				} else {
					requestForQueryPayeeList();
				}

				break;

			default:
				break;
			}

		}
	}

	/**
	 * PsnTransManagePayeeQueryPayeeList查询收款人列表
	 */
	public void requestForQueryPayeeList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.QUERYPAYEELIST);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.MANAGE_PAYEELIST_BOCFLAG_REQ, "");// 查询全部
		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "queryPayeeListCallBack");
	}

	/**
	 * PsnTransManagePayeeQueryPayeeList查询收款人列表
	 */
	@SuppressWarnings("unchecked")
	public void queryPayeeListCallBack(Object resultObj) {

		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> result = (List<Map<String, Object>>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setQueryPayeeList(result);
		disPartData();
		if (bocPayeeRb.isChecked()) {
			bocPayeeLv.setVisibility(View.VISIBLE);
			bocAdapter = new ManageBocPayeeAdapter(PayeeListActivity.this,
					bocPayeeResultList);
			bocPayeeLv.setAdapter(bocAdapter);
			// 设置头信息
			bocPayeeLv.setOnItemClickListener(bocPayeeListener);
		} else if (otherBankPayeeRb.isChecked()) {
			otherBankPayeeLv.setVisibility(View.VISIBLE);
			otherBankAdapter = new ManageOtherBankPayeeAdapter(
					PayeeListActivity.this, otherBankPayeeResultList);
			otherBankPayeeLv.setAdapter(otherBankAdapter);
			// 设置头信息
			otherBankPayeeLv.setOnItemClickListener(otherBankPayeeListener);
		}
	}

	/**
	 * 分离数据
	 */
	private void disPartData() {
		queryPayeeResult = TranDataCenter.getInstance().getQueryPayeeList();

		bocPayeeResultList = new ArrayList<Map<String, Object>>();
		otherBankPayeeResultList = new ArrayList<Map<String, Object>>();
		if (queryPayeeResult != null) {

			for (int i = 0; i < queryPayeeResult.size(); i++) {
				String bocFlag = (String) queryPayeeResult.get(i).get(
						Tran.MANAGE_PAYEELIST_BOCFLAG_RES);
				if ((ConstantGloble.BOCBANK).equals(bocFlag)) {
					bocPayeeResultList.add(queryPayeeResult.get(i));
				} else if ((ConstantGloble.OTHERBANK).equals(bocFlag)) {
					otherBankPayeeResultList.add(queryPayeeResult.get(i));
				}
			}

		}
	}

	// 请求实时收款人列表返回
	public void PsnEbpsRealTimePaymentQueryPayeeCallBack(Object resultObj) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		BiiResponseBody biiResponseBody = callBackResponse(resultObj);
		list = (List<Map<String, Object>>) biiResponseBody.getResult();
		BiiHttpEngine.dissMissProgressDialog();
		ebpsPayeeResultList = list;
		ebpsPayeeLv.setVisibility(View.VISIBLE);
		ebpsAdapter = new ManageEbpsBankPayeeAdapter(PayeeListActivity.this,
				ebpsPayeeResultList);
		ebpsPayeeLv.setAdapter(ebpsAdapter);
		// 设置头信息
		ebpsPayeeLv.setOnItemClickListener(ebpsPayeeListener);
	}
}
