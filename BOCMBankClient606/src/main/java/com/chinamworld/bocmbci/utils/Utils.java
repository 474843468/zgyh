/**
 * 文件名	：Utils.java
 * 创建日期	：2012-10-15
 * Copyright (c) 2003-2012 北京联龙博通

 * All rights reserved.
 */

package com.chinamworld.bocmbci.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.widget.CustomDialog;

public class Utils {

	private static final String TAG = "Utils"; // add by wez 2012.11.06

	/**
	 * bitmap --- base64 string 把bitmap数据编码成base64的字符串
	 * 
	 * @param bitmap
	 *            图片的bitmap
	 * @return
	 */
	public static String bitmaptoString(Bitmap bitmap) {

		// 将Bitmap转换成字符串
		String string = null;
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100, bStream);
		byte[] bytes = bStream.toByteArray();
		string = Base64.encodeToString(bytes, Base64.DEFAULT);
		return string;
	}

	/**
	 * 应用程序版本比较，只有版本较高时才会提示用户更新。版本字符串格式为x.x.x
	 * 
	 * @param newVersion
	 * @param oldVersion
	 * @return
	 */
	public static boolean versionCompare(String newVersion, String oldVersion) {
		if (StringUtil.isNullOrEmpty(newVersion)) {
			return false;
		}
		String[] version = oldVersion.split("\\.");
		String[] latestVersion = newVersion.split("\\.");
		int count = Math.min(version.length, latestVersion.length);
		for (int i = 0; i < count; i++) {
			if (Integer.parseInt(latestVersion[i]) > Integer
					.parseInt(version[i]))
				return true;
			else if (Integer.parseInt(latestVersion[i]) < Integer
					.parseInt(version[i]))
				return false;
		}
		return false;
	}

	/***
	 * 
	 * @param apkFile
	 * @throws IOException
	 *             以下三个函数均为 软件安装 接口
	 */
	public static void installThreadApp(String apkFile, Activity act)
			throws IOException {
		// 先把文件写到缓存上，再安装，注意要配置权限，如果没有权限则装不上
		File cacheDir = act.getCacheDir();
		String cachePath = cacheDir.getAbsolutePath() + "/temp.apk";
		storeApkToFile(act, apkFile, cachePath);
		chmod("666", cachePath);

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.parse("file://" + cachePath),
				"application/vnd.android.package-archive");

		act.startActivity(intent);
	}

	/**
	 * @param permission
	 * @param path
	 */
	private static void chmod(String permission, String path) {
		try {
			String command = "chmod " + permission + " " + path;
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec(command);
		} catch (IOException e) {
			LogGloble.w(TAG, "chmod  error", e);
		}
	}

	/**
	 * 
	 * @param context
	 * @param apkFile
	 * @param path
	 * @return
	 */
	private static boolean storeApkToFile(Context context, String apkFile,
			String path) {
		boolean bRet = false;
		try {
			File f = new File(apkFile);
			InputStream is = new FileInputStream(f);
			File file = new File(path);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);

			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}

			fos.close();
			is.close();

			bRet = true;
		} catch (IOException e) {
			LogGloble.w(TAG, "storeApkToFile  error", e);
		}

		return bRet;
	}

	/**
	 * 调整scrollview中的listview高度
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			if (listItem != null) {
				// ViewGroup.LayoutParams itemp = new
				// LayoutParams(LayoutParams.FILL_PARENT, 30);
				// listItem.setLayoutParams(itemp);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * 
	 * @param mContext
	 * @param appPath
	 *            注册包名
	 * @param actname
	 *            入口Activity 名字
	 * @param apkname
	 *            apk名字 .mp3
	 * @param actAllName
	 *            MainActivity全路径名字
	 */
	public static void aboutMapQuery(final Context mContext,
			final String appPath, final String actname, final String apkname,
			final String actAllName) {
		boolean updateFlag = needUpdateOrInstall(mContext, appPath, actname);
		String message = mContext.getResources().getString(
				R.string.third_no_instal);

		if (!updateFlag) {// 未安装
			BaseDroidApp.getInstanse().showErrorDialog("", message,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							BaseDroidApp.getInstanse().dismissErrorDialog();
							switch ((Integer) v.getTag()) {
							case CustomDialog.TAG_SURE:// 确定
								try {
									installThreadApp(mContext, apkname);
								} catch (Exception e) {
									LogGloble.exceptionPrint(e);
									BaseDroidApp
											.getInstanse()
											.showMessageDialog(
													mContext.getResources()
															.getString(
																	R.string.install_failed),
													new OnClickListener() {

														@Override
														public void onClick(
																View v) {
															BaseDroidApp
																	.getInstanse()
																	.dismissMessageDialog();

														}
													});

								}

							}
						}
					});
		} else {
			if (actAllName.equals(ConstantGloble.ActAllPath_zhifu)) { 
				// 验证支付签名包
//				String sign = Utils.readAssetsFile(mContext, "bocmbc_signature.txt");
				String str = Utils.getPackageSignature(mContext, "com.chinamworld.electronicpayment");
//				String []tmp = sign.split(";;");
				String sign = "3082026d308201d6a00302010202044ded7e9b300d06092a864886f70d0101050500307a310b300906035504061302434e310f300d06035504080c06e58c97e4baac3112301006035504070c09e8a5bfe59f8ee58cba31153013060355040a0c0ce4b8ade59bbde993b6e8a18c31183016060355040b0c0fe794b5e5ad90e993b6e8a18ce983a83115301306035504030c0ce4b8ade59bbde993b6e8a18c3020170d3131303630373031323735355a180f32353131303230363031323735355a307a310b300906035504061302434e310f300d06035504080c06e58c97e4baac3112301006035504070c09e8a5bfe59f8ee58cba31153013060355040a0c0ce4b8ade59bbde993b6e8a18c31183016060355040b0c0fe794b5e5ad90e993b6e8a18ce983a83115301306035504030c0ce4b8ade59bbde993b6e8a18c30819f300d06092a864886f70d010101050003818d0030818902818100c818796a5a28cb21da109fd71495eb23c35dd3419d1ee4db56437d846a09ba2db1143f07f9f66b54b97c42a45cbd50dd873da53c622c28855140d390664de513e13250a2df7fdf57a8d867fddd9fee5e5f5b93d6af69faab22e9430b5fca56f9af9b1920a45db29aa5f321bed41ade00ba16454fdea265c84f0f61033c1fece50203010001300d06092a864886f70d010105050003818100c381d0830c50aced46225d04f6cf56244c4f96bffce8047753d7bb41a44ae8ceeee919f150ed26cb22af2579f51f5d4c5d1b875bbb6a3b1f6c6221d4b76cb52bbf428d0ba42bfedaf135dcc2f15765b617ec154bf565b42b7763ffdcba1e2814137de61ebbf335af00841e368e578995616802db4d0dce3fe0ffd18c1d19d090";
				if(sign.equals(str) == false){
					// 签名不一致报错
					BaseDroidApp.getInstanse().showMessageDialog("签名不一致", new OnClickListener(){

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							BaseDroidApp.getInstanse().dismissMessageDialog();
						}
						
					});
					
					
					return ;
				}
				
				startPayPlui(mContext);
			} else {
				startOtherApp(mContext, new Intent(), appPath, actAllName);
			}
		}
	}

	/**
	 * 启动移动支付插件
	 */
	public static void startPayPlui(Context context) {
		// 注意启动支付插件时关掉手机银行后台的三分钟计时
		Intent tIntent = new Intent();
		tIntent.putExtra(ConstantGloble.ZHIFU_KEY_TYPE,
				ConstantGloble.APP_PACAGENAME);
		tIntent.putExtra(ConstantGloble.ZHIFU_KEY_COOKIE,
				BiiHttpEngine.cookieCurrent);
		tIntent.putExtra(ConstantGloble.ZHIFU_KEY_LOGININFO, BaseDroidApp
				.getInstanse().getLoginInfo());
		startOtherApp(context, tIntent, ConstantGloble.AppPath_zhifu,
				ConstantGloble.ActAllPath_zhifu);
	}

	private static void startOtherApp(Context context, final Intent tIntent,
			final String appPath, final String actAllName) {
		try {
			ComponentName tComp = new ComponentName(appPath, actAllName);
			tIntent.setComponent(tComp);
			tIntent.setAction("Android.intent.action.MAIN");
			tIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(tIntent);
			ConstantGloble.startOtherApp = true;
		} catch (ActivityNotFoundException e) {
		}
	}

	/***
	 * Function:
	 * 
	 * @author MeePwn
	 * @DateTime 2015-4-16 上午11:57:26
	 * @param mContext
	 * @param apkName
	 *            文件名
	 * @param urlDownload
	 *            下载地址
	 */
	public static boolean installApp(Context mContext, String apkName,
			String urlDownload) {
		// BaseHttpEngine.showProgressDialog();
		boolean bRet = false;
		OutputStream os = null;
		InputStream is = null;
		String newFilename = null;
		HttpURLConnection con = null;
		int length = 0;
		int startPosition = 0;
		try {
			// 获得存储卡路径，构成 保存文件的目标路径
			String dirName = Environment.getExternalStorageDirectory()
					+ "/BOCMBank/";
			File f = new File(dirName);
			if (!f.exists()) {
				f.mkdir();
			}
			// 准备拼接新的文件名（保存在存储卡后的文件名）
			newFilename = apkName + ".apk";
			newFilename = dirName + newFilename;
			File file = new File(newFilename);
			// 如果目标文件已经存在，则删除。产生覆盖旧文件的效果
			if (file.exists()) {
				file.delete();
			}
			// 构造URL
			URL url = new URL(urlDownload);
			// 打开连接
			con = (HttpURLConnection) url.openConnection();
			con.setAllowUserInteraction(true);
			// 获得文件的长度
			length = con.getContentLength();
			is = con.getInputStream();
			// 1K的数据缓冲
			byte[] bs = new byte[1024];
			// 输出的文件流
			os = new FileOutputStream(newFilename);
			// 开始读取
			byte[] buf = new byte[1024 * 10];
			int read = 0;
			int curSize = startPosition;
			while (true) {
				read = is.read(buf);
				if (read == -1) {
					break;
				}
				os.write(buf, 0, read);
				curSize = curSize + read;
				if (curSize == length) {
					bRet = true;
					break;
				}
				Thread.sleep(10);
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (is != null)
					is.close();
				if (os != null)
					os.close();
				if (con != null)
					con.disconnect();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return false;
		}
		try {
			if (is != null)
				is.close();
			if (os != null)
				os.close();
			if (con != null)
				con.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (bRet) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.parse("file://" + newFilename),
					"application/vnd.android.package-archive");
			mContext.startActivity(intent);
		}
		return bRet;
	}

	/**
	 * 判断程序是否安装
	 * 
	 * @param mContext
	 * @param packageName
	 *            包名
	 * @param actName
	 *            入口的页面名称
	 * @return
	 */
	public static boolean needUpdateOrInstall(Context mContext,
			String packageName, String actName) {
		List<PackageInfo> packs = mContext.getPackageManager()
				.getInstalledPackages(0);
		for (int i = 0; i < packs.size(); i++) {
			PackageInfo p = packs.get(i);
			if ((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				if (p.packageName.equals(packageName)) {
					return true;
				}
			}
		}
		return false;
	}

	public static void installThreadApp(Context mContext, String apkName)
			throws IOException {
		// 先把文件写到缓存上，再安装，注意要配置权限，如果没有权限则装不上
		File cacheDir = mContext.getCacheDir();
		String cachePath = cacheDir.getAbsolutePath() + "/temp.apk";
		retrieveApkFromAssets(mContext, apkName, cachePath);
		chmod("666", cachePath);

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.parse("file://" + cachePath),
				"application/vnd.android.package-archive");
		mContext.startActivity(intent);
	}

	public static boolean retrieveApkFromAssets(Context context,
			String fileName, String path) {
		boolean bRet = false;
		try {
			InputStream is = context.getAssets().open("apks/" + fileName);

			File file = new File(path);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);

			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.close();
			is.close();
			bRet = true;
		} catch (IOException e) {
			LogGloble.exceptionPrint(e);
		}
		return bRet;
	}

	/**
	 * 根据包名，获得签名信息
	 * 
	 * @param con
	 * @param pkgname
	 * @return
	 */
	public static String getPackageSignature(Context con, String pkgname) {
		String signature1 = "";
		try {
			/** 通过包管理器获得指定包名包含签名的包信息 **/
			PackageInfo packageInfo = con.getPackageManager().getPackageInfo(
					pkgname, PackageManager.GET_SIGNATURES);
			/******* 通过返回的包信息获得签名数组 *******/
			Signature[] signatures = packageInfo.signatures;
			/******* 循环遍历签名数组拼接应用签名 *******/
			StringBuilder builder = new StringBuilder();
			;
			for (Signature signature : signatures) {
				builder.append(signature.toCharsString());
			}
			/************** 得到应用签名 **************/
			signature1 = builder.toString();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return signature1;
	}

	/**
	 * 读取assets目录文件
	 * 
	 * @param c
	 * @param fileName
	 * @return
	 */
	public static String readAssetsFile(Context c, String fileName) {
		String result = null;
		InputStream in;
		try {
			in = c.getAssets().open(fileName);
			int length = in.available();
			byte[] buffer = new byte[length];
			in.read(buffer);
			result = EncodingUtils.getString(buffer, "UTF-8");
		} catch (IOException e) {
			LogGloble.exceptionPrint(e);
		}
		return result;
	}

}
