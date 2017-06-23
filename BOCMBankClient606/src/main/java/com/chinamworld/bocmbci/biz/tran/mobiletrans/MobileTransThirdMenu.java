package com.chinamworld.bocmbci.biz.tran.mobiletrans;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;

/**
 * 
 * @author
 * 
 */
public class MobileTransThirdMenu extends TranBaseActivity {
	private LinearLayout mobileTranLayout;
	private LinearLayout queryResultLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.mobile_trans));
		View view = mInflater.inflate(R.layout.tran_mobile_third_menu, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);

		mTopRightBtn.setVisibility(View.INVISIBLE);
		setLeftSelectedPosition("tranManager_2");
		mobileTranLayout = (LinearLayout) findViewById(R.id.ll_2dimen_generate_trans);
		queryResultLayout = (LinearLayout) findViewById(R.id.ll_2dimen_scan_trans);

		mobileTranLayout.setOnClickListener(mobileTranListener);
		queryResultLayout.setOnClickListener(queryResultListener);
	}

//	@Override
//	protected void onResume() {
//		super.onResume();
//		setLeftSelectedPosition("tranManager_2");
//	}



	/**
	 * 手机号转账
	 */
	private OnClickListener mobileTranListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent1 = new Intent();
			intent1.setClass(MobileTransThirdMenu.this,
					MobileTranActivity.class);
			startActivity(intent1);

		}
	};
	/**
	 * 交易结果查询
	 */
	private OnClickListener queryResultListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MobileTransThirdMenu.this,
					MobileTransferQueryActivity.class);
			startActivity(intent);
		}
	};
}
