package com.chinamworld.bocmbci.biz.comm.message;

import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.chinamworld.bocmbci.base.service.BaseService;
import com.chinamworld.bocmbci.log.LogGloble;

public class MessageService extends BaseService {

	IMessageService.Stub stub = new IMessageService.Stub() {

		@Override
		public void startMessagePooling() throws RemoteException {
//			Toast.makeText(MessageService.this, "message pooling",
//					Toast.LENGTH_LONG).show();
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return stub;
	}

	@Override
	public void onCreate() {
		LogGloble.i("service--------------", "service on create");
		super.onCreate();
//		Toast.makeText(this, "正在连接服务器...", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		LogGloble.i("service--------------", "service on start");
		super.onStart(intent, startId);
//		Toast.makeText(this, "Service Start", Toast.LENGTH_SHORT).show();

	}
}
