package com.chinamworld.bocmbci.biz;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConfig;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiApi;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.boc.ModelBoc;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.fidget.VersionUpdata;
import com.chinamworld.bocmbci.http.GetPhoneInfo;
import com.chinamworld.bocmbci.log.LogGloble;

import java.util.LinkedHashMap;

public class LoadingActivity extends BaseActivity {
	int index = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_activity_layout);
		GetPhoneInfo.initActFirst(this);
		GetPhoneInfo.getPhoneContracts();
		// 检测所有的数据实体模型是否已经建表，没有表则进行创建
		BaseDroidApp.getInstanse().getDbHelper().createTableIfNotExists();

	
		// 检测版本是否需要更新
		 if (SystemConfig.ISCHECKVERSION) {
			 try {
				 VersionUpdata.init(this);
			 }
			 catch (InterruptedException e) {
				 LogGloble.exceptionPrint(e);
			 }
		 }
		 else {
			 if(SystemConfig.IS_SELECT_INTER == false){
				 ModelBoc.gotoMainActivity(this);
//				 Intent intent = new Intent();
//				 intent.setClass(LoadingActivity.this, com.boc.bocsoft.mobile.bocmobile.buss.system.main.ui.MainActivity.class);
//				 startActivity(intent);
				 finish();
				 return;
			 }
		 	startHandler.sendEmptyMessageDelayed(0, 200);
		 }
	}
	private Handler startHandler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {

				showSelectInter();

			return true;
		}
	});
	boolean isShowSelect = true;
	private void showSelectInter() {
		// *************************开始 打开所有地址
		if (SystemConfig.IS_SELECT_INTER && isShowSelect) {
			isShowSelect = false;
			View view = LayoutInflater.from(LoadingActivity.this).inflate(R.layout.address_input, null);
			Spinner sp = (Spinner) view.findViewById(R.id.findpwd_sp_idcty);
			final LinkedHashMap<String, String> inters = LocalData.addressApi;
			final String[] data = new String[inters.size()];
			inters.keySet().toArray(data);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(LoadingActivity.this, R.layout.dept_spinner, data);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp.setAdapter(adapter);
			sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					BiiApi.BASE_HTTP_URL = inters.get(data[position]);
					BiiApi.BASE_URL = BiiApi.BASE_HTTP_URL;

					BiiApi.BASE_API_URL = BiiApi.BASE_URL + BiiApi.BASE_API;
					BiiApi.FINCADDRESS = "http://22.188.130.127:9080/EBankingInfoSvc/";
					if (position == 9) {// 公网地址
						Comm.AQUIRE_IMAGE_CODE = BiiApi.BASE_HTTP_URL + "ImageValidationNew/validation.gif";
					}
					SystemConfig.BASE_HTTP_URL = BiiApi.BASE_API_URL;
					ApplicationConfig.setBiiURL(BiiApi.BASE_API_URL ,BiiApi.BASE_HTTP_URL +"ImageValidationNew/validation.gif");
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}
			});
			final Button btnAddress = (Button) view.findViewById(R.id.btnConfirm);
			btnAddress.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					BaseDroidApp.getInstanse().dismissErrorDialog();
					// finish();
//					Intent intent = new Intent();
//					intent.setClass(LoadingActivity.this, com.boc.bocsoft.mobile.bocmobile.buss.system.main.ui.MainActivity.class);
//					startActivity(intent);
					ModelBoc.gotoMainActivity(LoadingActivity.this);
					finish();
				}
			});
			BaseDroidApp.getInstanse().showAddressDialog(view);

		}else {
			ModelBoc.gotoMainActivity(LoadingActivity.this);
			finish();
		}
	}

	
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		// 状态栏的高度
		LayoutValue.TITLEHEIGHT = LayoutValue.SCREEN_HEIGHT
				- ((LinearLayout) findViewById(R.id.rltop)).getMeasuredHeight();
		TextView tv = (TextView) findViewById(R.id.tv);
		int[] location = new int[2];
		tv.getLocationOnScreen(location);
		int y = location[1];
		LayoutValue.SCREEN_BOTTOMHIGHT = LayoutValue.SCREEN_HEIGHT - y;
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	}

}
