package com.chinamworld.bocmbci.biz.loan.loanQuery;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.biz.loan.adapter.LoanQuotaQueryAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 额度查询首页 */
public class LoanQuotaQueryActivity extends LoanBaseActivity {
	private static final String TAG = "LoanQuotaQueryActivity";
	private View searchView = null;
	private ListView listView = null;
	private List<Map<String, String>> listDate = null;
	private LoanQuotaQueryAdapter adapter = null;
	private int selectPosition = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.loan_left_three1));
		Button backButton = (Button) findViewById(R.id.ib_back);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		listDate = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOAN_QUARY_RESULT);
		if (StringUtil.isNullOrEmpty(listDate)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		init();
	}

	private void init() {
		searchView = LayoutInflater.from(this).inflate(R.layout.loan_quato_query, null);
		tabcontentView.addView(searchView);
		listView = (ListView) findViewById(R.id.loan_listView);
		adapter = new LoanQuotaQueryAdapter(this, listDate);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				selectPosition = position;
				Intent intent = new Intent(LoanQuotaQueryActivity.this, LoanQuotaQueryDetailActivity.class);
				intent.putExtra(ConstantGloble.FOREX_customerSelectedPosition, selectPosition);
				startActivity(intent);
			}
		});
	}
}
