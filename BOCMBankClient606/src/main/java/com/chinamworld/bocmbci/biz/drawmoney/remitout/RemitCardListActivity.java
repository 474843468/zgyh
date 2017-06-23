package com.chinamworld.bocmbci.biz.drawmoney.remitout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.drawmoney.DrawBaseActivity;
import com.chinamworld.bocmbci.biz.drawmoney.DrawMoneyData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @ClassName: RemitCardListActivity
 * @Description: 汇往取款人——初始页面
 * @author JiangWei
 * @date 2013-7-15 下午3:27:24
 */
public class RemitCardListActivity extends DrawBaseActivity {
	/** 账户列表 */
	private ListView cardList;
	/** 下一步按钮 */
	private Button nextBtn;
	/** 列表adapter */
	private RemitOutListAdapter remitListAdapter;
	/** 当前选中账户的位置 */
	private int mCurrentPosition = -1;
	/** 账户标识 */
	private String acountId;
	/** 账户号码 */
	private String acountNumber;
	/** 查询到的账户列表 */
	List<Map<String, Object>> listData;
	/** 提示信息*/
	private LinearLayout dept_save_regular_bottom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view = LayoutInflater.from(this).inflate(
				R.layout.drawmoney_remitout_list, null);
		tabcontent.addView(view);
		setTitle(R.string.remitout_title);
		init();
		BaseHttpEngine.showProgressDialog();
		requestForCardList();
	}

	/**
	 * @Title: init
	 * @Description: 初始化view和数据
	 * @param
	 * @return void
	 */
	private void init() {
		Button btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		dept_save_regular_bottom = (LinearLayout) findViewById(R.id.dept_save_regular_bottom);
		dept_save_regular_bottom.setVisibility(View.VISIBLE);
		btnRight.setText(R.string.agence_query);
		if (btnRight != null) {
			btnRight.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(RemitCardListActivity.this,
							AgencyQueryActivity.class);
					startActivity(intent);
				}
			});
		}
		cardList = (ListView) this.findViewById(R.id.remit_choose_cardlist);
		cardList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
		/*		mCurrentPosition = position;
				remitListAdapter.setSelectedPosition(position);*/
				/**ListView点击选中，再点击取消*/
				if (mCurrentPosition == position) {
					mCurrentPosition = -1;
					remitListAdapter.setSelectedPosition(mCurrentPosition);
				}else {
					mCurrentPosition = position;
					remitListAdapter.setSelectedPosition(mCurrentPosition);
				}
			}
		});

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
		if (!StringUtil.isNullOrEmpty(listData)) {
			if(mCurrentPosition < 0){
				BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.to_choose_card_for_remit));
				return;
			}
			Intent intent = new Intent(this, RemitInputInfoActivity.class);
			Map<String, Object> map = listData.get(mCurrentPosition);
			acountId = (String) map.get(Comm.ACCOUNT_ID);
			acountNumber = (String) map.get(Comm.ACCOUNTNUMBER); 
			intent.putExtra(Comm.ACCOUNT_ID, acountId);
			intent.putExtra(Comm.ACCOUNTNUMBER, acountNumber);
			startActivityForResult(intent, 1011);
		}
	}

	@Override
	public void communicationCallBack(int flag) {
		switch (flag) {
		case GET_ACCOUNT_IN_CALLBACK:
			listData = DrawMoneyData.getInstance().getAccountList();
			if(StringUtil.isNullOrEmpty(listData)){
				BaseDroidApp.getInstanse().showMessageDialog(
						this.getResources().getString(R.string.no_list_data_for_remit),new OnClickListener() {
							
							@Override
							public void onClick(View v) {
//								ActivityTaskManager.getInstance().removeAllActivity();
//								Intent intent = new Intent();
//								intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//										MainActivity.class);
//								BaseDroidApp.getInstanse().getCurrentAct()
//										.startActivity(intent);
								goToMainActivity();
							}
						});
				break;
			}
			BaseHttpEngine.dissMissProgressDialog();
			setListView(listData);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			remitListAdapter.setSelectedPosition(-1);
		}
	}
	
	

}
