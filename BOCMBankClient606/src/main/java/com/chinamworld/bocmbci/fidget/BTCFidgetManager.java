package com.chinamworld.bocmbci.fidget;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.kxml2.io.KXmlParser;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.LoadingActivity;
import com.chinamworld.bocmbci.biz.MainActivity;
import com.chinamworld.bocmbci.biz.more.MoreMenuActivity;
import com.chinamworld.bocmbci.biz.push.PushManager;
import com.chinamworld.bocmbci.biz.push.PushSetting;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.BTCLocalSrcManager;
import com.chinamworld.bocmbci.widget.CustomDialog;

public class BTCFidgetManager {
	public static final String TAG = BTCFidgetManager.class.getSimpleName();
	public static boolean err; // 通讯异常标识 xxh 2012-1-13
	// public static boolean timeout; // 通讯超时标识 xxh 2012-1-13
	public static boolean timeouted; // 通讯已经超时标识 xxh 2012-2-1
	public static boolean updataFlag; // 是否有更新标识 xxh 2012-1-13
	public static boolean haveInternetFlag; // 是否有网络标识 xxh 2012-2-22
	public static boolean updata;// 获取更新信息通讯是否结束
	public static boolean logoEnd;// logo页面是否结束
	static String APP_VERSION;// 当前程序版本
	public static String LAST_VERSION;// 最新程序版本
	static String JS_VERSION;// fidget公共脚本
	static String JS_LAST_VERSION;// fidget公共脚本
	static String JS_URL;// fidget公共脚本下载地址
	public static String defaultCity;// 首选机构
	public static String defaultCityID;// 首选机构号
	static BTCDBManager db;// 已安装fidget数据库
	public static List<Map<String, String>> cityList = new ArrayList<Map<String, String>>();// 机构列表
	static List<BTCFidget> fidgetUpdataList;// fidget可更新列表
	static List<BTCFidget> fidgetList = new ArrayList<BTCFidget>();// 当前机构已安装fidget列表
	static List<String> fidgetListId = new ArrayList<String>();// 当前机构已安装fidget列表Id
	static List<BTCFidget> fidgetDownloadList = new ArrayList<BTCFidget>();// 当前机构可增加fidget列表
	static int cityID;// 用于区分几个不同的机构选择列表，-1代表切换机构，0代表添加特色服务，1代表选择默认机构
	static int fidgetID;// 当前选中fidget在fidgetDownloadList中的索引
	static String[] fidgetDetil;// fidget详情
	public static BTCFidget fidget;// 当前使用的fidget
	static int fidgetPos;// 当前使用的fidget在fidgetList中的索引
	static SharedPreferences app;// 应用程序设置
	static SharedPreferences fidgetOrc;// 应用程序设置
	static Drawable fidgetIcon;// fidget详情图片
	static Resources res;
	public static Dialog mainDialog;// 主菜单显示的更新提示
	static Context mainAct;// 主菜单activity引用
	static String updataWord;// 主菜单activity引用
	private boolean isStop;// 通讯终端标志
	private Dialog pd;// 通讯进程条
	// add by qr 2011-08-18 版本下载路径
	private static String DOWNLOADPAHT = "/sdcard/bocmbcsup";
	private static String HTML_FILENAME = "bocup_ex.html";
	private static String BG_NAME = "bg.jpg";
	// add xxh 2012-1-9 强制更新标识
	static public String MustUpdate = "";
	static Activity activity;
	private Thread downLoadThread;
	public static ArrayList<HashMap<String, Object>> meumList;

	// end
	// 初始化fidgetManager
	public static void init(final Activity act) throws InterruptedException {
		initParams(act);
		mainDialog = new AlertDialog.Builder(act).create();
		if (mainDialog.isShowing()) {
			mainDialog.dismiss();
		}
		mainDialog.show();
		mainDialog.setCancelable(false);
		LayoutInflater factory = LayoutInflater.from(act);
		// 加载progress_dialog为对话框的布局xml
		View view = factory.inflate(R.layout.progress_figet, null);
		mainDialog.getWindow().setContentView(view);
		Window window = mainDialog.getWindow();
		// 设置通讯框显示时背景不变暗
		WindowManager.LayoutParams params = window.getAttributes();
		params.dimAmount = 0f;
		window.setAttributes(params);

		connect(act);
	}

	public static void initParams(final Activity act) {
		BTCHttpManager.init(act);
		activity = act;
		res = act.getResources();
		if (cityList == null) {
			cityList = new ArrayList<Map<String, String>>();
		}
		if (fidgetUpdataList == null) {
			fidgetUpdataList = new ArrayList<BTCFidget>();
		}
		if (fidgetList == null) {
			fidgetList = new ArrayList<BTCFidget>();
		}
		if (fidgetDownloadList == null) {
			fidgetDownloadList = new ArrayList<BTCFidget>();
		}

		// 获取程序版本信息，fidget机构
		if (app == null) {
			app = act.getSharedPreferences(BTCConstant.APP_INFO,
					Activity.MODE_PRIVATE);
		}
		APP_VERSION = app.getString(BTCConstant.VERSION,
				SystemConfig.APP_VERSION);

		if (fidgetOrc == null) {
			fidgetOrc = act.getSharedPreferences(BTCConstant.ORC_INFO,
					Activity.MODE_PRIVATE);
		}
		// 初始化数据库
		// act.deleteDatabase(Constant.FIDGET);
		if (db == null) {
			db = new BTCDBManager(act, BTCConstant.FIDGET);
		}

//		defaultCity = app.getString(BTCConstant.CITY_NAME, null);
//		defaultCityID = app.getString(BTCConstant.CITY_ID, null);
//		if (defaultCity == null || defaultCityID == null) {
//			setDefaultCityNotSeted();
//		}
		// db.execute("delete from fidget");

		// 如果是首次运行，将fidget公共脚本解压缩到指定目录，保存版本,保存md5加密值.将机构列表写入配置文件
		if (!app.contains(BTCConstant.JSVERSION)) {
			try {
				// 解压脚本
//				BTCUNZip.UnZipFolder(res.openRawResource(R.raw.js),
//						BTCConstant.FIDGET_PATH);
				app.edit()
						.putString(BTCConstant.JSVERSION,
								BTCConstant.DEFAULT_JSVERSION)
						.putString(BTCConstant.MD_CSS, BTCConstant.MD5_CSS)
						.putString(BTCConstant.MD_COMMON,
								BTCConstant.MD5_COMMON)
						.putString(BTCConstant.MD_EPAY, BTCConstant.MD5_EPAY)
						.commit();
				// 写入配置文件
				// setLocalOrcList();
			} catch (Exception e) {
				LogGloble.exceptionPrint(e);
			}
		}
		JS_VERSION = app.getString(BTCConstant.JSVERSION, null);
		// // 获取机构列表
		// readOrc();

	}

	// 应用程序版本比较，只有版本较高时才会提示用户更新。版本字符串格式为x.x.x
	public static boolean versionCompare() {
		String[] version = APP_VERSION.split("\\.");
		String[] lastversion = LAST_VERSION.split("\\.");
		int count = Math.min(version.length, lastversion.length);
		for (int i = 0; i < count; i++) {
			if (Integer.parseInt(lastversion[i]) > Integer.parseInt(version[i]))
				return true;
			else if (Integer.parseInt(lastversion[i]) < Integer
					.parseInt(version[i]))
				return false;
		}
		return false;
	}

	/**
	 * 读取城市列表
	 */
	public static void readOrc() {
		cityList.clear();
		@SuppressWarnings({ "unchecked", "rawtypes" })
		TreeMap<String, String> map = new TreeMap(
				(Map<String, String>) fidgetOrc.getAll());
		if (map.size() > 0) {
			Iterator<String> it = map.keySet().iterator();
			String s;
			Map<String, String> orc;
			while (it.hasNext()) {
				orc = new HashMap<String, String>();
				s = it.next();
				orc.put(BTCConstant.NAME, map.get(s));
				orc.put(BTCConstant.VALUE, s);
				LogGloble.d("info", "map.get(s)==  " + map.get(s) + "  s==  "
						+ s);
				cityList.add(orc);
			}
		}
	}

	// 将资源里的机构列表拷贝到getSharedPreferences
	public static void setLocalOrcList() {
		Element root = getRoot(res.openRawResource(R.raw.city));
		Element e;
		for (int i = 0; i < root.getChildCount(); i++) {
			e = root.getElement(i);
			if (e == null)
				continue;
			if (e.getName().equals(BTCConstant.ITEM)) {
				fidgetOrc
						.edit()
						.putString(e.getAttributeValue(null, BTCConstant.NAME),
								e.getText(0)).commit();
			}
		}
	}

