package com.chinamworld.bocmbci.biz.dept.zntzck;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.adapter.DeptZntzckQueryAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 智能通知存款---查询页面 */
public class DeptZntzckQueryActivity extends DeptBaseActivity {
	private static final String TAG = "DeptZntzckQueryActivity";
	private LinearLayout tabcontent;// 主Activity显示
	private Spinner accSpinner = null;
	private Button queryButton = null;
	private ListView accListView = null;
	private View queryView = null;
	private List<Map<String, String>> resultList = null;
	private List<String> accList = null;
	private String accountId = null;
	private DeptZntzckQueryAdapter adapter = null;
	private String accountNumber = null;
	private String accountType = null;
	private View mainLayout = null;
	private TextView moreButton = null;
	/** 记录总数 */
	private int recordNumber = 0;
	private int pageSize = 10;
	/** 刷新标志 */
	private boolean isRefresh = true;
	private RelativeLayout load_more = null;
	/** 当前页 */
	private int currentIndex = 0;
	private int position = -1;
	private List<Map<String, String>> dateList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		ibRight.setVisibility(View.VISIBLE);
		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent(DeptZntzckQueryActivity.this, DeptZntzckThreeMenuActivity.class);
				startActivity(intent);
			}
		});
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 0);
		}
		setTitle(getResources().getString(R.string.dept_zntzck_menu));
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		queryView = LayoutInflater.from(this).inflate(R.layout.dept_zntzck_query, null);
		tabcontent.addView(queryView);
		resultList = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.DEPT_RESULTLIST_LIST);
		dateList = new ArrayList<Map<String, String>>();
		if (StringUtil.isNullOrEmpty(resultList)) {
			return;
		}
		getAccList();
		if (StringUtil.isNullOrEmpty(accList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		init();
		initOnClick();
	}

	private void getAccList() {
		accList = new ArrayList<String>();
		int len = resultList.size();
		for (int i = 0; i < len; i++) {
			Map<String, String> map = resultList.get(i);
			String accountNumber = (String) map.get(Comm.ACCOUNTNUMBER);
			String accNum = StringUtil.getForSixForString(accountNumber);
			accList.add(accNum);
		}

	}
	

	private void init() {
		mainLayout = findViewById(R.id.layout_main);
		accSpinner = (Spinner) findViewById(R.id.spinner_acc);
		queryButton = (Button) findViewById(R.id.dept_query_deal_query);
		accListView = (ListView) findViewById(R.id.dept_list_view);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, accList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		accSpinner.setAdapter(adapter);
		accSpinner.setSelection(0);
		position = 0;
		accountNumber = accSpinner.getSelectedItem().toString();
		load_more = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.query_list_footer, null);
		moreButton = (TextView) load_more.findViewById(R.id.finc_listiterm_tv1);

	}

	public void initOnClick() {
		accSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view, int positions, long id) {
				position = positions;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		queryButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Map<String, String> map = resultList.get(position);
				accountId = (String) map.get(Comm.ACCOUNT_ID);
				String accountNumbers = (String) map.get(Comm.ACCOUNTNUMBER);
				accountNumber = StringUtil.getForSixForString(accountNumbers);
				accountType = map.get(Comm.ACCOUNT_TYPE);
				mainLayout.setVisibility(View.INVISIBLE);
				isRefresh = true;
				currentIndex = 0;
				clearDate();
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();

			}
		});
		moreButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isRefresh = false;
				currentIndex += pageSize;
				BaseHttpEngine.showProgressDialog();
				requsetPsnIntelligentNoticeDepositQuery(accountId, currentIndex, isRefresh);
			}
		});
	}

	private void clearDate() {
		if (dateList != null && !dateList.isEmpty()) {
			dateList.clear();
		}
		if (accListView.getFooterViewsCount() > 0) {
			accListView.removeFooterView(load_more);
		}
		if (adapter != null) {
			adapter.refshDate(new ArrayList<Map<String, String>>());
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requsetPsnIntelligentNoticeDepositQuery(accountId, currentIndex, isRefresh);
	}

	/** 智能通签约解约记录查询---回调 */
	@Override
	public void requsetPsnIntelligentNoticeDepositQueryCallback(Object resultObj) {
		super.requsetPsnIntelligentNoticeDepositQueryCallback(resultObj);
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = response.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) body.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		String count = (String) result.get(Dept.DEPT_RECORDNUMBER_RES);
		if (StringUtil.isNull(count)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		recordNumber = Integer.valueOf(count);
		List<Map<String, String>> list = (List<Map<String, String>>) result.get(Dept.DEPT_RESULTLIST_RES);
		if (StringUtil.isNullOrEmpty(list)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		dateList.addAll(dealTegethor(list));
		mainLayout.setVisibility(View.VISIBLE);
		if (recordNumber > pageSize && isRefresh) {
			accListView.addFooterView(load_more);
		}
		if (!isRefresh) {
			if (currentIndex + pageSize >= recordNumber) {
				accListView.removeFooterView(load_more);
			}
		}
		if (!isRefresh) {
			adapter.refshDate(dateList);
		} else {
			adapter = new DeptZntzckQueryAdapter(DeptZntzckQueryActivity.this, dateList, accList.get(position));
			accListView.setAdapter(adapter);
		}
		BaseHttpEngine.dissMissProgressDialog();
		adapter.setOnItemClickListener(onItemClickListener);
	}

	/** 在查询结果数据中添加账号、账户类型 */
	private List<Map<String, String>> dealTegethor(List<Map<String, String>> list) {
		List<Map<String, String>> lists = new ArrayList<Map<String, String>>();
		int len = list.size();
		for (int i = 0; i < len; i++) {
			Map<String, String> map = list.get(i);
			map.put(Comm.ACCOUNTNUMBER, accountNumber);
			map.put(Comm.ACCOUNT_TYPE, accountType);
			lists.add(map);
		}
		return lists;
	}


	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int index, long id) {
			if (index == 0) {
				Map<String, String> map = dateList.get(0);
				String accountNumber = (String) map.get(Comm.ACCOUNTNUMBER);// 已经处理--464
				String accountType = map.get(Comm.ACCOUNT_TYPE);
				String signCardNo = map.get(Dept.DEPT_SIGNCARDNO_RES);
				String cancelDate = map.get(Dept.DEPT_CANCELDATE_RES);// 解约日期
				String signCardNos = null;
				if (StringUtil.isNull(signCardNo)) {
					signCardNos = "-";
				} else {
					signCardNos = StringUtil.getForSixForString(signCardNo);
				}
				if (StringUtil.isNull(cancelDate)) {
					// 正常--解约
					Intent intent = new Intent(DeptZntzckQueryActivity.this, DeptZntzckCancelConfirmActivity.class);
					intent.putExtra(Comm.ACCOUNT_ID, accountId);
					if (ConstantGloble.ACC_TYPE_BRO.equals(accountType)) {
						// 借记卡--展示签约卡号
						intent.putExtra(Comm.ACCOUNTNUMBER, signCardNos);
					} else if (ConstantGloble.ACC_TYPE_ORD.equals(accountType)
							|| ConstantGloble.ACC_TYPE_RAN.equals(accountType)) {
						// 普活、活一本，展示账号
						intent.putExtra(Comm.ACCOUNTNUMBER, accountNumber);
					}

					startActivity(intent);
				} else {
					// 已解约
					Map<String, String> maps = resultList.get(position);
					BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.DEPT_ACCINFO, maps);
					Intent intnet = new Intent(DeptZntzckQueryActivity.this, DeptZntzckSignReadActivity.class);
					BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.DEPT_GOTO_TAG, "1");// 查询页面
					if (ConstantGloble.ACC_TYPE_BRO.equals(accountType)) {
						// 借记卡--展示签约卡号
						intnet.putExtra(Comm.ACCOUNTNUMBER, signCardNos);
					} else if (ConstantGloble.ACC_TYPE_ORD.equals(accountType)
							|| ConstantGloble.ACC_TYPE_RAN.equals(accountType)) {
						// 普活、活一本，展示账号
						intnet.putExtra(Comm.ACCOUNTNUMBER, accountNumber);
					}
					startActivity(intnet);
				}

			}

		}

	};
}
