package com.chinamworld.bocmbci.biz.bocinvt.productlist;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt.adapter.BuyProductChooseCardOutlayAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;

/**
 * 功能外置 理财计划 登陆后选择账号界面
 * 
 * @author sunh
 * 
 */
public class BuyProductChooseCardOutlayActivity extends BociBaseActivity {

	/** 主布局 */
	private View view;
	/** 卡列表 */
	private ListView lvAccCard;
	/** 确定按钮 */
	private Button btnOk;
	/** 银行卡适配器 */
	private BuyProductChooseCardOutlayAdapter mAdapter;
	List<Map<String, Object>> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = addView(R.layout.buy_choose_card_outlay_list);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0,
					0, 0);
		}
		setTitle(getString(R.string.buy_choose_title));
		setText(this.getString(R.string.acc_rightbtn_go_main));
		setRightBtnClick(rightBtnClick);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				BociDataCenter.getInstance().clearBociData();
				Intent intent = new Intent();
				intent.setClass(BuyProductChooseCardOutlayActivity.this,
						QueryProductActivity.class);
				startActivity(intent);
				BuyProductChooseCardOutlayActivity.this.finish();
			}
		});
		init();

		list = BociDataCenter.getInstance().getBocinvtAcctList();
		mAdapter = new BuyProductChooseCardOutlayAdapter(this, list);
		lvAccCard.setAdapter(mAdapter);
	}

	private void init() {

		lvAccCard = (ListView) view.findViewById(R.id.cardlist);
		lvAccCard.setOnItemClickListener(cardItemClick);
	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 主界面
//			Intent intent = new Intent(BuyProductChooseCardOutlayActivity.this,
//					MainActivity.class);
//			startActivity(intent);
//			ActivityTaskManager.getInstance().removeAllSecondActivity();
		}
	};
	/** 列表点击事件 **/
	private OnItemClickListener cardItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
		
			mAdapter.setSelectedPosition(position);
 
			BaseDroidApp
					.getInstanse()
					.getBizDataMap()
					.put(ConstantGloble.BOCINVT_BUYINIT_MAP,
							mAdapter.getItem(position));

			setResult(RESULT_OK);
			BuyProductChooseCardOutlayActivity.this.finish();

		}
	};
	/*
	 * 返回键 跳转到之原始查询页面
	 */
	public void onBackPressed() {
		
			BociDataCenter.getInstance().clearBociData();
			Intent intent = new Intent();
			intent.setClass(BuyProductChooseCardOutlayActivity.this,
					QueryProductActivity.class);
			startActivity(intent);
			BuyProductChooseCardOutlayActivity.this.finish();

	

	}
}
