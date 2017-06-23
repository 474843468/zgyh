package com.chinamworld.bocmbci.biz.push;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.chinamworld.bocmbci.biz.push.AlarmTime.OnAlarmListener;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.NetUtils;

/**
 * @ClassName: MsgService
 * @Description: 启动消息服务
 * @author luql
 * @date 2013-10-28 下午04:59:00
 */
public class PushService extends Service implements OnAlarmListener {

	private static final String TAG = PushService.class.getName();
	private AlarmTime mMsgAlarm;

	@Override
	public void onCreate() {
		super.onCreate();
		LogGloble.i(TAG, "onCreate");
		mMsgAlarm = new AlarmTime(this);
		mMsgAlarm.setAutoReset(true);
		mMsgAlarm.setOnAlarmListener(this);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		LogGloble.i(TAG, "onStart");
		if (intent != null) {
			int triggerAtTime = intent.getIntExtra("triggerAtTime", 0);
			long interval = intent.getLongExtra("interval", PushSetting.getPushTime());

			FLogGloble.i(TAG, "消息服务启动 triggerAtTime:" + triggerAtTime + ", interval:" + interval);
			if (!mMsgAlarm.isAlarming()) {
				mMsgAlarm.start(triggerAtTime, interval);
			}
		}

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LogGloble.i(TAG, "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogGloble.i(TAG, "onDestroy");
		if (mMsgAlarm != null) {
			mMsgAlarm.stop();
		}
	}

	/**
	 * 闹钟回调
	 */
	@Override
	public void onAlarm() {
		LogGloble.i(TAG, "PushService onAlarm()");
		PushDevice pushDevice = PushManager.getInstance(this).getPushDevice();
		if (pushDevice != null) {
			// 判断网络
			if (NetUtils.isConnected(this)) {
				boolean pushState = PushSetting.getPushState();
				LogGloble.i(TAG, "PushService push>>" + "pushState:" + pushState + "," + pushDevice);
				if (pushState) {
//					PushCount.REQUEST_COUNT++;
//					MessageService.getInstance(this).pushMessages(pushDevice.getDeviceType(), pushDevice.getDeviceId());
				}else{
					stopSelf();
				}
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
