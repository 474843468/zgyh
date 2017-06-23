package com.chinamworld.bocmbci.biz.setting.accmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.constant.LocalData;

/**
 *    修改别名成功页面  
 * @author xiaoyl
 *
 */
public class EditAccAliaSucceesActivity extends AccountManagerBaseActivity {
	private static final String TAG = "EditAccAliaSucceesActivity";
	
	private TextView accTextView;
	private TextView accTypeTextView;
	private TextView accAliasTextView;
	
	private Button confirmBtn;
	
	private String accNumStr;
	private String accTypeStr;
	private String accAliasStr;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		activityTaskManager.removeAllActivity();
		super.onCreate(savedInstanceState);
		init();
		initData();
	}
	
	@Override
	public void onBackPressed() {
	}

	/**
	 * 初始化布局
	 *@Author xyl
	 */
	private void init(){
		View childView = mainInflater.inflate(R.layout.setting_editalias_success, null);
		tabcontent.addView(childView);
		accTextView = (TextView) childView.findViewById(R.id.set_editalias_acc);
		accTypeTextView = (TextView) childView.findViewById(R.id.set_editalias_acctype);
		accAliasTextView = (TextView) childView.findViewById(R.id.set_editalias_accalias);
		confirmBtn = (Button) childView.findViewById(R.id.set_confirm);
		confirmBtn.setOnClickListener(this);
		setTitle(getResources().getString(R.string.set_title_editnickname));
		back.setVisibility(View.INVISIBLE);
		initRightBtnForMain();
	}
	/**
	 * 初始化数据
	 *@Author xyl
	 */
	private void initData(){
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		accTypeStr = extras.getString(Setting.I_ACCTYPE);
		accNumStr = extras.getString(Setting.I_ACCNUM);
		accAliasStr = extras.getString(Setting.I_ACCALIAS);
		accTextView.setText(accNumStr);
		accTypeTextView.setText(LocalData.AccountType.get(accTypeStr));
		accAliasTextView.setText(accAliasStr);
	}


	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.set_confirm:
			startActivity(new Intent(EditAccAliaSucceesActivity.this,AccountManagerActivity.class));	
			finish();
			break;
		case R.id.ib_top_right_btn:
			this.finish();
			break;
		default:
			break;
		}
	}
	
}
