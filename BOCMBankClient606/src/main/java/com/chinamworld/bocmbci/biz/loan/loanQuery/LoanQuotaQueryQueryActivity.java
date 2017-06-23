package com.chinamworld.bocmbci.biz.loan.loanQuery;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.biz.loan.adapter.LoanQuotaQueryQueryAdapter;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 额度用款查询结果页面 */
public class LoanQuotaQueryQueryActivity extends LoanBaseActivity {
	private static final String TAG = "LoanQuotaQueryQueryActivity";
	private View queryView = null;
	private ListView listView = null;
	private List<Map<String, String>> listDate = null;
	private LoanQuotaQueryQueryAdapter adapter = null;

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
				.get(Loan.LOAN_ACCOUNTLIST_RES);
		if (StringUtil.isNullOrEmpty(listDate)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		queryView = LayoutInflater.from(this).inflate(R.layout.loan_quato_query_query, null);
		tabcontentView.addView(queryView);
		listView = (ListView) findViewById(R.id.loan_listView);
		adapter = new LoanQuotaQueryQueryAdapter(this, listDate);
		listView.setAdapter(adapter);
	}

}
