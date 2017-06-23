package com.chinamworld.bocmbci.biz.plps.payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.adapter.PaymentSignAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 签约信息查询列表
 * @author panwe
 *
 */
public class PaymentSignListActivity extends PlpsBaseActivity{
	private View mFooterView;
	private ListView mListView;
	private EditText mEditText;
	private PaymentSignAdapter mAdapter;
	private String phoneNumber;
	private String recordNumber;
	private List<Map<String, Object>> mList = new ArrayList<Map<String,Object>>();
	private int index = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.plps_payment_sign_list);
		setTitle(PlpsDataCenter.payment[0]);
		getIntentData();
		setUpGetMoreView();
		setUpViews();
	}
	
	private void getIntentData(){
		Intent intent = getIntent();
		phoneNumber = intent.getStringExtra(Plps.PHONENUMBER);
		recordNumber = intent.getStringExtra(Plps.RECORDNUMBER);
	}
	
	private void setUpViews(){
		if(!StringUtil.isNullOrEmpty(PlpsDataCenter.getInstance().getSignList())){
			mList.addAll(PlpsDataCenter.getInstance().getSignList());
		}
		((TextView) findViewById(R.id.phoneNum)).setText(phoneNumber);
		mListView = (ListView) findViewById(R.id.listview);
		addFooterView(recordNumber);
		mAdapter = new PaymentSignAdapter(this, mList);
		mListView.setOnItemClickListener(this);
		mListView.setAdapter(mAdapter);
	}
	
	/**
	 * 初始化分页布局
	 */
	private void setUpGetMoreView(){
		mFooterView = inflateView(R.layout.epay_tq_list_more);
		((RelativeLayout) mFooterView.findViewById(R.id.rl_get_more))
				.setBackgroundColor(getResources().getColor(
						R.color.transparent_00));
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
				BaseHttpEngine.showProgressDialog();
				requestSignList(String.valueOf(index));
			}
		});
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		super.onItemClick(parent, view, position, id);
		PlpsDataCenter.getInstance().setSignList(mList);
		startActivityForResult(new Intent(this, PaymentSignDetailActivity.class)
		.putExtra(Plps.PHONENUMBER, phoneNumber)
		.putExtra("p", position),1001);
	}

	/**
	 * 修改手机号
	 * @param v
	 */
	public void phoneNumberChangeOnclick(View v){
//		showChangeDialog();
		startActivityForResult(new Intent(PaymentSignListActivity.this, PaymentChangePhoneNumberActivity.class)
			.putExtra(Plps.PHONENUMBER, phoneNumber), 1003);
	}
	
	/**
	 * 弹出手机号修改框
	 */
	private void showChangeDialog(){
		View dialogView = inflateView(R.layout.plps_payment_phone_change_dialog);
		mEditText = (EditText)dialogView.findViewById(R.id.phone);
		mEditText.setText(phoneNumber);
		BaseDroidApp.getInstanse().showDialog(dialogView);
	}
	
	/**
	 * 手机号修改事件处理
	 * @param v
	 */
	/*public void dialogClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cance:
			BaseDroidApp.getInstanse().dismissErrorDialog();
			break;

		case R.id.btn_confirm:
			phoneNumber = mEditText.getText().toString().trim();
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			RegexpBean reMobile = new RegexpBean("手机号码",phoneNumber, Plps.REGEXMOBILE);
			lists.add(reMobile);
			if (RegexpUtils.regexpDate(lists)) {
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
			}break;
		}
	}*/
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1001) {
			if (resultCode == RESULT_OK) {
				finish();
			}
		} else if (requestCode == 1003) {
			if (resultCode == RESULT_OK) {
				requestCustomerInfo();
			}
		}
	}
	@Override
	public void customerInfoCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.customerInfoCallBack(resultObj);
		((TextView) findViewById(R.id.phoneNum)).setText(phoneNumber);
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		pSNGetTokenId();
	}
	
	/***
	 * 请求token
	 */
	public void pSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "aquirePSNGetTokenId");
	}
	
	public void aquirePSNGetTokenId(Object resultObj) {
		String tokenId = (String) this.getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog(); return;
		}
		requestPhoneChange(tokenId);
	}
	
	/**
	 * 请求修改手机号
	 * @param tokenId
	 */
	private void requestPhoneChange(String tokenId){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.CUSTOMERINFOUPDATE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put(Plps.TOKEN, tokenId);
		param.put(Plps.PHONENUMBER, phoneNumber);
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "phoneChangeCallBack");
	}
	
	public void phoneChangeCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BaseDroidApp.getInstanse().dismissErrorDialog();
		CustomDialog.toastShow(this,getString(R.string.plps_payment_modify_success));
		((TextView) findViewById(R.id.phoneNum)).setText(phoneNumber);
	}

	@Override
	public void signListCallBack(Object resultObj) {
		super.signListCallBack(resultObj);
		index = index+Integer.valueOf(ConstantGloble.FOREX_PAGESIZE);
		mList.addAll(PlpsDataCenter.getInstance().getSignList());
		addFooterView(recordNumber);
		mAdapter.setData(mList);
	}
	
}
