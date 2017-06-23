package com.chinamworld.bocmbci.fidget;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.kxml2.io.KXmlParser;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.base.application.PublicDataCenter;
import com.chinamworld.bocmbci.biz.LoadingActivity;
import com.chinamworld.bocmbci.biz.MainActivity;
import com.chinamworld.bocmbci.biz.more.MoreMenuActivity;
import com.chinamworld.bocmbci.biz.push.PushManager;
import com.chinamworld.bocmbci.biz.push.PushSetting;
import com.chinamworld.bocmbci.boc.ModelBoc;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.BTCLocalSrcManager;
import com.chinamworld.bocmbci.widget.CustomDialog;

public class VersionUpdata {

	public static Dialog mainDialog;// 主菜单显示的更新提示
	static Resources res;

	static public String MustUpdate = "";
	static Activity activity;

	static SharedPreferences app;// 应用程序设置
	static String APP_VERSION;// 当前程序版本
	private static String LAST_VERSION;// 最新程序版本

	static final String VERSION = "version";

	static BTCDBManager db;// 已安装fidget数据库

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


	private static void initParams(final Activity act) {
		BTCHttpManager.init(act);
		activity = act;
		res = act.getResources();
		// 获取程序版本信息，fidget机构
		if (app == null) {
			app = act.getSharedPreferences(BTCConstant.APP_INFO,
					Activity.MODE_PRIVATE);
		}
		APP_VERSION = app.getString(VERSION, SystemConfig.APP_VERSION);
		if (db == null) {
			db = new BTCDBManager(act, BTCConstant.FIDGET);
		}

	}

	private static Context mainAct;// 主菜单activity引用

	public static boolean err; // 通讯异常标识 xxh 2012-1-13

	static final String FIDGETINFO = "FidgetInfo";
	public static boolean updataFlag; // 是否有更新标识 xxh 2012-1-13

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
					httppost = new HttpPost(SystemConfig.UpdateUrl);
//					
//					if (BTCGlobal.DEBUGMODE.equals(BTCGlobal.DEBUGMODEPRODUCT)) {
//						httppost = new HttpPost(BTCGlobal.UpdateUrl);
//					} else if (BTCGlobal.DEBUGMODE
//							.equals(BTCGlobal.DEBUGMODESIT)) {
//						httppost = new HttpPost(
//								mainAct.getString(R.string.UpdateUrlSIT));
//					} else if (BTCGlobal.DEBUGMODE
//							.equals(BTCGlobal.DEBUGMODEUAT)) {
//						httppost = new HttpPost(
//								mainAct.getString(R.string.UpdateUrlUAT));
//					} else {
//						httppost = new HttpPost(BTCGlobal.OrgdetailUrl);
//					}
					List<NameValuePair> nvp = new ArrayList<NameValuePair>();
					nvp.add(new BasicNameValuePair(FIDGETINFO, getFidgetInfo()));
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
//						updataFlag = true;
						
