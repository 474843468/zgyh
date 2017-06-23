package com.chinamworld.bocmbci.biz.finc.myfund;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.adapter.FincFundDueDateListAdapter;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;

import java.util.List;
import java.util.Map;

/**
 * 我的基金 短期理财到期日查询
 * 
 * 
 * 
 */
public class FincFundDueDateListActivity extends FincBaseActivity {

	
	/**短期理财到期日 主 view*/
	private View myFincView;
	
	/**显示列表listview*/
	private ListView  dueListView;
	
	/**listviewHead*/
	private TextView head1;
	private TextView head2;
	private TextView head3;
	
	int newPosition ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		init();
		getViewValue();
		initOnClick();
		
	}
	
	/**初始化控件*/
	private void init(){
		setRightToMainHome();
		myFincView = mainInflater.inflate(R.layout.finc_fund_due_date,
				null);
		tabcontent.addView(myFincView);
		setTitle(getResources().getString(R.string.finc_myfinc_short_query));
		dueListView = (ListView) myFincView.findViewById(R.id.finc_combin_query_listView);
		head1 = (TextView) myFincView.findViewById(R.id.list_header_tv1);
		head2 = (TextView) myFincView.findViewById(R.id.list_header_tv2);
		head3 = (TextView) myFincView.findViewById(R.id.list_header_tv3);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				head1);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				head2);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				head3);
		
		FincFundDueDateListAdapter adapter =new FincFundDueDateListAdapter(this, fincControl.fincFundDueDateQuery  );
		dueListView.setAdapter(adapter);
	}
	/**给控件赋值*/
	private void getViewValue(){
		head1.setText(getResources().getString(R.string.finc_myfinc_short_reality_share));
		head2.setText(getResources().getString(R.string.finc_myfinc_short_regist_date));
		head3.setText(getResources().getString(R.string.finc_myfinc_short_redemption_date));
		
	}
	
	/**添加监听器*/
	private void initOnClick(){
		dueListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				BaseHttpEngine.showProgressDialog();
				newPosition = position;
				getFundDetailByFundCode((String)fincControl.fincFundDueDateQuery.
						get(position).get(Finc.FINC_FUNDCODEM_REQ));
			}

		});
		
	}
	
	@Override
	public void getFundDetailByFundCodeCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.getFundDetailByFundCodeCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> resultMap = (Map<String, String>) biiResponseBody
				.getResult();
		String currency = resultMap.get(Finc.FINC_CURRENCY);
		goNextActivity(currency);

	}
	
	protected void goNextActivity(String currency) {
		Intent intent =new Intent(this, FincFundDueDateDetailsActivity.class);
		intent.putExtra("position", newPosition);
		intent.putExtra("currency", currency);
		startActivity(intent);
		
	}


}