	// 是否到服务器获取更新信息,每天最多只获取一次，不管成功与否
	public static boolean appConnect() {
		String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar
				.getInstance().getTime());
		if (date.equals(app.getString(BTCConstant.CONNECT, "")))
			// modify by xxh 2012-1-9 每天更新一次修改为每次登录都更新
			return true;
		else {
			app.edit().putString(BTCConstant.CONNECT, date).commit();
			return true;
		}
	}

	// 计算保存公共脚本文件md5和版本信息
	public static void setJS(String version) throws FileNotFoundException {
//		app.edit()
//				.putString(BTCConstant.JSVERSION, version)
//				.putString(
//						BTCConstant.MD_CSS,
//						BTCMD5.getMD5(new FileInputStream(new File(
//								BTCConstant.FIDGET_PATH + BTCConstant.MD_CSS)),
//								true))
//				.putString(
//						BTCConstant.MD_COMMON,
//						BTCMD5.getMD5(new FileInputStream(
//								new File(BTCConstant.FIDGET_PATH
//										+ BTCConstant.MD_COMMON)), true))
//				.putString(
//						BTCConstant.MD_EPAY,
//						BTCMD5.getMD5(
//								new FileInputStream(new File(
//										BTCConstant.FIDGET_PATH
//												+ BTCConstant.MD_EPAY)), true))
//				.commit();

	}

	/**
	 * 链接服务区发送版本更新请求
	 * 
	 * @param act
	 */
	public static void connect(final Activity act) {
		// 连接服务器，更新版本信息
		// final Context con = context;
		mainAct = act.getApplicationContext();
		// add by xxh 2012-1-10 通讯异常标识，初始为false，通讯异常时置为false
		err = false;
		new Thread() {
			public void run() {
				Looper.prepare();
				try {
					HttpPost httppost = null;
					if (BTCGlobal.DEBUGMODE.equals(BTCGlobal.DEBUGMODEPRODUCT)) {
						httppost = new HttpPost(SystemConfig.UpdateUrl);
					} else if (BTCGlobal.DEBUGMODE
							.equals(BTCGlobal.DEBUGMODESIT)) {
						httppost = new HttpPost(
								mainAct.getString(R.string.UpdateUrlSIT));
					} else if (BTCGlobal.DEBUGMODE
							.equals(BTCGlobal.DEBUGMODEUAT)) {
//						httppost = new HttpPost(BTCGlobal.UpdateUrl);
						httppost = new HttpPost(
								mainAct.getString(R.string.UpdateUrlUAT));
					} else {
						httppost = new HttpPost(BTCGlobal.OrgdetailUrl);
					}
					List<NameValuePair> nvp = new ArrayList<NameValuePair>();
					nvp.add(new BasicNameValuePair(BTCConstant.FIDGETINFO,
							getFidgetInfo()));
					httppost.setEntity(new UrlEncodedFormEntity(nvp));
					HttpResponse response = BTCHttpManager.execute(httppost,
							mainAct);
					
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						updata(getResponse(response.getEntity().getContent()),
								mainAct);
					} else {
						err = true;
					}
				} catch (Exception e) {
					LogGloble.exceptionPrint(e);
					// // 请求超时
					// new AlertDialog.Builder(act)
					// .setTitle(R.string.severityInfo)
					// .setMessage(
					// mainAct.getResources().getString(
					// R.string.net_cannot_use))
					// .setPositiveButton(R.string.confirm,
					// new DialogInterface.OnClickListener() {
					// public void onClick(
					// DialogInterface dialog,
					// int which) {
					// System.exit(0);
					// }
					// }).show();

					err = true;
				} finally {
					// modify by xxh 2012-1-9 强制更新\
					if (mainDialog != null && mainDialog.isShowing()) {
						mainDialog.dismiss();
					}
					if (!err) {
						// add by qr 2011-08-25 版本下载弹出提示框，点击下载开始下载
						// modify xxh 2012-1-13 修改更新流程
						updataFlag = appUpdata();
						if (updataFlag) {// 需要更新 弹出更新提示框
							createDownloadDiaog(act);
						} else if(act instanceof MoreMenuActivity){//wuhan
							AlertDialog.Builder builder = new AlertDialog.Builder(act);
//							builder.setTitle("提示");
							builder.setMessage(act.getString(R.string.more_dialog_noinfo));
							builder.setPositiveButton(act.getString(R.string.more_dialog_conform),null);
							builder.show();
						} else {
							// TODO 正式打开
							Intent intent = new Intent(act, MainActivity.class);
							act.startActivity(intent);
							act.finish();
						}
						// end
					} else {
						// modify xxh 2012-1-9 通讯异常，网络不可用，弹出提示。

						if(act instanceof MoreMenuActivity){
							 AlertDialog.Builder builder = new AlertDialog.Builder(act);
//								builder.setTitle("提示");
								builder.setMessage(act.getString(R.string.more_net_cannot_use));
								builder.setPositiveButton(act.getString(R.string.more_dialog_conform),null);
								builder.show();
						 }else{
							 BaseDroidApp.getInstanse().showMessageDialog(
										mainAct.getResources().getString(
												R.string.net_cannot_use),
										new OnClickListener() {

											@Override
											public void onClick(View v) {
												System.exit(0);
											}
										});
						 }
						
						// new AlertDialog.Builder(act)
						// .setTitle(R.string.severityInfo)
						// .setMessage(
						// mainAct.getResources().getString(
						// R.string.net_cannot_use))
						// .setPositiveButton(R.string.confirm,
						// new DialogInterface.OnClickListener() {
						// public void onClick(
						// DialogInterface dialog,
						// int which) {
						// System.exit(0);
						// }
						// }).show();
					}
				}
				Looper.loop();
			}
		}.start();
	}

	/**
	 * 版本下载弹出提示框，点击下载开始下载
	 * 
	 * @param act
	 */
	public static void createDownloadDiaog(final Activity act) {
		final Context con = act.getApplicationContext();
		int downloadId = R.string.download;
		@SuppressWarnings("unused")
		String cancle;
		String updataInfo;
		int cancleId;
		if (BTCFidgetManager.MustUpdate.equals("true")) {
			cancle = con.getResources().getString(R.string.logout);
			cancleId = R.string.logout;
			updataInfo = con.getResources().getString(R.string.mustupdata_info);
		} else {
			cancle = con.getResources().getString(R.string.cancle);
			cancleId = R.string.cancle;
			updataInfo = updataWord;
		}

		BaseDroidApp.getInstanse().showErrorDialog(updataInfo, cancleId,
				downloadId, new OnClickListener() {

					@Override
					public void onClick(View v) {

						switch ((Integer) v.getTag()) {
						case CustomDialog.TAG_SURE:// 确定
							// 判断sd卡是否存在
							if (CheckDownLoadFile(con)) {
								BaseDroidApp.getInstanse().dismissErrorDialog();
								String sdcardpath = Environment
										.getExternalStorageDirectory()
										.getAbsolutePath();

								for (int i = 0; i < 10; i++) {
									for (int j = 0; j < 5; j++) {
										File file = new File(sdcardpath
												+ File.separator + "download"
												+ File.separator + "bocmbci_a"
												+ "-" + i + "-" + j + ".apk");
										if (file.exists()) {
											file.delete();
										}
									}
									if (i == 0) {
										File file = new File(sdcardpath
												+ File.separator + "download"
												+ File.separator
												+ "bocmbci_a.apk");

										if (file.exists()) {
											file.delete();
										}
									} else {
										File file = new File(sdcardpath
												+ File.separator + "download"
												+ File.separator + "bocmbci_a"
												+ "-" + i + ".apk");
										if (file.exists()) {
											file.delete();
										}
									}
								}

								Intent serviceIntent = new Intent(act,
										DownLoadService.class);
								act.startService(serviceIntent);
								// Intent intent = new Intent();
								// intent.setAction(Intent.ACTION_VIEW);
								// // // 获取sd卡中静态页面的地址
								// String html = "file://" + DOWNLOADPAHT
								// + "/" + HTML_FILENAME;
								// Uri CONTENT_URI_BROWSERS = Uri
								// .parse("http://www.baidu.com/");
								// intent.setData(CONTENT_URI_BROWSERS);
								//
								// intent.addCategory(Intent.CATEGORY_BROWSABLE);
								//
								// Intent browserIntent = new Intent(
								// Intent.ACTION_VIEW);
								// PackageManager packageManager = act
								// .getPackageManager();
								// browserIntent.setData(CONTENT_URI_BROWSERS);
								// List<ResolveInfo> list = packageManager
								// .queryIntentActivities(
								// browserIntent, 0);
								// if(list==null|| list.size()==0){
								// Toast.makeText(act,
								// act.getResources().getString(R.string.installclient_please),
								// 3000).show();
								// return;
								// }
								// ResolveInfo defaultresolveInfo = null;
								// for (ResolveInfo resolveInfo : list) {
								// String activityName =
								// resolveInfo.activityInfo.name;
								// if (activityName
								// .contains("chrome")) {
								// defaultresolveInfo = resolveInfo;
								// }
								// if (activityName
								// .contains("BrowserActivity")) {
								// defaultresolveInfo = resolveInfo;
								// break;
								// }
								// }
								//
								// if (defaultresolveInfo == null) {
								// defaultresolveInfo = list.get(0);
								// }
								// browserIntent = packageManager
								// .getLaunchIntentForPackage(defaultresolveInfo.activityInfo.packageName);
								// ComponentName comp = new ComponentName(
								// defaultresolveInfo.activityInfo.packageName,
								// defaultresolveInfo.activityInfo.name);
								// if (browserIntent == null) {
								// Uri uri = Uri
								// .parse("http://pic.bankofchina.com/bocappd/mbs/bocmbci_a.apk");
								// Intent it = new Intent(Intent.ACTION_VIEW,
								// uri);
								// act.startActivity(it);
								//
								// } else {
								// browserIntent.setAction(Intent.ACTION_VIEW);
								// browserIntent
								// .addCategory(Intent.CATEGORY_BROWSABLE);
								// browserIntent.setComponent(comp);
								// CONTENT_URI_BROWSERS = Uri.parse(html);
								// browserIntent.setData(CONTENT_URI_BROWSERS);
								// act.startActivity(browserIntent);
								// }
//								act.finish();
								//wuhan P503
								if(act instanceof MoreMenuActivity){//wuhan
									BaseDroidApp.getInstanse().dismissErrorDialog();
								} else{
									act.finish();
								}

							}
							break;
						case CustomDialog.TAG_CANCLE:// 取消
							BaseDroidApp.getInstanse().dismissErrorDialog();
							if (BTCFidgetManager.MustUpdate.equals("true")) {
								System.exit(0);
							} else {
								if(act instanceof LoadingActivity){
									Intent intent = new Intent(act,
											MainActivity.class);
									act.startActivity(intent);
									act.finish();
								}
								
							}
							break;
						default:
							break;
						}
					}
				});
	}

	// end
	// add by qr 2011-08-18 检查版本sd卡下载路径是否存在，
	// 并将工程私有目录下的静态页面写入到sd卡，然后从sd卡读取静态页面传给系统浏览器下载。
	public static boolean CheckDownLoadFile(Context context) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// 文件保存目录
			AssetManager assetManager = context.getResources().getAssets();
			InputStream inStream;
			try {
				inStream = assetManager.open(HTML_FILENAME);
				String result = inputStream2String(inStream);
				try {
					BTCLocalSrcManager.sdwrite(HTML_FILENAME,
							result.getBytes(), DOWNLOADPAHT);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					LogGloble.exceptionPrint(e);
				}
				inStream.close();

				// modify by xxh 2011-08-22 版本下载中图片写入sd卡中
				inStream = assetManager.open(BG_NAME);
				try {
					BTCLocalSrcManager.sdwriteJPG(BG_NAME, inStream,
							DOWNLOADPAHT);
					// end
				} catch (Exception e) {
					// TODO Auto-generated catch block
					LogGloble.exceptionPrint(e);
				}
				inStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LogGloble.exceptionPrint(e);
			}
			return true;
		} else {
			String insert_sdcard = context.getResources().getString(
					R.string.please_insert_sdCard);
			Toast.makeText(context, insert_sdcard, Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	// end

	// add by qr 2011-08-18 转码：输入流转成字符串
	private static String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}

	// end
	public void createPd(Context context) {
		pd = CustomDialog.createProgressDialog(context, R.string.connectting,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						pd.dismiss();
						isStop = true;
					}
				});
		// pd = new AlertDialog.Builder(context).create();
		// pd.show();
		// pd.setCancelable(false);
		// LayoutInflater factory = LayoutInflater.from(context);
		// // 加载progress_dialog为对话框的布局xml
		// View view = factory.inflate(R.layout.progress_dialog_start, null);
		// pd.setContentView(view);
		// // pd.setIndeterminate(true);
		// ((Button) view.findViewById(R.id.btn))
		// .setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// pd.dismiss();
		// isStop = true;
		// }
		// });
		// WindowManager.LayoutParams lp = pd.getWindow().getAttributes();
		// lp.width = LayoutValue.SCREEN_WIDTH * 3 / 4;
		// lp.height = LayoutValue.SCREEN_WIDTH / 3;
		// pd.getWindow().setAttributes(lp);
		isStop = false;
	}

	/**
	 * 下载的通信框
	 * 
	 * @param context
	 */
	public void createDownloadPd(Context context) {
		pd = CustomDialog.createProgressDialog(context, R.string.loading,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						pd.dismiss();
						isStop = true;
					}
				});
		// pd = new AlertDialog.Builder(context).create();
		// pd.show();
		// pd.setCancelable(false);
		// LayoutInflater factory = LayoutInflater.from(context);
		// // 加载progress_dialog为对话框的布局xml
		// View view = factory.inflate(R.layout.progress_dialog_start, null);
		// pd.getWindow().setContentView(view);
		// ((TextView) view.findViewById(R.id.tv)).setText(R.string.loading);
		// ((Button) view.findViewById(R.id.btn))
		// .setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// pd.dismiss();
		// isStop = true;
		// }
		// });
		// WindowManager.LayoutParams lp = pd.getWindow().getAttributes();
		// lp.width = LayoutValue.SCREEN_WIDTH * 3 / 4;
		// lp.height = LayoutValue.SCREEN_WIDTH / 3;
		// pd.getWindow().setAttributes(lp);
		// pd.setCancelable(false);

		isStop = false;
	}

	// 手动更新fidget
	public void updataByHand(Context context) {
		// 连接服务器，更新版本信息
		createPd(context);
		final Context con = context;
		new Thread() {
			public void run() {
				Looper.prepare();
				try {
					// modify by qr 20110805 for debug mode
					HttpPost httppost = null;
					if (BTCGlobal.DEBUGMODE.equals(BTCGlobal.DEBUGMODEPRODUCT)) {
						httppost = new HttpPost(SystemConfig.UpdateUrl);
					} else if (BTCGlobal.DEBUGMODE
							.equals(BTCGlobal.DEBUGMODESIT)) {
						httppost = new HttpPost(
								con.getString(R.string.UpdateUrlSIT));
					} else if (BTCGlobal.DEBUGMODE
							.equals(BTCGlobal.DEBUGMODEUAT)) {
//						httppost = new HttpPost(BTCGlobal.UpdateUrl);
						httppost = new HttpPost(con.getString(R.string.UpdateUrlUAT));
					} else {// 增加默认的
						httppost = new HttpPost(SystemConfig.UpdateUrl);
					}
					// modify by qr 20110805 for debug mode end
					List<NameValuePair> nvp = new ArrayList<NameValuePair>();
					nvp.add(new BasicNameValuePair(BTCConstant.FIDGETINFO,
							getFidgetInfo()));
					httppost.setEntity(new UrlEncodedFormEntity(nvp));
					HttpResponse response = BTCHttpManager.execute(httppost,
							con);
					if (isStop)
						return;
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						if (isStop)
							return;
						updata(getResponse(response.getEntity().getContent()),
								con);
						pd.dismiss();
						// add by qr 2011-08-18 版本下载弹出提示框，点击下载开始下载
						if (LAST_VERSION != null && versionCompare()) {
							// modify by xxh 2011-08-25 版本下载弹出提示框，点击下载开始下载
							// BTCSystemLog.i("wrong", "updataByHand");
							// createDownloadDiaog(con);
							// end
							LAST_VERSION = null;
						} else
							new AlertDialog.Builder(con)
									.setTitle(R.string.severityInfo)
									.setMessage(
											con.getResources().getString(
													R.string.noUpdateInfo))
									.setPositiveButton(R.string.confirm, null)
									.show();
					} else {
						showAlertDownLoadError(pd, con);
					}
				} catch (Exception e) {
					LogGloble.exceptionPrint(e);
					showAlertDownLoadError(pd, con);
				}
				Looper.loop();
			}
		}.start();
	}

	// add by qr 2011-08-18 获取HtmlFileIntent
	public static Intent getHtmlFileIntent(String param) {
		Uri uri = Uri.parse(param).buildUpon()
				.encodedAuthority("com.android.htmlfileprovider")
				.scheme("content").encodedPath(param).build();
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.setDataAndType(uri, "text/html");
		return intent;
	}

	// end
	public static void showDialog(Context main) {
		mainAct = main;
		if (updata) {
			mainDialog = ProgressDialog.show(main, "", main.getResources()
					.getString(R.string.checkVersion), true, false);
			mainAct = main;
			logoEnd = true;
			// BTCSystemLog.i("wrong", "logoEnd");
		} else if (appUpdata()) {
			// modify by xxh 2011-08-25 版本下载弹出提示框，点击下载开始下载
			// BTCSystemLog.i("wrong", "showDialog");
			// createDownloadDiaog(mainAct);
			// end
		}
	}

	/**
	 * 获取可添加的程序列表
	 * 
	 * @param context
	 * @param position
	 */
	public void updataFidget(Context context, int position) {/*
		createPd(context);
		// 连接服务器，更新可添加fidget列表
		final Context con = context;
		final String cityid = cityList.get(position).get(BTCConstant.VALUE);
		fidgetDownloadList.clear();// 清理列表
		new Thread() {
			public void run() {
				Looper.prepare();
				try {
					HttpResponse response = updataDownloadList(con,
							BTCConstant.DEFAULTCITY_ID);
					if (isStop)
						return;
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						if (isStop)
							return;
						getFidgetList(getResponse(response.getEntity()
								.getContent()));
						if (isStop)
							return;
						// 二次通讯
						HttpResponse response1 = updataDownloadList(con, cityid);
						if (isStop)
							return;
						if (response1.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
							if (isStop)
								return;
							getFidgetList(getResponse(response1.getEntity()
									.getContent()));
						} else {
							System.out
									.println("responseCodeError:"
											+ response1.getStatusLine()
													.getStatusCode());
							showAlert(pd, con);
							return;
						}
						if (isStop)
							return;
						pd.dismiss();
//						Intent intent = new Intent();
//						intent.setClass(con, BTCServiceDonwloadList.class);
//						con.startActivity(intent);
					} else {
						// BTCSystemLog.i("responseCodeError:",
						// "responseCodeError:"
						// + response.getStatusLine()
						// .getStatusCode());
						showAlert(pd, con);
					}
				} catch (Exception e) {
					LogGloble.exceptionPrint(e);
					showAlert(pd, con);
				}
				Looper.loop();
			}
		}.start();
	*/}

	/**
	 * 获取可添加的程序列表 add by xby
	 * 
	 * @param context
	 * @param position
	 */