						if (updataFlag) {// 需要更新 弹出更新提示框
//							createDownloadDiaog(act);
							createDownloadDialog(act,updataWord,MustUpdate);
						} else if (act instanceof MoreMenuActivity) {// wuhan
							AlertDialog.Builder builder = new AlertDialog.Builder(
									act);
							// builder.setTitle("提示");
							builder.setMessage(act
									.getString(R.string.more_dialog_noinfo));
							builder.setPositiveButton(
									act.getString(R.string.more_dialog_conform),
									null);
							builder.show();
						} else {
							// TODO 正式打开
//							Intent intent = new Intent(act, MainActivity.class);
//							act.startActivity(intent);
							ModelBoc.gotoMainActivity(act);
							act.finish();
						}
						// end
					} else {
						// modify xxh 2012-1-9 通讯异常，网络不可用，弹出提示。

						if (act instanceof MoreMenuActivity) {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									act);
							// builder.setTitle("提示");
							builder.setMessage(act
									.getString(R.string.more_net_cannot_use));
							builder.setPositiveButton(
									act.getString(R.string.more_dialog_conform),
									null);
							builder.show();
						} else {
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

					}
				}
				Looper.loop();
			}
		}.start();
	}

	// 初始通讯中获取可更新fidget列表，头字段"FidgetInfo"的value,即已安装的全部fidget信息
	private static String getFidgetInfo() {
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

	static String updataWord;// 主菜单activity引用

	/**
	 * 解析版本更新 并且更新fidget列表 机构列表 wml页面
	 * 
	 * @param is
	 * @param context
	 * @throws Exception
	 */
	private static void updata(InputStream is, Context context)
			throws Exception {
		String stringContent = inputStream2String(is);
		LogGloble.v("BiiHttpEngine", "版本更新地址：" + SystemConfig.UpdateUrl);
		LogGloble.v("BiiHttpEngine", "版本更新返回数据：" + stringContent);
		is.reset();
		Element root = getRoot(is);
		Element e;
		Element ee;
		String name;
		String[] ver;
		marketInfoList.clear();
		for (int i = 0; i < root.getChildCount(); i++) {
			e = root.getElement(i);
			if (e == null)
				continue;
			name = e.getName();
			if (name.equals(BTCConstant.APP)) {// 版本
				LAST_VERSION = e.getAttributeValue(null, BTCConstant.VERSION);
				PublicDataCenter.getInstance().lastVersion = LAST_VERSION;
				updataWord = e.getText(0);
				// add xxh 2012-1-9 解析相知更新标识
				MustUpdate = e.getAttributeValue(null,
						BTCConstant.FIDGET_MustUpdate);
				// end
			}
			else if(name.equals("updateurllistt")){ // 版本升级地址
				// 遍历此标签下的子标签，获得市场数据
				Element element = null;
				for(int j = 0; j < e.getChildCount();j++){
					element = e.getElement(j);
					if(element == null)
						continue;
					if(element.getName().equals("urlinfo")){
						MarketInfo info = new MarketInfo();
						info.url = element.getAttributeValue(null, "path");
						info.marketName = element.getAttributeValue(null, "describe");
						info.id = marketIdToResourceId(element.getAttributeValue(null, "describe"));
						marketInfoList.add(info);
					}
				}
			}
			else if (name.equals(BTCConstant.ITV)) {// 客户端查询推送服务器的间隔时间，单位（分钟）。
				try {
					// 客户端保存消息时间为毫秒
					String value = e.getAttributeValue(null, BTCConstant.VALUE);
					PushSetting.setPushTime(Integer.valueOf(value) * 60 * 1000);
					PushManager.getInstance(context).restartPushService();
				} catch (Exception ex) {
					LogGloble.e("VersionUpdata", ex.getMessage(), ex);
				}
			} else if (name.equals(BTCConstant.SFLAG)) {// 统计请求标识。
				// 当标识为“1”时客户端正常向服务器上送功能使用情况，如果标识为“0”时客户端暂停向服务器上送功能使用情况。
				String value = e.getAttributeValue(null, BTCConstant.VALUE);
				if ("1".equals(value)) {
					BaseDroidApp.getInstanse().setSflag(true);
				} else {
					BaseDroidApp.getInstanse().setSflag(false);
				}

			}
		}
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

	private static void initMarketList() {
		MarketInfo m = new MarketInfo();
		m.marketName = "中行官网";
		m.id = R.drawable.zhongguoyinhang;
		marketInfoList.add(m);
		m = new MarketInfo();
		m.marketName = "360助手";
		m.id = R.drawable.shoujizhushou;
		m.url = "http://zhushou.360.cn/detail/index/soft_id/35383";
		marketInfoList.add(m);
		m = new MarketInfo();
		m.marketName = "安智市场";
		m.id = R.drawable.anzhishicang;
		m.url = "http://m.anzhi.com/info_2406953.html";
		marketInfoList.add(m);
		m = new MarketInfo();
		m.marketName = "安卓市场";
		m.url = "http://m.apk.hiapk.com/detail/4048238";
		m.id = R.drawable.anzhuoshicang;
		marketInfoList.add(m);
		m = new MarketInfo();
		m.marketName = "百度助手";
		m.id = R.drawable.baiduzhushou;
		m.url = "http://shouji.baidu.com/soft/item?docid=8822747";
		marketInfoList.add(m);m = new MarketInfo();
		m.marketName = "乐商店";
		m.id = R.drawable.leshangdian;
		m.url = "http://3g.lenovomm.com/appdetail/com.chinamworld.bocmbci/37?17071";
		marketInfoList.add(m);m = new MarketInfo();
		m.marketName = "小米商店";
		m.id = R.drawable.xiaomishangdian;
		m.url = "http://m.app.mi.com/detail/2081";
		marketInfoList.add(m);m = new MarketInfo();
		m.marketName = "应用宝";
		m.id = R.drawable.yingyongbao;
		m.url = "http://app.qq.com/#id=detail&appid=100757597";
		marketInfoList.add(m);m = new MarketInfo();
		m.marketName = "应用汇";
		m.id = R.drawable.yingyonghui;
		m.url = "http://m.appchina.com/app/com.chinamworld.bocmbci";
		marketInfoList.add(m);
	}

	
	private static void createDownloadDiaog(final Activity act) {
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

		View v = LayoutInflater.from(act).inflate(
				R.layout.updata_version_layout, null, false);

		CustomDialog d = new CustomDialog(act, v);
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

								// wuhan P503
								if (act instanceof MoreMenuActivity) {// wuhan
									BaseDroidApp.getInstanse()
											.dismissErrorDialog();
								} else {
									act.finish();
								}

							}
							break;
						case CustomDialog.TAG_CANCLE:// 取消
							BaseDroidApp.getInstanse().dismissErrorDialog();
							if (BTCFidgetManager.MustUpdate.equals("true")) {
								System.exit(0);
							} else {
								if (act instanceof LoadingActivity) {

//									Intent intent = new Intent(act,
//											MainActivity.class);
//									act.startActivity(intent);
									ModelBoc.gotoMainActivity(act);
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

	private static void createDownloadDialog(Activity context,String marketInfo,String mustUpData){
//		initMarketList();
		View v = createUpDataDialogView(context,marketInfo,mustUpData != null && mustUpData.toLowerCase().equals("true"));
		
		CustomDialog d = new CustomDialog(context, v);
		WindowManager.LayoutParams lp = d.getWindow().getAttributes();
		lp.width = LayoutValue.SCREEN_WIDTH * 6 / 7;
//		lp.height = LayoutValue.SCREEN_HEIGHT * 6 / 7;
		lp.gravity = Gravity.CENTER;
		d.getWindow().setAttributes(lp);
		d.setCancelable(false);
		d.show();
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

	// add by qr 2011-08-18 版本下载路径
	private static String DOWNLOADPAHT = "/sdcard/bocmbcsup";
	private static String HTML_FILENAME = "bocup_ex.html";
	private static String BG_NAME = "bg.jpg";

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

	// add by qr 2011-08-18 转码：输入流转成字符串
	private static String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}
	
	/**
	 * 市场列表
	 */
	private static List<MarketInfo> marketInfoList = new ArrayList<MarketInfo>();

	/**
	 * 通过后台返回来的ID，来对应市场图片
	 * @param id
	 * @return
	 */
	private static int marketIdToResourceId(String id){
		String ID = id.trim();
		if(ID.equals("中国银行")){
			return R.drawable.zhongguoyinhang;
		}else if (ID.equals("360助手")) {
			return R.drawable.shoujizhushou;
		}else if (ID.equals("安智市场")) {
			return R.drawable.anzhishicang;
		}else if (ID.equals("安卓市场")) {
			return R.drawable.anzhuoshicang;
		}else if (ID.equals("百度助手")) {
			return R.drawable.baiduzhushou;
		}else if (ID.equals("乐商店")) {
			return R.drawable.leshangdian;
		}else if (ID.equals("小米商店")) {
			return R.drawable.xiaomishangdian;
		}else if (ID.equals("应用宝")) {
			return R.drawable.yingyongbao;
		}else if (ID.equals("应用汇")) {
			return R.drawable.yingyonghui;
		}
		else {
			return R.drawable.qitashichang;
		}
//		int ID = Integer.parseInt(id);
//		switch(ID){
//		case 1:
//			return R.drawable.zhongguoyinhang;
//		case 2:
//			return R.drawable.shoujizhushou;
//		case 3:
//			return R.drawable.anzhishicang;
//		case 4:
//			return R.drawable.anzhuoshicang;
//		case 5:
//			return R.drawable.baiduzhushou;
//		case 6: 
//			return R.drawable.leshangdian;
//		case 7:
//			return R.drawable.xiaomishangdian;
//		case 8:
//			return R.drawable.yingyongbao;
//		case 9:
//			return R.drawable.yingyonghui;
//			
//		}
//		return R.drawable.zhongguoyinhang;
	}
	
	
	/**
	 * 创建版本更新弹出框
	 * @param isMustUpData
	 * @return
	 */
	private static View createUpDataDialogView(final Activity context,String updataMessage,boolean isMustUpData) {
	
		View v = LayoutInflater.from(context).inflate(
				R.layout.updata_version_layout, null, false);
		LinearLayout linearLayout = (LinearLayout) v
				.findViewById(R.id.marketPanel);
		
		((TextView)v.findViewById(R.id.textView)).setText(isMustUpData ? context.getResources().getString(R.string.mustupdata_info):updataMessage);
		Button bt = (Button)v.findViewById(R.id.updata_bt);
		bt.setText(isMustUpData ? "退出" :"暂不更新");
		bt.setTag(isMustUpData);
		bt.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Boolean isMustUpData = (Boolean)v.getTag();
				if(isMustUpData == true) {
					System.exit(0);
				}
				else {
					if(context instanceof LoadingActivity){
//						Intent intent = new Intent(context,
//								MainActivity.class);
//						context.startActivity(intent);
						ModelBoc.gotoMainActivity(context);
						context.finish();
					}
				}
			}
			
		});
		
		LinearLayout l = null;
		for(int i = 0 ; i < marketInfoList.size(); i++){
			if(i % 3 == 0){
				l = new LinearLayout(activity);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
				lp.setMargins(0, 10, 0, 10);
				l.setLayoutParams(lp);
				l.setOrientation(LinearLayout.HORIZONTAL);
				
				linearLayout.addView(l);

			}
			l.addView(createMarketView(marketInfoList.get(i)));
		}		
		return v;
	}

	/**
	 * 创建市场视图
	 * @param info
	 * @return
	 */
	private static View createMarketView(MarketInfo info) {
		RelativeLayout v = (RelativeLayout)LayoutInflater.from(activity).inflate(
				R.layout.grid_item_img_text_2, null, false);
		ImageView image = (ImageView) v.findViewById(R.id.grid_icon);
		image.setBackgroundResource(info.id);
		TextView tv = (TextView) v.findViewById(R.id.grid_text);
		tv.setText(info.marketName);
		v.setLayoutParams(new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT,1));
		v.setTag(info);
		v.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				MarketInfo info = (MarketInfo)v.getTag();
				if(info.id == R.drawable.zhongguoyinhang){
					// 判断sd卡是否存在
					if (CheckDownLoadFile(activity)) {
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

						Intent serviceIntent = new Intent(activity,
								DownLoadService.class);
						activity.startService(serviceIntent);

						// wuhan P503
						if (activity instanceof MoreMenuActivity) {// wuhan
							BaseDroidApp.getInstanse()
									.dismissErrorDialog();
						} else {
							activity.finish();
						}

					}
					return;
				}
				// 通过外部浏览器下载应用
				Uri url = Uri.parse(info.url.toString());
				Intent it = new Intent(Intent.ACTION_VIEW,url);
				activity.startActivity(it);
			}
		});
		return v;
	}

	
	

}
