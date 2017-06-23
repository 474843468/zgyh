package com.chinamworld.bocmbci.biz.loan.loanQuery;

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
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.biz.loan.adapter.LoanUseQueryAccountAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 用款查询--账户选择页面
 * 
 * @author wanbing
 * 
 */
public class LoanUseQueryAccountActivity extends LoanBaseActivity {
	private static final String TAG = "LoanUseQueryAccountActivity";
	private ListView listView = null;
	private View contentView = null;
	private Button nextButton = null;
	private List<Map<String, String>> resultList = null;
	private LoanUseQueryAccountAdapter adapter = null;
	private String loanActNum = null;
    private String currency=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.loan_use_query));
		Button btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setVisibility(View.VISIBLE);
		setLeftSelectedPosition("loan_1");
		resultList = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOAN_USELOAN_RESULTLIST);
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		init();
		initOnClick();
	}

	private void init() {
		contentView = mInflater.inflate(R.layout.loan_use_query_account, null);
		tabcontentView.addView(contentView);
		listView = (ListView) findViewById(R.id.loan_listView);
		nextButton = (Button) findViewById(R.id.loan_nextButton);
		LoanUseQueryAccountAdapter.selection = -1;
		adapter = new LoanUseQueryAccountAdapter(this, resultList);
		listView.setAdapter(adapter);
	}

	private void initOnClick() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				LoanUseQueryAccountAdapter.selection = position;
				Map<String, String> map = resultList.get(position);
				loanActNum = map.get(Loan.LOAN_ACCOUNTNUMBER_RES);
				currency = map.get(Loan.LOANACC_CURRENCYCODE_RES);
				adapter.notifyDataSetChanged();
			}
		});
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (LoanUseQueryAccountAdapter.selection < 0) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							getResources().getString(R.string.loan_choose_query_acc));
					return;
				}
				Intent intent = new Intent(LoanUseQueryAccountActivity.this, LoanUseQueryActivity.class);
				intent.putExtra("loanActNum", loanActNum);
				intent.putExtra(Loan.LOANACC_CURRENCYCODE_RES, currency);
				startActivity(intent);
			}
		});
	}

}