//	public void updataFidget(Context context, final String cityId) {
//		createPd(context);
//		// 连接服务器，更新可添加fidget列表
//		final Context con = context;
//		fidgetDownloadList.clear();// 清理列表
//		new Thread() {
//			public void run() {
//				Looper.prepare();
//				try {
//					// 首先发送全国的请求 add by xby
//					HttpResponse response = updataDownloadList(con,
//							BTCConstant.DEFAULTCITY_ID);
//					if (isStop)
//						return;
//					int status = response.getStatusLine().getStatusCode();
//					if (status == HttpStatus.SC_OK) {
//						if (isStop)
//							return;
//						getFidgetList(getResponse(response.getEntity()
//								.getContent()));
//						if (isStop)
//							return;
//						if (!BTCConstant.DEFAULTCITY_ID.equals(cityId)) {// 代表不是全国的请求
//
//							// 二次通讯 请求地方的可添加资源
//							HttpResponse response1 = updataDownloadList(con,
//									cityId);
//							if (isStop)
//								return;
//							if (response1.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//								if (isStop)
//									return;
//								getFidgetList(getResponse(response1.getEntity()
//										.getContent()));
//							} else {
//								System.out.println("responseCodeError:"
//										+ response1.getStatusLine()
//												.getStatusCode());
//								showAlert(pd, con);
//								return;
//							}
//							if (isStop)
//								return;
//						}
//						// TODO反应较慢
//						pd.dismiss();
////						Intent intent = new Intent();
////						intent.setClass(con, BTCServiceDonwloadList.class);
////						con.startActivity(intent);
//					} else {
//						// BTCSystemLog.i("responseCodeError:",
//						// "responseCodeError:"
//						// + response.getStatusLine()
//						// .getStatusCode());
//						showAlert(pd, con);
//					}
//				} catch (Exception e) {
//					LogGloble.exceptionPrint(e);
//					showAlert(pd, con);
//				}
//				Looper.loop();
//			}
//		}.start();
//	}

	public HttpResponse updataDownloadList(Context con, String cityid)
			throws KeyManagementException, NoSuchAlgorithmException,
			KeyStoreException, CertificateException, UnrecoverableKeyException,
			IOException, Exception {
		// HttpPost httppost = new
		// HttpPost(con.getString(R.string.OrgdownloadUrl));
		// modify by qr 20110805 for debug mode
		HttpPost httppost = null;
		if (BTCGlobal.DEBUGMODE.equals(BTCGlobal.DEBUGMODEPRODUCT)) {
			httppost = new HttpPost(BTCGlobal.OrgdownloadUrl);
		} else if (BTCGlobal.DEBUGMODE.equals(BTCGlobal.DEBUGMODESIT)) {
			httppost = new HttpPost(con.getString(R.string.OrgdownloadUrlSIT));
		} else if (BTCGlobal.DEBUGMODE.equals(BTCGlobal.DEBUGMODEUAT)) {
			httppost = new HttpPost(con.getString(R.string.OrgdownloadUrlUAT));
		} else {
			httppost = new HttpPost(BTCGlobal.OrgdetailUrl);
		}
		// modify by qr 20110805 for debug mode end
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair(BTCConstant.BANKNO, cityid));
		// 上传已经安装的机构列表
		// nvp.add(new BasicNameValuePair(BTCConstant.INSTALL,
		// getInstallFidget(cityid)));
		httppost.setEntity(new UrlEncodedFormEntity(nvp));

		return BTCHttpManager.execute(httppost, con);
	}

	// 获取fidget详情
	public void updataDetail(Context context, int id) {/*
		createPd(context);
		// 连接服务器，更新可添加fidget列表
		final Context con = context;
		fidgetID = id;
		new Thread() {
			public void run() {
				Looper.prepare();
				try {
					// modify by qr 20110805 for debug mode
					HttpPost httppost = null;
					if (BTCGlobal.DEBUGMODE.equals(BTCGlobal.DEBUGMODEPRODUCT)) {
						httppost = new HttpPost(BTCGlobal.OrgdetailUrl);
					} else if (BTCGlobal.DEBUGMODE
							.equals(BTCGlobal.DEBUGMODESIT)) {
						httppost = new HttpPost(
								con.getString(R.string.OrgdetailUrlSIT));
					} else if (BTCGlobal.DEBUGMODE
							.equals(BTCGlobal.DEBUGMODEUAT)) {
						httppost = new HttpPost(
								con.getString(R.string.OrgdetailUrlUAT));
					} else {
						httppost = new HttpPost(BTCGlobal.OrgdetailUrl);
					}
					// modify by qr 20110805 for debug mode end
					List<NameValuePair> nvp = new ArrayList<NameValuePair>();
					nvp.add(new BasicNameValuePair(BTCConstant.FIDGETNO,
							fidgetDownloadList.get(fidgetID).getID()));
					nvp.add(new BasicNameValuePair(BTCConstant.FIDGETORG,
							fidgetDownloadList.get(fidgetID).getOrc()));
					httppost.setEntity(new UrlEncodedFormEntity(nvp));
					HttpResponse response = BTCHttpManager.execute(httppost,
							con);

					if (isStop)
						return;
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						if (isStop)
							return;
						getFidgetDetail(getResponse(response.getEntity()
								.getContent()), con);
						if (isStop)
							return;
						pd.dismiss();
//						Intent intent = new Intent();
//						intent.setClass(con, BTCServiceHelpDes.class);
//						con.startActivity(intent);

					} else {
						// BTCSystemLog.i("responseCodeError:",
						// "responseCodeError:"
						// + response.getStatusLine()
						// .getStatusCode());
						showAlert(pd, con);
					}
				} catch (Exception e) {
					LogGloble.exceptionPrint(e);
					showAlert(pd, con);
				}
				Looper.loop();
			}
		}.start();
	*/}

	// uid=-4代表index被篡改,从新下载，uid=-1(fidget列表页面)，-2(fidget详情页面)代表下载并安装fidget,uid>=0代表更新fidget
	public void download(Activity context, final int uid) {/*
		createDownloadPd(context);
		// 连接服务器，更新可添加fidget列表
		final Context con = context;
		downLoadThread = new Thread() {
			public void run() {
				Looper.prepare();
				boolean md5error = false;
				try {
					BTCFidget fidget = null;
					if (uid >= 0)
						fidget = fidgetUpdataList.get(uid);
					else if (uid == -1 || uid == -2)
						fidget = fidgetDownloadList.get(fidgetID);
					else if (uid == -4)
						fidget = fidgetList.get(fidgetPos);
					if (fidget != null) {
						HttpGet httpget = new HttpGet(fidget.getUrl().trim());
						HttpResponse response = BTCHttpManager.execute(httpget,
								con);
						if (isStop)
							return;
						if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
							// 将下载的zip文件存储到指定位置
							if (isStop)
								return;
							InputStream is = response.getEntity().getContent();
							File f = new File(BTCConstant.FIDGET_PATH
									+ BTCConstant.FIDGETZIP);
							writeFile(is, f);
							if (isStop)
								return;
							// md5校验
//							if (!fidget.getMD5()
//									.equals(BTCMD5.getMD5(
//											new FileInputStream(f), false))) {
//								f.delete();
//								md5error = true;
//								throw new Exception();
//							}
							// modify xxh 2012-2-3 如果是更新，只删除文件，不删除只更新数据库，
							if (uid >= 0) {
								// 删除文件
								deleteDir(BTCConstant.FIDGET_PATH
										+ fidget.getOrc()
										+ BTCConstant.PATH_SEPRATE
										+ fidget.getID());
							} else if (uid == -4) {
								deleteFidget();
							}
							// end 解压到指定文件夹
//							BTCUNZip.UnZipFolder(
//									new FileInputStream(f),
//									BTCConstant.FIDGET_PATH + fidget.getOrc()
//											+ BTCConstant.PATH_SEPRATE
//											+ fidget.getID());
							f.delete();
							// 保存index.html文件的md5值，使用时校验
//							String md5 = BTCMD5
//									.getMD5(new FileInputStream(new File(
//											BTCConstant.FIDGET_PATH
//													+ fidget.getOrc()
//													+ BTCConstant.PATH_SEPRATE
//													+ fidget.getID()
//													+ BTCConstant.FIDGET_INDEX)),
//											true);
							// add by xxh 2012-1-5 增加三个字段，中行标识，全国标识，下标
							String nationwide;
							// add xxh 2012-1-31若是更新，owe不变，若是下载，更新下标
							String owe;
							if (uid >= 0) {
								owe = BTCFidgetManager.fidget.getOwned();
							} else {
								owe = fidget.getOwned();
								update_FullDB_index();
							}
							// end
							// modify xxh 2012-1-31 修改写数据库的是参数index和owed
							if (uid >= 0) {
								// 更新数据库
								db.execute("UPDATE " + BTCConstant.FIDGET
										+ " SET " + BTCConstant.FIDGET_URL
										+ "='" + fidget.getUrl() + "' , "
										+ BTCConstant.FIDGET_VERSION + "='"
										+ fidget.getVersion() + "' , "
										+ BTCConstant.FIDGET_MD5 + "='"
										+ fidget.getMD5() + "' , "
										+ BTCConstant.FIDGET_INDEXMD5 + "='"
//										+ md5 + "' WHERE "
										+ BTCConstant.FIDGET_ID + "='"
										+ fidget.getID() + "'");

							} else {
								// 插入数据库
								if (fidget.getOrc().equals(
										BTCConstant.DEFAULTCITY_ID)) {
									nationwide = "true";
									for (int i = 0; i < 35; i++) {
										// 更新数据库
										String org = null;
										if (i < 10) {
											org = "00" + String.valueOf(i);
										} else if (i >= 10 && i < 100) {
											org = "0" + String.valueOf(i);
										}
										Cursor cor = db.query("SELECT * from "
												+ BTCConstant.FIDGET
												+ " WHERE org = '" + org + "'",
												null);
										int s = cor.getCount();
										String index = String.valueOf(s);
										if (uid >= 0) {
											index = BTCFidgetManager.fidget
													.getIndex();
										}
										if (!cor.isClosed())
											cor.close();
										db.execute("insert into "
												+ BTCConstant.FIDGET + " ("
												+ BTCConstant.FIDGET_ID + ","
												+ BTCConstant.FIDGET_CITY + ","
												+ BTCConstant.FIDGET_NAME + ","
												+ BTCConstant.FIDGET_URL + ","
												+ BTCConstant.FIDGET_VERSION
												+ "," + BTCConstant.FIDGET_MD5
												+ ","
												+ BTCConstant.FIDGET_INDEXMD5
												+ ","
												+ BTCConstant.FIDGET_Owned
												+ ","
												+ BTCConstant.FIDGET_Nationwide
												+ ","
												+ BTCConstant.FIDGET_Index_org
												+ ") values('" + fidget.getID()
												+ "','" + org + "','"
												+ fidget.getName() + "','"
												+ fidget.getUrl() + "','"
												+ fidget.getVersion() + "','"
//												+ fidget.getMD5() + "','" + md5
												+ "','" + owe + "','"
												+ nationwide + "','" + index
												+ "')");
									}
								} else {
									nationwide = "false";
									Cursor cor = db.query(
											"SELECT * from "
													+ BTCConstant.FIDGET
													+ " WHERE org = '"
													+ fidget.getOrc() + "'",
											null);
									int s = cor.getCount();
									String index = String.valueOf(s);
									if (uid >= 0) {
										index = BTCFidgetManager.fidget
												.getIndex();
									}
									if (!cor.isClosed())
										cor.close();
									// 更新数据库
									db.execute("insert into "
											+ BTCConstant.FIDGET + " ("
											+ BTCConstant.FIDGET_ID + ","
											+ BTCConstant.FIDGET_CITY + ","
											+ BTCConstant.FIDGET_NAME + ","
											+ BTCConstant.FIDGET_URL + ","
											+ BTCConstant.FIDGET_VERSION + ","
											+ BTCConstant.FIDGET_MD5 + ","
											+ BTCConstant.FIDGET_INDEXMD5 + ","
											+ BTCConstant.FIDGET_Owned + ","
											+ BTCConstant.FIDGET_Nationwide
											+ ","
											+ BTCConstant.FIDGET_Index_org
											+ ") values('" + fidget.getID()
											+ "','" + fidget.getOrc() + "','"
											+ fidget.getName() + "','"
											+ fidget.getUrl() + "','"
											+ fidget.getVersion() + "','"
//											+ fidget.getMD5() + "','" + md5
											+ "','" + owe + "','" + nationwide
											+ "','" + index + "')");
								}
							}
							// end
							setFidgetList(defaultCityID);
							// 提示安装成功
							pd.dismiss();
							// 更新可用fidget列表
							if (uid >= 0 || uid == -4) {
								setFidgetList(defaultCityID);
								// new AlertDialog.Builder(con).setTitle(
								// R.string.severityInfo).setMessage(
								// con.getResources().getString(
								// R.string.updateInfoAccomplish))
								// .setPositiveButton(R.string.confirm,
								// null).show();
								BaseDroidApp
										.getInstanse()
										.showInfoMessageDialog(
												con.getResources()
														.getString(
																R.string.updateInfoAccomplish));

							} else {

								BaseDroidApp.getInstanse()
										.dismissMessageDialog();

								if (uid == -2) {// 详情里面的下载
//									((BTCServiceHelpDes) con).handlerDownload
//											.sendEmptyMessage(0);
									return;
								}
//								if (BaseDroidApp.getInstanse().getCurrentAct() instanceof BTCServiceDonwloadList) {// 下载页面
//									// TODO 页面即可更新
//									((BTCServiceDonwloadList) con).handlerDownload
//											.sendEmptyMessage(0);
//									return;
//								}
								// 刷新页面
								// BaseDroidApp.getInstanse().showMessageDialog(
								// con.getResources().getString(
								// R.string.loadAccomplish),
								// new OnClickListener() {
								//
								// @Override
								// public void onClick(View v) {
								// // TODO Auto-generated
								// // method stub
								// BaseDroidApp.getInstanse()
								// .dismissMessageDialog();
								// if (uid == -2) {//详情里面的下载
								// ((BTCServiceHelpDes)
								// con).handlerDownload.sendEmptyMessage(0);
								// return;
								// }
								// if (BaseDroidApp.getInstanse()
								// .getCurrentAct() instanceof
								// BTCServiceDonwloadList) {// 下载页面
								// // TODO 页面即可更新
								// ((BTCServiceDonwloadList)
								// con).handlerDownload.sendEmptyMessage(0);
								// return;
								// }
								// }
								// });
							}
						} else {
							// BTCSystemLog.i("responseCodeError:",
							// "responseCodeError:"
							// + response.getStatusLine()
							// .getStatusCode());
							showAlertDownLoadError(pd, con);
						}
					}
				} catch (Exception e) {
					LogGloble.exceptionPrint(e);
					if (md5error)
						showMd5Alert(pd, con);
					else
						showAlertDownLoadError(pd, con);
				}
				Looper.loop();
			}
		};
		downLoadThread.start();
	*/}

	/**
	 * 获取已安装的机构列表
	 * 
	 * @param org
	 * @return
	 */
	public static String getInstallFidget(String org) {
		StringBuffer info = new StringBuffer("");
		Cursor cursor = db.query("SELECT * from " + BTCConstant.FIDGET
				+ " where " + BTCConstant.FIDGET_CITY + "='" + org + "'", null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				info.append(cursor.getString(cursor
						.getColumnIndex(BTCConstant.FIDGET_ID)));
				if (!cursor.isLast())
					info.append(BTCConstant.VERTICAL_LINE);
			} while (cursor.moveToNext());
		}
		// BTCSystemLog.i("info:", info.toString());
		if (!cursor.isClosed())
			cursor.close();
		return info.toString();
	}

	// 初始通讯中获取可更新fidget列表，头字段"FidgetInfo"的value,即已安装的全部fidget信息
	public static String getFidgetInfo() {
		StringBuffer info = new StringBuffer("");
		// modify by xxh 2012-1-6 重新改造数据库后，修改sql查询语句
		// Cursor cursor = db.query("SELECT * from " + BTCConstant.FIDGET,
		// null);
		try {
			Cursor cursor1 = db.query("SELECT * from " + BTCConstant.FIDGET,
					null);
			// add xxh 2012-2-8 如果数据库字段个数不对，删除数据库
			int clumnCount = cursor1.getColumnCount();
			if (clumnCount < 11) {
				db.execute("DROP TABLE " + BTCConstant.FIDGET);
				// deleteDir(BTCConstant.FIDGET_PATH);
				db.execute("CREATE TABLE " + BTCConstant.FIDGET
						+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
						+ BTCConstant.FIDGET_ID + " TEXT,"
						+ BTCConstant.FIDGET_CITY + " TEXT,"
						+ BTCConstant.FIDGET_NAME + " TEXT,"
						+ BTCConstant.FIDGET_URL + " TEXT,"
						+ BTCConstant.FIDGET_VERSION + " TEXT,"
						+ BTCConstant.FIDGET_MD5 + " TEXT,"
						+ BTCConstant.FIDGET_INDEXMD5 + " TEXT,"
						+ BTCConstant.FIDGET_Owned + " TEXT,"
						+ BTCConstant.FIDGET_Nationwide + " TEXT,"
						+ BTCConstant.FIDGET_Index_org + " TEXT)");
				if (!cursor1.isClosed())
					cursor1.close();
				return info.toString();
			}
			if (!cursor1.isClosed())
				cursor1.close();
			Cursor cursor = db.query("SELECT * from " + BTCConstant.FIDGET
					+ " WHERE " + BTCConstant.FIDGET_CITY + "='"
					+ BTCConstant.DEFAULTCITY_ID + "'" + " OR "
					+ BTCConstant.FIDGET_Nationwide + "='false'", null);
			// end
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					info.append(cursor.getString(cursor
							.getColumnIndex(BTCConstant.FIDGET_ID)));
					info.append(BTCConstant.UNDER_LINE);
					info.append(cursor.getString(cursor
							.getColumnIndex(BTCConstant.FIDGET_CITY)));
					info.append(BTCConstant.UNDER_LINE);
					info.append(cursor.getString(cursor
							.getColumnIndex(BTCConstant.FIDGET_VERSION)));
					if (!cursor.isLast())
						info.append(BTCConstant.VERTICAL_LINE);
				} while (cursor.moveToNext());
			}
			// BTCSystemLog.i("info:", info.toString());
			if (!cursor.isClosed())
				cursor.close();
			if (db != null) {
				db.close();
			}
			return info.toString();
		} catch (Exception e) {

			LogGloble.exceptionPrint(e);
			return null;
			// TODO: handle exception
		}

	}

	// 解析fidget详情wml页面
	public static void getFidgetDetail(InputStream is, Context con) {
		Element root = getRoot(is);
		Element e;
		fidgetDetil = new String[3];
		fidgetDetil[2] = "";
		for (int i = 0; i < root.getChildCount(); i++) {
			e = root.getElement(i);
			if (e == null)
				continue;
			if (e.getName().equals(BTCConstant.TITLE)) {
				fidgetDetil[0] = e.getAttributeValue(null, BTCConstant.NAME);
				fidgetDetil[1] = e.getAttributeValue(null, BTCConstant.VER);
				setFidgetIcon(e.getAttributeValue(null, BTCConstant.SRC), con);
			} else if (e.getName().equals(BTCConstant.P)
					&& e.getText(0) != null)
				fidgetDetil[2] += e.getText(0) + BTCConstant.BR;
		}
	}

	// 设定fidget详情中的图片
	public static void setFidgetIcon(String name, Context con) {
		try {
			// 从本地assets目录中搜索图片
			InputStream is = res.getAssets().open(name);
			fidgetIcon = Drawable.createFromStream(is, "");
		} catch (Exception e) {
			File f = new File(BTCConstant.FIDGET_PATH + name);
			if (!f.exists()) {// 从网络获取图片,保存到指定位置
				// HttpGet httpget = new HttpGet(con.getString(R.string.MapUrl)
				// + name);
				// modify by qr 20110805 for debug mode
				HttpGet httpget = null;
				if (BTCGlobal.DEBUGMODE.equals(BTCGlobal.DEBUGMODEPRODUCT)) {
					httpget = new HttpGet(BTCGlobal.MapUrl + name);
				} else if (BTCGlobal.DEBUGMODE.equals(BTCGlobal.DEBUGMODESIT)) {
					httpget = new HttpGet(con.getString(R.string.MapUrlSIT)
							+ name);
				} else if (BTCGlobal.DEBUGMODE.equals(BTCGlobal.DEBUGMODEUAT)) {
					httpget = new HttpGet(con.getString(R.string.MapUrlUAT)
							+ name);
				}
				// modify by qr 20110805 for debug mode end
				HttpResponse response;
				try {
					response = BTCHttpManager.execute(httpget, con);
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						writeFile(response.getEntity().getContent(), f);
					}
					// BTCSystemLog.i("responseCodeError:",
					// "responseCodeError:"
					// + response.getStatusLine()
					// .getStatusCode());
				} catch (Exception e1) {
				}
			}
			try {
				// 从本地fidget目录中创建图片
				fidgetIcon = Drawable.createFromPath(BTCConstant.FIDGET_PATH
						+ name);
			} catch (Exception e2) {
			}
		}
	}

	public static void writeFile(InputStream is, File f) throws Exception {
		FileOutputStream os = new FileOutputStream(f);
		int len;
		byte[] buffer = new byte[8192];
		while ((len = is.read(buffer)) != -1) {
			os.write(buffer, 0, len);
			os.flush();
		}
		os.close();
	}

	// 解析可添加fidget列表wml页面
	/*public static void getFidgetList(InputStream is) throws Exception {
		Element root = getRoot(is);
		Element e;
		for (int i = 0; i < root.getChildCount(); i++) {
			e = root.getElement(i);
			if (e == null)
				continue;
			if (e.getName().equals(BTCConstant.ITEM)) {
				// modify by xxh 2012-1-5 构造函数增加一个字段，是否为中行应用标识
				fidgetDownloadList.add(new BTCFidget(e.getAttributeValue(null,
						BTCConstant.ID), e.getAttributeValue(null,
						BTCConstant.ORG), e.getAttributeValue(null,
						BTCConstant.VER), e.getAttributeValue(null,
						BTCConstant.NAME), e.getAttributeValue(null,
						BTCConstant.URL), e.getAttributeValue(null,
						BTCConstant.MD5), "", e.getAttributeValue(null,
						BTCConstant.OWNED)));
				// end
			}
		}
	}*/

	/**
	 * 解析版本更新 并且更新fidget列表 机构列表 wml页面
	 * 
	 * @param is
	 * @param context
	 * @throws Exception
	 */
	private static void updata(InputStream is, Context context)
			throws Exception {
		
//		String stringContent = inputStream2String(is);
			
		Element root = getRoot(is);
		Element e;
		Element ee;
		String name;
		String[] ver;
		for (int i = 0; i < root.getChildCount(); i++) {
			e = root.getElement(i);
			if (e == null)
				continue;
			name = e.getName();
			if (name.equals(BTCConstant.APP)) {// 版本
				LAST_VERSION = e.getAttributeValue(null, BTCConstant.VERSION);
				updataWord = e.getText(0);
				// add xxh 2012-1-9 解析相知更新标识
				MustUpdate = e.getAttributeValue(null,
						BTCConstant.FIDGET_MustUpdate);
				// end
			} else if (name.equals(BTCConstant.FIDGET_UPDATA)) {// 更新fidget公共脚本
				JS_LAST_VERSION = e.getAttributeValue(null, BTCConstant.VER);
				JS_URL = e.getAttributeValue(null, BTCConstant.URL);
			} else if (name.equals(BTCConstant.FIDGET)) {// 需要更新的fidget列表
//				fidgetUpdataList.clear();
//				for (int j = 0; j < e.getChildCount(); j++) {
//					ee = e.getElement(j);
//					if (ee == null)
//						continue;
//					if (ee.getName().equals(BTCConstant.ITEM)) {
//						ver = (ee.getAttributeValue(null, BTCConstant.VER))
//								.split("\\|");
//						// modify by xxh 2012-1-5 构造函数增加一个字段，是否为中行应用标识
//						fidgetUpdataList.add(new BTCFidget(ver[1], ver[0],
//								ver[2], "", ee.getAttributeValue(null,
//										BTCConstant.URL), ee.getAttributeValue(
//										null, BTCConstant.MD5), "", ee
//										.getAttributeValue(null,
//												BTCConstant.OWNED)));
//						// end
//					}
//				}
			} else if (name.equals(BTCConstant.CITY)) {// 保存城市 机构列表

//				fidgetOrc.edit().clear().commit();
//				for (int j = 0; j < e.getChildCount(); j++) {
//					ee = e.getElement(j);
//					if (ee == null)
//						continue;
//					if (ee.getName().equals(BTCConstant.ITEM)) {
//						// add by xby 全国的也要保存
//						// if (!ee.getAttributeValue(null, BTCConstant.VALUE)
//						// .equals(BTCConstant.DEFAULTCITY_ID)) {
//						Map<String, String> map = new HashMap<String, String>();
//						map.put(BTCConstant.NAME,
//								ee.getAttributeValue(null, BTCConstant.NAME));
//						map.put(BTCConstant.VALUE,
//								ee.getAttributeValue(null, BTCConstant.VALUE));
//						fidgetOrc
//								.edit()
//								.putString(map.get(BTCConstant.VALUE),
//										map.get(BTCConstant.NAME)).commit();
//					}
//					// }
//				}
//				// 再次读取城市机构列表
//				readOrc();
//				// S设置默认选择城市，没有设置过的话就设置全国
//				if (BTCFidgetManager.defaultCity == null) {
//					setDefaultCityNotSeted();
//				} else {
//
//					setFidgetList(defaultCityID);
//
//				}
			} else if (name.equals(BTCConstant.PARAM)) {// 登陆地址信息
				for (int j = 0; j < e.getChildCount(); j++) {
					ee = e.getElement(j);
					if (ee == null)
						continue;
					if (ee.getName().equals(BTCConstant.PARAMITEM)) {
						// 此处将客户端访问的一切地址存放在此页面中，以后只要去本地取即可，地址(value)与key的关系对应如下
						/**
						 * value key 客户端手机号登录地址：
						 * http://mbstest.boc.cn:8081/BOCWapBank
						 * /InputMobileNOLogin.do MobileNoLogin 自助注册：
						 * http://mbstest.boc.cn:8081/BOCWapBank/LOGNRegCheck.do
						 * SelfCheck 在线预约申请开户：
						 * http://mbstest.boc.cn:8081/BOCWapBank
						 * /APPLYAgreement.do Online 图片验证码：
						 * http://mbstest.boc.cn:8081/BOCWapBank/GETImage.do
						 * GetImage 获得随机数和图形验证码:
						 * http://mbstest.boc.cn:8081/BOCWapBank
						 * /LOGNGetRandomAndImage.do RandomAndImage 忘记密码：
						 * http://
						 * mbstest.boc.cn:8081/BOCWapBank/LOGNPwdForgetMod.do
						 * ForgetPassword
						 */
						app.edit()
								.putString(
										ee.getAttributeValue(null,
												BTCConstant.NAME),
										ee.getAttributeValue(null,
												BTCConstant.VALUE)).commit();

					}
				}
			}else if (name.equals(BTCConstant.ITV)) {// 客户端查询推送服务器的间隔时间，单位（分钟）。
				try{
					//客户端保存消息时间为毫秒
					String value = e.getAttributeValue(null, BTCConstant.VALUE);
					PushSetting.setPushTime(Integer.valueOf(value) * 60 * 1000);
					PushManager.getInstance(context).restartPushService();
				}catch(Exception ex){
					LogGloble.e(TAG, ex.getMessage(),ex);
				}
			}else if (name.equals(BTCConstant.SFLAG)) {//统计请求标识。
				//当标识为“1”时客户端正常向服务器上送功能使用情况，如果标识为“0”时客户端暂停向服务器上送功能使用情况。
				String value = e.getAttributeValue(null, BTCConstant.VALUE);
				if("1".equals(value)){
					BaseDroidApp.getInstanse().setSflag(true);
				}else {
					BaseDroidApp.getInstanse().setSflag(false);
				}
				
			}
		}
	}

	// 获取wml页面根元素
	public static Element getRoot(InputStream is) {
		try {
			KXmlParser parser = new KXmlParser();
			parser.setInput(is, BTCConstant.ENCODE);
			Document doc = new Document();
			doc.parse(parser);
			return doc.getRootElement();
		} catch (Exception e) {
			LogGloble.exceptionPrint(e);
			return null;
		}
	}

	// 从http的实体中获取解析wml页面的输入流
	private static InputStream getResponse(InputStream is) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is), 8192);
		// BufferedReader br = new BufferedReader(new InputStreamReader(
		// new GZIPInputStream(is)), 8192);
		String line = null;
		StringBuffer sb = new StringBuffer("");
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		// String s = sb.toString().replaceAll("&", "&amp;");
		String s = sb.toString();
		LogGloble.d("info", "s=====  " + s);
		return new ByteArrayInputStream(s.getBytes(BTCConstant.ENCODE));
	}

	// 判断应用程序是否需要更新
	public static boolean appUpdata() {
		if (LAST_VERSION != null && versionCompare()) {
			String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar
					.getInstance().getTime());
			if (date.equals(app.getString(BTCConstant.DATE, "")))
				// modify by xxh 2012-1-9 每天更新一次修改为每次登录都更新
				return true;
			else {
				app.edit().putString(BTCConstant.DATE, date).commit();
				return true;
			}
		} else
			return false;
	}

	public static void setCityId(int id) {
		cityID = id;
	}

	public static int getCityId() {
		return cityID;
	}

	// 设定首选机构
