package com.chinamworld.bocmbci.biz.bocinvt.acctmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt.adapter.BindingResetAdapter;

/**
 * 绑定资金账户列表
 * 
 * @author panwe
 * 
 */
public class BindAcctListActivity extends BociBaseActivity implements OnClickListener {
	private ListView mListView;
	/** 列表选中条目 */
	private int selectposition = -1;
	private BindingResetAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.bocinvt_binding_choose);
		setTitle(R.string.bocinvt_bind_title);
		setText(this.getString(R.string.close));
		setLeftButtonPopupGone();
		setRightBtnClick(this);
		setBottomTabGone();
		goneLeftButton();
		setUpViews();
	}

	private void setUpViews() {
		((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, getResources().
				getDimensionPixelSize(R.dimen.fill_margin_top));
		((TextView) findViewById(R.id.tv_financeic_choose_title)).setText(getString(R.string.bocinvt_bind_acctip));
		findViewById(R.id.btnNext).setOnClickListener(this);
		mListView = (ListView) findViewById(R.id.bocinvt_binding_list);
		mListView.setOnItemClickListener(itemOnClick);
		mAdapter = new BindingResetAdapter(this, BociDataCenter.getInstance().getAllAcctList());
		mListView.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ib_top_right_btn) {
			finish(); return;
		}
		if (selectposition > -1) {
			startActivityForResult(new Intent(this, BindConfirmActivity.class).
					putExtra("p", selectposition), 1009); return;
		} 
		BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.bocinvt_bind_acctip));
	}

	private OnItemClickListener itemOnClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (selectposition == position) return;
			selectposition = position;
			mAdapter.setSelectedPosition(selectposition);
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			setResult(RESULT_OK);
			finish();
		}
	}
}
