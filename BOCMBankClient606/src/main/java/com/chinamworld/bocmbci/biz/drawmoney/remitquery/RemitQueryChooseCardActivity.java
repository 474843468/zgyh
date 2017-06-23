package com.chinamworld.bocmbci.biz.drawmoney.remitquery;

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
import com.chinamworld.bocmbci.biz.drawmoney.DrawBaseActivity;
import com.chinamworld.bocmbci.biz.drawmoney.DrawMoneyData;
import com.chinamworld.bocmbci.biz.drawmoney.remitout.RemitOutListAdapter;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @ClassName: QueryChooseCardActivity
 * @Description: 汇出查询——选择账户页面
 * @author JiangWei
 * @date 2013-7-15 下午3:51:58
 */
public class RemitQueryChooseCardActivity extends DrawBaseActivity{
	/** 账户列表 */
	private ListView cardList;
	/** 下一步按钮 */
	private Button nextBtn;
	/** 列表adapter */
	private RemitOutListAdapter remitListAdapter;
	/** 当前选中账户的位置 */
	private int mCurrentPosition = -1;
	/** 查询到的账户列表 */
	private List<Map<String, Object>> listData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view = LayoutInflater.from(this).inflate(
				R.layout.drawmoney_remitout_list, null);
		tabcontent.addView(view);
		
		Button btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setVisibility(View.GONE);

		setTitle(R.string.remitout_query_title);
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
		TextView seconedTitle = (TextView) this.findViewById(R.id.tv_service_title);
		seconedTitle.setText(R.string.to_choose_card_for_remit_query);
		cardList = (ListView) this.findViewById(R.id.remit_choose_cardlist);
		cardList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
			/*	mCurrentPosition = position;
				remitListAdapter.setSelectedPosition(position);*/
				//设置ListView 条目 点击选中,在点取消
				if (mCurrentPosition == position) {
					mCurrentPosition = -1;
					remitListAdapter.setSelectedPosition(mCurrentPosition);
				}else{
					mCurrentPosition = position;
					remitListAdapter.setSelectedPosition(mCurrentPosition);
				}
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
		if (!StringUtil.isNullOrEmpty(listData)) {
			if(mCurrentPosition < 0){
				BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.to_choose_card_for_remit_query));
				return;
			}
			requestSystemDateTime();
			BaseHttpEngine.showProgressDialog();
		}
	}
	
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestCommConversationIdCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent(this, RemitQueryBeforeActivity.class);
		intent.putExtra(CURRENT_POSITION, mCurrentPosition);
		intent.putExtra(CURRENT_DATETIME, dateTime);
		startActivity(intent);
	}
	
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		requestCommConversationId();
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
			DrawMoneyData.getInstance().setScreenAccountList(listData);
			setListView(listData);
			break;
			
		default:
			break;
		}
	}

}