//	public static String setCity(int id) {
//		Map<String, String> map = cityList.get(id);
//		defaultCity = map.get(BTCConstant.NAME);
//		defaultCityID = map.get(BTCConstant.VALUE);
//		LogGloble.d("info", "defaultCity---- " + defaultCity
//				+ "  defaultCityID-- " + defaultCityID + " cityID-- " + cityID);
//		app.edit().putString(BTCConstant.CITY_NAME, defaultCity)
//				.putString(BTCConstant.CITY_ID, defaultCityID).commit();
//		if (cityID != 1)
//			setFidgetList(defaultCityID);
//		return defaultCity;
//	}

//	public static void setDefaultCityNotSeted() {
//		defaultCity = "全国";
//		defaultCityID = "000";
//		cityID = 0;
//		LogGloble.d("info", "defaultCityID==  " + defaultCityID);
//		if (cityID != 1)
//			setFidgetList(defaultCityID);
//	}

	/**
	 * 获取设定机构已安装的fidget列表
	 * 
	 * @param id
	 */
//	public static void setFidgetList(String id) {
//		fidgetList.clear();
//		fidgetListId.clear();
//		// addFidgetList(BTCConstant.DEFAULTCITY_ID);// 全国排在前
//		addFidgetList(id);
//	}

	// modified by qr
	// modified xxh 2012-1-30 修改下标有跳跃时的情况
