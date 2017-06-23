package com.chinamworld.bocmbci.biz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.infoserve.push.PushInit;
import com.chinamworld.bocmbci.http.GetPhoneInfo;

public class DexActivity extends FragmentActivity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//BaseDroidApp instanse = new BaseDroidApp();
		//instanse.onCreate(this.getApplicationContext());
		GetPhoneInfo.initActFirst(this);
		PushInit.initPushManager(this);
		setContentView(R.layout.loading_activity_layout);		
		Intent it=new Intent(DexActivity.this, GuidePageActivity.class);
		startActivity(it);
		finish();

	}

}
