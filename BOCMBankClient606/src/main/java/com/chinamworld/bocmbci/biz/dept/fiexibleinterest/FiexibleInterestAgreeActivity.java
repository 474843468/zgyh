package com.chinamworld.bocmbci.biz.dept.fiexibleinterest;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.bii.constant.Inves;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;

public class FiexibleInterestAgreeActivity extends DeptBaseActivity implements
		OnClickListener {
	private View view;
	private int interestProductType = 0;
	private Button btn_cancel, btn_corfirm;
	private TextView contract_title_tv, tv_head, tv_foot;
	private LinearLayout ll_middle;
	/** 选择框 */
	private CheckBox agree_cb;
	/** 警告 */
	private TextView tips_warn;
	private TextView agree_tv;
	private ScrollView scrollview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getString(R.string.dept_server_info));

		ibBack.setVisibility(View.GONE);
		interestProductType = getIntent().getIntExtra(
				Dept.flexibleInterest_interestProductType, -1);
		view = addView(R.layout.dept_fiexibleinterest_agree_layout);
		contract_title_tv = (TextView) view
				.findViewById(R.id.contract_title_tv);
		tv_head = (TextView) view.findViewById(R.id.tv_head);
		tv_foot = (TextView) view.findViewById(R.id.tv_foot);
		ll_middle = (LinearLayout) view.findViewById(R.id.ll_middle);

		((TextView) findViewById(R.id.tvFirst))
				.setText((String) ((Map<String, Object>) BaseDroidApp
						.getInstanse().getBizDataMap()
						.get(ConstantGloble.BIZ_LOGIN_DATA))
						.get(Inves.CUSTOMERNAME));

		if (interestProductType == 15) {
			contract_title_tv.setText(Html
					.fromHtml(getString(R.string.depagreement_bbk)));
			tv_head.setText(Html
					.fromHtml(getString(R.string.deptagreebbk_head)));
			View listview = LayoutInflater.from(this).inflate(
					R.layout.dept_fiexibleinterest_bbk_info, null);
			tv_foot.setText(Html
					.fromHtml(getString(R.string.deptagreebbk_foot)));
			ll_middle.addView(listview);
		} else {
			contract_title_tv.setText(Html
					.fromHtml(getString(R.string.depagreement_enrichment)));
			tv_head.setText(Html
					.fromHtml(getString(R.string.deptagree_enrichment_head)));
			View listview = LayoutInflater.from(this).inflate(
					R.layout.dept_fiexibleinterest_enrichment_info, null);
			ll_middle.addView(listview);
			tv_foot.setText(Html
					.fromHtml(getString(R.string.deptagree_enrichment_foot)));

		}
		scrollview=(ScrollView) view.findViewById(R.id.scrollview);
		agree_cb = (CheckBox) view.findViewById(R.id.agree_cb);
		agree_tv = (TextView) view.findViewById(R.id.agree_tv);
		tips_warn = (TextView) view.findViewById(R.id.tips_warn);
		agree_tv.setOnClickListener(checkedListener);
		btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(this);
		btn_corfirm = (Button) view.findViewById(R.id.btn_corfirm);
		btn_corfirm.setOnClickListener(this);
	}

	private OnClickListener checkedListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (agree_cb.isChecked()) {
				agree_cb.setChecked(false);
			} else {
				agree_cb.setChecked(true);
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel:
			finish();
			break;
		case R.id.btn_corfirm:

			if (agree_cb.isChecked()) {
				
				Intent intent = new Intent(FiexibleInterestAgreeActivity.this,
						FiexibleInterestConfirmActivity.class);
				intent.putExtra(Dept.IS_CHACEL, false);
				intent.putExtra(Dept.flexibleInterest_interestProductType,
						interestProductType);
				startActivityForResult(intent, 5);
			} else {
				scrollview.scrollTo(0, 10000);
				tips_warn.setVisibility(View.VISIBLE);
			}

			break;

		default:
			break;
		}

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK, data);
			finish();
			break;

		default:
			break;
		}
	}
}