//	public static void addFidgetList(String id) {
//		Cursor cursor = db.query("SELECT * from " + BTCConstant.FIDGET
//				+ " where " + BTCConstant.FIDGET_CITY + "='" + id + "'"
//				+ " ORDER BY " + BTCConstant.FIDGET_Index_org, null);
//		// TODO 525手机最后一条重复
//		ArrayList<String> temp = new ArrayList<String>();
//		if (cursor.getCount() > 0) {
//			// add xxh 2012-1-30
//			int lastNum = -1;
//			int index_cursor = 0;
//			// end
//			for (int i = 0; i < cursor.getCount(); i++) {
//				// add xxh 2012-1-30
//				int indexNum = lastNum;
//				int j = -1;
//				cursor.moveToFirst();
//				// end
//				do {
//					j++;
//					// add xxh 2012-1-30 修改下标有跳跃时的情况
//					int indexValue = Integer.valueOf(cursor.getString(cursor
//							.getColumnIndex(BTCConstant.FIDGET_Index_org)));
//					if (indexValue > lastNum) {
//						if (indexNum == lastNum || indexValue < indexNum) {
//							indexNum = indexValue;
//							index_cursor = j;
//						}
//					}
//					// end
//				} while (cursor.moveToNext());
//				lastNum = indexNum;
//				cursor.moveToPosition(index_cursor);
//				// modify by xxh 2012-1-5 构造函数增加一个字段，是否为中行应用标识
//				BTCFidget fid = new BTCFidget(cursor.getString(cursor
//						.getColumnIndex(BTCConstant.FIDGET_ID)),
//						cursor.getString(cursor
//								.getColumnIndex(BTCConstant.FIDGET_CITY)),
//						cursor.getString(cursor
//								.getColumnIndex(BTCConstant.FIDGET_VERSION)),
//						cursor.getString(cursor
//								.getColumnIndex(BTCConstant.FIDGET_NAME)),
//						cursor.getString(cursor
//								.getColumnIndex(BTCConstant.FIDGET_URL)),
//						cursor.getString(cursor
//								.getColumnIndex(BTCConstant.FIDGET_MD5)),
//						cursor.getString(cursor
//								.getColumnIndex(BTCConstant.FIDGET_INDEXMD5)),
//						cursor.getString(cursor
//								.getColumnIndex(BTCConstant.FIDGET_Owned)));
//				fid.setNationwide(cursor.getString(cursor
//						.getColumnIndex(BTCConstant.FIDGET_Nationwide)));
//				fid.setIndex(cursor.getString(cursor
//						.getColumnIndex(BTCConstant.FIDGET_Index_org)));
//				if (!temp.contains(fid.getID())) {
//					temp.add(fid.getID());
//					fidgetList.add(fid);
//					fidgetListId.add(fid.getID());
//				}
//				// end
//			}
//		}
//		if (!cursor.isClosed())
//			cursor.close();
//	}

	// 客户点击某项特色服务后的校验和更新判断
	/*public static int getUpdataID() {
		if (!MD5Right())// 公共脚本md5校验失败返回-1
			return -1;
		else if (JS_LAST_VERSION != null && !JS_LAST_VERSION.equals(JS_VERSION)) // 公共脚本需要更新返回-2
			return -2;
		BTCFidget fid;
		for (int i = 0; i < fidgetUpdataList.size(); i++) {
			fid = fidgetUpdataList.get(i);
			// add xxh 2012-1-30 如果是全国服务，修改org
			String org;
			if (fidget.getNationwide().equals("true")) {
				org = BTCConstant.DEFAULTCITY_ID;
			} else {
				org = fidget.getOrc();
			}
			// end
			if (fid.getID().equals(fidget.getID()) && fid.getOrc().equals(org)) {
				if (fid.getMD5().trim().equals("null")) {// fidget服务已停止
					return -3;
				} else if (!fid.getVersion().equals(fidget.getVersion())) {// fidget需要更新,返回fidgetUpdataList中的fidget索引(>=0)
					fid.setName(fidget.getName());
					return i;
				}
			}
		}
		// 判断本地fidget文件是否被篡改
		try {
			String md5;
//			if (fidget.getNationwide().equals("true")) {
//				md5 = BTCMD5.getMD5(new FileInputStream(new File(
//						BTCConstant.FIDGET_PATH + BTCConstant.DEFAULTCITY_ID
//								+ BTCConstant.PATH_SEPRATE + fidget.getID()
//								+ BTCConstant.FIDGET_INDEX)), true);
//			} else {
//				md5 = BTCMD5.getMD5(new FileInputStream(new File(
//						BTCConstant.FIDGET_PATH + fidget.getOrc()
//								+ BTCConstant.PATH_SEPRATE + fidget.getID()
//								+ BTCConstant.FIDGET_INDEX)), true);
//			}

//			if (!fidget.getIndexMd5().equals(md5))
//				return -4;
		} catch (Exception e) {
			return -4;
		}
		return -5;// 可以正常使用fidget
	}
*/
	// 校验fidget本地公共脚本文件md5
	/*public static boolean MD5Right() {
//		try {
//			return (app.getString(BTCConstant.MD_CSS, "").equals(
//					BTCMD5.getMD5(new FileInputStream(new File(
//							BTCConstant.FIDGET_PATH + BTCConstant.MD_CSS)),
//							true))
//					&& app.getString(BTCConstant.MD_COMMON, "").equals(
//							BTCMD5.getMD5(new FileInputStream(new File(
//									BTCConstant.FIDGET_PATH
//											+ BTCConstant.MD_COMMON)), true)) && app
//					.getString(BTCConstant.MD_EPAY, "").equals(
//							BTCMD5.getMD5(new FileInputStream(new File(
//									BTCConstant.FIDGET_PATH
//											+ BTCConstant.MD_EPAY)), true)));
//		} catch (FileNotFoundException e2) {
//			return false;
//		}
		return true;
	}
*/
	// 下载fidget公共脚本并解压缩到指定位置
