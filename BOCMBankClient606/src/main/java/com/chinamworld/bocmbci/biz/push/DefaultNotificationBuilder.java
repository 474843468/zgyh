package com.chinamworld.bocmbci.biz.push;

import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.chinamworld.bocmbci.R;

public class DefaultNotificationBuilder {

	public static NotificationCompat.Builder binder(Context context, String ticker, String title, String text,int icon, PendingIntent pendingIntent) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//		builder.setNumber(1);
//		builder.setSubText("SubText");
		builder.setTicker(ticker);
//		builder.setWhen(PushConfig.NotificationId);
//		builder.setContentInfo(info);
		builder.setContentTitle(title);
		builder.setContentText(text);
		builder.setContentIntent(pendingIntent);
		builder.setSmallIcon(R.drawable.icon);
//		builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
		builder.setAutoCancel(true);
		return builder;
	}
}
