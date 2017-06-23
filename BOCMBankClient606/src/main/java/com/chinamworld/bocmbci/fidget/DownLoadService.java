package com.chinamworld.bocmbci.fidget;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.PublicDataCenter;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 更新下载后台服务
 * 
 * @author xby
 * 
 */
public class DownLoadService extends Service {

	private static final int NOTIFICATION_ID = 0x14;
	private Notification noti = null;
	private NotificationManager manager = null;
	private FinalHttp fh;
	private Binder serviceBinder = new DownLoadServiceBinder();
	@SuppressWarnings("rawtypes")
	private HttpHandler downloadHandler;
	private boolean isLoading = false;
	private static final String DOWNLOAD_PATH = "http://pic.bankofchina.com/bocappd/mbs/bocmbci_a.apk";
	private static final String DOWNLOADRECEIVER = "com.chinamworold.downloadcancle.receiver";
	private NotificationCompat.Builder builder = null;
	/**
	 * 程序下载文件夹SD卡中根目录下
	 */
	public static final String DOWNLOADFILENAME = "BOCMBCIDOWNLOAD";
	/**通知栏下载取消的监听广播*/
	private DownloadCancleReceiver mDownloadCancleReceiver;
	private long httpsize;
	private long filesize;

	@Override
	public void onCreate() {
		super.onCreate();

		builder = new NotificationCompat.Builder(getApplication());
		builder.setWhen(System.currentTimeMillis());
		builder.setNumber(NOTIFICATION_ID);
		builder.setTicker("中国银行");
		builder.setSmallIcon(R.drawable.icon);
		builder.setContentTitle("Title");
		builder.setContentText("ContentText");
		RemoteViews remoteViews = new RemoteViews(getApplication()
				.getPackageName(), R.layout.download_notification_layout);
		builder.setContent(remoteViews);

		PendingIntent pendingintent = PendingIntent.getActivity(this, 0,
				new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
		builder.setContentIntent(pendingintent);
		noti = builder.build();
		noti.contentIntent = pendingintent;
		PendingIntent deletePendingIntent = PendingIntent.getBroadcast(
				getApplicationContext(), 0, new Intent(DOWNLOADRECEIVER),
				PendingIntent.FLAG_CANCEL_CURRENT);
		builder.setDeleteIntent(deletePendingIntent);
		noti.contentView = remoteViews;
		noti.deleteIntent = deletePendingIntent;
		// 这是通知栏的点击事件
		manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		fh = new FinalHttp();
		String sdcardpath = android.os.Environment
				.getExternalStorageDirectory() + "/" + DOWNLOADFILENAME;
		File file = new File(sdcardpath);
		if (!file.exists()) {
			try {
				file.mkdirs();
			} catch (Exception e) {
			}
		}
		IntentFilter intentFilter = new IntentFilter(DOWNLOADRECEIVER);
		mDownloadCancleReceiver = new DownloadCancleReceiver();
		registerReceiver(mDownloadCancleReceiver, intentFilter);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return serviceBinder;
	}

	@Override
	public void onDestroy() {
	    this.unregisterReceiver(mDownloadCancleReceiver);
		if (downloadHandler != null) {
			downloadHandler.stop();
		}
		isLoading = false;
		manager.cancel(NOTIFICATION_ID);
		super.onDestroy();
	}

	public void startDownLoad(String url) {
		manager.notify(NOTIFICATION_ID, noti);
		downLoadClient(url);
	}

	public class DownLoadServiceBinder extends Binder {
		public DownLoadService getService() {
			return DownLoadService.this;
		}
	}

	/**
	 * 下载客户端
	 * 
	 * @param url
	 *            下载地址
	 */
	private void downLoadClient(String url) {
		// 下载文件
		String sdcardpath = android.os.Environment
				.getExternalStorageDirectory() + "/" + DOWNLOADFILENAME;
		final String fileName = sdcardpath + File.separator + "bocmbci"
				+ PublicDataCenter.getInstance().lastVersion + ".apk";
		if( StringUtil.isNullOrEmpty(PublicDataCenter.getInstance().lastVersion)){
			stopSelf();
			return;
		}
		File file = new File(sdcardpath);
		if (!file.exists()) {
			try {
				file.mkdirs();
				if (!file.mkdirs())
					Toast.makeText(getApplicationContext(),
							"未检测到sd卡，如需更新请进入官网下载最新版本", Toast.LENGTH_LONG)
							.show();
				return;
			} catch (Exception e) {
			}
		}
		downloadHandler = fh.download(url, fileName, true,
				new AjaxCallBack<File>() {

					@Override
					public void onStart() {
						super.onStart();
						isLoading = true;
					}

					@Override
					public void onLoading(long count, long current) {
						super.onLoading(count, current);
						int currentPro = (int) (current / (count / 100));
						if (isLoading) {
							if (currentPro > 0) {
								noti.contentView.setProgressBar(R.id.proBar,
										100, currentPro, false);
								noti.contentView.setTextViewText(R.id.tvPro,
										currentPro + "%");
								manager.notify(NOTIFICATION_ID, noti);
							}
						} else {
							manager.cancel(NOTIFICATION_ID);
						}
					}

					@Override
					public void onSuccess(File t) {
						manager.cancel(NOTIFICATION_ID);
						if (t != null) {
							// 跳转到安装目录
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							String type = "application/vnd.android.package-archive";
							intent.setDataAndType(Uri.fromFile(t), type);
							startActivity(intent);
						} else {

						}
						super.onSuccess(t);
						stopSelf();
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);
						isLoading = false;
						if (errorNo == 416) {// maybe you have download complete
							try {
								new Thread(new Runnable() {
									@Override
									public void run() {
										try {
											URL url = new URL(DOWNLOAD_PATH);
											HttpURLConnection urlcon = (HttpURLConnection) url
													.openConnection();
											// 根据响应获取文件大小
											httpsize = urlcon
													.getContentLength();
											File file = new File(fileName);
											if (file != null) {
												filesize = file.length();
											}
										} catch (Exception e) {
											LogGloble.exceptionPrint(e);
										} finally {
											fialueHandler.sendEmptyMessage(0);
										}
									}
								}).start();
							} catch (Exception e) {
								LogGloble.exceptionPrint(e);
							}
						} else {
							Toast.makeText(getApplicationContext(), "中国银行手机银行下载失败，请查看网络状态", Toast.LENGTH_SHORT).show();
							stopSelf();
						}
					}

				});

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (!isLoading) {
			startDownLoad(DOWNLOAD_PATH);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	Handler fialueHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			try {
				if ((httpsize == filesize) && httpsize > 0) {
					// 下载文件
					String sdcardpath = android.os.Environment
							.getExternalStorageDirectory()
							+ "/"
							+ DOWNLOADFILENAME;
					final String fileName = sdcardpath + File.separator
							+ "bocmbci" + PublicDataCenter.getInstance().lastVersion
							+ ".apk";
					File file = new File(fileName);
					// 跳转到安装目录
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					String type = "application/vnd.android.package-archive";
					intent.setDataAndType(Uri.fromFile(file), type);
					startActivity(intent);
					isLoading = false;
				}
			} catch (Exception e) {
				LogGloble.exceptionPrint(e);
			} finally {
				stopSelf();
			}
		}

	};
	private class DownloadCancleReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(DOWNLOADRECEIVER.equals(action)){//取消
				stopSelf();
			}
		}
	}

}