/*	public void updataJS(Context context) {
		createDownloadPd(context);
		final Context con = context;
		new Thread() {
			public void run() {
				Looper.prepare();
				try {
					HttpGet httpget = new HttpGet(JS_URL.trim());
					HttpResponse response;
					response = BTCHttpManager.execute(httpget, con);
					if (isStop)
						return;
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						if (isStop)
							return;
						InputStream is = response.getEntity().getContent();
						File f = new File(BTCConstant.FIDGET_PATH
								+ BTCConstant.FIDGETZIP);
						writeFile(is, f);
						if (isStop)
							return;
//						BTCUNZip.UnZipFolder(new FileInputStream(f),
//								BTCConstant.FIDGET_PATH);
						f.delete();
						JS_VERSION = JS_LAST_VERSION;
						setJS(JS_LAST_VERSION);
						pd.dismiss();
						new AlertDialog.Builder(con)
								.setTitle(R.string.severityInfo)
								.setMessage(
										con.getResources().getString(
												R.string.updateInfoAccomplish))
								.setPositiveButton(R.string.confirm,
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
											}
										}).show();
					} else {
						// BTCSystemLog.i("responseCodeError:",
						// "responseCodeError:"
						// + response.getStatusLine()
						// .getStatusCode());
						showAlert(pd, con);
					}
				} catch (Exception e) {
					LogGloble.exceptionPrint(e);
					showAlert(pd, con);
				}
				Looper.loop();
			}
		}.start();
	}*/

	// 设定机构选择列表
	/*public static ListView getCityListView(Context context) {
		if (BTCFidgetManager.cityList != null) {
			if (BTCFidgetManager.cityList.size() == 0) {// 重新读取配置文件中的城市列表
				BTCFidgetManager.setLocalOrcList();
				BTCFidgetManager.readOrc();
			}

		}
		SimpleAdapter checkedTextViewAdapter = new SimpleAdapter(context,
				BTCFidgetManager.cityList, R.layout.list_cell_01,
				new String[] { BTCConstant.NAME },
				new int[] { R.id.ItemCellDisplay });
		ListView listView = new ListView(context);
		listView.setAdapter(checkedTextViewAdapter);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		listView.setLayoutParams(params);
		listView.setFadingEdgeLength(0);
		listView.setScrollingCacheEnabled(false);
		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setBackgroundResource(R.drawable.bg_for_listview_item_write);
		readOrc();
		if (cityList != null && cityList.size() == 0) {
			listView.setVisibility(View.GONE);
		}
		return listView;
	}
*/
	/**
	 * 获取飞聚数据
	 * 
	 * @param context
	 * @param grid
	 */
