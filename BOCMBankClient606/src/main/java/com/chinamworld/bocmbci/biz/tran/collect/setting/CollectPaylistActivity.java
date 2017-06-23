package com.chinamworld.bocmbci.biz.tran.collect.setting;

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
import com.chinamworld.bocmbci.bii.constant.Collect;
import com.chinamworld.bocmbci.biz.tran.collect.CollectBaseActivity;
import com.chinamworld.bocmbci.biz.tran.collect.CollectData;
import com.chinamworld.bocmbci.biz.tran.collect.adapter.CollectPaylistAdapter;

/**
 * @ClassName: CollectPaylistActivity
 * @Description: 归集支付列表
 * @author luql
 * @date 2014-3-24 下午03:41:03
 */
public class CollectPaylistActivity extends CollectBaseActivity {

	public static final int EMPTY_RESULTCODE = 100;
	/** 子界面 */
	private View viewContent;
	private ListView mPaylistView;
	private String mPayeeAccount;
	private List<Map<String, Object>> mPayList;
	private CollectPaylistAdapter mAdapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		viewContent = addView(R.layout.collect_paylist_activity);
		setTitle(getString(R.string.collect_paylist_title));
		if (getIntentData()) {
			// 初始化弹窗按钮
			initPulldownBtn();
			goneLeftView();
			invisible();
			findView();
			setView();
		} else {
			finish();
		}
	}

	private void setView() {
		mAdapter = new CollectPaylistAdapter(this, mPayList);
		mPaylistView.setAdapter(mAdapter);
		mPaylistView.setOnItemClickListener(itemClick);
	}

	private boolean getIntentData() {
		Intent intent = getIntent();
		mPayeeAccount = intent.getStringExtra(Collect.cbAccCard);
		mPayList = CollectData.getInstance().getPayList(mPayeeAccount);
		return mPayList != null;
	}

	private void findView() {
		Button btnBack = (Button) findViewById(R.id.ib_back);
		btnBack.setVisibility(View.GONE);
		Button btnClose = (Button) findViewById(R.id.ib_top_right_btn);
		btnClose.setVisibility(View.VISIBLE);
		btnClose.setText(this.getString(R.string.close));
		btnClose.setOnClickListener(closeClick);
		
		mPaylistView = (ListView) viewContent.findViewById(R.id.lv);
	}

	private OnItemClickListener itemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent it = new Intent();
			it.putExtra("position", position);
			setResult(RESULT_OK, it);
			finish();
		}
	};

	/** 隐藏侧面和底部按钮 */
	private void invisible() {
		View view = findViewById(R.id.menu_popwindow);
		view.setVisibility(View.GONE); 
	}

	/** 返回主页Listener */
	private OnClickListener closeClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};

	private void emptyExit() {
		setResult(EMPTY_RESULTCODE);
		finish();
	}
}
