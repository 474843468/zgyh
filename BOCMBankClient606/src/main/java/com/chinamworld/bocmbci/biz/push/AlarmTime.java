package com.chinamworld.bocmbci.biz.push;

import java.util.UUID;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.chinamworld.bocmbci.log.LogGloble;

/**
 * @ClassName: AlarmTime
 * @Description: 定时器
 * @author luql
 * @date 2013-10-30 上午11:44:50
 */
public class AlarmTime {

	public static final String TAG = AlarmTime.class.getSimpleName();

	private Context context;
	private MyBroadcast myBroadcast;
	private PendingIntent pendingIntent;
	private AlarmManager mAlarmManager;

	private OnAlarmListener mOnAlarmListener;
	private boolean mResetCallbakFlag;
	private boolean mReset;
	private boolean mAlarmState;

	private String intentAction;

	public AlarmTime(Context context) {
		super();
		this.context = context;
		myBroadcast = new MyBroadcast();
		mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		intentAction = UUID.randomUUID().toString();
		Intent intent = new Intent();
		intent.setAction(intentAction);
		pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		mReset = false;
		mResetCallbakFlag = true;
		mAlarmState = false;
	}

	/**
	 * 开启闹钟,设置引发 Elapsed 事件的间隔。
	 */
	public void start(long interval) {
		setRepeating(System.currentTimeMillis(), interval);
	}

	/**
	 * 开启闹钟
	 * 
	 * @param triggerAtTime Time the alarm should first go off, using the
	 *        appropriate clock (depending on the alarm type).
	 * @param interval Interval between subsequent repeats of the alarm.
	 */
	public void start(long triggerAtTime, long interval) {
		setRepeating(System.currentTimeMillis() + triggerAtTime, interval);
	}

	private void setRepeating(long triggerAtTime, long interval) {
		if (!mAlarmState) {
			try {
				mAlarmState = true;
				IntentFilter intentF = new IntentFilter(intentAction);
				context.registerReceiver(myBroadcast, intentF);
				mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtTime, interval, pendingIntent);
			} catch (Exception e) {
				LogGloble.e(TAG, e.getMessage(), e);
			}
		}
	}

	/**
	 * 关闭
	 */
	public void stop() {
		if (mAlarmState) {
			try {
				mAlarmState = false;
				context.unregisterReceiver(myBroadcast);
				mAlarmManager.cancel(pendingIntent);
			} catch (Exception e) {
				LogGloble.e(TAG, e.getMessage(), e);
			}
		}
	}

	public boolean isAlarming() {
		return mAlarmState;
	}

	/**
	 * 获取或设置一个值，该值指示 Timer 是应在每次指定的间隔结束时引发 Elapsed 事件， 还是仅在指定的间隔第一次结束后引发该事件。
	 * 
	 * @param reset
	 */
	public void setAutoReset(boolean reset) {
		mReset = reset;
		mResetCallbakFlag = true;
	}

	public void setOnAlarmListener(OnAlarmListener listener) {
		mOnAlarmListener = listener;
	}

	public interface OnAlarmListener {
		void onAlarm();
	}

	private class MyBroadcast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			LogGloble.d(TAG, "Alarm Broadcast");
			if (mReset) {
				callback();
			} else {
				stop();
				if (mResetCallbakFlag) {
					mResetCallbakFlag = false;
					callback();
				}
			}
		}

		public void callback() {
			if (mOnAlarmListener != null) {
				mOnAlarmListener.onAlarm();
			}
		}
	}
}
