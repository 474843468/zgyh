package com.chinamworld.bocmbci.biz.comm.receiver;

import android.content.Context;
import android.content.Intent;

import com.chinamworld.bocmbci.base.receiver.BaseReceiver;
import com.chinamworld.bocmbci.biz.comm.message.MessageService;

public class BootReceiver extends BaseReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent(context, MessageService.class);
		context.startService(service);
		
	}

}
