package com.chinamworld.bocmbci.biz.loan.loanPledge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.biz.loan.adapter.LoanPledgeAccountAdapter;
import com.chinamworld.bocmbci.biz.loan.loanUse.LoanUseMenuActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 存单质押贷款——选择账户页面 */
public class LoanPledgeAccountActivity extends LoanBaseActivity {
	private static final String TAG = "LoanPledgeAccountActivity";
	private ListView listView = null;
	private View contentView = null;
	private Button nextButton = null;
	private List<Map<String, String>> resultList = null;
	private LoanPledgeAccountAdapter adapter = null;
	private String accountId = null;
	private String accountNumber = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.loan_two_one));
		Button backButton = (Button) findViewById(R.id.ib_back);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), LoanUseMenuActivity.class);
				BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
			}
		});
		Button btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setVisibility(View.VISIBLE);
		resultList = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOAN_NUM_RESULTLIST);
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		init();
		initOnClick();

	}

	private void init() {
		contentView = LayoutInflater.from(LoanPledgeAccountActivity.this).inflate(R.layout.loan_select_acc, null);
		tabcontentView.addView(contentView);
		listView = (ListView) findViewById(R.id.loan_listView);
		nextButton = (Button) findViewById(R.id.loan_nextButton);
		LoanPledgeAccountAdapter.selectedPosition = -1;
		adapter = new LoanPledgeAccountAdapter(this, resultList);
		listView.setAdapter(adapter);
	}

	private void initOnClick() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				LoanPledgeAccountAdapter.selectedPosition = position;
				Map<String, String> map = resultList.get(position);
				accountId = map.get(Loan.LOAN_ACCOUNTID_RES);
				accountNumber = map.get(Loan.LOAN_ACCOUNTNUMBER_RES);
				adapter.notifyDataSetChanged();
			}
		});
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (LoanPledgeAccountAdapter.selectedPosition < 0) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							getResources().getString(R.string.loan_choise_acc_list));
					return;
				}
				BaseHttpEngine.showProgressDialog();
				requestPsnQueryPersonalTimeAccount(accountId);
			}
		});
	}

	@Override
	public void requestPsnQueryPersonalTimeAccountCallback(Object resultObj) {
		super.requestPsnQueryPersonalTimeAccountCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, String>> result = (List<Map<String, String>>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(resultObj)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.loan_no_cdnumber_new_title));
			return;
		}
		List<Map<String, String>> normalDateList = getDateList(result);
		if (StringUtil.isNullOrEmpty(normalDateList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.loan_no_cdnumber_new_title));
			return;
		}
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent(this, LoanPledgeCdnumberInfoActivity.class);
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.LOAN_NORMALDATELIST, normalDateList);
		intent.putExtra(Loan.LOAN_ACCOUNTID_RES, accountId);
		intent.putExtra(Loan.LOAN_ACCOUNTNUMBER_RES, accountNumber);
		BaseDroidApp.getInstanse().getBizDataMap().put(Loan.LOAN_ACCOUNTID_RES, accountId);
		startActivity(intent);
	}

	private List<Map<String, String>> getDateList(List<Map<String, String>> result) {
		List<Map<String, String>> resultDateList = new ArrayList<Map<String, String>>();
		int len = result.size();
		for (int i = 0; i < len; i++) {
			Map<String, String> map = result.get(i);
			String status = map.get(Loan.LOAN_STATUS_RES);
			String type = map.get(Loan.LOAN_TYPE_RES);
			String convertType = map.get(Loan.LOAN_CONVERTTYPE_RES);
			if (!StringUtil.isNull(status) && !StringUtil.isNull(type) && !StringUtil.isNull(convertType)) {
				if (ConstantGloble.FOREX_ACCTYPE_NORMAL.equals(status)
						&& ConstantGloble.FOREX_ACCTYPE_ZCZQ.equals(type)
						&& ConstantGloble.LOAN_CONVERTTYPE_R.equals(convertType)) {
					resultDateList.add(map);
				}
			}
		}
		return resultDateList;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			ActivityTaskManager.getInstance().removeAllActivity();
			Intent intent = new Intent();
			intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), LoanUseMenuActivity.class);
			BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
		}
		return super.onKeyDown(keyCode, event);
	}
}