/*	public static void setFidgetGrid(Context context) {
		BTCFidget fid;
		meumList = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < fidgetList.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			// Drawable.createFromPath(Constant.FIDGET_PATH
			// +fidgetList.get(i).getID()+Constant.FIDGET_ICON
			fid = fidgetList.get(i);
			// modify by xxh 2012-1-9 如果是全国，修改路径的机构号
			if (fid.getNationwide().equals("true")) {
				map.put("ItemImage", BTCConstant.FIDGET_PATH
						+ BTCConstant.DEFAULTCITY_ID + BTCConstant.PATH_SEPRATE
						+ fid.getID() + BTCConstant.FIDGET_ICON);
			} else {
				map.put("ItemImage", BTCConstant.FIDGET_PATH + fid.getOrc()
						+ BTCConstant.PATH_SEPRATE + fid.getID()
						+ BTCConstant.FIDGET_ICON);
			}
			// end
			map.put("ItemText", fid.getName());
			// map.put("ItemText", "特色"+i);
			meumList.add(map);
		}

	}*/

	// 获取可增加fidget的名称列表，用于显示
/*	public static List<Map<String, String>> getDownloadList() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map;
		for (int i = 0; i < fidgetDownloadList.size(); i++) {
			map = new HashMap<String, String>();
			map.put(BTCConstant.NAME, fidgetDownloadList.get(i).getName());
			list.add(map);
		}
		return list;
	}
*/
	// 设定fidget详情信息，用于显示
	/*public static void setFidgetDetail(Activity act) {
		// ImageView iv = (ImageView) act.findViewById(R.id.detail_icon);
		// if (fidgetIcon != null)
		// iv.setImageDrawable(fidgetIcon);
		TextView title = (TextView) act.findViewById(R.id.detail_title);
		TextView tvversion = (TextView) act.findViewById(R.id.detail_version);
		tvversion.setText(fidgetDetil[1]);
		title.setText(fidgetDetil[0]);
		TextView des = (TextView) act.findViewById(R.id.detail_des);
		des.setText(fidgetDetil[2]);
	}*/

	// 通讯失败的提示信息
	public void showAlert(Dialog pd, Context context) {
		if (isStop)
			return;
		pd.dismiss();
		BaseDroidApp.getInstanse().showInfoMessageDialog(
				context.getResources().getString(R.string.communication_fail));
	}

	// 通讯失败的提示信息
	public void showAlertDownLoadError(Dialog pd, Context context) {
		if (isStop)
			return;
		pd.dismiss();
		BaseDroidApp.getInstanse().showInfoMessageDialog(
				context.getResources().getString(
						R.string.fidget_downloadupdata_fail));
	}

	public void showMd5Alert(Dialog pd, Context context) {
		if (isStop)
			return;
		pd.dismiss();
		BaseDroidApp.getInstanse().showInfoMessageDialog(
				context.getResources().getString(R.string.loadServiceFailure));
	}

	// add by QR 2012-01-10
