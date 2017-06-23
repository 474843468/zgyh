package com.chinamworld.bocmbci.biz.push;

import android.content.Context;
import android.content.Intent;

import com.chinamworld.bocmbci.log.LogGloble;

public class PushManager {

	private static final String TAG = PushManager.class.getSimpleName();

	private static PushManager instance;
	private static Context mContext;
	private PushDevice mPushDevice;

	private PushManager() {
		mPushDevice = PushDevice.load();
		if (mPushDevice != null) {
			mPushDevice.save();
		}
	}

	public static PushManager getInstance(Context context) {
		if (instance == null) {
			instance = new PushManager();
		}
		mContext = context;
		return instance;
	}

	/**
	 * 启动消息服务，如已启动不在启动
	 */
	public void startPushService() {
		try {
			Intent intent = new Intent(mContext, PushService.class);
			intent.putExtra("interval", PushSetting.getPushTime());
			mContext.startService(intent);
		} catch (Exception e) {
			LogGloble.e(TAG, e.getMessage(), e);
		}
	}

	/**
	 * 关闭消息服务
	 */
	public void closePushService() {
		try {
			Intent intent = new Intent(mContext, PushService.class);
			mContext.stopService(intent);
		} catch (Exception e) {
			LogGloble.e(TAG, e.getMessage(), e);
		}
	}

	/**
	 * 重启消息服务
	 */
	public void restartPushService() {
		try {
			closePushService();
			Intent intent = new Intent(mContext, PushService.class);
			// intent.putExtra("triggerAtTime", getPushTime().getInterval());
			intent.putExtra("interval", PushSetting.getPushTime());
			mContext.startService(intent);
		} catch (Exception e) {
			LogGloble.e(TAG, e.getMessage(), e);
		}
	}

	 

	/**
	 * 获取轮询设备，如果deviceId无效则返回空
	 * 
	 * @return
	 */
	public PushDevice getPushDevice() {
		return mPushDevice;
	}

	/**
	 * 登陆成功更新pushDevice
	 * 
	 * @param pushDevice
	 */
	public void setPushDevice(PushDevice pushDevice) {
		mPushDevice = pushDevice;
		if (mPushDevice != null) {
			mPushDevice.save();
		}else{
			PushDevice.clear();
		}
	}

}
