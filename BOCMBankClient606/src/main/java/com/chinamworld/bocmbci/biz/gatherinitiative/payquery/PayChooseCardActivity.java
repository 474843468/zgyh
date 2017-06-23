package com.chinamworld.bocmbci.biz.gatherinitiative.payquery;

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
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.drawmoney.remitout.RemitOutListAdapter;
import com.chinamworld.bocmbci.biz.gatherinitiative.GatherBaseActivity;
import com.chinamworld.bocmbci.biz.gatherinitiative.GatherInitiativeData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: PayChooseCardActivity
 * @Description: 选择付款账户页面
 * @author JiangWei
 * @date 2013-8-31上午10:47:13
 */
public class PayChooseCardActivity extends GatherBaseActivity{
	/** 账户列表 */
	private ListView cardList;
	/** 下一步按钮 */
	private Button nextBtn;
	/** 列表adapter */
	private RemitOutListAdapter remitListAdapter;
	/** 当前选中账户的位置 */
	private int mCurrentPosition = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(R.string.to_pay);
		View view = LayoutInflater.from(this).inflate(R.layout.drawmoney_remitout_list, null);
		tabcontent.addView(view);
		
		init();
		requestPayAccountList();
	}
	
	/**
	 * @Title: init
	 * @Description: 初始化view和数据
	 * @param
	 * @return void
	 */
	private void init() {
		TextView seconedTitle = (TextView) this.findViewById(R.id.tv_service_title);
		seconedTitle.setText(R.string.to_choose_card_for_pay);
		cardList = (ListView) this.findViewById(R.id.remit_choose_cardlist);
		cardList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				mCurrentPosition = position;
				remitListAdapter.setSelectedPosition(position);
			}
		});
		cardList.setSelected(false);

		nextBtn = (Button) this.findViewById(R.id.sureButton);
		nextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				excuseNext();
			}
		});
	}

	/**
	 * @Title: setListView
	 * @Description: 填充视图的数据
	 * @param
	 * @return void
	 */
	private void setListView(List<Map<String, Object>> accountList) {
		if (remitListAdapter == null) {
			remitListAdapter = new RemitOutListAdapter(this, accountList);
			cardList.setAdapter(remitListAdapter);
		} else {
			remitListAdapter.setData(accountList);
		}
	}

	/**
	 * @Title: excuseNext
	 * @Description: 执行下一步操作
	 * @param
	 * @return void
	 */
	private void excuseNext() {
		if(mCurrentPosition < 0){
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.please_choose_card_for_pay));
			return;
		}
		BaseHttpEngine.showProgressDialog();
		requestSystemDateTime();
	}
	
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		BaseHttpEngine.dissMissProgressDialog();
		super.requestSystemDateTimeCallBack(resultObj);
		dateTime = dateTime.substring(0,10);
		Intent intent = new Intent(this, PayInputInfoActivity.class);
		intent.putExtra(CURRENT_POSITION, mCurrentPosition);
		intent.putExtra(CURRENT_DATETIME, dateTime);
		startActivityForResult(intent, 1001);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case (1001):
			if (resultCode == RESULT_OK) {
				setResult(RESULT_OK);
				finish();
			}
			break;
		}
	}

	@Override
	public void communicationCallBack(int flag) {
		switch (flag) {
		case QUERY_PAY_ACCOUNT_CALLBACK:
			List<Map<String, Object>> listData = GatherInitiativeData.getInstance().getPayAcountCallBackList();
			if(StringUtil.isNullOrEmpty(listData)){
				BaseDroidApp.getInstanse().showMessageDialog(
						this.getResources().getString(R.string.no_suitable_pay_account),new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								finish();
							}
						});
				break;
			}
			setListView(listData);
			break;
			
		default:
			break;
		}
	}

}
