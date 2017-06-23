package com.chinamworld.bocmbci.biz.dept.fiexibleinterest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;

public class FiexibleInterestFinishActivity extends DeptBaseActivity {
	private View view;
	private int interestProductType = 0;
	private boolean iscancel;

	private TextView success_info;
	private Button btn_finish;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ibBack.setVisibility(View.GONE);
		view = addView(R.layout.dept_fiexibleinterest_finish_layout);
		interestProductType = getIntent().getIntExtra(
				Dept.flexibleInterest_interestProductType, -1);
		iscancel = getIntent().getBooleanExtra(Dept.IS_CHACEL, false);
		success_info = (TextView) view.findViewById(R.id.success_info);
		

		if (iscancel) {
			setTitle(getString(R.string.dept_fiexibleinterest_cancel));
			if (interestProductType == 15) {
				success_info.setText(getString(R.string.dept_bbk_cancel_success));
			} else {
				success_info.setText(getString(R.string.dept_enrich_cancel_success));
			}
			
		} else {
			setTitle(getString(R.string.dept_fiexibleinterest_sign));
			if (interestProductType == 15) {
				success_info.setText(getString(R.string.dept_bbk_sign_success));
			} else {
				success_info.setText(getString(R.string.dept_enrich_sign_success));
			}
		}
		btn_finish = (Button) findViewById(R.id.btn_finish);
		btn_finish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent data=new Intent();
				data.putExtra(Dept.flexibleInterest_interestProductType, interestProductType);
				data.putExtra(Dept.IS_CHACEL, iscancel);
				setResult(RESULT_OK,data);
				finish();
			}
		});
	}
}
