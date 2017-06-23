package com.chinamworld.bocmbci.biz.goldbonus.accountmanager;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.biz.goldbonus.GoldBonusBaseActivity;
import com.chinamworld.bocmbci.biz.goldbonus.GoldbonusLocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

public class GoldbounsReminderActivity extends GoldBonusBaseActivity{
	private TextView tv_message;
	private Button confirm;
	private String titleStr;
	@Override   
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Button right = (Button) findViewById(R.id.ib_top_right_btn);
		right.setVisibility(View.GONE);
		getBackgroundLayout().setRightButtonNewText(null);
		Button back = (Button) findViewById(R.id.ib_back);
		// back.setVisibility(View.GONE);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ActivityTaskManager.getInstance().removeAllSecondActivity();
			}
		});
//		setTitle(R.string.goldbonus_account_manager);//默认账户管理标题
		getBackgroundLayout().setTitleNewText(R.string.goldbonus_account_manager);
		getBackgroundLayout().setRightButtonNewText(null);

		setContentView(R.layout.goldbouns_reminder_message_info);
		titleStr = this.getIntent().getStringExtra("title");
		if(titleStr != null){
//			setTitle(titleStr);
			getBackgroundLayout().setTitleNewText(titleStr);
		}
		tv_message=(TextView) findViewById(R.id.tv_message);
		confirm=(Button) findViewById(R.id.bremit_confirm_info_ok);
		String accountNumber = GoldbonusLocalData.getInstance().accountNumber;
		tv_message.setText((getResources().getString(R.string.goldbonus_account_reminder_message_ahead))+
				StringUtil.getForSixForString(accountNumber)+getResources().getString(R.string.goldbonus_account_reminder_message_after));
		
		confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				ActivityTaskManager.getInstance().removeAllActivity();
				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				finish();
			}
		});
	}

}
