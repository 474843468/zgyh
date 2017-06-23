package com.chinamworld.bocmbci.biz.audio;


import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.device.key.KeyCertInfo;
import com.boc.device.key.KeyInfo;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.audio.bocm.BOCMKeyModel;

/**
 * 
 * 音频key(中银E盾)信息
 *
 */
public class AudioKeyInfoActivity extends AudioKeyBaseActivity {

	private LinearLayout tabcontent;
	private View view;
	
	/** 连接状态 */
	private TextView connect_status;
	/** PIN码状态 */
	private TextView pin_status;
	/** 序列号 */
	private TextView serial_num;
	/** 低电状态 */
	private TextView low_power_status;
	/** 证书到期日期 */
	private TextView end_date;
	/** 音频key版本驱动号 */
	private TextView driver_version;
	/** 低电量提示信息 */
	private LinearLayout low_power_tip;
	
	private boolean needShowDialog = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setObserver(this);
		init();
	}
	
	private void getAudioKeyInfo() {
		if (AudioKeyManager.getInstance().hasAudioKey()) {
			if (BaseDroidApp.getInstanse().isWiredHeadsetOn()) {
//				needShowDialog = false;
				getKeyInfo(this, null);
			} else {
				BaseDroidApp.getInstanse().showMessageDialog(
						getString(R.string.no_device_connect), cancelListener);
			}
		} else {
			BaseDroidApp.getInstanse().showMessageDialog(
					getString(R.string.no_have_audio_key), cancelListener);
		}
	}

	private void init() {
		setTitle(getString(R.string.audio_key_info_title));
		ibRight.setVisibility(View.GONE);
		tabcontent = (LinearLayout) findViewById(R.id.sliding_body);
		view = mInflater.inflate(R.layout.audio_key_info_activity, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		setLeftSelectedPosition("settingManager_10");
		
		connect_status = (TextView) findViewById(R.id.connect_status);
		pin_status = (TextView) findViewById(R.id.pin_status);
		serial_num = (TextView) findViewById(R.id.serial_num);
		low_power_status = (TextView) findViewById(R.id.low_power_status);
		end_date = (TextView) findViewById(R.id.end_date);
		driver_version = (TextView) findViewById(R.id.driver_version);
		low_power_tip = (LinearLayout) findViewById(R.id.low_power_tip);
		
		TextView driver_version_title = (TextView) findViewById(R.id.driver_version_title);
		TextView low_power_tip_tv = (TextView) findViewById(R.id.low_power_tip_tv);
		setOnShowAllTextListener(this,driver_version_title,low_power_tip_tv,end_date,serial_num);
	}
	
	public void initViews(){
		connect_status.setText(getString(R.string.disconnect));
		pin_status.setText("-");
		serial_num.setText("-");
		low_power_status.setText("-");
		end_date.setText("-");
		driver_version.setText("-");
		low_power_tip.setVisibility(View.GONE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		initViews();
		getAudioKeyInfo();
	}
	
	@Override
	public void deviceDisconnect() {
		super.deviceDisconnect();
		initViews();
	}
	
	@Override
	public void successCallback(Object obj) {
		super.successCallback(obj);
		if (obj instanceof BOCMKeyModel) {
			updateViews((BOCMKeyModel) obj);
		} 
	}
	
	public void updateViews(BOCMKeyModel model){
		KeyInfo mKeyInfo = model.getmKeyInfo();
		KeyCertInfo mKeyCertInfo = model.getmKeyCertInfo();
		String driverVersion = model.getDriverVersion();

		connect_status.setText(getString(R.string.connect));

		int pinStatus = mKeyInfo.getPinStatus();
		switch (pinStatus) {
		case 0:
			pin_status.setText(getString(R.string.locked));
			break;
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
			pin_status.setText(getString(R.string.remain) + pinStatus + getString(R.string.time));
			break;
		default:
			pin_status.setText("-");
			break;
		}

		serial_num.setText(mKeyInfo.getKeySN());

		int batteryStatus = mKeyInfo.getBatteryStatus();
		switch (batteryStatus) {
		case 0:
			low_power_status.setText(getString(R.string.low_power));
			low_power_tip.setVisibility(View.VISIBLE);
			break;
		case 1:
			low_power_status.setText(getString(R.string.normal_power));
			low_power_tip.setVisibility(View.GONE);
			break;
		default:
			low_power_status.setText("-");
			low_power_tip.setVisibility(View.GONE);
			break;
		}

		String date = mKeyCertInfo.getExpiredDate();
		if (date.contains("/")) {
		} else {
			if(date.length() == 8){
				String year = date.substring(0,4);
				String month = date.substring(4, 6);
				String day = date.substring(6);
				date = year + "/" + month + "/" + day;
			}
		}
		end_date.setText(date);
		
		driver_version.setText("v" + driverVersion);
//		if (needShowDialog) {
			if (mKeyInfo.isPinNeedModify()) {
				showNeedModifyPinDialog();
			}
//		}
//		needShowDialog = true;
	}
	
	@Override
	public void commonErrorHandle(int errorId) {
		super.commonErrorHandle(errorId);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
