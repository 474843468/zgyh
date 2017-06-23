package com.chinamworld.bocmbci.biz.comm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.chinamworld.bocmbci.biz.push.PushManager;
import com.chinamworld.bocmbci.log.LogGloble;

/**
 * @ClassName: ConnectivityReceiver
 * @Description: 监听网络状态
 * @author luql
 * @date 2013-10-30 下午02:41:12
 */
// P402
public class ConnectivityReceiver extends BroadcastReceiver {

	private static final String TAG = ConnectivityReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			// LogGloble.i(TAG, "getExtraInfo:" + networkInfo.getExtraInfo());
			// LogGloble.i(TAG, "getReason:" + networkInfo.getReason());
			// LogGloble.d(TAG, "getState:" + networkInfo.getState());
			// LogGloble.i(TAG, "getState().name:" +
			// networkInfo.getState().name());
			// LogGloble.i(TAG, "getSubtype:" + networkInfo.getSubtype());
			// LogGloble.i(TAG, "getSubtypeName:" +
			// networkInfo.getSubtypeName());
			// LogGloble.i(TAG, "getTypeName:" + networkInfo.getTypeName());
			// LogGloble.i(TAG, "isAvailable:" + networkInfo.isAvailable());
			// LogGloble.i(TAG, "isConnected:" + networkInfo.isConnected());
			// LogGloble.i(TAG, "isConnectedOrConnecting:" +
			// networkInfo.isConnectedOrConnecting());
			// LogGloble.i(TAG, "isFailover:" + networkInfo.isFailover());
			// LogGloble.i(TAG, "isRoaming:" + networkInfo.isRoaming());
			if (networkInfo.isConnected()) {
				LogGloble.i(TAG, "Network connected");
				// P402 网络发生改变重启推送服务
				PushManager.getInstance(context).restartPushService();
			}
		} else {
			LogGloble.e(TAG, "Network unavailable");
		}
	}

}
