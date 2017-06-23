package com.chinamworld.bocmbci.biz.plps.annuity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.adapter.CommonAdapter;
import com.chinamworld.bocmbci.widget.adapter.ICommonAdapter;

/**
 * 养老金交易明细
 * @author panwe
 *
 */
public class AnnuityAcctInfoTranActivity extends PlpsBaseActivity implements ICommonAdapter<Map<String, Object>>{
	private View mFooterView;
	private ListView mListView;
//	private AnnuityAcctAdapter mAdapter;
	CommonAdapter  adapter;
	private int index = 10;
	private String planNo;
	private List<Map<String, Object>> mList = new ArrayList<Map<String,Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.plps_annuity_acct_tran);
		setTitle(R.string.plps_annuity_acctitle);
		getIntentData();
		setUpGetMoreView();
		setUpView();
	}
	
	private void getIntentData(){
		recordNumber = getIntent().getStringExtra(Plps.RECORDNUMBER);
		planNo = getIntent().getStringExtra(Plps.PLANNO);
		
	}
	
	private void setUpView(){
		mListView = (ListView) findViewById(R.id.listview);
		mList.addAll(PlpsDataCenter.getInstance().getAcctInfoList());
		addFooterView(recordNumber);
//		mAdapter = new AnnuityAcctAdapter(this, mList);
		adapter=new CommonAdapter<Map<String, Object>>(AnnuityAcctInfoTranActivity.this, mList, this);
		mListView.setAdapter(adapter);
//		mListView.setAdapter(mAdapter);
	}
	
	/**
	 * 初始化分页布局
	 */
	private void setUpGetMoreView(){
		mFooterView = inflateView(R.layout.epay_tq_list_more);
	}
	
	/**
	 * 添加更多按钮
	 * @param totalCount
	 */
	private void addFooterView(String totalCount) {
		if (Integer.valueOf(totalCount) > mList.size()) {
			if (mListView.getFooterViewsCount() <= 0) {
				mListView.addFooterView(mFooterView);
			}
			mListView.setClickable(true);
		} else {
			if (mListView.getFooterViewsCount() > 0) {
				mListView.removeFooterView(mFooterView);
			}
		}
		mFooterView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				requestAnnuityAcctInfoList(String.valueOf(index), planNo);
			}
		});
	}

	@Override
	public void annuityAcctInfoListCallBack(Object resultObj) {
		super.annuityAcctInfoListCallBack(resultObj);
		index = index+Integer.valueOf(ConstantGloble.FOREX_PAGESIZE);
		mList.addAll(PlpsDataCenter.getInstance().getAcctInfoList());
		addFooterView(recordNumber);
//		adapter.setData(mList);
		adapter.notifyDataSetChanged();
	}

	@Override
	public View getView(int arg0, Map<String, Object> currentItem,
			LayoutInflater inflater, View convertView, ViewGroup viewGroup) {
		ViewHodler h;
		if (convertView == null) {
			h = new ViewHodler();
			convertView = inflater.inflate(R.layout.plps_annuity_acct_tran_item, null);
			h.accountNo = (TextView) convertView.findViewById(R.id.userid);
			h.accountName = (TextView) convertView.findViewById(R.id.username);
			h.investCompoundingName = (TextView) convertView.findViewById(R.id.investcompoundingname);
			h.amount = (TextView) convertView.findViewById(R.id.amount);
			h.unitValue = (TextView) convertView.findViewById(R.id.unitvalue);
			h.purityDate = (TextView) convertView.findViewById(R.id.puritydate);
			h.worth = (TextView) convertView.findViewById(R.id.worth);
			convertView.setTag(h);
		}else{
			h = (ViewHodler) convertView.getTag();
		}
		final Map<String, Object> map = mList.get(arg0);
		if(!StringUtil.isNullOrEmpty(map.get(Plps.ACCOUNTNO))){
			h.accountNo.setText((String)map.get(Plps.ACCOUNTNO));
		}
		if(!StringUtil.isNullOrEmpty(map.get(Plps.ACCOUNTNAME))){
			h.accountName.setText((String)map.get(Plps.ACCOUNTNAME));
		}
		if(!StringUtil.isNullOrEmpty(map.get(Plps.INVESTCOMPOUNDINGNAME))){
			h.investCompoundingName.setText((String)map.get(Plps.INVESTCOMPOUNDINGNAME));
		}
		if (!StringUtil.isNullOrEmpty(map.get(Plps.AMOUNT))) {
			String ammountNumber = StringUtil.parseStringPattern(
					(String) map.get(Plps.AMOUNT), 2);
			if(ammountNumber.equals("0.00")){
				h.amount.setText("-");
			}else {
				h.amount.setText(ammountNumber);
			}
		}
		if(!StringUtil.isNullOrEmpty(map.get(Plps.UNITVALUE))){
			String unitValueNumber = StringUtil.parseStringPattern((String)map.get(Plps.UNITVALUE), 0);
			h.unitValue.setText(unitValueNumber);
		}
		if(!StringUtil.isNullOrEmpty(map.get(Plps.PURITYDATE))){
			h.purityDate.setText((String)map.get(Plps.PURITYDATE));
		}
		if(!StringUtil.isNullOrEmpty(map.get(Plps.WORTH))){
			h.worth.setText((String)map.get(Plps.WORTH));
		}
		return convertView;
	}
	public class ViewHodler {
		public TextView accountNo;
		public TextView accountName;
		public TextView investCompoundingName;
		public TextView amount;
		public TextView unitValue;
		public TextView purityDate;
		public TextView worth;
	}
}
