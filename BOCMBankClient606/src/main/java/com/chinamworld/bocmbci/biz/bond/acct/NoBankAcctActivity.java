package com.chinamworld.bocmbci.biz.bond.acct;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.BusinessModelControl;
import com.chinamworld.bocmbci.biz.bond.BondBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;

/**
 * 提示关联账户页面
 * 
 * @author panwe
 * 
 */
public class NoBankAcctActivity extends BondBaseActivity {

	/** 主布局 */
	private View mainView;
	private boolean isBuy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 添加布局
		mainView = (View) LayoutInflater.from(this).inflate(
				R.layout.bond_nobankacct, null);
		addView(mainView);
		setLeftButtonPopupGone();
		setTitle(this.getString(R.string.bond_acct_open_title));
		setText(this.getString(R.string.close));
		btnBack.setVisibility(View.GONE);
		setRightBtnClick(backClick);


		
		isBuy = getIntent().getBooleanExtra(ISBUY, false);
		// 关联新账户
		Button btnDescription = (Button) mainView
				.findViewById(R.id.btn_description);
		btnDescription.setVisibility(View.GONE);// 屏蔽自助关联
		btnDescription.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(NoBankAcctActivity.this,
//						AccInputRelevanceAccountActivity.class);
//				startActivityForResult(intent,
//						ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
				
				BusinessModelControl.gotoAccRelevanceAccount(NoBankAcctActivity.this, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, null);
				if (!isBuy) {
					finish();
				}
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE:
			switch (resultCode) {
			case RESULT_OK:
				setResult(RESULT_OK);
				finish();
				break;
			}
			break;
		}
	}
}