/*	public static void update_DB_index() {
		LogGloble.d("info", "更新数据库");
		for (int i = 0; i < fidgetList.size(); i++) {
			db.execute("UPDATE " + BTCConstant.FIDGET + " SET "
					+ BTCConstant.FIDGET_Index_org + "='" + i + "' WHERE "
					+ BTCConstant.FIDGET_ID + "='" + fidgetList.get(i).getID()
					+ "'" + " AND " + BTCConstant.FIDGET_CITY + "='"
					+ fidgetList.get(i).getOrc() + "'");
			// LogGloble.i("qrr", "" + fidgetList.get(i).getID());
		}
	}
*/
	//

	// add by xxh 2012-01-30 更新所以机构下所以服务的下标
	/*public static void update_FullDB_index() {
		for (int i = 0; i < 35; i++) {
			// 更新数据库
			String org = null;
			if (i < 10) {
				org = "00" + String.valueOf(i);
			} else if (i >= 10 && i < 100) {
				org = "0" + String.valueOf(i);
			}
			setFidgetList(org);
			for (int j = 0; j < fidgetList.size(); j++) {
				db.execute("UPDATE " + BTCConstant.FIDGET + " SET "
						+ BTCConstant.FIDGET_Index_org + "='" + j + "' WHERE "
						+ BTCConstant.FIDGET_ID + "='"
						+ fidgetList.get(j).getID() + "'" + " AND "
						+ BTCConstant.FIDGET_CITY + "='"
						+ fidgetList.get(j).getOrc() + "'");
			}
		}
		setFidgetList(defaultCityID);
	}*/

	//

	// 当前使用的fidget
/*	public static void setSelectFidget(int id) {
		fidget = fidgetList.get(id);
		fidgetPos = id;
	}*/

	// 获取当前fidget的入口url
	/*public static String getFidgetUrl() {
		// modify by xxh 2012-1-9 如果是全国，修改路径的机构号
		if (fidget.getNationwide().equals("true")) {
			return "file:" + BTCConstant.FIDGET_PATH
					+ BTCConstant.DEFAULTCITY_ID + BTCConstant.PATH_SEPRATE
					+ fidget.getID() + BTCConstant.FIDGET_INDEX;
		} else {
			return "file:" + BTCConstant.FIDGET_PATH + fidget.getOrc()
					+ BTCConstant.PATH_SEPRATE + fidget.getID()
					+ BTCConstant.FIDGET_INDEX;
		}
		// end
	}*/

	// 卸载当前fidget并更新显示列表 add by xby
/*	public static void removeFidget(Context context) {
		deleteFidget();
//		fidgetList.remove(fidgetPos);
		setFidgetGrid(context);
	}*/

	// 删除fidget的文件和数据库记录
	/*public static void deleteFidget() {
		// 删除文件
//		deleteDir(BTCConstant.FIDGET_PATH + fidget.getOrc()
//				+ BTCConstant.PATH_SEPRATE + fidget.getID());
		// add xxh 2012-2-8 删除某特色业务在sd下创建的文件
//		deleteDir(BTCFileManager.getSD_Path());
		// 删除数据库记录
		// modify xxh 2012-2-9 删除某特色业务数据库记录,如果是非全国业务，按org + id查找
//		if (fidget.getNationwide().equals("true")) {
//			db.execute("delete from " + BTCConstant.FIDGET + " where "
//					+ BTCConstant.FIDGET_ID + "='" + fidget.getID() + "'");
//		} else {
//			db.execute("delete from " + BTCConstant.FIDGET + " where "
//					+ BTCConstant.FIDGET_ID + "='" + fidget.getID() + "' AND "
//					+ BTCConstant.FIDGET_CITY + "='" + fidget.getOrc() + "'");
//		}
		// end
	}*/

	// 删除整个目录
	public static void deleteDir(String path) {
		File dir = new File(path);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return; // 检查参数
		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete(); // 删除所有文件
			else if (file.isDirectory())
				deleteDir(file.getAbsolutePath()); // 递规的方式删除文件夹
		}
		dir.delete();// 删除目录本身
	}

	public static void stop() {
		db.close();
	}
}

class ImageAdapter extends SimpleAdapter {
	public ImageAdapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
	}

	public void setViewImage(ImageView v, String value) {
		v.setImageBitmap(BitmapFactory.decodeFile(value));
	}
}

class DragAdapter extends ArrayAdapter<HashMap<String, Object>> {

	private ArrayList<HashMap<String, Object>> array;
	private int item_layout;
	private String[] item_text;
	private int[] item_id;
	private Context con;

	public DragAdapter(Context con, ArrayList<HashMap<String, Object>> ay,
			int resource, String[] text, int[] id) {
		super(con, resource, ay);
		array = ay;
		item_layout = resource;
		item_text = text;
		item_id = id;
		this.con = con;
	}

	public ArrayList<HashMap<String, Object>> getList() {
		return array;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			@SuppressWarnings("static-access")
			LayoutInflater inflater = (LayoutInflater) con
					.getSystemService(con.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(item_layout, parent, false);
		}
		ImageView imageView = (ImageView) row.findViewById(item_id[0]);
		// imageView.setImageResource(Integer.valueOf(array.get(position).get(item_text[0]).toString()));
		// imageView.setBackgroundDrawable(Drawable.createFromPath(array.get(position).get(item_text[0]).toString()));
		imageView.setImageBitmap(BitmapFactory.decodeFile(array.get(position)
				.get(item_text[0]).toString()));
		TextView textView = (TextView) row.findViewById(item_id[1]);
		textView.setText(array.get(position).get(item_text[1]).toString());
		return (row);
	}
}
