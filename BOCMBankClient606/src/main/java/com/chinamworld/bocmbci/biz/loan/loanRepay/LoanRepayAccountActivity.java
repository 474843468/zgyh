package com.chinamworld.bocmbci.biz.loan.loanRepay;

import java.util.HashMap;
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
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.biz.loan.adapter.LoanRepayAccountAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 提前还款--选择账户页面 */
public class LoanRepayAccountActivity extends LoanBaseActivity {
	private static final String TAG = "LoanRepayAccountActivity";
	private View choiseView = null;
	private ListView accListView = null;
	private Button nextButton = null;
	private List<Map<String, String>> loanAccountList = null;
	private LoanRepayAccountAdapter adapter = null;
	/** 贷款账号 */
	private String loanAccount = null;
	/**selection 被点击的item 的位置*/
	public static int selection;
	/**线上标识*/
	public static String onlineFlag;
	/**循环类型*/
	public static String cycleType;
	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.loan_three_one));
		Button backButton = (Button) findViewById(R.id.ib_back);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent(LoanRepayAccountActivity.this, LoanRepayMenuActivity.class);
				startActivity(intent);
				finish();
			}
		});
		Button btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setVisibility(View.VISIBLE);
		loanAccountList = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOAN_LOANACCOUNTLIST);
		if (StringUtil.isNullOrEmpty(loanAccountList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		init();
		initOnclick();
	}

	private void init() {
		choiseView = LayoutInflater.from(this).inflate(R.layout.loan_repay_choise_acc, null);
		tabcontentView.addView(choiseView);
		accListView = (ListView) findViewById(R.id.loan_listView);
		nextButton = (Button) findViewById(R.id.loan_nextButton);
		LoanRepayAccountAdapter.selection = -1;
		adapter = new LoanRepayAccountAdapter(this, loanAccountList);
		accListView.setAdapter(adapter);
	}

	private void initOnclick() {
		accListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				selection = position;
				LoanRepayAccountAdapter.selection = position;
				Map<String, String> map = loanAccountList.get(position);
				loanAccount = map.get(Loan.LOAN_LOANACCOUNT_REQ);
				onlineFlag=map.get(Loan.LOAN_ONLINE_FLAG);
				cycleType=map.get(Loan.CYCLE_TYPE);
				adapter.notifyDataSetChanged();
			}
		});
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (LoanRepayAccountAdapter.selection < 0) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							getResources().getString(R.string.loan_repay_acc_title));
					return;
				}
				BaseHttpEngine.showProgressDialog();
				requestPsnLOANAdvanceRepayAccountDetailQuery(loanAccount);
			}

		});
	}

	/**
	 * 提前还款贷款账户详情查询
	 * 
	 * @param loanAccount
	 */
	private void requestPsnLOANAdvanceRepayAccountDetailQuery(String loanAccount) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_PSNLOANADVANCEREPAYACCOUNTDETAILQUERY_API);
		biiRequestBody.setConversationId(null);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Loan.LOAN_LOANACCOUNT_REQ, loanAccount);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnLOANAdvanceRepayAccountDetailQueryCallback");

	}

	/** 提前还款贷款账户详情查询-----回调 */
	public void requestPsnLOANAdvanceRepayAccountDetailQueryCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.LOAN_RESULT, result);
		BaseDroidApp.getInstanse().getBizDataMap().put(Loan.LOAN_LOANACCOUNTLIST_REQ, loanAccountList);
		gotoActivity();
	}

	private void gotoActivity() {
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent(LoanRepayAccountActivity.this, LoanRepayAccountDetailActivity.class);
		intent.putExtra(ConstantGloble.POSITION, LoanRepayAccountAdapter.selection);
		startActivity(intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			ActivityTaskManager.getInstance().removeAllActivity();
			Intent intent = new Intent(this, LoanRepayMenuActivity.class);
			startActivity(intent);
		}
		return super.onKeyDown(keyCode, event);
	}
}
